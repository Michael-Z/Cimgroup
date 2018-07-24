package cn.com.cimgroup.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Command;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 微信登陆--未绑定用户完善资料
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class WeChatImprove extends BaseActivity implements View.OnClickListener {
	/** 返回 */
	private TextView id_wechat_improve_activity_back;
	/** 获取验证码 */
	private TextView id_wechat_improve_getcode;
	/** 手机号 */
	private EditText id_wechat_improve_phone;
	/** 验证码 */
	private EditText id_wechat_improve_code;
	/** 用户名 */
	private EditText id_wechat_improve_username;
	/** 密码 */
	private EditText id_wechat_improve_password;
	/** 提交/完成 */
	private TextView id_improve_activity_commit;
	/** 用户名布局 */
	private LinearLayout id_wechat_improve_layout_username;
	/** 密码布局 */
	private LinearLayout id_wechat_improve_layout_password;

	/** WeChat openId */
	private String mOpenId;

	/** 获取验证码倒计时开始时间 */
	private static int second = 29;
	/** 通知时间倒计时 */
	private static final int TIME = 1;

	private static final int OTHER = 2;

	/** 请求接口 0=绑定手机号，1完善信息 */
	private int isImprove = 0;

	/** 保存用户信息（自动登陆+保存密码+登陆方式） **/
	private SharedPreferences shared;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == TIME) {
				if (second > 0) {
					second--;
					id_wechat_improve_getcode.setText(getResources().getString(
							R.string.register_again_second, second));
					mHandler.sendEmptyMessageDelayed(TIME, 1000);
				} else {
					second = 29;
					id_wechat_improve_getcode.setEnabled(true);
					id_wechat_improve_getcode.setSelected(false);
					id_wechat_improve_getcode.setText(getResources().getString(
							R.string.register_again));
				}
			} else if (msg.what == OTHER) {
				second = 29;
				id_wechat_improve_getcode.setClickable(true);
				id_wechat_improve_getcode.setEnabled(true);
				id_wechat_improve_getcode.setText(getResources().getString(
						R.string.register_again));
				id_wechat_improve_getcode.setBackgroundColor(getResources()
						.getColor(R.color.color_red));
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wechat_improve);
		initView();
		initEvent();
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_wechat_improve_activity_back = (TextView) findViewById(R.id.id_wechat_improve_activity_back);
		id_wechat_improve_getcode = (TextView) findViewById(R.id.id_wechat_improve_getcode);
		id_wechat_improve_phone = (EditText) findViewById(R.id.id_wechat_improve_phone);
		id_wechat_improve_code = (EditText) findViewById(R.id.id_wechat_improve_code);
		id_wechat_improve_username = (EditText) findViewById(R.id.id_wechat_improve_username);
		id_wechat_improve_password = (EditText) findViewById(R.id.id_wechat_improve_password);
		id_improve_activity_commit = (TextView) findViewById(R.id.id_improve_activity_commit);
		id_wechat_improve_layout_username = (LinearLayout) findViewById(R.id.id_wechat_improve_layout_username);
		id_wechat_improve_layout_password = (LinearLayout) findViewById(R.id.id_wechat_improve_layout_password);
		// 初始化后，隐藏用户名密码填写框
		id_wechat_improve_layout_username.setVisibility(View.GONE);
		id_wechat_improve_layout_password.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalTools.hideSoftInput(WeChatImprove.this);
	}

	@Override
	public void onPause() {
		GlobalTools.hideSoftInput(WeChatImprove.this);
		super.onPause();
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		id_wechat_improve_getcode.setOnClickListener(this);
		id_improve_activity_commit.setOnClickListener(this);
		id_wechat_improve_activity_back.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mOpenId = getIntent().getStringExtra("openId");
		// mIsAuto = getIntent().getBooleanExtra("auto",true);
		// mPassword = getIntent().getStringExtra("password");
		// mIsRem = getIntent().getBooleanExtra("rem",true);
		if (shared == null) {
			shared = getSharedPreferences(GlobalConstants.PATH_SHARED_MAC,
					MODE_PRIVATE);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		id_wechat_improve_getcode.setEnabled(true);
		id_wechat_improve_getcode.setClickable(true);
		initData();
	}

	private CallBack mBack = new CallBack() {
		/** 获取验证码成功 */
		public void getCodeSuccess(final GetCode code) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (code != null && code.getResCode().equals("0")) {
						second = 29;
						// 获取倒计时
//						mHandler.sendEmptyMessageDelayed(TIME, 1000);
					} else {
						ToastUtil.shortToast(WeChatImprove.this,
								code.getResMsg());
						second = 29;
						id_wechat_improve_getcode.setEnabled(true);
						id_wechat_improve_getcode.setClickable(true);
						mHandler.sendEmptyMessage(OTHER);

					}

				}
			});
		};

		/** 获取验证码成功 */
		public void getCodeFailure(String error) {
			hideLoadingDialog();
			ToastUtil.shortToast(WeChatImprove.this, error);
			second = 29;
			id_wechat_improve_getcode.setEnabled(true);
			id_wechat_improve_getcode.setClickable(true);
			mHandler.sendEmptyMessage(OTHER);
		};

		/** 手机验证成功 */
		public void weChatBindPhoneSuccess(final String json) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					id_wechat_improve_getcode.setEnabled(false);
					id_wechat_improve_getcode.setClickable(true);
					id_improve_activity_commit.setEnabled(true);
					id_improve_activity_commit.setSelected(false);
					getJson(json);
				}
			});
		};

		/** 手机验证失败 */
		public void weChatBindPhoneError(final String message) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					id_improve_activity_commit.setEnabled(true);
					id_improve_activity_commit.setSelected(false);
					ToastUtil.shortToast(WeChatImprove.this, message);
				}
			});
		};

		/** 用户注册成功 */
		public void weChatRegistUserSuccess(final String json) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					id_improve_activity_commit.setEnabled(true);
					id_improve_activity_commit.setSelected(false);
					getRegisterJson(json);
				}
			});
		};

		/** 用户注册失败 */
		public void weChatRegistUserError(String message) {
			hideLoadingDialog();
			id_improve_activity_commit.setEnabled(true);
			id_improve_activity_commit.setSelected(false);
			ToastUtil.shortToast(WeChatImprove.this, message);
		};

	};

	/**
	 * 解析json
	 * 
	 * @param json
	 */
	public void getJson(String json) {
		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null) {
				if (resCode.equals("0")) {
					String isBind = object.getString("isBind");
					if (isBind.equals("0")) {
						// 未绑定用户跳转绑定界面`
						mOpenId = object.getString("openId");
						// 显示 用户名跟密码的填写框
						id_wechat_improve_layout_username
								.setVisibility(View.VISIBLE);
						id_wechat_improve_layout_password
								.setVisibility(View.VISIBLE);
						isImprove = 1;// 设置为新注册状态
						id_wechat_improve_phone.setFocusable(false);
						id_wechat_improve_code.setFocusable(false);

					} else if (isBind.equals("1")) {
						// 已经绑定用户 获取绑定信息
						// okToJump(object);
						// if (App.userInfo == null) {
						// App.userInfo = new UserInfo();
						// }

						App.userInfo = (UserInfo) Parser.parse(Command.LOGIN,
								object);
						// 保存用户的登录状态为微信
						Editor ed = shared.edit();
						ed.putInt("loginpattern", 1);
						ed.putString("flag", "success");
						ed.putString("openid", object.getString("openId"));
						ed.commit();
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						finish();
					}
				} else if (resCode.equals("100201")) {
					ToastUtil.shortToast(WeChatImprove.this, "用户ID为空");
				} else if (resCode.equals("400002")) {
					ToastUtil.shortToast(WeChatImprove.this, "账户被冻结");
				} else if (resCode.equals("400001")) {
					ToastUtil.shortToast(WeChatImprove.this, "账户不存在");
				} else if (resCode.equals("100202")) {
					ToastUtil.shortToast(WeChatImprove.this, "用户不存在");
				} else {
					ToastUtil.shortToast(WeChatImprove.this, "验证码错误或者已经失效");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (CException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 绑定成功
	 * 
	 * @param object
	 */
	private void okToJump(JSONObject object) {
		try {
			if (App.userInfo == null) {
				App.userInfo = new UserInfo();
			}
			App.userInfo.setPassword(id_wechat_improve_password.getText()
					.toString().trim());
			App.userInfo.setIsCompUserName("0");
			App.userInfo.setIsBindedBank("0");
			App.userInfo.setUserId(object.getString("userId"));
			App.userInfo.setUserName(object.getString("userName"));
			App.userInfo.setMobile(id_wechat_improve_phone.getText().toString()
					.trim());
			// 保存用户的登录状态为微信
			Editor ed = shared.edit();
			ed.putInt("loginpattern", 1);
			ed.putString("flag", "success");
			ed.commit();
			UserLogic.getInstance().saveUserInfo(App.userInfo);
			startActivity(UserManageActivity.class);
			finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 微信注册成功
	 * 
	 * @param json
	 */
	private void getRegisterJson(String json) {
		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null && resCode.equals("0")) {
				// 微信绑定用户成功
				okToJump(object);
				// 用户注册成功
			} else if (resCode.equals("100101")) {
				ToastUtil.shortToast(WeChatImprove.this, "用户名已存在");
			} else if (resCode.equals("100102")) {
				ToastUtil.shortToast(WeChatImprove.this, "用户名为空");
			} else if (resCode.equals("100103")) {
				ToastUtil.shortToast(WeChatImprove.this, "密码为空");
			} else if (resCode.equals("100104")) {
				ToastUtil.shortToast(WeChatImprove.this, "手机号为空");
			} else if (resCode.equals("100105")) {
				ToastUtil.shortToast(WeChatImprove.this, "手机号格式错误");
			} else if (resCode.equals("100106")) {
				ToastUtil.shortToast(WeChatImprove.this, "手机号已经存在");
			} else if (resCode.equals("100107")) {
				ToastUtil.shortToast(WeChatImprove.this, "用户创建失败");
			} else if (resCode.equals("100108")) {
				ToastUtil.shortToast(WeChatImprove.this, "用户账号创建失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 账号匹配汉字、字母、数字、下划线 */
	private String mMatchesStrName = "^[a-zA-Z0-9_\u4e00-\u9fa5]{4,12}+$";
	/** 密码匹配字母、数字、下划线 */
	private String mMatchesStrPwd = "^[a-zA-Z0-9_]{6,16}+$";

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_wechat_improve_activity_back:
			startActivity(LoginActivity.class);
			finish();
			break;
		case R.id.id_wechat_improve_getcode:
			// 获取验证码

			String phone = id_wechat_improve_phone.getText().toString().trim();
			String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
			if (TextUtils.isEmpty(phone)) {
				ToastUtil.shortToast(WeChatImprove.this, "手机号不能为空");
			} else if (!phone.matches(telRegex)) {
				ToastUtil.shortToast(WeChatImprove.this, "请输入正确的手机号");
			} else {
				v.setEnabled(false);
				showLoadingDialog();
				// 验证手机号，和之后的获取验证码判断重复，故删除
				mHandler.sendEmptyMessageDelayed(TIME, 1000);
				Controller.getInstance().getCode(GlobalConstants.NUM_SENDCODE,
						phone, "WECHAT_LOGIN", mBack);
				v.setSelected(true);
			}

			break;

		case R.id.id_improve_activity_commit:
			String phoneC = id_wechat_improve_phone.getText().toString().trim();
			String code = id_wechat_improve_code.getText().toString().trim();
			String telRegexC = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
			// 提交/完成
			if (isImprove == 0) {
				// 请求绑定手机号接口

				if (TextUtils.isEmpty(phoneC)) {
					ToastUtil.shortToast(WeChatImprove.this, "手机号码不能为空");
				} else if (TextUtils.isEmpty(code)) {
					ToastUtil.shortToast(WeChatImprove.this, "验证码不能为空");
				} else if (!phoneC.matches(telRegexC)) {
					ToastUtil.shortToast(WeChatImprove.this, "请输入正确的手机号");
				} else {
					id_improve_activity_commit.setEnabled(false);
					id_improve_activity_commit.setSelected(true);
					Controller.getInstance().weiChatBindPhone(
							GlobalConstants.URL_WECHAT_BIND_PHONE, code,
							phoneC, mOpenId, mBack);
				}
			} else if (isImprove == 1) {
				String username = id_wechat_improve_username.getText()
						.toString().trim();
				String password = id_wechat_improve_password.getText()
						.toString().trim();
				int nameLength = TextUtils.isEmpty(username) ? 0
						: getUserNameLength(username);
				// 完善信息
				if (TextUtils.isEmpty(phoneC)) {
					ToastUtil.shortToast(WeChatImprove.this, "手机号码不能为空");
				} else if (TextUtils.isEmpty(code)) {
					ToastUtil.shortToast(WeChatImprove.this, "验证码不能为空");
				} else if (!phoneC.matches(telRegexC)) {
					ToastUtil.shortToast(WeChatImprove.this, "请输入正确的手机号");
				} else if (TextUtils.isEmpty(username)) {
					ToastUtil.shortToast(WeChatImprove.this, "用户名不能为空");
				} else if (!username.matches(mMatchesStrName) || nameLength < 4
						|| nameLength > 12||isNumeric(username)) {
					ToastUtil.shortToast(WeChatImprove.this,
							"4-12个字符或下划线，不能全为数字");
				} else if (TextUtils.isEmpty(password)) {
					ToastUtil.shortToast(WeChatImprove.this, "密码不能为空");
				} else if (!password.matches(mMatchesStrPwd)) {
					ToastUtil.shortToast(WeChatImprove.this, "请输入6-16位字符");
				} else {
					showLoadingDialog();
					id_improve_activity_commit.setEnabled(false);
					id_improve_activity_commit.setSelected(true);
					Controller.getInstance().weiChatRegistUser(
							GlobalConstants.URL_REGIST_WECHAT_USER, phoneC,
							mOpenId, username, password, mBack);
				}
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
	 * 
	 * @param mNameText2
	 */
	private int getUserNameLength(String mNameText2) {
		int count = 0;
		Pattern p = Pattern.compile(mMatchesStrName);
		Matcher m = p.matcher(mNameText2);
		System.out.print("提取出来的中文有：");
		while (m.find()) {
			count++;
		}
		return mNameText2.length() + count;
	}

	/**
	 * 点击返回键响应方法
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			startActivity(LoginActivity.class);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
