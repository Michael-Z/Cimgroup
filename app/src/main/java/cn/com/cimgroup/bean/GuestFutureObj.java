package cn.com.cimgroup.bean;

import java.io.Serializable;

public class GuestFutureObj implements Serializable {
	
	private static final long serialVersionUID = -4427538366997714340L;
	private String guestFutureName;
	private String guestFutureTime;
	private String guestFutureDZ;
	private String guestFutureJG;
	
	public String getGuestFutureName() {
		return guestFutureName;
	}
	public void setGuestFutureName(String guestFutureName) {
		this.guestFutureName = guestFutureName;
	}
	public String getGuestFutureTime() {
		return guestFutureTime;
	}
	public void setGuestFutureTime(String guestFutureTime) {
		this.guestFutureTime = guestFutureTime;
	}
	public String getGuestFutureDZ() {
		return guestFutureDZ;
	}
	public void setGuestFutureDZ(String guestFutureDZ) {
		this.guestFutureDZ = guestFutureDZ;
	}
	public String getGuestFutureJG() {
		return guestFutureJG;
	}
	public void setGuestFutureJG(String guestFutureJG) {
		this.guestFutureJG = guestFutureJG;
	}
}
