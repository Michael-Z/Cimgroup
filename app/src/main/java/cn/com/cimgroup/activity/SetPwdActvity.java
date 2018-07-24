package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.view.DisableExpressionEditText;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 修改密码
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月5日
 */
@SuppressLint("HandlerLeak")
public class SetPwdActvity extends BaseActivity implements OnClickListener,
		OnFocusChangeListener {

	/** 提交修改密码 */
	private TextView textView_setpwd_submit;

	private String mTitleText = "";

	private String mDefaultOld = "";

	private String mDefaultNew = "";

	private String mDefaultNew1 = "";
	/** 修改密码--旧密码 */
	private DisableExpressionEditText id_editText_setpwd_oldpwd;

	private String mPwdText;
	/** 修改密码--新密码（一） */
	private DisableExpressionEditText id_editText_setpwd_newpwd;

	private String mPwdText1;
	/** 修改密码--新密码（二） */
	private DisableExpressionEditText id_editText_setpwd_new1pwd;

	private String mPwdText2;

	/** 密码匹配汉字、字母、数字、下划线 */
	private String mMatchesStrPwd = "^[a-zA-Z0-9_\u4e00-\u9fa5]{6,16}+$";

	private final int FORMER_PASSWORD_WRONG = 0000;
	private final int FORMER_PASSWORD_RIGHT = 1111;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			textView_setpwd_submit.setEnabled(true);
			switch (msg.what) {
			case FORMER_PASSWORD_WRONG:
				ToastUtil.shortToast(SetPwdActvity.this, (String) msg.obj);
				id_editText_setpwd_oldpwd.setText("");
				id_editText_setpwd_oldpwd.requestFocus();
				id_editText_setpwd_newpwd.setText("");
				id_editText_setpwd_new1pwd.setText("");
				hideLoadingDialog();
				break;
			case FORMER_PASSWORD_RIGHT:
				ToastUtil.shortToast(SetPwdActvity.this, (String) msg.obj);
				// SetPwdActvity.this.finish();
				id_editText_setpwd_oldpwd.setText("");
				id_editText_setpwd_newpwd.setText("");
				id_editText_setpwd_new1pwd.setText("");
				hideLoadingDialog();
				break;
			default:
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setpwd);
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	}

	private void initView() {
		mTitleText = getResources().getString(R.string.page_setpwd);

		mDefaultOld = getResources().getString(R.string.setpwd_write_old);

		mDefaultNew = getResources().getString(R.string.setpwd_write_new);

		mDefaultNew1 = getResources().getString(R.string.setpwd_write_new1);

		// title 左侧的文字
		TextView word = (TextView) findViewById(R.id.textView_title_word);
		word.setText(mTitleText);

		TextView back = (TextView) findViewById(R.id.textView_title_back);

		id_editText_setpwd_oldpwd = (DisableExpressionEditText) findViewById(R.id.id_editText_setpwd_oldpwd);

		id_editText_setpwd_newpwd = (DisableExpressionEditText) findViewById(R.id.id_editText_setpwd_newpwd);

		id_editText_setpwd_new1pwd = (DisableExpressionEditText) findViewById(R.id.id_editText_setpwd_new1pwd);

		textView_setpwd_submit = (TextView) findViewById(R.id.textView_setpwd_submit);

		TextView explain = (TextView) findViewById(R.id.textView_setpwd_explain);
		explain.setText(Html.fromHtml(getResources().getString(
				R.string.setpwd_explain)));
		explain.setMovementMethod(LinkMovementMethod.getInstance());

		back.setOnClickListener(this);
		textView_setpwd_submit.setOnClickListener(this);

		id_editText_setpwd_oldpwd.setOnFocusChangeListener(this);
		id_editText_setpwd_newpwd.setOnFocusChangeListener(this);
		id_editText_setpwd_new1pwd.setOnFocusChangeListener(this);
	}

	private CallBack mBack = new CallBack() {

//		public void getBackPWDSuccess(final UserInfo info) {
//			runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					if (Integer.parseInt(info.getResCode()) == 0) {
//						UserLogic.getInstance().clearUserInfo();
//						// //跳登录界面？ 还是做登录处理
//						SetPwdActvity.this.finish();
//					}
//
//				}
//			});
//		};
//
//		public void getBackPWDFailure(String error) {
//			textView_setpwd_submit.setEnabled(true);
//		};

		// 修改密码成功的回调
		public void reSetPWDSuccess(final UserInfo info) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!info.getResCode().isEmpty()) {
						// System.out.println("info的值为"+info.getResCode());
						if (Integer.parseInt(info.getResCode()) == 0) {
							// System.out.println("修改密码成功的回调-rescode=0");
//							Message msg = Message.obtain();
//							msg.what = FORMER_PASSWORD_RIGHT;
//							msg.obj = info.getResMsg();
//							handler.sendMessage(msg);
							hideLoadingDialog();
//							PromptDialog0 dialog = new PromptDialog0(SetPwdActvity.this);
							PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(SetPwdActvity.this);
//							dialog.setTitle("提示");
							dialog.setMessage("登陆密码修改成功，请妥善保管！");
							dialog.setPositiveButton("确定", new OnClickListener() {
								@Override
								public void onClick(View v) {
									finish();
								}
							});
							dialog.showDialog();
						} else {
							Message msg = Message.obtain();
							msg.what = FORMER_PASSWORD_WRONG;
							msg.obj = info.getResMsg();
							handler.sendMessage(msg);
						}
					}
				}
			});

		}

		// 修改密码失败的回调
		public void reSetPWDFailure(final String message) {
			textView_setpwd_submit.setEnabled(true);
			System.out.println("修改密码失败的回调");
		};
	};

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		mPwdText = id_editText_setpwd_oldpwd.getText().toString();
		boolean isOk = mPwdText.matches(mMatchesStrPwd);
		mPwdText1 = id_editText_setpwd_newpwd.getText().toString();
		boolean pwdRes = mPwdText1.matches(mMatchesStrPwd);
		mPwdText2 = id_editText_setpwd_new1pwd.getText().toString();
