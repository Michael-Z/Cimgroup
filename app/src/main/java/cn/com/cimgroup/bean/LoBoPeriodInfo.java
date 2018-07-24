package cn.com.cimgroup.bean;

public class LoBoPeriodInfo {
	// 结束时间
	private Long endTime;
	// 开始时间
	private Long startTime;
	// 销售期次
	private String issue;
	// 彩种编号
	private String gameNo;
	// 1：可销售 ；2：暂不可销售
	private String issueStatus;
	// 销售倒计时，毫秒值
	private String timeStep;
	
	public String getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(String timeStep) {
		this.timeStep = timeStep;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
}
