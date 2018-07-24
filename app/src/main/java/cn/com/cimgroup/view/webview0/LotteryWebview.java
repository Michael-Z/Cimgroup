package cn.com.cimgroup.view.webview0;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

/**
 * 彩票webveiw 
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright © www.wenchuang.com
 * @Date:2014-12-7
 */
public class LotteryWebview extends WebView {

	private WebSettings mWebSetting = null;

	public LotteryWebview(Context context) {
		super(context);
		mWebSetting = super.getSettings();
		// TODO Auto-generated constructor stub
	}

	public LotteryWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mWebSetting = super.getSettings();
		// TODO Auto-generated constructor stub
	}

	public void loadSettings() {
		if (mWebSetting == null) {
			mWebSetting = getSettings();
		}
		// 支持javascript
		mWebSetting.setJavaScriptEnabled(true);
		// 使用localStorage则必须打开
		mWebSetting.setDomStorageEnabled(true);
		// 设置数据库路径
		// mWebSetting.setDatabasePath("");/
		// 支持变焦
		mWebSetting.setSupportZoom(true);
		// UTF-8编码
		mWebSetting.setDefaultTextEncodingName("UTF-8");
		;// 支持通过JS打开新窗口
		mWebSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		// 支持缩放
		mWebSetting.setSupportZoom(true);
		// 支持自动加载图片
		mWebSetting.setLoadsImagesAutomatically(true);
		// 支持缩放
		setInitialScale(35);
		// 启用地理定位
		mWebSetting.setRenderPriority(RenderPriority.HIGH);
		mWebSetting.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
		// 启用WebView访问文件数据
		mWebSetting.setAllowFileAccess(true);
		// 打开H5缓存
		mWebSetting.setAppCacheEnabled(true);
		String appCacheDir = getSDPath() + "/android/h5";
		if (!new File(appCacheDir).exists()) {
			new File(appCacheDir).mkdirs();
		}
		mWebSetting.setAppCachePath(appCacheDir);
		// 只要本地有缓存，都使用缓存。本地没有缓存时才从网络上获取
		mWebSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}
}
