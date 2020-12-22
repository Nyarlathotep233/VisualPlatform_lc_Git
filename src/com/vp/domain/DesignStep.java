package com.vp.domain;

import java.util.List;

public class DesignStep {
	private List<List<String>> relationFaces;
	private List<String> operation;

	public List<List<String>> getRelationFaces() {
		return relationFaces;
	}
	public void setRelationFaces(List<List<String>> relationFaces) {
		this.relationFaces = relationFaces;
	}
	public List<String> getOperation() {
		return operation;
	}
	public void setOperation(List<String> operation) {
		this.operation = operation;
	}
	@Override
	public String toString() {
		return "DesignStep [relationFaces=" + relationFaces + ", operation=" + operation + "]";
	}
	

}
