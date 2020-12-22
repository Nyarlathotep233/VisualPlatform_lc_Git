package com.feature.service;

import com.feature.domain.AdvancedFace;
import com.feature.domain.EdgeCurve;
import com.feature.domain.Point;
import com.google.gson.Gson;

public class CommonUtil {
	
	public static Gson gson = new Gson();
	
	/**
	 * 获得平面类型advancedFace的外法向量n
	 * @param advancedFace
	 * @param advancedFaceFlag
	 * @return
	 */
	public static Point getPlaneAdvancedFaceNormalVector(AdvancedFace advancedFace){
		Point n = new Point();
		String advancedFaceFlag = advancedFace.getAdvancedFaceFlag();
		if(advancedFaceFlag.equals("T")){
			String direction = advancedFace.getAdvancedFaceMap().get("face_direction").toString();
			n = gson.fromJson(direction,Point.class);
		}
		if(advancedFaceFlag.equals("F")){
			String direction = advancedFace.getAdvancedFaceMap().get("face_direction").toString();
			Point tempn = gson.fromJson(direction,Point.class);
			n.setCoordinates_x(0-tempn.getCoordinates_x());
			n.setCoordinates_y(0-tempn.getCoordinates_y());
			n.setCoordinates_z(0-tempn.getCoordinates_z());
		}
		return n;
	}
	
	/**
	 * 获得圆柱面类型advancedFace的外法向量n
	 * @param edgeCurve          相交边
	 * @return
	 */
	public static Point getCylindricalAdvancedFaceNormalVector(AdvancedFace planeFace,AdvancedFace cylindricalFace, EdgeCurve edgeCurve){
		Point n = new Point();
		Point p = edgeCurve.getVertexPoint();
		//圆心
		Point p0 = edgeCurve.getCartesianPoint();
		//由p指向p0的向量
		Point pVec = new Point();
		if(p0 != null && p != null){
			pVec.setCoordinates_x(p0.getCoordinates_x() - p.getCoordinates_x());
			pVec.setCoordinates_y(p0.getCoordinates_y() - p.getCoordinates_y());
			pVec.setCoordinates_z(p0.getCoordinates_z() - p.getCoordinates_z());
		}else{
			pVec.setCoordinates_x(1);
			pVec.setCoordinates_y(1);
			pVec.setCoordinates_z(1);
		}
		//edgeCurve为曲线时任意axis2_placement_3d实例中的第一个direction
		Point vec = edgeCurve.getCircleFirstDirection();
		//pVec和vec的向量积
		Point rVec = getN1XN2(pVec, vec);
		//判断柱面是否是其外表面组成零件实体表面
		String advancedFaceFlag = cylindricalFace.getAdvancedFaceFlag();
		String faceBoundFlag = cylindricalFace.getFaceBoundFlag();
		
		Point n2 = new Point();
		//是则计算n2=rVecXvec
		if(advancedFaceFlag.equals(faceBoundFlag)){
			n2 = getN1XN2(rVec, vec);
		//否则计算n2=vecXrVec
		}else{
			n2 = getN1XN2(vec, rVec);
		}
		
		//边e的方向矢量 ne = pVecXn1
		//n1为平面的法向量
		Point n1 = getPlaneAdvancedFaceNormalVector(planeFace);
		Point ne = getN1XN2(pVec, n1);
		
		//柱面的法向量
		n = getN1XN2(ne, n2);
		
		return n;
	}
	
	/**
	 * 获得直线edgeCurve的方向矢量n
	 * @param advancedFace
	 * @param edgeCurve
	 * @return
	 */
	public static Point getEdgeCurveNormalVector(EdgeCurve edgeCurve){
		Point n = new Point();
		//1、获取所在面的oriented_edge和edge_curve的flag
		String orientedEdgeFlag = edgeCurve.getOriented_edge_flag();
		String edgeCurveFlag = edgeCurve.getEdge_curve_flag();
		//2、获取该条边的vector中的direction实例
		Point direction = edgeCurve.getEdge_curve_direction();
		//3、若上面的两个flag一致则就是direction否则是反的
		if(orientedEdgeFlag.equals(edgeCurveFlag)){
			n = direction;
		}else{
			Point tempn = direction;
			n.setCoordinates_x(0-tempn.getCoordinates_x());
			n.setCoordinates_y(0-tempn.getCoordinates_y());
			n.setCoordinates_z(0-tempn.getCoordinates_z());
		}
		return n;
	}
	/**
	 * 计算n1叉乘n2 
	 * 设a=(X1,Y1,Z1),b=(X2,Y2,Z2),
	 * a×b=（Y1Z2-Y2Z1,Z1X2-Z2X1,X1Y2-X2Y1）
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static Point getN1XN2(Point n1, Point n2){
		Point n = new Point();
		if(n1 != null && n2 != null){
			n.setCoordinates_x(n1.getCoordinates_y() * n2.getCoordinates_z() - 
					   n2.getCoordinates_y() * n1.getCoordinates_z());
			n.setCoordinates_y(n1.getCoordinates_z() * n2.getCoordinates_x() - 
					   n2.getCoordinates_z() * n1.getCoordinates_x());
			n.setCoordinates_z(n1.getCoordinates_x() * n2.getCoordinates_y() - 
					   n2.getCoordinates_x() * n1.getCoordinates_y());
		}else{
			n.setCoordinates_x(1);
			n.setCoordinates_y(1);
			n.setCoordinates_z(1);
		}
		
		return n;
	}
	
	/**
	 * 计算两个向量的夹角
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static double getAngleFromN1AndN2(Point n1, Point n2){
		double x1 = n1.getCoordinates_x();
		double y1 = n1.getCoordinates_y();
		double z1 = n1.getCoordinates_z();
		double x2 = n2.getCoordinates_x();
		double y2 = n2.getCoordinates_y();
		double z2 = n2.getCoordinates_z();
		
		double nn1 = x1 * x2 + y1 * y2 + z1 * z2;
		
		double sqrt1 = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
		double sqrt2 = Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
		
		return Math.acos(nn1 / (sqrt1 * sqrt2) * Math.PI / 180);
	}
}
