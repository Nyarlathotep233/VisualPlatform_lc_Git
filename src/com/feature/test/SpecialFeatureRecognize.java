package com.feature.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feature.service.AdjacencyMatrixService;
import com.feature.service.AdvancedFaceService;
import com.feature.service.SpecialFeatureService;

/**
 * ����������ʶ��
 * @author admin
 *
 */
public class SpecialFeatureRecognize {
	
	public static SpecialFeatureService specialFeatureService = new SpecialFeatureService();
	public static AdjacencyMatrixService adjacencyMatrixService = new AdjacencyMatrixService();
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	
	public static void main(String[] args) {
		
		String fileName = "test.txt";
		
		//�ڽ�ͼ����
		int[][] adjacencyMatrix = adjacencyMatrixService.getAdjacencyMatrixFromAdvancedFaceList(fileName);
		int[][] needAdjacencyMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
		
		for(int i = 0; i < adjacencyMatrix.length; i++){
			for(int j = 0; j < adjacencyMatrix.length; j++){
				needAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
			}
		}
		
		System.out.println("�����Ϊ��");
		for(int i = 0; i < adjacencyMatrix.length; i++){
			for(int j = 0; j < adjacencyMatrix.length; j++){
				System.out.print(adjacencyMatrix[i][j] + " ");
			}
			System.out.println();
		}
		
//		//���ݴ����������ʾ����
//		int[] a = {448,451,452,453,454};
//		for(int i = 0; i < a.length; i++){
//			a[i] = a[i] - 402;
//		}
//		
//		for(int i = 0; i < a.length; i++){
//			for(int j = 0; j < a.length; j++){
//				System.out.print(adjacencyMatrix[a[i]][a[j]] + "\t");
//			}
//			System.out.println();
//		}
		
		//�ж�Բ��͹̨����
		Map<String,String> map = advancedFaceService.planeIntersectCylindrical();
		System.out.println("ƽ���Բ�����ཻ��mapΪ��");
		System.out.println(map);
		//������2������ƽ��ɾ����õ�����map
		Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            if(!advancedFaceService.advancedFaceHas2().contains(entry.getKey())) {
				iterator.remove();
			}
        }
//		for(Map.Entry<String, String> id : map.entrySet()) {
//			if(!advancedFaceService.advancedFaceHas2().contains(id.getKey())) {
//				map.remove(id.getKey());
//			}
//		}
		System.out.println("������2������ƽ��ɾ����õ�����mapΪ:");
		System.out.println(map);
		int firstId = advancedFaceService.getFirstAdvancedFaceId();
		System.out.println("firstIdΪ"+firstId);
		for(Map.Entry<String, String> id : map.entrySet()) {
			int planeId = Integer.valueOf(id.getKey().substring(1))-firstId;
			int cylindricalId = Integer.valueOf(id.getValue().substring(1))-firstId;
			System.out.println("�ڴ�����ж�Ӧλ�õ�ֵΪ��"+adjacencyMatrix[planeId][cylindricalId]);
			if(adjacencyMatrix[planeId][cylindricalId]==11) {
				System.out.println("ƽ��"+id.getKey()+"�Ͼ���Բ��͹̨�������Ҹ�Բ��͹̨��ƽ��"+id.getKey()+"��Բ����"+id.getValue()+"���");
			}
			
		}
		
		int startFaceId = advancedFaceService.getFirstAdvancedFaceId();
		
		//��ȡ�������ڽ�ͼ��ͼ����
		List<List<Integer>> childMatrixLists = new ArrayList<>();
		adjacencyMatrixService.simplifyMatrix(adjacencyMatrix, childMatrixLists);
		
		for(List<Integer> faceList : childMatrixLists){
			for(int i = 0; i < faceList.size(); i++){
				faceList.set(i, faceList.get(i) + startFaceId);
			}
		}
		//�������ڽ�ͼ��ͼ�漯��
		System.out.println("�������ڽ�ͼ��ͼ�漯��Ϊ��" + childMatrixLists);
		
		//�洢��������������ͼ�������Ƭ����
		List<List<Integer>> coplanarFaceIdLists = specialFeatureService.getCoplanarFaceIdLists(needAdjacencyMatrix);
		System.out.println("������ڽ�ͼ��ͼ�漯��Ϊ��" + coplanarFaceIdLists);
		
		List<Integer> childListIndexs = new ArrayList<Integer>();
		List<Integer> coplanarFaceIdIndexs = new ArrayList<Integer>();
		
		//���ݹ������ͼ�����µ�һ����ͼ�漯��
		for(List<Integer> faceList : childMatrixLists){
			for(List<Integer> coplanarFaceIdList : coplanarFaceIdLists){
				if(faceList.containsAll(coplanarFaceIdList) && faceList.size() > coplanarFaceIdList.size()){
					if(!childListIndexs.contains(childMatrixLists.indexOf(faceList))){
						childListIndexs.add(childMatrixLists.indexOf(faceList));
					}
					if(!coplanarFaceIdIndexs.contains(coplanarFaceIdLists.indexOf(coplanarFaceIdList))){
						coplanarFaceIdIndexs.add(coplanarFaceIdLists.indexOf(coplanarFaceIdList));
					}
				}
			}
		}
		
		//����ɾ������
		for(int i : childListIndexs){
			childMatrixLists.remove(i);
		}
		//������Ӳ���
		for(int i : coplanarFaceIdIndexs){
			childMatrixLists.add(coplanarFaceIdLists.get(i));
		}
		
		System.out.println("���ջ�ȡ����ͼ����Ϊ ��" + childMatrixLists);
	}
}
