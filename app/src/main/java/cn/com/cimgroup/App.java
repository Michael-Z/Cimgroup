package cn.com.cimgroup;

import java.util.Observable;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.UserLogic;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 整个项目全局上下文
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public class App extends Application {
	// 日志开关
	public final static Boolean DEBUG = true;
	/** application 全局实例 **/
	private static App mApp;

	public static String wxAppId = "";

	public static UserInfo userInfo;
	/** 推送监听观察者 */
	public static MyObservable mObservableListener;

	/** 微信支付后页面跳转标识--WECHAT_RECHARAGE=充值;WECHAT_COMMITPAY==支付 */
	public static String CALLBACK_ACTIVITY = "";

	public static String callForWXAuthority = "";

	public static App getInstance() {
		return mApp;
	}

	/** 用户是否是第一次展示弹出广告 **/
	private boolean isFirstShow;

	public boolean isFirstShow() {
		return isFirstShow;
	}

	public void setFirstShow(boolean isFirstShow) {
		this.isFirstShow = isFirstShow;
	}

	private static String wxCode = "-1";

	public static String getWxCode() {
		return wxCode;
	}

	public static void setWxCode(String wxCode) {
		App.wxCode = wxCode;
	}

	@Override
	public void onCreate() {
		isFirstShow = true;
		mApp = this;
		// //如果存在已经解压的网络下载的h5文件，且没有替换原有文件，那么进行替换
		// Controller.getInstance().overOldH5Resource();
		// //assets的h5页面解压到data/data/package/cache中 且进行最新版h5模块更新
		// LotteryWebpageFileUtil.getInstance().init(3);
		// //图片下载及本地缓存（可处理高并发）
		// RequestManager.init(this);
		getUserInfo();
		super.onCreate();
		initImageLoader(this);
		JPushInterface.setDebugMode(DEBUG); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		mNotificationManager.cancelAll();
		if (mObservableListener == null) {
			mObservableListener = new MyObservable();
		}
	}

	/**
	 * 从本地读取userinfo
	 */
	private void getUserInfo() {
		userInfo = UserLogic.getInstance().getDefaultUserInfo();
	}

	// 加载imageloader
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// //将保存的时候的URI名称用MD5 加密
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 监听消息推送
	 */
	public class MyObservable extends Observable {

		public void update() {
			setChanged();
			notifyObservers();
		}

		public void update(Object response) {
			setChanged();
			notifyObservers(response);
		}

	}

}
