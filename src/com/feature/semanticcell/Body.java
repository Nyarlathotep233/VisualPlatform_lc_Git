package com.feature.semanticcell;

public class Body {
	private Instance instance;

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	@Override
	public String toString() {
		return "Body [instance=" + instance + "]";
	}
	
}
