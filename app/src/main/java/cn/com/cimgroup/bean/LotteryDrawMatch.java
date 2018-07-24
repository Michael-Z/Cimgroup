package cn.com.cimgroup.bean;

import java.io.Serializable;

public class LotteryDrawMatch implements Serializable {
	
	private static final long serialVersionUID = 6268113459635716613L;
	private String matchNum;
	private String hostVsAgent;
	private String 	matchResult;
	public String getMatchNum() {
		return matchNum;
	}
	public void setMatchNum(String matchNum) {
		this.matchNum = matchNum;
	}
	public String getHostVsAgent() {
		return hostVsAgent;
	}
	public void setHostVsAgent(String hostVsAgent) {
		this.hostVsAgent = hostVsAgent;
	}
	public String getMatchResult() {
		return matchResult;
	}
	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}
}
