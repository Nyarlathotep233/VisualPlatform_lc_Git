package com.feature.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feature.domain.AdvancedFace;
import com.feature.domain.EdgeCurve;

/**
 * 邻接图矩阵的相关方法
 * @author admin
 *
 */
public class AdjacencyMatrixService {
	
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	
	/**
	 * 根据advancedList.txt文件获取邻接图矩阵
	 * @param advancedFaceList
	 * @return
	 */
	public int[][] getAdjacencyMatrixFromAdvancedFaceList(String fileName){
		List<AdvancedFace> advancedFaceList = ParseTxt.getAdvancedFaceListFromTxt(fileName);
		//邻接图矩阵
		int[][] adjacencyMatrix = new int[advancedFaceList.size()][advancedFaceList.size()];
		//初始化矩阵元素为0
		for(int i = 0; i < advancedFaceList.size(); i++){
			for(int j = 0; j < advancedFaceList.size(); j++){
				adjacencyMatrix[i][j] = 0;
			}
		}
		for(int i = 0; i < advancedFaceList.size() - 1; i++){
			for(int j = i + 1; j < advancedFaceList.size(); j++){
				AdvancedFace face1 = advancedFaceList.get(i);
				AdvancedFace face2 = advancedFaceList.get(j);
				EdgeCurve edgeCurve = advancedFaceService.judgeIsIntersection(face1, face2);//判断是否相交并返回相交边
				//两面相交
				if(edgeCurve != null){
					//平面和圆柱面相交，平面为0，圆柱面为1
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
						//获取凹凸性并写入矩阵
						int m = advancedFaceService.judgeFromPlaneFaceAndCylindricalFace(planeFace, cylindricalFace, edgeCurve);
						adjacencyMatrix[i][j] = m;
						adjacencyMatrix[j][i] = m;
					//两个面均为平面 type均为0
					}else if(face1.getAdvancedFaceType() == face2.getAdvancedFaceType() && face1.getAdvancedFaceType() == 0){
						//获取凹凸性并写入矩阵
						int m = advancedFaceService.judgeFromTwoCommonFaces(face1, face2, edgeCurve);
						adjacencyMatrix[i][j] = m;
						adjacencyMatrix[j][i] = m;
					}
				//不存在相交边
				}else{
					adjacencyMatrix[i][j] = -1;
					adjacencyMatrix[j][i] = -1;
					System.out.println("不存在相交边");
				}
			}
		}
		return adjacencyMatrix;
	}
	
	/**
	 * 根据邻接图矩阵获取第一个子图所组成的元素(原矩阵的下标值)list
	 * @param matrix
	 * @return
	 */
	public List<Integer> getFirstChildMatrixList(int[][] matrix){
		List<Integer> childMatrixList = new ArrayList<Integer>();
		//1、逐列扫描，寻找第一个包含10的列，记录列的下标
		boolean found = true;
		int columnIndex = 0;
		for(int i = 0; i < matrix.length && found; i++){  //i代表列
			for(int j = 0; j < matrix.length; j++){	//j代表行
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
		//2、根据记录的列的下标columnIndex找到下标为columnIndex的整行数据,并记录其中包含10的列数
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
		//3、遍历包含10的列数list，重复步骤2
		for(Integer m : list){
			for(int i = 0; i < matrix.length; i++){
				if(matrix[m][i] == 10){
					if(!childMatrixList.contains(i)){
						childMatrixList.add(i);
					}
				}
			}
		}
		//从小到大排序
		Collections.sort(childMatrixList);
		return childMatrixList;
	}
	
	/**
	 * 根据邻接图矩阵和list获取第一个子图矩阵
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
	 * 将原矩阵的子图矩阵值全部变为-100
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
	 * 递归化简矩阵
	 * @param matrix
	 */
	public void simplifyMatrix(int[][] matrix,List<List<Integer>> childMatrixLists){
		List<Integer> childMatrixList = getFirstChildMatrixList(matrix);
		if(childMatrixList.size() == 1 && childMatrixList.get(0) == 0){
			System.out.println("属性邻接图矩阵简化完成!");
			return;
		}
		childMatrixLists.add(childMatrixList);
		//打印出子图矩阵由原矩阵的什么下标构成
		int[][] childMatrix = getFirstChildMatrix(matrix, childMatrixList);
		System.out.println("由数组下标集合" + childMatrixList + "组成的子图矩阵为：");
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
	 * 取出所有子图中包含3个或以上的环所在的advanced_face的子图
	 * @param childMatrixLists
	 * @return
	 */
	public int[][] isTheOne(List<List<Integer>> childMatrixLists,int[][] adjacencyMatrix) {
		//hasThreeEdgeLoop为含3个或以上的环所在的advanced_face的编号
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
			keyId.get(i);//获取到了第一个容器中的每个值
			for(j=0 ; j<childMatrixLists.size();j++) {
				childMatrixLists.get(j);//大容器包含的每一个小容器
				for(k=0;k<childMatrixLists.get(j).size();k++) {
					childMatrixLists.get(j).get(k);//包含小容器中的每一个值
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
			System.out.println("needMap的Key为"+entry.getKey());
			System.out.println("needMap的Value为"+entry.getValue());
			
			for(i=0;i<entry.getValue().size();i++) {
				System.out.println("第一个需要增加的子图为"+entry.getValue().get(i));
				entry.getValue().get(i).add(entry.getKey());
				System.out.println("第一个增加后为："+entry.getValue().get(i));
				System.out.println("entry.getValue().get(i).size()为"+entry.getValue().get(i).size());
				System.out.println("新的大矩阵为");
				int [][] newAdjacencyMatrix = new int[entry.getValue().get(i).size()][entry.getValue().get(i).size()];
				List<Integer> middleList = new ArrayList<>();
				for(int a :entry.getValue().get(i)){
					for(int b :entry.getValue().get(i)){
						middleList.add(adjacencyMatrix[a][b]);
						System.out.print(adjacencyMatrix[a][b] + "\t");
					}			
					System.out.println();
				}
				System.out.println("middleList为"+middleList);
				System.out.println("entry.getValue().get(i).size()为"+entry.getValue().get(i).size());
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