package cn.com.cimgroup.bean;

import java.io.Serializable;


public class HallText implements Serializable {

	private static final long serialVersionUID = 7022895985399454337L;
	private String content;
	private String gameId;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getGameId() {
		return gameId;
	}
	
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
}
