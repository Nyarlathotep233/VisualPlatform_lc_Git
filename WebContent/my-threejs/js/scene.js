function initRenderer() {
  renderer = new THREE.WebGLRenderer({ antialias: true }); //, alpha: true
  //renderer.setSize( window.innerWidth/2, window.innerHeight);
  renderer.setSize(container.offsetWidth / 2, container.offsetHeight);
  renderer.setClearColor(0xf4f1bb, 1); //场景的颜色f4f1bb,000020
  renderer.shadowMapEnabled = true;
  renderer.shadowMap.type = THREE.PCFSoftShadowMap;
}
function initLight() {
  //add subtle ambient lighting
  var ambiColor = "#0c0c0c";
  var ambientLight = new THREE.AmbientLight(ambiColor);
  scene.add(ambientLight);

  var light;
  light = new THREE.PointLight(0xeeeeee);
  light.position.set(-500, 0, 5000);
  scene.add(light);

  var light2;
  light2 = new THREE.PointLight(0xeeeeee);
  light2.position.set(500, 0, -5000);
  scene.add(light2);

  var light3;
  light3 = new THREE.PointLight(0xeeeeee);
  light3.position.set(0, 5000, -500);
  scene.add(light3);

  var light4;
  light4 = new THREE.PointLight(0xeeeeee);
  light4.position.set(0, -5000, 500);
  scene.add(light4);

  var light5;
  light5 = new THREE.PointLight(0xeeeeee);
  light5.position.set(-5000, 500, 0);
  scene.add(light5);

  var light6;
  light6 = new THREE.PointLight(0xeeeeee);
  light6.position.set(5000, 0, 0);
  scene.add(light6);
}

function render() {
  requestAnimationFrame(render);
  controls.update();
  renderer.render(scene, camera);
}
