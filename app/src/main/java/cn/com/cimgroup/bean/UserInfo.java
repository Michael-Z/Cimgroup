package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 3788380676336034230L;
	/** 用户名 */
	private String userName;
	/** 用户id */
	private String userId;
	/** 用户真实姓名 */
	private String realName;
	/** 身份证 */
	private String idCard;
	/** 用户性别 */
	private String sex;
	/** 生日 */
	private String birthday;
	/** 省份 */
	private String province;
	/** 城市 */
	private String city;
	/** 绑定开户行名称 */
	private String bankName;
	/** 绑定开户行卡号 */
	private String bankCardId;
	/** 支行名称 */
	private String branchBankName;
	/** 手机号 */
	private String mobile;
	/** UNIOPAY-银联绑定 BANDING-身份证与姓名已完善，不返回banktype则是未绑定 */
	private String banktype;
	/** 账户绑定状态为3位数* 第一位代表手机号绑定;* 第二位代表邮箱绑定;* 第三位代表密保问题绑定;例如: 000 ，(0代表未绑定1代表绑定） */
	private String bingtype;
	/** 邮箱 */
	private String email;
	/** 是否记住密码 */
	private boolean rem;
	/** 是否自动登陆 */
	private boolean auto;
	/** 密码 */
	private String password;
	/** 投注账户 默认为0，单位（分） */
	private String betAcnt;
	/** 奖金账户 默认为0，单位（分） */
	private String prizeAcnt;
	/** 积分值 默认为0，单位（分） */
	private String integralAcnt;
	/** 累计中奖金额 默认为0，单位（分） */
	private String totalPrizeMoney;
	/** 是否已经完善了用户名 1是 0否 用于验证用户名和身份份证是否填写 */
	private String isCompUserName;
	/** 银行卡是否绑定：0未绑定，1已绑定，默认为0 */
	private String isBindedBank;
	/** 极光push的appkey，用于判断是否和本地不同，是否进行Push注册 */
	private String jpushAppKey;
	/** 极光push的推送唯一id，用于判断是否和本地不同，是否进行Push注册 */
	private String jpushRegistrationID;
	/** 用户头像地址 */
	private String headImgUrl;

	/** 服务器返回的状态码 */
	private String resCode;
	/** 服务器的提示文字 */
	private String resMsg;
	
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	// 关于红包新添加的字段;
	/** 红包剩余总量 */
	public String redPkgNum;
	/** 红包剩余总额 */
	public String redPkgAccount;
	/** 购买红包金额 */
	public String buyRedMoney;
	/** 兑换红包个数 */
	public String convertRedNum;
	/** 兑换红包金额 */
	public String convertRedTotalMoney;
	/** 兑换红包列表 */
	public HashMap<String, List<TcjczqObj>> convertRedList;
	/** 赠送红包个数 */
	public String giveRedNum;
	/** 赠送红包金额 */
	public String giveRedTotalMoney;
	/** 赠送红包 */
	public HashMap<String, List<TcjczqObj>> giveRedList;
	
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	/** 用户的登陆模式（1-微信；2-自营）**/
	public int loginPattern;

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}

	public String getIsBindedBank() {
		return isBindedBank;
	}

	public void setIsBindedBank(String isBindedBank) {
		this.isBindedBank = isBindedBank;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBanktype() {
		return banktype;
	}

	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}

	public String getBingtype() {
		return bingtype;
	}

	public void setBingtype(String bingtype) {
		this.bingtype = bingtype;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public boolean isRem() {
		return rem;
	}

	public void setRem(boolean rem) {
		this.rem = rem;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBetAcnt() {
		return betAcnt;
	}

	public void setBetAcnt(String betAcnt) {
		this.betAcnt = betAcnt;
	}

	public String getPrizeAcnt() {
		return prizeAcnt;
	}

	public void setPrizeAcnt(String prizeAcnt) {
		this.prizeAcnt = prizeAcnt;
	}

	public String getIntegralAcnt() {
		return integralAcnt;
	}

	public void setIntegralAcnt(String integralAcnt) {
		this.integralAcnt = integralAcnt;
	}

	public String getTotalPrizeMoney() {
		return totalPrizeMoney;
	}

	public void setTotalPrizeMoney(String totalPrizeMoney) {
		this.totalPrizeMoney = totalPrizeMoney;
	}

	public String getIsCompUserName() {
		return isCompUserName;
	}

	public void setIsCompUserName(String isCompUserName) {
		this.isCompUserName = isCompUserName;
	}

	public String getJpushAppKey() {
		return jpushAppKey;
	}

	public void setJpushAppKey(String jpushAppKey) {
		this.jpushAppKey = jpushAppKey;
	}

	public String getJpushRegistrationID() {
		return jpushRegistrationID;
	}

	public void setJpushRegistrationID(String jpushRegistrationID) {
		this.jpushRegistrationID = jpushRegistrationID;
	}
	public boolean isAuto() {
		return auto;
	}
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	public int getLoginPattern() {
		return loginPattern;
	}
	public void setLoginPattern(int loginPattern) {
		this.loginPattern = loginPattern;
	}
}
