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
 * 处理neo4j图数据库中的节点和连接
 * @author admin
 *
 */
public class SemanticAndLinkService {
	
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	private static LinkDAO linkDAO = new LinkDAO();
	
	/**
	 * 获取所有的语义元节点集合
	 * @return
	 */
	public List<SemanticCell> getSCs(){
		return semanticCellDAO.getAllSemanticCellList();
	}
	/**
	 * 根据类型获取该类型的语义元节点集合
	 * @param type
	 * @return
	 */
	public List<SemanticCell> getSCsByType(String type){
		return semanticCellDAO.getSCsByType(type);
	}
	
	/**
	 * 获取所有的连线
	 * @return
	 */
	public List<Link> getLinks(){
		return linkDAO.getLinkList();
	}
	
	/**
	 * 根据起始语义元节点的id找出从该节点出发的连线
	 * @param fromId
	 * @return
	 */
	public List<Link> getLinksByFromId(String fromId){
		return linkDAO.getLinksByFromId(fromId);
	}
	
	/**
	 * 获取所有的语义元类型集合
	 * @return
	 */
	public List<String> getSCTypes(){
		return semanticCellDAO.getSCTypes();
	}
	
	/**
	 * 创建一个语义元节点
	 * @param sc
	 */
	public void createSemanticCell(SemanticCell sc){
		semanticCellDAO.createSemanticCell(sc);
	}
	
	/**
	 * 由Knowledge和权重创建连线
	 * @param knowledge
	 */
	public void createLinkByKnowledge(Knowledge knowledge, double weight){
		linkDAO.createLinkByKnowledge(knowledge,weight);
	}
	
	/**
	 * 获得所有的连线类型(link.name)集合
	 * @return
	 */
	public List<String> getLinkTypes() {
		return linkDAO.getLinkTypes();
	}
	
	/**
	 * 根据语义元节点instancedId找到他与其他节点连接的语义元节点
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByInstanceId(String instancedId){
		return semanticCellDAO.getSemanticCellsByInstanceId(instancedId);
	}
	
	/**
	 * 根据语义元节点instancedId和连接节点的type找到他与其他节点连接的语义元节点
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByIdAndType(String instancedId, String type){
		return semanticCellDAO.getSemanticCellsByIdAndType(instancedId, type);
	}
	
	/**
	 * 根据路径计算路径权重值
	 * @return
	 */
	public List<Map<String,Object>> getPathWeight(String path){
		return semanticCellDAO.getPathWeight(path);
	}
	
	/**
	 * 根据link两端的语义元的instanceId找到该link并改变其权重值
	 * @param instanceId1
	 * @param instanceId1
	 * @param weight
	 */
	public void updateWeightByInstanceIds(String instanceId1, String instanceId2, double weight){
		linkDAO.updateWeightByInstanceIds(instanceId1, instanceId2, weight);
	}
	
	/**
	 * 查找根节点
	 * @return
	 */
	public List<SemanticCell> getBaseSemanticCells(){
        List<SemanticCell> scs = semanticCellDAO.findBaseSemanticCells();
        return scs;
    }
	
	/**
	 * 查找从根节点到advanced_face，用于显示在界面上
	 * @return
	 */
	public ScsAndLinks getToFace() {
		ScsAndLinks scsAndLinks = semanticCellDAO.findToFace();
		return scsAndLinks;
	}
	
	/**
	 * 获取所有的设计意图语义元节点图
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
	 * 根据语义元节点数组，获取它们之间所有的连线
	 * @param scs
	 * @return
	 */
	public List<Link> getLinksByScs(List<SemanticCell> scs) {
		return linkDAO.getLinksByScs(scs);
	}
	
	/**
	 * 根据语义元节点instancedId数组查出属于该数组的语义元集合
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByIds(String[] instancedIds){
		return semanticCellDAO.getSemanticCellsByIds(instancedIds);
	}

}
