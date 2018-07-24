package cn.com.cimgroup.xutils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ta.utdid2.android.utils.StringUtils;

import cn.com.cimgroup.App;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * 网络工具类
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2013-7-24
 */
public class NetUtil {

	public static final int NETTYPE_WIFI = 1;
	public static final int NETTYPE_CMWAP = 2;
	public static final int NETTYPE_CMNET = 3;

	public static ConnectivityManager getConnectivityManager(Context c) {
		return (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	private static WifiLock wifilock = null;

	private static NetworkInfo netState;

	public static boolean isNetAvailable(Context c) {
		NetworkInfo info = getNetworkInfo(c);
		return (null != info && info.isAvailable());
	}

	public static NetworkInfo getNetworkInfo(Context c) {

		netState = getConnectivityManager(c).getActiveNetworkInfo();
		return netState;
	}

	/**
	 * 使用Wifi连接, wifi锁
	 */
	public static void startUseWifi(Context c) {
		WifiManager wifimanager = (WifiManager) c
				.getSystemService(Context.WIFI_SERVICE);
		if (wifimanager != null) {
			wifilock = wifimanager.createWifiLock(WifiManager.WIFI_MODE_FULL,
					"meq");
			if (wifilock != null) {
				wifilock.acquire();
			}
		}
	}

	/**
	 * 释放wifi锁
	 */
	public static void stopUseWifi() {
		if (wifilock != null && wifilock.isHeld()) {
			wifilock.release();
		}
		wifilock = null;
	}

	/**
	 * 是否存在已经连接上的网络, 不论是wifi/cmwap/cmnet还是其他
	 * 
	 * @return
	 */
	public static boolean isConnected(Context c) {
		NetworkInfo ni = getConnectivityManager(c).getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}

	/**
	 * @description wifi 是否连接或正在连接
	 */
	public static boolean isWifiConnecting(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			if (wifi == State.CONNECTING || wifi == State.CONNECTED) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @description 判断连接的网络是否是2g/3g
	 */
	public static boolean isMobileConnecting(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			if (wifi != State.CONNECTED) {
				State mobile = conMan.getNetworkInfo(
						ConnectivityManager.TYPE_MOBILE).getState();
				if (mobile == State.CONNECTED) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}

	/**
	 * 获取当前的网络状态 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 * 
	 * @param context
	 * @return
	 */

	public static int getNetworkType(Context context) {
		int netWorkType = 0;
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netWorkType;
		}
		netWorkType = networkInfo.getType();
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netWorkType = NETTYPE_CMNET;
				} else {
					netWorkType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netWorkType = NETTYPE_WIFI;
		}
		return netWorkType;
	}

	/**
	 * 关闭当前的移动网络
	 * @param context
	 */
	public static void closeUsertNet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Method setMobileDataEnabl;
		try {
			setMobileDataEnabl = connectivityManager.getClass()
					.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			setMobileDataEnabl.invoke(connectivityManager, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
