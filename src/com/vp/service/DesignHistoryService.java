package com.vp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.vp.dao.DesignHistoryDao;
import com.vp.domain.Block;
import com.vp.domain.DesignStep;

public class DesignHistoryService {
	public static DesignHistoryDao designHistoryDao = new DesignHistoryDao();
	/**
	 * ��ȡ���ʵ������У�����2�����ϻ�����
	 * @return
	 */
	public Map<String,List<String>> maxBlockFace(){
		//1.1��ȡ���ʵ�����
		Map<String,String> blockMap = new HashMap<String,String>();
		blockMap = designHistoryDao.getBlockFaces();
		//1.2��ȡ����2�������ϵ�������
		List<String> faces = new ArrayList<String>();
		faces = designHistoryDao.faceHasBounds();
		//�Ƚ�����ȡ�����ϵ������newBlockMap��
		Map<String,List<String>> newBlockMap = new HashMap<String,List<String>>();
		Set<String> blockIds = blockMap.keySet();
		for(String blockId : blockIds) {
			String str = blockMap.get(blockId);
			List<String> blockFaces = new ArrayList<String>();
			blockFaces = Arrays.asList(str.split(","));
			List<String> newBlockFaces = new ArrayList<String>();
			for(String face : blockFaces) {
				if(faces.contains(face)) {
					newBlockFaces.add(face);
				}
			}
			System.out.println(blockId+"�����ǻ������ListΪ��"+newBlockFaces);
			newBlockMap.put(blockId, newBlockFaces);
		}
		return newBlockMap;
	}
	
	/**
	 * ������������͸����е�һ��edge_curve��������ڸ�edge_curve�͸����ཻ����
	 * @param inFace
	 * @param curveId
	 * @return
	 */
	public String findOtherFace(String inFace, String curveId) {
		String otherFace = null;
		List<String> faces = new ArrayList<String>();
		faces = designHistoryDao.otherFace(curveId);
		for(String face : faces) {
			if(!face.equals(inFace)) {
				otherFace = face;
				System.out.println("��"+inFace+"�ཻ����Ϊ��"+otherFace);
			}			
		}
		return otherFace;		
	}
	
	/**
	 * �жϸ������ĸ�ͼ����
	 * @param face
	 * @return
	 */
	public List<String> inWhichBlock(String face) {
		List<String> inBlocks =new ArrayList<String>();
		inBlocks = designHistoryDao.inWhichBlock(face);
		return inBlocks;
	}
	
