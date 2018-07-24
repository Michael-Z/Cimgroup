package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.com.cimgroup.bean.MatchFootballState;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.FootballLotteryUtil;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 半全场购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
@SuppressWarnings("unchecked")
public class LotteryFootBallBQCCartAdapter extends BaseLotteryBidOnlyCartAdapter implements
		OnClickListener, OnClickEvent {

	// 投注选项dialog
	private FootballLotteryWayDailog mDialog = null;
	
	private MatchSearchType mSearchType;
	
	private FootballLotteryUtil mFootballLotteryUtil;

	public LotteryFootBallBQCCartAdapter(Context context, MatchSearchType searchType, FootballLotteryUtil footballLotteryUtil) {
		super(context);
		mSearchType = searchType;
		mFootballLotteryUtil = footballLotteryUtil;
		// TODO Auto-generated constructor stub‘
		mDialog = new FootballLotteryWayDailog(mContext, this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewHolder mHolder = (ViewHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_footballlottery_click_open, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mOpenClickView = (TextView) convertView.findViewById(R.id.txtView_footballlottery_onclick_open);
		}
		// 获取该条数据在整个数据集中得索引
//		int dataPosition = super.mSelectItemKeys.get(position);
		List<Integer> selectList = new ArrayList<Integer>(mSelectItemKeys);
		Collections.sort(selectList);
		int dataPosition = selectList.get(position);
		
		int[] arr = FootballLotteryTools.getMatchPositioin(dataPosition, getlotterybidMatchsInfos());
		Match match = mMatchs.get(arr[0]).get(arr[1]);
		
		String bqcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
		
		// 判断是否在投注选项中进行选择，如果选择了，那么在item进行展示
		if (mSelectItemIndexs.containsKey(dataPosition)) {
			StringBuilder builder = new StringBuilder();
			// 获取投注选项的索引
			List<Integer> selectWayIndex = mSelectItemIndexs.get(dataPosition);
			if (selectWayIndex != null && selectWayIndex.size() > 0) {
				for (int i = 0; i < selectWayIndex.size(); i++) {
					if(bqcSp.indexOf("@") != -1){
						String[] bqcArr = bqcSp.split("\\@");
						if(bqcArr[selectWayIndex.get(i)].split("\\_").length > 1) {
							builder.append(bqcArr[selectWayIndex.get(i)].split("\\_")[0] + ",");
						}
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
		
		switch (mSearchType) {
		case SINGLE:
			mHolder.mDan.setVisibility(View.GONE);
			break;
		case TWIN:
			mHolder.mDan.setVisibility(View.VISIBLE);
			
			List<String> list = mFootballLotteryUtil.getSelectChuanState();
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
		
		match.setPosition(dataPosition);
		mHolder.mOpenClickView.setTag(match);
		return super.getView(position, convertView, parent);
	}

	class ViewHolder extends ViewSuperHolder {

		/** 胜平负 **/
		private TextView mOpenClickView;
	}

	@Override
	public ViewSuperHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.txtView_footballlottery_onclick_open:
			// 获取该条数据在整个数据集中得索引
			Match match = (Match) v.getTag();
			Integer dataPosition = match.getPosition();
			
			// TODO Auto-generated method stub
			// 获取MatchNo
			if (mSelectItemIndexs.get(dataPosition) == null) {
				mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
			}
			// 弹出选择投注选项dialog
			mDialog.showDialog(DialogType.BQC, mSelectItemIndexs.get(dataPosition), dataPosition, match, mSearchType);
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
					List<String> chuans = new ArrayList<String>(mFootballLotteryUtil.getSelectChuanState());
					for (int i = 0; i < chuans.size(); i++) {
						if (mSelectItemIndexs.size() < Integer.parseInt(chuans.get(i).split(mContext.getResources().getString(R.string.football_chuan))[0])) {
							chuans.remove(i);
						}
					}
					if (mSelectItemIndexs.size() <= Integer.parseInt(chuans.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0])) {
						dans.clear();
					}
				}
			}
			mFootballLotteryUtil.addDans(dans);
			// 如果注册了数据发生改变监听，那么进行通知
			if (mAdapterDataChagne != null) {
				mAdapterDataChagne.onIndexClickCartChagne();
			}
			notifyDataSetChanged();
			break;
		case R.id.txtView_lotterybid_cart_dan:
			int position = (Integer) v.getTag();
			Object[] result = null;
			
			List<String> list = mFootballLotteryUtil.getSelectChuanState();
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
			mFootballLotteryUtil.addDans(dans);
//			result = mFootballLotteryUtil.startCalculate();
//			int itemTotal = (Integer) result[0];
//			int numPrice = (Integer) result[1];
//			String winPrice = (String) result[2];
//			((LotteryBidCartActivity)mContext).mTotal2PriceView.setText(mContext.getResources().getString(R.string.num_money, itemTotal, Integer.parseInt(((LotteryBidCartActivity)mContext).mMultipleEditView.getText().toString()), numPrice));
//			((LotteryBidCartActivity)mContext).mWinPriceView.setText(winPrice + mContext.getResources().getString(R.string.lemi_unit));
			if (mAdapterDataChagne != null) {
				mAdapterDataChagne.onIndexClickCartChagne();
			}
			notifyDataSetChanged();
			break;
		default:
			break;
		}

	}

	@Override
	public void OnOKClick(Object... objects) {
		// 获取现在item数量
		int startCount = getCount();
		// 将原来的选择的值清空，从新赋值
		if (objects != null && objects.length == 2) {
			Integer position = (Integer) objects[1];
			List<Integer> indexs = (List<Integer>) objects[0];
			// 删除此项原来的数据，以下为重新赋值
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
			
			Collections.sort(super.mSelectItemKeys);
			int dataPosition = super.mSelectItemKeys.indexOf(position);
			if (indexs != null) {
				if (indexs.size() != 0) {
					mSelectItemIndexs.put(position, indexs);
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
					for (int i = 0; i < dans.size(); i++) {
						int value = dans.get(i);
						
						if (value > position) {
							dans.remove(i);
							dans.add(i,(value - 1));
						}
					}
					List<String> chuans = mFootballLotteryUtil.getSelectChuanState();
					if (mSelectItemIndexs.size() <= Integer.parseInt(chuans.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0])) {
						dans.clear();
					}
				}
			}
			mFootballLotteryUtil.addDans(dans);
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
