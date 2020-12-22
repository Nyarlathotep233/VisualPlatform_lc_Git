package com.vp.semanticcell;

import java.util.Map;

public class GeometryLink {
	private Map<String, String> geometryMap;

	public Map<String, String> getGeometryMap() {
		return geometryMap;
	}

	public void setGeometryMap(Map<String, String> geometryMap) {
		this.geometryMap = geometryMap;
	}

	@Override
	public String toString() {
		return "GeometryLink [geometryMap=" + geometryMap + "]";
	}
	
}
