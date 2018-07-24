package cn.com.cimgroup.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.DaemonService;
import cn.jpush.android.service.PushService;


public class BootUpReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
//			Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
//	        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        context.startActivity(mainActivityIntent);
//			ToastUtil.longToast(context, "有消息了--------------");
//			Intent i = context.getPackageManager().getLaunchIntentForPackage("cn.com.cimgroup");
//			context.startActivity(i);
			JPushInterface.init(context); // 初始化 JPush
		}
	}
}
