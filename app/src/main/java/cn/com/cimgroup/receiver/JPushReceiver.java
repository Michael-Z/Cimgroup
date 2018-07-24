package cn.com.cimgroup.receiver;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.HtmlCommonActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LotteryDrawListActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.activity.TzDetailActivity;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.util.JPushUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * 激光推送Receiver
 * 
 * @author 秋风
 * 
 */
public class JPushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
//		Log.d("qiufeng", "[MyReceiver] onReceive - " + intent.getAction()
//				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//			Log.d("qiufeng", "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {

			if (bundle != null) {
				if (bundle != null) {
					String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
					String push_code = bundle.getString(JPushInterface.EXTRA_EXTRA);
					parserMessage(context, push_code, message);
				}
			}
			//			Log.d("qiufeng",
//					"[MyReceiver] 接收到推送下来的自定义消息: "
//							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// Log.e("qiufeng", "bundle:"+bundle.getString(key));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
//			Log.d("qiufeng", "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//			Log.d("qiufeng", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//			 String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			 Log.e("qiufeng", "extras:"+extras);
//			receivingNotification(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d("qiufeng", "[MyReceiver] 用户点击打开了通知");
			// 打开自定义的Activity
//			Intent i = new Intent(context, MessageCenterActivity.class);
//			i.putExtras(bundle);
//			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			context.startActivity(i);
			
			if (bundle != null) {
				String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
				String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
				JPushUtils.parserNotify(context, extra, alert);
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.e("qiufeng",
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
//			boolean connected = intent.getBooleanExtra(
//					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//			Log.e("qiufeng", "[MyReceiver]" + intent.getAction()
//					+ " connected state change to " + connected);
		} else {
//			Log.e("qiufeng","[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
//					Log.e("qiufeng", "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
//					Log.e("qiufeng", "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	/**
	 * 处理推送来的数据
	 * 
	 * @param context
	 * @param push_code
	 * @param message
	 */
	private void parserMessage(Context context, String push_code, String message) {
//		Log.e("qifueng", message);
		try {
			JSONObject code=new JSONObject(push_code);
			String pushCode=code.getString("pushCode");
			if (!TextUtils.isEmpty(pushCode)) {
				// 时时比分推送
				if (App.mObservableListener!=null) {
					App.mObservableListener.update(message);
				}
			}
		} catch (JSONException e) {
		}
		
	}
	

	// // send msg to MainActivity
	// private void processCustomMessage(Context context, Bundle bundle) {
	// if (PushActivity.isForeground) {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Log.e("qiufeng", message);
	// // Intent msgIntent = new Intent(PushActivity.MESSAGE_RECEIVED_ACTION);
	// // msgIntent.putExtra(PushActivity.KEY_MESSAGE, message);
	// // if (!ExampleUtil.isEmpty(extras)) {
	// // try {
	// // JSONObject extraJson = new JSONObject(extras);
	// // if (null != extraJson && extraJson.length() > 0) {
	// // msgIntent.putExtra(PushActivity.KEY_EXTRAS, extras);
	// // }
	// // } catch (JSONException e) {
	// //
	// // }
	// //
	// // }
	// // context.sendBroadcast(msgIntent);
	// }
	// }
}
