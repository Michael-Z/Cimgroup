package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ZhuiDetailList implements Serializable {

	private static final long serialVersionUID = 3239311909314649900L;
	private String resCode;
	private String resMsg;
	private String pageNo;
	/**总记录数*/
	private String total;
	/**追号已完成期数*/
	private String finishTimes;
	/**追号总金额*/
	private String chaseAllMoney;
	/**追号总期数*/
	private String chaseAllTimes;
	/**彩种编号*/
	private String gameNo;
	/**追号状态（描述）：追号中、追停结束、追号结束，未返款*/
	private String chaseStatusDes;
	/**彩种编号名称*/
	private String gameName;
	/**追号状态：1、追号中，2：追停结束，3、追号结束，未返款*/
	private String chaseStatus;
	/**撤销期数*/
	private String cancelTimes;
	/**彩种的开奖号码*/
	private String awardCode;
	/**追号剩余金额*/
	private String chaseLavelMoney;
	/**累计税后奖金*/
	private String taxedWinMoney;
	/**追停条件：中奖不追停；中奖追停*/
	private String stopCondition;
	/**投注时间*/
	private String betTime;
	
	/**投注注数*/
	private String countNum;
	
	
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
	private List<ZhuiDetail> issueList;
	
	private List<ZhuiContent> contentList;
	
	public String getBetTime() {
		return betTime;
	}
	public void setBetTime(String betTime) {
		this.betTime = betTime;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getFinishTimes() {
		return finishTimes;
	}
	public void setFinishTimes(String finishTimes) {
		this.finishTimes = finishTimes;
	}
	public String getChaseAllMoney() {
		return chaseAllMoney;
	}
	public void setChaseAllMoney(String chaseAllMoney) {
		this.chaseAllMoney = chaseAllMoney;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getChaseAllTimes() {
		return chaseAllTimes;
	}
	public void setChaseAllTimes(String chaseAllTimes) {
		this.chaseAllTimes = chaseAllTimes;
	}
	public String getChaseStatusDes() {
		return chaseStatusDes;
	}
	public void setChaseStatusDes(String chaseStatusDes) {
		this.chaseStatusDes = chaseStatusDes;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getChaseStatus() {
		return chaseStatus;
	}
	public void setChaseStatus(String chaseStatus) {
		this.chaseStatus = chaseStatus;
	}
	public String getCancelTimes() {
		return cancelTimes;
	}
	public void setCancelTimes(String cancelTimes) {
		this.cancelTimes = cancelTimes;
	}
	public String getAwardCode() {
		return awardCode;
	}
	public void setAwardCode(String awardCode) {
		this.awardCode = awardCode;
	}
	public String getChaseLavelMoney() {
		return chaseLavelMoney;
	}
	public void setChaseLavelMoney(String chaseLavelMoney) {
		this.chaseLavelMoney = chaseLavelMoney;
	}
	public String getTaxedWinMoney() {
		return taxedWinMoney;
	}
	public void setTaxedWinMoney(String taxedWinMoney) {
		this.taxedWinMoney = taxedWinMoney;
	}
	public String getStopCondition() {
		return stopCondition;
	}
	public void setStopCondition(String stopCondition) {
		this.stopCondition = stopCondition;
	}
	public List<ZhuiDetail> getIssueList() {
		return issueList;
	}
	public void setIssueList(List<ZhuiDetail> issueList) {
		this.issueList = issueList;
	}
	public List<ZhuiContent> getContentList() {
		return contentList;
	}
	public void setContentList(List<ZhuiContent> contentList) {
		this.contentList = contentList;
	}
	
}
