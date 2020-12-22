package com.feature.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedFace {
	//�������#156=ADVANCED_FACE('',(#139),#149,.T.);
	//���ڶ������� ΪPLANE ��Ϊƽ��                                                      0
	//���ڶ������� ΪCYLINDRICAL_SURFACE ��Ϊ����                1
	//���ڶ������� ΪTOROIDAL_SURFACE ��ΪԲ����                     2
	//���ڶ������� ΪSHERICAL_SURFACE ��Ϊ����                         3
	//���ڶ������� ΪCONICAL_SURFACE ��ΪԲ׶��                        4
	private int advancedFaceType;
	private String advancedFaceName;
	private String advancedFaceFlag;
	private String faceBoundFlag;
	Map<String, Object> advancedFaceMap = new HashMap<String, Object>();
	List<String> edges = new ArrayList<String>();
	List<EdgeCurve> edgeCurveList = new ArrayList<EdgeCurve>();
	public int getAdvancedFaceType() {
		return advancedFaceType;
	}
	public void setAdvancedFaceType(int advancedFaceType) {
		this.advancedFaceType = advancedFaceType;
	}
	public String getAdvancedFaceName() {
		return advancedFaceName;
	}
	public void setAdvancedFaceName(String advancedFaceName) {
		this.advancedFaceName = advancedFaceName;
	}
	public String getAdvancedFaceFlag() {
		return advancedFaceFlag;
	}
	public void setAdvancedFaceFlag(String advancedFaceFlag) {
		this.advancedFaceFlag = advancedFaceFlag;
	}
	public Map<String, Object> getAdvancedFaceMap() {
		return advancedFaceMap;
	}
	public void setAdvancedFaceMap(Map<String, Object> advancedFaceMap) {
		this.advancedFaceMap = advancedFaceMap;
	}
	public List<String> getEdges() {
		return edges;
	}
	public void setEdges(List<String> edges) {
		this.edges = edges;
	}
	public List<EdgeCurve> getEdgeCurveList() {
		return edgeCurveList;
	}
	public void setEdgeCurveList(List<EdgeCurve> edgeCurveList) {
		this.edgeCurveList = edgeCurveList;
	}
	public String getFaceBoundFlag() {
		return faceBoundFlag;
	}
	public void setFaceBoundFlag(String faceBoundFlag) {
		this.faceBoundFlag = faceBoundFlag;
	}
	@Override
	public String toString() {
		return "AdvancedFace [advancedFaceType=" + advancedFaceType
				+ ", advancedFaceName=" + advancedFaceName
				+ ", advancedFaceFlag=" + advancedFaceFlag + ", faceBoundFlag="
				+ faceBoundFlag + ", advancedFaceMap=" + advancedFaceMap
				+ ", edges=" + edges + ", edgeCurveList=" + edgeCurveList + "]";
	}
	
}
