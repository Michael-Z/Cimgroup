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
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 一场制胜
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-10-19
 */
public class LotteryOneWinAdapter extends BaseLotteryBidOnlyAdapter implements OnClickListener {

	private MatchSearchType mSearchType;
	
	public LotteryOneWinAdapter(Context context, MatchSearchType searchType) {
		super(context);
		mSearchType = searchType;
	}
	@SuppressLint("UseValueOf")
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		convertView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		// 获取控件结果集 , 父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_football_onewin_select, null);
			// 初始化进球个数view
			mHolder.mFoolballNumbersView = new LinearLayout[] {
					(LinearLayout) view
							.findViewById(R.id.linearView_footballlottery_onewin_spf_0),
					(LinearLayout) view
							.findViewById(R.id.linearView_footballlottery_onewin_spf_1),
					(LinearLayout) view
							.findViewById(R.id.linearView_footballlottery_onewin_spf_2) };
			((LinearLayout) view.findViewById(R.id.id_select_dan_layout)).setVisibility(View.GONE);
			mHolder.spfStop = (RelativeLayout) view.findViewById(R.id.layoutView_footballlottery_onewin_spf_stop);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		// 获取该条在全部比赛中得索引位置
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		
		Match match = mChildMatchs.get(groupPosition).get(childPosition);
		List<MatchAgainstSpInfo> spList = match.getMatchAgainstSpInfoList();
		String sp = FootballLotteryTools.getMatchSp(spList, GlobalConstants.TC_1CZS, mSearchType);
		
		if (sp == null) {
			mHolder.spfStop.setVisibility(View.VISIBLE);
		} else {
			for (int i = 0; i < mHolder.mFoolballNumbersView.length; i++) {
	//			// 赋值给每个进球个数view
				TextView spTextView = (TextView) mHolder.mFoolballNumbersView[i].getChildAt(1);
				if(sp.indexOf("@") != -1) {
					String[] jqsArr = sp.split("\\@");
					if(jqsArr[i].split("\\_").length > 1) {
						mHolder.spfStop.setVisibility(View.GONE);
						spTextView.setText(jqsArr[i].split("\\_")[1]);
					} else {
						mHolder.spfStop.setVisibility(View.VISIBLE);
						break;
					}
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
				} else {
					mHolder.spfStop.setVisibility(View.VISIBLE);
					break;
				}
			}
		}

		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		/** 进球数view结合 **/
		private LinearLayout mFoolballNumbersView[];
		private RelativeLayout spfStop;
	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		return new ViewChildHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
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
					if (mSelectItemIndexs.size() >= 5) {
						ToastUtil.shortToast(mContext, "最多可选5场比赛");
					} else {
						mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
						mSelectItemIndexs.get(dataPosition).add(index);
					}
				}
			}
			((LotteryFootballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
			notifyDataSetChanged();
			break;
		}

	}

}
