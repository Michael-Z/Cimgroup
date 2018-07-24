package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.DialogType;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.OnClickEvent;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 足彩 混合过关Adapter
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年12月29日
 */
public class LotteryFootBallHHGGAdapter extends BaseLotteryBidAdapter implements OnClickListener,
		OnClickEvent {

	/**
	 * 用于存储选择投注选项的索引 key=所选item在数据集中得索引位置 value=所选item的投注方式索引<Map<Integer, List<Integer>> key=投注方式
	 * value=投注方式索引
	 **/
	private Map<Integer, Map<Integer, List<Integer>>> mSelectItemIndexs = new HashMap<Integer, Map<Integer, List<Integer>>>();
	
	/** 投注选项dialog **/
	private FootballLotteryWayDailog mDialog = null;
	
	private MatchSearchType mSearchType;

	public LotteryFootBallHHGGAdapter(Context context, MatchSearchType searchType) {
		super(context);
		mDialog = new FootballLotteryWayDailog(mContext, this);
		mSearchType = searchType;
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

	@SuppressWarnings("null")
	@SuppressLint("UseValueOf")
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		convertView = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_footballlottery_spf_select, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mFootballSPFViews = new LinearLayout[] { (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_0), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_1), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_2) };
			mHolder.mFootballrSPFViews = new LinearLayout[] { (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_0), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_1), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_2) };
			mHolder.mExAllSelectView = (TextView) view.findViewById(R.id.txtView_football_item_select);
			mHolder.spfStop = (RelativeLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_stop);
			mHolder.rspfStop = (RelativeLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_stop);
			mHolder.rspfText = (TextView) view.findViewById(R.id.txtView_footballlottery_hhgg_allow1);
		}
		// 获取该条数据在整个数据集中得索引
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition, childPosition);
		// 获取已经选择的该item投注方式索引的集合用于刷新UI
		Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(dataPosition);
		// 选择的投注方式索引集合 胜平负
		List<Integer> selectIndex1 = selectWay != null ? (selectWay.containsKey(FootballLotteryConstants.L320) ? selectWay.get(FootballLotteryConstants.L320) : null) : null;
		// 选择的投注方式索引集合 让球胜平负
		List<Integer> selectIndex2 = selectWay != null ? (selectWay.containsKey(FootballLotteryConstants.L301) ? selectWay.get(FootballLotteryConstants.L301) : null) : null;
		
		Match match = (Match) super.getChild(groupPosition, childPosition);
//		List<MatchAgainstSpInfo> spList = match.getMatchAgainstSpInfoList();
		
