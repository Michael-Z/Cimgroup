package cn.com.cimgroup.bean;

import java.io.Serializable;

public class FootballDetail implements Serializable {
	
	private static final long serialVersionUID = 2732589712839276729L;
	private String matchCode;
	private String levelName;
	private String matchTime;
	private String homeTeam;
	private String guestTeam;
	private String playMethod;
	private String haifScore;
	private String fullScore;
	private String result;
	private String betItem;
	private String isDT;
	
	public String getMatchCode() {
		return matchCode;
	}
	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getGuestTeam() {
		return guestTeam;
	}
	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}
	public String getPlayMethod() {
		return playMethod;
	}
	public void setPlayMethod(String playMethod) {
		this.playMethod = playMethod;
	}
	public String getHaifScore() {
		return haifScore;
	}
	public void setHaifScore(String haifScore) {
		this.haifScore = haifScore;
	}
	public String getFullScore() {
		return fullScore;
	}
	public void setFullScore(String fullScore) {
		this.fullScore = fullScore;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getBetItem() {
		return betItem;
	}
	public void setBetItem(String betItem) {
		this.betItem = betItem;
	}
	public String getIsDT() {
		return isDT;
	}
	public void setIsDT(String isDT) {
		this.isDT = isDT;
	}
}
