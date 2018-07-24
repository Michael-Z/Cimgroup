package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 账户信息
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-2-27
 */
public class AccountDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**添加时间（long）*/
	private String createTime;
	/**添加时间（String）*/
	private String createTimeSys;
	private String oprType;
	//类型
	private String typeDes;
	//发生金额
	private String money;
	//进账/入账: + / -
	private String inOut;
	//余额
	private String balance;
	//彩种名称
	private String gameNo;
	//明细描述
	private String detailMsg;
	
	
	// 充值/提现金额
	private String amount;
	// 充值/提现说明
	private String description;
	// 充值/提现状态
	private String status;
	// 充值/提现操作时间
	private long addTime;
	
	private String resMsg;
	
	
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getOprType() {
		return oprType;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setOprType(String oprType) {
		this.oprType = oprType;
	}
	public String getTypeDes() {
		return typeDes;
	}
	public void setTypeDes(String typeDes) {
		this.typeDes = typeDes;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getInOut() {
		return inOut;
	}
	public void setInOut(String inOut) {
		this.inOut = inOut;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getDetailMsg() {
		return detailMsg;
	}
	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
	
	
	
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public String getCreateTimeSys() {
		return createTimeSys;
	}
	public void setCreateTimeSys(String createTimeSys) {
		this.createTimeSys = createTimeSys;
	}
	
	
}
