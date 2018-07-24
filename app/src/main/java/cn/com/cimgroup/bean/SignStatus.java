package cn.com.cimgroup.bean;

import java.io.Serializable;

public class SignStatus implements Serializable {
	
	private static final long serialVersionUID = -2217386044485310953L;
	private String resCode;
	private String resMsg;
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
	
}
