"use strict";
//------------------------------重要相关参数说明---------------------
//shellindex =i就是第i个shell
////kf1:面1，kf2：面2， v点集合，faces：面集合
///geogroup:把几何体 打成一组
///linegroup:把每个物体打成一组
var material = new THREE.MeshLambertMaterial({
  vertexColors: THREE.VertexColors,
  side: THREE.DoubleSide,
});
var line_material1 = new THREE.LineBasicMaterial({ color: 0xff0000 });
var lunkuo_material = new THREE.LineBasicMaterial({
  color: 0x000000,
  transparent: true,
  opacity: 0.5,
});
var container = document.getElementById("container");
//console.log(container.clientWidth)
var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera(
  60,
  window.innerWidth / 2 / window.innerHeight,
  1,
  10000
);
camera.position.set(-1000, 600, 0); //z视角的位置（x,y,z）
var frustumSize = 1000;
var renderer;
initRenderer();
initLight();
container.appendChild(renderer.domElement);
var controls = new THREE.OrbitControls(camera, renderer.domElement); //鼠标控制
var resolution = new THREE.Vector2(window.innerWidth, window.innerHeight);

var geogroup = new Array(); //几何体
var linegroup = new Array(); //轮廓
var alllinegeom = new THREE.Geometry();
var annogroup = new Array(); //标注的组
var facegroup = new Array(); //面
var objects = new Array();

//------------读地址栏url，并自动解析
var c = window.location.href;
c = c.split("?");
var base = c[1].split("/");
//原本是根据地址栏url读取的  现在先写死固定显示,注意同时也要改下面43行xmlDoc.load(c[1]);里的c这个参数
//var c="my-threejs/123/index.xml";
//var base=c.split("/");

//----------------------开始读取index.xml 文件
// var xmlDoc=document.implementation.createDocument("","",null);
// xmlDoc.async=false;
// xmlDoc.load(c[1]);
function LoadXMLFile(xmlFile) {
  var xmlDom = null;
  if (window.ActiveXObject) {
    xmlDom = new ActiveXObject("Microsoft.XMLDOM");
    //xmlDom.loadXML(xmlFile);//如果用的是XML字符串
    xmlDom.load(xmlFile); //如果用的是xml文件。
  } else if (
    document.implementation &&
    document.implementation.createDocument
  ) {
    var xmlhttp = new window.XMLHttpRequest();
    xmlhttp.open("GET", xmlFile, false);
    xmlhttp.send(null);
    xmlDom = xmlhttp.responseXML.documentElement; //一定要有根节点(否则google浏览器读取不了)
  } else {
    xmlDom = null;
  }
  return xmlDom;
}
var xmlDoc = LoadXMLFile(c[1]);
console.log("LoadXMLFile(c[1])", xmlDoc);

var xshell = xmlDoc.getElementsByTagName("shell");
var shapes = xmlDoc.getElementsByTagName("shape");
var childOfShape0 = shapes[0].getElementsByTagName("child");
var map1 = []; //存放shape[0]child标签内的ref和xform的对应关系
for (var i = 0; i < childOfShape0.length; i++) {
  var a = childOfShape0[i].getAttribute("ref");
  var b = childOfShape0[i].getAttribute("xform");
  var shuz = [a, b];
  map1.push(shuz);
}

var map2 = []; //----------------2*3的数组:ref，shell的id和其对应的xml文件,以及对应的annotation的id
for (var j = 1; j < shapes.length; j++) {
  var ref = shapes[j].getAttribute("id");
  var child = shapes[j].getElementsByTagName("child");
  for (var s = j; s <= j + child.length; s++) {
    if (
      shapes[s] != null &&
      shapes[s].getAttribute("shell") != null &&
      shapes[s].getAttribute("shell").length < 20
    ) {
      var shell_id = shapes[s].getAttribute("shell");
      var annotation_id = shapes[s].getAttribute("annotation");
      var shell_url = "shell_" + shell_id + ".xml";
      map2.push([ref, shell_id, shell_url, annotation_id]);
    } else continue;
  }
  j = j + child.length;
}

for (
  var i = 0;
  i < map1.length;
  i++ //ref,xform,shell_id
) {
  for (var j in map2) {
    if (map1[i][0] == map2[j][0]) map1[i].push(map2[j][1]);
  }
}

var xform = [];
for (var x = 0; x < map1.length; x++) {
  var xxx = map1[x][1].split(" ");
  xform.push(xxx);
}

var products = xmlDoc.getElementsByTagName("product");
var annotations = xmlDoc.getElementsByTagName("annotation"); //开始读annotation标签
var num = 0;
/*
 * 原来这里传的是固定的shell,以下动态的将第一个shell的名称取出并赋值
 */
var shellId = xshell[0].getAttribute("id");
var shellname = "shell_" + shellId;
//console.log(shellname)
initGeoGroup(); //画几何体
initLineGroup(); //画所有面的轮廓

var faceline = new THREE.Group();
//drawfaceline([16,17,18,19],shellname);//画某些面的轮廓
var shell_id = shellname.split("_")[1];
console.log("shellname", shellname);
// drawface([12, 13, 14, 15, 16, 17], shellname, "381154"); //画某些面
drawface([0, 1, 2, 3], shellname, "381154"); //画某些面
//redrawGeo(shellname);
//console.log(objects)

render();
onWindowResize();
window.addEventListener("resize", onWindowResize);
var dragControls = new THREE.DragControls(objects, camera, renderer.domElement);
dragControls.addEventListener("dragstart", function (event) {
  controls.enabled = false;
});
dragControls.addEventListener("dragend", function (event) {
  controls.enabled = true;
});
document.getElementById("3dfooter").innerHTML =
  "零件个数:" + geogroup.length + "<br>" + "标注个数：" + annotations.length;
//console.log(geogroup)

//----------------加载结束

function onWindowResize() {
  var w = container.clientWidth;
  var h = container.clientHeight;
  var aspect = w / h;
  camera.left = (-frustumSize * aspect) / 2;
  camera.right = (frustumSize * aspect) / 2;
  camera.top = frustumSize / 2;
  camera.bottom = -frustumSize / 2;
  camera.updateProjectionMatrix();
  renderer.setSize(w, h);
  resolution.set(w, h);
}

function transform(xform, vec) {
  //----------------根据装配关系处理点坐标
  var inv = mat4.create();
  mat4.inverse(xform, inv);
  var xfomr = inv;
  var inv2 = mat4.create();
  mat4.inverse(xfomr, inv2);
  mat4.multiplyVec3(inv2, vec);
  return vec;
}
function show_anno() {
  if (annotations && annogroup.length == 0) show_annotation(annotations);
}
function remove_anno() {
  if (annogroup.length > 0) {
    for (var a = 0; a < annogroup.length; a++) scene.remove(annogroup[a]);
    annogroup.length = 0;
  }
  //console.log(annogroup.length)
}
