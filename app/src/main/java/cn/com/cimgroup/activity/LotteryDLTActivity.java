package cn.com.cimgroup.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import cn.com.cimgroup.activity.ViewStub.DLT_DanTuo;
import cn.com.cimgroup.frament.LoboHallFrament;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu;
import cn.com.cimgroup.popwindow.PopupWndSwitch_DLT;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num.PlayMenuItemClick;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.DragLayout;
import cn.com.cimgroup.view.LBGridView;
import cn.com.cimgroup.view.LBGridviewController;
import cn.com.cimgroup.xutils.ActivityManager;

/**
 * 大乐透购彩
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月17日
 */
public class LotteryDLTActivity extends BaseLoteryActivity implements
		PlayMenuItemClick {

	private TextView mSwitchTextView;
	private LinearLayout mBgPopLinearLayout;// popwindow的背景
	private PopupWndSwitch_DLT mPopChance;
	private PopupWndSwitchPLayMenu mPopMenu;
	private LBGridviewController mRedControllerGV;
	private LBGridviewController mBuleControllerGV;
	private TextView mTouzhuHint;
	private TextView mTouzhuConfig;
	private LinearLayout mLayoutBiaoZhun;// 标准布局
	private ViewStub mLayoutDanTuo;// 胆拖布局
	private View mViewDanTuo;
	private DLT_DanTuo danTuo;
	private DragLayout mDragLayout;
	private TextView mIssueTextView;
	private TextView mIssueTimeTextView;
	private ListView mMulTermListView;
	private PopupWndSwitch_Jx_Num jxNum;
	private RelativeLayout bottomTZ;
	private RelativeLayout dltTop;
	private TextView mJxtView;
	private ImageView mShakeView;

	@Override
	protected void onHandleMessage(Message message) {
		switch (message.what) {
		case GlobalConstants.MSG_NOTIFY_SWITCH_WAY_DLT:// 玩法切换
			tag = message.arg1;
			showLayout((String) message.obj);
			clearData();
			mPopChance.dissmiss();
			if (tag == GlobalConstants.LOTTERY_DLT_DT) {
				mJxtView.setText(getResources().getString(R.string.btn_clear));
				mJxtView.setCompoundDrawablesWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.icon_trash_all),
						null, null);
				mShakeView.setVisibility(View.GONE);
			} else {
				mJxtView.setText("机选");
				mJxtView.setCompoundDrawablesWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.lottery_jx),
						null, null);
				mShakeView.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	private void showLayout(String title) {
		mSwitchTextView.setText(title);
		if (tag == GlobalConstants.LOTTERY_DLT_PT) {
			mLayoutBiaoZhun.setVisibility(View.VISIBLE);
			mLayoutDanTuo.setVisibility(View.GONE);
			initTzHint();
			isShake(true);
		} else {
			mLayoutBiaoZhun.setVisibility(View.GONE);
			if (mViewDanTuo == null) {
				mViewDanTuo = mLayoutDanTuo.inflate();
				danTuo = new DLT_DanTuo(mViewDanTuo, mTouzhuHint);
			} else {
				mLayoutDanTuo.setVisibility(View.VISIBLE);
			}
			initTzDTHint();
			isShake(false);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_dlt);
		initView();

		tag = GlobalConstants.LOTTERY_DLT_PT;

		isCartForResult = intent.getBooleanExtra(
				GlobalConstants.CART_IS_FORRESULT, false);
		if (isCartForResult) {
			showIntentData(intent);
		}
		if (obj != null && source != 1) {
			promptDialog0.showDialog();
		}
	}

	private void showIntentData(Intent intent) {
		tag = intent.getIntExtra(GlobalConstants.LOTTERY_TYPE, 0);
		ArrayList<Integer> redList = intent
				.getIntegerArrayListExtra(GlobalConstants.LIST_REDBALL);
		ArrayList<Integer> buleList = intent
				.getIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL);
		switch (tag) {
		case 0:
			tag = GlobalConstants.LOTTERY_DLT_PT;
			break;
		case GlobalConstants.LOTTERY_DLT_PT:
			showLayout(getResources().getString(R.string.lotteryhall_dlt) + "-"
					+ getResources().getString(R.string.lottery_dlt_pt));
			mRedControllerGV.insertSelect(redList);
			mBuleControllerGV.insertSelect(buleList);
			initTzHint();
			break;
		case GlobalConstants.LOTTERY_DLT_DT:
			mJxtView.setVisibility(View.GONE);
			mShakeView.setVisibility(View.GONE);
			showLayout(getResources().getString(R.string.lotteryhall_dlt) + "-"
					+ getResources().getString(R.string.lottery_dlt_dt));
			ArrayList<Integer> redTuoList = intent
					.getIntegerArrayListExtra(GlobalConstants.LIST_REDBALL_DT);
			ArrayList<Integer> buleTuoList = intent
					.getIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL_DT);
			danTuo.mRedDanControllerGV.insertSelect(redList);
			danTuo.mRedTuoControllerGV.insertSelect(redTuoList);
			danTuo.mBuleDanControllerGV.insertSelect(buleList);
			danTuo.mBuleTuoControllerGV.insertSelect(buleTuoList);
			initTzDTHint();
			break;
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

	private void initView() {
		mBgPopLinearLayout = (LinearLayout) findViewById(R.id.pop_dlt_bg);
		mSwitchTextView = (TextView) findViewById(R.id.textView_title_word);
		mTouzhuHint = (TextView) findViewById(R.id.textView_touzhu_hint);
		mTouzhuConfig = (TextView) findViewById(R.id.rl_touzhu_config);
		mIssueTextView = (TextView) findViewById(R.id.issue_tv);
		mIssueTimeTextView = (TextView) findViewById(R.id.issue_time_tv);
		mDragLayout = (DragLayout) findViewById(R.id.dlt_dragLayout);
		bottomTZ = (RelativeLayout) findViewById(R.id.tz_layout);
		dltTop = (RelativeLayout) findViewById(R.id.dlt_top);
		mJxtView = (TextView) findViewById(R.id.textView_lottery_jx);
		mShakeView = (ImageView) findViewById(R.id.imageView_lottery_shake);
		mShakeView.setOnClickListener(this);
		((ImageView) findViewById(R.id.textView_title_menu))
				.setVisibility(View.VISIBLE);

		mMulTermListView = (ListView) findViewById(R.id.dlt_lv_multerm);

		mLayoutBiaoZhun = (LinearLayout) findViewById(R.id.dlt_biaozhun_ll);
		mLayoutDanTuo = (ViewStub) findViewById(R.id.dlt_dantuo_ll);

		LBGridView mRedBiaozhunGridView = (LBGridView) findViewById(R.id.gridView_red_biaozhun);
		mRedControllerGV = mRedBiaozhunGridView.getController();
		mRedBiaozhunGridView.setCallBack(this);

		LBGridView mBuleBiaozhunGridView = (LBGridView) findViewById(R.id.gridView_bule_biaozhun);
		mBuleControllerGV = mBuleBiaozhunGridView.getController();
		mBuleBiaozhunGridView.setCallBack(this);

		mSwitchTextView.setText(getResources().getString(
				R.string.lotteryhall_dlt)
				+ "-" + getResources().getString(R.string.lottery_dlt_pt));
		initTzHint();
	}

	private void initTzHint() {
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney(
				mRedControllerGV.getSelectData(),
				mBuleControllerGV.getSelectData()));
	}

	private void initTzDTHint() {
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney(
				danTuo.mRedDanControllerGV.getSelectData(),
				danTuo.mRedTuoControllerGV.getSelectData(),
				danTuo.mBuleDanControllerGV.getSelectData(),
				danTuo.mBuleTuoControllerGV.getSelectData()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
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
		case R.id.imageView_lottery_pull:
			if (mDragLayout.isOpen()) {
				mDragLayout.close(DragLayout.PART_1);
				break;
			}
			mDragLayout.open(DragLayout.PART_1);
			break;
		case R.id.textView_title_word:// 玩法切换Popupwindow
//			mPopChance = new PopupWndSwitch_DLT(mActivity, tag, mHandlerEx,
//					mBgPopLinearLayout, mSwitchTextView);
//			mPopChance.showPopWindow(dltTop);
//			mBgPopLinearLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.textView_title_menu:
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchPLayMenu(mActivity, this, true);
			}
			mPopMenu.showPopWindow(v);

			break;
		case R.id.textView_lottery_jx:
			if (tag == GlobalConstants.LOTTERY_DLT_DT) {
				clearData();
			} else {
				if (jxNum == null) {
					jxNum = new PopupWndSwitch_Jx_Num(mActivity, this);
				}
				jxNum.showListItemViewPopupWindow(mActivity, bottomTZ);
			}
			break;
		case R.id.imageView_lottery_shake:
			onShakeEvent();
			break;
		case R.id.rl_touzhu_config:// 投注
			if (mLayoutBiaoZhun.getVisibility() == View.VISIBLE) {
				ArrayList<Integer> mRedList = mRedControllerGV.getSelectData();
				ArrayList<Integer> mBuleList = mBuleControllerGV
						.getSelectData();
				boolean isJx = false;
				if (mRedList.size() < 5) {
					if (mRedList.size() > 0 || obj == null) {
						isJx = true;
					}
					// showToast(getResources().getString(R.string.lottery_dlt_red));
				}
				if (mBuleList.size() < 2) {
					if (mBuleList.size() > 0 || obj == null) {
						isJx = true;
					}
					// showToast(getResources().getString(R.string.lottery_dlt_blue));
					
				}
				if (LotteryBettingUtil.getDltMoney(mRedList, mBuleList) > GlobalConstants.LOTTERY_MAX_PRICE) {
					showToast(getResources().getString(
							R.string.lottery_money_than));
					return;
				}
				if (isJx) {
					onShakeEvent();
					return;
				}

				if (isCartForResult) {
					Intent data = new Intent();
					setIntentBZ(data, mRedList, mBuleList);
					setResult(RESULT_OK, data);
					// mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryDLTCartActivity.class);
					setIntentBZ(intent, mRedList, mBuleList);
					startActivity(intent);
					// mActivity.finish();
				}
			} else {
				ArrayList<Integer> redDanList = danTuo.mRedDanControllerGV
						.getSelectData();
				ArrayList<Integer> redTuoList = danTuo.mRedTuoControllerGV
						.getSelectData();
				ArrayList<Integer> buleDanList = danTuo.mBuleDanControllerGV
						.getSelectData();
				ArrayList<Integer> buleTuoList = danTuo.mBuleTuoControllerGV
						.getSelectData();
				if (redDanList.size() < 1) {
					showToast(getResources().getString(
							R.string.lottery_dlt_red_dm));
					return;
				}
				if (redDanList.size() + redTuoList.size() < 6) {
					showToast(getResources().getString(
							R.string.lottery_dlt_red_dmtm));
					return;
				}
				// if (buleDanList.size() < 1) {
				// showToast(getResources().getString(R.string.lottery_dlt_blue_dm));
				// return;
				// }
				if (buleTuoList.size() < 2) {
					showToast(getResources().getString(
							R.string.lottery_dlt_blue_tm));
					return;
				}
				if (LotteryBettingUtil.getDltMoney(redDanList, redTuoList,
						buleDanList, buleTuoList) > GlobalConstants.LOTTERY_MAX_PRICE) {
					showToast(getResources().getString(
							R.string.lottery_money_than));
					return;
				}
				if (isCartForResult) {
					Intent data = new Intent();
					setIntentDT(data, redDanList, redTuoList, buleDanList,
							buleTuoList);
					setResult(RESULT_OK, data);
					mActivity.finish();
				} else {
					Intent intent = new Intent(mActivity,
							LotteryDLTCartActivity.class);
					setIntentDT(intent, redDanList, redTuoList, buleDanList,
							buleTuoList);
					startActivity(intent);
					// mActivity.finish();
				}
			}
			break;
		default:
			break;
		}
	}

	private void setIntentDT(Intent data, ArrayList<Integer> redDanList,
			ArrayList<Integer> redTuoList, ArrayList<Integer> buleDanList,
			ArrayList<Integer> buleTuoList) {
		data.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_DLT_DT);
		data.putIntegerArrayListExtra(GlobalConstants.LIST_REDBALL, redDanList);
		data.putIntegerArrayListExtra(GlobalConstants.LIST_REDBALL_DT,
				redTuoList);
		data.putIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL,
				buleDanList);
		data.putIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL_DT,
				buleTuoList);
		data.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		data.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		data.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_DLT));
	}

	private void setIntentBZ(Intent data, ArrayList<Integer> mRedList,
			ArrayList<Integer> mBuleList) {
		data.putExtra(GlobalConstants.LOTTERY_TYPE,GlobalConstants.LOTTERY_DLT_PT);
		data.putIntegerArrayListExtra(GlobalConstants.LIST_REDBALL, mRedList);
		data.putIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL, mBuleList);
		data.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText().toString());
		data.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		data.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_DLT));
	}

	private void clearData() {
		mRedControllerGV.clearSelectState();
		mBuleControllerGV.clearSelectState();
		if (danTuo != null) {
			danTuo.mRedDanControllerGV.clearSelectState();
			danTuo.mRedTuoControllerGV.clearSelectState();
			danTuo.mBuleDanControllerGV.clearSelectState();
			danTuo.mBuleTuoControllerGV.clearSelectState();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		if (parent == mRedControllerGV.getGridView()) {
			mRedControllerGV.updateSelectDlt_Red(position);
		} else if (parent == mBuleControllerGV.getGridView()) {
			mBuleControllerGV.updateSelect(position);
		}
		initTzHint();
	}

	@Override
	protected int getLotteryName() {
		return GlobalConstants.LOTTERY_DLT;
	}

	@Override
	protected void onShakeEvent() {
		super.onShakeEvent();
		if (mLayoutBiaoZhun.getVisibility() == View.VISIBLE) {
			mRedControllerGV
					.randomBall(mRedControllerGV.getSelectData(), 5, 35);
			mBuleControllerGV.randomBall(mBuleControllerGV.getSelectData(), 2,
					12);
			initTzHint();
		}
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
		return mMulTermListView;
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
	public void PopJx(int jxNum) {
		if (isCartForResult) {
			ArrayList<Integer> list1 = new ArrayList<Integer>();
			ArrayList<Integer> list2 = new ArrayList<Integer>();
			for (int i = 0; i < jxNum; i++) {
				LotteryShowUtil.randomAddBall(list1, 5, 35);
				LotteryShowUtil.randomAddBall(list2, 2, 12);
			}

			Intent data = new Intent();
			setIntentBZ(data, list1, list2);
			data.putExtra(GlobalConstants.LIST_JXNUM, jxNum);
			data.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			data.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView
					.getText().toString());
			data.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
					LotteryBettingUtil
							.getIssueEndTime(GlobalConstants.LOTTERY_DLT));
			setResult(RESULT_OK, data);
			mActivity.finish();
		} else {
			Intent intent = new Intent(mActivity, LotteryDLTCartActivity.class);
			intent.putExtra(GlobalConstants.LOTTERY_TYPE,
					GlobalConstants.LOTTERY_DLT_JX);
			intent.putExtra(GlobalConstants.LIST_JXNUM, jxNum);
			intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView
					.getText().toString());
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
					LotteryBettingUtil
							.getIssueEndTime(GlobalConstants.LOTTERY_DLT));
			startActivity(intent);
		}
	}

	@Override
	public void PopZST() {
		Intent intent=new Intent(this,ZSTWebViewActivity.class);
		intent.putExtra("url", GlobalConstants.getZSTUrl(0));
		intent.putExtra("title", "大乐透");
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		startActivity(intent);
	}

}
