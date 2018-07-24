package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class BankList implements Serializable {
	
	private static final long serialVersionUID = -6913062781864268531L;
	
	private String resCode;
	private String resMsg;
	private String timeId;
	
	private List<BankInfo> list;

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

	public String getTimeId() {
		return timeId;
	}

	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}

	public List<BankInfo> getList() {
		return list;
	}

	public void setList(List<BankInfo> list) {
		this.list = list;
	}

}
