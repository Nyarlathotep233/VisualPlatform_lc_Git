package com.vp.test;

import com.vp.dao.LinkDAO;
import com.vp.domain.Link;

public class ExtraTest2 {
	
	public static LinkDAO linkDAO = new LinkDAO();
	
	public static void main(String[] args) {
		
		//Ìí¼ÓÁ´½Ó
		String[] ids = {"#177", "%convex_feature"};
		
		System.out.println(ids[0].substring(1));
		
		Link link = new Link();
		link.setName(ids[0].substring(1) + "_advanced_face");
		link.setFromId(ids[0]);
		link.setToId(ids[1]);
		link.setFromToId(ids[0] + "->" + ids[1]);
		link.setType("semanticLink");
		link.setWeight(1.0);
		
		linkDAO.createLink(link);
	}
}
