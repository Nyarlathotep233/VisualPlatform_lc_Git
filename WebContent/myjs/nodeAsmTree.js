//获得空的DataJson对象
function getEmptyDataJson(){
	var dataJson={"nodes":[],"links":[]};
    return dataJson;
} 
//删除字符串左右两端的空格
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
//根据id得到节点对象
function getLowNodeById(parsedData,id){
  var lowNode={};
  for(var i=0;i<parsedData.length;i++){
    if(parsedData[i].id==id){
      lowNode=parsedData[i];
    }
  }
  return lowNode;
}
//复制节点
function copyNode(source){
    var node={};
    node.assembly=source.assembly;
    node.bbox=source.bbox;
    node.children=source.children;
    node.id=source.id;
    node.name=source.name;
    node.shapes=source.shapes;
    node.stepfile=source.stepfile;
    node.visible=source.visible;
    node.weight=source.weight;
    node.rootNode=source.rootNode;
    node.color=source.color;
    node.lowChildren=source.lowChildren;
    return node; 
}
//根据id获取节点对象
function getNode(dataJson,id){
	  var nodes=dataJson.nodes;
	    for(var i=0;i<nodes.length;i++){
	  var nodeId=nodes[i].id;
	  if(nodeId==id){
	     return nodes[i];
	  }
	   }
	   return null;
} 
//生成节点图
function makeAsmNodeTree(ASSEMBLY, baseSemanticCells) {

	var el=ASSEMBLY.tree_el;
    var tn=ASSEMBLY.tree;
    var dataJson=ASSEMBLY.dataJson;
    
    //遍历初始语义元节点并添入dataJson中
    for(var i = 0; i < baseSemanticCells.length; i++){
    	var node = baseSemanticCells[i];
    	node.id = baseSemanticCells[i].body.instance.instanceId;
    	//颜色数组下标
    	node.color = 0;
    	//半径大小比例
    	node.r = 1;
    	node.visible = true;
    	node.name = baseSemanticCells[i].type;
    	dataJson.nodes.push(node);
    }

    var oMenu = document.getElementById("rightMenu");  
    var aUl = oMenu.getElementsByTagName("ul");  
    var aLi = oMenu.getElementsByTagName("li");
    var oShowAttributes=document.getElementById("showAttributes");

    var showTimer = hideTimer = null;  
    var i = 0;  
    var maxWidth = maxHeight = 0;  
    var aDoc = [800, document.documentElement.offsetHeight];  

    oMenu.style.display = "none";    

    //console.log(el); <span id="assembly">
    //console.log(tn); Object { SGContextId: 1, xform: undefined, sg: Object, children: Array[1] }

    var width =  800;
    var height = 450;

    //取得20个颜色的序列
    // var color = d3.scale.category20();
    var color=['#EE0000','#008000','#0000FF','#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB',
        '#CD5C5C','#FFA500','#00FF00','#FFFF00','#808080','#FFC0CB'];

    //定义画布
    var svg = d3.select(el).append("svg")
                 .attr("width", width)
                 .attr("height", height);

    //定义力学结构
    var force = d3.layout.force()
                .charge(-30)
                .linkDistance(180)
                .size([width, height]);

    force.nodes(dataJson.nodes)
         .links(dataJson.links)
         .start();

    //定义连线
    var link = svg.selectAll(".link")
          .data(dataJson.links)
          .enter()
          .append("line")
          .attr("class", "link")
          .attr("stroke","#09F")
          .attr("stroke-opacity","0.4")
          .style("stroke-width",function(d){return d.width * 2;});

    //定义节点标记
    var node = svg.selectAll(".node")
          .data(dataJson.nodes)
          .enter()
          .append("g");

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
        .text(function(d) { return d.linkType; })

    //拖拽事件
    var drag = force.drag() 
      .on("dragstart",function(d,i){   
      })  
      .on("dragend",function(d,i){  
           d.fixed = true;    //拖拽开始后设定被拖拽对象为固定   
      })  
      .on("drag",function(d,i){ 
      })   

      // //力学图运动开始时  
      // force.on("start", function(){  
      //     console.log("开始");  
      // });  
        
      // //力学图运动结束时  
      // force.on("end", function(){  
      //     console.log("结束"); 
      // });  
          
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
              .on("mousedown",function(d){
                  if(d3.event.button==0&&!d3.event.altKey){
                    point.x=d.x;
                    point.y=d.y;
                  }else if(d3.event.button==0&&d3.event.altKey){
                    //这里是控制单击使得画面变化，加红色框

                      var tn = d.treenode;
                      
                      var tree = tn.getRootNode();

                      var selected = tn.isSelected();   

                      // console.log(tn)  
                      // console.log(ASSEMBLY.viewer.scenegraph)  

                      var preserve = d3.event.shiftKey;

                      if (!preserve) {
                          if (selected && tree.countSelects() > 0) 
                              selected = false;

                          tree.clearSelection();
                        }

                        tn.select(!selected);
                        
                        tn.getRoot().viewer.draw();
                        // tn.redraw();

                        tree.updateTree();
                        
                        d3.event.stopPropagation();
                  }                                           
              })
              .on("mouseup",function(d){ 
                    if(d3.event.button==0&&!d3.event.shiftKey&&d.x==point.x&&d.y==point.y){
                      setChildVisible(d);       
                   }
                                                                          
              })
              .on("contextmenu",function(d){

                var event = d3.event; 

                showAttributes(oShowAttributes,d);

                mouseSet(aLi,d,dataJson,force,node,link,color);
                
                oMenu.style.display = "block";  
                oMenu.style.top = event.clientY + "px";  
                oMenu.style.left = event.clientX + "px";  
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
        //根据count获取节点对象
          function getNodeByCount(count){
            var nodes=dataJson.nodes;
              for(var i=0;i<nodes.length;i++){
                var nodeCount=nodes[i].count;
                if(nodeCount==count){
                  return nodes[i];
                }
              }
              return null;
            }
        //根据节点对象来使其子节点及连线的显示方式发生变化
        function setChildVisible(node){
          var linkArray=getLinksByNode(node,dataJson);
          for(var i=0;i<linkArray.length;i++){
            var linkTypes=linkArray[i].linkType;
            for(var j=0;j<linkTypes.length;j++){
              if(linkTypes[j].name=='parent-children'){
                linkArray[i].visible=!linkArray[i].visible;
              } 
            }                     
          }
          for(var i=0;i<node.children.length;i++){
            var childNode=getNodeByCount(node.children[i].count);
            childNode.visible=!childNode.visible;
            setChildVisible(childNode);
          }
        }

        return el.firstChild;

  }
