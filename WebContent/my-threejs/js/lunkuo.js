/**
 * @param  {} shellname
 */
function initLineGroup(shellname) {
  var count = 0;
  // console.log(map1[0][0])
  for (
    var i = 0;
    i < map1.length;
    i++ // 遍历map1 ，分别画出相关的物体map1.length
  ) {
    var dex = '1';
    var ref = '1';
    var p = 0;
    for (var s = 0; s < map2.length; s++) {
      if (map1[i][0] == map2[s][0]) {
        ref = map1[i][0];
        dex = map2[s][2];
        p = s;
        break;
      }
    }
    if (dex != '1') {
      linegroup[count] = new THREE.Group();
      dealline(dex, i); /// / -----------dealline()先把组件、轮廓分别保存在geogroup，linegroup
      if (dex == `${shellname}.xml`) window.num = count; // 记录targetShell的count
      scene.add(linegroup[count]); // 将轮廓添加到场景中
      count++;
    }
  }
}

function dealline(shellname, shellindex) {
  // 加载xml文件
  var baseurl = `${base[0]}/${base[1]}/`;
  var xmlDoc;
  try {
    // 判断文件是否存在
    xmlDoc = window.LoadXMLFile(baseurl + shellname);
  } catch (err) {
    // 如果文件不存在，退出，return 0;
    return 0;
  }
  var faces = xmlDoc.getElementsByTagName('facets');
  var vertex_pos = xmlDoc.getElementsByTagName('verts');
  var vp = vertex_pos[0].getElementsByTagName('v'); // 数组，所有的v标签
  var v = new Array(); // 所有点的坐标的集合
  for (var i = 0; i < vp.length; i++) {
    v.push(vp[i].getAttribute('p')); // eg：["12 12 11 " ,"",""]
  }
  var lunkuodian = initline1(v, faces, shellindex); // 画轮廓
  // console.log(lunkuodian)
  var geometryal = new THREE.Geometry();
  // var material=new THREE.LineBasicMaterial({color:0x000000});
  var dianji = [];
  for (var i = 0; i < lunkuodian.length; i++) {
    for (var j = 0; j < lunkuodian[i].length; j++) {
      var sz = lunkuodian[i][j];
      dianji.push(sz);
    }
  }
  for (var d = 0; d < dianji.length; d++) {
    var p = new THREE.Vector3(dianji[d][0], dianji[d][1], dianji[d][2]);
    geometryal.vertices.push(p);
  }
  var mesh = new THREE.LineSegments(geometryal, lunkuo_material); // 线模型对象(断续绘制)
  var p = linegroup.length;
  linegroup[p - 1].add(mesh);
  // mesh.updateMatrix();
  // alllinegeom.merge(mesh.geometry, mesh.matrix);
}

function initline1(v, faces, shellindex) {
  var hhh = [];
  for (var k = 0; k < faces.length; k++) {
    for (var p = k + 1; p < faces.length; p++) {
      var diansz = drawlain1(k, p, v, faces, shellindex);
      if (diansz) hhh.push(diansz);
    }
  }
  // console.log(hhh)
  return hhh;
}

