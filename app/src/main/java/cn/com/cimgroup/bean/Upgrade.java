package cn.com.cimgroup.bean;

import java.io.Serializable;

public class Upgrade implements Serializable {
	
	private static final long serialVersionUID = -2354999799707911670L;
	private String newVersion;
	private String isReview;
	private String isUpdate;
	private String isMust;
	private String title;
	private String versionMsg;
	private String downUrl;
	private String resCode;
	private String resMsg;
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	public String getIsReview() {
		return isReview;
	}
	public void setIsReview(String isReview) {
		this.isReview = isReview;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getIsMust() {
		return isMust;
	}
	public void setIsMust(String isMust) {
		this.isMust = isMust;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVersionMsg() {
		return versionMsg;
	}
	public void setVersionMsg(String versionMsg) {
		this.versionMsg = versionMsg;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
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
