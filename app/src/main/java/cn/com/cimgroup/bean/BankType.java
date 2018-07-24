package cn.com.cimgroup.bean;

import java.io.Serializable;

public class BankType implements Serializable {
	
	private static final long serialVersionUID = -2995875176708551041L;
	private String resCode;
	private String message;
	private String cardNum;
//	card类型：1，借记卡；2，信用卡
	private String cardType;
	private String bankCode;
	private String bankName;
	
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
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
