package com.vp.domain;

import java.util.List;

import com.vp.semanticcell.SemanticCell;

/**
 * 用于传给前端
 * @author admin
 *
 */
public class ScsAndLinks {
	private List<SemanticCell> scs;
	private List<Link> links;
	public List<SemanticCell> getScs() {
		return scs;
	}
	public void setScs(List<SemanticCell> scs) {
		this.scs = scs;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public ScsAndLinks(List<SemanticCell> scs, List<Link> links) {
		super();
		this.scs = scs;
		this.links = links;
	}
	
	public ScsAndLinks() {
	}
}
