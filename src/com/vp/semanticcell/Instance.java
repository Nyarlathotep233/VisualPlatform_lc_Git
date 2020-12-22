package com.vp.semanticcell;

import com.vp.domain.Point;


public class Instance {
	private String instanceId;
	private Point coordinates;
	private String radius;
	private String elementface;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}
	
	public String getElementface() {
		return elementface;
	}

	public void setElementface(String elementface) {
		this.elementface = elementface;
	}

	@Override
	public String toString() {
		return "Instance [instanceId=" + instanceId + ", coordinates="
				+ coordinates + ", radius=" + radius + ", elementface=\" + elementface + \"]";
	}
}
