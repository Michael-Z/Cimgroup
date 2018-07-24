package cn.com.cimgroup.bean;

import java.io.Serializable;

public class MatchAgainstSpInfo implements Serializable {

	private static final long serialVersionUID = 2473502876825474918L;
	
	//玩法编号
	private String playMethod;
	//Sp值（需根据不同玩法解析）
	private String realTimeSp;
	//过关方式   2为过关，1为单关
	private String passType;
	//修改时间
	private String updateTime;
	/**让球信息*/
	private String letScoreMinSp;
	
	
	public String getLetScoreMinSp() {
		return letScoreMinSp;
	}
	public void setLetScoreMinSp(String letScoreMinSp) {
		this.letScoreMinSp = letScoreMinSp;
	}
	public String getPlayMethod() {
		return playMethod;
	}
	public void setPlayMethod(String playMethod) {
		this.playMethod = playMethod;
	}
	public String getRealTimeSp() {
		return realTimeSp;
	}
	public void setRealTimeSp(String realTimeSp) {
		this.realTimeSp = realTimeSp;
	}
	public String getPassType() {
		return passType;
	}
	public void setPassType(String passType) {
		this.passType = passType;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
