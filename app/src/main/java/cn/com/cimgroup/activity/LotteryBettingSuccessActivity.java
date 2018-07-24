package cn.com.cimgroup.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.DLTConfiguration;
import cn.com.cimgroup.frament.GridFragment;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 投注成功页面
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月21日
 */
public class LotteryBettingSuccessActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 购买红包成功跳转标识
	 */
	public static final String PAY_SUCESS_RED_BAG = "paySucess";

	/**
	 * 购买乐米成功跳转标识
	 */
	public static final String PAY_SUCESS_LEMI = "paySucess_lemi";

	/**
	 * 回退
	 **/
	private TextView mBackView;
	/**
	 * 继续投注
	 **/
	private TextView mOtherBettingView;
	/**
	 * 查看投注详情
	 **/
	private TextView mLookBettingInfo;

	private TextView mLotoType;

	private TextView mIssue;
	/** 购买类型标识 */
	private String lototype;
	
	private int lotoId;

	private TextView sucessTitle;
	// 投注成功对应的投注期次
	private String issue;

	private UserInfo userInfo;
	private LinearLayout linearLayout;
	/** 查看记录标题 */
	private String mLookTitle = "查看投注记录";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_lottery_betting_success);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		issue = intent.getStringExtra("issue");
		lototype = intent.getStringExtra("lototype");
		lotoId = intent.getIntExtra("lotoId", 0);
		userInfo = UserLogic.getInstance().getDefaultUserInfo();
		switch (lototype) {
		case GlobalConstants.TC_DLT:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "dlt_save";
			break;
		case GlobalConstants.TC_PL3:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p3_save";
			break;
		case GlobalConstants.TC_PL5:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p5_save";
			break;
		case GlobalConstants.TC_QXC:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "qxc_save";
			break;
		default:
			break;
		}
		if (!intent.getBooleanExtra("isQuick", false)) {
			DLTConfiguration.getConfiguration().clear();
		}
		
		initUI();
		initListener();
	}

	private void initUI() {
		mBackView = (TextView) findViewById(R.id.textView_title_back);
		((TextView) findViewById(R.id.textView_title_word)).setText("支付状态");
		mOtherBettingView = (TextView) findViewById(R.id.txtView_other_betting);
		mLotoType = (TextView) findViewById(R.id.textView_betting_success_lototype);
		mIssue = (TextView) findViewById(R.id.textView_betting_success_issue);
		linearLayout = (LinearLayout) findViewById(R.id.layout_betting_success_explan);
		sucessTitle = (TextView) findViewById(R.id.tv_sucess_title);
		mLookBettingInfo = (TextView) findViewById(R.id.txtView_look_betting_info);

		TextView money = (TextView) findViewById(R.id.textView_betting_money);
		TextView lemi = (TextView) findViewById(R.id.textView_betting_lemi);
		TextView redPacket = (TextView) findViewById(R.id.textView_betting_red_packet);
		TextView redNum = (TextView) findViewById(R.id.textView_betting_red_num);
		
		((TextView) findViewById(R.id.txtView_other_tomain)).setOnClickListener(this);


		//余额显示数量
		String moneyShowText="";
		
		String actStr=TextUtils.isEmpty(userInfo.getBetAcnt())?"0":userInfo.getBetAcnt();
		BigDecimal moneyAct=new BigDecimal(actStr);
		
		String prizeStr=TextUtils.isEmpty(userInfo.getPrizeAcnt())?"0":userInfo.getPrizeAcnt();
		BigDecimal prizeAct=new BigDecimal(prizeStr);
		BigDecimal moneyBD=moneyAct.add(prizeAct);
		if (moneyBD.doubleValue() > 100000d) {
			moneyShowText = moneyBD.divide(new BigDecimal("10000"), 2,
					RoundingMode.DOWN).toPlainString()
					+ "万";
		}else {
			moneyShowText=moneyBD.toPlainString();
		}
		money.setText(moneyShowText
				+ getResources().getString(R.string.price_unit));
		
		
		//乐米显示数量
		String lemiStr = "0";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			// 获取开放游戏、获取签到状态
			BigDecimal bigMoney = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getIntegralAcnt()) ? "0" : App.userInfo.getIntegralAcnt());

			if (bigMoney.doubleValue() > 100000d) {
				lemiStr = bigMoney.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				lemiStr = bigMoney.toPlainString();
		}
		lemi.setText(lemiStr + "米");
		
		
	

		if (!StringUtil.isEmpty(userInfo.redPkgAccount)) {
			String redArr = userInfo.redPkgAccount.split("\\.")[0];
			if (redArr.length() > 5) {
				redPacket.setText(redArr.substring(0, redArr.length() - 4)
						+ getResources().getString(R.string.price_ten_thousand)
						+ getResources().getString(R.string.price_unit));
			} else {
				redPacket.setText(userInfo.redPkgAccount
						+ getResources().getString(R.string.price_unit));
			}
		}

		redNum.setText(userInfo.redPkgNum
				+ getResources().getString(R.string.red_ge));

		if (StringUtil.isEmpty(issue)) {
			mIssue.setVisibility(View.GONE);
		} else {
			mIssue.setVisibility(View.VISIBLE);
			mIssue.setText(getResources().getString(
					R.string.record_tz_detail_qi, issue));
		}
		switch (lototype) {
		// 竞彩足球
		case GlobalConstants.TC_JCZQ:
			mLotoType.setText(getResources().getString(
					R.string.tz_select_football));
			break;
		// 大乐透
		case GlobalConstants.TC_DLT:
			mLotoType.setText(getResources().getString(R.string.tz_select_dlt));
			break;
		case GlobalConstants.TC_PL3:
			mLotoType.setText(getResources().getString(R.string.tz_select_p3));
			break;
		case GlobalConstants.TC_PL5:
			mLotoType.setText(getResources().getString(R.string.tz_select_p5));
			break;
		case GlobalConstants.TC_QXC:
			mLotoType.setText(getResources().getString(R.string.tz_select_qxc));
			break;
		case GlobalConstants.TC_SF14:
			mLotoType.setText(getResources().getString(R.string.tz_select_sfc));
			break;
		case GlobalConstants.TC_SF9:
			mLotoType.setText(getResources().getString(R.string.tz_select_r9));
			break;
		case GlobalConstants.TC_JQ4:
			mLotoType.setText(getResources().getString(R.string.tz_select_jqs));
			break;
		case GlobalConstants.TC_BQ6:
			mLotoType.setText(getResources().getString(R.string.tz_select_bqc));
			break;
		case PAY_SUCESS_RED_BAG:
			mLookTitle = "查看红包记录";
			linearLayout.setVisibility(View.GONE);
			sucessTitle.setText("恭喜您,购买成功");
			mOtherBettingView.setText("继续购买");
			break;
		case PAY_SUCESS_LEMI:
			mLookTitle = "查看乐米明细";
			linearLayout.setVisibility(View.GONE);
			sucessTitle.setText("恭喜您,购买成功");
			mOtherBettingView.setText("继续购买");
			break;
		default:
			break;
		}
		mLookBettingInfo.setText(mLookTitle);

	}

	private void initListener() {
		mBackView.setOnClickListener(this);
		mLookBettingInfo.setOnClickListener(this);
		mOtherBettingView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
//			if (!lototype.equals(PAY_SUCESS_RED_BAG)) {
//				Intent data = new Intent();
//				setResult(RESULT_OK, data);
//			}
			jump();
			finish();
			break;
		case R.id.txtView_other_tomain:
			MainActivity mainActivity = (MainActivity) ActivityManager
					.isExistsActivity(MainActivity.class);
			if (mainActivity != null) {
				// 如果存在MainActivity实例，那么展示LoboHallFrament页面
				ActivityManager.retain(MainActivity.class);
				mainActivity.showFrament(0);
			}
			break;
		// 继续投注
		case R.id.txtView_other_betting:
			jump();
			this.finish();
			break;
		case R.id.txtView_look_betting_info:
			if (lototype.equals(PAY_SUCESS_RED_BAG)) {
				// 查看红包记录;
				Intent intent = new Intent(this, MessageCenterActivity.class);
				intent.putExtra(MessageCenterActivity.TYPE,
						MessageCenterActivity.REDPACKET);
				startActivity(intent);
			} else if (lototype.equals(PAY_SUCESS_LEMI)) {
				// 查看乐米明细
				//跳转回到个人中心 并显示我的投注布局
				GlobalConstants.personTagIndex = 1;
				GlobalConstants.isRefreshFragment = true;
				GlobalConstants.isShowLeMiFragment = true;
				MainActivity main = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (main != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					ActivityManager.retain(MainActivity.class);
					main.showFrament(3);
				}
				
//				Intent lemiIntent = new Intent(
//						LotteryBettingSuccessActivity.this,
//						MyLeMiActivity.class);
//				lemiIntent.putExtra(MyLeMiFragment.TYPE, MyLeMiFragment.LEMI);
//				startActivity(lemiIntent);
			} else {
				// 查看投注记录
				GlobalConstants.personTagIndex = 0;
				GlobalConstants.personEndIndex =0;
				GlobalConstants.isRefreshFragment = true;
				GlobalConstants.isShowLeMiFragment = true;
				GlobalConstants.personGameNo = "ALL";
				MainActivity main = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (main != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					ActivityManager.retain(MainActivity.class);
					main.showFrament(3);
				}
//				startActivity(new Intent(this, TzListActivity.class));
			}

			break;
		default:
			break;
		}

	}
	
	
	private void jump() {
		int oldFootballType = 0;
		switch (lototype) {
		// 竞彩足球
		case GlobalConstants.TC_JCZQ:
			// startActivity(LotteryFootballActivity.class);
//			Intent intent1 = new Intent(mActivity,
//					LotteryFootballActivity.class);
//			intent1.putExtra("isClear", true);
//			int selectIndex = 4;
//			switch (lotoId) {
//			// 比分
//			case FootballLotteryConstants.L302:
//				selectIndex = 1;
//				break;
//			// 总进球数
//			case FootballLotteryConstants.L303:
//				selectIndex = 2;
//				break;
//			// 半全场
//			case FootballLotteryConstants.L304:
//				selectIndex = 3;
//				break;
//			case FootballLotteryConstants.L305:
//				selectIndex = 4;
//				break;
//			case FootballLotteryConstants.L320:
//				selectIndex = 0;
//				break;
//			case FootballLotteryConstants.L502:
//				selectIndex = 5;
//				break;
//			case FootballLotteryConstants.L501:
//				selectIndex = 6;
//				break;
//			default:
//				break;
//			}
//			intent1.putExtra("selectIndex", selectIndex);
//			startActivity(intent1);
			ActivityManager.popTopOthers(LotteryFootballActivity.class);
			break;
		case GlobalConstants.TC_JCLQ:
//			Intent intent8 = new Intent(mActivity, LotteryBasketballActivity.class);
//			intent8.putExtra("isClear", true);
//			int lqSelectIndex = 3;
//			switch (lotoId) {
//				case FootballLotteryConstants.L306:
//					lqSelectIndex = 0;
//					break;
//				case FootballLotteryConstants.L307:
//					lqSelectIndex = 0;
//					break;
//				case FootballLotteryConstants.L308:
//					lqSelectIndex = 2;
//					break;
//				case FootballLotteryConstants.L309:
//					lqSelectIndex = 1;
//					break;
//				case FootballLotteryConstants.L310:
//					lqSelectIndex = 3;
//					break;
//				default:
//					break;
//			}
//			intent8.putExtra("selectIndex", lqSelectIndex);
//			startActivity(intent8);
			ActivityManager.popTopOthers(LotteryBasketballActivity.class);
			break;
		// 大乐透
		case GlobalConstants.TC_DLT:
//			Intent intent = new Intent(mActivity, LotteryDLTActivity.class);
//			intent.putExtra("isClear", true);
//			startActivity(intent);
			ActivityManager.popTopOthers(LotteryDLTActivity.class);
			break;
		case GlobalConstants.TC_PL3:
//			Intent intent2 = new Intent(mActivity, LotteryPL3Activity.class);
//			intent2.putExtra("isClear", true);
//			startActivity(intent2);
			ActivityManager.popTopOthers(LotteryPL3Activity.class);
			break;
		case GlobalConstants.TC_PL5:
//			Intent intent3 = new Intent(mActivity, LotteryPL5Activity.class);
//			intent3.putExtra("isClear", true);
//			startActivity(intent3);
			ActivityManager.popTopOthers(LotteryPL5Activity.class);
			break;
		case GlobalConstants.TC_QXC:
//			Intent intent4 = new Intent(mActivity, LotteryQxcActivity.class);
//			intent4.putExtra("isClear", true);
//			startActivity(intent4);
			ActivityManager.popTopOthers(LotteryQxcActivity.class);
			break;
		case GlobalConstants.TC_SF14:
			oldFootballType = 0;
		case GlobalConstants.TC_SF9:
//			oldFootballType = 0;
//			Intent intent5 = new Intent(mActivity,
//					LotteryOldFootballActivity.class);
//			intent5.putExtra("isClear", true);
//			intent5.putExtra("lotoId", oldFootballType);
//			startActivity(intent5);
//			 ActivityManager.popTopOthers(LotteryOldFootballActivity.class);
//			break;
		case GlobalConstants.TC_JQ4:
//			oldFootballType = 1;
//			Intent intent6 = new Intent(mActivity,
//					LotteryOldFootballActivity.class);
//			intent6.putExtra("isClear", true);
//			intent6.putExtra("lotoId", oldFootballType);
//			startActivity(intent6);
//			 ActivityManager.popTopOthers(LotteryOldFootballActivity.class);
//			break;
		case GlobalConstants.TC_BQ6:
//			oldFootballType = 2;
//			Intent intent7 = new Intent(mActivity,
//					LotteryOldFootballActivity.class);
//			intent7.putExtra("isClear", true);
//			intent7.putExtra("lotoId", oldFootballType);
//			startActivity(intent7);
			ActivityManager.popTopOthers(LotteryOldFootballActivity.class);
			break;
		case PAY_SUCESS_LEMI:
//			Intent buyLeMi = new Intent(LotteryBettingSuccessActivity.this,
//					BuyRedPacketActivity.class);
//			buyLeMi.putExtra(GridFragment.TYPE, GridFragment.BUYLEMI);
//			startActivity(buyLeMi);
			ActivityManager.popTopOthers(BuyRedPacketActivity.class);
			break;
		default:

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// switch (lototype) {
			// // 竞彩足球
			// case GlobalConstants.TC_JCZQ:
			// startActivity(LotteryFootballActivity.class);
			// break;
			// // 大乐透
			// case GlobalConstants.TC_DLT:
			// startActivity(LotteryDLTActivity.class);
			// break;
			// case GlobalConstants.TC_PL3:
			// startActivity(LotteryPL3Activity.class);
			// break;
			// case GlobalConstants.TC_PL5:
			// startActivity(LotteryPL5Activity.class);
			// break;
			// case GlobalConstants.TC_QXC:
			// startActivity(LotteryQxcActivity.class);
			// break;
			// default:
			// break;
			// }
//			if (!lototype.equals(PAY_SUCESS_RED_BAG)) {
//				Intent data = new Intent();
//				setResult(RESULT_OK, data);
//			}
			jump();
		}
		return super.onKeyDown(keyCode, event);
	}

}
