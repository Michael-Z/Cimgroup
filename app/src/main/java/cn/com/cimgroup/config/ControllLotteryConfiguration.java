package cn.com.cimgroup.config;

import android.content.Context;
import cn.com.cimgroup.App;
import cn.com.cimgroup.bean.ControllLottery;
import cn.com.cimgroup.bean.Upgrade;
import cn.com.cimgroup.xutils.PreferenceUtil;

public class ControllLotteryConfiguration extends PreferenceUtil {
	
	private static final String PREFERENCE_NAME = "common_controll";
	
	private static ControllLotteryConfiguration mConfiguration;
	
	private ControllLottery controll;
	
	public ControllLotteryConfiguration(String name) {
		super(PREFERENCE_NAME);
		// TODO Auto-generated constructor stub
		controll = new ControllLottery();
	}
	
	public static ControllLotteryConfiguration getConfiguration() {

		if (mConfiguration == null) {

			mConfiguration = new ControllLotteryConfiguration(PREFERENCE_NAME);
		}
		return mConfiguration;
	}
	
	public ControllLottery getLocalControllInfo() {
		return controll = getControllByConfig();
	}

	private ControllLottery getControllByConfig() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ControllLottery controll = (ControllLottery) readObject(App.getInstance(), "controllinfo");
		return controll;
	}
	
	public void saveControllConfig(ControllLottery controll) {
		saveObject(App.getInstance(), "controllinfo", controll);
	}
	
	public void clear() {
		super.clear();
	}

	@Override
	protected Context getContext() {
		// TODO Auto-generated method stub
		return App.getInstance();
	}

}
