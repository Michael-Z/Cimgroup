package cn.com.cimgroup.config;

import android.content.Context;
import cn.com.cimgroup.App;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.xutils.PreferenceUtil;

/**
 * Account 本地存储用户内容
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright © www.wenchuang.com
 * @Date:2014-11-30
 */
public class UserConfiguration extends PreferenceUtil {

	/** Account 在本地文件中保存的键名称 */
//	// 用户名
//	public static String USERNAME = "username";
//	// 密码
//	public static String PASSWORD = "password";
//	// uid
//	public static String UID = "uid";
//	
//	public static String USERTYPE = "userType";
//	
//	public static String PAYPASSWD = "paypasswd";
//	
//	public static String MOBILE = "mobile";
//	
//	public static String MOBILEACTIVE = "mobileActive";
//	
//	public static String CARDTYPE = "cardType";
//	
//	public static String EMAIL = "email";
//	
//	public static String EMAILACTIVE = "emailActive";
//	
//	public static String STATUS = "status";
//	
//	public static String REGISTERDT = "registerDt";
//	
//	public static String REALNAME = "realName";
//	
//	public static String ADDRESS = "address";
//	
//	public static String IDCARD = "idcard";
//	
//	public static String ISREM = "isRem";
//	
//	public static String UNIONUSERID="unionUserId";
	

	// 文件名
	private static final String PREFERENCE_NAME = "common_config";

	public UserConfiguration(String name) {
		super(PREFERENCE_NAME);
		App.userInfo = new UserInfo();
		// TODO Auto-generated constructor stub
	}

	private static UserConfiguration mConfiguration;

	/**
	 * 单例初始化account配置文件
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public static UserConfiguration getConfiguration() {

		if (mConfiguration == null) {

			mConfiguration = new UserConfiguration(PREFERENCE_NAME);
		}
		return mConfiguration;
	}

	/**
	 * 获取当前已经存在的用户名密码、及其他用户信息
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public UserInfo getLocalUserInfo() {
		return App.userInfo = getUserByConfig();
	}

	private UserInfo getUserByConfig() {
		UserInfo userInfo = (UserInfo)readObject(App.getInstance(), "userInfo");
//		account.setUserName(getString(USERNAME, ""));
//		account.setPassword(getString(PASSWORD, ""));
//		account.setuId(getString(UID, ""));
//		account.setAddress(getString(ADDRESS, ""));
//		account.setCardType(getString(CARDTYPE, ""));
//		account.setEmail(getString(EMAIL, ""));
//		account.setEmailActive(getString(EMAILACTIVE, ""));
//		account.setIdcard(getString(IDCARD, ""));
//		account.setMobile(getString(MOBILE, ""));
//		account.setMobileActive(getString(MOBILEACTIVE, ""));
//		account.setPassword(getString(PASSWORD, ""));
//		account.setPaypasswd(getString(PAYPASSWD, ""));
//		account.setRealName(getString(REALNAME, ""));
//		account.setRegisterDt(getString(REGISTERDT, ""));
//		account.setStatus(getString(STATUS, ""));
//		account.setUserType(getString(USERTYPE, ""));
//		account.setRem(Boolean.parseBoolean(getString(ISREM, "")));
//		account.setUnionUserId(getInt(UNIONUSERID, 0));
		return userInfo;
	}

	/**
	 * 保存account元素
	 * 
	 * @Description:
	 * @param account
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public void saveUserConfig(UserInfo userInfo) {
//		putString(USERNAME, account.getUserName());
//		putString(PASSWORD, account.getPassword());
//		putString(UID, account.getuId());
//		putString(USERTYPE, account.getUserType());
//		putString(PAYPASSWD, account.getPaypasswd());
//		putString(MOBILE, account.getMobile());
//		putString(MOBILEACTIVE, account.getMobileActive());
//		putString(CARDTYPE, account.getCardType());
//		putString(EMAIL, account.getEmail());
//		putString(EMAILACTIVE, account.getEmailActive());
//		putString(STATUS, account.getStatus());
//		putString(REGISTERDT, account.getRegisterDt());
//		putString(REALNAME, account.getRealName());
//		putString(ADDRESS, account.getAddress());
//		putString(IDCARD, account.getIdcard());
//		putString(ISREM, account.getRem() + "");
//		putInt(UNIONUSERID, account.getUnionUserId());
		saveObject(App.getInstance(), "userInfo", userInfo);
	}

	/**
	 * 修改某一元素值
	 * 
	 * @Description:
	 * @param key
	 * @param value
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public void updateUserInfo(String key, String value) {
		putString(key, value);
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		super.clear();
	}

	@Override
	protected Context getContext() {
		// TODO Auto-generated method stub
		return App.getInstance();
	}
}
