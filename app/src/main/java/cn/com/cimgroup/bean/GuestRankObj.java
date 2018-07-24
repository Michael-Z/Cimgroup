package cn.com.cimgroup.bean;

import java.io.Serializable;

public class GuestRankObj implements Serializable {
	
	private static final long serialVersionUID = 7363564611441488759L;
	private String guestRankResult;
	private String guestAllResult;
	private String guestHResult;
	private String guestGResult;
	
	public String getGuestRankResult() {
		return guestRankResult;
	}
	public void setGuestRankResult(String guestRankResult) {
		this.guestRankResult = guestRankResult;
	}
	public String getGuestAllResult() {
		return guestAllResult;
	}
	public void setGuestAllResult(String guestAllResult) {
		this.guestAllResult = guestAllResult;
	}
	public String getGuestHResult() {
		return guestHResult;
	}
	public void setGuestHResult(String guestHResult) {
		this.guestHResult = guestHResult;
	}
	public String getGuestGResult() {
		return guestGResult;
	}
	public void setGuestGResult(String guestGResult) {
		this.guestGResult = guestGResult;
	}
}
