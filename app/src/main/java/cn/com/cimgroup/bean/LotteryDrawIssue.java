package cn.com.cimgroup.bean;

import java.io.Serializable;

public class LotteryDrawIssue implements Serializable {
	
	private static final long serialVersionUID = -2701776237753242249L;
	private String gameNo;
	private String issue;
	private String date;
	private String winCode;
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWinCode() {
		return winCode;
	}
	public void setWinCode(String winCode) {
		this.winCode = winCode;
	}
	
}
