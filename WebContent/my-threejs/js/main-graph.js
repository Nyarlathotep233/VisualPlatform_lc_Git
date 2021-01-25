// ------------------------------重要相关参数说明---------------------
// shellindex =i就是第i个shell
// kf1:面1，kf2：面2， v点集合，faces：面集合
// geogroup:把几何体 打成一组
// linegroup:把每个物体打成一组
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
var container = document.getElementById('container');
// console.log(container.clientWidth)
var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera(
  60,
  window.innerWidth / 2 / window.innerHeight,
  1,
  10000,
);
camera.position.set(-1000, 600, 0); // z视角的位置（x,y,z）
var frustumSize = 1000;
var renderer;
initRenderer();
initLight();
container.appendChild(renderer.domElement);
var controls = new THREE.OrbitControls(camera, renderer.domElement); // 鼠标控制
var resolution = new THREE.Vector2(window.innerWidth, window.innerHeight);

var geogroup = new Array(); // 几何体
var linegroup = new Array(); // 轮廓
var alllinegeom = new THREE.Geometry();
var annogroup = new Array(); // 标注的组
var facegroup = new Array(); // 面
var objects = new Array();

// ------------读地址栏url，并自动解析
var fileName = window.location.href.split('?')[1];
var base = fileName.split('/');

// ----------------------开始读取index.xml 文件
var xmlDoc = window.LoadXMLFile(fileName);

var xshell = xmlDoc.getElementsByTagName('shell');
var shapes = xmlDoc.getElementsByTagName('shape');
var childOfShape0 = shapes[0].getElementsByTagName('child');
var map1 = []; // 存放shape[0]child标签内的ref和xform的对应关系
for (var i = 0; i < childOfShape0.length; i++) {
  var ref = childOfShape0[i].getAttribute('ref');
  var xform = childOfShape0[i].getAttribute('xform');
  map1.push([ref, xform]);
}

var map2 = []; // ----------------2*3的数组:ref，shell的id和其对应的xml文件,以及对应的annotation的id
for (var j = 1; j < shapes.length; j++) {
  var ref = shapes[j].getAttribute('id');
  var child = shapes[j].getElementsByTagName('child');
  for (var s = j; s <= j + child.length; s++) {
    if (
      shapes[s] != null
      && shapes[s].getAttribute('shell') != null
      && shapes[s].getAttribute('shell').length < 20
    ) {
      var shellId = shapes[s].getAttribute('shell');
      var annotation_id = shapes[s].getAttribute('annotation');
      var shell_url = `shell_${shellId}.xml`;
      console.log('shellId', shellId);
      map2.push([ref, shellId, shell_url, annotation_id]);
    } else continue;
  }
  j += child.length;
}

for (
  var i = 0;
  i < map1.length;
  i++ // ref,xform,shellId
) {
  for (var j in map2) {
    if (map1[i][0] == map2[j][0]) map1[i].push(map2[j][1]);
  }
}

var xform = [];
for (var x = 0; x < map1.length; x++) {
  var xxx = map1[x][1].split(' ');
  xform.push(xxx);
}

var products = xmlDoc.getElementsByTagName('product');
var annotations = xmlDoc.getElementsByTagName('annotation'); // 开始读annotation标签
var num = 0;
/*
 * 原来这里传的是固定的shell,以下动态的将第一个shell的名称取出并赋值
 */
// xshell:[shell#id388, shell#id386, shell#id391, shell#id392, shell#id390, shell#id389, shell#id387, shell#id393]

initGeoGroup(); // 画几何体
var targetShellId = xshell[0].getAttribute('id');
var targetShellName = `shell_${targetShellId}`;
initLineGroup(targetShellName); // 画所有面的轮廓  TODO:这里targetShellName的作用是什么？

var faceline = new THREE.Group();
// drawfaceline([16,17,18,19],targetShellName);//画某些面的轮廓  （？）
drawface([0], targetShellName, '381154'); // 高亮某些面
// redrawGeo(targetShellName);

render();
onWindowResize();
window.addEventListener('resize', onWindowResize);
var dragControls = new THREE.DragControls(objects, camera, renderer.domElement);
dragControls.addEventListener('dragstart', (event) => {
  controls.enabled = false;
});
dragControls.addEventListener('dragend', (event) => {
  controls.enabled = true;
});
document.getElementById('3dfooter').innerHTML = `零件个数:${geogroup.length}<br>` + `标注个数：${annotations.length}`;
// console.log(geogroup)

// ----------------加载结束

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
  // ----------------根据装配关系处理点坐标
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
  // console.log(annogroup.length)
}
