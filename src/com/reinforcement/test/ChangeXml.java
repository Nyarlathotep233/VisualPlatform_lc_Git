package com.reinforcement.test;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
/**
 * �ڵ������ݿ�֮ǰ����Ҫ��ִ�и��ļ�����ÿ�����Ķ�����д��XML�С�
 * ��ȡ����Ԫ�еĶ�����
 * @author LINC
 *
 */
public class ChangeXml{


    public static void main(String[] args) throws Exception{
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();

        DocumentBuilder db=dbf.newDocumentBuilder();

        Document document = db.parse("zhou.xml");

        update(document);
        save(document);
    }


    public static void save(Document doc) throws Exception{

        TransformerFactory tff = TransformerFactory.newInstance();

        Transformer tf = tff.newTransformer();

        tf.transform(new DOMSource(doc), new StreamResult("zhouAction.xml"));
        System.out.println("��ϲ������������ȡ��ϡ�");
    }



    static void update(Document doc){
        NodeList elelist = doc.getElementsByTagName("Semantic");
        System.out.println("������Ԫ�����Ϊ��"+elelist.getLength());
        //elelist.getLength()��ȡ����Ԫ���ĸ���
        for (int i=0;i<elelist.getLength();i++){
            Node node=  elelist.item(i);//nodeΪһ������Ԫ
            NodeList childnodes=node.getChildNodes();//childnodesΪ����Ԫ����������ӱ�ǩ��
            String v="";
            String re="";
            //childnodes.getLength()����Ԫ��ӵ�е��ӱ�ǩ�Ե�����;
            for(int j=0;j<childnodes.getLength();j++){
                Node n= childnodes.item(j);
                //n.getNodeName()�õ�����head��body��tail;
                if( n.getNodeName().equals("head")){
                    NodeList nl=n.getChildNodes();//���head���ӱ�ǩ����
                    //ѭ���ӱ�ǩ��
                    for (int k=0;k<nl.getLength();k++){
                        NodeList pl= nl.item(k).getChildNodes();
                        for (int p=0;p<pl.getLength();p++){
                            Node jing= pl.item(p); //jing��ʾ����#
                            if (jing.getNodeType()==Node.ELEMENT_NODE){
                                String a=jing.getTextContent();//��ȡֵ
                                String b="x";
                                if(a.charAt(0)=='('){
                                     b= a.substring(1,a.length()-1);//ȡ�����е�ֵ
                                }else {
                                    b=a;
                                }
                                v+=b+',';
                            }
                        }

                    }

                }
                if (v.length()>2)
                {
                    re= v.substring(0,v.length()-1);
                }
                
                if( n.getNodeName().equals("tail")){
                    Element element = doc.createElement("action");
                    element.setTextContent(re);
                    n.appendChild(element);
                }

            }
            System.out.println("��"+(i+1)+"�������ȡ...");
        }
    }
}