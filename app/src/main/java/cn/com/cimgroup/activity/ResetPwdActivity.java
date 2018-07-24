package cn.com.cimgroup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ToastUtil;

public class ResetPwdActivity extends BaseActivity implements OnClickListener,OnFocusChangeListener {
	
	/**用户的电话-标识**/
	private static final String PHONE = "phone";
	/**用户的验证码-标识**/
	private static final String CODE = "code";
	/**用户的userid-标识**/
	private static final String USERID = "userid";
	
	/**密码-编辑**/
	private EditText et_pwd;
	/**确认密码-编辑**/
	private EditText et_confirm_pwd;
	/**提示内容**/
	private TextView tv_explain;
	/** 提交**/
	private TextView tv_commit;
	/** 返回**/
	private TextView tv_back;
	
	/**电话内容**/
	private String mPhoneText;
	/**验证码内容**/
	private String mCodeText;
	/**用户id内容**/
	private String mUserId;
	/** 密码内容**/
	private String mPwdText;
	/** 确认密码内容**/
	private String mConfirmPwdText;
	
	/** 密码匹配字母、数字、下划线 */
	private String mMatchesStrPwd = "^[a-zA-Z0-9_]{6,16}+$";
	
	/**hint提示语-密码**/
	private String mDefaultPwd = "请输入新密码";
	/**hint提示语-确认密码**/
	private String mDefaultConfirmPwd = "请再次确认密码";
	/**密码是否合规**/
	private boolean mPwdRes;
	/**确认密码是否合规**/
	private boolean mPwdConfirmRes;
	
	private boolean isFirstShown;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		
		initView();
		initData();
		initEvent();
	}

	
	private void initEvent() {
		et_pwd.setOnFocusChangeListener(this);
		et_confirm_pwd.setOnFocusChangeListener(this);
		tv_commit.setOnClickListener(this);
		tv_back.setOnClickListener(this);
	}


	private void initData() {
		
		Intent intent = getIntent();
		mPhoneText = intent.getStringExtra(PHONE);
		mCodeText = intent.getStringExtra(CODE);
		mUserId = intent.getStringExtra(USERID);
		
		isFirstShown = true;
		
	}

	private void initView() {
		
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
		tv_explain = (TextView) findViewById(R.id.tv_explain);
		tv_explain.setText(Html.fromHtml("如有疑问请联系客服，"+getResources().getString(R.string.login_phonetext)));
		tv_commit = (TextView) findViewById(R.id.tv_commit);
		
		tv_back = (TextView) findViewById(R.id.tv_back);
		
	}

	/**
	 * 跳转限定--从忘记密码（获取验证码）页面跳转过来的请求
	 * @param context
	 * @param phone
	 * @param code
	 * @param userid
	 */
	public static void toActivity(Context context, String phone, String code, String userid){
		
		Intent intent = new Intent();
		intent.setClass(context, ResetPwdActivity.class);
		//传递用户电话
		intent.putExtra(PHONE, phone);
		//传递用户验证码
		intent.putExtra(CODE, code);
		//传递用户userid
		intent.putExtra(USERID, userid);
		context.startActivity(intent);
		
		Log.e("wushiqiu", phone+"------------"+code+"--------------"+userid);
		
	}

	@Override
	public void onClick(View view) {
		
		
		switch (view.getId()) {
		
			//提交按钮的点击事件
		case R.id.tv_commit:
			
			tv_commit.setClickable(false);
			//隐藏软键盘
			GlobalTools.hideSoftInput(ResetPwdActivity.this);
			mConfirmPwdText = et_confirm_pwd.getText().toString().trim();
			mPwdConfirmRes = mConfirmPwdText.matches(mMatchesStrPwd);
			mPwdText = et_pwd.getText().toString().trim();
			mPwdRes = mPwdText.matches(mMatchesStrPwd);
			if (!TextUtils.isEmpty(mConfirmPwdText)  && !TextUtils.isEmpty(mPwdText) ) {
				
				if (mPwdRes && mPwdConfirmRes) {
					if (mPwdText.equals(mConfirmPwdText)) {
						//发送请求
						Controller.getInstance().getBackPWD(
								GlobalConstants.NUM_GETBACKPWD, mUserId, mPhoneText,
								mCodeText, mPwdText, mBack);
					}else {
						ToastUtil.shortToast(ResetPwdActivity.this, "新旧密码不一致！");
						tv_commit.setClickable(true);
					}
					
				}else if (!mPwdRes) {
					
					ToastUtil.shortToast(ResetPwdActivity.this, "新密码为6-16个汉字、字母、数字或下划线！");
					tv_commit.setClickable(true);
				}else if (!mPwdConfirmRes) {
					
					ToastUtil.shortToast(ResetPwdActivity.this, "确认密码为6-16个汉字、字母、数字或下划线！");
					tv_commit.setClickable(true);
				}
				
				
			}else if(TextUtils.isEmpty(mPwdText)){
				
				ToastUtil.shortToast(ResetPwdActivity.this, "新密码不能为空！");
				tv_commit.setClickable(true);
			}else if (TextUtils.isEmpty(mConfirmPwdText)) {
				
				ToastUtil.shortToast(ResetPwdActivity.this,"确认密码不能为空！");
				tv_commit.setClickable(true);
			}
			
			break;
			
		case R.id.tv_back:
			
			ResetPwdActivity.this.finish();

			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 请求回调
	 */
	private CallBack mBack = new CallBack() {
		
		public void getBackPWDSuccess(final UserInfo info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					tv_commit.setEnabled(true);
					if (Integer.parseInt(info.getResCode()) == 0) {
						ToastUtil.shortToast(ResetPwdActivity.this,
								"密码修改成功，请妥善保管！");
						UserLogic.getInstance().clearUserInfo();
						
						FindPasswordActivity.instance.finish();
						ResetPwdActivity.this.finish();
					} else {
						ToastUtil.shortToast(ResetPwdActivity.this,
								info.getResMsg());
					}
				}
			});
		};
		
		public void getBackPWDFailure(String error) {
			tv_commit.setEnabled(true);
		}
		
	};

	/**
	 * 焦点切换事件提示
	 */
	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		switch (view.getId()) {
		case R.id.et_pwd:
			
			mPwdText = et_pwd.getText().toString().trim();
			mPwdRes = mPwdText.matches(mMatchesStrPwd);
			if (hasFocus) {
				if (!TextUtils.isEmpty(mPwdText)) {
					et_pwd.setHint("");
				}else {
				}
			}else {
				if (TextUtils.isEmpty(mPwdText)) {
					et_pwd.setHint(mDefaultPwd);
					ToastUtil.shortToast(this, "密码不能为空");
				}else if (!mPwdRes) {
					ToastUtil.shortToast(this, "密码为6-16个汉字、字母、数字或下划线");
				}
				
			}
			
			break;

		case R.id.et_confirm_pwd:
			
			mConfirmPwdText = et_confirm_pwd.getText().toString().trim();
			mPwdConfirmRes = mConfirmPwdText.matches(mMatchesStrPwd);
			if (hasFocus) {
				if (!TextUtils.isEmpty(mConfirmPwdText)) {
					et_confirm_pwd.setHint("");
				}else {
				}
			}else {
				if (TextUtils.isEmpty(mConfirmPwdText)) {
					et_confirm_pwd.setHint(mDefaultConfirmPwd);
					ToastUtil.shortToast(this, "确认密码不能为空");
				}else if (!mPwdConfirmRes) {
					ToastUtil.shortToast(this, "确认密码为6-16个汉字、字母、数字或下划线");
				}
				
			}
			
			break;
		default:
			break;
		}
		
	}
}
