package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import cn.com.cimgroup.activity.LotteryBidCartActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryHHGGUtil;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 胜平负购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryFootBallSPFCartAdapter extends BaseLotteryBidCartAdapter implements OnClickListener {

	private MatchSearchType mSearchType;
	
	private FootballLotteryHHGGUtil mFootballLotteryHHGGUtil;;
	
	/** 用于存储选择投注选项的索引 key=所选item在数据集中得索引位置 value=所选item的投注方式索引 **/
	protected Map<Integer, Map<Integer, List<Integer>>> mSelectItemIndexs = new TreeMap<Integer, Map<Integer, List<Integer>>>();
	
	public LotteryFootBallSPFCartAdapter(Context context, MatchSearchType searchType, FootballLotteryHHGGUtil footballLotteryHHGGUtil) {
		super(context);
		mSearchType = searchType;
		mFootballLotteryHHGGUtil = footballLotteryHHGGUtil;
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
			LinearLayout view = (LinearLayout) View.inflate(mContext,R.layout.include_footballlottery_spf_select, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mHolder.mFootballSPFViews = new LinearLayout[] {(LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_0), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_1), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_2) };
			mHolder.mFootballrSPFViews = new LinearLayout[] {(LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_0), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_1), (LinearLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_2) };
			mHolder.rspfText = (TextView) view.findViewById(R.id.txtView_footballlottery_hhgg_allow1);
			mHolder.spfStop = (RelativeLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_spf_stop);
			mHolder.rspfStop = (RelativeLayout) view.findViewById(R.id.txtView_footballlottery_hhgg_sspf_stop);
			((TextView) view.findViewById(R.id.txtView_football_item_select)).setVisibility(View.GONE);;
		}
		// 获取该条数据在整个数据集中得索引
