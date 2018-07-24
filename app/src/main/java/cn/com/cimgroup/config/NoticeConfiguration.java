package cn.com.cimgroup.config;

import android.content.Context;
import cn.com.cimgroup.App;
import cn.com.cimgroup.bean.HallNotice;
import cn.com.cimgroup.xutils.PreferenceUtil;

public class NoticeConfiguration extends PreferenceUtil {
	
	private static final String PREFERENCE_NAME = "common_notice";
	
	private static NoticeConfiguration mConfiguration;
	
	private HallNotice notice;
	
	public NoticeConfiguration(String name) {
		super(PREFERENCE_NAME);
		// TODO Auto-generated constructor stub
		notice = new HallNotice();
	}
	
	public static NoticeConfiguration getConfiguration() {

		if (mConfiguration == null) {

			mConfiguration = new NoticeConfiguration(PREFERENCE_NAME);
		}
		return mConfiguration;
	}
	
	public HallNotice getLocalNoticeInfo() {
		return notice = getNoticeByConfig();
	}

	private HallNotice getNoticeByConfig() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		HallNotice notice = (HallNotice) readObject(App.getInstance(), "noticeinfo");
		return notice;
	}
	
	public void saveNoticeConfig(HallNotice notice) {
		saveObject(App.getInstance(), "noticeinfo", notice);
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
