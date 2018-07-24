package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.DialogType;
import cn.com.cimgroup.dailog.FootballLotteryWayDailog.OnClickEvent;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryHHGGUtil;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 足彩购物车适配器(混合过关)
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryFootBallHHGGCartAdapter extends BaseLotteryBidCartAdapter implements
		OnClickListener, OnClickEvent {

	/** 投注选项dialog **/
	private FootballLotteryWayDailog mDialog = null;
	
	private FootballLotteryHHGGUtil mFootballLotteryHHGGUtil;;
	
	/** 用于存储选择投注选项的索引 key=所选item在数据集中得索引位置 value=所选item的投注方式索引 **/
	protected Map<Integer, Map<Integer, List<Integer>>> mSelectItemIndexs = new TreeMap<Integer, Map<Integer, List<Integer>>>();
	
	private MatchSearchType mSearchType;

	public LotteryFootBallHHGGCartAdapter(Context context, MatchSearchType searchType, FootballLotteryHHGGUtil footballLotteryHHGGUtil) {
		super(context);
		mDialog = new FootballLotteryWayDailog(mContext, this);
		mFootballLotteryHHGGUtil = footballLotteryHHGGUtil;
		mSearchType = searchType;
	}

	/**
	 * 填充数据(混合过关)
	 */
	public void setData(List<List<Match>> mMatchs, Map<Integer, Map<Integer, List<Integer>>> selectItemindexs, List<String> spiltTimes) {
		this.mMatchs = mMatchs;
		this.mSpiltTimes = spiltTimes;
		this.mSelectItemIndexs = selectItemindexs;
		mSelectItemKeys = new ArrayList<Integer>(mSelectItemIndexs.keySet());
		notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewHolder mHolder = (ViewHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_footballlottery_hhgg_select, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mTextView = (TextView) view.findViewById(R.id.textView_football_hhgg_text);
		}

		List<Integer> selectList = new ArrayList<Integer>(super.mSelectItemKeys);
		Collections.sort(selectList);
		// 获取该条数据在整个数据集中得索引
		int dataPosition = selectList.get(position);
		
		int[] arr = FootballLotteryTools.getMatchPositioin(dataPosition, getlotterybidMatchsInfos());
		Match match = mMatchs.get(arr[0]).get(arr[1]);
		
		// 获取胜平负内容
		String spfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZSPF, mSearchType);
		// 获取让球胜平负内容
		String rspfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZXSPF, mSearchType);
		// 获取半全场内容
		String bqcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
		// 获取比分内容
		String bfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBF, mSearchType);
		// 获取进球数
		String jqsSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
		
		// 获取已经选择的该item投注方式索引的集合用于刷新UI
		
		Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(dataPosition);
		
		// 胜平负 按钮选择的结果集
		final List<Integer> selectIndex1 = selectWay.get(FootballLotteryConstants.L320) == null ? new ArrayList<Integer>() : selectWay.get(FootballLotteryConstants.L320);
		// 让球胜平负 按钮选择的结果集
		final List<Integer> selectIndex2 = selectWay.get(FootballLotteryConstants.L301) == null ? new ArrayList<Integer>() : selectWay.get(FootballLotteryConstants.L301);
		// 半全场 半全场按钮选择的结果集
		final List<Integer> selectIndex3 = selectWay.get(FootballLotteryConstants.L304) == null ? new ArrayList<Integer>() : selectWay.get(FootballLotteryConstants.L304);
		// 比分
		final List<Integer> selectIndex4 = selectWay.get(FootballLotteryConstants.L302) == null ? new ArrayList<Integer>() : selectWay.get(FootballLotteryConstants.L302);
		// 进球数
		final List<Integer> selectIndex5 = selectWay.get(FootballLotteryConstants.L303) == null ? new ArrayList<Integer>() : selectWay.get(FootballLotteryConstants.L303);
		
		StringBuilder sb = new StringBuilder();
		if(spfSp.indexOf("@") != -1){
			String[] spArr = spfSp.split("\\@");
			for (int i = 0; i < selectIndex1.size(); i++) {
				String text = "";
				switch (spArr[selectIndex1.get(i)].split("\\_")[0]) {
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
		}
		
		if(rspfSp.indexOf("@") != -1){
			String[] rspfArr = rspfSp.split("\\@");
			for (int i = 0; i < selectIndex2.size(); i++) {
				String text = "";
				switch (rspfArr[selectIndex2.get(i)].split("\\_")[0]) {
				case "3":
					text = "让" + mContext.getResources().getString(R.string.victory);
					break;
				case "1":
					text = "让" + mContext.getResources().getString(R.string.draw);
					break;
				case "0":
					text = "让" + mContext.getResources().getString(R.string.negative);
					break;
				default:
					break;
				}
				sb.append(text + " ");
			}
		}
		
		if(bqcSp.indexOf("@") != -1){
			String[] bqcArr = bqcSp.split("\\@");
			for (int i = 0; i < selectIndex3.size(); i++) {
				sb.append(bqcArr[selectIndex3.get(i)].split("\\_")[0] + " ");
			}
		}
		
		if(bfSp.indexOf("@") != -1){
			String[] bfArr = bfSp.split("\\@");
			for (int i = 0; i < selectIndex4.size(); i++) {
				sb.append(bfArr[selectIndex4.get(i)].split("\\_")[0] + " ");
			}
		}
		
		if(jqsSp.indexOf("@") != -1){
			String[] jqsArr = jqsSp.split("\\@");
			for (int i = 0; i < selectIndex5.size(); i++) {
				sb.append(jqsArr[selectIndex5.get(i)].split("\\_")[0] + " ");
			}
		}
		mHolder.mTextView.setText(sb.toString());
		mHolder.mTextView.setSelected(true);
		mHolder.mTextView.setOnClickListener(this);
		
		switch (mSearchType) {
		case SINGLE:
			mHolder.mDan.setVisibility(View.GONE);
			break;
		case TWIN:
			mHolder.mDan.setVisibility(View.VISIBLE);
			
			List<String> list = mFootballLotteryHHGGUtil.getSelectChuanState();
			int num = Integer.parseInt(list.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0]);
			int lastNum = num;
			if (list.size() > 1) {
				lastNum = Integer.parseInt(list.get(list.size() - 1).split(mContext.getResources().getString(R.string.football_chuan))[0]);
			}
			int selectLen = super.mSelectItemKeys.size();
			int setDanNum = selectLen - lastNum;
			if (setDanNum > 0) {
				if ((num - 1) == dans.size()) {
					mHolder.mDan.setSelected(false);
					mHolder.mDan.setEnabled(false);
				} else {
					mHolder.mDan.setEnabled(true);
				}
			} else {
				mHolder.mDan.setSelected(false);
				mHolder.mDan.setEnabled(false);
			}
			
//			List<Integer> keys = new ArrayList<Integer>(dans.keySet());
			if (dans.size() != 0 && setDanNum > 0) {
				for (int i = 0; i < dans.size(); i++) {
					int value = dans.get(i);
					if (position == value) {
						mHolder.mDan.setEnabled(true);
						mHolder.mDan.setSelected(true);
						break;
					} else {
						mHolder.mDan.setSelected(false);
					}
				}
			} else {
				if (dans.size() > 0) {
					dans.clear();
				}
				mHolder.mDan.setSelected(false);
			}
			
		break;
		default:
			break;
		}
		
		match.setPosition(dataPosition);
		mHolder.mTextView.setTag(match);
		return super.getView(position, convertView, parent);
	}

	class ViewHolder extends ViewSuperHolder {

//		// 胜平负
//		private LinearLayout mFootballSPFViews[];
//		// 让球胜平负
//		private LinearLayout mFootballrSPFViews[];
		// 展开全部
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
		case R.id.textView_football_hhgg_text:
			Match match = (Match) v.getTag();
			Integer dataPosition = match.getPosition();
			// 获取MatchNo
			if (mSelectItemIndexs.get(dataPosition) == null) {
				mSelectItemIndexs.put(dataPosition, new TreeMap<Integer, List<Integer>>());
			}
			// // 弹出选择投注选项dialog
			mDialog.showDialog(DialogType.HHGG, mSelectItemIndexs.get(dataPosition), dataPosition, match, mSearchType);
			break;
		// 删除此选项
		case R.id.imgView_lotterybid_cart_trash:
			int dataPosition1 = (Integer) v.getTag();
			// 获取该条数据在整个数据集中得索引
			Collections.sort(super.mSelectItemKeys);
			int dataPosition2 = super.mSelectItemKeys.get(dataPosition1);
			switch (mSearchType) {
			case TWIN:
				if (super.mSelectItemKeys.size() <= 2) {
					dans.clear();
					ToastUtil.shortToast(mContext, "至少保留2场比赛");
					return;
				}
				break;
			case SINGLE:
				if (super.mSelectItemKeys.size() <= 2) {
					dans.clear();
				}
				
				if (super.mSelectItemKeys.size() <= 1) {
					ToastUtil.shortToast(mContext, "至少保留1场比赛");
					return;
				}
				break;
			default:
				break;
			}
			mSelectItemIndexs.remove(dataPosition2);
			mSelectItemKeys.remove(dataPosition1);
			if (dans.size() > 0) {
				//删除一个胆，如果删除胆的位置后面还有胆，则胆的位置逐级减一
				Collections.sort(dans);
				if (dans.contains(dataPosition1)) {
					int index = dans.indexOf(dataPosition1);
					for (int i = 0; i < dans.size(); i++) {
						if (i > index) {
							int value = dans.get(i);
							dans.remove(i);
							dans.add(i,(value - 1));
						}
					}
					dans.remove(index);
				} else {
					for (int i = 0; i < dans.size(); i++) {
						int value = dans.get(i);
						if (value > dataPosition1) {
							dans.remove(i);
							dans.add(i,(value - 1));
						}
					}
					List<String> chuans = mFootballLotteryHHGGUtil.getSelectChuanState();
					if (mSelectItemIndexs.size() <= Integer.parseInt(chuans.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0])) {
						dans.clear();
					}
				}
			}
			mFootballLotteryHHGGUtil.addDans(dans);
			// 如果注册了数据发生改变监听，那么进行通知
			if (mAdapterDataChagne != null) {
				mAdapterDataChagne.onIndexClickCartChagne();
			}
			notifyDataSetChanged();
			break;
		case R.id.txtView_lotterybid_cart_dan:
			int position = (Integer) v.getTag();
			Object[] result = null;
			
			List<String> list = mFootballLotteryHHGGUtil.getSelectChuanState();
			int num = Integer.parseInt(list.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0]);
			int lastNum = num;
			if (list.size() > 1) {
				lastNum = Integer.parseInt(list.get(list.size() - 1).split(mContext.getResources().getString(R.string.football_chuan))[0]);
			}
			int selectLen = super.mSelectItemKeys.size();
			int setDanNum = selectLen - lastNum;
			if (setDanNum > 0) {
				if ((num - 1) >= dans.size()) {
					if (!dans.contains(position)) {
						dans.add(position);
					} else {
						for (int i = 0; i < dans.size(); i++) {
							if (position == dans.get(i)) {
								dans.remove(i);
							}
						}
					}
				}
			} else {
				dans.clear();
			}
			mFootballLotteryHHGGUtil.addDans(dans);
