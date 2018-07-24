package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class LotteryDrawBasketballMatch implements Serializable {
	
	private static final long serialVersionUID = -7082969394989364680L;
	
	private String matchNo;
	private String matchName;
	private String leagueShort;
	private String gameNo;
	private String issueNo;
	private String week;
	private String matchSort;
	private String homeTeamId;
	private String homeTeamName;
	private String guestTeamId;
	private String guestTeamName;
	private long matchSellOutTime;
	private long matchTimeDate;
	private String matchStatus;
	private String fullScore;
	private String letScore;
	private List<MatchAgainstSpInfo> matchAgainstSpInfoList;
	
	public List<MatchAgainstSpInfo> getMatchAgainstSpInfoList() {
		return matchAgainstSpInfoList;
	}
	public void setMatchAgainstSpInfoList(
			List<MatchAgainstSpInfo> matchAgainstSpInfoList) {
		this.matchAgainstSpInfoList = matchAgainstSpInfoList;
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
	public String getLeagueShort() {
		return leagueShort;
	}
	public void setLeagueShort(String leagueShort) {
		this.leagueShort = leagueShort;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
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
	public String getHomeTeamId() {
		return homeTeamId;
	}
	public void setHomeTeamId(String homeTeamId) {
		this.homeTeamId = homeTeamId;
	}
	public String getHomeTeamName() {
		return homeTeamName;
	}
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	public String getGuestTeamId() {
		return guestTeamId;
	}
	public void setGuestTeamId(String guestTeamId) {
		this.guestTeamId = guestTeamId;
	}
	public String getGuestTeamName() {
		return guestTeamName;
	}
	public void setGuestTeamName(String guestTeamName) {
		this.guestTeamName = guestTeamName;
	}
	public long getMatchSellOutTime() {
		return matchSellOutTime;
	}
	public void setMatchSellOutTime(long matchSellOutTime) {
		this.matchSellOutTime = matchSellOutTime;
	}
	public long getMatchTimeDate() {
		return matchTimeDate;
	}
	public void setMatchTimeDate(long matchTimeDate) {
		this.matchTimeDate = matchTimeDate;
	}
	public String getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}
	public String getFullScore() {
		return fullScore;
	}
	public void setFullScore(String fullScore) {
		this.fullScore = fullScore;
	}
	public String getLetScore() {
		return letScore;
	}
	public void setLetScore(String letScore) {
		this.letScore = letScore;
	}
//	public List<MatchAgainstSpInfo> getMatchAgainstSpInfoList() {
//		return matchAgainstSpInfoList;
//	}
//	public void setMatchAgainstSpInfoList(
//			List<MatchAgainstSpInfo> matchAgainstSpInfoList) {
//		this.matchAgainstSpInfoList = matchAgainstSpInfoList;
//	}
	
}
