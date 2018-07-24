package cn.com.cimgroup.logic;

import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.UserConfiguration;

/**
 * 用户信息操作类
 * @Description:
 * @author:zhangjf
 * @see:   
 * @since:      
 * @copyright © www.wenchuang.com
 * @Date:2014-12-29
 */
public class UserLogic {
	private static UserLogic logic;
	

	public static UserLogic getInstance() {
		if (logic == null) {
			logic = new UserLogic();
		}
		return logic;
	}
	
	/**
	 * 返回当前已经登录的用户信息
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public UserInfo getDefaultUserInfo(){
		UserInfo userInfo = UserConfiguration.getConfiguration().getLocalUserInfo();
		if(userInfo != null){
			return userInfo;
		}
		return null;
	}
	
	/**
	 * 保存用户信息
	 * @Description:
	 * @param account
	 * @return
	 * @see: 
	 * @since: 
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public void saveUserInfo(UserInfo userInfo){
		UserConfiguration.getConfiguration().saveUserConfig(userInfo);
	}
	
	/**
	 * 修改用户信息
	 * @Description:
	 * @param key
	 * @param value
	 * @see: 
	 * @since: 
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public void updateUserInfo(String key,String value){
		UserConfiguration.getConfiguration().updateUserInfo(key,value);
	}
	
	/**
	 * 清空用户信息
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author: zhangjf
	 * @date:2014-12-29
	 */
	public void clearUserInfo(){
		UserConfiguration.getConfiguration().clear();
	}
}
