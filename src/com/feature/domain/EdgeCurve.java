package com.feature.domain;

public class EdgeCurve {
	private String edge_curve_name;
	private String edge_curve_flag;
	private String oriented_edge_flag;
	private Point edge_curve_direction;
	//edgeCurve为曲线时任意vertex_point实例
	private Point vertexPoint;
	//edgeCurve为曲线时任意axis2_placement_3d实例中的cartesian_point
	private Point cartesianPoint;
	//edgeCurve为曲线时任意axis2_placement_3d实例中的第一个direction
	private Point circleFirstDirection;
	public String getEdge_curve_name() {
		return edge_curve_name;
	}
	public void setEdge_curve_name(String edge_curve_name) {
		this.edge_curve_name = edge_curve_name;
	}
	public String getEdge_curve_flag() {
		return edge_curve_flag;
	}
	public void setEdge_curve_flag(String edge_curve_flag) {
		this.edge_curve_flag = edge_curve_flag;
	}
	public String getOriented_edge_flag() {
		return oriented_edge_flag;
	}
	public void setOriented_edge_flag(String oriented_edge_flag) {
		this.oriented_edge_flag = oriented_edge_flag;
	}
	public Point getEdge_curve_direction() {
		return edge_curve_direction;
	}
	public void setEdge_curve_direction(Point edge_curve_direction) {
		this.edge_curve_direction = edge_curve_direction;
	}
	public Point getVertexPoint() {
		return vertexPoint;
	}
	public void setVertexPoint(Point vertexPoint) {
		this.vertexPoint = vertexPoint;
	}
	public Point getCartesianPoint() {
		return cartesianPoint;
	}
	public void setCartesianPoint(Point cartesianPoint) {
		this.cartesianPoint = cartesianPoint;
	}
	public Point getCircleFirstDirection() {
		return circleFirstDirection;
	}
	public void setCircleFirstDirection(Point circleFirstDirection) {
		this.circleFirstDirection = circleFirstDirection;
	}
	@Override
	public String toString() {
		return "EdgeCurve [edge_curve_name=" + edge_curve_name
				+ ", edge_curve_flag=" + edge_curve_flag
				+ ", oriented_edge_flag=" + oriented_edge_flag
				+ ", edge_curve_direction=" + edge_curve_direction
				+ ", vertexPoint=" + vertexPoint + ", cartesianPoint="
				+ cartesianPoint + ", circleFirstDirection="
				+ circleFirstDirection + "]";
	}
	
	
}
