package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;


public class HallTextList implements Serializable {
	
	private static final long serialVersionUID = -1004602898838604710L;
	private String resCode;
	private String resMsg;
	private List<HallText> list;
	
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
	
	public List<HallText> getList() {
		return list;
	}
	
	public void setList(List<HallText> list) {
		this.list = list;
	}
}
