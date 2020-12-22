package com.feature.test;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feature.domain.AdvancedFace;
import com.feature.service.AdvancedFaceService;
import com.google.gson.Gson;

/*
 
 *����.txt�ļ�
 */
public class GetListStr {
	
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	public static Gson gson = new Gson();
	
	public static void main(String[] args) {
		
		List<AdvancedFace> advancedFaceList = advancedFaceService.getAdvancedFaceList();
		//����advacnedFaceListʱ�仨�ѱȽϾã����Ƚ�������ĵ��У��ٶ�ȡ����
		String advancedFaceListStr = gson.toJson(advancedFaceList);
		
		System.out.println(advancedFaceListStr);
		try {
			File file = new File("test.txt");
			FileWriter fw = new FileWriter(file);
			fw.write(advancedFaceListStr);   
			fw.close();
			System.out.println("advancedFaceList.txt ���ɳɹ���");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
