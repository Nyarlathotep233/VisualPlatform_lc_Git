package com.vp.designintent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vp.semanticcell.Body;
import com.vp.semanticcell.Head;
import com.vp.semanticcell.Instance;
import com.vp.semanticcell.SemanticCell;
import com.vp.semanticcell.SemanticLink;
import com.vp.semanticcell.Tail;
import com.vp.domain.Link;

public class DesignIntentKnowledge {
	
	/**
	 * 创建自定义设计意图语义元
	 * @param type
	 * @param semanticMap
	 * @param attribute
	 * @return
	 */
	public static SemanticCell createDesignIntentSemanticCell(String type, Map<String,String> semanticMap, Map<String, String> attribute){
		SemanticCell sc = new SemanticCell();
		sc.setType(type);
		
		Head head = new Head();
		SemanticLink semanticLink = new SemanticLink();
		semanticLink.setSemanticMap(semanticMap);
		head.setSemanticLink(semanticLink);
		sc.setHead(head);
		
		Body body = new Body();
		Instance instance = new Instance();
		//语义元的实例id即为该设计意图类型
		instance.setInstanceId(type);
		body.setInstance(instance);
		sc.setBody(body);
		
		Tail tail = new Tail();
		tail.setAttribute(attribute);
		sc.setTail(tail);
		
		return sc;
	}

	/**
	 * 初始化设计意图知识图谱中的节点
	 * @return
	 */
	public static List<SemanticCell> initDesignIntentKnowledgeNode(){
		List<SemanticCell> dIList = new ArrayList<SemanticCell>();
		
		//创建根节点DesignIntent
		Map<String,String> semanticMap1 = new HashMap<String, String>();
		//设计历史
		semanticMap1.put("ConstructionHistory", "ConstructionHistory");
		//参数
		semanticMap1.put("Parameters", "Parameters");
		//约束
		semanticMap1.put("Constraints", "Constraints");
		//特征
		semanticMap1.put("Feature", "Feature");
		Map<String, String> attribute1 = new HashMap<String, String>();
		SemanticCell sc1 = createDesignIntentSemanticCell("DesignIntent", semanticMap1, attribute1);
		dIList.add(sc1);
		
		//创建节点ConstructionHistory
		Map<String,String> semanticMap2 = new HashMap<String, String>();
		//设计步骤
		semanticMap2.put("DesignProcedure", "DesignProcedure");
		Map<String, String> attribute2 = new HashMap<String, String>();
		SemanticCell sc2 = createDesignIntentSemanticCell("ConstructionHistory", semanticMap2, attribute2);
		dIList.add(sc2);
		
		//创建节点Parameters
		Map<String,String> semanticMap3 = new HashMap<String, String>();
		//显式参数
		semanticMap3.put("ExplicitParameters", "ExplicitParameters");
		//隐式参数
		semanticMap3.put("ImplicitParameters", "ImplicitParameters");
		Map<String, String> attribute3 = new HashMap<String, String>();
		SemanticCell sc3 = createDesignIntentSemanticCell("Parameters", semanticMap3, attribute3);
		dIList.add(sc3);
		
		//创建节点Constraints
		Map<String,String> semanticMap4 = new HashMap<String, String>();
		//显式约束
		semanticMap4.put("ExplicitConstraints", "ExplicitConstraints");
		//隐式约束
		semanticMap4.put("ImplicitConstraints", "ImplicitConstraints");
		Map<String, String> attribute4 = new HashMap<String, String>();
		SemanticCell sc4 = createDesignIntentSemanticCell("Constraints", semanticMap4, attribute4);
		dIList.add(sc4);
		
		//创建节点Feature
		Map<String,String> semanticMap5 = new HashMap<String, String>();
		//参数特征
		semanticMap5.put("ParameterFeature", "ParameterFeature");
		//草图特征
		semanticMap5.put("SketchFeature", "SketchFeature");
		//基于草图的特征
		semanticMap5.put("BaseSketchFeature", "BaseSketchFeature");
		//局部特征
		semanticMap5.put("LocalFeature", "LocalFeature");
		//组合特征
		semanticMap5.put("CombinationFeature", "CombinationFeature");
		Map<String, String> attribute5 = new HashMap<String, String>();
		SemanticCell sc5 = createDesignIntentSemanticCell("Feature", semanticMap5, attribute5);
		dIList.add(sc5);
		
		//创建节点LocalFeature
		Map<String,String> semanticMap6 = new HashMap<String, String>();
		//孔特征
		semanticMap6.put("HoleFeature", "HoleFeature");
		//圆角特征
		semanticMap6.put("RoundFeature", "RoundFeature");
		Map<String, String> attribute6 = new HashMap<String, String>();
		SemanticCell sc6 = createDesignIntentSemanticCell("LocalFeature", semanticMap6, attribute6);
		dIList.add(sc6);
		
		//创建节点HoleFeature
		Map<String,String> semanticMap7 = new HashMap<String, String>();
		//通孔特征
		semanticMap7.put("ThroughHoleFeature", "ThroughHoleFeature");
		//盲孔特征
		semanticMap7.put("BlindHoleFeature", "BlindHoleFeature");
		Map<String, String> attribute7 = new HashMap<String, String>();
		SemanticCell sc7 = createDesignIntentSemanticCell("HoleFeature", semanticMap7, attribute7);
		dIList.add(sc7);
		
		//创建节点ThroughHoleFeature
		Map<String,String> semanticMap8 = new HashMap<String, String>();
		Map<String, String> attribute8 = new HashMap<String, String>();
		SemanticCell sc8 = createDesignIntentSemanticCell("ThroughHoleFeature", semanticMap8, attribute8);
		dIList.add(sc8);
		
		//创建节点BlindHoleFeature
		Map<String,String> semanticMap9 = new HashMap<String, String>();
		Map<String, String> attribute9 = new HashMap<String, String>();
		SemanticCell sc9 = createDesignIntentSemanticCell("BlindHoleFeature", semanticMap9, attribute9);
		dIList.add(sc9);
		
		return dIList;
	}
	
	/**
	 * 初始化设计意图知识图谱中的连线
	 * @param dIList
	 * @return
	 */
	public static List<Link> initDesignIntentKnowledgeLink(List<SemanticCell> dIList){
		List<Link> links = new ArrayList<Link>();
		for(SemanticCell sc : dIList){
			Map<String, String> semanticMap = sc.getHead().getSemanticLink().getSemanticMap();
			if(!semanticMap.isEmpty()){
				for(Entry<String, String> entry : semanticMap.entrySet()){
					Link link = new Link();
					link.setFromId(sc.getBody().getInstance().getInstanceId());
					link.setToId(entry.getValue());
					link.setType("semanticLink");
					SemanticCell targetSC = getSemanticCellByInstanceId(dIList, entry.getValue());
					String name = "";
					if(targetSC != null){
						name = sc.getType() + "_contains_" + targetSC.getType();
					}
					link.setName(name);
					link.setWeight(1.0);
					links.add(link);
				}
			}
		}
		return links;
	}

	/**
	 * 根据instanceId获取语义元
	 * @param dIList
	 * @param instanceId
	 * @return
	 */
	public static SemanticCell getSemanticCellByInstanceId(
			List<SemanticCell> dIList, String instanceId) {
		for(SemanticCell semanticCell : dIList){
			if(semanticCell.getBody().getInstance().getInstanceId().equals(instanceId)){
				return semanticCell;
			}
		}
		return null; 
	}
}
