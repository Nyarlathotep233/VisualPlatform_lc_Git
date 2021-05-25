# VisualPlatform_lc_Git

## 三维展示和语义展示模块说明

> 对三维展示和语义展示模块的进行的修改和开发的说明

### 1.utils.js文件里存放工具函数

对于一些比较通用的工具函数,将它们放入了utils.js文件

| 函数名                     | 用途                              |
| -------------------------- | --------------------------------- |
| LoadXMLFile(<XML文件路径>) | 加载XML文件，返回XMLDom树的根节点 |
| uuidv1()                   | 返回一个16位的随机ID              |



### 2.读取XML文件的兼容性写法

由于不同浏览器下读取XML文件存在兼容性问题，封装了一个全局方法LoadXMLFile来读取XML文件，通过传入文件路径来读取相应的XML文件，返回XMLDom树的根节点

```javascript
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
// 将LoadXMLFile方法挂载到全局window变量上
try {
  window.LoadXMLFile = LoadXMLFile;
} catch (_) {
  module.exports = LoadXMLFile;
}
```

### 3.新导入的框架和第三方库

#### (1)React
> 利用React可以逐步采用的特性,部分组件使用了React来开发

在testMain.jsp中使用script标签引入react的两个js文件,都是压缩后的生产就绪（production-ready）版本
```html
<!-- testMain.jsp -->
<script src="assets/js/react.production.min.js"></script>
<script src="assets/js/react-dom.production.min.js"></script>
```
