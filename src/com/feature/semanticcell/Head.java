package com.feature.semanticcell;

public class Head {
	private GeometryLink geometryLink;
	private SemanticLink semanticLink;
	public GeometryLink getGeometryLink() {
		return geometryLink;
	}
	public void setGeometryLink(GeometryLink geometryLink) {
		this.geometryLink = geometryLink;
	}
	public SemanticLink getSemanticLink() {
		return semanticLink;
	}
	public void setSemanticLink(SemanticLink semanticLink) {
		this.semanticLink = semanticLink;
	}
	@Override
	public String toString() {
		return "Head [geometryLink=" + geometryLink + ", semanticLink="
				+ semanticLink + "]";
	}
	
	
}
