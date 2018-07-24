package cn.com.cimgroup.activity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.TcjczqObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 支付成功的跳转页面
 * 
 * @author 秋风
 * 
 */
public class PaySuccessActivity extends BaseActivity implements OnClickListener {
	/** 返回 */
	private RelativeLayout id_back;

	/** 支付状态 */
	private TextView id_pay_state;

	/** 支付详情 */
	private TextView id_pay_details;

	/** 账户余额 */
	private TextView id_num_money;
	/** 红包标题 */
	private TextView id_title_red_package;

	/** 账户红包总面值 */
	private TextView id_num_red_package;
	/** 账户乐米 */
	@SuppressWarnings("unused")
	private TextView id_num_lemi;

	/** 查看记录 */
	private TextView id_jump_recode;
	/** 继续购买彩票/红包 */
	private TextView id_jump_continue;

	/** 乐米信息显示布局 */
	private LinearLayout id_lemi_layout;
	/** 用户账户余额 */
	private BigDecimal mUserMoney;
	/** 投注期号 */
	private String issue;
	/** 投注彩种类型 */
	private String lototype;

	public static final String PAY_SUCESS = "paySucess";

	private TextView mLotoType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		initView();
		initDatas();
		initEvent();
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_back = (RelativeLayout) findViewById(R.id.id_back);
		id_pay_state = (TextView) findViewById(R.id.id_pay_state);
		id_pay_details = (TextView) findViewById(R.id.id_pay_details);
		// id_num_money = (TextView) findViewById(R.id.id_num_money);
		// id_title_red_package = (TextView)
		// findViewById(R.id.id_title_red_package);
		// id_num_red_package = (TextView)
		// findViewById(R.id.id_num_red_package);
		// id_lemi_layout = (LinearLayout) findViewById(R.id.id_lemi_layout);
		// id_num_lemi = (TextView) findViewById(R.id.id_num_lemi);
		id_jump_recode = (TextView) findViewById(R.id.id_jump_recode);
		id_jump_continue = (TextView) findViewById(R.id.id_jump_continue);

