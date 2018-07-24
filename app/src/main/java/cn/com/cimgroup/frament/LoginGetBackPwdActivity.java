package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 忘记密码
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
@SuppressLint("HandlerLeak")
public class LoginGetBackPwdActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener {

	/** 提交--找回密码 */
	private TextView textView_getbackpwd_submit;

	private String mTitleText = "";

	private String mDefaultPhone = "";

	private String mDefaultCode = "";

	private String mDefaultPwd = "";

	private String mPhoneError = "";

	private String mPwdText = "";

	// 用户名
	private EditText mPhone;

	// 输入的验证码
	private EditText mInputCode;

	private EditText mPassword;

	// 获取验证码
	private TextView textView_getbackpwd_getCode;

	private String mCodeText;

	private String mPhoneText;

	// 验证手机号
	private boolean mPhoneRes;

	private Timer timer;

	private TimerTask timerTask;

	// 获取验证码倒计时开始时间
	private static long second = 29;

	private String checkCode = "";

	private String userId;

	private final static int TIME = 1;
	
	private static long leaveTime;
	private static long returnTime;
	private static long pastTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_getbackpwd);
		initView();
	}
	

	private void initView() {
		mTitleText = getResources().getString(R.string.page_getbackpwd);

		mDefaultPhone = getResources().getString(R.string.register_write_phone);

		mDefaultCode = getResources().getString(R.string.register_write_code);

		mDefaultPwd = getResources().getString(R.string.getbackpwd_write_pwd);

		mPhoneError = getResources().getString(R.string.getbackpwd_phone_error);

		// title 左侧的文字
		TextView word = (TextView) findViewById(R.id.textView_title_word);
		word.setText(mTitleText);

		TextView back = (TextView) findViewById(R.id.textView_title_back);

		textView_getbackpwd_submit = (TextView) findViewById(R.id.textView_getbackpwd_submit);

		mPhone = (EditText) findViewById(R.id.editView_getbackpwd_phone);

		mInputCode = (EditText) findViewById(R.id.editText_getbackpwd_inputCode);

		mPassword = (EditText) findViewById(R.id.editView_getbackpwd_password);

		textView_getbackpwd_getCode = (TextView) findViewById(R.id.textView_getbackpwd_getCode);

		// 设置下方显示的说明文字样式
		TextView explain = (TextView) findViewById(R.id.textView_getbackpwd_explain);
		// String explainContent = "如有问题请及时与客服联系:";
		// explain.setText(Html.fromHtml(explainContent+"<font color='#bababa'>"+"<a href='tel:010-63135707'>010-63135707</a>"+"</font>"));
		// explain.setMovementMethod(LinkMovementMethod.getInstance());

		explain.setText(Html.fromHtml(getResources().getString(
				R.string.login_phonetext)));
		explain.setMovementMethod(LinkMovementMethod.getInstance());
		//
		// explain.setText(Html.fromHtml(getResources().getString(R.string.setnewpwd_reminde_string)));

		mPhone.setOnFocusChangeListener(this);
		mInputCode.setOnFocusChangeListener(this);
		mPassword.setOnFocusChangeListener(this);

		back.setOnClickListener(this);
		textView_getbackpwd_getCode.setOnClickListener(this);
		textView_getbackpwd_submit.setOnClickListener(this);
	}

	private CallBack mBack = new CallBack() {

		public void getBackPWDSuccess(final UserInfo info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (Integer.parseInt(info.getResCode()) == 0) {
						ToastUtil.shortToast(LoginGetBackPwdActivity.this,
								"密码修改成功，请妥善保管！");
						UserLogic.getInstance().clearUserInfo();
						LoginGetBackPwdActivity.this.finish();
					} else {
						textView_getbackpwd_submit.setEnabled(true);
						ToastUtil.shortToast(LoginGetBackPwdActivity.this,
								info.getResMsg());
					}
				}
			});
		};

		public void getBackPWDFailure(String error) {
			textView_getbackpwd_submit.setEnabled(true);
		};

		public void getCodeSuccess(final GetCode code) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (Integer.parseInt(code.getResCode()) == 0) {
//						App.getInstance().setIsCounting(true);
						userId = code.getUserId();
						checkCode = "1";
//						second = 29;
						// 获取倒计时
						handler.sendEmptyMessageDelayed(TIME, 1000);
					} else {
						ToastUtil.shortToast(LoginGetBackPwdActivity.this,
								code.getResMsg());
						second = 29;
						textView_getbackpwd_getCode.setEnabled(true);
					}

				}
			});
		};

		public void veriPhoneSuccess(final GetCode code) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!code.getResCode().equals("0")) {
						textView_getbackpwd_getCode.setEnabled(true);
					} else {
						ToastUtil.shortToast(LoginGetBackPwdActivity.this,
								"手机号没有注册过");
						textView_getbackpwd_getCode.setEnabled(false);
					}
				}
			});
		};

		public void veriPhoneFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(LoginGetBackPwdActivity.this, error);
				}
			});
		};

	};

	@Override
	protected void onDestroy() {
//		Controller.getInstance().removeCallback(mBack);
//		leaveTime = 0;
		super.onDestroy();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.editText_getbackpwd_inputCode:
			if (hasFocus) {
				mInputCode.setHint("");
				GlobalTools.openSoftInput(mInputCode,
						LoginGetBackPwdActivity.this);
			} else {
				mCodeText = mInputCode.getText().toString();
				// 失去焦点后，对输入的验证码判断是否符合
				if (mCodeText.length() == 0) {
					mInputCode.setHint(mDefaultCode);
				}
			}
			break;
		case R.id.editView_getbackpwd_phone:
			if (hasFocus) {
				mPhone.setHint("");
				GlobalTools.openSoftInput(mPhone, LoginGetBackPwdActivity.this);
			} else {
				mPhoneText = mPhone.getText().toString();
				// 失去焦点后，对输入的验证码判断是否符合
				if (mPhoneText.length() == 0) {
					mPhone.setHint(mDefaultPhone);
				} else {
					String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
					mPhoneRes = mPhoneText.matches(telRegex);
					if (!mPhoneRes) {
						ToastUtil.shortToast(this, mPhoneError);
					} else {
						Controller.getInstance().veriPhone(
								GlobalConstants.NUM_VERIUSERNAME, mPhoneText,
								mBack);
					}
				}
			}
			break;
		case R.id.editView_register_password:
			if (hasFocus) {
				mPassword.setHint("");
				GlobalTools.openSoftInput(mPassword,
						LoginGetBackPwdActivity.this);
			} else {
				mPwdText = mPassword.getText().toString();
				String regEx = "[A-Za-z0-9]*";
				boolean mPwdRes = mPwdText.matches(regEx);

				if (mPwdRes) {
					if (mPwdText.length() == 0) {
						mPassword.setHint(mDefaultPwd);
					} else if (mPwdText.length() < 6) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.register_error2));
					}
				} else {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.register_error7));
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
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			if (timerTask != null) {
				timerTask = null;
			}
			GlobalTools.hideSoftInput(LoginGetBackPwdActivity.this);
			finish();
			break;
		// 获取验证码
		case R.id.textView_getbackpwd_getCode:
			mPhoneText = mPhone.getText().toString();
			// 失去焦点后，对输入的验证码判断是否符合
			if (mPhoneText.length() == 0) {
				mPhone.setHint(mDefaultPhone);
				ToastUtil
						.shortToast(
								this,
								getResources().getString(
										R.string.register_write_phone));
			} else {
				String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
				mPhoneRes = mPhoneText.matches(telRegex);
				if (!mPhoneRes) {
					ToastUtil.shortToast(this, mPhoneError);
				}
			}
			if (mPhoneRes) {
				// 验证手机的请求 和getCode-请求验证码重复 故删掉
				// Controller.getInstance().veriPhone(GlobalConstants.NUM_VERIUSERNAME,
				// mPhoneText, mBack);
				Controller.getInstance().getCode(GlobalConstants.NUM_SENDCODE,
						mPhoneText, "FORGET", mBack);
				textView_getbackpwd_getCode.setBackgroundColor(getResources()
						.getColor(R.color.hall_grey_word));
				textView_getbackpwd_getCode.setEnabled(false);
				// second = 29;
				// // 获取倒计时
				// timerTask = new TimerTask() {
				//
				// @Override
				// public void run() {
				// Message msg = new Message();
				// msg.what = 0;
				// handler.sendMessage(msg);
				// }
				// };
				//
				// timer = new Timer();
				// timer.schedule(timerTask, 0, 1000);
			}

			break;
		// 判断验证码是否正确
		case R.id.textView_getbackpwd_submit:
			GlobalTools.hideSoftInput(LoginGetBackPwdActivity.this);
			mCodeText = mInputCode.getText().toString();
			mPhoneText = mPhone.getText().toString();
			mPwdText = mPassword.getText().toString();
			String regEx = "[A-Za-z0-9]*";
			boolean mPwdRes = mPwdText.matches(regEx);
			if (mPhoneRes && mPwdRes && mPwdText.length() >= 6
					&& !StringUtil.isEmpty(checkCode)
					&& mCodeText.length() == 4) {
				showLoadingDialog();
				textView_getbackpwd_submit.setEnabled(false);
				Controller.getInstance().getBackPWD(
						GlobalConstants.NUM_GETBACKPWD, userId, mPhoneText,
						mCodeText, mPwdText, mBack);
			} else if (!mPhoneRes) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error0));
			} else if (!mPwdRes) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error7));
			} else if (mCodeText.length() == 0) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error3));
			} else if (mPwdText.length() == 0) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error1));
			} else if (mPwdText.length() < 6) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error2));
			} else if (StringUtil.isEmpty(checkCode)) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.getbackpwd_error0));
			}
			// if (!mCodeText.equals(checkCode)) {
			// ToastUtil.shortToast(this,
			// getResources().getString(R.string.getbackpwd_error0));
			// }

			break;
		default:
			break;
		}
	}

	Handler handler = new Handler() {

		// public void handleMessage(Message msg) {
		// if (msg.what == 0) {
		// if (second > 0) {
		// second--;
		// mGetCode.setText(getResources().getString(R.string.register_again_second,
		// second));
		// } else {
		// if (timer != null) {
		// timer.cancel();
		// timer = null;
		// }
		// if (timerTask != null) {
		// timerTask = null;
		// }
		// second = 29;
		// mGetCode.setClickable(true);
		// mGetCode.setEnabled(true);
		// mGetCode.setText(getResources().getString(R.string.register_again));
		// }
		// }
		// }

		public void handleMessage(Message msg) {
			if (msg.what == TIME) {
				if (second > 0) {
					second--;
					textView_getbackpwd_getCode.setText(getResources()
							.getString(R.string.register_again_second, second));
					handler.sendEmptyMessageDelayed(TIME, 1000);
				} else {
					second = 29;
					textView_getbackpwd_getCode.setClickable(true);
					textView_getbackpwd_getCode.setEnabled(true);
					textView_getbackpwd_getCode.setText(getResources()
							.getString(R.string.register_again));
					textView_getbackpwd_getCode.setBackgroundColor(getResources().getColor(R.color.color_red));
				}
			}
		};
	};
	
	@Override
	public void onPause() {
		//记录用户离开的时间
		handler.removeMessages(TIME);
		leaveTime = System.currentTimeMillis();
//		System.out.println("离开的时间为:"+leaveTime);
		super.onPause();
		
	};
	
	
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onResume() {
//		if(leaveTime != 0){
		//记录用户返回的时间
			returnTime = System.currentTimeMillis();
//			if(leaveTime != null  )
			pastTime = returnTime - leaveTime;
			if(pastTime > second*1000){
				//返回时及时已结束
				second = 29;
				textView_getbackpwd_getCode.setClickable(true);
				textView_getbackpwd_getCode.setEnabled(true);
				textView_getbackpwd_getCode.setText(getResources()
						.getString(R.string.register_getcode));
			}else{
				//返回时及时未结束，那么就人为减去时间，并开始计时
//				textView_getbackpwd_getCode.setEnabled(false);
				second = second - Math.round(pastTime/1000) ;
//				System.out.println("计算之后的时间间隔为:"+second);
				handler.sendEmptyMessageDelayed(TIME, 1000);
			}
//			System.out.println("返回的时间为:"+returnTime);
//			System.out.println("间隔的时间为:"+pastTime);
//		}
//			else{
//			second = 29;
//			textView_getbackpwd_getCode.setClickable(true);
//			textView_getbackpwd_getCode.setEnabled(true);
//			textView_getbackpwd_getCode.setText(getResources()
//					.getString(R.string.register_getcode));
//		}
		super.onResume();
	}
	

	/**
	 * 退出当前activity后关闭计时器，否则下次进入的时候计时器时间加速减少
	 */
	@Override
	protected void onStop() {
		handler.removeMessages(TIME);
//		second = 29;
		// timer.cancel();
//		System.out.println("进入到了onstop方法");
//		App.getInstance().setIsCounting(false);
		super.onStop();
		
		Controller.getInstance().removeCallback(mBack);
		leaveTime = 0;
	}
	
}
