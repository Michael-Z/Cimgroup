package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.activity.ScoreListActivity;
import cn.com.cimgroup.activity.ScoreListActivity.MScoreList;
import cn.com.cimgroup.adapter.ScoreListAdapter;
import cn.com.cimgroup.adapter.ScoreListAdapter.RefreshDataListener;
import cn.com.cimgroup.bean.ScoreList;
import cn.com.cimgroup.bean.ScoreListObj;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;

/**
 * 比分列表fragment
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年12月8日
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class ScoreListFragment extends BaseLoadFragment implements
		RefreshDataListener{

	private MScoreList mScoreList;

	private static String MSCORELIST = "mscorelist";

	// 投注记录adapter
	private ScoreListAdapter mAdapter;

	// 投注记录集合
	private PullToRefreshListView mPullToRefreshListView;

	private boolean mShouldInitialize = true;

	private View mView;

	private String gameNo = "TC_JCZQ";

	private String type;

	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;

	/** 没有消息显示时的提示 */
	private TextView id_state_toast;
	/** 比赛信息 */
	private List<ScoreMatchBean> mMatchList;
	/** 比赛筛选信息 */
	private List<ScoreMatchBean> mMatchChooseList;

	private List<String> mMatchNameList;

	private final int NOTIFY_JPUSH_REFRESH = 0;
	
	private static String TITLE = "title";
	
	private String issueNo;
	
	private static final int GET_SCORELIST = 8888;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Bundle bundle = getArguments();
		issueNo = bundle.getString(TITLE);
		
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
		if (mShouldInitialize) {
			mScoreList = (MScoreList) getArguments()
					.getSerializable(MSCORELIST);
			switch (mScoreList) {
			// 未结束
			case NOOVER:
				type = "1";
				break;
			// 已结束
			case OVER:
				type = "2";
				break;
//			// 关注
			case LIKE:
				type = "3";
				break;
			case LIST:
				//未开赛+已开赛
				type = "0";
				break;
			default:
				break;
			}

			addToastView();
			String toastStr = "";
			if (type.equals("0"))
				toastStr = "暂无比赛内容";
			else if (type.equals("3")) {
				toastStr = "没有关注的比赛";
			}
			id_state_toast.setText(toastStr);
			mMatchList = new ArrayList<ScoreMatchBean>();
			mMatchChooseList = new ArrayList<ScoreMatchBean>();
			mMatchNameList = new ArrayList<String>();
			mPullToRefreshListView = (PullToRefreshListView) mView
					.findViewById(R.id.tzAlllistView);
			mAdapter = new ScoreListAdapter(mFragmentActivity, null,
					ScoreListActivity.mMatchType, type);
//			mPullToRefreshListView.setAdapter(mAdapter);
			// 关注标签显示时，设置取消关注刷新页面的监听
			mAdapter.setRefreshDataListener(this);
			isPrepared = true;
			lazyLoad();
		}
		
		//TODO 为毛请求走了两遍啊
//		sendRequest();
		
		return mView;
	}
	
	private void addToastView() {
		id_state_toast = new TextView(getActivity());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		id_state_toast.setGravity(Gravity.CENTER);
		id_state_toast.setTextColor(getActivity().getResources().getColor(R.color.color_gray_secondary));
		id_state_toast.setLayoutParams(lp);
	}

	@Override
	public void onPause() {
		super.onPause();
		hideLoadingDialog();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mCallBack);
	}

	/** 第一次调用方法的时间 */
	private long startTime = 0;

	/**
	 * pulltorefresh中的接口回调
	 * 加载第一页+加载下一页时调用
	 */
	@Override
	protected void loadData(int page) {
		getDatas();
	}

	/**
	 * 获取数据
	 */
	public void getDatas() {
		long endTime = System.currentTimeMillis();
		if (endTime - startTime > 800) {
			handler.sendEmptyMessage(GET_SCORELIST);
			startTime = endTime;
		}else {
			loadFinish();
		}
	}

	/**
	 * 获取数据
	 */
	public void getDatasFromActivity() {
		showLoadingDialog();
		getDatas();
		// lazyLoad();
	}

