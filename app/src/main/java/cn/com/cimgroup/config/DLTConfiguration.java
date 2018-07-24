package cn.com.cimgroup.config;

import org.json.JSONObject;

import android.content.Context;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.bean.LotterySaveObj;
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
public class DLTConfiguration extends PreferenceUtil {

	public DLTConfiguration(String name) {
		super(GlobalConstants.NUM_LOTTERY_FILE_NAME);
		// TODO Auto-generated constructor stub
	}

	private static DLTConfiguration mConfiguration;

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
	public static DLTConfiguration getConfiguration() {

//		if (mConfiguration == null) {

			mConfiguration = new DLTConfiguration(GlobalConstants.NUM_LOTTERY_FILE_NAME);
//		}
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
	public LotterySaveObj getLocalUserInfo() {
		return getUserByConfig();
	}

	private LotterySaveObj getUserByConfig() {
		LotterySaveObj obj = (LotterySaveObj)readObject(App.getInstance(), "lottery_file");
		return obj;
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
	public void saveUserConfig(Object obj) {
		saveObject(App.getInstance(), "lottery_file", obj);
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
