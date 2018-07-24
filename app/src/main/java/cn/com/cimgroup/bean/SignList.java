package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class SignList implements Serializable {
	
	private static final long serialVersionUID = -9155093687106994752L;
	private String resCode;
	private String resMsg;
	private List<Sign> list;
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
	public List<Sign> getList() {
		return list;
	}
	public void setList(List<Sign> list) {
		this.list = list;
	}
}
