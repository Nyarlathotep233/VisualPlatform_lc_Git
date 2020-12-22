var dataJson={"nodes":[],"links":[]};
var dataJson4={"nodes":[],"links":[]};
var force;
var node;
var link;
var color;

function refresh(){
	var nodecontainer_width=$("#nodeContainer")[0].clientWidth;
	var nodecontainer_height=$("#nodeContainer")[0].clientHeight;
	updateSVGWithWH(force,dataJson,node,link,color,nodecontainer_width,nodecontainer_height);
}

/**
 * 展开基本节点图
 * @returns
 */
function showBaseNodeGraph(){
	
	dataJson = getInitialDataJson();	
	var rootNode = dataJson.nodes[0];
	var url= "/VisualPlatform/showNodes?method=showBaseSemanticCells";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		var scs = data.scs;
		console.log("scs为：");
		console.log(scs.length);
		//遍历初始语义元节点并添入dataJson中
		for(var i = 0; i < scs.length; i++){
	    	var tNode = scs[i];
	    	tNode.id = scs[i].body.instance.instanceId;
	    	//颜色数组下标
			tNode.color = 1;
	    	//半径大小比例
	    	tNode.r = 1.2;
	    	tNode.visible = true;
	    	tNode.name = scs[i].type;
	    	tNode.isAggregation = false;
	    	
	    	var nodeIds = getNodeIdArray(dataJson.nodes);
	    	if(nodeIds.indexOf(tNode.id) == -1){
	    		dataJson.nodes.push(tNode);
	    		
	    		var link = {};
	    		link.id = rootNode.id + "_" + tNode.id;
	    		link.source = rootNode;
	    		link.target = tNode;;
	    		link.visible = true;
	    		//连线的宽度
	    		link.width = 1;
	    		link.weight = 1.0;
	    		link.type = "geometryLink";
	    		link.fromId = rootNode.id;
	    		link.toId = tNode.id;
	    		
	    		if(dataJson.links.indexOf(link) == -1){
	    			dataJson.links.push(link);
	    		}
	    	}
	    }		
	},"JSON");
}

/**
 * 展开几何节点图
 * @param w
 * @param h
 * @returns
 */
function showGeometryNodeGraph(w,h){
	
	var url= "/VisualPlatform/showNodes?method=showSCToFace";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data,w,h);
	},"JSON");
}

/**
 * 展开语义节点图
 * @returns
 */
function showSemanticNodeGraph(){
	
	var url= "/VisualPlatform/showNodes?method=showSemanticNodeGraph";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data,w,h);
	},"JSON");
}

/**
 * 展开推理后的语义节点图
 * @returns
 */
function showReasonNodeGraph(){
	
	var url= "/VisualPlatform/showNodes?method=reasonNodeGraph";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data,w,h);
	},"JSON");
}

/**
 * 使用函数，快速将图数据库中的节点显示在画布中
 * @param w
 * @param h
 * @returns
 */
function quickNodeGraph(w,h){	
	var url= "/VisualPlatform/showNodes?method=reasonNodeGraph";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data,w,h);
	},"JSON");
}

/**
 * 打开模型对应图
 * @returns
 */
function moddelshow() {

    location.href = 'modelshow.jsp';  
}

/**
 * 自己创建一个名为“rootNode”的根节点，并赋给DataJson对象，做为初始值。
 * @returns
 */
function getInitialDataJson(){	
	var node = {};
	node.id = "rootNode";
	//颜色数组下标
	node.color = 0;
	//半径大小比例
	node.r = 1.5;
	node.visible = true;
	node.name = "rootNode";
	node.type = "rootNode";
	node.isAggregation = false;	
	var nodeIds = getNodeIdArray(dataJson.nodes);
	if(nodeIds.indexOf(node.id) == -1){
		dataJson.nodes.push(node);
	}
    return dataJson;
} 

/**
 * 删除字符串左右两端的空格
 * @param str
 * @returns
 */
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 打开节点图
 * @param scsAndLinks
 * @param w
 * @param h
 * @returns
 */
