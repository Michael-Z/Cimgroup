package cn.com.cimgroup.config;

import android.content.Context;
import cn.com.cimgroup.App;
import cn.com.cimgroup.bean.Upgrade;
import cn.com.cimgroup.xutils.PreferenceUtil;

public class UpgradeConfiguration extends PreferenceUtil {
	
	private static final String PREFERENCE_NAME = "common_upgrade";
	
	private static UpgradeConfiguration mConfiguration;
	
	private Upgrade upgrade;
	
	public UpgradeConfiguration(String name) {
		super(PREFERENCE_NAME);
		// TODO Auto-generated constructor stub
		upgrade = new Upgrade();
	}
	
	public static UpgradeConfiguration getConfiguration() {

		if (mConfiguration == null) {

			mConfiguration = new UpgradeConfiguration(PREFERENCE_NAME);
		}
		return mConfiguration;
	}
	
	public Upgrade getLocalUpgradeInfo() {
		return upgrade = getUpgradeByConfig();
	}

	private Upgrade getUpgradeByConfig() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Upgrade upgrade = (Upgrade) readObject(App.getInstance(), "upgradeinfo");
		return upgrade;
	}
	
	public void saveUpgradeConfig(Upgrade upgrade) {
		saveObject(App.getInstance(), "upgradeinfo", upgrade);
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
