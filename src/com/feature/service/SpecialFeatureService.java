package com.feature.service;

import java.util.ArrayList;
import java.util.List;

import com.feature.dao.SpecialFeatureDAO;

/**
 * ������������������һЩҵ���������
 * @author admin
 *
 */
public class SpecialFeatureService {
	public static SpecialFeatureDAO specialFeatureDAO = new SpecialFeatureDAO();
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	public static AdjacencyMatrixService adjacencyMatrixService = new AdjacencyMatrixService();
	
	/**
	 * �ҳ����ϵ�face_bound����face_outer_bound�ĸ����������������List
	 * @return
	 */
	public List<String> getCoplanarAdvancedFaceIdList(){
		return specialFeatureDAO.getCoplanarAdvancedFaceIdList();
	}
	
	/**
	 * ����advancedFaceId��ȡface_boundId��face_outer_boundId��list
	 * @param advancedFaceId
	 * @return
	 */
	public List<String> getBoundIdListByAdvancedFaceId(String advancedFaceId){
		return specialFeatureDAO.getBoundIdListByAdvancedFaceId(advancedFaceId);
	}
	
	/**
	 * ����boundId�ҳ�edgeCurveIdList
	 * @param advancedFaceId
	 * @return
	 */
	public List<String> getEdgeCurveIdListByBoundId(String boundId){
		return specialFeatureDAO.getEdgeCurveIdListByBoundId(boundId);
	}
	
	/**
	 * ����edgeCurveId��ȡ�����������ཻ������
	 * @param edgeCurveId
	 * @return
	 */
	public List<String> getAdvancedFaceIdListByEdgeCurveId(String edgeCurveId){
		return specialFeatureDAO.getAdvancedFaceIdListByEdgeCurveId(edgeCurveId);
	}
	
	/**
	 * �����������id������ɵ������ڽӾ����ȡ��������ߵİ�͹��
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
	 * ��ȡ��������������ͼ�������Ƭ����
	 * @param adjacencyMatrix
	 * @return
	 */
	public List<List<Integer>> getCoplanarFaceIdLists(int[][] adjacencyMatrix){
		List<List<Integer>> coplanarFaceIdLists = new ArrayList<List<Integer>>();
		//1���ҳ�һ������face_bound����face_outer_bound�ĸ���������������ȡ��Щ���List
		List<String> coplanarFaceIds = getCoplanarAdvancedFaceIdList();
		
		//��¼͹�����ĸ���
		int count = 0;
		for(String advancedFaceId : coplanarFaceIds){
			//�洢����͹������������
			List<String> featureBoundIds = new ArrayList<String>();
			count = 0;
			//����advancedFaceId��ȡface_boundId��face_outer_boundId��list
			List<String> boundIdList = getBoundIdListByAdvancedFaceId(advancedFaceId);
			//2���ж�face_bound��face_outer_bound����һ����һ�����ϵİ��ߣ���Ϊ͹����
			for(String boundId : boundIdList){
				boolean flag = false;
				//����boundId�ҳ�edgeCurveIdList
				List<String> edgeCurveIdList = getEdgeCurveIdListByBoundId(boundId);
				//����edgeCurveId�жϰ�͹��
				//����edgeCurveId��ȡ�����������ཻ������
				for(String edgeCurveId : edgeCurveIdList){
					List<String> intersectionFaceIds = getAdvancedFaceIdListByEdgeCurveId(edgeCurveId);
					//�����������id������ɵ������ڽӾ����ȡ��������ߵİ�͹��
					int startFaceId = advancedFaceService.getFirstAdvancedFaceId();
					int bump = getBumpByTwoAdvancedFaceIds(intersectionFaceIds.get(0), intersectionFaceIds.get(1), startFaceId, adjacencyMatrix);
					//�������Ϊ����
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
			//3����������ϵ�face_bound��face_outer_bound���������������ϵ�͹����������Ҫ�����������
			if(count >= 2){
				//����������
				for(String boundId : featureBoundIds){
					//���ݻ���Id����ȡ���������ı�
					//����boundId�ҳ�edgeCurveIdList
					List<String> edgeCurveIdList = getEdgeCurveIdListByBoundId(boundId);
					//4��ȡ����Щ�������ڵ��棬�γ����id����
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
