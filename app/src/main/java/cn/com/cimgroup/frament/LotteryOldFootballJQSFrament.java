package cn.com.cimgroup.frament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.cimgroup.activity.LotteryOldFootballActivity;
import cn.com.cimgroup.activity.LotteryOldFootballActivity;
import cn.com.cimgroup.adapter.LotteryOldFootBallJQSAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;


/**
 * 足彩 进球数
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryOldFootballJQSFrament extends BaseLotteryBidFrament {

	List<String> spiltTimes = new ArrayList<String>();
	
	List<List<Match>> mMatchs = new ArrayList<List<Match>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 初始化UI
		super.initView(inflater);
		return getContentView();
	}

	@Override
	public void initData() {
		mBaseAdapter = new LotteryOldFootBallJQSAdapter(mFragmentActivity);
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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		// 可见时候调用
		if (getUserVisibleHint()) {
			// 获取已经选择的场次集合，用于更新底部UI
			if (mBaseAdapter != null) {
				((LotteryOldFootballActivity) mFragmentActivity).changeSelectMatchCount2state(mBaseAdapter.getSelectItemIndexs() == null ? 0 : mBaseAdapter.getSelectItemIndexs().size());
			} else {
				((LotteryOldFootballActivity) mFragmentActivity).changeSelectMatchCount2state(0);
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
	@SuppressWarnings("unchecked")
	@Override
	public void onSuperActivityOnClick(int id, Object... data) {
		// TODO Auto-generated method stub
		mBaseAdapter.setSelectItemIndexs(null);
		// 获取已经选择的场次集合，用于更新底部UI
		((LotteryOldFootballActivity) mFragmentActivity).changeSelectMatchCount2state(0);
	}

	private CallBack mCallBack = new CallBack() {

		/**
		 * 老足彩进球数加载成功
		 */
		public void getOldFootBallMatchsJQSSuccess(final FootBallMatch info, final List<LoBoPeriodInfo> infos) {
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
		 * 老足彩进球数获取失败
		 */
		public void getOldFootBallMatchsJQSFailure(final String error) {
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
		// TODO Auto-generated method stub
	}

	@Override
	protected void loadData(int page) {
		// TODO Auto-generated method stub
		((LotteryOldFootballActivity)getActivity()).addData(1);
	}

}
