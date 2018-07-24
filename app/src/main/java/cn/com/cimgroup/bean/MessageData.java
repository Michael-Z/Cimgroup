package cn.com.cimgroup.bean;

import java.io.Serializable;

public class MessageData implements Serializable {

	private static final long serialVersionUID = -8801364267202074874L;
	
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
