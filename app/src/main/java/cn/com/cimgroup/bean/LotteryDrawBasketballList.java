package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class LotteryDrawBasketballList implements Serializable {
	
	private static final long serialVersionUID = -6058648951414143963L;
	
	private String resCode;
	private String resMsg;
	private List<LotteryDrawBasketballMatch> matchList;
	private List<MatchAgainstSpInfo> matchAgainstSpInfoList;

	
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public List<LotteryDrawBasketballMatch> getMatchList() {
		return matchList;
	}
	public void setMatchList(List<LotteryDrawBasketballMatch> matchList) {
		this.matchList = matchList;
	}
	public List<MatchAgainstSpInfo> getMatchAgainstSpInfoList() {
		return matchAgainstSpInfoList;
	}
	public void setMatchAgainstSpInfoList(
			List<MatchAgainstSpInfo> matchAgainstSpInfoList) {
		this.matchAgainstSpInfoList = matchAgainstSpInfoList;
	}
	
	
	
	
}
