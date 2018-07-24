package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class LotteryDrawDetailInfo implements Serializable {
	
	private static final long serialVersionUID = 1317690879408632045L;
	private String resCode;
	private String resMsg;
	private String new_gameNo;
	private String new_issue;
	private String new_date;
	private String new_winCode;
	private String new_tion_fund;
	private String new_pond_money;
	private List<LotteryDrawIssue> issueList;
	private List<LotteryDrawMatch> matchList;
	private List<LotteryDrawSnatch> awardSnatchList;
	public List<LotteryDrawSnatch> getAwardSnatchList() {
		return awardSnatchList;
	}
	public void setAwardSnatchList(List<LotteryDrawSnatch> awardSnatchList) {
		this.awardSnatchList = awardSnatchList;
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
	public String getNew_gameNo() {
		return new_gameNo;
	}
	public void setNew_gameNo(String new_gameNo) {
		this.new_gameNo = new_gameNo;
	}
	public String getNew_issue() {
		return new_issue;
	}
	public void setNew_issue(String new_issue) {
		this.new_issue = new_issue;
	}
	public String getNew_date() {
		return new_date;
	}
	public void setNew_date(String new_date) {
		this.new_date = new_date;
	}
	public String getNew_winCode() {
		return new_winCode;
	}
	public void setNew_winCode(String new_winCode) {
		this.new_winCode = new_winCode;
	}
	public String getNew_tion_fund() {
		return new_tion_fund;
	}
	public void setNew_tion_fund(String new_tion_fund) {
		this.new_tion_fund = new_tion_fund;
	}
	public String getNew_pond_money() {
		return new_pond_money;
	}
	public void setNew_pond_money(String new_pond_money) {
		this.new_pond_money = new_pond_money;
	}
	public List<LotteryDrawIssue> getIssueList() {
		return issueList;
	}
	public void setIssueList(List<LotteryDrawIssue> issueList) {
		this.issueList = issueList;
	}
	public List<LotteryDrawMatch> getMatchList() {
		return matchList;
	}
	public void setMatchList(List<LotteryDrawMatch> matchList) {
		this.matchList = matchList;
	}
}
