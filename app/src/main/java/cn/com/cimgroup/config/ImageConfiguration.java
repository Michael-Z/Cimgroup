package cn.com.cimgroup.config;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.com.cimgroup.App;
import cn.com.cimgroup.bean.HallAd;
import cn.com.cimgroup.bean.ImageObj;
import cn.com.cimgroup.xutils.PreferenceUtil;



public class ImageConfiguration extends PreferenceUtil {
	private static final String PREFERENCE_NAME = "common_image";
	
	private static ImageConfiguration mConfiguration;
	
	private HallAd ad;
	
	public ImageConfiguration(String name) {
		super(PREFERENCE_NAME);
		ad = new HallAd();
	}
	
	public static ImageConfiguration getConfiguration() {

		if (mConfiguration == null) {

			mConfiguration = new ImageConfiguration(PREFERENCE_NAME);
		}
		return mConfiguration;
	}
	
	public HallAd getLocalImageInfo() {
		return ad = getImageByConfig();
	}

	private HallAd getImageByConfig() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		HallAd ad = (HallAd) readObject(App.getInstance(), "imageinfo");
		return ad;
	}
	
	public void saveImageConfig(HallAd ad) {
		saveObject(App.getInstance(), "imageinfo", ad);
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
