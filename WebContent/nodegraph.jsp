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

<script src="myjs/nodeGraph.js"></script>
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

<body class="withvernav">

<div class="bodywrapper">
    
    <div class="header">
    
		<h1 class="logo" align="center" style="margin-top: 10px"><span>可视化平台</span></h1>
            
        <br clear="all" />
        
    </div><!--header-->
    
    <div class="vernav2 iconmenu" style="margin-top: -100px">
    	<ul>
    		<li class="current" style="text-align:center;margin-bottom: 10px">
        		<span>载入Step文件</span>
            </li>
        	<li class="current" style="text-align:center;margin-bottom: 10px">
        		<input type="file" id="file" onchange="importStepFile(this.files)"/> 
            </li>
            <li class="current" style="text-align:center;margin-bottom: 10px">
        		<button class="stdbtn btn_yellow" onclick="showGeometryNodeGraph()">打开几何节点图</button> 
            </li>
            <li class="current" style="text-align:center;margin-bottom: 10px">
        		<button class="stdbtn btn_yellow" onclick="showSemanticNodeGraph()">打开语义节点图</button> 
            </li>
            <li class="current" style="text-align:center;margin-bottom: 10px">
        		<button class="stdbtn btn_yellow" onclick = "moddelshow()">展示三维模型</button> 
            </li>
            <li class="current" style="text-align:center;margin-bottom: 10px">
        		<button class="stdbtn btn_yellow" onclick = "reason()">设计意图推理</button> 
            </li>
            <li class="current" style="text-align:center;margin-bottom: 10px">
        		<div id="result" style="visibility: hidden;" class="profile_about">
        			<p>组成圆柱凸台特征的面集合有：</p>
        			<p>[#546,#548],[#448,#456],[#547,#545],[#461,#465],[#462,#463]</p>
        			<p>组成台阶特征的面集合有：</p>
        			<p>[#407,#408],[#413,#414],[#437,#447],[#467,#470],[#469,#471],[#472,#481],[#473,#484],[#505,#525],[#510,#528]</p>
        			<p>组成盲孔特征的面集合有：</p>
        			<p>[#526,#530],[#496,#497],[#496,#498],[#496,#499],[#496,#500],[#496,#501],[#496,#502],[#496,#503],[#496,#504]</p>
        			<p>组成通孔特征的面集合有：</p>
        			<p>[#427,#428,#429],[#434,#435,#436],[#450,#458,#459],[#475,#476,#477],[#511,#531,#532],[#535,#537,#538]</p>
        			<p>组成凸台特征的面集合有：</p>
        			<p>[#421,#442,#443,#444,#445],[#448,#451,#452,#453,#454]</p>
       			</div>
            </li>
        </ul>
    </div><!--leftmenu-->
        
    <div class="centercontent">
    
        <div class="pageheader">
            <h1 class="pagetitle">节点图</h1>
        </div><!--pageheader-->
        
        <div id="nodeContainer" class="contentwrapper">
			
        </div><!--contentwrapper-->
        
        <div id="rightMenu">  
		    <ul>
		        <li>open</li>  
		        <li>hide</li>  
		        <li>show</li>  
		        <li>addAnnotation</li>
		    </ul>
		    <div id="showAttributes">  
		    </div>  
		 </div>
        
	</div><!-- centercontent -->
    
    
</div><!--bodywrapper-->

</body>
</html>
