package cn.com.cimgroup.bean;

import java.io.Serializable;


public class ResMatchAgainstInfo implements Serializable {
	
	private static final long serialVersionUID = -8684577547883823212L;
	private String resCode;
	private String resMsg;
	private Match ResMatchAgainstInfo;
	
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

	
	public Match getResMatchAgainstInfo() {
		return ResMatchAgainstInfo;
	}

	
	public void setResMatchAgainstInfo(Match resMatchAgainstInfo) {
		ResMatchAgainstInfo = resMatchAgainstInfo;
	}
	
}
