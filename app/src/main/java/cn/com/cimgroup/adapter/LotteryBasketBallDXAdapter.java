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
import cn.com.cimgroup.activity.LotteryBasketballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 篮球、大小分
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-6-3
 */
public class LotteryBasketBallDXAdapter extends BaseLotteryBidOnlyAdapter implements OnClickListener {

	private MatchSearchType mSearchType;
	
	public LotteryBasketBallDXAdapter(Context context, MatchSearchType searchType) {
		super(context);
		mSearchType = searchType;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		convertView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		// 获取控件结果集 ,父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加让球胜负所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_basketball_select, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mBigView = (TextView) convertView.findViewById(R.id.txtView_basketballottery_left);
			mHolder.mSmallView = (TextView) convertView.findViewById(R.id.txtView_basketballottery_right);
			mHolder.mBigLayoutView = (LinearLayout) convertView.findViewById(R.id.layoutView_basketballottery_left);
			mHolder.mSmallLayoutView = (LinearLayout) convertView.findViewById(R.id.layoutView_basketballottery_right);
			mHolder.mStop = (TextView) convertView.findViewById(R.id.textView_basketball_dx_stop);
		}
		// 获取该条在全部比赛中得索引位置
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		
		Match match = mChildMatchs.get(groupPosition).get(childPosition);
		String dxSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLDXF, mSearchType);
		
		mHolder.mStop.setVisibility(View.GONE);
		// 将比率显示出来
		if(!StringUtil.isEmpty(dxSp)) {
			if(dxSp.indexOf("@") != -1) {
				String[] bfArr = dxSp.split("\\@");
				int spIndex = 0;
				for (String str : bfArr) {
					if (spIndex == 0) {
						TextView spDecTextView = (TextView) mHolder.mBigLayoutView.getChildAt(0);
						TextView spTextView = (TextView) mHolder.mBigLayoutView.getChildAt(1);
						spDecTextView.setText(str.split("\\_")[0].split("\\|")[1]);
						mHolder.id_vs.setText(str.split("\\_")[0].split("\\|")[0]);
						spTextView.setText(str.split("\\_")[1]);
//						mHolder.mBigView.setText(str.split("\\_")[1]);
					} else {
						TextView spDecTextView = (TextView) mHolder.mSmallLayoutView.getChildAt(0);
						TextView spTextView = (TextView) mHolder.mSmallLayoutView.getChildAt(1);
						spDecTextView.setText(str.split("\\_")[0]);
						spTextView.setText(str.split("\\_")[1]);
//						mHolder.mSmallView.setText(str.split("\\_")[1]);
					}
					
					// 默认将所有投注选项标识为未选中
					mHolder.mBigLayoutView.setSelected(false);
					mHolder.mSmallLayoutView.setSelected(false);
					if (mSelectItemIndexs.containsKey(dataPosition)) {
						// 将已经选择的投注选项的UI进行改变
						List<Integer> indexs = mSelectItemIndexs.get(dataPosition);
						if (indexs != null) {
							for (int i = 0; i < indexs.size(); i++) {
								if (indexs.get(i) == 0) {
									mHolder.mBigLayoutView.setSelected(true);
								} else if (indexs.get(i) == 1) {
									mHolder.mSmallLayoutView.setSelected(true);
								}
							}
						}
					}
					spIndex++;
				}
			} else {
				mHolder.mStop.setVisibility(View.VISIBLE);
			}
		} else {
			mHolder.mStop.setVisibility(View.VISIBLE);
		}
		mHolder.mBigLayoutView.setTag(new Integer[] { dataPosition, 0 });
		mHolder.mSmallLayoutView.setTag(new Integer[] { dataPosition, 1 });
		mHolder.mBigLayoutView.setOnClickListener(this);
		mHolder.mSmallLayoutView.setOnClickListener(this);
		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		/** 胜平负 **/
		private LinearLayout mBigLayoutView, mSmallLayoutView;
		private TextView mBigView, mSmallView;
		private TextView mBigDesView, mSmallDesView;
		private TextView mRqCountView;
		private TextView mStop;
	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		return new ViewChildHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 修改投注选项的状态
		case R.id.layoutView_basketballottery_left:
		case R.id.layoutView_basketballottery_right:
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 2) {
				// 获取该条数据在整个数据集中得索引
				Integer dataPosition = (Integer) obj[0];
				// 每个item的投注选项索引
				Integer index = (Integer) obj[1];
				// 最大选择场次为GlobalConstants.MAX_SELECT_FOOTBALL2BASKBALL_SUM
//				if (mSelectItemIndexs.size() == GlobalConstants.MAX_SELECT_FOOTBALL2BASKBALL_SUM) {
//					if (!mSelectItemIndexs.containsKey(dataPosition)) {
//						UAToast.showToast(mContext, mContext.getString(R.string.max_select_football_basketball_sum, GlobalConstants.MAX_SELECT_FOOTBALL2BASKBALL_SUM));
//						notifyDataSetChanged();
//						return;
//					}
//				}
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
			}
			((LotteryBasketballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
			notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

}
