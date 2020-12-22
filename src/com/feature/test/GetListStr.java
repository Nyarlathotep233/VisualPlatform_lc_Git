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
 
 *生成.txt文件
 */
public class GetListStr {
	
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	public static Gson gson = new Gson();
	
	public static void main(String[] args) {
		
		List<AdvancedFace> advancedFaceList = advancedFaceService.getAdvancedFaceList();
		//由于advacnedFaceList时间花费比较久，故先将其存入文档中，再读取出来
		String advancedFaceListStr = gson.toJson(advancedFaceList);
		
		System.out.println(advancedFaceListStr);
		try {
			File file = new File("test.txt");
			FileWriter fw = new FileWriter(file);
			fw.write(advancedFaceListStr);   
			fw.close();
			System.out.println("advancedFaceList.txt 生成成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