function openNodeGraph(scsAndLinks,w,h) {
	
	var scs = scsAndLinks.scs;
	var links = scsAndLinks.links;
    
    //遍历初始语义元节点并添入dataJson中
	for(var i = 0; i < scs.length; i++){
    	var tNode = scs[i];
    	tNode.id = scs[i].body.instance.instanceId;
    	//表明是几何节点
    	if(tNode.id.indexOf('#') > -1){
    		//颜色数组下标
    		tNode.color = 1;
    	}else if(tNode.id.indexOf('%') > -1){
    		//颜色数组下标
    		tNode.color = 2;
    	}else{
    		tNode.color = 3;
    	}
    	//半径大小比例
    	tNode.r = 1.2;
    	tNode.visible = true;
    	tNode.name = scs[i].type;
    	tNode.isAggregation = false;
    	
    	var nodeIds = getNodeIdArray(dataJson.nodes);
    	if(nodeIds.indexOf(tNode.id) == -1){
    		dataJson.nodes.push(tNode);
    	}
    }
	for(var i = 0; i < links.length; i++){
    	var tLink = links[i];
    	tLink.id = tLink.fromId + "_" + tLink.toId;
    	tLink.source = getNodeByFromId(dataJson.nodes, tLink.fromId);
    	tLink.target = getNodeByToId(dataJson.nodes, tLink.toId);
    	tLink.visible = true;
    	//连线的宽度
    	tLink.width = 1;
    	
    	if(dataJson.links.indexOf(tLink) == -1){
    		dataJson.links.push(tLink);
    	}
    }
    //存储变量的值  
	var dataJsonStr = JSON.stringify(dataJson);
	localStorage.setItem("dataJson",dataJsonStr);
	drawGraph(w,h);
}

/**
 * 设计意图推理
 * @returns
 */
function reason(){
//	showResult();
//	var resultDiv = document.getElementById("result");
//	resultDiv.style.visibility="visible";
	
	//展开所有推理后的节点图
	showReasonNodeGraph();
}

/**
 * 打开推理结果页面
 * @returns
 */
function showResult() {
  var html = "";
  html += '<div class="profile_about">';
  html += '<p>组成圆柱凸台特征的面集合有：</p>';
  html += '<p>[#546,#548],[#448,#456],[#547,#545],[#461,#465],[#462,#463]</p>';
  html += '<p>组成台阶特征的面集合有：</p>';
  html += '<p></p>';
  html += '<p></p>';
  html += '</div>';
  new MyLayer({
    top:"10%",
    left:"30%",
    width:"40%",
    height:"80%",
    title:"推理结果展示",
    content:html
  }).openLayer();
}

/**
 * 构建二维节点图
 * @param w
 * @param h
 * @returns
 */
function drawGraph(w,h){
	
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

    var width = w;
    var height = h;

    //取得20个颜色的序列
    color=['#0000FF','#EE0000','#008000','#FFFF00','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB',
        '#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB'];

    //定义画布
    var svg = d3.select("#nodeContainer").append("svg")
    			 .attr("id", "svg")
                 .attr("width", width)
                 .attr("height", height);
    
    //定义力学结构
    force = d3.layout.force()
                .charge(-240)
                .linkDistance(150)
                .size([width, height]);

    force.nodes(dataJson.nodes)
         .links(dataJson.links)
         .start();
    
    //定义节点标记
    node = svg.selectAll(".node")
          .data(dataJson.nodes)
          .enter()
          .append("g");

    //定义连线
    link = svg.selectAll(".link")
          .data(dataJson.links)
          .enter()
          .append("line")
          .attr("class", "link")
          .attr("stroke","#09F")
          .attr("stroke-opacity","0.4")
          .style("stroke-width",function(d){return d.width * 2;});

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
      .text(function(d) { return d.id; });

    link.append("title")
        .text(function(d) { return d.type; })

    //拖拽事件
    var drag = force.drag() 
      .on("dragstart",function(d,i){   
      })  
      .on("dragend",function(d,i){  
           d.fixed = true;    //拖拽开始后设定被拖拽对象为固定   
      })  
      .on("drag",function(d,i){ 
      })   

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
            myWidth = parseInt(document.getElementsByTagName("li")[1].style.width)*5;
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

/**
 * 更新svg图
 */
function updateSVGWithWH(force,dataJson,node,link,color,width,height){
	
    var container = document.getElementById("nodeContainer");
    
    var svg = document.getElementById("svg");
    svg.setAttribute("width", width);
    svg.setAttribute("height", height);
    
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
            myWidth = parseInt(document.getElementsByTagName("li")[1].style.width)*5;
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

/**
 * 打开特征视图
 * @returns
 */
function showFeatureNodeGraph(){	
	var url= "/VisualPlatform/showNodes?method=featureNodeGraph";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openFeatureGraph(data);
	},"JSON");
}
/**
 * 打开设计历史
 * @returns
 */
