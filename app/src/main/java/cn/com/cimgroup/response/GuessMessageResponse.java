package cn.com.cimgroup.response;

import java.io.Serializable;
import java.util.List;

import cn.com.cimgroup.bean.GuessGameMessage;

/**
 * 竞猜游戏留言板Response
 * 
 * @author 秋风
 * 
 */
public class GuessMessageResponse implements Serializable {

	private static final long serialVersionUID = -4685701349209651187L;
	/** 消息集合 */
	private List<GuessGameMessage> list;
	/** 当前页码 */
	private String pageNo;
	/** 总记录条数 */
	private String total;
	/** 请求码 */
	private String resCode;
	/** 请求返回信息 */
	private String resMsg;

	public List<GuessGameMessage> getList() {
		return list;
	}

	public void setList(List<GuessGameMessage> list) {
		this.list = list;
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

}
