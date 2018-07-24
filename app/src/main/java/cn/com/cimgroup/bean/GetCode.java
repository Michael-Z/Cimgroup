package cn.com.cimgroup.bean;

import java.io.Serializable;

public class GetCode implements Serializable {
	
	private static final long serialVersionUID = 3458976435229214090L;
	
	private String checkCode;
	private String userId;
	private String resCode;
	private String resMsg;
	
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
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
