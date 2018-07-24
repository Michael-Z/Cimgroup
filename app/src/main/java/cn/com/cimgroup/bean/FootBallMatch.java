package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class FootBallMatch implements Serializable {

	private static final long serialVersionUID = -2707115183649184231L;
	
	private String resCode;
	private String resMsg;
	private List<Matchs> list;
	
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
	public List<Matchs> getList() {
		return list;
	}
	public void setList(List<Matchs> list) {
		this.list = list;
	}

}
