package com.vp.designintent;

import java.util.List;

import com.vp.semanticcell.SemanticCell;
import com.vp.dao.LinkDAO;
import com.vp.dao.SemanticCellDAO;
import com.vp.domain.Link;
import com.vp.xml.ParseXML;

/**
 * ���Զ���������ͼ֪ʶͼ����ӵ�neo4j���ݿ���
 * @author admin
 *
 */
public class InitDataBaseByDI {
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	private static LinkDAO linkDAO = new LinkDAO();
	
	public static void init(List<SemanticCell> scs, List<Link> links){
		for(SemanticCell semanticCell : scs){
			semanticCellDAO.createSemanticCell(semanticCell);
		}
		for(Link link : links){
			linkDAO.createLink(link);
		}
	}
	
	public static void main(String[] args) {
		try {
			List<SemanticCell> scs = DesignIntentKnowledge.initDesignIntentKnowledgeNode();
			List<Link> links = DesignIntentKnowledge.initDesignIntentKnowledgeLink(scs);
			init(scs, links);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
