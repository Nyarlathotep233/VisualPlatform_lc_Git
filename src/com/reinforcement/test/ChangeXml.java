package com.reinforcement.test;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
/**
 * 在导入数据库之前，需要先执行改文件，将每个结点的动作集写入XML中。
 * 获取语义元中的动作集
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
        System.out.println("恭喜！！动作集提取完毕。");
    }



    static void update(Document doc){
        NodeList elelist = doc.getElementsByTagName("Semantic");
        System.out.println("总语义元结点数为："+elelist.getLength());
        //elelist.getLength()获取语义元结点的个数
        for (int i=0;i<elelist.getLength();i++){
            Node node=  elelist.item(i);//node为一个语义元
            NodeList childnodes=node.getChildNodes();//childnodes为语义元下面的所有子标签对
            String v="";
            String re="";
            //childnodes.getLength()语义元中拥有的子标签对的数量;
            for(int j=0;j<childnodes.getLength();j++){
                Node n= childnodes.item(j);
                //n.getNodeName()得到的是head，body，tail;
                if( n.getNodeName().equals("head")){
                    NodeList nl=n.getChildNodes();//获得head的子标签集合
                    //循环子标签对
                    for (int k=0;k<nl.getLength();k++){
                        NodeList pl= nl.item(k).getChildNodes();
                        for (int p=0;p<pl.getLength();p++){
                            Node jing= pl.item(p); //jing表示符号#
                            if (jing.getNodeType()==Node.ELEMENT_NODE){
                                String a=jing.getTextContent();//获取值
                                String b="x";
                                if(a.charAt(0)=='('){
                                     b= a.substring(1,a.length()-1);//取括号中的值
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
            System.out.println("第"+(i+1)+"个完成提取...");
        }
    }
}