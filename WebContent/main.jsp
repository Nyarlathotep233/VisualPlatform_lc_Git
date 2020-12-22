<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8">
        <title>可视化平台</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <meta name="author" content="SuggeElson" />
        <meta name="description" content=""/>
        <meta name="keywords" content=""/>
        <meta name="application-name" content="sprFlat admin template" />
        <link href="assets/css/icons.css" rel="stylesheet" />
        <link href="assets/css/bootstrap.css" rel="stylesheet" />
        <link href="assets/css/plugins.css" rel="stylesheet" />
        <link href="assets/css/main.css" rel="stylesheet" />
        <meta name="msapplication-TileColor" content="#3399cc" />
        <style>
            .resizable { background-position: center; }
            .draggable { padding: 0.5em; }
            .content-wrapper {left: -150px;}
            #draggable div { cursor: move; }
        </style>
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
        <!-- Start #header -->
        <div id="header">
            <div class="container-fluid">
                <div class="navbar">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="index.html">
                            <i class="im-windows8 text-logo-element animated bounceIn"></i><span class="text-logo">可视化</span><span class="text-slogan">平台</span> 
                        </a>
                    </div>
                     <!-- 上方导航菜单左侧 -->
                    <nav class="top-nav" role="navigation">
                        <ul class="nav navbar-nav pull-left">
                        </ul>
                        <!-- 上方导航菜单右侧 -->
                        <ul class="nav navbar-nav pull-right">
                            <li>
                                <a href="#" id="toggle-header-area"><i class="ec-download"></i></a>
                            </li>
                            <li>
                                 <a href="#" class="full-screen"><i class="fa-fullscreen"></i></a>
                            </li>
                        </ul>
                    </nav>
                </div>
                <!-- Start #header-area -->
                <!-- 下拉菜单内容 -->
                <div id="header-area" class="fadeInDown">
                    <div class="header-area-inner">
                        <ul class="list-unstyled list-inline">
                            <li>
                                <div class="shortcut-button">
                                    <a href="#">
                                        <i class="im-pie"></i>
                                        <span>Earning Stats</span>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="shortcut-button">
                                    <a href="#">
                                        <i class="ec-images color-dark"></i>
                                        <span>Gallery</span>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="shortcut-button">
                                    <a href="#">
                                        <i class="en-light-bulb color-orange"></i>
                                        <span>Fresh ideas</span>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="shortcut-button">
                                    <a href="#">
                                        <i class="ec-link color-blue"></i>
                                        <span>Links</span>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="shortcut-button">
                                    <a href="#">
                                        <i class="ec-support color-red"></i>
                                        <span>Support</span>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="shortcut-button">
                                    <a href="#">
                                        <i class="st-lock color-teal"></i>
                                        <span>Lock area</span>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- End #header-area -->
            </div>
            <!-- Start .header-inner -->
        </div>
        <!-- End #header -->
        <!-- Start #content -->
        <div id="content">
            <!-- Start .content-wrapper -->
            <div class="content-wrapper">
                    <div class="row">
                        <!-- Start .row -->
                        <div class="col-lg-6 col-md-6 sortable-layout" >
                            <!-- Start col-lg-6 -->
                            <div class="panel panel-teal toggle panelClose panelRefresh resizable draggable window1">
                                <!-- Start .panel -->
                                <div class="panel-heading heading1">
                                    <h4 class="panel-title"><i class="im-bars"></i>节点图</h4>
                                </div>
                                <div id="nodeContainer" class="panel-body">
                                </div>
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
                            </div>
                            <!-- End .panel -->
                        </div>
                        <!-- End col-lg-6 -->
                        <div class="col-lg-6 col-md-6 sortable-layout" >
                            <!-- Start col-lg-6 -->
                            <div class="panel panel-primary plain toggle panelClose panelRefresh resizable draggable window2">
                                <!-- Start .panel -->
                                <div class="panel-heading heading2">
                                    <h4 class="panel-title"><i class="im-bars"></i>三维模型展示</h4>
                                </div>
                                <div id="container" class="panel-body">
                                </div>
                            </div>
                            <!-- End .panel -->
                        </div>
                        <!-- End col-lg-6 -->
                    </div>
                    <!-- End .row -->
                    <!-- Page End here -->
                </div>
                <!-- End .outlet -->
            </div>
            <!-- End .content-wrapper -->
            <div class="clearfix"></div>
        </div>
        <!-- End #content --> 
        <script src="assets/plugins/core/pace/pace.min.js"></script>
        <script src="assets/js/jquery-1.8.3.min.js"></script>
        <script>
        window.jQuery || document.write('<script src="assets/js/libs/jquery-2.1.1.min.js">\x3C/script>')
        </script>
        <script src="assets/js/jquery-ui.js"></script>
        <script>
        window.jQuery || document.write('<script src="assets/js/libs/jquery-ui-1.10.4.min.js">\x3C/script>')
        </script>
        <script>
        jQuery(document).ready(function($) {
            $( ".resizable" ).resizable();
            $( ".draggable" ).draggable({ handle: "div.panel-heading" });
   
        
          } );
        
        $(document).ready(function(){
        	  $(".heading1").hover(function(){
        	    $(".heading1").css("background-color","yellow");
        	    },function(){
        	    $(".heading1").css("background-color","pink");
        	  });
        	});
        $(document).ready(function(){
      	  $(".heading2").hover(function(){
      	    $(".heading2").css("background-color","yellow");
      	    },function(){
      	    $(".heading2").css("background-color","pink");
      	  });
      	});
      
      	</script>
        <script src="assets/js/bootstrap/bootstrap.js"></script>
        <script src="assets/js/jRespond.min.js"></script>
        <script src="assets/plugins/core/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="assets/plugins/core/slimscroll/jquery.slimscroll.horizontal.min.js"></script>
        <script src="assets/plugins/forms/autosize/jquery.autosize.js"></script>
        <script src="assets/plugins/core/quicksearch/jquery.quicksearch.js"></script>
        <script src="assets/plugins/ui/bootbox/bootbox.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.pie.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.resize.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.time.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.growraf.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.categories.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.stack.js"></script>
        <script src="assets/plugins/charts/flot/jquery.flot.tooltip.min.js"></script>
        <script src="assets/plugins/charts/sparklines/jquery.sparkline.js"></script>
        <script src="assets/plugins/charts/pie-chart/jquery.easy-pie-chart.js"></script>
        <script src="assets/plugins/forms/icheck/jquery.icheck.js"></script>
        <script src="assets/plugins/forms/tags/jquery.tagsinput.min.js"></script>
        <script src="assets/plugins/forms/tinymce/tinymce.min.js"></script>
        <script src="assets/plugins/misc/highlight/highlight.pack.js"></script>
        <script src="assets/plugins/misc/countTo/jquery.countTo.js"></script>
        <script src="assets/plugins/ui/weather/skyicons.js"></script>
        <script src="assets/plugins/ui/notify/jquery.gritter.js"></script>
        <script src="assets/plugins/ui/calendar/fullcalendar.js"></script>
        <script src="assets/js/jquery.sprFlat.js"></script>
        <script src="assets/js/app.js"></script>
        <script src="assets/js/pages/dashboard.js"></script>
        
        <script src="myjs/nodeGraph.js"></script>
		<script src="myjs/d3_test.js"></script>
		<script src="myjs/rightButton.js" > </script>
		<script src="myjs/StepNode.js" > </script>
		<script src="myjs/attributeShow.js" > </script>
		<script type="text/javascript" src="myjs/myLayer.js"></script>
		
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
		<script>
			showBaseNodeGraph();
			showGeometryNodeGraph();
		</script>
    </body>
</html>