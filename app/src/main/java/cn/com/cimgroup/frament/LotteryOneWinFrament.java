package cn.com.cimgroup.frament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryBidCartActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.adapter.LotteryOneWinAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.bean.ResMatchAgainstInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 一场制胜
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-10-19
 */
public class LotteryOneWinFrament extends BaseLotteryBidFrament {

	private LotteryOneWinAdapter mAdapter;
	
	List<String> spiltTimes = new ArrayList<String>();
	
	List<List<Match>> mMatchs = new ArrayList<List<Match>>();
	
	private int mlotoId = FootballLotteryConstants.L502;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 初始化UI
		super.initView(inflater);
		return getContentView();
	}

	@Override
	public void initData() {
		mAdapter = new LotteryOneWinAdapter(mFragmentActivity, mSearchType);
		mExpandableListView.setAdapter(mAdapter);
		// 展开二级菜单
		expandGroup();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mController.addCallback(mCallBack);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// 可见时候调用
		if (getUserVisibleHint()) {
			// 获取已经选择的场次集合，用于更新底部UI
			if (mAdapter != null) {
				((LotteryFootballActivity) mFragmentActivity).changeSelectMatchCount2state(mAdapter.getSelectItemIndexs() == null ? 0 : mAdapter.getSelectItemIndexs().size());
			} else {
				((LotteryFootballActivity) mFragmentActivity).changeSelectMatchCount2state(0);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mCallBack);
		spiltTimes = new ArrayList<String>();
		mMatchs = new ArrayList<List<Match>>();
	}

	/**
	 * 响应宿主activity的点击事件
	 */
	@Override
	public void onSuperActivityOnClick(int id, Object... data) {
		switch (id) {

		// 进入购物车
		case R.id.txtView_lotterybid_match_finish:
			int matchCount = mAdapter.getSelectItemIndexs() == null ? 0 : mAdapter.getSelectItemIndexs().size();
			if (matchCount < 1) {
				ToastUtil.shortToast(mFragmentActivity, mFragmentActivity.getResources().getString(R.string.lottery_match_choose2));
				return;
			}
			
			int allCount = 0;
			for (List<Match> object : mMatchs) {
				allCount += object.size();
			}
			
			if (matchCount == allCount) {
				ToastUtil.shortToast(mFragmentActivity, "场次无法匹配，请重新选择");
				return;
			}
			Map<Integer, List<Integer>> selectIndexs = mAdapter.getSelectItemIndexs();
			List<Integer> selectKeys = new ArrayList<Integer>(selectIndexs.keySet());
			Collections.sort(selectKeys);
			List<String> SpiltTimes = mAdapter.getSpiltTimes();
			List<List<Match>> allMatchs = mAdapter.getlotterybidMatchsInfos();
			String upSpiltTime = "";
			int num = 0;
			StringBuilder matchNosb = new StringBuilder();
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < selectKeys.size(); i++) {
				int[] arr = FootballLotteryTools.getMatchPositioin(selectKeys.get(i), allMatchs);
				Match match = mMatchs.get(arr[0]).get(arr[1]);
				String matchSort = match.getMatchSort();
				String SpiltTime = SpiltTimes.get(arr[0]).split(" ")[0];
				if (upSpiltTime.isEmpty()) {
					upSpiltTime = SpiltTime;
					num++;
					matchNosb.append(matchSort);
					if (selectKeys.size() == 1) {
						sb.append(upSpiltTime + "-");
						sb.append(num + "-");
						sb.append(matchNosb.toString() + "@");
					}
				} else {
					if (upSpiltTime.equals(SpiltTime)) {
						num++;
						matchNosb.append("|");
						matchNosb.append(matchSort);
						if (i == (selectKeys.size() - 1)) {
							sb.append(upSpiltTime + "-");
							sb.append(num + "-");
							sb.append(matchNosb.toString() + "@");
						}
					} else {
						sb.append(upSpiltTime + "-");
						sb.append(num + "-");
						sb.append(matchNosb.toString() + "@");
						matchNosb.delete(0, matchNosb.length());
						upSpiltTime = SpiltTime;
						num = 1;
						matchNosb.append(matchSort);
					}
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			
			showLoadingDialog();
			mController.getOneWinSys(GlobalConstants.NUM_ONEWINSYS, sb.toString(), data[0].toString(), mCallBack);
//			selectIndexs.put(selectIndexs.size(), new ArrayList<Integer>());
//			LotteryBidCartActivity.forwardLotteryBidOnlyCartActivity(mFragmentActivity, mAdapter.getSpiltTimes(), mAdapter.getlotterybidMatchsInfos(), mAdapter.getSelectItemIndexs(), mSearchType, mlotoId);
			break;
		// 清空此次选择
		case R.id.imgView_lotterybid_trash:
			mAdapter.setSelectItemIndexs(null);
			// 获取已经选择的场次集合，用于更新底部UI
			((LotteryFootballActivity) mFragmentActivity).changeSelectMatchCount2state(0);
			break;
		// 赛事筛选
		case R.id.btnView_football_match_ok:
			FootBallMatch tempFootballMatch = (FootBallMatch) data[0];
			if (tempFootballMatch != null) {
				spiltTimes.clear();
				mMatchs.clear();
				List<Matchs> matchs = tempFootballMatch.getList();
				for (int i = 0; i < matchs.size(); i++) {
					Matchs match = matchs.get(i);
					spiltTimes.add(match.getSpiltTime());
					mMatchs.add(match.getMatchs());
				}
//				mAdapter.setData(spiltTimes, mMatchs, sps);
				mAdapter.setData(spiltTimes, mMatchs);
			}
			mAdapter.setSelectItemIndexs(null);
			expandGroup();
			break;
			//清空selectIndex
		case R.id.btnView_football_match_clear_selectindex:
			mAdapter.setSelectItemIndexs(null);
		default:
			break;
		}
	}

	private CallBack mCallBack = new CallBack() {
		
		public void getOneWinSysSuccess(final ResMatchAgainstInfo info) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (info.getResCode().equals("0")) {
						if (mSearchType == MatchSearchType.TWIN && mAdapter != null) {
							Map<Integer, List<Integer>> selectIndexs = mAdapter.getSelectItemIndexs();
							if (selectIndexs.size() > 0) {
								List<Integer> keys = new ArrayList<Integer>(selectIndexs.keySet());
								Collections.sort(keys);
								selectIndexs.put((keys.get(keys.size() - 1)) + 1, new ArrayList<Integer>());
								LotteryBidCartActivity.forwardLotteryOneWinCartActivity(mFragmentActivity, mAdapter.getSpiltTimes(), mAdapter.getlotterybidMatchsInfos(), mAdapter.getSelectItemIndexs(), mSearchType, info.getResMatchAgainstInfo(), mlotoId);
							}
						}
					} else {
						ToastUtil.shortToast(mFragmentActivity, info.getResMsg());
					}
				}
			});
		};
		
		public void getOneWinSysFailure(String message) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(mFragmentActivity, "系统匹配场次失败");
				}
			});
		};

		/**
		 * 老足彩进球数加载成功
		 */
		public void getFootBallMatchsOneWinSuccess(final FootBallMatch info, final List<LoBoPeriodInfo> infos) {
			// 将引用传递中得值重新赋值给变量
			if (mFragmentActivity != null) {
				mFragmentActivity.runOnUiThread(new Runnable() {
	
					@Override
					public void run() {
						loadFinish();
						if (info.getResCode().equals("0")) {
							mEmptyView.setVisibility(View.GONE);
							List<Matchs> matchs = info.getList();
							if (matchs != null) {
								spiltTimes.clear();
								mMatchs.clear();
								int count = 0;
								for (int i = 0; i < matchs.size(); i++) {
									Matchs match = matchs.get(i);
									spiltTimes.add(match.getSpiltTime());
									mMatchs.add(match.getMatchs());
									count += match.getMatchs().size();
								}
								
								if (mAdapter != null) {
									if (count <= 1) {
										mEmptyView.setVisibility(View.VISIBLE);
										NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 8);
									} else {
										mAdapter.setData(spiltTimes, mMatchs);
										expandGroup();
									}
								}
							} else {
								mEmptyView.setVisibility(View.VISIBLE);
								NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 8);
							}
						} else {
							mEmptyView.setVisibility(View.VISIBLE);
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 8);
						}
					}
				});
			}
		};

		/**
		 * 老足彩进球数获取失败
		 */
		public void getFootBallMatchsOneWinFailure(final String error) {
			if (mFragmentActivity != null) {
				mFragmentActivity.runOnUiThread(new Runnable() {
	
					@Override
					public void run() {
						loadFinish();
						mEmptyView.setVisibility(View.VISIBLE);
						NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, error, 3);
					}
				});
			}
		};
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mlotoId) {
			if (data != null) {
				mFragmentActivity.setIntent(data);
				mAdapter.setSelectItemIndexs((Map<Integer, List<Integer>>) data.getSerializableExtra(LotteryBidCartActivity.LOTTERYBIDMATCH_SELECT_INDEXS));
				// 获取已经选择的场次集合，用于更新底部UI
				((LotteryFootballActivity) mFragmentActivity).changeSelectMatchCount2state(mAdapter.getSelectItemIndexs() == null ? 0 : mAdapter.getSelectItemIndexs().size());
			}
		}
	}

	@Override
	protected void loadData(int page) {
		((LotteryFootballActivity)getActivity()).addData(5);
	}

}
