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
import com.feature.semanticcell.Tail;
import com.feature.domain.Point;
import com.vp.util.Neo4jUtil;

public class AdvancedFaceDAO {
	private  Gson gson = new Gson();
	
	/**获取advancedFace的ID
	 * @return
	 */
	public List<String> getAdvancedFaceName(){
		List<String> advancedFaceNameList = new ArrayList<String>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql = " Match (n: SemanticCell) where n.type = \"advanced_face\""
					     + " return n.instanceId"; 
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String advancedFaceName = resultSet.getString(1);
				advancedFaceNameList.add(advancedFaceName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFaceNameList;
	}
	
	/**获取advancedFace的类型
	 * @return
	 */
	public int getAdvancedFaceType(String advancedFaceName){
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
			preparedStatement.setObject(1,advancedFaceName);
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
	
	/**获取advancedFace的方向标志布尔值
	 * @return
	 */
	public String getAdvancedFaceFlag(String advancedFaceName){
		String advancedFaceFlag = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="MATCH (n:SemanticCell) WHERE n.instanceId={1} RETURN n.tail,n.instanceId;";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,advancedFaceName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String tailStr = resultSet.getString(1);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				advancedFaceFlag = tail.getAttribute().get("same_sense").substring(1, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFaceFlag;
	}
	
	/**获取face_bound或face_out_bound的方向标志布尔值
	 * @return
	 */
	public String getFaceBoundFlag(String advancedFaceName){
		String faceBoundFlag = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql = "MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)"
					+ "where n1.instanceId={1} and n2.type=\"face_bound\" or n2.type= \"face_outer_bound\""
					+ "RETURN n2.tail "; 
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,advancedFaceName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String tailStr = resultSet.getString(1);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				faceBoundFlag = tail.getAttribute().get("orientation").substring(1, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return faceBoundFlag;
	}
	
	/**获取advancedFace的方向矢量
	 * 根据advancedFace和axis2_placement_3d_directionID的第一个direction的id
	 * @param advancedFaceName
	 * @return
	 */
	public Point getAdvancedFaceDirection(String advancedFaceName){
		
		Point advancedFacedirection = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet directionResultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql = "MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell)"
							+ "where n1.instanceId={1} and n2.type=\"plane\" or n2.type= \"cylindrical_surface\""
							+ " and n3.type=\"axis2_placement_3d\""
							+ "RETURN n3.head "; 
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,advancedFaceName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String headStr = resultSet.getString(1);
				Head head = gson.fromJson(headStr, Head.class);
				String axis2_placement_3d_directionID = head.getGeometryLink().getGeometryMap().get("axis");
				String queryDirsql = "MATCH (n:SemanticCell) WHERE n.instanceId={1} RETURN n.body";
				preparedStatement=connection.prepareStatement(queryDirsql);
				preparedStatement.setObject(1,axis2_placement_3d_directionID);
				directionResultSet = preparedStatement.executeQuery();
				while(directionResultSet.next()){
					String bodyStr = directionResultSet.getString(1);
					Body body = gson.fromJson(bodyStr, Body.class);
					advancedFacedirection = body.getInstance().getCoordinates();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFacedirection;
	}
	
	/**获取包含3个（包含）或以上的edge_loop的advanced_face的编号
	 * @return
	 */
	public Map<Integer,Integer> advancedFaceHas3(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Map<Integer,Integer> scs = new HashMap<Integer,Integer>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell{type:\"advanced_face\"})-[:Link*]->(end:SemanticCell) "
					+ "where (end.type=\"face_bound\" or end.type=\"face_outer_bound\") "
					+ "return start.instanceId,count(*)"	;
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){				
				if(resultSet.getInt(2)>=3) {
					//将得到的#56 变成56
					int a = Integer.valueOf(resultSet.getString(1).substring(1));
					scs.put(a, resultSet.getInt(2));
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
	 * 获取由advanced_face的id组成的List
	 * 
	 */
	public List<Integer> getAdvancedFaceId(){
		List<Integer> advancedFaceId = new ArrayList<>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql = "MATCH (ee:SemanticCell) WHERE ee.type = \"advanced_face\" RETURN ee.instanceId"; 
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){				
				String id = resultSet.getString(1).substring(1);
				advancedFaceId.add(Integer.valueOf(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFaceId;
	}

	
	/**
	 * 从数据库中查出有circle的advanced_face；以Map形式返回
	 * 
	 * @return
	 */
	public Map<String, List<String>> findADFaceHasCircle() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Map<String, List<String>> scs = new HashMap<String, List<String>>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (n1:SemanticCell{type:\"advanced_face\"})-[r1:Link*]->(n2:SemanticCell)-[r2:Link*]->(n3:SemanticCell{type:\"circle\"}) "
					+ "where (n2.type=\"face_bound\" or n2.type=\"face_outer_bound\") "
					+ "return n1.instanceId,n3.instanceId;"	;
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String key = resultSet.getString(1);
				String value = resultSet.getString(2);
				if(!scs.containsKey(key)) {
					List<String> list = new ArrayList<>();
					list.add(value);
					scs.put(key,list);
				}else {
					List<String> list = scs.get(key);
					list.add(value);
					scs.put(key, list);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return scs;
	}
	
	/**
	 * 获取包含2个（包含）或以上的edge_loop的advanced_face的编号
	 * @return
	 */
	public List<String> advancedFaceHas2(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> scs = new ArrayList<>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell{type:\"advanced_face\"})-[:Link*]->(end:SemanticCell) "
					+ "where (end.type=\"face_bound\" or end.type=\"face_outer_bound\") "
					+ "return start.instanceId,count(*)"	;
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){				
				if(resultSet.getInt(2)>=2) {
					scs.add(resultSet.getString(1));
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


