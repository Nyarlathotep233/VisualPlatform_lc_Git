package com.vp.sc_assembly;

public class SemanticAssembly {
	private String type;
	private AssemblyHead head;
	private AssemblyBody body;
	private AssemblyTail tail;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public AssemblyHead getHead() {
		return head;
	}
	public void setHead(AssemblyHead head) {
		this.head = head;
	}
	public AssemblyBody getBody() {
		return body;
	}
	public void setBody(AssemblyBody body) {
		this.body = body;
	}
	public AssemblyTail getTail() {
		return tail;
	}
	public void setTail(AssemblyTail tail) {
		this.tail = tail;
	}
	@Override
	public String toString() {
		return "SemanticAssembly [type=" + type + ", head=" + head + ", body=" + body + ", tail=" + tail + "]";
	}
	
	
}
