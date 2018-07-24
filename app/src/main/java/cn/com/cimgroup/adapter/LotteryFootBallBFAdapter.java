package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.DialogType;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.OnClickEvent;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 足彩 比分Adapter
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年12月31日
 */
public class LotteryFootBallBFAdapter extends BaseLotteryBidOnlyAdapter implements OnClickListener,
		OnClickEvent {

	// 投注选项dialog
	private FootballLotteryWayDailog mDialog = null;
	
	private MatchSearchType mSearchType;

	public LotteryFootBallBFAdapter(Context context, MatchSearchType searchType) {
		super(context);
		mDialog = new FootballLotteryWayDailog(mContext, this);
		mSearchType = searchType;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//		Tick tick = new Tick();
//		tick.begin();
		convertView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
//		tick.end("convertView");
		// 获取控件结果集 //父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_footballlottery_click_open, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mOpenClickView = (TextView) convertView.findViewById(R.id.txtView_footballlottery_onclick_open);
		}
//		tick.end("addview");
		// 获取该条数据在整个数据集中得索引
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		
		Match match = mChildMatchs.get(groupPosition).get(childPosition);
		String bfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBF, mSearchType);
		
		mHolder.mOpenClickView.setVisibility(View.VISIBLE);
		if (StringUtil.isEmpty(bfSp)) {
			mHolder.mOpenClickView.setVisibility(View.GONE);
		} else {
			// 判断是否在投注选项中进行选择，如果选择了，那么在item进行展示
			if (mSelectItemIndexs.containsKey(dataPosition)) {
				StringBuilder builder = new StringBuilder();
				// 获取投注选项的索引
				List<Integer> selectWayIndex = mSelectItemIndexs.get(dataPosition);
				if (selectWayIndex != null && selectWayIndex.size() > 0) {
					for (int i = 0; i < selectWayIndex.size(); i++) {
						if(bfSp.indexOf("@") != -1){
							String[] bfArr = bfSp.split("\\@");
							if(bfArr[selectWayIndex.get(i)].split("\\_").length > 1) {
								builder.append(bfArr[selectWayIndex.get(i)].split("\\_")[0] + ",");
							}
						} else {
							mHolder.mOpenClickView.setVisibility(View.GONE);
						}
					}
					builder.delete(builder.length() - 1, builder.length());
					mHolder.mOpenClickView.setText(builder.toString());
					mHolder.mOpenClickView.setSelected(true);
				} else {
					mHolder.mOpenClickView.setText(mContext.getString(R.string.football_open_click));
				}
				
			} else {
				mHolder.mOpenClickView.setText(mContext.getString(R.string.football_open_click));
				mHolder.mOpenClickView.setSelected(false);
			}
			mHolder.mOpenClickView.setOnClickListener(this);
		}
		match.setPosition(dataPosition);
		mHolder.mOpenClickView.setTag(match);
//		tick.end("end");

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
		Match match = (Match) v.getTag();
		Integer dataPosition = match.getPosition();
		
		if (dataPosition != null) {
			if (mSelectItemIndexs.get(dataPosition) == null) {
				if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
					ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
				} else {
					mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
				}
			}
			// 弹出选择投注选项dialog
			mDialog.showDialog(DialogType.BF, mSelectItemIndexs.get(dataPosition), dataPosition, match, mSearchType);
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
					if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
						ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
					} else {
						mSelectItemIndexs.put(dataPosition, indexs);
					}
				}
				notifyDataSetChanged();
			}
		}
		((LotteryFootballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs.size());
	}

	@Override
	public void OnCanlClick() {
		notifyDataSetChanged();
	}

	@Override
	public void OnOtherClick() {

	}

}
