package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.FindPasswordActivity;
import cn.com.cimgroup.activity.RegisterActivity;
import cn.com.cimgroup.activity.WeChatImprove;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Command;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.util.JPushUtils;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.jpush.android.api.JPushInterface;


/**
 * 个人中心
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class LoginFragment extends BaseFrament implements OnClickListener {
	// 在用户登录成功后切换界面，开始的时候根据用户是否登录然后分别创建不同的View
	/** 登录界面 */
	private View mView;
	/** 个人中心界面 */
	private View mPersonView;

	// 登录界面声明变量
	/** 登录名 */
	private EditText id_login_name;
	/** 登录密码 */
	private EditText id_login_password;

	/** 自动登陆图片 */
	private ImageView id_login_auto_image;

	/** 是否记住密码 **/
	private boolean mIsRem = true;

	/** 是否自动登录 **/
	private boolean mIsAuto = true;
	/** 登陆时记录的用户密码 */
	private String mPassword = "";

	/** 保存用户信息（自动登陆+保存密码+登陆方式） **/
	private SharedPreferences shared;

	// 个人中心界面声明变量
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// 用户未登录
		mView = inflater.inflate(R.layout.fragment_login, container, false);
		// 初始化登录界面视图组件
		initLoginView();
		// 绑定登录界面事件
		initLoginEvent();
		return mView;
	}

	/**
	 * 绑定登录界面事件
	 */
	private void initLoginEvent() {
		// 自动登录
		mView.findViewById(R.id.id_login_auto).setOnClickListener(this);
		// 忘记密码
		mView.findViewById(R.id.id_password_forgot).setOnClickListener(this);
		// 注册
		mView.findViewById(R.id.id_register).setOnClickListener(this);
		// 登录
		mView.findViewById(R.id.id_login).setOnClickListener(this);
		
		mView.findViewById(R.id.id_weixin_load).setOnClickListener(this);

	}

	/**
	 * 初始化登录界面视图组件
	 */
	@SuppressWarnings("static-access")
	private void initLoginView() {
		id_login_name = (EditText) mView.findViewById(R.id.id_login_name);
		id_login_password = (EditText) mView
				.findViewById(R.id.id_login_password);
		id_login_auto_image = (ImageView) mView
				.findViewById(R.id.id_login_auto_image);
		id_login_auto_image.setSelected(true);
		shared = getActivity().getSharedPreferences(
				GlobalConstants.PATH_SHARED_MAC, getActivity().MODE_PRIVATE);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLoginReceiver!=null) {
			getActivity().unregisterReceiver(mLoginReceiver);
			mLoginReceiver=null;
		}
	}
	private long startTime=0;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_login_auto:
			// 自动登陆
			id_login_auto_image.setSelected(!id_login_auto_image.isSelected());
			mIsAuto = id_login_auto_image.isSelected();
			break;
		case R.id.id_password_forgot:
			// 忘记密码
			// startActivity(new Intent(getActivity(),
			// LoginGetBackPwdActivity.class));
			Intent intent = new Intent();
			intent.setClass(getActivity(), FindPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.id_register:
			// 注册
			startActivity(new Intent(getActivity(), RegisterActivity.class));
			break;
		case R.id.id_login:
			// 登录
			doLogin();
			break;
		case R.id.id_weixin_load:
			//微信登陆
			weiXinLogin();
			break;
		default:
			break;
		}
	}
	/**
	 * 登录
	 */
	private void doLogin() {
		String username = id_login_name.getText().toString().trim();
		mPassword = id_login_password.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			ToastUtil.shortToast(getActivity(), "请输入用户名/手机号");
		} else if (TextUtils.isEmpty(mPassword)) {
			ToastUtil.shortToast(getActivity(), "请输入密码");
		} else if (mPassword.length() < 6) {
			ToastUtil.shortToast(getActivity(), "密码至少需要6位数字或者字母");
		} else {
			showLoadingDialog();
			Controller.getInstance().login(GlobalConstants.NUM_LOGIN, username,
					mPassword, mBack);
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
	private WeiXinLoginReceiver mLoginReceiver;

	/**微信登录*/
	private void weiXinLogin() {
		if (System.currentTimeMillis() - startTime > 800) {
			showLoadingDialog();
			Controller.getInstance().getAppId(
					GlobalConstants.URL_WECHAT_GET_APPID, "1", mBack);
			startTime = System.currentTimeMillis();
		}
		
		
		if (mLoginReceiver == null) {
			mLoginReceiver = new WeiXinLoginReceiver();
			
		}
		if (intentFilter==null) {
			intentFilter = new IntentFilter("com.lebocp.wxlogin");
		}
		getActivity().registerReceiver(mLoginReceiver, intentFilter);
	}
	private IntentFilter intentFilter;
	/**
	 * 获取code
	 * 
	 * @param appId
	 */
	private void loginWithWeixin(String appId) {
		if (!WXAPIFactory.createWXAPI(getActivity(), appId).isWXAppInstalled()) {
			ToastUtil.shortToast(getActivity(), "未安装微信客户端,不能使用微信登陆");
			hideLoadingDialog();
		} else {
			api = WXAPIFactory.createWXAPI(getActivity(), appId, true);
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
		}
	}
	/**进行极光推送注册*/
	private void doRegistJPush(){
		Controller.getInstance().registerPush(GlobalConstants.NUM_JPUSH, App.userInfo.getUserId(), App.userInfo.getMobile(), getAppKey(getActivity()), JPushInterface
				.getRegistrationID(getActivity()), mBack);
	}
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
				appKey = metaData.getString("JPUSH_APPKEY");
				if ((null == appKey) || appKey.length() != 24) {
					appKey = null;
				}
			}
		} catch (NameNotFoundException e) {

		}
		return appKey;
	}
	/**
	 * 网络请求回调
	 */
	private CallBack mBack = new CallBack() {
		public void loginSuccess(final UserInfo info) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					GlobalTools.hideSoftInput(getActivity());
//					hideLoadingDialog();
					if (info != null && !TextUtils.isEmpty(info.getResCode())
							&& info.getResCode().equals("0")) {
						App.userInfo = info;
						App.userInfo.setPassword(mPassword);
						Editor ed = shared.edit();
						ed.putBoolean("rem", mIsRem);
						ed.putBoolean("auto", mIsAuto);
						ed.putInt("loginpattern", 2);
						ed.putString("flag", "success");
						ed.commit();
						//进行极光注册
						doRegistJPush();
						// 将用户信息保存到配置文件中
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						// 替换成个人中心界面
						id_login_name.setText("");
						id_login_password.setText("");
						if (mLoginListener!=null) {
							mLoginListener.onLogin();
						}
					}else {
						hideLoadingDialog();
						ToastUtil.shortToast(getActivity(), info.getResMsg());
					}				
				}
			});
		};

		public void loginFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(getActivity(), error);
				}
			});
		};
		
		public void registerPushSuccess(GetCode code) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					App.userInfo
							.setJpushAppKey(getAppKey(getActivity()));
					App.userInfo.setJpushRegistrationID(JPushInterface
							.getRegistrationID(getActivity()));
					UserLogic.getInstance().saveUserInfo(App.userInfo);
					JPushUtils.getInstance(getActivity()).setJPushAlias(false);
				}
			});
		};

		public void registerPushFailure(String error) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					hideLoadingDialog();
					JPushUtils.getInstance(getActivity()).setJPushAlias(false);
				}
			});
		};

		
		
		
		
		
		/** 获取appID成功 */
		public void getAppIdSuccess(
				final cn.com.cimgroup.bean.AppIdBean appIdBean) {
			getActivity().runOnUiThread(new Runnable() {
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
							ToastUtil.shortToast(getActivity(),
									appIdBean.getResMsg());
						}
					}
				}
			});
		};
		/** 获取appID失败 */
		public void getAppIdError(final String message) {
			
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(getActivity(), message);
				}
			});
			hideLoadingDialog();
			ToastUtil.shortToast(getActivity(), message);
		};
		
		/** 微信登陆--后台审核成功成功 */
		public void weChatLoadSuccess(final String json) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					getJson(json);
				};
			});
		};

		/** 微信登陆--后台审核成功失败 */
		public void weChatLoadError(final String message) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(getActivity(), message);
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
			String resCode = object.getString("resCode");
			if (!TextUtils.isEmpty(resCode)) {
				if (resCode.equals("0")) {
					String isBind = object.getString("isBind");
					if (isBind.equals("0")) {
						// 未绑定用户跳转绑定界面
						String openId = object.getString("openId");
						Intent intent = new Intent();
						intent.setClass(getActivity(), WeChatImprove.class);
						//未绑定用户去绑定，暂不保存信息
						intent.putExtra("openId", openId);
						startActivity(intent);
					} else if (isBind.equals("1")) {
						// 已经绑定用户 获取绑定信息
						App.userInfo = (UserInfo) Parser.parse(Command.LOGIN,
								object);
						Editor ed = shared.edit();
						ed.putString("flag", "success");
						ed.putString("openid", object.getString("openId"));
						ed.commit();
						doRegistJPush();
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						//通知父类更改视图
						if (mLoginListener!=null) {
							mLoginListener.onLogin();
						}
					}
				}else{
					ToastUtil.shortToast(getActivity(), object.optString("resCode","数据异常"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (CException e) {
			e.printStackTrace();
		}
	}
	private OnLoginListener mLoginListener;

	public void setOnLoginListener(OnLoginListener listener) {
		mLoginListener = listener;
	}

	/**
	 * 登陆监听
	 * 
	 * @author 秋风
	 * 
	 */
	interface OnLoginListener {
		void onLogin();
	}

	@Override
	protected void lazyLoad() {
		
	}
	private String mCode;
	/**微信登陆广播*/
	class WeiXinLoginReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			mCode = App.getWxCode();
			if (!mCode.equals("-1")) {// 如果mCode！=null则进行微信登陆
				showLoadingDialog();
				Controller.getInstance().weiChatLoad(
						GlobalConstants.URL_WECHAT_LOAD, mCode, mBack);
				App.setWxCode("-1");
			}
		}
		
	}

}
