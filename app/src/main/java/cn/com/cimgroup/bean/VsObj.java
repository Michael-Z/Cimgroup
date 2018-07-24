package cn.com.cimgroup.bean;

import java.io.Serializable;

public class VsObj implements Serializable {
	
	private static final long serialVersionUID = -2216514296262168467L;
	private String matchName;
	private String matchTime;
	private String duizhen;
	private String matchResult;
	
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getDuizhen() {
		return duizhen;
	}
	public void setDuizhen(String duizhen) {
		this.duizhen = duizhen;
	}
	public String getMatchResult() {
		return matchResult;
	}
	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}
}