function showDesignHistory(stepNum){	
	var url= "/VisualPlatform/showNodes?method=getDesignHistory";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		
		var url= "/VisualPlatform/showNodes?method=getFirstId";
		var args = {"times" : new Date()};
		jQuery.post(url ,args, function(data1){
			designStr+=openDesignHistory(data,data1,stepNum);
			$("#design").html(designStr);
		},"JSON");
		
	},"JSON");
	
	
}

function showAllDesignHistory(){	
	var url= "/VisualPlatform/showNodes?method=getDesignHistory";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		
		var url= "/VisualPlatform/showNodes?method=getFirstId";
		var args = {"times" : new Date()};
		jQuery.post(url ,args, function(data1){
			openAllDesignHistory(data,data1);
		},"JSON");
		
	},"JSON");	
}

function removeAllDesignHistory(){	
	designStep=0;
	designStr='';
	$("#design").html(designStr);
	var url= "/VisualPlatform/showNodes?method=getDesignHistory";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		
		var url= "/VisualPlatform/showNodes?method=getFirstId";
		var args = {"times" : new Date()};
		jQuery.post(url ,args, function(data1){
			deleteAllDesignHistory(data,data1);
		},"JSON");
		
	},"JSON");	
}

function openAllDesignHistory(designStep,firstId){
	var relationFaces = designStep.relationFaces;
	var operation = designStep.operation;
	var faces = "";
	var str = "";
//	str+="<ul>";
	$("#design").html(str);
	var meshName=new Array();
	for(var i=0;i<operation.length;i++){
		var faceArray=new Array();
		for(var j=0;j<relationFaces[i].length;j++){
			var faceId=relationFaces[i][j].split("#")[1];
			faces+=relationFaces[i][j]+" ";
			faceArray.push(faceId-firstId);
		}	
		meshName[i]="design";
		for(var x=0;x<faceArray.length;x++)
			meshName[i]+=faceArray[x];				
	}
	console.log(meshName)
	for(var y=0;y<meshName.length;y++)
		{
		var obj = scene.getObjectByName(meshName[y]);
		scene.remove(obj);
		}
	for(var i=0;i<operation.length;i++){
		var faceArray=new Array();
		for(var j=0;j<relationFaces[i].length;j++){
			var faceId=relationFaces[i][j].split("#")[1];
			faces+=relationFaces[i][j]+" ";
			faceArray.push(faceId-firstId);
		}	
		str+="<li>"+"第"+(i+1)+"步："+/*faces+"--"+*/operation[i]+"</li>";
		str+="</ul>";
		$("#design").html(str);
		faces = "[";
//		console.log(i+":"+faceArray)
		drawDesignface(faceArray,shellname,"381154");
	}
	
	
}

