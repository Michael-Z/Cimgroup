package cn.com.cimgroup.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 微信登录支持回调页面
 * 
 * @author 秋风
 * 
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, App.wxAppId);
        api.handleIntent(getIntent(), this);
		// appId = "wx12250ad177d4b087";
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@SuppressWarnings("unused")
	@Override
	public void onResp(BaseResp resp) {

		Bundle bundle = new Bundle();
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			if (resp instanceof SendAuth.Resp) {
				// 可用以下两种方法获得code
				String code = ((SendAuth.Resp) resp).code;
				// 判断拉起授权的页面
				App.setWxCode(code);
				Intent intent=new Intent();
				intent.setAction("com.lebocp.wxlogin");
				sendBroadcast(intent);
				finish();
			} else if (resp instanceof SendMessageToWX.Resp) {
				if (App.userInfo != null) {
					Controller.getInstance().shareAddLeMi(
							GlobalConstants.NUM_SHAREADDLEMI,
							App.userInfo.getUserId(), "0", "", mBack);
				}
			}
		} else if (resp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED
				|| resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
			// 用户取消登陆 显示前一个页面
			if (resp instanceof SendAuth.Resp) {

				App.userInfo = null;
				UserLogic.getInstance().saveUserInfo(App.userInfo);
			} else if (resp instanceof SendMessageToWX.Resp) {

			}

			finish();
		}

	}

	private CallBack mBack = new CallBack() {
		public void shareAddLeMiSuccess(final ShareAddLeMiObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						App.userInfo
								.setIntegralAcnt((Double
										.parseDouble(App.userInfo
												.getIntegralAcnt()) + Double
										.parseDouble(obj.getAddIntegral()))
										+ "");
						UserLogic.getInstance().saveUserInfo(App.userInfo);
					}
					finish();
				}
			});
		};

		public void shareAddLeMiFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(WXEntryActivity.this, error);
					finish();
				}
			});
		};
	};
}
