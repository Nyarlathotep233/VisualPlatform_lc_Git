package com.vp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Neo4jUtil {
	public static Connection getConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		//Class.forName("org.neo4j.jdbc.Driver");
	    Class.forName("org.neo4j.jdbc.Driver").newInstance();  
		Connection connection = DriverManager.getConnection("jdbc:neo4j:http://localhost:7474/","neo4j","123456");
		return connection;
	}
	
	public static void release(ResultSet resultSet,Statement statement,Connection connection){
		try {
			if(resultSet!=null){
				resultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(statement!=null){
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(connection!=null){
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
