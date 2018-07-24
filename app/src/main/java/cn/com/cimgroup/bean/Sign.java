package cn.com.cimgroup.bean;

import java.io.Serializable;

public class Sign implements Serializable {
	
	private static final long serialVersionUID = -1551226985042529231L;
	private String day;
	private String resCode;
	private boolean isSelect;
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
}
