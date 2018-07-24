package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 资金明细集合
 * @author 秋风
 *
 */
public class AccountDetailList implements Serializable{

	private static final long serialVersionUID = -3899472087194280048L;
	private String inMoney ;
	private String outMoney;
	private String pageNo;
	private String total;
	private String resCode;
	private String resMsg;
	private List<AccountDetail> list;
	public String getInMoney() {
		return inMoney;
	}
	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}
	public String getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
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
	public List<AccountDetail> getList() {
		return list;
	}
	public void setList(List<AccountDetail> list) {
		this.list = list;
	}
	
	
}
