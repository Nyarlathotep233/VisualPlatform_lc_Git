/* eslint-disable radix */
/* eslint-disable prefer-destructuring */
function initGeoGroup() {
  // map1
  // map2
  // geogroup
  // scene
  let count = 0;
  for (
    let i = 0;
    i < map1.length;
    i += 1 // 遍历map1 ，分别画出相关的物体map1.length
  ) {
    let dex = '1'; // shell_idXXX.xml
    let ref = '1';
    let p = 0;
    for (let s = 0; s < map2.length; s += 1) {
      if (map1[i][0] === map2[s][0]) {
        ref = map1[i][0];
        dex = map2[s][2];
        p = s;
        break;
      }
    }
    if (dex !== '1') {
      geogroup[count] = new THREE.Group();
      dealgeo(dex, i); /// / -----------dealgeo()先把组件、轮廓分别保存在geogroup，linegroup
      scene.add(geogroup[count]); // 将组件添加到场景中
      count += 1;
    }
  }
}

function dealgeo(shellname, shellindex) {
  // 加载xml文件
  const baseurl = `${base[0]}/${base[1]}/`;
  let xmlDoc;
  try {
    // 判断文件是否存在
    xmlDoc = window.LoadXMLFile(baseurl + shellname);
  } catch (err) {
    // 如果文件不存在，退出，return 0;
    return 0;
  }
  const faces = xmlDoc.getElementsByTagName('facets');
  const vertex_pos = xmlDoc.getElementsByTagName('verts');
  const vp = vertex_pos[0].getElementsByTagName('v'); // 数组，所有的v标签
  const v = new Array(); // 所有点的坐标的集合
  for (let i = 0; i < vp.length; i += 1) {
    v.push(vp[i].getAttribute('p')); // eg：["12 12 11 " ,"",""]
  }
  initface(v, faces, shellindex); // 画几何体,
  // initline(v,faces,shellindex);//画轮廓
  return 1;
}

function initface(v, faces, shellindex) {
  const p = geogroup.length;
  for (let k = 0; k < faces.length; k += 1) {
    const yushu = k % 13; // c是第几个面。求余数，决定选用哪个颜色
    const f = faces[k].getElementsByTagName('f'); // f是指一个面的三角面片的 集合
    const facecolor = `0x${dealcolor(faces[k].getAttribute('color'))}`;
    const geometryFace = new THREE.Geometry();
    const faceIndex = k;

    for (let i = 0; i < v.length; i += 1) {
      const xxs = v[i].split(' '); // 第i个点的坐标["123.2","y","z"]
      let x = parseFloat(xxs[0]);
      let y = parseFloat(xxs[1]);
      let z = parseFloat(xxs[2]);

      let vec = [x, y, z];
      vec = transform(xform[shellindex], vec);
      x = vec[0];
      y = vec[1];
      z = vec[2];
      const p1 = new THREE.Vector3(x, y, z); // 顶点1坐标
      geometryFace.vertices.push(p1); // 顶点坐标添加到geometry对象
    }

    for (let j = 0; j < f.length; j += 1) {
      // 遍历所有的三角面片
      const dex = f[j].getAttribute('v'); // 点索引（0,1，2）
      const sz = dex.split(' ');
      const faxian = f[j].getAttribute('fn');
      const szet = faxian.split(' '); // 法向量["0","-1","-0"]
      const normal = new THREE.Vector3(
        parseInt(szet[0]),
        parseInt(szet[1]),
        parseInt(szet[2]),
      );
      const face = new THREE.Face3(
        parseInt(sz[0]),
        parseInt(sz[1]),
        parseInt(sz[2]),
        normal,
      ); // 创建三角面
      face.color.setHex(facecolor); // 面的颜色
      geometryFace.faces.push(face);
    }
    // 生成法向量
    geometryFace.computeFaceNormals();
    const faceMesh = new THREE.Mesh(geometryFace, material);
    geogroup[p - 1].add(faceMesh);
    // geogroup[shellindex].add(mesh);

    // eslint-disable-next-line no-underscore-dangle
    const _targetShellName = targetShellName;
    // eslint-disable-next-line no-underscore-dangle
    const _chooseFace = chooseFace;

    meshClickManager.addMesh(faceMesh, (mesh) => {
      console.log('!', mesh, faceIndex);
      _chooseFace([faceIndex], _targetShellName); // targetShellName 为测试用
    });
  }
}

function dealcolor(facolor) {
  // 颜色处理 把"  8844" 处理成-----> "008844"
  let fayans = facolor.trim();
  for (let k = 0; k < 6 - facolor.trim().length; k += 1) {
    fayans = `0${fayans}`;
  }
  return fayans;
}