function openDesignHistory(designStep,firstId,stepNum){
	
	var relationFaces = designStep.relationFaces;
	var operation = designStep.operation;
	var faces = "";
	var str = "";
//	str+="<ul>";
//	$("#design").html(str);
//	for(var i=0;i<operation.length;i++){
		var faceArray=new Array();
		for(var j=0;j<relationFaces[stepNum].length;j++){
			var faceId=relationFaces[stepNum][j].split("#")[1];
			faces+=relationFaces[stepNum][j]+" ";
			faceArray.push(faceId-firstId);
		}
//		str+="<li>"+"第"+(stepNum+1)+"步："+/*faces+"--"+*/operation[stepNum]+"</li>";
		str="<li>"+"第"+(stepNum+1)+"步："+/*faces+"--"+*/operation[stepNum]+"</li>";
//		str+="</ul>";
//		$("#design").html(str);
		faces = "[";
//		console.log(i+":"+faceArray)
		drawDesignface(faceArray,shellname,"381154");
		return str;
//	}
}

function deleteAllDesignHistory(designStep,firstId){
	var relationFaces = designStep.relationFaces;
	var operation = designStep.operation;
	var faces = "";
	var str = "";
//	str+="<ul>";
	$("#design").html(str);
	var meshName=new Array();
	for(var i=0;i<operation.length;i++){
		var faceArray=new Array();
		for(var j=0;j<relationFaces[i].length;j++){
			var faceId=relationFaces[i][j].split("#")[1];
			faces+=relationFaces[i][j]+" ";
			faceArray.push(faceId-firstId);
		}	
		meshName[i]="design";
		for(var x=0;x<faceArray.length;x++)
			meshName[i]+=faceArray[x];				
	}
	console.log(meshName)
	for(var y=0;y<meshName.length;y++)
		{
		var obj = scene.getObjectByName(meshName[y]);
		scene.remove(obj);
		}	
}
/**
 * 图片展示===打开特征视图
 * @param scsAndLinks
 * @returns
 */

function openFeatureGraph(scsAndLinks) {
	
	var scs = scsAndLinks.scs;
	var links = scsAndLinks.links;
    
    //遍历初始语义元节点并添入dataJson4中
	for(var i = 0; i < scs.length; i++){
    	var tNode = scs[i];
    	tNode.id = scs[i].body.instance.instanceId;
    	tNode.feature = scs[i].tail.attribute.description;
    	
    	if(tNode.type=="part"){
    		//颜色数组下标
    		tNode.img = 0;
    		tNode.r = 2.5;
    	}else if(tNode.type=="block"){
    		//颜色数组下标
    		tNode.img = 1;
    		tNode.r = 2.5;
    	}else if(tNode.type=="feature"){
    		tNode.r = 2;
    		if(tNode.feature=="throughhole"){
    			tNode.img = 2
    		}else if(tNode.feature=="blindhole"){
    			tNode.img =3;
    		}else if(tNode.feature=="groove"){
    			tNode.img = 4;
    		}else if(tNode.feature=="stairhole"){
    			tNode.img = 5;
    		}else 
    			tNode.img = 6;
    	}else if(tNode.type=="tolerance"){
    		tNode.img = 1;
    		tNode.r = 2.5;
    	}else{
    		tNode.r = 1;
    		tNode.img = 7;
    	}
    	tNode.visible = true;
    	tNode.name = scs[i].type;
    	var nodeIds = getNodeIdArray(dataJson4.nodes);
    	if(nodeIds.indexOf(tNode.id) == -1){
    		dataJson4.nodes.push(tNode);
    	}
    }
	for(var i = 0; i < links.length; i++){
    	var tLink = links[i];
    	tLink.id = tLink.fromId + "_" + tLink.toId;
    	tLink.source = getNodeByFromId(dataJson4.nodes, tLink.fromId);
    	tLink.target = getNodeByToId(dataJson4.nodes, tLink.toId);
    	tLink.visible = true;
    	//连线的宽度
    	tLink.width = 0.5;
    	
    	if(dataJson4.links.indexOf(tLink) == -1){
    		dataJson4.links.push(tLink);
    	}
    }
    //存储变量的值  
	var dataJsonStr = JSON.stringify(dataJson4);
	localStorage.setItem("dataJson4",dataJsonStr);
	featureDrawGraph();
}

