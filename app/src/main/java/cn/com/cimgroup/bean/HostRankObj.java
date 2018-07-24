package cn.com.cimgroup.bean;

import java.io.Serializable;

public class HostRankObj implements Serializable {
	
	private static final long serialVersionUID = 3686506961705868650L;
	private String hostRankResult;
	private String hostAllResult;
	private String hostHResult;
	private String hostGResult;
	
	public String getHostRankResult() {
		return hostRankResult;
	}
	public void setHostRankResult(String hostRankResult) {
		this.hostRankResult = hostRankResult;
	}
	public String getHostAllResult() {
		return hostAllResult;
	}
	public void setHostAllResult(String hostAllResult) {
		this.hostAllResult = hostAllResult;
	}
	public String getHostHResult() {
		return hostHResult;
	}
	public void setHostHResult(String hostHResult) {
		this.hostHResult = hostHResult;
	}
	public String getHostGResult() {
		return hostGResult;
	}
	public void setHostGResult(String hostGResult) {
		this.hostGResult = hostGResult;
	}
}