	/**
	 * �ҳ�ͼ���ĸ������ϵ,����һ��List<Block>,��������ͼ�����Ϣ��
	 * @return
	 */
	public List<Block> baseFaces(Map<String,List<String>> newBlockMap) {		
		//blockMap�д����������ʵ����档��newBlockMap�д���Ǻ���2�������ϵ����ʵ���棬��blockMap���Ӽ���
		Map<String,String> blockMap = new HashMap<String,String>();
		blockMap = designHistoryDao.getBlockFaces();
		//����ͼ��,�����е�ͼ�����һ��List�������ӿ���blockBaesFaces.size����0.
		List<Block> blockList = new ArrayList<Block>();
		for(String blockId : newBlockMap.keySet()) {
			//����һ��Block���͵�block�������ڴ��ʵ����ÿ��ͼ����������
			Block block = new Block();
			block.setBlockId(blockId);
			//blockBaesFaces���ڴ��ͼ���д��ڵĻ���
			Set<String> blockBaesFaces = new HashSet<String>();			
			List<String> faces = new ArrayList<String>();
			faces = newBlockMap.get(blockId);
			//����ͼ���е�ÿһ���棬��Ϊÿ���涼�п����ǻ���
			for(String face : faces) {
				//childBlocks���ڴ����Ӧ�����ϵ���ͼ��
				List<String> childBlocks = new ArrayList<String>();
				List<String> bounds = new ArrayList<String>();
				List<String> curves = new ArrayList<String>();
				bounds = designHistoryDao.boundsList(face);
				int i = 1;
				//�������еĻ�����Ϊ��֪���ĸ������������ӿ顣
				for(String bound : bounds) {
					curves = designHistoryDao.boundsHasCurve(bound);
					//������ཻ��ÿһ���涼Ӧ��λ���ӿ��ϣ����ֻҪ��һ�������ϣ��û��Ͳ�����Ϊ����������ȡһ��ʵ��
					String curve = curves.get(0);
					System.out.println("ͼ��"+blockId+"�е�"+face+"��ĵ�"+i+"�����ĵ�һ��edge_curve idΪ��"+curve);
					String otherFace = findOtherFace(face, curve);
					//�ٴγ��Լ��������ͼ�飬�����Ľ���Ӧ�����ӿ�����ʵ������Ӽ�
					for(String blocksId : blockMap.keySet()) {
						if(blocksId.equals(blockId)) {
							continue;
						}
						//������ͼ������ʵ��������ý���ʱ�����ͼ���п���Ϊ�ӿ�
						if(blockMap.get(blocksId).contains(otherFace)) {
							//���ӿ�ΪԲ��͹̨ʱ��curves.size() == 1
							if(curves.size() == 1) {
								blockBaesFaces.add(face);
								childBlocks.add(blocksId);
								System.out.println(face+"Ϊ�����Ҹû�ΪԲ!!!!!!!!!!��"+blockId+"Ϊ"+blocksId+"�ĸ���ͼ��");
							}
							//���ӿ�Ϊһ��͹̨ʱ��curves.size() > 1 ���ӿ�Ϊ��Բ��ʱ����Ҫ�ж���������ܱ�֤��
							if(curves.size() > 1) {
								String curve1 = curves.get(1);
								String otherFace1 = findOtherFace(face, curve1);
								if(blockMap.get(blocksId).contains(otherFace1)) {
									blockBaesFaces.add(face);
									childBlocks.add(blocksId);
									System.out.println(face+"Ϊ����!!!!!!!!!!��"+blockId+"Ϊ"+blocksId+"�ĸ���ͼ��");
								}
							}
						}
					}
					i+=1;		
				}
				//���������ӿ飬����һ���ӿ��Map,���ڽ�ÿ����������Ӧ���ӿ���block�С�
				if(childBlocks.size() > 0) {
					Map<String,List<String>> childBlocksMap = new HashMap<String,List<String>>();
					childBlocksMap.put(face, childBlocks);
					block.setChildBlocks(childBlocksMap);
				}
			}
			//�������������󣬽����������block������
			if(blockBaesFaces.size() > 0) {
				block.setBaseFaces(blockBaesFaces);
			}
			blockList.add(block);
			System.out.println(block);
		}
		return blockList;
	}
	
	/**
	 * �ڿ������������Ϣ
	 * @param blockList
	 * @return
	 */
	public List<Block> addBlockFeatures(List<Block> blockList){
		for(int i = 0; i < blockList.size(); i++) {
			String blockId = blockList.get(i).getBlockId();
			List<String> features =new ArrayList<String>();
			features = designHistoryDao.getBlockFeatures(blockId);
			blockList.get(i).setFeatures(features);
		}
		return blockList;		
	}
	
	/**
	 * ������ʵ������Ϣ
	 * @param blockList
	 * @return
	 */
	public List<Block> addBlockFaces(List<Block> blockList) {
		Map<String,String> maxBlockFaces = new HashMap<String,String>();
		maxBlockFaces = designHistoryDao.getBlockFaces();
		for(int i = 0; i < blockList.size(); i++) {
			String blockId = blockList.get(i).getBlockId();
			for(String id : maxBlockFaces.keySet()) {
				if(id.equals(blockId)) {
					String str = maxBlockFaces.get(id);
					List<String> faces = new ArrayList<String>();
					faces = Arrays.asList(str.split(","));
					blockList.get(i).setFaces(faces);
				}
			}			
		}
		return blockList;
	}
	
	/**
	 * ������Ϣ������ͼ���List����������Ҷ���ͼ�������
	 * @param blockList
	 * @return
	 */
	public List<String> leavesFeature(List<Block> blockList) {
		List<String> allLeavesFeatures = new ArrayList<String>();
		for(int i = 0; i < blockList.size(); i++) {
			if(blockList.get(i).getBaseFaces() == null) {
				allLeavesFeatures.addAll(blockList.get(i).getFeatures());
			}			
		}
		return allLeavesFeatures;
	}

