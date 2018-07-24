package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 游戏竞猜记录
 * 
 * @author honglin
 * 
 */
public class GuessRecordBean implements Serializable {

	private static final long serialVersionUID = -2526953726751286021L;
	private String userName;
	private String userId;
	private String addTime;
	private String hostCode;
	private String hostName;
	private String guestCode;
	private String guestName;
	private String question;
	private String content;
	private String lotteryResult;
	private String status;
	private String betAmount;
	private String winAmount;
	private String returnAmount;
	private String dateNo;
	
	private YlcRaceRecordBean recordBean;
	

	public YlcRaceRecordBean getRecordBean() {
		return recordBean;
	}

	public void setRecordBean(YlcRaceRecordBean recordBean) {
		this.recordBean = recordBean;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getHostCode() {
		return hostCode;
	}

	public void setHostCode(String hostCode) {
		this.hostCode = hostCode;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getGuestCode() {
		return guestCode;
	}

	public void setGuestCode(String guestCode) {
		this.guestCode = guestCode;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLotteryResult() {
		return lotteryResult;
	}

	public void setLotteryResult(String lotteryResult) {
		this.lotteryResult = lotteryResult;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(String betAmount) {
		this.betAmount = betAmount;
	}

	public String getWinAmount() {
		return winAmount;
	}

	public void setWinAmount(String winAmount) {
		this.winAmount = winAmount;
	}

	public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getDateNo() {
		return dateNo;
	}

	public void setDateNo(String dateNo) {
		this.dateNo = dateNo;
	}

}
