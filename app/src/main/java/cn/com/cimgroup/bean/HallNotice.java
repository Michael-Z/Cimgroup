package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class HallNotice implements Serializable {
	
	private static final long serialVersionUID = -7999995114650276641L;
	private String timeid;
	private String showType;
	private String isAlwaysShow;
	private String imgUrl;
	private String imgEvent;
	private List<NoticeContent> eventContent;
	private String resCode;
	private String resMsg;
	public List<NoticeContent> getEventContent() {
		return eventContent;
	}
	public void setEventContent(List<NoticeContent> eventContent) {
		this.eventContent = eventContent;
	}
	public String getTimeid() {
		return timeid;
	}
	public void setTimeid(String timeid) {
		this.timeid = timeid;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getIsAlwaysShow() {
		return isAlwaysShow;
	}
	public void setIsAlwaysShow(String isAlwaysShow) {
		this.isAlwaysShow = isAlwaysShow;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgEvent() {
		return imgEvent;
	}
	public void setImgEvent(String imgEvent) {
		this.imgEvent = imgEvent;
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
}
