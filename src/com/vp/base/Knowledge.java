package com.vp.base;

/**
 * 知识图谱的基本单位：三元组
 * @author admin
 *
 */
public class Knowledge {
	private int id;
	private String entityType1;
	private String relation;
	private String entityType2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEntityType1() {
		return entityType1;
	}
	public void setEntityType1(String entityType1) {
		this.entityType1 = entityType1;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getEntityType2() {
		return entityType2;
	}
	public void setEntityType2(String entityType2) {
		this.entityType2 = entityType2;
	}
	@Override
	public String toString() {
		return "Knowledge ["+ entityType1+" --> "+ relation + " --> "+ entityType2 +"]";
	}
	
	
}
