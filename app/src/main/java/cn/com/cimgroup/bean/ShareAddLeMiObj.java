package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ShareAddLeMiObj implements Serializable{
	
	private static final long serialVersionUID = -6904848255072064979L;
	private String addIntegral;
	private String resCode;
	private String resMsg;
	public String getAddIntegral() {
		return addIntegral;
	}
	public void setAddIntegral(String addIntegral) {
		this.addIntegral = addIntegral;
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
}
