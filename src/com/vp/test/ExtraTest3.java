package com.vp.test;

import java.util.List;

import com.vp.dao.SemanticCellDAO;
import com.vp.semanticcell.SemanticCell;

public class ExtraTest3 {
	
	public static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	
	public static void main(String[] args) {
		String[] instancedIds = {"#546","#548","#448","#456","#547","#545","#461","#465","#462","#463",
								 "#407","#408","#413","#414","#437","#447","#467","#470","#469","#471","#472","#481","#473","#484","#505,#525","#510","#528",
								 "#526","#530","#496","#497","#498","#499","#500","#501","#502","#503","#504",
								 "#427","#428","#429","#434","#435","#436","#450","#458","#459","#475","#476","#477","#511","#531","#532","#535","#537","#538",
								 "#421","#442","#443","#444","#445","#448","#451","#452","#453","#454",
								 "%cylindrical_convex_feature","%step_feature","%blind_hole_feature","%through_hole_feature","%convex_feature"};
		//根据Id数组查出所有的语义元节点
		List<SemanticCell> scs = semanticCellDAO.getSemanticCellsByIds(instancedIds);
		
	}
}
