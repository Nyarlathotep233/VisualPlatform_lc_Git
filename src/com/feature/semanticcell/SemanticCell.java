package com.feature.semanticcell;

public class SemanticCell {
	private String type;
	private Head head;
	private Body body;
	private Tail tail;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Head getHead() {
		return head;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public Tail getTail() {
		return tail;
	}
	public void setTail(Tail tail) {
		this.tail = tail;
	}
	@Override
	public String toString() {
		return "SemanticCell [type=" + type + ", head=" + head + ", body="
				+ body + ", tail=" + tail + "]";
	}
	
}
