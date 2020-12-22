package com.vp.base;

/**
 * ¹æÔò¿â
 * 
 * @author admin
 *
 */
public class Rule {
	private int id;
	private String path;
	private String result;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Rule [" + path + "-> " + result + "]";
	}

}
