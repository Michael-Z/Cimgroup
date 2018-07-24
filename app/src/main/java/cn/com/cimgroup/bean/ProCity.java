package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ProCity implements Serializable {

	private static final long serialVersionUID = 9141706004517208618L;
	
	private String resCode;
	private String resMsg;
	private String timeId;
	
	private List<Province> provinces;

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

	public List<Province> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}

}
