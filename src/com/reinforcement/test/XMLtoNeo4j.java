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
		//�����ɾ�������Щ���⣬��ʱ����ʹ��InitDataBase�е�ɾ����
//		qLearningService.serviceDeleteAllSCs();
//		qLearningService.serviceDeleteAllLinks();
		SAXReader reader = new SAXReader();
		Document doc=reader.read(new File("zhouTry.xml"));
		Element rootElem=doc.getRootElement();
		List<Element> elements = rootElem.elements("Semantic");
		List<List<String>> allLink = new ArrayList<List<String>>();
		for(int i=0; i<elements.size(); i++) {
			//ͨ���������õ���Ҫ�浽����е�ֵ������:type,instanceId,pmi_linear,actions,name
			String type = "";
			String instanceId = "";
			String actions = "";
			String pmi_linear = "";
			String name = "";
			List<String> valueList = new ArrayList<String>();//���δ����ϲ���
			List<String> linkList = new ArrayList<String>();//����link��Ҫ�Ĳ�����instanceId��actions
			Element element = elements.get(i);
			//��ȡtype
			type = element.attributeValue("type");//��ȡ��ǩ����ֵ
			valueList.add(type);
			//��ȡinstanceId
			Element elementBody = element.element("body");//��ȡ�ӱ�ǩ
			Element elementBodyInstance = elementBody.element("Instance");
			instanceId = elementBodyInstance.attributeValue("ID");//��ȡ��ǩ����ֵ
			valueList.add(instanceId);
			linkList.add(instanceId);
			//��ȡ����PMIֵ
			Element elementBodyPmiLinear = elementBody.element("pmi_linear");
			if(elementBodyPmiLinear != null) {
				pmi_linear = elementBodyPmiLinear.getText();
			}
			valueList.add(pmi_linear);
			//��ȡ������
			Element elementTail = element.element("tail");
			Element elementTailAction = elementTail.element("action");
			actions = elementTailAction.getText();//��ȡ��ǩ����
			if(actions.length() == 0) {
				actions = "nul";
			}
			valueList.add(actions);
			linkList.add(actions);
			name = instanceId+"_"+type;
			valueList.add(name);
			allLink.add(linkList);
			//������㣬������Ҫ�����ԡ�
			qLearningService.serviceCreateNode(valueList);
		}
		for(int i=0; i<allLink.size(); i++) {
			String fromId = allLink.get(i).get(0);
			String[] toIds = allLink.get(i).get(1).split(",");
			if(!toIds[0].equals("nul")) {
				for(int j=0; j<toIds.length; j++) {
					//��formId�������toIds���룬�������֮�������
					qLearningService.serviceCreateLink(fromId, toIds[j]);
					//�ڽ������ߵ�ͬʱ��������������ӵ���Ӧ�Ľ����
					qLearningService.serviceAddActionFan(toIds[j], fromId);
				}
			}
		}
		//�������н�㣬����allAction
		qLearningService.serviceInitAllActions();
		qLearningService.serviceInitActionNumber();
		System.out.println("node , link , action , action_fan , action_all ��ʼ����ϣ�");
	}
}
