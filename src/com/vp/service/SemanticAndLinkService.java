package com.vp.service;

import java.util.List;
import java.util.Map;

import com.vp.semanticcell.SemanticCell;
import com.vp.base.Knowledge;
import com.vp.dao.LinkDAO;
import com.vp.dao.SemanticCellDAO;
import com.vp.domain.Link;
import com.vp.domain.ScsAndLinks;

/**
 * ����neo4jͼ���ݿ��еĽڵ������
 * @author admin
 *
 */
public class SemanticAndLinkService {
	
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	private static LinkDAO linkDAO = new LinkDAO();
	
	/**
	 * ��ȡ���е�����Ԫ�ڵ㼯��
	 * @return
	 */
	public List<SemanticCell> getSCs(){
		return semanticCellDAO.getAllSemanticCellList();
	}
	/**
	 * �������ͻ�ȡ�����͵�����Ԫ�ڵ㼯��
	 * @param type
	 * @return
	 */
	public List<SemanticCell> getSCsByType(String type){
		return semanticCellDAO.getSCsByType(type);
	}
	
	/**
	 * ��ȡ���е�����
	 * @return
	 */
	public List<Link> getLinks(){
		return linkDAO.getLinkList();
	}
	
	/**
	 * ������ʼ����Ԫ�ڵ��id�ҳ��Ӹýڵ����������
	 * @param fromId
	 * @return
	 */
	public List<Link> getLinksByFromId(String fromId){
		return linkDAO.getLinksByFromId(fromId);
	}
	
	/**
	 * ��ȡ���е�����Ԫ���ͼ���
	 * @return
	 */
	public List<String> getSCTypes(){
		return semanticCellDAO.getSCTypes();
	}
	
	/**
	 * ����һ������Ԫ�ڵ�
	 * @param sc
	 */
	public void createSemanticCell(SemanticCell sc){
		semanticCellDAO.createSemanticCell(sc);
	}
	
	/**
	 * ��Knowledge��Ȩ�ش�������
	 * @param knowledge
	 */
	public void createLinkByKnowledge(Knowledge knowledge, double weight){
		linkDAO.createLinkByKnowledge(knowledge,weight);
	}
	
	/**
	 * ������е���������(link.name)����
	 * @return
	 */
	public List<String> getLinkTypes() {
		return linkDAO.getLinkTypes();
	}
	
	/**
	 * ��������Ԫ�ڵ�instancedId�ҵ����������ڵ����ӵ�����Ԫ�ڵ�
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByInstanceId(String instancedId){
		return semanticCellDAO.getSemanticCellsByInstanceId(instancedId);
	}
	
	/**
	 * ��������Ԫ�ڵ�instancedId�����ӽڵ��type�ҵ����������ڵ����ӵ�����Ԫ�ڵ�
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByIdAndType(String instancedId, String type){
		return semanticCellDAO.getSemanticCellsByIdAndType(instancedId, type);
	}
	
	/**
	 * ����·������·��Ȩ��ֵ
	 * @return
	 */
	public List<Map<String,Object>> getPathWeight(String path){
		return semanticCellDAO.getPathWeight(path);
	}
	
	/**
	 * ����link���˵�����Ԫ��instanceId�ҵ���link���ı���Ȩ��ֵ
	 * @param instanceId1
	 * @param instanceId1
	 * @param weight
	 */
	public void updateWeightByInstanceIds(String instanceId1, String instanceId2, double weight){
		linkDAO.updateWeightByInstanceIds(instanceId1, instanceId2, weight);
	}
	
	/**
	 * ���Ҹ��ڵ�
	 * @return
	 */
	public List<SemanticCell> getBaseSemanticCells(){
        List<SemanticCell> scs = semanticCellDAO.findBaseSemanticCells();
        return scs;
    }
	
	/**
	 * ���ҴӸ��ڵ㵽advanced_face��������ʾ�ڽ�����
	 * @return
	 */
	public ScsAndLinks getToFace() {
		ScsAndLinks scsAndLinks = semanticCellDAO.findToFace();
		return scsAndLinks;
	}
	
	/**
	 * ��ȡ���е������ͼ����Ԫ�ڵ�ͼ
	 * @return
	 */
	public ScsAndLinks getSemanticNodesAndLinks() {
		ScsAndLinks scsAndLinks = new ScsAndLinks();
		List<SemanticCell> scs = semanticCellDAO.getAllSemanticScs();
		List<Link> links = linkDAO.getLinksByScs(scs);
		scsAndLinks.setScs(scs);
		scsAndLinks.setLinks(links);
		return scsAndLinks;
	}
	
	/**
	 * ��������Ԫ�ڵ����飬��ȡ����֮�����е�����
	 * @param scs
	 * @return
	 */
	public List<Link> getLinksByScs(List<SemanticCell> scs) {
		return linkDAO.getLinksByScs(scs);
	}
	
	/**
	 * ��������Ԫ�ڵ�instancedId���������ڸ����������Ԫ����
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByIds(String[] instancedIds){
		return semanticCellDAO.getSemanticCellsByIds(instancedIds);
	}

}
