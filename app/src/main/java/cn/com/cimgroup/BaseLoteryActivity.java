package cn.com.cimgroup;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.activity.HtmlCommonActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LotteryCartActivity;
import cn.com.cimgroup.activity.LotteryDLTActivity;
import cn.com.cimgroup.activity.LotteryDLTCartActivity;
import cn.com.cimgroup.activity.LotteryDrawListActivity;
import cn.com.cimgroup.activity.LotteryPL3CartActivity;
import cn.com.cimgroup.activity.LotteryPL5CartActivity;
import cn.com.cimgroup.activity.LotteryQxcCartActivity;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.activity.TzListActivity;
import cn.com.cimgroup.adapter.LotteryDrawInfoAdapter;
import cn.com.cimgroup.bean.IssuePreList;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.LotteryList;
import cn.com.cimgroup.bean.LotteryListObj;
import cn.com.cimgroup.bean.LotterySaveObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.DLTConfiguration;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.frament.LoboHallFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu.PlayMenuItemClick;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.LBGridView.OnLBItemClickCallBack;
import cn.com.cimgroup.view.ShakeListener;
import cn.com.cimgroup.view.ShakeListener.OnShakeListener;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 数字彩购彩基类Activity
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public abstract class BaseLoteryActivity extends BaseActivity implements OnClickListener,
		OnLBItemClickCallBack, PlayMenuItemClick, OnShakeListener {

	protected int tag = 1;
	protected boolean isCartForResult;// 是否从购物车返回来的
	private Vibrator mVibrator;
	private ShakeListener mShakeListener;
	private boolean isShake = true;
	// 彩票距离结束的时间差
	private long differTime;
	private UserInfo userInfo;
	protected String issues;
	
	protected boolean isClear = false;
	
	protected Intent intent;
	
	protected int source;
	
	protected abstract void onHandleMessage(Message message);

	@SuppressLint("HandlerLeak")
	public final Handler mHandlerEx = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			onHandleMessage(msg);
		}
	};

	protected abstract int getLotteryName();
	
	protected PromptDialog_Black_Fillet promptDialog0;

	protected LotterySaveObj obj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		intent = getIntent();
		source = intent.getIntExtra("source", 0);
		promptDialog0 = new PromptDialog_Black_Fillet(mActivity);
		switch (getLotteryName()) {
		case GlobalConstants.LOTTERY_DLT:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "dlt_save";
			break;
		case GlobalConstants.LOTTERY_PL3:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p3_save";
			break;
		case GlobalConstants.LOTTERY_PL5:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p5_save";
			break;
		case GlobalConstants.LOTTERY_QXC:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "qxc_save";
			break;
		default:
			break;
		}
		obj = DLTConfiguration.getConfiguration().getLocalUserInfo();
		showEnterDialog();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mShakeListener != null) {
			mShakeListener.start();
		} else {
			mShakeListener = new ShakeListener(this);
			mShakeListener.setOnShakeListener(this);
		}
		userInfo = UserLogic.getInstance().getDefaultUserInfo();
		
		switch (getLotteryName()) {
		case GlobalConstants.LOTTERY_DLT:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "dlt_save";
			break;
		case GlobalConstants.LOTTERY_PL3:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p3_save";
			break;
		case GlobalConstants.LOTTERY_PL5:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "p5_save";
			break;
		case GlobalConstants.LOTTERY_QXC:
			GlobalConstants.NUM_LOTTERY_FILE_NAME = "qxc_save";
			break;
		default:
			break;
		}
		obj = DLTConfiguration.getConfiguration().getLocalUserInfo();
		showEnterDialog();
		// 请求预售期数
		Controller.getInstance().getLoBoPeriod(GlobalConstants.NUM_LOTTERY_PRE, LotteryBettingUtil.getLottery(getLotteryName()), mCallBack);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mHandlerEx.removeCallbacks(mDltRunnable);
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandlerEx.removeCallbacks(mDltRunnable);
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	public void onShake() {
		if (isShake) {
			mShakeListener.stop();
			mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
			mHandlerEx.postDelayed(new Runnable() {

				@Override
				public void run() {
					onShakeEvent();
					mShakeListener.start();
					mVibrator.cancel();
				}
			}, 600);
		}
	}

	protected void showToast(String msg) {
		ToastUtil.shortToast(mActivity, msg);
	}

	/**
	 * 摇一摇触发的事件
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @date:2015-3-6
	 */
	protected void onShakeEvent() {
	}

	/**
	 * 是否可以摇一摇
	 * 
	 * @Description:
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-6
	 */
	protected void isShake(boolean isShake) {
		this.isShake = isShake;
	}

	/** 开奖信息 **/
	@Override
	public void PopDraw() {
//		MainActivity mainActivity = (MainActivity) ActivityManager.isExistsActivity(MainActivity.class);
//		if (mainActivity != null) {
//			ActivityManager.retain(MainActivity.class);
//			// 如果存在MainActivity实例，那么展示LoboHallFrament页面
//			mainActivity.showFrament(LoboDrawFrament.class);
//		}
		LotteryDrawInfo info = new LotteryDrawInfo();
		info.setGameNo(LotteryBettingUtil.getLottery(getLotteryName()));
		switch (LotteryBettingUtil.getLottery(getLotteryName())) {
		case GlobalConstants.TC_DLT:
			info.setGameName(getResources().getString(R.string.tz_select_dlt));
			break;
		case GlobalConstants.TC_PL3:
			info.setGameName(getResources().getString(R.string.tz_select_p3));
			break;
		case GlobalConstants.TC_PL5:
			info.setGameName(getResources().getString(R.string.tz_select_p5));
			break;
		case GlobalConstants.TC_QXC:
			info.setGameName(getResources().getString(R.string.tz_select_qxc));
			break;
		default:
			break;
		}
		Intent intent = new Intent(BaseLoteryActivity.this, LotteryDrawListActivity.class);
		intent.putExtra("LotteryDrawInfo", info);
		startActivity(intent);
	}

	/** 跳转各玩法说明 **/
	@Override
	public void PopPLay() {
		Intent intent = new Intent(mActivity, HtmlCommonActivity.class);
		intent.putExtra("isWeb", false);
		intent.putExtra("lotteryName", getLotteryName());
		startActivity(intent);
	}
	
	/** 投注记录 **/
	@Override
	public void PopTzList() {
		int type = GlobalConstants.TAG_TZLIST_DLT;
		String text = getResources().getString(R.string.tz_select_dlt);
			
		switch (getLotteryName()) {
		case GlobalConstants.LOTTERY_DLT:
			GlobalConstants.personGameNo = GlobalConstants.TC_DLT;
			type = GlobalConstants.TAG_TZLIST_DLT;
			text = getResources().getString(R.string.tz_select_dlt);
			break;
		case GlobalConstants.LOTTERY_PL3:
			GlobalConstants.personGameNo = GlobalConstants.TC_PL3;
			type = GlobalConstants.TAG_TZLIST_P3;
			text = getResources().getString(R.string.tz_select_p3);
			break;
		case GlobalConstants.LOTTERY_PL5:
			GlobalConstants.personGameNo = GlobalConstants.TC_PL5;
			type = GlobalConstants.TAG_TZLIST_P5;
			text = getResources().getString(R.string.tz_select_p5);
			break;
		case GlobalConstants.LOTTERY_QXC:
			GlobalConstants.personGameNo = GlobalConstants.TC_QXC;
			type = GlobalConstants.TAG_TZLIST_QXC;
			text = getResources().getString(R.string.tz_select_qxc);
			break;
		default:
			break;
		}
		GlobalConstants.isRefreshFragment = true;
		GlobalConstants.personEndIndex = 0;
		GlobalConstants.personTagIndex = 0;
		GlobalConstants.isShowLeMiFragment = true;
		MainActivity main = (MainActivity) ActivityManager
				.isExistsActivity(MainActivity.class);
		if (main != null) {
			// 如果存在MainActivity实例，那么展示LoboHallFrament页面
			ActivityManager.retain(MainActivity.class);
			main.showFrament(3);
		}
//			Intent intent = new Intent(this, TzListActivity.class);
//			intent.putExtra("type", type);
//			intent.putExtra("text", text);
//			startActivity(intent);
	}
	
	/** 控制遗漏是否显示 **/
