/* eslint-disable no-underscore-dangle */
let FIRSTINDEX;
/**
 * @param  {Object} originPMITreeData PMI树的根节点
 * Example: {
 *  label: 'MBD产品模型',
 *  children: [
 *    {
 *      label: '零件PMI',
 *      children: ...,
 *    },
 *  ],
 *};
 * @param  {Array} currentArray PMI树高亮的节点的ID Example:['#467', '#468']
 */
function renderPMITree(originPMITreeData, currentArray = []) {
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
      let rstPMITreeData = state.PMITreeData;

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

      rstPMITreeData = {
        ...rstPMITreeData,
        _showChildren: false,
        children: recExpAll(rstPMITreeData.children),
      };
      state.PMITreeData = rstPMITreeData;

      setState({
        ...state,
      });
    }
    function colAll() {
      let rstPMITreeData = state.PMITreeData;

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

      rstPMITreeData = {
        ...rstPMITreeData,
        _showChildren: true,
        children: recColAll(rstPMITreeData.children),
      };
      state.PMITreeData = rstPMITreeData;

      setState({
        ...state,
      });
    }
    function createNode(node) {
      function ul() {
        const showul = node.children && node.children.length && node._showChildren;
        if (showul) {
          return (
            <ul>{node.children.map((item, index) => createNode(item))}</ul>
          );
        }
        return null;
      }

      function switchBtn() {
        const { _showChildren } = node;
        if (node.children && node.children.length) {
          return _showChildren ? (
            <img src="./images/icons/minus.png"></img>
          ) : (
            <img src="./images/icons/add.png"></img>
          );
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
        if (node.handleClick && typeof node.handleClick === 'function') {
          return true;
        }
        return false;
      }

      function switchShow() {
        setCurrentNodeAttr('_showChildren', !node._showChildren);
      }

      function imgError({ target }) {
        // eslint-disable-next-line no-param-reassign
        target.style.display = 'none';
      }

      function getLabel() {
        if (haveClickEvent() || node.chooseable) {
          return <a onClick={handleClick}>{getName()}</a>;
        }
        return <span onClick={handleClick}>{getName()}</span>;
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
        }
        if (typeof label === 'object') {
          return (
            <div style={{ display: 'flex', 'align-items': 'center' }}>
              <img
                src={label.icon}
                style={{
                  display: 'flex',
                  height: '16px',
                  'margin-right': '6px',
                }}
                onError={imgError}
              ></img>
              <span>{content}</span>
            </div>
          );
        }
        return content;
      }

      return (
        <li
          style={{
            'list-style-type': 'none',
            border: `${node._choosed ? '2' : '0'}px red solid`,
          }}
        >
          <div
            style={{
              display: 'flex',
              'align-items': 'center',
              height: '20px',
            }}
          >
            <span onClick={switchShow} className="spread-btn">
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
          <ul style={{ 'list-style-type': 'none' }}>
            {createNode(state.PMITreeData)}
          </ul>
        </div>
      </div>
    );
  }

  const domContainer = document.querySelector('#tree-container');
  ReactDOM.render(
    <PMITree data={originPMITreeData} current={currentArray} />,
    domContainer,
  );
}
/**
 * @param  {Array} nodeID  ["#467", "flatness_tolerance1"]
 * @param  {String} shellName 高亮的面所在的零件的shellName，由于pmi树暂时未考虑多零件所以暂时不起作用
 */
function highLightTreeNode(nodeID, shellName) {
  renderPMITree(window.PMITreeData, nodeID);
}

function loadPMI(xmlFile) {
  const XML = LoadXMLFile(xmlFile);
  const SemanticList = XML.getElementsByTagName('Semantic');
  const elementFaceList = [];
  const elementFaceMap = {};

  Array.from(SemanticList).forEach((element) => {
    const type = element.getAttribute('type');

    if (type === 'FirstFace') {
      const faceID = element.getElementsByTagName('elementface')[0].firstChild.nodeValue.slice(1);
      FIRSTINDEX = Number(faceID);
    }
  });

  // 解析 SemanticList elementFaceList
  Array.from(SemanticList).forEach((element) => {
    const type = element.getAttribute('type');
    if (type === 'FirstFace') {
      return;
    }
    const head = element.getElementsByTagName('head')[0];
    const body = element.getElementsByTagName('body')[0];
    const tail = element.getElementsByTagName('tail')[0];

    const elementfaceStr = head.getElementsByTagName('elementface')[0].firstChild.nodeValue;
    const elementfaceList = elementfaceStr.split(',');

    elementfaceList.forEach((elementface) => {
      let PMIElement = {
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

            chooseFace([faceIndex], targetShellName); // 临时用targetShellName，不确定多零件下是否有问题
          },
        };
        elementFaceMap[elementface] = elementFaceElement;
      }

      const faceID = body.getElementsByTagName('Instance')[0] && body.getElementsByTagName('Instance')[0].getAttribute('rdf:ID');
      const typeName = typeNameList[type];
      const datumface = head.getElementsByTagName('datumface')[0]
      && head.getElementsByTagName('datumface')[0].firstChild.nodeValue;
      const toleranceID = body.getElementsByTagName('Instance')[0]
      && body.getElementsByTagName('Instance')[0].getAttribute('rdf:ID');
      const value = body.getElementsByTagName('value')[0]
      && body.getElementsByTagName('value')[0].firstChild.nodeValue;
      const tolerancevalue = (body.getElementsByTagName('tolerancevalue')[0]
      && body.getElementsByTagName('tolerancevalue')[0].firstChild.nodeValue) || '';
      const toleranceNum = elementFaceMap[elementface].toleranceNum + 1;
      if (!elementFaceName2ID[faceID]) {
        elementFaceName2ID[faceID] = { elementface };
      }

      switch (type) {
        case 'datum':
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
          elementFaceMap[elementface].toleranceNum += 1;
          PMIElement = {
            ...PMIElement,
            label: {
              content: `公差${toleranceNum}`,
              icon: `./images/icons/semantic/${type}.png`,
            },
            chooseable: true,
            key: toleranceID,
            handleClick: () => {
              const faceIndex = String(elementface).slice(1) - FIRSTINDEX;
              chooseTolerance(
                [faceIndex, datumface],
                targetShellName,
                toleranceID,
              ); // t临时用argetShellName，不确定多零件下是否有问题
            },
          };
          PMIElement.children.push({
            label: `类型: ${typeName}`,
            children: [],
          });
          PMIElement.children.push({
            label: `值: ${value} ${tolerancevalue}`,
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
    });
  });

  // elementFaceList
  Object.keys(elementFaceMap).forEach((elementface) => {
    elementFaceList.push(elementFaceMap[elementface]);
  });
  // 排序
  elementFaceList.sort(
    (a, b) => Number(a.elementface.slice(1, a.elementface.length))
      - Number(b.elementface.slice(1, b.elementface.length)),
  );
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

  renderPMITree(treeData);
}

// 获取PMI文件路径
const PMIFileName = `my-threejs/${window.location.href.split('?')[1].split('/')[1]}/output1.xml`;

// 加载PMI数据
loadPMI(PMIFileName);
