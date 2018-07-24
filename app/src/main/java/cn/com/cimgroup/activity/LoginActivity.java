package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.frament.LoboDrawFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Command;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.util.JPushUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.jpush.android.api.JPushInterface;


/**
 * 登录
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener,
		OnFocusChangeListener {
	/** 微信登陆按钮 */
	private ImageView id_weixin_load;

	/** 登录 */
	private TextView id_login;

	private static final String CALLTHEPAGE = "callthepage";


	private String mDefaultName = "";

	private String mDefaultPwd = "";

	// 用户名
	private EditText mName;

	// 密码ba
	private EditText mPassword;

	private String mNameText;

	private String mPwdText;

	/** 是否记住密码 **/
	private boolean mIsRem = true;

	/** 是否自动登录 **/
	private boolean mIsAuto = true;

	/** 页面调用者标识、某些页面需要做些不同的操作 **/
	private CallThePage mCallThePage;

	private ImageView id_login_auto_image;

	private String mCode = "";
	
	/** 入口是比分直播-关注**/
	private String source = "";
	
	/** 开奖中心**/
    private String source_fragment = "";

	/** 保存用户信息（自动登陆+保存密码+登陆方式） **/
	private SharedPreferences shared;

	/**
	 * 调用此页面的来源
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月4日
	 */
	public enum CallThePage {
		// 会员中心
		LOBOPERCENTER,
		// 购彩大厅
		LOBOHALL,
	}

	public static void forwartLoginActivity(Context mContext,
			CallThePage callThePage) {
		Intent tIntent = new Intent(mContext, LoginActivity.class);
		tIntent.putExtra(CALLTHEPAGE, callThePage);
		mContext.startActivity(tIntent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mCallThePage = (CallThePage) getIntent().getSerializableExtra(
				CALLTHEPAGE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initDatas();
		initEvent();
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {

		mCode = getIntent().getStringExtra("code");
		source = getIntent().getStringExtra(ScoreListActivity.SOURCE_ACTIVITY);
		source_fragment = getIntent().getStringExtra(LoboDrawFrament.SOURCE_FRAGMENT);
		shared = getSharedPreferences(GlobalConstants.PATH_SHARED_MAC,
				MODE_PRIVATE);
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {

		// 微信登陆
		id_weixin_load.setOnClickListener(this);
		// 普通登陆
		id_login.setOnClickListener(this);
	}

	private void initView() {

		// 微信登陆
		id_weixin_load = (ImageView) findViewById(R.id.id_weixin_load);
		mDefaultName = getResources().getString(R.string.login_name);
		mDefaultPwd = getResources().getString(R.string.login_password);
		// 登录
		id_login = (TextView) findViewById(R.id.id_login);

		mName = (EditText) findViewById(R.id.id_login_name);

		mPassword = (EditText) findViewById(R.id.id_login_password);

		// 自动登录
		((LinearLayout) findViewById(R.id.id_login_auto))
				.setOnClickListener(this);
		id_login_auto_image = (ImageView) findViewById(R.id.id_login_auto_image);
		id_login_auto_image.setSelected(true);

		TextView expain = (TextView) findViewById(R.id.textView_login_expain);
		expain.setText(Html.fromHtml(getResources().getString(
				R.string.login_phonetext)));
		expain.setMovementMethod(LinkMovementMethod.getInstance());

		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_register).setOnClickListener(this);
		findViewById(R.id.id_password_forgot).setOnClickListener(this);
		mName.setOnFocusChangeListener(this);
		mPassword.setOnFocusChangeListener(this);
	}

	private long startTime = 0;

	@Override
	public void onResume() {
		super.onResume();
		long endTime = System.currentTimeMillis();
		if (endTime - startTime > 1000) {
//			id_login.setEnabled(true);
			if (App.userInfo != null) {
				boolean isRem = shared.getBoolean("rem", true);
				if (isRem) {
					mName.setText(App.userInfo.getUserName());
					if (App.userInfo.getPassword() != null) {
						mPassword.setText(App.userInfo.getPassword());
					}
				}
			}
			mCode = App.getWxCode();
			if (!mCode.equals("-1")) {// 如果mCode！=null则进行微信登陆
				showLoadingDialog();
				Controller.getInstance().weiChatLoad(
						GlobalConstants.URL_WECHAT_LOAD, mCode, mBack);
				App.setWxCode("-1");
			}
			startTime = endTime;
		}
	}

	@Override
	protected void onDestroy() {
		Controller.getInstance().removeCallback(mBack);
		super.onDestroy();
	}

	public static final String KEY_APP_KEY = "JPUSH_APPKEY";

	public static String getAppKey(Context context) {
		Bundle metaData = null;
		String appKey = null;
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai)
				metaData = ai.metaData;
			if (null != metaData) {
				appKey = metaData.getString(KEY_APP_KEY);
				if ((null == appKey) || appKey.length() != 24) {
					appKey = null;
				}
			}
		} catch (NameNotFoundException e) {

		}
		return appKey;
	}

	// 登录请求的回调方法
	private CallBack mBack = new CallBack() {
		public void loginSuccess(final UserInfo userInfo) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					hideLoadingDialog();
					if (userInfo != null && userInfo.getResCode() != null
							&& Integer.parseInt(userInfo.getResCode()) == 0) {
						App.userInfo = userInfo;
						App.userInfo.setPassword(mPwdText);
						Editor ed = shared.edit();
						ed.putBoolean("rem", mIsRem);
						ed.putBoolean("auto", mIsAuto);
						ed.putInt("loginpattern", 2);
						ed.putString("flag", "success");
						ed.commit();
						// 将用户信息保存到配置文件中
						Controller.getInstance().registerPush(GlobalConstants.NUM_JPUSH, App.userInfo.getUserId(), App.userInfo.getMobile(), getAppKey(getApplicationContext()), JPushInterface
							.getRegistrationID(getApplicationContext()), mBack);
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						
						//针对极光推送，登陆后继续做极光跳转处理
						if (!StringUtil.isEmpty(GlobalConstants.JPUSHEXTRA)) {
							JPushUtils.parserMessage(LoginActivity.this, GlobalConstants.JPUSHEXTRA, GlobalConstants.JPUSHALERT);
						}
//						LoginActivity.this.finish();
					} else {
//						id_login.setEnabled(true);
						hideLoadingDialog();
						ToastUtil.shortToast(LoginActivity.this,
								userInfo.getResMsg());
					}
				}
			});
		};

		// 登录失败
		public void loginFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
