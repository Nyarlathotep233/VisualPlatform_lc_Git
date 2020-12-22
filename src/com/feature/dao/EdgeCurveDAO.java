package com.feature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.feature.semanticcell.Body;
import com.feature.semanticcell.Head;
import com.feature.semanticcell.SemanticCell;
import com.feature.semanticcell.Tail;
import com.feature.domain.Point;
import com.vp.util.Neo4jUtil;

public class EdgeCurveDAO {
	private static Gson gson = new Gson();
	
	/**获取advancedFace下的edgelist
	 * @return
	 */
	public List<SemanticCell> getEdgeCurveList(String advancedFaceName){
		List<SemanticCell> edgeCurvelist = new ArrayList<SemanticCell>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			
			String sql ="Match (start:SemanticCell{instanceId:{1}})-[:Link*]->(end:SemanticCell{type:\"edge_curve\"}) return end.type,end.head,end.body,end.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,advancedFaceName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				SemanticCell sc = new SemanticCell();
				sc.setType(resultSet.getString(1));
				String headStr = resultSet.getString(2);
				Head head = gson.fromJson(headStr, Head.class);
				sc.setHead(head);
				String bodyStr = resultSet.getString(3);
				Body body = gson.fromJson(bodyStr, Body.class);
				sc.setBody(body);
				String tailStr = resultSet.getString(4);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				sc.setTail(tail);
				
				if(!edgeCurvelist.contains(sc)){
					edgeCurvelist.add(sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return edgeCurvelist;
	}
	/**获取edgeCurve的flag标志
	 * 根据edgeCurve的id
	 * @param edgeCurveName
	 * @return
	 */
	public String getEdgeCurveFlag(String edgeCurveName){
		String edgeCurveFlag = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="Match (n: SemanticCell) where n.instanceId={1} return n.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,edgeCurveName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String EtailStr = resultSet.getString(1);
				Tail etail = gson.fromJson(EtailStr, Tail.class);
				edgeCurveFlag = etail.getAttribute().get("same_sense").substring(1, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return edgeCurveFlag;
	}
	/**
	 * 得到edgecurve的几何定义曲线的id
	 * @param edgeCurveName
	 * @return
	 */
	public String getEdgeCurveType(String edgeCurveName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String str = null;
		String[] str1 = null;
		String jihedingyiquxian=null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) Where n.instanceId=\""
					+ edgeCurveName
					+ "\" RETURN n.head";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				str = resultSet.getString(1);
//				System.out.println("未分割前："+str);
				str1=str.split("\"");
//				System.out.println("分割后：");
//				for(int i=0;i<str1.length;i++)
//					System.out.println(str1[i]);
				jihedingyiquxian=str1[7];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		String type=getType(jihedingyiquxian);
		return type;
	}
	
	/**
	 * 依据曲线id查询语义元节点的type
	 * @param jihedingyiquxian
	 * @return
	 */
	public String getType(String jihedingyiquxian) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String type = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql="MATCH (n:SemanticCell) Where n.instanceId=\""
					+ jihedingyiquxian
					+ "\" RETURN n.type";
			preparedStatement=connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				type = resultSet.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return type;
	}
	/**获取orientedEdge的flag标志
	 * @param advancedFaceName
	 * @param edgeCurveName
	 * @return
	 */
	public String getOrientedEdgeFlag(String advancedFaceName ,String edgeCurveName){
		String orientedEdgeFlag = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->"
					+ "(n3:SemanticCell)-[r3:Link]->(n4:SemanticCell)-[r4:Link]->(n5:SemanticCell) "
					+ "where  n1.instanceId={1} and n4.type=\"oriented_edge\""
					+ " and  n5.instanceId={2} "
					+ " RETURN n4.tail";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,advancedFaceName);
			preparedStatement.setObject(2,edgeCurveName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String tailStr = resultSet.getString(1);
				Tail tail = gson.fromJson(tailStr, Tail.class);
				orientedEdgeFlag = tail.getAttribute().get("orientation").substring(1, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return orientedEdgeFlag;
	}
	
	/**当相交棱边是两平面相交而成的直线时
	 * 获得直线边的方向向量
	 * @param edgeCurveName
	 * @return
	 */
 	public Point getedgeCurveDirection(String edgeCurveName){
		Point edgeCurvedirection = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell)"
					+ "-[r3:Link]->(n4:SemanticCell)"
					+ "where n1.instanceId={1} "
                    + "and n2.type=\"line\" and n3.type=\"vector\" and n4.type=\"direction\""
                    + "RETURN n4.body";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,edgeCurveName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String bodyStr = resultSet.getString(1);
				Body body = gson.fromJson(bodyStr, Body.class);
				edgeCurvedirection = body.getInstance().getCoordinates();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		//System.out.println(edgeCurvedirection);
		return edgeCurvedirection;
	}
 	
 	/**当相交棱边是平面和柱面相交的曲线时
	 * 获得axis2_placement_3d的第一个direction
	 * @param edgeCurveName
	 * @return
	 */
 	public Point getCircleFirstDirection(String edgeCurveName){
		Point edgeCurvedirection = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell)"
					+ "-[r3:Link]->(n4:SemanticCell)"
					+ "where n1.instanceId={1} "
                    + "and n2.type=\"circle\" and n3.type=\"axis2_placement_3d\" and n4.type=\"direction\""
                    + "RETURN n4.body";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,edgeCurveName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String bodyStr = resultSet.getString(1);
				Body body = gson.fromJson(bodyStr, Body.class);
				edgeCurvedirection = body.getInstance().getCoordinates();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		//System.out.println(edgeCurvedirection);
		return edgeCurvedirection;
	}
 	
 	/**
 	 * 根据平面和柱面相交的EdgeCurve求出vertex_point
 	 * @param edgeCurveName
 	 * @return
 	 */
 	public Point getVertexPointFromEdgeCurve(String edgeCurveName){
		Point vertexPoint = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell)"
					+ "where n1.instanceId={1} "
                    + "and n2.type=\"vertex_point\" and n3.type=\"cartesian_point\""
                    + "RETURN n3.body";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,edgeCurveName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String bodyStr = resultSet.getString(1);
				Body body = gson.fromJson(bodyStr, Body.class);
				vertexPoint = body.getInstance().getCoordinates();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return vertexPoint;
	}
 	
 	/**
 	 * 根据平面和柱面相交的EdgeCurve求出cartesian_point
 	 * @param edgeCurveName
 	 * @return
 	 */
 	public Point getCartesianPointFromEdgeCurve(String edgeCurveName){
		Point cartesianPoint = null;
		Connection  connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = Neo4jUtil.getConnection();
			String sql ="MATCH p=(n1:SemanticCell)-[r1:Link]->(n2:SemanticCell)-[r2:Link]->(n3:SemanticCell)"
					+ "-[r3:Link]->(n4:SemanticCell)"
					+ "where  n1.instanceId={1} "
                    + "and n2.type=\"circle\" and n3.type=\"axis2_placement_3d\" and n4.type=\"cartesian_point\""
                    + "RETURN n4.body";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setObject(1,edgeCurveName);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String bodyStr = resultSet.getString(1);
				Body body = gson.fromJson(bodyStr, Body.class);
				cartesianPoint = body.getInstance().getCoordinates();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Neo4jUtil.release(resultSet, preparedStatement, connection);
		}
		return cartesianPoint;
	}
}
