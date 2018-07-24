package cn.com.cimgroup;


import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.com.cimgroup.dailog.LotteryProgressDialog;
import cn.com.cimgroup.xutils.ActivityManager;

public abstract class BaseFramentActivity extends FragmentActivity {

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mProgressDialog=new LotteryProgressDialog(this);
		ActivityManager.push(this);
		super.onCreate(savedInstanceState);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onDestroy() {
		ActivityManager.pop(this);
		super.onDestroy();
	}
}
