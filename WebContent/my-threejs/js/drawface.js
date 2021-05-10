function hasArray(array) {
  var str = '';
  for (var i = 0; i < array.length; i += 1) var str = str + array[i];
  if (scene.getObjectByName(str) == null) return false;
  return true;
}

function drawface(theface, shellname, color) {
  var baseurl = `${base[0]}/${base[1]}/`;

  // var xmlDoc=document.implementation.createDocument("","",null);
  // xmlDoc.async=false;
  // try {//判断文件是否存在
  //   xmlDoc.load(baseurl+shellname+".xml");
  // }
  // catch(err){//如果文件不存在，退出，return 0;
  //   return 0;
  // }
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
  var allverts = []; // 存储所有的点坐标
  for (var i = 0; i < v.length; i += 1) {
    var vp = v[i].getAttribute('p');
    vp = vp.split(' ');
    allverts.push(vp);
  }
  shellname = shellname.split('_'); // shellname：数组，如["shell","id20235"]
  var shellindex = 0;
  for (
    var n = 0;
    n < map1.length;
    n += 1 // 查询shellname对应的几何体下标
  ) {
    if (map1[n][2] == shellname[1]) {
      shellindex = n;
    }
  }
  var xf = xform[shellindex];
  var geometry = new THREE.Geometry(); // 声明一个空几何体对象
  for (var t = 0; t < theface.length; t += 1) {
    for (var i = 0; i < allverts.length; i += 1) {
      var xxs = allverts[i]; // 第i个点的坐标["123.2","y","z"]
      var x = parseFloat(xxs[0]);
      var y = parseFloat(xxs[1]);
      var z = parseFloat(xxs[2]);

      var vec = [x, y, z];
      vec = transform(xform[shellindex], vec);
      x = vec[0];
      y = vec[1];
      z = vec[2];
      var p1 = new THREE.Vector3(x, y, z); // 顶点1坐标
      geometry.vertices.push(p1); // 顶点坐标添加到geometry对象
    }
    if (color == '0') color = faces[t].getAttribute('color'); // 读面的颜色
    var facecolor = `0x${dealcolor(color)}`;
    facecolor = parseInt(facecolor);

    console.log('! faces', faces);
    var f = faces[theface[t]].getElementsByTagName('f'); // 面中所以f标签，代表每个三角面片的信息
    for (
      var i = 0;
      i < f.length;
      i += 1 // 遍历每个三件面片，读点索引及法向量
    ) {
      var vindex = f[i].getAttribute('v');
      vindex = vindex.split(' '); // 每个面片的点索引数组
      var fn = f[i].getAttribute('fn');
      fn = fn.split(' ');
      var normal = new THREE.Vector3(fn[0], fn[1], fn[2]);

      var face = new THREE.Face3(vindex[0], vindex[1], vindex[2], normal);
      // face.color.setHex(facecolor);//面的颜色
      geometry.faces.push(face);
    }
  }
  var material = new THREE.MeshLambertMaterial({
    vertexColors: THREE.VertexColors,
    side: THREE.DoubleSide,
    visible: true,
    color: facecolor,
    transparent: true,
    opacity: 1,
  });
  geometry.computeFaceNormals();
  var mesh = new THREE.Mesh(geometry, material);
  for (var t = 0; t < theface.length; t += 1) {
    mesh.name += theface[t];
  }
  // mesh.position.set(300,300,300);
  facegroup.push(mesh); // 将此mesh对象放入facegroup组中
  objects.push(mesh);
  scene.add(mesh);

  return mesh;
}

