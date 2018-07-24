package cn.com.cimgroup.dailog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 足彩投注产生的dialog
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-20
 */
@SuppressLint("ResourceAsColor")
public class FootballLotteryWayDailog implements OnClickListener {

	private Dialog mDialog;

	// 自定义dialog布局
	private View mView;

	// 容器.用于承载足彩各种投注方式的子控件
	private LinearLayout layoutView;

	// 取消按钮
	private Button mCanlView;
	// 确认按钮
	private Button mOkView;
	// title name
	private TextView mTitleTxtView;

	// 回调接口
	public OnClickEvent mCallBack = null;

	// 调用者传递的tag值 ;调用者大部分为listview的item，此值用于标识item调用者，以便关闭dialog后，方便对数据的处理
	private Object tag = null;

	public enum DialogType {
		// 半全场
		BQC,
		// 比分
		BF,
		// 混合过关
		HHGG,
		// 篮球 胜分差
		SFC,
		// 篮球 混合
		LQHHGG,
	}

	public interface OnClickEvent {

		// 确定按钮的点击回调
		public void OnOKClick(Object... objects);

		// 取消按钮的点击回调
		public void OnCanlClick();

		// 其他按钮的点击回调
		public void OnOtherClick();
	}

	private Context mContext;

	public FootballLotteryWayDailog(Context context, OnClickEvent callBack) {
		this.mContext = context;
		this.mCallBack = callBack;
		initView();
		// 去除title
		mOkView.setOnClickListener(this);
		mCanlView.setOnClickListener(this);
	}

	/**
	 * 初始化通用控件
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-21
	 */
	private void initView() {
		mView = View.inflate(mContext, R.layout.dialog_football_lottery_way,
				null);
		layoutView = (LinearLayout) mView
				.findViewById(R.id.layoutView_football_lottery_content);
		mCanlView = (Button) mView
				.findViewById(R.id.btnView_football_lottery_dialog_canl);
		mOkView = (Button) mView
				.findViewById(R.id.btnView_football_lottery_dialog_ok);
		mTitleTxtView = (TextView) mView
				.findViewById(R.id.txtView_football_lottery_dialog_title);
		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int screenSize[] = GlobalTools.getScreenSize((Activity) mContext);
		int width = (int) (screenSize[0] * 0.8);
		int height = (int) (screenSize[1] * 0.6);
		mDialog.setContentView(mView, new LayoutParams(width, height));
	}

