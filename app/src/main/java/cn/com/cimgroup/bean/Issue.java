package cn.com.cimgroup.bean;

import java.io.Serializable;

public class Issue implements Serializable {
	
	private static final long serialVersionUID = -4189318194363214292L;
	
	private String issue;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}
}
