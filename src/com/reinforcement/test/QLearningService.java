package com.reinforcement.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QLearningService {
	public static QLearningDao qLearningDao = new QLearningDao();
	
	/**
	 * ��ʼ��������
	 * @param instanceId
	 * @param action
	 */
	public void serviceInitAction(String instanceId,String action) {
		qLearningDao.initActions(instanceId, action);
	}
	
	/**
	 * ��ʼ��Qֵ
	 */
	public void serviceInitQValue() {
		//��ȡÿ������ID
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		for(int i=0; i<idList.size(); i++) {
			String str = idList.get(i);
			//��ȡÿ��ID�Ķ�����action_all
			String actions = "";
			actions = qLearningDao.getActionAllString(str);
			String[] strActions = actions.split(",");
			Map<String,String> map = new HashMap<String,String>();
			for(int j=0; j<strActions.length; j++) {
				map.put(strActions[j], "0");
			}
			//��ʼ��ÿ��ID��Qֵ
			qLearningDao.initQValue(str,map);
			System.out.println("���idΪ��"+str+"��Qֵ��ʼ��");
		}
		System.out.println("��ϲ��Qֵ��ʼ�����");
	}

	/**
	 * ��ʼ����������Rֵ
	 */
	public void serviceInitRValue() {
		//��ȡÿ������ID
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		for(int i=0; i<idList.size(); i++) {
			String str = idList.get(i);
			qLearningDao.initRValue(str);
			System.out.println("���idΪ��"+str+"��Rֵ��ʼ��");
		}
		System.out.println("��ϲ��Rֵ��ʼ�����");
	}

	/**
	 * �����յ��Rֵ
	 * @param instanceId
	 * @param value
	 */
	public void serviceSetFinalR(String instanceId,int value) {
		qLearningDao.setFinalR(instanceId,value);
		System.out.println("�յ�ֵ���óɹ�");
	}
	
	/**
	 * ���ݽ���instancedId�����ؽ��Ķ�����action
	 * @param instancedId
	 * @return
	 */
	public String[] serviceGetActions(String instancedId) {
		String[] actionValues = null;
		actionValues = qLearningDao.getActions(instancedId);
		return actionValues;
	}
	
	/**
	 * ���ݽ���instancedId�����ؽ��Ķ�����action_all
	 * @param instancedId
	 * @return
	 */
	public String[] serviceGetActionAll(String instancedId) {
		String[] actionValues = null;
		String actions = "";
		actions = qLearningDao.getActionAllString(instancedId);
		actionValues = actions.split(",");
		return actionValues;
	}
	
	/**
	 * ����instancedId�����ҳ������ά����Qֵ����������map��ʽ���ء�
	 * @param instancedId
	 * @return
	 */
	public Map<String, String> serviceGetQV(String instancedId) {
		String qValue = qLearningDao.getQV(instancedId);
		Map<String,String> map = new HashMap<String,String>();
		String str = qValue.substring(1, qValue.length()-1);//ȥ��{}				
		String[] strs = str.split(",");			
		for(int i=0; i<strs.length; i++) {
			strs[i] = strs[i].substring(strs[i].indexOf("#"));//ȥ��#ǰ��Ŀո�
			String[] strTemp = strs[i].split("=");
			map.put(strTemp[0], strTemp[1]);				
		}
		return map;
	}
	
	/**
	 * ��ȡ����Rֵ
	 * @param instanceId
	 * @return
	 */
	public String serviceGetRValue(String instanceId) {
		String rValue = qLearningDao.getRValue(instanceId);
		return rValue;
	}
	
	/**
	 * ��ȡ���������Qֵ����map����
	 * @param instanceId
	 * @return
	 */
	public Map<String,Double> serviceGetMaxQ(String instanceId) {
		Map<String,Double> mapReturn = new HashMap<String,Double>();
		Map<String,String> map = new HashMap<String,String>();
		map = serviceGetQV(instanceId);
		//��ȡmap��Qֵ�����
		double maxValue = 0;
		String maxInstanceId = null;
		for(String str : map.keySet()) {
			double tempValue = Double.valueOf(map.get(str));
			if(tempValue >= maxValue) {
				maxInstanceId = str;
				maxValue = tempValue;
			}
		}
		mapReturn.put(maxInstanceId, maxValue);
		return mapReturn;
	}
	
	/**
	 * ��ȡ������ƶ�ID��key-value
	 * @param instanceId
	 * @param findId
	 * @return
	 */
	public String[] serviceGetKV(String instanceId,String findId) {
		String[] strs = new String[2];
		Map<String,String> map = new HashMap<String,String>();
		map = serviceGetQV(instanceId);
		for(String str : map.keySet()) {
			if(str.equals(findId)) {
				strs[0] = findId;
				strs[1] = map.get(findId);
			}
		}
		return strs;
	}

	/**
	 * ����Qֵ
	 * @param instanceId
	 * @param findId
	 * @param newQValue
	 */
	public void serviceUpdateQValue(String instanceId,String findId,double newQValue) {
		String strQValue = String.valueOf(newQValue);
		Map<String,String> map = new HashMap<String,String>();
		map = serviceGetQV(instanceId);
		map.put(findId,strQValue);
		//��Map������
		qLearningDao.initQValue(instanceId,map);
	}

	/**
	 * ��ȡ��������type
	 * @param instanceId
	 * @return
	 */
	public String serviceGetType(String instanceId) {
		String type = qLearningDao.getType(instanceId);
		return type;
	}
	
	/**
	 * ����Neo4j������Ԫ�ڵ�
	 * @param valueList
	 */
	public void serviceCreateNode(List<String> valueList) {
		qLearningDao.createNode(valueList);
	}
	
	/**
	 * ����Neo4j������Ԫ�ڵ�֮�������
	 * @param fromId
	 * @param toId
	 */
	public void serviceCreateLink(String fromId, String toId) {
		qLearningDao.createLink(fromId, toId);
	}
	
	/**
	 * ɾ��Neo4j��������Ԫ�ڵ�
	 */
	public void serviceDeleteAllSCs() {
		qLearningDao.deleteAllSCs();
	}
	
	/**
	 * ɾ��Neo4j�������߼��������Ԫ�ڵ�
	 */
	public void serviceDeleteAllLinks() {
		qLearningDao.deleteAllLinks();
	}
	
	/**
	 * idΪ��Ҫ��ӷ������Ľ��id,actionΪ�䷴������
	 * @param id
	 * @param action
	 */
	public void serviceAddActionFan(String id,String action) {
		//����id�õ����ķ���������
		String actionFan = qLearningDao.getActionFan(id);
		if(actionFan.length() == 0) {
			actionFan = actionFan + action;
		}else {
			actionFan = actionFan +"," +action;
		}
		qLearningDao.updateActionFan(id,actionFan);
	}
	
	/**
	 * ��ʼ����������action_allֵ,����������������������ӵ��ܵĶ�������
	 */
	public void serviceInitAllActions() {
		//��ȡÿ������ID
		Random random = new Random();
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		String allActions = "";
		for(int i=0; i<idList.size(); i++) {
			String actionAll = "";
			String id = idList.get(i);
			String actions = qLearningDao.getActionString(id);
			String actionFan = qLearningDao.getActionFanString(id);
			if(actions.equals("nul")) {//Ҷ�ӽ��
				allActions = actionFan;
			}else if(actionFan.length() == 0){//�����
				allActions = actions ;
			}else{//�������
				allActions = actions +","+actionFan;
			}
			if(allActions != ""){
				String[] actionsArray = allActions.split(",");
				if(actionsArray.length == 4){
					System.out.println("4");
					actionAll = allActions;
				}else if(actionsArray.length == 3){
					System.out.println("3");
					int x = random.nextInt(2895) + 10;
					actionAll = allActions + ",#" + x;
				}else if(actionsArray.length == 2){
					System.out.println("2");
					int x = random.nextInt(2895) + 10;
					int y = random.nextInt(2895) + 10;
					boolean flag = (x == y); 
					while(flag == true){
						x = random.nextInt(2895) + 10;
						y = random.nextInt(2895) + 10;
						flag = (x == y);
					}
					actionAll = allActions + ",#" + x + ",#" + y;
				}else if(actionsArray.length == 1){
					System.out.println("1");
					int x = random.nextInt(2895) + 10;
					int y = random.nextInt(2895) + 10;
					int z = random.nextInt(2895) + 10;
					boolean flag = (x==y||x==z||y==z);
					while(flag == true){
						x = random.nextInt(2895) + 10;
						y = random.nextInt(2895) + 10;
						z = random.nextInt(2895) + 10;
						flag = (x==y||x==z||y==z);
					}
					actionAll = allActions + ",#" + x + ",#" + y + ",#" + z;
				}else{
					System.out.println(">4");
					actionAll = actionsArray[0] + "," + actionsArray[1] + "," + actionsArray[actionsArray.length-1] + "," + actionsArray[actionsArray.length-2];
				}
				
			}
			else{
				System.out.println("0");
				int count = 0;
				while(count < 4){
					int x = random.nextInt(2903);
					if(x>=10 && count<3){
						count++;
						actionAll = actionAll + "#" + x + ",";
					}
					else if(x>=10 && count==3){
						count++;
						actionAll = actionAll + "#" + x ;
					}
				}
			}
			//����allAction
			qLearningDao.updateAllAction(id, actionAll);
			System.out.println("��ʼ��"+id+"��allAction��ϣ�++" + actionAll);
		}
	}
	/**
	 * ��ʼ����������actionNumֵ
	 */
	public void serviceInitActionNumber() {
		//��ȡÿ������ID
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		int actionsNum =0;
		int minNum = 10;
		int maxNum = 0;
		for(int i=0; i<idList.size(); i++) {
			String id = idList.get(i);
			String allActions = qLearningDao.getActionAllString(id);
			if(allActions.length()!=0){
				if(allActions.indexOf(",")!=-1){
					actionsNum = allActions.split(",").length;
				}
				else{
					actionsNum = 1;
				}
			}
			else{
				actionsNum = 0;
			}
			if(actionsNum>maxNum){
				maxNum = actionsNum;
			}
			if(actionsNum<minNum){
				minNum = actionsNum;
			}
			//����allAction
			qLearningDao.updateActionNum(id, actionsNum);
			System.out.println("��ʼ��"+id+"��actionNum��ϣ�");
			System.out.println("���Ķ���������"+maxNum+"  ��С�Ķ���������"+minNum);
		}
	}
	
	
	public static void main(String[] args) {
		
	}
}
