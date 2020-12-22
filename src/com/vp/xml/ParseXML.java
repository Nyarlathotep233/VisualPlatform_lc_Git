package com.vp.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.vp.domain.Link;
import com.vp.domain.Point;
import com.vp.sc_assembly.AssemblyBody;
import com.vp.sc_assembly.AssemblyGeometryLink;
import com.vp.sc_assembly.AssemblyHead;
import com.vp.sc_assembly.AssemblyTail;
import com.vp.sc_assembly.Child;
import com.vp.sc_assembly.SemanticAssembly;
import com.vp.semanticcell.Body;
import com.vp.semanticcell.GeometryLink;
import com.vp.semanticcell.Head;
import com.vp.semanticcell.Instance;
import com.vp.semanticcell.SemanticCell;
import com.vp.semanticcell.SemanticLink;
import com.vp.semanticcell.Tail;

public class ParseXML {
	//读取文件的操作
	public static Element getRoot(String fileName) throws Exception{
		
		SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(new File(fileName));

        // 获取根元素
        Element root = document.getRootElement();
        
        return root;
	}
	
	//获取所有的信息节点List
	public static List<SemanticCell> getSemanticCells(String fileName) throws Exception{
		List<SemanticCell> scs = new ArrayList<SemanticCell>();
		Element root = ParseXML.getRoot(fileName);
		List<Element> classLists = root.elements("Semantic");
		for(Element e : classLists){
			SemanticCell sc = new SemanticCell();
			String type = e.attributeValue("type");
			sc.setType(type);
			
			//处理head部分
			Element headElment = e.element("head");
			Element geometryLinkElement = headElment.element("geometry_link");
			Map<String, String> geometryMap = new HashMap<String, String>();
			for(Element e2 : geometryLinkElement.elements()){
				System.out.println(e2.getName());
				System.out.println( e2.getText());
				geometryMap.put(e2.getName(), e2.getText());
			}
			Element semanticLinkElement = headElment.element("semantic_link");
			Map<String, String> semanticMap = new HashMap<String, String>();
			for(Element e2 : semanticLinkElement.elements()){
				semanticMap.put(e2.getName(), e2.getText());
			}
			
			Head head = new Head();
			GeometryLink geometryLink = new GeometryLink();
			geometryLink.setGeometryMap(geometryMap);
			
			SemanticLink semanticLink = new SemanticLink();
			semanticLink.setSemanticMap(semanticMap);

			head.setGeometryLink(geometryLink);
			head.setSemanticLink(semanticLink);
			sc.setHead(head);
			
			//处理body部分
			Body body = new Body();
			Instance instance = new Instance();
			Element bodyElment = e.element("body");
			Element instanceElemnt = bodyElment.element("Instance");
			String instanceId = instanceElemnt.attributes().get(0).getValue();
			instance.setInstanceId(instanceId);
			
			Element elementfaceElement = instanceElemnt.element("elementface");			
			if(elementfaceElement != null) {
				System.out.println(elementfaceElement.getText());
				String elementFace = elementfaceElement.getText();
				instance.setElementface(elementFace);
			}
			//针对direction和cartesian_point，需要拿到body里的坐标		
			Element coordinateElement = instanceElemnt.element("coordinates");
			Element directionElemnt = instanceElemnt.element("direction_ratios");
			Element radiusElement = instanceElemnt.element("radius");			
			String x = null;
			String y = null;
			String z = null;
			Point point = new Point();
			if(type.equals("direction")){
				String d_coordinates = directionElemnt.getText();
				x = d_coordinates.substring(d_coordinates.indexOf("(")+1, d_coordinates.indexOf(","));
				y = d_coordinates.substring(d_coordinates.indexOf(",")+1, d_coordinates.lastIndexOf(","));
				z = d_coordinates.substring(d_coordinates.lastIndexOf(",")+1, d_coordinates.indexOf(")"));
				point.setCoordinates_x(Double.valueOf(x).doubleValue());
				point.setCoordinates_y(Double.valueOf(y).doubleValue());
				point.setCoordinates_z(Double.valueOf(z).doubleValue());
				instance.setCoordinates(point);
				
			} 
			if(type.equals("cartesian_point")){
				String c_coordinates = coordinateElement.getText();
				x = c_coordinates.substring(c_coordinates.indexOf("(")+1, c_coordinates.indexOf(","));
				y = c_coordinates.substring(c_coordinates.indexOf(",")+1, c_coordinates.lastIndexOf(","));
				z = c_coordinates.substring(c_coordinates.lastIndexOf(",")+1, c_coordinates.indexOf(")"));
				point.setCoordinates_x(Double.valueOf(x).doubleValue());
				point.setCoordinates_y(Double.valueOf(y).doubleValue());
				point.setCoordinates_z(Double.valueOf(z).doubleValue());
				instance.setCoordinates(point);
			}
			if(type.equals("circle")){
				String radius = radiusElement.getText();
				instance.setRadius(radius);
			}
			
			body.setInstance(instance);
			sc.setBody(body);
			
			//处理tail部分
			Tail tail = new Tail();	
			Element tailElment = e.element("tail");
			Map<String, String> attribute = new HashMap<String, String>();
			for(Element e3 : tailElment.elements()){
				attribute.put(e3.getName(), e3.getText());
			}
			tail.setAttribute(attribute);
			sc.setTail(tail);
			
			System.out.println("complete:" + sc.getType() + " : " + sc.getBody().getInstance().getInstanceId());
			scs.add(sc);
		}
		return scs;
	}
	
