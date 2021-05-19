// ------------------------------重要相关参数说明---------------------
// shellindex =i就是第i个shell
// kf1:面1，kf2：面2， v点集合，faces：面集合
// geogroup:把几何体 打成一组
// linegroup:把每个物体打成一组
var material = new THREE.MeshPhongMaterial({
  vertexColors: THREE.VertexColors,
  side: THREE.DoubleSide,
  specular: 0x4488ee,
  shininess: 8,
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
var facegroup = new Array(); // 面 TODO：作用是什么？
var objects = new Array(); // TODO：作用是什么？
var highLightFaceIndex = new Array(); // 目前高亮的面的序号
var highLightFaceList = new Array(); // 目前高亮面的mesh的数组
var allFaceLineList = new Array(); // 目前高亮轮廓的mesh的数组
var meshClickManager = new MeshClick();
var PMITreeData = null;

// ------------读地址栏url，并自动解析
var fileName = window.location.href.split('?')[1];
var base = fileName.split('/');

// ----------------------开始读取index.xml 文件
var xmlDoc = window.LoadXMLFile(fileName);

var xshell = xmlDoc.getElementsByTagName('shell');
var shapes = xmlDoc.getElementsByTagName('shape');
var childOfShape0 = shapes[0].getElementsByTagName('child');
var map1 = []; // 存放shape[0]child标签内的ref和xform的对应关系
for (var i = 0; i < childOfShape0.length; i += 1) {
  var ref = childOfShape0[i].getAttribute('ref');
  var shapexform = childOfShape0[i].getAttribute('xform');
  map1.push([ref, shapexform]);
}

var map2 = []; // ----------------2*3的数组:ref，shell的id和其对应的xml文件,以及对应的annotation的id
for (var j = 1; j < shapes.length; j += 1) {
  var shapesRef = shapes[j].getAttribute('id');
  var child = shapes[j].getElementsByTagName('child');
  for (var s = j; s <= j + child.length; s += 1) {
    if (
      shapes[s] != null
      && shapes[s].getAttribute('shell') != null
      && shapes[s].getAttribute('shell').length < 20
    ) {
      var shellId = shapes[s].getAttribute('shell');
      var annotation_id = shapes[s].getAttribute('annotation');
      var shell_url = `shell_${shellId}.xml`;
      console.log('shellId', shellId);
      map2.push([shapesRef, shellId, shell_url, annotation_id]);
    }
  }
  j += child.length;
}

map1.forEach((item, index) => {
  map2.forEach((item2, index2) => {
    // eslint-disable-next-line eqeqeq
    if (item[0] == item2[0]) {
      item.push(item2[1]);
    }
  });
});

var xform = [];
for (var x = 0; x < map1.length; x += 1) {
  var xxx = map1[x][1].split(' ');
  xform.push(xxx);
}

var products = xmlDoc.getElementsByTagName('product');
var annotations = xmlDoc.getElementsByTagName('annotation'); // 开始读annotation标签
window.num = 0;
/*
 * 原来这里传的是固定的shell,以下动态的将第一个shell的名称取出并赋值
 */
// xshell:[shell#id388, shell#id386, shell#id391, shell#id392, shell#id390, shell#id389, shell#id387, shell#id393]

var targetShellId = xshell[0].getAttribute('id');
var targetShellName = `shell_${targetShellId}`;
initGeoGroup(); // 绘制几何体
initLineGroup(targetShellName); // 绘制所有面的轮廓 (TODO：同时记录targetShell的count,是为了什么？)

render();
onWindowResize();
window.addEventListener('resize', onWindowResize);
document.getElementById('3dfooter').innerHTML = `零件个数:${geogroup.length}<br>标注个数：${annotations.length}`;

// ----------------加载结束

function chooseFace(faceIndexList, shellName) {
  highLight(faceIndexList, shellName);
  highLightTreeNode(faceIndexList, shellName);
}

/**
 * @param  {} faceIndexList 高亮面的数组
 * @param  {} shellName 高亮面所在零件的ID
 */
function highLight(faceIndexList, shellName) {
  allFaceLineList.forEach((line) => {
    scene.remove(line);
  });
  allFaceLineList = new Array();
  highLightFaceList.forEach((mesh) => {
    scene.remove(mesh);
  });
  highLightFaceList = new Array();

  var allFaceLines = drawfaceline(faceIndexList, shellName);// 画某些面的轮廓  （？）
  allFaceLines.forEach((allFaceLine) => {
    allFaceLineList.push(allFaceLine);
  });
  var highLightFace = drawface(faceIndexList, shellName, '381154'); // 高亮某些面
  highLightFaceList.push(highLightFace);

  var dragControls = new THREE.DragControls(highLightFaceList, camera, renderer.domElement);
  dragControls.addEventListener('dragstart', (event) => {
    controls.enabled = false;
  });
  dragControls.addEventListener('dragend', (event) => {
    controls.enabled = true;
  });
  dragControls.addEventListener('clickDragObject', (event) => {
    var selected = event.object;
    // 改透明度
    // eslint-disable-next-line eqeqeq
    if (selected.material.opacity == 0.5) {
    // selected.material.visible=true;
      selected.material.opacity = 1;
    } else {
      selected.material.opacity = 0.5;
    }
  // 改透明度结束
  });

  highLightFaceIndex = [];
  highLightFaceIndex.push(...faceIndexList);
}

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

function transform(transformxform, vec) {
  // ----------------根据装配关系处理点坐标
  var inv = mat4.create();
  mat4.inverse(transformxform, inv);
  var xfomr = inv;
  var inv2 = mat4.create();
  mat4.inverse(xfomr, inv2);
  mat4.multiplyVec3(inv2, vec);
  return vec;
}
function show_anno() {
  // eslint-disable-next-line eqeqeq
  if (annotations && annogroup.length == 0) show_annotation(annotations);
}
function remove_anno() {
  if (annogroup.length > 0) {
    for (var a = 0; a < annogroup.length; a += 1) scene.remove(annogroup[a]);
    annogroup.length = 0;
  }
  // console.log(annogroup.length)
}
