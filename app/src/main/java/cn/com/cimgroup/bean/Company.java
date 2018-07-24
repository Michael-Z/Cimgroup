package cn.com.cimgroup.bean;

import java.io.Serializable;

public class Company implements Serializable {
	
	private static final long serialVersionUID = 7771409044779994070L;
	private String companyName;
	private String companyId;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
