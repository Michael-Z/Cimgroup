package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class TzDetail implements Serializable {
	
	private static final long serialVersionUID = -7445655693843080635L;
	private String resCode;
	private String resMsg;
	private String awardCode;
	private String multiple;
	private Long createTime;
	private String issue;
	private String statusCode;
	private String winMoney;
	private String betMoney;
	private String redMoney;
	private String money;
	private String gameNo;
	private String orderId;
	private String status;
	private String statusDes;
	private String gameName;
	/**投注注数*/
	private String countNum;
	
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
	List<PlanContent> list;
	
	public String getStatusDes() {
		return statusDes;
	}
	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(String winMoney) {
		this.winMoney = winMoney;
	}
	public String getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(String betMoney) {
		this.betMoney = betMoney;
	}
	public String getRedMoney() {
		return redMoney;
	}
	public void setRedMoney(String redMoney) {
		this.redMoney = redMoney;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public String getAwardCode() {
		return awardCode;
	}
	public void setAwardCode(String awardCode) {
		this.awardCode = awardCode;
	}
	public List<PlanContent> getList() {
		return list;
	}
	public void setList(List<PlanContent> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "TzDetail [resCode=" + resCode + ", resMsg=" + resMsg
				+ ", awardCode=" + awardCode + ", multiple=" + multiple
				+ ", createTime=" + createTime + ", issue=" + issue
				+ ", statusCode=" + statusCode + ", winMoney=" + winMoney
				+ ", betMoney=" + betMoney + ", redMoney=" + redMoney
				+ ", money=" + money + ", gameNo=" + gameNo + ", orderId="
				+ orderId + ", status=" + status + ", statusDes=" + statusDes
				+ ", gameName=" + gameName + ", list=" + list + "]";
	}
	
	
}
