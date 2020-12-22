package com.feature.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedFace {
	//面的类型#156=ADVANCED_FACE('',(#139),#149,.T.);
	//若第二个参数 为PLANE 则为平面                                                      0
	//若第二个参数 为CYLINDRICAL_SURFACE 则为柱面                1
	//若第二个参数 为TOROIDAL_SURFACE 则为圆环面                     2
	//若第二个参数 为SHERICAL_SURFACE 则为球面                         3
	//若第二个参数 为CONICAL_SURFACE 则为圆锥面                        4
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
