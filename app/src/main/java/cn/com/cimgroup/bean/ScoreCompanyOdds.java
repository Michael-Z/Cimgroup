package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ScoreCompanyOdds implements Serializable {

	private static final long serialVersionUID = 2568200647885276419L;
	private String resCode;
	private String resMsg;
	private String timeId;
	private String pageNo;
	private String total;
	private List<Company> companyList;
	private List<TheOdds> list;
	
	public List<Company> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}
	public List<TheOdds> getList() {
		return list;
	}
	public void setList(List<TheOdds> list) {
		this.list = list;
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
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
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
	
}
