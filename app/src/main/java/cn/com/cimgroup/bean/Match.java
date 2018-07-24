package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class Match implements Serializable {

	private static final long serialVersionUID = 8140876593224775488L;
	
	//赛事编号
	private String matchNo;
	//赛事名称
	private String matchName;
	
	private String gameNo;
	//星期
	private String week;
	//比赛顺序
	private String matchSort;
	//联赛简称
	private String leagueShort;
	//彩种编号
	private String gameId;
	//期次编号
	private String issueNo;
	//让球个数
	private String letScore;
	//主队ID
	private String homeTeamId;
	//主队名称
	private String homeTeamName;
	//客队ID
	private String guestTeamId;
	//客队名称
	private String guestTeamName;
	//赛事时间(老足彩用)
	private Long matchTimeDate;
	//销售截止时间（竞彩等用）
	private Long matchSellOutTime;
	//赛事状态
	private String matchStatus;
	//比赛结果
	private String matchResult;
	//让分胜负结果
	private String rfsfResult;
	//胜负结果
	private String sfResult;
	//大小结果
	private String dxResult;
	//分差结果
	private String fcResult;
	//全场比分
	private String fullScore;
	//半场比分
	private String halfScore;
	//让分盘口
	private String goal;
	//总分盘口
	private String totalGoal;
	/**比赛编号*/
	private String leagueCode;
	//玩法对应SP值
	private List<MatchAgainstSpInfo> matchAgainstSpInfoList;
	
	private int position;
	
	private String avgOdds;
	/**两队对阵信息*/
	private AgainstDetails againstDetails;
	
	public AgainstDetails getAgainstDetails() {
		return againstDetails;
	}
	public void setAgainstDetails(AgainstDetails againstDetails) {
		this.againstDetails = againstDetails;
	}
	/**
	 * 是否显示详细信息
	 */
	private boolean isShow=false;
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	public String getAvgOdds() {
		return avgOdds;
	}
	public void setAvgOdds(String avgOdds) {
		this.avgOdds = avgOdds;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getLetScore() {
		return letScore;
	}
	public void setLetScore(String letScore) {
		this.letScore = letScore;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
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
	public String getLeagueShort() {
		return leagueShort;
	}
	public void setLeagueShort(String leagueShort) {
		this.leagueShort = leagueShort;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
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
	public String getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
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
	public String getSfResult() {
		return sfResult;
	}
	public void setSfResult(String sfResult) {
		this.sfResult = sfResult;
	}
	public String getDxResult() {
		return dxResult;
	}
	public void setDxResult(String dxResult) {
		this.dxResult = dxResult;
	}
	public String getFcResult() {
		return fcResult;
	}
	public void setFcResult(String fcResult) {
		this.fcResult = fcResult;
	}
	public String getFullScore() {
		return fullScore;
	}
	public void setFullScore(String fullScore) {
		this.fullScore = fullScore;
	}
	public String getHalfScore() {
		return halfScore;
	}
	public void setHalfScore(String halfScore) {
		this.halfScore = halfScore;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getTotalGoal() {
		return totalGoal;
	}
	public void setTotalGoal(String totalGoal) {
		this.totalGoal = totalGoal;
	}
	public Long getMatchTimeDate() {
		return matchTimeDate;
	}
	public void setMatchTimeDate(Long matchTimeDate) {
		this.matchTimeDate = matchTimeDate;
	}
	public Long getMatchSellOutTime() {
		return matchSellOutTime;
	}
	public void setMatchSellOutTime(Long matchSellOutTime) {
		this.matchSellOutTime = matchSellOutTime;
	}
	public List<MatchAgainstSpInfo> getMatchAgainstSpInfoList() {
		return matchAgainstSpInfoList;
	}
	public void setMatchAgainstSpInfoList(
			List<MatchAgainstSpInfo> matchAgainstSpInfoList) {
		this.matchAgainstSpInfoList = matchAgainstSpInfoList;
	}
	public String getLeagueCode() {
		return leagueCode;
	}
	public void setLeagueCode(String leagueCode) {
		this.leagueCode = leagueCode;
	} 
	
	
}
