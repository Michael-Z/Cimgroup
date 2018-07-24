package cn.com.cimgroup;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import cn.com.cimgroup.dailog.LotteryProgressDialog;

/**
 * frament 父类
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public abstract class BaseFrament extends Fragment {

	protected FragmentActivity mFragmentActivity;
	/** 网络加载loding框 **/
	protected LotteryProgressDialog mProgressDialog;

	/**
	 * 显示加载框
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月21日
	 */
	protected void showLoadingDialog() {
		if (mProgressDialog != null) {
			/**
			 * 只有在页面可见时展示
			 */
			if (getUserVisibleHint()) {
				mProgressDialog.showDialog();
			}
		}
	}

	/**
	 * 隐藏加载框
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月21日
	 */
	protected void hideLoadingDialog() {
		if (mProgressDialog != null) {
			 mProgressDialog.hideDialog();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.e(getClass().getName() + "==" + savedInstanceState, (new Throwable()).getStackTrace()[0].getMethodName());
		super.onCreate(savedInstanceState);
		mFragmentActivity = getActivity();
		mProgressDialog = new LotteryProgressDialog(mFragmentActivity);
	}

	protected void startActivity(Class<? extends Activity> activity) {
		Intent intent = new Intent(mFragmentActivity, activity);
		startActivity(intent);
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mFragmentActivity = null;
		mProgressDialog = null;
	}
	
	//以下代码实现限制fragment预加载
		protected boolean isVisible;
		protected boolean isPrepared;
		
	    /**
	     * 在这里实现Fragment数据的缓加载.
	     * @param isVisibleToUser
	     */
	    @Override
	    public void setUserVisibleHint(boolean isVisibleToUser) {
	        super.setUserVisibleHint(isVisibleToUser);
	        if(getUserVisibleHint()) {
	            isVisible = true;
	            onVisible();
	        } else {
	            isVisible = false;
	            onInvisible();
	        }
	    }
	    
	    protected void onVisible(){
	        lazyLoad();
	    }
	    
	    protected abstract void lazyLoad();
	    protected void onInvisible(){}
}
