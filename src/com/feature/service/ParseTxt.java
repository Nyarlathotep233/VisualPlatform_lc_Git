package com.feature.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.feature.domain.AdvancedFace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseTxt {
	
	public static Gson gson = new Gson();
	
	//根据txt文档获取List<AdvancedFace>
	public static List<AdvancedFace> getAdvancedFaceListFromTxt(String fileName){
		List<AdvancedFace> advancedFaceList = new ArrayList<AdvancedFace>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String s = br.readLine();
			Type advancedFaceType = new TypeToken<ArrayList<AdvancedFace>>(){}.getType();
			advancedFaceList = gson.fromJson(s,advancedFaceType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return advancedFaceList;
	}
}
