package cn.com.cimgroup.bean;

import java.io.Serializable;

public class ZhuiContent implements Serializable {
	
	private static final long serialVersionUID = -1051741763718851544L;
	private String play;
	private String content;
	private String saleCnt;
	private String multiBetCnt;
	private String playDes;
	public String getPlay() {
		return play;
	}
	public void setPlay(String play) {
		this.play = play;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSaleCnt() {
		return saleCnt;
	}
	public void setSaleCnt(String saleCnt) {
		this.saleCnt = saleCnt;
	}
	public String getMultiBetCnt() {
		return multiBetCnt;
	}
	public void setMultiBetCnt(String multiBetCnt) {
		this.multiBetCnt = multiBetCnt;
	}
	public String getPlayDes() {
		return playDes;
	}
	public void setPlayDes(String playDes) {
		this.playDes = playDes;
	}
}
