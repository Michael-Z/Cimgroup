package cn.com.cimgroup.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseLoteryActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ViewStub.PL3_Z3;
import cn.com.cimgroup.activity.ViewStub.PL3_Z6;
import cn.com.cimgroup.activity.ViewStub.PL3_ZHIXHZ;
import cn.com.cimgroup.activity.ViewStub.PL3_ZXHZ;
import cn.com.cimgroup.frament.LoboHallFrament;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num.PlayMenuItemClick;
import cn.com.cimgroup.popwindow.PopupWndSwitch_PL3;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.DragLayout;
import cn.com.cimgroup.view.LBGridView;
import cn.com.cimgroup.view.LBGridviewController;
import cn.com.cimgroup.xutils.ActivityManager;

/**
 * 排列3购彩
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月21日
 */
public class LotteryPL3Activity extends BaseLoteryActivity implements
		PlayMenuItemClick {

	private TextView mTouzhuHint;
	private TextView mTouzhuConfig;
	private LBGridviewController mBwController;
	private LBGridviewController mSwController;
	private LBGridviewController mGwController;
	private TextView mSwitchTextView;
	private TextView mHintTextView;
	private PopupWndSwitchPLayMenu mPopMenu;
	private LinearLayout mBgPop;// popwindow的背景
	private PopupWndSwitch_PL3 pwSwitch_PL3;
	private LinearLayout mLayoutZx;
	private ViewStub mLayoutZxhz;
	private ViewStub mLayoutZ3;
	private ViewStub mLayoutZhixhz;
	private ViewStub mLayoutZ6;
	private View mViewZhixhz;
	private View mViewZ3;
	private View mViewZxhz;
	private View mViewZ6;
	private PL3_ZHIXHZ pl3_ZHIXHZ;
	private PL3_Z3 pl3_Z3;
	private PL3_ZXHZ pl3_ZXHZ;
	private PL3_Z6 pl3_Z6;

	private RelativeLayout bottomTZ;
	private PopupWndSwitch_Jx_Num jxNum;

	private TextView mIssueTextView;
	private TextView mIssueTimeTextView;
	private ListView mLotteryDrawListView;

	private DragLayout mDragLayout;
	
	private int source;

	@Override
	protected void onHandleMessage(Message message) {
		switch (message.what) {
		case GlobalConstants.MSG_NOTIFY_SWITCH_WAY_PL3:
			tag = message.arg1;
			showLayout((String) message.obj);
			pwSwitch_PL3.dissmiss();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_pl3);
		initView();
		tag = GlobalConstants.LOTTERY_PL3_ZX;
		intent = getIntent();
		source = intent.getIntExtra("source", 0);
		isCartForResult = intent.getBooleanExtra(
				GlobalConstants.CART_IS_FORRESULT, false);
		if (isCartForResult) {
			showIntentData(intent);
		}
		
		if (obj != null && source != 1) {
			promptDialog0.showDialog();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		isClear = intent.getBooleanExtra("isClear", true);
		if (isClear) {
			clearData();
			initTzHint();
		}
	}

	/**
	 * 根据不同的类型显示不同的页面内容
	 * 
	 * @Description:
	 * @param text
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	private void showLayout(String text) {
		mSwitchTextView.setText(text);
		switch (tag) {
		// 直选
		case GlobalConstants.LOTTERY_PL3_ZX:
			mLayoutZhixhz.setVisibility(View.GONE);
			mLayoutZ3.setVisibility(View.GONE);
			mLayoutZxhz.setVisibility(View.GONE);
			mLayoutZ6.setVisibility(View.GONE);
			mLayoutZx.setVisibility(View.VISIBLE);
			mHintTextView.setText(LotteryShowUtil.setRedText(mActivity,
					getResources().getString(R.string.lottery_p3_zxtj),
					1040 + getResources().getString(R.string.lemi_unit)));
			break;
		// 直选和值
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			mLayoutZx.setVisibility(View.GONE);
			mLayoutZ3.setVisibility(View.GONE);
			mLayoutZxhz.setVisibility(View.GONE);
			mLayoutZ6.setVisibility(View.GONE);
			if (mViewZhixhz == null) {
				mViewZhixhz = mLayoutZhixhz.inflate();
				pl3_ZHIXHZ = new PL3_ZHIXHZ(mViewZhixhz, mTouzhuHint);
			} else {
				mLayoutZhixhz.setVisibility(View.VISIBLE);
			}
			mHintTextView.setText(LotteryShowUtil.setRedText(mActivity,
					getResources().getString(R.string.lottery_p3_zhixhztj),
					1040 + getResources().getString(R.string.lemi_unit)));
			break;
		// 组三
		case GlobalConstants.LOTTERY_PL3_Z3:
			mLayoutZx.setVisibility(View.GONE);
			mLayoutZhixhz.setVisibility(View.GONE);
			mLayoutZxhz.setVisibility(View.GONE);
			mLayoutZ6.setVisibility(View.GONE);
			if (mViewZ3 == null) {
				mViewZ3 = mLayoutZ3.inflate();
				pl3_Z3 = new PL3_Z3(mViewZ3, mTouzhuHint);
			} else {
				mLayoutZ3.setVisibility(View.VISIBLE);
			}
			mHintTextView.setText(LotteryShowUtil.setRedText(mActivity,
					getResources().getString(R.string.lottery_p3_z3tj),
					346 + getResources().getString(R.string.lemi_unit)));
			break;
		// 组选和值
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			mLayoutZx.setVisibility(View.GONE);
			mLayoutZhixhz.setVisibility(View.GONE);
			mLayoutZ3.setVisibility(View.GONE);
			mLayoutZ6.setVisibility(View.GONE);
			if (mViewZxhz == null) {
				mViewZxhz = mLayoutZxhz.inflate();
				pl3_ZXHZ = new PL3_ZXHZ(mViewZxhz, mTouzhuHint);
			} else {
				mLayoutZxhz.setVisibility(View.VISIBLE);
			}
			mHintTextView.setText(Html.fromHtml(getResources().getString(
					R.string.lottery_p3_zxhztj, 346, 173)));
			break;
		// 组六
		case GlobalConstants.LOTTERY_PL3_Z6:
			mLayoutZx.setVisibility(View.GONE);
			mLayoutZhixhz.setVisibility(View.GONE);
			mLayoutZ3.setVisibility(View.GONE);
			mLayoutZxhz.setVisibility(View.GONE);
			if (mViewZ6 == null) {
				mViewZ6 = mLayoutZ6.inflate();
				pl3_Z6 = new PL3_Z6(mViewZ6, mTouzhuHint);
			} else {
				mLayoutZ6.setVisibility(View.VISIBLE);
			}
			mHintTextView.setText(LotteryShowUtil.setRedText(mActivity,
					getResources().getString(R.string.lottery_p3_z6tj),
					173 + getResources().getString(R.string.lemi_unit)));
			break;
		default:
			break;
		}
		initTzHint();
	}

	private void showIntentData(Intent intent) {
		tag = intent.getIntExtra(GlobalConstants.LOTTERY_TYPE, 0);
		ArrayList<Integer> list1 = intent
				.getIntegerArrayListExtra(GlobalConstants.BALL_LIST1);
		switch (tag) {
		case 0:
			tag = GlobalConstants.LOTTERY_PL3_ZX;
			break;
		// 直选
		case GlobalConstants.LOTTERY_PL3_ZX:
			showLayout(getResources().getString(R.string.lottery_p3) + "-"
					+ getResources().getString(R.string.lottery_p3_pop_zx));
			ArrayList<Integer> list2 = intent
					.getIntegerArrayListExtra(GlobalConstants.BALL_LIST2);
			ArrayList<Integer> list3 = intent
					.getIntegerArrayListExtra(GlobalConstants.BALL_LIST3);
			mBwController.insertSelect(list1);
			mSwController.insertSelect(list2);
			mGwController.insertSelect(list3);
			break;
		// 直选和值
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			showLayout(getResources().getString(R.string.lottery_p3) + "-"
					+ getResources().getString(R.string.lottery_p3_zxhz));
			pl3_ZHIXHZ.mZhixhzController.insertSelect(list1);
			break;
		// 组三
		case GlobalConstants.LOTTERY_PL3_Z3:
			showLayout(getResources().getString(R.string.lottery_p3) + "-"
					+ getResources().getString(R.string.lottery_p3_pop_z3));
			pl3_Z3.mZ3Controller.insertSelect(list1);
			break;
		// 组选和值
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			showLayout(getResources().getString(R.string.lottery_p3) + "-"
					+ getResources().getString(R.string.lottery_p3_pop_zxhz));
			pl3_ZXHZ.mZxhzController.insertSelect(list1);
			break;
		// 组六
		case GlobalConstants.LOTTERY_PL3_Z6:
			showLayout(getResources().getString(R.string.lottery_p3) + "-"
					+ getResources().getString(R.string.lottery_p3_pop_z6));
			pl3_Z6.mZ6Controller.insertSelect(list1);
			break;
		}
		initTzHint();
	}

	private void initView() {
		mLayoutZx = (LinearLayout) findViewById(R.id.layout_pl3_zx);
		mLayoutZhixhz = (ViewStub) findViewById(R.id.layout_pl3_zhixhz);
		mLayoutZ3 = (ViewStub) findViewById(R.id.layout_pl3_z3);
		mLayoutZxhz = (ViewStub) findViewById(R.id.layout_pl3_zxhz);
		mLayoutZ6 = (ViewStub) findViewById(R.id.layout_pl3_z6);
		mBgPop = (LinearLayout) findViewById(R.id.pop_pl3_bg);
		mSwitchTextView = (TextView) findViewById(R.id.textView_title_word2);
		mSwitchTextView.setText(getResources().getString(R.string.lottery_p3)
				+ "-" + getResources().getString(R.string.lottery_p3_pop_zx));
		((ImageView) findViewById(R.id.textView_title_menu2))
				.setVisibility(View.VISIBLE);
		((ImageView) findViewById(R.id.imageView_lottery_shake))
				.setOnClickListener(this);
		bottomTZ = (RelativeLayout) findViewById(R.id.tz_layout);
		mTouzhuHint = (TextView) findViewById(R.id.textView_touzhu_hint);
		mTouzhuConfig = (TextView) findViewById(R.id.rl_touzhu_config);
		mHintTextView = (TextView) findViewById(R.id.sv_hint);
		mHintTextView.setText(LotteryShowUtil.setRedText(mActivity,
				getResources().getString(R.string.lottery_p3_init),
				1040 + getResources().getString(R.string.lemi_unit)));
		LBGridView mBwGridView = (LBGridView) findViewById(R.id.gridView_pl3_bw);
		LBGridView mSwGridView = (LBGridView) findViewById(R.id.gridView_pl3_sw);
		LBGridView mGwGridView = (LBGridView) findViewById(R.id.gridView_pl3_gw);
		mBwController = mBwGridView.getController();
		mSwController = mSwGridView.getController();
		mGwController = mGwGridView.getController();
		mBwGridView.setCallBack(this);
		mSwGridView.setCallBack(this);
		mGwGridView.setCallBack(this);

		mIssueTextView = (TextView) findViewById(R.id.issue_tv);
		mIssueTimeTextView = (TextView) findViewById(R.id.issue_time_tv);
		mLotteryDrawListView = (ListView) findViewById(R.id.listView_pl3_lv_multerm);
		mDragLayout = (DragLayout) findViewById(R.id.dragLayout_pl3);
	}

	/**
	 * 更新底部注数金额
	 */
	private void initTzHint() {
		switch (tag) {
		// 直选
		case GlobalConstants.LOTTERY_PL3_ZX:
			mTouzhuHint.setText(LotteryBettingUtil.setNumMoney(
					mBwController.getSelectData(),
					mSwController.getSelectData(),
					mGwController.getSelectData()));
			break;
		// 直选和值
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			mTouzhuHint.setText(LotteryBettingUtil
					.setNumMoney_Pl3Zhixhz(pl3_ZHIXHZ.mZhixhzController
							.getSelectData()));
			break;
		// 组三
		case GlobalConstants.LOTTERY_PL3_Z3:
			mTouzhuHint.setText(LotteryBettingUtil
					.setNumMoney_Pl3Z3(pl3_Z3.mZ3Controller.getSelectData()));
			break;
		// 组选和值
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			mTouzhuHint.setText(LotteryBettingUtil
					.setNumMoney_Pl3Zxhz(pl3_ZXHZ.mZxhzController
							.getSelectData()));
			break;
		// 组六
		case GlobalConstants.LOTTERY_PL3_Z6:
			mTouzhuHint.setText(LotteryBettingUtil
					.setNumMoney_Pl3Z6(pl3_Z6.mZ6Controller.getSelectData()));
			break;
		}
	}

	/**
	 * 清空选中的号码
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	private void clearData() {
		switch (tag) {
		// 直选
		case GlobalConstants.LOTTERY_PL3_ZX:
			mBwController.clearSelectState();
			mSwController.clearSelectState();
			mGwController.clearSelectState();
			break;
		// 直选和值
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			pl3_ZHIXHZ.mZhixhzController.clearSelectState();
			break;
		// 组三
		case GlobalConstants.LOTTERY_PL3_Z3:
			pl3_Z3.mZ3Controller.clearSelectState();
			break;
		// 组选和值
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			pl3_ZXHZ.mZxhzController.clearSelectState();
			break;
		// 组六
		case GlobalConstants.LOTTERY_PL3_Z6:
			pl3_Z6.mZ6Controller.clearSelectState();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back2:
			switch (source) {
			case 1:
				MainActivity mainActivity = (MainActivity) ActivityManager.isExistsActivity(MainActivity.class);
				if (mainActivity != null) {
					// 如果存在MainActivity实例，那么展示用户中心页面
					mainActivity.showFrament(0);
				}
				ActivityManager.retain(MainActivity.class);  
				break;

			default:
				mActivity.finish();
				break;
			}
			break;
		case R.id.issue_time_tv:
			if (mDragLayout.isOpen()) {
				mDragLayout.close(DragLayout.PART_1);
				break;
			}
			mDragLayout.open(DragLayout.PART_1);
			break;
		case R.id.textView_title_word2:
			pwSwitch_PL3 = new PopupWndSwitch_PL3(mActivity, tag, mHandlerEx,
					mBgPop, mSwitchTextView);
			pwSwitch_PL3.showPopWindow(v);
			mBgPop.setVisibility(View.VISIBLE);
			break;
		case R.id.textView_title_menu2:
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchPLayMenu(mActivity, this, true);
			}
			mPopMenu.showPopWindow(v);
			break;
		case R.id.imageView_lottery_shake:
			onShakeEvent();
			break;
		case R.id.textView_lottery_jx:
			if (jxNum == null) {
				jxNum = new PopupWndSwitch_Jx_Num(mActivity, this);
			}
			jxNum.showListItemViewPopupWindow(mActivity, bottomTZ);
			break;
		case R.id.rl_touzhu_config:// 投注
			switch (tag) {
			// 直选
			case GlobalConstants.LOTTERY_PL3_ZX:
				ArrayList<Integer> bwList = mBwController.getSelectData();
				ArrayList<Integer> swList = mSwController.getSelectData();
				ArrayList<Integer> gwList = mGwController.getSelectData();
				if (bwList.size() < 1 || swList.size() < 1 || gwList.size() < 1) {
					// showToast(getResources().getString(R.string.lottery_qxc_sv_hint));
					if (bwList.size() > 0 || swList.size() > 0 || gwList.size() > 0 || obj == null) {
						onShakeEvent();
						return;
					}
				}
				if (isCartForResult) {
					Intent data = new Intent();
					setZxIntent(bwList, swList, gwList, data);
					setResult(RESULT_OK, data);
					mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryPL3CartActivity.class);
					setZxIntent(bwList, swList, gwList, intent);
					startActivity(intent);
					// mActivity.finish();
				}
				break;
			// 直选和值
			case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
				ArrayList<Integer> zhixhzList = pl3_ZHIXHZ.mZhixhzController
						.getSelectData();
				if (zhixhzList.size() < 1) {
					// showToast(getResources().getString(R.string.lottery_p3_hz_must));
					onShakeEvent();
					return;
				}
				if (isCartForResult) {
					Intent data = new Intent();
					setZhixhzIntent(zhixhzList, data);
					setResult(RESULT_OK, data);
					mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryPL3CartActivity.class);
					setZhixhzIntent(zhixhzList, intent);
					startActivity(intent);
					// mActivity.finish();
				}
				break;
			// 组三
			case GlobalConstants.LOTTERY_PL3_Z3:
				ArrayList<Integer> z3List = pl3_Z3.mZ3Controller
						.getSelectData();
				if (z3List.size() < 2) {
					// showToast(getResources().getString(R.string.lottery_p3_z3_must,2));
					if (z3List.size() > 0 || obj == null) {
						onShakeEvent();
						return;
					}
				}
				if (isCartForResult) {
					Intent data = new Intent();
					setZ3Intent(z3List, data);
					setResult(RESULT_OK, data);
					mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryPL3CartActivity.class);
					setZ3Intent(z3List, intent);
					startActivity(intent);
					// mActivity.finish();
				}
				break;
			// 组选和值
			case GlobalConstants.LOTTERY_PL3_ZXHZ:
				ArrayList<Integer> zxhzList = pl3_ZXHZ.mZxhzController
						.getSelectData();
				if (zxhzList.size() < 1) {
					// showToast(getResources().getString(R.string.lottery_p3_hz_must));
					onShakeEvent();
					return;
				}
				if (isCartForResult) {
					Intent data = new Intent();
					setZxhzIntent(zxhzList, data);
					setResult(RESULT_OK, data);
					mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryPL3CartActivity.class);
					setZxhzIntent(zxhzList, intent);
					startActivity(intent);
					// mActivity.finish();
				}
				break;
			// 组六
			case GlobalConstants.LOTTERY_PL3_Z6:
				ArrayList<Integer> z6List = pl3_Z6.mZ6Controller
						.getSelectData();
				if (z6List.size() < 3) {
					// showToast(getResources().getString(R.string.lottery_p3_z3_must,3));
					if (z6List.size() > 0 || obj == null) {
						onShakeEvent();
						return;
					}
				}
				if (isCartForResult) {
					Intent data = new Intent();
					setZ6Intent(z6List, data);
					setResult(RESULT_OK, data);
					mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryPL3CartActivity.class);
					setZ6Intent(z6List, intent);
					startActivity(intent);
					// mActivity.finish();
				}
				break;
			}
			break;
		default:
			break;
		}
	}

	private void setZxIntent(ArrayList<Integer> list1,
			ArrayList<Integer> list2, ArrayList<Integer> list3, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_PL3_ZX);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, list1);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, list2);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, list3);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
	}

	private void setZhixhzIntent(ArrayList<Integer> list1, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_PL3_ZHIXHZ);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, list1);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
	}

	private void setZ3Intent(ArrayList<Integer> list1, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_PL3_Z3);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, list1);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
	}

	private void setZxhzIntent(ArrayList<Integer> list1, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_PL3_ZXHZ);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, list1);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
	}

	private void setZ6Intent(ArrayList<Integer> list1, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_PL3_Z6);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, list1);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		if (parent == mBwController.getGridView()) {
			mBwController.updateSelect(position);
		} else if (parent == mSwController.getGridView()) {
			mSwController.updateSelect(position);
		} else if (parent == mGwController.getGridView()) {
			mGwController.updateSelect(position);
		}
		initTzHint();
	}

	@Override
	protected int getLotteryName() {
		return GlobalConstants.LOTTERY_PL3;
	}

	@Override
	protected void onShakeEvent() {
		super.onShakeEvent();
		switch (tag) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			mBwController.randomBall(mBwController.getSelectData(), 1, 10);
			mSwController.randomBall(mSwController.getSelectData(), 1, 10);
			mGwController.randomBall(mGwController.getSelectData(), 1, 10);
			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			pl3_ZHIXHZ.mZhixhzController.randomBall(
					pl3_ZHIXHZ.mZhixhzController.getSelectData(), 1, 28);
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			pl3_Z3.mZ3Controller.randomBall(
					pl3_Z3.mZ3Controller.getSelectData(), 2, 10);
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			pl3_ZXHZ.mZxhzController.randomBall(
					pl3_ZXHZ.mZxhzController.getSelectData(), 1, 28);
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			pl3_Z6.mZ6Controller.randomBall(
					pl3_Z6.mZ6Controller.getSelectData(), 3, 10);
			break;
		}
		initTzHint();
	}

	@Override
	protected TextView getIssueTextView() {
		return mIssueTextView;
	}

	@Override
	protected TextView getIssueTimeTextView() {
		return mIssueTimeTextView;
	}

	@Override
	protected ListView getMulTermListView() {
		return mLotteryDrawListView;
	}

	@Override
	protected TextView getTZTextView() {
		return mTouzhuHint;
	}

	@Override
	protected TextView getConfigRelativeLayout() {
		return mTouzhuConfig;
	}

	@Override
	public void PopJx(int num) {
		if (isCartForResult) {
			ArrayList<Integer> list1 = new ArrayList<Integer>();
			ArrayList<Integer> list2 = new ArrayList<Integer>();
			ArrayList<Integer> list3 = new ArrayList<Integer>();

			Intent data = new Intent();
			for (int i = 0; i < num; i++) {
				switch (tag) {
				case GlobalConstants.LOTTERY_PL3_ZX:
					LotteryShowUtil.randomAddBall(list1, 1, 10);
					LotteryShowUtil.randomAddBall(list2, 1, 10);
					LotteryShowUtil.randomAddBall(list3, 1, 10);
					data.putExtra(GlobalConstants.LOTTERY_TYPE,
							GlobalConstants.LOTTERY_PL3_ZX);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1,
							list1);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2,
							list2);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3,
							list3);
					break;
				case GlobalConstants.LOTTERY_PL3_ZXHZ:
					LotteryShowUtil.randomAddBall(list1, 1, 26);
					data.putExtra(GlobalConstants.LOTTERY_TYPE,
							GlobalConstants.LOTTERY_PL3_ZXHZ);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1,
							list1);
					break;
				case GlobalConstants.LOTTERY_PL3_Z3:
					LotteryShowUtil.randomAddBall(list1, 2, 10);
					data.putExtra(GlobalConstants.LOTTERY_TYPE,
							GlobalConstants.LOTTERY_PL3_Z3);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1,
							list1);
					break;
				case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
					LotteryShowUtil.randomAddBall(list1, 1, 27);
					data.putExtra(GlobalConstants.LOTTERY_TYPE,
							GlobalConstants.LOTTERY_PL3_ZHIXHZ);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1,
							list1);
					break;
				case GlobalConstants.LOTTERY_PL3_Z6:
					LotteryShowUtil.randomAddBall(list1, 3, 10);
					data.putExtra(GlobalConstants.LOTTERY_TYPE,
							GlobalConstants.LOTTERY_PL3_Z6);
					data.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1,
							list1);
					break;
				default:
					break;
				}
			}
			data.putExtra(GlobalConstants.LIST_JXNUM, num);
			data.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			data.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView
					.getText().toString());
			data.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
					LotteryBettingUtil
							.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
			setResult(RESULT_OK, data);
			mActivity.finish();
		} else {
			Intent intent = new Intent(mActivity, LotteryPL3CartActivity.class);
			switch (tag) {
			case GlobalConstants.LOTTERY_PL3_ZX:
				intent.putExtra(GlobalConstants.LOTTERY_P3_TYPE,
						GlobalConstants.LOTTERY_PL3_ZX);
				break;
			case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
				intent.putExtra(GlobalConstants.LOTTERY_P3_TYPE,
						GlobalConstants.LOTTERY_PL3_ZHIXHZ);
				break;
			case GlobalConstants.LOTTERY_PL3_Z3:
				intent.putExtra(GlobalConstants.LOTTERY_P3_TYPE,
						GlobalConstants.LOTTERY_PL3_Z3);
				break;
			case GlobalConstants.LOTTERY_PL3_ZXHZ:
				intent.putExtra(GlobalConstants.LOTTERY_P3_TYPE,
						GlobalConstants.LOTTERY_PL3_ZXHZ);
				break;
			case GlobalConstants.LOTTERY_PL3_Z6:
				intent.putExtra(GlobalConstants.LOTTERY_P3_TYPE,
						GlobalConstants.LOTTERY_PL3_Z6);
				break;
			default:
				break;
			}
			intent.putExtra(GlobalConstants.LOTTERY_TYPE,
					GlobalConstants.LOTTERY_DLT_JX);
			intent.putExtra(GlobalConstants.LIST_JXNUM, num);
			intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView
					.getText().toString());
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
					LotteryBettingUtil
							.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
			startActivity(intent);
		}

	}

	@Override
	public void PopZST() {
		Intent intent=new Intent(this,ZSTWebViewActivity.class);
		intent.putExtra("url", GlobalConstants.getZSTUrl(1));
		intent.putExtra("title", "排列三");
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		startActivity(intent);
	}
	
}
