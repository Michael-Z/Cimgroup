package cn.com.cimgroup.xutils;

import java.util.Locale;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalTools;

/**
 * 设备帮助类
 * 
 * @author 秋风
 * 
 */
public class AppUtils {
	/**
	 * 获取应用版本名称
	 */
	public static String getVersionName() {
		String versionName = "";
		try {
			versionName = App.getInstance().getPackageManager()
					.getPackageInfo(App.getInstance().getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取应用版本号
	 */
	public static String getVersionCode() {
		String versionCode = "";
		try {
			versionCode += App.getInstance().getPackageManager()
					.getPackageInfo(App.getInstance().getPackageName(), 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 移动设备国际识别码,是手机的唯一识别号码
	 */
	public static String getIMEI() {
		TelephonyManager ts = (TelephonyManager) App.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ts.getDeviceId();
	}

	/**
	 * 获取IMSI
	 */
	private static String getIMSI() {
		TelephonyManager ts = (TelephonyManager) App.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ts.getSubscriberId();
	}

	/**
	 * 获取macAddress
	 */
	public static String getMacAddress() {
		WifiManager wm = (WifiManager) App.getInstance().getSystemService(
				Context.WIFI_SERVICE);
		return wm.getConnectionInfo().getMacAddress();
	}

	/**
	 * 获取设备唯一识别码
	 */
	public static String getSign() {
		String imei = getIMEI();// 仅仅只对Android手机有效
		String macAddress = getMacAddress();
		return GlobalTools.md5Encrypt(macAddress + imei+"AAA");
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getPhoneType() {
		return Build.MODEL;
	}
	
}
