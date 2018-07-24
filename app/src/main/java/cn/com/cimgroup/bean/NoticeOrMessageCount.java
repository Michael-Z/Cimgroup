package cn.com.cimgroup.bean;

import java.io.Serializable;

public class NoticeOrMessageCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8255528155703711714L;
	
	private String userIdString;
	private String number;
	private String resCode;
	private String resMsg;
	private String isNotMsg;
	private String isNotActivityMsg;
	
	public String getUserIdString() {
		return userIdString;
	}
	public void setUserIdString(String userIdString) {
		this.userIdString = userIdString;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
	public String getIsNotMsg() {
		return isNotMsg;
	}
	public void setIsNotMsg(String isNotMsg) {
		this.isNotMsg = isNotMsg;
	}
	public String getIsNotActivityMsg() {
		return isNotActivityMsg;
	}
	public void setIsNotActivityMsg(String isNotActivityMsg) {
		this.isNotActivityMsg = isNotActivityMsg;
	}
}
