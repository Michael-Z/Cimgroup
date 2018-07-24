package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.TzDetailActivity;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.adapter.CenterListTzAdapter;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.bean.AllBettingList;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 投注记录-全部
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年10月31日
 */
@SuppressLint("InflateParams")
public class TzListFragment extends BaseLoadFragment {
	/** 消息提示文本 */
	private String mToastStr = "";

	private static final String MCENTERRECORDTZ = "mcenterrecordtz";

	// 投注记录adapter
	private CenterListTzAdapter recordTzAdapter;
	// 投注记录类型
	private MCenterRecordTz mCenterRecordTz;

	// 投注记录类型
	// private MCenterRecordTz mCenterRecordTz;

	// 投注记录集合
	private PullToRefreshListView mPullToRefreshListView;

	// private LinearLayout items;

	private boolean mShouldInitialize = true;

	private View mView;

	private static String gameNo = "ALL";

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	private int mPage;
	/** 没有消息时显示 */
	private TextView id_state_toast;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_mcenter_record_tzall,
					null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		initViews();
		if (mShouldInitialize) {
			mCenterRecordTz = (MCenterRecordTz) getArguments().getSerializable(
					MCENTERRECORDTZ);
			mPullToRefreshListView = (PullToRefreshListView) mView
					.findViewById(R.id.tzAlllistView);
			recordTzAdapter = new CenterListTzAdapter(mFragmentActivity,
					mCenterRecordTz);
			id_state_toast.setText("近三个月内无记录");
			mPullToRefreshListView.setAdapter(recordTzAdapter);
//			mPullToRefreshListView.setEmptyView(mView.findViewById(R.id.empty));

			isPrepared = true;
			lazyLoad();

		}

