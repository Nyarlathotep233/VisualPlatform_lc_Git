package com.feature.test;

import com.feature.service.AdvancedFaceService;

public class Test1 {
	public static AdvancedFaceService advancedFaceService = new AdvancedFaceService();
	
	public static void main(String[] args) {
		System.out.println(advancedFaceService.advancedFaceHas3());
		System.out.println(advancedFaceService.getFirstAdvancedFaceId());
	}

}
