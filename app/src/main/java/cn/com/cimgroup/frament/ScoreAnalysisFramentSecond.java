package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.bean.GuestFutureObj;
import cn.com.cimgroup.bean.GuestNearObj;
import cn.com.cimgroup.bean.GuestRankObj;
import cn.com.cimgroup.bean.HostFutureObj;
import cn.com.cimgroup.bean.HostNearObj;
import cn.com.cimgroup.bean.HostRankObj;
import cn.com.cimgroup.bean.ScoreDetailAnalysis;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.bean.VsObj;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.ViewUtils;

/**
 * 比分详情-分析
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年12月9日
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class ScoreAnalysisFramentSecond extends BaseFrament {

	private boolean mShouldInitialize = true;

	private View mView;

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	/** 包含scrollview 的LinearLayout */
	private LinearLayout id_layout_with_scrollview;

	/** 滚动视图 */
	private ScrollView id_scrollview;

	// /** scrolview包含的linearlayout */
	// private LinearLayout id_scrollview_relativelayout;
	/** 下拉刷新 头部局 */
	private LinearLayout scrollview_refresh_header_layout;
	/** 头部的指示箭头 */
	private ImageView id_header_arrow;
	/** 头部的进度圆圈 */
	private ProgressBar id_header_progressbar;
	/** 上一次的刷新时间 */
	private TextView id_header_last_update_time;
	/** 提示文字 */
	private TextView id_header_toast_title;

	/** 箭头转动动画 */
	private RotateAnimation arrowRoateAnimation;
	/** 箭反翻转动画 */
	private RotateAnimation arrowReverseAnimation;
	/** 头高度 */
	private int headerHeight;
	/** 最后一次调用Move Header的Padding */
	private int lastHeaderPadding;

	private boolean isBack; // 从Release 转到 pull
	private int headerState = DONE;// 头部状态，默认完成
	private static final int RELEASE_To_REFRESH = 0;
	private static final int PULL_To_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int DONE = 3;

	private ScoreMatchBean mBean;
	private String mAatchId;
	private String mGameNo;

	private TextView zhuFuture;
	private TextView keFuture;
	private LinearLayout vsOut;
	private LinearLayout nearOut;
	private LinearLayout futureOut;
	private LinearLayout jifenOut;
	private LinearLayout vs;
	private LinearLayout hostnear;
	private LinearLayout guestnear;
	private LinearLayout hostfuturevs;
	private LinearLayout guestfuturevs;
	private LinearLayout hostrank;
	private LinearLayout guestrank;
	
	private TextView vsText;
	private TextView zhuText;
	private TextView keText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mBean = ((ScoreDetailActivity) getActivity()).bean;
		if (mView == null) {
			mView = inflater.inflate(
					R.layout.fragment_mcenter_record_tzall_second, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {

			isPrepared = true;
		}
		zhuFuture = (TextView) mView
				.findViewById(R.id.textView_score_analysis_future_zhu);
		keFuture = (TextView) mView
				.findViewById(R.id.textView_score_analysis_future_ke);

		mAatchId = mBean.getMatchId();
		mGameNo = ((ScoreDetailActivity) getActivity()).gameNo;
		initHeaderLayout(LayoutInflater.from(getActivity()));
		setScrollViewHeaderEvent();

		initView();
		initDatas();

		return mView;
	}

	private void initDatas() {

	}

	private void initView() {
		keText = (TextView) mView
				.findViewById(R.id.textView_score_analysis_ke_text);
		zhuText = (TextView) mView
				.findViewById(R.id.textView_score_analysis_zhu_text);
		vsText = (TextView) mView
				.findViewById(R.id.textView_score_analysis_vs_text);
		vsOut = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_out_vs);
		nearOut = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_out_near);
		futureOut = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_out_future);
		jifenOut = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_out_jifen);

		vs = (LinearLayout) mView.findViewById(R.id.layout_score_analysis_vs);
		hostnear = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_hostnear);
		guestnear = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_guestnear);
		hostfuturevs = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_hostfuturevs);
		guestfuturevs = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_guestfuturevs);
		hostrank = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_hostrank);
		guestrank = (LinearLayout) mView
				.findViewById(R.id.layout_score_analysis_guestrank);

		zhuFuture.setText(mBean.getHostName());
		keFuture.setText(mBean.getGuestName());

	}

	/**
	 * 初始化下拉刷新头布局
	 * 
	 * @param inflater
	 */
	private void initHeaderLayout(LayoutInflater inflater) {

		// id_scrollview_relativelayout = (LinearLayout)
		// findViewById(R.id.id_scrollview_relativelayout);
		id_scrollview = (ScrollView) mView.findViewById(R.id.id_scrollview);
		id_layout_with_scrollview = (LinearLayout) mView
				.findViewById(R.id.id_layout_with_scrollview);

		scrollview_refresh_header_layout = (LinearLayout) inflater.inflate(
				R.layout.scrollview_refresh_header_layout, null);

		ViewUtils.measureView(scrollview_refresh_header_layout);
		ViewUtils.measureView(scrollview_refresh_header_layout);

		headerHeight = scrollview_refresh_header_layout.getMeasuredHeight();

		lastHeaderPadding = (-1 * headerHeight); // 最后一次调用Move Header的Padding
		scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding, 0, 0);
		scrollview_refresh_header_layout.invalidate();

		id_header_progressbar = (ProgressBar) scrollview_refresh_header_layout
				.findViewById(R.id.id_header_progressbar);
		id_header_progressbar.setVisibility(View.VISIBLE);// 显示旋转dialog
		id_header_arrow = (ImageView) scrollview_refresh_header_layout
				.findViewById(R.id.id_header_arrow);
		id_header_arrow.setVisibility(View.GONE);// 隐藏箭头
		id_header_arrow.setMinimumHeight((int) (30 * getResources()
				.getDisplayMetrics().density + 0.5f));
		id_header_arrow.setMinimumWidth((int) (30 * getResources()
				.getDisplayMetrics().density + 0.5f));
		id_header_toast_title = (TextView) scrollview_refresh_header_layout
				.findViewById(R.id.id_header_toast_title);
		id_header_last_update_time = (TextView) scrollview_refresh_header_layout
				.findViewById(R.id.id_header_last_update_time);

		id_layout_with_scrollview.addView(scrollview_refresh_header_layout, 0);

		// 箭头转动动画
		arrowRoateAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowRoateAnimation.setInterpolator(new LinearInterpolator());
		arrowRoateAnimation.setDuration(200); // 动画持续时间
		arrowRoateAnimation.setFillAfter(true); // 动画结束后保持动画
		// 箭头反转动画
		arrowReverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowReverseAnimation.setInterpolator(new LinearInterpolator());
		arrowReverseAnimation.setDuration(200);
		arrowReverseAnimation.setFillAfter(true);

	}

	/**
	 * 为滚动视图添加下拉刷新事件
	 */
	private void setScrollViewHeaderEvent() {
		id_scrollview.setOnTouchListener(new OnTouchListener() {
			int beginY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 加上下滑阻力与实际滑动距离的差（大概值）
					// beginY = (int) ((int) event.getY() +
					// id_main_fragment_move_scrollview.getScrollY() * 1.5);
					break;
				case MotionEvent.ACTION_MOVE:
					// 由于不知道 什么原因 MotionEvent.Action_DOWN不执行，用此方法来为beginY赋值
					if (beginY == 0) {
						// beginY = (int) ((int) event.getY() +
						// id_main_fragment_move_scrollview.getScrollY() * 1.5);
						beginY = (int) event.getY();
					}
					// sc.getScrollY == 0 scrollview 滑动到头了
					// lastHeaderPadding > (-1*headerHeight) 表示header还没完全隐藏起来时
					// headerState != REFRESHING 当正在刷新时
					int interval = (int) (event.getY() - beginY);
					if ((v.getScrollY() == 0) && headerState != REFRESHING) {
						// 拿到滑动的Y轴距离
						// 是向下滑动而不是向上滑动
						if (interval > 0) {
							interval = interval / 2;// 下滑阻力
							lastHeaderPadding = interval + (-1 * headerHeight);
							scrollview_refresh_header_layout.setPadding(0,
									lastHeaderPadding, 0, 0);
							if (lastHeaderPadding > 0) {
								// txView.setText("我要刷新咯");
								headerState = RELEASE_To_REFRESH;
								// 是否已经更新了UI
								if (!isBack) {
									isBack = true; // 到了Release状态，如果往回滑动到了pull则启动动画
									changeHeaderViewByState();
								}
							} else {
								headerState = PULL_To_REFRESH;
								changeHeaderViewByState();
							}
						}
					}
					break;

				case MotionEvent.ACTION_UP:
					beginY = 0;
					if (headerState != REFRESHING) {
						switch (headerState) {
						case DONE:
							// 什么也不干
							break;
						case PULL_To_REFRESH:
							headerState = DONE;
							lastHeaderPadding = -1 * headerHeight;
							scrollview_refresh_header_layout.setPadding(0,
									lastHeaderPadding, 0, 0);
							changeHeaderViewByState();
							break;
						case RELEASE_To_REFRESH:
							isBack = false; // 准备开始刷新，此时将不会往回滑动
							headerState = REFRESHING;
							changeHeaderViewByState();
							mHandler.sendEmptyMessage(0);
							break;
						}

					}
					break;
				}
				// 如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件
				if ((lastHeaderPadding > (-1 * headerHeight) && headerState != REFRESHING)) {
					return true;
				} else {

					return false;
				}
			}
		});

	}

	/***
	 * 头部状态改变时
	 */
	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				// id_header_arrow.startAnimation(arrowReverseAnimation);
				id_header_toast_title.setText("下拉刷新");
			}
			id_header_toast_title.setText("下拉刷新");
			break;
		case RELEASE_To_REFRESH:
			// id_header_arrow.setVisibility(View.VISIBLE);
			// id_header_progressbar.setVisibility(View.GONE);
			id_header_toast_title.setVisibility(View.VISIBLE);
			// id_header_last_update_time.setVisibility(View.VISIBLE);
			// id_header_arrow.clearAnimation();
			// id_header_arrow.startAnimation(arrowRoateAnimation);
			id_header_toast_title.setText("松开刷新");
			break;
		case REFRESHING:
			lastHeaderPadding = 0;
			scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding,
					0, 0);
			scrollview_refresh_header_layout.invalidate();
			// 刷新完成后，默认将焦点附加给ViewPager 强迫ScrollView滚动到顶端
			// id_header_progressbar.setVisibility(View.VISIBLE);
			// id_header_arrow.clearAnimation();
			// id_header_arrow.setVisibility(View.INVISIBLE);
			id_header_toast_title.setText("正在刷新...");
			// id_header_last_update_time.setVisibility(View.VISIBLE);
			break;
		case DONE:
			lastHeaderPadding = -1 * headerHeight;
			scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding,
					0, 0);
			scrollview_refresh_header_layout.invalidate();
			// id_header_progressbar.setVisibility(View.GONE);
			// id_header_arrow.clearAnimation();
			// id_header_arrow.setVisibility(View.VISIBLE);
			id_header_toast_title.setText("下拉刷新");
			// id_header_last_update_time.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			onResume();
		};
	};

	/**
	 * 刷新完成后
	 */
	@SuppressWarnings("deprecation")
	public void onRefreshComplete() {
		headerState = DONE;
		// id_header_last_update_time.setText("最近更新:"
		// + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	@Override
	public void onResume() {
		super.onResume();
		showLoadingDialog();
		Controller.getInstance().getScoreDetailAnalysis(
				GlobalConstants.NUM_SCOREDETAILANALYSIS, mGameNo, mAatchId,
				mCallBack);
	}

	private CallBack mCallBack = new CallBack() {
		public void getScoreDetailAnalysisSuccess(
				final ScoreDetailAnalysis analysis) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					if (analysis.getResCode().equals("0")) {
						if (analysis != null) {
							hideLoadingDialog();
							onRefreshComplete();
							loadDatas(analysis);
						}
					}
				}
			});
		};

		public void getScoreDetailAnalysisFailure(String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
				}
			});
			super.onFail(error);
		};
	};

	/**
	 * 加载界面
	 * 
	 * @param analysis
	 */
	private void loadDatas(ScoreDetailAnalysis analysis) {
		vs.removeAllViews();
		hostnear.removeAllViews();
		guestnear.removeAllViews();
		hostfuturevs.removeAllViews();
		guestfuturevs.removeAllViews();
		hostrank.removeAllViews();
		guestrank.removeAllViews();

		List<VsObj> vsList = analysis.getVsList();
		List<HostNearObj> hostNearList = analysis.getHostNearList();
		List<GuestNearObj> guestNearList = analysis.getGuestNearList();
		List<HostFutureObj> hostFutureVsList = analysis.getHostFutureVsList();
		List<GuestFutureObj> guestFutureVsList = analysis
				.getGuestFutureVsList();
		List<HostRankObj> hostRankList = analysis.getHostRankList();
		List<GuestRankObj> guestRankList = analysis.getGuestRankList();

		if (vsList.size() == 0) {
			vsOut.setVisibility(View.GONE);
		} else {
			vsOut.setVisibility(View.VISIBLE);
		}

		if (hostNearList.size() == 0) {
			nearOut.setVisibility(View.GONE);
		} else {
			nearOut.setVisibility(View.VISIBLE);
		}

		if (hostFutureVsList.size() == 0) {
			futureOut.setVisibility(View.GONE);
		} else {
			futureOut.setVisibility(View.VISIBLE);
		}

		if (hostRankList.size() == 0) {
			jifenOut.setVisibility(View.GONE);
		} else {
			jifenOut.setVisibility(View.VISIBLE);
		}

		String vsResult = analysis.getBothSideResult();
		String guestNResult = analysis.getGuestNearResult();
		String hostNResult = analysis.getHostNearResult();

		if (vsResult.indexOf("_") != -1) {
			String[] vsNArr = vsResult.split("\\_");
			vsText.setText(Html.fromHtml(getActivity().getResources()
					.getString(R.string.scoredetail_analysis_string, vsNArr[0],
							vsNArr[1], vsNArr[2], vsNArr[3], vsNArr[4],
							vsNArr[5])));
		}
		if (guestNResult.indexOf("_") != -1) {
			String[] guestNArr = guestNResult.split("\\_");
			keText.setText(Html.fromHtml(getActivity().getResources()
					.getString(R.string.scoredetail_analysis_string,
							guestNArr[0], guestNArr[1], guestNArr[2],
							guestNArr[3], guestNArr[4], guestNArr[5])));
		}
		if (hostNResult.indexOf("_") != -1) {
			String[] hostNArr = hostNResult.split("\\_");
			zhuText.setText(Html.fromHtml(getActivity().getResources()
					.getString(R.string.scoredetail_analysis_string,
							hostNArr[0], hostNArr[1], hostNArr[2], hostNArr[3],
							hostNArr[4], hostNArr[5])));
		}

		for (int i = 0; i < vsList.size(); i++) {
			VsObj vsObj = vsList.get(i);
			LinearLayout view = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis1_item1, null);
			AnalysisItem1 item = new AnalysisItem1(view);

			item.matchName.setText(vsObj.getMatchName());
			String duizhen = vsObj.getDuizhen();
			String[] duiArr = duizhen.split("\\_");
			item.zhu.setText(duiArr[0]);
			item.bifen.setText(duiArr[1]);
			item.ke.setText(duiArr[2]);
			switch (vsObj.getMatchResult()) {
			case "3":
				item.result.setText("胜");
				item.result.setTextColor(getActivity().getResources().getColor(
						R.color.color_red));
				break;
			case "1":
				item.result.setText("平");
				item.result.setTextColor(getActivity().getResources().getColor(
						R.color.color_green_warm));
				break;
			case "0":
				item.result.setText("负");
				item.result.setTextColor(getActivity().getResources().getColor(
						R.color.hall_blue));
				break;
			default:
				break;
			}

			item.time.setText(vsObj.getMatchTime());

			vs.addView(view);
		}

		for (int i = 0; i < hostNearList.size(); i++) {
			HostNearObj hostNearObj = hostNearList.get(i);
			LinearLayout view1 = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis1_item1, null);
			AnalysisItem1 item1 = new AnalysisItem1(view1);

			item1.matchName.setText(hostNearObj.getHostNearMatchName());
			String duizhen = hostNearObj.getHostNearDuiZhen();
			String[] duiArr = duizhen.split("\\_");
			item1.zhu.setText(duiArr[0]);
			item1.bifen.setText(duiArr[1]);
			item1.ke.setText(duiArr[2]);

			switch (hostNearObj.getHostNearMatchResult()) {
			case "3":
				item1.result.setText("胜");
				item1.result.setTextColor(getActivity().getResources()
						.getColor(R.color.color_red));
				break;
			case "1":
				item1.result.setText("平");
				item1.result.setTextColor(getActivity().getResources()
						.getColor(R.color.color_green_warm));
				break;
			case "0":
				item1.result.setText("负");
				item1.result.setTextColor(getActivity().getResources()
						.getColor(R.color.hall_blue));
				break;
			default:
				break;
			}
			item1.time.setText(hostNearObj.getHostNearMatchTime());

			hostnear.addView(view1);
		}

		for (int i = 0; i < guestNearList.size(); i++) {
			GuestNearObj guestNearObj = guestNearList.get(i);
			LinearLayout view2 = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis1_item1, null);
			AnalysisItem1 item2 = new AnalysisItem1(view2);

			item2.matchName.setText(guestNearObj.getGuestNearMatchName());
			String duizhen = guestNearObj.getGuestNearDuiZhen();
			String[] duiArr = duizhen.split("\\_");
			item2.zhu.setText(duiArr[0]);
			item2.bifen.setText(duiArr[1]);
			item2.ke.setText(duiArr[2]);
			switch (guestNearObj.getGuestNearMatchResult()) {
			case "3":
				item2.result.setText("胜");
				item2.result.setTextColor(getActivity().getResources()
						.getColor(R.color.color_red));
				break;
			case "1":
				item2.result.setText("平");
				item2.result.setTextColor(getActivity().getResources()
						.getColor(R.color.color_green_warm));
				break;
			case "0":
				item2.result.setText("负");
				item2.result.setTextColor(getActivity().getResources()
						.getColor(R.color.hall_blue));
				break;
			default:
				break;
			}
			item2.time.setText(guestNearObj.getGuestNearMatchTime());

			guestnear.addView(view2);
		}

		for (int i = 0; i < hostFutureVsList.size(); i++) {
			HostFutureObj hostFutureObj = hostFutureVsList.get(i);
			LinearLayout view3 = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis1_item2, null);
			AnalysisItem2 item3 = new AnalysisItem2(view3);

			item3.matchName.setText(hostFutureObj.getHostFutureName());
			String duizhen = hostFutureObj.getHostFutureDZ();
			String[] duiArr = duizhen.split("\\_");
			item3.zhu.setText(duiArr[0]);
			item3.ke.setText(duiArr[1]);
			item3.result.setText(hostFutureObj.getHostFutureJG());
			item3.time.setText(hostFutureObj.getHostFutureTime());

			hostfuturevs.addView(view3);
		}

		for (int i = 0; i < guestFutureVsList.size(); i++) {
			GuestFutureObj guestFutureObj = guestFutureVsList.get(i);
			LinearLayout view4 = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis1_item2, null);
			AnalysisItem2 item4 = new AnalysisItem2(view4);

			item4.matchName.setText(guestFutureObj.getGuestFutureName());
			String duizhen = guestFutureObj.getGuestFutureDZ();
			String[] duiArr = duizhen.split("\\_");
			item4.zhu.setText(duiArr[0]);
			item4.ke.setText(duiArr[1]);
			item4.result.setText(guestFutureObj.getGuestFutureJG());
			item4.time.setText(guestFutureObj.getGuestFutureTime());

			guestfuturevs.addView(view4);
		}

		for (int i = 0; i < hostRankList.size(); i++) {
			HostRankObj hostRankObj = hostRankList.get(i);
			LinearLayout view5 = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis2_item1, null);
			AnalysisItem3 item5 = new AnalysisItem3(view5);

			hostRankObj.getHostRankResult();
			String guestAll = hostRankObj.getHostAllResult();
			String guestG = hostRankObj.getHostGResult();
			String guestH = hostRankObj.getHostHResult();

			String[] allArr = guestAll.split("\\_");
			item5.name.setText("总成绩");
			item5.match.setText(allArr[0]);
			item5.sheng.setText(allArr[1]);
			item5.ping.setText(allArr[2]);
			item5.fu.setText(allArr[3]);
			item5.jin.setText(allArr[4]);
			item5.shi.setText(allArr[5]);
			item5.jifen.setText(allArr[6]);
			item5.pai.setText(allArr[7]);

			hostrank.addView(view5);

			String[] hArr = guestAll.split("\\_");
			item5.name.setText("主 场");
			item5.match.setText(hArr[0]);
			item5.sheng.setText(hArr[1]);
			item5.ping.setText(hArr[2]);
			item5.fu.setText(hArr[3]);
			item5.jin.setText(hArr[4]);
			item5.shi.setText(hArr[5]);
			item5.jifen.setText(hArr[6]);
			item5.pai.setText(hArr[7]);

			hostrank.addView(view5);

			String[] gArr = guestAll.split("\\_");
			item5.name.setText("客 场");
			item5.match.setText(gArr[0]);
			item5.sheng.setText(gArr[1]);
			item5.ping.setText(gArr[2]);
			item5.fu.setText(gArr[3]);
			item5.jin.setText(gArr[4]);
			item5.shi.setText(gArr[5]);
			item5.jifen.setText(gArr[6]);
			item5.pai.setText(gArr[7]);

			hostrank.addView(view5);

		}

		for (int i = 0; i < guestRankList.size(); i++) {
			GuestRankObj guestRankObj = guestRankList.get(i);
			LinearLayout view6 = (LinearLayout) View.inflate(getActivity(),
					R.layout.item_score_analysis2_item1, null);
			AnalysisItem3 item6 = new AnalysisItem3(view6);

			guestRankObj.getGuestRankResult();
			String guestAll = guestRankObj.getGuestAllResult();
			String guestG = guestRankObj.getGuestGResult();
			String guestH = guestRankObj.getGuestHResult();

			String[] allArr = guestAll.split("\\_");
			item6.name.setText("总成绩");
			item6.match.setText(allArr[0]);
			item6.sheng.setText(allArr[1]);
			item6.ping.setText(allArr[2]);
			item6.fu.setText(allArr[3]);
			item6.jin.setText(allArr[4]);
			item6.shi.setText(allArr[5]);
			item6.jifen.setText(allArr[6]);
			item6.pai.setText(allArr[7]);

			guestrank.addView(view6);

			String[] hArr = guestAll.split("\\_");
			item6.name.setText("主 场");
			item6.match.setText(hArr[0]);
			item6.sheng.setText(hArr[1]);
			item6.ping.setText(hArr[2]);
			item6.fu.setText(hArr[3]);
			item6.jin.setText(hArr[4]);
			item6.shi.setText(hArr[5]);
			item6.jifen.setText(hArr[6]);
			item6.pai.setText(hArr[7]);

			guestrank.addView(view6);

			String[] gArr = guestAll.split("\\_");
			item6.name.setText("客 场");
			item6.match.setText(gArr[0]);
			item6.sheng.setText(gArr[1]);
			item6.ping.setText(gArr[2]);
			item6.fu.setText(gArr[3]);
			item6.jin.setText(gArr[4]);
			item6.shi.setText(gArr[5]);
			item6.jifen.setText(gArr[6]);
			item6.pai.setText(gArr[7]);

			guestrank.addView(view6);

		}
	}

	public class AnalysisItem1 {

		private TextView matchName;

		private TextView time;

		private TextView zhu;

		private TextView ke;

		private TextView bifen;

		private TextView result;

		public AnalysisItem1(View view) {
			matchName = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item1_matchname);
			time = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item1_time);
			zhu = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item1_zhu);
			ke = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item1_ke);
			bifen = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item1_bifen);
			result = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item1_result);
		}
	}

	public class AnalysisItem2 {

		private TextView matchName;

		private TextView time;

		private TextView zhu;

		private TextView ke;

		private TextView result;

		public AnalysisItem2(View view) {
			matchName = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item2_matchname);
			time = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item2_time);
			zhu = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item2_zhu);
			ke = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item2_ke);
			result = (TextView) view
					.findViewById(R.id.textView_score_analysis1_item2_result);
		}
	}

	public class AnalysisItem3 {

		private TextView name;

		private TextView match;

		private TextView sheng;

		private TextView ping;

		private TextView fu;

		private TextView jin;

		private TextView shi;

		private TextView jifen;

		private TextView pai;

		public AnalysisItem3(View view) {
			name = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_name);
			match = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_match);
			sheng = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_s);
			ping = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_p);
			fu = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_f);
			jin = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_jin);
			shi = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_shi);
			jifen = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_jifen);
			pai = (TextView) view
					.findViewById(R.id.textView_score_analysis2_item1_pai);

		}
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}

}
