<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <% String path=request.getContextPath(); String basePath=request.getScheme() + "://" + request.getServerName() + ":"
    +request.getServerPort() + path + "/" ; %>
    <html lang="en">

    <head>
      <meta charset="utf-8" />
      <title>可视化平台</title>

      <link rel="stylesheet" href="ww/font-awesome-4.3.0/css/font-awesome.min.css" />
      <link rel="stylesheet" href="ww/css/documentation.css" />
      <link href="ww/css/prettify.css" rel="stylesheet" />
      <link href="ww/css/jquery.toolbar.css" rel="stylesheet" />

      <!-- Mobile specific metas -->
      <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
      <!-- Force IE9 to render in normal mode -->
      <!--[if IE]><meta http-equiv="x-ua-compatible" content="IE=9" /><![endif]-->
      <meta name="author" content="SuggeElson" />
      <meta name="description" content="" />
      <meta name="keywords" content="" />
      <meta name="application-name" content="sprFlat admin template" />
      <!-- Import google fonts - Heading first/ text second -->
      <link rel="stylesheet" type="text/css" />

      <!-- Css files -->
      <!-- Icons -->
      <link href="assets/css/icons.css" rel="stylesheet" />
      <!-- jQueryUI -->
      <!-- <link href="assets/css/sprflat-theme/jquery.ui.all.css" rel="stylesheet" /> -->
      <!-- Bootstrap stylesheets (included template modifications) -->
      <link href="assets/css/bootstrap.css" rel="stylesheet" />
      <!-- Plugins stylesheets (all plugin custom css) -->
      <link href="assets/css/plugins.css" rel="stylesheet" />
      <!-- Main stylesheets (template main css file) -->
      <link href="assets/css/main.css" rel="stylesheet" />
      <link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/jquery-ui-1.11.0.css" />
      <link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/bootstrap-3.3.4.css" />
      <!-- <link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css"> -->
      <link rel="stylesheet" type="text/css" href="ww/font-awesome-4.3.0.css" />
      <link rel="stylesheet" href="dist/css/lobipanel.min.css" />

      <meta name="msapplication-TileColor" content="#3399cc" />
      <style type="text/css">
        #rightMenu {
          top: -9999px;
          left: -9999px;
          position: absolute;
        }

        #rightMenu ul {
          float: left;
          border: 1px solid #979797;
          background: #f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png) 24px 0 repeat-y;
          padding: 2px;
          box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.6);
        }

        #rightMenu ul li {
          float: left;
          height: 24px;
          cursor: pointer;
          line-height: 24px;
          white-space: nowrap;
          padding: 0 0px;
          text-align: center;
        }

        #rightMenu ul li.sub {
          background-repeat: no-repeat;
          background-position: right 9px;
          background-image: url(http://js.fgm.cc/learn/lesson6/img/arrow.png);
        }

        #rightMenu ul li.active {
          background-color: #f1f3f6;
          border-radius: 3px;
          border: 1px solid #aecff7;
          height: 22px;
          line-height: 22px;
          background-position: right -8px;
          padding: 0 0px;
        }

        #rightMenu ul ul {
          display: none;
          position: absolute;
        }

        #showAttributes {
          border: 1px solid #979797;
          background: #f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png);
          padding: 2px;
          box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.6);
        }

        #showAttributes span {
          padding-left: 27px;
          height: 24px;
          display: block;
          border-top: 2px solid #979797;
        }

        #showAttributes span select {
          width: 150px;
          text-align: center;
          height: 24px;
        }

        #rightMenu3 {
          top: -9999px;
          left: -9999px;
          position: absolute;
        }

        #rightMenu3 ul {
          float: left;
          border: 1px solid #979797;
          background: #f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png) 24px 0 repeat-y;
          padding: 2px;
          box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.6);
        }

        #rightMenu3 ul li {
          float: left;
          height: 24px;
          cursor: pointer;
          line-height: 24px;
          white-space: nowrap;
          padding: 0 0px;
          text-align: center;
        }

        #rightMenu3 ul li.sub {
          background-repeat: no-repeat;
          background-position: right 9px;
          background-image: url(http://js.fgm.cc/learn/lesson6/img/arrow.png);
        }

        #rightMenu3 ul li.active {
          background-color: #f1f3f6;
          border-radius: 3px;
          border: 1px solid #aecff7;
          height: 22px;
          line-height: 22px;
          background-position: right -8px;
          padding: 0 0px;
        }

        #rightMenu3 ul ul {
          display: none;
          position: absolute;
        }

        #rightMenu4 {
          top: -9999px;
          left: -9999px;
          position: absolute;
        }

        #rightMenu4 ul {
          float: left;
          border: 1px solid #979797;
          background: #f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png) 24px 0 repeat-y;
          padding: 2px;
          width: 502px;
        }

        #rightMenu4 ul li {
          float: left;
          height: 24px;
          cursor: pointer;
          line-height: 24px;
          white-space: nowrap;
          padding: 0 0px;
          text-align: center;
        }

        #rightMenu4 ul li.sub {
          background-repeat: no-repeat;
          background-position: right 9px;
          background-image: url(http://js.fgm.cc/learn/lesson6/img/arrow.png);
        }

        #rightMenu4 ul li.active {
          background-color: #f1f3f6;
          border-radius: 3px;
          border: 1px solid #aecff7;
          height: 22px;
          line-height: 22px;
          background-position: right -8px;
          padding: 0 0px;
        }

        #rightMenu4 ul ul {
          display: none;
          position: absolute;
        }

        .qlh {
          margin: 20px auto;
          text-align: center;
        }

        /* #rightMenu4{top:-9999px;left:-9999px;position:absolute;}
        #rightMenu4 ul{float:left;border:1px solid #979797;background:#f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png) 24px 0 repeat-y;padding:2px;box-shadow:2px 2px 2px rgba(0,0,0,.6);}
        #rightMenu4 ul li{float:left;height:24px;cursor:pointer;line-height:24px;white-space:nowrap;padding:0 0px;text-align:center;}
        #rightMenu4 ul li.sub{background-repeat:no-repeat;background-position:right 9px;background-image:url(http://js.fgm.cc/learn/lesson6/img/arrow.png);}
        #rightMenu4 ul li.active{background-color:#f1f3f6;border-radius:3px;border:1px solid #aecff7;height:22px;line-height:22px;background-position:right -8px;padding:0 0px;}
        #rightMenu4 ul ul{display:none;position:absolute;}
        #showAttributes4 {border:1px solid #979797;background:#f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png);padding: 2px;box-shadow:2px 2px 2px rgba(0,0,0,.6);}
        #showAttributes4 span {padding-left: 27px;height:34px;display: block;border-top:2px solid #979797;}
        #showAttributes4 span select {width:150px;text-align:center;height:34px} */
        .button {
          background-color: #d19275;
          /* Green */
          border: none;
          color: white;
          text-align: center;
          text-decoration: none;
          display: inline-block;
          font-size: 10px;
          margin: 2px 1px;
          cursor: pointer;
          border-radius: 4px;
        }

        .feature_desc_con {
          background: #f1f1f1;
          margin-top: 25px;
          border: 1px solid #979797;
        }

        .val_cla {
          margin-left: 2px;
        }

        .feature_cla {
          width: 400px;
          height: 30px;
          margin-top: 13px;
        }

        .left_feature_cla {
          width: 100px;
          float: left;
        }

        .right_feature_cla {
          width: 290px;
          float: left;
        }

        #showAttributes4 {
          border: 1px solid #979797;
          background: #f1f1f1 url(http://js.fgm.cc/learn/lesson6/img/line.png);
          padding: 2px;
        }

        #showAttributes4 span {
          padding-left: 27px;
          height: 32px;
          display: block;
          border-top: 2px solid #979797;
        }

        #showAttributes4 span select {
          width: 150px;
          text-align: center;
          height: 30px;
        }

        .spread-btn img {
          width: 20px;
        }
      </style>
    </head>

    <body>
      <!-- Start #header -->
      <div id="header">
        <div class="container-fluid">
          <div class="navbar">
            <div class="navbar-header">
              <a class="navbar-brand" href="index.html">
                <i class="im-windows8 text-logo-element animated bounceIn"></i><span class="text-logo">可视化</span><span
                  class="text-slogan">平台</span>
              </a>
            </div>
            <!-- 上方导航菜单左侧 -->
            <nav class="top-nav" role="navigation">
              <ul class="nav navbar-nav pull-left"></ul>
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
                    <a href="javascript:void(0)">
                      <i class="im-pie but1" onclick="nodeGraph()"></i>
                      <span>几何关系图</span>
                    </a>
                  </div>
                </li>
                <li>
                  <div class="shortcut-button">
                    <a href="javascript:void(0)">
                      <i class="ec-images color-dark but2"></i>
                      <span>尺寸关系图</span>
                    </a>
                  </div>
                </li>
                <li>
                  <div class="shortcut-button">
                    <a href="#">
                      <i class="en-light-bulb color-orange but3"></i>
                      <span>三维展示</span>
                    </a>
                  </div>
                </li>
                <li>
                  <div class="shortcut-button">
                    <a href="#">
                      <i class="ec-link color-blue but4" onclick="showFeature()"></i>
                      <span>特征关系图</span>
                    </a>
                  </div>
                </li>
                <li>
                  <div class="shortcut-button">
                    <a href="#">
                      <i class="ec-support color-red but5"></i>
                      <span>设计意图</span>
                    </a>
                  </div>
                </li>
                <li>
                  <div class="shortcut-button">
                    <a href="#">
                      <i class="st-lock color-teal but6"></i>
                      <span>知识图谱</span>
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
            <div class="special1 col-lg-5 col-md-5 sortable-layout resizable" id="one" style="display: none">
              <!-- Start col-lg-5 -->
              <div class="panel panel-teal panel1">
                <!-- Start .panel -->
                <div class="panel-heading" id="beforenodeContainer">
                  <h4 class="panel-title"><i class="im-bars"></i>几何关系图</h4>
                </div>
                <div id="nodeContainer" class="panel-body" style="padding: 0px"></div>
                <div class="panel-footer" id="afternodeContainer">
                  面板脚注
                  <button id="button1">刷新</button>
                </div>
                <div>
                  <div id="rightMenu">
                    <ul>
                      <li>open</li>
                      <li>hide</li>
                      <li>show</li>
                      <li>addMark</li>
                      <li>aggregation</li>
                      <li>spread</li>
                    </ul>
                    <div id="showAttributes"></div>
                  </div>
                </div>
              </div>
              <!-- End .panel -->
            </div>
            <!-- End col-lg-5 -->
            <div class="col-lg-5 col-md-5 sortable-layout" id="two" style="display: none">
              <!-- Start col-lg-5 -->
              <div class="panel panel-primary">
                <!-- Start .panel -->
                <div class="panel-heading">
                  <h4 class="panel-title"><i class="im-bars"></i>尺寸关系图</h4>
                </div>
                <div class="panel-body">panel-body</div>
                <div class="panel-footer">面板脚注</div>
              </div>
              <!-- End .panel -->
            </div>
            <div class="special3 col-lg-5 col-md-5 sortable-layout" id="three" style="display: none">
              <!-- Start col-lg-5 -->
              <div class="panel panel-brown">
                <!-- Start .panel -->
                <div class="panel-heading">
                  <h4 class="panel-title">
                    <i class="im-bars"></i>三维展示
                    <button class="button show_anno">显示标注</button>
                    <button class="button hide_anno">隐藏标注</button>
                  </h4>
                </div>
                <!-- container 这个DIV用于显示三维模型 -->
                <div id="container" class="panel-body"></div>
                <div id="3dfooter" class="panel-footer"></div>
                <div>
                  <div id="rightMenu3">
                    <ul>
                      <li>open</li>
                      <li>hide</li>
                      <li>relation</li>
                      <li>aggregation</li>
                    </ul>
                    <div id="showAttributes3"></div>
                  </div>
                </div>
              </div>
              <!-- End .panel -->
            </div>
            <div class="col-lg-5 col-md-5 sortable-layout" id="four" style="display: none">
              <!-- Start col-lg-5 -->
              <div class="panel panel-success">
                <!-- Start .panel -->
                <div class="panel-heading" id="featureNodeContainer">
                  <h4 class="panel-title"><i class="im-bars"></i>特征关系图</h4>
                </div>
                <div id="featureContainer" class="panel-body" style="padding: 0px"></div>
                <div class="panel-footer">
                  面板脚注
                  <!-- <button id="button4">刷新</button> -->
                </div>
                <div>
                  <div id="rightMenu4">
                    <ul>
                      <li id="rightMenu4_open">open</li>
                      <li>hide</li>
                      <li>relation</li>
                      <li>aggregation</li>
                      <li>cancel_relation</li>
                    </ul>
                    <!-- 特征类型 -->
                    <div name="feature_desc" class="feature_desc_con" id="feature_desc">
                      <form action="#" method="get">
                        <div id="feature_one" class="feature_cla">
                          <div class="left_feature_cla">
                            <input id="hole" name="feature" type="radio" />孔类
                          </div>
                          <div class="right_feature_cla">
                            <span class="val_cla">通孔(<span id="through_hole">0</span>)
                            </span>
                            <span class="val_cla">盲孔(<span id="blind_hole">0</span>)
                            </span>
                          </div>
                        </div>
                        <div id="feature_two" class="feature_cla">
                          <div class="left_feature_cla">
                            <input id="convex_plate" name="feature" type="radio" />凸台
                          </div>
                          <div class="right_feature_cla">
                            <span class="val_cla">一般凸台(<span id="normal_convex_plate">0</span>)
                            </span>
                            <span class="val_cla">立方凸台(<span id="cube_convex_plate">0</span>)
                            </span>
                            <span class="val_cla">圆柱凸台(<span id="cylinder_convex_plate">0</span>)
                            </span>
                          </div>
                        </div>
                        <div id="feature_three" class="feature_cla">
                          <div class="left_feature_cla">
                            <input id="groove" name="feature" type="radio" />槽类
                          </div>
                          <div class="right_feature_cla">
                            <span class="val_cla">开槽(<span id="open_groove">0</span>)
                            </span>
                            <span class="val_cla">T型槽(<span id="T_groove">0</span>)
                            </span>
                            <span class="val_cla">直方槽(<span id="rectangular_groove">0</span>)
                            </span>
                            <span class="val_cla">V型槽(<span id="V_groove">0</span>)
                            </span>
                            <span class="val_cla">通槽(<span id="through_groove">0</span>)
                            </span>
                            <span class="val_cla">燕槽(<span id="swallowing_groove">0</span>)
                            </span>
                          </div>
                        </div>
                        <div id="feature_four" class="feature_cla">
                          <div class="left_feature_cla">
                            <input id="plane" name="feature" type="radio" />平面类
                          </div>
                          <div class="right_feature_cla">
                            <span class="val_cla">台阶(<span id="step_plane">0</span>)
                            </span>
                            <span class="val_cla">平面(<span id="sub_plane">0</span>)
                            </span>
                          </div>
                        </div>
                        <div id="feature_five" class="feature_cla">
                          <div class="left_feature_cla">
                            <input id="transition" name="feature" type="radio" />过渡特征
                          </div>
                          <div class="right_feature_cla">
                            <span class="val_cla">倒角(<span id="chamfer">0</span>)
                            </span>
                            <span class="val_cla">圆角(<span id="fillet">0</span>)
                            </span>
                          </div>
                        </div>
                        <div id="feature_six" class="feature_cla">
                          <div class="left_feature_cla">
                            <input id="complex" name="feature" type="radio" />复杂特征
                          </div>
                          <div class="right_feature_cla">
                            <span class="val_cla">阶梯孔(<span id="ladder_hole">0</span>)
                            </span>
                            <span class="val_cla">阶梯槽(<span id="ladder_groove">0</span>)
                            </span>
                          </div>
                        </div>
                      </form>
                    </div>
                    <div id="showAttributes4"></div>
                  </div>
                </div>
              </div>
              <!-- End .panel -->
            </div>
            <div class="col-lg-5 col-md-5 sortable-layout" id="five" style="display: none">
              <!-- Start col-lg-5 -->
              <div class="panel panel-warning">
                <!-- Start .panel -->
                <div class="panel-heading">
                  <h4 class="panel-title" style="font-size: 30px; font-weight: bold">
                    <i class="im-bars"></i>design intent
                    <!-- <button class="nextStep button" onclick="showDesignHistory(designStep)">one_step</button> -->
                    <!-- <button class="button" onclick="showAllDesignHistory()">all</button> -->
                    <!--<button class="button" onclick="removeAllDesignHistory()">清空</button>-->
                  </h4>
                </div>
                <div>
                  <div class="design_body" id="design">
                    <div class="left-bottons">
                      <button class="leftbuttons" id="buttona" style="width: 60px">
                        规则写入
                      </button>
                      <button class="leftbuttons" id="buttonb" style="width: 60px">
                        规则挖掘
                      </button>
                      <button class="leftbuttons" id="buttonc" style="width: 60px">
                        约束尺寸
                      </button>
                      <button class="leftbuttons" id="buttond" style="width: 60px">
                        特征信息
                      </button>
                      <button class="leftbuttons" id="buttone" style="width: 60px">
                        设计历史
                      </button>
                    </div>
                    <div class="right-show">
                      <div id="show1">
                        <div class="inputtext" style="padding: 30px 0px 0px 10px">
                          <textarea id="inputrule" style="
                            font-size: 25px;
                            vertical-align: top;
                            width: 95%;
                            height: 95%;
                            resize: none;
                          " placeholder="请输入规则"></textarea>
                        </div>
                        <div class="importtolib">
                          <button class="buttons left-botton" id="leftbutton">
                            本地规则库
                          </button>
                          <button class="buttons right-botton" id="rightbutton">
                            远程规则库
                          </button>
                        </div>
                      </div>
                      <div id="show2">
                        <div class="warning">
                          <div>
                            提示：系统将使用增强学习进行规则挖掘，请输入相应状态空间并耐心等候！
                          </div>
                        </div>
                        <div class="inputtexts">
                          <form>
                            <p style="margin: 0px 65px">
                              <lable>起点空间状态:</lable>
                              <input id="inputstart" type="text" style="width: 45px; margin: 5px 15px" />
                              <lable>终点空间状态:</lable>
                              <input id="inputend" type="text" style="width: 45px; margin: 5px 15px" size="6" />
                              <button class="buttons left-botton" id="startRL" style="height: 40px">
                                开始
                              </button>
                            </p>
                          </form>
                        </div>
                        <div class="showrules">
                          <ul class="rule_ul">
                            <li class="rule_li">
                              DIMENSIONAL_CHARACTERISTIC_REPRESENTATION_
                            </li>
                            <li class="rule_li">
                              contains_DIMENSIONAL_LOCATION(?d,?l)∧
                            </li>
                            <li class="rule_li">DIMENSIONAL_LOCATION_</li>
                            <li class="rule_li">
                              _contains_SHAPE_ASPECT(?l,?s)∧
                            </li>
                            <li class="rule_li">SHAPE_ASPECT_contains^(-1)</li>
                            <li class="rule_li">
                              _GEOMETRIC_ITEM_SPECIFIC_USAGE(?s,?g)∧
                            </li>
                            <li class="rule_li">GEOMETRIC_ITEM_SPECIFIC_USAGE</li>
                            <li class="rule_li">
                              _contains_ADVANCED_FACE(?g,?a)∧
                            </li>
                            <li class="rule_li">
                              ADVANCED_FACE_contains^(-1)_CLOSED_SHELL(?a,?c)
                            </li>
                            <li class="rule_li">→</li>
                            <li class="rule_li">CLOSED_SHELL_has_PMI(?c)</li>
                          </ul>
                        </div>
                        <div class="importtolib">
                          <button class="buttons left-botton" id="storetolib">
                            存入规则库
                          </button>
                          <button class="buttons right-botton" id="abandom">
                            放弃规则库
                          </button>
                        </div>
                      </div>
                      <div id="show3">
                        <div class="topbuttons">
                          <button class="buttons left-botton" id="weidu">
                            维度约束
                          </button>
                          <button class="buttons right-botton" id="zhuangpei">
                            装配约束
                          </button>
                        </div>
                        <div class="weidulimit">
                          <ul class="box" id="box1"></ul>
                          <button class="go" id="go" style="height: 40px; width: 100px">
                            三维关联
                          </button>
                        </div>
                        <div class="zhuangpeilimit">
                          <div class="textbox" style="flex: 0 1 auto">
                            <ul class="box" id="box2"></ul>
                          </div>
                          <p style="flex: 0 1 auto">库中类似文件:</p>
                          <div class="similarpics">
                            <div class="similarpic" style="width: 40%">
                              <img id="pic1" style="height: 140px; width: 100%" />
                            </div>
                            <div class="similarpic" style="width: 40%">
                              <img id="pic2" style="height: 140px; width: 100%" />
                            </div>
                            <div class="similarpic" style="width: 40%">
                              <img id="pic3" style="height: 140px; width: 100%" />
                            </div>
                          </div>
                        </div>
                      </div>
                      <div id="show4">
                        <div class="top">结果</div>
                        <div class="rest">
                          <ul class="show4box" id="show4ul" style="margin: 50px 0"></ul>
                        </div>
                      </div>
                      <div id="show5"></div>
                    </div>
                  </div>
                  <div class="panel-footer">面板脚注</div>
                </div>
                <!-- End .panel -->
              </div>
              <div class="col-lg-5 col-md-5 sortable-layout" id="six" style="display: none">
                <!-- Start col-lg-5 -->
                <div class="panel panel-info">
                  <!-- Start .panel -->
                  <div class="panel-heading">
                    <h4 class="panel-title"><i class="im-bars"></i>知识图谱</h4>
                  </div>
                  <div class="panel-body">panel-body</div>
                  <div class="panel-footer">面板脚注</div>
                </div>
                <!-- End .panel -->
              </div>
              <!-- End col-lg-6 -->
            </div>
            <div class="special3 col-lg-5 col-md-5 sortable-layout" id="seven" style="display: none">
              <!-- Start 二维pmi显示 -->
              <!-- Start col-lg-5 -->
              <div class="panel panel-brown">
                <!-- Start .panel -->
                <div class="panel-heading">
                  <h4 class="panel-title"><i class="im-bars"></i>语义展示</h4>
                </div>
                <div id="tree-container" class="panel-body"></div>
                <div id="3dfooter" class="panel-footer"></div>
                <!-- End .panel -->
              </div>
              <!-- End col-lg-5 -->
              <!-- End 二维pmi显示 -->
            </div>
            <!-- End .row -->
            <!-- Page End here -->
          </div>
          <!-- End .outlet -->
        </div>
        <!-- End .content-wrapper -->
        <div class="clearfix"></div>
        <!-- End #content -->
        <!-- Javascripts -->
        <!-- Load pace first -->
      </div>

      <!-- 三方库 -->
      <script src="assets/js/react.production.min.js"></script>
      <script src="assets/js/react-dom.production.min.js"></script>
      <script src="assets/js/lodash.js"></script>
      <script src="assets/js/babel.min.js"></script>

      <script src="assets/plugins/core/pace/pace.min.js"></script>
      <!-- Important javascript libs(put in all pages) -->
      <script src="assets/js/jquery-1.8.3.min.js"></script>
      <script>
        window.jQuery ||
          document.write(
            '<script src="assets/js/libs/jquery-2.1.1.min.js">\x3C/script>'
          );
      </script>
      <script src="assets/js/jquery-ui.js"></script>
      <script>
        window.jQuery ||
          document.write(
            '<script src="assets/js/libs/jquery-ui-1.10.4.min.js">\x3C/script>'
          );
      </script>
      <script src="http://www.jq22.com/jquery/jquery-1.10.2.js"></script>
      <script src="lib/jquery-ui.min.js"></script>
      <script src="lib/jquery.ui.touch-punch.min.js"></script>
      <script src="http://www.jq22.com/jquery/bootstrap-3.3.4.js"></script>
      <script src="dist/js/lobipanel.js"></script>

      <!-- Bootstrap plugins -->
      <script src="assets/js/bootstrap/bootstrap.js"></script>
      <!-- Core plugins ( not remove ever) -->
      <!-- Handle responsive view functions -->
      <script src="assets/js/jRespond.min.js"></script>
      <!-- Custom scroll for sidebars,tables and etc. -->
      <script src="assets/plugins/core/slimscroll/jquery.slimscroll.min.js"></script>
      <script src="assets/plugins/core/slimscroll/jquery.slimscroll.horizontal.min.js"></script>
      <!-- Resize text area in most pages -->
      <script src="assets/plugins/forms/autosize/jquery.autosize.js"></script>
      <!-- Proivde quick search for many widgets -->
      <script src="assets/plugins/core/quicksearch/jquery.quicksearch.js"></script>
      <!-- Bootbox confirm dialog for reset postion on panels -->
      <script src="assets/plugins/ui/bootbox/bootbox.js"></script>
      <!-- Other plugins ( load only nessesary plugins for every page) -->
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

      <script src="js/prettify.js"></script>
      <script src="ww/js/jquery.toolbar.js"></script>

      <!-- 当页面加载完成时就会执行以下script  -->
      <script>
        var designStep = 0;
        var designStr = "";
        $(function () {
          $("#buttona").click(function () {
            $("#show1").show();
            $("#show2").hide();
            $("#show3").hide();
            $("#show4").hide();
            $("#show5").hide();
            $("#buttona").css({ "background-color": "rgb(34, 243, 7)" });
            $("#buttonb").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonc").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttond").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttone").css({ "background-color": "rgb(241, 211, 35)" });
          });
        });
        $(function () {
          $("#buttonb").click(function () {
            $("#show1").hide();
            $("#show2").show();
            $("#show3").hide();
            $("#show4").hide();
            $("#show5").hide();
            $("#buttona").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonb").css({ "background-color": "rgb(34, 243, 7)" });
            $("#buttonc").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttond").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttone").css({ "background-color": "rgb(241, 211, 35)" });
          });
        });
        $(function () {
          $("#buttonc").click(function () {
            $("#show1").hide();
            $("#show2").hide();
            $("#show3").show();
            $("#show4").hide();
            $("#show5").hide();
            $("#buttona").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonb").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonc").css({ "background-color": "rgb(34, 243, 7)" });
            $("#buttond").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttone").css({ "background-color": "rgb(241, 211, 35)" });
            $(".zhuangpeilimit").hide();
            //约束信息界面维度约束表的展示，首先要清空原有的li，防止点一下按钮就重复生成
            $("#box1 li").remove();
            var li1 = $("<li>规则</li>");
            var li2 = $("<li>结果</li>");
            li1.appendTo("#box1");
            li2.appendTo("#box1");
            var li = new Array();
            li[0] = $("<li>CLOSED_SHELL_has_PMI(?#1060)</li>");
            li[1] = $("<li>传动轴具有PMI信息；</li>");
            li[2] = $(
              "<li>CLOSED_SHELL_has_CYLINDRICITY_TOLERANCE(?#1060?#30)</li>"
            );
            li[3] = $("<li>传动轴具有圆柱度约束信息；</li>");
            li[4] = $(
              "<li>CLOSED_SHELL_has_CIRCULAR_RUNOUT_TOLERANCE(?#1060?#22)</li>"
            );
            li[5] = $("<li>传动轴具有圆跳动约束信息；</li>");
            li[6] = $(
              "<li>CLOSED_SHELL_has_LINEAR_PMI(?#1060?#62)\nLINEAR_PMI_relation_ADVANCED_FACE(?#62,?#1039)\nLINEAR_PMI_relation_ADVANCED_FACE(?#62,?#1037)\nSPECIAL_TYPE_has_LINEAR_PMI_VALUE(?#77?12)</li>"
            );
            li[7] = $(
              "<li>传动轴具有PMI线性尺寸信息，该线性尺寸信息关联面为#1039和#1037面，且该线性尺寸信息值为12mm；</li>"
            );
            li[8] = $(
              "<li>CLOSED_SHELL_has_RADIAL_PMI(?#1060?#47)\nRADIAL_PMI_relation_ADVANCED_FACE(?#47,?#1030)\nSPECIAL_TYPE_has_RADIAL_PMI_VALUE(?#80?55)</li>"
            );
            li[9] = $(
              "<li>传动轴具有PMI径向尺寸信息，该径向尺寸信息关联面为#1030，且径向尺寸为55mm</li>"
            );
            for (var i = 0; i < li.length; i++) {
              li[i].appendTo("#box1");
            }
          });
        });
        $(function () {
          $("#buttond").click(function () {
            $("#show1").hide();
            $("#show2").hide();
            $("#show3").hide();
            $("#show4").show();
            $("#show5").hide();
            $("#buttona").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonb").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonc").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttond").css({ "background-color": "rgb(34, 243, 7)" });
            $("#buttone").css({ "background-color": "rgb(241, 211, 35)" });
            $("#show4ul li").remove();
            var li1 = $("<li>分析对象</li>");
            var li2 = $("<li>分析结果</li>");
            li1.appendTo("#show4ul");
            li2.appendTo("#show4ul");
            var li = new Array();
            li[0] = $("<li>传动轴</li>");
            li[1] = $(
              '<li><button class="show4ul_button">不规则槽特征</button></br><button class="show4ul_button">不规则开槽特征</button></br><button class="show4ul_button">圆柱凸台特征</button></li>'
            );
            for (var i = 0; i < li.length; i++) {
              li[i].appendTo("#show4ul");
            }
          });
        });
        $(function () {
          $("#buttone").click(function () {
            $("#show1").hide();
            $("#show2").hide();
            $("#show3").hide();
            $("#show4").hide();
            $("#show5").show();
            $("#buttona").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonb").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttonc").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttond").css({ "background-color": "rgb(241, 211, 35)" });
            $("#buttone").css({ "background-color": "rgb(34, 243, 7)" });
            $("#show5 div").remove();
            var div1 = $(
              '<div class="results"><div style="text-align:center;font-size:20px;flex:0 1 auto;">可能结果1</div><div class="resultbuttons"><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">不规则槽特征-切除拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">凸台-拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">不规则开槽特征-切除拉伸</button></div></div>'
            );
            div1.appendTo("#show5");
            var div2 = $(
              '<div class="results"><div style="text-align:center;font-size:20px;flex:0 1 auto;">可能结果2</div><div class="resultbuttons"><button class="resultbutton">草图-旋转</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">不规则槽特征-切除拉伸</button><div class="arrows"><img class="arrowimg" src="./images/right_arrow.png" /></div><button class="resultbutton">不规则开槽特征-切除拉伸</button></div></div>'
            );
            div2.appendTo("#show5");
          });
        });
        $(function () {
          $("#weidu").click(function () {
            $(".zhuangpeilimit").hide();
            $(".weidulimit").show();
          });
        });
        $(function () {
          $("#zhuangpei").click(function () {
            $(".zhuangpeilimit").show();
            $(".weidulimit").hide();
            //var picurl = "url(./images/limit/4.png)";
            //$('#pic1').css({"background":picurl});
            //var picurll = "./images/limit/4.png";
            $("#pic1").attr("src", "./images/limit/chilun.png");
            $("#pic2").attr("src", "./images/limit/jian.png");
            $("#pic3").attr("src", "./images/limit/zhoucheng.png");
            $("#box2 li").remove();
            var li1 = $("<li>可能装配零件可能装配零件</li>");
            var li2 = $("<li>装配关系</li>");
            li1.appendTo("#box2");
            li2.appendTo("#box2");
            var li = new Array();
            li[0] = $("<li>齿轮</li>");
            li[1] = $("<li>接触、同心</li>");
            li[2] = $("<li>键</li>");
            li[3] = $("<li>接触</li>");
            li[4] = $("<li>轴承</li>");
            li[5] = $("<li>接触，同心</li>");
            for (var i = 0; i < li.length; i++) {
              li[i].appendTo("#box2");
            }
          });
        });
        $(document).ready(function (e) {
          $("#show2").hide();
          $("#show3").hide();
          $("#show4").hide();
          $("#show5").hide();
          $("#buttona").css({ "background-color": "rgb(34, 243, 7)" });

          $(".but1").click(function (e) {
            $("#one").toggle();
          });
          $(".but2").click(function (e) {
            $("#two").toggle();
          });

          $(".but3").click(function (e) {
            $("#three").toggle();
            $("#seven").toggle();
          });
          $(".but4").click(function (e) {
            $("#four").toggle();
          });
          $(".but5").click(function (e) {
            $("#five").toggle();
          });
          $(".but6").click(function (e) {
            $("#six").toggle();
          });

          $('div[data-toolbar="transport-options"]').toolbar({
            content: "#transport-options",
            position: "top",
          });

          $('div[data-toolbar="transport-options-o"]').toolbar({
            content: "#transport-options-o",
            position: "bottom",
            event: "click",
            hideOnClick: true,
          });

          $('div[data-toolbar="style-option"]').toolbar({
            content: "#transport-options",
            position: "bottom",
            style: "primary",
          });

          $('div[data-toolbar="set-01"]').toolbar({
            content: "#set-01-options",
            position: "top",
          });

          $('div[data-toolbar="set-02"]').toolbar({
            content: "#set-02-options",
            position: "left",
          });

          $('div[data-toolbar="set-03"]').toolbar({
            content: "#set-03-options",
            position: "bottom",
          });

          $('div[data-toolbar="set-04"]').toolbar({
            content: "#set-04-options",
            position: "right",
          });
          $(".show_anno").click(function (e) {
            //三位展示div中的显示标注按钮
            show_anno();
          });
          $(".hide_anno").click(function (e) {
            //三位展示div中的显示标注按钮
            remove_anno();
          });
          $(".nextStep").click(function () {
            designStep++;
          });
        });
      </script>

      <!-- 二维节点显示的js -->
      <script src="myjs/nodeGraph.js"></script>
      <script src="myjs/d3_test.js"></script>
      <script src="myjs/rightButton.js"></script>
      <script src="myjs/StepNode.js"></script>
      <script src="myjs/attributeShow.js"></script>
      <script type="text/javascript" src="myjs/myLayer.js"></script>

      <!-- 三维显示的js -->
      <script src="my-threejs/js/libs/three.min.js"></script>
      <script src="my-threejs/js/libs/dat.gui.js"></script>
      <script src="my-threejs/js/libs/OrbitControls.js"></script>
      <script src="my-threejs/js/libs/DragControls.js"></script>
      <script src="my-threejs/js/libs/THREE.MeshLine.js"></script>
      <script src="my-threejs/js/libs/gl-matrix-min.js"></script>
      <script src="my-threejs/js/libs/gl-matrix.js"></script>
      <script src="my-threejs/js/utils.js" type="text/babel"></script>
      <script src="my-threejs/js/annotation.js" type="text/babel"></script>
      <script src="my-threejs/js/geom.js" type="text/babel"></script>
      <script src="my-threejs/js/lunkuo.js" type="text/babel"></script>
      <script src="my-threejs/js/scene.js" type="text/babel"></script>
      <script src="my-threejs/js/drawface.js" type="text/babel"></script>
      <script src="my-threejs/js/MeshClick.js" type="text/babel"></script>
      <script src="my-threejs/js/main-graph.js" type="text/babel"></script>
      <script src="my-threejs/js/pmi-info.js" type="text/babel"></script>
      <!-- <script type="text/babel">
        const e = React.createElement;

        class PMITree extends React.Component {
          constructor(props) {
            super(props);
            this.state = { liked: false };
          }

          render() {
            if (this.state.liked) {
              return 'You liked this.';
            }

            return (
              <button onClick={() => this.setState({ liked: true })}>
                123{this.props.name}
              </button>
            );
          }
        }
        window.onload = function () {
          var data = "123"

          const domContainer = document.querySelector('#pmi-tree');

          ReactDOM.render(<PMITree name="Sara" />, domContainer);

        }

      </script> -->

      <script>
        function nodeGraph() {
          showBaseNodeGraph();
          showGeometryNodeGraph(500, 500);
          $("#button1").click(function () {
            refresh();
          });
        }
      </script>

      <script>
        function showFeature() {
          //showBaseNodeGraph();
          showFeatureNodeGraph();
          $("#button4").click(function () {
            //refresh();
          });
        }
      </script>

      <script>
        function test() {
          showBaseNodeGraph();
          quickNodeGraph(500, 500);
          alert("到测试这里了！");
        }
        function test1() {
          console.log("ok");
        }
      </script>

      <script>
        //监听div大小变化
        (function ($, h, c) {
          var a = $([]),
            e = ($.resize = $.extend($.resize, {})),
            i,
            k = "setTimeout",
            j = "resize",
            d = j + "-special-event",
            b = "delay",
            f = "throttleWindow";
          e[b] = 250;
          e[f] = true;
          $.event.special[j] = {
            setup: function () {
              if (!e[f] && this[k]) {
                return false;
              }
              var l = $(this);
              a = a.add(l);
              $.data(this, d, {
                w: l.width(),
                h: l.height(),
              });
              if (a.length === 1) {
                g();
              }
            },
            teardown: function () {
              if (!e[f] && this[k]) {
                return false;
              }
              var l = $(this);
              a = a.not(l);
              l.removeData(d);
              if (!a.length) {
                clearTimeout(i);
              }
            },
            add: function (l) {
              if (!e[f] && this[k]) {
                return false;
              }
              var n;
              function m(s, o, p) {
                var q = $(this),
                  r = $.data(this, d);
                r.w = o !== c ? o : q.width();
                r.h = p !== c ? p : q.height();
                n.apply(this, arguments);
              }
              if ($.isFunction(l)) {
                n = l;
                return m;
              } else {
                n = l.handler;
                l.handler = m;
              }
            },
          };
          function g() {
            i = h[k](function () {
              a.each(function () {
                var n = $(this),
                  m = n.width(),
                  l = n.height(),
                  o = $.data(this, d);
                if (m !== o.w || l !== o.h) {
                  n.trigger(j, [(o.w = m), (o.h = l)]);
                }
              });
              g();
            }, e[b]);
          }
        })(jQuery, this);

        $("#container").resize(function () {
          onWindowResize();
        });
      </script>

      <script type="text/javascript">
        $(function () {
          $(".panel").lobiPanel({
            // 配置参数
            sortable: true,
            minWidth: 300,
            minHeight: 300,
            maxWidth: 1200,
            maxHeight: 1200,
            unpin: {
              tooltip: "拖动",
            },
            reload: {
              tooltip: "重新加载",
            },
            minimize: {
              tooltip: "最小化",
            },
            close: {
              tooltip: "关闭",
            },
            editTitle: {
              tooltip: "停止编辑",
            },
            expand: {
              tooltip: "全屏",
            },
          });
          //$('.lobipanel').lobiPanel('enableDrag');
          //$('.lobipanel').lobiPanel('enableResize');
        });
      </script>
    </body>

    </html>