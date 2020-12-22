//这里设置右键菜单中的监听事件
function mouseSet(aLi,d,dataJson,force,node,link,color){
	//alert("进入mouseSet");
    for (i = 0; i < aLi.length; i++){                     
         //鼠标移入  
         aLi[i].onmouseover = function (){ 
            var oThis = this;                            
            //鼠标移入样式  
            oThis.className += " active";                                  								
         };                             
         //鼠标移出    
         aLi[i].onmouseout = function (){  
             var oThis = this;   
             //鼠标移出样式  
             oThis.className = oThis.className.replace(/\s?active/,"");  
                            
        };  
    }
    //第一个按钮：以该节点为中心，展示从该节点出发到其他的节点和连线
    aLi[0].onclick=function(){
    	
    	var url= "http://localhost:8080/VisualPlatform/showNodes?method=getScsAndLinksFromInstancedId";
    	var args = {"instancedId" : d.id};
    	jQuery.post(url ,args, function(data){
    		var scs = data.scs;
    		var links = data.links;
    		for(var i = 0; i < scs.length; i++){
    	    	var tNode = scs[i];
    	    	tNode.id = scs[i].body.instance.instanceId;
    	    	//颜色数组下标
	    		tNode.color = 1;
    	    	//半径大小比例
    	    	tNode.r = d.r - 0.05;
    	    	tNode.visible = true;
    	    	tNode.name = scs[i].type;
    	    	
    	    	var nodeIds = getNodeIdArray(dataJson.nodes);
    	    	if(nodeIds.indexOf(tNode.id) == -1){
    	    		dataJson.nodes.push(tNode);
    	    	}
    	    }
    		for(var i = 0; i < links.length; i++){
    	    	var tLink = links[i];
    	    	tLink.id = tLink.fromId + "_" + tLink.toId;
    	    	tLink.source = getNodeByFromId(dataJson.nodes, tLink.fromId);
    	    	tLink.target = getNodeByToId(dataJson.nodes, tLink.toId);;
    	    	tLink.visible = true;
    	    	//连线的宽度
    	    	tLink.width = 1;
    	    	
    	    	if(dataJson.links.indexOf(tLink) == -1){
    	    		dataJson.links.push(tLink);
    	    	}
    	    }
    		updateSVG(force,dataJson,node,link,color);
    		
    	},"JSON");
      
    }
    aLi[1].onclick=function(){
    	//隐藏节点
    	d.visible = false;
    	for(var i = 0; i < dataJson.links.length; i++){
    		var link = dataJson.links[i];
    		if(link.source == d || link.target == d){
    			link.visible = false;
    		}
    	}
    } 
    aLi[2].onclick=function(){
    	//显示节点
    	d.visible = true;
    }
    aLi[3].onclick=function(){
    	//添加标注
    	openWindow();
    	jQuery('#addAnnotation').click(function(){
    		var nodeName = jQuery("input#nodeName").val();
    		var nodeType = jQuery("input#nodeType").val();
    		var nodeDesc = jQuery("input#nodeDesc").val();
    		var linkName = jQuery("input#linkName").val();
    		var linkDesc = jQuery("input#linkDesc").val();
    		
    		var tNode = {};
    		tNode.id = "$" + nodeName;
    		//颜色数组下标
    		tNode.color = 3;
	    	//半径大小比例
    		tNode.r = 1.2;
    		tNode.visible = true;
    		tNode.name = nodeName;
    		tNode.type = nodeType;
    		tNode.desc = nodeDesc;
    		
    		var nodeIds = getNodeIdArray(dataJson.nodes);
	    	if(nodeIds.indexOf(tNode.id) == -1){
	    		dataJson.nodes.push(tNode);
	    		
	    		var tLink = {};
	    		tLink.id = d.id + "_" + tNode.id;
	    		tLink.source = d;
	    		tLink.target = tNode;;
	    		tLink.visible = true;
	    		//连线的宽度
	    		tLink.width = 1;
	    		tLink.weight = 1.0;
	    		tLink.type = "semanticLink";
	    		tLink.desc = linkDesc;
	    		tLink.name = linkName;
	    		
	    		if(dataJson.links.indexOf(tLink) == -1){
	    			dataJson.links.push(tLink);
	    		}
	    	}
	    	
	    	updateSVG(force,dataJson,node,link,color);
	    	return false;
    	});
    }
    aLi[4].onclick=function(){
    	if(d.isAggregation == true){
    		alert("不能再聚合了！");
    		return;
    	}
    	//聚合操作
    	//1、查出该节点所有的子节点
    	var slaveNodes = getSlaveNodes(dataJson.links, d);
    	//2、隐藏所有连接到该子节点的连线
    	if(slaveNodes.length > 0){
    		//3、创建一个新节点
    		var aggregationNode = {};
        	aggregationNode.id = "aggregationNode" + d.id;
        	//颜色数组下标
        	aggregationNode.color = 4;
        	//半径大小比例
        	aggregationNode.r = 2;
        	aggregationNode.visible = true;
        	aggregationNode.name = "aggregationNode";
        	aggregationNode.type = "aggregationNode";
        	aggregationNode.isAggregation = true;
    		for(var i = 0; i < slaveNodes.length; i++){
    			var slaveNode = slaveNodes[i];
    			aggregationNode.name = aggregationNode.name + slaveNodes[i].id + "_";
    			hideLinksBySlaveNode(dataJson.links, slaveNode);
    			
    			//4、原来连到各子节点的连线都连到这个新节点上
            	dataJson = createAggregationLinks(dataJson, aggregationNode, slaveNode);
    		} 			
        	var nodeIds = getNodeIdArray(dataJson.nodes);
        	if(nodeIds.indexOf(aggregationNode.id) == -1){
        		dataJson.nodes.push(aggregationNode);
        	}
        	d.isAggregation = true;
        	updateSVG(force,dataJson,node,link,color);
    	}
    }       
}
//原来连到各子节点的连线都连到这个新节点上
function createAggregationLinks(dataJson, aggregationNode, slaveNode){
	var nodes = dataJson.nodes;
	var links = dataJson.links;
	for(var i = 0; i < links.length; i++){
		if(links[i].toId == slaveNode.id){
			var aggregationLink = {};
			aggregationLink.id = links[i].fromId + "_" + aggregationNode.id;
			aggregationLink.source = links[i].source;
			aggregationLink.target = aggregationNode;;
			aggregationLink.visible = true;
    		//连线的宽度
			aggregationLink.width = 1;
			aggregationLink.weight = 1.0;
			aggregationLink.type = "semanticLink";
			aggregationLink.desc = "聚合产生的临时连线";
			aggregationLink.name = "aggregationLink";
    		
    		if(dataJson.links.indexOf(aggregationLink) == -1){
    			dataJson.links.push(aggregationLink);
    		}
		}
	}
	return dataJson;
}
//查出该节点所有的子节点（不包含聚合过的节点）
function getSlaveNodes(links, parentNode){
	var slaveNodes = [];
	for(var i = 0; i < links.length; i++){
		if(links[i].fromId == parentNode.id && links[i].target.type != "aggregationNode"){
			if(slaveNodes.indexOf(links[i]) == -1){
				slaveNodes.push(links[i].target);
			}
		}
	}
	return slaveNodes;
}
//查出所有连接到该节点的连线，并隐藏掉
function hideLinksBySlaveNode(links, slaveNode){
	slaveNode.visible = false;
	for(var i = 0; i < links.length; i++){
		if(links[i].toId == slaveNode.id){
			links[i].visible = false;
		}
	}
}
//打开添加标注窗口
function openWindow() {
  var html = "";
  html += '<form class="stdform">';
  html += '	<p>';
  html += '	<label>节点名称</label>';
  html += '    <span class="field"><input type="text" id="nodeName" class="smallinput" /></span>';
  html += '	</p>';
  html += '	<p>';
  html += '	<label>节点类型</label>';
  html += '    <span class="field"><input type="text" id="nodeType" class="smallinput" /></span>';
  html += '	</p>';
  html += '	<p>';
  html += '	<label>节点描述</label>';
  html += '    <span class="field"><input type="text" id="nodeDesc" class="smallinput" /></span>';
  html += '	</p>';
  html += '	<p>';
  html += '	<label>连接名称</label>';
  html += '    <span class="field"><input type="text" id="linkName" class="smallinput" /></span>';
  html += '	</p>';
  html += '	<p>';
  html += '	<label>连接描述</label>';
  html += '    <span class="field"><input type="text" id="linkDesc" class="smallinput" /></span>';
  html += '	</p>';
  html += '	<p class="stdformbutton">';
  html += '		<button class="submit radius2" id="addAnnotation">添加</button>';
  html += ' </p>';
  html += '</form>';
  new MyLayer({
    top:"10%",
    left:"10%",
    width:"80%",
    height:"80%",
    title:"添加标注",
    content:html
  }).openLayer();
}
/**
 * 根据dataJson的nodes数组和fromId获得node
 * @param nodes
 * @param fromId
 * @returns
 */