//					id_login.setEnabled(true);
					ToastUtil.shortToast(LoginActivity.this, error);
				}
			});
		};

		public void registerPushSuccess(GetCode code) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					App.userInfo
							.setJpushAppKey(getAppKey(getApplicationContext()));
					App.userInfo.setJpushRegistrationID(JPushInterface
							.getRegistrationID(getApplicationContext()));
					UserLogic.getInstance().saveUserInfo(App.userInfo);
					
					//只有当  入口为"比分直播-关注"的时候 才需要通知ScoreListActivity
					if (!TextUtils.isEmpty(source) && ScoreListActivity.SOURCE_ACTIVITY.equals(source)) {
						//发送广播到ScoreListActivity 登陆完成
						Intent intent = new Intent();
						intent.setAction("login_activity_wushiqiu");
						sendBroadcast(intent);
					}
					if (!TextUtils.isEmpty(source_fragment) 
							&& LoboDrawFrament.SOURCE_FRAGMENT.equals(source_fragment)) {
						//入口是    开奖中心-消息推送  登陆成功 跳转消息推送
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, CenterPushSetActivity.class);
						startActivity(intent);
						
					}
					JPushUtils.getInstance(mActivity).setJPushAlias(false);
					LoginActivity.this.finish();
				}
			});
		};

		public void registerPushFailure(String error) {
			runOnUiThread(new Runnable(){
				@Override
				public void run(){
					hideLoadingDialog();
					JPushUtils.getInstance(mActivity).setJPushAlias(false);
					LoginActivity.this.finish();
				}
			});
		};

		/** 获取appID成功 */
		public void getAppIdSuccess(
				final cn.com.cimgroup.bean.AppIdBean appIdBean) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();

					if (appIdBean != null) {
						if (appIdBean.getResCode() == 0) {
							Message msg = mHandler.obtainMessage();
							msg.what = GETAPPIDSUCCESS;
							msg.obj = appIdBean.getAppId();
							mHandler.sendMessage(msg);
						} else {
							ToastUtil.shortToast(LoginActivity.this,
									appIdBean.getResMsg());
						}
					}
				}
			});
		};

		/** 获取appID失败 */
		public void getAppIdError(String message) {
			hideLoadingDialog();
			ToastUtil.shortToast(LoginActivity.this, message);
		};

		/** 微信登陆--后台审核成功成功 */
		public void weChatLoadSuccess(final String json) {
			runOnUiThread(new Runnable() {
				public void run() {
//					hideLoadingDialog();
					getJson(json);
				};
			});
		};

		/** 微信登陆--后台审核成功失败 */
		public void weChatLoadError(final String message) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(LoginActivity.this, message);
				}
			});
		}
	};

	/**
	 * 解析json
	 * 
	 * @param json
	 */
	public void getJson(String json) {
		try {
			JSONObject object = new JSONObject(json);
//			Log.e("qiufeng", json);
			String resCode = object.getString("resCode");
			if (!TextUtils.isEmpty(resCode)) {
				if (resCode.equals("0")) {
					String isBind = object.getString("isBind");
					if (isBind.equals("0")) {
						// 未绑定用户跳转绑定界面
						String openId = object.getString("openId");
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, WeChatImprove.class);
						//未绑定用户去绑定，暂不保存信息
//						Editor ed = shared.edit();
//						ed.putString("flag", "continue");
//						ed.putString("openid", object.getString("openId"));
//						ed.commit();
						intent.putExtra("openId", openId);

						startActivity(intent);
						finish();
					} else if (isBind.equals("1")) {
						// 已经绑定用户 获取绑定信息
						// if (App.userInfo == null) {
						// App.userInfo = new UserInfo();
						// }
						App.userInfo = (UserInfo) Parser.parse(Command.LOGIN,
								object);
						Editor ed = shared.edit();
						ed.putString("flag", "success");
						ed.putString("openid", object.getString("openId"));
						ed.commit();
						// 将用户信息保存到配置文件中
						Controller.getInstance().registerPush(GlobalConstants.NUM_JPUSH, App.userInfo.getUserId(), App.userInfo.getMobile(), getAppKey(getApplicationContext()), JPushInterface
							.getRegistrationID(getApplicationContext()), mBack);
						UserLogic.getInstance().saveUserInfo(App.userInfo);
//						finish();
					}

				}else{
					ToastUtil.shortToast(mActivity, object.optString("resCode","数据异常"));
				}
					
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (CException e) {
			e.printStackTrace();
		}
	}

	/** 成功获取到AppId通知 */
	private final static int GETAPPIDSUCCESS = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETAPPIDSUCCESS:
				App.wxAppId= (String) msg.obj;
				loginWithWeixin(App.wxAppId);
				break;

			default:
				break;
			}
		};
	};
	private IWXAPI api;

	/**
	 * 获取code
	 * 
	 * @param appId
	 */
	private void loginWithWeixin(String appId) {
		if (!WXAPIFactory.createWXAPI(this, appId).isWXAppInstalled()) {
			ToastUtil.shortToast(this, "未安装微信客户端,不能使用微信登陆");
			hideLoadingDialog();
		} else {
			api = WXAPIFactory.createWXAPI(this, appId, true);
			api.registerApp(appId);
			SendAuth.Req req = new SendAuth.Req();
			//授权读取用户信息  
			req.scope = "snsapi_userinfo";
			//自定义信息 
			req.state = "wechat_sdk_demo_test";
			//向微信发送请求
			api.sendReq(req);

			Editor ed = shared.edit();
			ed.putBoolean("rem", mIsRem);
			ed.putBoolean("auto", mIsAuto);
			ed.putInt("loginpattern", 1);
			ed.putString("flag", "continue");
			ed.putString("source", "activity_login");
			ed.commit();
			App.callForWXAuthority = "loginActivity";
//			finish();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 微信登陆
		case R.id.id_weixin_load:
			if (System.currentTimeMillis() - startTime > 800) {
				showLoadingDialog();
				Controller.getInstance().getAppId(
						GlobalConstants.URL_WECHAT_GET_APPID, "1", mBack);
				startTime = System.currentTimeMillis();
			}
			break;
		case R.id.id_back:
			GlobalTools.hideSoftInput(LoginActivity.this);
			if (mCallThePage == CallThePage.LOBOPERCENTER
					|| mCallThePage == CallThePage.LOBOHALL) {
				// 获取MainActivity实例
				MainActivity mainActivity = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (mainActivity != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					mainActivity.showFrament(0);
					Intent i = new Intent(this, MainActivity.class);
					startActivity(i);
				}
			}
			finish();
			break;
		case R.id.id_login:
			// 如果都输入正确，才能发出登录请求 5-5 bug219修改 下边两行是原代码
			mNameText = mName.getText().toString();
			mPwdText = mPassword.getText().toString();

			if (mNameText.length() > 0 && mPwdText.length() >= 6) {
//				id_login.setEnabled(false);
				showLoadingDialog();
				Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
						mNameText, mPwdText, mBack);
			} else {
				if (mNameText.length() == 0) {
					ToastUtil.shortToast(
							this,
							getResources().getString(
									R.string.login_password_error1));
				} else {
					if (mPwdText.length() == 0) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.login_password));
					} else {
						if (mPwdText.length() < 6) {
							ToastUtil.shortToast(this, getResources()
									.getString(R.string.login_password_error));
						}
					}
				}
			}
			break;
		// 注册
		case R.id.id_register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.id_password_forgot:
			//找回密码  2.5期