/**
 * 图片展示==绘制特征节点在指定的画布上
 * @returns
 */

function featureDrawGraph(){

	var container = document.getElementById("featureContainer");
    var oMenu4 = document.getElementById("rightMenu4");
    var aUl = oMenu4.getElementsByTagName("ul");  
    var aLi = oMenu4.getElementsByTagName("li");
    var oShowAttributes=document.getElementById("showAttributes4");

    oMenu4.style.display = "none"; //这行代码使得右键暂时不显示。   
    var width = 800;
    var height = 800;

    //取得20个颜色的序列
    var color=['#0000FF','#EE0000','#008000','#FFFF00','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB',
        '#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB'];
    var imgs=["featureicons/part.png","featureicons/block.png","featureicons/t_hole.png","featureicons/b_hole.png","featureicons/r_boss.png","featureicons/step_hole.png",
              "featureicons/feature.png","featureicons/advanced_face.png"];
    
    //定义画布
    var svg = d3.select("#featureContainer").append("svg")
                 .attr("width", width)
                 .attr("height", height);
    
    //定义力学结构
    var force = d3.layout.force()
                .charge(-240)
                .linkDistance(80)
                .size([width, height]);

    force.nodes(dataJson4.nodes)
         .links(dataJson4.links)
         .start();
    
    var img_w = 80;
    var img_h = 77;
    var radius1 = 18 ;
    var radius2 = 15 ;    //圆形半径
    
    //定义节点标记
    var node = svg.selectAll("image")
    .data(dataJson4.nodes)
    .enter()
    .append("circle")
	  .attr("class", "circleImg")
	  .attr("r", function(d){return d.r * 10;})
	  .attr("fill", function(d, i){

    //创建圆形图片
    var defs = svg.append("defs").attr("id", "imgdefs")

    var catpattern = defs.append("pattern")
                          .attr("id", "catpattern" + i)
                          .attr("height", 3)
                          .attr("width", 3.5)

    catpattern.append("image")
          .attr("x", - (img_w / 2 - radius1))
          .attr("y", - (img_h / 2 - radius2))
          .attr("width", img_w)
          .attr("height", img_h)
          .attr("xlink:href", imgs[d.img])

   return "url(#catpattern" + i + ")";
})
    //定义连线
    var link = svg.selectAll(".link")
          .data(dataJson4.links)
          .enter()
          .append("line")
          .attr("class", "link")
          .attr("stroke","#09F")
          .attr("stroke-opacity","0.4")
          .style("stroke-width",function(d){return d.width * 2;});
    
    //标记鼠标悬停的标签，显示节点的名称
    node.append("title")
        .text(function(d) { return d.name; });

    //节点上显示的id
    node.append("text")
      .attr("dy", ".3em")
      .attr("class","nodetext")
      .style("text-anchor", "middle")
      .text(function(d) { return d.id; });

    //拖拽事件
    var drag = force.drag() 
      .on("dragstart",function(d,i){   
      })  
      .on("dragend",function(d,i){  
           d.fixed = true;    //拖拽开始后设定被拖拽对象为固定   
      })  
      .on("drag",function(d,i){ 
      })   

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
        })//代码执行到这里，可以画出线，但是线和节点没有连在一起。

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
        	  featureMouseSet(aLi,d,dataJson4,force,node,link,color);           
        	  oMenu4.style.display = "block";  
        	  oMenu4.style.top = parseInt(coordinates[1]) + 40 + "px";  
        	  oMenu4.style.left = parseInt(coordinates[0]) + 20 + "px";
        	  setWidth(aUl[0]);  
              
        	  /*myWidth = parseInt(document.getElementsByTagName("li")[1].style.width)*5;
        	  maxWidth = aDoc[0] - myWidth;
        	  maxHeight = aDoc[1] - oMenu.offsetHeight;  
              
        	  //防止菜单溢出  
        	  oMenu.offsetTop > maxHeight && (oMenu.style.top = maxHeight + "px");  
        	  oMenu.offsetLeft > maxWidth && (oMenu.style.left = maxWidth + "px");  
*/
        	  return false; 

          })
          .on("dblclick",function(d,i){                     
                    d.fixed = false; 
              });
          });
      //禁止原有的右键菜单，如果注释掉下面这段代码，则会出现页面原有的右键菜单。
      document.oncontextmenu = function ()
      {
         return false;
      }
      //点击隐藏菜单  
	  document.onclick = function (event)  
	  {  
	    if(!(event.target==oMenu4)&&!oMenu4.contains(event.target)){
	      oMenu4.style.display = "none"      
	    }    
	  };  
}


