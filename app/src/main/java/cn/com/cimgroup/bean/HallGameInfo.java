package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 游戏大厅游戏信息
 * @author 秋风
 *
 */
public class HallGameInfo implements Serializable{
	private static final long serialVersionUID = -825804137454477614L;
	/**玩法ID*/
	private String gameId;
	/**游戏名称*/
	private String gameName;
	/**状态*/
	private String status;
	/**状态描述*/
	private String statusStr;
	/**操作类型0:跳转原生；1：跳转H5*/
	private String jumpType;
	/**H5跳转地址*/
	private String jumpUrl;
	/**游戏图片*/
	private String gameImg;
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getJumpType() {
		return jumpType;
	}
	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}
	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	public String getGameImg() {
		return gameImg;
	}
	public void setGameImg(String gameImg) {
		this.gameImg = gameImg;
	}
	
	public HallGameInfo() {
	}
	public HallGameInfo(String gameId, String gameName, String status,
			String statusStr, String jumpType, String jumpUrl, String gameImg) {
		super();
		this.gameId = gameId;
		this.gameName = gameName;
		this.status = status;
		this.statusStr = statusStr;
		this.jumpType = jumpType;
		this.jumpUrl = jumpUrl;
		this.gameImg = gameImg;
	}
	
}
