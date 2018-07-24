package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class HallAd implements Serializable  {
	
	private static final long serialVersionUID = -5056503301009232211L;
	private String timeId;
	private String ishow;
	private List<ImageObj> list;
	private String resCode;
	private String resMsg;
	public List<ImageObj> getList() {
		return list;
	}
	public void setList(List<ImageObj> list) {
		this.list = list;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public String getIshow() {
		return ishow;
	}
	public void setIshow(String ishow) {
		this.ishow = ishow;
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
