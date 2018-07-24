package cn.com.cimgroup.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

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
 * 大乐透购物车
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月19日
 */
public class LotteryDLTCartActivity extends LotteryCartActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitleTextView.setText(getResources().getString(R.string.lottery_dlt_tz));
//		LotterySaveObj obj = DLTConfiguration.getConfiguration().getLocalUserInfo();
		if (obj != null) {
			List<CodeInfo> arr = obj.getData();
			if (arr != null) {
				for (CodeInfo info : arr) {
					String str = info.mCode;
					ArrayList<Integer> jxList1 = new ArrayList<Integer>();
					ArrayList<Integer> jxList2 = new ArrayList<Integer>();
					ArrayList<Integer> jxList3 = new ArrayList<Integer>();
					ArrayList<Integer> jxList4 = new ArrayList<Integer>();
					
					String[] strArr = str.split("\\|");
					String[] strArr1 = strArr[0].split("\\,");
					String[] strArr2 = strArr[1].split("\\,");
					
					for (String string : strArr1) {
						jxList1.add(Integer.parseInt(string) - 1);
					}
					
					for (String string : strArr2) {
						jxList2.add(Integer.parseInt(string) - 1);
					}
					if (strArr.length > 2) {
						String[] strArr3 = strArr[2].split("\\,");
						String[] strArr4 = strArr[3].split("\\,");
						
						for (String string : strArr3) {
							jxList3.add(Integer.parseInt(string));
						}
						
						for (String string : strArr4) {
							jxList4.add(Integer.parseInt(string));
						}
						setDTCartLayout(jxList1, jxList2, jxList3, jxList4);
					} else {
						setCartLayout(jxList1, jxList2);
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
		case GlobalConstants.LOTTERY_DLT_PT:// 大乐透普通
			ArrayList<Integer> redList = data.getIntegerArrayListExtra(GlobalConstants.LIST_REDBALL);
			ArrayList<Integer> buleList = data.getIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL);
			if (data.getBooleanExtra(GlobalConstants.CART_IS_ITEM_FORRESULT, false)) {
				bindBZCartLayout(redList, buleList, mClickCartLayout);
			} else {
				if (jxNum > 0) {
					for (int i = 0; i < redList.size() / 5; i++) {
						ArrayList<Integer> tempList1 = new ArrayList<Integer>();
						ArrayList<Integer> tempList2 = new ArrayList<Integer>();
						tempList1.add(redList.get(i * 5));
						tempList1.add(redList.get(i * 5 + 1));
						tempList1.add(redList.get(i * 5 + 2));
						tempList1.add(redList.get(i * 5 + 3));
						tempList1.add(redList.get(i * 5 + 4));
						tempList2.add(buleList.get(i * 2));
						tempList2.add(buleList.get(i * 2 + 1));
						setCartLayout(tempList1, tempList2);
					}
				} else {
					//从自增加一注点击返回，list为空判断
					if (redList.size() > 0 && buleList.size() > 0) {
						setCartLayout(redList, buleList);
					}
				}
				
			}
			break;
		case GlobalConstants.LOTTERY_DLT_DT:// 大乐透胆拖
			ArrayList<Integer> redDanList = data.getIntegerArrayListExtra(GlobalConstants.LIST_REDBALL);
			ArrayList<Integer> redTuoList = data.getIntegerArrayListExtra(GlobalConstants.LIST_REDBALL_DT);
			ArrayList<Integer> buleDanList = data.getIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL);
			ArrayList<Integer> buleTuoList = data.getIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL_DT);
			if (data.getBooleanExtra(GlobalConstants.CART_IS_ITEM_FORRESULT, false)) {
				bindDTCartLayout(redDanList, redTuoList, buleDanList, buleTuoList, mClickCartLayout);
			} else {
				//从自增加一注点击返回，list为空判断
				if (redDanList.size() > 0 && redTuoList.size() > 0 && buleTuoList.size() > 0) {
					setDTCartLayout(redDanList, redTuoList, buleDanList, buleTuoList);
				}
			}
			break;
		case GlobalConstants.LOTTERY_DLT_JX:
			
			for (int i = 0; i < jxNum; i++) {
				jxIntoCart();
			}
			break;
		default:
			break;
		}
		getTotalNumMoney();
	}

	private void setCartLayout(ArrayList<Integer> redList, ArrayList<Integer> buleList) {
		CartLayout cartHolder = initSetLayout(mCartLayout);
		
		initKeep();

		bindBZCartLayout(new ArrayList<Integer>(redList), new ArrayList<Integer>(buleList), cartHolder);
	}

	private void bindBZCartLayout(ArrayList<Integer> redList, ArrayList<Integer> buleList, CartLayout cartHolder) {
		Collections.sort(redList);
		Collections.sort(buleList);
		int itemNum = LotteryBettingUtil.Combination(5, redList.size()) * LotteryBettingUtil.Combination(2, buleList.size());
		int itemMoney = itemNum * 2;
		cartHolder.isZhuiAdd = mCartZjia.isSelected();
		DecimalFormat format = new DecimalFormat("00");
		initSetTag(cartHolder, itemNum, itemMoney);
		cartHolder.mRLCart.setTag(R.id.tag_ball_type, GlobalConstants.LOTTERY_DLT_PT);
		cartHolder.mRLCart.setTag(R.id.tag_dlt_red, redList);
		cartHolder.mRLCart.setTag(R.id.tag_dlt_bule, buleList);
		cartHolder.mTVCartType.setText(LotteryBettingUtil.cartDltType(redList, buleList));
		cartHolder.mFLCartNum.removeAllViews();
		if (redList.size() > 0) {
			for (Integer integer : redList) {
				TextView numTv = new TextView(mActivity);
				numTv.setTextSize(textSize);
				numTv.setText(format.format(integer + 1));
				numTv.setTextColor(redColor);
				cartHolder.mFLCartNum.addView(numTv);
			}
		}
		if (buleList.size() > 0) {
			for (Integer integer : buleList) {
				TextView numTv = new TextView(mActivity);
				numTv.setTextSize(textSize);
				numTv.setText(format.format(integer + 1));
				numTv.setTextColor(buleColor);
				cartHolder.mFLCartNum.addView(numTv);
			}
		}
	}

	private void setDTCartLayout(ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList, ArrayList<Integer> buleDanList, ArrayList<Integer> buleTuoList) {
		CartLayout cartHolder = initSetLayout(mCartLayout);
		initKeep();
		bindDTCartLayout(new ArrayList<Integer>(redDanList), new ArrayList<Integer>(redTuoList), new ArrayList<Integer>(buleDanList), new ArrayList<Integer>(buleTuoList), cartHolder);
	}

	private void bindDTCartLayout(ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList, ArrayList<Integer> buleDanList, ArrayList<Integer> buleTuoList, CartLayout cartHolder) {
		Collections.sort(redDanList);
		Collections.sort(redTuoList);
		Collections.sort(buleDanList);
		Collections.sort(buleTuoList);
		int itemNum = LotteryBettingUtil.getDanTuoNum(5, redDanList.size(), redTuoList.size()) * LotteryBettingUtil.getDanTuoNum(2, buleDanList.size(), buleTuoList.size());
		int itemMoney = itemNum * 2;
		cartHolder.isZhuiAdd = mCartZjia.isSelected();
		DecimalFormat format = new DecimalFormat("00");
		initSetTag(cartHolder, itemNum, itemMoney);
		cartHolder.mRLCart.setTag(R.id.tag_ball_type, GlobalConstants.LOTTERY_DLT_DT);
		cartHolder.mRLCart.setTag(R.id.tag_dlt_red, redDanList);
		cartHolder.mRLCart.setTag(R.id.tag_dlt_red_tuo, redTuoList);
		cartHolder.mRLCart.setTag(R.id.tag_dlt_bule, buleDanList);
		cartHolder.mRLCart.setTag(R.id.tag_dlt_bule_tuo, buleTuoList);
		cartHolder.mTVCartType.setText(getResources().getString(R.string.lottery_dlt_dt_tz));
		cartHolder.mFLCartNum.removeAllViews();
		if (redDanList.size() > 0) {
			TextView tv1 = new TextView(mActivity);
			tv1.setTextSize(18);
			tv1.setTextColor(redColor);
			tv1.setText("【胆");
			cartHolder.mFLCartNum.addView(tv1);
			for (Integer integer : redDanList) {
				TextView numTv = new TextView(mActivity);
				numTv.setTextSize(textSize);
				numTv.setText(format.format(integer + 1));
				numTv.setTextColor(redColor);
				cartHolder.mFLCartNum.addView(numTv);
			}
			TextView tv2 = new TextView(mActivity);
			tv2.setTextSize(textSize);
			tv2.setTextColor(redColor);
			tv2.setText("】");
			cartHolder.mFLCartNum.addView(tv2);
		}
		
		for (Integer integer : redTuoList) {
			TextView numTv = new TextView(mActivity);
			numTv.setTextSize(textSize);
			numTv.setText(format.format(integer + 1));
			numTv.setTextColor(redColor);
			cartHolder.mFLCartNum.addView(numTv);
		}
		
		if (buleDanList.size() > 0) {
			TextView bule1 = new TextView(mActivity);
			bule1.setTextSize(textSize);
			bule1.setTextColor(buleColor);
			bule1.setText("【胆");
			cartHolder.mFLCartNum.addView(bule1);
			for (Integer integer : buleDanList) {
				TextView numTv = new TextView(mActivity);
				numTv.setTextSize(textSize);
				numTv.setText(format.format(integer + 1));
				numTv.setTextColor(buleColor);
				cartHolder.mFLCartNum.addView(numTv);
			}
			TextView bule2 = new TextView(mActivity);
			bule2.setTextSize(textSize);
			bule2.setTextColor(buleColor);
			bule2.setText("】");
			cartHolder.mFLCartNum.addView(bule2);
		}
		
		for (Integer integer : buleTuoList) {
			TextView numTv = new TextView(mActivity);
			numTv.setTextSize(textSize);
			numTv.setText(format.format(integer + 1));
			numTv.setTextColor(buleColor);
			cartHolder.mFLCartNum.addView(numTv);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void itemCartClick(View v) {
		super.itemCartClick(v);
		Intent intent = new Intent(mActivity, LotteryDLT2Activity.class);
		
		Integer tag = (Integer) v.getTag(R.id.tag_ball_type);
		switch (tag) {
		case GlobalConstants.LOTTERY_DLT_PT:
			intent.putExtra(GlobalConstants.LOTTERY_TYPE, GlobalConstants.LOTTERY_DLT_PT);
			break;
		case GlobalConstants.LOTTERY_DLT_DT:
			ArrayList<Integer> redTuo = (ArrayList<Integer>) v.getTag(R.id.tag_dlt_red_tuo);
			ArrayList<Integer> buleTuo = (ArrayList<Integer>) v.getTag(R.id.tag_dlt_bule_tuo);
			intent.putExtra(GlobalConstants.LOTTERY_TYPE, GlobalConstants.LOTTERY_DLT_DT);
			intent.putIntegerArrayListExtra(GlobalConstants.LIST_REDBALL_DT, redTuo);
			intent.putIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL_DT, buleTuo);
			break;

		default:
			break;
		}
		ArrayList<Integer> redList = (ArrayList<Integer>) v.getTag(R.id.tag_dlt_red);
		ArrayList<Integer> blueList = (ArrayList<Integer>) v.getTag(R.id.tag_dlt_bule);
		intent.putExtra(GlobalConstants.CART_IS_FORRESULT, true);
		intent.putIntegerArrayListExtra(GlobalConstants.LIST_REDBALL, redList);
		intent.putIntegerArrayListExtra(GlobalConstants.LIST_BULEBALL, blueList);
		mActivity.startActivityForResult(intent, GlobalConstants.CART_ITEM_RESULT);
	}
 
	@Override
	public void jxIntoCart() {
		ArrayList<Integer> jxList1 = new ArrayList<Integer>();
		ArrayList<Integer> jxList2 = new ArrayList<Integer>();
		LotteryShowUtil.randomBall(jxList1, 5, 35);
		LotteryShowUtil.randomBall(jxList2, 2, 12);
		setCartLayout(jxList1, jxList2);
	}

	@Override
	public void zxIntoCart() {
		zxIntent(LotteryDLT2Activity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void goBuy(LinearLayout itemLayout, CodeInfo codeInfo) {
//		{"gameType":"113","isStop":0,"buyType":0,"buyNumberArray":[{"buyNumber":"02,15,17,25,29#05,08","typeId":"00","seleId":"01","sum":2,"multiple":1,"status":0,"num":1,"lastMatch":0,"hbcls":0},{"buyNumber":"01,08,12,14,21#03,09","typeId":"00","seleId":"01","sum":2,"multiple":1,"status":0,"num":1,"lastMatch":0,"hbcls":0}],"issueArray":[{"issue":"15018","multiple":1}],"title":"大乐透15018期追号方案","explain":"0","secrecy":"1","splitAmount":"0","isUpload":"0","projectid":"0","buyAmount":0,"floorsAmount":"0","totalSum":"4.0","qcdy":"0","unoid":"0","commision":"0","commisiontype":"0"}
//		codeInfo.mTypeId = "00";
//		mSeleId = 01单式，02复式，03胆拖
		codeInfo.mTypeId  = mCartZjia.isSelected() ? "01" : "00";
		int zhuMoney = 2;
		if (mCartZjia.isSelected()) {
			zhuMoney = 3;
		} else {
			zhuMoney = 2;
		}
		int ballType = (Integer)itemLayout.getTag(R.id.tag_ball_type);
		ArrayList<Integer> redList = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_dlt_red);
		ArrayList<Integer> buleList = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_dlt_bule);
		switch (ballType) {
		case GlobalConstants.LOTTERY_DLT_PT:
			Collections.sort(redList);
			Collections.sort(buleList);
			codeInfo.mNum = LotteryBettingUtil.Combination(5, redList.size()) * LotteryBettingUtil.Combination(2, buleList.size());
			codeInfo.mPrice = codeInfo.mNum * zhuMoney;
			codeInfo.mCode = LotteryBettingUtil.tzDlt(redList, buleList);
			codeInfo.mSeleId = LotteryBettingUtil.getSeleID_Dlt(redList, buleList);
			break;
		case GlobalConstants.LOTTERY_DLT_DT:
			ArrayList<Integer> redTuoList = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_dlt_red_tuo);
			ArrayList<Integer> buleTuoList = (ArrayList<Integer>) itemLayout.getTag(R.id.tag_dlt_bule_tuo);
			codeInfo.mNum = LotteryBettingUtil.getDanTuoNum(5, redList.size(), redTuoList.size()) * LotteryBettingUtil.getDanTuoNum(2, buleList.size(), buleTuoList.size());
			codeInfo.mPrice = codeInfo.mNum * zhuMoney;
			codeInfo.mCode = LotteryBettingUtil.tzDlt(redList, redTuoList, buleList, buleTuoList);
			codeInfo.mSeleId = "03";
			break;
		default:
			break;
		}
	}

	@Override
	public String getGameType() {
		return GlobalConstants.TC_DLT;
	}

	@Override
	public String getTitle(String term, int buyType) {
		return getResources().getString(R.string.lottery_dlttz_buytype, term, getBuyTypeTitle(buyType));
	}

	@Override
	public int getLotteryName() {
		return GlobalConstants.LOTTERY_DLT;
	}

}
