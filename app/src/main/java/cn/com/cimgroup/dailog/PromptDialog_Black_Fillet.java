package cn.com.cimgroup.dailog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.DensityUtils;
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
public class PromptDialog_Black_Fillet {

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
	
	private ImageView mCloseView;
	/** dialog view **/
	private View mView;
	
	private onKeydownListener mListener;
	
	private long mBackPressed;
	/**黑色*/
	private int mColorBlack = 0;
	
	public PromptDialog_Black_Fillet(Context context) {
		this.context = context;
		mColorBlack = this.context.getResources().getColor(R.color.color_gray_secondary);
		initDialogParam();
		initDefaultData();
	}

	private Dialog mProgressDialog = null;
	
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
		mProgressDialog = new Dialog(context, R.style.MessageDelDialog);
		mView = View.inflate(context, R.layout.dialog_confirm_convert, null);
		
		//标题栏
		mTitleView = (TextView) mView
				.findViewById(R.id.dia_des);
		//说明文字
		mMessageView = (TextView) mView
				.findViewById(R.id.sure_confirm);
		//取消按钮
		mCanlBtnView = (TextView) mView
				.findViewById(R.id.cancel);
		//确定按钮
		mOkBtnView = (TextView) mView
				.findViewById(R.id.comfirm);
		
		mCloseView = (ImageView) mView
				.findViewById(R.id.imageView_confirm_close);
		mCanlBtnView.setVisibility(View.GONE);
		mOkBtnView.setVisibility(View.GONE);
		mTitleView.setVisibility(View.GONE);
		mCloseView.setVisibility(View.GONE);
		mProgressDialog.setContentView(mView);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		stopKeycodeBack();
	}

	void initDefaultData() {
//		setTitle(context.getString(R.string.prompt));
		mCanlBtnView.setText(context.getString(R.string.btn_canl));
		mOkBtnView.setText(context.getString(R.string.btn_ok));
	}

	/**
	 * 取消按钮点击事件
	 * 
	 * @Description:
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
			context.getClass().getName();
			mOkBtnView.setText(text);
			mOkBtnView.setOnClickListener(listener);
			mOkBtnView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setCloseView(int visibility, final View.OnClickListener listener) {
		if (mCloseView != null) {
			mCloseView.setVisibility(visibility);
			mCloseView.setOnClickListener(listener);
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
			mTitleView.setVisibility(View.VISIBLE);
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
	
	public void setMessage(Spanned message) {
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
	 * 重置右边按钮为白底黑字
	 * @param text
	 */
	public void setPositiveButtonShape(String text){
		
		int padding = DensityUtils.dip2px(10);
		mOkBtnView.setText(text);
		mOkBtnView.setTextColor(mColorBlack);
		mOkBtnView.setBackgroundResource(R.drawable.selector_bg_center_textview);
		mOkBtnView.setPadding(padding, padding, padding, padding);
	}
	
}
