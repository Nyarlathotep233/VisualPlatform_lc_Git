package com.vp.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Block {
	private String blockId;//存放图块的id
	private Set<String> baseFaces;//存放图块中所含的基面
	private Map<String,List<String>> childBlocks;//存放基面和基面上的子块
	private List<String> features;//存放图块所含的特征
	private List<String> faces;//存放图块的最大实体表面
	private int isRoot = -1;
	
	public String getBlockId() {
		return blockId;
	}
	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
	public Set<String> getBaseFaces() {
		return baseFaces;
	}
	public void setBaseFaces(Set<String> baseFaces) {
		this.baseFaces = baseFaces;
	}
	public Map<String, List<String>> getChildBlocks() {
		return childBlocks;
	}
	public void setChildBlocks(Map<String, List<String>> childBlocks) {
		this.childBlocks = childBlocks;
	}
	public List<String> getFeatures() {
		return features;
	}
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	public List<String> getFaces() {
		return faces;
	}
	public void setFaces(List<String> faces) {
		this.faces = faces;
	}
	
	public int getIsRoot() {
		return isRoot;
	}
	public void setIsRoot(int isRoot) {
		this.isRoot = isRoot;
	}
	@Override
	public String toString() {
		return "Block [blockId=" + blockId + ", baseFaces=" + baseFaces + ", childBlocks=" + childBlocks + ", features="
				+ features + ", faces=" + faces + ", isRoot=" + isRoot + "]";
	}

	
	

	
	
	
}
