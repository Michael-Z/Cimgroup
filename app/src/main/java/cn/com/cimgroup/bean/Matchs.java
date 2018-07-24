package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class Matchs implements Serializable {

	private static final long serialVersionUID = -6998062739056266891L;
	
	//分隔每一天的对阵信息
	private String spiltTime;
	private List<Match> matchs;
	
	public String getSpiltTime() {
		return spiltTime;
	}
	public void setSpiltTime(String spiltTime) {
		this.spiltTime = spiltTime;
	}
	public List<Match> getMatchs() {
		return matchs;
	}
	public void setMatchs(List<Match> matchs) {
		this.matchs = matchs;
	}

}
