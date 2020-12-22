package com.vp.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.vp.base.Knowledge;
import com.vp.dao.KnowledgeDAO;
import com.vp.dao.RuleDAO;

public class TestMysql {
	
	private static KnowledgeDAO knowledgeDAO;
	
	static{
		knowledgeDAO = new KnowledgeDAO();
	}
	
	public static void main(String[] args) {
//		Knowledge knowledge = new Knowledge();
//		knowledge.setEntityType1("edge_curve");
//		knowledge.setRelation("edge_curve_contains_vertex_point");
//		knowledge.setEntityType2("vertex_point");
		knowledgeDAO.getRelations();
		System.out.println("haha");
	}
	
}