function drawlain1(kf1, kf2, v, faces, shellindex) {
  if (kf1 == kf2) {
    return 0;
  }
  var f1vindex = new Array(); // 面f1的索引集合
  var allvindex = new Array(); // 面f1 与 f2 共有的索引集合
  var f = faces[kf1].getElementsByTagName('f');
  var diansz = [];
  for (var j = 0; j < f.length; j++) {
    // 遍历所有的三角面片
    var dex = f[j].getAttribute('v'); // 点索引（0,1，2）
    var sz = dex.split(' ');
    f1vindex.push(`${sz[0]} ${sz[1]}`);
    f1vindex.push(`${sz[1]} ${sz[0]}`);
    f1vindex.push(`${sz[1]} ${sz[2]}`);
    f1vindex.push(`${sz[2]} ${sz[1]}`);
    f1vindex.push(`${sz[2]} ${sz[0]}`);
    f1vindex.push(`${sz[0]} ${sz[2]}`);
  }
  var f2 = faces[kf2].getElementsByTagName('f');
  for (var j = 0; j < f2.length; j++) {
    // 遍历所有的三角面片
    var dex = f2[j].getAttribute('v'); // 点索引（0,1，2）
    var sz = dex.split(' ');
    // 在f1中存在该点集，则保存
    if (f1vindex.indexOf(`${sz[0]} ${sz[1]}`) >= 0) {
      allvindex.push([sz[0], sz[1]]);
    }
    if (f1vindex.indexOf(`${sz[1]} ${sz[0]}`) >= 0) {
      allvindex.push([sz[1], sz[0]]);
    }

    if (f1vindex.indexOf(`${sz[0]} ${sz[2]}`) >= 0) {
      allvindex.push([sz[0], sz[2]]);
    }
    if (f1vindex.indexOf(`${sz[2]} ${sz[0]}`) >= 0) {
      allvindex.push([sz[2], sz[0]]);
    }

    if (f1vindex.indexOf(`${sz[1]} ${sz[2]}`) >= 0) {
      allvindex.push([sz[1], sz[2]]);
    }
    if (f1vindex.indexOf(`${sz[2]} ${sz[1]}`) >= 0) {
      allvindex.push([sz[2], sz[1]]);
    }
  }
  if (allvindex.length < 1) {
    return 0;
  }
  // 存在公共 点，就划线
  // allvindex =[["1","2"],["0","123"]]
  for (var m = 0; m < allvindex.length; m += 2) {
    var dian1 = v[parseInt(allvindex[m][0])].split(' '); // ["x","y","z"]
    var dian2 = v[parseInt(allvindex[m][1])].split(' '); // ["x","y","z"]
    var z1 = parseFloat(dian1[2]) + 0 * shellindex;
    var z2 = parseFloat(dian2[2]) + 0 * shellindex;
    var indian1 = new THREE.Vector3(
      parseFloat(dian1[0]),
      parseFloat(dian1[1]),
      z1,
    );
    var indian2 = new THREE.Vector3(
      parseFloat(dian2[0]),
      parseFloat(dian2[1]),
      z2,
    );
      // console.log(indian1.x)
    var vec1 = [indian1.x, indian1.y, indian1.z];
    vec1 = transform(xform[shellindex], vec1);
    indian1.x = vec1[0];
    indian1.y = vec1[1];
    indian1.z = vec1[2];
    var vec2 = [indian2.x, indian2.y, indian2.z];
    vec2 = transform(xform[shellindex], vec2);
    indian2.x = vec2[0];
    indian2.y = vec2[1];
    indian2.z = vec2[2];
    diansz.push([indian1.x, indian1.y, indian1.z]);
    diansz.push([indian2.x, indian2.y, indian2.z]);
  }

  // console.log(diansz)
  return diansz;
}

// -------------画出指定的shellname的某些面的轮廓--------------
function drawfaceline(theface, shellname) {
  // theface:要画的面的下标的数组，shellname：shell_id20235
  var baseurl = `${base[0]}/${base[1]}/`;
  var xmlDoc;
  try {
    // 判断文件是否存在
    xmlDoc = window.LoadXMLFile(`${baseurl + shellname}.xml`);
  } catch (err) {
    // 如果文件不存在，退出，return 0;
    return 0;
  }
  var faces = xmlDoc.getElementsByTagName('facets');
  var verts = xmlDoc.getElementsByTagName('verts');
  var v = verts[0].getElementsByTagName('v'); // 数组，所有的v标签
  var allverts = [];
  for (var i = 0; i < v.length; i++) {
    var vp = v[i].getAttribute('p');
    // vp=vp.split(" ");
    allverts.push(vp);
  }
  // console.log(allverts)
  // console.log(shellname)
  shellname = shellname.split('_'); // shellname["shell","id20235"]
  var shellindex = 0;
  for (var n = 0; n < map1.length; n++) {
    if (map1[n][2] == shellname[1]) {
      shellindex = n;
      // console.log("1");
    }
  }
  // console.log(shellindex);
  var allFaceLines = new Array();
  for (var t = 0; t < theface.length; t++) {
    for (var i = 0; i < faces.length; i++) {
      var facelines = drawlain(i, theface[t], allverts, faces, shellindex);
      if (facelines && facelines.length) {
        facelines.forEach((faceline) => {
          allFaceLines.push(faceline);
        });
      }
    }
  }
  // console.log(verts)
  return allFaceLines;
}

