package com.vp.sc_assembly;

public class AssemblyTail {
	private String assemblyURL;
	private String partRelation;
	private String constraintsType;
	private String assemblyParameter;
	private String functionInformation;
	public String getAssemblyURL() {
		return assemblyURL;
	}
	public void setAssemblyURL(String assemblyURL) {
		this.assemblyURL = assemblyURL;
	}
	public String getPartRelation() {
		return partRelation;
	}
	public void setPartRelation(String partRelation) {
		this.partRelation = partRelation;
	}
	public String getConstraintsType() {
		return constraintsType;
	}
	public void setConstraintsType(String constraintsType) {
		this.constraintsType = constraintsType;
	}
	public String getAssemblyParameter() {
		return assemblyParameter;
	}
	public void setAssemblyParameter(String assemblyParameter) {
		this.assemblyParameter = assemblyParameter;
	}
	public String getFunctionInformation() {
		return functionInformation;
	}
	public void setFunctionInformation(String functionInformation) {
		this.functionInformation = functionInformation;
	}
	
	@Override
	public String toString() {
		return "AssemblyTail [assemblyURL=" + assemblyURL + ", partRelation=" + partRelation + ", constraintsType="
				+ constraintsType + ", assemblyParameter=" + assemblyParameter + ", functionInformation="
				+ functionInformation + "]";
	}
	
}
