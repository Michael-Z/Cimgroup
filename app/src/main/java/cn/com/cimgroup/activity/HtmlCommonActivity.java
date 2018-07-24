package cn.com.cimgroup.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare.PlayMenuItemClick;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.AppUtils;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.com.cimgroup.xutils.WebViewUtils;
import cn.com.cimgroup.xutils.XLog;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class HtmlCommonActivity extends BaseActivity implements OnClickListener,PlayMenuItemClick {
	private String localUrl = "file:///android_asset/";
	private String endUrl = "";
	private String title = "";
	private WebView mWebView;
	private boolean isWeb;
	private TextView mTitleName;
	private TextView mBack;
	private ImageView share;
	private String url;
	private int lotteryName;
	private String content;
	private String imageUrl;
	private boolean isInfo = false;
	//是否进入二层详情页面，如果是，则需要再次请求js方法获取数据再分享，否则直接进入分享
	private boolean isEnter2Detail = false;
	/** 分享渠道Pop */
	private PopupWndSwitchShare mPopMenu;
	private int mType;
	
	private ShareUtils shareUtils = new ShareUtils();
	
	private LinearLayout emptyLayout;
	private ImageView emptyImage;
	private TextView oneText;
	private TextView twoText;
	private TextView button;
	
	private boolean isLoginBack = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feature_html_common);
		
		Intent intent = getIntent();
		
		isWeb = intent.getBooleanExtra("isWeb", false);
		url = intent.getStringExtra("webUrl");
		title = intent.getStringExtra("title");
		lotteryName = intent.getIntExtra("lotteryName", 0);
		content = intent.getStringExtra("content");
		isInfo = intent.getBooleanExtra("isInfo", false);
		imageUrl = intent.getStringExtra("imageUrl");
		
		initView();
		initWebViewSettings();
		initEvent();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StrictMode.setThreadPolicy(new ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new VmPolicy.Builder().penaltyLog().penaltyDeath().build());
