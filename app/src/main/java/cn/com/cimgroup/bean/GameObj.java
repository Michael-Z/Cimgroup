package cn.com.cimgroup.bean;

import java.io.Serializable;

public class GameObj implements Serializable {
	
	private static final long serialVersionUID = -8117662015793164936L;
	private String gameNo;
	private String gameName;
	private String isAward;
	private String isSale;
	private String imgUrl;
	private String countdown;
	private String isTodayAward;
	private String gameMsg;
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getIsAward() {
		return isAward;
	}
	public void setIsAward(String isAward) {
		this.isAward = isAward;
	}
	public String getIsSale() {
		return isSale;
	}
	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getCountdown() {
		return countdown;
	}
	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}
	public String getIsTodayAward() {
		return isTodayAward;
	}
	public void setIsTodayAward(String isTodayAward) {
		this.isTodayAward = isTodayAward;
	}
	public String getGameMsg() {
		return gameMsg;
	}
	public void setGameMsg(String gameMsg) {
		this.gameMsg = gameMsg;
	}
}
