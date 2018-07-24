package cn.com.cimgroup.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 找回密码
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {

	/** 注册时的手机号 */
	private EditText id_phone;
	/** 验证码 */
	private EditText id_code;
	/** 发送验证码 */
	private TextView id_send_code;

	/** 提交 */
	private TextView id_commit;

	/** 说明 */
	private TextView id_explain;
	
	/**返回**/
	private TextView tv_back;

	/** 倒计时 */
	private int times = 30;
	/** 倒计时标识 */
	private final static int CHANGE_TIME = 0x100;
	
	private String mCodeText;
	private String mPhoneText;
	private String mUserId;
	
	private boolean mPhoneRes;
	
	public static FindPasswordActivity instance;
	
	private String checkCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);

		initView();
		initEvent();
		initData();
	}

	private void initData() {
		instance = this;
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_phone = (EditText) findViewById(R.id.id_phone);
		id_code = (EditText) findViewById(R.id.id_code);
		id_send_code = (TextView) findViewById(R.id.id_send_code);
		id_commit = (TextView) findViewById(R.id.id_commit);
		id_explain = (TextView) findViewById(R.id.id_explain);
		id_explain.setText(Html.fromHtml("如有疑问请联系客服，"
				+ getResources().getString(R.string.login_phonetext)));

		id_explain.setMovementMethod(LinkMovementMethod.getInstance());
		tv_back = (TextView) findViewById(R.id.tv_back);
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		findViewById(R.id.id_back_layout).setOnClickListener(this);
		id_send_code.setOnClickListener(this);
		id_commit.setOnClickListener(this);
		tv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.id_send_code:
			// 发送验证码
			getCode();
			break;
		case R.id.id_commit:
			// 提交手机号
			commitPhone();
			break;
		case R.id.tv_back:
			FindPasswordActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 提交手机号
	 */
	private void commitPhone() {
		
		//隐藏软件盘
		GlobalTools.hideSoftInput(FindPasswordActivity.this);
		id_commit.setEnabled(false);
		mCodeText = id_code.getText().toString();
		mPhoneText = id_phone.getText().toString();
		
		//输入的各项条目规范性验证
		String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
		String codeRegex = "^[\\d]{4}$";
		if (!TextUtils.isEmpty(mPhoneText)) {
			mPhoneRes = mPhoneText.matches(telRegex);
		}
		if (mPhoneRes && !TextUtils.isEmpty(mCodeText)
				&& !TextUtils.isEmpty(mUserId) && mCodeText.length() == 4) {
			// if (!mCodeText.equals(checkCode)) {
			// ToastUtil.shortToast(this,"验证码错误，请确认后重试");
			// }else {
			// ResetPwdActivity.toActivity(FindPasswordActivity.this,
			// mPhoneText, mCodeText, mUserId);
			// }
			Controller.getInstance().validateCode(
					GlobalConstants.VALIDATE_CODE, mUserId, mCodeText,
					mPhoneText, mCallback);
		} else if (!mPhoneRes) {
			ToastUtil.shortToast(this,
					getResources().getString(R.string.register_error0));
		} else if (mCodeText.length() == 0) {
			ToastUtil.shortToast(this,
					getResources().getString(R.string.register_error3));
		} else if (TextUtils.isEmpty(checkCode)) {

			ToastUtil.shortToast(this, "请先获取验证码");
		} else if (!mCodeText.matches(codeRegex)) {
			ToastUtil.shortToast(this, "验证码格式不正确");
		}
		id_commit.setEnabled(true);
	}

	/**
	 * 获取验证码
	 */
	@SuppressLint("NewApi")
	private void getCode() {
		// String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
		String phone = id_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.shortToast(mActivity, "请输入手机号码");
		} else {
			showLoadingDialog();
			id_send_code.setEnabled(false);
			id_send_code.setClickable(false);
			times = 29;
			mHandler.sendEmptyMessage(CHANGE_TIME);
			id_send_code.setBackground(getResources().getDrawable(
					R.drawable.shape_bg_gray_round_5dp_right_corner));
			Controller.getInstance().getCode(GlobalConstants.NUM_SENDCODE,
					phone, "FORGET", mCallback);
		}
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			if (msg.what == CHANGE_TIME) {
				times--;
				id_send_code.setText(times + "s");
				if (times < 0) {
					id_send_code.setText("重新发送");
					id_send_code.setBackground(getResources().getDrawable(
							R.drawable.shape_bg_red_round_5dp_right_corner));
					id_send_code.setEnabled(true);
					id_send_code.setClickable(true);
				} else
					mHandler.sendEmptyMessageDelayed(CHANGE_TIME, 1000);
			}
		};
	};

	/**
	 * 返回CallBack
	 */
	private CallBack mCallback = new CallBack() {
		// 成功获取验证码
		public void getCodeSuccess(final cn.com.cimgroup.bean.GetCode code) {
			runOnUiThread(new Runnable() {
				@SuppressLint("NewApi")
				@Override
				public void run() {
					hideLoadingDialog();
					if (code.getResCode().equals("0")) {
						ToastUtil.shortToast(mActivity, "验证码已下发");
						// 开始倒计时
						// times = 29;
						// mHandler.sendEmptyMessage(CHANGE_TIME);
						checkCode = code.getCheckCode();
						// id_send_code.setBackground(getResources().getDrawable(R.drawable.shape_bg_gray_round_5dp_right_corner));
						// 赋值mPhoneText，用于传值到下一个页面
						mPhoneText = id_phone.getText().toString().trim();
						// 赋值mUserId,用于传值到下一个页面
						mUserId = code.getUserId();
					} else {
						id_send_code.setEnabled(true);
						id_send_code.setClickable(true);
						// id_send_code.setBackground(getResources().getDrawable(R.drawable.shape_bg_gray_round_5dp_right_corner));
						id_send_code.setText("获取验证码");
						ToastUtil.shortToast(mActivity, code.getResMsg());
					}
				}
			});
		};

		// 获取验证码失败
		public void getCodeFailure(String error) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					id_send_code.setEnabled(true);
					id_send_code.setClickable(true);
					id_send_code.setBackgroundResource(R.color.color_red);
					id_send_code.setText("获取验证码");
				}
			});
		};

		// 验证用户密码成功
		public void validateCodeSuccess(final String json) {

			runOnUiThread(new Runnable() {
				public void run() {
					try {
						JSONObject object = new JSONObject(json);
						String resCode = object.optString("resCode", "");
						if (!TextUtils.isEmpty(resCode) && resCode.equals("0")) {
							// 验证成功 跳转提交修改密码页面
							ResetPwdActivity.toActivity(
									FindPasswordActivity.this, mPhoneText,
									mCodeText, mUserId);
						}else {
							// 验证失败
							ToastUtil.shortToast(FindPasswordActivity.this,
									"验证码错误，请确认后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			});

		};

		// 验证用户密码失败
		public void validateCodeFail(String msg) {

			runOnUiThread(new Runnable() {
				public void run() {
					ToastUtil.shortToast(FindPasswordActivity.this,
							"验证失败，请确认后重试");
				}
			});
		};

	};

}
