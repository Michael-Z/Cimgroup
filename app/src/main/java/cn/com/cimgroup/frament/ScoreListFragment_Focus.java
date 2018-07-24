package cn.com.cimgroup.frament;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.activity.ScoreListActivity;
import cn.com.cimgroup.activity.ScoreListActivity.MScoreList;
import cn.com.cimgroup.adapter.ScoreListAdapter;
import cn.com.cimgroup.adapter.ScoreListAdapter.RefreshDataListener;
import cn.com.cimgroup.bean.ScoreList;
import cn.com.cimgroup.bean.ScoreListObj;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.frament.ScoreListFragment.onChangeMatchTimesListener;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;

public class ScoreListFragment_Focus extends BaseLoadFragment implements RefreshDataListener{

	/** 布局填充器**/
//	private LayoutInflater mInflater;
	
	private View mView;
	
	/**下拉刷新控件**/
	private PullToRefreshListView mPullToRefreshListView;
	
	private ScoreListAdapter mAdapter;
	
	private boolean mShouldInitialize = true;
	private boolean isPrepared;
	
	private static final int SEND_REQUEST = 666;
	
	public static final String TITLES = "titles";
	
	/** 期次信息集和**/
	private List<String> mList;
	
	/** 比赛信息 */
	private List<ScoreMatchBean> mMatchList;
	
	/** 比赛筛选信息 */
	private List<ScoreMatchBean> mMatchChooseList;

	private List<String> mMatchNameList;
	
	private static String MSCORELIST = "mscorelist";
	
	private final int NOTIFY_JPUSH_REFRESH = 0;
	
	/** 没有消息显示时的提示 */
	private TextView id_state_toast;
	
	private MScoreList mScoreList;
	
