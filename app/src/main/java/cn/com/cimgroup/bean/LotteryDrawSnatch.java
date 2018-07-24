package cn.com.cimgroup.bean;

import java.io.Serializable;

public class LotteryDrawSnatch implements Serializable {
	
	private static final long serialVersionUID = 3152815485821273843L;
	private String snatchId;
	private String lotteryId;
	private String issue;
	private String awardLevel;
	private String awardLevelMsg;
	private String playMethod;
	private String awardNum;
	private String singleAwardMoney;
	private String allAward;
	private long addTime;
	private long editTime;
	public String getSnatchId() {
		return snatchId;
	}
	public void setSnatchId(String snatchId) {
		this.snatchId = snatchId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getAwardLevel() {
		return awardLevel;
	}
	public void setAwardLevel(String awardLevel) {
		this.awardLevel = awardLevel;
	}
	public String getAwardLevelMsg() {
		return awardLevelMsg;
	}
	public void setAwardLevelMsg(String awardLevelMsg) {
		this.awardLevelMsg = awardLevelMsg;
	}
	public String getPlayMethod() {
		return playMethod;
	}
	public void setPlayMethod(String playMethod) {
		this.playMethod = playMethod;
	}
	public String getAwardNum() {
		return awardNum;
	}
	public void setAwardNum(String awardNum) {
		this.awardNum = awardNum;
	}
	public String getSingleAwardMoney() {
		return singleAwardMoney;
	}
	public void setSingleAwardMoney(String singleAwardMoney) {
		this.singleAwardMoney = singleAwardMoney;
	}
	public String getAllAward() {
		return allAward;
	}
	public void setAllAward(String allAward) {
		this.allAward = allAward;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public long getEditTime() {
		return editTime;
	}
	public void setEditTime(long editTime) {
		this.editTime = editTime;
	}
}
