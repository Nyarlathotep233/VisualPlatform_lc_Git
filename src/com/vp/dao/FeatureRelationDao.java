package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.vp.util.Neo4jUtil;

public class FeatureRelationDao {
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
			String sql="Match (start:SemanticCell)-[:Link*]->(end:SemanticCell) "
					+ "where (start.instanceId='"
					+ blockId
					+ "' and end.type='feature') "
					+ "return end.tail,end.body";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			//int count = 0;
			while(resultSet.next()){
				//这里是取“（）”中的值，如果头部出现多组括号的情况，这里会出错。
				//String str = resultSet.getString(1).substring(resultSet.getString(1).indexOf("(")+1,resultSet.getString(1).indexOf(")"));
				//if(count == 0){
				blockFeatures.add(resultSet.getString(2));
				//}
				//count++;
				blockFeatures.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return blockFeatures;
	}
	
	/**
	 * 以List的形式返回所有的块
	 * @param blockId
	 * @return
	 */
	public List<String> getBlock() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> allblock = new ArrayList<String>();
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) "
					+ "Where n.type='block' "
							+ "RETURN n.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				//这里是取“（）”中的值，如果头部出现多组括号的情况，这里会出错。
				//String str = resultSet.getString(1).substring(resultSet.getString(1).indexOf("(")+1,resultSet.getString(1).indexOf(")"));
				allblock.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return allblock;
	}
}
