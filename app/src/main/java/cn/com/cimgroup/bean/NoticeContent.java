package cn.com.cimgroup.bean;

import java.io.Serializable;

public class NoticeContent implements Serializable {
	
	private static final long serialVersionUID = 8530507909839776238L;
	private String title;
	private String pubTime;
	private String content;
	private String isWeb;
	private String webUrl;
	private String isLogin;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIsWeb() {
		return isWeb;
	}
	public void setIsWeb(String isWeb) {
		this.isWeb = isWeb;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
}
