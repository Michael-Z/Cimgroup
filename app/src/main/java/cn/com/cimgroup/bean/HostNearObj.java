package cn.com.cimgroup.bean;

import java.io.Serializable;

public class HostNearObj implements Serializable {
	
	private static final long serialVersionUID = 4009424543361018834L;
	private String hostNearMatchName;
	private String hostNearMatchTime;
	private String hostNearDuiZhen;
	private String hostNearMatchResult;
	
	public String getHostNearMatchName() {
		return hostNearMatchName;
	}
	public void setHostNearMatchName(String hostNearMatchName) {
		this.hostNearMatchName = hostNearMatchName;
	}
	public String getHostNearMatchTime() {
		return hostNearMatchTime;
	}
	public void setHostNearMatchTime(String hostNearMatchTime) {
		this.hostNearMatchTime = hostNearMatchTime;
	}
	public String getHostNearDuiZhen() {
		return hostNearDuiZhen;
	}
	public void setHostNearDuiZhen(String hostNearDuiZhen) {
		this.hostNearDuiZhen = hostNearDuiZhen;
	}
	public String getHostNearMatchResult() {
		return hostNearMatchResult;
	}
	public void setHostNearMatchResult(String hostNearMatchResult) {
		this.hostNearMatchResult = hostNearMatchResult;
	}
	
}
