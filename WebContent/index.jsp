<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>step模型展示</title>
<script src="js/sti_utils.js"> </script>
<script>

function init() {
  
    var view_frame = window.view;   //右方的frame
    var ctl_frame = window.control; //上方的frame
    var asm_frame = window.assembly;//左方的frame

    if (!view_frame)
        throw new Error ("No view frame");
    
    //调用asm-control.html中的setViewer()方法 使得VIEW_FRAME=view_frame即右方的frame
    ctl_frame.setViewer(view_frame);

    //调用asm-view3d.html的setup_page()方法 传入三个参数asm_frame, params, init_fn
    //asm_frame是左方的frame
    //params 调用sti_utils.js中的parse_search(s, def)方法 
    //location.search是从当前URL的?号开始的字符串 地址栏中的1.xml
    //init_fn:初始化右方和上方的frame
    //view_frame.draw()把右方的frame画出来
    //调用asm-control.html中的syncForm()方法

    view_frame.setup_page(asm_frame, parse_search(location.search),
      function(view) {
        view_frame.draw();
        ctl_frame.syncForm();
      });
}


function saveComplete(path) {
   alert ("Saving state at" +path);
}

/* Needed to work-around a race condition in Chrome, where the frame sizes 
 * may not be initialized after onload (as the docs say it should)
 */
window.addEventListener("load", function() {setTimeout(init, 100);}, false);

</script>
</head>

<frameset cols="50%, *" >
  
  <frameset rows="55, *">
    <frame name="control" src="control.jsp">
    <frame id="a" name="assembly" src="tree.jsp">
  </frameset>
  <frame name="view" src="view3d.jsp" 
       marginwidth="0" marginheight="0" scrolling="no">

</frameset>
</html>