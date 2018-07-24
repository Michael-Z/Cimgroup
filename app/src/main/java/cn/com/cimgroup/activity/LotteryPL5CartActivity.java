package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import cn.com.cimgroup.xutils.XLog;

/**
 * 排列5购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryPL5CartActivity extends LotteryCartActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitleTextView.setText(getResources().getString(R.string.lottery_p5_tz));
		
//		LotterySaveObj obj = DLTConfiguration.getConfiguration().getLocalUserInfo();
		if (obj != null) {
			List<CodeInfo> arr = obj.getData();
			if (arr != null) {
				for (CodeInfo info : arr) {
					String str = info.mCode;
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					ArrayList<Integer> list2 = new ArrayList<Integer>();
					ArrayList<Integer> list3 = new ArrayList<Integer>();
					ArrayList<Integer> list4 = new ArrayList<Integer>();
					ArrayList<Integer> list5 = new ArrayList<Integer>();
					String[] strArr = str.split("\\|");
					String[] strArr1 = strArr[0].split("\\,");
					String[] strArr2 = strArr[1].split("\\,");
					String[] strArr3 = strArr[2].split("\\,");
					String[] strArr4 = strArr[3].split("\\,");
					String[] strArr5 = strArr[4].split("\\,");
					for (String string : strArr1) {
						list1.add(Integer.parseInt(string));
					}
					
					for (String string : strArr2) {
						list2.add(Integer.parseInt(string));
					}
					
					for (String string : strArr3) {
						list3.add(Integer.parseInt(string));
					}
					
					for (String string : strArr4) {
						list4.add(Integer.parseInt(string));
					}
					
					for (String string : strArr5) {
						list5.add(Integer.parseInt(string));
					}
					setCartLayout(list1, list2, list3, list4, list5);
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

	@Override
	public void initData(Intent data) {
		int lottery_type = data.getIntExtra(GlobalConstants.LOTTERY_TYPE, 0);
		int jxNum = data.getIntExtra(GlobalConstants.LIST_JXNUM, 0);
		mIssueTextView.setText(data.getStringExtra(GlobalConstants.LOTTERY_ISSUE));
//		mIssueTimeTextView.setText("截止到：" + data.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME));
		if (!StringUtil.isEmpty(data.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME))) {
			getTermAndTime(data.getStringExtra(GlobalConstants.LOTTERY_ISSUE),System.currentTimeMillis() + "",data.getStringExtra(GlobalConstants.LOTTERY_ISSUE_TIME));
		}
		switch (lottery_type) {
		case GlobalConstants.LOTTERY_P5_CART:
			ArrayList<Integer> list1 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST1);
			ArrayList<Integer> list2 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST2);
			ArrayList<Integer> list3 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST3);
			ArrayList<Integer> list4 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST4);
			ArrayList<Integer> list5 = data.getIntegerArrayListExtra(GlobalConstants.BALL_LIST5);
			if (data.getBooleanExtra(GlobalConstants.CART_IS_ITEM_FORRESULT, false)) {
				bindCartLayout(list1, list2, list3, list4, list5, mClickCartLayout);
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < list1.size(); i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						ArrayList<Integer> tempList2 = new ArrayList<Integer>();
						ArrayList<Integer> tempList3 = new ArrayList<Integer>();
						ArrayList<Integer> tempList4 = new ArrayList<Integer>();
						ArrayList<Integer> tempList5 = new ArrayList<Integer>();
						tempList1.add(list1.get(i));
						tempList2.add(list2.get(i));
						tempList3.add(list3.get(i));
						tempList4.add(list4.get(i));
						tempList5.add(list5.get(i));
						setCartLayout(tempList1, tempList2, tempList3, tempList4, tempList5);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (list1.size() > 0 && list2.size() > 0 && list3.size() > 0 && list4.size() > 0 && list5.size() > 0) {
						setCartLayout(list1, list2, list3, list4, list5);
					}
				}
			}
			getTotalNumMoney();
			break;
		case GlobalConstants.LOTTERY_DLT_JX:
			for (int i = 0; i < jxNum; i++) {
				jxIntoCart();
			}
			getTotalNumMoney();
			break;
		default:
			break;
		}
	}

	private void setCartLayout(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5) {
		CartLayout cartHolder = initSetLayout(mCartLayout);
		initKeep();
		bindCartLayout(new ArrayList<Integer>(list1), new ArrayList<Integer>(list2), new ArrayList<Integer>(list3), new ArrayList<Integer>(list4), new ArrayList<Integer>(list5), cartHolder);
	}

	private void bindCartLayout(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5, CartLayout cartHolder) {
		Collections.sort(list1);
		Collections.sort(list2);
		Collections.sort(list3);
		Collections.sort(list4);
		Collections.sort(list5);
		int itemNum = list1.size() * list2.size() * list3.size() * list4.size() * list5.size();
		int itemMoney = itemNum * 2;
		initSetTag(cartHolder, itemNum, itemMoney);
		cartHolder.mRLCart.setTag(R.id.tag_list1, list1);
		cartHolder.mRLCart.setTag(R.id.tag_list2, list2);
		cartHolder.mRLCart.setTag(R.id.tag_list3, list3);
		cartHolder.mRLCart.setTag(R.id.tag_list4, list4);
		cartHolder.mRLCart.setTag(R.id.tag_list5, list5);
		cartHolder.mTVCartType.setText(LotteryBettingUtil.cartPl5Type(list1, list2, list3, list4, list5));
		cartHolder.mFLCartNum.removeAllViews();
		for (Integer integer : list1) {
			TextView tv = new TextView(mActivity);
			tv.setTextSize(textSize);
			tv.setTextColor(redColor);
			tv.setText(integer + "");
			cartHolder.mFLCartNum.addView(tv);
		}
		TextView tv1 = new TextView(mActivity);
		tv1.setTextSize(textSize);
		tv1.setTextColor(redColor);
		tv1.setText("|");
		cartHolder.mFLCartNum.addView(tv1);
		for (Integer integer : list2) {
			TextView tv = new TextView(mActivity);
			tv.setTextSize(textSize);
			tv.setTextColor(redColor);
			tv.setText(integer + "");
			cartHolder.mFLCartNum.addView(tv);
		}
		TextView tv2 = new TextView(mActivity);
		tv2.setTextSize(textSize);
		tv2.setTextColor(redColor);
		tv2.setText("|");
		cartHolder.mFLCartNum.addView(tv2);
		for (Integer integer : list3) {
			TextView tv = new TextView(mActivity);
			tv.setTextSize(textSize);
			tv.setTextColor(redColor);
			tv.setText(integer + "");
			cartHolder.mFLCartNum.addView(tv);
		}
		TextView tv3 = new TextView(mActivity);
		tv3.setTextSize(textSize);
		tv3.setTextColor(redColor);
		tv3.setText("|");
		cartHolder.mFLCartNum.addView(tv3);
		for (Integer integer : list4) {
			TextView tv = new TextView(mActivity);
			tv.setTextSize(textSize);
			tv.setTextColor(redColor);
			tv.setText(integer + "");
			cartHolder.mFLCartNum.addView(tv);
		}
		TextView tv4 = new TextView(mActivity);
		tv4.setTextSize(textSize);
		tv4.setTextColor(redColor);
		tv4.setText("|");
		cartHolder.mFLCartNum.addView(tv4);
		for (Integer integer : list5) {
			TextView tv = new TextView(mActivity);
			tv.setTextSize(textSize);
			tv.setTextColor(redColor);
			tv.setText(integer + "");
			cartHolder.mFLCartNum.addView(tv);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void itemCartClick(View v) {
		super.itemCartClick(v);
		Intent intent = new Intent(mActivity, LotteryPL52Activity.class);
		intent.putExtra(GlobalConstants.CART_IS_FORRESULT, true);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, (ArrayList<Integer>) v.getTag(R.id.tag_list1));
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, (ArrayList<Integer>) v.getTag(R.id.tag_list2));
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, (ArrayList<Integer>) v.getTag(R.id.tag_list3));
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST4, (ArrayList<Integer>) v.getTag(R.id.tag_list4));
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST5, (ArrayList<Integer>) v.getTag(R.id.tag_list5));
		mActivity.startActivityForResult(intent, GlobalConstants.CART_ITEM_RESULT);
	}

	@Override
	public void jxIntoCart() {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		ArrayList<Integer> list3 = new ArrayList<Integer>();
		ArrayList<Integer> list4 = new ArrayList<Integer>();
		ArrayList<Integer> list5 = new ArrayList<Integer>();
		LotteryShowUtil.randomBall(list1, 1, 10);
		LotteryShowUtil.randomBall(list2, 1, 10);
		LotteryShowUtil.randomBall(list3, 1, 10);
		LotteryShowUtil.randomBall(list4, 1, 10);
		LotteryShowUtil.randomBall(list5, 1, 10);
		setCartLayout(list1, list2, list3, list4, list5);
	}

	@Override
	public void zxIntoCart() {
		zxIntent(LotteryPL52Activity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void goBuy(LinearLayout itemLayout, CodeInfo codeInfo) {
		ArrayList<Integer> list1 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list1);
		ArrayList<Integer> list2 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list2);
		ArrayList<Integer> list3 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list3);
		ArrayList<Integer> list4 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list4);
		ArrayList<Integer> list5 = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_list5);
		codeInfo.mCode = LotteryBettingUtil.tzPl5(list1, list2, list3, list4, list5);
		codeInfo.mNum = list1.size() * list2.size() * list3.size() * list4.size() * list5.size();
		codeInfo.mPrice = codeInfo.mNum * 2;
		codeInfo.mTypeId = "00";
		codeInfo.mSeleId = LotteryBettingUtil.getSeleID_Pl5(list1, list2, list3, list4, list5);
	}

	@Override
	public String getGameType() {
		return GlobalConstants.TC_PL5;
	}

	@Override
	public String getTitle(String term, int buyType) {
		return getResources().getString(R.string.lottery_p3tz_buytype, term, getBuyTypeTitle(buyType));
	}

	@Override
	public int getLotteryName() {
		return GlobalConstants.LOTTERY_PL5;
	}


}
