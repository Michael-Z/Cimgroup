package cn.com.cimgroup.bean;

import java.io.Serializable;

public class PushObj implements Serializable {

	private static final long serialVersionUID = 1117879107522919440L;
	
	private String key;
	private String isPush;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getIsPush() {
		return isPush;
	}
	public void setIsPush(String isPush) {
		this.isPush = isPush;
	}
}
