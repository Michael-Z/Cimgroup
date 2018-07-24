package cn.com.cimgroup.frament;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ScoreCompanyOddsActivity;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.adapter.ScoreEuropeAdapter;
import cn.com.cimgroup.bean.ScoreDetailOdds;
import cn.com.cimgroup.bean.ScoreDetailOddsList;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;
/**
 * 欧赔
 * @author 秋风
 *
 */
public class ScoreEuropeFrament extends BaseLoadFragment {

	private boolean mShouldInitialize = true;

	private View mView;
	
	private PullToRefreshListView mPullToRefreshListView;
	
	private ScoreEuropeAdapter scoreEuropeAdapter;
	
	private String matchId;
	
	private String gameNo;
	
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mView == null) {
//			R.layout.frament_score_game
			mView = inflater.inflate(R.layout.frament_score_asia, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			
			mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.score_asis_listView);
			scoreEuropeAdapter = new ScoreEuropeAdapter(mFragmentActivity, null);
			mPullToRefreshListView.setAdapter(scoreEuropeAdapter);
			
			isPrepared = true;
			lazyLoad();
		}
		return mView;
	}

	@Override
	protected void loadData(int page) {
		matchId = ((ScoreDetailActivity)getActivity()).bean.getMatchId();
		gameNo = ((ScoreDetailActivity)getActivity()).gameNo;
		Controller.getInstance().getScoreDetailEurope(GlobalConstants.NUM_SCOREDETEUROPE, gameNo, matchId, mCallBack);
	}

	@Override
	public void loadData(int title, int page) {

	}
	
	private CallBack mCallBack = new CallBack() {
		public void getScoreDetailEuropeSuccess(final ScoreDetailOddsList list) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					loadFinish();
					if (list.getResCode().equals("0")) {
						if (isFirstPage()) {
							scoreEuropeAdapter.replaceAll(list.getList());
							
						} else {
							scoreEuropeAdapter.addAll(list.getList());
						}
						if(list.getList() == null || list.getList().size() == 0){
							// 还原页码
							restorePage();
						}
						if(scoreEuropeAdapter.getCount() == 0){
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 13);
						}
					} else {
						NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 13);
					}
				}
			});
		};
		public void getScoreDetailEuropeFailure(final String error) {
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
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		ScoreDetailOdds odds = (ScoreDetailOdds) scoreEuropeAdapter.getItem(position - 1);
		Intent intent = new Intent(mFragmentActivity, ScoreCompanyOddsActivity.class);
		intent.putExtra("companyId", odds.getCompanyId());
		intent.putExtra("matchId", matchId);
		intent.putExtra("gameNo", gameNo);
		intent.putExtra("companyType", "1");
		intent.putExtra("companyName", odds.getCompany());
		
		startActivity(intent);
	};

	@Override
	protected void lazyLoad() {
		if(!isPrepared || !isVisible) {
			return;
		} else {
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			super.initLoad(mPullToRefreshListView, scoreEuropeAdapter);
		}
	}

}
