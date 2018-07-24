package cn.com.cimgroup.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseLotteryCartActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Betting;
import cn.com.cimgroup.bean.IssuePreList;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotterySaveObj;
import cn.com.cimgroup.bean.TzObj;
import cn.com.cimgroup.config.DLTConfiguration;
import cn.com.cimgroup.dailog.DialogProgress;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.GetPlayCodeUtil;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 数字彩购物车
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月21日
 */
public abstract class LotteryCartActivity extends BaseLotteryCartActivity
		implements OnClickListener {

	protected TextView mTitleTextView;
	protected LinearLayout mCartLayout;// 购物车投注详情所在布局
	protected int redColor;
	protected int buleColor;
	private EditText mTbEditText;// 投多少倍
	private EditText mZqEditText;// 追多少期
	private int mMultiple = 999;// 倍数
	private int mPeriod = 50;// 倍数
	private TextView mTVCartNumMult;// 投注注数跟倍数
	private TextView mTVCartTermMoney;// 投注期数跟钱数
	private String totalMoney;
	private int multiple;
	private DialogProgress dialogProgress;
	private LinearLayout mCartTzhiCheckBox;// 中奖后停止
	protected LinearLayout mCartZjiaCheckBox;// 追加
	private ImageView mCartTzhi;// 中奖后停止
	protected ImageView mCartZjia;// 追加
	private TextView mCartNullHint;// 购物车空提醒选购
	protected TextView mIssueTextView;
	protected TextView mIssueTimeTextView;
	private ImageView mShakeView;
	protected Intent intent;
	private String issues;
	private ImageView mClearTextView;
	// 彩票距离结束的时间差
	private long differTime;
	
	private String currIssue;

	@SuppressLint("HandlerLeak")
	public final Handler mHandlerEx = new Handler() {
	};
	
	protected LotterySaveObj obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_cart);
		initView();
		GlobalTools.hideSoftInput(LotteryCartActivity.this);
		redColor = getResources().getColor(R.color.color_red);
		buleColor = getResources().getColor(R.color.hall_blue);
		intent = getIntent();
		issues = intent.getStringExtra(GlobalConstants.LOTTERY_ISSUES);
		currIssue = intent.getStringExtra(GlobalConstants.LOTTERY_ISSUE);
		if (!StringUtil.isEmpty(currIssue)) {
			if (currIssue.indexOf("期") > 1) {
				currIssue = currIssue.substring(1, currIssue.indexOf("期"));
			}
		}
		switch (getGameType()) {
		case GlobalConstants.TC_DLT:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "dlt_save";
			break;
		case GlobalConstants.TC_QXC:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "qxc_save";
			break;
		case GlobalConstants.TC_PL3:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p3_save";
			break;
		case GlobalConstants.TC_PL5:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p5_save";
			break;
		default:
			break;
		}
		obj = DLTConfiguration.getConfiguration().getLocalUserInfo();
	}

	private void initView() {
		mTitleTextView = (TextView) findViewById(R.id.textView_title_word);

		mClearTextView = (ImageView) findViewById(R.id.lottery_cart_del);

		((TextView) findViewById(R.id.textView_tz_shuoming))
				.setOnClickListener(this);

		mCartLayout = (LinearLayout) findViewById(R.id.lottery_cart_ll);
		mCartNullHint = (TextView) findViewById(R.id.lottery_crat_null_hint);
		mCartNullHint.setText(LotteryShowUtil.setCartNullBuleText(mActivity,
				getResources().getString(R.string.cart_null_hint)));

		mIssueTextView = (TextView) findViewById(R.id.issue_tv);
		mIssueTimeTextView = (TextView) findViewById(R.id.issue_time_tv);
		mShakeView = (ImageView) findViewById(R.id.imageView_lottery_shake);
		mShakeView.setVisibility(View.GONE);
		((ImageView) findViewById(R.id.imageView_lottery_pull))
				.setVisibility(View.GONE);

		mTbEditText = (EditText) findViewById(R.id.editText_tb);
		mZqEditText = (EditText) findViewById(R.id.editText_zq);

		mCartTzhiCheckBox = (LinearLayout) findViewById(R.id.lottery_cart_tingzhi);
		mCartZjiaCheckBox = (LinearLayout) findViewById(R.id.lottery_cart_zhuijia);
		mCartTzhiCheckBox.setOnClickListener(this);
		mCartZjiaCheckBox.setOnClickListener(this);
		mCartTzhi = (ImageView) findViewById(R.id.imageView_cart_tingzhi);
		mCartTzhi.setEnabled(false);
		mCartZjia = (ImageView) findViewById(R.id.imageView_cart_zhuijia);

		mTVCartNumMult = (TextView) findViewById(R.id.textView_cart_num_mult);
		mTVCartTermMoney = (TextView) findViewById(R.id.textView_cart_term_money);

		mTbEditText.addTextChangedListener(new TbTextWatcher());
		mZqEditText.addTextChangedListener(new ZqTextWatcher());

		LotteryShowUtil.setSelection(mTbEditText);
		LotteryShowUtil.setSelection(mZqEditText);
		if (getLotteryName() == GlobalConstants.LOTTERY_DLT) {
			mCartZjiaCheckBox.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mHandlerEx.removeCallbacks(mDltRunnable);
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandlerEx.removeCallbacks(mDltRunnable);
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
//			goBack();
			showExitDialog();
			break;
		case R.id.textView_tz_shuoming:
			// 跳转法律说明页面
			Intent intent = new Intent(this, HtmlCommonActivity.class);
			intent.putExtra("isWeb", false);
			intent.putExtra("lotteryName", 0);
			startActivity(intent);
			break;
		case R.id.lottery_crat_null_hint:// 购物车为空，去选购
			// zxIntoCart();
			finish();
			break;
		case R.id.lottery_cart_del:// 购物车清空
			showDelCartDialog();
			break;
		case R.id.lottery_cart_add_zx:// 自选号码
			zxIntoCart();
			break;
		case R.id.lottery_cart_add_jx:// 机选号码
			mCartNullHint.setVisibility(View.GONE);
			jxIntoCart();
			getTotalNumMoney();
			notifyCart();
			break;
		case R.id.textView_tb_sub:// 减倍数
			String string = mTbEditText.getText().toString();
			int etTbSubInt = 0;
			if (!TextUtils.isEmpty(string)) {
				etTbSubInt = Integer.parseInt(string);
				if (etTbSubInt > 1) {
					mTbEditText.setText((--etTbSubInt) + "");
					LotteryShowUtil.setSelection(mTbEditText);
					getTotalNumMoney();
				}
			}

			break;
		case R.id.textView_tb_add:// 加倍数
			String mTAdd = mTbEditText.getText().toString();
			int etTbAddInt = 0;
			if (!TextUtils.isEmpty(mTAdd)) {
				etTbAddInt = Integer.parseInt(mTAdd);
				if (etTbAddInt >= mMultiple) {
					etTbAddInt--;
					// showToast(getResources().getString(R.string.cart_max_mult,
					// mMultiple));
				}
			} 
			mTbEditText.setText((++etTbAddInt) + "");
			LotteryShowUtil.setSelection(mTbEditText);
			getTotalNumMoney();
			break;
		case R.id.textView_zq_sub:// 减期数
			String zqStr = mZqEditText.getText().toString();
			int etZq = 0;
			if (!TextUtils.isEmpty(zqStr)) {
				etZq = Integer.parseInt(zqStr);
				if (etZq > 1) {
					mZqEditText.setText((--etZq) + "");
					LotteryShowUtil.setSelection(mZqEditText);
					getTotalNumMoney();
				}

			}
			break;
		case R.id.textView_zq_add:// 加期数
			String zqStrAdd = mZqEditText.getText().toString().trim();
			int etZqAddInt = 0;
			if (!TextUtils.isEmpty(zqStrAdd)) {
				etZqAddInt = Integer.parseInt(zqStrAdd);
				if (etZqAddInt < mPeriod) {
					etZqAddInt++;
				}
			} else {
				etZqAddInt = 1;
			}
			mZqEditText.setText(etZqAddInt + "");
			LotteryShowUtil.setSelection(mZqEditText);
			getTotalNumMoney();
			break;
		case R.id.lottery_cart_buy_tv:
			// 购买
			if (mCartLayout.getChildCount() > 0) {
				// dialogProgress =
				// DialogProgress.newIntance(getResources().getString(R.string.lottery_tz_loading),
				// true);
				// dialogProgress.show(getSupportFragmentManager(),
				// "dialogProgress");
				boolean expire = LotteryBettingUtil.isExpire(getLotteryName());
				if (expire) {
					// mTbEditText
					// mZqEditText
					String tb = mTbEditText.getText().toString().trim();
					if (TextUtils.isEmpty(tb) || Integer.parseInt(tb) < 1) {
						mTbEditText.setText("1");
					}
					String zq = mZqEditText.getText().toString().trim();
					if (TextUtils.isEmpty(zq) || Integer.parseInt(zq) < 1) {
						mZqEditText.setText("1");
					}
					getTotalNumMoney();
					postData();
				} else {
					// 获取期号
					showLoadingDialog();
					Controller.getInstance().getLoBoPeriod(
							GlobalConstants.NUM_LOTTERY_PRE,
							LotteryBettingUtil.getLottery(getLotteryName()),
							mCallBack);
				}
			} else {
				showToast(getResources().getString(R.string.cart_null_buy_hint));
			}
			break;
		case R.id.lottery_cart_zhuijia:
			if (mCartZjia.isSelected()) {
				mCartZjia.setSelected(false);
			} else {
				mCartZjia.setSelected(true);
			}
			for (CartLayout cartLayout : itemDataList) {
				cartLayout.isZhuiAdd = mCartZjia.isSelected();
				cartLayout.updataLayout();
			}
			getTotalNumMoney();
			break;
		case R.id.lottery_cart_tingzhi:
			if (mCartTzhi.isEnabled()) {
				if (mCartTzhi.isSelected()) {
					mCartTzhi.setSelected(false);
				} else {
					mCartTzhi.setSelected(true);
				}
			}
			break;
		default:
			break;
		}
	}

	private void dismissProDialog() {
		if (dialogProgress != null)
			dialogProgress.dismiss();
		dialogProgress = null;
	}

	/**
	 * 进行投注
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	private void postData() {

		if (Long.parseLong(totalMoney) > GlobalConstants.LOTTERY_MAX_PRICE) {
			showToast(getResources().getString(R.string.lottery_money_than));
			return;
		}

		CartInfo cartInfo = getData();
		ArrayList<CodeInfo> itemList = cartInfo.itemList;
		int size=itemList.size();
		for (int i = 0; i < size; i++) {
			String code=itemList.get(i).mCode;
			itemList.get(i).mCode=code.replace(" ", ",");
		}
		String isChase = "0";
		String chase = "1";
		String bettingType = "1";
		String gameNo = getGameType();
		String issue = "0";
		String stopCondition = "0";
		// 总金额
		String totalSum = totalMoney;
		String choiceType = "1";
		Map<Integer, Map<String, String>> lists = new LinkedHashMap<Integer, Map<String, String>>();

		String planEndTime = "";

		if (Integer.parseInt(totalSum) > GlobalConstants.LOTTERY_MAX_PRICE) {
			showToast(getResources().getString(R.string.lottery_money_than));
			return;
		}
		String price=mTbEditText.getText().toString().trim();

		// 期数
		String zq = "1";
		for (int i = 0; i < itemList.size(); i++) {
			CodeInfo codeInfo = itemList.get(i);
			// for (CodeInfo codeInfo : itemList) {
			/**
			 * 大乐透： seleId 01单式，02复式，03胆拖 typeId 是否是追加，00不追加，01追加
			 */

			/**
			 * 排三 typeId 01直选，02直选和值，03排三组三，04组选和值，05排三组六 seleId 01单式，02复式
			 */

			/**
			 * 排五 typeId 00 seleId 01单式，02复式
			 */

			/**
			 * 七星彩 typeId 00 seleId 01单式，02复式
			 */

			String seleId = codeInfo.mSeleId;

			String typeId = codeInfo.mTypeId;

			String codePlay = GetPlayCodeUtil.getCodePlay(gameNo, seleId,
					typeId);

			Map<String, String> bettingReq = new LinkedHashMap<String, String>();

			bettingReq.put("codePlay", codePlay);
			// 选中的注码
			bettingReq.put("codeContent", codeInfo.mCode);
			// 单个倍数
			bettingReq.put("codeMultiple", "1");
			// 单个钱数
			bettingReq.put("codeMoney", codeInfo.mPrice + "");
			// 注数
			bettingReq.put("codeNumbers", codeInfo.mNum + "");

			lists.put(i, bettingReq);
		}
		issue = LotteryBettingUtil.getIssue(getLotteryName());// 期号
		zq = mZqEditText.getText().toString();
		if (!TextUtils.isEmpty(issues)) {
			int zqInt = Integer.parseInt(zq);
			if (zqInt > 1) {
				isChase = "1";
				chase = zq;
				StringBuilder sb = new StringBuilder();
				String[] list = issues.split("\\,");
				for (int j = 0; j < Integer.parseInt(chase); j++) {
					sb.append(list[j]).append("^");
				}
				sb.deleteCharAt(sb.length() - 1);
				issue = sb.toString();
			} else {
				isChase = "0";
			}
			if (mCartTzhi.isSelected()) {
				stopCondition = "1";
			}

			if (App.userInfo == null) {
				startActivity(LoginActivity.class);
			} else {
				// Controller.getInstance().syncLotteryBetting(GlobalConstants.NUM_BETTING,
				// App.userInfo.getUserId(), bettingType, gameNo, issue,
				// multiple,
				// totalSum, isChase, chase, isRedPackage, redPackageId,
				// redPackageMoney, choiceType, planEndTime, stopCondition,
				// list, mCallBack);
				TzObj obj = new TzObj();
				obj.bettingType = bettingType;
				obj.gameNo = gameNo;
				obj.issue = issue;
				obj.multiple = price;
				obj.totalSum = totalSum; // 订单金额
				obj.chase = chase;
				obj.isChase = isChase;
				obj.choiceType = choiceType;
				obj.planEndTime = planEndTime;
				obj.stopCondition = stopCondition;
				obj.gameType = getGameType();
				obj.lotteryName = getLotteryName();
				obj.bettingReq = lists;

				Intent intent = new Intent(LotteryCartActivity.this,
						CommitPayActivity.class);
				intent.putExtra(CommitPayActivity.DATA, obj);
				intent.putExtra("desc", mTVCartNumMult.getText());
				startActivity(intent);
				// TODO 投注
				// finish();
			}
		} else {
			// 获取期号
			Controller.getInstance().getLoBoZhuiPeriod(GlobalConstants.NUM_LOTTERY_ZHUI_LIST, LotteryBettingUtil.getLottery(getLotteryName()), currIssue, mCallBack);
		}
	}

	private CallBack mCallBack = new CallBack() {

		public void getLoBoPeriodSuccess(final List<LoBoPeriodInfo> infos) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (infos.size() > 0) {
						LoBoPeriodInfo periodInfo = infos.get(0);
						if (periodInfo != null) {
							LotteryBettingUtil.setIssueAndEndTime(getLotteryName(),
									periodInfo.getIssue(), periodInfo.getEndTime()
											+ "");
							currIssue = periodInfo.getIssue();
							postData();
						}
					}
					hideLoadingDialog();
					dismissProDialog();
				}
			});
		};

		public void getLoBoPeriodFailure(final String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(LotteryCartActivity.this, error);
				}
			});
		};
		
		public void getLoBoZhuiPeriodSuccess(final IssuePreList list) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (list.getResCode().equals("0")) {
						issues = list.getIssueArr();
						postData();
					}
				}
			});
		};
		
		public void getLoBoZhuiPeriodFailure(final String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(LotteryCartActivity.this, error);
				}
			});
		};

		/**
		 * 投注成功
		 */
		public void syncLotteryBettingSuccess(final Betting obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(LotteryCartActivity.this, "投注成功");
					App.userInfo.setIntegralAcnt(obj.getIntegralAcnt());
					App.userInfo.setPrizeAcnt(obj.getPrizeAcnt());
					App.userInfo.setBetAcnt(obj.getBetAcnt());
					UserLogic.getInstance().saveUserInfo(App.userInfo);

					Intent tIntent = new Intent();
					setResult(GlobalConstants.CLEARLATTERYSELECT, tIntent);
					// 投注成功，关闭本页面
					ActivityManager.pop(LotteryCartActivity.this);
					Intent intent = new Intent(LotteryCartActivity.this,
							LotteryBettingSuccessActivity.class);
					intent.putExtra("issue",
							LotteryBettingUtil.getIssue(getLotteryName()));
					intent.putExtra("lototype", getGameType());
					startActivity(intent);
				}
			});
		};
		
		public void syncLotteryBettingFailure(final String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(LotteryCartActivity.this, error);
				}
			});
		};

	};

	public class CartInfo {

		ArrayList<CodeInfo> itemList = new ArrayList<CodeInfo>();

		public void addCodeInfoToList(CodeInfo codeInfo) {
			itemList.add(codeInfo);
		}
	}

	public static class CodeInfo implements Parcelable, Serializable {

		String mCode;// 单个注码
		int mPrice;// 单个钱数
		int mMultipe;// 单个倍数
		String mTypeId;// 玩法编码
		String mSeleId;// 选号方式
		int mNum;// 注数
		int mBallType;// 彩种哪种玩法

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.mCode);
			dest.writeInt(this.mPrice);
			dest.writeInt(this.mMultipe);
			dest.writeString(this.mTypeId);
			dest.writeString(this.mSeleId);
			dest.writeInt(this.mNum);
			dest.writeInt(this.mBallType);
		}

		public CodeInfo() {
		}

		protected CodeInfo(Parcel in) {
			this.mCode = in.readString();
			this.mPrice = in.readInt();
			this.mMultipe = in.readInt();
			this.mTypeId = in.readString();
			this.mSeleId = in.readString();
			this.mNum = in.readInt();
			this.mBallType = in.readInt();
		}

		public static final Creator<CodeInfo> CREATOR = new Creator<CodeInfo>() {
			public CodeInfo createFromParcel(Parcel source) {
				return new CodeInfo(source);
			}

			public CodeInfo[] newArray(int size) {
				return new CodeInfo[size];
			}
		};
	}

	private CartInfo getData() {
		CartInfo cartInfo = new CartInfo();
		multiple = Integer.parseInt(mTbEditText.getText().toString());
		for (int i = 0; i < mCartLayout.getChildCount(); i++) {
			View itemLayout = mCartLayout.getChildAt(i);
			CartLayout cartLayout = (CartLayout) itemLayout.getTag();
			if (cartLayout != null) {
				LinearLayout itemtLayout = cartLayout.mRLCart;
				CodeInfo codeInfo = new CodeInfo();
				cartInfo.addCodeInfoToList(codeInfo);
				codeInfo.mMultipe = 1;
				goBuy(itemtLayout, codeInfo);
			}
		}
		return cartInfo;
	}

	/**
	 * 设置底部显示的*注*倍*期*元
	 */
	@Override
	protected void getTotalNumMoney() {
		super.getTotalNumMoney();
		String moneyString = getResources().getString(R.string.all);
		String num = String.valueOf(tzTotalNum);
		String numString = getResources().getString(R.string.betting1);
		String mult = mTbEditText.getText().toString();
		mult = TextUtils.isEmpty(mult) || Integer.parseInt(mult) < 1 ? "1"
				: mult;
		String multString = getResources().getString(R.string.cart_add_bei);
		String term = mZqEditText.getText().toString();
		term = TextUtils.isEmpty(term) || Integer.parseInt(term) < 1 ? "1"
				: term;
		String termString = getResources().getString(R.string.cart_add_qi);
		

		tzTotalMoney = 0;
		for (CartLayout cartLayout : itemDataList) {
			tzTotalMoney += cartLayout.getItemMoney();
		}
		
		int temp = 2;
		if (mCartZjia.isSelected()) {
			temp = 3;
		} else {
			temp = 2;
		}
		
		String totalNum = moneyString + tzTotalMoney/temp + numString + mult + multString
				+ term + termString;

		mTVCartNumMult.setText(totalNum);

		totalMoney = String.valueOf(tzTotalMoney * (Integer.parseInt(mult))
				* (Integer.parseInt(term)));
		String money = totalMoney
				+ getResources().getString(R.string.lemi_unit);
		mTVCartTermMoney.setText(money);
	}

	private class TbTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String string = s.toString();
			if (!TextUtils.isEmpty(string)) {
				int parseInt = Integer.parseInt(string);
				if (parseInt < 1) {
					// showToast(getResources().getString(
					// R.string.cart_least_mult, 1));
				}
				if (parseInt > mMultiple) {
					mTbEditText.setText(mMultiple + "");
					// showToast(getResources().getString(R.string.cart_max_mult,
					// mMultiple));
				}
				if (string.length() > 1 && string.substring(0, 1).equals("0")) {
					mTbEditText.setText(string.substring(1));
				}
			}
			LotteryShowUtil.setSelection(mTbEditText);
			getTotalNumMoney();
		}
	}

	private class ZqTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String string = s.toString();
			if (!TextUtils.isEmpty(string)) {
				int parseInt = Integer.parseInt(string);
				if (parseInt < 1) {
					mCartTzhi.setSelected(false);
					mCartTzhi.setEnabled(false);
					// showToast(getResources().getString(R.string.cart_least_qi,
					// 1));
				} else if (parseInt > 1) {
					mCartTzhi.setEnabled(true);
				} else {
					mCartTzhi.setSelected(false);
					mCartTzhi.setEnabled(false);

				}
				if (parseInt > mPeriod) {
					mZqEditText.setText(mPeriod + "");
					mCartTzhi.setEnabled(true);
					// showToast(getResources().getString(R.string.cart_max_qi,
					// mPeriod));
				}
				if (string.length() > 1 && string.substring(0, 1).equals("0")) {
					mZqEditText.setText(string.substring(1));
				}
			} else {
				mCartTzhi.setEnabled(false);
				mCartTzhi.setSelected(false);
			}
			LotteryShowUtil.setSelection(mZqEditText);
			getTotalNumMoney();
		}
	}

	private class CheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			for (CartLayout cartLayout : itemDataList) {
				cartLayout.updataLayout();
			}
			getTotalNumMoney();
		}
	}

	/**
	 * 购物车点击item，进入彩种选择页面，重新选取球后，点击完成的startActivityForResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GlobalConstants.CART_ITEM_RESULT:
				data.putExtra(GlobalConstants.CART_IS_ITEM_FORRESULT, true);
				initData(data);
				break;
			case GlobalConstants.CART_ZX_RESULT:
				mCartNullHint.setVisibility(View.GONE);
				initData(data);
				break;
			default:
				break;
			}
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // 返回时提醒
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// showExitDialog();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	/**
	 * 后退提示
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	private void showExitDialog() {
		if (mCartLayout.getChildCount() > 0) {
//			final PromptDialog0 promptDialog0 = new PromptDialog0(mActivity);
			final PromptDialog_Black_Fillet promptDialog0 = new PromptDialog_Black_Fillet(mActivity);
//			promptDialog0.setTitle(getResources().getString(R.string.prompt));
			promptDialog0.setMessage(getResources().getString(
					R.string.cart_out_tz));
			promptDialog0.setPositiveButton(
					getResources().getString(R.string.btn_no_save),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							promptDialog0.hideDialog();
							DLTConfiguration.getConfiguration().clear();
//							mActivity.finish();
							goBack();
						}
					});
			promptDialog0.setNegativeButton(
					getResources().getString(R.string.btn_save),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							promptDialog0.hideDialog();
							saveData();
//							mActivity.finish();
							goBack();
						}
					});
			promptDialog0.setCloseView(View.VISIBLE, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					promptDialog0.hideDialog();
				}
			});
			promptDialog0.showDialog();
		} else {
			mActivity.finish();
		}
	}

	/**
	 * 清空列表提示
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	private void showDelCartDialog() {
//		final PromptDialog0 promptDialog0 = new PromptDialog0(mActivity);
		final PromptDialog_Black_Fillet promptDialog0 = new PromptDialog_Black_Fillet(mActivity);
//		promptDialog0.setTitle(getResources()
//				.getString(R.string.cart_btn_clear));
		promptDialog0.setMessage(getResources().getString(
				R.string.cart_btn_sure_clear));
		promptDialog0.setPositiveButton(
				getResources().getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCartLayout.removeAllViews();
						itemDataList.clear();
						notifyCart();
						promptDialog0.hideDialog();
					}
				});
		promptDialog0.setNegativeButton(
				getResources().getString(R.string.btn_canl),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						promptDialog0.hideDialog();
					}
				});
		promptDialog0.setPositiveButtonShape("确定");
		promptDialog0.showDialog();
	}

	@Override
	protected void notifyCart() {
		super.notifyCart();
		if (!(mCartLayout.getChildCount() > 0)) {
			mCartNullHint.setVisibility(View.VISIBLE);
			mClearTextView.setVisibility(View.GONE);
			tzTotalNum = 0;
			tzTotalMoney = 0;
			getTotalNumMoney();
		} else {
			mClearTextView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 自选带参数开启Activity
	 * 
	 * @Description:
	 * @param activity
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	protected void zxIntent(Class<? extends Activity> activity) {
		Intent intent = new Intent(mActivity, activity);
		intent.putExtra(GlobalConstants.CART_IS_FORRESULT, true);
		startActivityForResult(intent, GlobalConstants.CART_ZX_RESULT);
	}

	private int getIntForEdit(EditText et) {
		String etString = et.getText().toString();
		return Integer.parseInt(etString);
	}

	/**
	 * 距离活动结束倒计时
	 * 
	 * @Description:
	 * @param issue
	 * @param startDateTime
	 * @param endDateTime
	 * @author:www.wenchuang.com
	 * @date:2015-2-5
	 */
	protected void getTermAndTime(String issue, String startDateTime,
			String endDateTime) {
		long startTime = Long.parseLong(startDateTime);
		long nowTime = System.currentTimeMillis();
		long endTime = Long.parseLong(endDateTime);
		if (nowTime > startTime) {
			differTime = endTime - nowTime;
		} else {
			differTime = endTime - startTime;
		}

		if (differTime > 1000) {
			mHandlerEx.postDelayed(mDltRunnable, 1000);
		} else {
			mIssueTextView.setText(getResources().getString(
					R.string.lottery_end_sell, issue));
		}
	}

	Runnable mDltRunnable = new Runnable() {

		@Override
		public void run() {
			differTime -= 1000;
			int red = getResources().getColor(R.color.color_red);
			String formatTime = "距截止剩："
					+ DateUtil.formatTime(differTime, false, red);
			mIssueTimeTextView.setText(Html.fromHtml(formatTime));
			if (differTime > 0) {
				mHandlerEx.postDelayed(this, 1000);
			}
		}
	};
	
	/**
	 * 返回购彩页面处理
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-3
	 */
	private void goBack() {
		switch (getGameType()) {
		case GlobalConstants.TC_DLT:
			LotteryDLTActivity dltActivity = (LotteryDLTActivity) ActivityManager.isExistsActivity(LotteryDLTActivity.class);
			if (dltActivity == null) {
				@SuppressWarnings("null")
				Intent intent = new Intent(LotteryCartActivity.this, LotteryDLTActivity.class);
				intent.putExtra("source", 1);
				startActivity(intent);
			} else {
				finish();
//				showExitDialog();
			}
			break;
		case GlobalConstants.TC_QXC:
			LotteryQxcActivity qxcActivity = (LotteryQxcActivity) ActivityManager.isExistsActivity(LotteryQxcActivity.class);
			if (qxcActivity == null) {
				@SuppressWarnings("null")
				Intent intent = new Intent(LotteryCartActivity.this, LotteryQxcActivity.class);
				intent.putExtra("source", 1);
				startActivity(intent);
			} else {
				finish();
//				showExitDialog();
			}
			break;
		case GlobalConstants.TC_PL3:
			LotteryPL3Activity PL3Activity = (LotteryPL3Activity) ActivityManager.isExistsActivity(LotteryPL3Activity.class);
			if (PL3Activity == null) {
				@SuppressWarnings("null")
				Intent intent = new Intent(LotteryCartActivity.this, LotteryPL3Activity.class);
				intent.putExtra("source", 1);
				startActivity(intent);
			} else {
				finish();
//				showExitDialog();
			}
			break;
		case GlobalConstants.TC_PL5:
			LotteryPL5Activity pl5Activity = (LotteryPL5Activity) ActivityManager.isExistsActivity(LotteryPL5Activity.class);
			if (pl5Activity == null) {
				@SuppressWarnings("null")
				Intent intent = new Intent(LotteryCartActivity.this, LotteryPL5Activity.class);
				intent.putExtra("source", 1);
				startActivity(intent);
			} else {
				finish();
//				showExitDialog();
			}
			break;

		default:
			finish();
			break;
		}
	}
	
	private void saveData() {
		// TODO Auto-generated method stub
		LotterySaveObj obj = new LotterySaveObj();
		switch (getGameType()) {
		case GlobalConstants.TC_DLT:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "dlt_save";
			break;
		case GlobalConstants.TC_QXC:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "qxc_save";
			break;
		case GlobalConstants.TC_PL3:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p3_save";
			ArrayList<CodeInfo> itemList = getData().itemList;
			ArrayList<CodeInfo> list = new ArrayList<CodeInfo>();

			for (int i = 0; i < itemList.size(); i++) {
				switch (itemList.get(i).mTypeId) {
				case "01":
					list.add(itemList.get(i));
					break;
				case "02":
					
					break;
				case "03":
					CodeInfo info2 = new CodeInfo();
					StringBuilder sb2 = new StringBuilder();
					String[] arr2 = itemList.get(i).mCode.split("\\,");
					if(!arr2[0].contains(arr2[1])) {
						sb2.append(arr2[0] + ",");
						sb2.append(arr2[1]);
						
						info2.mBallType = itemList.get(i).mBallType;
						info2.mMultipe = itemList.get(i).mMultipe;
						info2.mNum = itemList.get(i).mNum;
						info2.mPrice = itemList.get(i).mPrice;
						info2.mSeleId = itemList.get(i).mSeleId;
						info2.mTypeId = itemList.get(i).mTypeId;
						info2.mCode = sb2.toString();
						list.add(info2);
					}
					
					break;
				case "04":
					list.add(itemList.get(i));
					break;
				case "05":
					break;
				default:
					break;
				}
				
			}

			obj.setData(list);
			break;
		case GlobalConstants.TC_PL5:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p5_save";
			break;
		default:
			break;
		}
		
		if (!getGameType().equals(GlobalConstants.TC_PL3)) {
			obj.setData(getData().itemList);
		}
		DLTConfiguration.getConfiguration().saveUserConfig(obj);
	}

	// 初始化数据显示
	public abstract void initData(Intent data);

	// 机选号码添加到购物车
	public abstract void jxIntoCart();

	// 自选号码添加到购物车
	public abstract void zxIntoCart();

	// 彩票类型
	public abstract String getGameType();

	// 彩票投注title
	public abstract String getTitle(String term, int buyType);

	// 彩票名称
	public abstract int getLotteryName();

	public abstract void goBuy(LinearLayout itemLayout, CodeInfo codeInfo);
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			goBack();
			showExitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
}
