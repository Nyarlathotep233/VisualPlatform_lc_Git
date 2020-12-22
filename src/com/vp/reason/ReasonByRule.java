package com.vp.reason;

import java.awt.image.RescaleOp;
import java.util.List;
import java.util.Map;

import com.vp.semanticcell.SemanticCell;
import com.vp.base.Knowledge;
import com.vp.base.Rule;
import com.vp.designintent.DesignIntentKnowledge;
import com.vp.domain.Link;
import com.vp.service.KnowledgeService;
import com.vp.service.RuleService;
import com.vp.service.SemanticAndLinkService;

public class ReasonByRule {
	
	private static SemanticAndLinkService semanticAndLinkService = new SemanticAndLinkService();
	private static RuleService ruleService = new RuleService();
	private static KnowledgeService knowledgeService = new KnowledgeService();
	
	//���ݹ����������
	public static void infer(){
		List<String> allTypes = semanticAndLinkService.getSCTypes();
		List<String> allNames = semanticAndLinkService.getLinkTypes();
		List<String> allRelations = knowledgeService.getRelations();
		
		List<Rule> rules = ruleService.getAllRules();
		for(Rule rule : rules){
			String result = rule.getResult();
			if(result.contains("DIcircle")){
				String types[] = result.split("_has_");
				//��ǰ�����Ͳ����ڣ���ֱ������ѭ��
				if(!allTypes.contains(types[0])){
					System.out.println("�������е�ǰ�����Ͳ����ڣ�");
					break;
				}
				if(!allTypes.contains(types[1])){
					//1�����������֪ʶ��
					Knowledge knowledge = new Knowledge();
					knowledge.setEntityType1(types[0]);
					knowledge.setEntityType2(types[1]);
					knowledge.setRelation(result);
					knowledgeService.createKnowledge(knowledge);
					//2����ʵ��ͼ���ݿ��д����ú������͵�����Ԫ�ڵ�
					SemanticCell sc = DesignIntentKnowledge.createDesignIntentSemanticCell(types[1], null, null);
					semanticAndLinkService.createSemanticCell(sc);
				}
				//��֪ʶ������Ȼ��������ʵ�����ͣ���û����Ӧ�Ĺ�ϵ��Ҳ��Ҫ����
				if(!allRelations.contains(result)){
					//���֪ʶ��
					Knowledge knowledge = new Knowledge();
					knowledge.setEntityType1(types[0]);
					knowledge.setEntityType2(types[1]);
					knowledge.setRelation(result);
					knowledgeService.createKnowledge(knowledge);
				}
				
				//���ǰ�����ͺͺ������;���������link������Ȩ��ֵ
				//1�����Ȼ�ȡ֪ʶ���ж��ڵ�һ��֪ʶ��
				Knowledge knowledge = knowledgeService.getKnowledgeByRelation(result);
				if(knowledge == null){
					System.out.println(rule.getResult() + " : ·���д���δ֪����Ԫ�飡");
					break;
				}
				//2������knowledge��weight��������
				//������Ҫ�ж�ʵ���е�����link�Ƿ��Ѿ������ˣ����������ý��������������
				if(!allNames.contains(result)){
					semanticAndLinkService.createLinkByKnowledge(knowledge, 0.0);
					//3�������������������·��������ʵ����ǰ��Ԫ�ص�����Ԫ�ص�Ȩ��ֵ
					String path = rule.getPath();
					List<Map<String,Object>> instancePathList = semanticAndLinkService.getPathWeight(path);
					for(Map<String, Object> map : instancePathList){
						double weight = (double) map.get("weight");
						String instancePath = (String) map.get("instancePath");
						//ʵ��·���еĵ�һ��Ԫ�ص�instanceId
						String preInstanceId = instancePath.substring(0, instancePath.indexOf("/"));
						//�ı��id���������͵Ľڵ�Ȩ��
						semanticAndLinkService.updateWeightByInstanceIds(preInstanceId, types[1], weight);
					}
				}
			}
			
		}
	}
	
	public static void main(String[] args) {
		
		//���CircleIn��CircleOut������Ԫ��
//		Knowledge knowledge = new Knowledge();
//		knowledge.setEntityType1("HoleFeature");
//		knowledge.setEntityType2("OuterVisible");
//		knowledge.setRelation("HoleFeature_has_OuterVisible");
//		knowledgeService.createKnowledge(knowledge);
		
//		Knowledge knowledge = new Knowledge();
//		knowledge.setEntityType1("BasedSketchFeature");
//		knowledge.setEntityType2("BasedRotate");
//		knowledge.setRelation("BasedSketchFeature_contains_BasedRotate");
//		knowledgeService.createKnowledge(knowledge);
		
		//��ͼ���ݿ���������ֽڵ�
//		semanticCell sc = DesignIntentKnowledge.createDesignIntentsemanticCell("CircleOut", null, null);
//		semanticAndLinkService.createsemanticCell(sc);	
		
		//����DIcircle��CircleOut��CircleIn����������
//		Knowledge knowledge = knowledgeService.getKnowledgeByRelation("DIcircle_has_CircleOut");
//		semanticAndLinkService.createLinkByKnowledge(knowledge, 0.5);
		
//		infer();
//		
		String resultPath = "advanced_face/DIcircle";
//		String resultPath = "advanced_face/face_bound/edge_loop/oriented_edge/edge_curve/circle";
		List<Map<String,Object>> resultPathList = semanticAndLinkService.getPathWeight(resultPath);
		for(Map<String, Object> map : resultPathList){
			System.out.println(resultPath + " : " + map.get("instancePath") + " : " + map.get("weight"));
		}
	}
}
