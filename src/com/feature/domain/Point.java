package com.feature.domain;

public class Point {
	private double coordinates_x;
	private double coordinates_y;
	private double coordinates_z;
	public double getCoordinates_x() {
		return coordinates_x;
	}
	public void setCoordinates_x(double coordinates_x) {
		this.coordinates_x = coordinates_x;
	}
	public double getCoordinates_y() {
		return coordinates_y;
	}
	public void setCoordinates_y(double coordinates_y) {
		this.coordinates_y = coordinates_y;
	}
	public double getCoordinates_z() {
		return coordinates_z;
	}
	public void setCoordinates_z(double coordinates_z) {
		this.coordinates_z = coordinates_z;
	}
	@Override
	public String toString() {
		return "Point [coordinates_x=" + coordinates_x + ", coordinates_y="
				+ coordinates_y + ", coordinates_z=" + coordinates_z + "]";
	}
	
}
