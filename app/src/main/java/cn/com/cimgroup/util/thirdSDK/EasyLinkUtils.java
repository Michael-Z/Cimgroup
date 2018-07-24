package cn.com.cimgroup.util.thirdSDK;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.activity.CenterReChargeActivity;
import cn.com.cimgroup.activity.EasyLinkBankActivity;
import cn.com.cimgroup.config.UserConfiguration;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.com.cimgroup.xutils.XLog;

public class EasyLinkUtils {
	
	/**
	 * @Fields payecoPayBroadcastReceiver : 易联支付插件广播
	 */
	public static BroadcastReceiver payecoPayBroadcastReceiver;
	
	private static long startTime = 0;
	
	/**
	 * @Title registerPayecoPayBroadcastReceiver 
	 * @Description 注册广播接收器
	 */
	public static void registerPayecoPayBroadcastReceiver(Context context) {
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConstants.BROADCAST_PAY_END);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		context.registerReceiver(payecoPayBroadcastReceiver, filter);
	}

	/**	
	 * @Title unRegisterPayecoPayBroadcastReceiver 
	 * @Description 注销广播接收器
	 */
	public static void unRegisterPayecoPayBroadcastReceiver(Context context) {
		
		if (payecoPayBroadcastReceiver != null) {
			context.unregisterReceiver(payecoPayBroadcastReceiver);
			payecoPayBroadcastReceiver = null;
		}
	}
	
	
	//初始化支付结果广播接收器
	public static void initPayecoPayBroadcastReceiver(Context context) {
		payecoPayBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				
				//接收易联支付插件的广播回调
				String action = intent.getAction();
				if (!GlobalConstants.BROADCAST_PAY_END.equals(action)) {
					XLog.e("接收到广播，但与注册的名称不一致["+action+"]");
					return ;
				}
				
				//商户的业务处理
				//{"respDesc":"T431:银行卡允许的输入密码次数超限，请核实后次日再重新支付","Status":"05","MerchantId":"502050000209","PayTime":"","Amount":"0.01","Version":"2.0.0","SettleDate":"20151127","OrderId":"502015112700214739","MerchOrderId":"201511271113450000000008","ExtData":"cmVjaGFyZ2U=","respCode":"T431","Sign":"DNASwrTvgB7gukDyyHgpaK08gjaVWGOlQmRDNRE5fEXLcnw4tnKNchCiUTOY8VhyijtWZhvMdhS1AmYMkX7trT383LF7uL+KoJQ6uJhHNuj8+HPJYRWL2YXhdXd00PU77suPwTcIznwk8+8UKuvSjdvxBjVUdfteuhGAgo8cfvw="}
				//{"respDesc":"交易成功","Status":"02","MerchantId":"502050000209","PayTime":"20151127113145","Amount":"0.01","Version":"2.0.0","SettleDate":"20151127","OrderId":"502015112700214749","MerchOrderId":"201511271126330000000002","ExtData":"cmVjaGFyZ2U=","respCode":"0000","Sign":"SLhSQjajZXeB2suKcMyiPxXMVbruteep2f5N4r86PuwQw4IaGgUVlyC8RlhQLUAzm6nAefpcO4w6k4Z/v5qEHUz75RuE/Ir6CLtT7oDUI6abLeVqEzkGJt9Rd+Wor7//D1a1O/7V+aEWmSBKWRU4IIpiECXT8jzu16dbxpf8yJA="}
				String result = intent.getExtras().getString("upPay.Rsp");
				XLog.i("接收到广播内容："+result);
				try {
					JSONObject json = new JSONObject(result);
					if (json.optString("respCode").equals("0000")) {
						EasyLinkBankActivity easyLinkBankActivity = (EasyLinkBankActivity) ActivityManager
								.isExistsActivity(EasyLinkBankActivity.class);
						if (easyLinkBankActivity != null) {
//							ActivityManager.pop(easyLinkBankActivity);
							easyLinkBankActivity.finish();
						}
						
						CenterReChargeActivity rechargeActivity = (CenterReChargeActivity) ActivityManager
								.isExistsActivity(CenterReChargeActivity.class);
						if (rechargeActivity != null) {
//							ActivityManager.pop(rechargeActivity);
							rechargeActivity.finish();
						}
						
						long currentTime = System.currentTimeMillis();
						if (currentTime - startTime > 1000) {
							App.userInfo.setBetAcnt((Double.parseDouble(App.userInfo.getBetAcnt()) + Double.parseDouble(json.optString("Amount"))) + "");
							UserConfiguration.getConfiguration().saveUserConfig(App.userInfo);
							startTime = currentTime;
						}
					}
					ToastUtil.shortToast(context, json.optString("respDesc"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
}