//		share.setVisibility(View.VISIBLE);
		if (isLoginBack) {
			WebViewUtils.synCookies(this, url, "Client=lebo");
			WebViewUtils.synCookies(this, url, GlobalConstants.JSESSIONID);
			String userid = "";
			if (App.userInfo != null) {
				userid = "userId=" + App.userInfo.getUserId();
			} else {
				userid = "userId=" + "";
			}
			WebViewUtils.synCookies(this, url, userid);
			WebViewUtils.synCookies(this, url, "version=" + AppUtils.getVersionName().substring(1, AppUtils.getVersionName().length()));
			mWebView.loadUrl(url);
			isLoginBack = false;
		}
		
	}

	@SuppressLint("NewApi")
	private void initView() {
		// title 左侧的文字
		mTitleName = (TextView) findViewById(R.id.textView_html_word);
		
		mBack = (TextView) findViewById(R.id.textView_html_back);
		
		share = (ImageView) findViewById(R.id.imageView_html_share);
		
		mWebView = (WebView) findViewById(R.id.webView_feature_common);
		
		emptyLayout = (LinearLayout) findViewById(R.id.html_common_emptyView);
		
		emptyImage = (ImageView) findViewById(R.id.imageView_load_empty);
		
		oneText = (TextView) findViewById(R.id.textView_load_one);
		
		twoText = (TextView) findViewById(R.id.textView_load_two);
		
		button = (TextView) findViewById(R.id.button_load);
		
		emptyImage.setImageResource(R.drawable.icon_nodata_nowifi);
		oneText.setText(getResources().getString(R.string.nodata_no_net));
		twoText.setText(getResources().getString(R.string.nodata_no_net1));
		button.setText(getResources().getString(R.string.nodata_no_net_btn));
		button.setTextColor(getResources().getColor(R.color.color_gray_indicator));
		button.setBackground(getResources().getDrawable(R.drawable.selector_bg_center_textview));
		twoText.setVisibility(View.VISIBLE);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(this);
		
		if (!NetUtil.isConnected(App.getInstance())) {
			emptyLayout.setVisibility(View.VISIBLE);
		} else {
			emptyLayout.setVisibility(View.GONE);
		}
	}
	
	private void initEvent() {
		
		if (isWeb) {
			
			if (isInfo) {
				share.setVisibility(View.VISIBLE);
			} else {
				share.setVisibility(View.GONE);
			}
			
			if (StringUtil.isEmpty(title) || isInfo) {
				mTitleName.setText("详情");
				mBack.setText("关闭");
			} else {
				mTitleName.setText(title);
				mBack.setText("返回");
			}
			if (!StringUtil.isEmpty(url)) {
				WebViewUtils.synCookies(this, url, "Client=lebo");
				WebViewUtils.synCookies(this, url, GlobalConstants.JSESSIONID);
				String userid = "";
				if (App.userInfo != null) {
					userid = "userId=" + App.userInfo.getUserId();
				} else {
					userid = "userId=" + "";
				}
				WebViewUtils.synCookies(this, url, userid);
				WebViewUtils.synCookies(this, url, "version=" + AppUtils.getVersionName().substring(1, AppUtils.getVersionName().length()));
				mWebView.loadUrl(url);
			}
		} else {
			if (StringUtil.isEmpty(content)) {
				if (lotteryName > 0) {
					title = LotteryShowUtil.getLotteryName(lotteryName) + getResources().getString(R.string.feature_play_description);
				} else {
					title = LotteryShowUtil.getLotteryName(lotteryName);
				}
				endUrl = LotteryShowUtil.getPLayExplainHtml(lotteryName);
				mTitleName.setText(title);
				mWebView.loadUrl(localUrl + endUrl);
			} else {
				mTitleName.setText("详情");
				mWebView.loadData(content, "text/html; charset=UTF-8", null);
			}
			
		}
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
//		mWebView.getSettings().setBuiltInZoomControls(true);
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
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (!NetUtil.isConnected(App.getInstance())) {
					emptyLayout.setVisibility(View.VISIBLE);
					HtmlCommonActivity.this.url = url;
				} else {
					emptyLayout.setVisibility(View.GONE);
					if (!StringUtil.isEmpty(url)) {
						HtmlCommonActivity.this.url = url;
						isEnter2Detail = true;
						WebViewUtils.synCookies(HtmlCommonActivity.this, url, "Client=lebo");
						WebViewUtils.synCookies(HtmlCommonActivity.this, url, GlobalConstants.JSESSIONID);
						String userid = "";
						if (App.userInfo != null) {
							userid = "userId=" + App.userInfo.getUserId();
						} else {
							userid = "userId=" + "";
						}
						WebViewUtils.synCookies(HtmlCommonActivity.this, url, userid);
						WebViewUtils.synCookies(HtmlCommonActivity.this, url, "version=" + AppUtils.getVersionName().substring(1, AppUtils.getVersionName().length()));
					}
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.addJavascriptInterface(new JSBridge(), "leboapp");
	}
	
	public class JSBridge {
		@JavascriptInterface
		public void interActive(String message) {
			try {
				JSONObject object = new JSONObject(message);
				String transactionType = object.optString("transactionType",
						"10000004");
				if (transactionType.equals("10000003")) {
					String type = object.optString("type", "");
					switch (type) {
					case GlobalConstants.TC_JCZQ:
						startActivity(LotteryFootballActivity.class);
						break;
					case GlobalConstants.TC_JCLQ:
						startActivity(LotteryBasketballActivity.class);
						break;
					case GlobalConstants.TC_SF14:
					case GlobalConstants.TC_SF9:
						Intent intent = new Intent(HtmlCommonActivity.this, LotteryOldFootballActivity.class);
						intent.putExtra("lotoId", 0);
						startActivity(intent);
						break;
					case GlobalConstants.TC_BQ6:
						Intent intent1 = new Intent(HtmlCommonActivity.this, LotteryOldFootballActivity.class);
						intent1.putExtra("lotoId", 2);
						startActivity(intent1);
						break;
					case GlobalConstants.TC_JQ4:
						Intent intent2 = new Intent(HtmlCommonActivity.this, LotteryOldFootballActivity.class);
						intent2.putExtra("lotoId", 1);
						startActivity(intent2);
						break;
					case GlobalConstants.TC_DLT:
						startActivity(LotteryDLTActivity.class);
						break;
					case GlobalConstants.TC_PL3:
						startActivity(LotteryPL3Activity.class);
						break;
					case GlobalConstants.TC_PL5:
						startActivity(LotteryPL5Activity.class);
						break;
					case GlobalConstants.TC_QXC:
						startActivity(LotteryQxcActivity.class);
						break;
					case GlobalConstants.TC_1CZS:
						Intent iItent = new Intent(HtmlCommonActivity.this, LotteryFootballActivity.class);
						iItent.putExtra("selectIndex", 5);
						startActivity(iItent);
						break;
					case GlobalConstants.TC_2X1:
						Intent iiItent = new Intent(HtmlCommonActivity.this, LotteryFootballActivity.class);
						iiItent.putExtra("selectIndex", 6);
						startActivity(iiItent);
						break;
					case "register":
						if (App.userInfo != null) {
							MainActivity mainActivity = (MainActivity) ActivityManager
									.isExistsActivity(MainActivity.class);
							if (mainActivity != null) {
								ActivityManager.retain(MainActivity.class);
								mainActivity.showFrament(3);
							}
							finish();
						} else {
							startActivity(RegisterActivity.class);
						}
						break;
					case "login":
						isLoginBack = true;
						startActivity(LoginActivity.class);
						break;
					case "exchange":
						Intent convertIntent = new Intent(HtmlCommonActivity.this, MessageCenterActivity.class);
						convertIntent.putExtra(MessageCenterActivity.TYPE, MessageCenterActivity.LEMICONVERT);
						startActivity(convertIntent);
						break;
					default:
						break;
					}
				} else if (transactionType.equals("10000004")) {
					String tempTitle = object.optString("title", "");
					String tempUrl = object.optString("url", "");
					String logoFile = object.optString("logoFile", "");
					String summary = object.optString("summary", "");
					if (isEnter2Detail) {
						title = tempTitle;
						url = tempUrl;
						imageUrl = logoFile;
						content = summary;
						setJump();
					} else {
						Intent it = new Intent(HtmlCommonActivity.this, HtmlCommonActivity.class);
						it.putExtra("isWeb", true);
						it.putExtra("webUrl", tempUrl);
						it.putExtra("title", tempTitle);
						it.putExtra("imageUrl", logoFile);
						it.putExtra("isInfo", true);
						it.putExtra("content", summary);
						startActivity(it);
					}
				} else if (transactionType.equals("10000005")) {
					String area = object.optString("area", "");
					final String type = object.optString("type", "");
					switch (area) {
					case "share":
						runOnUiThread(new Runnable() {

							public void run() {
								if (type.equals("0")) {
									share.setVisibility(View.GONE);
								} else {
									share.setVisibility(View.VISIBLE);
								}
							}
						});
						break;

					default:
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			ToastUtil.shortToast(mActivity, "分享成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.shortToast(mActivity, e.errorMessage);
		}

		@Override
		public void onCancel() {
			ToastUtil.shortToast(mActivity, "取消分享");
		}
	};
	
	
	@Override
	public void PopShare(int type) {
		mType = type;
		shareUtils.initApi(HtmlCommonActivity.this);
		if (isEnter2Detail) {
			try {
				JSONObject object = new JSONObject();
				object.put("transactionType", "10002001");
				mWebView.loadUrl("javascript:leboh5.interActive('" + object.toString() + "')");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return;
		}
		setJump();
	}
	
	private void setJump() {
		switch (mType) {
		case 0:
		case 1:
			// 微信分享到_0：好友;_1：朋友圈
			// Bitmap
			// bitmap=shareUtils.getBitMBitmap(obj.getImgUrl());
			Bitmap bm = BitmapFactory.decodeResource(
					getResources(), R.drawable.e_ui_game_share);
			
			shareUtils.shareToWX(title,
					content, url, imageUrl, mType);
			break;
		case 2:
			// 分享到QQ空间
			shareUtils.shareToQQzone(title,
					url, imageUrl,
					qqShareListener);
			break;
		case 3:
			// 分享到QQ
			shareUtils.shareToQQ(title,
			                     content, url,
			                     imageUrl, qqShareListener);
			break;
		}
	}
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		WebViewUtils.removeCookie(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		WebViewUtils.removeCookie(this);
		if (mWebView != null) {
			mWebView.destroy();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_html_back:
			finish();
			break;
		case R.id.imageView_html_share:
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(mActivity, this);
			}
			mPopMenu.showPopWindow(v);
			break;
		case R.id.button_load:
			if (!NetUtil.isConnected(App.getInstance())) {
				emptyLayout.setVisibility(View.VISIBLE);
			} else {
				mWebView.loadUrl(url);
				emptyLayout.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
}