function getNodeByFromId(nodes, fromId){
	var node = null;
	for(var i = 0; i < nodes.length; i++){
		if(nodes[i].id == fromId){
			node = nodes[i];
		}
	}
	return node;
}
/**
 * 根据dataJson的nodes数组和toId获得node
 * @param nodes
 * @param toId
 * @returns
 */
function getNodeByToId(nodes, toId){
	var node = null;
	for(var i = 0; i < nodes.length; i++){
		if(nodes[i].id == toId){
			node = nodes[i];
		}
	}
	return node;
}
/**
 * 获取dataJson中所有的node Id集合
 * @param nodes
 * @returns {Array}
 */
function getNodeIdArray(nodes){
	var nodeIds = [];
	for(var i = 0; i < nodes.length; i++){
		if(nodeIds.indexOf(nodes[i].id) == -1){
			nodeIds.push(nodes[i].id);
		}
	}
	return nodeIds;
}
//取li中最大的宽度, 并赋给同级所有li    
function setWidth(obj){  
    maxWidth = 0;  
    for (i = 0; i < obj.children.length; i++){  
        var oLi = obj.children[i];            
        var iWidth = oLi.clientWidth - parseInt(oLi.currentStyle ? oLi.currentStyle["paddingLeft"] : getComputedStyle(oLi,null)["paddingLeft"]) * 2  
        if (iWidth > maxWidth) maxWidth = iWidth;  
    }  
    for (i = 0; i < obj.children.length; i++) obj.children[i].style.width = maxWidth + "px";  
}
//更新svg图
function updateSVG(force,dataJson,node,link,color){
	
	var width = 1000;
    var height = 800;
    
    var container = document.getElementById("nodeContainer");
    
    var oMenu = document.getElementById("rightMenu");  
    var aUl = oMenu.getElementsByTagName("ul");  
    var aLi = oMenu.getElementsByTagName("li");  
    var oShowAttributes=document.getElementById("showAttributes");

    var showTimer = hideTimer = null;  
    var i = 0;  
    var maxWidth = maxHeight = 0;  
    var aDoc = [800, document.documentElement.offsetHeight];  

    oMenu.style.display = "none";
    
    node=node.data(force.nodes());
    link=link.data(force.links());
    node.exit().remove();
    link.exit().remove();
    force.start();

    node.enter()                 
        .append("g")

    //节点圆形标记
    node.append("circle")
          .attr("class", "node")
          .attr("r",function(d){return d.r * 10;})
          .style("fill", function(d) { return color[d.color]; });


    //标记鼠标悬停的标签
    node.append("title")
        .text(function(d) { return d.name; });

    //节点上显示的id
    node.append("text")
      .attr("dy", ".3em")
      .attr("class","nodetext")
      .style("text-anchor", "middle")
      .text(function(d) { 
//    	  return d.type + "(" + d.id + ")";
    	  return d.id;
	  });

    link.append("title")
        .text(function(d) { return d.linkType; });
    
    //拖拽事件
    var drag = force.drag() 
      .on("dragstart",function(d,i){   
      })  
      .on("dragend",function(d,i){  
           d.fixed = true;    //拖拽开始后设定被拖拽对象为固定   
      })  
      .on("drag",function(d,i){ 
      }) 

    link.enter()                 
        .append("line")
        .attr("class", "link")
        .attr("stroke","#09F")
        .attr("stroke-opacity","0.4")
        .style("stroke-width",function(d){return d.width * 2;});
    
    var point={};

    //力学图每一帧  
    force.on("tick", function() {
      link.attr("x1", function(d) { 
          if(d.visible==true){
            return d.source.x;
          }else{
            return null;
          }
        })
        .attr("y1", function(d) { 
          if(d.visible==true){
            return d.source.y;
          }else{
            return null;
          }
        })
        .attr("x2", function(d) {
          if(d.visible==true){
            return d.target.x;
          }else{
            return null;
          } 
        })
        .attr("y2", function(d) {
          if(d.visible==true){
            return d.target.y;
          }else{
            return null;
          }  
        })
        .on("contextmenu",function(d){
        	
            var event = d3.event; 
            
            var coordinates = d3.mouse(container);

            showAttributes(oShowAttributes,d);

            mouseSet(aLi,d,dataJson,force,node,link,color);
            
            oMenu.style.display = "block";  
            oMenu.style.top = parseInt(coordinates[1]) + "px";  
            oMenu.style.left = parseInt(coordinates[0]) + "px";   
            setWidth(aUl[0]);  
              
            //最大显示范围
            //maxWidth = aDoc[0] - oMenu.offsetWidth;
            //maxHeight = aDoc[1] - oMenu.offsetHeight;
            //alert(aLi[0].style.width*4);
            //alert(aDoc[0]);
            //alert(oMenu.offsetWidth);
            //alert(aDoc[0] - oMenu.offsetWidth);
            myWidth = parseInt(document.getElementsByTagName("li")[1].style.width)*5;
            //alert(document.getElementsByTagName("li")[1].style.width);
            maxWidth = aDoc[0] - myWidth;
            maxHeight = aDoc[1] - oMenu.offsetHeight;  
              
            //防止菜单溢出  
            oMenu.offsetTop > maxHeight && (oMenu.style.top = maxHeight + "px");  
            oMenu.offsetLeft > maxWidth && (oMenu.style.left = maxWidth + "px");  

            return false; 

          });
        dataJson.nodes.forEach(function(d,i){
            d.x = d.x - 5/2 < 0     ? 5/2 : d.x ;
            d.x = d.x + 5/2 > width ? width - 5/2 : d.x ;
            d.y = d.y - 5/2 < 0      ? 5/2 : d.y ;
            d.y = d.y + 5/2 > height ? height - 5/2 : d.y ;
        });
        node.attr("transform", function(d){ 

            if(d.visible==true){
              return "translate("+d.x+"," + d.y + ")";
            }else{
              return null;
            }           
          })
          .call(drag)
          .on("contextmenu",function(d){
        	  
            var event = d3.event; 
            
            var coordinates = d3.mouse(container);

            showAttributes(oShowAttributes,d);

            mouseSet(aLi,d,dataJson,force,node,link,color);
            
            oMenu.style.display = "block";  
            oMenu.style.top = parseInt(coordinates[1]) + 40 + "px";  
            oMenu.style.left = parseInt(coordinates[0]) + 20 + "px";   
            setWidth(aUl[0]);  
              
            //最大显示范围
            //maxWidth = aDoc[0] - oMenu.offsetWidth;
            //maxHeight = aDoc[1] - oMenu.offsetHeight;
            //alert(aLi[0].style.width*4);
            //alert(aDoc[0]);
            //alert(oMenu.offsetWidth);
            //alert(aDoc[0] - oMenu.offsetWidth);
            myWidth = parseInt(document.getElementsByTagName("li")[1].style.width)*5;
            //alert(document.getElementsByTagName("li")[1].style.width);
            maxWidth = aDoc[0] - myWidth;
            maxHeight = aDoc[1] - oMenu.offsetHeight;  
              
            //防止菜单溢出  
            oMenu.offsetTop > maxHeight && (oMenu.style.top = maxHeight + "px");  
            oMenu.offsetLeft > maxWidth && (oMenu.style.left = maxWidth + "px");  

            return false; 

          })
          .on("dblclick",function(d,i){                     
                    d.fixed = false; 
              });
          });
      //禁止原有的右键菜单
      document.oncontextmenu = function ()
      {
         return false;
      }
      //点击隐藏菜单  
	  document.onclick = function (event)  
	  {  
	    if(!(event.target==oMenu)&&!oMenu.contains(event.target)){
	      oMenu.style.display = "none"      
	    }    
	  };  
}

