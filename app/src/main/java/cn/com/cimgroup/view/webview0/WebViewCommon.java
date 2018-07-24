package cn.com.cimgroup.view.webview0;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;


public class WebViewCommon extends BaseActivity{
	
	private WebView mWebView;
	
	private String mTitleText = "设置新密码";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_common);
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		// TODO Auto-generated method stub
		// title 左侧的文字
//		TextView mTitleName = (TextView) findViewById(R.id.layoutView_title_left0);
//		mTitleName.setText(mTitleText);
				
		mWebView = (WebView)findViewById(R.id.common_webView);
		
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		
		Intent intent = getIntent();
		String url = intent.getStringExtra(GlobalConstants.WEBVIEW_URL);
		if (!TextUtils.isEmpty(url)) {
			mWebView.loadUrl(url);
		}
		
	}
	
}
