package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class lotteryDrawInfoList implements Serializable {
	
	private static final long serialVersionUID = -2275616748016935265L;
	private String resCode;
	private String resMsg;
	private List<LotteryDrawInfo> list;
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
	public List<LotteryDrawInfo> getList() {
		return list;
	}
	public void setList(List<LotteryDrawInfo> list) {
		this.list = list;
	}
}
