package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 彩种期次返回值
 * 
 * @author 秋风
 * 
 */
public class LotteryNOs implements Serializable {
	private static final long serialVersionUID = -8426209601846260410L;
	/** 彩种编号 */
	private String lotteryNo;
	/** 期次列表 */
	private String issues;

	public String getLotteryNo() {
		return lotteryNo;
	}

	public void setLotteryNo(String lotteryNo) {
		this.lotteryNo = lotteryNo;
	}

	public String getIssues() {
		return issues;
	}

	public void setIssues(String issues) {
		this.issues = issues;
	}

	public String getCurrIssues() {
		return currIssues;
	}

	public void setCurrIssues(String currIssues) {
		this.currIssues = currIssues;
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

	/** 当前期次（对应老足彩） */
	private String currIssues;
	/** 请求返回编码 */
	private String resCode;
	/** 请求返回Message */
	private String resMsg;

}
