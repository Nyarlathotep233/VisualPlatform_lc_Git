package com.vp.reason;

import com.vp.base.Knowledge;
import com.vp.service.KnowledgeService;
import com.vp.service.RuleService;

/**
 * 规则的建立
 * @author admin
 *
 */
public class RuleConstruction {
	
	private static KnowledgeService knowledgeService = new KnowledgeService();
	private static RuleService ruleService = new RuleService();
	
	public static void main(String[] args) {

//		String rule = "advanced_face/DIcircle/CircleIn->advanced_face_has_HoleFeature"; 
//		String rule = "advanced_face/DIcircle/CircleOut->advanced_face_has_ConvexFeature";
//		String rule = "advanced_face/HoleFeature/OuterVisible->advanced_face_has_BlindHoleFeature";
		String rule = "advanced_face/HoleFeature/InnerVisible->advanced_face_has_ThroughHoleFeature";
		
//		String rule = "part/closed_shell/BasedSketchFeature/BasedRotate->part_has_BasedRotate";
//		String rule = "part/closed_shell/BasedSketchFeature/BasedExtrude->part_has_BasedExtrude";
//		String rule = "part/closed_shell/BasedSketchFeature/BasedCut->part_has_BasedCut";
		
//		String rule = "advanced_face/face_bound/edge_loop/oriented_edge/edge_curve/circle->advanced_face_has_DIcircle";
		ruleService.addRule(rule);
		
	}
}