//		int dataPosition = super.mSelectItemKeys.get(position);
		List<Integer> selectList = new ArrayList<Integer>(mSelectItemKeys);
		Collections.sort(selectList);
		int dataPosition = selectList.get(position);
		
		int[] arr = FootballLotteryTools.getMatchPositioin(dataPosition, getlotterybidMatchsInfos());
		Match match = mMatchs.get(arr[0]).get(arr[1]);
		
		mHolder.rspfText.setText(match.getLetScore());
		
		String sp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZSPF, mSearchType);
		String rsp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZXSPF, mSearchType);
		
		// 获取已经选择的该item投注方式索引的集合用于刷新UI
		Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(dataPosition);
		// 选择的投注方式索引集合 胜平负
		List<Integer> selectIndex1 = selectWay != null ? (selectWay.containsKey(FootballLotteryConstants.L320) ? selectWay.get(FootballLotteryConstants.L320) : null) : null;
		// 选择的投注方式索引集合 让球胜平负
		List<Integer> selectIndex2 = selectWay != null ? (selectWay.containsKey(FootballLotteryConstants.L301) ? selectWay.get(FootballLotteryConstants.L301) : null) : null;

		
		if (sp.indexOf("@") != -1) {
			String[] spArr = sp.split("\\@");
			for (int i = 0; i < spArr.length; i++) {
				TextView spTextView = (TextView) mHolder.mFootballSPFViews[i].getChildAt(1);
				if (spArr[i].split("\\_").length > 1) {
					mHolder.spfStop.setVisibility(View.GONE);
					spTextView.setText(spArr[i].split("\\_")[1]);
					mHolder.mFootballSPFViews[i].setTag(new Object[] {dataPosition, FootballLotteryConstants.L320, i });
					mHolder.mFootballSPFViews[i].setOnClickListener(this);
					mHolder.mFootballSPFViews[i].setSelected(false);
					// 如果选择了投注方式/取消，那么跟新此按钮为选择状态 否则移除
					if (selectIndex1 != null) {
						if (selectIndex1.contains(new Integer(i))) {
							mHolder.mFootballSPFViews[i].setSelected(true);
						} else {
							mHolder.mFootballSPFViews[i].setSelected(false);
						}
					} else {
						mHolder.mFootballSPFViews[i].setSelected(false);
					}
				} else {
					mHolder.mFootballSPFViews[i].setSelected(false);
				}
			}
		} else {
			mHolder.spfStop.setVisibility(View.VISIBLE);
			mHolder.mFootballSPFViews[0].setSelected(false);
			mHolder.mFootballSPFViews[1].setSelected(false);
			mHolder.mFootballSPFViews[2].setSelected(false);
		}

		if (rsp.indexOf("@") != -1) {
			String[] rspArr = rsp.split("\\@");
			for (int i = 0; i < rspArr.length; i++) {
				TextView spTextView = (TextView) mHolder.mFootballrSPFViews[i]
						.getChildAt(1);
				if (rspArr[i].split("\\_").length > 1) {
					mHolder.rspfStop.setVisibility(View.GONE);
					spTextView.setText(rspArr[i].split("\\_")[1]);
					mHolder.mFootballrSPFViews[i].setTag(new Object[] {dataPosition, FootballLotteryConstants.L301, i });
					mHolder.mFootballrSPFViews[i].setOnClickListener(this);
					mHolder.mFootballrSPFViews[i].setSelected(false);
					if (selectIndex2 != null) {
						if (selectIndex2.contains(new Integer(i))) {
							mHolder.mFootballrSPFViews[i].setSelected(true);
						} else {
							mHolder.mFootballrSPFViews[i].setSelected(false);
						}
					} else {
						mHolder.mFootballrSPFViews[i].setSelected(false);
					}
				} else {
					mHolder.mFootballrSPFViews[i].setSelected(false);
				}
			}
		} else {
			mHolder.rspfStop.setVisibility(View.VISIBLE);
			mHolder.mFootballrSPFViews[0].setSelected(false);
			mHolder.mFootballrSPFViews[1].setSelected(false);
			mHolder.mFootballrSPFViews[2].setSelected(false);
		}
		
		switch (mSearchType) {
		case SINGLE:
			mHolder.mDan.setVisibility(View.GONE);
			break;
		case TWIN:
			mHolder.mDan.setVisibility(View.VISIBLE);
			
			List<String> list = mFootballLotteryHHGGUtil.getSelectChuanState();
			Collections.sort(list);
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
		return super.getView(position, convertView, parent);
	}

	class ViewHolder extends ViewSuperHolder {

		// 胜平负
		private LinearLayout mFootballSPFViews[];
		// 让球胜平负
		private LinearLayout mFootballrSPFViews[];
		
		private RelativeLayout spfStop;
		
		private RelativeLayout rspfStop;
		
		private TextView rspfText;
	}

	@Override
	public ViewSuperHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			// 获取现在item数量
			int startCount = getCount();
			// 点击投注选项控件
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 3) {
				// 获取该条数据在整个数据集中得索引
				Integer position3 = (Integer) obj[0];
				// 投注方式
				Integer matchType = (Integer) obj[1];
				// 此选择的投注方式的索引位置
				Integer matchIndex = (Integer) obj[2];
				if (!mSelectItemIndexs.containsKey(position3)) {
					mSelectItemIndexs.put(position3, new TreeMap<Integer, List<Integer>>());
				}
				Map<Integer, List<Integer>> selectWay = mSelectItemIndexs.get(position3);
				if (selectWay == null) {
					selectWay = new TreeMap<Integer, List<Integer>>();
					mSelectItemIndexs.put(position3, selectWay);
				}
				if (!selectWay.containsKey(matchType)) {
					selectWay.put(matchType, new ArrayList<Integer>());
				}
				
				// 判断是否进行过此投注方式的当前索引位置的点击，如果点击过，那么将其移除、否则添加
				List<Integer> indexChid = selectWay.get(matchType);
				if (indexChid.contains(matchIndex)) {
					switch (mSearchType) {
					case TWIN:
						if (super.mSelectItemKeys.size() <= 2) {
							List<Integer> selectList = new ArrayList<Integer>(selectWay.keySet());
							int count = 0;
							for (int i = 0; i < selectList.size(); i++) {
								count += selectWay.get(selectList.get(i)).size();
							}
							dans.clear();
							if (count == 1) {
								ToastUtil.shortToast(mContext, "至少保留2场比赛");
								return;
							}
							
						}
						break;
					case SINGLE:
						if (super.mSelectItemKeys.size() <= 1) {
							List<Integer> selectList = new ArrayList<Integer>(selectWay.keySet());
							int count = 0;
							for (int i = 0; i < selectList.size(); i++) {
								count += selectWay.get(selectList.get(i)).size();
							}
							if (count == 1) {
								ToastUtil.shortToast(mContext, "至少保留1场比赛");
								return;
							}
						}
						break;
					default:
						break;
					}

					if (indexChid.size() > 0) {
						indexChid.remove(matchIndex);
					}
					if (indexChid.size() == 0) {
						// 如果此投注方式没有数据，那么将其移除
						selectWay.remove(matchType);
						// 如果此item没有任何一项选择那么将整条选择记录移除
						if (selectWay.size() == 0) {
							mSelectItemKeys.remove(position3);
							mSelectItemIndexs.remove(position3);
						}
					}
				} else {
					indexChid.add(matchIndex);
				}
				
//				Collections.sort(super.mSelectItemKeys);
//				int dataPosition3 = super.mSelectItemKeys.indexOf(position3);
				
				if (dans.size() > 0) {
					//删除一个胆，如果删除胆的位置后面还有胆，则胆的位置逐级减一
					Collections.sort(dans);
					if (dans.contains(position3)) {
						int index1 = dans.indexOf(position3);
						for (int i = 0; i < dans.size(); i++) {
							if (i > index1) {
								int value = dans.get(i);
								dans.remove(i);
								dans.add(i,(value - 1));
							}
						}
						dans.remove(index1);
					} else {
						if (mSelectItemIndexs.get(position3) != null) {
							if (mSelectItemIndexs.get(position3).size() == 0) {
								for (int i = 0; i < dans.size(); i++) {
									int value = dans.get(i);
									
									if (value > position3) {
										dans.remove(i);
										dans.add(i,(value - 1));
									}
								}
							}
						} else {
							for (int i = 0; i < dans.size(); i++) {
								int value = dans.get(i);
								
								if (value > position3) {
									dans.remove(i);
									dans.add(i,(value - 1));
								}
							}
						}
						List<String> chuans = mFootballLotteryHHGGUtil.getSelectChuanState();
						if (mSelectItemIndexs.size() <= Integer.parseInt(chuans.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0])) {
							dans.clear();
						}
					}
				}
				mFootballLotteryHHGGUtil.addDans(dans);
				
				if (mAdapterDataChagne != null) {
					mAdapterDataChagne.onIndexClickCartChagne();
				}
				notifyDataSetChanged();
			}
			break;
		}

	}

}
