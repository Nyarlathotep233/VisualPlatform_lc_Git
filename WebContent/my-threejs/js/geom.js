function initGeoGroup() {
  // map1
  // map2
  // geogroup
  // scene
  var count = 0;
  console.log("map1?", map1);
  for (
    var i = 0;
    i < map1.length;
    i++ //遍历map1 ，分别画出相关的物体map1.length
  ) {
    var dex = "1"; //shell_idXXX.xml
    var ref = "1";
    var p = 0;
    for (var s = 0; s < map2.length; s++) {
      if (map1[i][0] == map2[s][0]) {
        ref = map1[i][0];
        dex = map2[s][2];
        p = s;
        break;
      }
    }
    if (dex != "1") {
      console.log("no here?");
      geogroup[count] = new THREE.Group();
      dealgeo(dex, i); //// -----------dealgeo()先把组件、轮廓分别保存在geogroup，linegroup
      scene.add(geogroup[count]); // 将组件添加到场景中
      count++;
    }
  }
}

function dealgeo(shellname, shellindex) {
  //加载xml文件
  var baseurl = base[0] + "/" + base[1] + "/";
  // var xmlDoc=document.implementation.createDocument("","",null);
  // xmlDoc.async=false;
  // try {//判断文件是否存在
  //   xmlDoc.load(baseurl+shellname);
  // }
  // catch(err){//如果文件不存在，退出，return 0;
  //   return 0;
  // }
  var xmlDoc;
  try {
    //判断文件是否存在
    xmlDoc = window.LoadXMLFile(baseurl + shellname);
  } catch (err) {
    //如果文件不存在，退出，return 0;
    return 0;
  }
  var faces = xmlDoc.getElementsByTagName("facets");
  var vertex_pos = xmlDoc.getElementsByTagName("verts");
  var vp = vertex_pos[0].getElementsByTagName("v"); //数组，所有的v标签
  var v = new Array(); //所有点的坐标的集合
  for (var i = 0; i < vp.length; i++) {
    v.push(vp[i].getAttribute("p")); //eg：["12 12 11 " ,"",""]
  }
  initface(v, faces, shellindex); //画几何体,
  //initline(v,faces,shellindex);//画轮廓
}

function initface(v, faces, shellindex) {
  //console.log(shellindex)
  var geometryall = new THREE.Geometry(); //声明一个空几何体对象
  for (var i = 0; i < v.length; i++) {
    var xxs = v[i].split(" "); //第i个点的坐标["123.2","y","z"]
    var x = parseFloat(xxs[0]);
    var y = parseFloat(xxs[1]);
    var z = parseFloat(xxs[2]);

    var vec = [x, y, z];
    vec = transform(xform[shellindex], vec);
    x = vec[0];
    y = vec[1];
    z = vec[2];
    var p1 = new THREE.Vector3(x, y, z); //顶点1坐标
    geometryall.vertices.push(p1); //顶点坐标添加到geometry对象
  }
  for (var k = 0; k < faces.length; k++) {
    var yushu = k % 13; //c是第几个面。求余数，决定选用哪个颜色
    var f = faces[k].getElementsByTagName("f"); //f是指一个面的三角面片的 集合
    var facecolor = "0x" + dealcolor(faces[k].getAttribute("color"));
    for (var j = 0; j < f.length; j++) {
      //遍历所有的三角面片
      var dex = f[j].getAttribute("v"); //点索引（0,1，2）
      var sz = dex.split(" ");
      var faxian = f[j].getAttribute("fn");
      var szet = faxian.split(" "); //法向量["0","-1","-0"]
      var normal = new THREE.Vector3(
        parseInt(szet[0]),
        parseInt(szet[1]),
        parseInt(szet[2])
      );
      var face = new THREE.Face3(
        parseInt(sz[0]),
        parseInt(sz[1]),
        parseInt(sz[2]),
        normal
      ); //创建三角面
      face.color.setHex(facecolor); //面的颜色
      geometryall.faces.push(face);
    }
  }
  //生成法向量
  geometryall.computeFaceNormals();
  var mesh = new THREE.Mesh(geometryall, material); //网格模型对象
  //mesh.receiveShadow=true;
  var p = geogroup.length;
  //console.log(p)
  geogroup[p - 1].add(mesh);
  //geogroup[shellindex].add(mesh);
  //scene.add(mesh);
}

function dealcolor(facolor) {
  //颜色处理 把"  8844" 处理成-----> "008844"
  var fayans = facolor.trim();
  for (var k = 0; k < 6 - facolor.trim().length; k++) {
    fayans = "0" + fayans;
  }
  return fayans;
}
