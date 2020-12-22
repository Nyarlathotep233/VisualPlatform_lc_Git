package com.vp.reason;

import java.util.List;

import com.vp.base.Knowledge;
import com.vp.dao.KnowledgeDAO;
import com.vp.dao.LinkDAO;
import com.vp.domain.Link;

/**
 * ��ͼ���ݿ��ж�ȡ��Ӧ����Ԫ�飬��ŵ�֪ʶ����
 * @author admin
 *
 */
public class KnowLedgeExtraction {
	private static LinkDAO linkDAO = new LinkDAO();
	private static KnowledgeDAO knowledgeDAO = new KnowledgeDAO();
	
	/**
	 * ��ͼ���ݿ��links�г�ȡ��Ԫ��д�뵽֪ʶ����
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
