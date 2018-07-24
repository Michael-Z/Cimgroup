package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ScoreDetailOdds implements Serializable {
	
	private static final long serialVersionUID = 4292609231350986861L;
	//公司
	private String company;
	private String companyId;
	private String initOdds;
	private String changeOdds;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getInitOdds() {
		return initOdds;
	}
	public void setInitOdds(String initOdds) {
		this.initOdds = initOdds;
	}
	public String getChangeOdds() {
		return changeOdds;
	}
	public void setChangeOdds(String changeOdds) {
		this.changeOdds = changeOdds;
	}
	
}
