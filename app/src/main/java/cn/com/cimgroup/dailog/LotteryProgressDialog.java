package cn.com.cimgroup.dailog;

import cn.com.cimgroup.view.ProgressWheel;
import cn.com.cimgroup.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
 * 网络加载loding框
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class LotteryProgressDialog {

	/**上下文**/
	private Context context;

	private ProgressWheel mDialogView;

	/**dialog view**/
	private View mView;

	public LotteryProgressDialog(Context context) {
		this.context = context;
		
	}

	private Dialog mProgressDialog = null;

	/**
	 * 显示
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void showDialog() {
		if (mProgressDialog == null) {
			initDialogParam();
		}
		if (!mProgressDialog.isShowing()) {

			mProgressDialog.show();
		}
		
	}

	
	void initDialogParam() {
		mProgressDialog = new Dialog(context, R.style.progress_dialog);
		mView = View.inflate(context, R.layout.dialog_loading_0, null);
		mDialogView = (ProgressWheel) mView.findViewById(R.id.cvproView_loading);
		mProgressDialog.setContentView(mView);
		mProgressDialog.setCancelable(true);
	}

	/**
	 * 隐藏
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void hideDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
			mProgressDialog.dismiss();
		}
	}
	
}