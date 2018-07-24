package cn.com.cimgroup.bean;

import java.io.Serializable;


public class ScoreObj implements Serializable {
	
	private static final long serialVersionUID = 5985118374023534437L;
	private String resMsg;
	private String resCode;
	private ScoreListObj watch;
	
	public String getResMsg() {
		return resMsg;
	}
	
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	
	public String getResCode() {
		return resCode;
	}
	
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	
	public ScoreListObj getWatch() {
		return watch;
	}
	
	public void setWatch(ScoreListObj watch) {
		this.watch = watch;
	}
}
