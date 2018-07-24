package cn.com.cimgroup.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.adapter.ScoreAnalysisAdapter;
import cn.com.cimgroup.bean.ScoreDetailAnalysis;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;

/**
 * 比分详情-分析
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年12月9日
 */
public class ScoreAnalysisFrament extends BaseLoadFragment {
	
	private boolean mShouldInitialize = true;

	private View mView;
	
	private PullToRefreshListView mPullToRefreshListView;
	
	private ScoreAnalysisAdapter scoreAnalysisAdapter;
	
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mView == null) {
//			R.layout.frament_score_game
			mView = inflater.inflate(R.layout.fragment_mcenter_record_tzall, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			
			mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.tzAlllistView);
			scoreAnalysisAdapter = new ScoreAnalysisAdapter(mFragmentActivity, ((ScoreDetailActivity)getActivity()).bean);
			mPullToRefreshListView.setAdapter(scoreAnalysisAdapter);
			
			isPrepared = true;
			lazyLoad();
		}
		return mView;
	}

	@Override
	protected void loadData(int page) {
		String matchId = ((ScoreDetailActivity)getActivity()).bean.getMatchId();
		String gameNo = ((ScoreDetailActivity)getActivity()).gameNo;
		Controller.getInstance().getScoreDetailAnalysis(GlobalConstants.NUM_SCOREDETAILANALYSIS, gameNo, matchId, mCallBack);
	}

	@Override
	public void loadData(int title, int page) {

	}
	
	private CallBack mCallBack = new CallBack() {
		public void getScoreDetailAnalysisSuccess(final ScoreDetailAnalysis analysis) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					loadFinish();
					if (analysis.getResCode().equals("0")) {
						List<ScoreDetailAnalysis> list = new ArrayList<ScoreDetailAnalysis>();
						list.add(analysis);
						scoreAnalysisAdapter.replaceAll(list);
						if(list == null || list.size() == 0){
							// 还原页码
							restorePage();
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 4);
						}
					} else {
						NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 4);
					}
				}
			});
		};
		public void getScoreDetailAnalysisFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 还原页码
					restorePage();
					loadFinish();
//					ToastUtil.shortToast(mFragmentActivity, error);
					NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, error, 0);
				}
			});
			super.onFail(error);
		};
	};

	@Override
	protected void lazyLoad() {
		if(!isPrepared || !isVisible) {
			return;
		} else {
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			super.initLoad(mPullToRefreshListView, scoreAnalysisAdapter);
		}
	}
}
