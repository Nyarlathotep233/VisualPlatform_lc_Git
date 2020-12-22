package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.vp.base.Knowledge;
import com.vp.util.MySqlUtil;

public class KnowledgeDAO {
	
	/**
	 * 根据relation找到三元组
	 * @param relation
	 * @return
	 */
	public Knowledge getKnowledgeByRelation(String relation){
		Knowledge knowledge = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = MySqlUtil.getConnection();
			String sql = " select id,entity_type1,relation,entity_type2 from knowledge "
					+ " where relation=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, relation);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				 knowledge = new Knowledge();
				 knowledge.setId(resultSet.getInt("id"));
				 knowledge.setEntityType1(resultSet.getString("entity_type1"));
				 knowledge.setEntityType2(resultSet.getString("entity_type2"));
				 knowledge.setRelation(relation);
			}
			return knowledge;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MySqlUtil.release(null, preparedStatement, connection);
		}
		return knowledge;
	}

	/**
	 * 添加知识库三元组
	 * @param knowledge
	 */
	public void addKnowledge(Knowledge knowledge){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try{
			connection = MySqlUtil.getConnection();
			//这里使用ignore忽略主键冲突，从而插入不同的记录
			String sql = " insert ignore into knowledge "
					+ " (entity_type1,relation,entity_type2) "
					+ " values (?,?,?)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, knowledge.getEntityType1());
			preparedStatement.setString(2, knowledge.getRelation());
			preparedStatement.setString(3, knowledge.getEntityType2());
			preparedStatement.execute();
			System.out.println("Complete insert" + knowledge);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MySqlUtil.release(null, preparedStatement, connection);
		}
	}
	
	/**
	 * 返回最新插入元素的自增id
	 * @return
	 */
	public int getLastedId(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int lastedId = 0;
		
		try{
			connection = MySqlUtil.getConnection();
			String sql = " select max(id) from knowledge ";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				lastedId = resultSet.getInt(1);
			}
			return lastedId;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MySqlUtil.release(null, preparedStatement, connection);
		}
		
		return lastedId;
	}
	/**
	 * 获取所有的三元组的关系列表，用来查看哪些三元组已经创建，哪些没有
	 * @return
	 */
	public List<String> getRelations(){
		List<String> relations = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			connection = MySqlUtil.getConnection();
			String sql = " select relation from knowledge ";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				if(!relations.contains(resultSet.getString(1)))
				 relations.add(resultSet.getString(1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MySqlUtil.release(null, preparedStatement, connection);
		}
		return relations;
	}
	
}