	/**
	 * 弹出dialog
	 * 
	 * @Description:
	 * @param dialogType
	 *            dialog类型
	 * @param objects
	 *            任意长度参数
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	public synchronized void showDialog(DialogType dialogType,
			Object... objects) {
		// 清除原来laoutView中的子控件
		try {
			layoutView.removeAllViews();
			tag = ((Object[]) objects)[1];
			showFootballDialog(dialogType, objects);
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(mContext, "dialog data error--", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@SuppressWarnings("unchecked")
	private void showFootballDialog(DialogType dialogType, Object... objects) {

		Match match = (Match) objects[2];
		List<MatchAgainstSpInfo> sps = match.getMatchAgainstSpInfoList();
		MatchSearchType mSearchType = (MatchSearchType) objects[3];
		mTitleTxtView.setText(Html.fromHtml(match.getHomeTeamName()
				+ "<span>&nbsp;&nbsp;&nbsp;VS&nbsp;&nbsp;&nbsp;</span>"
				+ match.getGuestTeamName()));

		switch (dialogType) {
		// 半全场
		case BQC:
			List<Integer> selectBqcIndexs = new ArrayList<Integer>();
			// 已经选择的item选项回调
			List<Integer> tempIndexsbqc = (List<Integer>) ((Object[]) objects)[0];
			if (tempIndexsbqc != null && tempIndexsbqc.size() != 0) {
				selectBqcIndexs.addAll(tempIndexsbqc);
			}

			showDialogbqc(selectBqcIndexs, sps, mSearchType);
			// 将indexs赋值给okButton以便点击确认按钮是，将数据返回
			mOkView.setTag(selectBqcIndexs);
			break;
		// 比分
		case BF:
			List<Integer> selectBfIndexs = new ArrayList<Integer>();
			// 已经选择的item选项回调
			List<Integer> tempIndexs1 = (List<Integer>) ((Object[]) objects)[0];
			if (tempIndexs1 != null && tempIndexs1.size() != 0) {
				selectBfIndexs.addAll(tempIndexs1);
			}
			showDialogbf(selectBfIndexs, sps, mSearchType);
			// 将indexs赋值给okButton以便点击确认按钮是，将数据返回
			mOkView.setTag(selectBfIndexs);
			break;
		// 混合过关
		case HHGG:
			tag = ((Object[]) objects)[1];
			Map<Integer, List<Integer>> selecthhggIndexs = new HashMap<Integer, List<Integer>>();
			Map<Integer, List<Integer>> tempHhggIndexs = (Map<Integer, List<Integer>>) objects[0];
			if (tempHhggIndexs != null && tempHhggIndexs.size() != 0) {
				selecthhggIndexs = new HashMap<Integer, List<Integer>>();
				List<Integer> key = new ArrayList<Integer>(
						tempHhggIndexs.keySet());
				if (key != null) {
					for (int i = 0; i < key.size(); i++) {
						List<Integer> value = tempHhggIndexs.get(key.get(i));
						if (value != null && value.size() != 0) {
							selecthhggIndexs.put(key.get(i),
									new ArrayList<Integer>(value));
						}
					}
				}
			}
			showDialoghhbf(selecthhggIndexs, sps, mSearchType, match);
			mOkView.setTag(selecthhggIndexs);
			break;
		// 胜分差
		case SFC:
			List<Integer> selectSFCIndexs = new ArrayList<Integer>();
			// 已经选择的item选项回调
			List<Integer> tempIndexs2 = (List<Integer>) ((Object[]) objects)[0];
			if (tempIndexs2 != null && tempIndexs2.size() != 0) {
				selectSFCIndexs.addAll(tempIndexs2);
			}
			// 该选项的比率数据
			showDialogSfc(selectSFCIndexs, sps, mSearchType);
			// 将indexs赋值给okButton以便点击确认按钮是，将数据返回
			mOkView.setTag(selectSFCIndexs);
			break;
		case LQHHGG:
			tag = ((Object[]) objects)[1];
			Map<Integer, List<Integer>> selectlqhhggIndexs = new HashMap<Integer, List<Integer>>();
			Map<Integer, List<Integer>> templqHhggIndexs = (Map<Integer, List<Integer>>) objects[0];
			if (templqHhggIndexs != null && templqHhggIndexs.size() != 0) {
				selecthhggIndexs = new HashMap<Integer, List<Integer>>();
				List<Integer> key = new ArrayList<Integer>(
						templqHhggIndexs.keySet());
				if (key != null) {
					for (int i = 0; i < key.size(); i++) {
						List<Integer> value = templqHhggIndexs.get(key.get(i));
						if (value != null && value.size() != 0) {
							selectlqhhggIndexs.put(key.get(i),
									new ArrayList<Integer>(value));
						}
					}
				}
			}
			showDialoglqhhbf(selectlqhhggIndexs, sps, mSearchType, match);
			mOkView.setTag(selectlqhhggIndexs);
			break;
		default:
			break;
		}
	}

	/**
	 * 半全场 dialog
	 * 
	 * @Description:
	 * @param index
	 *            选择的button
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	private void showDialogbqc(final List<Integer> indexs,
			List<MatchAgainstSpInfo> sps, MatchSearchType mSearchType) {
		// 半全场中所使用的view
		View childView = View.inflate(mContext,
				R.layout.dialog_item_football_lottery_bqc, null);
		Button[] buttons = {
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_1)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_2)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_3)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_4)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_5)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_6)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_7)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_8)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_bqc_9)) };
		layoutView.addView(childView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// 获取半全场内容
		String bqcSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZBQC, mSearchType);
		// 获取所有比分
		if (bqcSp.indexOf("@") != -1) {
			String[] bqcArr = bqcSp.split("\\@");
			for (int index = 0; index < bqcArr.length; index++) {

				// 如果此button原来已经选择、且未取消，那么现在让其显示
				for (int i = 0; i < indexs.size(); i++) {
					if (indexs.contains(index)) {
						buttons[index].setSelected(true);
					} else {
						buttons[index].setSelected(false);
					}
				}
				Spanned textHtml = null;
				if (bqcArr[index].split("\\_").length > 1) {
					textHtml = Html
							.fromHtml(bqcArr[index].split("\\_")[0]
									+ "<br><small><font color='#"+(buttons[index].isSelected()?"FFFFFF":"999999")+"'>"
									+ bqcArr[index].split("\\_")[1]
									+ "</font></small>");
				}
				buttons[index].setText(textHtml);
				buttons[index].setTag(index);
				buttons[index].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (indexs.contains(tag)) {
								indexs.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								indexs.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}
						}
					}
				});
			}
		}

		mDialog.show();
	}

	/**
	 * 比分 dialog
	 * 
	 * @Description:
	 * @param index
	 *            选择的button
	 * @param sp
	 *            比率
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	private void showDialogbf(final List<Integer> indexs,
			List<MatchAgainstSpInfo> sps, MatchSearchType mSearchType) {
		// 半全场中所使用的view
		View childView = View.inflate(mContext,
				R.layout.dialog_item_football_lottery_bf, null);
		Button[] buttons = {
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_0)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_1)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_2)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_3)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_4)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_5)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_6)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_7)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_8)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_9)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_10)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_11)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_12)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_13)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_14)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_15)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_16)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_17)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_18)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_19)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_20)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_21)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_22)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_23)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_24)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_25)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_26)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_27)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_28)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_29)),
				((Button) childView
						.findViewById(R.id.football_lottery_dialog_bf_30)) };
		layoutView.addView(childView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// 获取所有比分
		String bfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZBF, mSearchType);
		if (bfSp.indexOf("@") != -1) {
			String[] bfArr = bfSp.split("\\@");
			for (int index = 0; index < bfArr.length; index++) {
				Spanned textHtml = null;
				// 如果此button原来已经选择、且未取消，那么现在让其显示
				for (int i = 0; i < indexs.size(); i++) {
					if (indexs.contains(index)) {
						buttons[index].setSelected(true);
					} else {
						buttons[index].setSelected(false);
					}
				}

				if (bfArr[index].split("\\_").length > 1) {
					textHtml = Html.fromHtml(bfArr[index].split("\\_")[0]
							+ "<br><small><font color='"
							+ (buttons[index].isSelected() ? "#FFFFFF'>"
									: "#999999'>")
							+ bfArr[index].split("\\_")[1] + "</font></small>");
				}
				buttons[index].setText(textHtml);
				// 将半全场的结果key赋值给button
				buttons[index].setTag(index);
				buttons[index].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (indexs.contains(tag)) {
								indexs.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								indexs.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}

						}
					}
				});
			}
		}

		mDialog.show();
	}

	/**
	 * 混合比分 dialog
	 * 
	 * @Description:
	 * @param index
	 *            选择的button
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	@SuppressLint("UseValueOf")
	private void showDialoghhbf(final Map<Integer, List<Integer>> indexs,
			List<MatchAgainstSpInfo> sps, MatchSearchType mSearchType,
			Match match) {
		// 获取胜平负内容
		String spfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZSPF, mSearchType);
		// 获取让球胜平负内容
		String rspfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZXSPF, mSearchType);
		// 获取半全场内容
		String bqcSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZBQC, mSearchType);
		// 获取比分内容
		String bfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZBF, mSearchType);
		// 获取进球数
		String jqsSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JZJQS, mSearchType);

		// 胜平负 按钮选择的结果集
		final List<Integer> selectIndex1 = indexs
				.get(FootballLotteryConstants.L320) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L320);
		// 让球胜平负 按钮选择的结果集
		final List<Integer> selectIndex2 = indexs
				.get(FootballLotteryConstants.L301) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L301);
		// 半全场 半全场按钮选择的结果集
		final List<Integer> selectIndex3 = indexs
				.get(FootballLotteryConstants.L304) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L304);
		// 比分
		final List<Integer> selectIndex4 = indexs
				.get(FootballLotteryConstants.L302) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L302);
		// 进球数
		final List<Integer> selectIndex5 = indexs
				.get(FootballLotteryConstants.L303) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L303);
		// 原因是因为按钮的onclick时候需要final的selectIndex1，所以这里需要从新将集合存储到Map中。用于引用传递，防止数据丢失
		indexs.put(FootballLotteryConstants.L320, selectIndex1);
		indexs.put(FootballLotteryConstants.L301, selectIndex2);
		indexs.put(FootballLotteryConstants.L304, selectIndex3);
		indexs.put(FootballLotteryConstants.L302, selectIndex4);
		indexs.put(FootballLotteryConstants.L303, selectIndex5);
		// 半全场中所使用的view
		View childView = View.inflate(mContext,
				R.layout.dialog_item_football_lottery_hhgg, null);
		layoutView.addView(childView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		((TextView) childView
				.findViewById(R.id.txtView_football_lottery_dialog_hhgg_allow1))
				.setText(match.getLetScore());
		((TextView) childView
				.findViewById(R.id.txtView_football_dialog_bqc_desc))
				.setText(Html.fromHtml(mContext.getResources().getString(
						R.string.football_dialog_bqc_html)));
		((TextView) childView
				.findViewById(R.id.txtView_football_dialog_bf_desc))
				.setText(Html.fromHtml(mContext.getResources().getString(
						R.string.football_dialog_bf_html)));
		((TextView) childView
				.findViewById(R.id.txtView_football_dialog_jqs_desc))
				.setText(Html.fromHtml(mContext.getResources().getString(
						R.string.football_dialog_zjqs_html)));
		// 胜平负
		Button buttonsSpf[] = new Button[] {
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_spf_0)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_spf_1)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_spf_2)) };
		// 让球胜平负
		Button buttonsrSpf[] = new Button[] {
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_rspf_0)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_rspf_1)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_rspf_2)) };
		// 半全场
		Button buttonsBqc[] = new Button[] {
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_0)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_1)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_2)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_3)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_4)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_5)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_6)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_7)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bqc_8)) };
		// 比分
		Button buttonsBf[] = new Button[] {
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_0)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_1)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_2)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_3)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_4)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_5)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_6)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_7)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_8)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_9)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_10)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_11)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_12)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_13)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_14)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_15)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_16)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_17)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_18)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_19)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_20)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_21)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_22)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_23)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_24)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_25)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_26)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_27)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_28)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_29)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_bf_30)) };
		// 进球数
		Button buttonsJqs[] = new Button[] {
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_0)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_1)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_2)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_3)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_4)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_5)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_6)),
				((Button) childView
						.findViewById(R.id.btnView_football_lottery_dialog_hhgg_jqs_7)) };

		// 胜平负
		// 将半全场的所有结果赋值给button
		if (spfSp.indexOf("@") != -1) {
			String[] spArr = spfSp.split("\\@");
			for (int index0 = 0; index0 < spArr.length; index0++) {
				if (selectIndex1 != null) {
					if (selectIndex1.contains(new Integer(index0))) {
						buttonsSpf[index0].setSelected(true);
					}
				}
				Spanned textHtml = null;
				if (spArr[index0].split("\\_").length > 1) {
					String key = "";
					switch (spArr[index0].split("\\_")[0]) {
					case "3":
						key = mContext.getResources().getString(
								R.string.victory);
						break;
					case "1":
						key = mContext.getResources().getString(R.string.draw);
						break;
					case "0":
						key = mContext.getResources().getString(
								R.string.negative);
						break;
					default:
						break;
					}
					textHtml = Html
							.fromHtml(key + " <small><font color='#"+(buttonsSpf[index0].isSelected()?"FFFFFF":"999999")+"'>"
									+ spArr[index0].split("\\_")[1]
									+ "</font></small>");
				}

				buttonsSpf[index0].setText(textHtml);
				buttonsSpf[index0].setTag(index0);
				buttonsSpf[index0].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split(" ");
							if (selectIndex1.contains(tag)) {
								selectIndex1.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font> <small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								selectIndex1.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font> <small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}
						}
					}
				});
				// 判断改控件是否正在选择，如果是那么改其UI状态为已选择
			}
		}

		// /////////////////让球胜平负///////////////////////
		// 遍历内容，赋值给胜平负对应的每一个button控件
		if (rspfSp.indexOf("@") != -1) {
			String[] rspfArr = rspfSp.split("\\@");
			for (int index5 = 0; index5 < rspfArr.length; index5++) {
				// 判断改控件是否正在选择，如果是那么改其UI状态为已选择
				if (selectIndex2 != null) {
					if (selectIndex2.contains(new Integer(index5))) {
						buttonsrSpf[index5].setSelected(true);
					}
				}
				// 将半全场的所有结果赋值给button
				Spanned textHtml = null;
				if (rspfArr[index5].split("\\_").length > 1) {
					String key = "";
					switch (rspfArr[index5].split("\\_")[0]) {
					case "3":
						key = mContext.getResources().getString(
								R.string.victory);
						break;
					case "1":
						key = mContext.getResources().getString(R.string.draw);
						break;
					case "0":
						key = mContext.getResources().getString(
								R.string.negative);
						break;
					default:
						break;
					}
					textHtml = Html.fromHtml(key
							+ " <small><font color='#"+(buttonsrSpf[index5].isSelected()?"FFFFFF":"999999")+"'>"
							+ rspfArr[index5].split("\\_")[1]
							+ "</font></small>");
				}
				buttonsrSpf[index5].setText(textHtml);
				buttonsrSpf[index5].setTag(index5);
				buttonsrSpf[index5].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split(" ");
							if (selectIndex2.contains(tag)) {
								selectIndex2.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font> <small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								selectIndex2.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font> <small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}
						}
					}
				});
			}
		}

		// /////////////////半全场////////////////////////
		if (bqcSp.indexOf("@") != -1) {
			String[] bqcArr = bqcSp.split("\\@");
			for (int index1 = 0; index1 < bqcArr.length; index1++) {
				// 判断改控件是否正在选择，如果是那么改其UI状态为已选择
				if (selectIndex3 != null) {
					if (selectIndex3.contains(new Integer(index1))) {
						buttonsBqc[index1].setSelected(true);
					}
				}
				Spanned textHtml = null;
				if (bqcArr[index1].split("\\_").length > 1) {
					textHtml = Html.fromHtml(bqcArr[index1].split("\\_")[0]
							+ "<br><small><font color='#"+(buttonsBqc[index1].isSelected()?"FFFFFF":"999999")+"'>"
							+ bqcArr[index1].split("\\_")[1]
							+ "</font></small>");
				}
				buttonsBqc[index1].setText(textHtml);
				buttonsBqc[index1].setTag(index1);
				buttonsBqc[index1].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (selectIndex3.contains(tag)) {
								selectIndex3.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								selectIndex3.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}

						}
					}
				});
			}
		}

		// //////////////////比分/////////////////////////////
		if (bfSp.indexOf("@") != -1) {
			String[] bfArr = bfSp.split("\\@");
			for (int index3 = 0; index3 < bfArr.length; index3++) {
				// 判断改控件是否正在选择，如果是那么改其UI状态为已选择
				if (selectIndex4 != null) {
					if (selectIndex4.contains(new Integer(index3))) {
						buttonsBf[index3].setSelected(true);
					}
				}
				Spanned textHtml = null;
				if (bfArr[index3].split("\\_").length > 1) {
					textHtml = Html
							.fromHtml(bfArr[index3].split("\\_")[0]
									+ "<br><small><font color='#"+(buttonsBf[index3].isSelected()?"FFFFFF":"999999")+"'>"
									+ bfArr[index3].split("\\_")[1]
									+ "</font></small>");
				}
				buttonsBf[index3].setText(textHtml);
				buttonsBf[index3].setTag(index3);
				buttonsBf[index3].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (selectIndex4.contains(tag)) {
								selectIndex4.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								selectIndex4.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}

						}
					}
				});
			}

		}

		// //////////////////进球数/////////////////////////////
		if (jqsSp.indexOf("@") != -1) {
			String[] jqsArr = jqsSp.split("\\@");
			for (int index4 = 0; index4 < jqsArr.length; index4++) {
				// 判断改控件是否正在选择，如果是那么改其UI状态为已选择
				if (selectIndex5 != null) {
					if (selectIndex5.contains(new Integer(index4))) {
						buttonsJqs[index4].setSelected(true);
					}
				}
				Spanned textHtml = null;
				if (jqsArr[index4].split("\\_").length > 1) {
					textHtml = Html.fromHtml(jqsArr[index4].split("\\_")[0]
							+ "球" + "<br><small><font color='#"+(buttonsJqs[index4].isSelected()?"FFFFFF":"999999")+"'>"
							+ jqsArr[index4].split("\\_")[1]
							+ "</font></small>");
				}

				buttonsJqs[index4].setText(textHtml);
				buttonsJqs[index4].setTag(index4);
				buttonsJqs[index4].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (selectIndex5.contains(tag)) {
								selectIndex5.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								selectIndex5.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}
						}
					}
				});
			}
		}

		mDialog.show();
	}

	/**
	 * 篮球胜分差 dialog
	 * 
	 * @Description:
	 * @param index
	 *            选择的button
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	private void showDialogSfc(final List<Integer> indexs,
			List<MatchAgainstSpInfo> sps, MatchSearchType mSearchType) {
		// 客胜
		View childView = View.inflate(mContext,
				R.layout.dialog_item_basketball_lottery_sfc, null);
		Button[] buttons1 = {
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_1)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_2)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_3)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_4)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_5)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_6)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_7)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_8)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_9)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_10)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_11)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_12)) };
		layoutView.addView(childView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// 获取所有比分
		// MatchFootballState allState =
		// FootballLotteryConstants.getMatchResult(FootballLotteryConstants.L308);
		// Map<Integer, String> data = allState.getData();
		String bfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JLSFC, mSearchType);
		if (bfSp.indexOf("@") != -1) {
			String[] bfArr = bfSp.split("\\@");
			for (int index = 0; index < bfArr.length; index++) {
				// 如果此button原来已经选择、且未取消，那么现在让其显示
				for (int i = 0; i < indexs.size(); i++) {
					if (indexs.contains(index)) {
						buttons1[index].setSelected(true);
					} else {
						buttons1[index].setSelected(false);
					}
				}
				// 将半全场的所有结果赋值给button
				Spanned textHtml = null;
				if (bfArr[index].split("\\_").length > 1) {
					textHtml = Html.fromHtml(bfArr[index].split("\\_")[0]
							+ "<br><small><font color='#"+(buttons1[index].isSelected()?"FFFFFF":"999999")+"'>" + bfArr[index].split("\\_")[1]
							+ "</font></small>");
				}
				// 将半全场的所有结果赋值给button
				buttons1[index].setText(textHtml);
				// 将半全场的结果key赋值给button
				buttons1[index].setTag(index);
				buttons1[index].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (indexs.contains(tag)) {
								indexs.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								indexs.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}
						}
					}
				});
			}
		}

		mDialog.show();
	}

	@SuppressLint("UseValueOf")
	private void showDialoglqhhbf(final Map<Integer, List<Integer>> indexs,
			List<MatchAgainstSpInfo> sps, MatchSearchType mSearchType,
			Match match) {
		// 获取胜平负内容
		String sfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JLSF, mSearchType);
		// 获取让球胜平负内容
		String rsfSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JLXSF, mSearchType);
		// 获取胜分差
		String sfcSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JLSFC, mSearchType);
		// 获取大小分
		String dxSp = FootballLotteryTools.getMatchSp(sps,
				GlobalConstants.TC_JLDXF, mSearchType);

		// 胜平负 按钮选择的结果集
		final List<Integer> selectIndex1 = indexs
				.get(FootballLotteryConstants.L307) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L307);
		// 让球胜平负 按钮选择的结果集
		final List<Integer> selectIndex2 = indexs
				.get(FootballLotteryConstants.L306) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L306);
		// 胜分差
		final List<Integer> selectIndex3 = indexs
				.get(FootballLotteryConstants.L308) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L308);
		// 大小分
		final List<Integer> selectIndex4 = indexs
				.get(FootballLotteryConstants.L309) == null ? new ArrayList<Integer>()
				: indexs.get(FootballLotteryConstants.L309);
		// 原因是因为按钮的onclick时候需要final的selectIndex1，所以这里需要从新将集合存储到Map中。用于引用传递，防止数据丢失
		indexs.put(FootballLotteryConstants.L307, selectIndex1);
		indexs.put(FootballLotteryConstants.L306, selectIndex2);
		indexs.put(FootballLotteryConstants.L308, selectIndex3);
		indexs.put(FootballLotteryConstants.L309, selectIndex4);
		// 半全场中所使用的view
		View childView = View.inflate(mContext,
				R.layout.dialog_item_basketball_lottery_hhgg, null);
		layoutView.addView(childView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// ((TextView)
		// childView.findViewById(R.id.txtView_football_lottery_dialog_hhgg_allow1)).setText(match.getLetScore());
		// ((TextView)
		// childView.findViewById(R.id.txtView_football_dialog_bqc_desc)).setText(Html.fromHtml(mContext.getResources().getString(R.string.football_dialog_bqc_html)));
		// ((TextView)
		// childView.findViewById(R.id.txtView_football_dialog_bf_desc)).setText(Html.fromHtml(mContext.getResources().getString(R.string.football_dialog_bf_html)));
		// ((TextView)
		// childView.findViewById(R.id.txtView_football_dialog_jqs_desc)).setText(Html.fromHtml(mContext.getResources().getString(R.string.football_dialog_zjqs_html)));
		// 胜平负
		// Button buttonsSpf[] = new Button[] { ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_hhgg_spf_0)),
		// ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_hhgg_spf_1)),
		// ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_hhgg_spf_2))
		// };
		LinearLayout[] mBasketballSPFViews = new LinearLayout[] {
				(LinearLayout) childView
						.findViewById(R.id.layoutView_basketballottery_spf_left),
				(LinearLayout) childView
						.findViewById(R.id.layoutView_basketballottery_spf_right) };
		// 让球胜平负
		LinearLayout[] mBasketballrSPFViews = new LinearLayout[] {
				(LinearLayout) childView
						.findViewById(R.id.layoutView_basketballottery_left),
				(LinearLayout) childView
						.findViewById(R.id.layoutView_basketballottery_right) };
		// Button buttonsrSpf[] = new Button[] { ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_hhgg_rspf_0)),
		// ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_hhgg_rspf_1)),
		// ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_hhgg_rspf_2))
		// };
		// 胜分差
		Button buttonsBf[] = new Button[] {
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_1)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_2)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_3)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_4)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_5)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_6)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_7)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_8)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_9)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_10)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_11)),
				((Button) childView
						.findViewById(R.id.btnView_basketballlottery_sfc_12)) };
		// 大小分
		// Button buttonsJqs[] = new Button[] { ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_dx_1)),
		// ((Button)
		// childView.findViewById(R.id.btnView_football_lottery_dialog_dx_2)) };
		LinearLayout[] mBasketballDXViews = new LinearLayout[] {
				(LinearLayout) childView
						.findViewById(R.id.layoutView_basketballottery_dx_left),
				(LinearLayout) childView
						.findViewById(R.id.layoutView_basketballottery_dx_right) };

		if (!StringUtil.isEmpty(sfSp)) {
			if (sfSp.indexOf("@") != -1) {
				String[] bfArr = sfSp.split("\\@");
				int spIndex = 0;
				for (String str : bfArr) {
					TextView spDecTextView = (TextView) mBasketballSPFViews[spIndex]
							.getChildAt(0);
					TextView spTextView = (TextView) mBasketballSPFViews[spIndex]
							.getChildAt(1);
					spDecTextView.setText(str.split("\\_")[0]);
					spTextView.setText(str.split("\\_")[1]);
					mBasketballSPFViews[spIndex].setTag(spIndex);
					mBasketballSPFViews[spIndex]
							.setOnClickListener(new OnClickListener() {
								//
								@Override
								public void onClick(View v) {
									Integer tag = (Integer) v.getTag();
									// 如果此button原来已经选择、且未取消，那么现在让其显示
									if (tag != null) {
										if (selectIndex1.contains(tag)) {
											((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.color_gray_secondary));
											((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(mContext.getResources().getColor(R.color.color_gray_indicator));
											selectIndex1.remove(tag);
											v.setSelected(false);
										} else {
											selectIndex1.add(tag);
											v.setSelected(true);
											((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.color_white));
											((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(mContext.getResources().getColor(R.color.color_white));
										}
									}
								}
							});
					// mBasketballSPFViews[spIndex].setSelected(false);
					if (selectIndex1 != null) {
						if (selectIndex1.contains(new Integer(spIndex))) {
							mBasketballSPFViews[spIndex].setSelected(true);
						}
					}
					spIndex++;
				}
			}
		}

		if (!StringUtil.isEmpty(rsfSp)) {
			if (rsfSp.indexOf("@") != -1) {
				String[] bfArr = rsfSp.split("\\@");
				String[] rArr = bfArr[0].split("\\|");
				int spIndex = 0;
				for (String str : bfArr) {
					TextView spDecTextView = (TextView) mBasketballrSPFViews[spIndex]
							.getChildAt(0);
					TextView spTextView = (TextView) mBasketballrSPFViews[spIndex]
							.getChildAt(1);
					spDecTextView.setText(rArr[1].split("\\_")[0]);
					spTextView.setText(str.split("\\_")[1]);
					mBasketballrSPFViews[spIndex].setTag(spIndex);
					mBasketballrSPFViews[spIndex]
							.setOnClickListener(new OnClickListener() {
								//
								@Override
								public void onClick(View v) {
									Integer tag = (Integer) v.getTag();
									// 如果此button原来已经选择、且未取消，那么现在让其显示
									if (tag != null) {
										if (selectIndex2.contains(tag)) {
											((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.color_gray_secondary));
											((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(mContext.getResources().getColor(R.color.color_gray_indicator));
											selectIndex2.remove(tag);
											v.setSelected(false);
										} else {
											selectIndex2.add(tag);
											v.setSelected(true);
											((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.color_white));
											((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(mContext.getResources().getColor(R.color.color_white));
										}
//										
//										
//										
//										
//										
//										
//										String[] str = ((Button) v).getText()
//												.toString().split("\\n");
//										if (selectIndex2.contains(tag)) {
//											selectIndex2.remove(tag);
//											v.setSelected(false);
//											((Button) v).setText(Html
//													.fromHtml("<font color='#000000'>"
//															+ str[0]
//															+ "</font><br><small><font color='#999999'>"
//															+ str[1]
//															+ "</font></small>"));
//										} else {
//											selectIndex2.add(tag);
//											v.setSelected(true);
//											((Button) v).setText(Html
//													.fromHtml("<font color='#FFFFFF'>"
//															+ str[0]
//															+ "</font><br><small><font color='#FFFFFF'>"
//															+ str[1]
//															+ "</font></small>"));
//										}
										// if (selectIndex2.contains(tag)) {
										// selectIndex2.remove(tag);
										// v.setSelected(false);
										// } else {
										// selectIndex2.add(tag);
										// v.setSelected(true);
										// }
									}
								}
							});
					// mBasketballrSPFViews[spIndex].setSelected(false);
					if (selectIndex2 != null) {
						if (selectIndex2.contains(new Integer(spIndex))) {
							mBasketballrSPFViews[spIndex].setSelected(true);
						}
					}
					if (spIndex == (bfArr.length - 1)) {
						spDecTextView
								.setText(rArr[1].split("\\_")[0] + rArr[0]);
					}
					spIndex++;
				}
			}
		}

		// //////////////////胜分差/////////////////////////////
		if (sfcSp.indexOf("@") != -1) {
			String[] bfArr = sfcSp.split("\\@");
			for (int index3 = 0; index3 < bfArr.length; index3++) {
				Spanned textHtml = null;
				if (bfArr[index3].split("\\_").length > 1) {
					textHtml = Html.fromHtml(bfArr[index3].split("\\_")[0]
							+ "<br><small><font color='#"+(buttonsBf[index3].isSelected()?"FFFFFF":"999999")+"'>" + bfArr[index3].split("\\_")[1]
							+ "</font></small>");
				}
				buttonsBf[index3].setText(textHtml);
				buttonsBf[index3].setTag(index3);
				buttonsBf[index3].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer tag = (Integer) v.getTag();
						// 如果此button原来已经选择、且未取消，那么现在让其显示
						if (tag != null) {
							String[] str = ((Button) v).getText().toString()
									.split("\\n");
							if (selectIndex3.contains(tag)) {
								selectIndex3.remove(tag);
								v.setSelected(false);
								((Button) v).setText(Html
										.fromHtml("<font color='#000000'>"
												+ str[0]
												+ "</font><br><small><font color='#999999'>"
												+ str[1] + "</font></small>"));
							} else {
								selectIndex3.add(tag);
								v.setSelected(true);
								((Button) v).setText(Html
										.fromHtml("<font color='#FFFFFF'>"
												+ str[0]
												+ "</font><br><small><font color='#FFFFFF'>"
												+ str[1] + "</font></small>"));
							}
							// if (selectIndex3.contains(tag)) {
							// selectIndex3.remove(tag);
							// v.setSelected(false);
							// } else {
							// selectIndex3.add(tag);
							// v.setSelected(true);
							// }
						}
					}
				});
				// 判断改控件是否正在选择，如果是那么改其UI状态为已选择
				if (selectIndex3 != null) {
					if (selectIndex3.contains(new Integer(index3))) {
						buttonsBf[index3].setSelected(true);
					}
				}
			}

		}

		// //////////////////大小分/////////////////////////////
		if (!StringUtil.isEmpty(dxSp)) {
			if (dxSp.indexOf("@") != -1) {
				String[] bfArr = dxSp.split("\\@");
				String[] rArr = bfArr[0].split("\\|");
				int spIndex = 0;
				for (String str : bfArr) {
					TextView spDecTextView = (TextView) mBasketballDXViews[spIndex]
							.getChildAt(0);
					TextView spTextView = (TextView) mBasketballDXViews[spIndex]
							.getChildAt(1);
					spDecTextView.setText(rArr[1].split("\\_")[0]);
					spTextView.setText(str.split("\\_")[1]);
					mBasketballDXViews[spIndex].setTag(spIndex);
					mBasketballDXViews[spIndex]
							.setOnClickListener(new OnClickListener() {
								//
								@Override
								public void onClick(View v) {
									Integer tag = (Integer) v.getTag();
									//TODO 如果此button原来已经选择、且未取消，那么现在让其显示
									if (tag != null) {
										if (selectIndex4.contains(tag)) {
											selectIndex4.remove(tag);
											v.setSelected(false);
											((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.color_gray_secondary));
											((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(mContext.getResources().getColor(R.color.color_gray_indicator));
										} else {
											selectIndex4.add(tag);
											v.setSelected(true);
											((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(mContext.getResources().getColor(R.color.color_white));
											((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(mContext.getResources().getColor(R.color.color_white));
										}
									}
								}
							});
					// mBasketballrSPFViews[spIndex].setSelected(false);
					if (selectIndex4 != null) {
						if (selectIndex4.contains(new Integer(spIndex))) {
							mBasketballDXViews[spIndex].setSelected(true);
						}
					}
					if (spIndex == 0) {
						spDecTextView.setText(rArr[1].split("\\_")[0] + ">"
								+ rArr[0]);
					} else {
						spDecTextView.setText(str.split("\\_")[0] + "<"
								+ rArr[0]);
					}
					spIndex++;
				}
			}
		}

		mDialog.show();
	}

	/**
	 * hide dialog
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	public void hide() {
		mDialog.hide();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnView_football_lottery_dialog_canl:
			hide();

			if (mCallBack != null) {
				mCallBack.OnCanlClick();
			}
			break;

		case R.id.btnView_football_lottery_dialog_ok:
			// dialog中得数据集，保存了当前所有的操作结果，用于返回调用者
			Object object = mOkView.getTag();
			if (mCallBack != null) {
				mCallBack.OnOKClick(object, tag);
			}
			hide();

			break;
		default:
			break;
		}
	}

}
