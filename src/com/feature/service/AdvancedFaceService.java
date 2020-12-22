package com.feature.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feature.dao.AdvancedFaceDAO;
import com.feature.dao.EdgeCurveDAO;
import com.feature.dao.SemanticCellDAO;
import com.feature.domain.AdvancedFace;
import com.feature.domain.EdgeCurve;
import com.feature.domain.Point;
import com.feature.semanticcell.SemanticCell;
/**
 * 
 * @author LinC
 *
 */
public class AdvancedFaceService {
	private static AdvancedFaceDAO advancedFaceDAO = new AdvancedFaceDAO();
	private static EdgeCurveDAO edgeCurveDAO = new EdgeCurveDAO();
	private static SemanticCellDAO semanticCellDAO = new SemanticCellDAO();
	
	/**
	 * 获取所有的advancedFace的集合列表
	 * @return
	 */
	public List<AdvancedFace> getAdvancedFaceList(){
		
		List<SemanticCell> adFSCs = semanticCellDAO.getSCsByType("advanced_face");
		List<AdvancedFace> advancedFaceList = new ArrayList<>();
		for(SemanticCell sc : adFSCs){
			AdvancedFace advancedFace = new AdvancedFace();
			String advancedFaceName = sc.getBody().getInstance().getInstanceId();
			int advancedFaceType = advancedFaceDAO.getAdvancedFaceType(advancedFaceName);
			//面的类型为平面
			String advancedFaceFlag = sc.getTail().getAttribute().get("same_sense").substring(1, 2);
			Point advancedFaceDirection = advancedFaceDAO.getAdvancedFaceDirection(advancedFaceName);
			String faceBoundFlag = advancedFaceDAO.getFaceBoundFlag(advancedFaceName);
			Map<String, Object> advancedFaceMap = new HashMap<String,Object>();
			
			advancedFace.setAdvancedFaceName(advancedFaceName);
			advancedFace.setAdvancedFaceFlag(advancedFaceFlag);
			advancedFaceMap.put("face_name", advancedFaceName);
			advancedFaceMap.put("face_flag", advancedFaceFlag);
			advancedFaceMap.put("face_type", advancedFaceType);
			advancedFaceMap.put("face_direction",advancedFaceDirection);
			
			advancedFace.setAdvancedFaceMap(advancedFaceMap);
			advancedFace.setAdvancedFaceType(advancedFaceType);
			advancedFace.setFaceBoundFlag(faceBoundFlag);
			
			List<SemanticCell> ecScs = edgeCurveDAO.getEdgeCurveList(advancedFaceName);
			List<EdgeCurve> edgeCurveList = new ArrayList<EdgeCurve>();
			for (SemanticCell sc2 : ecScs) {
				EdgeCurve edgeCurve = new EdgeCurve();
				
				String edgeCurveName = sc2.getBody().getInstance().getInstanceId();
				String edgeCurveFlag = sc2.getTail().getAttribute().get("same_sense").substring(1, 2);
				String orientedEdgeFlag = edgeCurveDAO.getOrientedEdgeFlag(advancedFaceName, edgeCurveName);
				Point edgeCurveDirection = edgeCurveDAO.getedgeCurveDirection(edgeCurveName);
				
				Point vertexPoint = edgeCurveDAO.getVertexPointFromEdgeCurve(edgeCurveName);
				Point cartesianPoint = edgeCurveDAO.getCartesianPointFromEdgeCurve(edgeCurveName);
				Point circleFirstDirection = edgeCurveDAO.getCircleFirstDirection(edgeCurveName);
				
				edgeCurve.setEdge_curve_name(edgeCurveName);
				edgeCurve.setEdge_curve_flag(edgeCurveFlag);
				edgeCurve.setOriented_edge_flag(orientedEdgeFlag);
				edgeCurve.setEdge_curve_direction(edgeCurveDirection);
				edgeCurve.setVertexPoint(vertexPoint);
				edgeCurve.setCartesianPoint(cartesianPoint);
				edgeCurve.setCircleFirstDirection(circleFirstDirection);
				edgeCurveList.add(edgeCurve);
			}
			advancedFace.setEdgeCurveList(edgeCurveList);
			advancedFaceList.add(advancedFace);
		}
		return advancedFaceList;
	}	
	/**
	 * 两平面之间相交的直线边的凹凸性判定
	 * @param face1
	 * @param face2
	 * @param edgeCurve
	 * @return
	 */
	public int judgeFromTwoCommonFaces(AdvancedFace face1, AdvancedFace face2, EdgeCurve edgeCurve){
		//分别获取face1的法向量n1,face2的法向量n2,公共边edgeCurve的方向矢量ne
		Point n1 = CommonUtil.getPlaneAdvancedFaceNormalVector(face1);
		Point n2 = CommonUtil.getPlaneAdvancedFaceNormalVector(face2);
		
		Point ne = CommonUtil.getEdgeCurveNormalVector(edgeCurve);
		
		//在获得n1,n2,ne之后，计算ne与n2的向量积获得方向矢量n
		//设a=(X1,Y1,Z1),b=(X2,Y2,Z2),
		//a×b=（Y1Z2-Y2Z1,Z1X2-Z2X1,X1Y2-X2Y1）
		Point n = CommonUtil.getN1XN2(ne, n2);
		
		//判断n和n1之间的夹角t,判断|t|和PI/2的关系，若大于则该边为凸边，若小于为凹边
		double t =  CommonUtil.getAngleFromN1AndN2(n, n1);
		
		if(Math.abs(t) > Math.PI / 2){
			System.out.println("face" + face1.getAdvancedFaceName() + "和" + 
							   "face" + face2.getAdvancedFaceName() + "存在" +
							   "相交边edgeCurve" + edgeCurve.getEdge_curve_name() + 
							   "且该边为凸边");
			return 11;
		}else{
			System.out.println("face" + face1.getAdvancedFaceName() + "和" + 
					   "face" + face2.getAdvancedFaceName() + "存在" +
					   "相交边edgeCurve" + edgeCurve.getEdge_curve_name() + 
					   "且该边为凹边");
			return 10;
		}
	}
	
