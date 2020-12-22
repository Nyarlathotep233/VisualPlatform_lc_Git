package com.vp.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Block {
	private String blockId;//���ͼ���id
	private Set<String> baseFaces;//���ͼ���������Ļ���
	private Map<String,List<String>> childBlocks;//��Ż���ͻ����ϵ��ӿ�
	private List<String> features;//���ͼ������������
	private List<String> faces;//���ͼ������ʵ�����
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
