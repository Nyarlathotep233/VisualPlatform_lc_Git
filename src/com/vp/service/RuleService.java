package com.vp.service;

import java.util.List;

import com.vp.base.Knowledge;
import com.vp.base.Rule;
import com.vp.dao.RuleDAO;

/**
 * 规则相关方法
 * @author admin
 *
 */
public class RuleService {
	
	private static RuleDAO ruleDAO = new RuleDAO();
	/**
	 * 根据规则字符串获取规则对象
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
	 * 添加规则，并返回最新插入的自增id
	 * @param rule
	 * @return
	 */
	public int addRule(String ruleStr){
		Rule rule = getRuleFromRuleStr(ruleStr);
		ruleDAO.addRule(rule);
		return ruleDAO.getLastedId();
	}
	
	/**
	 * 获取所有的规则
	 * @return
	 */
	public List<Rule> getAllRules(){
		return ruleDAO.getAllRules();
	}
	
}
