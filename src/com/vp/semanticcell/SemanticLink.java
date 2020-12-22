package com.vp.semanticcell;

import java.util.Map;

public class SemanticLink {
	private Map<String, String> semanticMap;

	public Map<String, String> getSemanticMap() {
		return semanticMap;
	}

	public void setSemanticMap(Map<String, String> semanticMap) {
		this.semanticMap = semanticMap;
	}

	@Override
	public String toString() {
		return "SemanticLink [semanticMap=" + semanticMap + "]";
	}
	
}
