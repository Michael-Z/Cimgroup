package cn.com.cimgroup.activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.view.DisableExpressionEditText;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 注册
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnFocusChangeListener, OnCheckedChangeListener {
	/** 注册 */
	private TextView textView_register_submit;

	private String mTitleText = "";

	private String mDefaultPhone = "";

	private String mDefaultName = "";

	private String mDefaultPwd = "";

	private String mCodeText = "";
	/** 用户名 */
	private DisableExpressionEditText mName;
	/** 密码 */
	private DisableExpressionEditText mPassword;

	private EditText mInputCode;

	private EditText mPhone;

	private String mNameText;

	private String mPwdText;

	private String mInputCodeText;

	private String mPhoneText;

	private boolean mIsAgree = true;
	// 验证手机号
	private boolean mPhoneRes;

	private TextView textView_register_getCode;

	private Timer timer;

	private TimerTask timerTask;
	/** 账号匹配汉字、字母、数字、下划线 */
	private String mMatchesStrName = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,12}+$";
	// String regEx1 = "^[0-9]*[A-Z|a-z|\u4e00-\u9fa5]+[0-9]*$";
	/** 密码匹配字母、数字、下划线 */
	private String mMatchesStrPwd = "^[a-zA-Z0-9_]{6,16}+$";
	
	/**匹配字符是否是文字*/
	private String regEx = "[\\u4e00-\\u9fa5]";

	// 获取验证码倒计时开始时间
	private static int second = 29;

	private String checkCode = "";

	private static final int TIME = 1;
	private static final int OTHER = 2;

	private String userId;

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == TIME) {
				if (second > 0) {
					second--;
					textView_register_getCode.setText(getResources().getString(
							R.string.register_again_second, second));
					handler.sendEmptyMessageDelayed(TIME, 1000);
				} else {
					second = 29;
					textView_register_getCode.setClickable(true);
					textView_register_getCode.setEnabled(true);
					textView_register_getCode.setText(getResources().getString(
							R.string.register_again));
					textView_register_getCode.setBackgroundColor(getResources()
							.getColor(R.color.color_red));
				}
			} else if (msg.what == OTHER) {
				second = 29;
				textView_register_getCode.setClickable(true);
				textView_register_getCode.setEnabled(true);
				textView_register_getCode.setText(getResources().getString(
						R.string.register_again));
				textView_register_getCode.setBackgroundColor(getResources()
						.getColor(R.color.color_red));
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		mTitleText = getResources().getString(R.string.page_register);

		mDefaultPhone = getResources().getString(R.string.register_write_phone);

		mDefaultName = getResources().getString(
				R.string.register_write_username);

		mDefaultPwd = getResources().getString(R.string.register_write_pwd);

		mCodeText = getResources().getString(R.string.register_write_code);

		// // title 左侧的文字
		TextView word = (TextView) findViewById(R.id.textView_title_word);
		word.setText(mTitleText);

		TextView back = (TextView) findViewById(R.id.textView_title_back);

		textView_register_submit = (TextView) findViewById(R.id.textView_register_submit);

		textView_register_getCode = (TextView) findViewById(R.id.textView_register_getCode);

		mInputCode = (EditText) findViewById(R.id.editText_register_inputCode);

		mPassword = (DisableExpressionEditText) findViewById(R.id.editView_register_password);

		mName = (DisableExpressionEditText) findViewById(R.id.editView_register_name);

		mPhone = (EditText) findViewById(R.id.editView_register_phone);

		CheckBox agree = (CheckBox) findViewById(R.id.checkBox_register_agree);

		TextView doc = (TextView) findViewById(R.id.textView_register_doc);

		back.setOnClickListener(this);
		textView_register_submit.setOnClickListener(this);
		doc.setOnClickListener(this);
		textView_register_getCode.setOnClickListener(this);

		agree.setOnCheckedChangeListener(this);

		mName.setOnFocusChangeListener(this);
		mPassword.setOnFocusChangeListener(this);
		mInputCode.setOnFocusChangeListener(this);
		mPhone.setOnFocusChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		Controller.getInstance().removeCallback(mBack);
		super.onDestroy();
	};

	private CallBack mBack = new CallBack() {
		public void registerSuccess(final UserInfo userInfo) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					// ResCode ==0表示注册成功，其余表示注册失败
					if (userInfo.getResCode().equals("0")) {
						userInfo.setPassword(mPwdText);
						userInfo.setIsCompUserName("0");
						userInfo.setIsBindedBank("0");
						App.userInfo = userInfo;
						App.userInfo.setMobile(mPhone.getText().toString()
								.trim());
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						startActivity(UserManageActivity.class);
						RegisterActivity.this.finish();
					} else {
						textView_register_submit.setEnabled(true);
						ToastUtil.shortToast(RegisterActivity.this,
								userInfo.getResMsg());
					}

				}
			});
		};

		public void registerFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					textView_register_submit.setEnabled(true);
					ToastUtil.shortToast(RegisterActivity.this, error);
				}
			});
		};

		public void getCodeSuccess(final GetCode code) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (Integer.parseInt(code.getResCode()) == 0) {
						userId = code.getUserId();
						checkCode = "1";
						second = 29;
						// 获取倒计时
//						handler.sendEmptyMessageDelayed(TIME, 1000);
					} else {
						ToastUtil.shortToast(RegisterActivity.this,
								code.getResMsg());
						second = 29;
						textView_register_getCode.setEnabled(true);
						Message message = Message.obtain();
						message.what = OTHER;
						handler.sendEmptyMessage(OTHER);

					}

				}
			});
		};
		
		public void getCodeFailure(String error) {
			runOnUiThread(new Runnable() {
				public void run() {
					textView_register_getCode.setEnabled(true);
					textView_register_getCode.setClickable(true);
				}
			});
		};

		public void veriPhoneSuccess(final GetCode code) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!code.getResCode().equals("0")) {
						ToastUtil.shortToast(RegisterActivity.this, "手机号已经注册过");
						textView_register_getCode
								.setBackgroundColor(getResources().getColor(
										R.color.hall_grey_word));
						textView_register_getCode.setEnabled(false);
					} else {
						// mGetCode.setBackgroundColor(getResources().getColor(R.color.hall_red));
						// mGetCode.setEnabled(true);
					}
				}
			});
		};

		public void veriPhoneFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(RegisterActivity.this, error);
				}
			});
		};
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.checkBox_register_agree:
			mIsAgree = isChecked;
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
			GlobalTools.hideSoftInput(RegisterActivity.this);
			finish();
			break;
		// 进行注册
		case R.id.textView_register_submit:
			if (mIsAgree) {
				mNameText = mName.getText().toString();
				mPwdText = mPassword.getText().toString();
				mInputCodeText = mInputCode.getText().toString();
				mPhoneText = mPhone.getText().toString();
				String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
				mPhoneRes = mPhoneText.matches(telRegex);
				// String regEx = "[A-Za-z0-9]*";
				boolean mPwdRes = mPwdText.matches(mMatchesStrPwd);
				// String regEx1 = "^[0-9]*[A-Z|a-z|\u4e00-\u9fa5]+[0-9]*$";
				boolean mNameRes = mNameText.matches(mMatchesStrName);
				int nameLength=TextUtils.isEmpty(mNameText)?0:getUserNameLength(mNameText);
				if (TextUtils.isEmpty(mNameText)) {
					ToastUtil.shortToast(this, "用户名不能为空");
				} else if (!mNameRes||nameLength<4||nameLength>12||isNumeric(mNameText)) {
					ToastUtil.shortToast(this, "用户名为4-12个字符或下划线，不能全为数字");
				} else if (TextUtils.isEmpty(mPwdText)) {
					ToastUtil.shortToast(this, "密码不能为空");
				}else if (!mPwdRes) {
					ToastUtil.shortToast(this, "密码为6-16位字符");
				}else if (TextUtils.isEmpty(mPhoneText)) {
					ToastUtil.shortToast(this, " 手机号不能为空");
				}else if (!mPhoneRes) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.register_error0));
				}else if (TextUtils.isEmpty(mInputCodeText)) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.register_error3));
				} else {
					textView_register_submit.setEnabled(false);
					showLoadingDialog();
					Controller.getInstance().register(
							GlobalConstants.NUM_REGISTER, mNameText,
							mPhoneText, mPwdText, mInputCodeText, mBack);
				}
			} else {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_agree_law));
			}

			break;
		case R.id.textView_register_doc:
			// 跳转法律说明页面
			Intent intent = new Intent(this, HtmlCommonActivity.class);
			intent.putExtra("isWeb", false);
			intent.putExtra("lotteryName", 0);
			startActivity(intent);
			break;
		case R.id.textView_register_getCode:
			mPhoneText = mPhone.getText().toString();
			String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
			mPhoneRes = mPhoneText.matches(telRegex);
			if (mPhoneRes) {
				textView_register_getCode.setEnabled(false);
				textView_register_getCode.setClickable(false);
				handler.sendEmptyMessageDelayed(TIME, 1000);
				// 验证手机号，和之后的获取验证码判断重复，故删除
				Controller.getInstance().getCode(GlobalConstants.NUM_SENDCODE,
						mPhoneText, "REG", mBack);
				textView_register_getCode.setBackgroundColor(getResources()
						.getColor(R.color.hall_grey_word));
			} else {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error0));
			}

			break;
		default:
			break;
		}
	}
	
	/**
	 * 判断字符串是否全为数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	/**
	 * 获取输入字符长度
	 * @param mNameText2
	 */
	private int getUserNameLength(String mNameText2) {
		int count = 0;
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(mNameText2);
		while (m.find()) {
			count++;
		}
		return mNameText2.length()+count;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.editView_register_phone:
			String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
			mPhoneText = mPhone.getText().toString();
			mPhoneRes = mPhoneText.matches(telRegex);
			if (hasFocus) {
				mPhone.setHint("");
				GlobalTools.openSoftInput(mPhone, RegisterActivity.this);
			} else if (TextUtils.isEmpty(mPhoneText)) {
				mPhone.setHint(mDefaultPhone);
				ToastUtil.shortToast(RegisterActivity.this, "手机号码不能为空");
			} else if (!mPhoneRes) {
				ToastUtil.shortToast(this,
						getResources().getString(R.string.register_error0));
			} else {// 是否需要在输入后就提示用户手机号已、未注册
				Controller.getInstance().veriPhone(
						GlobalConstants.NUM_VERIUSERNAME, mPhoneText, mBack);
			}

			break;
		case R.id.editView_register_name:
			mNameText = mName.getText().toString();
			boolean mNameRes = mNameText.matches(mMatchesStrName);
			if (hasFocus) {
				mName.setHint("");
				GlobalTools.openSoftInput(mName, RegisterActivity.this);
			} else if (TextUtils.isEmpty(mNameText)) {
				mName.setHint(mDefaultName);
				ToastUtil.shortToast(this, "用户名不能为空");
				mName.setHint(mDefaultName);
			} else if (!mNameRes) {
				ToastUtil.longToast(RegisterActivity.this,
						"用户名为4-12个汉字、字母、数字或下划线");
			}

			break;
		case R.id.editView_register_password:
			mPwdText = mPassword.getText().toString();
			boolean mPwdRes = mPwdText.matches(mMatchesStrPwd);
			if (hasFocus) {
				mPassword.setHint("");
				GlobalTools.openSoftInput(mPassword, RegisterActivity.this);
			} else if (TextUtils.isEmpty(mPwdText)) {
				mPassword.setHint(mDefaultPwd);
				ToastUtil.shortToast(this, "密码不能为空");
			} else if (!mPwdRes) {
				ToastUtil.shortToast(this, "密码为6-16个汉字、字母、数字或下划线");

			}
			break;
		case R.id.editText_register_inputCode:
			if (hasFocus) {
				mInputCode.setHint("");
				GlobalTools.openSoftInput(mInputCode, RegisterActivity.this);
			} else {
				mInputCodeText = mInputCode.getText().toString();
				// 失去焦点后，对输入的验证码判断是否符合
				if (TextUtils.isEmpty(mInputCodeText)) {
					mInputCode.setHint(mCodeText);
					ToastUtil.shortToast(RegisterActivity.this, "验证码不能为空");
				}
				// else {
				// if (!mInputText.equals(checkCode)) {
				// ToastUtil.shortToast(this,
				// getResources().getString(R.string.register_error5));
				// }
				// }
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		textView_register_submit.setEnabled(true);
		textView_register_getCode.setEnabled(true);
		textView_register_getCode.setClickable(true);
	}
}
