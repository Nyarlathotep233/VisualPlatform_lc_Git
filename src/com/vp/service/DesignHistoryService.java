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
	 * 获取最大实体表面中，含有2个以上环的面
	 * @return
	 */
	public Map<String,List<String>> maxBlockFace(){
		//1.1获取最大实体表面
		Map<String,String> blockMap = new HashMap<String,String>();
		blockMap = designHistoryDao.getBlockFaces();
		//1.2获取含有2个或以上的所有面
		List<String> faces = new ArrayList<String>();
		faces = designHistoryDao.faceHasBounds();
		//比较两者取出符合的面存入newBlockMap中
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
			System.out.println(blockId+"可能是基面的面List为："+newBlockFaces);
			newBlockMap.put(blockId, newBlockFaces);
		}
		return newBlockMap;
	}
	
	/**
	 * 方法：输入面和该面中的一个edge_curve，输出基于该edge_curve和该面相交的面
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
				System.out.println("与"+inFace+"相交的面为："+otherFace);
			}			
		}
		return otherFace;		
	}
	
	/**
	 * 判断该面在哪个图块上
	 * @param face
	 * @return
	 */
	public List<String> inWhichBlock(String face) {
		List<String> inBlocks =new ArrayList<String>();
		inBlocks = designHistoryDao.inWhichBlock(face);
		return inBlocks;
	}
	
	/**
	 * 找出图块间的父子类关系,返回一个List<Block>,里面存放着图块的信息。
	 * @return
	 */
	public List<Block> baseFaces(Map<String,List<String>> newBlockMap) {		
		//blockMap中存的是组成最大实体的面。而newBlockMap中存的是含有2个环以上的最大实体面，是blockMap的子集。
		Map<String,String> blockMap = new HashMap<String,String>();
		blockMap = designHistoryDao.getBlockFaces();
		//遍历图块,将所有的图块存入一个List，若有子块则blockBaesFaces.size大于0.
		List<Block> blockList = new ArrayList<Block>();
		for(String blockId : newBlockMap.keySet()) {
			//创建一个Block类型的block对象用于存放实例中每个图块的相关属性
			Block block = new Block();
			block.setBlockId(blockId);
			//blockBaesFaces用于存放图块中存在的基面
			Set<String> blockBaesFaces = new HashSet<String>();			
			List<String> faces = new ArrayList<String>();
			faces = newBlockMap.get(blockId);
			//遍历图块中的每一个面，因为每个面都有可能是基面
			for(String face : faces) {
				//childBlocks用于存放相应基面上的子图块
				List<String> childBlocks = new ArrayList<String>();
				List<String> bounds = new ArrayList<String>();
				List<String> curves = new ArrayList<String>();
				bounds = designHistoryDao.boundsList(face);
				int i = 1;
				//遍历面中的环，因为不知道哪个环生长出了子块。
				for(String bound : bounds) {
					curves = designHistoryDao.boundsHasCurve(bound);
					//与基环相交的每一个面都应该位于子块上，因此只要有一个不符合，该环就不可能为基环，所有取一个实验
					String curve = curves.get(0);
					System.out.println("图块"+blockId+"中的"+face+"面的第"+i+"个环的第一个edge_curve id为："+curve);
					String otherFace = findOtherFace(face, curve);
					//再次除自己以外遍历图块，基环的交面应该是子块的最大实体面的子集
					for(String blocksId : blockMap.keySet()) {
						if(blocksId.equals(blockId)) {
							continue;
						}
						//当其他图块的最大实体面包含该交面时，这个图块有可能为子块
						if(blockMap.get(blocksId).contains(otherFace)) {
							//当子块为圆柱凸台时，curves.size() == 1
							if(curves.size() == 1) {
								blockBaesFaces.add(face);
								childBlocks.add(blocksId);
								System.out.println(face+"为基面且该环为圆!!!!!!!!!!且"+blockId+"为"+blocksId+"的父级图块");
							}
							//当子块为一般凸台时，curves.size() > 1 当子块为非圆柱时，需要判断两个面才能保证。
							if(curves.size() > 1) {
								String curve1 = curves.get(1);
								String otherFace1 = findOtherFace(face, curve1);
								if(blockMap.get(blocksId).contains(otherFace1)) {
									blockBaesFaces.add(face);
									childBlocks.add(blocksId);
									System.out.println(face+"为基面!!!!!!!!!!且"+blockId+"为"+blocksId+"的父级图块");
								}
							}
						}
					}
					i+=1;		
				}
				//如果面存在子块，创建一个子块的Map,用于将每个基面所对应的子块存放block中。
				if(childBlocks.size() > 0) {
					Map<String,List<String>> childBlocksMap = new HashMap<String,List<String>>();
					childBlocksMap.put(face, childBlocks);
					block.setChildBlocks(childBlocksMap);
				}
			}
			//遍历完成所有面后，将基面添加至block对象中
			if(blockBaesFaces.size() > 0) {
				block.setBaseFaces(blockBaesFaces);
			}
			blockList.add(block);
			System.out.println(block);
		}
		return blockList;
	}
	
	/**
	 * 在块中添加特征信息
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
	 * 添加最大实体面信息
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
	 * 传入信息完整的图块的List，返回所有叶结点图块的特征
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
	 * 传入特征的id，返回的是特征的具体类型和相关的关联面
	 * @param featureId
	 * @return
	 */
	public Map<String,List<String>> featureType(String featureId) {
		Map<String,List<String>> featureRelation = new HashMap<String,List<String>>();
		List<String> relationFaces =new ArrayList<String>();
		//从具有关联面的head中获取相关的关联面。
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
		//使用正则表达式从featureId中获取特征的具体类别。这里只判断了两种类型的具体特征
		boolean isHole = Pattern.matches(".*hole.*", featureId);
		boolean isConcave = Pattern.matches(".*concave.*", featureId);
		boolean isCompoundfeature = Pattern.matches(".*compoundfeature.*", featureId);
		if(isHole) {
			featureRelation.put("hole", relationFaces);
			System.out.println(featureId+"的类型为：孔");
		}
		if(isConcave) {
			featureRelation.put("concave", relationFaces);
			System.out.println(featureId+"的类型为：直方槽");
		}
		if(isCompoundfeature) {
			featureRelation.put("compoundfeature", relationFaces);
			System.out.println(featureId+"的类型为：复杂特征");
		}
		return featureRelation;
	}
	

	/**
	 * 使用递归的方法获取零件的设计历史
	 * @param rootBlock
	 * @param allLeavesFeatures
	 * @param blockList
	 * @param designStep
	 * @return
	 */
	public DesignStep designHistoryMap(Block rootBlock , List<String> allLeavesFeatures , List<Block> blockList , DesignStep designStep) {
		System.out.println("开始推理"+rootBlock.getBlockId()+"的设计历史");
		//operations用于顺序存放每个步骤的操作
		List<String> operations = new ArrayList<String>();
		//relationFaces用于顺序存放每步操作需要关联的面
		List<List<String>> relationFaces = new ArrayList<List<String>>();
		operations.add("凸台-拉伸");
		relationFaces.add(rootBlock.getFaces());
		//遍历根结点所含的特征,这里需要将共有的特征放在叶结点（叶结点的判断方法是不具有子图块）
		for(String feature : rootBlock.getFeatures()) {
			if(rootBlock.getBaseFaces() != null && allLeavesFeatures.contains(feature)) {
					continue;
			}
			//依据特征类型和特征包含的面添加图块的特征设计历史。由于图块中并没有存具体的特征类型，所有的特征的类型都是feature，
			//因此需要使用正则表达式从ID中获取特征的具体类型。
			Map<String,List<String>> featureRelation = new HashMap<String,List<String>>();
			featureRelation = featureType(feature);
			for(String featureType : featureRelation.keySet()) {
				if(featureType.equals("hole")) {
					operations.add("孔-切除拉伸");
					relationFaces.add(featureRelation.get(featureType));
				}
				if(featureType.equals("concave")) {
					operations.add("直方槽-切除拉伸");
					relationFaces.add(featureRelation.get(featureType));
				}
				if(featureType.equals("compoundfeature")) {
					operations.add("复制特征-切除拉伸");
					relationFaces.add(featureRelation.get(featureType));
				}
			}
		}
		//遍历所有的基面，依次找出所有的子块（这个功能写好，应该可以使用递归来实现整个零件的设计历史的构建）
		if(rootBlock.getBaseFaces() != null) {
			for(String baseFace :rootBlock.getBaseFaces()) {						
				operations.add("基准面"+baseFace);
				List<String> result = Arrays.asList(baseFace);
				relationFaces.add(result);
				//打印该块的设计历史
				System.out.println(designStep);
				List<String> addOperation =new ArrayList<String>();
				if(designStep.getOperation() != null) {
					System.out.println("将子父块的操作存入");
					addOperation =designStep.getOperation();
					addOperation.addAll(operations);
					designStep.setOperation(addOperation);
				}else {
					designStep.setOperation(operations);
				}
				List<List<String>> addRelationFaces =new ArrayList<List<String>>();
				if(designStep.getRelationFaces() != null) {
					System.out.println("将子父块的关联面存入");
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
				//依据基准面遍历子图块，每一个图块的处理方式和根结点的图块一致，需要写成一个方法才可以递归。
				List<String> childBlocks = rootBlock.getChildBlocks().get(baseFace);
				for(int i = 0; i < childBlocks.size(); i++) {
					//这个循环为了得到对应blockId的图块
					for(Block block : blockList) {
						if(block.getBlockId().equals(childBlocks.get(i))) {
							System.out.println("迭代的子块为id："+childBlocks.get(i));
							designStep = designHistoryMap(block,allLeavesFeatures,blockList,designStep);
						}
					}		
				}
			}
		}else {
			//打印该块的设计历史
			System.out.println(designStep);
			List<String> addOperation =new ArrayList<String>();
			if(designStep.getOperation() != null) {
				System.out.println("将叶子结点块的操作存入");
				addOperation =designStep.getOperation();
				addOperation.addAll(operations);
				designStep.setOperation(addOperation);
			}else {
				designStep.setOperation(operations);
			}
			List<List<String>> addRelationFaces =new ArrayList<List<String>>();
			if(designStep.getRelationFaces() != null) {
				System.out.println("将叶子结点块的关联面存入");
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
	 * 根据块的id获取最大实体表面的面
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
			
//			System.out.println("我在打印所有面的id："+Newfaces.get(i));

		}
//		int intMin=Integer.parseInt(min);
//		System.out.println("最小的面是哪个？"+intMin);
		return min;
	}
}
