package cn.com.cimgroup.bean;

import java.io.Serializable;

public class IssuePreList implements Serializable {
	
	private static final long serialVersionUID = -6880371562310534573L;
	private String resCode;
	private String resMsg;
	private String issueArr;
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
	public String getIssueArr() {
		return issueArr;
	}
	public void setIssueArr(String issueArr) {
		this.issueArr = issueArr;
	}
}
