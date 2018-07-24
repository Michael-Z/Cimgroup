package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 比分直播数据解析Bean
 * 
 * @author 秋风
 * 
 */
public class ScoreMatchBean implements Serializable {

	private static final long serialVersionUID = -1154867101717528061L;
	/** 联赛编号 */
	private String matchId;
	/** 联赛场次(期次) */
	private String matchTimes;
	/** 联赛名称 */
	private String matchName;
	/** 主球队名称 */
	private String hostName;
	/** 客球队名称 */
	private String guestName;
	/** 比赛时间 */
	private String matchTime;
	/** 让球数 */
	private String rangBallNum;
	/**主队总进球*/
	private String hostFullGoals;
	/**客队总进球*/
	private String guestFullGoals;
	/**主队半场进球*/
	private String hostHalfGoals;
	/**客队半场进球*/
	private String guestHalfGoals;
	/** 主队红牌 */
	private String redCardZhu;
	/** 主队黄牌 */
	private String yellowCardZhu;
	/** 客队红牌 */
	private String redCardKe;
	/** 客队黄牌 */
	private String yellowCardKe;
	/** 主队编号 */
	private String numZhu;
	/** 客队编号 */
	private String numKe;
	/** 是否关注 */
	private String watchStatus;
	/** 查询其次 */
	private String issueNo;
	/** 比赛进行时间(比赛时间与当前时间间隔) */
	private String time;
	/** 赛事状态 */
	private String status;

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getMatchTimes() {
		return matchTimes;
	}

	public void setMatchTimes(String matchTimes) {
		this.matchTimes = matchTimes;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	public String getRangBallNum() {
		return rangBallNum;
	}

	public String getHostFullGoals() {
		return hostFullGoals;
	}

	public void setHostFullGoals(String hostFullGoals) {
		this.hostFullGoals = hostFullGoals;
	}

	public String getGuestFullGoals() {
		return guestFullGoals;
	}

	public void setGuestFullGoals(String guestFullGoals) {
		this.guestFullGoals = guestFullGoals;
	}

	public String getHostHalfGoals() {
		return hostHalfGoals;
	}

	public void setHostHalfGoals(String hostHalfGoals) {
		this.hostHalfGoals = hostHalfGoals;
	}

	public String getGuestHalfGoals() {
		return guestHalfGoals;
	}

	public void setGuestHalfGoals(String guestHalfGoals) {
		this.guestHalfGoals = guestHalfGoals;
	}

	public void setRangBallNum(String rangBallNum) {
		this.rangBallNum = rangBallNum;
	}

	
	public String getRedCardZhu() {
		return redCardZhu;
	}

	public void setRedCardZhu(String redCardZhu) {
		this.redCardZhu = redCardZhu;
	}

	public String getYellowCardZhu() {
		return yellowCardZhu;
	}

	public void setYellowCardZhu(String yellowCardZhu) {
		this.yellowCardZhu = yellowCardZhu;
	}

	public String getRedCardKe() {
		return redCardKe;
	}

	public void setRedCardKe(String redCardKe) {
		this.redCardKe = redCardKe;
	}

	public String getYellowCardKe() {
		return yellowCardKe;
	}

	public void setYellowCardKe(String yellowCardKe) {
		this.yellowCardKe = yellowCardKe;
	}

	public String getNumZhu() {
		return numZhu;
	}

	public void setNumZhu(String numZhu) {
		this.numZhu = numZhu;
	}

	public String getNumKe() {
		return numKe;
	}

	public void setNumKe(String numKe) {
		this.numKe = numKe;
	}

	public String getWatchStatus() {
		return watchStatus;
	}

	public void setWatchStatus(String watchStatus) {
		this.watchStatus = watchStatus;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
