package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class Province implements Serializable {

	private static final long serialVersionUID = 1029708271310815159L;
	
	private String province;
	private String proCode;
	
	List<City> citys;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public List<City> getCitys() {
		return citys;
	}

	public void setCitys(List<City> citys) {
		this.citys = citys;
	}

}
