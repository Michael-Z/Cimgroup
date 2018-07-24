package cn.com.cimgroup.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryCartActivity.CodeInfo;
import cn.com.cimgroup.bean.LotterySaveObj;
import cn.com.cimgroup.config.DLTConfiguration;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 排列3购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月21日
 */
public class LotteryPL3CartActivity extends LotteryCartActivity {

	private int lottery_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitleTextView.setText(getResources().getString(R.string.lottery_p3_tz));
//		LotterySaveObj obj = DLTConfiguration.getConfiguration().getLocalUserInfo();
		if (obj != null) {
			List<CodeInfo> arr = obj.getData();
			if (arr != null) {
				for (CodeInfo info : arr) {
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					ArrayList<Integer> list2 = new ArrayList<Integer>();
					ArrayList<Integer> list3 = new ArrayList<Integer>();
					/**
					 * 排三 typeId 01直选，02直选和值，03排三组三，04组选和值，05排三组六 seleId 01单式，02复式
					 */
					String str = info.mCode;
					String[] strArr = str.split("\\|");
					String[] strArr1 = strArr[0].split("\\,");
					for (String string : strArr1) {
						list1.add(Integer.parseInt(string));
					}
					switch (info.mTypeId) {
					case "01":
						String[] strArr2 = strArr[1].split("\\,");
						String[] strArr3 = strArr[2].split("\\,");
						
						for (String string : strArr2) {
							list2.add(Integer.parseInt(string));
						}
						
						for (String string : strArr3) {
							list3.add(Integer.parseInt(string));
						}
						setZxCartLayout(list1, list2, list3);
						break;
					case "02":
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_ZHIXHZ, false);
						break;
					case "03":
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z3, false);
						break;
					case "04":
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z6, true);
						break;
					case "05":
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_ZXHZ, false);
						break;
					default:
						break;
					}
				}
				mIssueTextView.setText(intent.getStringExtra(GlobalConstants.LOTTERY_ISSUE));
				if (!StringUtil.isEmpty(intent.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME))) {
					getTermAndTime(intent.getStringExtra(GlobalConstants.LOTTERY_ISSUE),System.currentTimeMillis() + "",intent.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME));
				}
			} else {
//				initData(intent);
			}
		} else {
//			initData(intent);
		}
		initData(intent);
	}

	@SuppressLint("NewApi") @Override
	public void initData(Intent data) {
		lottery_type = data.getIntExtra(GlobalConstants.LOTTERY_TYPE, 0);
		mIssueTextView.setText(data.getStringExtra(GlobalConstants.LOTTERY_ISSUE));
		int jxNum = data.getIntExtra(GlobalConstants.LIST_JXNUM, 0);
//		mIssueTimeTextView.setText("截止到：" + data.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME));
		if (!StringUtil.isEmpty(data.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME))) {
			getTermAndTime(data.getStringExtra(GlobalConstants.LOTTERY_ISSUE),System.currentTimeMillis() + "",data.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME));
		}
		boolean isItemForResult = data.getBooleanExtra(GlobalConstants.CART_IS_ITEM_FORRESULT, false);
		ArrayList<Integer> list1 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST1);
		switch (lottery_type) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			ArrayList<Integer> list2 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST2);
			ArrayList<Integer> list3 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST3);
			if (isItemForResult) {
				bindZxCartLayout(list1, list2, list3, mClickCartLayout);
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < list1.size() / 3; i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						ArrayList<Integer> tempList2 = new ArrayList<Integer>();
						ArrayList<Integer> tempList3 = new ArrayList<Integer>();
						tempList1.add(list1.get(i));
						tempList2.add(list2.get(i + 1));
						tempList3.add(list3.get(i + 2));
						setZxCartLayout(tempList1, tempList2, tempList3);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (list1.size() > 0 && list2.size() > 0 && list3.size() > 0) {
						setZxCartLayout(list1, list2, list3);
					}
				}
			}
			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			if (isItemForResult) {
				bindOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_ZHIXHZ, mClickCartLayout, false);
				mClickCartLayout.mTVCartDel.callOnClick();
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < list1.size(); i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						tempList1.add(list1.get(i));
						setOnlyListCartLayout(tempList1, GlobalConstants.LOTTERY_PL3_ZHIXHZ, false);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (list1.size() > 0) {
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_ZHIXHZ, false);
					}
				}
			}
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			if (isItemForResult) {
				bindOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z3, mClickCartLayout, false);
				mClickCartLayout.mTVCartDel.callOnClick();
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < list1.size()/2; i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						tempList1.add(list1.get(i));
						tempList1.add(list1.get(i + 1));
						setOnlyListCartLayout(tempList1, GlobalConstants.LOTTERY_PL3_Z3, false);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (list1.size() > 0) {
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z3, false);
					}
				}
			}
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			if (isItemForResult) {
				bindOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_ZXHZ, mClickCartLayout, true);
				mClickCartLayout.mTVCartDel.callOnClick();
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < list1.size(); i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						tempList1.add(list1.get(i));
						setOnlyListCartLayout(tempList1, GlobalConstants.LOTTERY_PL3_ZXHZ, true);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (list1.size() > 0) {
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_ZXHZ, true);
					}
				}
			}
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			if (isItemForResult) {
				bindOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z6, mClickCartLayout, false);
				mClickCartLayout.mTVCartDel.callOnClick();
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < list1.size()/3; i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						tempList1.add(list1.get(i));
						tempList1.add(list1.get(i + 1));
						tempList1.add(list1.get(i + 2));
						setOnlyListCartLayout(tempList1, GlobalConstants.LOTTERY_PL3_Z6, false);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (list1.size() > 0) {
						setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z6, false);
					}
				}
			}
			break;
		case GlobalConstants.LOTTERY_DLT_JX:
			lottery_type = data.getIntExtra(GlobalConstants.LOTTERY_P3_TYPE, 0);
			
			for (int i = 0; i < jxNum; i++) {
				jxIntoCart();
			}
			break;
		default:
			break;
		}
		getTotalNumMoney();
	}

	private void setZxCartLayout(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		CartLayout cartHolder = initSetLayout(mCartLayout);
		initKeep();

		bindZxCartLayout(new ArrayList<Integer>(list1), new ArrayList<Integer>(list2), new ArrayList<Integer>(list3), cartHolder);
	}

	private void bindZxCartLayout(ArrayList<Integer> bwList, ArrayList<Integer> swList, ArrayList<Integer> gwList, CartLayout cartHolder) {
		Collections.sort(bwList);
		Collections.sort(swList);
		Collections.sort(gwList);
		int itemNum = bwList.size() * swList.size() * gwList.size();
		int itemMoney = itemNum * 2;
		initSetTag(cartHolder, itemNum, itemMoney);
		cartHolder.mRLCart.setTag(R.id.tag_list1, bwList);
		cartHolder.mRLCart.setTag(R.id.tag_list2, swList);
		cartHolder.mRLCart.setTag(R.id.tag_list3, gwList);
		cartHolder.mRLCart.setTag(R.id.tag_ball_type, GlobalConstants.LOTTERY_PL3_ZX);
		cartHolder.mTVCartType.setText(LotteryBettingUtil.cartPl3_ZxType(bwList, swList, gwList));
		cartHolder.mFLCartNum.removeAllViews();
		for (Integer integer : bwList) {
			TextView numTv = new TextView(mActivity);
			numTv.setTextSize(textSize);
			numTv.setText(integer + "");
			numTv.setTextColor(redColor);
			cartHolder.mFLCartNum.addView(numTv);
		}
		TextView tv1 = new TextView(mActivity);
		tv1.setTextSize(textSize);
		tv1.setTextColor(redColor);
		tv1.setText("|");
		cartHolder.mFLCartNum.addView(tv1);
		for (Integer integer : swList) {
			TextView numTv = new TextView(mActivity);
			numTv.setTextSize(textSize);
			numTv.setText(integer + "");
			numTv.setTextColor(redColor);
			cartHolder.mFLCartNum.addView(numTv);
		}
		TextView tv2 = new TextView(mActivity);
		tv2.setTextSize(textSize);
		tv2.setTextColor(redColor);
		tv2.setText("|");
		cartHolder.mFLCartNum.addView(tv2);
		for (Integer integer : gwList) {
			TextView numTv = new TextView(mActivity);
			numTv.setTextSize(textSize);
			numTv.setText(integer + "");
			numTv.setTextColor(redColor);
			cartHolder.mFLCartNum.addView(numTv);
		}
	}

	private void setOnlyListCartLayout(ArrayList<Integer> list, int ballType, boolean isZx) {
//		CartLayout cartHolder = initSetLayout(mCartLayout);
		initKeep();
		bindOnlyListCartLayout(new ArrayList<Integer>(list), ballType, null, isZx);
	}

	private void bindOnlyListCartLayout(ArrayList<Integer> redList, int ballType, CartLayout cartHolder1, boolean isZx) {
		int itemNum = 0;
		String cartType = "";
		Collections.sort(redList);
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		switch (ballType) {
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			itemNum = LotteryBettingUtil.getPl3_ZhixhzNum(redList);
			list = LotteryBettingUtil.getPl3_Zhixhz(redList);
			cartType = getResources().getString(R.string.lottery_p3_zxhz);
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			itemNum = LotteryBettingUtil.getPl3_ZxhzNum(redList);
			list = LotteryBettingUtil.getPl3_Zxhz(redList);
			cartType = getResources().getString(R.string.lottery_p3_zuxhz);
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			itemNum = (int) LotteryBettingUtil.Arrangement(2, redList.size());
			itemNum = 1;
			list = LotteryBettingUtil.ArrangementPL3Z3(redList);
			cartType = getResources().getString(R.string.lottery_p3_z3);
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			itemNum = LotteryBettingUtil.Combination(3, redList.size());
			itemNum = 1;
			list = LotteryBettingUtil.CombinationPL3Z6(redList);
			if (redList.size() > 3) {
				cartType = getResources().getString(R.string.lottery_p3_z6);
			} else {
				cartType = getResources().getString(R.string.lottery_p3_z6_ds);
			}
			break;
		default:
			break;
		}
		int itemMoney = itemNum * 2;
		
//		initSetTag(cartHolder, itemNum, itemMoney);
//		cartHolder.mRLCart.setTag(R.id.tag_ball_type, ballType);
//		cartHolder.mRLCart.setTag(R.id.tag_list1, redList);
//		cartHolder.mTVCartType.setText(cartType);
//		cartHolder.mFLCartNum.removeAllViews();
//		for (Integer integer : redList) {
//			TextView numTv = new TextView(mActivity);
//			numTv.setTextSize(textSize);
//			if (isZx) {
//				numTv.setText((integer + 1) + "");
//			} else {
//				numTv.setText(integer + "");
//			}
//			numTv.setTextColor(redColor);
//			cartHolder.mFLCartNum.addView(numTv);
//		}
		for (List<Integer> items : list) {
			switch (ballType) {
			case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
				if (items.size() > 1) {
					List<Integer> temp = new ArrayList<Integer>();
					if (items.get(0) == 9) {
						temp.add(27);
					} else {
						temp.add(items.get(0));
					}
					
					cartType = getResources().getString(R.string.lottery_p3_zxds);
					itemNum = LotteryBettingUtil.getPl3_ZhixhzNum(new ArrayList<Integer>(temp));
				} else {
					cartType = getResources().getString(R.string.lottery_p3_zxhz);
					itemNum = LotteryBettingUtil.getPl3_ZhixhzNum(new ArrayList<Integer>(items));
				}
				break;
			case GlobalConstants.LOTTERY_PL3_ZXHZ:
				if (items.size() > 1) {
					List<Integer> temp = new ArrayList<Integer>();
					if (items.get(0) == 8) {
						temp.add(25);
					} else {
						temp.add(items.get(0));
					}
					cartType = getResources().getString(R.string.lottery_p3_z3ds);
					itemNum = LotteryBettingUtil.getPl3_ZxhzNum(new ArrayList<Integer>(temp));
				} else {
					cartType = getResources().getString(R.string.lottery_p3_zuxhz);
					itemNum = LotteryBettingUtil.getPl3_ZxhzNum(new ArrayList<Integer>(items));
				}
				break;
			default:
				break;
			}
			itemMoney = itemNum * 2;
			CartLayout cartHolder = initSetLayout(mCartLayout);
			initSetTag(cartHolder, itemNum, itemMoney);
			cartHolder.mRLCart.setTag(R.id.tag_ball_type, ballType);
			cartHolder.mRLCart.setTag(R.id.tag_list1, items);
			cartHolder.mTVCartType.setText(cartType);
			cartHolder.mFLCartNum.removeAllViews();
			TextView numTv = new TextView(mActivity);
			numTv.setTextSize(textSize);
			StringBuilder sb = new StringBuilder();
			if (items.size() > 1) {
				for (int i : items) {
					if (ballType == GlobalConstants.LOTTERY_PL3_ZHIXHZ) {
						sb.append(i + " | ");
					} else {
						sb.append(i + " , ");
					}
				}
			} else {
				if (isZx) {
					sb.append(items.get(0) + 1 + " , ");
				} else {
					sb.append(items.get(0) + " , ");
				}
			}
			sb.delete(sb.length() - 2, sb.length());
			numTv.setText(sb.toString());
			numTv.setTextColor(redColor);
			cartHolder.mFLCartNum.addView(numTv);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void itemCartClick(View v) {
		super.itemCartClick(v);
		Intent intent = new Intent(mActivity, LotteryPL32Activity.class);
		ArrayList<Integer> list1 = (ArrayList<Integer>) v.getTag(R.id.tag_list1);
		Integer tag = (Integer) v.getTag(R.id.tag_ball_type);
		switch (tag) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, (ArrayList<Integer>) v.getTag(R.id.tag_list2));
			intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, (ArrayList<Integer>) v.getTag(R.id.tag_list3));
			break;
//		case GlobalConstants.LOTTERY_PL3_Z3DS:
//			intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, (ArrayList<Integer>) v.getTag(R.id.tag_list2));
//			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			List<Integer> temp = new ArrayList<Integer>();
			if (list1.size() > 1) {
				temp.add(list1.get(0) + list1.get(1) + list1.get(2));
				list1 = (ArrayList<Integer>) temp;
			}
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			List<Integer> temp1 = new ArrayList<Integer>();
			if (list1.size() > 1) {
				temp1.add(list1.get(0) + list1.get(1) + list1.get(2) - 1);
				list1 = (ArrayList<Integer>) temp1;
			}
			
			break;
		default:
			break;
		}
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, list1);
		intent.putExtra(GlobalConstants.LOTTERY_TYPE, tag);
		intent.putExtra(GlobalConstants.CART_IS_FORRESULT, true);
		mActivity.startActivityForResult(intent, GlobalConstants.CART_ITEM_RESULT);
	}

	@Override
	public void jxIntoCart() {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		switch (lottery_type) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			LotteryShowUtil.randomBall(list1, 1, 10);
			ArrayList<Integer> list2 = new ArrayList<Integer>();
			ArrayList<Integer> list3 = new ArrayList<Integer>();
			LotteryShowUtil.randomBall(list2, 1, 10);
			LotteryShowUtil.randomBall(list3, 1, 10);
			setZxCartLayout(list1, list2, list3);
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			ArrayList<Integer> zxhzList = new ArrayList<Integer>();
			LotteryShowUtil.randomBall(zxhzList, 1, 26);
			setOnlyListCartLayout(zxhzList, GlobalConstants.LOTTERY_PL3_ZXHZ, true);
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			LotteryShowUtil.randomBall(list1, 2, 10);
			setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z3, false);
			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			ArrayList<Integer> zhixhzList = new ArrayList<Integer>();
			LotteryShowUtil.randomBall(zhixhzList, 1, 27);
			setOnlyListCartLayout(zhixhzList, GlobalConstants.LOTTERY_PL3_ZHIXHZ, false);
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			LotteryShowUtil.randomBall(list1, 3, 10);
			setOnlyListCartLayout(list1, GlobalConstants.LOTTERY_PL3_Z6, false);
			break;

		default:
			break;
		}
	}

	@Override
	public void zxIntoCart() {
		zxIntent(LotteryPL32Activity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void goBuy(LinearLayout itemLayout, CodeInfo codeInfo) {
		int ballType = (Integer) itemLayout.getTag(R.id.tag_ball_type);
		ArrayList<Integer> list1 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list1);
		//每一注的价格
		int zhuMoney = 2;
		//typeId 01直选，02直选和值，03排三组三，04组选和值，05排三组六
		//seleId 01单式，02复式
		switch (ballType) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			ArrayList<Integer> list2 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list2);
			ArrayList<Integer> list3 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list3);
			codeInfo.mNum = list1.size() * list2.size() * list3.size();
			codeInfo.mPrice = codeInfo.mNum * zhuMoney;
			codeInfo.mCode = LotteryBettingUtil.tzPl3_Zx(list1, list2, list3);
			codeInfo.mTypeId = "01";
			codeInfo.mSeleId = LotteryBettingUtil.getSeleID_Pl3_Zx(list1, list2, list3);
			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			if (list1.size() > 1) {
				List<Integer> temp = new ArrayList<Integer>();
				if (list1.get(0) == 9){
					temp.add(9);
				} else {
					temp.add(0);
				}
				codeInfo.mNum = temp.size() * temp.size() * temp.size();
				codeInfo.mPrice = codeInfo.mNum * zhuMoney;
				codeInfo.mCode = LotteryBettingUtil.tzPl3_Zx((ArrayList<Integer>)temp, (ArrayList<Integer>)temp, (ArrayList<Integer>)temp);
				codeInfo.mTypeId = "01";
				codeInfo.mSeleId = "01";
			} else {
//				codeInfo.mCode = new DecimalFormat("00").format(list1.get(0));
				codeInfo.mCode = list1.get(0) + "";
				codeInfo.mNum = LotteryBettingUtil.getPl3_ZhixhzNum(list1);
				codeInfo.mPrice = codeInfo.mNum * zhuMoney;
				codeInfo.mTypeId = "02";
				codeInfo.mSeleId = "01";
			}