	/**
	 * 平面和柱面之间相交的曲边的凹凸性判定
	 * @param face1
	 * @param face2
	 * @return
	 */
	public int judgeFromPlaneFaceAndCylindricalFace(AdvancedFace planeFace, AdvancedFace cylindricalFace, EdgeCurve edgeCurve){

		//柱面方向矢量n
		Point n = CommonUtil.getCylindricalAdvancedFaceNormalVector(planeFace, cylindricalFace, edgeCurve);
		//计算柱面方向矢量n和平面法向量n1的夹角
		double t = CommonUtil.getAngleFromN1AndN2(n, CommonUtil.getPlaneAdvancedFaceNormalVector(planeFace));
		
		if(Math.abs(t) > Math.PI / 2){
			System.out.println("planeFace" + planeFace.getAdvancedFaceName() + "和" + 
							   "cylindricalFace" + cylindricalFace.getAdvancedFaceName() + "存在" +
							   "相交边edgeCurve" + edgeCurve.getEdge_curve_name() + 
							   "且该边为凸边");
			return 21;
		}else{
			System.out.println("planeFace" + planeFace.getAdvancedFaceName() + "和" + 
					   "cylindricalFace" + cylindricalFace.getAdvancedFaceName() + "存在" +
					   "相交边edgeCurve" + edgeCurve.getEdge_curve_name() + 
					   "且该边为凹边");
			return 20;
		}
	}
	
	/**
	 * 判断两个面是否相交
	 * @param face1
	 * @param face2
	 * @return
	 */
	public EdgeCurve judgeIsIntersection(AdvancedFace face1, AdvancedFace face2){
		//1、获取每个advanced_face的edgeCurveList即每个面由哪些边组成
		List<EdgeCurve> edgeCurveList1 = face1.getEdgeCurveList();
		List<EdgeCurve> edgeCurveList2 = face2.getEdgeCurveList();
		//2、判断两个面是否相交，即看两个edgeCurveList是否有公共元素，即为他们的相交边
		EdgeCurve edgeCurve = null;
		for(EdgeCurve edgeCurve1 : edgeCurveList1){
			for(EdgeCurve edgeCurve2 : edgeCurveList2){
				if(edgeCurve1.getEdge_curve_name().equals(edgeCurve2.getEdge_curve_name())){
					edgeCurve = edgeCurve1;
				}
			}
		}
		return edgeCurve;
	}
	
