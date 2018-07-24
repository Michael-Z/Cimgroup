package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 竞猜游戏留言
 * 
 * @author 秋风
 * 
 */
public class GuessGameMessage implements Serializable {

	private static final long serialVersionUID = -2155386879454126185L;
	/** 用户ID */
	private String userId;
	/** 用户昵称 */
	private String userName;
	/** 留言内容 */
	private String content;
	/** 留言时间 */
	private String addTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

}
