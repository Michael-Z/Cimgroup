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
import cn.com.cimgroup.activity.LotteryBasketballActivity;
import cn.com.cimgroup.activity.LotteryBidCartActivity;
import cn.com.cimgroup.adapter.LotteryBasketBallHHGGAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 篮球 混合
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-6-3
 */
public class LotteryBasketballHHGGFrament extends BaseLotteryBidFrament {

	/** 混合过关Adapter **/
	private LotteryBasketBallHHGGAdapter mAdapter = null;
	
	List<String> spiltTimes = new ArrayList<String>();
	
	List<List<Match>> mMatchs = new ArrayList<List<Match>>();
	
	List<List<LotterySp>> sps = new ArrayList<List<LotterySp>>();

	private int mlotoId = FootballLotteryConstants.L310;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 初始化UI
		super.initView(inflater);
		return getContentView();
	}
	
	@Override
	public void initData() {
		mAdapter = new LotteryBasketBallHHGGAdapter(mFragmentActivity, mSearchType);
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
		((LotteryBasketballActivity)getActivity()).addData(3);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// 可见时候调用
		if (getUserVisibleHint()) {
			// 获取已经选择的场次集合，用于更新底部UI
			if (mAdapter != null) {
				((LotteryBasketballActivity) mFragmentActivity).changeSelectMatchCount2state(mAdapter.getSelectItemIndexs() == null ? 0 : mAdapter.getSelectItemIndexs().size());
			} else {
				((LotteryBasketballActivity) mFragmentActivity).changeSelectMatchCount2state(0);
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
		 * 篮球混合加载成功
		 */
		public void getBasketballMatchsHHGGSuccess(final FootBallMatch info) {
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
								}
								if (mAdapter != null) {
									mAdapter.setData(spiltTimes, mMatchs);
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
		 * 篮球混合获取失败
		 */
		public void getBasketballMatchsHHGGError(final String error) {
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
			((LotteryBasketballActivity) mFragmentActivity).changeSelectMatchCount2state(0);
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
				((LotteryBasketballActivity) mFragmentActivity).changeSelectMatchCount2state(mSelectItemindexs.size());
			}
		}
	}

}
