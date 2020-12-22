package com.vp.service;

import java.util.List;

import com.vp.base.Knowledge;
import com.vp.base.Rule;
import com.vp.dao.RuleDAO;

/**
 * ������ط���
 * @author admin
 *
 */
public class RuleService {
	
	private static RuleDAO ruleDAO = new RuleDAO();
	/**
	 * ���ݹ����ַ�����ȡ�������
	 * @param ruleStr
	 * @return
	 */
	public Rule getRuleFromRuleStr(String ruleStr){
		String path = ruleStr.substring(0,ruleStr.indexOf("->"));
		String result = ruleStr.substring(ruleStr.indexOf("->")+2);
		Rule rule = new Rule();
		rule.setPath(path);
		rule.setResult(result);
		return rule;
	}
	
	/**
	 * ��ӹ��򣬲��������²��������id
	 * @param rule
	 * @return
	 */
	public int addRule(String ruleStr){
		Rule rule = getRuleFromRuleStr(ruleStr);
		ruleDAO.addRule(rule);
		return ruleDAO.getLastedId();
	}
	
	/**
	 * ��ȡ���еĹ���
	 * @return
	 */
	public List<Rule> getAllRules(){
		return ruleDAO.getAllRules();
	}
	
}
