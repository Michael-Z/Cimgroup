package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 请求AppId返回
 * 
 * @author 秋风
 * 
 */
public class AppIdBean implements Serializable {

	private static final long serialVersionUID = 5560144203255125987L;
	/** 返回code */
	private int resCode;
	/** 返回信息 */
	private String resMsg;
	/** 商户AppID */
	private String appId;
	public int getResCode() {
		return resCode;
	}
	public void setResCode(int resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	

}
