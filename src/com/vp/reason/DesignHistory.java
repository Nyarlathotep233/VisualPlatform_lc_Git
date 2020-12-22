package com.vp.reason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vp.domain.Block;
import com.vp.domain.DesignStep;
import com.vp.service.DesignHistoryService;

public class DesignHistory {
	//ע�⣺��������������ʷ��ǰ�᣺1�����ݿ��д������ͼ�����Ϣ������ͼ����������������ͼ������ʵ����棬�Լ�����������Щ����ɵģ�2����������ļ�����Ϣ
	//�㷨���裺
	//1.����ͼ������νṹ��ϵ
	//1.1����ͼ��ĸ������ϵ
	//���壺���ӿ�������涨��Ϊ���棬���ɻ�����Ϊ������
	//����һ��������ļ�����Ϣ�в��Һ���2�������ϻ�����
	//�������ɸѡ��ͼ�����ʵ�����к���2�������ϻ�����
	//������������ÿ��ͼ���������棬�����Ƿ���ڻ���
	//�����ģ����ݻ��е�egde_curve�ҵ��ཻ��
	//�����壺���Ҹ����Ƿ�Ϊ����������ʵ�����е���
	//���������ж�curves.size�ó������ͼ��ĸ������ϵ��
	//2.���ν�ͼ������������ɡ�
	//3.��������ǰ��������ṹ
	public static DesignHistoryService designHistoryService = new DesignHistoryService();
	public static void main(String[] args) {
		//ɸѡ��ͼ�����ʵ�����к���2�������ϵ��棬����µ�newBlockMap
		Map<String,List<String>> newBlockMap = new HashMap<String,List<String>>();
		newBlockMap = designHistoryService.maxBlockFace();
		//�ж�ͼ��֮��ĸ������ϵ
		List<Block> blockList = new ArrayList<Block>();
		blockList = designHistoryService.baseFaces(newBlockMap);
		//���ͼ��������������
		System.out.println("���ͼ��������������:-------------");
		List<Block> blockListFeatures =new ArrayList<Block>();
		blockListFeatures = designHistoryService.addBlockFeatures(blockList);
		for(int i = 0; i < blockListFeatures.size(); i++) {
			System.out.println(blockListFeatures.get(i));
		}
		//���ͼ������ʵ����
		System.out.println("���ͼ������ʵ����:-------------");
		List<Block> blockListOk =new ArrayList<Block>();
		blockListOk = designHistoryService.addBlockFaces(blockListFeatures);
		for(int i = 0; i < blockListOk.size(); i++) {
			System.out.println(blockListOk.get(i));
		}
		//---------���´�����ҪΪ�ҳ�ͼ���еĸ���㣨���Է�װ�ɷ���������service�㣩-----------
		System.out.println("����ͼ��ĸ����:-------------");
		List<String> allChildBlocks = new ArrayList<String>();
		for(int i = 0; i < blockListOk.size(); i++) {
			if(blockListOk.get(i).getBaseFaces() != null) {
				//��һ��ͼ�������е��ӿ���ӵ�һ��List��
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
				blockListOk.get(i).setIsRoot(1);//��1��ʾ����㣬�������Ϊ-1.
				System.out.println(blockListOk.get(i).getBlockId()+"Ϊ�����");
			}
		}
		//---------���´���������������������ʷ-------------
		System.out.println("��������������ʷ:-------------");
		DesignStep designStep = new DesignStep();
		//����������ֳܷ��ӿ���������һ���������һ��������
		if(blockListOk.size() == 1) {
			System.out.println("�����ֻ��һ��ͼ��");
			//operations����˳����ÿ������Ĳ���
			List<String> operations = new ArrayList<String>();
			//relationFaces����˳����ÿ��������Ҫ��������
			List<List<String>> relationFaces = new ArrayList<List<String>>();
			operations.add("͹̨-����");
			relationFaces.add(blockListOk.get(0).getFaces());
			for(String feature : blockListOk.get(0).getFeatures()) {
				//�����������ͺ����������������ͼ������������ʷ������ͼ���в�û�д������������ͣ����е����������Ͷ���feature��
				//�����Ҫʹ��������ʽ��ID�л�ȡ�����ľ������͡�
				Map<String,List<String>> featureRelation = new HashMap<String,List<String>>();
				featureRelation = designHistoryService.featureType(feature);
				for(String featureType : featureRelation.keySet()) {
					if(featureType.equals("hole")) {
						operations.add("��-�г�����");
						relationFaces.add(featureRelation.get(featureType));
					}
					if(featureType.equals("concave")) {
						operations.add("ֱ����-�г�����");
						relationFaces.add(featureRelation.get(featureType));
					}
				}
			}
			//��������List,չʾ�����ʷ
			designStep.setOperation(operations);
			designStep.setRelationFaces(relationFaces);
			for(int i = 0; i < designStep.getOperation().size(); i++) {
				System.out.println(designStep.getOperation().get(i));
			}
			for(int i = 0; i < designStep.getRelationFaces().size(); i++) {
				System.out.println(designStep.getRelationFaces().get(i));
			}
		}
		//��������ֳܷɶ��������
		if(blockListOk.size() > 1) {
			//���÷��������н�Ҷ����ͼ���к��е�������һ��leavesFeatureList
			List<String> allLeavesFeatures = new ArrayList<String>();
			allLeavesFeatures = designHistoryService.leavesFeature(blockListOk);
			//���һ������ֳܷɶ���飬������Ҫ�ҵ�����㣬�Ը����Ϊ���������
			for(int i = 0; i < blockListOk.size(); i++) {
				//������isRoot����Ϊ1
				if(blockListOk.get(i).getIsRoot() == 1) {
					System.out.println(blockListOk.get(i).getBlockId());
					designStep = designHistoryService.designHistoryMap(blockListOk.get(i), allLeavesFeatures, blockListOk,designStep);
				}
			}
		}
		System.out.println("-----����������������ʷ���Ϊ��-----");
		System.out.println(designStep);
	}
}
