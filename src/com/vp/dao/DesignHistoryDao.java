package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vp.util.Neo4jUtil;



public class DesignHistoryDao {
	/**
	 * 查找含有两个或以上环的面，以List形式返回面的ID
	 * @return
	 */
	public List<String> faceHasBounds(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> faces = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell{type:\"advanced_face\"})-[:Link*]->(end:SemanticCell) "
					+ "where (end.type=\"face_bound\" or end.type=\"face_outer_bound\") "
					+ "return start.instanceId,count(*)"	;
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				if(resultSet.getInt(2)>=2) {
					faces.add(resultSet.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return faces;
	}
	/**
	 * 根据面的id查找面的环，以List形式返回环的集合
	 * @param faceId
	 * @return
	 */
	public List<String> boundsList(String faceId){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> bounds = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell)-[:Link*]->(end:SemanticCell) "
					+ "where (start.instanceId=\""
					+ faceId
					+ "\" and end.type=\"face_bound\" or end.type=\"face_outer_bound\") "
					+ "return end.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				bounds.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return bounds;
	}
	/**
	 * 根据环的id查找edge_curve的集合，以List的方式返回
	 * @param boudId
	 * @return
	 */
	public List<String> boundsHasCurve(String boudId){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> curves = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell{instanceId:\""
					+ boudId
					+ "\"})"
					+ "-[:Link*]->(end:SemanticCell) "
					+ "where (end.type=\"edge_curve\") "
					+ "return end.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				curves.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return curves;
	}
	/**
	 * 根据edge_curve的id查找相交的面，以List的方式返回
	 * @param curveId
	 * @return
	 */
	public List<String> otherFace(String curveId){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> faces = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell{type:\"advanced_face\"})"
					+ "-[:Link*]->(end:SemanticCell) "
					+ "where (end.instanceId=\""
					+ curveId
					+ "\") "
					+ "return start.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){				
				faces.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return faces;
	}
	/**
	 * 输入面的id,返回该面是所属的图块
	 * @param faceId
	 * @return
	 */
	public List<String> inWhichBlock(String faceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> blocks = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="Match (start:SemanticCell{type:\"block\"})"
					+ "-[:Link*]->(end:SemanticCell{type:\"advanced_face\"}) "
					+ "where (end.instanceId=\""
					+ faceId
					+ "\") "
					+ "return start.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				blocks.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return blocks;
	}
	
	/**
	 * 获取图块的最大实体表面，以Map形式返回块和组成最大实体的面。
	 * @return
	 */
	public Map<String,String> getBlockFaces() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Map<String,String> blockMap = new HashMap<String,String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) Where n.type=\"block\" RETURN n.instanceId, n.body"	;
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String blockName = resultSet.getString(1);
				String st2 = resultSet.getString(2).substring(resultSet.getString(2).indexOf("(")+1,resultSet.getString(2).indexOf(")"));
				blockMap.put(blockName, st2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return blockMap;
	}
	
	/**
	 * 传入块的Id,以List的形式返回该块具有的特征
	 * @param blockId
	 * @return
	 */
	public List<String> getBlockFeatures(String blockId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> blockFeatures = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) Where n.instanceId=\""
					+ blockId
					+ "\" RETURN n.head";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				//这里是取“（）”中的值，如果头部出现多组括号的情况，这里会出错。
				String str = resultSet.getString(1).substring(resultSet.getString(1).indexOf("(")+1,resultSet.getString(1).indexOf(")"));
				blockFeatures = Arrays.asList(str.split(","));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return blockFeatures;
	}
	
	/**
	 * 传入特征的ID，返回该特征含有关联面的head。
	 * @param featureId
	 * @return
	 */
	public String getFeatureFace(String featureId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String str = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) Where n.instanceId=\""
					+ featureId
					+ "\" RETURN n.head";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				str = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return str;
	}
	/**
	 * 返回advanced face的起始id
	 * @param faceId
	 * @return
	 */
	public List<String> AllFaceId(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> faces = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) Where n.type=\"advanced_face\" RETURN n.instanceId"	;

			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				faces.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return faces;
	}
}
