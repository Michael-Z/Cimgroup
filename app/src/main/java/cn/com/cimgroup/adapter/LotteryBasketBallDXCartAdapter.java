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
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.FootballLotteryUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;


/**
 * 篮球大小分购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
@SuppressLint("UseValueOf")
public class LotteryBasketBallDXCartAdapter extends BaseLotteryBidOnlyCartAdapter implements
		OnClickListener {
	
	private MatchSearchType mSearchType;
	
	private FootballLotteryUtil mFootballLotteryUtil;

	public LotteryBasketBallDXCartAdapter(Context context, MatchSearchType searchType, FootballLotteryUtil footballLotteryUtil) {
		super(context);
		mSearchType = searchType;
		mFootballLotteryUtil = footballLotteryUtil;
		mLotoId = mFootballLotteryUtil.getLotId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewHolder mHolder = (ViewHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.include_basketball_select, null);
			matchTypeLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mBigView = (TextView) convertView.findViewById(R.id.txtView_basketballottery_left);
			mHolder.mSmallView = (TextView) convertView.findViewById(R.id.txtView_basketballottery_right);
			mHolder.mBigLayoutView = (LinearLayout) convertView.findViewById(R.id.layoutView_basketballottery_left);
			mHolder.mSmallLayoutView = (LinearLayout) convertView.findViewById(R.id.layoutView_basketballottery_right);
		}
		// 获取该条数据在整个数据集中得索引
//		int dataPosition = super.mSelectItemKeys.get(position);
		List<Integer> selectList = new ArrayList<Integer>(mSelectItemKeys);
		Collections.sort(selectList);
		int dataPosition = selectList.get(position);
		
		int[] arr = FootballLotteryTools.getMatchPositioin(dataPosition, getlotterybidMatchsInfos());
		Match match = mMatchs.get(arr[0]).get(arr[1]);
		
		String dxSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLDXF, mSearchType);
		
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
						mHolder.mVS.setText(str.split("\\_")[0].split("\\|")[0]);
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
			}
		}
		mHolder.mBigLayoutView.setTag(new Integer[] { dataPosition, 0 });
		mHolder.mSmallLayoutView.setTag(new Integer[] { dataPosition, 1 });
		mHolder.mBigLayoutView.setOnClickListener(this);
		mHolder.mSmallLayoutView.setOnClickListener(this);
		
		switch (mSearchType) {
		case SINGLE:
			mHolder.mDan.setVisibility(View.GONE);
			break;
		case TWIN:
			mHolder.mDan.setVisibility(View.VISIBLE);
			
			List<String> list = mFootballLotteryUtil.getSelectChuanState();
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

		private LinearLayout mBigLayoutView, mSmallLayoutView;
		private TextView mBigView, mSmallView;
	}

	@Override
	public ViewSuperHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 修改投注选项的状态
		case R.id.layoutView_basketballottery_left:
		case R.id.layoutView_basketballottery_right:
			// 获取现在item数量
			int startCount = getCount();
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 2) {
				// 获取该条数据在整个数据集中得索引
				Integer dataPosition = (Integer) obj[0];
				// 每个item的投注选项索引
				Integer index = (Integer) obj[1];
				// 获取该条在全部比赛中得索引位置
				if (mSelectItemIndexs.containsKey(dataPosition)) {
					if (mSelectItemIndexs.get(dataPosition).contains(index)) {
						switch (mSearchType) {
						case TWIN:
							if (super.mSelectItemKeys.size() > 2) {
								mSelectItemIndexs.get(dataPosition).remove(index);
							} else {
								dans.clear();
								if (mSelectItemIndexs.get(dataPosition).size() == 1) {
									ToastUtil.shortToast(mContext, "至少保留2场比赛");
									return;
								} else {
									mSelectItemIndexs.get(dataPosition).remove(index);
								}
							}
							break;
						case SINGLE:
							if (super.mSelectItemKeys.size() <= 2) {
								dans.clear();
							}
							
							if (super.mSelectItemKeys.size() > 1) {
								mSelectItemIndexs.get(dataPosition).remove(index);
							} else {
								if (mSelectItemIndexs.get(dataPosition).size() == 1) {
									ToastUtil.shortToast(mContext, "至少保留1场比赛");
									return;
								} else {
									mSelectItemIndexs.get(dataPosition).remove(index);
								}
//								ToastUtil.shortToast(mContext, "至少保留1场比赛");
//								return;
							}
							break;
						default:
							break;
						}
						if (mSelectItemIndexs.get(dataPosition).size() == 0) {
							switch (mSearchType) {
							case TWIN:
								if (super.mSelectItemKeys.size() > 2) {
									mSelectItemKeys.remove(dataPosition);
									mSelectItemIndexs.remove(dataPosition);
								}
								break;
							case SINGLE:
								if (super.mSelectItemKeys.size() <= 2) {
									dans.clear();
								}
								
								if (super.mSelectItemKeys.size() > 1) {
									mSelectItemKeys.remove(dataPosition);
									mSelectItemIndexs.remove(dataPosition);
								}
								break;
							default:
								break;
							}
						}
					} else {
						mSelectItemIndexs.get(dataPosition).add(index);
					}
				} else {
					mSelectItemIndexs.put(dataPosition, new ArrayList<Integer>());
					mSelectItemIndexs.get(dataPosition).add(index);
				}
				
				if (dans.size() > 0) {
					//删除一个胆，如果删除胆的位置后面还有胆，则胆的位置逐级减一
					Collections.sort(dans);
					if (dans.contains(dataPosition)) {
						int index1 = dans.indexOf(dataPosition);
						for (int i = 0; i < dans.size(); i++) {
							if (i > index1) {
								int value = dans.get(i);
								dans.remove(i);
								dans.add(i,(value - 1));
							}
						}
						dans.remove(index1);
					} else {
						if (mSelectItemIndexs.get(dataPosition) != null) {
							if (mSelectItemIndexs.get(dataPosition).size() == 0) {
								for (int i = 0; i < dans.size(); i++) {
									int value = dans.get(i);
									if (value > dataPosition) {
										dans.remove(i);
										dans.add(i, (value - 1));
									}
								}
							}
						} else {
							for (int i = 0; i < dans.size(); i++) {
								int value = dans.get(i);
								if (value > dataPosition) {
									dans.remove(i);
									dans.add(i, (value - 1));
								}
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
						mFootballLotteryUtil.addChuans(chuans);
					}
				}
				
				// 如果注册了数据发生改变监听，那么进行通知
				if (mAdapterDataChagne != null) {
					mAdapterDataChagne.onIndexClickCartChagne();
				}
				notifyDataSetChanged();
			}
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
					int index1 = dans.indexOf(dataPosition1);
					for (int i = 0; i < dans.size(); i++) {
						if (i > index1) {
							int value = dans.get(i);
							dans.remove(i);
							dans.add(i,(value - 1));
						}
					}
					dans.remove(index1);
				} else {
					for (int i = 0; i < dans.size(); i++) {
						int value = dans.get(i);
						if (value > dataPosition1) {
							dans.remove(i);
							dans.add(i, (value - 1));
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

}
