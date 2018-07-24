package cn.com.cimgroup.bean;

import java.io.Serializable;

public class GuestNearObj implements Serializable {
	
	private static final long serialVersionUID = -8409359711325311774L;
	private String guestNearMatchName;
	private String guestNearMatchTime;
	private String guestNearDuiZhen;
	private String guestNearMatchResult;
	
	public String getGuestNearMatchName() {
		return guestNearMatchName;
	}
	public void setGuestNearMatchName(String guestNearMatchName) {
		this.guestNearMatchName = guestNearMatchName;
	}
	public String getGuestNearMatchTime() {
		return guestNearMatchTime;
	}
	public void setGuestNearMatchTime(String guestNearMatchTime) {
		this.guestNearMatchTime = guestNearMatchTime;
	}
	public String getGuestNearDuiZhen() {
		return guestNearDuiZhen;
	}
	public void setGuestNearDuiZhen(String guestNearDuiZhen) {
		this.guestNearDuiZhen = guestNearDuiZhen;
	}
	public String getGuestNearMatchResult() {
		return guestNearMatchResult;
	}
	public void setGuestNearMatchResult(String guestNearMatchResult) {
		this.guestNearMatchResult = guestNearMatchResult;
	}
	
}
