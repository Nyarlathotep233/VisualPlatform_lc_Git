var dataJson={"nodes":[],"links":[]}; 

//展开基本节点图
function showBaseNodeGraph(){
	
	dataJson = getInitialDataJson();
	var rootNode = dataJson.nodes[0];
	
	var url= "/VisualPlatform/showNodes?method=showBaseSemanticCells";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		
		var scs = data.scs;
		//遍历初始语义元节点并添入dataJson中
		for(var i = 0; i < scs.length; i++){
	    	var tNode = scs[i];
	    	tNode.id = scs[i].body.instance.instanceId;
	    	//颜色数组下标
			tNode.color = 1;
	    	//半径大小比例
	    	tNode.r = 1.2-0.005*i;
	    	tNode.visible = true;
	    	tNode.name = scs[i].type;
	    	
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
	    		
	    		if(dataJson.links.indexOf(link) == -1){
	    			dataJson.links.push(link);
	    		}
	    	}
	    }
		
//		openNodeGraph(data);
	},"JSON");
}

//展开几何节点图
function showGeometryNodeGraph(){
	var url= "/VisualPlatform/showNodes?method=showSCToFace";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data);
	},"JSON");
}

//展开语义节点图
function showSemanticNodeGraph(){
	
	var url= "/VisualPlatform/showNodes?method=showSemanticNodeGraph";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data);
	},"JSON");
}

//展开推理后的语义节点图
function showReasonNodeGraph(){
	
	var url= "/VisualPlatform/showNodes?method=reasonNodeGraph";
	var args = {"times" : new Date()};
	jQuery.post(url ,args, function(data){
		openNodeGraph(data);
	},"JSON");
}

//打开模型对应图
function moddelshow() {

    location.href = 'modelshow.jsp';  
}

//获得初始的DataJson对象
function getInitialDataJson(){
	
	var node = {};
	node.id = "rrootNode";
	//颜色数组下标
	node.color = 0;
	//半径大小比例
	node.r = 1.5;
	node.visible = true;
	node.name = "rootNode";
	node.type = "rootNode";
	
	var nodeIds = getNodeIdArray(dataJson.nodes);
	if(nodeIds.indexOf(node.id) == -1){
		dataJson.nodes.push(node);
	}
    return dataJson;
} 
//删除字符串左右两端的空格
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
//打开节点图
function openNodeGraph(scsAndLinks) {
	
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
    	tNode.r = 1.2-0.005*i;
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
    //存储变量的值  
	var dataJsonStr = JSON.stringify(dataJson);
	localStorage.setItem("dataJson",dataJsonStr);
	drawGraph();
}
//构建二维节点图
function drawGraph(){
	
	jQuery("#nodeContainer").empty();
	
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

    var width = 500;
    var height =500;

    //取得20个颜色的序列
    // var color = d3.scale.category20();
    var color=['#0000FF','#EE0000','#008000','#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB',
        '#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB'];

    //定义画布
    var svg = d3.select("#nodeContainer").append("svg")
                 .attr("width", width)
                 .attr("height", height);
    
    //定义力学结构
    var force = d3.layout.force()
                .charge(-240)
                .linkDistance(80)
                .size([width, height]);

    force.nodes(dataJson.nodes)
         .links(dataJson.links)
         .start();
    
    //定义节点标记
    var node = svg.selectAll(".node")
          .data(dataJson.nodes)
          .enter()
          .append("g");

    //定义连线
    var link = svg.selectAll(".link")
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
      .text(function(d) { return d.id.substring(1); });

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

    

