package cn.com.cimgroup.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.LotteryDrawDetailInfo;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.LotteryDrawMatch;
import cn.com.cimgroup.bean.LotteryDrawSnatch;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.DateUtils;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.FlowLayout;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 开奖详情
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年3月15日
 */
public class LotteryDrawInfoActivity extends BaseActivity implements
		OnClickListener {
	/** 点击跳转到相应的投注界面 */
	private LinearLayout layout_award_bottom;

	// 本期销售
	private TextView mLotterySellView;
	// 奖池余额
	private TextView mPgbalanceView;
	// 详细开奖等级
	private LinearLayout mAwardLiveLayout;
	// 开奖号码
	FlowLayout infoNumberView;
	// 期次
	private TextView infotermView;
	// 开奖时间
	private TextView infoDateView;

	private LotteryDrawInfo drawinfo;

	private LotteryDrawDetailInfo mInfo;

	private LinearLayout oldSpf;

	private LinearLayout oldSpfLayout;

	private TextView resultView;

	private TextView result1View;

	private TextView goTZ;

	private LinearLayout awardLayout;

	private LinearLayout chiLayout;

	private String mGameNo;
	private String mGameName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frament_lotterydrawinfo);
		Intent intent = getIntent();
		drawinfo = (LotteryDrawInfo) intent
				.getSerializableExtra("lotterydrawinfo");
		mGameNo = intent.getStringExtra("gameNo");
		mGameName = intent.getStringExtra("gameName");
		initView();
		showLoadingDialog();
		Controller.getInstance().syncLotteryDrawInfo(
				GlobalConstants.NUM_LOTTERYDRAWINFO, mGameNo, "", "",
				drawinfo.getIssueNo(), mBack);
	}

	/***
	 * 初始化视图组件
	 */
	private void initView() {
		layout_award_bottom = (LinearLayout) findViewById(R.id.layout_award_bottom);
//		((TextView) findViewById(R.id.textView_title_word)).setText(mGameName
//				+ getString(R.string.lotterydraw_detail_text));
		((TextView) findViewById(R.id.textView_title_word)).setText(getString(R.string.lotterydraw_detail_text));
		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);
		mLotterySellView = (TextView) findViewById(R.id.txtView_lottery_sell);
		mPgbalanceView = (TextView) findViewById(R.id.txtView_pgbalance);
		mAwardLiveLayout = (LinearLayout) findViewById(R.id.layoutView_award_leve);
		infoNumberView = (FlowLayout) findViewById(R.id.cvView_lotterydrawinfo_number);
		infotermView = (TextView) findViewById(R.id.txtView_lotterydrawinfo_term);
		infoDateView = (TextView) findViewById(R.id.txtView_lotterydrawinfo_date);
		oldSpf = (LinearLayout) findViewById(R.id.layoutView_award_oldfootball_spf);
		resultView = (TextView) findViewById(R.id.textView_award_info_result);
		result1View = (TextView) findViewById(R.id.textView_award_info_result1);
		oldSpfLayout = (LinearLayout) findViewById(R.id.layout_award_oldfootball);
		awardLayout = (LinearLayout) findViewById(R.id.layout_award);
		chiLayout = (LinearLayout) findViewById(R.id.layout_draw_chi);

		goTZ = (TextView) findViewById(R.id.textView_award_gotz);
		goTZ.setText(mGameName + getResources().getString(R.string.betting));
		layout_award_bottom.setOnClickListener(this);
	}

	/**
	 * 加载UI数据
	 * 
	 * @Description:
	 * @param info
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	@SuppressWarnings("deprecation")
	public void addUIData(LotteryDrawDetailInfo info) {
		if (info != null) {
			infotermView.setText(getResources().getString(R.string.term_num,
					mGameName + "  " + drawinfo.getIssueNo()));
			String date = drawinfo.getAwardDate();
			if (mGameNo.equals(GlobalConstants.TC_DLT)
					|| mGameNo.equals(GlobalConstants.TC_QXC)) {
				infoDateView
						.setText(date.substring(0, 10)
								+ " ("
								+ DateUtils.getWeek_(DateUtils
										.getSecondsFromDate(date)) + ")");
			} else {
				infoDateView.setText(date.substring(0, 10));
			}

			// 篮球
			String winbasecode = "";
			// 红球
			String winspecialcode = "";
			String[] jiCode = drawinfo.getWinCode().split("\\ ");
			if (jiCode != null && jiCode.length > 0) {
				winbasecode = jiCode[0];
			}
			if (jiCode != null && jiCode.length > 1) {
				winspecialcode = jiCode[1];
			}
			infoNumberView.removeAllViews();

			boolean showBg = false;
			switch (mGameNo) {
			case GlobalConstants.TC_SF14:
			case GlobalConstants.TC_SF9:
			case GlobalConstants.TC_JQ4:
			case GlobalConstants.TC_BQ6:
				showBg = true;
				break;

			default:
				showBg = false;
				break;
			}

			LotteryShowUtil.showNumberLotteryView(this, 0, infoNumberView,
					winbasecode, showBg);
			LotteryShowUtil.showNumberLotteryView(this, 1, infoNumberView,
					winspecialcode, showBg);

			// 本期销售
			mLotterySellView.setText(Html.fromHtml("<font color='#F52B43'>"
					+ info.getNew_tion_fund() + "</font>"
					+ getString(R.string.price_unit)));
			// 奖池累计
			if (!StringUtil.isEmpty(info.getNew_pond_money())) {
				mPgbalanceView.setText(Html.fromHtml("<font color='#F52B43'>"
						+ info.getNew_pond_money() + "</font>"
						+ getString(R.string.price_unit)));
			} else {
				mPgbalanceView.setText(Html
						.fromHtml("<font color='#F52B43'>0</font>"
								+ getString(R.string.lemi_unit)));
			}

			switch (mGameNo) {
			case GlobalConstants.TC_PL3:
			case GlobalConstants.TC_PL5:
				chiLayout.setVisibility(View.GONE);
				break;

			default:
				chiLayout.setVisibility(View.VISIBLE);
				break;
			}

			mAwardLiveLayout.removeAllViews();
			List<LotteryDrawSnatch> snatchs = info.getAwardSnatchList();
			for (int i = 0; i < snatchs.size(); i++) {
				LotteryDrawSnatch snatch = snatchs.get(i);
				View showLayout = View.inflate(this,
						R.layout.activity_lottery_award_detail, null);
				ShowDetail showDetail = new ShowDetail(showLayout);
				showDetail.mTVDetailAwardNameView.setText(snatch
						.getAwardLevelMsg());
				showDetail.mTVDetailWinNumberView.setText(snatch.getAwardNum());
				showDetail.mTVDetailWinMoneyView.setText(snatch
						.getSingleAwardMoney());
				mAwardLiveLayout.addView(showLayout);
			}

			oldSpf.removeAllViews();
			List<LotteryDrawMatch> matchs = info.getMatchList();
			if (matchs != null) {
				if (matchs.size() > 0) {
					oldSpfLayout.setVisibility(View.VISIBLE);
					switch (mGameNo) {
					case GlobalConstants.TC_SF14:
					case GlobalConstants.TC_SF9:
						result1View.setVisibility(View.GONE);
						break;
					case GlobalConstants.TC_JQ4:
						result1View.setVisibility(View.VISIBLE);
						result1View.setText("客队");
						resultView.setText("主队");
						break;
					case GlobalConstants.TC_BQ6:
						result1View.setVisibility(View.VISIBLE);
						result1View.setText("全");
						resultView.setText("半");
						break;
					default:
						break;
					}
					for (int i = 0; i < matchs.size(); i++) {
						LotteryDrawMatch match = matchs.get(i);
						View showLayout = View.inflate(this,
								R.layout.item_lottery_award_oldfootball_spf,
								null);
						ShowSpfMatch showDetail = new ShowSpfMatch(showLayout);
						showDetail.num.setText(match.getMatchNum());
						showDetail.duizhen.setText(match.getHostVsAgent());
						if (match.getMatchResult().indexOf("#") != -1) {
							String[] arr = match.getMatchResult().split("\\#");
							showDetail.result.setText(arr[0]);
							showDetail.result1.setText(arr[1]);
							showDetail.result1.setVisibility(View.VISIBLE);
						} else {
							showDetail.result1.setVisibility(View.GONE);
							showDetail.result.setText(match.getMatchResult());
						}
						;

						oldSpf.addView(showLayout);
					}
				}
			}
		}
	}

	/**
	 * 奖级view
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2016年3月15日
	 */
	public class ShowDetail {

		TextView mTVDetailAwardNameView;
		TextView mTVDetailWinNumberView;
		TextView mTVDetailWinMoneyView;

		public ShowDetail(View view) {
			mTVDetailAwardNameView = (TextView) view
					.findViewById(R.id.txtView_detail_awardname);
			mTVDetailWinNumberView = (TextView) view
					.findViewById(R.id.txtView_detail_winnumber);
			mTVDetailWinMoneyView = (TextView) view
					.findViewById(R.id.txtView_detail_winmoney);
		}

	}

	public class ShowSpfMatch {

		TextView num;
		TextView duizhen;
		TextView result;
		TextView result1;

		public ShowSpfMatch(View view) {
			num = (TextView) view
					.findViewById(R.id.textView_award_oldfootball_spf_num);
			duizhen = (TextView) view
					.findViewById(R.id.textView_award_oldfootball_spf_duizhen);
			result = (TextView) view
					.findViewById(R.id.textView_award_oldfootball_spf_result);
			result1 = (TextView) view
					.findViewById(R.id.textView_award_oldfootball_spf_result1);
		}

	}

	private CallBack mBack = new CallBack() {

		public void syncLotteryDrawInfoSuccess(final LotteryDrawDetailInfo info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					mInfo = info;
					addUIData(mInfo);
				}
			});
		};

		public void syncLotteryDrawInfoError(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
				}
			});
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			finish();
			break;
		case R.id.layout_award_bottom:
			Intent intent = new Intent(this, LotteryOldFootballActivity.class);
			switch (mGameNo) {
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
			break;
		default:
			break;
		}
	}
}
