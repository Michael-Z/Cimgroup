package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ScoreDetailGameEvent implements Serializable {
	
	private static final long serialVersionUID = 7845697928183075406L;
	private String event;
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
}
