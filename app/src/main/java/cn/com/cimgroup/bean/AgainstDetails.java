package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 比赛对阵信息
 * 
 * @author 秋风
 * 
 */
public class AgainstDetails implements Serializable {

	private static final long serialVersionUID = 5304481425556097879L;
	/** 平均赔率 */
	private String avgOdds;
	/** 主队近期战绩 */
	private String hostNearResult;
	/** 客队近期战绩 */
	private String guestNearResult;
	/** 历史交战 */
	private String bothSideResult;
	public String getAvgOdds() {
		return avgOdds;
	}
	public void setAvgOdds(String avgOdds) {
		this.avgOdds = avgOdds;
	}
	public String getHostNearResult() {
		return hostNearResult;
	}
	public void setHostNearResult(String hostNearResult) {
		this.hostNearResult = hostNearResult;
	}
	public String getGuestNearResult() {
		return guestNearResult;
	}
	public void setGuestNearResult(String guestNearResult) {
		this.guestNearResult = guestNearResult;
	}
	public String getBothSideResult() {
		return bothSideResult;
	}
	public void setBothSideResult(String bothSideResult) {
		this.bothSideResult = bothSideResult;
	}
	

}
