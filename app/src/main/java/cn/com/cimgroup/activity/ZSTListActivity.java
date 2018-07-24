package cn.com.cimgroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.IssuePreList;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 走势图列表页
 * 
 * @author 秋风
 * 
 */
public class ZSTListActivity extends BaseActivity implements OnClickListener {
	/** 期次信息 */
	private String mIssues;

	private int mGameNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zst_list);
		initView();
		initEvent();
	}

	/**
	 * 获取期次信息
	 */
	private void getIssues() {
		showLoadingDialog();
		Controller.getInstance().getLoBoPeriod(GlobalConstants.NUM_LOTTERY_PRE,
				LotteryBettingUtil.getLottery(mGameNo), mCallBack);
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_dlt_layout).setOnClickListener(this);
		findViewById(R.id.id_qxc_layout).setOnClickListener(this);
		findViewById(R.id.id_pl3_layout).setOnClickListener(this);
		findViewById(R.id.id_pl5_layout).setOnClickListener(this);
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_back:
			finish();
			break;
		case R.id.id_dlt_layout:
			// 跳转大乐透走势图
			mGameNo = GlobalConstants.LOTTERY_DLT;
//			getIssues();
			startActivity();
			break;
		case R.id.id_qxc_layout:
			// 跳转七星彩走势图
			mGameNo = GlobalConstants.LOTTERY_QXC;
//			getIssues();
			startActivity();
			break;
		case R.id.id_pl3_layout:
			// 跳转排列3走势图
			mGameNo = GlobalConstants.LOTTERY_PL3;
//			getIssues();
			startActivity();
			break;
		case R.id.id_pl5_layout:
			// 跳转排列5走势图
			mGameNo = GlobalConstants.LOTTERY_PL5;
//			getIssues();
			startActivity();
			break;
		default:
			break;
		}
	}

	private CallBack mCallBack = new CallBack() {
		public void getLoBoPeriodSuccess(final List<LoBoPeriodInfo> infos) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (infos != null && infos.size() != 0) {
						LoBoPeriodInfo periodInfo = infos.get(0);
						LotteryBettingUtil.setIssueAndEndTime(GlobalConstants.LOTTERY_DLT, periodInfo.getIssue(), periodInfo.getEndTime() + "");
						LotteryBettingUtil.setIssueAndEndTime(GlobalConstants.LOTTERY_PL3, periodInfo.getIssue(), periodInfo.getEndTime() + "");
						LotteryBettingUtil.setIssueAndEndTime(GlobalConstants.LOTTERY_PL5, periodInfo.getIssue(), periodInfo.getEndTime() + "");
						LotteryBettingUtil.setIssueAndEndTime(GlobalConstants.LOTTERY_QXC, periodInfo.getIssue(), periodInfo.getEndTime() + "");
						Controller.getInstance().getLoBoZhuiPeriod(
								GlobalConstants.NUM_LOTTERY_ZHUI_LIST,
								LotteryBettingUtil.getLottery(mGameNo),
								infos.get(0).getIssue(), mCallBack);
					}
				}
			});
		};

		public void getLoBoPeriodFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					switch (Integer.parseInt(error)) {
					
					case CException.NET_ERROR:
					case CException.IOERROR:
						ToastUtil.shortToast(mActivity, getResources().getString(R.string.nodata_no_net));
						break;
					default:
						break;
					}
				}
			});
		};

		public void getLoBoZhuiPeriodSuccess(final IssuePreList list) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (list.getResCode().equals("0")) {
						mIssues = list.getIssueArr();
						startActivity();
					}
				}

			});
		};

		public void getLoBoZhuiPeriodFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					switch (Integer.parseInt(error)) {
					
					case CException.NET_ERROR:
					case CException.IOERROR:
						ToastUtil.shortToast(mActivity, getResources().getString(R.string.nodata_no_net));
						break;
					default:
						break;
					}
				}
			});
		};

	};

	/**
	 * 相应走势图跳转
	 */
	private void startActivity() {
		String title = "";
		int urlNo = 0;
		switch (mGameNo) {
		case GlobalConstants.LOTTERY_DLT:
			title = "大乐透";
			urlNo = 0;
			break;
		case GlobalConstants.LOTTERY_PL3:
			title = "排列三";
			urlNo = 1;
			break;
		case GlobalConstants.LOTTERY_PL5:
			title = "排列五";
			urlNo = 2;
			break;
		case GlobalConstants.LOTTERY_QXC:
			title = "七星彩";
			urlNo = 3;
			break;

		default:
			break;
		}
		Intent intent = new Intent(this, ZSTWebViewActivity.class);
		intent.putExtra("url", GlobalConstants.getZSTUrl(urlNo));
		intent.putExtra("title", title + "走势图");
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, mIssues);
		startActivity(intent);
		hideLoadingDialog();
	}

}
