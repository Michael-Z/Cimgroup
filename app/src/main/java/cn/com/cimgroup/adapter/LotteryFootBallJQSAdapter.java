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
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 足彩 进球数adapter
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年12月31日
 */
public class LotteryFootBallJQSAdapter extends BaseLotteryBidOnlyAdapter implements OnClickListener {

	private MatchSearchType mSearchType;
	
	public LotteryFootBallJQSAdapter(Context context, MatchSearchType searchType) {
		super(context);
		mSearchType = searchType;
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
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_footballlottery_jqs_select, null);
			// 初始化进球个数view
			mHolder.mFoolballNumbersView = new LinearLayout[] { ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select1)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select2)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select3)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select4)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select5)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select6)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select7)), ((LinearLayout) view.findViewById(R.id.txtView_footballlottery_jqs_select8)) };
			mHolder.mFootballLayout = (LinearLayout) view.findViewById(R.id.layout_lotteryfootball_jqs_open);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		// 获取该条在全部比赛中得索引位置
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		
		Match match = mChildMatchs.get(groupPosition).get(childPosition);
		List<MatchAgainstSpInfo> spList = match.getMatchAgainstSpInfoList();
		
		String sp = FootballLotteryTools.getMatchSp(spList, GlobalConstants.TC_JZJQS, mSearchType);
		
		mHolder.mFootballLayout.setVisibility(View.VISIBLE);
		if (StringUtil.isEmpty(sp)) {
			mHolder.mFootballLayout.setVisibility(View.GONE);
		} else {
			for (int i = 0; i < mHolder.mFoolballNumbersView.length; i++) {
				// 赋值给每个进球个数view
				if (mHolder.mFoolballNumbersView[i].getChildCount() == 2) {
					TextView textView = (TextView) mHolder.mFoolballNumbersView[i].getChildAt(0);
					TextView spTextView = (TextView) mHolder.mFoolballNumbersView[i].getChildAt(1);
					if (spTextView != null) {
						if(sp.indexOf("@") != -1){
							String[] jqsArr = sp.split("\\@");
							if(jqsArr[i].split("\\_").length > 1) {
								textView.setText(jqsArr[i].split("\\_")[0] + "球");
								spTextView.setText(jqsArr[i].split("\\_")[1]);
							} else {
								mHolder.mFootballLayout.setVisibility(View.GONE);
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
							mHolder.mFootballLayout.setVisibility(View.GONE);
						}
					} else {
						mHolder.mFootballLayout.setVisibility(View.GONE);
					}
				} else {
					mHolder.mFootballLayout.setVisibility(View.GONE);
				}
			}
		}

		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		/** 进球数view结合 **/
		private LinearLayout mFoolballNumbersView[];
		private LinearLayout mFootballLayout;
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
					if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
						ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
					} else {
						mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
						mSelectItemIndexs.get(dataPosition).add(index);
					}
				}
				notifyDataSetChanged();
			}
			((LotteryFootballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
			notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

}
