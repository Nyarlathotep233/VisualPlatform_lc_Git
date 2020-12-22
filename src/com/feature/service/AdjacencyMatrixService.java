package com.feature.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feature.domain.AdvancedFace;
import com.feature.domain.EdgeCurve;

/**
 * �ڽ�ͼ�������ط���
 * @author admin
 *
 */
public class AdjacencyMatrixService {
	
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	
	/**
	 * ����advancedList.txt�ļ���ȡ�ڽ�ͼ����
	 * @param advancedFaceList
	 * @return
	 */
	public int[][] getAdjacencyMatrixFromAdvancedFaceList(String fileName){
		List<AdvancedFace> advancedFaceList = ParseTxt.getAdvancedFaceListFromTxt(fileName);
		//�ڽ�ͼ����
		int[][] adjacencyMatrix = new int[advancedFaceList.size()][advancedFaceList.size()];
		//��ʼ������Ԫ��Ϊ0
		for(int i = 0; i < advancedFaceList.size(); i++){
			for(int j = 0; j < advancedFaceList.size(); j++){
				adjacencyMatrix[i][j] = 0;
			}
		}
		for(int i = 0; i < advancedFaceList.size() - 1; i++){
			for(int j = i + 1; j < advancedFaceList.size(); j++){
				AdvancedFace face1 = advancedFaceList.get(i);
				AdvancedFace face2 = advancedFaceList.get(j);
				EdgeCurve edgeCurve = advancedFaceService.judgeIsIntersection(face1, face2);//�ж��Ƿ��ཻ�������ཻ��
				//�����ཻ
				if(edgeCurve != null){
					//ƽ���Բ�����ཻ��ƽ��Ϊ0��Բ����Ϊ1
					if(face1.getAdvancedFaceType() + face2.getAdvancedFaceType() == 1){
						AdvancedFace planeFace = null;
						AdvancedFace cylindricalFace = null;
						if(face1.getAdvancedFaceType() < face2.getAdvancedFaceType()){
							planeFace = face1;
							cylindricalFace = face2;
						}else{
							planeFace = face2;
							cylindricalFace = face1;
						}
						//��ȡ��͹�Բ�д�����
						int m = advancedFaceService.judgeFromPlaneFaceAndCylindricalFace(planeFace, cylindricalFace, edgeCurve);
						adjacencyMatrix[i][j] = m;
						adjacencyMatrix[j][i] = m;
					//�������Ϊƽ�� type��Ϊ0
					}else if(face1.getAdvancedFaceType() == face2.getAdvancedFaceType() && face1.getAdvancedFaceType() == 0){
						//��ȡ��͹�Բ�д�����
						int m = advancedFaceService.judgeFromTwoCommonFaces(face1, face2, edgeCurve);
						adjacencyMatrix[i][j] = m;
						adjacencyMatrix[j][i] = m;
					}
				//�������ཻ��
				}else{
					adjacencyMatrix[i][j] = -1;
					adjacencyMatrix[j][i] = -1;
					System.out.println("�������ཻ��");
				}
			}
		}
		return adjacencyMatrix;
	}
	
