/* eslint-disable no-underscore-dangle */

function MeshClick() {
  this._meshList = [];
  this.addMesh = function (newmesh, cb = function () {}) {
    this._meshList.push({ mesh: newmesh, cb });
  };
  this.onMouseClick = function (event) {
    event.preventDefault();

    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();
    const _domElement = renderer.domElement;
    const rect = _domElement.getBoundingClientRect();
    // 将鼠标点击位置的屏幕坐标转换成threejs中的标准坐标

    mouse.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
    mouse.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;

    // 通过鼠标点的位置和当前相机的矩阵计算出raycaster
    raycaster.setFromCamera(mouse, camera);

    // // 获取raycaster直线和所有模型相交的数组集合(intersectList)和最近的模型
    const intersectList = [];
    let latestIntersect;
    let latestDistance;

    this._meshList.forEach((element) => {
      const intersects = raycaster.intersectObject(element.mesh, true);
      if (intersects && intersects.length) {
        intersectList.push(element);
        if (latestIntersect) {
          if (latestDistance > intersects[0].distance) {
            latestIntersect = element;
            latestDistance = intersects[0].distance;
          }
        } else {
          latestIntersect = element;
          latestDistance = intersects[0].distance;
        }
      }
    });
    if (latestIntersect) {
      latestIntersect.cb(latestIntersect.mesh);
    }
  };

  window.addEventListener('click', (event) => { this.onMouseClick(event); }, false);
}
