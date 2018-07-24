package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class LotteryDrawFootballList implements Serializable {

	private static final long serialVersionUID = 6073390269097908537L;

	private String resCode;
	
	private String resMsg;
	
	private List<LotteryDrawFootballMatch> matchList;

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

	public List<LotteryDrawFootballMatch> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<LotteryDrawFootballMatch> matchList) {
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
