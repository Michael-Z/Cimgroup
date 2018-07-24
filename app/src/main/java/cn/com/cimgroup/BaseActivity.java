package cn.com.cimgroup;

//import com.zgy.thirdParty.util.UmentUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.com.cimgroup.dailog.LotteryProgressDialog;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;

/**
 * activity 父类
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public class BaseActivity extends FragmentActivity {

	protected BaseActivity mActivity;
	
	/**网络加载loding框**/
	protected LotteryProgressDialog mProgressDialog;
	
	/**
	 * 显示加载框
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月21日
	 */
	protected void showLoadingDialog(){
		if(mProgressDialog!=null){
			mProgressDialog.showDialog();
		}
	}
	
	/**
	 * 隐藏加载框
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月21日
	 */
	protected void hideLoadingDialog(){
		if(mProgressDialog!=null){
			mProgressDialog.hideDialog();
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgressDialog=new LotteryProgressDialog(this);
		ActivityManager.push(this);
		
//		 getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//	        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//	        	Window window = getWindow();
//				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//						| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//				window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//								| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//								| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//	            window.setStatusBarColor(Color.TRANSPARENT);
//	            window.setNavigationBarColor(Color.TRANSPARENT);
//	        }

		mActivity = this;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(mActivity);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(mActivity);
	}

	@Override
	protected void onDestroy() {
		mProgressDialog.hideDialog();
		ActivityManager.pop(this);
		super.onDestroy();
	}

	protected void showToast(String message) {
		ToastUtil.shortToast(mActivity, message);
	}
	
	protected void startActivity(Class<? extends Activity> activity) {
		Intent intent = new Intent(mActivity, activity);
		startActivity(intent);
	}
	
}
