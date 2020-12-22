package com.feature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.feature.semanticcell.Body;
import com.feature.semanticcell.Head;
import com.feature.semanticcell.SemanticCell;
import com.feature.semanticcell.Tail;
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
		
			//MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell) where n1.type="advanced_face" and n2.type="face_bound" and n3.type="edge_loop" return (r1.weight+r2.weight),n1.instanceId,n2.instancedId
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
}
