package com.reinforcement.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLtoNeo4j {
	public static QLearningService qLearningService = new QLearningService();
	public static void main(String[] args) throws Exception {
		//这里的删除结点有些问题，暂时还是使用InitDataBase中的删除。
//		qLearningService.serviceDeleteAllSCs();
//		qLearningService.serviceDeleteAllLinks();
		SAXReader reader = new SAXReader();
		Document doc=reader.read(new File("zhouTry.xml"));
		Element rootElem=doc.getRootElement();
		List<Element> elements = rootElem.elements("Semantic");
		List<List<String>> allLink = new ArrayList<List<String>>();
		for(int i=0; i<elements.size(); i++) {
			//通过解析，得到需要存到结点中的值和属性:type,instanceId,pmi_linear,actions,name
			String type = "";
			String instanceId = "";
			String actions = "";
			String pmi_linear = "";
			String name = "";
			List<String> valueList = new ArrayList<String>();//依次存以上参数
			List<String> linkList = new ArrayList<String>();//保存link需要的参数，instanceId和actions
			Element element = elements.get(i);
			//获取type
			type = element.attributeValue("type");//获取标签属性值
			valueList.add(type);
			//获取instanceId
			Element elementBody = element.element("body");//获取子标签
			Element elementBodyInstance = elementBody.element("Instance");
			instanceId = elementBodyInstance.attributeValue("ID");//获取标签属性值
			valueList.add(instanceId);
			linkList.add(instanceId);
			//获取线性PMI值
			Element elementBodyPmiLinear = elementBody.element("pmi_linear");
			if(elementBodyPmiLinear != null) {
				pmi_linear = elementBodyPmiLinear.getText();
			}
			valueList.add(pmi_linear);
			//获取动作集
			Element elementTail = element.element("tail");
			Element elementTailAction = elementTail.element("action");
			actions = elementTailAction.getText();//获取标签内容
			if(actions.length() == 0) {
				actions = "nul";
			}
			valueList.add(actions);
			linkList.add(actions);
			name = instanceId+"_"+type;
			valueList.add(name);
			allLink.add(linkList);
			//创建结点，存入需要的属性。
			qLearningService.serviceCreateNode(valueList);
		}
		for(int i=0; i<allLink.size(); i++) {
			String fromId = allLink.get(i).get(0);
			String[] toIds = allLink.get(i).get(1).split(",");
			if(!toIds[0].equals("nul")) {
				for(int j=0; j<toIds.length; j++) {
					//将formId，和逐个toIds传入，建立结点之间的连接
					qLearningService.serviceCreateLink(fromId, toIds[j]);
					//在建立连线的同时，将反向动作级添加到相应的结点中
					qLearningService.serviceAddActionFan(toIds[j], fromId);
				}
			}
		}
		//遍历所有结点，生成allAction
		qLearningService.serviceInitAllActions();
		qLearningService.serviceInitActionNumber();
		System.out.println("node , link , action , action_fan , action_all 初始化完毕！");
	}
}
