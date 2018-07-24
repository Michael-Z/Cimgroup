package cn.com.cimgroup.frament;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryBidCartActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.adapter.LotteryFootBallHHGGAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.com.cimgroup.xutils.XLog;

/**
 * 足彩 混合过关
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年12月29日
 */
public class LotteryFootballHHGGFrament extends BaseLotteryBidFrament {

	/** 混合过关Adapter **/
	private LotteryFootBallHHGGAdapter mAdapter = null;
	
	List<String> spiltTimes = new ArrayList<String>();
	
	List<List<Match>> mMatchs = new ArrayList<List<Match>>();
	
	List<List<LotterySp>> sps = new ArrayList<List<LotterySp>>();

	private int mlotoId = FootballLotteryConstants.L305;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 初始化UI
		super.initView(inflater);
		return getContentView();
	}
	
	@Override
	public void initData() {
		mAdapter = new LotteryFootBallHHGGAdapter(mFragmentActivity, mSearchType);
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
	protected void loadData(int page) {
		((LotteryFootballActivity)getActivity()).addData(4);
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
		sps = new ArrayList<List<LotterySp>>();
	}

	private CallBack mCallBack = new CallBack() {

		/**
		 * 足彩胜负彩加载成功
		 */
		public void getFootBallMatchsHHGGSuccess(final FootBallMatch info) {
			//将引用传递中得值重新赋值给变量
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
								for (int i = 0; i < matchs.size(); i++) {
									Matchs match = matchs.get(i);
									spiltTimes.add(match.getSpiltTime());
									mMatchs.add(match.getMatchs());
									List<LotterySp> tempSps = new ArrayList<LotterySp>();
									for (int j = 0; j < match.getMatchs().size(); j++) {
										List<MatchAgainstSpInfo> spList = match.getMatchs().get(j).getMatchAgainstSpInfoList();
										LotterySp sp = new LotterySp();
										for (MatchAgainstSpInfo info : spList) {
											switch (mSearchType) {
											case TWIN:
												sp = FootballLotteryTools.getLotterySp(info, sp, "2");
												break;
											case SINGLE:
												sp = FootballLotteryTools.getLotterySp(info, sp, "1");
												break;
											default:
												break;
											}
										}
										tempSps.add(sp);
									}
									sps.add(tempSps);
								}
								
								if (mAdapter != null) {
									mAdapter.setData(spiltTimes, mMatchs, sps);
									expandGroup();
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
		 * 足彩胜负彩获取失败
		 */
		public void getFootBallMatchsHHGGError(final String error) {
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

	/**
	 * 响应宿主activity的点击事件
	 */
	@Override
	public void onSuperActivityOnClick(int id, Object... data) {
		switch (id) {
		// 进入购物车
		case R.id.txtView_lotterybid_match_finish:
			int matchCount = mAdapter.getSelectItemIndexs() == null ? 0 : mAdapter.getSelectItemIndexs().size();
			switch (mSearchType) {
			case TWIN:
				if (matchCount < 2) {
					ToastUtil.shortToast(mFragmentActivity, mFragmentActivity.getResources().getString(R.string.lottery_match_choose1));
					return;
				}
				break;
			case SINGLE:
				if (matchCount < 1) {
					ToastUtil.shortToast(mFragmentActivity, mFragmentActivity.getResources().getString(R.string.lottery_match_choose2));
					return;
				}
				break;
			default:
				break;
			}
			LotteryBidCartActivity.forwardLotteryFootballHhggCartActivity(mFragmentActivity, mAdapter.getSpiltTimes(), mAdapter.getlotterybidMatchsInfos(), mAdapter.getSelectItemIndexs(), mSearchType, mlotoId);
			break;
		// 清空此次选择
		case R.id.imgView_lotterybid_trash:
			mAdapter.setSelectItemIndexs(null);
			// 用于更新底部UI
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
				mAdapter.setData(spiltTimes, mMatchs, sps);
			}
			mAdapter.setSelectItemIndexs(null);
			//清空selectIndex
		case R.id.btnView_football_match_clear_selectindex:
			mAdapter.setSelectItemIndexs(null);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 其他页面更改了投注选项回调 li:购物车添加更多比赛的回调
		if (requestCode == mlotoId) {
			if (data != null) {
				Map<Integer, Map<Integer, List<Integer>>> mSelectItemindexs = (Map<Integer, Map<Integer, List<Integer>>>) data.getSerializableExtra(LotteryBidCartActivity.LOTTERYBIDMATCH_SELECT_INDEXS);
				mAdapter.setSelectItemIndexs(mSelectItemindexs);
				// 获取已经选择的场次集合，用于更新底部UI
				((LotteryFootballActivity) mFragmentActivity).changeSelectMatchCount2state(mSelectItemindexs.size());
			}
		}
	}

}
