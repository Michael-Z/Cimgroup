package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.IssueList;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryDrawFootballList;
import cn.com.cimgroup.bean.LotteryDrawFootballMatch;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.popwindow.PopWindowFootballLotteryWay;
import cn.com.cimgroup.popwindow.PopWindowFootballLotteryWay.onItemClick;
import cn.com.cimgroup.popwindow.PopupWndSwitchCommon;
import cn.com.cimgroup.popwindow.PopupWndSwitchCommon.PlayMenuItemClick;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 竞彩足球开奖
 * 
 * @author 秋风
 * 
 */
public class LotteryDrawFootballListActivity extends BaseActivity implements
		OnClickListener, onItemClick, PlayMenuItemClick {
	/** 竞彩足球投注跳转 */
	private TextView id_jczq_betting_jump;

	// 胜平负
	private static final int SPF = 0;
	// 让球胜平负
	private static final int RQSPF = 1;
	// 比分
	private static final int BF = 2;
	// 进球数
	private static final int JQS = 3;
	// 半全场
	private static final int BQC = 4;

	private TextView back;

	private TextView word;

	private TextView time;

	private ImageView select;

	private LinearLayout layout;

	private LinearLayout mBgPop;

	private RelativeLayout title;

	private int mSelectIndex = SPF;

	// 比赛方式popupwindow
	private PopWindowFootballLotteryWay mPopWindowJczq;

	private LotteryDrawInfo mInfo;

	private List<LotteryDrawFootballMatch> list;

	private PopupWndSwitchCommon common;

	private String issue;
	/** 右侧图片按钮容器layout */
	private RelativeLayout id_title_right_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotterydraw_football_list);
		Intent intent = getIntent();
		mInfo = (LotteryDrawInfo) intent
				.getSerializableExtra("LotteryDrawInfo");
		issue = mInfo.getIssueNo();
		initView();
		showLoadingDialog();
		Controller.getInstance().syncLotteryDrawFootballList(
				GlobalConstants.NUM_LOTTERYDRAWFOOTBALLLIST, mInfo.getGameNo(),
				mInfo.getIssueNo(), mBack);
	}

	private void initView() {
		id_title_right_layout = (RelativeLayout) findViewById(R.id.id_title_right_layout);

		id_jczq_betting_jump = (TextView) findViewById(R.id.id_jczq_betting_jump);

		back = (TextView) findViewById(R.id.layoutView_title_left1);
		word = (TextView) findViewById(R.id.textView_lotterybid_text);
		select = (ImageView) findViewById(R.id.imgView_footballbid_more);
		select.setVisibility(View.VISIBLE);
		layout = (LinearLayout) findViewById(R.id.layout_lotterydraw_football_item);
		mBgPop = (LinearLayout) findViewById(R.id.pop_common_bg);

		title = (RelativeLayout) findViewById(R.id.layoutView_lotterydraw_football_title);

		mPopWindowJczq = new PopWindowFootballLotteryWay(this, mBgPop);
		mPopWindowJczq.setTitleText(getResources().getString(R.string.scorelist_title_match));
		mPopWindowJczq.setData(
				FootballLotteryConstants.getFootballLotteryDrawWay(),
				mSelectIndex);
		mPopWindowJczq.addCallBack(this);
		word.setText(mPopWindowJczq.getData().get(mSelectIndex)[1]);

		time = (TextView) findViewById(R.id.textView_lotterydraw_football_time);

		back.setOnClickListener(this);
		word.setOnClickListener(this);
		select.setOnClickListener(this);
		id_title_right_layout.setOnClickListener(this);
		id_jczq_betting_jump.setOnClickListener(this);
	}

	private CallBack mBack = new CallBack() {

		public void syncLotteryDrawFootballListSuccess(
				final LotteryDrawFootballList info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (info.getResCode().equals("0")) {
						list = info.getMatchList();
						addUI(list);
						time.setText(getResources().getString(
								R.string.record_tz_detail_qi, issue)
								+ "共" + list.size() + "场比赛");
						Controller.getInstance().getMatchTime(
								GlobalConstants.NUM_MATCHTIME,
								GlobalConstants.TC_JZSPF, "6", mBack);
					}
				}
			});
		};

		public void syncLotteryDrawFootballListError(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Controller.getInstance().getMatchTime(
							GlobalConstants.NUM_MATCHTIME,
							GlobalConstants.TC_JZSPF, "6", mBack);
				}
			});
		};

		public void getMatchTimeSuccess(final IssueList list) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					List<String> data = new ArrayList<String>();
					for (int i = 0; i < list.getIssues().size(); i++) {
						LoBoPeriodInfo info = list.getIssues().get(i);
						data.add(info.getIssue());
					}
					if (common == null) {
						common = new PopupWndSwitchCommon(
								LotteryDrawFootballListActivity.this, data,
								LotteryDrawFootballListActivity.this);
					}
				}
			});
		};

		public void getMatchTimeFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
				}
			});
		};
	};

	private void addUI(List<LotteryDrawFootballMatch> list) {
		layout.removeAllViews();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LotteryDrawFootballMatch match = list.get(i);
				LinearLayout view = (LinearLayout) View.inflate(
						LotteryDrawFootballListActivity.this,
						R.layout.item_lotterydraw_football_list, null);
				LotteryDrawFootball item = new LotteryDrawFootball(view);
				String wString = "";
				switch (Integer.parseInt(match.getWeek())) {
				case 1:
					wString = getResources().getString(R.string.lottery_monday);
					break;
				case 2:
					wString = getResources()
							.getString(R.string.lottery_tuesday);
					break;
				case 3:
					wString = getResources().getString(
							R.string.lottery_wednesday);
					break;
				case 4:
					wString = getResources().getString(
							R.string.lottery_thursday);
					break;
				case 5:
					wString = getResources().getString(R.string.lottery_friday);
					break;
				case 6:
					wString = getResources().getString(
							R.string.lottery_saturday);
					break;
				case 7:
					wString = getResources().getString(R.string.lottery_sunday);
					break;
				default:
					break;
				}
				item.rang.setVisibility(View.GONE);
				item.result.setVisibility(View.GONE);

				item.num.setText(wString + match.getMatchSort());
				item.matchName.setText(match.getLeagueShort());
				item.zhu.setText(match.getHomeTeamName());
				item.rang.setText(match.getLetScore());

				item.bifen.setText(match.getFullScore());
				item.ke.setText(match.getGuestTeamName());

				if (!StringUtil.isEmpty(match.getFullScore())) {

					String[] fullArr = null;
					if (match.getFullScore() != null) {
						fullArr = match.getFullScore().split("\\:");
					}
					String[] halfArr = null;
					if (match.getHalfScore() != null) {
						halfArr = match.getHalfScore().split("\\:");
					}

					StringBuilder sb = new StringBuilder();

					switch (mSelectIndex) {
					case SPF:
						if (Integer.parseInt(fullArr[0]) > Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_red));
							sb.append(getResources()
									.getString(R.string.victory));
						} else if (Integer.parseInt(fullArr[0]) == Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_green_warm));
							sb.append(getResources().getString(R.string.draw));
						} else {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.hall_blue));
							sb.append(getResources().getString(
									R.string.negative));
						}
						item.result.setVisibility(View.VISIBLE);
						break;
					case RQSPF:
						if ((Integer.parseInt(fullArr[0]) + Integer
								.parseInt(match.getLetScore())) > Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_red));
							sb.append(getResources()
									.getString(R.string.victory));
						} else if ((Integer.parseInt(fullArr[0]) + Integer
								.parseInt(match.getLetScore())) == Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_green_warm));
							sb.append(getResources().getString(R.string.draw));
						} else {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.hall_blue));
							sb.append(getResources().getString(
									R.string.negative));
						}
						item.rang.setVisibility(View.VISIBLE);
						item.result.setVisibility(View.VISIBLE);
						break;
					case BF:
						if (Integer.parseInt(fullArr[0]) > Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_red));
							sb.append(getResources()
									.getString(R.string.victory));
						} else if (Integer.parseInt(fullArr[0]) == Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_green_warm));
							sb.append(getResources().getString(R.string.draw));
						} else {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.hall_blue));
							sb.append(getResources().getString(
									R.string.negative));
						}
						break;
					case JQS:
						if (Integer.parseInt(fullArr[0]) > Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_red));
							sb.append(getResources()
									.getString(R.string.victory));
						} else if (Integer.parseInt(fullArr[0]) == Integer
								.parseInt(fullArr[1])) {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.color_green_warm));
							sb.append(getResources().getString(R.string.draw));
						} else {
							item.item.setBackgroundColor(getResources()
									.getColor(R.color.hall_blue));
							sb.append(getResources().getString(
									R.string.negative));
						}
						sb.append((Integer.parseInt(fullArr[0]) + Integer
								.parseInt(fullArr[1])));
						item.result.setVisibility(View.VISIBLE);
						break;
					case BQC:
						if (halfArr != null) {
							if (Integer.parseInt(halfArr[0]) > Integer
									.parseInt(halfArr[1])) {
								item.item.setBackgroundColor(getResources()
										.getColor(R.color.color_red));
								sb.append(getResources().getString(
										R.string.victory));
							} else if (Integer.parseInt(halfArr[0]) == Integer
									.parseInt(halfArr[1])) {
								item.item.setBackgroundColor(getResources()
										.getColor(R.color.color_green_warm));
								sb.append(getResources().getString(
										R.string.draw));
							} else {
								item.item.setBackgroundColor(getResources()
										.getColor(R.color.hall_blue));
								sb.append(getResources().getString(
										R.string.negative));
							}
						}

						if (fullArr != null) {
							if (Integer.parseInt(fullArr[0]) > Integer
									.parseInt(fullArr[1])) {
								item.item.setBackgroundColor(getResources()
										.getColor(R.color.color_red));
								sb.append(getResources().getString(
										R.string.victory));
							} else if (Integer.parseInt(fullArr[0]) == Integer
									.parseInt(fullArr[1])) {
								item.item.setBackgroundColor(getResources()
										.getColor(R.color.color_green_warm));
								sb.append(getResources().getString(
										R.string.draw));
							} else {
								item.item.setBackgroundColor(getResources()
										.getColor(R.color.hall_blue));
								sb.append(getResources().getString(
										R.string.negative));
							}
						}

						item.result.setVisibility(View.VISIBLE);
						break;
					default:
						break;
					}
					item.result.setText(mSelectIndex != JQS ? sb.toString()
							: (sb.toString().substring(1, sb.length()) + "球"));
				}
				layout.addView(view);
			}
		}
	}

	/**
	 * 类似于viewHolder，用于初始化显示的控件（item）
	 * @author 
	 *
	 */
	public class LotteryDrawFootball {
		/** 比赛编号 */
		private TextView num;
		/** 比赛结果 */
		private TextView result;
		/** 比赛主场队 */
		private TextView zhu;
		/** 比赛客场队 */
		private TextView ke;
		/** 比赛名称 */
		private TextView matchName;
		/** 比赛比分 */
		private TextView bifen;

		private TextView rang;

		private LinearLayout item;

		public LotteryDrawFootball(View view) {
			num = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_num);
			zhu = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_zhu);
			result = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_result);
			ke = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_ke);
			matchName = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_matchName);
			bifen = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_bifen);
			rang = (TextView) view
					.findViewById(R.id.textView_lotterydraw_football_item_rang);
			item = (LinearLayout) view
					.findViewById(R.id.layout_lotterydraw_football_item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layoutView_title_left1:
			finish();
			break;
		case R.id.textView_lotterybid_text:
			// 打开投注方式popWindow
			mPopWindowJczq.show2hideWindow(title);
			mBgPop.setVisibility(View.VISIBLE);
			break;
		case R.id.id_title_right_layout:
			if (common != null) {
				common.showPopWindow(v);
			}
			break;
		case R.id.id_jczq_betting_jump:
			startActivity(LotteryFootballActivity.class);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onSelectItemClick(String[] selectData, int position) {
		word.setText(selectData[1]);
		if (mSelectIndex != position) {
			mSelectIndex = position;
		}
		addUI(list);
	}

	@Override
	public void PopTzList(View v) {
		showLoadingDialog();
		issue = (String) v.getTag();
		Controller.getInstance().syncLotteryDrawFootballList(
				GlobalConstants.NUM_LOTTERYDRAWFOOTBALLLIST, mInfo.getGameNo(),
				(String) v.getTag(), mBack);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	}

}
