package com.reinforcement.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.math.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ʹ��Qlearning�㷨ʵ����ǿѧϰѰ·ģ��ʵ��
 * @author LINC
 *
 */
public class QLearning {
	public static QLearningService qLearningService = new QLearningService();
	
	/**
	 * ��ʼ��������,��XMLtoNeo4j���У���ʱ����Ҫ�ˡ�
	 * @throws Exception
	 */
	public static void inputActions() throws Exception{
		String instanceId = null;
		String action = null;
		SAXReader reader = new SAXReader();
		Document doc=reader.read(new File("xclass1.xml"));
		Element rootElem=doc.getRootElement();
		List<Element> elements = rootElem.elements("Semantic");
		for(int i=0; i<elements.size(); i++) {
			Element element = elements.get(i);
			//��ȡinstanceId
			Element elementBody = element.element("body");//��ȡ�ӱ�ǩ
			Element elementBodyInstance = elementBody.element("Instance");
			instanceId = elementBodyInstance.attributeValue("ID");//��ȡ��ǩ����ֵ
			//��ȡ������
			Element elementTail = element.element("tail");
			Element elementTailAction = elementTail.element("action");
			action = elementTailAction.getText();//��ȡ��ǩ����
			if(action.length() == 0) {
				action = "nul";
			}
			qLearningService.serviceInitAction(instanceId, action);
			System.out.println("���"+instanceId+" �Ķ�������ʼ��,�䶯����Ϊ��"+action);
		}
		System.out.println("��ɽ�㶯������ʼ��!");
	}
	
