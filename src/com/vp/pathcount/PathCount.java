package com.vp.pathcount;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vp.semanticcell.SemanticCell;
import com.vp.dao.LinkDAO;
import com.vp.dao.PathDAO;
import com.vp.dao.SemanticCellDAO;
import com.vp.domain.Link;
import com.vp.xml.ParseXML;

/**
 * �������·���ĸ�������ѵ����
 * @author admin
 *
 */
public class PathCount {
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	private static LinkDAO linkDAO = new LinkDAO();
	private static PathDAO pathDAO = new PathDAO();
	
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
		
		String filePath = "E:\\gears\\";
		
		File mainDirectory = new File(filePath);
		
		File[] directorys = mainDirectory.listFiles();
		
		for(File directory : directorys){
			File[] files = directory.listFiles();
			for(File file : files){
				//1���Ѳ�ͬ��step�ļ����뵽neo4jͼ���ݿ���
				//(1)���neo4jͼ���ݿ�
				linkDAO.deleteAllLinks();
				semanticCellDAO.deleteAllSCs();
				
				//(2)��ȡxml�ļ�
				//(3)���뵽neo4jͼ������
				try {
					List<SemanticCell> scs = ParseXML.getSemanticCells(file.toString());
					List<Link> links = ParseXML.getLinks(scs);
					init(scs,links);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//2������neo4jͼ���ݿ�õ�����·���ĸ����洢��map��
				String type1 = "advanced_face";
				String[] types = {"face_bound","face_outer_bound","plane","edge_loop","cylindrical_surface","toroidal_surface",
						"sherical_surface","conical_surface","axis2_placement_3d","oriented_edge","edge_curve","line","circle"};
				Map<String,Integer> map = new HashMap<String, Integer>();
				for(String type2 : types){
					String path = type1 + "/" + type2;
					int count = pathDAO.getLinkCountByTypes(type1, type2);
					map.put(path, count);
				}
				
				//3�����ɶ�Ӧ��txt�ļ�
				StringBuffer keySb = new StringBuffer();
				StringBuffer countSb = new StringBuffer();
				for(Map.Entry<String, Integer> entry : map.entrySet()){
					String key = entry.getKey();
					int value = entry.getValue();
					keySb.append(key);
					keySb.append(" ");
					countSb.append(value);
					countSb.append(" ");
				}
				FileWriter fw = null;
				try {
					File f = new File("traindata.txt");
					fw = new FileWriter(f, true);
					PrintWriter pw = new PrintWriter(fw);
//					pw.append(keySb.toString());
//					pw.append("\n");
					pw.append(file.toString());
					pw.append("\n");
					pw.append(countSb.toString());
					pw.append("\n");
					fw.flush();
					pw.flush();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
