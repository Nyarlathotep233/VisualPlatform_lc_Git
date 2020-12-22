package com.vp.test;

import java.util.List;
import java.util.Map;

import com.vp.semanticcell.SemanticCell;
import com.vp.dao.LinkDAO;
import com.vp.dao.SemanticCellDAO;
import com.vp.domain.Link;

public class TestNeo4j {
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	private static LinkDAO linkDAO = new LinkDAO();
	
	public static void main(String[] args) {
//		List<SemanticCell> scs = semanticCellDAO.getAllSemanticCellList();
//		for(SemanticCell sc : scs){
//			System.out.println(sc);
//		}
//		List<Link> links = linkDAO.getLinkList();
//		for(Link link : links){
//			System.out.println(link);
//		}
		String path = "advanced_face/face_bound/edge_loop/oriented_edge";
		List<Map<String,Object>> instancePathList = semanticCellDAO.getPathWeight(path);
		for(Map<String, Object> map : instancePathList){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		}
	}
}
