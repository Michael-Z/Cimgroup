package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class LotteryList implements Serializable {
	
	private static final long serialVersionUID = 3490353431167311304L;
	private String resCode;
	private String resMsg;
	private String pageNo;
	private String total;
	private List<LotteryListObj> list;
	
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
	public List<LotteryListObj> getList() {
		return list;
	}
	public void setList(List<LotteryListObj> list) {
		this.list = list;
	}
	
	
}