		return mView;
	}

	private void initViews() {
		id_state_toast = new TextView(getActivity());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		id_state_toast.setGravity(Gravity.CENTER);
		id_state_toast.setText("近三个月内无记录");
		id_state_toast.setTextColor(getActivity().getResources().getColor(R.color.color_gray_secondary));
		id_state_toast.setLayoutParams(lp);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	protected void loadData(int page) {
		mPage = page;
		String num = "";
		String onlyWin = "";
		switch (mCenterRecordTz) {
		case ALL:
			num = GlobalConstants.NUM_ALL;
			onlyWin = "";
			break;
		case BUY:
			num = GlobalConstants.NUM_TZDG;
			break;
		case ZHUI:
			num = GlobalConstants.NUM_ZHUI;
			break;
		case WINNING:
			num = GlobalConstants.NUM_ALL;
			onlyWin = "1";
			break;
		default:
			break;
		}
		showLoadingDialog();
		if (App.userInfo != null) {
			Controller.getInstance().getLoBoTzList(gameNo, num,
					App.userInfo.getUserId(), onlyWin, 10 + "", page + "",
					mCallBack);
		}

	}

	private CallBack mCallBack = new CallBack() {

		@Override
		public void getLoBoTzListSuccess(final AllBettingList all) {

			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					loadFinish();
					if (all.getResCode().equals("0")) {
						List<AllBetting> list = all.getList();
						if (isFirstPage()) {
							recordTzAdapter.replaceAll(list);

						} else {
							recordTzAdapter.addAll(list);
						}
						if (list == null || list.size() == 0) {
							// 还原页码
							mPullToRefreshListView.setEmptyView(id_state_toast);
							restorePage();
						}
						if (mPage * 10 > Integer.parseInt(all.getTotal())) {
							mListView.setMode(Mode.PULL_FROM_START);
						} else {
							mListView.setMode(Mode.BOTH);
						}
					} else {
						ToastUtil.shortToast(getActivity(), all.getResMsg());
					}
				}
			});

		}

		@Override
		public void getLoBoTzListFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 还原页码
					restorePage();
					loadFinish();
					ToastUtil.shortToast(mFragmentActivity, error);
				}
			});
			super.onFail(error);

		}
	};
	/**
	 * 每一条的数据及处理
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		AllBetting allBetting = (AllBetting) recordTzAdapter
				.getItem(position - 1);

		Intent intent = new Intent(mFragmentActivity, TzDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", allBetting);
		intent.putExtras(bundle);
		intent.putExtra(MCENTERRECORDTZ, mCenterRecordTz);
		startActivity(intent);
	}

	@Override
	public void loadData(int arg1, int page) {
		mCenterRecordTz = (MCenterRecordTz) getArguments().getSerializable(
				MCENTERRECORDTZ);
		switch (arg1) {
		case GlobalConstants.TAG_TZLIST_ALL:
			gameNo = "ALL";
			break;
		case GlobalConstants.TAG_TZLIST_FOOTBALL:
			gameNo = GlobalConstants.TC_JCZQ;
			break;
		case GlobalConstants.TAG_TZLIST_BASKETBALL:
			gameNo = GlobalConstants.TC_JCLQ;
			break;
		case GlobalConstants.TAG_TZLIST_SFC:
			gameNo = GlobalConstants.TC_SF14;
			break;
		case GlobalConstants.TAG_TZLIST_R9:
			gameNo = GlobalConstants.TC_SF9;
			break;
		case GlobalConstants.TAG_TZLIST_JQS:
			gameNo = GlobalConstants.TC_JQ4;
			break;
		case GlobalConstants.TAG_TZLIST_BQC:
			gameNo = GlobalConstants.TC_BQ6;
			break;
		case GlobalConstants.TAG_TZLIST_DLT:
			gameNo = GlobalConstants.TC_DLT;
			break;
		case GlobalConstants.TAG_TZLIST_P3:
			gameNo = GlobalConstants.TC_PL3;
			break;
		case GlobalConstants.TAG_TZLIST_QXC:
			gameNo = GlobalConstants.TC_QXC;
			break;
		case GlobalConstants.TAG_TZLIST_P5:
			gameNo = GlobalConstants.TC_PL5;
			break;
		case GlobalConstants.TAG_TZLIST_11Y:
			gameNo = GlobalConstants.TC_11Y;
			break;
		case GlobalConstants.TAG_TZLIST_11X5:
			gameNo = GlobalConstants.TC_11X5;
			break;
		default:
			break;
		}
		loadData(page);
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		} else {
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			super.initLoad(mPullToRefreshListView, recordTzAdapter);
		}
	}

	/**
	 * 通过进入个人中心的入口 设置查看内容的类型
	 * 
	 * @param type
	 */
	public void updateType(int type) {
		mCenterRecordTz = (MCenterRecordTz) getArguments().getSerializable(
				MCENTERRECORDTZ);
		switch (type) {
		case GlobalConstants.TAG_TZLIST_ALL:
			gameNo = "ALL";
			break;
		case GlobalConstants.TAG_TZLIST_FOOTBALL:
			gameNo = GlobalConstants.TC_JCZQ;
			break;
		case GlobalConstants.TAG_TZLIST_BASKETBALL:
			gameNo = GlobalConstants.TC_JCLQ;
			break;
		case GlobalConstants.TAG_TZLIST_SFC:
			gameNo = GlobalConstants.TC_SF14;
			break;
		case GlobalConstants.TAG_TZLIST_R9:
			gameNo = GlobalConstants.TC_SF9;
			break;
		case GlobalConstants.TAG_TZLIST_JQS:
			gameNo = GlobalConstants.TC_JQ4;
			break;
		case GlobalConstants.TAG_TZLIST_BQC:
			gameNo = GlobalConstants.TC_BQ6;
			break;
		case GlobalConstants.TAG_TZLIST_DLT:
			gameNo = GlobalConstants.TC_DLT;
			break;
		case GlobalConstants.TAG_TZLIST_P3:
			gameNo = GlobalConstants.TC_PL3;
			break;
		case GlobalConstants.TAG_TZLIST_QXC:
			gameNo = GlobalConstants.TC_QXC;
			break;
		case GlobalConstants.TAG_TZLIST_P5:
			gameNo = GlobalConstants.TC_PL5;
			break;
		case GlobalConstants.TAG_TZLIST_11Y:
			gameNo = GlobalConstants.TC_11Y;
			break;
		case GlobalConstants.TAG_TZLIST_11X5:
			gameNo = GlobalConstants.TC_11X5;
			break;
		default:
			break;
		}
	}
	
	//2016-9-19 修改记录 将removeCallBack放到onStop()中进行
	@Override
	public void onStop() {
		super.onStop();
		Controller.getInstance().removeCallback(mCallBack);
	}
}
