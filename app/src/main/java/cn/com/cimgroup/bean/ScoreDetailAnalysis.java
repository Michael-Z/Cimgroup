package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

public class ScoreDetailAnalysis implements Serializable {

	private static final long serialVersionUID = -5089236417527987876L;
	private String resCode;
	private String resMsg;
	
	//交战列表说明
	private String bothSideResult;
	//主队近期战绩赛果总结
	private String hostNearResult;
	//客队近期战绩赛果总结
	private String guestNearResult;
	
	//主队积分排名
	private List<HostRankObj> hostRankList;
	//客队积分排名
	private List<GuestRankObj> guestRankList;
	//主队近期战绩
	private List<HostNearObj> hostNearList;
	//客队近期战绩
	private List<GuestNearObj> guestNearList;
	//双方交战
	private List<VsObj> vsList;
	//主队未来赛事
	private List<HostFutureObj> hostFutureVsList;
	//客队未来赛事
	private List<GuestFutureObj> guestFutureVsList;
	
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
	public String getBothSideResult() {
		return bothSideResult;
	}
	public void setBothSideResult(String bothSideResult) {
		this.bothSideResult = bothSideResult;
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
	public List<HostRankObj> getHostRankList() {
		return hostRankList;
	}
	public void setHostRankList(List<HostRankObj> hostRankList) {
		this.hostRankList = hostRankList;
	}
	public List<GuestRankObj> getGuestRankList() {
		return guestRankList;
	}
	public void setGuestRankList(List<GuestRankObj> guestRankList) {
		this.guestRankList = guestRankList;
	}
	public List<HostNearObj> getHostNearList() {
		return hostNearList;
	}
	public void setHostNearList(List<HostNearObj> hostNearList) {
		this.hostNearList = hostNearList;
	}
	public List<GuestNearObj> getGuestNearList() {
		return guestNearList;
	}
	public void setGuestNearList(List<GuestNearObj> guestNearList) {
		this.guestNearList = guestNearList;
	}
	public List<VsObj> getVsList() {
		return vsList;
	}
	public void setVsList(List<VsObj> vsList) {
		this.vsList = vsList;
	}
	public List<HostFutureObj> getHostFutureVsList() {
		return hostFutureVsList;
	}
	public void setHostFutureVsList(List<HostFutureObj> hostFutureVsList) {
		this.hostFutureVsList = hostFutureVsList;
	}
	public List<GuestFutureObj> getGuestFutureVsList() {
		return guestFutureVsList;
	}
	public void setGuestFutureVsList(List<GuestFutureObj> guestFutureVsList) {
		this.guestFutureVsList = guestFutureVsList;
	}
	
}