	/**
	 * 根据节点建立连接
	 * @param scs
	 * @return
	 */
	public static List<Link> getLinks(List<SemanticCell> scs){
		List<Link> links = new ArrayList<Link>();
		for(SemanticCell sc : scs){
			Map<String, String> geometryMap = sc.getHead().getGeometryLink().getGeometryMap();
			if(!geometryMap.isEmpty()){
				for(Entry<String, String> entry : geometryMap.entrySet()){
					if(!entry.getValue().contains("(")){
						Link link = new Link();
						link.setFromId(sc.getBody().getInstance().getInstanceId());
						link.setToId(entry.getValue());
						link.setType("geometryLink");
						SemanticCell targetSC = getSemanticCellByInstanceId(scs, entry.getValue());
						String name = "";
						if(targetSC != null){
							name = sc.getType() + "_contains_" + targetSC.getType();
						}
						link.setName(name);
						link.setWeight(1.0);
						links.add(link);
						System.out.println("complete : " + link.getName());
					}else{
						String value = entry.getValue();
						value = value.substring(1, value.lastIndexOf(')'));
						String[] targetIds = value.split(",");
						for(int i = 0; i < targetIds.length; i++){
							Link link = new Link();
							link.setFromId(sc.getBody().getInstance().getInstanceId());
							link.setToId(targetIds[i]);
							link.setType("geometryLink");
							SemanticCell targetSC = getSemanticCellByInstanceId(scs, targetIds[i]);
							String name = "";
							if(targetSC != null){
								name = sc.getType() + "_contains_" + targetSC.getType();
							}
							link.setName(name);
							link.setWeight(1.0);
							links.add(link);
							System.out.println("complete : " + link.getName());
						}
					}
				}
			}
			Map<String, String> semanticMap = sc.getHead().getSemanticLink().getSemanticMap();
			if(!semanticMap.isEmpty()){
				for(Entry<String, String> entry : semanticMap.entrySet()){
					if(!entry.getValue().contains("(")){//不包含（的情况
						Link link = new Link();
						link.setFromId(sc.getBody().getInstance().getInstanceId());
						link.setToId(entry.getValue());
						link.setType("semanticLink");
						SemanticCell targetSC = getSemanticCellByInstanceId(scs, entry.getValue());
						String name = "";
						if(targetSC != null){
							name = sc.getType() + "_contains_" + targetSC.getType();
						}
						link.setName(name);
						link.setWeight(1.0);
						link.setFromToId(sc.getBody().getInstance().getInstanceId()+"-->"+entry.getValue());
						links.add(link);
						System.out.println("complete : " + link.getName());
					}else{//包含（的情况
						String value = entry.getValue();
						value = value.substring(1, value.lastIndexOf(')'));
						String[] targetIds = value.split(",");
						for(int i = 0; i < targetIds.length; i++){
							Link link = new Link();
							link.setFromId(sc.getBody().getInstance().getInstanceId());
							link.setToId(targetIds[i]);
							link.setType("semanticLink");
							SemanticCell targetSC = getSemanticCellByInstanceId(scs, targetIds[i]);
							String name = "";
							if(targetSC != null){
								name = sc.getType() + "_contains_" + targetSC.getType();
							}
							link.setName(name);
							link.setWeight(1.0);
							link.setFromToId(sc.getBody().getInstance().getInstanceId()+"-->"+entry.getValue());
							links.add(link);
							System.out.println("complete : " + link.getName());
						}
					}
				}
			}
		}
		return links;
	}
	
