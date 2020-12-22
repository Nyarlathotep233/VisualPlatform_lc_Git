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
	 * �����Զ��������ͼ����Ԫ
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
		//����Ԫ��ʵ��id��Ϊ�������ͼ����
		instance.setInstanceId(type);
		body.setInstance(instance);
		sc.setBody(body);
		
		Tail tail = new Tail();
		tail.setAttribute(attribute);
		sc.setTail(tail);
		
		return sc;
	}

	/**
	 * ��ʼ�������ͼ֪ʶͼ���еĽڵ�
	 * @return
	 */
	public static List<SemanticCell> initDesignIntentKnowledgeNode(){
		List<SemanticCell> dIList = new ArrayList<SemanticCell>();
		
		//�������ڵ�DesignIntent
		Map<String,String> semanticMap1 = new HashMap<String, String>();
		//�����ʷ
		semanticMap1.put("ConstructionHistory", "ConstructionHistory");
		//����
		semanticMap1.put("Parameters", "Parameters");
		//Լ��
		semanticMap1.put("Constraints", "Constraints");
		//����
		semanticMap1.put("Feature", "Feature");
		Map<String, String> attribute1 = new HashMap<String, String>();
		SemanticCell sc1 = createDesignIntentSemanticCell("DesignIntent", semanticMap1, attribute1);
		dIList.add(sc1);
		
		//�����ڵ�ConstructionHistory
		Map<String,String> semanticMap2 = new HashMap<String, String>();
		//��Ʋ���
		semanticMap2.put("DesignProcedure", "DesignProcedure");
		Map<String, String> attribute2 = new HashMap<String, String>();
		SemanticCell sc2 = createDesignIntentSemanticCell("ConstructionHistory", semanticMap2, attribute2);
		dIList.add(sc2);
		
		//�����ڵ�Parameters
		Map<String,String> semanticMap3 = new HashMap<String, String>();
		//��ʽ����
		semanticMap3.put("ExplicitParameters", "ExplicitParameters");
		//��ʽ����
		semanticMap3.put("ImplicitParameters", "ImplicitParameters");
		Map<String, String> attribute3 = new HashMap<String, String>();
		SemanticCell sc3 = createDesignIntentSemanticCell("Parameters", semanticMap3, attribute3);
		dIList.add(sc3);
		
		//�����ڵ�Constraints
		Map<String,String> semanticMap4 = new HashMap<String, String>();
		//��ʽԼ��
		semanticMap4.put("ExplicitConstraints", "ExplicitConstraints");
		//��ʽԼ��
		semanticMap4.put("ImplicitConstraints", "ImplicitConstraints");
		Map<String, String> attribute4 = new HashMap<String, String>();
		SemanticCell sc4 = createDesignIntentSemanticCell("Constraints", semanticMap4, attribute4);
		dIList.add(sc4);
		
		//�����ڵ�Feature
		Map<String,String> semanticMap5 = new HashMap<String, String>();
		//��������
		semanticMap5.put("ParameterFeature", "ParameterFeature");
		//��ͼ����
		semanticMap5.put("SketchFeature", "SketchFeature");
		//���ڲ�ͼ������
		semanticMap5.put("BaseSketchFeature", "BaseSketchFeature");
		//�ֲ�����
		semanticMap5.put("LocalFeature", "LocalFeature");
		//�������
		semanticMap5.put("CombinationFeature", "CombinationFeature");
		Map<String, String> attribute5 = new HashMap<String, String>();
		SemanticCell sc5 = createDesignIntentSemanticCell("Feature", semanticMap5, attribute5);
		dIList.add(sc5);
		
		//�����ڵ�LocalFeature
		Map<String,String> semanticMap6 = new HashMap<String, String>();
		//������
		semanticMap6.put("HoleFeature", "HoleFeature");
		//Բ������
		semanticMap6.put("RoundFeature", "RoundFeature");
		Map<String, String> attribute6 = new HashMap<String, String>();
		SemanticCell sc6 = createDesignIntentSemanticCell("LocalFeature", semanticMap6, attribute6);
		dIList.add(sc6);
		
		//�����ڵ�HoleFeature
		Map<String,String> semanticMap7 = new HashMap<String, String>();
		//ͨ������
		semanticMap7.put("ThroughHoleFeature", "ThroughHoleFeature");
		//ä������
		semanticMap7.put("BlindHoleFeature", "BlindHoleFeature");
		Map<String, String> attribute7 = new HashMap<String, String>();
		SemanticCell sc7 = createDesignIntentSemanticCell("HoleFeature", semanticMap7, attribute7);
		dIList.add(sc7);
		
		//�����ڵ�ThroughHoleFeature
		Map<String,String> semanticMap8 = new HashMap<String, String>();
		Map<String, String> attribute8 = new HashMap<String, String>();
		SemanticCell sc8 = createDesignIntentSemanticCell("ThroughHoleFeature", semanticMap8, attribute8);
		dIList.add(sc8);
		
		//�����ڵ�BlindHoleFeature
		Map<String,String> semanticMap9 = new HashMap<String, String>();
		Map<String, String> attribute9 = new HashMap<String, String>();
		SemanticCell sc9 = createDesignIntentSemanticCell("BlindHoleFeature", semanticMap9, attribute9);
		dIList.add(sc9);
		
		return dIList;
	}
	
	/**
	 * ��ʼ�������ͼ֪ʶͼ���е�����
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
	 * ����instanceId��ȡ����Ԫ
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
