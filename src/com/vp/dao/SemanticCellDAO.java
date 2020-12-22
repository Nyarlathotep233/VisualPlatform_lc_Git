package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.vp.domain.Link;
import com.vp.domain.ScsAndLinks;
import com.vp.semanticcell.Body;
import com.vp.semanticcell.Head;
import com.vp.semanticcell.SemanticCell;
import com.vp.semanticcell.Tail;
import com.vp.util.Neo4jUtil;

public class SemanticCellDAO {
	
	private Gson gson = new Gson();
	
	/**
	 * 创建Neo4j的语义元节点
	 * @param sc
	 */
	public void createSemanticCell(SemanticCell sc){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = Neo4jUtil.getConnection();
			String sql = "CREATE (n:SemanticCell {instanceId:{1},type:{2},name:{3},head:{4},body:{5},tail:{6}})";
			preparedStatement=connection.prepareStatement(sql);
			String instanceId = sc.getBody().getInstance().getInstanceId();
			String name = instanceId + "_" + sc.getType();
			String headJson = gson.toJson(sc.getHead());
			String bodyJson = gson.toJson(sc.getBody());
			String tailJson = gson.toJson(sc.getTail());
			preparedStatement.setObject(1, instanceId);
			preparedStatement.setObject(2, sc.getType());
			preparedStatement.setObject(3, name);
			preparedStatement.setObject(4, headJson);
			preparedStatement.setObject(5, bodyJson);
			preparedStatement.setObject(6, tailJson);
			preparedStatement.execute();
			System.out.println("complete：" + name);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Neo4jUtil.release(null, preparedStatement, connection);
		}
	}
	
	/**
	 * 查询出所有的SemanticCell 节点存入到一个list中
	 * @return
	 */
	public List<SemanticCell> getAllSemanticCellList(){
		List<SemanticCell> scs = new ArrayList<>();
		
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) RETURN n.type,n.head,n.body,n.tail";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				SemanticCell sc = new SemanticCell();
				sc.setType(resultSet.getString(1));
				String headStr = resultSet.getString(2);
				Head head = gson.fromJson(headStr, Head.class);
				sc.setHead(head);
				String bodyStr = resultSet.getString(3);
				Body body = gson.fromJson(bodyStr, Body.class);
				sc.setBody(body);
				String tailStr = resultSet.getString(4);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				sc.setTail(tail);
				
				if(!scs.contains(sc)){
					scs.add(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		
		return scs;
	}

	/**
	 * 获得所有的语义元类型集合
	 * @return
	 */
	public List<String> getSCTypes() {
		List<String> types = new ArrayList<>();
		
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) RETURN n.type,count(n.type)";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String type = resultSet.getString(1);
				int count = resultSet.getInt(2);
				if(!types.contains(type)){
					types.add(type);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		
		return types;
	}

	/**
	 * 根据类型获取该类型的语义元节点集合
	 * @param type
	 * @return
	 */
	public List<SemanticCell> getSCsByType(String type) {
		List<SemanticCell> scs = new ArrayList<>();
		
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) WHERE n.type={1} RETURN n.type,n.head,n.body,n.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, type);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				SemanticCell sc = new SemanticCell();
				sc.setType(resultSet.getString(1));
				String headStr = resultSet.getString(2);
				Head head = gson.fromJson(headStr, Head.class);
				sc.setHead(head);
				String bodyStr = resultSet.getString(3);
				Body body = gson.fromJson(bodyStr, Body.class);
				sc.setBody(body);
				String tailStr = resultSet.getString(4);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				sc.setTail(tail);
				
				if(!scs.contains(sc)){
					scs.add(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		
		return scs;
	}
	
	/**
	 * 根据语义元节点instancedId找到他与其他节点连接的语义元节点
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByInstanceId(String instancedId){
		List<SemanticCell> scs = new ArrayList<SemanticCell>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell)-[r:Link]->(n2:SemanticCell) where n1.instanceId={1} RETURN n2.type,n2.head,n2.body,n2.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, instancedId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				SemanticCell sc = new SemanticCell();
				sc.setType(resultSet.getString(1));
				String headStr = resultSet.getString(2);
				Head head = gson.fromJson(headStr, Head.class);
				sc.setHead(head);
				String bodyStr = resultSet.getString(3);
				Body body = gson.fromJson(bodyStr, Body.class);
				sc.setBody(body);
				String tailStr = resultSet.getString(4);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				sc.setTail(tail);
				
				if(!scs.contains(sc)){
					scs.add(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return scs;
	}
	
	/**
	 * 根据语义元节点instancedId和连接节点的type找到他与其他节点连接的语义元节点
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByIdAndType(String instancedId, String type){
		List<SemanticCell> scs = new ArrayList<SemanticCell>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell)-[r:Link]->(n2:SemanticCell) where n1.instanceId={1} and n2.type={2} RETURN n2.type,n2.head,n2.body,n2.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, instancedId);
			preparedStatement.setObject(2, type);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				SemanticCell sc = new SemanticCell();
				sc.setType(resultSet.getString(1));
				String headStr = resultSet.getString(2);
				Head head = gson.fromJson(headStr, Head.class);
				sc.setHead(head);
				String bodyStr = resultSet.getString(3);
				Body body = gson.fromJson(bodyStr, Body.class);
				sc.setBody(body);
				String tailStr = resultSet.getString(4);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				sc.setTail(tail);
				
				if(!scs.contains(sc)){
					scs.add(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return scs;
	}
	
	/**
	 * 根据路径计算路径权重值
	 * @return
	 */
	public List<Map<String,Object>> getPathWeight(String path){
		String[] types = path.split("/");
		int size = types.length;
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			StringBuffer sb = new StringBuffer("MATCH p=");
			for(int i = 1; i < size; i++){
				String str = "(n" + i + ":SemanticCell)-[r" + i + ":Link]->";
				sb.append(str);
			}
			String lastStr = "(n" + size + ":SemanticCell) where ";
			sb.append(lastStr);
			for(int i = 1; i < size; i++){
				String str = "n" + i +".type={" + i + "} and ";
				sb.append(str);
			}
			String lastStr2 = "n" + size + ".type={" + size + "} return (";
			sb.append(lastStr2);
			for(int i = 1; i < size - 1; i++){
				String str = "r" + i + ".weight+";
				sb.append(str);
			}
			String lastStr3 = "r" + (size - 1) + ".weight),";
			sb.append(lastStr3);
			
			for(int i = 1; i < size; i++){
				String str = "n" + i + ".instanceId,";
				sb.append(str);
			}
			
			String lastStr4 = "n" + size + ".instanceId;";
			sb.append(lastStr4);
			
			String sql = sb.toString();
//			System.out.println(sql);
		
			//MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:semanticCell) where n1.type="advanced_face" and n2.type="face_bound" and n3.type="edge_loop" return (r1.weight+r2.weight),n1.instanceId,n2.instancedId
			preparedStatement=connection.prepareStatement(sql);

			for(int i = 0; i < size; i++){
				preparedStatement.setObject((i + 1), types[i]);
			}
			
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Map<String,Object> map = new HashMap<String, Object>(); 
				StringBuffer idPath = new StringBuffer();
				double weight = resultSet.getDouble(1);
				for(int i = 1; i < size; i++){
					idPath.append(resultSet.getString(i+1) + "/");
				}
				idPath.append(resultSet.getString(size+1));
				map.put("instancePath", idPath.toString());
				map.put("weight", weight);
				result.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return result;
	}
	
	/**
	 * 删除Neo4j所有语义元节点
	 * @param sc
	 */
	public void deleteAllSCs(){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = Neo4jUtil.getConnection();
			String sql = "MATCH (n:SemanticCell) delete n";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.execute();
			System.out.println("All SemanticCells have deleted");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Neo4jUtil.release(null, preparedStatement, connection);
		}
	} 
	
	/**
	 * 查找根节点
	 * @return
	 */
    public List<SemanticCell> findBaseSemanticCells(){
        List<SemanticCell> scs = new ArrayList<SemanticCell>();
        Connection  connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Neo4jUtil.getConnection();
            String sql="MATCH (ee:SemanticCell) WHERE ee.type = \"manifold_solid_brep\" RETURN ee.type,ee.head,ee.body,ee.tail";
            preparedStatement=connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                SemanticCell sc = new SemanticCell();
                sc.setType(resultSet.getString(1));
                String headStr = resultSet.getString(2);
                Head head = gson.fromJson(headStr, Head.class);
                sc.setHead(head);
                String bodyStr = resultSet.getString(3);
                Body body = gson.fromJson(bodyStr, Body.class);
                sc.setBody(body);
                String tailStr = resultSet.getString(4);
                Tail tail = gson.fromJson(tailStr, Tail.class);
                sc.setTail(tail);

                if(!scs.contains(sc)){
                    scs.add(sc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Neo4jUtil.release(resultSet, preparedStatement, connection);
        }
        return scs;
    }
    /**
     * 查找到advanced_face的结点和连线
     * @return
     */
    public ScsAndLinks findToFace() {
    	ScsAndLinks scsAndLinks = new ScsAndLinks();
    	List<SemanticCell> scs =new ArrayList<SemanticCell>();
    	List<Link> links = new ArrayList<Link>();
    	List<String> ids =new ArrayList<String>();
    	List<String> linkids =new ArrayList<String>();
    	Connection  connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Neo4jUtil.getConnection();
            String sql="MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell) where n1.type=\"manifold_solid_brep\" and n3.type=\"advanced_face\" RETURN "
            		+ "n1.type,n1.head,n1.body,n1.tail,n1.instanceId,"
            		+ "n2.type,n2.head,n2.body,n2.tail,n2.instanceId,"
            		+ "n3.type,n3.head,n3.body,n3.tail,n3.instanceId,"
            		+ "r1.fromId,r1.toId,r1.name ,r1.type,r1.weight,r1.fromToId,"
            		+ "r2.fromId,r2.toId,r2.name ,r2.type,r2.weight,r2.fromToId";
            preparedStatement=connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                for(int i=1; i<=3; i++) {
                	SemanticCell sc = new SemanticCell();
                	sc.setType(resultSet.getString(i*5-4));
                	String headStr = resultSet.getString(i*5-3);
                	Head head = gson.fromJson(headStr, Head.class);
                	sc.setHead(head);
                	String bodyStr = resultSet.getString(i*5-2);
                	Body body = gson.fromJson(bodyStr, Body.class);
                	sc.setBody(body);
                	String tailStr = resultSet.getString(i*5-1);
                	Tail tail = gson.fromJson(tailStr, Tail.class);
                	sc.setTail(tail);
                	String instanceIdStr = resultSet.getString(i*5);
                	if(!ids.contains(instanceIdStr)){
                		ids.add(instanceIdStr);
                		scs.add(sc);
                	}
                }
                for(int i = 1 ; i<=2; i++) {
                	Link link = new Link();
                	link.setFromId(resultSet.getString(i*6+10));
                	link.setToId(resultSet.getString(i*6+11));
                	link.setName(resultSet.getString(i*6+12));
                	link.setType(resultSet.getString(i*6+13));
                	double weight = Double.parseDouble(resultSet.getString(i*6+14));
                	link.setWeight(weight);
                	link.setFromToId(resultSet.getString(i*6+15));
                	String linkidsStr = resultSet.getString(i*6+15);
                	if(!linkids.contains(linkidsStr)){
                		linkids.add(linkidsStr);
                		links.add(link);
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Neo4jUtil.release(resultSet, preparedStatement, connection);
        }
        scsAndLinks.setLinks(links);
        scsAndLinks.setScs(scs);
        return scsAndLinks;
    }

    /**
     * 获取所有的设计意图语义元节点
     * @return
     */
	public List<SemanticCell> getAllSemanticScs() {
		List<SemanticCell> scs = new ArrayList<SemanticCell>();
        Connection  connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Neo4jUtil.getConnection();
            String sql="match (n:SemanticCell) where n.instanceId =~'%.*' return n.type,n.head,n.body,n.tail";
            preparedStatement=connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                SemanticCell sc = new SemanticCell();
                sc.setType(resultSet.getString(1));
                String headStr = resultSet.getString(2);
                Head head = gson.fromJson(headStr, Head.class);
                sc.setHead(head);
                String bodyStr = resultSet.getString(3);
                Body body = gson.fromJson(bodyStr, Body.class);
                sc.setBody(body);
                String tailStr = resultSet.getString(4);
                Tail tail = gson.fromJson(tailStr, Tail.class);
                sc.setTail(tail);

                if(!scs.contains(sc)){
                    scs.add(sc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Neo4jUtil.release(resultSet, preparedStatement, connection);
        }
        return scs;
	}
	
	/**获取advancedFace的类型
	 * @return
	 */
	public int getAdvancedFaceType(String advancedFaceId){
		int advancedFaceType = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql=" MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)"
						+ "where n1.instanceId={1} and (n2.type=\"plane\" or "
						+ "n2.type= \"cylindrical_surface\" or "
						+ "n2.type= \"toroidal_surface\" or "
						+ "n2.type= \"sherical_surface\" or "
						+ "n2.type= \"conical_surface\") return n2.type"	;
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,advancedFaceId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				if(resultSet.getString(1).equals("cylindrical_surface")){
					advancedFaceType = 1;
				}else if(resultSet.getString(1).equals("toroidal_surface")){
					advancedFaceType = 2;
				}else if(resultSet.getString(1).equals("sherical_surface")){
					advancedFaceType = 3;
				}else if(resultSet.getString(1).equals("conical_surface")){
					advancedFaceType = 4;
				}else{
					advancedFaceType = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFaceType;
	}
	
	/**
	 * 根据语义元节点instancedId数组查出属于该数组的语义元集合
	 * @param instancedId
	 * @return
	 */
	public List<SemanticCell> getSemanticCellsByIds(String[] instancedIds){
		List<SemanticCell> scs = new ArrayList<SemanticCell>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) where n.instanceId in {1} RETURN n.type,n.head,n.body,n.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, instancedIds);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				SemanticCell sc = new SemanticCell();
				sc.setType(resultSet.getString(1));
				String headStr = resultSet.getString(2);
				Head head = gson.fromJson(headStr, Head.class);
				sc.setHead(head);
				String bodyStr = resultSet.getString(3);
				Body body = gson.fromJson(bodyStr, Body.class);
				sc.setBody(body);
				String tailStr = resultSet.getString(4);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				sc.setTail(tail);
				
				if(!scs.contains(sc)){
					scs.add(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return scs;
	}

}
