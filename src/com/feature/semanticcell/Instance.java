package com.feature.semanticcell;

import com.feature.domain.Point;

public class Instance {
	private String instanceId;
	private Point coordinates;
	private String radius;

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

	@Override
	public String toString() {
		return "Instance [instanceId=" + instanceId + ", coordinates="
				+ coordinates + ", radius=" + radius + "]";
	}
}
