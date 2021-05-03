var PMI = {
  log(content) {
    console.log('PMI:\n', content);
  },
};

function renderTree(OriginPMITreeData) {
  const { createElement } = React;
  const { useState, useEffect } = React;
  let PMITreeData = JSON.parse(JSON.stringify(OriginPMITreeData));

  // init
  function initAllNode(showNode) {
    const initAttr = {
      showChildren: true,
      nodeID: window.uuidv1(),
    };
    if (showNode.children && showNode.children.length) {
      return {
        ...showNode,
        ...initAttr,
        children: showNode.children.map((item, index) => initAllNode(item)),
      };
    }
    return { ...showNode, ...initAttr, showChildren: true };
  }

  PMITreeData = initAllNode(PMITreeData);

  function PMITree(props) {
    const [state, setState] = useState(props.data);

    function expAll() {
      function recExpAll(childrenList) {
        if (childrenList && childrenList.length) {
          return childrenList.map((item, index) => {
            let children = [];
            if (item.children && item.children.length) {
              children = recExpAll(item.children);
            }
            return { ...item, showChildren: false, children };
          });
        }
        return [];
      }

      const rst = { ...state, showChildren: false, children: recExpAll(state.children) };

      setState(rst);
    }
    function colAll() {
      function recColAll(childrenList) {
        if (childrenList && childrenList.length) {
          return childrenList.map((item, index) => {
            let children = [];
            if (item.children && item.children.length) {
              children = recColAll(item.children);
            }
            return { ...item, showChildren: true, children };
          });
        }
        return [];
      }

      const rst = { ...state, showChildren: true, children: recColAll(state.children) };

      setState(rst);
    }

    function changeNodeAttr(nodeID, attrName, value) {}

    function expNode() {}

    function colNode() {}

    // function switchShow(nodeID) {
    //   node.showChildren = !node.showChildren;
    //   setState({
    //     ...state,
    //   });
    // }

    function createNode(node) {
      function ul(showul) {
        if (showul) {
          return <ul>{node.children.map((item, index) => createNode(item))}</ul>;
        }
        return null;
      }

      function switchShow(nodeID) {
        // eslint-disable-next-line no-param-reassign
        node.showChildren = !node.showChildren;
        setState({
          ...state,
        });
      }

      function imgError({ target }) {
        console.log('imgError', target);
        // eslint-disable-next-line no-param-reassign
        target.style.display = 'none';
      }

      function getLabel(label) {
        if (typeof label === 'string') {
          return label;
        } if (typeof label === 'object') {
          return (
            <div style = {{ display: 'flex', 'align-items': 'center' }}>
              <img src={label.icon} style = {{ display: 'flex', height: '16px', 'margin-right': '6px' }} onError={imgError}></img>
              <span>{label.content}</span>
            </div>
          );
        }
        return label;
      }

      return (
        <li style={{ 'list-style-type': 'none' }}>
          <a onClick={switchShow}>
            {getLabel(node.label)}
          </a>
          {ul(node.children && node.children.length && node.showChildren)}
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
          <ul style={{ 'list-style-type': 'none' }}>{createNode(state)}</ul>
        </div>
      </div>
    );
  }

  const domContainer = document.querySelector('#tree-container');
  ReactDOM.render(<PMITree data={PMITreeData} />, domContainer);
}

function loadPMI(xmlFile) {
  var XML = LoadXMLFile(xmlFile);
  var SemanticList = XML.getElementsByTagName('Semantic');
  var elementFaceList = [];
  var elementFaceMap = {};

  // 解析 SemanticList elementFaceList
  Array.from(SemanticList).forEach((element) => {
    var type = element.getAttribute('type');
    var head = element.getElementsByTagName('head')[0];
    var body = element.getElementsByTagName('body')[0];
    var tail = element.getElementsByTagName('tail')[0];

    var elementface = head.getElementsByTagName('elementface')[0].firstChild.nodeValue;
    var PMIElement = {
      children: [],
    };
    var typeNameList = {
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
      var elementFaceElement = {
        label: `面${elementface}`,
        elementface,
        toleranceNum: 0,
        children: [], // 公差
      };
      elementFaceMap[elementface] = elementFaceElement;
    }
    switch (type) {
      case 'datum':
        var faceID = body.getElementsByTagName('Instance')[0].getAttribute('rdf:ID');
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
        var typeName = typeNameList[type];
        var datumface = head.getElementsByTagName('datumface')[0] && head.getElementsByTagName('datumface')[0].firstChild.nodeValue;
        var toleranceID = body.getElementsByTagName('Instance')[0] && body.getElementsByTagName('Instance')[0].getAttribute('rdf:ID');
        var value = body.getElementsByTagName('value')[0] && body.getElementsByTagName('value')[0].firstChild.nodeValue;
        var toleranceNum = elementFaceMap[elementface].toleranceNum + 1;

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
    PMI.log(`----- ${elementface} -----`);
    PMI.log(PMIElement);
  });

  // elementFaceList
  Object.keys(elementFaceMap).forEach((elementface) => {
    elementFaceList.push(elementFaceMap[elementface]);
  });
  // 排序
  elementFaceList.sort((a, b) => Number(a.elementface.slice(1, a.elementface.length)) - Number(b.elementface.slice(1, b.elementface.length)));
  var treeData = {
    label: 'MBD产品模型',
    children: [
      {
        label: '零件PMI',
        children: elementFaceList,
      },
    ],
  };

  renderTree(treeData);
}

// 获取PMI路径
// var fileName = window.location.href.split('?')[1];
var fileName = 'my-threejs/pmi_output.xml';

// 加载PMI数据
PMI.log(fileName);
loadPMI(fileName);