// kf1:面1，kf2：面2， v点集合，faces：面集合
function drawlain(kf1, kf2, v, faces, shellindex) {
  if (kf1 == kf2) {
    return 0;
  }
  var f1vindex = new Array(); // 面f1的索引集合
  var allvindex = new Array(); // 面f1 与 f2 共有的索引集合
  var f = faces[kf1].getElementsByTagName('f');
  for (var j = 0; j < f.length; j++) {
    // 遍历所有的三角面片
    var dex = f[j].getAttribute('v'); // 点索引（0,1，2）
    var sz = dex.split(' ');
    f1vindex.push(`${sz[0]} ${sz[1]}`);
    f1vindex.push(`${sz[1]} ${sz[0]}`);
    f1vindex.push(`${sz[1]} ${sz[2]}`);
    f1vindex.push(`${sz[2]} ${sz[1]}`);
    f1vindex.push(`${sz[2]} ${sz[0]}`);
    f1vindex.push(`${sz[0]} ${sz[2]}`);
  }
  var f2 = faces[kf2].getElementsByTagName('f');
  for (var j = 0; j < f2.length; j++) {
    // 遍历所有的三角面片
    var dex = f2[j].getAttribute('v'); // 点索引（0,1，2）
    var sz = dex.split(' ');
    // 在f1中存在该点集，则保存
    if (f1vindex.indexOf(`${sz[0]} ${sz[1]}`) >= 0) {
      allvindex.push([sz[0], sz[1]]);
    }
    if (f1vindex.indexOf(`${sz[1]} ${sz[0]}`) >= 0) {
      allvindex.push([sz[1], sz[0]]);
    }

    if (f1vindex.indexOf(`${sz[0]} ${sz[2]}`) >= 0) {
      allvindex.push([sz[0], sz[2]]);
    }
    if (f1vindex.indexOf(`${sz[2]} ${sz[0]}`) >= 0) {
      allvindex.push([sz[2], sz[0]]);
    }

    if (f1vindex.indexOf(`${sz[1]} ${sz[2]}`) >= 0) {
      allvindex.push([sz[1], sz[2]]);
    }
    if (f1vindex.indexOf(`${sz[2]} ${sz[1]}`) >= 0) {
      allvindex.push([sz[2], sz[1]]);
    }
  }
  if (allvindex.length < 1) {
    return 0;
  }
  // 存在公共 点，就划线
  // allvindex =[["1","2"],["0","123"]]
  var facelines = new Array();
  for (var m = 0; m < allvindex.length; m += 2) {
    var dian1 = v[parseInt(allvindex[m][0])].split(' '); // ["x","y","z"]
    var dian2 = v[parseInt(allvindex[m][1])].split(' '); // ["x","y","z"]
    var z1 = parseFloat(dian1[2]) + 0 * shellindex;
    var z2 = parseFloat(dian2[2]) + 0 * shellindex;
    var indian1 = new THREE.Vector3(
      parseFloat(dian1[0]),
      parseFloat(dian1[1]),
      z1,
    );
    var indian2 = new THREE.Vector3(
      parseFloat(dian2[0]),
      parseFloat(dian2[1]),
      z2,
    );
      // console.log(indian1.x)
    var vec1 = [indian1.x, indian1.y, indian1.z];
    vec1 = transform(xform[shellindex], vec1);
    indian1.x = vec1[0];
    indian1.y = vec1[1];
    indian1.z = vec1[2];
    var vec2 = [indian2.x, indian2.y, indian2.z];
    vec2 = transform(xform[shellindex], vec2);
    indian2.x = vec2[0];
    indian2.y = vec2[1];
    indian2.z = vec2[2];
    var faceline = addline(indian2, indian1, shellindex);
    facelines.push(faceline);
  }

  return facelines;
}

function addline(p1, p2, shellindex) {
  var geometry = new THREE.Geometry(); // 定义几何体
  var material = new THREE.LineBasicMaterial({ color: 0xc71585, opacity: 0.5 });
  geometry.vertices.push(p1);
  geometry.vertices.push(p2);
  var line = new THREE.Line(geometry, material);
  var faceline = new THREE.Group(); // TODO：新增一个group的作用？
  faceline.add(line);
  scene.add(faceline);

  return faceline;
}