function getFeatureById(d){
   	
	var url= "http://localhost:8080/VisualPlatform/showNodes?method=getFeatureNum";
	var args = {"blockId" : d.id};
	jQuery.post(url ,args, function(data){
		//孔类-hole
		if(data.hole != null && data.hole != 0){
			$("#feature_one").show();
		}else{
			$("#feature_one").hide();
		}
		//凸台-concave
		if(data.aa != null && data.aa != 0){
			$("#feature_two").show();
		}else{
			$("#feature_two").hide();
		}
		//aa替换成槽类名
		if(data.concave != null && data.concave != 0){
			$("#feature_three").show();
		}else{
			$("#feature_three").hide();
		}
		//bb替换成平面类名
		if(data.bb != null && data.bb != 0){
			$("#feature_four").show();
		}else{
			$("#feature_four").hide();
		}
		//cc替换成过度特征名
		if(data.cc != null && data.cc != 0){
			$("#feature_five").show();
		}else{
			$("#feature_five").hide();
		}
		//dd替换成复杂特征名
		if(data.compoundfeature != null && data.compoundfeature != 0){
			$("#feature_six").show();
		}else{
			$("#feature_six").hide();
		}
		
		$("#through_hole").text("0");
		$("#through_hole").text(data.throughhole);
		$("#blind_hole").text("0");
		$("#blind_hole").text(data.blindhole);
		$("#open_groove").text("0");
		$("#open_groove").text(data.groove);
		$("#ladder_hole").text("0");
		$("#ladder_hole").text(data.stairhole);
		//console.log(data);
	},"JSON");
  

}
/**
 * 这里设置特征的右键菜单中的监听事件
 * @param aLi
 * @param d
 * @param dataJson
 * @param force
 * @param node
 * @param link
 * @param color
 * @returns
 */
