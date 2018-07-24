package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class IssueList implements Serializable {

	private static final long serialVersionUID = -6886948698143751538L;
	
	private String resCode;
	private String resMsg;
	private List<LoBoPeriodInfo> issues;
	
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
	public List<LoBoPeriodInfo> getIssues() {
		return issues;
	}
	public void setIssues(List<LoBoPeriodInfo> issues) {
		this.issues = issues;
	}

}