//		boolean mPwdRes = mPwdText2.matches(mMatchesStrPwd);
		
		switch (v.getId()) {
		case R.id.id_editText_setpwd_oldpwd:
			
			if (hasFocus) {
				id_editText_setpwd_oldpwd.setHint("");
				id_editText_setpwd_newpwd.setHint(mDefaultNew);
				id_editText_setpwd_new1pwd.setHint(mDefaultNew1);
				GlobalTools.openSoftInput(id_editText_setpwd_oldpwd,SetPwdActvity.this);
				
			}
			break;
		case R.id.id_editText_setpwd_newpwd:
			
			if (hasFocus) {
				id_editText_setpwd_newpwd.setHint("");
				id_editText_setpwd_oldpwd.setHint(mDefaultOld);
				id_editText_setpwd_new1pwd.setHint(mDefaultNew1);
				GlobalTools.openSoftInput(id_editText_setpwd_newpwd,SetPwdActvity.this);
				if (TextUtils.isEmpty(mPwdText)) {
					ToastUtil.shortToast(SetPwdActvity.this, "旧密码不能为空");
				} else if (!isOk) {
					ToastUtil.shortToast(this,getResources().getString(R.string.register_error7));
				} 
			} 
			break;
		case R.id.id_editText_setpwd_new1pwd:
			
			if (hasFocus) {
				id_editText_setpwd_new1pwd.setHint("");
				id_editText_setpwd_oldpwd.setHint(mDefaultOld);
				id_editText_setpwd_newpwd.setHint(mDefaultNew);
				GlobalTools.openSoftInput(id_editText_setpwd_new1pwd,SetPwdActvity.this);
				if (TextUtils.isEmpty(mPwdText)) {
					ToastUtil.shortToast(SetPwdActvity.this, "旧密码不能为空");
				} else if (!isOk) {
					ToastUtil.shortToast(this,getResources().getString(R.string.register_error7));
				} else if (TextUtils.isEmpty(mPwdText1)) {
					ToastUtil.shortToast(SetPwdActvity.this, "新密码不能为空");
				} else if (!pwdRes) {
					ToastUtil.shortToast(this,"新"+ getResources().getString(R.string.register_error7));
				}
			} 
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			GlobalTools.hideSoftInput(SetPwdActvity.this);
			finish();
			break;
		case R.id.textView_setpwd_submit:
			UserInfo userInfo = UserLogic.getInstance().getDefaultUserInfo();
			mPwdText = id_editText_setpwd_oldpwd.getText().toString();
			mPwdText1 = id_editText_setpwd_newpwd.getText().toString();
			mPwdText2 = id_editText_setpwd_new1pwd.getText().toString();
//			String regEx = "[A-Za-z0-9]*";
			boolean mPwdRes1 = mPwdText1.matches(mMatchesStrPwd);
			boolean mPwdRes2 = mPwdText2.matches(mMatchesStrPwd);
			boolean isOk = mPwdText.matches(mMatchesStrPwd);
			if (mPwdRes1 && mPwdRes2 && mPwdText.length() > 5
					&& mPwdText1.length() > 5 && mPwdText2.length() > 5
					&& mPwdText1.equals(mPwdText2)
					&& !mPwdText.equals(mPwdText1)
					&& !mPwdText.equals(mPwdText2)) {
				textView_setpwd_submit.setEnabled(false);
				showLoadingDialog();

				Controller.getInstance().getReSetPwd(GlobalConstants.NUM_RESET,
						userInfo.getUserId(), mPwdText1, mPwdText, mBack);
			} else {
				
				if (TextUtils.isEmpty(mPwdText)) {
					ToastUtil.shortToast(SetPwdActvity.this, "旧密码不能为空");
				} else if (!isOk) {
					ToastUtil.shortToast(this,getResources().getString(R.string.register_error7));
				} else if (TextUtils.isEmpty(mPwdText1)) {
					ToastUtil.shortToast(SetPwdActvity.this, "新密码不能为空");
				} else if (!mPwdRes1) {
					ToastUtil.shortToast(this,"新"+ getResources().getString(R.string.register_error7));
				} else if (TextUtils.isEmpty(mPwdText2)) {
					ToastUtil.shortToast(SetPwdActvity.this, "重复密码不能为空");
				} else if (!mPwdRes2) {
					ToastUtil.shortToast(this,"重复"+ getResources().getString(R.string.register_error7));
				} else if(mPwdText.equals(mPwdText1)){
					ToastUtil.shortToast(this,getResources().getString(R.string.setpwd_error1));
				} else if (mPwdText1.equals(mPwdText2)){
					ToastUtil.shortToast(this, getResources().getString(R.string.setpwd_error));
				}
//				if (mPwdText.equals(mPwdText1) || mPwdText.equals(mPwdText2)) {
//					ToastUtil.shortToast(this,
//							getResources().getString(R.string.setpwd_error1));
//				}
				else {
					if (!mPwdRes1) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.register_error7));
					} else {
						if (!mPwdRes2) {
							ToastUtil.shortToast(this, getResources()
									.getString(R.string.register_error7));
						} else {
							if (!mPwdText1.equals(mPwdText2)) {
								ToastUtil.shortToast(this, getResources()
										.getString(R.string.setpwd_error));
							} else {
								ToastUtil.shortToast(this, getResources()
										.getString(R.string.register_error2));
							}
						}
					}
				}
			}

			break;
		default:
			break;
		}
	}

}
