package cn.com.cimgroup.frament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryBidCartActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.activity.LotteryOldFootballActivity;
import cn.com.cimgroup.adapter.LotteryFootBallSPFAdapter;
import cn.com.cimgroup.adapter.LotteryOldFootBallSPFAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 足彩 胜平负
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
public class LotteryOldFootballSPFrament extends BaseLotteryBidFrament {

	// public LotteryOldFootBallSPFAdapter mAdapter = null;

	List<String> spiltTimes = new ArrayList<String>();

	List<List<Match>> mMatchs = new ArrayList<List<Match>>();

	private int mlotoId = 320;

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 初始化UI
		super.initView(inflater);
		return getContentView();
	}

	@Override
	public void initData() {
		mBaseAdapter = new LotteryOldFootBallSPFAdapter(mFragmentActivity,
				mSearchType);
		mExpandableListView.setAdapter(mBaseAdapter);
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
		((LotteryOldFootballActivity) getActivity()).addData(0);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// 可见时候调用
		if (getUserVisibleHint()) {
			// 获取已经选择的场次集合，用于更新底部UI
			if (mBaseAdapter != null) {
				((LotteryOldFootballActivity) mFragmentActivity)
						.changeSelectMatchCount2state(mBaseAdapter
								.getSelectItemIndexs() == null ? 0
								: mBaseAdapter.getSelectItemIndexs().size());
			} else {
				((LotteryOldFootballActivity) mFragmentActivity)
						.changeSelectMatchCount2state(0);
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

	private CallBack mCallBack = new CallBack() {

		/**
		 * 老足彩胜负彩加载成功
		 * 
		 * @Description:
		 * @param lotoId
		 * @param footballMatchs
		 * @param footballSp
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
		public void getOldFootBallMatchsInfoSPFSuccess(
				final FootBallMatch info, final List<LoBoPeriodInfo> infos) {
			// 将引用传递中得值重新赋值给变量
			if (mFragmentActivity != null) {
				mFragmentActivity.runOnUiThread(new Runnable() {
	
					@Override
					public void run() {
						loadFinish();
						if (info.getResCode().equals("0")) {
							mEmptyView.setVisibility(View.GONE);
							if (mBaseAdapter != null) {
								((LotteryOldFootballActivity) mFragmentActivity)
										.changeSelectMatchCount2state(mBaseAdapter
												.getSelectItemIndexs() == null ? 0
												: mBaseAdapter.getSelectItemIndexs()
														.size());
							} else {
								((LotteryOldFootballActivity) mFragmentActivity)
										.changeSelectMatchCount2state(0);
							}
							List<Matchs> matchs = info.getList();
							if (matchs != null) {
								spiltTimes.clear();
								mMatchs.clear();
								for (int i = 0; i < matchs.size(); i++) {
									Matchs match = matchs.get(i);
									spiltTimes.add(match.getSpiltTime());
									mMatchs.add(match.getMatchs());
								}
								
								if (mBaseAdapter != null) {
									mBaseAdapter.setData(spiltTimes, mMatchs, infos);
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
		 * 老足彩胜负彩获取失败
		 * 
		 * @Description:
		 * @param lotoId
		 * @param error
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
		public void getOldFootBallMatchsInfoSPFFailure(final String error) {
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
	@SuppressWarnings("unchecked")
	@Override
	public void onSuperActivityOnClick(int id, Object... data) {
		// TODO Auto-generated method stub
		if (id == R.id.txtView_lotterybid_match_finish) {
			mBaseAdapter.setSelectItemIndexs(null);
			// 获取已经选择的场次集合，用于更新底部UI
			((LotteryOldFootballActivity) mFragmentActivity)
					.changeSelectMatchCount2state(0);
		} else {
			((LotteryOldFootballActivity) mFragmentActivity)
					.changeSelectMatchCount2state(mBaseAdapter
							.getSelectItemIndexs() == null ? 0 : mBaseAdapter
							.getSelectItemIndexs().size());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 其他页面更改了投注选项回调 li:购物车添加更多比赛的回调
		if (requestCode == mlotoId) {
			if (data != null) {
				// mAdapter.setSelectItemIndexs((Map<Integer, Map<Integer,
				// List<Integer>>>)
				// data.getSerializableExtra(LotteryBidCartActivity.LOTTERYBIDMATCH_SELECT_INDEXS));
				// 获取已经选择的场次集合，用于更新底部UI
				// ((LotteryFootballActivity)
				// mFragmentActivity).changeSelectMatchCount2state(mAdapter.getSelectItemIndexs()
				// == null ? 0 : mAdapter.getSelectItemIndexs().size());
			}
		}
	}

}