	/**
	 * �����ڽ�ͼ�����ȡ��һ����ͼ����ɵ�Ԫ��(ԭ������±�ֵ)list
	 * @param matrix
	 * @return
	 */
	public List<Integer> getFirstChildMatrixList(int[][] matrix){
		List<Integer> childMatrixList = new ArrayList<Integer>();
		//1������ɨ�裬Ѱ�ҵ�һ������10���У���¼�е��±�
		boolean found = true;
		int columnIndex = 0;
		for(int i = 0; i < matrix.length && found; i++){  //i������
			for(int j = 0; j < matrix.length; j++){	//j������
				if(matrix[j][i] == 10){
					columnIndex = i;
					found = false;
					break;
				}
			}
		}
		if(!childMatrixList.contains(columnIndex)){
			childMatrixList.add(columnIndex);
		}
		//2�����ݼ�¼���е��±�columnIndex�ҵ��±�ΪcolumnIndex����������,����¼���а���10������
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < matrix.length; i++){
			if(matrix[columnIndex][i] == 10){
				list.add(i);
				if(!childMatrixList.contains(columnIndex)){
					childMatrixList.add(columnIndex);
				}
				if(!childMatrixList.contains(i)){
					childMatrixList.add(i);
				}
			}
		}
		//3����������10������list���ظ�����2
		for(Integer m : list){
			for(int i = 0; i < matrix.length; i++){
				if(matrix[m][i] == 10){
					if(!childMatrixList.contains(i)){
						childMatrixList.add(i);
					}
				}
			}
		}
		//��С��������
		Collections.sort(childMatrixList);
		return childMatrixList;
	}
	
	/**
	 * �����ڽ�ͼ�����list��ȡ��һ����ͼ����
	 * @param matrix
	 * @return
	 */
	public int[][] getFirstChildMatrix(int[][] matrix, List<Integer> childMatrixList){
		int[][] childMatrix = new int[childMatrixList.size()][childMatrixList.size()];
		for(int i = 0; i < childMatrix.length; i++){
			for(int j = 0; j < childMatrix.length; j++){
				childMatrix[i][j] = matrix[childMatrixList.get(i)][childMatrixList.get(j)];
			}
		}
		return childMatrix;
	}
	/**
	 * ��ԭ�������ͼ����ֵȫ����Ϊ-100
	 * @param matrix
	 * @param childMatrixList
	 * @return
	 */
	public int[][] changeMatrix(int[][] matrix, List<Integer> childMatrixList){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				if(childMatrixList.contains(i) || childMatrixList.contains(j)){
					matrix[i][j] = -100;
				}
			}
		}
		return matrix;
	}
	
	/**
	 * �ݹ黯�����
	 * @param matrix
	 */
	public void simplifyMatrix(int[][] matrix,List<List<Integer>> childMatrixLists){
		List<Integer> childMatrixList = getFirstChildMatrixList(matrix);
		if(childMatrixList.size() == 1 && childMatrixList.get(0) == 0){
			System.out.println("�����ڽ�ͼ��������!");
			return;
		}
		childMatrixLists.add(childMatrixList);
		//��ӡ����ͼ������ԭ�����ʲô�±깹��
		int[][] childMatrix = getFirstChildMatrix(matrix, childMatrixList);
		System.out.println("�������±꼯��" + childMatrixList + "��ɵ���ͼ����Ϊ��");
		for(int i = 0; i < childMatrix.length; i++){
			for(int j = 0; j < childMatrix.length; j++){
				System.out.print(childMatrix[i][j] + "\t");
			}
			System.out.println();
		}
		int[][] newMatrix = changeMatrix(matrix, childMatrixList);
		simplifyMatrix(newMatrix,childMatrixLists);
	}
	/**
	 * ȡ��������ͼ�а���3�������ϵĻ����ڵ�advanced_face����ͼ
	 * @param childMatrixLists
	 * @return
	 */
	public int[][] isTheOne(List<List<Integer>> childMatrixLists,int[][] adjacencyMatrix) {
		//hasThreeEdgeLoopΪ��3�������ϵĻ����ڵ�advanced_face�ı��
		List<Integer> keyId = new ArrayList<>();
		int firstId = advancedFaceService.getFirstAdvancedFaceId();
		Map<Integer,Integer> map = advancedFaceService.advancedFaceHas3();
		for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int cha = entry.getKey() - firstId;
			keyId.add(cha);
		};
		int i=0;
		int j=0;
		int k=0;
		Map<Integer,List<List<Integer>>> needMap = new HashMap<>();
		List<List<Integer>> needChildMatrixLists = new ArrayList<>();
		for(i=0 ; i<keyId.size();i++) {
			keyId.get(i);//��ȡ���˵�һ�������е�ÿ��ֵ
			for(j=0 ; j<childMatrixLists.size();j++) {
				childMatrixLists.get(j);//������������ÿһ��С����
				for(k=0;k<childMatrixLists.get(j).size();k++) {
					childMatrixLists.get(j).get(k);//����С�����е�ÿһ��ֵ
					if(keyId.get(i)==childMatrixLists.get(j).get(k)) {
						System.out.println(childMatrixLists.get(j));
						needChildMatrixLists.add(childMatrixLists.get(j)) ;
						needMap.put(keyId.get(i), needChildMatrixLists);
					}
				}
			}
		}
		System.out.println(needChildMatrixLists);
		
		for(Map.Entry<Integer, List<List<Integer>>> entry : needMap.entrySet()) {
			System.out.println("needMap��KeyΪ"+entry.getKey());
			System.out.println("needMap��ValueΪ"+entry.getValue());
			
			for(i=0;i<entry.getValue().size();i++) {
				System.out.println("��һ����Ҫ���ӵ���ͼΪ"+entry.getValue().get(i));
				entry.getValue().get(i).add(entry.getKey());
				System.out.println("��һ�����Ӻ�Ϊ��"+entry.getValue().get(i));
				System.out.println("entry.getValue().get(i).size()Ϊ"+entry.getValue().get(i).size());
				System.out.println("�µĴ����Ϊ");
				int [][] newAdjacencyMatrix = new int[entry.getValue().get(i).size()][entry.getValue().get(i).size()];
				List<Integer> middleList = new ArrayList<>();
				for(int a :entry.getValue().get(i)){
					for(int b :entry.getValue().get(i)){
						middleList.add(adjacencyMatrix[a][b]);
						System.out.print(adjacencyMatrix[a][b] + "\t");
					}			
					System.out.println();
				}
				System.out.println("middleListΪ"+middleList);
				System.out.println("entry.getValue().get(i).size()Ϊ"+entry.getValue().get(i).size());
				for(int a1=0; a1<entry.getValue().get(i).size();a1++) {
					for(int b1=0; b1<entry.getValue().get(i).size();b1++) {
						int c1=a1*10+b1;
						newAdjacencyMatrix[a1][b1]= middleList.get(c1);
						System.out.print(newAdjacencyMatrix[a1][b1] + "\t");
					}
					System.out.println();
				}
				return newAdjacencyMatrix;
			}		
		}
//		return adjacencyMatrix;		
		return null;
		
	}
	

	
}