//		String sp = FootballLotteryTools.getMatchSp(spList, GlobalConstants.TC_JZSPF, mSearchType);
//		String rsp = FootballLotteryTools.getMatchSp(spList, GlobalConstants.TC_JZXSPF, mSearchType);
		String[] sp = sps.get(groupPosition).get(childPosition).getSpfSp();
		String[] rsp = sps.get(groupPosition).get(childPosition).getRspfSp();
		if (match.getLetScore().equals("1")) {
			mHolder.rspfText.setText("+"+match.getLetScore());
			mHolder.rspfText.setBackgroundColor(mContext.getResources()
					.getColor(R.color.color_red));
		}else {
			mHolder.rspfText.setText(match.getLetScore());
			mHolder.rspfText.setBackgroundColor(mContext.getResources().getColor(R.color.color_green_warm));
		}
		
		if (sp == null) {
			mHolder.spfStop.setVisibility(View.VISIBLE);
		} else {
			if (sp.length > 0) {
				List<String> spList = Arrays.asList(sp);
				int spIndex = 0;
				for (String i : spList) {
					TextView spTextView = (TextView) mHolder.mFootballSPFViews[spIndex].getChildAt(1);
					if(!StringUtil.isEmpty(i)) {
						mHolder.spfStop.setVisibility(View.GONE);
						spTextView.setText(i);
						mHolder.mFootballSPFViews[spIndex].setTag(new Object[] { dataPosition, FootballLotteryConstants.L320, spIndex });
						mHolder.mFootballSPFViews[spIndex].setOnClickListener(this);
						mHolder.mFootballSPFViews[spIndex].setSelected(false);
						// 如果选择了投注方式/取消，那么跟新此按钮为选择状态 否则移除
						if (selectIndex1 != null) {
							if (selectIndex1.contains(new Integer(spIndex))) {
								mHolder.mFootballSPFViews[spIndex].setSelected(true);
							}
						}
					} else {
						mHolder.spfStop.setVisibility(View.VISIBLE);
					}
					spIndex++;
				}
			} else {
				mHolder.spfStop.setVisibility(View.VISIBLE);
			}
		}
		
		if (rsp == null) {
			mHolder.rspfStop.setVisibility(View.VISIBLE);
		} else {
			if (rsp.length > 0) {
				List<String> rspList = Arrays.asList(rsp);
				int rspIndex = 0;
				for (String i : rspList) {
					TextView spTextView = (TextView) mHolder.mFootballrSPFViews[rspIndex].getChildAt(1);
					if(!StringUtil.isEmpty(i)) {
						mHolder.rspfStop.setVisibility(View.GONE);
						spTextView.setText(i);
						mHolder.mFootballrSPFViews[rspIndex].setTag(new Object[] { dataPosition, FootballLotteryConstants.L301, rspIndex });
						mHolder.mFootballrSPFViews[rspIndex].setOnClickListener(this);
						mHolder.mFootballrSPFViews[rspIndex].setSelected(false);
						if (selectIndex2 != null) {
							if (selectIndex2.contains(new Integer(rspIndex))) {
								mHolder.mFootballrSPFViews[rspIndex].setSelected(true);
							}
						}
					} else {
						mHolder.rspfStop.setVisibility(View.VISIBLE);
					}
					rspIndex++;
				}
			} else {
				mHolder.rspfStop.setVisibility(View.VISIBLE);
			}
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
			mHolder.mExAllSelectView.setText("已选\n" + sum + "项");
//			mHolder.mExAllSelectView.setBackgroundColor(mContext.getResources().getColor(R.color.hall_red));
			mHolder.mExAllSelectView.setTextColor(mContext.getResources().getColor(R.color.color_red));
			sum = 0;
		} else {
			mHolder.mExAllSelectView.setText("展开\n全部");
//			mHolder.mExAllSelectView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			mHolder.mExAllSelectView.setTextColor(mContext.getResources().getColor(R.color.color_gray_secondary));
		}
		match.setPosition(dataPosition);
		mHolder.mExAllSelectView.setTag(match);
		mHolder.mExAllSelectView.setOnClickListener(this);
		
		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		// 胜平负
		private LinearLayout mFootballSPFViews[];
		// 让球胜平负
		private LinearLayout mFootballrSPFViews[];
		
		private RelativeLayout spfStop;
		
		private RelativeLayout rspfStop;
		
		private TextView rspfText;
		
		// 展开全部
		private TextView mExAllSelectView;
	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		return new ViewChildHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtView_football_item_select:
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
				mDialog.showDialog(DialogType.HHGG, mSelectItemIndexs.get(dataPosition), dataPosition, match, mSearchType);
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
//							ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.lottery_football_must));
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
				((LotteryFootballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs.size());
				notifyDataSetChanged();
			}
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void OnOKClick(Object... objects) {

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
			((LotteryFootballActivity) mContext).changeSelectMatchCount2state(mSelectItemIndexs == null ? 0 : mSelectItemIndexs.size());
		}
	}

	@Override
	public void OnCanlClick() {
		notifyDataSetChanged();
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
	public void setData(List<String> spiltTimes, List<List<Match>> mMatchs, List<List<LotterySp>> sps) {
		this.mGroupSpiltTimes = spiltTimes;
		this.mChildMatchs = mMatchs;
		this.sps = sps;
//		convertMap();
		notifyDataSetChanged();
	}

}
