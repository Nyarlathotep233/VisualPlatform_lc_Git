<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script>
"use strict";

// The maximum number of facets to load be default
var MAX_COST=250000

//DataLoader类，在DataLoder.js创建
var LOADER = new DataLoader();

function setup_page(asm_frame, params, init_fn) {

    LOADER.status_element = window.document.getElementById("status");

    //getAssemblyNode()该方法在asm-tree.html中定义,返回左侧的span元素（树形结构）
    //console.log(asm_tree) 结果为<span id="assembly">
    var asm_tree = asm_frame.getAssemblyNode();

    //c是一个canvas
    var c = document.getElementById('wgl');

    //resize_canvas()定义在view3d.js，用来设定canvas页面的大小
    resize_canvas(c);

    //GeomView类，在GeomView.js创建   调用构造方法 返回了一个webgl 声明了各类鼠标方法
    VIEWER = new GeomView(c);
   
    //调用了Assembly.js中的load静态方法 传入四个参数 viewer, tree_el, param_str, load_fn
    //VIEWER:右方的canvas
    //asm_tree:左方的span元素
    //params 调用sti_utils.js中的parse_search(s, def)方法 
    //location.search是从当前URL的?号开始的字符串 地址栏中的1.xml
    //load_fn:初始化左方的frame 和右方的canvas
    var ASSEMBLY = Assembly.load(VIEWER, asm_tree, params, 
        function(asm) {
          asm_frame.ASSEMBLY = asm;
          asm_frame.collapse_all();
          if (init_fn) init_fn(VIEWER);
        });

    VIEWER.draw();

    window.addEventListener("resize", 
       function() {
          resize_canvas(c);
          VIEWER.draw();
       }, false);
}

function draw() {VIEWER.draw();}
</script>

<style type-"text/css">
#status {
    background: white;
    position: fixed;  
    top : 10px;
    left : 10px;
}


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

<div id="status"></div>


<div class="menu" id="menu">
<ul>
<li class="menu_select_show">Select / Show</li>
<li class="menu_hide">Hide</li>
<li class="menu_show_only">Show Only This</li>
<li class="menu_show_all">Show All</li>
<hr>
<li class="menu_toggle_select">Toggle Selection</li>
<li class="menu_unselect_all">Clear Selection</li>
<li class="menu_select_all">Select all Instances</li>
<li class="menu_select_parent">Select Parent</li>
<hr>
<li class="menu_open_subpart">Open in New Window</li>
<hr>
<li class="menu_props">Download STEP File</li>
<li id="menu_download" class="menu_nothing"></li>
</ul>  
</div>

<canvas id="wgl" >
  This page requires a browser that supports HTML5 and WebGL.<br/>
  Go to <a href="http://get.webgl.org" TARGET="_top">get.webgl.org</a>
  to find out more.
</canvas>

</body>
</html>