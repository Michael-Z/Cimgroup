package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Betting implements Serializable {

	private static final long serialVersionUID = -1046708439532513273L;
	
	private String resCode;
	private String resMsg;
	private String userId;
	private String betAcnt;
	private String prizeAcnt;
	private String integralAcnt;
	private String bettingType;
	private String gameNo;
	private String planId;
	private String planNo;
	private String chaseId;
	private String mobile;
	private String isChase;
	
	//关于红包新添加的字段;

	public String redPkgNum;                //红包剩余总量;
	public String redPkgAccount;            //红包剩余总额;

	public String buyRedMoney;              //购买红包金额
	public String convertRedNum;            //兑换红包个数;
	public String convertRedTotalMoney;       //兑换红包金额;
	public HashMap<String ,List<TcjczqObj>> convertRedList;             //兑换红包列表
	public String giveRedNum;               //赠送红包个数;
	public String giveRedTotalMoney;        //赠送红包金额;
	public HashMap<String ,List<TcjczqObj>> giveRedList;                //赠送红包
	/**微信支付--prepayId*/
	public String prepayId;
	/**微信支付--签名*/
	public String sign ;
	/**微信支付--appId*/
	public String appId;
	/**微信支付--时间戳*/
	public String timestamp;
	/**微信支付--随机串*/
	public String nonceStr;
	/**微信支付--partnerId*/
	public String partnerId;
	/**微信支付 attach*/
	public String attach;
	
	
	
	
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBetAcnt() {
		return betAcnt;
	}
	public void setBetAcnt(String betAcnt) {
		this.betAcnt = betAcnt;
	}
	public String getPrizeAcnt() {
		return prizeAcnt;
	}
	public void setPrizeAcnt(String prizeAcnt) {
		this.prizeAcnt = prizeAcnt;
	}
	public String getIntegralAcnt() {
		return integralAcnt;
	}
	public void setIntegralAcnt(String integralAcnt) {
		this.integralAcnt = integralAcnt;
	}
	public String getBettingType() {
		return bettingType;
	}
	public void setBettingType(String bettingType) {
		this.bettingType = bettingType;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanNo() {
		return planNo;
	}
	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}
	public String getChaseId() {
		return chaseId;
	}
	public void setChaseId(String chaseId) {
		this.chaseId = chaseId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIsChase() {
		return isChase;
	}
	public void setIsChase(String isChase) {
		this.isChase = isChase;
	}

}
