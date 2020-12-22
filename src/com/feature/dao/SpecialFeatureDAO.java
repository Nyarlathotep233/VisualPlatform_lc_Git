package com.feature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.feature.semanticcell.Body;
import com.feature.semanticcell.Head;
import com.feature.semanticcell.SemanticCell;
import com.feature.semanticcell.Tail;
import com.vp.util.Neo4jUtil;
import com.google.gson.Gson;

/**
 * 用来处理特殊特征的一些数据库操作方法
 * @author admin
 *
 */
public class SpecialFeatureDAO {
	
	private Gson gson = new Gson();
	
	/**
	 * 找出面上的face_bound或者face_outer_bound的个数超过三个的面的List
	 * @return
	 */
	public List<String> getCoplanarAdvancedFaceIdList(){
		List<String> advancedFaceIdList = new ArrayList<String>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell{type:\"advanced_face\"})-[r:Link]->"
					+ "(n2:SemanticCell) where (n2.type=\"face_bound\" or n2.type=\"face_outer_bound\") "
					+ "RETURN n1.instanceId,count(n2)>=3";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String advancedFaceId = resultSet.getString(1);
				String flag = resultSet.getString(2);
				if(flag.endsWith("true")){
					advancedFaceIdList.add(advancedFaceId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFaceIdList;
	}
	
	/**
	 * 根据advancedFaceId获取face_boundId或face_outer_boundId的list
	 * @param advancedFaceId
	 * @return
	 */
	public List<String> getBoundIdListByAdvancedFaceId(String advancedFaceId){
		List<String> boundIdList = new ArrayList<String>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell{instanceId:{1}})-[r:Link]->"
					+ "(n2:SemanticCell) where (n2.type=\"face_bound\" or n2.type=\"face_outer_bound\") "
					+ "RETURN n2.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, advancedFaceId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String boundId = resultSet.getString(1);
				if(!boundIdList.contains(boundId)){
					boundIdList.add(boundId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return boundIdList;
	}
	/**
	 * 根据boundId找出edgeCurveIdList
	 * @param advancedFaceId
	 * @return
	 */
	public List<String> getEdgeCurveIdListByBoundId(String boundId){
		List<String> edgeCurveIdList = new ArrayList<String>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell{instanceId:{1}})-[r:Link*]->"
					+ "(n2:SemanticCell) where n2.type=\"edge_curve\" "
					+ "RETURN n2.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, boundId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String edgeCurveId = resultSet.getString(1);
				if(!edgeCurveIdList.contains(edgeCurveId)){
					edgeCurveIdList.add(edgeCurveId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return edgeCurveIdList;
	}
	/**
	 * 根据edgeCurveId获取是哪两个面相交于他的
	 * @param edgeCurveId
	 * @return
	 */
	public List<String> getAdvancedFaceIdListByEdgeCurveId(String edgeCurveId){
		List<String> advancedFaceIdList = new ArrayList<String>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell{type:\"advanced_face\"})-[r:Link*]->"
					+ "(n2:SemanticCell) where n2.instanceId={1} "
					+ "RETURN n1.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, edgeCurveId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String advanceFaceId = resultSet.getString(1);
				if(!advancedFaceIdList.contains(advanceFaceId)){
					advancedFaceIdList.add(advanceFaceId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return advancedFaceIdList;
	}
}
