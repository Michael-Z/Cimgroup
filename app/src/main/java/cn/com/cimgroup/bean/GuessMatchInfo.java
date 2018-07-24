package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 竞猜比戏比赛信息
 * @author 秋风
 *
 */
public class GuessMatchInfo implements Serializable{
	private static final long serialVersionUID = 6760969483834255098L;
	/**比赛ID*/
	private String leagueCode;
	/**主队名*/
	private String hostName;
	/**客队名*/
	private String guestName;
	/**赛事时间*/
	private String matchTime;
	/**赛事名称*/
	private String matchName;
	/**赛事图片*/
	private String matchImg;
	/**主队图片*/
	private String hostImg;
	/**客队图片*/
	private String guestImg;
	/**比赛题目集合*/
	private List<QuestionInfo> questionList;
	public String getLeagueCode() {
		return leagueCode;
	}
	public void setLeagueCode(String leagueCode) {
		this.leagueCode = leagueCode;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public String getMatchImg() {
		return matchImg;
	}
	public void setMatchImg(String matchImg) {
		this.matchImg = matchImg;
	}
	public String getHostImg() {
		return hostImg;
	}
	public void setHostImg(String hostImg) {
		this.hostImg = hostImg;
	}
	public String getGuestImg() {
		return guestImg;
	}
	public void setGuestImg(String guestImg) {
		this.guestImg = guestImg;
	}
	public List<QuestionInfo> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<QuestionInfo> questionList) {
		this.questionList = questionList;
	}
	
	
}
