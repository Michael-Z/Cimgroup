package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ReChargeChannel implements Serializable {
	
	private static final long serialVersionUID = -5000212474301333771L;
	
	private String resCode;
	private String resMsg;
	private String timeId;
	
	private List<BankInfo> bankInfos;
	
	private List<ChannelContent> contents;
	
	public List<BankInfo> getBankInfos() {
		return bankInfos;
	}
	public void setBankInfos(List<BankInfo> bankInfos) {
		this.bankInfos = bankInfos;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
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
	public List<ChannelContent> getContents() {
		return contents;
	}
	public void setContents(List<ChannelContent> contents) {
		this.contents = contents;
	}
	
}
