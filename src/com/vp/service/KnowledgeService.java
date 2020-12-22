package com.vp.service;

import java.util.List;

import com.vp.base.Knowledge;
import com.vp.dao.KnowledgeDAO;

/**
 * 关于知识库的一些操作
 * @author admin
 *
 */
public class KnowledgeService {
	private static KnowledgeDAO knowledgeDAO = new KnowledgeDAO();
	
	/**
	 * 创建知识库的一条记录，并返回创建的id值
	 * @param knowLedge
	 * @return
	 */
	public int createKnowledge(Knowledge knowledge){
		knowledgeDAO.addKnowledge(knowledge);
		return knowledgeDAO.getLastedId();
	}
	
	/**
	 * 根据relation获取Knowledge三元组
	 * @param relation
	 * @return
	 */
	public Knowledge getKnowledgeByRelation(String relation){
		return knowledgeDAO.getKnowledgeByRelation(relation);
	}
	
	/**
	 * 获取所有的三元组的关系列表，用来查看哪些三元组已经创建，哪些没有
	 * @return
	 */
	public List<String> getRelations(){
		return knowledgeDAO.getRelations();
	}
}