function featureMouseSet(aLi,d,dataJson,force,node,link,color){
	//console.log(dataJson)
	if(d.name == "block"){
		$("#rightMenu4_open").hide();
		$("#feature_desc").show();
	}
	
	if(d.name == "feature"){
		$("#rightMenu4_open").show();
		$("#feature_desc").hide();
	}
	
	getFeatureById(d);
	
    for (i = 0; i < aLi.length; i++){                     
         //鼠标移入  

         aLi[i].onmouseover = function (){ 
            var oThis = this;                            
            //鼠标移入样式  
            oThis.className += " active";                                  
         };                             
         //鼠标移出    
         aLi[i].onmouseout = function (){  
             var oThis = this;   
             //鼠标移出样式  
             oThis.className = oThis.className.replace(/\s?active/,"");  
                            
        };  
    }
    //第一个按钮：以该节点为中心，展示从该节点出发到其他的节点和连线
    aLi[0].onclick=function(){   	
    	var url= "http://localhost:8080/VisualPlatform/showNodes?method=getScsAndLinksFromInstancedId";
    	var args = {"instancedId" : d.id};
    	jQuery.post(url ,args, function(data){
    		var scs = data.scs;
    		var links = data.links;
    		for(var i = 0; i < scs.length; i++){
    	    	var tNode = scs[i];
    	    	tNode.id = scs[i].body.instance.instanceId;
    	    	//颜色数组下标
	    		tNode.color = 1;
    	    	//半径大小比例
    	    	tNode.r = d.r - 0.05;
    	    	tNode.visible = true;
    	    	tNode.name = scs[i].type;
    	    	
    	    	var nodeIds = getNodeIdArray(dataJson.nodes);
    	    	if(nodeIds.indexOf(tNode.id) == -1){
    	    		dataJson.nodes.push(tNode);
    	    	}
    	    }
    		for(var i = 0; i < links.length; i++){
    	    	var tLink = links[i];
    	    	tLink.id = tLink.fromId + "_" + tLink.toId;
    	    	tLink.source = getNodeByFromId(dataJson.nodes, tLink.fromId);
    	    	tLink.target = getNodeByToId(dataJson.nodes, tLink.toId);;
    	    	tLink.visible = true;
    	    	//连线的宽度
    	    	tLink.width = 1;
    	    	
    	    	if(dataJson.links.indexOf(tLink) == -1){
    	    		dataJson.links.push(tLink);
    	    	}
    	    }
    		updateSVG4(force,dataJson,node,link,color);
    		
    	},"JSON");
      
    }
    aLi[1].onclick=function(){
    	//隐藏节点
    	d.visible = false;
    	for(var i = 0; i < dataJson.links.length; i++){
    		var link = dataJson.links[i];
    		if(link.source == d || link.target == d){
    			link.visible = false;
    		}
    	}
    } 
    //该方法用于关联三维图，向有关方法传递特征信息
    aLi[2].onclick=function(){
		var index = 0;
		for (var k = 0; k < dataJson.nodes.length; k++) {
			if (dataJson.nodes[k].type == "advanced_face") {
				index = dataJson.nodes[k].id.replace("#", "");
				break;
			}
		}
    	if(d.type == "feature"){
    		//如果选择的是特征节点，则去数据库中取出与之相关的面的集合
    		var array = new Array();
    		var url= "http://localhost:8080/VisualPlatform/showNodes?method=getScsAndLinksFromInstancedId";
        	var args = {"instancedId" : d.id};
        	jQuery.post(url ,args, function(data){
        		var scs = data.scs;
        		for(var i = 0; i<scs.length ; i++){
        			//去掉id前面的#
					var temp = scs[i].body.instance.instanceId.replace("#", "");
					array[i] = temp - index;
        		}
        		if(!hasArray(array))//判断objects中是否有此以（array中所有面id联结成的）字符串为名称的mesh
    			{
            		alert("该节点的类型："+d.type+"   "+"你需要的面id的集合为："+array);//师妹，如果是特征的关联d.type和array是你需要的参数
        			drawface(array,shellname,"381154");//画某些面
    			}
    		else
    			alert("此节点已高亮标出");
        	},"JSON");       	
    	}
    	if(d.type == "advanced_face"){
    		var temp1 = d.id.replace("#", "");
    		faceID = temp1 - index;
    		if(!hasArray([faceID]))//判断objects中是否有此以（array中所有面id联结成的）字符串为名称的mesh
			{
    			alert("该节点的类型："+d.type+"    "+"你所需要的面的id："+faceID);//师妹，如果是面的关联，d.type和d.id是你需要的参数
        		drawface([faceID],shellname,"381154");//画某些面
			}
			else
				alert("此节点已高亮标出");
    		
    	}
    	if(d.type == "block"){
    		//console.log(d.id);

    		var url= "http://localhost:8080/VisualPlatform/showNodes?method=getBlockFace";
    		var args={"blockId":d.id};
    		jQuery.post(url,args,function(data){
    			var faces=data.split(",");
    			for(var f=0;f<faces.length;f++)
    				{
    					faces[f] = faces[f].split("#")[1];
    					faces[f]=parseInt(faces[f])-index;
    				}
    			if(!hasArray(faces))//判断objects中是否有此以（array中所有面id联结成的）字符串为名称的mesh
    			{
            		drawface(faces,shellname,"381154");//画某些面
    			}
	    		else
	    			alert("此块已高亮标出");
    			
    		},"JSON");
    	}
    }
	aLi[3].onclick = function() {
		if (d.isAggregation == true) {
			alert("不能再聚合了！");
			return;
		}
		//聚合操作
		//找到当前节点的父节点
		var parentNodes = getParentNodes(dataJson.links, d);
		//1、查出与该节点相同类型特征的兄弟节点
		var botherNodes = getBotherNodes(dataJson.links, parentNodes, d);

		if (parentNodes.length > 1) {
			alert("无意义的聚集，不允许操作");
			return;
		}
		//2、隐藏所有连接到该子节点的连线
		if (botherNodes.length > 0) {
			//3、创建一个新节点
			var aggregationNode = {};
			aggregationNode.id = "aggregationNode" + d.id;
			aggregationNode.color = 4;
			aggregationNode.r = 2;
			aggregationNode.visible = true;
			aggregationNode.name = "aggregationNode";
			aggregationNode.type = "aggregationNode";
			aggregationNode.isAggregation = true;
			for (var i = 0; i < botherNodes.length; i++) {
				var botherNode = botherNodes[i];
				aggregationNode.name = aggregationNode.name + botherNodes[i].id
						+ "_";
				hideLinksByBotherNode(dataJson.links, botherNode);

				//4、原来连到各子节点的连线都连到这个新节点上，并且保持新节点与父节点的连线
				dataJson = createTestAggregationLinks(dataJson,
						aggregationNode, botherNode);
			}
			var nodeIds = getNodeIdArray(dataJson.nodes);
			if (nodeIds.indexOf(aggregationNode.id) == -1) {
				dataJson.nodes.push(aggregationNode);
			}
			d.isAggregation = true;
			updateSVG4(force, dataJson, node, link, color);
		}
	}
	aLi[4].onclick = function() {
		var index = 0;
		for (var k = 0; k < dataJson.nodes.length; k++) {
			if (dataJson.nodes[k].type == "advanced_face") {
				index = dataJson.nodes[k].id.replace("#", "");
				break;
			}
		}
    	if(d.type == "feature"){
    		//如果选择的是特征节点，则去数据库中取出与之相关的面的集合
    		var array = new Array();
    		var url= "http://localhost:8080/VisualPlatform/showNodes?method=getScsAndLinksFromInstancedId";
        	var args = {"instancedId" : d.id};
        	jQuery.post(url ,args, function(data){
        		var scs = data.scs;
        		for(var i = 0; i<scs.length ; i++){
        			//去掉id前面的#
					var temp = scs[i].body.instance.instanceId.replace("#", "");
					array[i] = temp - index;
        		}
        		//alert("该节点的类型："+d.type+"   "+"你需要的面id的集合为："+array);
        		var str="";
        		for(var i=0;i<array.length;i++)
        			{
        				str+=array[i];
        			}
        		deleteFromObject(str);
        	},"JSON");       	
    	}
    	if(d.type == "advanced_face"){
    		var temp1 = d.id.replace("#", "");
    		faceID = temp1 - index;
    		//alert("该节点的类型："+d.type+"    "+"你所需要的面的id："+faceID);
    		deleteFromObject(faceID.toString());
    	}
    	if(d.type == "block"){
    		var url= "http://localhost:8080/VisualPlatform/showNodes?method=getBlockFace";
    		var args={"blockId":d.id};
    		jQuery.post(url,args,function(data){
    			var faces=data.split(",");
    			for(var f=0;f<faces.length;f++)
    				{
    					faces[f] = faces[f].split("#")[1];
    					faces[f]=parseInt(faces[f])-index;
    				}
    			var str="";
        		for(var i=0;i<faces.length;i++)
        			{
        				str+=faces[i];
        			}
        		deleteFromObject(str);
    			
    		},"JSON");
    	}
    
	}
}
//查出当前节点的父节点（不包含聚合过的节点）
function getParentNodes(links, testNode) {
	var parentNodes = [];
	for (var i = 0; i < links.length; i++) {
		if (links[i].toId == testNode.id
				&& links[i].target.type != "aggregationNode") {
			if (parentNodes.indexOf(links[i]) == -1) {
				parentNodes.push(links[i].fromId);
			}
		}
	}
	return parentNodes;
}

