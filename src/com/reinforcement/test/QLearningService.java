package com.reinforcement.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QLearningService {
	public static QLearningDao qLearningDao = new QLearningDao();
	
	/**
	 * 初始化动作集
	 * @param instanceId
	 * @param action
	 */
	public void serviceInitAction(String instanceId,String action) {
		qLearningDao.initActions(instanceId, action);
	}
	
	/**
	 * 初始化Q值
	 */
	public void serviceInitQValue() {
		//获取每个结点的ID
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		for(int i=0; i<idList.size(); i++) {
			String str = idList.get(i);
			//获取每个ID的动作集action_all
			String actions = "";
			actions = qLearningDao.getActionAllString(str);
			String[] strActions = actions.split(",");
			Map<String,String> map = new HashMap<String,String>();
			for(int j=0; j<strActions.length; j++) {
				map.put(strActions[j], "0");
			}
			//初始化每个ID的Q值
			qLearningDao.initQValue(str,map);
			System.out.println("完成id为："+str+"的Q值初始化");
		}
		System.out.println("恭喜！Q值初始化完成");
	}

	/**
	 * 初始化给个结点的R值
	 */
	public void serviceInitRValue() {
		//获取每个结点的ID
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		for(int i=0; i<idList.size(); i++) {
			String str = idList.get(i);
			qLearningDao.initRValue(str);
			System.out.println("完成id为："+str+"的R值初始化");
		}
		System.out.println("恭喜！R值初始化完成");
	}

	/**
	 * 设置终点的R值
	 * @param instanceId
	 * @param value
	 */
	public void serviceSetFinalR(String instanceId,int value) {
		qLearningDao.setFinalR(instanceId,value);
		System.out.println("终点值设置成功");
	}
	
	/**
	 * 根据结点的instancedId，返回结点的动作集action
	 * @param instancedId
	 * @return
	 */
	public String[] serviceGetActions(String instancedId) {
		String[] actionValues = null;
		actionValues = qLearningDao.getActions(instancedId);
		return actionValues;
	}
	
	/**
	 * 根据结点的instancedId，返回结点的动作集action_all
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
	 * 根据instancedId，查找出结点所维护的Q值，并将其以map形式返回。
	 * @param instancedId
	 * @return
	 */
	public Map<String, String> serviceGetQV(String instancedId) {
		String qValue = qLearningDao.getQV(instancedId);
		Map<String,String> map = new HashMap<String,String>();
		String str = qValue.substring(1, qValue.length()-1);//去除{}				
		String[] strs = str.split(",");			
		for(int i=0; i<strs.length; i++) {
			strs[i] = strs[i].substring(strs[i].indexOf("#"));//去除#前面的空格
			String[] strTemp = strs[i].split("=");
			map.put(strTemp[0], strTemp[1]);				
		}
		return map;
	}
	
	/**
	 * 获取结点的R值
	 * @param instanceId
	 * @return
	 */
	public String serviceGetRValue(String instanceId) {
		String rValue = qLearningDao.getRValue(instanceId);
		return rValue;
	}
	
	/**
	 * 获取结点中最大的Q值，以map返回
	 * @param instanceId
	 * @return
	 */
	public Map<String,Double> serviceGetMaxQ(String instanceId) {
		Map<String,Double> mapReturn = new HashMap<String,Double>();
		Map<String,String> map = new HashMap<String,String>();
		map = serviceGetQV(instanceId);
		//获取map中Q值最大动作
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
	 * 获取结点中制定ID的key-value
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
	 * 更新Q值
	 * @param instanceId
	 * @param findId
	 * @param newQValue
	 */
	public void serviceUpdateQValue(String instanceId,String findId,double newQValue) {
		String strQValue = String.valueOf(newQValue);
		Map<String,String> map = new HashMap<String,String>();
		map = serviceGetQV(instanceId);
		map.put(findId,strQValue);
		//将Map存入结点
		qLearningDao.initQValue(instanceId,map);
	}

	/**
	 * 获取结点的类型type
	 * @param instanceId
	 * @return
	 */
	public String serviceGetType(String instanceId) {
		String type = qLearningDao.getType(instanceId);
		return type;
	}
	
	/**
	 * 创建Neo4j的语义元节点
	 * @param valueList
	 */
	public void serviceCreateNode(List<String> valueList) {
		qLearningDao.createNode(valueList);
	}
	
	/**
	 * 创建Neo4j的语义元节点之间的连线
	 * @param fromId
	 * @param toId
	 */
	public void serviceCreateLink(String fromId, String toId) {
		qLearningDao.createLink(fromId, toId);
	}
	
	/**
	 * 删除Neo4j所有语义元节点
	 */
	public void serviceDeleteAllSCs() {
		qLearningDao.deleteAllSCs();
	}
	
	/**
	 * 删除Neo4j所有连线及相关语义元节点
	 */
	public void serviceDeleteAllLinks() {
		qLearningDao.deleteAllLinks();
	}
	
	/**
	 * id为需要添加反向动作的结点id,action为其反向动作。
	 * @param id
	 * @param action
	 */
	public void serviceAddActionFan(String id,String action) {
		//根据id得到结点的反向动作集。
		String actionFan = qLearningDao.getActionFan(id);
		if(actionFan.length() == 0) {
			actionFan = actionFan + action;
		}else {
			actionFan = actionFan +"," +action;
		}
		qLearningDao.updateActionFan(id,actionFan);
	}
	
	/**
	 * 初始化给个结点的action_all值,将反向动作集和正向动作集添加到总的动作集中
	 */
	public void serviceInitAllActions() {
		//获取每个结点的ID
		Random random = new Random();
		List<String> idList = new ArrayList<String>();
		idList = qLearningDao.getInstanceIDs();
		String allActions = "";
		for(int i=0; i<idList.size(); i++) {
			String actionAll = "";
			String id = idList.get(i);
			String actions = qLearningDao.getActionString(id);
			String actionFan = qLearningDao.getActionFanString(id);
			if(actions.equals("nul")) {//叶子结点
				allActions = actionFan;
			}else if(actionFan.length() == 0){//根结点
				allActions = actions ;
			}else{//其它结点
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
			//更新allAction
			qLearningDao.updateAllAction(id, actionAll);
			System.out.println("初始化"+id+"的allAction完毕！++" + actionAll);
		}
	}
	/**
	 * 初始化给个结点的actionNum值
	 */
	public void serviceInitActionNumber() {
		//获取每个结点的ID
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
			//更新allAction
			qLearningDao.updateActionNum(id, actionsNum);
			System.out.println("初始化"+id+"的actionNum完毕！");
			System.out.println("最大的动作数量有"+maxNum+"  最小的动作数量是"+minNum);
		}
	}
	
	
	public static void main(String[] args) {
		
	}
}
