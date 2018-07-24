package cn.com.cimgroup.bean;

import java.io.Serializable;

public class TheOdds implements Serializable {
	
	private static final long serialVersionUID = -9098903991799470314L;
	private String win;
	private String equals;
	private String lose;
	private String updateTime;
	private String timeDesc;
	public String getWin() {
		return win;
	}
	public void setWin(String win) {
		this.win = win;
	}
	public String getEquals() {
		return equals;
	}
	public void setEquals(String equals) {
		this.equals = equals;
	}
	public String getLose() {
		return lose;
	}
	public void setLose(String lose) {
		this.lose = lose;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getTimeDesc() {
		return timeDesc;
	}
	public void setTimeDesc(String timeDesc) {
		this.timeDesc = timeDesc;
	}
}
