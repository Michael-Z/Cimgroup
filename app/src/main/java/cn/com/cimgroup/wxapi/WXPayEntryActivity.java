package cn.com.cimgroup.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.cimgroup.App;
import cn.com.cimgroup.activity.CenterReChargeActivity;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, CenterReChargeActivity.appId);

		api.handleIntent(getIntent(), this);
		finish();
	}

	public void doBack(View v) {
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		// 支付成功
		if (resp == null) {
			return;
		}
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				Intent intent=new Intent();
				if (App.CALLBACK_ACTIVITY.equals("WECHAT_RECHARAGE")) {
					//充值返回 
					intent.setAction("com.lebocp.wxrecharge");
					intent.putExtra("state", "0");
					sendBroadcast(intent);
					
				}else if(App.CALLBACK_ACTIVITY.equals("COMMITPAY")){
					//支付返回 
					intent.setAction("com.lebocp.wxorderpay");
					intent.putExtra("state", "0");
					sendBroadcast(intent);
				}
				finish();
			} else {
				Intent intent=new Intent();
				if (App.CALLBACK_ACTIVITY.equals("WECHAT_RECHARAGE")) {
					//充值失败返回
					intent.setAction("com.lebocp.wxrecharge");
					intent.putExtra("state", "1");
					sendBroadcast(intent);
					
				}else if(App.CALLBACK_ACTIVITY.equals("COMMITPAY")){
					//支付失败返回
					intent.setAction("com.lebocp.wxorderpay");
					intent.putExtra("state", "1");
					sendBroadcast(intent);
				}
				finish();
			}
		}
	}
}