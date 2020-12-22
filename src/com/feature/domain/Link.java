package com.feature.domain;

public class Link {
	private String fromId;
	private String toId;
	private String name;
	private String type;
	private double weight;	//权重 0-1之间
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Link [fromId=" + fromId + ", toId=" + toId + ", name=" + name
				+ ", type=" + type + ", weight=" + weight + "]";
	}
	
	
}
