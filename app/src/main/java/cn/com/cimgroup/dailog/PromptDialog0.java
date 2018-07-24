package cn.com.cimgroup.dailog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 提示框
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class PromptDialog0 {

	/** 上下文 **/
	private Context context;

	/** title **/
	private TextView mTitleView;
	/** message **/
	private TextView mMessageView;

	/** button canl **/
	private TextView mCanlBtnView;
	/** button ok **/
	private TextView mOkBtnView;
	/** dialog view **/
	private View mView;
	
	private onKeydownListener mListener;
	
	private long mBackPressed;
	
	private ProgressBar pb_download;
	
	private LinearLayout linear_prompt_dialog0_cancel;
	
	private LinearLayout ll_layout;

	public PromptDialog0(Context context) {
		this.context = context;
		initDialogParam();
		initDefaultData();
	}

	private Dialog mProgressDialog = null;
	
	private boolean flag = false;
	
	private LinearLayout ll_download;
	
	/**
	 * 显示
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void showDialog() {
		hideDialog();
		mProgressDialog.show();
	}

	void initDialogParam() {
		mProgressDialog = new Dialog(context, R.style.prompt_dialog);
		mView = View.inflate(context, R.layout.dialog_prompt_0, null);
		mTitleView = (TextView) mView
				.findViewById(R.id.txtView_prompt_dialog0_title);
		mMessageView = (TextView) mView
				.findViewById(R.id.txtView_prompt_dialog0_message);
		mCanlBtnView = (TextView) mView
				.findViewById(R.id.txtView_prompt_dialog0_cancel);
		mOkBtnView = (TextView) mView
				.findViewById(R.id.txtView_prompt_dialog0_ok);
		//取消按钮外围的LinearLayout
		linear_prompt_dialog0_cancel = (LinearLayout) mView.findViewById(R.id.linear_prompt_dialog0_cancel);
		//
		pb_download = (ProgressBar) mView.findViewById(R.id.pb_download);
		
		ll_layout = (LinearLayout) mView.findViewById(R.id.ll_layout);
		
		ll_download = (LinearLayout) mView.findViewById(R.id.ll_download);
		
		mCanlBtnView.setVisibility(View.GONE);
		mOkBtnView.setVisibility(View.GONE);
		mProgressDialog.setContentView(mView);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		stopKeycodeBack();
	}

	void initDefaultData() {
		setTitle(context.getString(R.string.prompt));
		mCanlBtnView.setText(context.getString(R.string.btn_canl));
		mOkBtnView.setText(context.getString(R.string.btn_ok));
	}

	/**
	 * 取消按钮点击事件
	 * 
	 * @Description: 用的时候先setNegativeButton 不set或者先setPositionButton将导致该button不显示
	 * @param text
	 * @param listener
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void setNegativeButton(final String text,
			final View.OnClickListener listener) {
		if (mCanlBtnView != null) {
			mCanlBtnView.setText(text);
			mCanlBtnView.setOnClickListener(listener);
			mCanlBtnView.setVisibility(View.VISIBLE);
			flag = true;
		}
	}

	/**
	 * OK按钮点击事件
	 * 
	 * @Description:
	 * @param text
	 * @param listener
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void setPositiveButton(final String text,
			final View.OnClickListener listener) {
		if (mOkBtnView != null) {
			mOkBtnView.setText(text);
			mOkBtnView.setOnClickListener(listener);
			mOkBtnView.setVisibility(View.VISIBLE);
		}
		if (!flag && linear_prompt_dialog0_cancel != null) {
			linear_prompt_dialog0_cancel.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @Description:
	 * @param title
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void setTitle(String title) {
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	/**
	 * 设置内容
	 * 
	 * @Description:
	 * @param message
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void setMessage(String message) {
		if (mMessageView != null) {
			mMessageView.setText(message);
		}
	}

	/**
	 * 隐藏
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void hideDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}

	/**
	 * 设置dialog可否被点击关闭
	 * 
	 * @param flag
	 */
	public void setCancelable(boolean flag) {
		mProgressDialog.setCancelable(flag);
	}
	
	/**
	 * 拦截dialog状态下的物理返回键
	 * 该处的物理返回键监听会屏蔽掉dialog.setCancelable()屏蔽掉
	 */
	public void stopKeycodeBack(){
		mProgressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//					System.out.println("0000000000000000");
					if(mListener != null){
						int result = mListener.onBackClicked();
						if(result == 1){
							if((System.currentTimeMillis()) - mBackPressed > 2000){
								ToastUtil.shortToast(context,context.getResources().getString(R.string.app_exit_hint));
								mBackPressed = System.currentTimeMillis();
							}else{
								ActivityManager.popAll();
								System.exit(0);
							}
						}else if(result == 2){
							return true;
						}else {
							mProgressDialog.dismiss();
						}
					}else{
						mProgressDialog.dismiss();
					}
					
					//return true-表示拦截了点击事件 即点击后也不会关闭dialog
					//return false-表示不拦截点击事件 点击后关闭dialog
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * 自定义的物理返回事件的监听
	 * @author leo
	 *
	 */
	public interface onKeydownListener{
		/**
		 * 返回值   1-正常（点两下退出） 2-点击无法退出
		 * @return
		 */
		int onBackClicked();
	}
	
	/**
	 * 初始化监听
	 * 如果不设置监听 则点击物理返回键关闭dialog
	 */
	public void setOnKeydownListener(onKeydownListener listener){
		if(listener != null){
			this.mListener = listener;
		}
	}
	
	/**
	 * dialog是否正在显示
	 */
	public boolean isShowing(){
		return mProgressDialog.isShowing();
	}
	
	/**
	 * 设置 可/否 点击  
	 * @param type 1-left_button（取消）   2-right_button（确定）  0-both
	 * @param state 1-clickable=true    2-clickable=false
	 */
	public void setClickable(int type, int state){
		
		if (type==0 && state==1) {
			//全部可以点击
			mOkBtnView.setClickable(true);
			mCanlBtnView.setClickable(true);
		}else if (type==0 && state==2) {
			mOkBtnView.setClickable(false);
			mCanlBtnView.setClickable(false);
		}else if (type==1 && state==1) {
			mCanlBtnView.setClickable(true);
		}else if (type==1 && state==2) {
			mCanlBtnView.setClickable(false);
		}else if (type==2 && state==1) {
			mOkBtnView.setClickable(true);
		}else if (type==2 && state==2) {
			mOkBtnView.setClickable(false);
		}
		
	}
	
	/**
	 * 展示进度条，隐藏按钮
	 */
	public void showProgress(){
		hideDialog();
		if (mCanlBtnView != null) {
			mCanlBtnView.setVisibility(View.GONE);
		}
		if (ll_layout != null) {
			ll_layout.setVisibility(View.GONE);
		}
//		if (pb_download != null) {
//			pb_download.setVisibility(View.VISIBLE);
//		}
		if (ll_download != null) {
			ll_download.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 更新进度条进度
	 */
	public void updateProgress(int progress){
		pb_download.setProgress(progress);
	}
}
