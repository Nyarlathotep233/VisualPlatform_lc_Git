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
	 * ��ȡ���е�advancedFace�ļ����б�
	 * @return
	 */
	public List<AdvancedFace> getAdvancedFaceList(){
		
		List<SemanticCell> adFSCs = semanticCellDAO.getSCsByType("advanced_face");
		List<AdvancedFace> advancedFaceList = new ArrayList<>();
		for(SemanticCell sc : adFSCs){
			AdvancedFace advancedFace = new AdvancedFace();
			String advancedFaceName = sc.getBody().getInstance().getInstanceId();
			int advancedFaceType = advancedFaceDAO.getAdvancedFaceType(advancedFaceName);
			//�������Ϊƽ��
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
	 * ��ƽ��֮���ཻ��ֱ�߱ߵİ�͹���ж�
	 * @param face1
	 * @param face2
	 * @param edgeCurve
	 * @return
	 */
	public int judgeFromTwoCommonFaces(AdvancedFace face1, AdvancedFace face2, EdgeCurve edgeCurve){
		//�ֱ��ȡface1�ķ�����n1,face2�ķ�����n2,������edgeCurve�ķ���ʸ��ne
		Point n1 = CommonUtil.getPlaneAdvancedFaceNormalVector(face1);
		Point n2 = CommonUtil.getPlaneAdvancedFaceNormalVector(face2);
		
		Point ne = CommonUtil.getEdgeCurveNormalVector(edgeCurve);
		
		//�ڻ��n1,n2,ne֮�󣬼���ne��n2����������÷���ʸ��n
		//��a=(X1,Y1,Z1),b=(X2,Y2,Z2),
		//a��b=��Y1Z2-Y2Z1,Z1X2-Z2X1,X1Y2-X2Y1��
		Point n = CommonUtil.getN1XN2(ne, n2);
		
		//�ж�n��n1֮��ļн�t,�ж�|t|��PI/2�Ĺ�ϵ����������ñ�Ϊ͹�ߣ���С��Ϊ����
		double t =  CommonUtil.getAngleFromN1AndN2(n, n1);
		
		if(Math.abs(t) > Math.PI / 2){
			System.out.println("face" + face1.getAdvancedFaceName() + "��" + 
							   "face" + face2.getAdvancedFaceName() + "����" +
							   "�ཻ��edgeCurve" + edgeCurve.getEdge_curve_name() + 
							   "�Ҹñ�Ϊ͹��");
			return 11;
		}else{
			System.out.println("face" + face1.getAdvancedFaceName() + "��" + 
					   "face" + face2.getAdvancedFaceName() + "����" +
					   "�ཻ��edgeCurve" + edgeCurve.getEdge_curve_name() + 
					   "�Ҹñ�Ϊ����");
			return 10;
		}
	}
	
	/**
	 * ƽ�������֮���ཻ�����ߵİ�͹���ж�
	 * @param face1
	 * @param face2
	 * @return
	 */
	public int judgeFromPlaneFaceAndCylindricalFace(AdvancedFace planeFace, AdvancedFace cylindricalFace, EdgeCurve edgeCurve){

		//���淽��ʸ��n
		Point n = CommonUtil.getCylindricalAdvancedFaceNormalVector(planeFace, cylindricalFace, edgeCurve);
		//�������淽��ʸ��n��ƽ�淨����n1�ļн�
		double t = CommonUtil.getAngleFromN1AndN2(n, CommonUtil.getPlaneAdvancedFaceNormalVector(planeFace));
		
		if(Math.abs(t) > Math.PI / 2){
			System.out.println("planeFace" + planeFace.getAdvancedFaceName() + "��" + 
							   "cylindricalFace" + cylindricalFace.getAdvancedFaceName() + "����" +
							   "�ཻ��edgeCurve" + edgeCurve.getEdge_curve_name() + 
							   "�Ҹñ�Ϊ͹��");
			return 21;
		}else{
			System.out.println("planeFace" + planeFace.getAdvancedFaceName() + "��" + 
					   "cylindricalFace" + cylindricalFace.getAdvancedFaceName() + "����" +
					   "�ཻ��edgeCurve" + edgeCurve.getEdge_curve_name() + 
					   "�Ҹñ�Ϊ����");
			return 20;
		}
	}
	
	/**
	 * �ж��������Ƿ��ཻ
	 * @param face1
	 * @param face2
	 * @return
	 */
	public EdgeCurve judgeIsIntersection(AdvancedFace face1, AdvancedFace face2){
		//1����ȡÿ��advanced_face��edgeCurveList��ÿ��������Щ�����
		List<EdgeCurve> edgeCurveList1 = face1.getEdgeCurveList();
		List<EdgeCurve> edgeCurveList2 = face2.getEdgeCurveList();
		//2���ж��������Ƿ��ཻ����������edgeCurveList�Ƿ��й���Ԫ�أ���Ϊ���ǵ��ཻ��
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
	 * ȡ������3���������������ϵ�edge_loop��advanced_face�ı��
	 * @return
	 */
	public Map<Integer,Integer> advancedFaceHas3(){
		Map<Integer,Integer> a = advancedFaceDAO.advancedFaceHas3();
		return a;
	}
	
	/**
	 * ��ȡ��һ��advanced_face��idֵ
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
	 * ȡ������2���������������ϵ�edge_loop��advanced_face�ı��
	 * @return
	 */
	public List<String> advancedFaceHas2(){
		List<String> a = advancedFaceDAO.advancedFaceHas2();
		return a;
	}
	
	/**
	 * �õ���Բ�����ཻ��ƽ��
	 * @return 
	 */
	public Map<String, String> planeIntersectCylindrical() {
		System.out.println("Start�õ���Բ�����ཻ��ƽ��ķ�����");
		//1��������Բ������advanced_face
		Map<String,List<String>> map = advancedFaceDAO.findADFaceHasCircle();
		System.out.println("���ϴ���Բ����idΪ��"+map.keySet());
		System.out.println("��Ӧ����Բ��idΪ��"+map.values());
		Map<String,List<String>> planeMap = new HashMap<>();
		Map<String,List<String>> cylindricalMap = new HashMap<>();
		Map<String,String> returnMap = new HashMap<>();
		//2������map,ȡ��key(key��ŵ���advanced_face��id)
		for(Map.Entry<String,List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> value =entry.getValue();
			//����key�ж�ÿ���������
			int faceType = advancedFaceDAO.getAdvancedFaceType(key);		
			if(faceType == 1) {
				System.out.println(key+"������Ϊcylindrical_surface��Բ����");
				cylindricalMap.put(key,value);
			}else if(faceType == 2) {
				System.out.println(key+"������Ϊtoroidal_surface��Բ����");
			}else if(faceType == 3) {
				System.out.println(key+"������Ϊsherical_surface������");
			}else if(faceType == 4) {
				System.out.println(key+"������Ϊconical_surface��Բ׶��");
			}else{
				System.out.println(key+"������Ϊplane��ƽ��");
				planeMap.put(key,value);
			}
		};
		System.out.println("���е�Բ����map"+cylindricalMap);
		System.out.println("���е�ƽ��mapΪ"+planeMap);
		//3��������ƽ����ɵ�map,��ÿһ��value��Բ����map�е�value�ȶԣ�������ͬ��value,��˵�����ڹ���һ��Բ���������ƽ����Բ�����ཻ
		for(Map.Entry<String, List<String>> planeSearch : planeMap.entrySet()) {
			String key = planeSearch.getKey();
			System.out.println("������keyΪ��"+key);
			List<String> values =planeSearch.getValue();
			System.out.println("������valuesΪ��"+values);
			for(String value : values) {
				System.out.println("values�е�valueΪ��"+value);
				for(Map.Entry<String, List<String>> cylindricalSearch : cylindricalMap.entrySet()) {
					List<String> cylindricalValues =cylindricalSearch.getValue();
					String cylindricalKey = cylindricalSearch.getKey();
					System.out.println("������cylindricalValuesΪ��"+cylindricalValues);
					if(cylindricalValues.contains(value)) {
						System.out.println("ƽ��"+key+"��Բ����"+cylindricalKey+"�ཻ");
						returnMap.put(key, cylindricalKey);
					}else {
						System.out.println("û��ƽ�����ཻԲ����");
					}
				}
			};
		}
		System.out.println("End�����keyΪƽ��id,valueΪԲ����id");
		return returnMap;
	}
	/**
	 * ȡ��edgecurve������
	 * @return
	 */
	public String getEdgeType(String edgeCurveName){
		String num=null;
		String type = edgeCurveDAO.getEdgeCurveType(edgeCurveName);
//		System.out.println("��ӡ�ߵ����ͣ�"+type);
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
