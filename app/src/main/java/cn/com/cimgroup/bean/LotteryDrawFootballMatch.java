package cn.com.cimgroup.bean;

import java.io.Serializable;
/**
 * 开奖 竞彩足球比赛
 * @author 秋风
 *
 */
public class LotteryDrawFootballMatch implements Serializable {
	
	private static final long serialVersionUID = -5594065790062129564L;
	// 赛事编号
	private String matchNo;
	// 赛事名称
	private String matchName;
	// 星期
	private String week;
	
	private String leagueShort;
	// 比赛顺序
	private String matchSort;
	// 让球个数
	private String letScore;
	// 主队名称
	private String homeTeamName;
	// 客队名称
	private String guestTeamName;
	// 销售截止时间（竞彩等用）
	private Long matchSellOutTime;
	// 比赛结果
	private String matchResult;
	// 让分胜负结果
	private String rfsfResult;

	private String fullScore;
	
	private String halfScore;

	private String zjqs;

	private String sxds;

	private String bqc;

	public String getHalfScore() {
		return halfScore;
	}

	public void setHalfScore(String halfScore) {
		this.halfScore = halfScore;
	}

	public String getLeagueShort() {
		return leagueShort;
	}

	public void setLeagueShort(String leagueShort) {
		this.leagueShort = leagueShort;
	}

	public String getMatchNo() {
		return matchNo;
	}

	public void setMatchNo(String matchNo) {
		this.matchNo = matchNo;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getMatchSort() {
		return matchSort;
	}

	public void setMatchSort(String matchSort) {
		this.matchSort = matchSort;
	}

	public String getLetScore() {
		return letScore;
	}

	public void setLetScore(String letScore) {
		this.letScore = letScore;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getGuestTeamName() {
		return guestTeamName;
	}

	public void setGuestTeamName(String guestTeamName) {
		this.guestTeamName = guestTeamName;
	}

	public Long getMatchSellOutTime() {
		return matchSellOutTime;
	}

	public void setMatchSellOutTime(Long matchSellOutTime) {
		this.matchSellOutTime = matchSellOutTime;
	}

	public String getMatchResult() {
		return matchResult;
	}

	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}

	public String getRfsfResult() {
		return rfsfResult;
	}

	public void setRfsfResult(String rfsfResult) {
		this.rfsfResult = rfsfResult;
	}

	public String getFullScore() {
		return fullScore;
	}

	public void setFullScore(String fullScore) {
		this.fullScore = fullScore;
	}

	public String getZjqs() {
		return zjqs;
	}

	public void setZjqs(String zjqs) {
		this.zjqs = zjqs;
	}

	public String getSxds() {
		return sxds;
	}

	public void setSxds(String sxds) {
		this.sxds = sxds;
	}

	public String getBqc() {
		return bqc;
	}

	public void setBqc(String bqc) {
		this.bqc = bqc;
	}
	
}
