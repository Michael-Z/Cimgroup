package cn.com.cimgroup.bean;

import java.io.Serializable;

public class LotteryDrawInfo implements Serializable {
	
	private static final long serialVersionUID = -1595701391712819026L;
	private String gameNo;
	private String gameName;
	private String issueNo;
	private String awardDate;
	private String winCode;
	private String isJC;
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	public String getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}
	public String getWinCode() {
		return winCode;
	}
	public void setWinCode(String winCode) {
		this.winCode = winCode;
	}
	public String getIsJC() {
		return isJC;
	}
	public void setIsJC(String isJC) {
		this.isJC = isJC;
	}
	
}
