package cn.com.cimgroup.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.ScoreListObj;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.bean.ScoreObj;
import cn.com.cimgroup.constants.PushCode;
import cn.com.cimgroup.frament.ScoreAnalysisFramentSecond;
import cn.com.cimgroup.frament.ScoreAsiaFrament;
import cn.com.cimgroup.frament.ScoreEuropeFrament;
import cn.com.cimgroup.frament.ScoreGameFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.view.PagerSlidingTabStripChangeColor;

/**
 * 比分详情
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年12月8日
 */
@SuppressLint("HandlerLeak")
public class ScoreDetailActivity extends BaseActivity implements
		OnClickListener, Observer {
	/** 要展示的frament集合 MCenterRecordTz 为标识、主要在frament中区分投注记录类型，用于获取相应的数据 **/
	private Map<MScoreDetail, Fragment> mFraments = new HashMap<MScoreDetail, Fragment>();

	private TextView mTitle;

	private ScoreDetailPagerAdapter mPagerAdapter;

	private ViewPager mViewPager;

	/** 当前显示的活动Fragment */
	private Fragment mShowFragment;
	/** 比赛详情 */
	public ScoreMatchBean bean;
	/** 彩种编号 */
	public String gameNo;
	/** 跳转购买彩票 */
	private TextView id_tobuy_layout;

	/** VS 或者显示比分 */
	private TextView textView_score_detail_vs;
	/** 比赛时间 */
	private TextView textView_score_detail_ing_time;
	/** 进行中的时间 */
	private TextView textView_score_detail_begin;
	/**是否来自于足彩对阵信息页面，是：隐藏底部跳转；否：显示底部跳转*/
	private boolean mIsHideJump=false;
	
	private Intent intent;

	/**
	 * 比分详情类型
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年12月8日
	 */
	public enum MScoreDetail {
		// 赛事
		GAME,
		// 分析
		ANALYSIS,
		// 亚赔
		ASIA,
		// 欧赔
		EUROPE,
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_detail);
		intent = getIntent();
		bean = (ScoreMatchBean) intent.getSerializableExtra("bean");
		if (bean == null) {
			String lotteryNo = intent.getStringExtra("lotteryNo");
			String matchId = intent.getStringExtra("matchId");
			String userId = intent.getStringExtra("userId");
//			lotteryNo = "TC_JCZQ";
//			matchId = "10487728";
//			userId = "222";
			showLoadingDialog();
			Controller.getInstance().getScoreDetail(GlobalConstants.NUM_SCOREDETAIL, userId, lotteryNo, matchId, mBack);
		} else {
			initViews();
		}
		// mHandler.sendEmptyMessageDelayed(JPUSH_TEST, 5000);
	}

	/**
	 * 初始化视图组件
	 */
	private void initViews() {
		textView_score_detail_vs = (TextView) findViewById(R.id.textView_score_detail_vs);

		
		mTitle = ((TextView) findViewById(R.id.textView_title_word));
		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);
		mTitle.setText(getResources().getString(R.string.scoredetail_title));


		TextView matchTime = (TextView) findViewById(R.id.textView_score_detail_match_time);
		TextView matchName = (TextView) findViewById(R.id.textView_score_detail_name);
		textView_score_detail_begin = (TextView) findViewById(R.id.textView_score_detail_begin);
		TextView zhu = (TextView) findViewById(R.id.textView_score_detail_zhu);
		TextView ke = (TextView) findViewById(R.id.textView_score_detail_ke);
		textView_score_detail_ing_time = (TextView) findViewById(R.id.textView_score_detail_ing_time);

		gameNo = intent.getStringExtra("gameNo");
		mIsHideJump=intent.getBooleanExtra("isHideJump", false);
		id_tobuy_layout = (TextView) findViewById(R.id.id_tobuy_layout);
		if (!mIsHideJump) {
			id_tobuy_layout.setVisibility(View.VISIBLE);
			id_tobuy_layout.setOnClickListener(this);
		}else {
			id_tobuy_layout.setVisibility(View.GONE);
		}
		matchTime.setText(bean.getMatchTimes());
		matchName.setText(bean.getMatchName());
		zhu.setText(bean.getHostName());
		ke.setText(bean.getGuestName());
		mTitle.setText(bean.getHostName() + " VS " + bean.getGuestName());
		textView_score_detail_vs.setText("VS");
		switch (bean.getStatus()) {
		case "0":
			textView_score_detail_ing_time.setText(bean.getMatchTime() + "开赛");
			break;
		case "1":
		case "11":
		case "12":
			textView_score_detail_begin.setVisibility(View.VISIBLE);
			textView_score_detail_begin.setText(bean.getTime());
			textView_score_detail_ing_time.setText("进行中");
			textView_score_detail_vs.setText(bean.getHostFullGoals() + ":"
					+ bean.getGuestFullGoals());
			textView_score_detail_ing_time.setVisibility(View.GONE);
			break;
		case "2":
			textView_score_detail_ing_time.setText("完场");
			textView_score_detail_vs.setText(bean.getHostFullGoals() + ":"
					+ bean.getGuestFullGoals());
			break;
		case "3":
			textView_score_detail_ing_time.setText("改期");
			break;
		case "4":
			textView_score_detail_ing_time.setText("腰斩");
			break;
		case "5":
			textView_score_detail_ing_time.setText("中场");
			textView_score_detail_vs.setText(bean.getHostFullGoals() + ":"
					+ bean.getGuestFullGoals());
			break;
		case "13":
			textView_score_detail_ing_time.setText("延期");
			break;
		case "14":
			textView_score_detail_ing_time.setText("待定");
			break;
		case "15":

			break;
		case "16":

			break;
		default:
			break;
		}

		// 初始化pager适配器
		mPagerAdapter = new ScoreDetailPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.v4View_score_detail_pager);
		mFraments.put(MScoreDetail.GAME, new ScoreGameFrament());
		mFraments.put(MScoreDetail.ANALYSIS, new ScoreAnalysisFramentSecond());
		mFraments.put(MScoreDetail.ASIA, new ScoreAsiaFrament());
		mFraments.put(MScoreDetail.EUROPE, new ScoreEuropeFrament());
		mPagerAdapter.setFraments(mFraments);

		mViewPager.setAdapter(mPagerAdapter);
		String tabIndex = intent.getStringExtra("tabIndex");
		int index = TextUtils.isEmpty(tabIndex) ? 0 : Integer
				.parseInt(tabIndex);
		PagerSlidingTabStripChangeColor mPagerTab = (PagerSlidingTabStripChangeColor) findViewById(R.id.cvView_score_detail_pagertab);
		mPagerTab.setTextSize(DensityUtils.dip2px(14));
		mPagerTab.setViewPager(mViewPager);
		mViewPager.setCurrentItem(index);
		App.mObservableListener.addObserver(this);
	}
	
	private CallBack mBack = new CallBack() {
		public void getScoreDetailSuccess(final ScoreObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					getScoreMatchInfo(obj.getWatch());
					initViews();
				}
			});
		};
		public void getScoreDetailFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
				}
			});
		};
	};

	public class ScoreDetailPagerAdapter extends FragmentPagerAdapter {
		private static final String MSCOREDETAIL = "mscoredetail";

		private Map<MScoreDetail, Fragment> mFraments = new HashMap<MScoreDetail, Fragment>();

		FragmentManager mFragmentManager;

		public ScoreDetailPagerAdapter(FragmentManager fm) {
			super(fm);
			this.mFragmentManager = fm;
		}

		public void setFraments(Map<MScoreDetail, Fragment> fragments) {
			this.mFraments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			mShowFragment = (Fragment) object;
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			MScoreDetail type = MScoreDetail.values()[position];
			String title = "";
			switch (type) {
			// 赛事
			case GAME:
				title = getResources().getString(R.string.scoredetail_game);
				break;
			// 分析
			case ANALYSIS:
				title = getResources().getString(R.string.scoredetail_analysis);
				break;
			// 亚赔
			case ASIA:
				title = getResources().getString(R.string.scoredetail_asia);
				break;
			// 欧赔
			case EUROPE:
				title = getResources().getString(R.string.scoredetail_europe);
				break;
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
				frament = this.mFraments.get(MScoreDetail.values()[position]);
				if (frament.getArguments() == null) {
					Bundle bundleParam = new Bundle();
					bundleParam.putSerializable(MSCOREDETAIL,
							MScoreDetail.values()[position]);
					frament.setArguments(bundleParam);
				}
			}
			return frament;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 关闭当前页面
		case R.id.textView_title_back:
			mActivity.finish();
			break;
		case R.id.id_tobuy_layout:
			startActivity(LotteryFootballActivity.class);
			break;
		default:
			break;
		}
	}
	
	private void getScoreMatchInfo(ScoreListObj obj) {
		String[] arr = obj.getRecord().split("\\+");
		bean = new ScoreMatchBean();
		bean.setMatchId(arr[0]);
		bean.setMatchTimes(arr[1]);
		bean.setMatchName(arr[2]);
		bean.setHostName(arr[3]);
		bean.setGuestName(arr[4]);
		bean.setMatchTime(arr[5]);
		bean.setRangBallNum(arr[6]);
		String[] balls = arr[7].split("\\:");
		bean.setHostFullGoals(balls[0]);
		bean.setGuestFullGoals(balls[1]);
		String[] halfBaqlls = arr[8].split("\\:");
		bean.setHostHalfGoals(halfBaqlls[0]);
		bean.setGuestHalfGoals(halfBaqlls[1]);
		bean.setRedCardZhu(arr[9]);
		bean.setYellowCardZhu(arr[10]);
		bean.setRedCardKe(arr[11]);
		bean.setYellowCardKe(arr[12]);
		bean.setNumZhu(arr[13]);
		bean.setNumKe(arr[14]);
		bean.setWatchStatus(obj.getWatchStatus());
		bean.setIssueNo(obj.getIssueNo());
		bean.setTime(obj.getTime());
		bean.setStatus(obj.getStatus());
	}

	int i = 0;

	private void pushTest() {
		try {
			JSONObject object = new JSONObject();
			object.put("matchId", "8569806");
			object.put("hostFullGoals", "2");
			object.put("guestFullGoals", "" + (++i));
			object.put("hostHalfGoals", "0");
			object.put("guestHalfGoals", "0");
			object.put("matchCode", "325");
			object.put("matchName", "巴西甲");
			object.put("hostName", "米内罗");
			object.put("guestName", "科林蒂安");
			object.put("time", "83");
			object.put("goalTeam", "1");
			object.put("pushCode", "goal");
			object.put("eventType", "1");
			object.put("player", "卡萨雷斯");
			update(null, object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data != null && mShowFragment instanceof ScoreGameFrament) {
			try {
				JSONObject object = new JSONObject(data.toString());
				String pushCode = object.getString("pushCode");
				if (pushCode.equals(PushCode.PUSH_GOAL)) {
					String matchId = object.getString("matchId");
					if (bean.getMatchId().equals(matchId)) {
						Map<String, String> map = new HashMap<String, String>();
						String eventType = object.getString("eventType");
						map.put("eventType", eventType);
						map.put("player", object.getString("player"));
						map.put("matchId", matchId);
						map.put("goalTeam", object.getString("goalTeam"));
						map.put("hostFullGoals",
								object.optString("hostFullGoals","0"));
						map.put("guestFullGoals",
								object.optString("guestFullGoals","0"));
						map.put("hostHalfGoals",
								object.optString("hostHalfGoals","0"));
						map.put("guestHalfGoals",
								object.optString("guestHalfGoals","0"));
						map.put("matchCode", object.getString("matchCode"));
						map.put("matchName", object.getString("matchName"));
						map.put("hostName", object.getString("hostName"));
						map.put("guestName", object.getString("guestName"));
						map.put("time", object.getString("time"));
						if (eventType.equals("1") || eventType.equals("3")
								|| eventType.equals("2")) {
							pushGoalUpdate(map);
						}
						((ScoreGameFrament) mShowFragment).updateDatas(map);
						map = null;
					}

				} else if (pushCode.equals(PushCode.PUSH_MATCHSTATUS)) {
					// 比分直播--时时推送
					JSONArray array = object.getJSONArray("status");
					if (null != array && array.length() != 0) {
						Map<String, String> map = null;
						int size = array.length();
						for (int i = 0; i < size; i++) {
							JSONObject o = array.getJSONObject(i);
							map = new HashMap<String, String>();
							String matchId = o.getString("matchId");
							if (bean.getMatchId().equals(matchId)) {
								map.put("guestFullGoals",
										o.getString("guestFullGoals"));
								map.put("hostFullGoals",
										o.getString("hostFullGoals"));
								map.put("time", o.getString("status"));
								map.put("guestHalfGoals",
										o.getString("guestHalfGoals"));
								map.put("hostHalfGoals",
										o.getString("hostHalfGoals"));
								Message msg = mHandler.obtainMessage();
								msg.obj = map;
								msg.what = UPDATE_MATCH;
								msg.sendToTarget();
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 推送消息进球后更新页面信息
	 * 
	 * @param map
	 */
	private void pushGoalUpdate(Map<String, String> map) {
		textView_score_detail_vs.setText(map.get("hostFullGoals") + ":"
				+ map.get("guestFullGoals"));
		bean.setGuestFullGoals(map.get("guestFullGoals"));
		bean.setHostFullGoals(map.get("hostFullGoals"));
	}

	/** 更新比赛信息 */
	private final static int UPDATE_MATCH = 0;
	/** 测试推送 */
	private final static int JPUSH_TEST = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_MATCH:
				updateMatch_PUSH(msg.obj);
				break;
			case JPUSH_TEST:
				pushTest();
				mHandler.sendEmptyMessageDelayed(JPUSH_TEST, 10000);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 更新当前UI信息
	 * 
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	private void updateMatch_PUSH(Object obj) {
		Map<String, String> map = (Map<String, String>) obj;
		textView_score_detail_begin.setText(map.get("time"));
		textView_score_detail_vs.setText(map.get("hostHalfGoals") + ":"
				+ map.get("guestFullGoals"));
		bean.setGuestFullGoals(map.get("guestFullGoals"));
		bean.setHostFullGoals(map.get("hostHalfGoals"));
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		App.mObservableListener.deleteObserver(this);
	}
}
