package cn.com.cimgroup.bean;

import java.io.Serializable;

public class City implements Serializable {

	private static final long serialVersionUID = -8449656151451669930L;
	
	private String city;
	private String cityCode;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

}