	/**
	 * 取出具有3个（包含）或以上的edge_loop的advanced_face的编号
	 * @return
	 */
	public Map<Integer,Integer> advancedFaceHas3(){
		Map<Integer,Integer> a = advancedFaceDAO.advancedFaceHas3();
		return a;
	}
	
	/**
	 * 获取第一个advanced_face的id值
	 * @return 
	 * @return
	 */
	public Integer getFirstAdvancedFaceId(){
		List<Integer> advancedFaces = advancedFaceDAO.getAdvancedFaceId();
		Collections.sort(advancedFaces);
		return advancedFaces.get(0);
	}

	
	public void ADFaceWithCylindrical() {
		Map<String,List<String>> map = advancedFaceDAO.findADFaceHasCircle();
	}
	
	/**
	 * 取出具有2个（包含）或以上的edge_loop的advanced_face的编号
	 * @return
	 */
	public List<String> advancedFaceHas2(){
		List<String> a = advancedFaceDAO.advancedFaceHas2();
		return a;
	}
	
	/**
	 * 得到和圆柱面相交的平面
	 * @return 
	 */
	public Map<String, String> planeIntersectCylindrical() {
		System.out.println("Start得到和圆柱面相交的平面的方法：");
		//1、查找有圆特征的advanced_face
		Map<String,List<String>> map = advancedFaceDAO.findADFaceHasCircle();
		System.out.println("面上存在圆的面id为："+map.keySet());
		System.out.println("对应面上圆的id为："+map.values());
		Map<String,List<String>> planeMap = new HashMap<>();
		Map<String,List<String>> cylindricalMap = new HashMap<>();
		Map<String,String> returnMap = new HashMap<>();
		//2、遍历map,取出key(key存放的是advanced_face的id)
		for(Map.Entry<String,List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> value =entry.getValue();
			//根据key判断每个面的类型
			int faceType = advancedFaceDAO.getAdvancedFaceType(key);		
			if(faceType == 1) {
				System.out.println(key+"的类型为cylindrical_surface的圆柱面");
				cylindricalMap.put(key,value);
			}else if(faceType == 2) {
				System.out.println(key+"的类型为toroidal_surface的圆环面");
			}else if(faceType == 3) {
				System.out.println(key+"的类型为sherical_surface的球面");
			}else if(faceType == 4) {
				System.out.println(key+"的类型为conical_surface的圆锥面");
			}else{
				System.out.println(key+"的类型为plane的平面");
				planeMap.put(key,value);
			}
		};
		System.out.println("所有的圆柱面map"+cylindricalMap);
		System.out.println("所有的平面map为"+planeMap);
		//3、遍历由平面组成的map,将每一个value与圆柱面map中的value比对，若有相同的value,则说明存在共用一个圆的情况，则平面与圆柱面相交
		for(Map.Entry<String, List<String>> planeSearch : planeMap.entrySet()) {
			String key = planeSearch.getKey();
			System.out.println("遍历的key为："+key);
			List<String> values =planeSearch.getValue();
			System.out.println("遍历的values为："+values);
			for(String value : values) {
				System.out.println("values中的value为："+value);
				for(Map.Entry<String, List<String>> cylindricalSearch : cylindricalMap.entrySet()) {
					List<String> cylindricalValues =cylindricalSearch.getValue();
					String cylindricalKey = cylindricalSearch.getKey();
					System.out.println("遍历的cylindricalValues为："+cylindricalValues);
					if(cylindricalValues.contains(value)) {
						System.out.println("平面"+key+"与圆柱面"+cylindricalKey+"相交");
						returnMap.put(key, cylindricalKey);
					}else {
						System.out.println("没有平面有相交圆柱面");
					}
				}
			};
		}
		System.out.println("End输出的key为平面id,value为圆柱面id");
		return returnMap;
	}
	/**
	 * 取出edgecurve的类型
	 * @return
	 */
	public String getEdgeType(String edgeCurveName){
		String num=null;
		String type = edgeCurveDAO.getEdgeCurveType(edgeCurveName);
//		System.out.println("打印边的类型："+type);
		if(type.equals("line"))
			num="1";
		else if(type.equals("circle"))
			num="2";
		else if(type.equals("b_spline_curve_with_knots"))
			num="3";
		else
			num=type;
//		System.out.println(num);
		return num;
	}

}
