<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>可视化平台</title>
<link rel="stylesheet" href="css/style.default.css" type="text/css" />
<script type="text/javascript" src="js/plugins/jquery-1.7.min.js"></script>
<script type="text/javascript" src="js/plugins/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="js/plugins/jquery.cookie.js"></script>
<script type="text/javascript" src="js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="js/plugins/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/plugins/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="js/plugins/charCount.js"></script>
<script type="text/javascript" src="js/plugins/ui.spinner.min.js"></script>
<script type="text/javascript" src="js/plugins/chosen.jquery.min.js"></script>
<script type="text/javascript" src="js/custom/general.js"></script>
<script type="text/javascript" src="js/custom/forms.js"></script>

<script src="myjs/modelshow2.js"></script>
<script src="myjs/d3_test.js"></script>
<script src="myjs/rightButton.js" > </script>
<script src="myjs/StepNode.js" > </script>
<script src="myjs/attributeShow.js" > </script>
<script type="text/javascript" src="myjs/myLayer.js"></script>

<!--[if IE 9]>
    <link rel="stylesheet" media="screen" href="css/style.ie9.css"/>
<![endif]-->
<!--[if IE 8]>
    <link rel="stylesheet" media="screen" href="css/style.ie8.css"/>
<![endif]-->
<!--[if lt IE 9]>
	<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
<![endif]-->

<script>
	showBaseNodeGraph();
	showGeometryNodeGraph();
	function backtoHome() {
	    location.href = "nodegraph.jsp";
	}
</script>

<style type="text/css">
	#rightMenu{top:-9999px;left:-9999px;position:absolute;}
	#rightMenu ul{float:left;border:1px solid #979797;background:#f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png) 24px 0 repeat-y;padding:2px;box-shadow:2px 2px 2px rgba(0,0,0,.6);}
	#rightMenu ul li{float:left;height:24px;cursor:pointer;line-height:24px;white-space:nowrap;padding:0 0px;text-align:center;}
	#rightMenu ul li.sub{background-repeat:no-repeat;background-position:right 9px;background-image:url(http://js.fgm.cc/learn/lesson6/img/arrow.png);}
	#rightMenu ul li.active{background-color:#f1f3f6;border-radius:3px;border:1px solid #aecff7;height:22px;line-height:22px;background-position:right -8px;padding:0 0px;}
	#rightMenu ul ul{display:none;position:absolute;}
	#showAttributes {border:1px solid #979797;background:#f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png);padding: 2px;box-shadow:2px 2px 2px rgba(0,0,0,.6);}
	#showAttributes span {padding-left: 27px;height:24px;display: block;border-top:2px solid #979797;}
	#showAttributes span select {width:150px;text-align:center;height:24px}
</style>
</head>

<body>

<div class="bodywrapper">
    
    <div class="header">
		<div style="margin-top: 1%;margin-left:1%">
    		<button class="stdbtn btn_yellow"; style="margin-left:1%"; onclick = "backtoHome()">返回</button>		
		</div>
        <h1 class="logo" align="center" style="margin-top: -2%"><span>可视化平台</span></h1>
        <br clear="all" />
    </div><!--header-->
    
    <div style="width:50%;float:left">
    	<div>
    		<div class="pageheader">
            <h1 class="pagetitle" style="text-align:center;">节点图</h1>
    		<div id="nodeContainer" style="margin-top: 2%"></div>
        </div><!--pageheader-->
            <div>
        		<div id="rightMenu">  
		    		<ul>
		        		<li>open</li>  
		        		<li>hide</li>  
		        		<li>show</li>  
		       			<li>aggregation</li>
		        		<li>spread</li>  
		    		</ul>
		    		<div id="showAttributes">  
		    		</div>  
		 		</div>	 
            </div>
        </div><!--pageheader-->
    </div><!--leftmenu-->
        
    <div style="width:50%; float:right">
        <div class="pageheader">
            <h1 class="pagetitle" style="text-align:center;">三维模型展示</h1>
    		<div id="container" style="margin-top: 2%"></div>
        </div><!--pageheader-->
           
	</div><!--righttmenu-->
    
</div><!--bodywrapper-->

<script src="my-threejs/js/libs/three.min.js"></script>
<script src="my-threejs/js/libs/dat.gui.js"></script>
<script src="my-threejs/js/libs/OrbitControls.js"></script>
<script src="my-threejs/js/libs/THREE.MeshLine.js"></script>
<script src="my-threejs/js/libs/gl-matrix-min.js"></script>
<script src="my-threejs/js/libs/gl-matrix.js"></script>
<script src="my-threejs/js/annotation.js"></script>
<script src="my-threejs/js/geom.js"></script>
<script src="my-threejs/js/lunkuo.js"></script>
<script src="my-threejs/js/scene.js"></script>
<script src="my-threejs/js/main-graph.js"></script>
</body>
</html>
