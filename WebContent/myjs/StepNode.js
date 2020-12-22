//自定义StepNode类  
function StepNode(id,name,content,list){
  	this.id=id;
  	this.name=name;
  	this.content=content;
  	this.list=list;
}
//删除字符串左右两端的空格
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
//根据id得到节点对象
function getLowNodeById(parsedData,id){
  var lowNode={};
  for(var i=0;i<parsedData.length;i++){
    if(parsedData[i].id==id){
      lowNode=parsedData[i];
    }
  }
  return lowNode;
}
//处理lowNode的方法
//根据parentNode来添加节点
function addChildNodeByParent(parentNode,parsedData,dataJson,ids){
  if(parentNode.children.length==0&&parentNode.shapes.length!=0){
  	var childId=parentNode.shapes[0].shells[0].id;
  	var lowChildren=[];
  	var lowNode=getLowNodeById(parsedData,childId); 
  	lowChildren.push(lowNode);
  	parentNode.lowChildren=lowChildren;                 
  	//lowNode.color=parentNode.color+1;
  	lowNode.color=parentNode.color;
  	lowNode.visible=false;
  	lowNode.children=[];
  	lowNode.assembly=parentNode.assembly;
  	lowNode.bbox=parentNode.bbox;
  	lowNode.shapes=[];
  	lowChildren=[];        
  	var lowChildrenId='id'+lowNode.list[1].value.substring(1);
  	var testNode=getLowNodeById(parsedData,lowChildrenId);
  	lowChildren.push(testNode);
  	lowNode.lowChildren=lowChildren;
  	lowNode.stepfile=parentNode.stepfile;
  	lowNode.weight=parentNode.weight;
  	lowNode.rootNode=parentNode.rootNode;             
  	if(ids.indexOf(lowNode.id)==-1){
      dataJson.nodes.push(lowNode);        
    }
  }else if(parentNode.name=='MANIFOLD_SOLID_BREP'||parentNode.name=='PLANE'||parentNode.name=='VERTEX_POINT'){
    var childId='id'+parentNode.list[1].value.substring(1);
    var lowNode=getLowNodeById(parsedData,childId); 
    lowNode.id=childId;
    lowNode.color=parentNode.color;
    lowNode.visible=false;
    lowNode.children=[];
    lowNode.assembly=parentNode.assembly;
    lowNode.bbox=parentNode.bbox;
    lowNode.shapes=[];
    lowNode.lowChildren=[];
    lowNode.stepfile=parentNode.stepfile;
    lowNode.weight=parentNode.weight;
    lowNode.rootNode=parentNode.rootNode;             
    // if(dataJson.nodes.indexOf(lowNode)==-1){
    //   dataJson.nodes.push(lowNode);        
    // }
    if(ids.indexOf(lowNode.id)==-1){
      dataJson.nodes.push(lowNode);        
    }
    parentNode.lowChildren.push(lowNode);         
  }else if(parentNode.name=='CLOSED_SHELL'){
    var str=parentNode.list[1].value;
    var childStr=str.substring(0,str.length-1);
    var lowNode={};
    lowNode.id=parentNode.id+'#1';
    lowNode.name='CFS_FACES';
    lowNode.color=parentNode.color;
    lowNode.visible=false;
    lowNode.children=[];
    lowNode.assembly=parentNode.assembly;
    lowNode.bbox=parentNode.bbox;
    lowNode.shapes=[];
    lowNode.childStr=childStr;
    lowNode.lowChildren=[];
    lowNode.stepfile=parentNode.stepfile;
    lowNode.weight=parentNode.weight;
    lowNode.rootNode=parentNode.rootNode;             
    if(ids.indexOf(lowNode.id)==-1){
      dataJson.nodes.push(lowNode);        
    }
    parentNode.lowChildren.push(lowNode);
  }else if(parentNode.name=='CFS_FACES'){       
    var str=parentNode.childStr;
    var idArray=str.split(',');
    for(var i=0;i<idArray.length;i++){
      if(idArray[i].length>0){
        var lowChildrenId='id'+trim(idArray[i]).substring(1);
        var lowNode=getLowNodeById(parsedData,lowChildrenId); 
        lowNode.id=lowChildrenId;
        lowNode.color=parentNode.color;
        lowNode.visible=false;
        lowNode.children=[];
        lowNode.assembly=parentNode.assembly;
        lowNode.bbox=parentNode.bbox;
        lowNode.shapes=[];
        lowNode.lowChildren=[];
        lowNode.stepfile=parentNode.stepfile;
        lowNode.weight=parentNode.weight;
        lowNode.rootNode=parentNode.rootNode;             
        if(ids.indexOf(lowNode.id)==-1){
          dataJson.nodes.push(lowNode);        
        }
        parentNode.lowChildren.push(lowNode); 
      }                   
    }    
  }else if(parentNode.name=='ADVANCED_FACE'){      
    var lowNode={};
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      if(i==1){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name='BOUNDS';
        var childStr=value.substring(0,value.length-1); 
        lowNode.childStr=childStr;    
      }else if(i==2){
        lowNode={};
        var childId='id'+value.substring(2);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==3){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        if(value.indexOf('T')>-1){
          lowNode.name='SAME_SENSE：true';
        }else if(value.indexOf('F')>-1){
          lowNode.name='SAME_SENSE：false';
        }else{
          lowNode.name='SAME_SENSE';
        }
      }                 
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];         
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);
    }
  }else if(parentNode.name=='BOUNDS'||parentNode.name=='EDGE_LIST'){
    var str=parentNode.childStr;
    var idArray=str.split(',');
    for(var i=0;i<idArray.length;i++){
      if(idArray[i].length>0){
        var lowChildrenId='id'+trim(idArray[i]).substring(1);
        var lowNode=getLowNodeById(parsedData,lowChildrenId);
        lowNode.id=lowChildrenId;
        lowNode.color=parentNode.color;
        lowNode.visible=false;
        lowNode.children=[];
        lowNode.assembly=parentNode.assembly;
        lowNode.bbox=parentNode.bbox;
        lowNode.shapes=[];
        lowNode.lowChildren=[];
        lowNode.stepfile=parentNode.stepfile;
        lowNode.weight=parentNode.weight;
        lowNode.rootNode=parentNode.rootNode;             
        if(ids.indexOf(lowNode.id)==-1){
          dataJson.nodes.push(lowNode);        
        }
        parentNode.lowChildren.push(lowNode);                          
      }                      
    }    
  }else if(parentNode.name=='FACE_BOUND'||parentNode.name=='FACE_OUTER_BOUND'){
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==1){
        lowNode={};
        var childId='id'+value.substring(1);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==2){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        if(value.indexOf('T')>-1){
          lowNode.name='ORIENTATION：true';
        }else if(value.indexOf('F')>-1){
          lowNode.name='ORIENTATION：false';
        }else{
          lowNode.name='ORIENTATION';
        }
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);             
    }              
  }else if(parentNode.name=='PRODUCT_DEFINITION_SHAPE'){
     for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==2){
        lowNode={};
        var childId='id'+value.substring(1);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==1){
        lowNode={};
        var str=value.substring(1,value.length-1);
        lowNode.id=parentNode.id+'#'+''+i+'';
        if(str.length>0){
          lowNode.name='DESCRIPTION：'+str;
        }else{
          lowNode.name='DESCRIPTION：'+'null';
        }          
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);  
    }
  }else if(parentNode.name=='EDGE_LOOP'){
    var value=parentNode.list[1].value;
    var childStr=value.substring(0,value.length-1);
    var lowNode={};
    lowNode.id=parentNode.id+'#'+''+1+'';
    lowNode.name='EDGE_LIST';
    lowNode.color=parentNode.color;
    lowNode.visible=false;
    lowNode.children=[];
    lowNode.assembly=parentNode.assembly;
    lowNode.bbox=parentNode.bbox;
    lowNode.shapes=[];
    lowNode.childStr=childStr;
    lowNode.lowChildren=[];
    lowNode.stepfile=parentNode.stepfile;
    lowNode.weight=parentNode.weight;
    lowNode.rootNode=parentNode.rootNode;             
    if(ids.indexOf(lowNode.id)==-1){
      dataJson.nodes.push(lowNode);        
    }
    parentNode.lowChildren.push(lowNode); 
  }else if(parentNode.name=='AXIS2_PLACEMENT_3D'){
    for(var i=1;i<parentNode.list.length;i++){
      var lowNode={};
      var childId='id'+parentNode.list[i].value.substring(1);
      lowNode=getLowNodeById(parsedData,childId);
      lowNode.id=childId;
      lowNode.name=lowNode.name+'：('+lowNode.list[1].value;
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);  
    }
  }else if(parentNode.name=='CYLINDRICAL_SURFACE'){
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==1){
        lowNode={};
        var childId='id'+value.substring(1);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==2){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name='RADIUS：'+value;
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode); 
    }       
  }else if(parentNode.name=='ORIENTED_EDGE'){
    for(var i=3;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==3){
        lowNode={};
        var childId='id'+value.substring(1);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==4){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        if(value.indexOf('T')>-1){
          lowNode.name='ORIENTATION：true';
        }else if(value.indexOf('F')>-1){
          lowNode.name='ORIENTATION：false';
        }else{
          lowNode.name='ORIENTATION';
        }
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode); 
    }
  }else if(parentNode.name=='EDGE_CURVE'){
  	for(var i=1;i<parentNode.list.length;i++){
  		var value=parentNode.list[i].value;
      var lowNode={};
      if(i<4){
	      lowNode={};
	      var childId='id'+value.substring(1);
	      lowNode=getLowNodeById(parsedData,childId);
	      lowNode.id=childId;
      }else if(i==4){
      	lowNode={};
	      lowNode.id=parentNode.id+'#'+''+i+'';
	      if(value.indexOf('T')>-1){
	         lowNode.name='ORIENTATION：true';
	      }else if(value.indexOf('F')>-1){
	        lowNode.name='ORIENTATION：false';
	      }else{
	        lowNode.name='ORIENTATION';
	      }
      }
      	lowNode.color=parentNode.color;
	    lowNode.visible=false;
	    lowNode.children=[];
	    lowNode.assembly=parentNode.assembly;
	    lowNode.bbox=parentNode.bbox;
	    lowNode.shapes=[];
	    lowNode.lowChildren=[];
	    lowNode.stepfile=parentNode.stepfile;
	    lowNode.weight=parentNode.weight;
	    lowNode.rootNode=parentNode.rootNode;             
	    if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
	    parentNode.lowChildren.push(lowNode); 
  	}
  }else if(parentNode.name=='TOROIDAL_SURFACE'){
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==1){
        lowNode={};
        var childId='id'+value.substring(1);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==2){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name='MAJOR_RADIUS：'+value;
      }else if(i==3){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name='MINOR_RADIUS：'+value;
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);
    }
  }else if(parentNode.name=='CIRCLE'){
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==1){
        lowNode={};
        var childId='id'+value.substring(1);
        lowNode=getLowNodeById(parsedData,childId);
        lowNode.id=childId;
      }else if(i==2){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name='RADIUS：'+value;
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);
    }
  }else if(parentNode.name=='B_SPLINE_CURVE_WITH_KNOTS'){
    var names=['DEGREE','CONTROL_POINTS_LIST','CURVE_FORM','CLOSED_CURVE','SELF_INTERSECT','KNOT_MULTIPLICITIES','KNOTS','KNOT_SPEC'];
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==1||i==7){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1]+'：'+value;
      }else if(i==2){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1];
        var childStr=value.substring(0,value.length-1);
        lowNode.childStr=childStr;
      }else if(i==3||i==8){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1]+'：'+value.substring(1);
      }else if(i==4||i==5){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        if(value.indexOf('T')>-1){
          lowNode.name=names[i-1]+'：true';
        }else if(value.indexOf('F')>-1){
          lowNode.name=names[i-1]+'：false';
        }else{
          lowNode.name=names[i-1];
        }
      }else if(i==6){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1]+'：('+value;
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode); 
    }
  }else if(parentNode.name=='B_SPLINE_SURFACE_WITH_KNOTS'){
    var names=['U_DEGREE','V_DEGREE','CONTROL_POINTS_LIST','SURFACE_FORM','U_CLOSED','V_CLOSED','SELF_INTERSECT','U_MULTIPLICITIES','V_MULTIPLICITIES','U_KNOTS','V_KNOTS','KNOT_SPEC'];
    for(var i=1;i<parentNode.list.length;i++){
      var value=parentNode.list[i].value;
      var lowNode={};
      if(i==1||i==2||i==9||i==10||i==11){
        //去除字符串中的空格
        value = value.replace(/\s/g, "");
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1]+'：'+value;
      }else if(i==3){
        var childStr=value.substring(0,value.length-1);
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1];
        lowNode.childStr=childStr;
      }else if(i==4||i==12){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1]+'：'+trim(value.substring(1));
      }else if(i==5||i==6||i==7){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        if(value.indexOf('T')>-1){
          lowNode.name=names[i-1]+'：true';
        }else if(value.indexOf('F')>-1){
          lowNode.name=names[i-1]+'：false';
        }else{
          lowNode.name=names[i-1];
        }
      }else if(i==8){
        lowNode={};
        lowNode.id=parentNode.id+'#'+''+i+'';
        lowNode.name=names[i-1]+'：('+value;
      }
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);  
    }
  }else if(parentNode.name=='CONTROL_POINTS_LIST'){
    var str=parentNode.childStr;
    var list=[];
    var index=0;  
    var layer=0;
    var tempLayer=0;
    for(var j=0;j<str.length;j++){
      var indexChar=str.charAt(j);
      var temp="";
      if(j==str.length-1&&indexChar!=')'){
        var map={};
        if(index==0){
          temp=str.substring(index);
        }else{
          temp=str.substring(index+1);
        }
        map.key=tempLayer;
        map.value=temp;
        if(temp.length>0){
          list.push(map);
        }
      }
      if(indexChar==','&&tempLayer==0){
        var map={};
        if(index==0){
          temp=str.substring(index,j);
        }else{
          temp=str.substring(index+1,j);
        }
        map.key=tempLayer;
        map.value=temp;
        if(temp.length>0){
          list.push(map);
          index=j;
        }
      }
      if(indexChar=='('){
        layer++;
        if(tempLayer<layer){
          tempLayer=layer;
        }
      }
      if(indexChar==')'){
        layer--;
        if(layer==0){
          var map={};
          if(index==0){
            temp=str.substring(index, j+1);
          }else{
            temp=str.substring(index+2, j+1);
          }
          map.key=tempLayer;
          map.value=temp;
          list.push(map);
          index = j;
          tempLayer=0;
        }     
      }
    }
   for(var i=0;i<list.length;i++){
    var value=list[i].value;
    //去除字符串中的空格
    var reg = //s/g;
    value = value.replace(reg, "");
    var lowNode={};
    lowNode.id=parentNode.id+'#'+''+i+'';
    lowNode.name='LIST_CONTROL_POINTS';
    lowNode.childStr=value;
    lowNode.color=parentNode.color;
    lowNode.visible=false;
    lowNode.children=[];
    lowNode.assembly=parentNode.assembly;
    lowNode.bbox=parentNode.bbox;
    lowNode.shapes=[];
    lowNode.lowChildren=[];
    lowNode.stepfile=parentNode.stepfile;
    lowNode.weight=parentNode.weight;
    lowNode.rootNode=parentNode.rootNode;             
    if(ids.indexOf(lowNode.id)==-1){
      dataJson.nodes.push(lowNode);        
    }
    parentNode.lowChildren.push(lowNode); 
   }
  }else if(parentNode.name=='LIST_CONTROL_POINTS'){
    var str=parentNode.childStr;
    str=str.substring(1,str.length-1);
    //去除字符串中的空格
    str = str.replace(/\s/g, "");
    var values=[];
    values=str.substring(1).split(",");
    for(var i=0;i<values.length;i++){
      value=values[i];
      var childId='id'+value.substring(1);
      lowNode=getLowNodeById(parsedData,childId);
      lowNode.id=childId;
      lowNode.color=parentNode.color;
      lowNode.visible=false;
      lowNode.children=[];
      lowNode.assembly=parentNode.assembly;
      lowNode.bbox=parentNode.bbox;
      lowNode.shapes=[];
      lowNode.lowChildren=[];
      lowNode.stepfile=parentNode.stepfile;
      lowNode.weight=parentNode.weight;
      lowNode.rootNode=parentNode.rootNode;             
      if(ids.indexOf(lowNode.id)==-1){
        dataJson.nodes.push(lowNode);        
      }
      parentNode.lowChildren.push(lowNode);  
    }
  }

  return dataJson;
}
//根据nodes来创建links
function createLinksByNodes(dataJson){
  var nodes=dataJson.nodes;
  var links=dataJson.links;

  for(var i=0;i<nodes.length;i++){
  	var nodeId=nodes[i].id;
    var child=nodes[i].children;
    var lowChild=nodes[i].lowChildren;
 
     if(child.length!=0){
        for(var j=0;j<child.length;j++){
          var linkType={};
          linkType.name='parent-children';
          linkType.color=1;
          var linkTypes=[];
          linkTypes.push(linkType);
          var temp={id:nodes[i],source:0,target:0,value:1,linkNode:child[j],linkType:linkTypes,visible:true};
          if(links.indexOf(temp)==-1){
            links.push(temp);
          }           
        }

     }else if(child.length==0&&lowChild.length!=0){
        for(var j=0;j<lowChild.length;j++){
          var linkType={};
          linkType.name=lowChild[j].name;
          linkType.color=lowChild[j].color;
          var linkTypes=[];
          linkTypes.push(linkType);
          var temp={id:nodes[i],source:0,target:0,value:1,linkNode:lowChild[j],linkType:linkTypes,visible:false};
          if(links.indexOf(temp)==-1&&lowChild[j].count>0){
            links.push(temp);     
          }                      
        }                                              
     }
  }
  return dataJson;       
}