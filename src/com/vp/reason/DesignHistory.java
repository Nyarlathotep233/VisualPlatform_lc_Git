package com.vp.reason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vp.domain.Block;
import com.vp.domain.DesignStep;
import com.vp.service.DesignHistoryService;

public class DesignHistory {
	//注意：推理零件的设计历史的前提：1、数据库中存有零件图块的信息，包括图块中所含的特征和图块的最大实体表面，以及特征是由哪些面组成的；2、包含零件的几何信息
	//算法步骤：
	//1.建立图块的树形结构关系
	//1.1推理图块的父子类关系
	//定义：将子块的生成面定义为基面，生成环定义为基环。
	//步骤一：在零件的几何信息中查找含有2个或以上环的面
	//步骤二：筛选出图块最大实体面中含有2个或以上环的面
	//步骤三：遍历每个图块中所有面，查找是否存在基环
	//步骤四：根据环中的egde_curve找到相交面
	//步骤五：查找该面是否为其他块的最大实体面中的面
	//步骤六：判断curves.size得出基面和图块的父子类关系。
	//2.依次将图块的属性添加完成。
	//3.依照树的前序遍历树结构
	public static DesignHistoryService designHistoryService = new DesignHistoryService();
	public static void main(String[] args) {
		//筛选出图块最大实体面中含有2个或以上的面，组成新的newBlockMap
		Map<String,List<String>> newBlockMap = new HashMap<String,List<String>>();
		newBlockMap = designHistoryService.maxBlockFace();
		//判断图块之间的父子类关系
		List<Block> blockList = new ArrayList<Block>();
		blockList = designHistoryService.baseFaces(newBlockMap);
		//添加图块所包含的特征
		System.out.println("添加图块所包含的特征:-------------");
		List<Block> blockListFeatures =new ArrayList<Block>();
		blockListFeatures = designHistoryService.addBlockFeatures(blockList);
		for(int i = 0; i < blockListFeatures.size(); i++) {
			System.out.println(blockListFeatures.get(i));
		}
		//添加图块的最大实体面
		System.out.println("添加图块的最大实体面:-------------");
		List<Block> blockListOk =new ArrayList<Block>();
		blockListOk = designHistoryService.addBlockFaces(blockListFeatures);
		for(int i = 0; i < blockListOk.size(); i++) {
			System.out.println(blockListOk.get(i));
		}
		//---------以下代码主要为找出图块中的根结点（可以封装成方法，放在service层）-----------
		System.out.println("查找图块的根结点:-------------");
		List<String> allChildBlocks = new ArrayList<String>();
		for(int i = 0; i < blockListOk.size(); i++) {
			if(blockListOk.get(i).getBaseFaces() != null) {
				//将一个图块中所有的子块添加到一个List中
				for(String baseFaces : blockListOk.get(i).getBaseFaces()) {
					List<String> childBlocks = new ArrayList<String>();
					childBlocks = blockListOk.get(i).getChildBlocks().get(baseFaces);
					allChildBlocks.addAll(childBlocks);
				}
			}
		}
		for(int i = 0; i < blockListOk.size(); i++) {
			if(allChildBlocks.contains(blockListOk.get(i).getBlockId())) {
				continue;
			}else {
				blockListOk.get(i).setIsRoot(1);//用1表示根结点，其它结点为-1.
				System.out.println(blockListOk.get(i).getBlockId()+"为根结点");
			}
		}
		//---------以下代码用于生成零件的设计历史-------------
		System.out.println("生成零件的设计历史:-------------");
		DesignStep designStep = new DesignStep();
		//讨论零件不能分成子块的情况，即一个零件就是一个块的情况
		if(blockListOk.size() == 1) {
			System.out.println("该零件只有一个图块");
			//operations用于顺序存放每个步骤的操作
			List<String> operations = new ArrayList<String>();
			//relationFaces用于顺序存放每步操作需要关联的面
			List<List<String>> relationFaces = new ArrayList<List<String>>();
			operations.add("凸台-拉伸");
			relationFaces.add(blockListOk.get(0).getFaces());
			for(String feature : blockListOk.get(0).getFeatures()) {
				//依据特征类型和特征包含的面添加图块的特征设计历史。由于图块中并没有存具体的特征类型，所有的特征的类型都是feature，
				//因此需要使用正则表达式从ID中获取特征的具体类型。
				Map<String,List<String>> featureRelation = new HashMap<String,List<String>>();
				featureRelation = designHistoryService.featureType(feature);
				for(String featureType : featureRelation.keySet()) {
					if(featureType.equals("hole")) {
						operations.add("孔-切除拉伸");
						relationFaces.add(featureRelation.get(featureType));
					}
					if(featureType.equals("concave")) {
						operations.add("直方槽-切除拉伸");
						relationFaces.add(featureRelation.get(featureType));
					}
				}
			}
			//遍历两个List,展示设计历史
			designStep.setOperation(operations);
			designStep.setRelationFaces(relationFaces);
			for(int i = 0; i < designStep.getOperation().size(); i++) {
				System.out.println(designStep.getOperation().get(i));
			}
			for(int i = 0; i < designStep.getRelationFaces().size(); i++) {
				System.out.println(designStep.getRelationFaces().get(i));
			}
		}
		//讨论零件能分成多个块的情况
		if(blockListOk.size() > 1) {
			//调用方法，所有将叶结点的图块中含有的特征存一个leavesFeatureList
			List<String> allLeavesFeatures = new ArrayList<String>();
			allLeavesFeatures = designHistoryService.leavesFeature(blockListOk);
			//如果一个零件能分成多个块，我们需要找到根结点，以根结点为迭代的起点
			for(int i = 0; i < blockListOk.size(); i++) {
				//根结点的isRoot属性为1
				if(blockListOk.get(i).getIsRoot() == 1) {
					System.out.println(blockListOk.get(i).getBlockId());
					designStep = designHistoryService.designHistoryMap(blockListOk.get(i), allLeavesFeatures, blockListOk,designStep);
				}
			}
		}
		System.out.println("-----最终推理出的设计历史结果为：-----");
		System.out.println(designStep);
	}
}
