package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.Collections;
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
import cn.com.cimgroup.activity.LotteryBidCartActivity;
import cn.com.cimgroup.activity.LotteryOneWinActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.FootballLotteryUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 一场制胜 购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-10-19
 */
@SuppressLint("UseValueOf")
public class LotteryFootBallOneWinCartAdapter extends BaseLotteryBidOnlyCartAdapter implements
		OnClickListener {
	
	private MatchSearchType mSearchType;
	
	private FootballLotteryUtil mFootballLotteryUtil;
	
	private Match mMatch;

	public LotteryFootBallOneWinCartAdapter(Context context, MatchSearchType searchType, FootballLotteryUtil footballLotteryUtil, Match match) {
		super(context);
		mSearchType = searchType;
		mFootballLotteryUtil = footballLotteryUtil;
		mLotoId = mFootballLotteryUtil.getLotId();
		mMatch = match;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewHolder mHolder = (ViewHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_footballlottery_hhgg_select, null);
			
			mHolder.mTextView = (TextView) view.findViewById(R.id.textView_football_hhgg_text);
			mHolder.mTrashView.setVisibility(View.INVISIBLE);
			mHolder.mTextView.setSelected(true);
//			mHolder.mDan.setVisibility(View.GONE);
//			((LinearLayout) view.findViewById(R.id.id_select_dan_layout)).setVisibility(View.GONE);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		// 获取该条数据在整个数据集中得索引
//		int dataPosition = super.mSelectItemKeys.get(position);
		List<Integer> selectList = new ArrayList<Integer>(mSelectItemKeys);
		Collections.sort(selectList);
		int dataPosition = selectList.get(position);
		
		int[] arr = FootballLotteryTools.getMatchPositioin(dataPosition, getlotterybidMatchsInfos());
		
		int count = 0;
		for (int i = 0; i < mMatchs.size(); i++) {
			count += mMatchs.get(i).size();
		}
		
		Match match = null;
		if (arr[0] <= count && mMatchs.size() > arr[0]) {
			if (mMatchs.get(arr[0]).size() > 0) {
				match = mMatchs.get(arr[0]).get(arr[1]);
			}
		}
		
		String spfSp = "";
		if (match != null) {
			spfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_1CZS, mSearchType);
			mHolder.mMatchHomeView.setText(match.getHomeTeamName());
			mHolder.mMatchGuestView.setText(match.getGuestTeamName());
		}
		
		StringBuilder sb = new StringBuilder();
		if(spfSp.indexOf("@") != -1){
			String[] spArr = spfSp.split("\\@");
			if (mSelectItemIndexs.get(dataPosition).size() > 0) {
				mHolder.mDan.setText("自选");
				for (int i = 0; i < mSelectItemIndexs.get(dataPosition).size(); i++) {
					String text = "";
					switch (spArr[mSelectItemIndexs.get(dataPosition).get(i)].split("\\_")[0]) {
					case "3":
						text = mContext.getResources().getString(R.string.victory);
						break;
					case "1":
						text = mContext.getResources().getString(R.string.draw);
						break;
					case "0":
						text = mContext.getResources().getString(R.string.negative);
						break;
					default:
						break;
					}
					sb.append(text + " ");
				}
			} else {
				mHolder.mDan.setText("匹配");
				mHolder.mMatchHomeView.setText(mMatch.getHomeTeamName());
				mHolder.mMatchGuestView.setText(mMatch.getGuestTeamName());
				String x1sp = FootballLotteryTools.getMatchSp(mMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_2X1, mSearchType);
				if (!StringUtil.isEmpty(x1sp)) {
					if (x1sp.substring(0, 1).equals("3")) {
						sb.append("胜" + " 让负");
					} else {
						sb.append("负" + " 让胜");
					}
				}
			}
			
		} else {
			mHolder.mDan.setText("匹配");
			mHolder.mMatchHomeView.setText(mMatch.getHomeTeamName());
			mHolder.mMatchGuestView.setText(mMatch.getGuestTeamName());
			sb.append("胜" + " 让负");
		}
		mHolder.mTextView.setText(sb.toString());
		return super.getView(position, convertView, parent);
	}

	class ViewHolder extends ViewSuperHolder {

//		private LinearLayout mBigLayoutView, mSmallLayoutView;
		private TextView mTextView;
	}

	@Override
	public ViewSuperHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtView_lotterybid_cart_dan:
			
			break;
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
					mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
					mSelectItemIndexs.get(dataPosition).add(index);
				}
//				notifyDataSetChanged();
			}
//			((LotteryOneWinActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
			if (mAdapterDataChagne != null) {
				mAdapterDataChagne.onIndexClickCartChagne();
			}
			notifyDataSetChanged();
			break;
		}

	}

}
