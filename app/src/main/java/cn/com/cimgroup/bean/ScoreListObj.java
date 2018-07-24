package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ScoreListObj implements Serializable {

	private static final long serialVersionUID = -2722289557647218390L;
	// 赛事状态（0未开赛，1、进行中，2、完场，3、改期，4、腰斩，5、中场或半场，11、上，12、下，13、延期，14、待定或点球或加时）
	private String status;
	// 比赛进行时间（比赛时间与当前时间间隔） 状态1、进行中 时有效，其他状态无此字段
	private String time;
	/*
	 * 赛事格式(”+”、“:“分隔) ： 联赛编号+ 赛事场次+ 赛事名称+ 主队球队名称+ 客队球队名称+ 比赛时间+ 让球数+ 主队全场比分：
	 * 客队全场进球+ 主队半场进球： 客队半场进球+ 主队红牌： 主队黄牌+ 客队红牌： 客队黄牌+ 主队编号+ 客队编号
	 */
	private String record;
	// 是否关注（未登录不显示，登录后才有，0:未关注 1：已关注）
	private String watchStatus;
	private String issueNo;

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getWatchStatus() {
		return watchStatus;
	}

	public void setWatchStatus(String watchStatus) {
		this.watchStatus = watchStatus;
	}
}
