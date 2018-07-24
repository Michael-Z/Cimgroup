package cn.com.cimgroup.bean;

import java.io.Serializable;
/**
 * 竞猜游戏比赛问题信息
 * @author 秋风
 *
 */
public class QuestionInfo implements Serializable{

	private static final long serialVersionUID = 821181365942557099L;

	
	/**题目ID*/
	private String questionId;
	/**题目内容*/
	private String question;
	/**肯定奖池*/
	private String definitePool;
	/**否定奖池*/
	private String negativePool;
	/**肯定人数*/
	private String definiteUsers;
	/**否定人数*/
	private String negativeUsers;
	/**总人数*/
	private String totalUsers;
	/**我的已选方案【追投只能投已选方案】*/
	private String theChoice;
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getDefinitePool() {
		return definitePool;
	}
	public void setDefinitePool(String definitePool) {
		this.definitePool = definitePool;
	}
	public String getNegativePool() {
		return negativePool;
	}
	public void setNegativePool(String negativePool) {
		this.negativePool = negativePool;
	}
	public String getDefiniteUsers() {
		return definiteUsers;
	}
	public void setDefiniteUsers(String definiteUsers) {
		this.definiteUsers = definiteUsers;
	}
	public String getNegativeUsers() {
		return negativeUsers;
	}
	public void setNegativeUsers(String negativeUsers) {
		this.negativeUsers = negativeUsers;
	}
	public String getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(String totalUsers) {
		this.totalUsers = totalUsers;
	}
	public String getTheChoice() {
		return theChoice;
	}
	public void setTheChoice(String theChoice) {
		this.theChoice = theChoice;
	}
	
	
}
