package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class AllBettingList implements Serializable {
	
	private static final long serialVersionUID = 6145247744733476506L;
	private String resCode;
	private String resMsg;
	private String pageNo;
	private String total;
	private List<AllBetting> list;
	
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
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<AllBetting> getList() {
		return list;
	}
	public void setList(List<AllBetting> list) {
		this.list = list;
	}
}
