package cn.com.cimgroup.bean;

public class RankingListBean {

	public String userName;
	public String ranking;
	public String userId;
	public String winAmount;
	public boolean inTheRankingList;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(String winAmount) {
		this.winAmount = winAmount;
	}
	public boolean isInTheRankingList() {
		return inTheRankingList;
	}
	public void setInTheRankingList(boolean inTheRankingList) {
		this.inTheRankingList = inTheRankingList;
	}
	
	
}
