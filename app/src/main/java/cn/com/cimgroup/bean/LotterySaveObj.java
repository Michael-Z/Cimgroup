package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;

import cn.com.cimgroup.activity.LotteryCartActivity.CodeInfo;


public class LotterySaveObj implements Serializable {
	
	private static final long serialVersionUID = 4751898326830876984L;
	private List<CodeInfo> data;
	
	public List<CodeInfo> getData() {
		return data;
	}
	
	public void setData(List<CodeInfo> data) {
		this.data = data;
	}

	
}
