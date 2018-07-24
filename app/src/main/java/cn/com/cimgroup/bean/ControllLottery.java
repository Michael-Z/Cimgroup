package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ControllLottery implements Serializable {
	
	private static final long serialVersionUID = -6435469529123507391L;
	private String timeId;
	private List<GameObj> gameList;
	private String resCode;
	private String resMsg;
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public List<GameObj> getGameList() {
		return gameList;
	}
	public void setGameList(List<GameObj> gameList) {
		this.gameList = gameList;
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
}
