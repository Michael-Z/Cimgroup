package cn.com.cimgroup.util;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.activity.HtmlCommonActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LotteryDrawListActivity;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.activity.TzDetailActivity;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * JPush的工具类
 * 
 * @author 83920_000
 * 
 */
@SuppressLint("HandlerLeak")
public class JPushUtils {

	private static Context mContext;
	private static JPushUtils mInstance=null;
	/**获取工具类实例*/
	public static JPushUtils getInstance(Context context){
		mContext=context.getApplicationContext();
		if (null == mInstance) {
			synchronized (JPushUtils.class) {
			mInstance=new JPushUtils();	
			}
		}
		return mInstance;
	}
	
	private boolean mIsExit=false;
	/**
	 * 设置别名
	 * @param isExit是否是进行退出，true退出并清空别名，false设置别名
	 */
	public void setJPushAlias(boolean isExit){
		mIsExit=isExit;
		String alias="";
		if (!mIsExit) {
			if (GlobalConstants.isTest) {
				alias="test_"+((App.userInfo==null||App.userInfo.getUserId()==null)?"":App.userInfo.getUserId());	
			}else {
				alias=(App.userInfo==null||App.userInfo.getUserId()==null)?"":App.userInfo.getUserId();	
			}
		}
		JPushInterface.setAlias(mContext, alias, new TagAliasCallback() {
			@Override
			public void gotResult(int resultCode, String arg1, Set<String> arg2) {
				switch (resultCode) {
				case 0:
					//设置别名成功
					break;
				case 6002:
					//设置别名失败
					if (!mIsExit) {
						handler.sendEmptyMessageDelayed(SEND_ALIAS_ALIGN, 1000*6);
					}
					break;
				default:
					break;
				}
			}
		});
	}
	/**重新发送别名*/
	private static final int SEND_ALIAS_ALIGN=0;
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_ALIAS_ALIGN:
				setJPushAlias(mIsExit);
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 处理推送来的Message
	 * 
	 * @param context
	 * @param push_code
	 * @param message
	 */
	public static void parserMessage(Context context, String push_code, String message) {
		try {
			JSONObject code = new JSONObject(push_code);
			String pushCode = code.getString("pushCode");
			if (!TextUtils.isEmpty(pushCode)) {
				// 时时比分推送
				if (App.mObservableListener != null) {
					App.mObservableListener.update(message);
				}
			}
		} catch (JSONException e) {
		}
	}
	/**
	 * 处理推送来的通知
	 * 
	 * @param context
	 * @param push_code
	 * @param message
	 */
	public static void parserNotify(Context context, String extra, String message) {
		com.alibaba.fastjson.JSONObject extraObj = com.alibaba.fastjson.JSONObject.parseObject(extra);
		GlobalConstants.JPUSHEXTRA = "";
		GlobalConstants.JPUSHALERT = "";
		String type = extraObj.getString("type");
		if (!TextUtils.isEmpty(type)) {
			// 1、开奖	2、中奖	3、追号停止	4、活动	5、比分	6、关注比赛
			switch (type) {
			case "1":
				String gameNo = extraObj.getString("gameNo");
				setDrawLotteryJump(context, gameNo);
				break;
			case "2":
				if(App.userInfo == null) {
					GlobalConstants.JPUSHEXTRA = extra;
					GlobalConstants.JPUSHALERT = message;
					Intent intentx = new Intent(context, LoginActivity.class);
					intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intentx);
				} else {
					String userId = extraObj.getString("userId");
					if (App.userInfo.getUserId().equals(userId)) {
						Intent intent = new Intent(context, TzDetailActivity.class);
						Bundle bundle0 = new Bundle();
						bundle0.putString("orderId", extraObj.getString("orderId"));
						bundle0.putString("userId", userId);
						bundle0.putString("gameNo", extraObj.getString("gameNo"));
						bundle0.putBoolean("isZhui", false);
						intent.putExtras(bundle0);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
					} else {
						
					}
				}
				break;
			case "3":
				if(App.userInfo == null) {
					GlobalConstants.JPUSHEXTRA = extra;
					GlobalConstants.JPUSHALERT = message;
					Intent intentx = new Intent(context, LoginActivity.class);
					intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intentx);
				} else {
					String userId = extraObj.getString("userId");
					if (App.userInfo.getUserId().equals(userId)) {
						Intent intent1 = new Intent(context, TzDetailActivity.class);
						Bundle bundle1 = new Bundle();
						bundle1.putString("orderId", extraObj.getString("orderId"));
						bundle1.putString("userId", userId);
						bundle1.putString("gameNo", extraObj.getString("gameNo"));
						bundle1.putBoolean("isZhui", true);
						intent1.putExtras(bundle1);
						intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent1);
					} else {
						
					}
				}
				break;
			case "6":
				if(App.userInfo == null) {
					GlobalConstants.JPUSHEXTRA = extra;
					GlobalConstants.JPUSHALERT = message;
					Intent intentx = new Intent(context, LoginActivity.class);
					intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intentx);
				} else {
					String userId = extraObj.getString("userId");
					if (App.userInfo.getUserId().equals(userId)) {
						Intent intent3 = new Intent(context, ScoreDetailActivity.class);
						Bundle bundle3 = new Bundle();
						bundle3.putString("matchId", extraObj.getString("matchId"));
						bundle3.putString("userId", userId);
						bundle3.putString("lotteryNo", extraObj.getString("lotteryNo"));
						intent3.putExtras(bundle3);
						intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent3);
					} else {
						
					}
				}
				break;
			case "4":
				//loging 是否登录  0、不需要。1、需要
				//jumpType 跳转类型 0、页面。1、文字
				String url = extraObj.getString("url");
				String loging = extraObj.getString("loging");
				String jumpType = extraObj.getString("jumpType");
				if (loging.equals("1") && App.userInfo == null) {
					GlobalConstants.JPUSHEXTRA = extra;
					GlobalConstants.JPUSHALERT = message;
					Intent intentx = new Intent(context, LoginActivity.class);
					intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intentx);
				} else {
					Intent intent2 = new Intent(context, HtmlCommonActivity.class);
					if (jumpType.equals("0")) {
						Bundle bundle2 = new Bundle();
						bundle2.putString("webUrl", url);
						bundle2.putBoolean("isWeb", true);
						intent2.putExtras(bundle2);
					} else {
						Bundle bundle2 = new Bundle();
						bundle2.putString("content", message);
						bundle2.putBoolean("isWeb", false);
						intent2.putExtras(bundle2);
					}
					intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent2);
				}
			default:
				break;
			}
		}
		
	}
	
	/**
	 * 设置开奖彩种跳转
	 * @Description:
	 * @param gameNo
	 * @author:www.wenchuang.com
	 * @date:2016-12-9
	 */
	private static void setDrawLotteryJump(Context context, String gameNo) {
		LotteryDrawInfo info = new LotteryDrawInfo();
		info.setGameNo(gameNo);
		switch (gameNo) {
		case GlobalConstants.TC_DLT:
			info.setGameName("大乐透");
			break;
		case GlobalConstants.TC_PL3:
			info.setGameName("排列三");
			break;
		case GlobalConstants.TC_PL5:
			info.setGameName("排列五");
			break;
		case GlobalConstants.TC_QXC:
			info.setGameName("七星彩");
			break;
		case GlobalConstants.TC_SF14:
			info.setGameName("胜负彩");
			break;
		case GlobalConstants.TC_SF9:
			info.setGameName("任九场");
			break;
		case GlobalConstants.TC_BQ6:
			info.setGameName("半全场");
			break;
		case GlobalConstants.TC_JQ4:
			info.setGameName("进球彩");
			break;
		default:
			break;
		}
		Intent intent = new Intent(context, LotteryDrawListActivity.class);
		intent.putExtra("LotteryDrawInfo", info);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
		context.startActivity(intent);
	}
}
