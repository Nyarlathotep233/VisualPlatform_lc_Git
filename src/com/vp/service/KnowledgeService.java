package com.vp.service;

import java.util.List;

import com.vp.base.Knowledge;
import com.vp.dao.KnowledgeDAO;

/**
 * ����֪ʶ���һЩ����
 * @author admin
 *
 */
public class KnowledgeService {
	private static KnowledgeDAO knowledgeDAO = new KnowledgeDAO();
	
	/**
	 * ����֪ʶ���һ����¼�������ش�����idֵ
	 * @param knowLedge
	 * @return
	 */
	public int createKnowledge(Knowledge knowledge){
		knowledgeDAO.addKnowledge(knowledge);
		return knowledgeDAO.getLastedId();
	}
	
	/**
	 * ����relation��ȡKnowledge��Ԫ��
	 * @param relation
	 * @return
	 */
	public Knowledge getKnowledgeByRelation(String relation){
		return knowledgeDAO.getKnowledgeByRelation(relation);
	}
	
	/**
	 * ��ȡ���е���Ԫ��Ĺ�ϵ�б������鿴��Щ��Ԫ���Ѿ���������Щû��
	 * @return
	 */
	public List<String> getRelations(){
		return knowledgeDAO.getRelations();
	}
}
