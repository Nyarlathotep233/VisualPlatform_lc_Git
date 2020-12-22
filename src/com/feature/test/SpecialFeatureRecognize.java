package com.feature.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feature.service.AdjacencyMatrixService;
import com.feature.service.AdvancedFaceService;
import com.feature.service.SpecialFeatureService;

/**
 * 特殊特征的识别
 * @author admin
 *
 */
public class SpecialFeatureRecognize {
	
	public static SpecialFeatureService specialFeatureService = new SpecialFeatureService();
	public static AdjacencyMatrixService adjacencyMatrixService = new AdjacencyMatrixService();
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	
	public static void main(String[] args) {
		
		String fileName = "test.txt";
		
		//邻接图矩阵
		int[][] adjacencyMatrix = adjacencyMatrixService.getAdjacencyMatrixFromAdvancedFaceList(fileName);
		int[][] needAdjacencyMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
		
		for(int i = 0; i < adjacencyMatrix.length; i++){
			for(int j = 0; j < adjacencyMatrix.length; j++){
				needAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
			}
		}
		
		System.out.println("大矩阵为：");
		for(int i = 0; i < adjacencyMatrix.length; i++){
			for(int j = 0; j < adjacencyMatrix.length; j++){
				System.out.print(adjacencyMatrix[i][j] + " ");
			}
			System.out.println();
		}
		
//		//根据传入的数组显示矩阵
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
		
		//判断圆柱凸台特征
		Map<String,String> map = advancedFaceService.planeIntersectCylindrical();
		System.out.println("平面和圆柱面相交的map为：");
		System.out.println(map);
		//将少于2个环的平面删除后得到的新map
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
		System.out.println("将少于2个环的平面删除后得到的新map为:");
		System.out.println(map);
		int firstId = advancedFaceService.getFirstAdvancedFaceId();
		System.out.println("firstId为"+firstId);
		for(Map.Entry<String, String> id : map.entrySet()) {
			int planeId = Integer.valueOf(id.getKey().substring(1))-firstId;
			int cylindricalId = Integer.valueOf(id.getValue().substring(1))-firstId;
			System.out.println("在大矩阵中对应位置的值为："+adjacencyMatrix[planeId][cylindricalId]);
			if(adjacencyMatrix[planeId][cylindricalId]==11) {
				System.out.println("平面"+id.getKey()+"上具有圆柱凸台特征，且该圆柱凸台由平面"+id.getKey()+"和圆柱面"+id.getValue()+"组成");
			}
			
		}
		
		int startFaceId = advancedFaceService.getFirstAdvancedFaceId();
		
		//获取化简后的邻接图子图集合
		List<List<Integer>> childMatrixLists = new ArrayList<>();
		adjacencyMatrixService.simplifyMatrix(adjacencyMatrix, childMatrixLists);
		
		for(List<Integer> faceList : childMatrixLists){
			for(int i = 0; i < faceList.size(); i++){
				faceList.set(i, faceList.get(i) + startFaceId);
			}
		}
		//化简后的邻接图子图面集合
		System.out.println("化简后的邻接图子图面集合为：" + childMatrixLists);
		
		//存储共面拆分下来的子图矩阵的面片集合
		List<List<Integer>> coplanarFaceIdLists = specialFeatureService.getCoplanarFaceIdLists(needAdjacencyMatrix);
		System.out.println("共面的邻接图子图面集合为：" + coplanarFaceIdLists);
		
		List<Integer> childListIndexs = new ArrayList<Integer>();
		List<Integer> coplanarFaceIdIndexs = new ArrayList<Integer>();
		
		//根据共面的子图来更新第一个子图面集合
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
		
		//进行删除操作
		for(int i : childListIndexs){
			childMatrixLists.remove(i);
		}
		//进行添加操作
		for(int i : coplanarFaceIdIndexs){
			childMatrixLists.add(coplanarFaceIdLists.get(i));
		}
		
		System.out.println("最终获取的子图矩阵为 ：" + childMatrixLists);
	}
}
