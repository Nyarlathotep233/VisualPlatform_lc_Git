package com.vp.test;

import java.util.List;

import com.vp.semanticcell.SemanticCell;
import com.vp.xml.ParseXML;

public class TestParseXML {
	public static void main(String[] args) {
		try {
			List<SemanticCell> scs = ParseXML.getSemanticCells("model1.xml");
			for(SemanticCell semanticCell : scs){
				if(semanticCell.getBody().getInstance().getInstanceId().equals("#156")){
					System.out.println(semanticCell);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