//function getBlockFaces(blockId)
//{
//	var url="http://localhost:8080/VisualPlatform/showNodes?method=getBlockFace";
//	var args={"blockId":d.id};
//	jQuery.post(url,args,function(data){
//		console.log(data)
//		},"JSON");
//}

/**
 * 打开特征视图
 * @param scsAndLinks
 * @returns
 */
/*
function openFeatureGraph(scsAndLinks) {
	
	var scs = scsAndLinks.scs;
	var links = scsAndLinks.links;
    
    //遍历初始语义元节点并添入dataJson4中
	for(var i = 0; i < scs.length; i++){
    	var tNode = scs[i];
    	tNode.id = scs[i].body.instance.instanceId;
    	//表明是几何节点
    	if(tNode.type=="part"){
    		//颜色数组下标
    		tNode.color = 0;
    		tNode.r = 2.5;
    	}else if(tNode.type=="block"){
    		//颜色数组下标
    		tNode.color = 1;
    		tNode.r = 2;
    	}else if(tNode.type=="feature"){
    		tNode.color = 2;
    		tNode.r = 1.5;
    	}else {
    		tNode.r = 1;
    		tNode.color = 3;
    	}
    	//半径大小比例
    	//tNode.r = 2;
    	tNode.visible = true;
    	tNode.name = scs[i].type;
    	var nodeIds = getNodeIdArray(dataJson4.nodes);
    	if(nodeIds.indexOf(tNode.id) == -1){
    		dataJson4.nodes.push(tNode);
    	}
    }
	for(var i = 0; i < links.length; i++){
    	var tLink = links[i];
    	tLink.id = tLink.fromId + "_" + tLink.toId;
    	tLink.source = getNodeByFromId(dataJson4.nodes, tLink.fromId);
    	tLink.target = getNodeByToId(dataJson4.nodes, tLink.toId);
    	tLink.visible = true;
    	//连线的宽度
    	tLink.width = 0.5;
    	
    	if(dataJson4.links.indexOf(tLink) == -1){
    		dataJson4.links.push(tLink);
    	}
    }
    //存储变量的值  
	var dataJsonStr = JSON.stringify(dataJson4);
	localStorage.setItem("dataJson4",dataJsonStr);
	featureDrawGraph();
}
*/

/**
 * 绘制特征节点在指定的画布上
 * @returns
 */