//查出与该节点相同类型特征的兄弟节点（不包含聚合过的节点）
function getBotherNodes(links, parentNodes, testNode) {
	var botherNodesSet = new Set();
	var botherNodes = [];
	for (var i = 0; i < links.length; i++) {
		if (links[i].target.feature == testNode.feature
				&& links[i].fromId == parentNodes
				&& links[i].target.type != "aggregationNode") {
			botherNodesSet.add(links[i].target);
		}
	}
	botherNodes = Array.from(botherNodesSet);
	return botherNodes;
}

//查出所有连接到该节点的连线，并隐藏掉
function hideLinksByBotherNode(links, botherNode) {
	botherNode.visible = false;
	for (var i = 0; i < links.length; i++) {
		if (links[i].toId == botherNode.id) {
			links[i].visible = false;
		}
	}
}
//原来连到各子节点的连线都连到这个新节点上
function createTestAggregationLinks(dataJson, aggregationNode, botherNode) {
	var nodes = dataJson.nodes;
	var links = dataJson.links;

	//首先将feature下各子节点的连线隐藏掉
	for (var i = 0; i < links.length; i++) {
		if (links[i].fromId == botherNode.id) {
			links[i].visible = false;
		}
	}
	//创建聚集节点与子节点adcanced_face的连线
	for (var i = 0; i < links.length; i++) {
		if (links[i].fromId == botherNode.id) {
			var aggregationLink = {};
			aggregationLink.id = links[i].fromId + "_" + aggregationNode.id;
			aggregationLink.source = links[i].target;
			aggregationLink.target = aggregationNode;
			aggregationLink.visible = true;
			//连线的宽度
			aggregationLink.width = 0.5;
			aggregationLink.weight = 1.0;
			aggregationLink.type = "semanticLink";
			aggregationLink.desc = "聚合产生的临时连线";
			aggregationLink.name = "aggregationLink";

			if (dataJson.links.indexOf(aggregationLink) == -1) {
				dataJson.links.push(aggregationLink);
			}
		}
	}
	//创建聚集节点与父节block的连线
	for (var i = 0; i < links.length; i++) {
		if (links[i].toId == botherNode.id) {
			var aggregationLink = {};
			aggregationLink.id = links[i].fromId + "_" + aggregationNode.id;
			aggregationLink.source = links[i].source;
			aggregationLink.target = aggregationNode;
			aggregationLink.visible = true;
			//连线的宽度
			aggregationLink.width = 1;
			aggregationLink.weight = 1.0;
			aggregationLink.type = "semanticLink";
			aggregationLink.desc = "聚合产生的临时连线";
			aggregationLink.name = "aggregationLink";

			if (dataJson.links.indexOf(aggregationLink) == -1) {
				dataJson.links.push(aggregationLink);
			}
		}
	}
	return dataJson;
}
function updateSVG4(force, dataJson, node, link, color) {
	var width = 1000;
	var height = 800;
	var container = document.getElementById("featureNodeContainer");

	var oMenu = document.getElementById("rightMenu4");
	var aUl = oMenu.getElementsByTagName("ul");
	var aLi = oMenu.getElementsByTagName("li");
	var oShowAttributes = document.getElementById("showAttributes4");

	var showTimer = hideTimer = null;
	var i = 0;
	var maxWidth = maxHeight = 0;
	var aDoc = [ 800, document.documentElement.offsetHeight ];

	oMenu.style.display = "none";

	node = node.data(force.nodes());
	link = link.data(force.links());
	node.exit().remove();
	link.exit().remove();
	force.start();

	node.enter().append("g")

	//节点圆形标记
	node.append("circle").attr("class", "node").attr("r", function(d) {
		return d.r * 10;
	}).style("fill", function(d) {
		return color[d.color];
	});

	//标记鼠标悬停的标签
	node.append("title")
		.text(function(d) {
			return d.name;
	});

	//节点上显示的id
	node.append("text")
		.attr("dy", ".3em")
		.attr("class", "nodetext")
		.style("text-anchor", "middle")
		.text(function(d) {
			return d.id;
	});

	link.append("title")
		.text(function(d) {
			return d.linkType;
	});

	//拖拽事件
	var drag = force.drag()
		.on("dragstart", function(d, i) {})
		.on("dragend", function(d, i) {
			d.fixed = true; //拖拽开始后设定被拖拽对象为固定   
		})
		.on("drag", function(d, i) {})

	link.enter()
		.append("line")
		.attr("class", "link")
		.attr("stroke", "#09F")
		.attr("stroke-opacity", "0.4")
		.style("stroke-width", function(d) {
			return d.width * 2;
		});

	//力学图每一帧  
	force.on(
		"tick",
		function() {
		link
			.attr("x1", function(d) {
				if (d.visible == true) {
					return d.source.x;
				} else {
					return null;
				}
			})
			.attr("y1", function(d) {
				if (d.visible == true) {
					return d.source.y;
				} else {
					return null;
				}
			})
			.attr("x2", function(d) {
				if (d.visible == true) {
					return d.target.x;
				} else {
					return null;
				}
			})
			.attr("y2", function(d) {
				if (d.visible == true) {
					return d.target.y;
				} else {
					return null;
				}
			})
			dataJson.nodes.forEach(function(d, i) {
				d.x = d.x - 5 / 2 < 0 ? 5 / 2 : d.x;
				d.x = d.x + 5 / 2 > width ? width - 5 / 2 : d.x;
				d.y = d.y - 5 / 2 < 0 ? 5 / 2 : d.y;
				d.y = d.y + 5 / 2 > height ? height - 5 / 2 : d.y;
			});
			node.attr(
				"transform",
				function(d) {
					if (d.visible == true) {
						return "translate(" + d.x + ","+ d.y + ")";
					} else {
						return null;
					}
				})
				.call(drag)
				.on(
					"contextmenu",
					function(d) {
						var event = d3.event;
						var coordinates = d3
						.mouse(container);
						showAttributes(oShowAttributes, d);
						featureMouseSet(aLi, d, dataJson, force,node, link, color);
						oMenu.style.display = "block";
						oMenu.style.top = parseInt(coordinates[1])+ 40 + "px";
						oMenu.style.left = parseInt(coordinates[0])+ 20 + "px";
						setWidth(aUl[0]);
						myWidth = parseInt(document.getElementsByTagName("li")[1].style.width) * 5;
						maxWidth = aDoc[0] - myWidth;
						maxHeight = aDoc[1]- oMenu.offsetHeight;
						//防止菜单溢出  
						oMenu.offsetTop > maxHeight&& (oMenu.style.top = maxHeight+ "px");
						oMenu.offsetLeft > maxWidth&& (oMenu.style.left = maxWidth+ "px");
						return false;
					})
				.on("dblclick", function(d, i) {
					d.fixed = false;
				});
	});
	//禁止原有的右键菜单
	document.oncontextmenu = function() {
		return false;
	}
	//点击隐藏菜单  
	document.onclick = function(event) {
		if (!(event.target == oMenu) && !oMenu.contains(event.target)) {
			oMenu.style.display = "none"
		}
	};     
}

/**
 * 这里设置三维展示右键菜单中的监听事件
 * @param aLi
 * @param d
 * @param dataJson
 * @param force
 * @param node
 * @param link
 * @param color
 * @returns
 */
function threeMouseSet(aLi,d,dataJson,force,node,link,color){
    for (i = 0; i < aLi.length; i++){                     
         //鼠标移入  

         aLi[i].onmouseover = function (){ 
            var oThis = this;                            
            //鼠标移入样式  
            oThis.className += " active";                                  
         };                             
         //鼠标移出    
         aLi[i].onmouseout = function (){  
             var oThis = this;   
             //鼠标移出样式  
             oThis.className = oThis.className.replace(/\s?active/,"");  
                            
        };  
    }
    //第一个按钮：以该节点为中心，展示从该节点出发到其他的节点和连线
    aLi[0].onclick=function(){   	
    	    alert("第一个按钮");
    	    }
    		
    aLi[1].onclick=function(){
    	//隐藏节点
    	d.visible = false;
    	for(var i = 0; i < dataJson.links.length; i++){
    		var link = dataJson.links[i];
    		if(link.source == d || link.target == d){
    			link.visible = false;
    		}
    	}
    } 
    
}