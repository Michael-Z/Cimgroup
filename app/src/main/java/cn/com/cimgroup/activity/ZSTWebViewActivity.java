package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.xutils.AppUtils;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.WebViewUtils;


/**
 * 走势图H5支持页面
 * 
 * @author 秋风
 * 
 */
@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class ZSTWebViewActivity extends BaseActivity implements OnClickListener {
	private WebView mWebView;
	/** 访问网址 */
	private String url;
	/** 标题 */
	private String mTitle = "";
	
	private String issues="";

	private boolean isOpenh5Dialog = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview_game);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		mTitle = intent.getStringExtra("title");
		if (StringUtil.isEmpty(mTitle)) {
			mTitle = "";
		}
		issues = intent.getStringExtra(GlobalConstants.LOTTERY_ISSUES);
		initView();
		initWebViewSettings();
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
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		mWebView = (WebView) findViewById(R.id.id_webview);
		((RelativeLayout) findViewById(R.id.layout_zst_title)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.textView_html_word)).setText(mTitle);
		((TextView) findViewById(R.id.textView_html_back)).setOnClickListener(this);
		((ImageView) findViewById(R.id.imageView_html_select)).setOnClickListener(this);
//		((TextView) findViewById(R.id.id_title)).setText(mTitle);
//		findViewById(R.id.id_top_layout).setVisibility(View.GONE);
//		findViewById(R.id.id_test_tz).setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				leboNumTZ(getInitDatas());
//			}
//		});
	}

	/**
	 * 初始化WebView的设置
	 */
	@SuppressWarnings("deprecation")
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
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.addJavascriptInterface(new JSBridge(), "leboapp");
	}

	public class JSBridge {
		@JavascriptInterface
		public void interActive(String message) {
			// 测试使用
//			Toast.makeText(getApplicationContext(),
//					"通过Natvie传递的Toast:" + message, Toast.LENGTH_LONG).show();
			try {
				JSONObject object = new JSONObject(message);
				String transactionType = object.optString("transactionType",
						"10000002");
				if (transactionType.equals("10000001")) {
					// 走势图投注
					leboNumTZ(object);
//					leboNumTZ(getInitDatas());
				} else if (transactionType.equals("10000002")) {
					// 返回上一页
					finish();
				} else if (transactionType.equals("10000006")) {
					isOpenh5Dialog = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析走势图传的参数
	 * 
	 * @param object
	 */
	public void leboNumTZ(JSONObject object) {
		
		try {
			//玩法
			String gameNo = object.optString("gameNo", "");
			//期次
			String issue = object.optString("issue", "");
			//截止时间
//			String timSp = object.optString("timSp", "");
			JSONArray orderData = object.optJSONArray("orderData");
			JSONObject o = orderData.getJSONObject(0);
//			String type = o.optString("type", "");
//			String zhuShu = o.optString("zhuShu", "");
			String codeContent = o.optString("codeContent", "");
			Intent intent = new Intent();
			intent.putExtra("lottery_issue", issue);
//			intent.putExtra("", timSp);
			String[] strArr=codeContent.split("\\|");
			//大乐透
			if (gameNo.equals("TC_DLT")) {
				intent.setClass(mActivity, LotteryDLTCartActivity.class);
				intent.putExtra(GlobalConstants.LOTTERY_TYPE, 200);
				intent.putExtra("lottery_issue_time",
						LotteryBettingUtil
								.getIssueEndTime(GlobalConstants.LOTTERY_DLT));
				String red = strArr[0];
				String blue = strArr[1];
				intent.putIntegerArrayListExtra("list_redball",
						getDLTBalls(red));
				intent.putIntegerArrayListExtra("list_buleball",
						getDLTBalls(blue));
				//排列三
			} else if (gameNo.equals("TC_PL3")) {
				intent.setClass(mActivity, LotteryPL3CartActivity.class);
				intent.putExtra(GlobalConstants.LOTTERY_TYPE, 250);
				intent.putExtra("lottery_issue_time",
						LotteryBettingUtil
								.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
				String bs=strArr[0];
				String ss=strArr[1];
				String gs=strArr[2];
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, getDLTBalls(bs));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, getDLTBalls(ss));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, getDLTBalls(gs));
				//排列五
			} else if (gameNo.equals("TC_PL5")) {
				intent.setClass(mActivity, LotteryPL5CartActivity.class);
				intent.putExtra(GlobalConstants.LOTTERY_TYPE, 240);
				intent.putExtra("lottery_issue_time",
						LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL5));
				String ws=strArr[0];
				String qs=strArr[1];
				String bs=strArr[2];
				String ss=strArr[3];
				String gs=strArr[4];
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, getDLTBalls(ws));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, getDLTBalls(qs));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, getDLTBalls(bs));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST4, getDLTBalls(ss));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST5, getDLTBalls(gs));
				//七星彩
			} else if (gameNo.equals("TC_QXC")) {
				intent.setClass(mActivity, LotteryQxcCartActivity.class);
				intent.putExtra(GlobalConstants.LOTTERY_TYPE, 230);
				intent.putExtra("lottery_issue_time",
						LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_QXC));
				String s1=strArr[0];
				String s2=strArr[1];
				String s3=strArr[2];
				String s4=strArr[3];
				String s5=strArr[4];
				String s6=strArr[5];
				String s7=strArr[6];
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, getDLTBalls(s1));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, getDLTBalls(s2));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, getDLTBalls(s3));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST4, getDLTBalls(s4));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST5, getDLTBalls(s5));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST6, getDLTBalls(s6));
				intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST7, getDLTBalls(s7));
			}
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE, "第"+issue+"期销售中");
			intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			startActivity(intent);
			finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.textView_html_back:
				closeActivity();
				break;
			case R.id.imageView_html_select:
				try {
					isOpenh5Dialog = true;
					JSONObject object = new JSONObject();
					object.put("transactionType", "10002002");
					object.put("type", "1");
					mWebView.loadUrl("javascript:leboh5.interActive('" + object.toString() + "')");
				} catch (Exception e) {
					// TODO: handle exception
					isOpenh5Dialog = false;
				}
				break;
			default:
				break;
		}
	}

	private void closeActivity() {
		if (isOpenh5Dialog) {
			try {
				isOpenh5Dialog = false;
				JSONObject object = new JSONObject();
				object.put("transactionType", "10002002");
				object.put("type", "0");
				mWebView.loadUrl("javascript:leboh5.interActive('" + object.toString() + "')");
			} catch (Exception e) {
				// TODO: handle exception
				isOpenh5Dialog = true;
			}
		} else {
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isOpenh5Dialog) {
				closeActivity();
				return false;
			} else {
				if (mWebView.canGoBack()) {
					mWebView.goBack();
					return false;
				} else {
					return super.onKeyDown(keyCode, event);
				}
			}
		}
		return super.onKeyDown(keyCode, event);
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
	/**
	 * 测试数据
	 * @return
	 */
	private JSONObject getInitDatas() {
		JSONObject object=null;
		try {
			object=new JSONObject();
			object.put("timSp", "37532800");
			object.put("issue", "16298");
			object.put(GlobalConstants.LOTTERY_ISSUES, issues);
			object.put("multiple", "1");
			object.put("gameNo", "TC_PL3");
			JSONObject oData=new JSONObject();
			oData.put("type", "直选单式");
			oData.put("zhuShu", "1");
			oData.put("codeContent", "7|0|1");
			JSONArray aData=new JSONArray();
			aData.put(oData);
			object.put("orderData", aData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 将字符串转换为集合
	 * 
	 * @param red
	 * @return
	 */
	private ArrayList<Integer> getDLTBalls(String red) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String[] strs = red.split(",");
		int size = strs.length;
		for (int i = 0; i < size; i++) {
			list.add(Integer.parseInt(strs[i]));
		}
		return list;
	}

}