		// id_lemi_layout.setVisibility(View.GONE);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		Intent intent = getIntent();
		issue = intent.getStringExtra("issue");
		lototype = intent.getStringExtra("lototype");
		// id_num_lemi.setText(App.userInfo.getIntegralAcnt() + "米");
		initPageInfo();
		// initMoneyInfo();
		// initRedPackageInfo();
		// id_title_red_package.setText("红包(" + mRedpackageNum + ")");
		// id_num_red_package.setText(Html.fromHtml("<font color='#f66248'>"
		// + mRedpackageFace + "</font> 元"));
	}

	/**
	 * 初始化页面数据
	 */
	private void initPageInfo() {
		if (StringUtil.isEmpty(issue)) {
			id_pay_details.setVisibility(View.GONE);
		} else {
			id_pay_details.setVisibility(View.VISIBLE);
			id_pay_details.setText(getResources().getString(
					R.string.record_tz_detail_qi, issue));
			String text = id_pay_details.getText().toString().trim();
			switch (lototype) {
			// 竞彩足球
			case GlobalConstants.TC_JCZQ:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_football)
						+ text + "   请随时关注方案状态");
				break;
			// 大乐透
			case GlobalConstants.TC_DLT:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_dlt)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_PL3:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_p3)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_PL5:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_p5)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_QXC:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_qxc)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_SF14:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_sfc)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_SF9:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_r9)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_JQ4:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_jqs)
						+ text + "   请随时关注方案状态");
				break;
			case GlobalConstants.TC_BQ6:
				id_pay_details.setText(getResources().getString(
						R.string.tz_select_bqc)
						+ text + "   请随时关注方案状态");
				break;
			case PAY_SUCESS:
				id_pay_state.setText("恭喜您,购买成功");
				id_jump_recode.setText("查看记录");
				id_jump_continue.setText("继续购买");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 获取用户余额信息
	 */
	private void initMoneyInfo() {
		double moneys = TextUtils.isEmpty(App.userInfo.getBetAcnt()) ? 0
				: Double.parseDouble(App.userInfo.getBetAcnt());
		double prize = TextUtils.isEmpty(App.userInfo.getPrizeAcnt()) ? 0
				: Double.parseDouble(App.userInfo.getPrizeAcnt());
		mUserMoney = BigDecimal.valueOf(moneys + prize);
		id_num_money.setText(Html.fromHtml("<font color='#f66248'>"
				+ mUserMoney + "</font> 元"));
	}

	/** 红包数量 */
	private int mRedpackageNum = 0;
	/** 红包面值 */
	private BigDecimal mRedpackageFace;

	/**
	 * 计算redPackage信息，获取个数跟总面值
	 */
	private void initRedPackageInfo() {
		if (App.userInfo != null) {
			if (!TextUtils.isEmpty(App.userInfo.redPkgAccount)) {
				mRedpackageNum = 1;
				mRedpackageFace = BigDecimal.valueOf(Double
						.parseDouble(App.userInfo.redPkgAccount));
			}
			if (App.userInfo.convertRedList != null
					&& App.userInfo.convertRedList.size() != 0) {
				getConvertRedPackageInfo(App.userInfo.convertRedList);
			}
			if ((App.userInfo.giveRedList != null && App.userInfo.giveRedList
					.size() != 0)) {
				getGiveRedPackageInfo(App.userInfo.giveRedList);
			}
		}
	}

	/**
	 * 获取兑换红包个数以及面值
	 * 
	 * @param map
	 */
	private void getConvertRedPackageInfo(HashMap<String, List<TcjczqObj>> map) {
		if (map != null) {
			Iterator<Map.Entry<String, List<TcjczqObj>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<TcjczqObj>> entry = it.next();
				int convertNum = 0;
				List<TcjczqObj> conList = entry.getValue();
				convertNum = conList.size();
				mRedpackageNum += convertNum;
				if (convertNum > 0) {
					for (int i = 0; i < convertNum; i++) {
						mRedpackageFace.add(BigDecimal.valueOf(Double
								.parseDouble(conList.get(i).converRedMoney)));
					}

				}
			}
		}
	}

	/**
	 * 获取兑换红包信息
	 * 
	 * @param map
	 */
	private void getGiveRedPackageInfo(HashMap<String, List<TcjczqObj>> map) {
		if (map != null) {
			Iterator<Map.Entry<String, List<TcjczqObj>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<TcjczqObj>> entry = it.next();
				int convertNum = 0;
				List<TcjczqObj> conList = entry.getValue();
				convertNum = conList.size();
				if (convertNum > 0) {
					for (int i = 0; i < convertNum; i++) {
						mRedpackageFace.add(BigDecimal.valueOf(Double
								.parseDouble(conList.get(i).giveRedMoney)));
					}
				}
			}
		}
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		id_back.setOnClickListener(this);
		id_jump_continue.setOnClickListener(this);
		id_jump_recode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_jump_recode:
			if (!lototype.equals(PAY_SUCESS)) {
				//跳转回到个人中心 并显示我的投注布局
				GlobalConstants.isRefreshFragment = true;
				GlobalConstants.personTagIndex = 0;
				GlobalConstants.personEndIndex = 0;
				GlobalConstants.isShowLeMiFragment = true;
				MainActivity main = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (main != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					ActivityManager.retain(MainActivity.class);
					main.showFrament(3);
				}
			} else {
				// 查看红包记录;
				Intent intent = new Intent(this, MessageCenterActivity.class);
				intent.putExtra(MessageCenterActivity.TYPE,
						MessageCenterActivity.REDPACKET);
				startActivity(intent);
			}
			break;
		case R.id.id_back:
		case R.id.id_jump_continue:
			int oldFootballType = 0;
			Intent intent = new Intent();
			/** 跳转到响应的购彩页面继续购彩或者购买红包 */
			switch (lototype) {
			case GlobalConstants.TC_JCZQ:
				intent.setClass(mActivity, LotteryFootballActivity.class);
				intent.putExtra("isClear", true);
				break;
			case GlobalConstants.TC_DLT:
				intent.setClass(mActivity, LotteryDLTActivity.class);
				intent.putExtra("isClear", true);
				break;
			case GlobalConstants.TC_PL3:
				intent.setClass(mActivity, LotteryPL3Activity.class);
				intent.putExtra("isClear", true);
				break;
			case GlobalConstants.TC_PL5:
				intent.setClass(mActivity, LotteryPL5Activity.class);
				intent.putExtra("isClear", true);
				break;
			case GlobalConstants.TC_QXC:
				intent.setClass(mActivity, LotteryQxcActivity.class);
				intent.putExtra("isClear", true);
				break;
			case GlobalConstants.TC_SF14:
			case GlobalConstants.TC_SF9:
				oldFootballType = 0;
				intent.setClass(mActivity, LotteryOldFootballActivity.class);
				intent.putExtra("isClear", true);
				intent.putExtra("lotoId", oldFootballType);
				break;
			case GlobalConstants.TC_JQ4:
				oldFootballType = 1;
				intent.setClass(mActivity, LotteryOldFootballActivity.class);
				intent.putExtra("isClear", true);
				intent.putExtra("lotoId", oldFootballType);
				break;
			case GlobalConstants.TC_BQ6:
				oldFootballType = 2;
				intent.setClass(mActivity, LotteryOldFootballActivity.class);
				intent.putExtra("isClear", true);
				intent.putExtra("lotoId", oldFootballType);
				break;
			case PAY_SUCESS:
				intent.setClass(mActivity, BuyRedPacketActivity.class);
				break;
			case GlobalConstants.TC_JCLQ:
				intent.setClass(mActivity, LotteryBasketballActivity.class);
				break;
			default:

				break;
			}
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (App.CALLBACK_ACTIVITY == "WECHAT_RECHARAGE") {
			// 如果用户是通过微信的充值渠道到达该页面则查询用户的账户信息，获取账户余额
			getUserInfo();
		}

	}

	/**
	 * 获取信息
	 */
	private void getUserInfo() {
		if (App.userInfo != null && App.userInfo.getUserId() != null) {
			showLoadingDialog();
			Controller.getInstance().getUserInfo(
					GlobalConstants.NUM_GETUSERINFO, App.userInfo.getUserId(),
					mBack);
		}
	}

	/**
	 * 网络请求回调
	 */
	private CallBack mBack = new CallBack() {
		public void getUserInfoSuccess(final UserInfo info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (info != null && info.getResCode() != null
							&& info.getResCode().equals("0")) {
						info.setUserId(App.userInfo.getUserId());
						info.setPassword(App.userInfo.getPassword());
						App.userInfo = info;
						// 更新本地文件
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						initMoneyInfo();
						initRedPackageInfo();
					}
				}
			});
		};

		public void getUserInfoFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(mActivity,
							"网络获取数据异常，您的资金明细显示可能异常，请到资金明细查询");
				}
			});
		};
	};
}
