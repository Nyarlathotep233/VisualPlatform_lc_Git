<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="js/sti_utils.js"> </script>

<script src="js/GeomView.js"> </script>
<script src="js/SceneGraph.js" > </script>

<script src="js/Annotation.js" > </script>
<script src="js/Assembly.js" > </script>
<script src="js/BoundingBox.js"> </script>
<script src="js/DataLoader.js"> </script>
<script src="js/Executable.js"> </script>
<script src="js/GLTransform.js" > </script>

<script src="js/MachineModel.js" > </script>
<script src="js/Menu.js" > </script>
<script src="js/Operation.js" > </script>
<script src="js/Placement.js" > </script>
<script src="js/Project.js" > </script>
<script src="js/Selective.js" > </script>
<script src="js/Shape.js" > </script>
<script src="js/ShapeBuilder.js" > </script>
<script src="js/Shell.js" > </script>
<script src="js/Tree.js" > </script>
<script src="js/ViewVolume.js" > </script>
<script src="js/Workingstep.js" > </script>
<script src="js/Workplan.js" > </script>
<script src="js/webgl-utils.js"> </script>
<script src="js/gl-matrix.js"> </script>

<script src="js/view3d.js" > </script>
<script src="myjs/d3_test.js" > </script>
<script src="myjs/nodeAsmTree.js" > </script>
<script src="myjs/rightButton.js" > </script>
<script src="myjs/StepNode.js" > </script>
<script src="myjs/attributeShow.js" > </script>

<script src="js/jquery-1.8.3.min.js" > </script>

<script src="myjs/nodeGraph.js" > </script>

<script>
$.ajaxSetup({
	async:false
});
showBaseNodeGraph();
//返回左侧页面中的span元素,span中为树形结构
function getAssemblyNode() {return document.getElementById("nodeContainer");}

//展开所有的树状结构
function expand_all() { 
  
  ASSEMBLY.expandTree();
  showTree();

  return false;
}

//闭合所有的树状结构
function collapse_all() {
  ASSEMBLY.collapseTree();
  return false;
}

//展开节点图
function showNodeTree(){
	
		var tree_ul=makeAsmNodeTree(ASSEMBLY);

		var tree_doc = tree_ul.ownerDocument;

		var menu = ASSEMBLY.initTreeMenu(tree_doc.getElementById("menu"))

		ASSEMBLY.html_tree = new Tree(tree_ul, menu ,ASSEMBLY.sg_context);  
  
}
//保存当前节点图Json
function printDataJson(){
  console.log(ASSEMBLY.dataJson);
}

var getOffset = {  
       top: function (obj) {  
           return obj.offsetTop + (obj.offsetParent ? arguments.callee(obj.offsetParent) : 0)   
       },  
       left: function (obj) {  
           return obj.offsetLeft + (obj.offsetParent ? arguments.callee(obj.offsetParent) : 0)   
       }     
   };           
</script>



<style type="text/css">

.unselected {
    color: #000000;
}

.invisible {font-weight: normal}
.visible {font-weight: bold}

  /* for collapsible tree nodes */
#assembly  LI {
    list-style-type: none;

    /* Disable selection of menu items.  Otherwise, stuff gets highlighted,
     * when the tree nodes are toggled.
     */
    -moz-user-select: none;
}

#assembly UL {list-style-image: url(minus.gif)} 
#assembly LI.closed {list-style-image: url(plus.gif)} 
#assembly LI.closed UL {display: none}
#assembly LI.leaf {list-style-image: url(bullet.gif)} 

.noshell {
    color: #aaaaee;
    font-style: italic;
    font-weight: normal;
}

.selected {
    color: #c03030;
}

.menu-target {
    background-color: #c0c000;
}

</style>

<style type="text/css">  
html,body{height:100%;overflow:hidden;}
body,div,ul,li{margin:0;padding:0;}
body{font:15px/1.5 \5fae\8f6f\96c5\9ed1;}
ul{list-style-type:none;}
#rightMenu{position:absolute;top:-9999px;left:-9999px;}
#rightMenu ul{float:left;border:1px solid #979797;background:#f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png) 24px 0 repeat-y;padding:2px;box-shadow:2px 2px 2px rgba(0,0,0,.6);}
#rightMenu ul li{float:left;height:24px;cursor:pointer;line-height:24px;white-space:nowrap;padding:0 0px;text-align:center;}
#rightMenu ul li.sub{background-repeat:no-repeat;background-position:right 9px;background-image:url(http://js.fgm.cc/learn/lesson6/img/arrow.png);}
#rightMenu ul li.active{background-color:#f1f3f6;border-radius:3px;border:1px solid #aecff7;height:22px;line-height:22px;background-position:right -8px;padding:0 0px;}
#rightMenu ul ul{display:none;position:absolute;}
#showAttributes {border:1px solid #979797;background:#f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png);padding: 2px;box-shadow:2px 2px 2px rgba(0,0,0,.6);}
#showAttributes span {padding-left: 27px;height:24px;display: block;border-top:2px solid #979797;}
#showAttributes span select {width:150px;text-align:center;height:24px}  

</style>  

<style type-"text/css">

.menu li:hover {
    border: 1px solid black;
    background: #dde8dd;
}

.menu li { 
    whitespace: nowrap;
    border: 1px solid white;
}

.menu {
    border: 1px solid black;
    position:absolute;
    white-space: nowrap;
    display: none;
    background: white;
}

.menu ul {
    list-style-type: none;
    padding: .5em;
    margin: 0;
}

.menu-visible {
    display:block;
}

#menu_download {
    display:none;
}
</style>

</head>

<body>
  <P><b>Node graph</b>(Right click to open the function menu)<br>
   <!--  <a onclick="expand_all();" href="#">[展开所有节点]</a>
    <a onclick="collapse_all();" href="#">[闭合所有节点]</a> -->
    <!-- <a onclick="showNodeTree();" href="#">[展开节点图]</a> -->
    <input style="margin-left: 20px" type="button" value="Open geometry node graph" onclick="showGeometryNodeGraph()"/> 
    <input style="margin-left: 20px" type="button" value="Open the semantic node graph" onclick="showSemanticNodeGraph()"/> 
    <input style="margin-left: 20px" type="button" value="Reasoning of design intent" onclick="reason()"/>    
  <p> 
  <span id="nodeContainer"></span>

  <div id="rightMenu">  
    <ul>
        <li>open</li>  
        <li>openLink</li>  
        <li>closeLink</li>  
        <li>aggregation</li>
        <li>spread</li>  
    </ul>
    <div id="showAttributes">  
    </div>  
  </div>  

  <div class="menu" id="menu">
    <ul>
    <li class="menu_show">Show</li>
    <li class="menu_show_only">Show only this</li>
    <li class="menu_hide">Hide</li>
    <li class="menu_show_all">Show All</li>
    <hr>
    <li class="menu_select">Select</li>
    <li class="menu_select_all">Select all Instances</li>
    <li class="menu_unselect">Unselect</li>
    <li class="menu_unselect_all">Clear All Selections</li>
    <hr>
    <li class="menu_open_subpart">Open in New Window</li>
    <hr>
    <li class="menu_load_part">Load Part</li>
    <li class="menu_unload_part">Unload Part</li>
    <hr>
    <li class="menu_edit_links">Edit Links</li>
    <hr>
    <li class="menu_props">Download STEP File</li>
    <li id="menu_download" class="menu_nothing"></li>
    </ul>  
  </div>

</body>
</html>