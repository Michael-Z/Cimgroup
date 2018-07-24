package cn.com.cimgroup.adapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryBasketballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.DialogType;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.OnClickEvent;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 篮球、混合
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-6-3
 */
public class LotteryBasketBallHHGGAdapter extends BaseLotteryBidAdapter implements OnClickListener, OnClickEvent {
	
	private Map<Integer, Map<Integer, List<Integer>>> mSelectItemIndexs = new HashMap<Integer, Map<Integer, List<Integer>>>();
	
	private MatchSearchType mSearchType;
	
	/** 投注选项dialog **/
	private FootballLotteryWayDailog mDialog = null;

	public LotteryBasketBallHHGGAdapter(Context context, MatchSearchType searchType) {
		super(context);
		// TODO Auto-generated constructor stub
		mDialog = new FootballLotteryWayDailog(mContext, this);
		mSearchType = searchType;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		convertView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加让球胜负所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_basketball_select_hhgg, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mBasketballrSPFViews = new LinearLayout[] {(LinearLayout) view.findViewById(R.id.layoutView_basketballottery_left), (LinearLayout) view.findViewById(R.id.layoutView_basketballottery_right) };
			mHolder.mBasketballSPFViews = new LinearLayout[] {(LinearLayout) view.findViewById(R.id.layoutView_basketballottery_spf_left), (LinearLayout) view.findViewById(R.id.layoutView_basketballottery_spf_right) };
			mHolder.spfStop = (RelativeLayout) view.findViewById(R.id.layoutView_basketballottery_spf_stop);
			mHolder.rspfStop = (RelativeLayout) view.findViewById(R.id.layoutView_basketballottery_rspf_stop);
			mHolder.mMoreTextView = (TextView) convertView.findViewById(R.id.txtView_basketballottery_more);
		}
		// 获取该条在全部比赛中得索引位置
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		
		Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(dataPosition);
		// 选择的投注方式索引集合 胜平负
		List<Integer> selectIndex1 = selectWay != null ? (selectWay.containsKey(FootballLotteryConstants.L307) ? selectWay.get(FootballLotteryConstants.L307) : null) : null;
		// 选择的投注方式索引集合 让球胜平负
		List<Integer> selectIndex2 = selectWay != null ? (selectWay.containsKey(FootballLotteryConstants.L306) ? selectWay.get(FootballLotteryConstants.L306) : null) : null;

		
		Match match = mChildMatchs.get(groupPosition).get(childPosition);
		String sfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLSF, mSearchType);
		String rsfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLXSF, mSearchType);
		
		// 将比率显示出来
		if(!StringUtil.isEmpty(sfSp)) {
			if(sfSp.indexOf("@") != -1) {
				String[] bfArr = sfSp.split("\\@");
				int spIndex = 0;
				for (String str : bfArr) {
					if(!StringUtil.isEmpty(str)) {
						mHolder.spfStop.setVisibility(View.GONE);
						TextView spDecTextView = (TextView) mHolder.mBasketballSPFViews[spIndex].getChildAt(0);
						TextView spTextView = (TextView) mHolder.mBasketballSPFViews[spIndex].getChildAt(1);
						spDecTextView.setText(str.split("\\_")[0]);
						spTextView.setText(str.split("\\_")[1]);
						mHolder.mBasketballSPFViews[spIndex].setTag(new Object[] { dataPosition, FootballLotteryConstants.L307, spIndex });
						mHolder.mBasketballSPFViews[spIndex].setOnClickListener(this);
						mHolder.mBasketballSPFViews[spIndex].setSelected(false);
						if (selectIndex1 != null) {
							if (selectIndex1.contains(new Integer(spIndex))) {
								mHolder.mBasketballSPFViews[spIndex].setSelected(true);
							}
						}
						spIndex++;
					} else {
						mHolder.spfStop.setVisibility(View.VISIBLE);
					}
				}
			}
		} else {
			mHolder.spfStop.setVisibility(View.VISIBLE);
		}
		
		if(!StringUtil.isEmpty(rsfSp)) {
			if(rsfSp.indexOf("@") != -1) {
				String[] bfArr = rsfSp.split("\\@");
				String[] rArr = bfArr[0].split("\\|");
				int spIndex = 0;
				for (String str : bfArr) {
					if(!StringUtil.isEmpty(str)) {
						mHolder.rspfStop.setVisibility(View.GONE);
						TextView spDecTextView = (TextView) mHolder.mBasketballrSPFViews[spIndex].getChildAt(0);
						TextView spTextView = (TextView) mHolder.mBasketballrSPFViews[spIndex].getChildAt(1);
						spDecTextView.setText(rArr[1].split("\\_")[0]);
						spTextView.setText(str.split("\\_")[1]);
						mHolder.mBasketballrSPFViews[spIndex].setTag(new Object[] { dataPosition, FootballLotteryConstants.L306, spIndex });
						mHolder.mBasketballrSPFViews[spIndex].setOnClickListener(this);
						mHolder.mBasketballrSPFViews[spIndex].setSelected(false);
						if (selectIndex2 != null) {
							if (selectIndex2.contains(new Integer(spIndex))) {
								mHolder.mBasketballrSPFViews[spIndex].setSelected(true);
							}
						}
						if (spIndex == (bfArr.length - 1)) {
							spDecTextView.setText(Html.fromHtml(str.split("\\_")[0] + mContext.getResources().getString(R.string.basketball_match_hhgg_rsf_sp, rArr[0])));
						}
						spIndex++;
					} else {
						mHolder.rspfStop.setVisibility(View.VISIBLE);
					}
				}
			}
		} else {
			mHolder.rspfStop.setVisibility(View.VISIBLE);
		}
		
		int sum = 0;
		
		if (selectWay != null) {
			Set<Integer> set = selectWay.keySet();
			Iterator<Integer> it = set.iterator();  
			while (it.hasNext()) {  
				int str = it.next();
				sum += selectWay.get(str).size();
			}
		}
		
		if (sum > 0) {
			mHolder.mMoreTextView.setText("已选\n" + sum + "项");
//			mHolder.mExAllSelectView.setBackgroundColor(mContext.getResources().getColor(R.color.hall_red));
			mHolder.mMoreTextView.setTextColor(mContext.getResources().getColor(R.color.color_red));
			sum = 0;
		} else {
			mHolder.mMoreTextView.setText(mContext.getResources().getString(R.string.footballlottery_all_select));
//			mHolder.mExAllSelectView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			mHolder.mMoreTextView.setTextColor(mContext.getResources().getColor(R.color.color_gray_secondary));
		}
		match.setPosition(dataPosition);
		mHolder.mMoreTextView.setTag(match);
		mHolder.mMoreTextView.setOnClickListener(this);

		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		/** 胜平负 **/
		private LinearLayout mBasketballSPFViews[], mBasketballrSPFViews[];
		
		private RelativeLayout spfStop;
		
		private RelativeLayout rspfStop;
		
		private TextView mMoreTextView;
	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		// TODO Auto-generated method stub
		return new ViewChildHolder();
	}
	
	/**
	 * 投注选项集合，外部赋值
	 * @Description:
	 * @param selectItemindex
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void setSelectItemIndexs(Map<Integer, Map<Integer, List<Integer>>> selectItemindex) {
		if (selectItemindex == null) {
			selectItemindex = new TreeMap<Integer, Map<Integer, List<Integer>>>();
		}
		this.mSelectItemIndexs = selectItemindex;
		this.notifyDataSetChanged();

	}

	/**
	 * 获取选择的所有item的结果索引
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public Map<Integer, Map<Integer, List<Integer>>> getSelectItemIndexs() {
		return mSelectItemIndexs;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtView_basketballottery_more:
			//获取该条数据在整个数据集中得索引
			Match match = (Match) v.getTag();
			Integer dataPosition = match.getPosition();
				if (!mSelectItemIndexs.containsKey(dataPosition)) {
					if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
						ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
					} else {
						mSelectItemIndexs.put(dataPosition, new TreeMap<Integer, List<Integer>>());
					}
				}
				mDialog.showDialog(DialogType.LQHHGG, mSelectItemIndexs.get(dataPosition), dataPosition, match, mSearchType);
			break;
		default:
			// 点击投注选项控件
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 3) {
				// 获取该条数据在整个数据集中得索引
				Integer dataPosition1 = (Integer) obj[0];
				// 投注方式
				Integer matchType = (Integer) obj[1];
				// 此选择的投注方式的索引位置
				Integer matchIndex = (Integer) obj[2];
				if (!mSelectItemIndexs.containsKey(dataPosition1)) {
					if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
						ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
					} else {
						mSelectItemIndexs.put(dataPosition1, new TreeMap<Integer, List<Integer>>());
					}
				}
				Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(dataPosition1);
				if (selectWay == null) {
					selectWay = new TreeMap<Integer, List<Integer>>();
					if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
//						ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
					} else {
						mSelectItemIndexs.put(dataPosition1, selectWay);
					}
				}
				if (!selectWay.containsKey(matchType)) {
					selectWay.put(matchType, new ArrayList<Integer>());
				}
				// 判断是否进行过此投注方式的当前索引位置的点击，如果点击过，那么将其移除、否则添加
				List<Integer> indexChid = selectWay.get(matchType);
				if (indexChid.contains(matchIndex)) {
					indexChid.remove(matchIndex);
					if (indexChid.size() == 0) {
						// 如果此投注方式没有数据，那么将其移除
						selectWay.remove(matchType);
						// 如果此item没有任何一项选择那么将整条选择记录移除
						if (selectWay.size() == 0) {
							mSelectItemIndexs.remove(dataPosition1);
						}
					}
				} else {
					indexChid.add(matchIndex);
				}
				((LotteryBasketballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs.size());
				notifyDataSetChanged();
			}
			break;
		}

	}

	@Override
	public void OnOKClick(Object... objects) {
		// TODO Auto-generated method stub
		// 将原来的选择的值清空，从新赋值
		if (objects != null && objects.length == 2) {
			// 获取该条数据在整个数据集中得索引
			Integer dataPosition = (Integer) objects[1];
			// 获取所有的选择项
			Map<Integer, List<Integer>> indexs = (Map<Integer, List<Integer>>) objects[0];
			mSelectItemIndexs.remove(dataPosition);
			// 进行非空过滤
			Map<Integer, List<Integer>> tempIndexs = new TreeMap<Integer, List<Integer>>();
			List<Integer> key = new ArrayList<Integer>(indexs.keySet());
			if (key != null) {
				for (int i = 0; i < key.size(); i++) {
					List<Integer> value = indexs.get(key.get(i));
					if (value != null && value.size() != 0) {
						tempIndexs.put(key.get(i), value);
					}
				}
				if (tempIndexs.size() > 0) {
					if (mSelectItemIndexs.size() >= GlobalConstants.MUSTNUM) {
						ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
					} else {
						mSelectItemIndexs.put(dataPosition, tempIndexs);
					}
				}
			}
			notifyDataSetChanged();
			((LotteryBasketballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
		}
	}

	@Override
	public void OnCanlClick() {
		
	}

	@Override
	public void OnOtherClick() {
		
	}
	
	/**
	 * 填充数据
	 * @Description:
	 * @param spiltTimes
	 * @param mMatchs
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void setData(List<String> spiltTimes, List<List<Match>> mMatchs) {
		this.mGroupSpiltTimes = spiltTimes;
		this.mChildMatchs = mMatchs;
//		this.sps = sps;
//		convertMap();
		notifyDataSetChanged();
	}

}
