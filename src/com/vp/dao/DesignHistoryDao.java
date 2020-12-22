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
	 * ���Һ������������ϻ����棬��List��ʽ�������ID
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
	 * �������id������Ļ�����List��ʽ���ػ��ļ���
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
	 * ���ݻ���id����edge_curve�ļ��ϣ���List�ķ�ʽ����
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
	 * ����edge_curve��id�����ཻ���棬��List�ķ�ʽ����
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
	 * �������id,���ظ�����������ͼ��
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
	 * ��ȡͼ������ʵ����棬��Map��ʽ���ؿ��������ʵ����档
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
	 * ������Id,��List����ʽ���ظÿ���е�����
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
				//������ȡ���������е�ֵ�����ͷ�����ֶ������ŵ��������������
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
	 * ����������ID�����ظ��������й������head��
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
	 * ����advanced face����ʼid
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