//	@Override
//	public void PopMiss(View view) {
//		TextView textView = (TextView)view;
//		if (mMissData != "") {
//			if (textView.getText().toString().equals(getResources().getString(R.string.pop_missing_open))) {
//				textView.setText(getResources().getString(R.string.pop_missing_close));
//			}else {
//				textView.setText(getResources().getString(R.string.pop_missing_open));
//			}
//			
//		}
//	}
	
	protected void showEnterDialog() {
			
			promptDialog0.setMessage(getResources().getString(
					R.string.lottery_content));
			promptDialog0.setPositiveButton(
					getResources().getString(R.string.btn_lottery_go),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							promptDialog0.hideDialog();
							switch (getLotteryName()) {
							case GlobalConstants.LOTTERY_DLT:
								Intent intent = new Intent(BaseLoteryActivity.this, LotteryDLTCartActivity.class);
								intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
								intent.putExtra(GlobalConstants.LOTTERY_ISSUE, getIssueTextView().getText());
								intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_DLT));
								startActivity(intent);
								break;
							case GlobalConstants.LOTTERY_PL3:
								Intent intent1 = new Intent(BaseLoteryActivity.this, LotteryPL3CartActivity.class);
								intent1.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
								intent1.putExtra(GlobalConstants.LOTTERY_ISSUE, getIssueTextView().getText());
								intent1.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL3));
								startActivity(intent1);
								break;
							case GlobalConstants.LOTTERY_PL5:
