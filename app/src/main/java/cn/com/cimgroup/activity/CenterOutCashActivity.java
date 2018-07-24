package cn.com.cimgroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 提现
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class CenterOutCashActivity extends BaseActivity implements
		OnClickListener {

	private String mTitleText = "";
	/** 输入的提现金额 */
	private EditText money;
	/** 提现 */
	private TextView textView_outcash_submit;
	/** 修改银行卡 */
	private TextView id_update_card;

	// 是否快速提现 0-否 1-是，目前都是普通提现，没有快速提现
	private String isQuick = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mcenter_outcash);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (StringUtil.isEmpty(App.userInfo.getBankCardId())) {
			// 去绑定银行卡
		}
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	}

	private void initView() {
		mTitleText = getResources().getString(R.string.tradetype5);
		// title 左侧的文字
		TextView mTitleName = (TextView) findViewById(R.id.textView_title_word);
		mTitleName.setText(mTitleText);

		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);
		id_update_card = (TextView) findViewById(R.id.id_update_card);
		if (App.userInfo != null && App.userInfo.getIsBindedBank() != null
				&& App.userInfo.getIsBindedBank().equals("1")) {
			id_update_card.setVisibility(View.VISIBLE);
			id_update_card.setOnClickListener(this);
		} else {
			id_update_card.setVisibility(View.GONE);
		}

		TextView bankNum = (TextView) findViewById(R.id.textView_outcash_banknum);
		if (!TextUtils.isEmpty(App.userInfo.getBankCardId())) {
			bankNum.setText(App.userInfo.getBankCardId().substring(0, 4)
					+ "  ****  *****  "
					+ App.userInfo.getBankCardId().substring(
							App.userInfo.getBankCardId().length() - 4,
							App.userInfo.getBankCardId().length()));

		}else {
			startActivity(UserManageActivity.class);
			finish();
		}

		TextView bankName = (TextView) findViewById(R.id.textView_outcash_bankname);
		bankName.setText(App.userInfo.getBankName());

		TextView allMoney = (TextView) findViewById(R.id.textView_outcash_allmoney);
		allMoney.setText(App.userInfo.getPrizeAcnt());

		TextView realName = (TextView) findViewById(R.id.textView_outcash_realname);
		realName.setText(App.userInfo.getRealName());

		money = (EditText) findViewById(R.id.editText_outcash_money);

		textView_outcash_submit = ((TextView) findViewById(R.id.textView_outcash_submit));
		textView_outcash_submit.setOnClickListener(this);

		TextView explain = (TextView) findViewById(R.id.textView_outcash_explain);
		explain.setText(Html.fromHtml(getResources().getString(
				R.string.outcash_reminde)));
		TextView explain1 = (TextView) findViewById(R.id.textView_outcash_explain1);
		explain1.setText(Html.fromHtml(getResources().getString(
				R.string.outcash_reminde1)));
		explain1.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private CallBack mBack = new CallBack() {

		public void outCashSuccess(final GetCode code) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// 提现成功
					if (code.getResCode().equals("0")) {
						ToastUtil.shortToast(
								CenterOutCashActivity.this,
								getResources().getString(
										R.string.outcash_success));
					} else {
						// 提现失败
						ToastUtil.shortToast(
								CenterOutCashActivity.this,
								getResources().getString(
										R.string.outcash_failure));
					}
					// 通知 唤醒提现按钮可用
					mHandler.sendEmptyMessage(0);

				}
			});
		};

		public void outCashFailure(String error) {
			mHandler.sendEmptyMessage(0);

		};
	};
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			textView_outcash_submit.setEnabled(true);
			hideLoadingDialog();
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			GlobalTools.hideSoftInput(CenterOutCashActivity.this);
			finish();
			break;
		case R.id.id_update_card:
			if (App.userInfo != null && App.userInfo.getIsBindedBank() != null
					&& Integer.parseInt(App.userInfo.getIsBindedBank()) == 1) {
				Intent intent = new Intent(CenterOutCashActivity.this,
						CardAddCardActivity.class);
				String type = "2";
				intent.putExtra("type", type);
				startActivity(intent);
				finish();
			}
			break;
		case R.id.textView_outcash_submit:
			String moneyText = money.getText().toString();
			if (!TextUtils.isEmpty(moneyText)) {
				double extractMoney = Double.parseDouble(moneyText);
				if (extractMoney >= Double.parseDouble(App.userInfo
						.getPrizeAcnt())) {
					ToastUtil.shortToast(this, "提现金额不能大于可提现金额");
				} else if (extractMoney <= 10) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.outcash_error));
				} else {
					textView_outcash_submit.setEnabled(false);
					showLoadingDialog();
					Controller.getInstance()
							.outCash(GlobalConstants.NUM_OUTCASH,
									App.userInfo.getUserId(), moneyText,
									isQuick, mBack);

				}
			} else {
				ToastUtil.shortToast(CenterOutCashActivity.this, "请输入提现金额");
			}

			break;

		default:
			break;
		}
	}

}