//			startActivity(FindPasswordActivity.class);
			startActivity(new Intent(this, LoginGetBackPwdActivity.class));
			break;
		// 自动登陆
		case R.id.id_login_auto:
			if (id_login_auto_image.isSelected()) {
				id_login_auto_image.setSelected(false);
			} else {
				id_login_auto_image.setSelected(true);
			}
			mIsAuto = id_login_auto_image.isSelected();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mCallThePage == CallThePage.LOBOPERCENTER
					|| mCallThePage == CallThePage.LOBOHALL) {
				// 获取MainActivity实例
				MainActivity mainActivity = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (mainActivity != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					mainActivity.showFrament(0);
					Intent i = new Intent(this, MainActivity.class);
					startActivity(i);

				}
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.id_login_name:
			if (hasFocus) {
				mName.setHint("");
				GlobalTools.openSoftInput(mName, LoginActivity.this);
			} else {
				mNameText = mName.getText().toString();
				if (TextUtils.isEmpty(mNameText)) {
					mName.setHint(mDefaultName);
					ToastUtil.shortToast(LoginActivity.this, "用户名/手机号不能为空");
				}
			}
			break;
		case R.id.id_login_password:
			if (hasFocus) {
				mPassword.setHint("");
				GlobalTools.openSoftInput(mPassword, LoginActivity.this);
			} else {
				mPwdText = mPassword.getText().toString();
				if (TextUtils.isEmpty(mPwdText)) {
					mPassword.setHint(mDefaultPwd);
					ToastUtil.shortToast(
							this,
							getResources().getString(
									R.string.login_password_error1));
				} else if (mPwdText.length() < 6) {
					ToastUtil.shortToast(
							this,
							getResources().getString(
									R.string.login_password_error));

				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		hideLoadingDialog();
	}
	
}
