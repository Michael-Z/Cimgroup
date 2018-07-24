package cn.com.cimgroup.bean;

import java.io.Serializable;

public class FocusMatch implements Serializable {

	private static final long serialVersionUID = -4602563759792176461L;
	
	private String resCode;
	private String resMsg;
	private Match match;
	private MatchAgainstSpInfo spInfo;
	
	//赛事编号
	private String matchNoMinSp;
	//赛事名称
	private String matchNameMinSp;
	//星期
	private String weekMinSp;
	//比赛顺序
	private String matchSortMinSp;
	//联赛简称
	private String leagueShortMinSp;
	//彩种编号
	private String gameIdMinSp;
	//期次编号
	private String issueNoMinSp;
	//主队ID
	private String homeTeamIdMinSp;
	//主队名称
	private String homeTeamNameMinSp;
	//客队ID
	private String guestTeamIdMinSp;
	//客队名称
	private String guestTeamNameMinSp;
	//销售截止时间（竞彩等用）
	private Long matchSellOutTimeMinSp;
	private String playMethodMinSp;
	private String realTimeSpMinSp;
	private String passTypeMinSp;
	private String updateTimeMinSp;
	
	
	public String getMatchNoMinSp() {
		return matchNoMinSp;
	}
	
	public void setMatchNoMinSp(String matchNoMinSp) {
		this.matchNoMinSp = matchNoMinSp;
	}
	
	public String getMatchNameMinSp() {
		return matchNameMinSp;
	}
	
	public void setMatchNameMinSp(String matchNameMinSp) {
		this.matchNameMinSp = matchNameMinSp;
	}
	
	public String getWeekMinSp() {
		return weekMinSp;
	}
	
	public void setWeekMinSp(String weekMinSp) {
		this.weekMinSp = weekMinSp;
	}
	
	public String getMatchSortMinSp() {
		return matchSortMinSp;
	}
	
	public void setMatchSortMinSp(String matchSortMinSp) {
		this.matchSortMinSp = matchSortMinSp;
	}
	
	public String getLeagueShortMinSp() {
		return leagueShortMinSp;
	}
	
	public void setLeagueShortMinSp(String leagueShortMinSp) {
		this.leagueShortMinSp = leagueShortMinSp;
	}
	
	public String getGameIdMinSp() {
		return gameIdMinSp;
	}
	
	public void setGameIdMinSp(String gameIdMinSp) {
		this.gameIdMinSp = gameIdMinSp;
	}
	
	public String getIssueNoMinSp() {
		return issueNoMinSp;
	}
	
	public void setIssueNoMinSp(String issueNoMinSp) {
		this.issueNoMinSp = issueNoMinSp;
	}
	
	public String getHomeTeamIdMinSp() {
		return homeTeamIdMinSp;
	}
	
	public void setHomeTeamIdMinSp(String homeTeamIdMinSp) {
		this.homeTeamIdMinSp = homeTeamIdMinSp;
	}
	
	public String getHomeTeamNameMinSp() {
		return homeTeamNameMinSp;
	}
	
	public void setHomeTeamNameMinSp(String homeTeamNameMinSp) {
		this.homeTeamNameMinSp = homeTeamNameMinSp;
	}
	
	public String getGuestTeamIdMinSp() {
		return guestTeamIdMinSp;
	}
	
	public void setGuestTeamIdMinSp(String guestTeamIdMinSp) {
		this.guestTeamIdMinSp = guestTeamIdMinSp;
	}
	
	public String getGuestTeamNameMinSp() {
		return guestTeamNameMinSp;
	}
	
	public void setGuestTeamNameMinSp(String guestTeamNameMinSp) {
		this.guestTeamNameMinSp = guestTeamNameMinSp;
	}
	
	public Long getMatchSellOutTimeMinSp() {
		return matchSellOutTimeMinSp;
	}
	
	public void setMatchSellOutTimeMinSp(Long matchSellOutTimeMinSp) {
		this.matchSellOutTimeMinSp = matchSellOutTimeMinSp;
	}
	
	public String getPlayMethodMinSp() {
		return playMethodMinSp;
	}
	
	public void setPlayMethodMinSp(String playMethodMinSp) {
		this.playMethodMinSp = playMethodMinSp;
	}
	
	public String getRealTimeSpMinSp() {
		return realTimeSpMinSp;
	}
	
	public void setRealTimeSpMinSp(String realTimeSpMinSp) {
		this.realTimeSpMinSp = realTimeSpMinSp;
	}
	
	public String getPassTypeMinSp() {
		return passTypeMinSp;
	}
	
	public void setPassTypeMinSp(String passTypeMinSp) {
		this.passTypeMinSp = passTypeMinSp;
	}
	
	public String getUpdateTimeMinSp() {
		return updateTimeMinSp;
	}
	
	public void setUpdateTimeMinSp(String updateTimeMinSp) {
		this.updateTimeMinSp = updateTimeMinSp;
	}
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
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	public MatchAgainstSpInfo getSpInfo() {
		return spInfo;
	}
	public void setSpInfo(MatchAgainstSpInfo spInfo) {
		this.spInfo = spInfo;
	}

}
