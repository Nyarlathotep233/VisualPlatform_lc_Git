package com.reinforcement.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vp.util.Neo4jUtil;

public class QLearningDao {
	static Connection  connection = null;
	static {
		try {
			connection = Neo4jUtil.getConnection();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化每个结点的动作集
	 * @param instanceId
	 * @param action
	 */
	public void initActions(String instanceId,String action) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instanceId
					+ "\" SET n.actions = '"
					+ action
					+ "' RETURN n";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	/**
	 * 返回所有结点的instanceID
	 * @return
	 */
	public List<String> getInstanceIDs() {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> idList = new ArrayList<String>();
		try {
			String sql="MATCH (n:SemanticCell)   RETURN n.instanceId";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				idList.add(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return idList;
	}

	/**
	 * 根据结点的instancedId，返回结点的动作集
	 * @param instancedId
	 * @return
	 */
	public String[] getActions(String instancedId){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		String actionValue = "";
		String[] actionValues = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.actions";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				actionValue = resultSet.getString(1);
				actionValues = actionValue.split(",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return actionValues;
	}
	
	/**
	 * 根据结点的instancedId，返回结点的动作集的字符串
	 * @param instancedId
	 * @return
	 */
	public String getActionString(String instancedId){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		String actionValue = "";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.actions";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				actionValue = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return actionValue;
	}
	
	/**
	 * 根据结点的instancedId，返回结点反向动作集的字符串 action_fan
	 * @param instancedId
	 * @return
	 */
	public String getActionFanString(String instancedId){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		String actionFanValue = "";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.action_fan";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				actionFanValue = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return actionFanValue;
	}
	
	/**
	 * 根据结点的instancedId，返回结点所有动作集的字符串 action_all
	 * @param instancedId
	 * @return
	 */
	public String getActionAllString(String instancedId){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		String actionAllValue = "";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.action_all";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				//System.out.println("wowooooooooooooooowowowowowowow"+resultSet+"ueueueueueueueueueueueu"+resultSet.getString(1));
				actionAllValue = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return actionAllValue;
	}
	
	
	/**
	 * 初始化某个结点的Q值,也可以用于某个结点的Q值更新
	 * @param instancedId
	 * @param qValue
	 */
	public void initQValue(String instancedId,Map<String,String> qValue) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\" SET n.q_values = '"
					+ qValue
					+ "' RETURN n";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	/**
	 * 初始化R值为0
	 * @param instancedId
	 */
	public void initRValue(String instancedId) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\" SET n.r_values = '"
					+ 0
					+ "' RETURN n";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	/**
	 * 修改instancedId结点的R值为value。
	 * @param instancedId
	 * @param value
	 */
	public void setFinalR(String instancedId,int value) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\" SET n.r_values = '"
					+ value
					+ "' RETURN n";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	/**
	 * 根据instancedId，返回q_value标签的值
	 * @param instancedId
	 * @return
	 */
	public String getQV(String instancedId) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String qValue = null;		
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.q_values";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				qValue = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return qValue;
	}
	
	/**
	 * 根据instancedId得到结点的R值
	 * @param instancedId
	 * @return
	 */
	public String getRValue(String instancedId) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String rValue = null;		
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.r_values";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				rValue = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return rValue;
	}
	
	/**
	 * 根据instancedId得到结点的类型
	 * @param instancedId
	 * @return
	 */
	public String getType(String instancedId) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String type = null;		
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.type";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				type = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return type;
	}
	
	/**
	 * 删除Neo4j所有连线及相关语义元节点
	 */
	public void deleteAllLinks(){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		try{
			String sql = "MATCH p=()-[r:Link]->() delete p";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.execute();
			System.out.println("All Links have deleted");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
	
	/**
	 * 删除Neo4j所有语义元节点
	 */
	public void deleteAllSCs(){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		try{
			String sql = "MATCH (n:SemanticCell) delete n";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.execute();
			System.out.println("All SemanticCells have deleted");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	} 
	
	/**
	 * 创建Neo4j的语义元节点
	 * @param valueList
	 */
	public void createNode(List<String> valueList){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		String action_fan = "";
		String action_all = "";
		try{
			String sql = "CREATE (n:SemanticCell {type:{1},instanceId:{2},pmi_linear:{3},actions:{4},name:{5},action_fan:{6},action_all:{7}})";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, valueList.get(0));
			preparedStatement.setObject(2, valueList.get(1));
			preparedStatement.setObject(3, valueList.get(2));
			preparedStatement.setObject(4, valueList.get(3));
			preparedStatement.setObject(5, valueList.get(4));
			preparedStatement.setObject(6, action_fan);
			preparedStatement.setObject(7, action_all);
			preparedStatement.execute();
			System.out.println("complete：" + valueList.get(4));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
	
	/**
	 * 创建结点之间的连接
	 * @param fromId
	 * @param toId
	 */
	public void createLink(String fromId, String toId){
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		String name = fromId+"->"+toId;
		try {
			String sql="MATCH (n1 {instanceId: {1}}) MATCH (n2 {instanceId: {2}}) create (n1)-[:Link {fromId:{3},toId:{4},name:{5}}]->(n2)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1, fromId);
			preparedStatement.setObject(2, toId);
			preparedStatement.setObject(3, fromId);
			preparedStatement.setObject(4, toId);
			preparedStatement.setObject(5, name);
			preparedStatement.execute();
			System.out.println("complete："+fromId + "->" + toId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据instancedId得到结点的反向动作集
	 * @param instancedId
	 * @return
	 */
	public String getActionFan(String instancedId) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String action_fan = null;		
		try {
			String sql="MATCH (n:SemanticCell) where n.instanceId=\""
					+ instancedId
					+ "\"  RETURN n.action_fan";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				action_fan = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return action_fan;
	}
	
	/**
	 * 根据instancedId，更新反向动作集
	 * @param instancedId
	 * @param actionFan
	 */
	public void updateActionFan(String instancedId,String actionFan) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		try {
			String sql="MATCH (n:SemanticCell)where n.instanceId=\""
					+ instancedId
					+ "\" Set n.action_fan=\""
					+ actionFan
					+ "\"";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	/**
	 * 根据instancedId，更新action_all
	 * @param instancedId
	 * @param allAction
	 */
	public void updateAllAction(String instancedId,String allAction) {
		if(connection == null) {
			try {
				connection = Neo4jUtil.getConnection();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement preparedStatement = null;
		try {
			String sql="MATCH (n:SemanticCell)where n.instanceId=\""
					+ instancedId
					+ "\" Set n.action_all=\""
					+ allAction
					+ "\"";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}	
/**
 * 根据instancedId，更新actionNum
 * @param instancedId
 * @param actionNum
 */
public void updateActionNum(String instancedId,int actionNum) {
	if(connection == null) {
		try {
			connection = Neo4jUtil.getConnection();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}
	}
	PreparedStatement preparedStatement = null;
	try {
		String sql="MATCH (n:SemanticCell)where n.instanceId=\""
				+ instancedId
				+ "\" Set n.actionNum=\""
				+ actionNum
				+ "\"";
		preparedStatement=connection.prepareStatement(sql);
		preparedStatement.execute();
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
	}
}
}
