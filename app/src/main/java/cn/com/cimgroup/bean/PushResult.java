package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class PushResult implements Serializable {
	
	private static final long serialVersionUID = 2791587437218228187L;
	
	private String resCode;
	private String resMsg;
	private String userId;
	private List<PushObj> list;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<PushObj> getList() {
		return list;
	}
	public void setList(List<PushObj> list) {
		this.list = list;
	}
}
