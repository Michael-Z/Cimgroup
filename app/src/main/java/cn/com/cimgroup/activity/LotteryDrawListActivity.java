package cn.com.cimgroup.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseLoadActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.LotteryDrawAdapter;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 开奖列表
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年3月15日
 */
public class LotteryDrawListActivity extends BaseLoadActivity implements
		OnClickListener {

	private LotteryDrawInfo info;
	/** 去投注 */
	private LinearLayout layout_award_bottom;

	private String mGameName = "";

	private String mGameNo="";
	// 近期开奖集合
	private PullToRefreshListView mPullToRefreshListView;
	// 近期开奖adapter
	private LotteryDrawAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frament_latterydraw);

		Intent intent = getIntent();
		info = (LotteryDrawInfo) intent.getSerializableExtra("LotteryDrawInfo");
		mGameName = info.getGameName();
		mGameNo=info.getGameNo();

		initUI();
		super.initLoad(mPullToRefreshListView, mAdapter);
		// 让其具有上下拉刷新
		mListView.setMode(Mode.BOTH);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position
				- mListView.getRefreshableView().getHeaderViewsCount();
		// 更新详情数据
		LotteryDrawInfo lotteryDrawInfo = (LotteryDrawInfo) mAdapter
				.getItem(position);
		if (lotteryDrawInfo != null) {
			Intent intent = new Intent(this, LotteryDrawInfoActivity.class);
			intent.putExtra("lotterydrawinfo", lotteryDrawInfo);
			intent.putExtra("gameName", mGameName);
			intent.putExtra("gameNo", mGameNo);
			startActivity(intent);
		}

	}

	private void initUI() {

		TextView goTZ = (TextView) findViewById(R.id.textView_award_gotz);
		goTZ.setText(info.getGameName()
				+ getResources().getString(R.string.betting));
		layout_award_bottom = (LinearLayout) findViewById(R.id.layout_award_bottom);

		layout_award_bottom.setOnClickListener(this);

		((TextView) findViewById(R.id.textView_title_word)).setText(info.getGameName());
		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.cvLView_latelylottery_draw);
		mAdapter = new LotteryDrawAdapter(this, null, mGameName);

	}

	private CallBack mBack = new CallBack() {

		public void syncLotteryDrawSuccess(final List<LotteryDrawInfo> result) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadFinish();
					// 判断是否是首页，如果是则清空所有数据从新赋值 否则进行累加
					if (isFirstPage()) {
						mAdapter.replaceAll(result);
					} else {
						mAdapter.addAll(result);
					}
					if (mAdapter.getCount() == 0) {
						restorePage();
						NoDataUtils.setNoDataView(LotteryDrawListActivity.this, emptyImage, oneText, twoText, button, "0", 10);
					}
				}
			});
		};

		public void syncLotteryDrawError(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					restorePage();
					loadFinish();
					NoDataUtils.setNoDataView(LotteryDrawListActivity.this, emptyImage, oneText, twoText, button, error, 0);
				}
			});
		};
	};

	@Override
	public void onDestroy() {
		Controller.getInstance().removeCallback(mBack);
		super.onDestroy();
	}

	@Override
	protected void loadData(int page) {
		String gameNo = info.getGameNo();
		Controller.getInstance().syncLotteryDrawInfoList(
				GlobalConstants.NUM_LOTTERYDRAW, String.valueOf(page), gameNo,
				mBack);
	}

	@Override
	public void loadData(int title, int page) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 关闭当前页面
		case R.id.textView_title_back:
			mActivity.finish();
			break;
		case R.id.layout_award_bottom:
			Intent intent = new Intent(this, LotteryOldFootballActivity.class);
			switch (info.getGameNo()) {
			case GlobalConstants.TC_SF14:
				intent.putExtra("lotoId", 0);
				intent.putExtra("pageIndex", "0");
				startActivity(intent);
				break;
			case GlobalConstants.TC_SF9:
				intent.putExtra("lotoId", 0);
				intent.putExtra("pageIndex", "1");
				startActivity(intent);
				break;
			case GlobalConstants.TC_JQ4:
				intent.putExtra("lotoId", 1);
				startActivity(intent);
				break;
			case GlobalConstants.TC_BQ6:
				intent.putExtra("lotoId", 2);
				startActivity(intent);
				break;
			case GlobalConstants.TC_DLT:
				startActivity(LotteryDLTActivity.class);
				break;
			case GlobalConstants.TC_PL3:
				startActivity(LotteryPL3Activity.class);
				break;
			case GlobalConstants.TC_PL5:
				startActivity(LotteryPL5Activity.class);
				break;
			case GlobalConstants.TC_QXC:
				startActivity(LotteryQxcActivity.class);
				break;
			case GlobalConstants.TC_JCZQ:
				startActivity(LotteryFootballActivity.class);
				break;

			default:
				break;
			}
//			mActivity.finish();
		}
	}
}
