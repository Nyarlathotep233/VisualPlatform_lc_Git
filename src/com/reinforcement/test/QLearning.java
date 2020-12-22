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
 * 使用Qlearning算法实现增强学习寻路模拟实验
 * @author LINC
 *
 */
public class QLearning {
	public static QLearningService qLearningService = new QLearningService();
	
	/**
	 * 初始化动作集,在XMLtoNeo4j中有，暂时不需要了。
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
			//获取instanceId
			Element elementBody = element.element("body");//获取子标签
			Element elementBodyInstance = elementBody.element("Instance");
			instanceId = elementBodyInstance.attributeValue("ID");//获取标签属性值
			//获取动作集
			Element elementTail = element.element("tail");
			Element elementTailAction = elementTail.element("action");
			action = elementTailAction.getText();//获取标签内容
			if(action.length() == 0) {
				action = "nul";
			}
			qLearningService.serviceInitAction(instanceId, action);
			System.out.println("完成"+instanceId+" 的动作集初始化,其动作集为："+action);
		}
		System.out.println("完成结点动作集初始化!");
	}
	
	public static void main(String[] args) throws Exception {
		//程序开始时间
		long startTime=System.currentTimeMillis();
		//只需要初始化一次
//		inputActions();//初始化动作集
		qLearningService.serviceInitQValue();//初始化Q值
		qLearningService.serviceInitRValue();//初始化R值

		String start = "#52";//起始点的instanceID
		String end = "#1060";//终点instanceID
		double e = 20;//有20%的概率随机找，80%选择Q值最大的动作。
		int episode = 1;//episode用于控制探索的次数
		int maxEpisode = 80000;
		int count =0;
		int fin = 0;//用于记录找到目标点的次数
		List<List<String>> allPath = new ArrayList<List<String>>();//记录所有的路径
		List<String> optimalPath = new ArrayList<String>();
		qLearningService.serviceSetFinalR(end, 100);//设置终点的R值
		while(episode <= maxEpisode) {
			System.out.println("开始第"+episode+"次探索-------------------");
			List<String> path = new ArrayList<String>();
			//从起点出发遵循贪婪算法
			String tempNode = start;
			String nextNode = null;
			int step = 0;//用于累计走的步数
			//阶跃函数模拟e参数变化，一开始随机探索的概率为80%，当迭代超过一般后，随机探索的概率降低为20%
			if(episode <= maxEpisode/2) {
				e = 80;
			}else {
				e = 20;
			}
			//一次函数模拟
//			if(fin > 33) {
//				e = 0;
//			}else {
//				e = -3*fin+100;
//			}
			//正弦函数模拟
//			double pi = Math.PI;
//			e = 100*Math.cos(fin*pi/60);
			//每一次探索
			while(true) {
				path.add(tempNode);
				Random random = new Random();
				int x = random.nextInt(100);
				//获取结点的动作集合
				String[] tempActions = qLearningService.serviceGetActionAll(tempNode);//action_all的动作
				System.out.println("可选路径的数量:"+tempActions.length);
				if(x < e) { //x < e采用随机采用一个动作
					System.out.println("随机选择动作");
					//获取随机动作
					int y = random.nextInt(tempActions.length);
					nextNode = tempActions[y];
					System.out.println("随机选择的结点为："+nextNode);
				}else { //否则选取最大Q值的动作
					System.out.println("选取最大Q值的动作");
					Map<String,Double> map = new HashMap<String,Double>();
					map = qLearningService.serviceGetMaxQ(tempNode);
					//获取Q值最大的结点，作为下一个状态
					for(String str : map.keySet()) {
						nextNode = str ;
					}
					System.out.println("Q值最大的结点为："+nextNode);
				}
				//判断nextNode是否为目标点，若是结束本次探索
				if(nextNode.equals(end)) {
					fin +=1;
					System.out.println("恭喜!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!找到目标点，结束本次探索");
					path.add(nextNode);
					int isHad = 0;
					//若路径已经存在与所找到的路径中，则不重复添加
					for(int i=0; i<allPath.size(); i++) {
						if(allPath.get(i).equals(path)) {
							isHad +=1;
						}
					}
					if(isHad == 0) {
						allPath.add(path);
					}
					//将探索到的路径和最优路径进行长度比较
					if(optimalPath.size() == 0) {
						optimalPath = path;
					}else {
						if(path.size() < optimalPath.size()) {
							optimalPath = path;
						}
					}
					double rValue = Integer.valueOf(qLearningService.serviceGetRValue(nextNode));
					double length = (double)path.size();
					double lengthValue = (1/length)*rValue;//设置路径长度奖励
					System.out.println("路径长度为："+length+"路径长度奖励:"+lengthValue);
					double allValue = rValue + lengthValue; //总奖励等于找到目标的奖励和长度奖励之和。
					//获取需要更新的Q值
					String[] qUpdate = new String[2];
					qUpdate = qLearningService.serviceGetKV(tempNode,nextNode);
					System.out.println(Arrays.deepToString(qUpdate));
					double oldQValue = Double.valueOf(qUpdate[1]);
					//利用公式计算新的Q值
					double newQValue = oldQValue+0.6*(allValue+0.8*0-oldQValue);
					//将新Q值更新至结点中
					qLearningService.serviceUpdateQValue(tempNode,nextNode,newQValue);
					System.out.println("到目标结点"+nextNode+"的oldQValue为："+oldQValue+"  newQValue为："+newQValue);
					BigDecimal b1 = new BigDecimal(Double.toString(newQValue));
					BigDecimal b2 = new BigDecimal(Double.toString(oldQValue));
					if(newQValue!=0.0 &&oldQValue!=0.0 && b1.subtract(b2).doubleValue()<0.01 && b1.subtract(b2).doubleValue()>-0.01){
						System.out.println(b1+"///"+b2+"已经收敛啦！！！！");
						count++;
						episode=100000;
					}
					break;
				}
				String[] nextNodeActions = qLearningService.serviceGetActionAll(nextNode);//这里也应该用action_all
				System.out.println("nextNode可选路径的数量为："+nextNodeActions.length);
				System.out.println("nextNode能到达的结点为："+Arrays.deepToString(nextNodeActions));
				//判断nextNode是否为叶子结点，如果是则看是否为目标点，若不是结束本次循环
				String[] nextNodeAction = qLearningService.serviceGetActions(nextNode);//结点的action
				for(int i =0 ;i<nextNodeAction.length;i++){
					System.out.println(nextNodeAction[i]);
				}
				if(nextNodeAction.length <= 1 && nextNodeAction[0].equals("nul")) {
					if(!nextNode.equals(end)) {
						System.out.println("该节点为叶子结点，结束本次探索，进入下一次探索...");
						break;
					}
				}
				//得到动作,观察结点的R值，最大Q值
				//获取结点对应的R值
				int rValue = Integer.valueOf(qLearningService.serviceGetRValue(nextNode));
				System.out.println("nextNode的R值为："+rValue);
				//获取nextNode的最大Q值
				Map<String,Double> mapNext = new HashMap<String,Double>();
				mapNext = qLearningService.serviceGetMaxQ(nextNode);
				double nextQMax = 0;
				for(String str : mapNext.keySet()) {
					nextQMax = mapNext.get(str) ;
				}
				//走完第一步，更新Q值。
				//获取需要更新的Q值
				String[] qUpdate = new String[2];
				qUpdate = qLearningService.serviceGetKV(tempNode,nextNode);
				System.out.println(Arrays.deepToString(qUpdate));
				double oldQValue = Double.valueOf(qUpdate[1]);
				//利用公式计算新的Q值
				double newQValue = oldQValue+0.6*(rValue+0.8*nextQMax-oldQValue);
				
				//将新Q值更新至结点中
				qLearningService.serviceUpdateQValue(tempNode,nextNode,newQValue);
				System.out.println("R值："+rValue+"最大Q值："+nextQMax);
				System.out.println("到结点"+nextNode+"的oldQValue为："+oldQValue+"  newQValue为："+newQValue);
				//进入下一状态 
				tempNode = nextNode;
				step +=1;
				System.out.println("新一轮的起点为"+tempNode+"当前步数为："+step);
				if(step > 100) {
					System.out.println("到达最大探索步数，结束本次探索，进入下一次探索...");
					break;
				}
			}
			episode +=1; 	
		}
		System.out.println(episode-1+"次探索共找到目标点"+fin+"次");
		System.out.println("到达目标点的路径条数："+allPath.size());
		if(allPath.size()>0){
			for(int i =0; i<allPath.size(); i++) {
				System.out.println(allPath.get(i));
			}
		}
		else{
			System.out.println("没有路径？？？？？");
		}
		
		//上面程序得到了所有路劲，下面的程序得出最优路径，机器自己选择依靠Q值，（表现形式为最短路径）
		int[] allLength = new int[allPath.size()];
		List<List<String>> bestWay = new ArrayList<List<String>>();
		for(int i=0; i<allPath.size(); i++) {
			allLength[i] = allPath.get(i).size();
		}
		Arrays.sort(allLength);//获取最短路径的长度
		for(int i=0; i<allPath.size(); i++) {
			if(allPath.get(i).size() == allLength[0]) {
				bestWay.add(allPath.get(i));//将所有的最短路径添加进bestWay
			}
		}
		System.out.println("最优路径一个有："+bestWay.size()+"条");
		for(int i =0; i<bestWay.size(); i++) {
			System.out.println(bestWay.get(i));
		}
		System.out.println("Q-learning学习的新规则为：");
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
				//结点的action中是否存在nextNode结点，若存在则是正向动作，否则为反向动作
				int isIn = 0;
				for(String str : nodeActions){
					if(str.equals(nextNode)) {
						System.out.print(typeLists.get(i).get(j)+"_contains_"+typeLists.get(i).get(j+1)+"∧");
						isIn += 1; 
					}
				}
				if(isIn == 0) {
					System.out.print(typeLists.get(i).get(j)+"_contains(-1)_"+typeLists.get(i).get(j+1)+"∧");
				}
			}
			System.out.println("得到推理结果");
		}
		
		//!!!!注意：如果是OWL文件中有的结点，但是NEO4J数据库中没有的结点，在找路径的过程中会出现错误。实验的时候需要注意。
		//确定什么时间收敛，结束迭代。原本是前后两次整张Q表
		//根据R的设定和Q的更新对得到的路径选择最优路径。1、路径短的是优的，2、避免路径的重复选择，3、
		//得到最优路径后，根据结点的类型学习出规则
		//算法的改进点：1、从维护整张Q表，到结点维护自己Q值；2、探索和保守之间进行权衡，如何权衡。
		//程序结束时间
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		System.out.println("收敛了"+count+"次");
	}
}