	/**
	 * ����������id�����ص��������ľ������ͺ���صĹ�����
	 * @param featureId
	 * @return
	 */
	public Map<String,List<String>> featureType(String featureId) {
		Map<String,List<String>> featureRelation = new HashMap<String,List<String>>();
		List<String> relationFaces =new ArrayList<String>();
		//�Ӿ��й������head�л�ȡ��صĹ����档
		String str = designHistoryDao.getFeatureFace(featureId);
		String str1 = str.substring(str.indexOf("elementface")+13,str.indexOf("}"));
		int i = str1.indexOf("(");
		if(i == -1) {
			relationFaces.add(str1.substring(1, str1.length()-1));
		}
		if(i == 1) {
			String str2 = str1.substring(str1.indexOf("(")+1, str1.length()-2);
			List<String> result = Arrays.asList(str2.split(","));
			relationFaces.addAll(result);
		}
		//ʹ��������ʽ��featureId�л�ȡ�����ľ����������ֻ�ж����������͵ľ�������
		boolean isHole = Pattern.matches(".*hole.*", featureId);
		boolean isConcave = Pattern.matches(".*concave.*", featureId);
		boolean isCompoundfeature = Pattern.matches(".*compoundfeature.*", featureId);
		if(isHole) {
			featureRelation.put("hole", relationFaces);
			System.out.println(featureId+"������Ϊ����");
		}
		if(isConcave) {
			featureRelation.put("concave", relationFaces);
			System.out.println(featureId+"������Ϊ��ֱ����");
		}
		if(isCompoundfeature) {
			featureRelation.put("compoundfeature", relationFaces);
			System.out.println(featureId+"������Ϊ����������");
		}
		return featureRelation;
	}
	

