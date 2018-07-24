package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ScoreDetailOddsList implements Serializable {
	
	private static final long serialVersionUID = 6132686878872226846L;
	private String resCode;
	private String resMsg;
	private List<ScoreDetailOdds> list;
	
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
	public List<ScoreDetailOdds> getList() {
		return list;
	}
	public void setList(List<ScoreDetailOdds> list) {
		this.list = list;
	}
}
