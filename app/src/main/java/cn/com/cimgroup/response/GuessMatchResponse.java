package cn.com.cimgroup.response;

import java.io.Serializable;
import java.util.List;

import cn.com.cimgroup.bean.GuessMatchInfo;

/**
 * 竞猜游戏比赛信息返回
 * 
 * @author 秋风
 * 
 */
public class GuessMatchResponse implements Serializable {

	private static final long serialVersionUID = 5362706039991329077L;
	/** 请求返回码 */
	private String resCode;
	/** 请求返回信息 */
	private String resMsg;
	/** 比赛精彩信息集合 */
	private List<GuessMatchInfo> list;

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

	public List<GuessMatchInfo> getList() {
		return list;
	}

	public void setList(List<GuessMatchInfo> list) {
		this.list = list;
	}

}