	/**
	 * ʹ�õݹ�ķ�����ȡ����������ʷ
	 * @param rootBlock
	 * @param allLeavesFeatures
	 * @param blockList
	 * @param designStep
	 * @return
	 */
	public DesignStep designHistoryMap(Block rootBlock , List<String> allLeavesFeatures , List<Block> blockList , DesignStep designStep) {
		System.out.println("��ʼ����"+rootBlock.getBlockId()+"�������ʷ");
		//operations����˳����ÿ������Ĳ���
		List<String> operations = new ArrayList<String>();
		//relationFaces����˳����ÿ��������Ҫ��������
		List<List<String>> relationFaces = new ArrayList<List<String>>();
		operations.add("͹̨-����");
		relationFaces.add(rootBlock.getFaces());
		//�������������������,������Ҫ�����е���������Ҷ��㣨Ҷ�����жϷ����ǲ�������ͼ�飩
		for(String feature : rootBlock.getFeatures()) {
			if(rootBlock.getBaseFaces() != null && allLeavesFeatures.contains(feature)) {
					continue;
			}
			//�����������ͺ����������������ͼ������������ʷ������ͼ���в�û�д������������ͣ����е����������Ͷ���feature��
			//�����Ҫʹ��������ʽ��ID�л�ȡ�����ľ������͡�
			Map<String,List<String>> featureRelation = new HashMap<String,List<String>>();
			featureRelation = featureType(feature);
			for(String featureType : featureRelation.keySet()) {
				if(featureType.equals("hole")) {
					operations.add("��-�г�����");
					relationFaces.add(featureRelation.get(featureType));
				}
				if(featureType.equals("concave")) {
					operations.add("ֱ����-�г�����");
					relationFaces.add(featureRelation.get(featureType));
				}
				if(featureType.equals("compoundfeature")) {
					operations.add("��������-�г�����");
					relationFaces.add(featureRelation.get(featureType));
				}
			}
		}
		//�������еĻ��棬�����ҳ����е��ӿ飨�������д�ã�Ӧ�ÿ���ʹ�õݹ���ʵ����������������ʷ�Ĺ�����
		if(rootBlock.getBaseFaces() != null) {
			for(String baseFace :rootBlock.getBaseFaces()) {						
				operations.add("��׼��"+baseFace);
				List<String> result = Arrays.asList(baseFace);
				relationFaces.add(result);
				//��ӡ�ÿ�������ʷ
				System.out.println(designStep);
				List<String> addOperation =new ArrayList<String>();
				if(designStep.getOperation() != null) {
					System.out.println("���Ӹ���Ĳ�������");
					addOperation =designStep.getOperation();
					addOperation.addAll(operations);
					designStep.setOperation(addOperation);
				}else {
					designStep.setOperation(operations);
				}
				List<List<String>> addRelationFaces =new ArrayList<List<String>>();
				if(designStep.getRelationFaces() != null) {
					System.out.println("���Ӹ���Ĺ��������");
					addRelationFaces =designStep.getRelationFaces();
					addRelationFaces.addAll(relationFaces);
					designStep.setRelationFaces(addRelationFaces);
				}else {
					designStep.setRelationFaces(relationFaces);
				}
				
				for(int i = 0; i < designStep.getOperation().size(); i++) {
					System.out.println(designStep.getOperation().get(i));
				}
				for(int i = 0; i < designStep.getRelationFaces().size(); i++) {
					System.out.println(designStep.getRelationFaces().get(i));
				}
				//���ݻ�׼�������ͼ�飬ÿһ��ͼ��Ĵ���ʽ�͸�����ͼ��һ�£���Ҫд��һ�������ſ��Եݹ顣
				List<String> childBlocks = rootBlock.getChildBlocks().get(baseFace);
				for(int i = 0; i < childBlocks.size(); i++) {
					//���ѭ��Ϊ�˵õ���ӦblockId��ͼ��
					for(Block block : blockList) {
						if(block.getBlockId().equals(childBlocks.get(i))) {
							System.out.println("�������ӿ�Ϊid��"+childBlocks.get(i));
							designStep = designHistoryMap(block,allLeavesFeatures,blockList,designStep);
						}
					}		
				}
			}
		}else {
			//��ӡ�ÿ�������ʷ
			System.out.println(designStep);
			List<String> addOperation =new ArrayList<String>();
			if(designStep.getOperation() != null) {
				System.out.println("��Ҷ�ӽ���Ĳ�������");
				addOperation =designStep.getOperation();
				addOperation.addAll(operations);
				designStep.setOperation(addOperation);
			}else {
				designStep.setOperation(operations);
			}
			List<List<String>> addRelationFaces =new ArrayList<List<String>>();
			if(designStep.getRelationFaces() != null) {
				System.out.println("��Ҷ�ӽ���Ĺ��������");
				addRelationFaces =designStep.getRelationFaces();
				addRelationFaces.addAll(relationFaces);
				designStep.setRelationFaces(addRelationFaces);
			}else {
				designStep.setRelationFaces(relationFaces);
			}
			
			for(int i = 0; i < designStep.getOperation().size(); i++) {
				System.out.println(designStep.getOperation().get(i));
			}
			for(int i = 0; i < designStep.getRelationFaces().size(); i++) {
				System.out.println(designStep.getRelationFaces().get(i));
			}
		}
		return designStep;
	}
	
	/**
	 * ���ݿ��id��ȡ���ʵ��������
	 * @return
	 */
	public static Map<String,String> blockFace(){
		Map<String,String> blockMap = new HashMap<String,String>();
		blockMap = designHistoryDao.getBlockFaces();
		return blockMap;
	}	
	
	public static String getFirstFace(){
		List<String> faces = new ArrayList<String>();
//		List<Integer> Newfaces = new ArrayList<Integer>();
		faces = designHistoryDao.AllFaceId();
		String min=faces.get(0).substring(1);
		for (int i = 0; i < faces.size(); i++) {
			String faceNum=faces.get(i);
			faceNum=faceNum.substring(1);
			if(faceNum.compareTo(min)<0)
			{
				min=faceNum;
			}
			
//			System.out.println("���ڴ�ӡ�������id��"+Newfaces.get(i));

		}
//		int intMin=Integer.parseInt(min);
//		System.out.println("��С�������ĸ���"+intMin);
		return min;
	}
}
