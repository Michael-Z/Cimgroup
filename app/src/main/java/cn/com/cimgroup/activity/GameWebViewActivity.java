package cn.com.cimgroup.activity;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.com.cimgroup.xutils.WebViewUtils;

/**
 * 游戏Web页面
 * 
 * @author 秋风
 * 
 */
@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class GameWebViewActivity extends BaseActivity implements
		OnClickListener {
	private WebView mWebView;
	/** 访问网址 */
	private String url;
	/** 标题 */
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview_game);
		url = getIntent().getStringExtra("url");
		mTitle = getIntent().getStringExtra("title");
		if (mTitle.equals("幸运大转盘")) {
			initShape();
		}
		initView();
		initWebViewSettings();
		mWebView.loadUrl(url);
		initEvent();
	}

	private boolean isGuide;

	private void initShape() {
		SharedPreferences sp = getSharedPreferences(
				GlobalConstants.PATH_SHARED_MAC, MODE_PRIVATE);
		isGuide = sp.getBoolean("isGameGuideLuckyPan", true);
		String disport_roletable_guide = "1";
		if (isGuide) {
			disport_roletable_guide = "0";
			Editor editor = sp.edit();
			editor.putBoolean("isGameGuideLuckyPan", false);
			editor.commit();
		}
		String integralAcnt = App.userInfo == null
				|| TextUtils.isEmpty(App.userInfo.getIntegralAcnt()) ? "0"
				: App.userInfo.getIntegralAcnt();
		WebViewUtils.synCookies(mActivity, url, "Client=lebo",
				"disport_roletable_guide=" + disport_roletable_guide,
				"integralAcnt=" + integralAcnt);

	}

	private void initEvent() {
		findViewById(R.id.textView_title_back).setOnClickListener(this);

	}

	/**
	 * 初始化WebView的设置
	 */
	private void initWebViewSettings() {
		// 设置可以访问文件
		mWebView.getSettings().setAllowFileAccess(true);
		// 如果访问的页面中有Javascript，则webview必须设置支持Javascript
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 修改ua使得web端正确判断
		String ua = mWebView.getSettings().getUserAgentString();
		mWebView.getSettings().setUserAgentString(
				ua.replace("Android", "HFWSH_USER Android"));

		// 尝试缓存

		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.getSettings().setDatabaseEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		// 设置编码
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.getSettings().setBuiltInZoomControls(true);
		// 设置背景颜色 透明
		mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		// 设置本地调用对象及其接口
		/*** 打开本地缓存提供JS调用 **/
		mWebView.getSettings().setDomStorageEnabled(true);
		// 设置缓存大小
		mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getApplicationContext().getCacheDir()
				.getAbsolutePath();
		mWebView.getSettings().setAppCachePath(appCachePath);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setAppCacheEnabled(true);

		// 支持用户输入获取手势焦点
		mWebView.requestFocusFromTouch();
		// // 设置本地加载网页
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				isPageFinished = true;
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.addJavascriptInterface(new JSBridge(), "leboapp");
	}

	private boolean isPageFinished = false;

	/**
	 * 浏览器调用方法
	 * 
	 * @param message
	 */
	public class JSBridge {
		@JavascriptInterface
		public void toastMessage(String message) {
			// 测试使用
			Toast.makeText(getApplicationContext(),
					"通过Natvie传递的Toast:" + message, Toast.LENGTH_LONG).show();
		}

		@JavascriptInterface
		public void gameBettingFunction(String info) {
			if (App.userInfo != null) {
				if (!TextUtils.isEmpty(info)) {
					try {
						JSONObject object = new JSONObject(info);
						String betAmount = object.optString("betAmount", "");
						BigDecimal money = new BigDecimal(
								TextUtils.isEmpty(App.userInfo
										.getIntegralAcnt()) ? "0"
										: App.userInfo.getIntegralAcnt());
						if (money.intValue() > Integer.parseInt(betAmount)) {
							String gameId = object.optString("gameId", "");
							String leagueCode = object.optString("leagueCode",
									"");
							String questionId = object.optString("questionId",
									"");
							String content = object.optString("content", "");
							String transactionType=object.optString("transactionType","10110002");

							// Log.e("qiufeng",
							// "gameId:"+gameId+"__leagueCode:"+leagueCode+"___questionId:"+questionId+"___content"+content+"___betAmount:"+betAmount);
							Controller.getInstance().gameBetting(
									transactionType,
									App.userInfo.getUserId(), gameId,
									leagueCode, questionId, content, betAmount,
									new CallBack() {

										@Override
										public void resultSuccessStr(
												final String json) {
											runOnUiThread(new Runnable() {

												@Override
												public void run() {
													mWebView.loadUrl("javascript:appInteractive.betSuccessCallback("
															+ json + ")");
													getResultJson(json);
												}
											});

										}

										@Override
										public void resultFailure(
												final String error) {
											super.resultFailure(error);
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													mHandler.sendEmptyMessage(CANCEL_TOPAST);
													ToastUtil.shortToast(
															mActivity, error);

												}
											});
										}
									});
						} else {
							ToastUtil.shortToast(mActivity, "乐米不足，请去充值");
							mHandler.sendEmptyMessage(CANCEL_TOPAST);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mHandler.sendEmptyMessage(CANCEL_TOPAST);
						startActivity(LoginActivity.class);
					}
				});
			}
		}

		/**
		 * 乐米充值
		 */
		@JavascriptInterface
		public void lemiRecharge() {
			exchargeLemi();
		}
	}

	private void getResultJson(String json) {
		try {

			if (!TextUtils.isEmpty(json)) {
				JSONObject object = new JSONObject(json);
				String resCode = object.optString("resCode", "");
				if (!TextUtils.isEmpty(resCode) && resCode.equals("0")) {
					App.userInfo.setBetAcnt(object.getString("betAcnt"));
					App.userInfo.setIntegralAcnt(object
							.getString("integralAcnt"));
					UserLogic.getInstance().saveUserInfo(App.userInfo);
				} else {
					mHandler.sendEmptyMessage(CANCEL_TOPAST);
					ToastUtil.shortToast(mActivity, object.optString("resMsg"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mWebView = (WebView) findViewById(R.id.id_webview);
		((TextView) findViewById(R.id.textView_title_word)).setText(mTitle);
	}

	@Override
	public void onPause() {
		super.onPause();
		// mWebView.reload();
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		} else {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出网页",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WebViewUtils.removeCookie(mActivity);
		if (mWebView != null) {
			mWebView.destroy();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (App.userInfo != null && isPageFinished) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String integralAcnt = App.userInfo.getIntegralAcnt();
					mWebView.loadUrl("javascript:appInteractive.updateIntegralAcnt('"
							+ integralAcnt + "')");
				}
			});

		}

	}

	private final static int CANCEL_TOPAST = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CANCEL_TOPAST:
				mWebView.loadUrl("javascript:appInteractive.cancelLoadingToast()");
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 兑换乐米
	 */
	private void exchargeLemi() {
		if (App.userInfo != null) {
			Intent convertIntent = new Intent(GameWebViewActivity.this,
					MessageCenterActivity.class);
			convertIntent.putExtra(MessageCenterActivity.TYPE,
					MessageCenterActivity.LEMICONVERT);
			startActivity(convertIntent);
		} else {
			startActivity(LoginActivity.class);
		}

	}

}
