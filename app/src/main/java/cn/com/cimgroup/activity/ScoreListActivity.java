package cn.com.cimgroup.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.constants.PushCode;
import cn.com.cimgroup.frament.EmptyFragment;
import cn.com.cimgroup.frament.ScoreListFragment;
import cn.com.cimgroup.frament.ScoreListFragment.onChangeMatchTimesListener;
import cn.com.cimgroup.frament.ScoreListFragment_Focus;
import cn.com.cimgroup.frament.ScoreListFragment_Focus.onFocusMatchFilter;
import cn.com.cimgroup.frament.ScoreListFragment_Match;
import cn.com.cimgroup.frament.ScoreListFragment_Match.MyListener;
import cn.com.cimgroup.frament.ScoreListFragment_Match.onPageChangedListener;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.popwindow.PopWindowScoreMatchChoose;
import cn.com.cimgroup.popwindow.PopWindowScoreMatchChoose.onDateSelectedListener;
import cn.com.cimgroup.popwindow.PopWindowScoreMatchChoose.onMatchChooseItemClick;
import cn.com.cimgroup.popwindow.PopupWndScoreType;
import cn.com.cimgroup.popwindow.PopupWndScoreType.OnTypeItemClickListener;
import cn.com.cimgroup.popwindow.PopupWndSwitchCommon.PlayMenuItemClick;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 比分直播列表
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年12月8日
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint({ "HandlerLeak", "ClickableViewAccessibility", "SimpleDateFormat" })
public class ScoreListActivity extends BaseActivity implements OnClickListener,
		PlayMenuItemClick, OnTypeItemClickListener, MyListener,
		onMatchChooseItemClick, OnTouchListener, Observer, onDateSelectedListener, onPageChangedListener
		, onFocusMatchFilter, onChangeMatchTimesListener{
	/** 比分提示Layout */
	private LinearLayout id_toast_layout;
	/** 比分提示信息 */
	private TextView id_toast_text;
	/** 比分提示信息位移距离 */
	private float mMoveLength = 0;

	/** 显示or隐藏提示信息 */
	private boolean isShow = false;

	/** 选择标题布局 */
	private LinearLayout id_scorelist_choose_title_layout;

	/** 要展示的frament集合 MCenterRecordTz 为标识、主要在frament中区分投注记录类型，用于获取相应的数据 **/
	private Map<MScoreList, Fragment> mFraments = new HashMap<MScoreList, Fragment>();

	public LinearLayout mBgPop;

	private TextView id_score_activity_title;

	private ScorePagerAdapter mPagerAdapter;

	private int currentTab = 0;
	
	public static final String SOURCE_ACTIVITY = "scorelistactivity";

//	private ViewPager mViewPager;
	/** 所查询的即时比分的类别 */
	public static String mMatchType = "TC_JCZQ";
	/** 未结束比赛 */
//	private ScoreListFragment mNooverFragment;
	/** 已结束比赛 */
//	private ScoreListFragment mOverFragment;
	/** 关注的比赛 */
//	private ScoreListFragment mLikeFragment;
	/** 当前显示的活动Fragment */
//	private ScoreListFragment mShowFragment;
	/** 选择更多选项 */
	private RelativeLayout id_scorelist_activity_more;

	/** 联赛筛选（右上角选择，屏幕中间弹出）-- 比赛列表 */
	private PopWindowScoreMatchChoose mPopWindowScoreMatchChoose;
	/** 联赛筛选（右上角选择，屏幕中间弹出）-- 关注比赛 */
	private PopWindowScoreMatchChoose mPopWindowScoreMatchChoose_like;
	
	/** 期次筛选（右上角选择，屏幕中间弹出）-- 比赛列表**/
	private PopWindowScoreMatchChoose mPopWindowDateChoose_list;
	/** 期次筛选（右上角选择，屏幕中间弹出）-- 关注比赛**/
	private PopWindowScoreMatchChoose mPopWindowDateChoose_like;

	/** 显示彩票类别PopupWindow 改变标题（彩种类型，屏幕标题栏下方展开） */
	private PopupWndScoreType mPopupWindowScoreType;

	/** 期次Id */
	public static String issueNo = "";

	/** 期次数据 */
	private Map<String, String[]> mNOMap = new HashMap<String, String[]>();

	// /** 比赛赛事筛选 */
	// private List<String> mMatchNameList;

	/** 彩票类型数据源 */
	private List<String> mScoreTypeList = new ArrayList<String>();
	/** 头部布局 */
	private RelativeLayout id_scorelist_title_layout;
	/** 比分过滤按钮 */
	private RelativeLayout id_scorelist_activity_choose;
	/** 通知隐藏布局 */
	private final static int HIDE_TOAST = 3;
	/** 通知进球 */
	private final static int GOAL_TOAST = 4;
	/** 显示布局 */
	private final static int SHOW_TOAST_LAYOUT = 6;
	/** 隐藏布局 */
	private final static int HIDE_TOAST_LAYOUT = 7;
	/** 比赛列表**/
	private TextView tv_match_list;
	/** 关注**/
	private TextView tv_match_focus;
	/** tab底端红线**/
	private View view_line_red;
	/** 放置fragment的布局**/
	private FrameLayout id_fragment;
	/** fragment的切换事务**/
	private FragmentTransaction ft;
	/** fragment的manager**/
	private FragmentManager fm;
	/** 比赛列表的fragment**/
	private ScoreListFragment_Match fragment_match;
	private ScoreListFragment_Match fragment_match_zc;
	/** 关注比赛的fragment**/
	private ScoreListFragment_Focus fragment_like;
	/** 平移动画-红线从左至右**/
	private TranslateAnimation set_from_left_to_right;
	/** 平移动画-红线从右至左**/
	private TranslateAnimation set_from_right_to_left;
	/** 屏幕宽度，用于红线的滑动距离**/
	private int screenWidth;
	/** 当前正在显示的fragment**/
	private Fragment currentFragment;
	/** 竞彩足球期次信息集和**/
	private ArrayList<String> dateList;
	/** 老足彩期次信息集和**/
	private ArrayList<String> dateList_zc;
	private ArrayList<String> dateListCopy;
	private ArrayList<String> dateListCopy_zc;
	/** fragment类型   0：列表   1：关注**/
	private int type = 0;
	/**flag=1 表示  关注-登陆-登陆成功，这时应停留在关注页面上**/
	private int flag = 0;
	
	private MyReceiver myReciver;
	/** 当前显示的期次信息位置--列表**/
	private int position = 0;
	
	private EmptyFragment emptyFragment;
	
	public static boolean isFirstShown_list = true;
	public static boolean isFirstShown_like = true;
	
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case GlobalConstants.MSG_NOTIFY_SWITCH_WAY_SCORELIST:
				id_score_activity_title.setText((String) message.obj);
				ScoreListFragment fragment = (ScoreListFragment) mPagerAdapter
						.getItem(currentTab);
				fragment.loadData(message.arg1, 1);
				break;
			case GOAL_TOAST:
				// 进球
//				Log.e("qiufeng","______________________________________________________进球推送____动画");
				id_toast_text.setText(message.obj.toString());
				id_toast_text.setVisibility(View.VISIBLE);
				if (!isShow) {
					showView();
				}
				break;
			case HIDE_TOAST:
				if (isShow) {
					showView();
				}
				break;
			// case JPUSH_TEST:
			// jPushText();
			// mHandler.sendEmptyMessageDelayed(JPUSH_TEST, 10000);
			// break;
			case SHOW_TOAST_LAYOUT:
				id_toast_layout.setVisibility(View.VISIBLE); 
				break;
			case HIDE_TOAST_LAYOUT:
				id_toast_layout.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 比分列表类型
	 * 
	 * @Description
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年12月8日
	 */
	public static enum MScoreList {
		// 未结束
		NOOVER,
		// 已结束
		OVER,
		// 关注
		LIKE,
		// 列表（已开赛+为开赛）
		LIST,
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acccount_detail);
		
		fm = getSupportFragmentManager();
		initRequestNo();
		initViews();
		initEvent();
		initDatas();
		initAnimation();
		getNOMap();
		
		// 2.5期新增 胜负彩、半全场、进球彩的比分直播跳转过来的请求 要求停留在“老足彩”上
		String flag = getIntent().getStringExtra("special_flag");
		if (!TextUtils.isEmpty(flag) && flag.equals("lotteryoldfootball")) {
			changeTitle("老足彩");
			mPopupWindowScoreType.setData(mScoreTypeList, 1);
		}
	}
	
	private void initAnimation() {
		
		set_from_left_to_right = new TranslateAnimation(0, screenWidth/2, 0, 0);
		set_from_left_to_right.setDuration(500);
		set_from_left_to_right.setFillAfter(true);
		
		set_from_right_to_left = new TranslateAnimation(screenWidth/2, 0, 0, 0);
		set_from_right_to_left.setDuration(500);
		set_from_right_to_left.setFillAfter(true);
		
	}

	/**
	 * 初始化请求期号
	 */
	private void initRequestNo() {
		// 如果当前时间 在十一点之前 则用昨天的期号
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd HH");
		String time = sd.format(new Date());
		String[] timeArray = time.split(" ");
		String date = timeArray[0];
		String h = timeArray[1];
		if (Integer.parseInt(h) >= 11) {
			issueNo = date;
		} else {
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String day = date.substring(6);
			if (day.equals("1")) {
				int m = Integer.parseInt(month);
				day = DateUtil.getDaysOfMonth(year, (m - 1) + "") + "";
				month = (m - 1) + "";
			} else {
				int d = Integer.parseInt(day);
				day = (d - 1) < 10 ? "0" + (d - 1) : (d - 1) + "";
			}

			issueNo = year + month + day;
		}
		// 如果当前时间在十一点之后 则用今天的期号
	}

	/**
	 * 初始化数据
	 */
	@SuppressLint("ValidFragment")
	private void initDatas() {
		
		screenWidth = DensityUtils.getScreenWidth(ScoreListActivity.this);
		
		dateList = new ArrayList<String>();
		dateList_zc = new ArrayList<String>();
		dateListCopy = new ArrayList<String>();
		dateListCopy_zc = new ArrayList<String>();
		
		mScoreTypeList.add(getResources().getString(R.string.lotteryhall_jczq));
		mScoreTypeList.add(getResources().getString(
				R.string.oldfootball_dialog_desc));
		mPopupWindowScoreType.setData(mScoreTypeList);
		mPopupWindowScoreType.setOnTypeItemClickListener(this);
		
		emptyFragment = new EmptyFragment() {
			
			@Override
			public void loadFirstData() {
				//方法点击刷新按钮后调用
				getNOMap();
			}
		};
		
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		id_scorelist_choose_title_layout.setOnClickListener(this);
		id_scorelist_activity_choose.setOnClickListener(this);
		mPopWindowScoreMatchChoose.setOnCallBack(this);
		mPopWindowScoreMatchChoose_like.setOnCallBack(this);
		mPopWindowDateChoose_list.setOnDateSelectedListener(this);
		mPopWindowDateChoose_like.setOnDateSelectedListener(this);
		id_toast_layout.setOnTouchListener(this);
	}

	/**
	 * 初始化视图组件
	 */
	@SuppressLint("ValidFragment")
	private void initViews() {
		// mMatchNameList = new ArrayList<String>();

		id_toast_layout = (LinearLayout) findViewById(R.id.id_toast_layout);
		id_toast_text = (TextView) findViewById(R.id.id_toast_text);
		ViewUtils.measureView(id_toast_text);
		mMoveLength = id_toast_text.getLayoutParams().height;
		id_toast_text.setAlpha(0.9f);
		id_scorelist_title_layout = (RelativeLayout) findViewById(R.id.id_scorelist_title_layout);
		id_scorelist_choose_title_layout = (LinearLayout) findViewById(R.id.id_scorelist_choose_title_layout);
		id_scorelist_activity_more = (RelativeLayout) findViewById(R.id.id_scorelist_activity_more);
		id_scorelist_activity_more.setOnClickListener(this);// 2.3期之后打开
		id_scorelist_activity_more.setVisibility(View.VISIBLE);// 2.3期之后打开
		id_scorelist_activity_choose = (RelativeLayout) findViewById(R.id.id_scorelist_activity_choose);
		// 初始化比分筛选
		mPopWindowScoreMatchChoose = new PopWindowScoreMatchChoose(this, 1);
		mPopWindowScoreMatchChoose_like = new PopWindowScoreMatchChoose(this, 1);
		//初始化期次筛选
		mPopWindowDateChoose_list = new PopWindowScoreMatchChoose(this, 2);
		mPopWindowDateChoose_like = new PopWindowScoreMatchChoose(this, 2);
		
		emptyFragment = new EmptyFragment() {
			
			@Override
			public void loadFirstData() {
				
			}
		};

		id_score_activity_title = ((TextView) findViewById(R.id.id_score_activity_title));
		((TextView) findViewById(R.id.id_scorelist_activity_back))
				.setOnClickListener(this);
		// 暂时隐藏选择即时比分的能力功能
		id_score_activity_title.setOnClickListener(this);
		id_score_activity_title.setText(getResources().getString(
				R.string.tz_select_football));
		
		tv_match_focus = (TextView) findViewById(R.id.tv_match_focus);
		tv_match_focus.setOnClickListener(this);
		
		tv_match_list = (TextView) findViewById(R.id.tv_match_list);
		tv_match_list.setOnClickListener(this);
		
		view_line_red = findViewById(R.id.view_line_red);
		
		//设置红线的宽度为屏幕1/2
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		LayoutParams lp = view_line_red.getLayoutParams();
		lp.width = display.getWidth()/2;
		
		id_fragment = (FrameLayout) findViewById(R.id.id_fragment);

		mPopupWindowScoreType = new PopupWndScoreType(ScoreListActivity.this,
				id_scorelist_choose_title_layout);
		mPopupWindowScoreType.setTitleText(getResources().getString(R.string.scorelist_title_match));
		
		if (App.mObservableListener != null) {
			App.mObservableListener.addObserver(this);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (App.mObservableListener != null) {
			App.mObservableListener.deleteObserver(this);
		}
		unregisterReceiver(myReciver);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		myReciver = new MyReceiver();
		registerReceiver(myReciver, new IntentFilter("login_activity_wushiqiu"));
		
		//flag=1 表示  关注-登陆-登陆成功，这时应停留在关注页面上
		if (flag == 1) {
			setCurrentPage_focus();
			flag = 99999;
		}
//		getNOMap();
		if (tv_match_focus.isSelected()) {
			type = 1;
		}else {
			type = 0;
			tv_match_list.setSelected(true);
			id_scorelist_activity_more.setVisibility(View.VISIBLE);
			
		}
	}

	public class ScorePagerAdapter extends FragmentPagerAdapter {
		private static final String MSCORELIST = "mscorelist";

		private Map<MScoreList, Fragment> mFraments = new HashMap<MScoreList, Fragment>();

		FragmentManager mFragmentManager;

		public ScorePagerAdapter(FragmentManager fm) {
			super(fm);
			this.mFragmentManager = fm;
		}

		public void setFraments(Map<MScoreList, Fragment> fragments) {
			this.mFraments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			MScoreList type = MScoreList.values()[position];
			String title = "";
			switch (type) {
			// 未结束
			case NOOVER:
				title = getResources().getString(R.string.scorelist_noover);
				break;
			// 已结束
			case OVER:
				title = getResources().getString(R.string.scorelist_over);
				break;
			// 关注
			case LIKE:
				title = getResources().getString(R.string.scorelist_like);
				break;
			case LIST:
				//列表（已开赛+未开赛）
				title = getResources().getString(R.string.scorelist_list);
			default:
				break;
			}
			return title;
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			return newInstance(position);
		}

		@Override
		public int getCount() {
			return mFraments != null ? mFraments.size() : 0;
		}

		private Fragment newInstance(int position) {
			Fragment frament = null;
			if (mFraments != null) {
				frament = this.mFraments.get(MScoreList.values()[position]);
				if (frament.getArguments() == null) {
					Bundle bundleParam = new Bundle();
					bundleParam.putSerializable(MSCORELIST,
							MScoreList.values()[position]);
					frament.setArguments(bundleParam);
				}
			}
			return frament;
		}
	}

	/**
	 * 切换标签
	 * 
	 * @param str
	 */
	private void changeTitle(String str) {
		String title = id_score_activity_title.getText().toString().trim();
		if (!str.equals(title)) {
			id_score_activity_title.setText(str);
			if (str.equals("竞彩足球")) {
				mMatchType = "TC_JCZQ";
			} else if (str.equals("老足彩")) {
				mMatchType = "TC_ZC";
			}
//			issueNo = "";
			// 初始化比分筛选
			mPopWindowScoreMatchChoose = new PopWindowScoreMatchChoose(this, 1);
			mPopWindowScoreMatchChoose_like = new PopWindowScoreMatchChoose(this, 1);
			mPopWindowScoreMatchChoose.setOnCallBack(this);
			mPopWindowScoreMatchChoose_like.setOnCallBack(this);
			// 标题修改之后，获取期次信息
			new Thread() {
				@Override
				public void run() {
					getNOMap();
				}
			}.start();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 关闭当前页面
		case R.id.id_scorelist_activity_back:
			mMatchType = "TC_JCZQ";
			isFirstShown_list = true;
			isFirstShown_like = true;
			finish();
			break;
		case R.id.id_score_activity_title:
			//标题标签   = 竞彩足球/老足彩
			mPopupWindowScoreType.showOrhideWindow(id_scorelist_title_layout);
			id_scorelist_choose_title_layout.setVisibility(View.VISIBLE);
			break;
		case R.id.id_scorelist_choose_title_layout:
			//popwindow显示时的黑色背景层
			id_scorelist_choose_title_layout.setVisibility(View.GONE);
			break;
		case R.id.id_scorelist_activity_more:
			//期次的选择框
			if (mPopWindowDateChoose_list != null && type == 0) {
				if (mMatchType.equals("TC_JCZQ")) {
					mPopWindowDateChoose_list.showFootballDialog(position, 1);
				}else if (mMatchType.equals("TC_ZC")) {
					mPopWindowDateChoose_list.showFootballDialog(position, 1);
				}
				
			}
			break;
		case R.id.id_scorelist_activity_choose:
			//联赛的筛选
			if (mPopWindowScoreMatchChoose != null && type ==0) {
				//比赛列表部分
				mPopWindowScoreMatchChoose.showFootballDialog();
			}else if (mPopWindowScoreMatchChoose_like != null && type == 1) {
				//关注部分
				mPopWindowScoreMatchChoose_like.showFootballDialog();
			}
			break;
		case R.id.tv_match_focus:
			//关注
			initRequestNo();
			id_scorelist_activity_more.setVisibility(View.GONE);
			if (App.userInfo != null) {
				if (!tv_match_focus.isSelected()) {
					setCurrentPage_focus();
					type = 1;
				}
			}else {
				Intent intent = new Intent();
				intent.setClass(ScoreListActivity.this, LoginActivity.class);
				intent.putExtra(SOURCE_ACTIVITY, "scorelistactivity");
				startActivity(intent);
			}
			
			break;
		case R.id.tv_match_list:
			//比赛列表
			if (mMatchType.equals("TC_JCZQ")) {
				initRequestNo();
			}
			id_scorelist_activity_more.setVisibility(View.VISIBLE);
			if (!tv_match_list.isSelected()) {
				view_line_red.setAnimation(set_from_right_to_left);
				view_line_red.startAnimation(set_from_right_to_left);
				tv_match_list.setTextColor(getResources().getColor(R.color.color_red));
				tv_match_focus.setTextColor(getResources().getColor(R.color.color_gray_secondary));
				tv_match_focus.setSelected(false);
				tv_match_list.setSelected(true);
				type = 0;
			}
			int type = NetUtil.getNetworkType(ScoreListActivity.this);
			if (type == 0) {
				//没有网
				EmptyFragment.setmErrCode(CException.NET_ERROR + "");
				EmptyFragment.setmType(0);
				if (currentFragment == null) {
					addFragment(emptyFragment);
				}else {
					showFragment(emptyFragment);
				}

				hideLoadingDialog();
			} else {
				//有网了
				if (mMatchType.equals("TC_JCZQ")) {
					showFragment(fragment_match);
				} else {
					showFragment(fragment_match_zc);
				}
			}
			
			
			break;
		default:
			break;
		}
	}

	private void setCurrentPage_focus() {
		view_line_red.setAnimation(set_from_left_to_right);
		view_line_red.startAnimation(set_from_left_to_right);
		tv_match_focus.setTextColor(getResources().getColor(R.color.color_red));
		tv_match_list.setTextColor(getResources().getColor(R.color.color_gray_secondary));
		tv_match_focus.setSelected(true);
		tv_match_list.setSelected(false);
		int type = NetUtil.getNetworkType(ScoreListActivity.this);
		if (type == 0) {
			//没有网
			EmptyFragment.setmErrCode(CException.NET_ERROR + "");
			EmptyFragment.setmType(0);
			if (currentFragment == null) {
				addFragment(emptyFragment);
			}else {
				showFragment(emptyFragment);
			}

			hideLoadingDialog();
		}else{
			showFragment(fragment_like);
		}
		
	}

	/**
	 * 获取期次信息
	 */
	private void getNOMap() {
		
		int type = NetUtil.getNetworkType(ScoreListActivity.this);
		if (type == 0) {
			EmptyFragment.setmErrCode(CException.NET_ERROR + "");
			EmptyFragment.setmType(0);
			if (currentFragment == null) {
				addFragment(emptyFragment);
			}else {
				showFragment(emptyFragment);
			}

			hideLoadingDialog();
		} else {
			// 请求数据
			Controller.getInstance().getLotteryNOs(
					mMatchType.equals("TC_ZC") ? mMatchType : "TC_JCZQ", "9",
					GlobalConstants.NUM_SCORELISTTIME, mBack);
		}
		
	}

	/**
	 * 请求返回处理
	 */
	private CallBack mBack = new CallBack() {
		public void getLotteryNoSuccess(
				final cn.com.cimgroup.bean.LotteryNOs lotteryNOs) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					/** 请求成功 */
					if (lotteryNOs != null
							&& lotteryNOs.getResCode().equals("0")) {
						//重新请求数据 情况原数据
						dateList.clear();
						dateList_zc.clear();
						dateListCopy.clear();
						dateListCopy_zc.clear();
						String nos = lotteryNOs.getIssues();
						String[] noss = nos.split(",");
						if (!TextUtils.isEmpty(lotteryNOs.getCurrIssues())) {
							issueNo = lotteryNOs.getCurrIssues();
						}
						// 判断成功 表示dateList中的值已存在 不需要重新请求
						if (!TextUtils.isEmpty(lotteryNOs.getLotteryNo())) {
							if (lotteryNOs.getLotteryNo().equals("TC_JCZQ")) {
								for (int i = 0; i < noss.length; i++) {
									dateList.add(noss[i]);
								}
								dateListCopy.addAll(dateList);
								//重置IssueNo
								initRequestNo();
								
								fragment_match = ScoreListFragment_Match.newInstance(dateListCopy, MScoreList.LIST);
								fragment_match.setMyListener(ScoreListActivity.this);
								fragment_match.setOnPageChangedListener(ScoreListActivity.this);
								
								fragment_like = ScoreListFragment_Focus.newInstance(dateList, MScoreList.LIKE);
								fragment_like.setOnChangeMatchTimeListener(ScoreListActivity.this);
								fragment_like.onFocusMatchFilterListener(ScoreListActivity.this);
								
								//currentFragment==null 表示第一次加载
								if (currentFragment == null) {
									addFragment(fragment_match);
								} else {
									if (type == 1) {
										showFragment(fragment_like);
									} else if (type == 0) {
										showFragment(fragment_match);
									}
								}
								mPopWindowDateChoose_list.setDatas(dateList);
							}else if(lotteryNOs.getLotteryNo().equals("TC_ZC")){
								for (int i = 0; i < noss.length; i++) {
									dateList_zc.add(noss[i]);
								}
								
								dateListCopy_zc.addAll(dateList_zc);
								
								fragment_match_zc = ScoreListFragment_Match.newInstance(dateListCopy_zc, MScoreList.LIST);
								fragment_match_zc.setMyListener(ScoreListActivity.this);
								fragment_match_zc.setOnPageChangedListener(ScoreListActivity.this);
								
								fragment_like = ScoreListFragment_Focus.newInstance(dateList_zc, MScoreList.LIKE);
								fragment_like.setOnChangeMatchTimeListener(ScoreListActivity.this);
								fragment_like.onFocusMatchFilterListener(ScoreListActivity.this);
								
								//currentFragment==null 表示第一次加载
								if (currentFragment == null) {
									addFragment(fragment_match);
								} else {
									if (type == 1) {
										showFragment(fragment_like);
									}else if (type == 0) {
										showFragment(fragment_match_zc);
									}
								}
								mPopWindowDateChoose_list.setDatas(dateList_zc);
							}
						}
						mNOMap.put(mMatchType, noss);
						
					}else {
						//没有数据的情况 
						if (currentFragment == null) {
							addFragment(emptyFragment);
						}else {
							showFragment(emptyFragment);
						}
					}
				}
			});

		};

		public void getLotteryNoError(String message) {
			Log.e(ScoreListActivity.class.getName(), message);
			unregisterReceiver(myReciver);
			//没有数据的情况 
			if (currentFragment == null) {
				addFragment(emptyFragment);
			}else {
				showFragment(emptyFragment);
			}
		};
	};

	@Override
	public void PopTzList(View v) {
		issueNo = v.getTag().toString().trim();
	}

	@Override
	public void onSelectItemClick(int position) {
		changeTitle(mScoreTypeList.get(position));// 2.3期公开
	}

	/**
	 * 联赛筛选内容发生改变
	 */
	@Override
	public void onChange(List<String> matchNameList) {
		mPopWindowScoreMatchChoose.setDatas(matchNameList);
	}

	@Override
	public void onSelectItemClick(List<String> selectData, boolean flag) {
//		mShowFragment.chooseScoreMatch(selectData);
		if (selectData.size()==0 ) {
			if (!flag) {
				ToastUtil.shortToast(ScoreListActivity.this, "请至少选择一种赛事");
			}else {
				if (type == 0) {
					mPopWindowScoreMatchChoose.hide();
				}else if(type == 1){
					mPopWindowScoreMatchChoose_like.hide();
				}
			}
			
		}else {
			if (type == 0) {
				if (mMatchType.equals("TC_JCZQ")) {
					fragment_match.setPopupWdListener(selectData);
				}else if (mMatchType.equals("TC_ZC")) {
					fragment_match_zc.setPopupWdListener(selectData);
				}
				mPopWindowScoreMatchChoose.hide();
			}else if (type == 1) {
				fragment_like.chooseScoreMatch(selectData);
				mPopWindowScoreMatchChoose_like.hide();
			}
		}
	}
	
	/**
	 * 显示或隐藏视图
	 */
	public void showView() {
		if (isShow) {// 隐藏布局，逐渐减少父布局的padding值
			ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float alpha = animation.getAnimatedFraction()
							* (mMoveLength);
					id_toast_layout.setPadding(0, -(int) alpha, 0, 0);
					id_toast_layout.requestLayout();
				}
			});
			animator.setDuration(800);
			animator.start();
			mHandler.sendEmptyMessageDelayed(HIDE_TOAST_LAYOUT, 800);

		} else {// 显示布局，逐渐增加父布局的padding值
			mHandler.sendEmptyMessageDelayed(HIDE_TOAST, 5000);
			ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float alpha = animation.getAnimatedFraction()
							* (mMoveLength);
					id_toast_layout.setPadding(0, (int) (alpha - mMoveLength),
							0, 0);
					id_toast_layout.requestLayout();
				}
			});
			animator.setDuration(800);
			animator.start();
			id_toast_layout.setVisibility(View.VISIBLE);
		}
		isShow = !isShow;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isShow) {
			showView();
		}
		return true;
	}

	/**
	 * 进球推送测试
	 */
	private void jPushText() {

		try {
			JSONObject object = new JSONObject();
			object.put("matchId", "10701508");
			object.put("hostFullGoals", "5");
			object.put("guestFullGoals", "3");
			object.put("hostHalfGoals", "2");
			object.put("guestHalfGoals", "2");
			object.put("matchCode", "1024");
			object.put("matchName", "Copa Argentina");
			object.put("hostName", "河床");
			object.put("guestName", "罗萨里");
			object.put("time", "83");
			object.put("goalTeam", "2");
			object.put("pushCode", "goal");
			object.put("eventType", "3");
			object.put("status", "1");
			object.put("player", "托西格列里");
			update(null, object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data != null) {
			try {
				String json=data.toString();
				Log.e("qiufeng", "测试进球数据"+json);
				JSONObject object = new JSONObject(json);
				String pushCode = object.getString("pushCode");
				if (pushCode != null) {
					if (pushCode.equals(PushCode.PUSH_GOAL)) {
						// 比分直播--进球推送
						String eventType = object.getString("eventType");
//						Log.e("qiufeng", eventType+"______________________________________________________进球推送");
						if (eventType.equals("1") || eventType.equals("2")
								|| eventType.equals("3")) {
							
//							Log.e("qiufeng",
//									eventType
//											+ "______________________________________________________进球推送："
//											+ json);
							ScoreMatchBean bean = new ScoreMatchBean();
							bean.setMatchId(object.getString("matchId"));
							bean.setHostFullGoals(object
									.optString("hostFullGoals","0"));
							bean.setGuestFullGoals(object
									.optString("guestFullGoals","0"));
							bean.setHostHalfGoals(object
									.optString("hostHalfGoals","0"));
							bean.setGuestHalfGoals(object
									.optString("guestHalfGoals","0"));
							bean.setStatus(object.getString("time"));
							bean.setMatchName(object.getString("matchName"));
							bean.setHostName(object.getString("hostName"));
							bean.setGuestName(object.getString("guestName"));
							bean.setTime(object.getString("time"));
							bean.setStatus(object.getString("status"));
							String goalTeam = object.getString("goalTeam");
//							Log.e("qiufeng", object
//									.getString("guestName")+"||||||||"+object.getString("hostName")+"______________________________________________________进球推送____中");
//							mShowFragment.updateDatas(bean);
							if (mMatchType.equals("TC_JCZQ")) {
								if (type == 0) {
									//列表
									fragment_match.updateDatas(bean);
								}else if (type == 1) {
									//关注
									fragment_like.updateDatas(bean);
								}
							}else if(mMatchType.equals("TC_ZC")){
								if (type == 0) {
									//列表
									fragment_match_zc.updateDatas(bean);
								}else if (type == 1) {
									//关注
									fragment_like.updateDatas(bean);
								}
							}
							Message msg = mHandler.obtainMessage();
							msg.obj = goalTeam != null && goalTeam.equals("1") ? object
									.getString("hostName")
									+ "进1球      比分"
									+ bean.getHostFullGoals()
									+ ":"
									+ bean.getGuestFullGoals()
									: object.getString("guestName")
											+ "进1球      比分"
											+ bean.getHostFullGoals() + ":"
											+ bean.getGuestFullGoals();
							msg.what = GOAL_TOAST;
							mHandler.sendMessage(msg);
						}

					} else if (pushCode.equals(PushCode.PUSH_MATCHSTATUS)) {
						// 比分直播--时时推送
						JSONArray array = object.getJSONArray("status");
						if (null != array && array.length() != 0) {
							List<Map<String, String>> list = new ArrayList<Map<String, String>>();
							Map<String, String> map = null;
							int size = array.length();
							for (int i = 0; i < size; i++) {
								JSONObject o = array.getJSONObject(i);
								String status = o.getString("status");
								if (status.equals("完场")) {
									continue;
								}
								map = new HashMap<String, String>();
								map.put("guestFullGoals",
										o.getString("guestFullGoals"));
								map.put("hostFullGoals",
										o.getString("hostFullGoals"));
								map.put("status", status);
								map.put("matchId", o.getString("matchId"));
								map.put("guestHalfGoals",
										o.getString("guestHalfGoals"));
								map.put("hostHalfGoals",
										o.getString("hostHalfGoals"));
								list.add(map);
							}
							if (mMatchType.equals("TC_JCZQ")) {
								if (type == 0) {
									//列表
									fragment_match.updateDatas(list);
								}else if (type == 1) {
									//关注
									fragment_like.updateDatas(list);
								}
							}else if(mMatchType.equals("TC_ZC")){
								if (type == 0) {
									//列表
									fragment_match_zc.updateDatas(list);
								}else if (type == 1) {
									//关注
									fragment_like.updateDatas(list);
								}
							}
						}
					}
				}

			} catch (JSONException e) {
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mMatchType = "TC_JCZQ";
		isFirstShown_list = true;
		isFirstShown_like = true;
		finish();
	}
	
	/** 添加fragment**/
	private void addFragment(Fragment fragment) {
		ft = fm.beginTransaction();
		ft.add(R.id.id_fragment, fragment);
		currentFragment = fragment;
		ft.commit();
	}
	
	/**
	 * 切换当前fragment
	 * @param fragment
	 */
	private void showFragment(Fragment fragment){
		
		if (currentFragment != fragment) {
			ft = fm.beginTransaction();
			ft.hide(currentFragment);
			currentFragment = fragment;
			if (!fragment.isAdded()) {
				ft.add(R.id.id_fragment, fragment).show(fragment).commit();
			}else {
				ft.show(fragment).commit();
			}
		}
		
	}
	
	/**
	 * 广播，关注（未登录） -> 登陆完成 -> 返回
	 * @author 83920_000
	 *
	 */
	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			flag = 1;
			unregisterReceiver(myReciver);
		}
	}

	/**
	 * 筛选中选中某日期的接口回调
	 */
	@Override
	public void onDateSelected(int position) {
		if (type == 0) {
			//比赛列表处的期次位置信息
			if (mMatchType.equals("TC_JCZQ")) {
				position = dateList.size() - position - 1;
				mPopWindowDateChoose_list.hide();
				fragment_match.updateCurrentFragment_Match(position);
				fragment_match.setCurrentPage(position);
			}else if (mMatchType.equals("TC_ZC")) {
				position = dateList_zc.size() - position - 1;
				mPopWindowDateChoose_list.hide();
				fragment_match_zc.updateCurrentFragment_Match(position);
				fragment_match_zc.setCurrentPage(position);
			}
			
			this.position = position;
		}else if (type == 1) {
			//关注比赛处的期次位置信息
			mPopWindowDateChoose_like.hide();
		}
	}
	
	@Override
	public void onPageChanged(int position) {
		
		mPopWindowScoreMatchChoose = new PopWindowScoreMatchChoose(this, 1);
		mPopWindowScoreMatchChoose_like = new PopWindowScoreMatchChoose(this, 1);
		mPopWindowScoreMatchChoose.setOnCallBack(this);
		mPopWindowScoreMatchChoose_like.setOnCallBack(this);
		
		this.position = position;
	}
	
	/**
	 * 关注页面联赛筛选的内容更新回调
	 */
	@Override
	public void changeMatchList(List<String> matchNameList) {
		mPopWindowScoreMatchChoose_like.setDatas(matchNameList);
	}

	@Override
	public void onChangeMatchTimes(List<String> matchNameList) {
		if (type == 0) {
			mPopWindowScoreMatchChoose.setDatas(matchNameList);
		}else if(type == 1){
			mPopWindowScoreMatchChoose_like.setDatas(matchNameList);
		}
	}
	
}
