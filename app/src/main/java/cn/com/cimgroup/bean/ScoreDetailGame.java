package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ScoreDetailGame implements Serializable {
	
	private static final long serialVersionUID = 8812138673799342035L;
	private String resCode;
	private String resMsg;
	private List<ScoreDetailGameEvent> list;
	
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
	public List<ScoreDetailGameEvent> getList() {
		return list;
	}
	public void setList(List<ScoreDetailGameEvent> list) {
		this.list = list;
	}
}
