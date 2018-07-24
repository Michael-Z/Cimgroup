package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryBasketballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchFootballState;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.DialogType;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.OnClickEvent;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.Tick;

/**
 * 篮球 胜分差
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016-6-3
 */
public class LotteryBasketBallSFCAdapter extends BaseLotteryBidOnlyAdapter
		implements OnClickListener, OnClickEvent {

	// 投注选项dialog
	private FootballLotteryWayDailog mDialog = null;

	private MatchSearchType mSearchType;

	public LotteryBasketBallSFCAdapter(Context context,
			MatchSearchType searchType) {
		super(context);
		mDialog = new FootballLotteryWayDailog(mContext, this);
		mSearchType = searchType;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = super.getChildView(groupPosition, childPosition,
				isLastChild, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext,
					R.layout.include_footballlottery_click_open, null);
			matchTypeLayout.addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mOpenClickView = (TextView) convertView
					.findViewById(R.id.txtView_footballlottery_onclick_open);
		}
		// 获取该条数据在整个数据集中得索引
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition,
				childPosition);

		Match match = mChildMatchs.get(groupPosition).get(childPosition);
		String sfcSp = FootballLotteryTools.getMatchSp(
				match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLSFC,
				mSearchType);

		mHolder.mOpenClickView.setVisibility(View.VISIBLE);
		if (StringUtil.isEmpty(sfcSp)) {
			mHolder.mOpenClickView.setVisibility(View.GONE);
		} else {
			// 判断是否在投注选项中进行选择，如果选择了，那么在item进行展示
			if (mSelectItemIndexs.containsKey(dataPosition)) {
				if (mSelectItemIndexs.get(dataPosition).size() == 0) {
					mHolder.mOpenClickView.setText(mContext
							.getString(R.string.football_open_click));
					mHolder.mOpenClickView.setSelected(false);
					mSelectItemIndexs.remove(dataPosition);
				} else {
					StringBuilder builder = new StringBuilder();
					// 获取投注选项的索引
					List<Integer> selectWayIndex = mSelectItemIndexs
							.get(dataPosition);
					if (selectWayIndex != null && selectWayIndex.size() > 0) {
						for (int i = 0; i < selectWayIndex.size(); i++) {
							if (sfcSp.indexOf("@") != -1) {
								String[] bfArr = sfcSp.split("\\@");
								if (bfArr[selectWayIndex.get(i)].split("\\_").length > 1) {
									builder.append(bfArr[selectWayIndex.get(i)]
											.split("\\_")[0] + ",");
								}
							} else {
								mHolder.mOpenClickView.setVisibility(View.GONE);
							}
						}
						builder.delete(builder.length() - 1, builder.length());
						mHolder.mOpenClickView.setText(builder.toString());
					} else {
						mHolder.mOpenClickView.setText(mContext
								.getString(R.string.football_open_click));
					}
					mHolder.mOpenClickView.setSelected(true);
				}
	
			} else {
				mHolder.mOpenClickView.setText(mContext
						.getString(R.string.football_open_click));
				mHolder.mOpenClickView.setSelected(false);
			}
			mHolder.mOpenClickView.setOnClickListener(this);
		}
		
		match.setPosition(dataPosition);
		mHolder.mOpenClickView.setTag(match);

		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		/** 胜平负 **/
		private TextView mOpenClickView;
	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		return new ViewChildHolder();
	}

	@Override
	public void onClick(View v) {
		// 获取该条数据在整个数据集中得索引
		// Integer dataPosition = (Integer) v.getTag();
		Match match = (Match) v.getTag();
		Integer dataPosition = match.getPosition();
		// 最大选择场次为GlobalConstants.MAX_SELECT_FOOTBALL2BASKBALL_SUM
		// if (mSelectItemIndexs.size() ==
		// GlobalConstants.MAX_SELECT_FOOTBALL2BASKBALL_SUM) {
		// if (!mSelectItemIndexs.containsKey(dataPosition)) {
		// UAToast.showToast(mContext,
		// mContext.getString(R.string.max_select_football_basketball_sum,
		// GlobalConstants.MAX_SELECT_FOOTBALL2BASKBALL_SUM));
		// notifyDataSetChanged();
		// return;
		// }
		// }
		if (dataPosition != null) {
			if (mSelectItemIndexs.get(dataPosition) == null) {
				mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
			}
			// 弹出选择投注选项dialog
			mDialog.showDialog(DialogType.SFC,
					mSelectItemIndexs.get(dataPosition), dataPosition, match,
					mSearchType);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void OnOKClick(Object... objects) {
		// 将原来的选择的值清空，从新赋值
		if (objects != null && objects.length == 2) {
			Integer dataPosition = (Integer) objects[1];
			// 选择的索引集合
			List<Integer> indexs = (List<Integer>) objects[0];
			if (indexs != null) {
				mSelectItemIndexs.remove(dataPosition);
				if (indexs.size() != 0) {
					mSelectItemIndexs.put(dataPosition, indexs);
				}
				notifyDataSetChanged();
			}
		}
		((LotteryBasketballActivity) mContext)
				.changeSelectMatchCount2state(mSelectItemIndexs.size());
	}

	@Override
	public void OnCanlClick() {
		notifyDataSetChanged();
	}

	@Override
	public void OnOtherClick() {

	}

}
