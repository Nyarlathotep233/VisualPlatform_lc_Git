<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
var VIEW_FRAME;

function setViewer(v) {VIEW_FRAME=v;}

function getGeomView() {return VIEW_FRAME.getGeomView();}

//返回了一个webgl 声明了各类鼠标方法
function getView() {
  return getGeomView().getView();
}

function redrawView() {VIEW_FRAME.draw();}

function syncForm() {
    //上方表单中所有的控件 设定比例值
    var form = document.controls;
    form.zoom.value = getView().getZoom();
//    form.edlinks.checked =  getGeomView().getScenegraph().isEnabledEditLinks();
}

function zoom_view(e) {
  var zoom = parseFloat(e.form.zoom.value);
  if (isNaN(zoom))
    throw new Error ("Could not parse zoom as number: " +zoom);
  getView().setZoom(zoom);
  redrawView();
}

function zoom_in(e) {
  var zoom = getView().getZoom() * 1.5;
  getView().setZoom(zoom);

  syncForm();
  redrawView();
}

function zoom_out(e) {
  var zoom = getView().getZoom() / 1.5;
  getView().setZoom(zoom);

  syncForm();

  redrawView();
}

function mode_change(m) {
  getGeomView().setMouseMode(m);
}

function save_state() {
  getGeomView().getScenegraph().saveState();
}

//function toggle_edit_links(cb) {
//  var val = cb.checked;
//  getGeomView().getScenegraph().enableEditLinks(val);
//}

function help_popup() {
  console.log ("Doing popul");
  var win = window.open("help-save.html", "help", 
                      "height=600,width=500,scrollbars");
  win.focus();
}

</script>

</head>

<body onload="document.controls.reset()">

<form name="controls" onsubmit="return false;">
  <table width="100%">
    <tr>
      <td>
        <div style="padding-right: 1em;">
          <input type="radio" name="mode" value="rotate" onchange="mode_change('rotate')" checked>Rotate
          <input type="radio" name="mode" value="pan" onchange="mode_change('pan')"> Translate
        <!--   <input type="radio" name="mode" value="pick" onchange="mode_change('pick')">选择 -->
        </div>
      </td>
      <td>
        <div style="padding-right: 1em;">
          size: <input name="zoom" type="text" size=5 onchange="zoom_view(this);" 
          value="1.0" > 
          <input type="button" value="+" onClick='zoom_in(this);'>
          <input type="button" value="-" onClick='zoom_out(this);'>
        </div>
      </td>
      <td>
        <input type="button" value="Save state" onClick="save_state();">
      </td>
    </tr>
  </table>
</form>

</body>
</html>