//								startActivity(LotteryPL5CartActivity.class);
								Intent intent2 = new Intent(BaseLoteryActivity.this, LotteryPL5CartActivity.class);
								intent2.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
								intent2.putExtra(GlobalConstants.LOTTERY_ISSUE, getIssueTextView().getText());
								intent2.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL5));
								startActivity(intent2);
								break;
							case GlobalConstants.LOTTERY_QXC:
//								startActivity(LotteryQxcCartActivity.class);
								Intent intent3 = new Intent(BaseLoteryActivity.this, LotteryQxcCartActivity.class);
								intent3.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
								intent3.putExtra(GlobalConstants.LOTTERY_ISSUE, getIssueTextView().getText());
								intent3.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_QXC));
								startActivity(intent3);
								break;
							default:
								break;
							}
						}
					});
			promptDialog0.setNegativeButton(
					getResources().getString(R.string.btn_lottery_clear),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							promptDialog0.hideDialog();
							obj = null;
							DLTConfiguration.getConfiguration().clear();
						}
					});
//			promptDialog0.showDialog();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	/**
	 * 设置TextView右边的Drawable图形
	 * @Description:
	 * @param tv
	 * @param drawable
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	protected void setTextViewRightDrawable(TextView tv, Drawable drawable) {
		LotteryShowUtil.setTextViewRightDrawable(tv, drawable);
	}

	private CallBack mCallBack = new CallBack() {

		public void getLoBoPeriodSuccess(final List<LoBoPeriodInfo> infos) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (infos != null) {
						for (int i = 0; i < infos.size(); i++) {
							LoBoPeriodInfo periodInfo = infos.get(i);
							Controller.getInstance().getLoBoZhuiPeriod(GlobalConstants.NUM_LOTTERY_ZHUI_LIST, LotteryBettingUtil.getLottery(getLotteryName()), periodInfo.getIssue(), mCallBack);
							getIssueTextView().setText(getResources().getString(R.string.lottery_begin_sell, periodInfo.getIssue() + ""));
							LotteryBettingUtil.setIssueAndEndTime(getLotteryName(), periodInfo.getIssue(), periodInfo.getEndTime() + "");
							getTermAndTime(periodInfo.getIssue(), periodInfo.getStartTime() + "", periodInfo.getEndTime() + "");
						}
					}
					// 请求开奖记录
					Controller.getInstance().getLotteryList(GlobalConstants.NUM_LOTTERY_LIST, LotteryBettingUtil.getLottery(getLotteryName()), "1", "10", mCallBack);
				}
			});
		};
		
		public void getLoBoPeriodFailure(String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					// 请求开奖记录
					Controller.getInstance().getLotteryList(GlobalConstants.NUM_LOTTERY_LIST, LotteryBettingUtil.getLottery(getLotteryName()), "1", "10", mCallBack);
				}
			});
		};
		
		public void getLotteryListSuccess(final LotteryList list) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (list != null) {
						getMulTermList(list.getList());
					}
				}
			});
		};

		public void getLoBoZhuiPeriodSuccess(final IssuePreList list) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (list.getResCode().equals("0")) {
						issues = list.getIssueArr();
					}
				}
			});
		};

	};

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
	private void getTermAndTime(String issue, String startDateTime, String endDateTime) {
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
			getIssueTextView().setText(getResources().getString(R.string.lottery_end_sell, issue));
		}
	}
	
	Runnable mDltRunnable = new Runnable() {

		@Override
		public void run() {
			differTime -= 1000;
			int red = getResources().getColor(R.color.color_red);
			String formatTime = "距截止剩：" + DateUtil.formatTime(differTime, false, red);
			getIssueTimeTextView().setText(Html.fromHtml(formatTime));
			if (differTime > 0) {
				mHandlerEx.postDelayed(this, 1000);
			}
		}
	};

	/**
	 * 开奖记录添加适配器
	 * 
	 * @Description:
	 * @param lotteryDrawInfos
	 * @author:www.wenchuang.com
	 * @date:2015-2-6
	 */
	private void getMulTermList(List<LotteryListObj> list) {
		LotteryDrawInfoAdapter adapter = new LotteryDrawInfoAdapter(mActivity, list);
		getMulTermListView().setAdapter(adapter);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
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
		}
		return super.onKeyDown(keyCode, event);
	}

	protected abstract TextView getIssueTextView();

	protected abstract TextView getIssueTimeTextView();

	protected abstract ListView getMulTermListView();
	
	protected abstract TextView getTZTextView();
	
	protected abstract TextView getConfigRelativeLayout();
}