/*
function featureDrawGraph(){

	var container = document.getElementById("featureContainer");
    var oMenu4 = document.getElementById("rightMenu4"); //捕捉testMain中用于特征的右键菜单。
    var aUl = oMenu4.getElementsByTagName("ul");  
    var aLi = oMenu4.getElementsByTagName("li");
    var oShowAttributes=document.getElementById("showAttributes4");

//这里代码还未完全真实无用，暂时不要删除。
//    var showTimer = hideTimer = null;  
//    var i = 0;  
//    var maxWidth = maxHeight = 0;  
//    var aDoc = [800, document.documentElement.offsetHeight]; 
 

    oMenu4.style.display = "none"; //这行代码使得右键暂时不显示。   
    var width = 800;
    var height = 800;

    //取得20个颜色的序列
    var color=['#0000FF','#EE0000','#008000','#FFFF00','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB',
        '#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB'];
    
    //定义画布
    var svg = d3.select("#featureContainer").append("svg")
                 .attr("width", width)
                 .attr("height", height);
    
    //定义力学结构
    var force = d3.layout.force()
                .charge(-240)
                .linkDistance(80)
                .size([width, height]);

    force.nodes(dataJson4.nodes)
         .links(dataJson4.links)
         .start();
    
    //定义节点标记
    var node = svg.selectAll(".node")
          .data(dataJson4.nodes)
          .enter()
          .append("g");

    //定义连线
    var link = svg.selectAll(".link")
          .data(dataJson4.links)
          .enter()
          .append("line")
          .attr("class", "link")
          .attr("stroke","#09F")
          .attr("stroke-opacity","0.4")
          .style("stroke-width",function(d){return d.width * 2;});
    
    //节点圆形标记，这段代码执行完成时，就在画布上画出了圆形节点。
    node.append("circle")
          .attr("class", "node")
          .attr("r",function(d){return d.r * 10;})
          .style("fill", function(d) { return color[d.color]; });

    //标记鼠标悬停的标签，显示节点的名称
    node.append("title")
        .text(function(d) { return d.name; });

    //节点上显示的id
    node.append("text")
      .attr("dy", ".3em")
      .attr("class","nodetext")
      .style("text-anchor", "middle")
      .text(function(d) { return d.id; });

    //拖拽事件
    var drag = force.drag() 
      .on("dragstart",function(d,i){   
      })  
      .on("dragend",function(d,i){  
           d.fixed = true;    //拖拽开始后设定被拖拽对象为固定   
      })  
      .on("drag",function(d,i){ 
      })   

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
        })//代码执行到这里，可以画出线，但是线和节点没有连在一起。

//这里代码还未完全真实无用，暂时不要删除。
//        dataJson4.nodes.forEach(function(d,i){
//            d.x = d.x - 5/2 < 0     ? 5/2 : d.x ;
//            d.x = d.x + 5/2 > width ? width - 5/2 : d.x ;
//            d.y = d.y - 5/2 < 0      ? 5/2 : d.y ;
//            d.y = d.y + 5/2 > height ? height - 5/2 : d.y ;
//        });

        //下面node.attr("transform", function(d)方法将节点和线连接到了一起
        node.attr("transform", function(d){ 

            if(d.visible==true){
              return "translate("+d.x+"," + d.y + ")";
            }else{
              return null;
            }           
          })
          .call(drag)//解锁这行代码+解锁上面提到的拖拽代码可是实现节点的拖拽。
          //.on("contextmenu",function(d)这个函数用于设置右键菜单
          .on("contextmenu",function(d){        	  
        	  var event = d3.event;             
        	  var coordinates = d3.mouse(container);
        	  featureMouseSet(aLi,d,dataJson4,force,node,link,color);           
        	  oMenu4.style.display = "block";  
        	  oMenu4.style.top = parseInt(coordinates[1]) + 40 + "px";  
        	  oMenu4.style.left = parseInt(coordinates[0]) + 20 + "px";
        	  setWidth(aUl[0]);  

//这里代码还未完全真实无用，暂时不要删除。              
//        	  myWidth = parseInt(document.getElementsByTagName("li")[1].style.width)*5;
//        	  maxWidth = aDoc[0] - myWidth;
//        	  maxHeight = aDoc[1] - oMenu.offsetHeight;  
//              
//        	  //防止菜单溢出  
//        	  oMenu.offsetTop > maxHeight && (oMenu.style.top = maxHeight + "px");  
//        	  oMenu.offsetLeft > maxWidth && (oMenu.style.left = maxWidth + "px");  

        	  return false; 

          })
          .on("dblclick",function(d,i){                     
                    d.fixed = false; 
              });
          });
      //禁止原有的右键菜单，如果注释掉下面这段代码，则会出现页面原有的右键菜单。
      document.oncontextmenu = function ()
      {
         return false;
      }
      //点击隐藏菜单  
	  document.onclick = function (event)  
	  {  
	    if(!(event.target==oMenu4)&&!oMenu4.contains(event.target)){
	      oMenu4.style.display = "none"      
	    }    
	  };  
}
*/
