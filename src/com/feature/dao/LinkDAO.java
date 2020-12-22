package com.feature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.feature.domain.Link;
import com.feature.semanticcell.SemanticCell;
import com.vp.util.Neo4jUtil;

public class LinkDAO {
	/**
	 * 由具体instanceId创建连线
	 * @param link
	 */
	public void createLink(Link link){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n1 {instanceId: {1}}) MATCH (n2 {instanceId: {2}}) create (n1)-[:Link {fromId:{3},toId:{4},name:{5},type:{6},weight:{7}}]->(n2)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, link.getFromId());
			preparedStatement.setObject(2, link.getToId());
			preparedStatement.setObject(3, link.getFromId());
			preparedStatement.setObject(4, link.getToId());
			preparedStatement.setObject(5, link.getName());
			preparedStatement.setObject(6, link.getType());
			preparedStatement.setObject(7, link.getWeight());
			preparedStatement.execute();
			System.out.println("complete："+link.getFromId() + "->" + link.getToId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询出数据库中所有的连线，放在一个List中
	 * @return
	 */
	public List<Link> getLinkList (){
		List<Link> links = new ArrayList<>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=()-[r:Link]->() RETURN r.fromId,r.toId,r.name,r.type,r.weight";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Link link = new Link();
				link.setFromId(resultSet.getString(1));
				link.setToId(resultSet.getString(2));
				link.setName(resultSet.getString(3));
				link.setType(resultSet.getString(4));
				link.setWeight(resultSet.getDouble(5));
				if(!links.contains(link)){
					links.add(link);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return links;
	}
	
	/**
	 * 获得所有的连线类型(link.name)集合
	 * @return
	 */
	public List<String> getLinkTypes() {
		List<String> names = new ArrayList<>();
		
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=()-[r:Link]->() RETURN r.name,count(r.name)";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String name = resultSet.getString(1);
				int count = resultSet.getInt(2);
				if(!names.contains(name)){
					names.add(name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		
		return names;
	}
	
	/**
	 * 根据link两端的语义元的instanceId找到该link并改变其权重值
	 * @param instanceId1
	 * @param instanceId1
	 * @param weight
	 */
	public void updateWeightByInstanceIds(String instanceId1, String instanceId2, double weight){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell)-[r:Link]->(n2:SemanticCell) where n1.instanceId={1} and n2.instanceId={2} set r.weight=(r.weight+{3})";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, instanceId1);
			preparedStatement.setObject(2, instanceId2);
			preparedStatement.setObject(3, weight);
			preparedStatement.execute();
			System.out.println("complete update："+ instanceId1 + "->" + instanceId2 + " weight = weight + " + weight);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(null, preparedStatement, connection);
		}
	}
	
}
