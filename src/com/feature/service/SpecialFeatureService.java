package com.feature.service;

import java.util.ArrayList;
import java.util.List;

import com.feature.dao.SpecialFeatureDAO;

/**
 * 用来处理特殊特征的一些业务操作方法
 * @author admin
 *
 */
public class SpecialFeatureService {
	public static SpecialFeatureDAO specialFeatureDAO = new SpecialFeatureDAO();
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	public static AdjacencyMatrixService adjacencyMatrixService = new AdjacencyMatrixService();
	
	/**
	 * 找出面上的face_bound或者face_outer_bound的个数超过三个的面的List
	 * @return
	 */
	public List<String> getCoplanarAdvancedFaceIdList(){
		return specialFeatureDAO.getCoplanarAdvancedFaceIdList();
	}
	
	/**
	 * 根据advancedFaceId获取face_boundId或face_outer_boundId的list
	 * @param advancedFaceId
	 * @return
	 */
	public List<String> getBoundIdListByAdvancedFaceId(String advancedFaceId){
		return specialFeatureDAO.getBoundIdListByAdvancedFaceId(advancedFaceId);
	}
	
	/**
	 * 根据boundId找出edgeCurveIdList
	 * @param advancedFaceId
	 * @return
	 */
	public List<String> getEdgeCurveIdListByBoundId(String boundId){
		return specialFeatureDAO.getEdgeCurveIdListByBoundId(boundId);
	}
	
	/**
	 * 根据edgeCurveId获取是哪两个面相交于他的
	 * @param edgeCurveId
	 * @return
	 */
	public List<String> getAdvancedFaceIdListByEdgeCurveId(String edgeCurveId){
		return specialFeatureDAO.getAdvancedFaceIdListByEdgeCurveId(edgeCurveId);
	}
	
	/**
	 * 根据两个面的id结合生成的属性邻接矩阵获取该条有向边的凹凸性
	 * @param advancedFaceId1
	 * @param advancedFaceId2
	 * @param startFaceId
	 * @param adjacencyMatrix
	 * @return
	 */
	public int getBumpByTwoAdvancedFaceIds(String advancedFaceId1, String advancedFaceId2, int startFaceId, int[][] adjacencyMatrix){
		int advancedFaceNum1 = Integer.valueOf(advancedFaceId1.substring(1)) - startFaceId;
		int advancedFaceNum2 = Integer.valueOf(advancedFaceId2.substring(1)) - startFaceId;
		return adjacencyMatrix[advancedFaceNum1][advancedFaceNum2];
	}
	
	/**
	 * 获取共面拆分下来的子图矩阵的面片集合
	 * @param adjacencyMatrix
	 * @return
	 */
	public List<List<Integer>> getCoplanarFaceIdLists(int[][] adjacencyMatrix){
		List<List<Integer>> coplanarFaceIdLists = new ArrayList<List<Integer>>();
		//1、找出一个面上face_bound或者face_outer_bound的个数超过三个，获取这些面的List
		List<String> coplanarFaceIds = getCoplanarAdvancedFaceIdList();
		
		//记录凸特征的个数
		int count = 0;
		for(String advancedFaceId : coplanarFaceIds){
			//存储包含凸特征的特征环
			List<String> featureBoundIds = new ArrayList<String>();
			count = 0;
			//根据advancedFaceId获取face_boundId或face_outer_boundId的list
			List<String> boundIdList = getBoundIdListByAdvancedFaceId(advancedFaceId);
			//2、判断face_bound或face_outer_bound包含一个或一个以上的凹边，则为凸特征
			for(String boundId : boundIdList){
				boolean flag = false;
				//根据boundId找出edgeCurveIdList
				List<String> edgeCurveIdList = getEdgeCurveIdListByBoundId(boundId);
				//根据edgeCurveId判断凹凸性
				//根据edgeCurveId获取是哪两个面相交于他的
				for(String edgeCurveId : edgeCurveIdList){
					List<String> intersectionFaceIds = getAdvancedFaceIdListByEdgeCurveId(edgeCurveId);
					//根据两个面的id结合生成的属性邻接矩阵获取该条有向边的凹凸性
					int startFaceId = advancedFaceService.getFirstAdvancedFaceId();
					int bump = getBumpByTwoAdvancedFaceIds(intersectionFaceIds.get(0), intersectionFaceIds.get(1), startFaceId, adjacencyMatrix);
					//该有向边为凹边
					if(bump == 10){
						flag = true;
						if(!featureBoundIds.contains(boundId)){
							featureBoundIds.add(boundId);
						}
					}
				}
				if(flag == true){
					count++;
				}
			}
			//3、若这个面上的face_bound或face_outer_bound由两个或两个以上的凸特征，则需要解决共面问题
			if(count >= 2){
				//按环来讨论
				for(String boundId : featureBoundIds){
					//根据环的Id来获取组成这个环的边
					//根据boundId找出edgeCurveIdList
					List<String> edgeCurveIdList = getEdgeCurveIdListByBoundId(boundId);
					//4、取出这些凹边所在的面，形成面的id集合
					List<Integer> advancedFaceIds = new ArrayList<Integer>();
					for(String edgeCurveId : edgeCurveIdList){
						List<String> intersectionFaceIds = getAdvancedFaceIdListByEdgeCurveId(edgeCurveId);
						String faceId1 = intersectionFaceIds.get(0);
						String faceId2 = intersectionFaceIds.get(1);
						int faceIdNum1 = Integer.valueOf(faceId1.substring(1));
						int faceIdNum2 = Integer.valueOf(faceId2.substring(1));
						if(!advancedFaceIds.contains(faceIdNum1)){
							advancedFaceIds.add(faceIdNum1);
						}
						if(!advancedFaceIds.contains(faceIdNum2)){
							advancedFaceIds.add(faceIdNum2);
						}
					}
					if(!coplanarFaceIdLists.contains(advancedFaceIds)){
						coplanarFaceIdLists.add(advancedFaceIds);
					}
					
				}
				
			}
		}
		return coplanarFaceIdLists;
	}
}
