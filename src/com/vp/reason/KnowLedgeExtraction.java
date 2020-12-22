package com.vp.reason;

import java.util.List;

import com.vp.base.Knowledge;
import com.vp.dao.KnowledgeDAO;
import com.vp.dao.LinkDAO;
import com.vp.domain.Link;

/**
 * 从图数据库中读取相应的三元组，存放到知识库中
 * @author admin
 *
 */
public class KnowLedgeExtraction {
	private static LinkDAO linkDAO = new LinkDAO();
	private static KnowledgeDAO knowledgeDAO = new KnowledgeDAO();
	
	/**
	 * 从图数据库的links中抽取三元组写入到知识库中
	 */
	public static void extractKnowledgeFromLinks(List<Link> links){
		for(Link link : links){
			String linkName = link.getName();
			String[] entitys = linkName.split("_contains_");
			Knowledge knowledge = new Knowledge();
			knowledge.setEntityType1(entitys[0]);
			knowledge.setRelation(linkName);
			knowledge.setEntityType2(entitys[1]);
			knowledgeDAO.addKnowledge(knowledge);
		}
	}
	
	public static void main(String[] args) {
		List<Link> links = linkDAO.getLinkList();
		extractKnowledgeFromLinks(links);
	}
	
}
