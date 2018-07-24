package cn.com.cimgroup.response;

import java.io.Serializable;
import java.util.List;

import cn.com.cimgroup.bean.HallGameInfo;

/**
 * 游戏大厅数据返回Response
 * 
 * @author 秋风
 * 
 */
public class HallGameResponse implements Serializable {

	private static final long serialVersionUID = -4196429504798771289L;
	/** 游戏列表 */
	private List<HallGameInfo> list;
	/** 请求返回码 */
	private String resCode;
	/** 请求返回MSG */
	private String resMsg;
	/**时间修改*/
	private String timeId;

	public List<HallGameInfo> getList() {
		return list;
	}

	public void setList(List<HallGameInfo> list) {
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
	
	

}
