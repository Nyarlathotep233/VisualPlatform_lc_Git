/* eslint-disable no-bitwise */

// LoadXMLFile
function LoadXMLFile(xmlFile) {
  var xmlDom = null;
  if (window.ActiveXObject) {
    xmlDom = new ActiveXObject('Microsoft.XMLDOM');
    // xmlDom.loadXML(xmlFile);//如果用的是XML字符串
    xmlDom.load(xmlFile); // 如果用的是xml文件。
  } else if (
    document.implementation
      && document.implementation.createDocument
  ) {
    var xmlhttp = new window.XMLHttpRequest();
    xmlhttp.open('GET', xmlFile, false);
    xmlhttp.send(null);
    xmlDom = xmlhttp.responseXML.documentElement; // 一定要有根节点(否则google浏览器读取不了)
  } else {
    xmlDom = null;
  }
  return xmlDom;
}
try {
  window.LoadXMLFile = LoadXMLFile;
} catch (_) {
  module.exports = LoadXMLFile;
}

// uuidv1
{
  let globalCounter = 0;
  const uuidv1 = () => {
    var nowHex = (1e16 + (1e4 * (+new Date() + 122192928e5)).toString(16)).slice(-16);
    globalCounter %= 4096;
    const rst = [nowHex.slice(8, 16), '-', nowHex.slice(4, 8), -1, nowHex.slice(1, 4), -8, (1e3 + (globalCounter += 1).toString(16)).slice(-3), '-']
      .concat((`${1e11}`).split('').map(() => Math.random().toString(16)[2])).join('');
    return rst;
  };
  try {
    window.uuidv1 = uuidv1;
  } catch (_) {
    module.exports = uuidv1;
  }
}
