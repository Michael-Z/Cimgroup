package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryOldFootballActivity;
import cn.com.cimgroup.xutils.DateUtil;

/**
 * 足彩 进球数adapter
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年12月31日
 */
public class LotteryOldFootBallJQSAdapter extends BaseLotteryBidOnlyAdapter implements OnClickListener {

	public LotteryOldFootBallJQSAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		convertView = super.getGroupView(groupPosition, isExpanded, convertView, parent);
		ViewGroupHolder holder = (ViewGroupHolder) convertView.getTag();
		StringBuilder sb = new StringBuilder();
		sb.append(mContext.getResources().getString(R.string.record_tz_detail_qi,infos.get(1).getIssue()));
		sb.append(DateUtil.getTimeInMillisToStr(infos.get(1).getEndTime() + "") + "截止");
		holder.mGroupTextView.setText(sb.toString());
		return convertView;
	}

	@SuppressLint("UseValueOf")
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		convertView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_oldfootball_jqs_select, null);
			// 初始化进球个数view
			mHolder.mFoolballNumbersView = new LinearLayout[] { ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select1)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select2)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select3)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select4)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select5)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select6)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select7)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select8)) };
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		// 获取该条在全部比赛中得索引位置
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		
		for (int i = 0; i < mHolder.mFoolballNumbersView.length; i++) {
//			// 赋值给每个进球个数view
			mHolder.mFoolballNumbersView[i].setTag(new Object[] { dataPosition, i });
			mHolder.mFoolballNumbersView[i].setOnClickListener(this);
			mHolder.mFoolballNumbersView[i].setSelected(false);
			// 判断是否已经选择、点击了该按钮，进行相应按钮状态改变
			if (mSelectItemIndexs.containsKey(dataPosition)) {
				List<Integer> selectWay = mSelectItemIndexs.get(dataPosition);
				if (selectWay != null && selectWay.contains(new Integer(i))) {
					mHolder.mFoolballNumbersView[i].setSelected(true);
				}
			}
		}

		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		/** 进球数view结合 **/
		private LinearLayout mFoolballNumbersView[];
	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		return new ViewChildHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtView_footballlottery_jqs_select1:
		case R.id.txtView_footballlottery_jqs_select2:
		case R.id.txtView_footballlottery_jqs_select3:
		case R.id.txtView_footballlottery_jqs_select4:
		case R.id.txtView_footballlottery_jqs_select5:
		case R.id.txtView_footballlottery_jqs_select6:
		case R.id.txtView_footballlottery_jqs_select7:
		case R.id.txtView_footballlottery_jqs_select8:
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 2) {
				// 获取该条数据在整个数据集中得索引
				Integer dataPosition = (Integer) obj[0];
				// 每个item的投注选项索引
				Integer index = (Integer) obj[1];
				// 获取该条在全部比赛中得索引位置
				if (mSelectItemIndexs.containsKey(dataPosition)) {
					if (mSelectItemIndexs.get(dataPosition).contains(index)) {
						mSelectItemIndexs.get(dataPosition).remove(index);
						if (mSelectItemIndexs.get(dataPosition).size() == 0) {
							mSelectItemIndexs.remove(dataPosition);
						}
					} else {
						mSelectItemIndexs.get(dataPosition).add(index);
					}
				} else {
					mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
					mSelectItemIndexs.get(dataPosition).add(index);
				}
				notifyDataSetChanged();
			}
			((LotteryOldFootballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
			notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

}
