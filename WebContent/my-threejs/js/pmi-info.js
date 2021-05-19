/* eslint-disable no-underscore-dangle */
const FIRSTINDEX = 467; // 测试用

function renderPMITree(originPMITreeData, currentArray) {
  const { createElement } = React;
  const { useState, useEffect } = React;
  function PMITree(props) {
    // init
    function initAllNode(node) {
      const initAttr = {
        _showChildren: true,
        _choosed: false,
        _nodeID: window.uuidv1(),
      };

      // 初始化被选中的node
      if (props.current.indexOf(node.key) !== -1) {
        initAttr._choosed = true;
      }

      if (node.children && node.children.length) {
        return {
          ...node,
          ...initAttr,
          children: node.children.map((item, index) => initAllNode(item)),
        };
      }
      return { ...node, ...initAttr, _showChildren: true };
    }

    let PMITreeData = _.cloneDeep(props.data);
    PMITreeData = initAllNode(PMITreeData);

    const [state, setState] = useState({ PMITreeData, current: props.current });

    // eslint-disable-next-line no-underscore-dangle
    function _changeNodeAttr(nodeID, attrName, value) {
      _loopAllNode((item) => {
        if (item._nodeID === nodeID) {
          // eslint-disable-next-line no-param-reassign
          item[attrName] = value;
        }
      }, state.PMITreeData);
      setState({
        ...state,
      });
    }
    // eslint-disable-next-line no-underscore-dangle
    function _loopAllNode(cb, node) {
      cb(node);
      if (node.children && node.children.length) {
        node.children.forEach((item) => {
          _loopAllNode(cb, item);
        });
      }
    }

    function expAll() {
      function recExpAll(childrenList) {
        if (childrenList && childrenList.length) {
          return childrenList.map((item, index) => {
            let children = [];
            if (item.children && item.children.length) {
              children = recExpAll(item.children);
            }
            return { ...item, _showChildren: false, children };
          });
        }
        return [];
      }

      state.PMITreeData = { ...state, _showChildren: false, children: recExpAll(state.children) };

      setState({
        ...state,
      });
    }
    function colAll() {
      function recColAll(childrenList) {
        if (childrenList && childrenList.length) {
          return childrenList.map((item, index) => {
            let children = [];
            if (item.children && item.children.length) {
              children = recColAll(item.children);
            }
            return { ...item, _showChildren: true, children };
          });
        }
        return [];
      }

      state.PMITreeData = { ...state, _showChildren: true, children: recColAll(state.children) };

      setState({
        ...state,
      });
    }
    function createNode(node) {
      function ul() {
        const showul = node.children && node.children.length && node._showChildren;
        if (showul) {
          return <ul>{node.children.map((item, index) => createNode(item))}</ul>;
        }
        return null;
      }

      function switchBtn() {
        const { _showChildren } = node;
        if (node.children && node.children.length) {
          return _showChildren ? <img src="./images/icons/minus.png"></img> : <img src="./images/icons/add.png"></img>;
        }
        return '';
      }

      function setCurrentNodeAttr(attrName, value) {
        _changeNodeAttr(node._nodeID, attrName, value);
      }

      function handleClick() {
        if (haveClickEvent()) {
          node.handleClick(setCurrentNodeAttr, node);
        }
        if (node.chooseable) {
          _loopAllNode((item, cb) => {
            _changeNodeAttr(item._nodeID, '_choosed', false);
          }, state.PMITreeData);
          setCurrentNodeAttr('_choosed', true);
        }
      }

      function haveClickEvent() {
        if (node.handleClick && (typeof node.handleClick === 'function')) {
          return true;
        }
        return false;
      }

      function switchShow() {
        setCurrentNodeAttr('_showChildren', !node._showChildren);
      }

      function imgError({ target }) {
        console.log('imgError', target);
        // eslint-disable-next-line no-param-reassign
        target.style.display = 'none';
      }

      function getLabel() {
        if (haveClickEvent()) {
          return (
            <a onClick={handleClick}>
              {getName()}
            </a>
          );
        }
        return (
          <span onClick={handleClick}>
            {getName()}
          </span>
        );
      }

      function getName() {
        const { label } = node;
        let content;
        if (typeof label === 'object') {
          content = label.content;
        } else {
          content = label;
        }

        if (typeof label === 'string') {
          return content;
        } if (typeof label === 'object') {
          return (
            <div style = {{ display: 'flex', 'align-items': 'center' }}>
              <img src={label.icon} style = {{ display: 'flex', height: '16px', 'margin-right': '6px' }} onError={imgError}></img>
              <span>{content}</span>
            </div>
          );
        }
        return content;
      }

      return (
        <li style={{ 'list-style-type': 'none', border: `${node._choosed ? '2' : '0'}px red solid` }}>
          <div style={{
            display: 'flex', 'align-items': 'center', height: '20px',
          }} >
            <span onClick={switchShow} className= 'spread-btn'>
              {switchBtn()}
            </span>
            {getLabel()}
          </div>
          {ul()}
        </li>
      );
    }
    return (
      <div>
        <div>
          <button onClick={expAll}>折叠</button>
          <button onClick={colAll}>展开</button>
        </div>
        <div>
          <ul style={{ 'list-style-type': 'none' }}>{createNode(state.PMITreeData)}</ul>
        </div>
      </div>
    );
  }

  const domContainer = document.querySelector('#tree-container');
  ReactDOM.render(<PMITree data={originPMITreeData} current={currentArray}/>, domContainer);
}

