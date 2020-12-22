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
	
	//根据规则进行推理
	public static void infer(){
		List<String> allTypes = semanticAndLinkService.getSCTypes();
		List<String> allNames = semanticAndLinkService.getLinkTypes();
		List<String> allRelations = knowledgeService.getRelations();
		
		List<Rule> rules = ruleService.getAllRules();
		for(Rule rule : rules){
			String result = rule.getResult();
			if(result.contains("DIcircle")){
				String types[] = result.split("_has_");
				//若前置类型不存在，则直接跳出循环
				if(!allTypes.contains(types[0])){
					System.out.println("规则结果中的前置类型不存在！");
					break;
				}
				if(!allTypes.contains(types[1])){
					//1、首先先添加知识库
					Knowledge knowledge = new Knowledge();
					knowledge.setEntityType1(types[0]);
					knowledge.setEntityType2(types[1]);
					knowledge.setRelation(result);
					knowledgeService.createKnowledge(knowledge);
					//2、在实例图数据库中创建该后置类型的语义元节点
					SemanticCell sc = DesignIntentKnowledge.createDesignIntentSemanticCell(types[1], null, null);
					semanticAndLinkService.createSemanticCell(sc);
				}
				//若知识库中虽然有这两个实体类型，但没有相应的关系则也需要创建
				if(!allRelations.contains(result)){
					//添加知识库
					Knowledge knowledge = new Knowledge();
					knowledge.setEntityType1(types[0]);
					knowledge.setEntityType2(types[1]);
					knowledge.setRelation(result);
					knowledgeService.createKnowledge(knowledge);
				}
				
				//如果前置类型和后置类型均存在则建立link并设立权重值
				//1、首先获取知识库中对于的一条知识库
				Knowledge knowledge = knowledgeService.getKnowledgeByRelation(result);
				if(knowledge == null){
					System.out.println(rule.getResult() + " : 路径中存在未知的三元组！");
					break;
				}
				//2、根据knowledge和weight建立连接
				//这里需要判断实例中的这条link是否已经存在了，若存在则不用进行下面的推理了
				if(!allNames.contains(result)){
					semanticAndLinkService.createLinkByKnowledge(knowledge, 0.0);
					//3、根据满足这条规则的路径来设置实例中前置元素到后置元素的权重值
					String path = rule.getPath();
					List<Map<String,Object>> instancePathList = semanticAndLinkService.getPathWeight(path);
					for(Map<String, Object> map : instancePathList){
						double weight = (double) map.get("weight");
						String instancePath = (String) map.get("instancePath");
						//实例路径中的第一个元素的instanceId
						String preInstanceId = instancePath.substring(0, instancePath.indexOf("/"));
						//改变该id到后置类型的节点权重
						semanticAndLinkService.updateWeightByInstanceIds(preInstanceId, types[1], weight);
					}
				}
			}
			
		}
	}
	
	public static void main(String[] args) {
		
		//添加CircleIn和CircleOut两种三元组
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
		
		//给图数据库添加这两种节点
//		semanticCell sc = DesignIntentKnowledge.createDesignIntentsemanticCell("CircleOut", null, null);
//		semanticAndLinkService.createsemanticCell(sc);	
		
		//建立DIcircle和CircleOut与CircleIn的两条连线
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
