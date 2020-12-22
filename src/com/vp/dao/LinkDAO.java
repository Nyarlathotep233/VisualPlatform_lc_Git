package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.vp.semanticcell.Body;
import com.vp.semanticcell.Head;
import com.vp.semanticcell.SemanticCell;
import com.vp.semanticcell.Tail;
import com.vp.base.Knowledge;
import com.vp.domain.Link;
import com.vp.util.Neo4jUtil;

public class LinkDAO {
	/**
	 * �ɾ���instanceId��������
	 * @param link
	 */
	public void createLink(Link link){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n1 {instanceId: {1}}) MATCH (n2 {instanceId: {2}}) create (n1)-[:Link {fromId:{3},toId:{4},name:{5},type:{6},weight:{7},fromToId:{8}}]->(n2)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, link.getFromId());
			preparedStatement.setObject(2, link.getToId());
			preparedStatement.setObject(3, link.getFromId());
			preparedStatement.setObject(4, link.getToId());
			preparedStatement.setObject(5, link.getName());
			preparedStatement.setObject(6, link.getType());
			preparedStatement.setObject(7, link.getWeight());
			preparedStatement.setObject(8, link.getFromId() + "->" + link.getToId());
			preparedStatement.execute();
			System.out.println("complete��"+link.getFromId() + "->" + link.getToId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��Knowledge��Ȩ�ش�������
	 * @param link
	 */
	public void createLinkByKnowledge(Knowledge knowledge, double weight){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n1 {type: {1}}) MATCH (n2 {type: {2}}) create (n1)-[:Link {fromId:n1.instanceId,toId:n2.instanceId,name:{3},type:{4},weight:{5}}]->(n2)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, knowledge.getEntityType1());
			preparedStatement.setObject(2, knowledge.getEntityType2());
			preparedStatement.setObject(3, knowledge.getRelation());
			preparedStatement.setObject(4, "semanticLink");
			preparedStatement.setObject(5, weight);
			preparedStatement.execute();
			System.out.println("complete create��"+ knowledge.getEntityType1() + "->" + knowledge.getEntityType2());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����instanceId�ҵ���Χ������
	 * @param instancedId
	 * @return
	 */
	public List<Link> getLinksByFromId(String instancedId){
		List<Link> links = new ArrayList<>();
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH p=(n1:SemanticCell)-[r:Link]->(n2:SemanticCell) where n1.instanceId={1} RETURN r.fromId,r.toId,r.name,r.type,r.weight";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, instancedId);
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
	 * ��ѯ�����ݿ������е����ߣ�����һ��List��
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
	 * ������е���������(link.name)����
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
	 * ����link���˵�����Ԫ��instanceId�ҵ���link���ı���Ȩ��ֵ
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
			System.out.println("complete update��"+ instanceId1 + "->" + instanceId2 + " weight = weight + " + weight);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(null, preparedStatement, connection);
		}
	}
	/**
	 * ɾ��Neo4j�������߼��������Ԫ�ڵ�
	 * @param sc
	 */
	public void deleteAllLinks(){
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = Neo4jUtil.getConnection();
			String sql = "MATCH p=()-[r:Link]->() delete p";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.execute();
			System.out.println("All Links have deleted");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Neo4jUtil.release(null, preparedStatement, connection);
		}
	}

	/**
	 * ��������Ԫ�ڵ����飬��ȡ����֮�����е�����  
	 * @param scs
	 * @return
	 */
	public List<Link> getLinksByScs(List<SemanticCell> scs) {
		
		List<String> instancedIds = new ArrayList<String>();
		
		for(int i = 0; i < scs.size(); i++){
			if(!instancedIds.contains(scs.get(i).getBody().getInstance().getInstanceId())){
				instancedIds.add(scs.get(i).getBody().getInstance().getInstanceId());
			}
		}
		
		List<Link> links = new ArrayList<Link>();
		Connection  connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Neo4jUtil.getConnection();
            String sql="match p=(n1:SemanticCell)-[r:Link]->(n2:SemanticCell) "
            		+ "where n1.instanceId in {1} and n2.instanceId in {2} "
            		+ "return r.fromId,r.toId,r.name,r.type,r.weight";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setObject(1, instancedIds);
            preparedStatement.setObject(2, instancedIds);
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
}
