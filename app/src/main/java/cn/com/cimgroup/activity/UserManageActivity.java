package cn.com.cimgroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 完善用户资料
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月26日
 */
public class UserManageActivity extends BaseActivity implements OnClickListener {
	/** 用户名 */
	private TextView textView_usermanage_username;

	/** 用户手机号 */
	private TextView textView_usermanage_phone;

	/** 用户余额 */
	private TextView textView_usermanage_money;

	private EditText realName;

	private EditText idCard;

	private TextView cardText;

	private TextView card;
	/** 绑定、修改银行卡 */
	private TextView textView_usermanage_bindbank;

	private TextView mSubmit;

	// private TextView serverNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manage);

		initView();
	}

	private void initView() {
		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.textView_title_word);
		title.setText(getResources().getString(R.string.page_usermanage));

		textView_usermanage_username = ((TextView) findViewById(R.id.textView_usermanage_username));
		textView_usermanage_phone = (TextView) findViewById(R.id.textView_usermanage_phone);
		textView_usermanage_money = (TextView) findViewById(R.id.textView_usermanage_money);

		realName = (EditText) findViewById(R.id.editView_usermanage_realname);

		idCard = (EditText) findViewById(R.id.editView_usermanage_realnum);

		cardText = (TextView) findViewById(R.id.textView_usermanage_cardnum_text);

		card = (TextView) findViewById(R.id.editText_usermanage_cardnum);

		textView_usermanage_bindbank = (TextView) findViewById(R.id.textView_usermanage_bindbank);

		mSubmit = (TextView) findViewById(R.id.textView_usermanage_submit);

		TextView explain = (TextView) findViewById(R.id.textView_usermanage_explain);
		explain.setText(Html.fromHtml(getResources().getString(
				R.string.usermanage_explain_bind_card)));
		TextView explain1 = (TextView) findViewById(R.id.textView_usermanage_explain1);
		explain1.setText(Html.fromHtml(getResources().getString(
				R.string.usermanage_explain1)));
		explain1.setMovementMethod(LinkMovementMethod.getInstance());

		textView_usermanage_bindbank.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (App.userInfo != null) {
			textView_usermanage_username.setText(App.userInfo.getUserName());
			textView_usermanage_phone.setText(App.userInfo.getMobile());
			textView_usermanage_money.setText(App.userInfo.getBetAcnt()==null?"0":(App.userInfo.getBetAcnt()
					+ getResources().getString(R.string.price_unit)));
			if (App.userInfo.getIsCompUserName() != null) {
				if (App.userInfo.getIsCompUserName().equals("1")) {
					realName.setText(App.userInfo.getRealName());
					realName.setEnabled(false);
					idCard.setText(App.userInfo.getIdCard().substring(0, App.userInfo.getIdCard().length()-4)+" ****");
					idCard.setEnabled(false);
					mSubmit.setVisibility(View.GONE);
				} else {
					realName.setEnabled(true);
					idCard.setEnabled(true);
				}
			} else {
				realName.setEnabled(true);
				idCard.setEnabled(true);
			}

			if (Integer.parseInt(App.userInfo.getIsBindedBank()) == 1) {
				card.setVisibility(View.VISIBLE);
				card.setText(App.userInfo.getBankCardId().substring(0,4) +
						" **** **** " +
						App.userInfo.getBankCardId().substring(App.userInfo.getBankCardId().length()-4, App.userInfo.getBankCardId().length()));
				cardText.setVisibility(View.VISIBLE);
				textView_usermanage_bindbank.setText(getResources().getString(
						R.string.usermanage_resetbank));
			} else {
				card.setVisibility(View.GONE);
				cardText.setVisibility(View.GONE);
				textView_usermanage_bindbank.setText(getResources().getString(
						R.string.usermanage_bindbank));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	};

	CallBack mBack = new CallBack() {
		public void bindUserInfoSuccess(final UserInfo userInfo) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if(userInfo != null && userInfo.getResCode() != null && userInfo.getResCode().equals("0")){
						ToastUtil.shortToast(
								UserManageActivity.this,
								getResources().getString(
										R.string.usermanage_success));
						App.userInfo.setRealName(realName.getText().toString());
						App.userInfo.setIdCard(idCard.getText().toString());
						App.userInfo.setIsCompUserName("1");
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						finish();
					}else if(userInfo != null && userInfo.getResCode() != null && userInfo.getResMsg() != null){
						ToastUtil.shortToast(UserManageActivity.this, userInfo.getResMsg());
					}
				}
			});
		};

		public void bindUserInfoFailure(String error) {
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			// 获取MainActivity实例
			MainActivity mainActivity = (MainActivity) ActivityManager
					.isExistsActivity(MainActivity.class);
			if (mainActivity != null) {
				// 如果存在MainActivity实例，那么展示LotteryHallFrament页面
				mainActivity.showFrament(3);
			}
			finish();
			break;
		case R.id.textView_usermanage_bindbank:
			Intent intent = new Intent(UserManageActivity.this,
					CardAddCardActivity.class);
			String type = "1";
			if (Integer.parseInt(App.userInfo.getIsBindedBank()) == 1) {
				type = "2";
			} else {
				type = "1";
			}
			intent.putExtra("type", type);
			startActivity(intent);
			break;
		case R.id.textView_usermanage_submit:
			String idCardText = idCard.getText().toString();
			String realNameText = realName.getText().toString();
			String numRegex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
			boolean idCardRes = idCardText.matches(numRegex);
			if (idCardRes && !StringUtil.isEmpty(realNameText)
					&& !StringUtil.isEmpty(idCardText)) {
				showLoadingDialog();
				Controller.getInstance().bindUserInfo(
						GlobalConstants.NUM_BINDUSERINFO,
						App.userInfo.getUserId(), realNameText, idCardText,
						mBack);
			} else {
				if (StringUtil.isEmpty(realNameText)) {
					ToastUtil.shortToast(this,
							getResources()
									.getString(R.string.usermanage_error1));
				} else {
					if (StringUtil.isEmpty(idCardText)) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.usermanage_error3));
					} else {
						if (!idCardRes) {
							ToastUtil.shortToast(this, getResources()
									.getString(R.string.usermanage_error2));
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
