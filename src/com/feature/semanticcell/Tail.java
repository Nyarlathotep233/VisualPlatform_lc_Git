package com.feature.semanticcell;

import java.util.Map;

public class Tail {
	private Map<String, String> attribute;

	public Map<String, String> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return "Tail [attribute=" + attribute + "]";
	}
	
}