//			result = mFootballLotteryHHGGUtil.startCalculate();
//			int itemTotal = (Integer) result[0];
//			int numPrice = (Integer) result[1];
//			Double winPrice = (Double) result[2];
//			((LotteryBidCartActivity)mContext).mTotal2PriceView.setText(mContext.getResources().getString(R.string.num_money, itemTotal, Integer.parseInt(((LotteryBidCartActivity)mContext).mMultipleEditView.getText().toString()), numPrice));
//			((LotteryBidCartActivity)mContext).mWinPriceView.setText(winPrice + mContext.getResources().getString(R.string.lemi_unit));
			if (mAdapterDataChagne != null) {
				mAdapterDataChagne.onIndexClickCartChagne();
			}
			notifyDataSetChanged();
			break;
		default:
			// 点击投注选项控件
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 3) {
				// 竞彩标识
				Integer position1 = (Integer) obj[0];
				// 投注类型
				Integer matchType = (Integer) obj[1];
				// 此投注类型索引
				Integer matchIndex = (Integer) obj[2];
				// 判断是否添加过matchno当做索引的key，如果没有添加过那么进行初始化
				if (!mSelectItemIndexs.containsKey(position1)) {
					mSelectItemIndexs.put(position1, new TreeMap<Integer, List<Integer>>());
				}
				Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(position1);
				if (selectWay == null) {
					selectWay = new TreeMap<Integer, List<Integer>>();
					mSelectItemIndexs.put(position1, selectWay);
				}
				// 判断是否进行过胜负彩、让球胜负彩的点击，如果点击过，那么将其移除、否则添加
				if (!selectWay.containsKey(matchType)) {
					selectWay.put(matchType, new ArrayList<Integer>());
				}
				List<Integer> indexChid = selectWay.get(matchType);
				if (indexChid.contains(matchIndex)) {
					indexChid.remove(matchIndex);
					if (indexChid.size() == 0) {
						selectWay.remove(matchType);
					}
					if (selectWay.size() == 0) {
						mSelectItemIndexs.remove(position1);
						mSelectItemKeys.remove(position1);
					}
				} else {
					indexChid.add(matchIndex);
				}
				
				// 如果注册了数据发生改变监听，那么进行通知
				if (mAdapterDataChagne != null) {
					mAdapterDataChagne.onIndexClickCartChagne();
				}
				notifyDataSetChanged();
			}
			break;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void OnOKClick(Object... objects) {
		// 获取现在item数量
		int startCount = getCount();
		// 将原来的选择的值清空，从新赋值
		if (objects != null && objects.length == 2) {
			Integer position = (Integer) objects[1];
			// 获取所有的选择项
			Map<Integer, List<Integer>> indexs = (Map<Integer, List<Integer>>) objects[0];
			switch (mSearchType) {
			case TWIN:
				if (super.mSelectItemKeys.size() > 2) {
					mSelectItemIndexs.remove(position);
				}
				break;
			case SINGLE:
				if (super.mSelectItemKeys.size() <= 2) {
					dans.clear();
				}
				
				if (super.mSelectItemKeys.size() > 1) {
					mSelectItemIndexs.remove(position);
				}
				break;
			default:
				break;
			}
			// 进行非空过滤
			Map<Integer, List<Integer>> tempIndexs = new TreeMap<Integer, List<Integer>>();
			List<Integer> key = new ArrayList<Integer>(indexs.keySet());
			Collections.sort(super.mSelectItemKeys);
			int dataPosition = super.mSelectItemKeys.indexOf(position);
			if (key != null) {
				for (int i = 0; i < key.size(); i++) {
					List<Integer> value = indexs.get(key.get(i));
					if (value != null && value.size() != 0) {
						tempIndexs.put(key.get(i), value);
					}
				}
				if (tempIndexs.size() > 0) {
					mSelectItemIndexs.put(position, tempIndexs);
				} else {
					switch (mSearchType) {
					case TWIN:
						if (super.mSelectItemKeys.size() <= 2) {
							dans.clear();
							ToastUtil.shortToast(mContext, "至少保留2场比赛");
							return;
						}
						break;
					case SINGLE:
						if (super.mSelectItemKeys.size() <= 2) {
							dans.clear();
						}
						
						if (super.mSelectItemKeys.size() <= 1) {
							ToastUtil.shortToast(mContext, "至少保留1场比赛");
							return;
						}
						break;
					default:
						break;
					}
					// 如果该项投注内容被清空，那么从已经选择的足彩索引集合中将本记录删除
					mSelectItemKeys.remove(position);
				}
			}
			
			if (dans.size() > 0) {
				//删除一个胆，如果删除胆的位置后面还有胆，则胆的位置逐级减一
				Collections.sort(dans);
				if (dans.contains(dataPosition)) {
					int index = dans.indexOf(dataPosition);
					for (int i = 0; i < dans.size(); i++) {
						if (i > index) {
							int value = dans.get(i);
							dans.remove(i);
							dans.add(i,(value - 1));
						}
					}
					dans.remove(index);
				} else {
					//大于position的位置逐级向前递增一位，更新坐标
					for (int i = 0; i < dans.size(); i++) {
						int value = dans.get(i);
						
						if (value > position) {
							dans.remove(i);
							dans.add(i,(value - 1));
						}
					}
					List<String> chuans = mFootballLotteryHHGGUtil.getSelectChuanState();
					if (mSelectItemIndexs.size() <= Integer.parseInt(chuans.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0])) {
						dans.clear();
					}
				}
			}
			mFootballLotteryHHGGUtil.addDans(dans);
			// 如果注册了数据发生改变监听，那么进行通知
			if (mAdapterDataChagne != null) {
				mAdapterDataChagne.onIndexClickCartChagne();
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public void OnCanlClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnOtherClick() {
		// TODO Auto-generated method stub

	}

}
