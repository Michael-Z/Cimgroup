package cn.com.cimgroup.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.ScoreCompanyListAdapter;
import cn.com.cimgroup.adapter.ScoreCompanyOddsAdapter;
import cn.com.cimgroup.bean.ScoreCompanyOdds;
import cn.com.cimgroup.logic.CallBack;

public class ScoreCompanyOddsFrament extends BaseLoadFragment {

	private boolean mShouldInitialize = true;

	private View mView;
	
	private PullToRefreshListView mPullToRefreshListView;
	
	private ScoreCompanyListAdapter listAdapter;
	
	private ListView listView;
	
	private ScoreCompanyOddsAdapter scoreCompanyOddsAdapter;
	
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
//			R.layout.frament_score_game
			mView = inflater.inflate(R.layout.frament_score_company_odds, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			
			mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.pullrefreshView_score_company_odds);
			scoreCompanyOddsAdapter = new ScoreCompanyOddsAdapter(mFragmentActivity, null);
			mPullToRefreshListView.setAdapter(scoreCompanyOddsAdapter);
			
			listView = (ListView) mView.findViewById(R.id.listView_score_company_odds);
			listAdapter = new ScoreCompanyListAdapter(mFragmentActivity, null);
			listView.setAdapter(listAdapter);
			
			isPrepared = true;
			lazyLoad();
		}
		return mView;
	}

	@Override
	protected void loadData(int page) {
//		String matchId = ((ScoreDetailActivity)getActivity()).bean.getMatchId();
//		String gameNo = ((ScoreDetailActivity)getActivity()).gameNo;
//		String companyId = ((ScoreDetailActivity)getActivity()).companyId;
//		String companyType = ((ScoreDetailActivity)getActivity()).companyType;
//		Controller.getInstance().getScoreOdds(GlobalConstants.NUM_SCOREODDS, companyId, gameNo, matchId, companyType, page + "", mCallBack);
//		Controller.getInstance().getScoreCompany(GlobalConstants.NUM_SCORECOMPANY, timeId, mCallBack);
	}

	@Override
	public void loadData(int title, int page) {

	}
	
	private CallBack mCallBack = new CallBack() {
//		public void getScoreCompanySuccess(final ScoreCompanyOdds obj) {
//			getActivity().runOnUiThread(new Runnable() {
//				public void run() {
//					loadFinish();
//					if (isFirstPage()) {
////						listAdapter.setData(obj.getCompanies());
////						listAdapter = new ListViewAdapter(ScoreCompanyOddsActivity.this, obj.getCompanies());
////						listView.setAdapter(listAdapter);
//					} else {
////						scoreCompanyOddsAdapter.addAll(obj.getOdds());
//					}
//					if (obj.getCompanies() == null || obj.getCompanies().size() == 0) {
//						// 还原页码
//						restorePage();
//					}
//				}
//			});
//		};
		
		public void getScoreOddsSuccess(final ScoreCompanyOdds obj) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					loadFinish();
					if (isFirstPage()) {
						scoreCompanyOddsAdapter.replaceAll(obj.getList());
//						scoreCompanyOddsAdapter.notifyDataSetChanged();
					} else {
						scoreCompanyOddsAdapter.addAll(obj.getList());
					}
					if (obj.getList() == null || obj.getList().size() == 0) {
						// 还原页码
						restorePage();
					}
				}
			});
		};
		
//		public void getScoreCompanyFailure(String error) {
//			getActivity().runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					// 还原页码
//					restorePage();
//					loadFinish();
//					// ToastUtil.shortToast(mFragmentActivity, error);
//				}
//			});
//			super.onFail(error);
//		};
		
		public void getScoreOddsFailure(String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 还原页码
					restorePage();
					loadFinish();
					// ToastUtil.shortToast(mFragmentActivity, error);
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
			super.initLoad(mPullToRefreshListView, scoreCompanyOddsAdapter);
		}
	}

}
