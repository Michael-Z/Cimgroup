package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ImageObj implements Serializable {

	private static final long serialVersionUID = 8027373500951666463L;
	private String imgUrl;
	private String title;
	private String time;
	private String content;
	private String isWeb;
	private String webUrl;
	private String turnImgUrl;
	private String isLogin;
	public String getTurnImgUrl() {
		return turnImgUrl;
	}
	public void setTurnImgUrl(String turnImgUrl) {
		this.turnImgUrl = turnImgUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
