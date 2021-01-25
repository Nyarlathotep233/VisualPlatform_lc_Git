package com.vp.xml;

import java.util.List;

import com.vp.semanticcell.SemanticCell;
import com.vp.dao.LinkDAO;
import com.vp.dao.SemanticCellDAO;
import com.vp.domain.Link;


/**
 * ����ȡ��xml�ļ�д�뵽neo4j���ݿ���
 * @author admin
 *
 */
public class InitDataBaseByXML {
	
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	private static LinkDAO linkDAO = new LinkDAO();
	
	//��ͼ���ݿ��в�����Ϣ
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
			//�������������������ͼ���ݿ�
			linkDAO.deleteAllLinks();
			semanticCellDAO.deleteAllSCs();
			//����ָ����xml�ļ�
//			List<SemanticCell> scs = ParseXML.getSemanticCells("zhouTry.xml");
//			List<Link> links = ParseXML.getLinks(scs);
//			//�������õ�����Ԫ�����ӵļ������ӵ�ͼ���ݿ�
//			init(scs,links);
//			System.out.println("��ɴ���ͼ���ݿ�");
//			List<SemanticCell> scs = ParseXML.getSemanticCells("firstXML\\zhoucheng206.xml");
//			List<Link> links = ParseXML.getLinks(scs);
			//�������õ�����Ԫ�����ӵļ������ӵ�ͼ���ݿ�
//			init(scs,links);
			
			//���붨��������ͼ
//			List<SemanticCell> scs2 = ParseXML.getSemanticCells("designintent.xml");
//			List<Link> links2 = ParseXML.getLinks(scs2);
//			init(scs2,links2);
			
			//���붨�������Ԫ
//			List<SemanticCell> scs3 = ParseXML.getSemanticCells("feature.xml");
//			List<Link> links3 = ParseXML.getLinks(scs3);
//			init(scs3,links3);

//			List<SemanticCell> scs3 = ParseXML.getSemanticCells("tolerance.xml");
//			List<Link> links3 = ParseXML.getLinks(scs3);
//			init(scs3,links3);
			
			//����װ��������Ԫ
//			List<SemanticAssembly> scs4 = ParseXML.getAssemblySemantics("example.xml");
//			List<Link> links4 = ParseXML.getLinks(scs4);
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}