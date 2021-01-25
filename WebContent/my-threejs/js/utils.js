function LoadXMLFile(xmlFile) {
  var xmlDom = null;
  if (window.ActiveXObject) {
    xmlDom = new ActiveXObject("Microsoft.XMLDOM");
    //xmlDom.loadXML(xmlFile);//如果用的是XML字符串
    xmlDom.load(xmlFile); //如果用的是xml文件。
  } else if (
    document.implementation &&
    document.implementation.createDocument
  ) {
    var xmlhttp = new window.XMLHttpRequest();
    xmlhttp.open("GET", xmlFile, false);
    xmlhttp.send(null);
    xmlDom = xmlhttp.responseXML.documentElement; //一定要有根节点(否则google浏览器读取不了)
  } else {
    xmlDom = null;
  }
  return xmlDom;
}
window.LoadXMLFile = LoadXMLFile;
// export { LoadXMLFile };
