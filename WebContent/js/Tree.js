/* $RCSfile: Tree.js,v $
 * $Revision: 1.3 $ $Date: 2012/08/03 17:00:47 $
 * Auth: Jochen Fritz (jfritz@steptools.com)
 * 
 * Copyright (c) 1991-2012 by STEP Tools Inc. 
 * All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and
 * its documentation is hereby granted, provided that this copyright
 * notice and license appear on all copies of the software.
 * 
 * STEP TOOLS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. STEP TOOLS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 		----------------------------------------
 *
 * HTML Tree class.  Implemented using nested <ul> elements in plain HTML.
 * After the nested lists are created, call the Tree() fucntion to attach the
 * expand and collapse functions.
 */


"use strict";

function Tree(list, menu,sg_context)
{

	var nodeList = list.childNodes;
	var nodes=[];
	var links=[];

	for(var i=0;i<nodeList.length;i++){
		if(nodeList[i].localName=='g'){
			nodes.push(nodeList[i]);
		}
	}
	
	var sg_contextArray=sg_context.parts;

	var temp=sg_contextArray[5];
	sg_contextArray[5]=sg_contextArray[10];
	sg_contextArray[10]=temp;

	// console.log(nodes)
	// console.log(sg_contextArray[5].sg.product.id)
	// console.log(sg_contextArray[6].sg.product.id)
	// console.log(sg_contextArray[7].sg.product.id)
	// console.log(sg_contextArray[8].sg.product.id)
	// console.log(sg_contextArray[9].sg.product.id)
	// console.log(sg_contextArray[10].sg.product.id)

	for(var i=0;i<nodes.length;i++){
		nodes[i].treenode=sg_contextArray[i+1];
	}


//    this.classes = classtab;

    // label_nodes(list);

    // console.log(list)
    //<ul class="unselected">

    for(var i=0;i<nodes.length;i++){

    	nodes[i].addEventListener("dblclick", function(e) {	

    			console.log(e)

				//这里是控制单击使得画面变化，加红色框
			    var tn = this.treenode;
			    var tree = tn.getRootNode();

			    var selected = tn.isSelected();	    

			    var preserve = e.shiftKey;

			    if (!preserve) {
				if (selected && tree.countSelects() > 0) 
				    selected = false;

				tree.clearSelection();
			    }

			    tn.select(!selected);
			    
			    tn.getRoot().viewer.draw();
			    // tn.redraw();

			    tree.updateTree();
			    
			    e.stopPropagation();	    
		}, true);

	    if (menu) {
			nodes[i].addEventListener("contextmenu", function(ev) {
			    ev.preventDefault();
			},  false);
			
			nodes[i].addEventListener("click", function(ev) {
			    return cancel_menu(ev, menu);},
					      true);
			nodes[i].addEventListener("contextmenu", function(ev) {
			    return cancel_menu(ev, menu);},
					      true);
			this.menu = menu;
	    }
    }

    
}

function cancel_menu(ev, menu)
{
    if (menu.isUp()) {
	menu.popdown();
	ev.stopPropagation();
	ev.preventDefault();
    }
    
}

function tree_click(ev) {

	console.log(ev)

    var target = ev.target;

    if (target.tagName == "LI") {
	if (target.classList.contains("leaf")) 
	    return;

	toggle(target);
	return;
    }
}

function label_nodes(parent)
{

    var nl = parent.childNodes;
    var found = false;

    for (var i=0; i<nl.length; i++) {
	var n = nl[i];
	
	if (n.nodeType == Node.ELEMENT_NODE) {

	    if (n.tagName == "LI") {
		found = true;
		label_nodes(n);
	    }

	    else if (n.tagName == "UL") {
		found = true;
		label_nodes(n);
	    }
	}
    }
    
    if (!found) {
	parent.classList.add("leaf");
    }
}

function toggle(n)
{
    n.classList.toggle("closed");
}

function expand_all(tree)
{

    var nl = tree.getElementsByTagName("LI");
    for (var i=0; i<nl.length; i++) {
	var n = nl[i];
	n.classList.remove("closed");
    }
}

function collapse_all(tree)
{
    var nl = tree.getElementsByTagName("LI");
    for (var i=0; i<nl.length; i++) {
	var n = nl[i];
	if (n.classList.contains("leaf"))
	    continue;
	n.classList.add("closed");
    }
}