function highLightTreeNode(faces, shellName) {
  const faceIDList = faces.map((faceIndex) => `#${faceIndex + FIRSTINDEX}`);
  renderPMITree(window.PMITreeData, faceIDList);
}

function loadPMI(xmlFile) {
  const XML = LoadXMLFile(xmlFile);
  const SemanticList = XML.getElementsByTagName('Semantic');
  const elementFaceList = [];
  const elementFaceMap = {};

  // 解析 SemanticList elementFaceList
  Array.from(SemanticList).forEach((element) => {
    const type = element.getAttribute('type');
    const head = element.getElementsByTagName('head')[0];
    const body = element.getElementsByTagName('body')[0];
    const tail = element.getElementsByTagName('tail')[0];

    const elementface = head.getElementsByTagName('elementface')[0].firstChild.nodeValue;
    const PMIElement = {
      children: [],
    };
    const typeNameList = {
      angularity_tolerance: '倾斜度公差',
      flatness_tolerance: '平面度公差',
      parallelism_tolerance: '平行度公差',
      perpendicularity_tolerance: '垂直度公差',
      roundness_tolerance: '圆度公差',
      straightness_tolerance: '直线度公差',
      geometric_tolerance: '位置度',
      plus_minus_tolerance: '正负',
      circular_runoutput_tolerance: '圆跳动公差',
    };
    if (!elementFaceMap[elementface]) {
      const elementFaceElement = {
        label: `面${elementface}`,
        elementface,
        toleranceNum: 0,
        children: [], // 公差
        chooseable: true,
        key: elementface,
        handleClick: () => {
          const faceIndex = String(elementface).slice(1) - FIRSTINDEX;

          chooseFace([faceIndex], targetShellName); // targetShellName 为测试用
        },
      };
      elementFaceMap[elementface] = elementFaceElement;
    }
    let faceID;
    let typeName;
    let datumface;
    let toleranceID;
    let value;
    let toleranceNum;
    switch (type) {
      case 'datum':
        faceID = body.getElementsByTagName('Instance')[0].getAttribute('rdf:ID');
        PMIElement.label = `基准面${faceID}`;
        break;
      case 'angularity_tolerance':
      case 'flatness_tolerance':
      case 'parallelism_tolerance':
      case 'perpendicularity_tolerance':
      case 'roundness_tolerance':
      case 'straightness_tolerance':
      case 'geometric_tolerance':
      case 'plus_minus_tolerance':
      case 'circular_runoutput_tolerance':
        typeName = typeNameList[type];
        datumface = head.getElementsByTagName('datumface')[0] && head.getElementsByTagName('datumface')[0].firstChild.nodeValue;
        toleranceID = body.getElementsByTagName('Instance')[0] && body.getElementsByTagName('Instance')[0].getAttribute('rdf:ID');
        value = body.getElementsByTagName('value')[0] && body.getElementsByTagName('value')[0].firstChild.nodeValue;
        toleranceNum = elementFaceMap[elementface].toleranceNum + 1;

        elementFaceMap[elementface].toleranceNum += 1;
        PMIElement.label = { content: `公差${toleranceNum}`, icon: `./images/icons/semantic/${type}.png` };
        PMIElement.children.push({
          label: `类型: ${typeName}`,
          children: [],
        });
        PMIElement.children.push({
          label: `值: ${value}`,
          children: [],
        });
        if (datumface) {
          PMIElement.children.push({
            label: `基准面: ${datumface}`,
            children: [],
          });
        }
        break;
      default:
        PMIElement.label = `未知类型 ${type}`;
    }
    elementFaceMap[elementface].children.push(PMIElement);
    console.log(`----- ${elementface} -----`);
    console.log(PMIElement);
  });

  // elementFaceList
  Object.keys(elementFaceMap).forEach((elementface) => {
    elementFaceList.push(elementFaceMap[elementface]);
  });
  // 排序
  elementFaceList.sort((a, b) => Number(a.elementface.slice(1, a.elementface.length)) - Number(b.elementface.slice(1, b.elementface.length)));
  const treeData = {
    label: 'MBD产品模型',
    children: [
      {
        label: '零件PMI',
        children: elementFaceList,
      },
    ],
  };

  window.PMITreeData = treeData;

  renderPMITree(treeData, ['#467', '#468']);
}

// 获取PMI路径
// let fileName = window.location.href.split('?')[1];
const fileName = 'my-threejs/pmi_output.xml';

// 加载PMI数据
console.log(fileName);
loadPMI(fileName);
