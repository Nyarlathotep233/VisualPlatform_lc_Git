package com.vp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.vp.base.Rule;
import com.vp.util.MySqlUtil;

public class RuleDAO {
	/**
	 * 添加规则库中的规则
	 * @param rule
	 */
	public void addRule(Rule rule){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try{
			connection = MySqlUtil.getConnection();
			String sql = " insert ignore into rule "
					+ " (path,result) "
					+ " values (?,?)";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, rule.getPath());
			preparedStatement.setString(2, rule.getResult());
			preparedStatement.execute();
			System.out.println("Complete insert" + rule);
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
			String sql = " select max(id) from rule ";
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
	 * 返回所有的规则数组
	 * @return
	 */
	public List<Rule> getAllRules(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Rule> rules = new ArrayList<Rule>();
		
		try{
			connection = MySqlUtil.getConnection();
			String sql = " select id,path,result from rule order by id";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Rule rule = new Rule();
				rule.setId(resultSet.getInt("id"));
				rule.setPath(resultSet.getString("path"));
				rule.setResult(resultSet.getString("result"));
				rules.add(rule);
			}
			return rules;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MySqlUtil.release(null, preparedStatement, connection);
		}
		
		return rules;
	}
}
