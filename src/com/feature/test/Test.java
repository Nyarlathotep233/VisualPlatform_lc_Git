package com.feature.test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.feature.service.AdjacencyMatrixService;

public class Test {
	
	public static AdjacencyMatrixService adjacencyMatrixService = new AdjacencyMatrixService();
	
	public static void main(String[] args) {
		String fileName = "test.txt";
		//ÁÚ½ÓÍ¼¾ØÕó
		int[][] adjacencyMatrix = adjacencyMatrixService.getAdjacencyMatrixFromAdvancedFaceList(fileName);
//		int[][] needAdjacencyMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
//		for(int i = 0; i < adjacencyMatrix.length; i++){
//			for(int j = 0; j < adjacencyMatrix.length; j++){
//				needAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
//			}
//		}
		System.out.println("ÊôÐÔÁÚ½ÓÍ¼¾ØÕóÎª£º");
		try {
//			File file = new File("all_plane21.txt");
//			FileWriter fw = new FileWriter(file);
			for(int i = 0; i < adjacencyMatrix.length; i++){
				for(int j = 0; j < adjacencyMatrix.length; j++){
					System.out.print(adjacencyMatrix[i][j] + "\t");
//					fw.write(adjacencyMatrix[i][j] + "\t");   
						
				}
				System.out.println();
//				fw.write("\r\n");
			}
//			fw.close();
			//¼ò»¯Í¼¾ØÕó
			List<List<Integer>> childMatrixLists = new ArrayList<>();
			adjacencyMatrixService.simplifyMatrix(adjacencyMatrix, childMatrixLists);
			System.out.println(childMatrixLists);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		adjacencyMatrixService.isTheOne(childMatrixLists,needAdjacencyMatrix);
//		int[][] theOne = adjacencyMatrixService.isTheOne(childMatrixLists,needAdjacencyMatrix);
//		System.out.println("theOneÎª£º");
//		for(int i = 0; i < theOne.length; i++){
//			for(int j = 0; j < theOne.length; j++){
//				System.out.print(theOne[i][j] + "\t");
//			}
//			System.out.println();
//		}
//		List<List<Integer>> newChildMatrixLists = new ArrayList<>();
//		adjacencyMatrixService.simplifyMatrix(theOne, newChildMatrixLists);
//		System.out.println(theOne);
//		adjacencyMatrixService.changeTheOne(theOne);
	}
}
