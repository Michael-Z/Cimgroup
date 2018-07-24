package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ZhuiDetail implements Serializable {
	
	private static final long serialVersionUID = -1493996555087864987L;
	private String chaseIssueId;
	private String orderId;
	private String money;
	private String multiBetCnt;
	private String winStatusDes;
	private String taxedWinMoney;
	private String issueNo;
	private String orderStatusDes;
	
	public String getChaseIssueId() {
		return chaseIssueId;
	}
	public void setChaseIssueId(String chaseIssueId) {
		this.chaseIssueId = chaseIssueId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getMultiBetCnt() {
		return multiBetCnt;
	}
	public void setMultiBetCnt(String multiBetCnt) {
		this.multiBetCnt = multiBetCnt;
	}
	public String getWinStatusDes() {
		return winStatusDes;
	}
	public void setWinStatusDes(String winStatusDes) {
		this.winStatusDes = winStatusDes;
	}
	public String getTaxedWinMoney() {
		return taxedWinMoney;
	}
	public void setTaxedWinMoney(String taxedWinMoney) {
		this.taxedWinMoney = taxedWinMoney;
	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	public String getOrderStatusDes() {
		return orderStatusDes;
	}
	public void setOrderStatusDes(String orderStatusDes) {
		this.orderStatusDes = orderStatusDes;
	}
}
