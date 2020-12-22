package com.feature.test;

import java.util.List;

import com.feature.domain.AdvancedFace;
import com.feature.domain.EdgeCurve;
import com.feature.service.AdvancedFaceService;
import com.google.gson.Gson;

public class GetAAG {
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	public static Gson gson = new Gson();
	
	public static void main(String[] args) 
	{
		
		List<AdvancedFace> advancedFaceList = advancedFaceService.getAdvancedFaceList();
		String advancedFaceListStr = gson.toJson(advancedFaceList);
		
		String adjacencyTable[][] = new String[advancedFaceList.size()][3];
		for(int a=0;a<advancedFaceList.size();a++)
			for(int d=0;d<3;d++)
				adjacencyTable[a][d]="";
		
		for(int i = 0; i < advancedFaceList.size(); i++)
		{
				AdvancedFace face1 = advancedFaceList.get(i);
				adjacencyTable[i][0]=face1.getAdvancedFaceName();
				adjacencyTable[i][1]=String.valueOf(face1.getAdvancedFaceType());
//				System.out.println(face1);
			for(int j = 0; j < advancedFaceList.size(); j++)
			{
				AdvancedFace face2 = advancedFaceList.get(j);
				EdgeCurve edgeCurve = advancedFaceService.judgeIsIntersection(face1, face2);//判断是否相交并返回相交边
				if(edgeCurve != null&&face1.getAdvancedFaceName()!=face2.getAdvancedFaceName())
				{	
					String edgeName=edgeCurve.getEdge_curve_name();
					adjacencyTable[i][2] += '('+face2.getAdvancedFaceName()+','+advancedFaceService.getEdgeType(edgeName)+')';
				}
			}
		}
		for(int i = 0; i < advancedFaceList.size(); i++)
		{
			for(int j = 0; j < 3; j++)
			{
				System.out.print(adjacencyTable[i][j]+' ');
			}
			System.out.println();
		}
	}
}

