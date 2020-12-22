package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vp.util.Neo4jUtil;

/**
 * 关于路径的处理
 * @author admin
 *
 */
public class PathDAO {
	/**
	 * 根据两个语义元类别，确定之间路径的条数
	 * @param type1
	 * @param type2
	 * @return
	 */
	public int getLinkCountByTypes(String type1, String type2){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 0;
		try {
			connection = Neo4jUtil.getConnection();
			
			String sql ="Match (start:SemanticCell{type:{1}})-[:Link*]->(end:SemanticCell{type:{2}}) return count(*)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,type1);
			preparedStatement.setObject(2,type2);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				count = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return count;
	}
}
