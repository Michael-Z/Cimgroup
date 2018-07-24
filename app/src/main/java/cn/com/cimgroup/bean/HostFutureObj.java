package cn.com.cimgroup.bean;

import java.io.Serializable;

public class HostFutureObj implements Serializable {
	
	private static final long serialVersionUID = 8295583454464959681L;
	private String hostFutureName;
	private String hostFutureTime;
	private String hostFutureDZ;
	private String hostFutureJG;
	
	public String getHostFutureName() {
		return hostFutureName;
	}
	public void setHostFutureName(String hostFutureName) {
		this.hostFutureName = hostFutureName;
	}
	public String getHostFutureTime() {
		return hostFutureTime;
	}
	public void setHostFutureTime(String hostFutureTime) {
		this.hostFutureTime = hostFutureTime;
	}
	public String getHostFutureDZ() {
		return hostFutureDZ;
	}
	public void setHostFutureDZ(String hostFutureDZ) {
		this.hostFutureDZ = hostFutureDZ;
	}
	public String getHostFutureJG() {
		return hostFutureJG;
	}
	public void setHostFutureJG(String hostFutureJG) {
		this.hostFutureJG = hostFutureJG;
	}
}