//			codeInfo.mTypeId = "02";
//			codeInfo.mSeleId = "01";
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			codeInfo.mCode = LotteryBettingUtil.tzPl3_z3(list1);
//			codeInfo.mNum = (int) LotteryBettingUtil.Arrangement(2, list1.size());
			codeInfo.mNum = 1;
			codeInfo.mPrice = codeInfo.mNum * zhuMoney;
			codeInfo.mTypeId = "03";
			if (list1.size() > 2) {
				codeInfo.mSeleId = "02";
			} else {
				codeInfo.mSeleId = "01";
			}
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			if (list1.size() > 1) {
//				List<Integer> temp = new ArrayList<Integer>();
//				if (list1.get(0) == 8){
//					temp.add(25);
//				} else {
//					temp.add(0);
//				}
				codeInfo.mCode = list1.get(0) + "," + list1.get(1) + "," + list1.get(2);
				codeInfo.mNum = 1;
				codeInfo.mPrice = codeInfo.mNum * zhuMoney;
				codeInfo.mTypeId = "03";
				codeInfo.mSeleId = "01";
			} else {
//				codeInfo.mCode = new DecimalFormat("00").format(list1.get(0) + 1);
				codeInfo.mCode = (list1.get(0) + 1) + "";
				codeInfo.mNum = LotteryBettingUtil.getPl3_ZxhzNum(list1);
				codeInfo.mPrice = codeInfo.mNum * zhuMoney;
				codeInfo.mTypeId = "05";
				codeInfo.mSeleId = "01";
			}
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			codeInfo.mCode = LotteryBettingUtil.tzPl3_z3(list1);
//			codeInfo.mNum = LotteryBettingUtil.Combination(3, list1.size());
			codeInfo.mNum = 1;
			codeInfo.mPrice = codeInfo.mNum * zhuMoney;
			codeInfo.mTypeId = "04";
			if (list1.size() > 3) {
				codeInfo.mSeleId = "02";
			} else {
				codeInfo.mSeleId = "01";
			}
			break;
		default:
			break;
		}
	}

	@Override
	public String getGameType() {
		return GlobalConstants.TC_PL3;
	}

	@Override
	public String getTitle(String term, int buyType) {
		return getResources().getString(R.string.lottery_p3tz_buytype, term, getBuyTypeTitle(buyType));
	}

	@Override
	public int getLotteryName() {
		return GlobalConstants.LOTTERY_PL3;
	}
}