//	private boolean isShowEmptyView = false;
	private CallBack mCallBack = new CallBack() {
		@Override
		public void getScoreListSuccess(final ScoreList list) {

			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
//					if (!isShowEmptyView) {
//						mPullToRefreshListView.setEmptyView(id_state_toast);
//						isShowEmptyView = true;
//					}
					loadFinish();
					if (list != null && list.getResCode() != null
							&& list.getResCode().equals("0")) {
						if (list.getList() != null
								&& list.getList().size() != 0
								&& !TextUtils.isEmpty(list.getList().get(0).getRecord())) {
							getScoreMatchInfo(list.getList());
							if (mChangeMatchTimesListener != null) {
								mChangeMatchTimesListener
										.onChangeMatchTimes(mMatchNameList);
							}
							mAdapter.setDatas(mMatchChooseList);
							mAdapter.setLotteryNo(ScoreListActivity.mMatchType);
						} else {
							mAdapter.setDatas(new ArrayList<ScoreMatchBean>());
							if (mChangeMatchTimesListener != null) {
								mChangeMatchTimesListener
										.onChangeMatchTimes(null);
							}
						}
						if (mAdapter.getCount() == 0) {
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 8);
						}
					} else {
						loadFinish();
						if (mMatchList == null && mMatchChooseList == null
								&& mMatchNameList == null) {
							mMatchList = new ArrayList<ScoreMatchBean>();
							mMatchChooseList = new ArrayList<ScoreMatchBean>();
							mMatchNameList = new ArrayList<String>();
						}
						mMatchNameList.clear();
						mMatchList.clear();
						mMatchChooseList.clear();
						if (mChangeMatchTimesListener != null) {
							mChangeMatchTimesListener
									.onChangeMatchTimes(mMatchNameList);
						}
						NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 8);
					}
//					mAdapter.notifyDataSetChanged();

				}
			});
		}

		@Override
		public void getScoreListFailure(final String error) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 还原页码
					restorePage();
					loadFinish();