	private Handler mHandler = new Handler(){

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_REQUEST:
				sendRequest();
				break;
			case NOTIFY_JPUSH_REFRESH:
				
				// 刷新UI
				mAdapter.notifyDataSetChanged();
				// 通知父级Activity更改赛事类型
				mChangeMatchTimesListener.onChangeMatchTimes(mMatchNameList);
				
				break;
			default:
				break;
			}
		};
		
	};
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_viewpager_focus, null);
		}else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
			
		}
		if (mShouldInitialize) {
			
			initView();
			addToastView();
			String toastStr = "还没有关注比赛哦~";
			id_state_toast.setText(toastStr);
			mAdapter = new ScoreListAdapter(getActivity(), null,
					ScoreListActivity.mMatchType, "3");
			mPullToRefreshListView.setAdapter(mAdapter);
			initDate();
//			sendRequest();
			isPrepared = true;
			mAdapter.setRefreshDataListener(this);
			
			super.initLoad(mPullToRefreshListView, mAdapter);
		}
		
		return mView;
	}

	private void initDate() {
		
		Bundle bundle = getArguments();
		mList = new ArrayList<String>();
		mList = bundle.getStringArrayList(TITLES);
		mScoreList = (MScoreList) getArguments()
				.getSerializable(MSCORELIST);
		
		mMatchList = new ArrayList<ScoreMatchBean>();
		mMatchChooseList = new ArrayList<ScoreMatchBean>();
		mMatchNameList = new ArrayList<String>();
	}

	/**
	 * 获取所有用户已关注的信息
	 */
	private void sendRequest() {
		
		Controller.getInstance().getScoreList(GlobalConstants.NUM_SCORELIST, App.userInfo.getUserId(), ScoreListActivity.mMatchType,
				"4", "All", "", mCallBack);
	}

	private void initView() {
		
		mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.scorelist_focus);
		
	}
	
	/**用户首次调用接口的时间  目的是控制两次调用的间隔不要太短**/
	private long startTime = 0;
	
	private void getData(){
		
		long endTime = System.currentTimeMillis();
		if (endTime - startTime > 800) {
			//发送请求
			mHandler.sendEmptyMessage(SEND_REQUEST);
			startTime = endTime;
		}else {
			loadFinish();
		}
	}

	@Override
	protected void loadData(int page) {
		getData();
	}

	@Override
	public void loadData(int title, int page) {
		
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		} else {
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			
		}
	}
	
	public static ScoreListFragment_Focus newInstance(ArrayList<String> titles, MScoreList type){
		
		ScoreListFragment_Focus fragment = new ScoreListFragment_Focus();
		
		
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(TITLES, titles);
		bundle.putSerializable(MSCORELIST, type);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	private void addToastView() {
		id_state_toast = new TextView(getActivity());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		id_state_toast.setGravity(Gravity.CENTER);
		id_state_toast.setLayoutParams(lp);
		id_state_toast.setTextColor(getActivity().getResources().getColor(R.color.color_gray_secondary));
	}
	
	private boolean isShowEmptyView = false;
	private CallBack mCallBack = new CallBack() {
		
		@Override
		public void getScoreListSuccess(final ScoreList list) {

			getActivity().runOnUiThread(new Runnable() {

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
							if (mFocusMatchFilter != null) {
								mFocusMatchFilter.changeMatchList(mMatchNameList);
							}
							mAdapter.setDatas(mMatchChooseList);
							mAdapter.setLotteryNo(ScoreListActivity.mMatchType);
						} else {
							mAdapter.setDatas(new ArrayList<ScoreMatchBean>());
							if (mFocusMatchFilter != null) {
								mFocusMatchFilter.changeMatchList(new ArrayList<String>());
							}
						}
						
						if (mAdapter.getCount() == 0) {
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 5);
						}
					} else {
						if (mMatchList == null && mMatchChooseList == null
								&& mMatchNameList == null) {
							mMatchList = new ArrayList<ScoreMatchBean>();
							mMatchChooseList = new ArrayList<ScoreMatchBean>();
							mMatchNameList = new ArrayList<String>();
						}
						mMatchNameList.clear();
						mMatchList.clear();
						mMatchChooseList.clear();
						if (mFocusMatchFilter != null) {
							mFocusMatchFilter.changeMatchList(mMatchNameList);
						}
						NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 5);
					}
					mAdapter.notifyDataSetChanged();

				}
			});
		}

		@Override
		public void getScoreListFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

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
	
	public interface onFocusMatchFilter{
		void changeMatchList(List<String> matchNameList);
	}
	
	private onFocusMatchFilter mFocusMatchFilter;
	
	public void onFocusMatchFilterListener(onFocusMatchFilter changeMatchTimes) {
		this.mFocusMatchFilter = changeMatchTimes;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//刷新页面数据 重新发送请求
		super.initLoad(mPullToRefreshListView, mAdapter);
	}

	/**
	 * 因为使用了hide和show来显示fragment
	 * 切换到的fragment无法自动刷新，而是单纯的替换
	 * 故在这里重新调用刷新方法
	 * 假设AFragment切换到BFragment
	 * 那么先调用A的该方法，后调用B的
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			mAdapter.notifyDataSetChanged();
			super.initLoad(mPullToRefreshListView, mAdapter);
		}
	}
	
	/**
	 * 用户取消关注   自动刷新当前页面
	 */
	@Override
	public void refresh() {
		mAdapter.notifyDataSetChanged();
		super.initLoad(mPullToRefreshListView, mAdapter);
	}
	
	/**
	 * 重新发送请求 -- 获取选中期次的数据
	 * @param position
	 */
	public void sendRequest(int position){
		showLoadingDialog();
		Controller.getInstance().getScoreList(GlobalConstants.NUM_SCORELIST, App.userInfo.getUserId(), ScoreListActivity.mMatchType,
				"4", "All", "", mCallBack);
	}
	
	/**
	 * 通过位置获取当前的期次号
	 * @param position
	 * @return
	 */
	private String getIssueNoByCurrentPosition(int position){
		if (position == -1) {
			return "";
		}
		return mList.get(position);
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
		if (mScoreList == MScoreList.LIKE) {
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
		if (mScoreList == MScoreList.LIKE && list != null && list.size() != 0) {
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
					mHandler.sendEmptyMessage(NOTIFY_JPUSH_REFRESH);
				};
			}.start();

		}
	}
	
	private onChangeMatchTimesListener mChangeMatchTimesListener;

	public void setOnChangeMatchTimeListener(
			onChangeMatchTimesListener changeMatchTimes) {
		mChangeMatchTimesListener = changeMatchTimes;
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
		//为开赛的比赛在跳转详情页时，直接跳转分析的那个子页面
		if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equals("0")) {
			intent.putExtra("tabIndex", "1");
		}
		intent.putExtra("gameNo", ScoreListActivity.mMatchType);
		startActivity(intent);
	}
	
}
