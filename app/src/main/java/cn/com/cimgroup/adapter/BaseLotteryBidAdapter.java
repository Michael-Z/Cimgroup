package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.bean.AgainstDetails;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.dailog.LoadingDialogProgress;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.JsonHelper;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 足彩投注adapter
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月6日
 */
@SuppressLint("HandlerLeak")
public abstract class BaseLotteryBidAdapter extends BaseExpandableListAdapter
		implements OnClickListener {
	/** 加载Dialog */
	private LoadingDialogProgress mDialog = null;

	/** 上下文 **/
	protected Context mContext;

	/** 一级分组 **/
	List<String> mGroupSpiltTimes = new ArrayList<String>();

	/** 二级分组 **/
	List<List<Match>> mChildMatchs = new ArrayList<List<Match>>();

	List<List<LotterySp>> sps = new ArrayList<List<LotterySp>>();

	public BaseLotteryBidAdapter(Context context) {
		this.mContext = context;
	}

	/**
	 * 获取足彩基本信息
	 * 
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public List<List<Match>> getlotterybidMatchsInfos() {
		return mChildMatchs == null ? new ArrayList<List<Match>>()
				: mChildMatchs;
	}

	public List<String> getSpiltTimes() {
		return mGroupSpiltTimes == null ? new ArrayList<String>()
				: mGroupSpiltTimes;
	}

	@Override
	public int getGroupCount() {
		return mGroupSpiltTimes.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return mChildMatchs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return mGroupSpiltTimes.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return mChildMatchs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewGroupHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_lotterybid_groupview, null);
			holder = new ViewGroupHolder();
			holder.mGroupTextView = (TextView) convertView
					.findViewById(R.id.txtView_lotterybid_groupview_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewGroupHolder) convertView.getTag();
		}
		holder.mGroupTextView.setText(mGroupSpiltTimes.get(groupPosition) + " " + mChildMatchs.get(groupPosition).size() + "场比赛可投注");
		return convertView;
	}

	public abstract ViewSuperChildHolder getChildHolder();

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		ViewSuperChildHolder mHolder = null;
		if (view == null) {
			view = View.inflate(mContext, R.layout.item_lotterybid_childview,
					null);
			mHolder = getChildHolder();
			mHolder.id_match_time = (TextView) view
					.findViewById(R.id.id_match_time);
			mHolder.id_match_name = (TextView) view
					.findViewById(R.id.id_match_name);
			mHolder.id_match_end_time = (TextView) view
					.findViewById(R.id.id_match_end_time);
			mHolder.id_host_name = (TextView) view
					.findViewById(R.id.id_host_name);
			mHolder.id_guest_name = (TextView) view
					.findViewById(R.id.id_guest_name);
			mHolder.mMatchTypeView = (LinearLayout) view
					.findViewById(R.id.layoutView_match_type);
			mHolder.id_vs = (TextView) view.findViewById(R.id.id_vs);
			mHolder.id_bottom = view.findViewById(R.id.id_bottom);
			mHolder.id_show_detail_image = (ImageView) view
					.findViewById(R.id.id_show_detail_image);
			mHolder.id_show_detail_layout = (LinearLayout) view
					.findViewById(R.id.id_show_detail_layout);
			mHolder.id_detail_layout = (LinearLayout) view
					.findViewById(R.id.id_detail_layout);
			mHolder.id_detail_history_info = (TextView) view
					.findViewById(R.id.id_detail_history_info);
			mHolder.id_detail_near_info = (TextView) view
					.findViewById(R.id.id_detail_near_info);
			mHolder.id_detail_average_info = (TextView) view
					.findViewById(R.id.id_detail_average_info);
			mHolder.id_detail_average_s = (TextView) view.findViewById(R.id.id_detail_average_s);
			mHolder.id_detail_average_p = (TextView) view.findViewById(R.id.id_detail_average_p);
			mHolder.id_detail_average_f = (TextView) view.findViewById(R.id.id_detail_average_f);
			
			mHolder.id_to_details = (TextView) view
					.findViewById(R.id.id_to_details);
			mHolder.id_show_detail_image = (ImageView) view
					.findViewById(R.id.id_show_detail_image);
			mHolder.id_to_details = (TextView) view
					.findViewById(R.id.id_to_details);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewSuperChildHolder) view.getTag();
		}
		final Match match = (Match) getChild(groupPosition, childPosition);
		String stopSaleTime = "";
		String matchTimeIndex = "";
		if (match.getGameNo().equals(GlobalConstants.TC_JCZQ)
				|| match.getGameNo().equals(GlobalConstants.TC_JCLQ))
			matchTimeIndex = match.getMatchSort();
		else
			matchTimeIndex = match.getWeek();
		mHolder.id_bottom.setVisibility(isLastChild ? View.VISIBLE : View.GONE);
		mHolder.id_match_time.setText(matchTimeIndex);
		mHolder.id_match_name.setText(match.getLeagueShort());
		stopSaleTime = DateUtil.getTimeInMillisToStr(
				match.getMatchTimeDate() + "").split(" ")[1].substring(0, 5)
				+ " 截止";
		stopSaleTime = DateUtil.getTimeInMillisToStr(
				match.getMatchSellOutTime() + "").split(" ")[1].substring(0, 5)
				+ " 截止";
		// 截止时间
		mHolder.id_match_end_time.setText(stopSaleTime);
		if (match.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
			mHolder.id_host_name.setText("(客)" + match.getGuestTeamName());
			mHolder.id_guest_name.setText("(主)" + match.getHomeTeamName());
		} else {
			mHolder.id_host_name.setText(match.getHomeTeamName());
			mHolder.id_guest_name.setText(match.getGuestTeamName());
		}
		if (match.getAgainstDetails() != null) {
			AgainstDetails details = match.getAgainstDetails();
			setAgainstDetails(details, mHolder.id_detail_history_info,
					mHolder.id_detail_near_info,
					mHolder.id_detail_average_info,
					mHolder.id_detail_average_s, mHolder.id_detail_average_p,
					mHolder.id_detail_average_f);

		} else {
			match.setShow(false);
		}
		final TextView text1 = mHolder.id_detail_history_info;
		final TextView text2 = mHolder.id_detail_near_info;
		final TextView text3 = mHolder.id_detail_average_info;
		final TextView texts = mHolder.id_detail_average_s;
		final TextView textp = mHolder.id_detail_average_p;
		final TextView textf = mHolder.id_detail_average_f;

		if (match.isShow()) {
			mHolder.id_detail_layout.setVisibility(View.VISIBLE);
			mHolder.id_show_detail_image.setRotationX(180);
		} else{
			mHolder.id_show_detail_image.setRotationX(0);
			mHolder.id_detail_layout.setVisibility(View.GONE);
		}
		final LinearLayout layout = mHolder.id_detail_layout;
		final ImageView image = mHolder.id_show_detail_image;
		if (!match.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
			mHolder.id_show_detail_image.setVisibility(View.VISIBLE);
			mHolder.id_show_detail_layout
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (match.getLeagueCode().equals("0")) {
								ToastUtil.shortToast(mContext, "没有该比赛的对阵信息");
								return;
							}
							if (!match.isShow()) {
								if (match.getAgainstDetails() == null) {
									if (mDialog == null) {
										mDialog = LoadingDialogProgress
												.newIntance(true);
									}
									mDialog.show(((FragmentActivity) mContext)
											.getSupportFragmentManager(),
											"dialogProgress");
									Controller
											.getInstance()
											.getLeagueDetail(
													GlobalConstants.URL_GET_LEAGUE_DETAILS,
													match.getLeagueCode(),
													new CallBack() {
														public void getLeagueDetailSuccess(
																String json) {
															setDataAndSendHandler(groupPosition,
																	childPosition,
																	text1,text2,text3,
																	texts,textp,textf,
																	layout,image, json);
														}
													});
								} else {
									layout.setVisibility(View.VISIBLE);
									image.setRotationX(180);
									match.setShow(true);
								}
							} else {
								layout.setVisibility(View.GONE);
								image.setRotationX(0);
								match.setShow(false);
							}
						}
					});
			mHolder.id_to_details
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							layout.setVisibility(View.GONE);
							image.setRotationX(0);
							match.setShow(false);
							ScoreMatchBean bean = new ScoreMatchBean();
							bean.setMatchId(match.getLeagueCode());
							bean.setMatchName(match.getMatchName());
							bean.setHostName(match.getHomeTeamName());
							bean.setGuestName(match.getGuestTeamName());
							bean.setMatchTime(DateUtil
									.getTimeInMillisToStr(match
											.getMatchTimeDate() + ""));
							bean.setStatus("0");
							bean.setGuestFullGoals("0");
							bean.setHostFullGoals("0");
							String week = match.getWeek();
							if (week.equals("1")) {
								week = "周一";
							} else if (week.equals("2")) {
								week = "周二";
							} else if (week.equals("3")) {
								week = "周三";
							} else if (week.equals("4")) {
								week = "周四";
							} else if (week.equals("5")) {
								week = "周五";
							} else if (week.equals("6")) {
								week = "周六";
							} else {
								week = "周日";
							}
							bean.setMatchTimes(week + match.getMatchSort());

							Intent intent = new Intent(mContext,
									ScoreDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("bean", bean);
							intent.putExtras(bundle);
							intent.putExtra("gameNo", match.getGameNo());
							intent.putExtra("tabIndex", "1");
							intent.putExtra("isHideJump", true);
							mContext.startActivity(intent);
						}
					});
		} else
			mHolder.id_show_detail_image.setVisibility(View.GONE);

		return view;
	}
	/**
	 * 解析数据 ，保存并发送Handler
	 * @param groupPosition
	 * @param childPosition
	 * @param text1
	 * @param text2
	 * @param text3
	 * @param texts
	 * @param textp
	 * @param textf
	 * @param layout
	 * @param image
	 * @param json
	 */
	private void setDataAndSendHandler(final int groupPosition,
			final int childPosition, final TextView text1,
			final TextView text2, final TextView text3, final TextView texts,
			final TextView textp, final TextView textf,
			final LinearLayout layout, final ImageView image, String json) {
		Match match = (Match) getChild(groupPosition, childPosition);
		AgainstDetails details = JsonHelper.jsonToObject(json,
				AgainstDetails.class);
		match.setAgainstDetails(details);
		Message message = mHandler.obtainMessage();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("text1", text1);
		map.put("text2", text2);
		map.put("text3", text3);
		map.put("texts", texts);
		map.put("textp", textp);
		map.put("textf", textf);
		map.put("details", details);
		map.put("layout", layout);
		map.put("image", image);
		message.obj = map;
		message.sendToTarget();
		match.setShow(true);
	};
	/**
	 * 赋值对阵详情
	 * @param details
	 * @param id_detail_history_info
	 * @param id_detail_near_info
	 * @param id_detail_average_info
	 * @param id_detail_average_s
	 * @param id_detail_average_p
	 * @param id_detail_average_f
	 */
	private void setAgainstDetails(AgainstDetails details,
			TextView id_detail_history_info, TextView id_detail_near_info,
			TextView id_detail_average_info,TextView id_detail_average_s,TextView id_detail_average_p,TextView id_detail_average_f) {
		Spanned bathSide ;
		if (!TextUtils.isEmpty(details.getBothSideResult())) {
			String[] bothSide = details.getBothSideResult().split("_");
			int times=Integer.parseInt(bothSide[1])+Integer.parseInt(bothSide[2])+Integer.parseInt(bothSide[3]);
			
			bathSide = Html.fromHtml("近" + times + "次交战，" + bothSide[0]
					+ "  <font color = '" + getRedColor() + "'>" + bothSide[1]
					+ "</font>  胜  <font color = '" + getGreenColor() + "'>  "
					+ bothSide[2] + "  </font>平<font color = '" + getBlueColor()
					+ "'>  " + bothSide[3] + "  </font>负</font>");
		}else {
			bathSide = Html.fromHtml("<font  color='"+ mContext.getResources().getColor(R.color.color_kafei_line)
					+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暂无交战记录</font>");
		}

		id_detail_history_info.setText(bathSide);
		Spanned nearInfo;
		if (!TextUtils.isEmpty(details.getHostNearResult())
				&& !TextUtils.isEmpty(details.getGuestNearResult())) {
			String[] hostNear = details.getHostNearResult().split("_");
			String[] guestNear = details.getGuestNearResult().split("_");
			nearInfo = Html.fromHtml("<font  color='"
					+ mContext.getResources().getColor(R.color.color_gray_indicator)
					+ "'>主队" + hostNear[1] + "胜"
					+ hostNear[2] + "平" + hostNear[3] + "负，客队" + guestNear[1]
					+ "胜" + guestNear[2] + "平" + guestNear[3]
					+ "负</font>");
		}else {
			nearInfo = Html.fromHtml("<font  color='"+ mContext.getResources().getColor(R.color.color_gray_indicator)
					+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暂无交战记录</font>");
		}
		id_detail_near_info.setText(nearInfo);
		Spanned avgInfo ;
		if (!TextUtils.isEmpty(details.getAvgOdds())) {
			id_detail_average_info.setVisibility(View.GONE);
			id_detail_average_s.setVisibility(View.VISIBLE);
			id_detail_average_p.setVisibility(View.VISIBLE);
			id_detail_average_f.setVisibility(View.VISIBLE);
			String[] avgOdds = details.getAvgOdds().split("_");
			id_detail_average_s.setText(avgOdds[0]);
			id_detail_average_p.setText(avgOdds[1]);
			id_detail_average_f.setText(avgOdds[2]);
		}else {
			id_detail_average_info.setVisibility(View.VISIBLE);
			id_detail_average_s.setVisibility(View.GONE);
			id_detail_average_p.setVisibility(View.GONE);
			id_detail_average_f.setVisibility(View.GONE);
			avgInfo = Html.fromHtml("<font  color='"+ mContext.getResources().getColor(R.color.color_gray_indicator)
					+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暂无赔率信息</font>");
			id_detail_average_info.setText(avgInfo);
		}

		

	}
	
	/**返回红色*/
	private int getRedColor (){
		return mContext.getResources().getColor(R.color.color_red);
	}
	/**返回绿色*/
	private int getGreenColor (){
		return mContext.getResources().getColor(R.color.color_green_warm);
	}
	/**返回蓝色*/
	private int getBlueColor (){
		return mContext.getResources().getColor(R.color.color_blue);
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			Map<String, Object> map = (Map<String, Object>) msg.obj;
			setAgainstDetails(((AgainstDetails) map.get("details")),
					((TextView) map.get("text1")),
					((TextView) map.get("text2")),
					((TextView) map.get("text3")),((TextView) map.get("texts")),((TextView) map.get("textp")),((TextView) map.get("textf")));
			((LinearLayout) map.get("layout")).setVisibility(View.VISIBLE);
			((ImageView) map.get("image")).setRotationX(180);
			mDialog.dismiss();
		};
	};

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 * 比赛天数集合
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2016年1月4日
	 */
	class ViewGroupHolder {

		protected TextView mGroupTextView;
	}

	/**
	 * 某一天的所有比赛
	 */
	class ViewSuperChildHolder {

		/** 比赛时间 **/
		protected TextView id_match_time;
		/** 赛事名称 **/
		protected TextView id_match_name;
		/** 比赛结束时间 **/
		protected TextView id_match_end_time;
		protected LinearLayout mMatchTypeView;
		/** 主对 **/
		protected TextView id_host_name;
		/** 客对 **/
		protected TextView id_guest_name;

		protected TextView id_vs;
		/** 底部布局 */
		private View id_bottom;
		/** 是否显示更多信息Img */
		private ImageView id_show_detail_image;

		/** 点击展开详情信息布局 */
		private LinearLayout id_show_detail_layout;
		/** 详细信息布局 */
		private LinearLayout id_detail_layout;

		/** 历史交战 */
		private TextView id_detail_history_info;
		/** 近期交战 */
		private TextView id_detail_near_info;
		/** 平均赔率 没有值时显示*/
		private TextView id_detail_average_info;
		/** 平均赔率 胜*/
		private TextView id_detail_average_s;
		/** 平均赔率 平*/
		private TextView id_detail_average_p;
		/** 平均赔率 负*/
		private TextView id_detail_average_f;
		
		
		
		/** 跳转查看赛事分析 */
		private TextView id_to_details;

	}

	/**
	 * 获取childPosition在所有结果集中得位置
	 * 
	 * @Description:
	 * @param groupPosition
	 *            一级分组position
	 * @param childPosition
	 *            二级分支position
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月4日
	 */
	protected int getSelectClidIndexToAllData(int groupPosition,
			int childPosition) {
		int selectDataIndex = 0;
		if (groupPosition - 1 == -1) {
			selectDataIndex = childPosition;
		} else {
			for (int i = 0; i < groupPosition; i++) {
				selectDataIndex += getChildrenCount(i);
			}
			selectDataIndex += childPosition;
		}
		return selectDataIndex;
	}

}
