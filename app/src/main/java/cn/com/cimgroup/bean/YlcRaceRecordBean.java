package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * 竞猜记录--人数，奖池信息
 * @author 秋风
 *
 */
public class YlcRaceRecordBean implements Serializable{

	private static final long serialVersionUID = 6262964750410449601L;
	/**肯定奖池*/
	private String definitePool;
	/**否定奖池*/
	private String negativePool;
	/**肯定人数*/
	private String definiteUsers;
	/**否定人数*/
	private String negativeUsers;
	
	
	public String getDefinitePool() {
		return definitePool;
	}

	public void setDefinitePool(String definitePool) {
		this.definitePool = definitePool;
	}

	public String getNegativePool() {
		return negativePool;
	}

	public void setNegativePool(String negativePool) {
		this.negativePool = negativePool;
	}

	public String getDefiniteUsers() {
		return definiteUsers;
	}

	public void setDefiniteUsers(String definiteUsers) {
		this.definiteUsers = definiteUsers;
	}

	public String getNegativeUsers() {
		return negativeUsers;
	}

	public void setNegativeUsers(String negativeUsers) {
		this.negativeUsers = negativeUsers;
	}
}
