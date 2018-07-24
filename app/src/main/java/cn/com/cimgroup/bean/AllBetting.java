package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 投注记录实体
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月31日
 */
public class AllBetting implements Serializable {
	
	private static final long serialVersionUID = 5943794558676578253L;
	
	/**订单ID*/
	private String orderId;
	/**发起时间*/
	private String createTime;
	/**彩种名称*/
	private String gameName;
	/**彩种编号*/
	private String gameNo;
	/**状态描述*/
	private String statusDes;
	private String pageNo;
	private String total;
	
	//all - start
	private String issueDes;
	private String orderType;
	private String isAward;
	private String betMoney;
	private String awardDes;
	
	//all - end
	
	//dai - start
	//期号
	private String issue;
	//方案金额
	private String money;
	//方案奖金（税前）
	private String winMoney;
	//1-已受理  2-受理失败  3-出票成功  4-已退票  5-未中奖  6-已派奖  7-已中奖未派奖 
	//8-出票成功，扣费失败  9-出票失败，解冻失败 10.自算奖 11.需审核
	private String status;
	
	//dai - end
	
	//zhui - start
	private String chaseAllMoney;
	private String chaseAllTimes;
	private String finishTimes;
	private String cancelTimes;
	private String chaseStatus;
	private String chaseId;
	
	//zhui - end
	
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getStatusDes() {
		return statusDes;
	}
	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
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
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(String winMoney) {
		this.winMoney = winMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getIssueDes() {
		return issueDes;
	}
	public void setIssueDes(String issueDes) {
		this.issueDes = issueDes;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getIsAward() {
		return isAward;
	}
	public void setIsAward(String isAward) {
		this.isAward = isAward;
	}
	public String getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(String betMoney) {
		this.betMoney = betMoney;
	}
	public String getAwardDes() {
		return awardDes;
	}
	public void setAwardDes(String awardDes) {
		this.awardDes = awardDes;
	}
	public String getChaseAllMoney() {
		return chaseAllMoney;
	}
	public void setChaseAllMoney(String chaseAllMoney) {
		this.chaseAllMoney = chaseAllMoney;
	}
	public String getChaseAllTimes() {
		return chaseAllTimes;
	}
	public void setChaseAllTimes(String chaseAllTimes) {
		this.chaseAllTimes = chaseAllTimes;
	}
	public String getFinishTimes() {
		return finishTimes;
	}
	public void setFinishTimes(String finishTimes) {
		this.finishTimes = finishTimes;
	}
	public String getCancelTimes() {
		return cancelTimes;
	}
	public void setCancelTimes(String cancelTimes) {
		this.cancelTimes = cancelTimes;
	}
	public String getChaseStatus() {
		return chaseStatus;
	}
	public void setChaseStatus(String chaseStatus) {
		this.chaseStatus = chaseStatus;
	}
	public String getChaseId() {
		return chaseId;
	}
	public void setChaseId(String chaseId) {
		this.chaseId = chaseId;
	}
	@Override
	public String toString() {
		return "AllBetting [orderId=" + orderId + ", createTime=" + createTime
				+ ", gameName=" + gameName + ", gameNo=" + gameNo
				+ ", statusDes=" + statusDes + ", pageNo=" + pageNo
				+ ", total=" + total + ", issueDes=" + issueDes
				+ ", orderType=" + orderType + ", isAward=" + isAward
				+ ", betMoney=" + betMoney + ", awardDes=" + awardDes
				+ ", issue=" + issue + ", money=" + money + ", winMoney="
				+ winMoney + ", status=" + status + ", chaseAllMoney="
				+ chaseAllMoney + ", chaseAllTimes=" + chaseAllTimes
				+ ", finishTimes=" + finishTimes + ", cancelTimes="
				+ cancelTimes + ", chaseStatus=" + chaseStatus + ", chaseId="
				+ chaseId + "]";
	}
	
	
}