	/**
	 * 根据instanceId获取语义元
	 * @param scs
	 * @param instanceId
	 * @return
	 */
	public static SemanticCell getSemanticCellByInstanceId(List<SemanticCell> scs, String instanceId){
		for(SemanticCell semanticCell : scs){
			if(semanticCell.getBody().getInstance().getInstanceId().equals(instanceId)){
				return semanticCell;
			}
		}
		return null; 
	}
	
	/**
	 * 以下用于解析装配体语义元,主要包含设计意图，数字孪生的非语义信息。
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<SemanticAssembly> getAssemblySemantics(String fileName) throws Exception{
		List<SemanticAssembly> scs = new ArrayList<SemanticAssembly>();
		Element root = ParseXML.getRoot(fileName);
		List<Element> classLists = root.elements("Semantic");
		for(Element e : classLists){
			SemanticAssembly sc = new SemanticAssembly();
			String type = e.attributeValue("type");
			sc.setType(type);
			
			//处理head部分
			Element headElment = e.element("head");
			Element geometryLinkElement = headElment.element("geometry_link");		
			Map<String, String> childMap = new HashMap<String, String>();
			for(Element e2 : geometryLinkElement.elements()){
				String refValue = e2.attributeValue("ref");
				String xformValue = e2.attributeValue("xform");
				childMap.put(refValue, xformValue);
			}
			Element semanticLinkElement = headElment.element("semantic_link");		
			String semanticLinkValue = semanticLinkElement.getText();
			
			AssemblyHead head = new AssemblyHead();
			AssemblyGeometryLink geometryLink = new AssemblyGeometryLink();
			Child child = new Child();
			child.setChild(childMap);			
			geometryLink.setChild(child);
			head.setGeometryLink(geometryLink);
			head.setSemanticLink(semanticLinkValue);
			sc.setHead(head);
			
			//处理body部分
			Element bodyElment = e.element("body");
			Element instanceElement = bodyElment.element("Instance");
			String instanceId = instanceElement.attributes().get(0).getValue();
			
			AssemblyBody body = new AssemblyBody();
			Instance instance = new Instance();
			instance.setInstanceId(instanceId);
			body.setInstance(instance);
			sc.setBody(body);
		
			//处理tail部分
			Element tailElment = e.element("tail");
			Element assemblyURLElement = tailElment.element("assembly_url");
			Element partRelationElement = tailElment.element("part_relation");
			Element constraintsTypeElement = tailElment.element("constraints_type");
			Element assemblyParameterElement = tailElment.element("assembly_parameter");
			Element functionInformationElement = tailElment.element("function_information");
			String assemblyURL = assemblyURLElement.getText();
			String partRelation = partRelationElement.getText();
			String constraintsType = constraintsTypeElement.getText();
			String assemblyParameter = assemblyParameterElement.getText();
			String functionInformation = functionInformationElement.getText();
			
			AssemblyTail tail = new AssemblyTail();
			tail.setAssemblyURL(assemblyURL);
			tail.setPartRelation(partRelation);
			tail.setConstraintsType(constraintsType);
			tail.setAssemblyParameter(assemblyParameter);
			tail.setFunctionInformation(functionInformation);

			sc.setTail(tail);
			System.out.println(sc);
			System.out.println("complete:" + sc.getType() + " : " + sc.getBody().getInstance().getInstanceId());
			scs.add(sc);
			System.out.println(scs);
		}
		return scs;
	}
	
	public static List<Link> getAssemblyLinks(List<SemanticCell> scs){
		List<Link> links = new ArrayList<Link>();
		for(SemanticCell sc : scs){
			Map<String, String> geometryMap = sc.getHead().getGeometryLink().getGeometryMap();
			if(!geometryMap.isEmpty()){
				for(Entry<String, String> entry : geometryMap.entrySet()){
					if(!entry.getValue().contains("(")){
						Link link = new Link();
						link.setFromId(sc.getBody().getInstance().getInstanceId());
						link.setToId(entry.getValue());
						link.setType("geometryLink");
						SemanticCell targetSC = getSemanticCellByInstanceId(scs, entry.getValue());
						String name = "";
						if(targetSC != null){
							name = sc.getType() + "_contains_" + targetSC.getType();
						}
						link.setName(name);
						link.setWeight(1.0);
						links.add(link);
						System.out.println("complete : " + link.getName());
					}else{
						String value = entry.getValue();
						value = value.substring(1, value.lastIndexOf(')'));
						String[] targetIds = value.split(",");
						for(int i = 0; i < targetIds.length; i++){
							Link link = new Link();
							link.setFromId(sc.getBody().getInstance().getInstanceId());
							link.setToId(targetIds[i]);
							link.setType("geometryLink");
							SemanticCell targetSC = getSemanticCellByInstanceId(scs, targetIds[i]);
							String name = "";
							if(targetSC != null){
								name = sc.getType() + "_contains_" + targetSC.getType();
							}
							link.setName(name);
							link.setWeight(1.0);
							links.add(link);
							System.out.println("complete : " + link.getName());
						}
					}
				}
			}
			Map<String, String> semanticMap = sc.getHead().getSemanticLink().getSemanticMap();
			if(!semanticMap.isEmpty()){
				for(Entry<String, String> entry : semanticMap.entrySet()){
					if(!entry.getValue().contains("(")){
						Link link = new Link();
						link.setFromId(sc.getBody().getInstance().getInstanceId());
						link.setToId(entry.getValue());
						link.setType("semanticLink");
						SemanticCell targetSC = getSemanticCellByInstanceId(scs, entry.getValue());
						String name = "";
						if(targetSC != null){
							name = sc.getType() + "_contains_" + targetSC.getType();
						}
						link.setName(name);
						link.setWeight(1.0);
						link.setFromToId(sc.getBody().getInstance().getInstanceId()+"-->"+entry.getValue());
						links.add(link);
						System.out.println("complete : " + link.getName());
					}else{
						String value = entry.getValue();
						value = value.substring(1, value.lastIndexOf(')'));
						String[] targetIds = value.split(",");
						for(int i = 0; i < targetIds.length; i++){
							Link link = new Link();
							link.setFromId(sc.getBody().getInstance().getInstanceId());
							link.setToId(targetIds[i]);
							link.setType("semanticLink");
							SemanticCell targetSC = getSemanticCellByInstanceId(scs, targetIds[i]);
							String name = "";
							if(targetSC != null){
								name = sc.getType() + "_contains_" + targetSC.getType();
							}
							link.setName(name);
							link.setWeight(1.0);
							link.setFromToId(sc.getBody().getInstance().getInstanceId()+"-->"+entry.getValue());
							links.add(link);
							System.out.println("complete : " + link.getName());
						}
					}
				}
			}
		}
		return links;
	}
	
//	public static List<SemanticPart> getPartSemantics(String fileName) throws Exception{
//		List<SemanticPart> scs = new ArrayList<SemanticPart>();
//		Element root = ParseXML.getRoot(fileName);
//		List<Element> classLists = root.elements("Semantic");
//		for(Element e : classLists){
//			SemanticPart sc = new SemanticPart();
//			String type = e.attributeValue("type");
//			sc.setType(type);
//			
//			//处理head部分
//			Element headElment = e.element("head");
//			Element geometryLinkElement = headElment.element("geometry_link");		
//			String geometryLinkValue = geometryLinkElement.getText();
//			
//			Element semanticLinkElement = headElment.element("semantic_link");
//			Element blockElement = semanticLinkElement.element("block");
//			String blockValue = blockElement.getText();
//						
//			PartHead head = new PartHead();
//			PartSemanticLink semanticLink=new PartSemanticLink();
//			semanticLink.setBlock(blockValue);
//			head.setGeometryLink(geometryLinkValue);
//			head.setSemanticLink(semanticLink);
//			sc.setHead(head);
//				
//			//处理body部分
//			Element bodyElment = e.element("body");
//			Element instanceElement = bodyElment.element("Instance");
//			String instanceId = instanceElement.attributes().get(0).getValue();
//			
//			PartBody body = new PartBody();
//			Instance instance = new Instance();
//			instance.setInstanceId(instanceId);
//			body.setInstance(instance);
//			sc.setBody(body);
//		
//			//处理tail部分
//			Element tailElment = e.element("tail");
//			Element partURLElement = tailElment.element("part_url");
//			Element partTypeElement = tailElment.element("part_type");
//			Element partMaterialElement = tailElment.element("part_material");
//			Element partParameterElement = tailElment.element("part_parameter");
//			Element functionInformationElement = tailElment.element("function_information");
//			String partURL = partURLElement.getText();
//			String partType = partTypeElement.getText();
//			String partMaterial = partMaterialElement.getText();
//			String partParameter = partParameterElement.getText();
//			String functionInformation = functionInformationElement.getText();
//
//			PartTail tail = new PartTail();
//			tail.setPartURL(partURL);
//			tail.setPartType(partType);
//			tail.setPartMaterial(partMaterial);
//			tail.setPartParameter(partParameter);
//			tail.setFunctionInformation(functionInformation);
//
//			sc.setTail(tail);
////			System.out.println(sc);
//			System.out.println("complete:" + sc.getType() + " : " + sc.getBody().getInstance().getInstanceId());
//			scs.add(sc);
//			System.out.println(scs);
//		}
//		return scs;
//	}
	
//	public static List<Object> getNewLinks(List<Object> scs){
//		List<Object> links = new ArrayList<Object>();
//		for(Object sc : scs){
//			
//			Map<String, String> geometryMap = sc.getHead().getGeometryLink().getGeometryMap();
//			if(!geometryMap.isEmpty()){
//				for(Entry<String, String> entry : geometryMap.entrySet()){
//					if(!entry.getValue().contains("(")){
//						Link link = new Link();
//						link.setFromId(sc.getBody().getInstance().getInstanceId());
//						link.setToId(entry.getValue());
//						link.setType("geometryLink");
//						SemanticCell targetSC = getSemanticCellByInstanceId(scs, entry.getValue());
//						String name = "";
//						if(targetSC != null){
//							name = sc.getType() + "_contains_" + targetSC.getType();
//						}
//						link.setName(name);
//						link.setWeight(1.0);
//						links.add(link);
//						System.out.println("complete : " + link.getName());
//					}else{
//						String value = entry.getValue();
//						value = value.substring(1, value.lastIndexOf(')'));
//						String[] targetIds = value.split(",");
//						for(int i = 0; i < targetIds.length; i++){
//							Link link = new Link();
//							link.setFromId(sc.getBody().getInstance().getInstanceId());
//							link.setToId(targetIds[i]);
//							link.setType("geometryLink");
//							SemanticCell targetSC = getSemanticCellByInstanceId(scs, targetIds[i]);
//							String name = "";
//							if(targetSC != null){
//								name = sc.getType() + "_contains_" + targetSC.getType();
//							}
//							link.setName(name);
//							link.setWeight(1.0);
//							links.add(link);
//							System.out.println("complete : " + link.getName());
//						}
//					}
//				}
//			}
//			Map<String, String> semanticMap = sc.getHead().getSemanticLink().getSemanticMap();
//			if(!semanticMap.isEmpty()){
//				for(Entry<String, String> entry : semanticMap.entrySet()){
//					if(!entry.getValue().contains("(")){
//						Link link = new Link();
//						link.setFromId(sc.getBody().getInstance().getInstanceId());
//						link.setToId(entry.getValue());
//						link.setType("semanticLink");
//						SemanticCell targetSC = getSemanticCellByInstanceId(scs, entry.getValue());
//						String name = "";
//						if(targetSC != null){
//							name = sc.getType() + "_contains_" + targetSC.getType();
//						}
//						link.setName(name);
//						link.setWeight(1.0);
//						link.setFromToId(sc.getBody().getInstance().getInstanceId()+"-->"+entry.getValue());
//						links.add(link);
//						System.out.println("complete : " + link.getName());
//					}else{
//						String value = entry.getValue();
//						value = value.substring(1, value.lastIndexOf(')'));
//						String[] targetIds = value.split(",");
//						for(int i = 0; i < targetIds.length; i++){
//							Link link = new Link();
//							link.setFromId(sc.getBody().getInstance().getInstanceId());
//							link.setToId(targetIds[i]);
//							link.setType("semanticLink");
//							SemanticCell targetSC = getSemanticCellByInstanceId(scs, targetIds[i]);
//							String name = "";
//							if(targetSC != null){
//								name = sc.getType() + "_contains_" + targetSC.getType();
//							}
//							link.setName(name);
//							link.setWeight(1.0);
//							link.setFromToId(sc.getBody().getInstance().getInstanceId()+"-->"+entry.getValue());
//							links.add(link);
//							System.out.println("complete : " + link.getName());
//						}
//					}
//				}
//			}
//		}
//		return links;
//	}
}