//					mAdapter.setDatas(new ArrayList<ScoreMatchBean>());
//					mAdapter.notifyDataSetChanged();
//					ToastUtil.shortToast(mFragmentActivity, error);
					NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, error, 0);
				}
			});
			super.onFail(error);

		}
	};

	/**
	 * 解析并获取比赛信息
	 * 
	 * @param mList
	 */
	private void getScoreMatchInfo(List<ScoreListObj> mList) {
		if (mMatchList == null && mMatchChooseList == null
				&& mMatchNameList == null) {
			mMatchList = new ArrayList<ScoreMatchBean>();
			mMatchChooseList = new ArrayList<ScoreMatchBean>();
			mMatchNameList = new ArrayList<String>();
		}
		mMatchNameList.clear();
		mMatchList.clear();
		mMatchChooseList.clear();
		int size = mList.size();
		ScoreMatchBean bean;
		for (int i = 0; i < size; i++) {
			String[] arr = mList.get(i).getRecord().split("\\+");
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
			bean.setWatchStatus(mList.get(i).getWatchStatus());
			bean.setIssueNo(mList.get(i).getIssueNo());
			bean.setTime(mList.get(i).getTime());
			bean.setStatus(mList.get(i).getStatus());
			if (mMatchNameList.size() == 0)
				mMatchNameList.add(bean.getMatchName());
			else {
				int jSize = mMatchNameList.size();
				for (int j = 0; j < jSize; j++) {
					if (!mMatchNameList.contains(arr[2])) {
						mMatchNameList.add(arr[2]);
					}
				}
			}
			mMatchList.add(bean);
			mMatchChooseList.add(bean);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		ScoreMatchBean bean = (ScoreMatchBean) mAdapter.getItem(position - 1);
		Intent intent = new Intent(mFragmentActivity, ScoreDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("bean", bean);
		intent.putExtras(bundle);
		intent.putExtra("gameNo", gameNo);
		// 为开赛的比赛在跳转详情页时，直接跳转分析的那个子页面
		if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equals("0")) {
			intent.putExtra("tabIndex", "1");
		}
		startActivity(intent);
	}

	/**
	 * 方法未调用
	 */
	@Override
	public void loadData(int arg1, int page) {
		switch (arg1) {
		case GlobalConstants.TAG_TZLIST_BASKETBALL:
			gameNo = GlobalConstants.TC_JCLQ;
			break;
		// 投注记录竞彩足球
		case GlobalConstants.TAG_TZLIST_FOOTBALL:
			gameNo = GlobalConstants.TC_JCZQ;
			break;
		// 投注记录胜负彩
		case GlobalConstants.TAG_TZLIST_SFC:
			gameNo = GlobalConstants.TC_SF14;
			break;
		// 投注记录任九场
		case GlobalConstants.TAG_TZLIST_R9:
			gameNo = GlobalConstants.TC_SF9;
			break;
		// 投注记录进球数
		case GlobalConstants.TAG_TZLIST_JQS:
			gameNo = GlobalConstants.TC_JQ4;
			break;
		// 投注记录进球数
		case GlobalConstants.TAG_TZLIST_BQC:
			gameNo = GlobalConstants.TC_BQ6;
			break;
		default:
			break;
		}
		sendRequest();
	}

	public void sendRequest() {
		if (type.equals("3")) {
			//关注的比赛
			if (App.userInfo == null) {
				startActivity(LoginActivity.class);
			} else {
				Controller.getInstance().getScoreList(
						GlobalConstants.NUM_SCORELIST,
						App.userInfo.getUserId(), ScoreListActivity.mMatchType,
						type, "All", issueNo, mCallBack);
			}
		} else {
			Controller.getInstance().getScoreList(
					GlobalConstants.NUM_SCORELIST,
					App.userInfo == null ? "" : App.userInfo.getUserId(),
					ScoreListActivity.mMatchType, type, "All",
					issueNo, mCallBack);
		}
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		} else {
			isPrepared = false;
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			super.initLoad(mPullToRefreshListView, mAdapter);
		}
	}

	private long handlerStart = 0;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			long handlerEnd = System.currentTimeMillis();
			if (msg.what == GET_SCORELIST && handlerEnd - handlerStart > 800) {
				if (type.equals("3")) {
					if (App.userInfo == null) {
						startActivity(LoginActivity.class);
					} else {
						Controller.getInstance().getScoreList(
								GlobalConstants.NUM_SCORELIST,
								App.userInfo.getUserId(),
								ScoreListActivity.mMatchType, type, "All",
								issueNo, mCallBack);
					}
				} else {
					Controller.getInstance().getScoreList(
							GlobalConstants.NUM_SCORELIST,
							App.userInfo == null ? "" : App.userInfo
									.getUserId(), ScoreListActivity.mMatchType,
							type, "All", issueNo, mCallBack);
				}
				handlerStart = handlerEnd;
			} else if (msg.what == NOTIFY_JPUSH_REFRESH) {
				// 刷新UI
				mAdapter.notifyDataSetChanged();
				// 通知父级Activity更改赛事类型
				mChangeMatchTimesListener.onChangeMatchTimes(mMatchNameList);
			}
		}

	};

	/***
	 * 当标签为关注的时候 点击取消关注 刷新数据
	 */
	@Override
	public void refresh() {
		if (App.userInfo != null) {
			lazyLoad();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		lazyLoad();
	}

	private onChangeMatchTimesListener mChangeMatchTimesListener;

	public void setOnChangeMatchTimeListener(
			onChangeMatchTimesListener changeMatchTimes) {
		mChangeMatchTimesListener = changeMatchTimes;
	}

	/**
	 * 监听筛选改变
	 * 
	 * @author 秋风
	 * 
	 */
	public interface onChangeMatchTimesListener {
		void onChangeMatchTimes(List<String> matchNameList);
	}

	/**
	 * 筛选比赛
	 * 
	 * @param matchList
	 */
	public void chooseScoreMatch(List<String> matchList) {
		mMatchChooseList.clear();
		int size = mMatchList.size();
		int sizeName = matchList.size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < sizeName; j++) {
				if (matchList.get(j).contains(mMatchList.get(i).getMatchName())) {
					mMatchChooseList.add(mMatchList.get(i));
				}
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 更新当前比赛信息bean
	 * 
	 * @param bean
	 */
	public void updateDatas(ScoreMatchBean bean) {
//		Log.e("qiufeng","______________________________________________________进球推送____修改信息");
		if (mScoreList == MScoreList.LIST) {
			if (mMatchChooseList != null) {
				int size = mMatchChooseList.size();
				for (int i = 0; i < size; i++) {
					if (mMatchChooseList.get(i).getMatchId()
							.equals(bean.getMatchId())) {
						mMatchChooseList.get(i).setHostFullGoals(
								bean.getHostFullGoals());
						mMatchChooseList.get(i).setGuestFullGoals(
								bean.getGuestFullGoals());
						mMatchChooseList.get(i).setHostHalfGoals(
								bean.getHostHalfGoals());
						mMatchChooseList.get(i).setGuestHalfGoals(
								bean.getGuestHalfGoals());
						mMatchChooseList.get(i).setMatchName(
								bean.getMatchName());
						mMatchChooseList.get(i).setHostName(bean.getHostName());
						mMatchChooseList.get(i).setGuestName(
								bean.getGuestName());
						mMatchChooseList.get(i).setStatus(bean.getStatus());
						mMatchChooseList.get(i).setTime(bean.getTime());
					}
				}
			}
			if (mMatchList != null) {
				int size = mMatchList.size();
				for (int i = 0; i < size; i++) {
					if (mMatchList.get(i).getMatchId()
							.equals(bean.getMatchId())) {
						mMatchList.get(i).setHostFullGoals(
								bean.getHostFullGoals());
						mMatchList.get(i).setGuestFullGoals(
								bean.getGuestFullGoals());
						mMatchList.get(i).setHostHalfGoals(
								bean.getHostHalfGoals());
						mMatchList.get(i).setGuestHalfGoals(
								bean.getGuestHalfGoals());
						mMatchList.get(i).setMatchName(bean.getMatchName());
						mMatchList.get(i).setHostName(bean.getHostName());
						mMatchList.get(i).setGuestName(bean.getGuestName());
						mMatchList.get(i).setStatus(bean.getStatus());
						mMatchList.get(i).setTime(bean.getTime());
					}
				}
			}
			mAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 更新当前比赛信息List<Map<String,String>>
	 * 
	 * @param list
	 */
	public void updateDatas(final List<Map<String, String>> list) {
		if (mScoreList == MScoreList.LIST && list != null && list.size() != 0) {
			new Thread() {
				public void run() {
					if (mMatchList != null) {
						if (mMatchNameList == null) {
							mMatchNameList = new ArrayList<String>();
						} else {
							mMatchNameList.clear();
						}
						int size = list.size();
						for (int i = 0; i < size; i++) {
							for (int j = 0; j < mMatchList.size(); j++) {
								if (mMatchList.get(j).getMatchId()
										.equals(list.get(i).get("matchID"))) {
									// 在数据源中找到了该比赛---替换数据
									mMatchList.get(j).setGuestFullGoals(
											list.get(i).get("guestFullGoals"));
									mMatchList.get(j).setHostFullGoals(
											list.get(i).get("hostFullGoals"));
									mMatchList.get(j).setStatus(
											list.get(i).get("status"));
									mMatchList.get(j).setGuestHalfGoals(
											list.get(i).get("guestHalfGoals"));
									mMatchList.get(j).setHostHalfGoals(
											list.get(i).get("hostHalfGoals"));
									break;
								}
								if (j == mMatchList.size() - 1) {
									// 如果运行到该行，说明数据源中不存在该比赛
									mMatchList.remove(j);
								}
							}
						}
						// 更新父布局Activity顶部标题
						int sizeLise = mMatchList.size();
						for (int i = 0; i < sizeLise; i++) {
							if (mMatchNameList.size() == 0)
								mMatchNameList.add(mMatchList.get(i)
										.getMatchName());
							else {
								int jSize = mMatchNameList.size();
								for (int n = 0; n < jSize; n++) {
									if (!mMatchNameList.contains(mMatchList
											.get(i).getMatchName())) {
										mMatchNameList.add(mMatchList.get(i)
												.getMatchName());
										break;
									}
								}
							}
						}

						// int sizej = mMatchList.size();
						// for (int i = 0; i < sizej; i++) {
						// for (int j = 0; j < list.size(); j++) {
						// if (mMatchList.get(i).getMatchId()
						// .equals(list.get(j).get("matchID"))) {
						// break;
						// }
						// if (j == list.size() - 1) {
						// // 如果运行到该行，说明数据源中不存在该比赛
						// mMatchList.remove(j);
						// }
						// }
						// }
					}
					if (mMatchChooseList != null) {
						int size = list.size();
						for (int i = 0; i < size; i++) {
							for (int j = 0; j < mMatchChooseList.size(); j++) {
								if (mMatchChooseList.get(j).getMatchId()
										.equals(list.get(i).get("matchID"))) {
									// 在数据源中找到了该比赛---替换数据
									mMatchChooseList.get(j).setGuestFullGoals(
											list.get(i).get("guestFullGoals"));
									mMatchChooseList.get(j).setHostFullGoals(
											list.get(i).get("hostFullGoals"));
									mMatchChooseList.get(j).setStatus(
											list.get(i).get("status"));
									mMatchChooseList.get(j).setGuestHalfGoals(
											list.get(i).get("guestHalfGoals"));
									mMatchChooseList.get(j).setHostHalfGoals(
											list.get(i).get("hostHalfGoals"));
									break;
								}
								if (j == mMatchChooseList.size() - 1) {
									// 如果运行到该行，说明数据源中不存在该比赛
									mMatchChooseList.remove(j);
								}
							}

						}
						// int sizej = mMatchChooseList.size();
						// for (int i = 0; i < sizej; i++) {
						// for (int j = 0; j < list.size(); j++) {
						// if (mMatchChooseList.get(i).getMatchId()
						// .equals(list.get(j).get("matchID"))) {
						// break;
						// }
						// if (j == list.size() - 1) {
						// // 如果运行到该行，说明数据源中不存在该比赛
						// mMatchChooseList.remove(j);
						// }
						// }
						// }
					}
					handler.sendEmptyMessage(NOTIFY_JPUSH_REFRESH);
				};
			}.start();

		}
	}
	
	/**
	 * 本fragment的初始化
	 * @param title
	 * @param list
	 * @return
	 */
	public static ScoreListFragment newInstance(String title, MScoreList list){
		
		ScoreListFragment fragment = new ScoreListFragment();
		
		Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		bundle.putSerializable(MSCORELIST, list);
		
		fragment.setArguments(bundle);
		
		return fragment;
	}

	public void updateCurrentIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	
}
