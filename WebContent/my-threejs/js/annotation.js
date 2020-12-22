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
function read_annotation(id) {
  var xmlDom = LoadXMLFile(
    base[0] + "/" + base[1] + "/annotation_" + id + ".xml"
  );
  var root1 = xmlDom;
  //var annotations=root1.getElementsByTagName("annotation");
  var polyline = root1.getElementsByTagName("polyline");
  var pp = [];
  for (var i = 0; i < polyline.length; i++) {
    var p = polyline[i].getElementsByTagName("p");
    var zuobiao = [];
    for (var j = 0; j < p.length; j++) {
      var l = p[j].getAttribute("l");
      l = l.split(" ");
      zuobiao.push(l);
    }
    pp.push(zuobiao);
  }
  return pp; //以二维数组的形式返回所有点的坐标
}

function show_annotation(annotations) {
  for (var a = 0; a < annotations.length; a++) {
    var id = annotations[a].getAttribute("id"); //annotation的id
    var ref = [];
    for (var l = 0; l < map2.length; l++) {
      if (id == map2[l][3]) {
        ref = map2[l][0];
        break;
      }
    }
    var xform = [];
    for (var k = 0; k < map1.length; k++) {
      if (map1[k][0] == ref) xform = map1[k][1];
    }
    xform = xform.split(" ");
    annogroup[a] = new THREE.Group();
    var AnnoPos = read_annotation(id);
    for (var w = 0; w < AnnoPos.length; w++) {
      var linePos = AnnoPos[w];
      for (var m = 0; m < linePos.length - 1; m++) {
        annogroup[a].add(draw_line(linePos[m], linePos[m + 1], xform));
      }
      scene.add(annogroup[a]);
    }
  }
}

function draw_line(a, b, xform) {
  var x1 = a[0];
  var x2 = b[0];
  var y1 = a[1];
  var y2 = b[1];
  var z1 = a[2];
  var z2 = b[2];
  var vec1 = [x1, y1, z1];
  vec1 = transform(xform, vec1);
  var vec2 = [x2, y2, z2];
  vec2 = transform(xform, vec2);

  var geometry = new THREE.Geometry();
  var p1 = new THREE.Vector3(vec1[0], vec1[1], vec1[2]);
  var p2 = new THREE.Vector3(vec2[0], vec2[1], vec2[2]);
  geometry.vertices.push(p1);
  geometry.vertices.push(p2);
  var line = new THREE.Line(geometry, line_material1, THREE.LinePieces);
  scene.add(line);
  //line_material1.visible=false;
  return line;
}