function drawDesignface(theface, shellname, color) {
  var baseurl = `${base[0]}/${base[1]}/`;

  // var xmlDoc=document.implementation.createDocument("","",null);
  // xmlDoc.async=false;
  // try {//判断文件是否存在
  //   xmlDoc.load(baseurl+shellname+".xml");
  // }
  // catch(err){//如果文件不存在，退出，return 0;
  //   return 0;
  // }
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
  var allverts = []; // 存储所有的点坐标
  for (var i = 0; i < v.length; i += 1) {
    var vp = v[i].getAttribute('p');
    vp = vp.split(' ');
    allverts.push(vp);
  }
  shellname = shellname.split('_'); // shellname：数组，如["shell","id20235"]
  var shellindex = 0;
  for (
    var n = 0;
    n < map1.length;
    n += 1 // 查询shellname对应的几何体下标
  ) {
    if (map1[n][2] == shellname[1]) {
      shellindex = n;
    }
  }
  var xf = xform[shellindex];
  var geometry = new THREE.Geometry(); // 声明一个空几何体对象
  for (var t = 0; t < theface.length; t += 1) {
    for (var i = 0; i < allverts.length; i += 1) {
      var xxs = allverts[i]; // 第i个点的坐标["123.2","y","z"]
      var x = parseFloat(xxs[0]);
      var y = parseFloat(xxs[1]);
      var z = parseFloat(xxs[2]);

      var vec = [x, y, z];
      vec = transform(xform[shellindex], vec);
      x = vec[0];
      y = vec[1];
      z = vec[2];
      var p1 = new THREE.Vector3(x, y, z); // 顶点1坐标
      geometry.vertices.push(p1); // 顶点坐标添加到geometry对象
    }
    if (color == '0') color = faces[t].getAttribute('color'); // 读面的颜色
    var facecolor = `0x${dealcolor(color)}`;
    facecolor = parseInt(facecolor);
    var f = faces[theface[t]].getElementsByTagName('f'); // 面中所以f标签，代表每个三角面片的信息
    for (
      var i = 0;
      i < f.length;
      i += 1 // 遍历每个三件面片，读点索引及法向量
    ) {
      var vindex = f[i].getAttribute('v');
      vindex = vindex.split(' '); // 每个面片的点索引数组
      var fn = f[i].getAttribute('fn');
      fn = fn.split(' ');
      var normal = new THREE.Vector3(fn[0], fn[1], fn[2]);

      var face = new THREE.Face3(vindex[0], vindex[1], vindex[2], normal);
      // face.color.setHex(facecolor);//面的颜色
      geometry.faces.push(face);
    }
  }
  var material = new THREE.MeshLambertMaterial({
    vertexColors: THREE.VertexColors,
    side: THREE.DoubleSide,
    visible: true,
    color: facecolor,
    transparent: true,
    opacity: 1,
  });
  geometry.computeFaceNormals();
  var mesh = new THREE.Mesh(geometry, material);
  mesh.name = 'design'; // 与drawface唯一不同的是mesh的命名方式
  for (var t = 0; t < theface.length; t += 1) {
    mesh.name += theface[t];
  }
  scene.add(mesh);
}

function deleteFromObject(str) {
  for (var i = 0; i < objects.length; i += 1) {
    if (objects[i].name == str) {
      objects.splice(i, 1);
      scene.remove(scene.getObjectByName(str));
    }
  }
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

function redrawGeo(shellname) {
  var baseurl = `${base[0]}/${base[1]}/`;
  // var xmlDoc=document.implementation.createDocument("","",null);
  // xmlDoc.async=false;
  // try {//判断文件是否存在
  //   xmlDoc.load(baseurl+shellname+".xml");
  // }
  // catch(err){//如果文件不存在，退出，return 0;
  //   return 0;
  // }
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
  var allverts = []; // 存储所有的点坐标
  for (var i = 0; i < v.length; i += 1) {
    var vp = v[i].getAttribute('p');
    vp = vp.split(' ');
    allverts.push(vp);
  }
  shellname = shellname.split('_'); // shellname：数组，如["shell","id20235"]
  var shellindex = 0;
  for (
    var n = 0;
    n < map1.length;
    n += 1 // 查询shellname对应的几何体下标
  ) {
    if (map1[n][2] == shellname[1]) {
      shellindex = n;
    }
  }
  var xf = xform[shellindex];
  for (var t = 0; t < faces.length; t += 1) {
    var geometry = new THREE.Geometry(); // 声明一个空几何体对象
    for (var i = 0; i < allverts.length; i += 1) {
      var xxs = allverts[i]; // 第i个点的坐标["123.2","y","z"]
      var x = parseFloat(xxs[0]);
      var y = parseFloat(xxs[1]);
      var z = parseFloat(xxs[2]);

      var vec = [x, y, z];
      vec = transform(xform[shellindex], vec);
      x = vec[0];
      y = vec[1];
      z = vec[2];
      var p1 = new THREE.Vector3(x, y, z); // 顶点1坐标
      geometry.vertices.push(p1); // 顶点坐标添加到geometry对象
    }
    var color = faces[t].getAttribute('color'); // 读面的颜色
    var facecolor = `0x${dealcolor(color)}`;
    facecolor = parseInt(facecolor);
    var f = faces[t].getElementsByTagName('f'); // 面中所以f标签，代表每个三角面片的信息
    for (
      var i = 0;
      i < f.length;
      i += 1 // 遍历每个三件面片，读点索引及法向量
    ) {
      var vindex = f[i].getAttribute('v');
      vindex = vindex.split(' '); // 每个面片的点索引数组
      var fn = f[i].getAttribute('fn');
      fn = fn.split(' ');
      var normal = new THREE.Vector3(fn[0], fn[1], fn[2]);

      var face = new THREE.Face3(vindex[0], vindex[1], vindex[2], normal);
      // face.color.setHex(facecolor);//面的颜色
      geometry.faces.push(face);
    }
    var material = new THREE.MeshLambertMaterial({
      vertexColors: THREE.VertexColors,
      side: THREE.DoubleSide,
      visible: true,
      color: facecolor,
      transparent: true,
      opacity: 1,
    });
    geometry.computeFaceNormals();
    var mesh = new THREE.Mesh(geometry, material);
    scene.add(mesh);
    mesh.name = t;
    objects.push(mesh);
  }
}
