package com.vp.test;

import com.vp.dao.SemanticCellDAO;


public class ExtraTest {
	
	public static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	
	public static void main(String[] args) {
		
		//根据面的id获取面的类型
		String[] faceIds = {"#496", "#498"};
		for(int i = 0; i < faceIds.length; i++){
			String faceId = faceIds[i];
			int type = semanticCellDAO.getAdvancedFaceType(faceId);
			if(type == 0){
				System.out.println(faceId + " : plane");
			}else if(type == 1){
				System.out.println(faceId + " : cylindrical_surface");
			}
		}
	}
}