	public static void main(String[] args) throws Exception {
		//����ʼʱ��
		long startTime=System.currentTimeMillis();
		//ֻ��Ҫ��ʼ��һ��
//		inputActions();//��ʼ��������
		qLearningService.serviceInitQValue();//��ʼ��Qֵ
		qLearningService.serviceInitRValue();//��ʼ��Rֵ

		String start = "#52";//��ʼ���instanceID
		String end = "#1060";//�յ�instanceID
		double e = 20;//��20%�ĸ�������ң�80%ѡ��Qֵ���Ķ�����
		int episode = 1;//episode���ڿ���̽���Ĵ���
		int maxEpisode = 80000;
		int count =0;
		int fin = 0;//���ڼ�¼�ҵ�Ŀ���Ĵ���
		List<List<String>> allPath = new ArrayList<List<String>>();//��¼���е�·��
		List<String> optimalPath = new ArrayList<String>();
		qLearningService.serviceSetFinalR(end, 100);//�����յ��Rֵ
		while(episode <= maxEpisode) {
			System.out.println("��ʼ��"+episode+"��̽��-------------------");
			List<String> path = new ArrayList<String>();
			//����������ѭ̰���㷨
			String tempNode = start;
			String nextNode = null;
			int step = 0;//�����ۼ��ߵĲ���
			//��Ծ����ģ��e�����仯��һ��ʼ���̽���ĸ���Ϊ80%������������һ������̽���ĸ��ʽ���Ϊ20%
			if(episode <= maxEpisode/2) {
				e = 80;
			}else {
				e = 20;
			}
			//һ�κ���ģ��
//			if(fin > 33) {
//				e = 0;
//			}else {
//				e = -3*fin+100;
//			}
			//���Һ���ģ��
//			double pi = Math.PI;
//			e = 100*Math.cos(fin*pi/60);
			//ÿһ��̽��
			while(true) {
				path.add(tempNode);
				Random random = new Random();
				int x = random.nextInt(100);
				//��ȡ���Ķ�������
				String[] tempActions = qLearningService.serviceGetActionAll(tempNode);//action_all�Ķ���
				System.out.println("��ѡ·��������:"+tempActions.length);
				if(x < e) { //x < e�����������һ������
					System.out.println("���ѡ����");
					//��ȡ�������
					int y = random.nextInt(tempActions.length);
					nextNode = tempActions[y];
					System.out.println("���ѡ��Ľ��Ϊ��"+nextNode);
				}else { //����ѡȡ���Qֵ�Ķ���
					System.out.println("ѡȡ���Qֵ�Ķ���");
					Map<String,Double> map = new HashMap<String,Double>();
					map = qLearningService.serviceGetMaxQ(tempNode);
					//��ȡQֵ���Ľ�㣬��Ϊ��һ��״̬
					for(String str : map.keySet()) {
						nextNode = str ;
					}
					System.out.println("Qֵ���Ľ��Ϊ��"+nextNode);
				}
				//�ж�nextNode�Ƿ�ΪĿ��㣬���ǽ�������̽��
				if(nextNode.equals(end)) {
					fin +=1;
					System.out.println("��ϲ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!�ҵ�Ŀ��㣬��������̽��");
					path.add(nextNode);
					int isHad = 0;
					//��·���Ѿ����������ҵ���·���У����ظ����
					for(int i=0; i<allPath.size(); i++) {
						if(allPath.get(i).equals(path)) {
							isHad +=1;
						}
					}
					if(isHad == 0) {
						allPath.add(path);
					}
					//��̽������·��������·�����г��ȱȽ�
					if(optimalPath.size() == 0) {
						optimalPath = path;
					}else {
						if(path.size() < optimalPath.size()) {
							optimalPath = path;
						}
					}
					double rValue = Integer.valueOf(qLearningService.serviceGetRValue(nextNode));
					double length = (double)path.size();
					double lengthValue = (1/length)*rValue;//����·�����Ƚ���
					System.out.println("·������Ϊ��"+length+"·�����Ƚ���:"+lengthValue);
					double allValue = rValue + lengthValue; //�ܽ��������ҵ�Ŀ��Ľ����ͳ��Ƚ���֮�͡�
					//��ȡ��Ҫ���µ�Qֵ
					String[] qUpdate = new String[2];
					qUpdate = qLearningService.serviceGetKV(tempNode,nextNode);
					System.out.println(Arrays.deepToString(qUpdate));
					double oldQValue = Double.valueOf(qUpdate[1]);
					//���ù�ʽ�����µ�Qֵ
					double newQValue = oldQValue+0.6*(allValue+0.8*0-oldQValue);
					//����Qֵ�����������
					qLearningService.serviceUpdateQValue(tempNode,nextNode,newQValue);
					System.out.println("��Ŀ����"+nextNode+"��oldQValueΪ��"+oldQValue+"  newQValueΪ��"+newQValue);
					BigDecimal b1 = new BigDecimal(Double.toString(newQValue));
					BigDecimal b2 = new BigDecimal(Double.toString(oldQValue));
					if(newQValue!=0.0 &&oldQValue!=0.0 && b1.subtract(b2).doubleValue()<0.01 && b1.subtract(b2).doubleValue()>-0.01){
						System.out.println(b1+"///"+b2+"�Ѿ���������������");
						count++;
						episode=100000;
					}
					break;
				}
				String[] nextNodeActions = qLearningService.serviceGetActionAll(nextNode);//����ҲӦ����action_all
				System.out.println("nextNode��ѡ·��������Ϊ��"+nextNodeActions.length);
				System.out.println("nextNode�ܵ���Ľ��Ϊ��"+Arrays.deepToString(nextNodeActions));
				//�ж�nextNode�Ƿ�ΪҶ�ӽ�㣬��������Ƿ�ΪĿ��㣬�����ǽ�������ѭ��
				String[] nextNodeAction = qLearningService.serviceGetActions(nextNode);//����action
				for(int i =0 ;i<nextNodeAction.length;i++){
					System.out.println(nextNodeAction[i]);
				}
				if(nextNodeAction.length <= 1 && nextNodeAction[0].equals("nul")) {
					if(!nextNode.equals(end)) {
						System.out.println("�ýڵ�ΪҶ�ӽ�㣬��������̽����������һ��̽��...");
						break;
					}
				}
				//�õ�����,�۲����Rֵ�����Qֵ
				//��ȡ����Ӧ��Rֵ
				int rValue = Integer.valueOf(qLearningService.serviceGetRValue(nextNode));
				System.out.println("nextNode��RֵΪ��"+rValue);
				//��ȡnextNode�����Qֵ
				Map<String,Double> mapNext = new HashMap<String,Double>();
				mapNext = qLearningService.serviceGetMaxQ(nextNode);
				double nextQMax = 0;
				for(String str : mapNext.keySet()) {
					nextQMax = mapNext.get(str) ;
				}
				//�����һ��������Qֵ��
				//��ȡ��Ҫ���µ�Qֵ
				String[] qUpdate = new String[2];
				qUpdate = qLearningService.serviceGetKV(tempNode,nextNode);
				System.out.println(Arrays.deepToString(qUpdate));
				double oldQValue = Double.valueOf(qUpdate[1]);
				//���ù�ʽ�����µ�Qֵ
				double newQValue = oldQValue+0.6*(rValue+0.8*nextQMax-oldQValue);
				
				//����Qֵ�����������
				qLearningService.serviceUpdateQValue(tempNode,nextNode,newQValue);
				System.out.println("Rֵ��"+rValue+"���Qֵ��"+nextQMax);
				System.out.println("�����"+nextNode+"��oldQValueΪ��"+oldQValue+"  newQValueΪ��"+newQValue);
				//������һ״̬ 
				tempNode = nextNode;
				step +=1;
				System.out.println("��һ�ֵ����Ϊ"+tempNode+"��ǰ����Ϊ��"+step);
				if(step > 100) {
					System.out.println("�������̽����������������̽����������һ��̽��...");
					break;
				}
			}
			episode +=1; 	
		}
		System.out.println(episode-1+"��̽�����ҵ�Ŀ���"+fin+"��");
		System.out.println("����Ŀ����·��������"+allPath.size());
		if(allPath.size()>0){
			for(int i =0; i<allPath.size(); i++) {
				System.out.println(allPath.get(i));
			}
		}
		else{
			System.out.println("û��·������������");
		}
		
		//�������õ�������·��������ĳ���ó�����·���������Լ�ѡ������Qֵ����������ʽΪ���·����
		int[] allLength = new int[allPath.size()];
		List<List<String>> bestWay = new ArrayList<List<String>>();
		for(int i=0; i<allPath.size(); i++) {
			allLength[i] = allPath.get(i).size();
		}
		Arrays.sort(allLength);//��ȡ���·���ĳ���
		for(int i=0; i<allPath.size(); i++) {
			if(allPath.get(i).size() == allLength[0]) {
				bestWay.add(allPath.get(i));//�����е����·����ӽ�bestWay
			}
		}
		System.out.println("����·��һ���У�"+bestWay.size()+"��");
		for(int i =0; i<bestWay.size(); i++) {
			System.out.println(bestWay.get(i));
		}
		System.out.println("Q-learningѧϰ���¹���Ϊ��");
		List<List<String>> typeLists = new ArrayList<List<String>>();
		for(int i =0; i<bestWay.size(); i++) {
			List<String> typeList = new ArrayList<String>();
			for(int j=0; j<bestWay.get(i).size(); j++) {
				String type = qLearningService.serviceGetType(bestWay.get(i).get(j));
				typeList.add(type);
			}
			typeLists.add(typeList);
		}
		for(int i=0; i<bestWay.size(); i++) {
			for(int j=0; j<bestWay.get(i).size()-1; j++) {
				String[] nodeActions =  qLearningService.serviceGetActions(bestWay.get(i).get(j));
				String nextNode = bestWay.get(i).get(j+1);
				//����action���Ƿ����nextNode��㣬����������������������Ϊ������
				int isIn = 0;
				for(String str : nodeActions){
					if(str.equals(nextNode)) {
						System.out.print(typeLists.get(i).get(j)+"_contains_"+typeLists.get(i).get(j+1)+"��");
						isIn += 1; 
					}
				}
				if(isIn == 0) {
					System.out.print(typeLists.get(i).get(j)+"_contains(-1)_"+typeLists.get(i).get(j+1)+"��");
				}
			}
			System.out.println("�õ�������");
		}
		
		//!!!!ע�⣺�����OWL�ļ����еĽ�㣬����NEO4J���ݿ���û�еĽ�㣬����·���Ĺ����л���ִ���ʵ���ʱ����Ҫע�⡣
		//ȷ��ʲôʱ������������������ԭ����ǰ����������Q��
		//����R���趨��Q�ĸ��¶Եõ���·��ѡ������·����1��·���̵����ŵģ�2������·�����ظ�ѡ��3��
		//�õ�����·���󣬸��ݽ�������ѧϰ������
		//�㷨�ĸĽ��㣺1����ά������Q�������ά���Լ�Qֵ��2��̽���ͱ���֮�����Ȩ�⣬���Ȩ�⡣
		//�������ʱ��
		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		System.out.println("��������ʱ�䣺 "+(endTime-startTime)+"ms");
		System.out.println("������"+count+"��");
	}
}
