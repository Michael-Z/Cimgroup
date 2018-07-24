package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class FootballDetailList implements Serializable {
	
	private static final long serialVersionUID = 2496072132127205653L;
	private String createTime;
	private String gameNo;
	private String status;
	private String passType;
	private String issue;
	private String statusDes;
	private String totalPrizeMoney;
	private String gameName;
	private String statusCode;
	private String userId;
	private String resCode;
	private String resMsg;
	private String message;
	private String money;
	private String multiple;
	private String winMoney;
	private String orderId;
	private List<FootballDetail> list;
	
	/**投注注数*/
	private String countNum;
	
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassType() {
		return passType;
	}
	public void setPassType(String passType) {
		this.passType = passType;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getStatusDes() {
		return statusDes;
	}
	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}
	public String getTotalPrizeMoney() {
		return totalPrizeMoney;
	}
	public void setTotalPrizeMoney(String totalPrizeMoney) {
		this.totalPrizeMoney = totalPrizeMoney;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public String getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(String winMoney) {
		this.winMoney = winMoney;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<FootballDetail> getList() {
		return list;
	}
	public void setList(List<FootballDetail> list) {
		this.list = list;
	}
}
