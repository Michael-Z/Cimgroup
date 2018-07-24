package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ScoreList implements Serializable {
	
	private static final long serialVersionUID = -8460529208110399782L;
	private String resCode;
	private String resMsg;
	private List<ScoreListObj> list;
	
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
	public List<ScoreListObj> getList() {
		return list;
	}
	public void setList(List<ScoreListObj> list) {
		this.list = list;
	}
}
