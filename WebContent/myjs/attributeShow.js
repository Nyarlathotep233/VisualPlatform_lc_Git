//删除字符串左右两端的空格
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function showAttributes(oShowAttributes,node){
	while(oShowAttributes.hasChildNodes()){
        oShowAttributes.removeChild(oShowAttributes.firstChild);
    }

    var textNode1=document.createTextNode("id："+node.id);
    var spanNode1=document.createElement("span");
    spanNode1.appendChild(textNode1);
    oShowAttributes.appendChild(spanNode1);

    var br = document.createElement("br");
    oShowAttributes.appendChild(br);

    var textNode2=document.createTextNode("name："+node.name);
    var spanNode2=document.createElement("span");
    spanNode2.appendChild(textNode2);
    oShowAttributes.appendChild(spanNode2);  

    if(node.name=='MANIFOLD_SOLID_BREP'){
   		var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("outer："));
        var childId='id'+node.list[1][0].substring(1);
		var optionNode = document.createElement("option");
		optionNode.appendChild(document.createTextNode(childId));
		var selectNode=document.createElement("select");
		selectNode.appendChild(optionNode);
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);     
    }else if(node.name=='PLANE'){
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("position："));
        var childId='id'+node.list[1][0].substring(1);
		var optionNode = document.createElement("option");
		optionNode.appendChild(document.createTextNode(childId));
		var selectNode=document.createElement("select");
		selectNode.appendChild(optionNode);
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='VERTEX_POINT'){
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("vertex_geometry："));
        var childId='id'+node.list[1][0].substring(1);
		var optionNode = document.createElement("option");
		optionNode.appendChild(document.createTextNode(childId));
		var selectNode=document.createElement("select");
		selectNode.appendChild(optionNode);
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='CLOSED_SHELL'){
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("cfs_faces："));
        var childId=node.id+'#1';;
		var optionNode = document.createElement("option");
		optionNode.appendChild(document.createTextNode(childId));
		var selectNode=document.createElement("select");
		selectNode.appendChild(optionNode);
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='CFS_FACES'){
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("set of face："));
   		var selectNode=document.createElement("select");
   		var str=node.childStr;
	    var idArray=str.split(',');
	    for(var i=0;i<idArray.length;i++){
	      	if(idArray[i].length>0){
		        var lowChildrenId='id'+trim(idArray[i]).substring(1);
		        var optionNode = document.createElement("option");
				optionNode.appendChild(document.createTextNode(lowChildrenId));
				selectNode.appendChild(optionNode);
		    }
		}	
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='ADVANCED_FACE'){
    	alert("需要调用吗")
    	var childId1=node.id+'#1';
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("bounds："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var childId2='id'+node.list[2][0].substring(2);
        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("face_geometry："));
   		var optionNode2 = document.createElement("option");
   		optionNode2.appendChild(document.createTextNode(childId2));
   		var selectNode2=document.createElement("select");
   		selectNode2.appendChild(optionNode2);
   		spanNode2.appendChild(selectNode2);
    	oShowAttributes.appendChild(spanNode2);

    	var text='';
    	var value=node.list[3][0];
    	if(value.indexOf('T')>-1){
          text='true';
        }else if(value.indexOf('F')>-1){
          text='false';
        }else{
          text='';
        }
    	var childId3=node.id+'#3';
        var spanNode3=document.createElement("span");
   		spanNode3.appendChild(document.createTextNode("same_sense："+text));
    	oShowAttributes.appendChild(spanNode3);
    }else if(node.name=='BOUNDS'){
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("set of face_bound："));
   		var selectNode=document.createElement("select");
   		var str=node.childStr;
	    var idArray=str.split(',');
	    for(var i=0;i<idArray.length;i++){
	      	if(idArray[i].length>0){
		        var lowChildrenId='id'+trim(idArray[i]).substring(1);
		        var optionNode = document.createElement("option");
				optionNode.appendChild(document.createTextNode(lowChildrenId));
				selectNode.appendChild(optionNode);
		    }
		}	
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='EDGE_LIST'){
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("list of oriented_edge："));
   		var selectNode=document.createElement("select");
   		var str=node.childStr;
	    var idArray=str.split(',');
	    for(var i=0;i<idArray.length;i++){
	      	if(idArray[i].length>0){
		        var lowChildrenId='id'+trim(idArray[i]).substring(1);
		        var optionNode = document.createElement("option");
				optionNode.appendChild(document.createTextNode(lowChildrenId));
				selectNode.appendChild(optionNode);
		    }
		}	
		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='FACE_BOUND'||node.name=='FACE_OUTER_BOUND'){
    	var childId1='id'+node.list[1][0].substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("bound："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var text='';
    	var value=node.list[2][0];
    	if(value.indexOf('T')>-1){
          text='true';
        }else if(value.indexOf('F')>-1){
          text='false';
        }else{
          text='null';
        }
        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("orientation："+text));
    	oShowAttributes.appendChild(spanNode2);
    }else if(node.name=='PRODUCT_DEFINITION_SHAPE'){
    	var value=node.list[1].value;
    	var str=value.substring(1,value.length-1);
    	var text='';
    	if(str.length>0){
          text=str;
        }else{
          text='null';
        }      
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("description："+text));
    	oShowAttributes.appendChild(spanNode1);

    	var childId2='id'+node.list[2][0].substring(2);
        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("definition："));
   		var optionNode2 = document.createElement("option");
   		optionNode2.appendChild(document.createTextNode(childId2));
   		var selectNode2=document.createElement("select");
   		selectNode2.appendChild(optionNode2);
   		spanNode2.appendChild(selectNode2);
    	oShowAttributes.appendChild(spanNode2);
    }else if(node.name=='EDGE_LOOP'){
    	var childId=node.id+'#1';
    	var spanNode=document.createElement("span");
   		spanNode.appendChild(document.createTextNode("edge_list："));
   		var optionNode = document.createElement("option");
   		optionNode.appendChild(document.createTextNode(childId));
   		var selectNode=document.createElement("select");
   		selectNode.appendChild(optionNode);
   		spanNode.appendChild(selectNode);
    	oShowAttributes.appendChild(spanNode);
    }else if(node.name=='AXIS2_PLACEMENT_3D'){
    	var childId1='id'+node.list[1][0].substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("location："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var childId2='id'+node.list[2][0].substring(2);
        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("axis："));
   		var optionNode2 = document.createElement("option");
   		optionNode2.appendChild(document.createTextNode(childId2));
   		var selectNode2=document.createElement("select");
   		selectNode2.appendChild(optionNode2);
   		spanNode2.appendChild(selectNode2);
    	oShowAttributes.appendChild(spanNode2);

    	var childId3='id'+node.list[3][0].substring(3);
        var spanNode3=document.createElement("span");
   		spanNode3.appendChild(document.createTextNode("ref_direction："));
   		var optionNode3 = document.createElement("option");
   		optionNode3.appendChild(document.createTextNode(childId3));
   		var selectNode3=document.createElement("select");
   		selectNode3.appendChild(optionNode3);
   		spanNode3.appendChild(selectNode3);
    	oShowAttributes.appendChild(spanNode3);
    }else if(node.name=='CYLINDRICAL_SURFACE'){
    	var childId1='id'+node.list[1][0].substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("position："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("radius："+node.list[2][0]));
    	oShowAttributes.appendChild(spanNode2);
    }else if(node.name=='ORIENTED_EDGE'){
    	var childId1='id'+node.list[3][0].substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("edge_element："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var text='';
    	var value=node.list[4][0];
    	if(value.indexOf('T')>-1){
          text='true';
        }else if(value.indexOf('F')>-1){
          text='false';
        }else{
          text='null';
        }
        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("orientation："+text));
    	oShowAttributes.appendChild(spanNode2);
    }else if(node.name=='EDGE_CURVE'){
    	var childId1='id'+node.list[1][0].substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("edge_start："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var childId2='id'+node.list[2][0].substring(2);
        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("edge_end："));
   		var optionNode2 = document.createElement("option");
   		optionNode2.appendChild(document.createTextNode(childId2));
   		var selectNode2=document.createElement("select");
   		selectNode2.appendChild(optionNode2);
   		spanNode2.appendChild(selectNode2);
    	oShowAttributes.appendChild(spanNode2);

    	var childId3='id'+node.list[3][0].substring(3);
        var spanNode3=document.createElement("span");
   		spanNode3.appendChild(document.createTextNode("edge_geometry："));
   		var optionNode3 = document.createElement("option");
   		optionNode3.appendChild(document.createTextNode(childId3));
   		var selectNode3=document.createElement("select");
   		selectNode3.appendChild(optionNode3);
   		spanNode3.appendChild(selectNode3);
    	oShowAttributes.appendChild(spanNode3);

    	var text='';
    	var value=node.list[4][0];
    	if(value.indexOf('T')>-1){
          text='true';
        }else if(value.indexOf('F')>-1){
          text='false';
        }else{
          text='';
        }
        var spanNode4=document.createElement("span");
   		spanNode4.appendChild(document.createTextNode("same_sense："+text));
    	oShowAttributes.appendChild(spanNode4);
    }else if(node.name=='TOROIDAL_SURFACE'){
    	var childId1='id'+node.list[1].value.substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("edge_start："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

        var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("major_radius："+node.list[2][0]));
    	oShowAttributes.appendChild(spanNode2);

    	var spanNode3=document.createElement("span");
   		spanNode3.appendChild(document.createTextNode("minor_radius："+node.list[3][0]));
    	oShowAttributes.appendChild(spanNode3);
    }else if(node.name=='CIRCLE'){
    	var childId1='id'+node.list[1].value.substring(1);
        var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("position："));
   		var optionNode1 = document.createElement("option");
   		optionNode1.appendChild(document.createTextNode(childId1));
   		var selectNode1=document.createElement("select");
   		selectNode1.appendChild(optionNode1);
   		spanNode1.appendChild(selectNode1);
    	oShowAttributes.appendChild(spanNode1);

    	var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("radius："+node.list[2][0]));
    	oShowAttributes.appendChild(spanNode2);
    }else if(node.name=='B_SPLINE_CURVE_WITH_KNOTS'){
    	var spanNode1=document.createElement("span");
   		spanNode1.appendChild(document.createTextNode("degree："+node.list[1][0]));
    	oShowAttributes.appendChild(spanNode1);

    	var childId2=node.id+'#2';
    	var spanNode2=document.createElement("span");
   		spanNode2.appendChild(document.createTextNode("control_points_list："+childId2));
    	oShowAttributes.appendChild(spanNode2);

    	var spanNode3=document.createElement("span");
   		spanNode3.appendChild(document.createTextNode("curve_form："+node.list[3][0]));
    	oShowAttributes.appendChild(spanNode3);

    	var text1='';
    	var value1=node.list[4][0];
    	if(value1.indexOf('T')>-1){
          text1='true';
        }else if(value1.indexOf('F')>-1){
          text1='false';
        }else{
          text1='';
        }
        var spanNode4=document.createElement("span");
   		spanNode4.appendChild(document.createTextNode("closed_curve："+text1));
   		oShowAttributes.appendChild(spanNode4);

   		var text2='';
    	var value2=node.list[5][0];
    	if(value2.indexOf('T')>-1){
          text2='true';
        }else if(value2.indexOf('F')>-1){
          text2='false';
        }else{
          text2='';
        }
        var spanNode5=document.createElement("span");
   		spanNode5.appendChild(document.createTextNode("self_intersect："+text2));
   		oShowAttributes.appendChild(spanNode5);

    	var spanNode6=document.createElement("span");
   		spanNode6.appendChild(document.createTextNode("knot_multiplicities：("+node.list[6][0]));
    	oShowAttributes.appendChild(spanNode6);

    	var spanNode7=document.createElement("span");
   		spanNode7.appendChild(document.createTextNode("knots："+node.list[7][0]));
    	oShowAttributes.appendChild(spanNode7);

    	var spanNode8=document.createElement("span");
   		spanNode8.appendChild(document.createTextNode("knot_spec："+node.list[8][0]));
    	oShowAttributes.appendChild(spanNode8);
    }else if(node.name=='B_SPLINE_SURFACE_WITH_KNOTS'){
    	var names=['u_degree','v_degree','control_points_list','surface_form','u_closed','v_closed','SELF_INTERSECT','U_MULTIPLICITIES','V_MULTIPLICITIES','U_KNOTS','V_KNOTS','KNOT_SPEC'];
    	for(var i=1;i<node.list.length;i++){
    		if(i==1||i==2){
    			var value=node.list[i][0];
		        var spanNode=document.createElement("span");
		   		spanNode.appendChild(document.createTextNode(names[i-1]+'：'+value));
		    	oShowAttributes.appendChild(spanNode);
    		}else if(i==3){
		        var childId=node.id+'#3';
		    	var spanNode=document.createElement("span");
		    	spanNode.appendChild(document.createTextNode(names[i-1]+'：'));
		    	var optionNode = document.createElement("option");
		   		optionNode.appendChild(document.createTextNode(childId));
		   		var selectNode=document.createElement("select");
		   		selectNode.appendChild(optionNode);
				spanNode.appendChild(selectNode);   		
		    	oShowAttributes.appendChild(spanNode);
		    }else if(i==4||i==12){
		    	var value=node.list[i][0];
		    	var spanNode=document.createElement("span");
		   		spanNode.appendChild(document.createTextNode(names[i-1]+'：'+trim(value.substring(1))));
		    	oShowAttributes.appendChild(spanNode);
		    }else if(i==5||i==6||i==7){
		    	var value=node.list[i][0];
		        var text='';
		    	if(value.indexOf('T')>-1){
		          text='true';
		        }else if(value.indexOf('F')>-1){
		          text='false';
		        }else{
		          text='';
		        }
		        var spanNode=document.createElement("span");
		   		spanNode.appendChild(document.createTextNode(names[i-1]+'：'+text));
		   		oShowAttributes.appendChild(spanNode);
		    }else if(i==8){
		    	var value=node.list[i][1];
		    	var spanNode=document.createElement("span");
		   		spanNode.appendChild(document.createTextNode(names[i-1]+'：('+value));
		    	oShowAttributes.appendChild(spanNode);
		    }else if(i==9||i==10||i==11){
		    	var value=node.list[i][1];
		        var spanNode=document.createElement("span");
		   		spanNode.appendChild(document.createTextNode(names[i-1]+'：'+value));
		    	oShowAttributes.appendChild(spanNode);
		    }
    	}
    }else if(node.name=='CONTROL_POINTS_LIST'){
    	var str=node.childStr;
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
	    var spanNode=document.createElement("span");
    	spanNode.appendChild(document.createTextNode('LIST_CONTROL_POINTS：'));
    	var selectNode=document.createElement("select");
	   for(var i=0;i<list.length;i++){
	    var value=list[i].value;
	    var childId=node.id+'#'+''+(i+1)+'';   	
    	var optionNode = document.createElement("option");
   		optionNode.appendChild(document.createTextNode(childId));  		
   		selectNode.appendChild(optionNode);		  		   	
	   }
	   spanNode.appendChild(selectNode); 
	   oShowAttributes.appendChild(spanNode);	
	}else if(node.name=='LIST_CONTROL_POINTS'){
		var str=node.childStr;
	    var values=[];
	    values=str.split(",");
	    var spanNode=document.createElement("span");
    	spanNode.appendChild(document.createTextNode('lis of cartesian_point：'));
    	var selectNode=document.createElement("select");
	    for(var i=0;i<values.length;i++){
	    	value=values[i];
	    	var childId='id'+value.substring(1);
	    	var optionNode = document.createElement("option");
	   		optionNode.appendChild(document.createTextNode(childId));  		
	   		selectNode.appendChild(optionNode);	
	    }
	    spanNode.appendChild(selectNode); 
	    oShowAttributes.appendChild(spanNode);
	}else if(node.name=='CARTESIAN_POINT'){
		var value="("+node.list[1][1];
		var spanNode=document.createElement("span");
    	spanNode.appendChild(document.createTextNode('coordinates：'+value));
	    oShowAttributes.appendChild(spanNode);
	}  
}