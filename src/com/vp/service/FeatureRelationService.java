package com.vp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vp.dao.FeatureRelationDao;

public class FeatureRelationService {
	private static FeatureRelationDao FeatureRelationDao = new FeatureRelationDao();

	/**
	 * ��ȡ���п��������������ӳ��map
	 * 
	 * @return
	 */
	public HashMap<String, HashMap<String, Integer>> getAllFeatureNum() {
		HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();
		List<String> listById = new ArrayList<String>();
		List<String> allList = new ArrayList<String>();
		allList = FeatureRelationDao.getBlock();// ȡ���п��id
		for (int i = 0; i < allList.size(); i++) {
			String blockid = allList.get(i);
			listById = FeatureRelationDao.getBlockFeatures(blockid);// ����instanceId��ȡ�����������

			HashMap<String, Integer> m = dealForMap(listById);
			map.put(blockid, m);
		}
		return map;

	}

	/**
	 * ����ĳ�����id��ȡ��������������map
	 * 
	 * @return
	 */

	public HashMap<String, Integer> getFeatureNumById(String blockId) {
		List<String> listById = new ArrayList<String>();
		listById = FeatureRelationDao.getBlockFeatures(blockId);// ����instanceId��ȡ�����������
		HashMap<String, Integer> m = dealForMap(listById);
		return m;
	}

	private HashMap<String, Integer> dealForMap(List<String> list) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		for (int j = 0; j < list.size(); j++) {
			String s = list.get(j);
			String[] strarray = s.split("\"");
			String featureType = strarray[5];
			if (featureType.contains("#")) {
				featureType = getInstance(featureType);
			}
			if (m.containsKey(featureType)) {
				int num = m.get(featureType);
				m.put(featureType, num + 1);
			} else
				m.put(featureType, 1);
		}
		return m;
	}

	private String getInstance(String instances) {
		String regex = "[0-9]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(instances);
		int count = 0;
		while (m.find()) {
			count++;
		}

		return instances.substring(1, instances.length() - count);
	}

}
