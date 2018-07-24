package cn.com.cimgroup.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 胜平负、进球数、比分、半全场、让球胜平负 注数、奖金计算
 * @Description:
 * @author:zhangjf 
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2015-2-10
 */
public class FootballLotteryUtil {

	/** 胆 内容为比赛列表的索引 **/
//	private Map<Integer, Integer> mDan = new HashMap<Integer, Integer>();
	private List<Integer> mDan = new ArrayList<Integer>();
	/** 选择的号码 key 比赛列表的索引 values 投注的索引 **/
	private Map<Integer, List<Integer>> mSelectNumber = new HashMap<Integer, List<Integer>>();
	/** 选择的串 **/
	private List<String> mSelectChuan2Num = new ArrayList<String>();
	/** 足彩索引-》比赛项目信息 **/
	private List<List<Match>> mMatchInfo = new ArrayList<List<Match>>();
	/** 总金额 **/
	private int mSumPrice = 0;
	/** 倍数 **/
	private int mMultiple = 1;
	
	private int itemTotal = 0;
	
	private MatchSearchType mSearchType;
	
	private int mLotoId;

	private int minDanLen = 0;
	private int maxDanLen = 0;

	/**
	 * 设置胆
	 * 
	 * @Description:
	 * @param dan
	 *            为赛场的索引
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	@SuppressLint("UseValueOf")
	public void addDans(List<Integer> dans) {
		if (dans != null) {
			mDan.clear();
			mDan = new ArrayList<Integer>(dans);
		} else {
			mDan.clear();
		}
	}
	
	public List<Integer> getDans(){
		return mDan;
	}

	/**
	 * 设置最小胆
	 * 
	 * @Description:
	 * @param minDan
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-27
	 */
	public void setMinDan(int minDan) {
		this.minDanLen = minDan;
	}

	/**
	 * 设置最大的胆
	 * 
	 * @Description:
	 * @param maxDan
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-27
	 */
	public void setMaxDan(int maxDan) {
		this.maxDanLen = maxDan;
	}

	/**
	 * 设置串
	 * 
	 * @Description:
	 * @param chuan
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public void addChuans(List<String> chuans) {
		if (chuans != null) {
			mSelectChuan2Num.clear();
			mSelectChuan2Num.addAll(chuans);
		} else {
			mSelectChuan2Num.clear();
		}
	}


	/**
	 * 得到串
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public List<String> getSelectChuanState() {
		return mSelectChuan2Num;
	}

	public void setSearchType(MatchSearchType searchType) {
		// TODO Auto-generated method stub
		mSearchType = searchType;
	};
	
	public void setLotId(int lotoId) {
		// TODO Auto-generated method stub
		mLotoId = lotoId;
	}
	
	public int getLotId() {
		// TODO Auto-generated method stub
		return mLotoId;
	}
	
	/**
	 * 得到投注倍数
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public int getMultiple() {
		return mMultiple;
	}
	
	/**
	 * 获取注数
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月15日
	 */
	public String getLotteryItemTotal(){
		return itemTotal + "";
	}

	/**
	 * 获取所有比赛信息
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-27
	 */
	public List<List<Match>> getFootballMatchs() {
		return mMatchInfo;
	}

	public void setFootballMatchs(List<List<Match>> footballMatch) {
		mMatchInfo.clear();
		if (footballMatch != null) {
			mMatchInfo.addAll(footballMatch);
		}
	}

	/**
	 * 设置倍数
	 * 
	 * @Description:
	 * @param mMultiple
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public void setMultiple(int multiple) {
		this.mMultiple = multiple;
	}

	/**
	 * 得到选中的比赛列表结果集
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public Map<Integer, List<Integer>> getSelectNumber() {
		return mSelectNumber;
	}

	public void setSelectNumber(Map<Integer, List<Integer>> selectNumber) {
		this.mSelectNumber = selectNumber;
	}

	
	/**
	 * 需要花费的金额
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-2-4
	 */
	public int getSumPrice(){
		return mSumPrice;
	}
	

	public Object[] startSingleCalculate() {
		itemTotal = 0;
		BigDecimal winPrice = null;
		try {
			// 计算注数
			if (mSelectNumber != null) {
				Set<Integer> set = mSelectNumber.keySet();
				Iterator<Integer> it = set.iterator();  
				while (it.hasNext()) {  
					int str = it.next();
					itemTotal += mSelectNumber.get(str).size();
				}
			}
			// 钱数
			mSumPrice = itemTotal * mMultiple * 2;
			// 预计奖金
			winPrice = getSingleWinTotal();
			winPrice = new BigDecimal((winPrice.doubleValue() *getMultiple())).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Object[]{itemTotal,mSumPrice,winPrice.toString()};
	}
	
	@SuppressWarnings("null")
	private BigDecimal getSingleWinTotal() {
		Double total = 0d;
		Set<Integer> key = mSelectNumber.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			Integer s = (Integer) it.next();
			int[] positionArr = FootballLotteryTools.getMatchPositioin(s, mMatchInfo);
			Match match = mMatchInfo.get(positionArr[0]).get(positionArr[1]);
			String spfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZSPF, mSearchType);
			String rspfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZXSPF, mSearchType);
			String bfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBF, mSearchType);
			String jqsSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
			String bqcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
			
			String[] bfArr = bfSp.split("\\@");
			String[] jqsArr = jqsSp.split("\\@");
			String[] bqcArr = bqcSp.split("\\@");
			
			String[] arrs = new String[100];
			List<Integer> list = new ArrayList<Integer>();
			switch (mLotoId) {
			case 302:
				list = mSelectNumber.get(s);
				for (int i = 0; i < list.size(); i++) {
					arrs[i] = bfArr[list.get(i)].split("\\_")[1] + "";
				}
				break;
			case 303:
				list = mSelectNumber.get(s);
				for (int i = 0; i < list.size(); i++) {
					arrs[i] = jqsArr[list.get(i)].split("\\_")[1] + "";
				}
				break;
			case 304:
				list = mSelectNumber.get(s);
				for (int i = 0; i < list.size(); i++) {
					arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
				}
				break;
//			case 320:
//				//胜平负
////				spfSp
//				break;
//			case 305:
//				//混合过关
////				rspfSp
//				break;
			default:
				String[] spArr = match.getAvgOdds().split("\\_");
				for (int i = 0; i < spArr.length; i++) {
					arrs[i] = spArr[i] + "";
				}
				break;
			}
			for (String sp : arrs) {
				if (!StringUtil.isEmpty(sp)) {
//					total += Double.parseDouble(sp) * 2;
					if (Double.parseDouble(sp) > total) {
						total = Double.parseDouble(sp);
					}
				}
				
			}
		}
		
		return new BigDecimal(total * 2);
	}



	/**
	 * 开始计算
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-27
	 */
	public Object[] startCalculate() {
		
		BigDecimal winPrice = null;
		try {
			// 计算注数
			itemTotal = getItemTotal();
			// 钱数
			mSumPrice = itemTotal * mMultiple * 2;
			// 预计奖金
			if (mSearchType != null) {
				winPrice = getWinTotal();
				winPrice = new BigDecimal((winPrice.doubleValue() *getMultiple()* 2)).setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				winPrice = new BigDecimal(0);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Object[]{itemTotal,mSumPrice,winPrice.toString()};
	}

	/**
	 * li.计算注数 li.购买所花得钱 li.预计获奖的金额
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	private int getItemTotal() {
		// 不带胆的选择投注选项的个数
		List<Double> selectNumberCount = new ArrayList<Double>();
		// 带胆的选择投注选项的个数
		List<Double> selectNumberDansCount = new ArrayList<Double>();

		Set<Integer> key = mSelectNumber.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			Integer s = (Integer) it.next();
			if (mDan.contains(s)) {
				selectNumberDansCount.add((double) mSelectNumber.get(s).size());
			} else {
				selectNumberCount.add((double) mSelectNumber.get(s).size());
			}
		}
		int total = 0;
		for (int i = 0; i < mSelectChuan2Num.size(); i++) {
			String c = (mSelectChuan2Num.get(i).equals("单关") ? "1串1" : mSelectChuan2Num.get(i));
//			 z("chuan="+chuan[i].replace(/串/g,"_")+"||nbs="+nbs+"||dans="+
//			 dans+"||minDanLen="+minDanLen+"||maxDanLen="+maxDanLen)
			total += myCalc(c, selectNumberCount, selectNumberDansCount);
		}

		return total;

	}

	// 获得最大/最小奖金
	@SuppressWarnings("null")
	private BigDecimal getWinTotal() {
		// 得到每场最大的赔率选项
		List<Double> newNb = new ArrayList<Double>();
		Set<Integer> key = mSelectNumber.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			List<Double> arr = new ArrayList<Double>();
			Integer s = (Integer) it.next();
			int[] positionArr = FootballLotteryTools.getMatchPositioin(s, mMatchInfo);
			Match match = mMatchInfo.get(positionArr[0]).get(positionArr[1]);
			String[] bfArr = null;
			String[] jqsArr = null;
			String[] bqcArr = null;
			if (mSearchType != null) {
				String spfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZSPF, mSearchType);
				String rspfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZXSPF, mSearchType);
				String bfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBF, mSearchType);
				String jqsSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
				String bqcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
				
				bfArr = bfSp.split("\\@");
				jqsArr = jqsSp.split("\\@");
				bqcArr = bqcSp.split("\\@");
			}
			
			String[] arrs = new String[100];
			switch (mLotoId) {
			case 302:
				for (int i = 0; i < bfArr.length; i++) {
					arrs[i] = bfArr[i].split("\\_")[1] + "";
				}
				break;
			case 303:
				for (int i = 0; i < jqsArr.length; i++) {
					arrs[i] = jqsArr[i].split("\\_")[1] + "";
				}
				break;
			case 304:
				for (int i = 0; i < bqcArr.length; i++) {
					arrs[i] = bqcArr[i].split("\\_")[1] + "";
				}
				break;
			default:
				String[] spArr = match.getAvgOdds().split("\\_");
				for (int i = 0; i < spArr.length; i++) {
					arrs[i] = spArr[i] + "";
				}
				break;
			}
			for (String sp : arrs) {
				if (!StringUtil.isEmpty(sp)) {
					arr.add(Double.parseDouble(sp));
				}
				
			}
			Double max = 0.0d;
			for (int n = 0; n < mSelectNumber.get(s).size(); n++) {
				if (max < arr.get(mSelectNumber.get(s).get(n))) {
					max = arr.get(mSelectNumber.get(s).get(n)); // 获得最大值
				}
			}
			newNb.add(max);
		}
		// 将带胆的、不带胆的，赔率分别提取出来

		List<Double> dans = new ArrayList<Double>();
		List<Double> nbs = new ArrayList<Double>();

		for (int i = 0; i < newNb.size(); i++) {
			if (mDan.size() - 1 >= i) {
				if (mDan.get(i) != null) {
					dans.add(newNb.get(i));
				} else {
					nbs.add(newNb.get(i));
				}
			} else {
				nbs.add(newNb.get(i));
			}
		}
		Double total = 0d;
		for (int i = 0; i < mSelectChuan2Num.size(); i++) {
			String c = (mSelectChuan2Num.get(i).equals("单关") ? "1串1" : mSelectChuan2Num.get(i));
			// z("chuan="+chuan[i].replace(/串/g,"_")+"||nbs="+nbs+"||dans="+
			// dans+"||minDanLen="+minDanLen+"||maxDanLen="+maxDanLen)
			total += myCalc(c, nbs, dans);
		}
		// z(total)
		return new BigDecimal(total);
	}

	private Double myCalc(String chuan, List<Double> selectNumberCount, List<Double> selectNumberDansCount) {
		if (selectNumberDansCount.size() == 0) {
			return calc(chuan, selectNumberCount, selectNumberDansCount);
		} else {
			Double wcount = 0d;
//			for (int k = 2; k <= 2; k++) {
//				Map<Integer, LinkedList<Double>> bm = combinArray(selectNumberCount, k);
//				Set<Integer> keys = bm.keySet();
//				for (Iterator it = keys.iterator(); it.hasNext();) {
//					Integer key = (Integer) it.next();
					wcount += calc(chuan, selectNumberCount, selectNumberDansCount);
//				}
//			}
			return wcount;
		}
	}

	private Double calc(String chuan, List<Double> selectNumberCount, List<Double> selectNumberDansCount) {
		Double WagerCount = 0d;
		String[] t_list = chuan.split(App.getInstance().getResources().getString(R.string.football_chuan));
		int pc = Integer.parseInt(t_list[0]);
		int AbsCount = mDan.size();
		int len = pc - AbsCount;
		if (len == 0 && AbsCount > 0) {
			Double[] pm = new Double[AbsCount];
			for (int p = 0; p < selectNumberDansCount.size(); p++) {
				Double AbsVoteC = selectNumberDansCount.get(p);
				for (int k = 0; k < pc; k++) {
					if (pm[k] == 0 || pm[k] == null) {
						pm[k] = AbsVoteC;
						break;
					}
				}
			}
			WagerCount += calcuteWC(chuan, pm);
		} else {
			List<Double> arr = new ArrayList<Double>();
			for (int i = 0; i < selectNumberCount.size(); i++) {
				arr.add((double) i);
			}
			Map<Integer, LinkedList<Double>> w = combinArray(arr, len);
			Set<Integer> keys = w.keySet();
			for (Iterator it = keys.iterator(); it.hasNext();) {
				Integer key = (Integer) it.next();
				LinkedList<Double> splitArr = w.get(key);
				Double[] pm = new Double[pc];
				for (int k = 0; k < pc; k++) {
					if (k <= splitArr.size() - 1) {
						pm[k] = selectNumberCount.get(splitArr.get(k).intValue());
					} else {
						pm[k] = (double) 0;
					}
				}
				if (AbsCount > 0) {
					for (int P = 0; P < selectNumberDansCount.size(); P++) {
						Double AbsVoteC = selectNumberDansCount.get(P);
						for (int k = 0; k < pc; k++) {
							if (pm[k] == 0 || pm[k] == null) {
								pm[k] = AbsVoteC;
								break;
							}
						}
					}
				}
				WagerCount += calcuteWC(chuan, pm);
			}
		}
		return WagerCount;
	}

	/**
	 * 获得注数
	 * 
	 * @Description:
	 * @param arr
	 * @param len
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public Map<Integer, LinkedList<Double>> combinArray(List<Double> arr, int len) {
		Map<Integer, LinkedList<Double>> Re = new HashMap<Integer, LinkedList<Double>>();
		// arr.sort();
		if (arr.size() < len || len < 1) {
			return Re;
		} else if (arr.size() == len) {
			// Re[0] = arr;
			Re.put(0, new LinkedList<Double>());
			Re.get(0).addAll(arr);
			return Re;
		}
		if (len == 1) {
			for (int i = 0; i < arr.size(); i++) {
				if (Re.get(i) == null) {
					Re.put(i, new LinkedList<Double>());
				}
				Re.get(i).add(arr.get(i));
			}
			return Re;
		}
		if (len > 1) {
			for (int i = 0; i < arr.size(); i++) {

				List<Double> arr_b = new ArrayList<Double>();

				for (int j = 0; j < arr.size(); j++) {
					if (j > i) {
						arr_b.add(arr.get(j));
					}
				}
				Map<Integer, LinkedList<Double>> s = combinArray(arr_b, len - 1);
				if (s.size() > 0) {
					Set<Integer> keys = s.keySet();
					for (Iterator it = keys.iterator(); it.hasNext();) {
						Integer key = (Integer) it.next();
						LinkedList<Double> p = s.get(key);
						p.addFirst(arr.get(i));
						Re.put(new Integer(Re.size()), p);
					}
				}
			}
		}
		return Re;
	}

	private Double calcuteWC(String chuan, Double[] index) {
		Object[] key = index;
		Double re = 0d;
		Double a = key.length >= 1 ? Double.parseDouble(key[0] + "") : 0.0f;
		Double b = key.length >= 2 ? Double.parseDouble(key[1] + "") : 0.0f;
		Double c = key.length >= 3 ? Double.parseDouble(key[2] + "") : 0.0f;
		Double d = key.length >= 4 ? Double.parseDouble(key[3] + "") : 0.0f;
		Double e = key.length >= 5 ? Double.parseDouble(key[4] + "") : 0.0f;
		Double f = key.length >= 6 ? Double.parseDouble(key[5] + "") : 0.0f;
		Double g = key.length >= 7 ? Double.parseDouble(key[6] + "") : 0.0f;
		Double h = key.length >= 8 ? Double.parseDouble(key[7] + "") : 0.0f;
		Double i = key.length >= 9 ? Double.parseDouble(key[8] + "") : 0.0f;
		Double j = key.length >= 10 ? Double.parseDouble(key[9] + "") : 0.0f;
		Double k = key.length >= 11 ? Double.parseDouble(key[10] + "") : 0.0f;
		Double l = key.length >= 12 ? Double.parseDouble(key[11] + "") : 0.0f;
		Double m = key.length >= 13 ? Double.parseDouble(key[12] + "") : 0.0f;
		Double n = key.length >= 14 ? Double.parseDouble(key[13] + "") : 0.0f;
		Double o = key.length >= 15 ? Double.parseDouble(key[14] + "") : 0.0f;
		if (chuan.equals("1" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a;

		} else if (chuan.equals("2" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b;

		} else if (chuan.equals("2" + App.getInstance().getResources().getString(R.string.football_chuan) + "3")) {
			re = (a + 1) * (b + 1) - 1;

		} else if (chuan.equals("3" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c;

		} else if (chuan.equals("3" + App.getInstance().getResources().getString(R.string.football_chuan) + "3")) {
			re = a * b + a * c + b * c;

		} else if (chuan.equals("3" + App.getInstance().getResources().getString(R.string.football_chuan) + "4")) {
			re = a * b * c + a * b + a * c + b * c;

		} else if (chuan.equals("3" + App.getInstance().getResources().getString(R.string.football_chuan) + "7")) {
			re = (a + 1) * (b + 1) * (c + 1) - 1;

		} else if (chuan.equals("4" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d;

		} else if (chuan.equals("4" + App.getInstance().getResources().getString(R.string.football_chuan) + "4")) {
			re = a * b * c + a * b * d + a * c * d + b * c * d;

		} else if (chuan.equals("4" + App.getInstance().getResources().getString(R.string.football_chuan) + "5")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) - (a * (b + c + d + 1) + b * (c + d + 1) + (c + 1) * (d + 1));

		} else if (chuan.equals("4" + App.getInstance().getResources().getString(R.string.football_chuan) + "6")) {
			re = a * b + a * c + a * d + b * c + b * d + c * d;

		} else if (chuan.equals("4" + App.getInstance().getResources().getString(R.string.football_chuan) + "11")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) - (a + b + c + d + 1);

		} else if (chuan.equals("4" + App.getInstance().getResources().getString(R.string.football_chuan) + "15")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) - 1;

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e;

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "5")) {
			re = a * b * c * d + a * b * c * e + a * b * d * e + a * c * d * e + b * c * d * e;

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "6")) {
			re = a * b * c * d * e + a * b * c * d + a * b * c * e + a * b * d * e + a * c * d * e + b * c * d * e;

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "10")) {
			re = a * b + a * c + a * d + a * e + b * c + b * d + b * e + c * d + c * e + d * e;

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "16")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) - (a * (b + c + d + e + 1) + b * (c + d + e + 1) + c * (d + e + 1) + (d + 1) * (e + 1));

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "20")) {
			re = a * b * c + a * b * d + a * b * e + a * c * d + a * c * e + a * d * e + b * c * d + b * c * e + b * d * e + c * d * e + a * b + a * c + a * d + a * e + b * c + b * d + b * e + c * d + c * e + d * e;

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "26")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) - (a + b + c + d + e + 1);

		} else if (chuan.equals("5" + App.getInstance().getResources().getString(R.string.football_chuan) + "31")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) - 1;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "6")) {
			re = a * b * c * d * e + a * b * c * d * f + a * b * c * e * f + a * b * d * e * f + a * c * d * e * f + b * c * d * e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "7")) {
			re = a * b * c * d * e * f + a * b * c * d * e + a * b * c * d * f + a * b * c * e * f + a * b * d * e * f + a * c * d * e * f + b * c * d * e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "15")) {
			re = a * b + a * c + a * d + a * e + a * f + b * c + b * d + b * e + b * f + c * d + c * e + c * f + d * e + d * f + e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "20")) {
			re = a * b * c + a * b * d + a * b * e + a * b * f + a * c * d + a * c * e + a * c * f + a * d * e + a * d * f + a * e * f + b * c * d + b * c * e + b * c * f + b * d * e + b * d * f + b * e * f + c * d * e + c * d * f + c * e * f + d * e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "22")) {
			re = a * b * c * d * e * f + a * b * c * d * e + a * b * c * d * f + a * b * c * e * f + a * b * d * e * f + a * c * d * e * f + b * c * d * e * f + a * b * c * d + a * b * c * e + a * b * c * f + a * b * d * e + a * b * d * f + a * b * e * f + a * c * d * e + a * c * d * f + a * c * e * f + a * d * e * f + b * c * d * e + b * c * d * f + b * c * e * f + b * d * e * f + c * d * e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "35")) {
			re = a * b * c + a * b * d + a * b * e + a * b * f + a * c * d + a * c * e + a * c * f + a * d * e + a * d * f + a * e * f + b * c * d + b * c * e + b * c * f + b * d * e + b * d * f + b * e * f + c * d * e + c * d * f + c * e * f + d * e * f + a * b + a * c + a * d + a * e + a * f + b * c + b * d + b * e + b * f + c * d + c * e + c * f + d * e + d * f + e * f;

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "42")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) * (f + 1) - (a * (b + c + d + e + f + 1) + b * (c + d + e + f + 1) + c * (d + e + f + 1) + d * (e + f + 1) + (e + 1) * (f + 1));

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "50")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) * (f + 1) - (a + b + c + d + e + f + 1) - (a * b * c * d * e + a * b * c * d * f + a * b * c * e * f + a * b * d * e * f + a * c * d * e * f + b * c * d * e * f + a * b * c * d * e * f);

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "57")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) * (f + 1) - (a + b + c + d + e + f + 1);

		} else if (chuan.equals("6" + App.getInstance().getResources().getString(R.string.football_chuan) + "63")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) * (f + 1) - 1;

		} else if (chuan.equals("7" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g;

		} else if (chuan.equals("7" + App.getInstance().getResources().getString(R.string.football_chuan) + "7")) {
			re = a * b * c * d * e * f + a * b * c * d * e * g + a * b * c * d * f * g + a * b * c * e * f * g + a * b * d * e * f * g + a * c * d * e * f * g + b * c * d * e * f * g;

		} else if (chuan.equals("7" + App.getInstance().getResources().getString(R.string.football_chuan) + "8")) {
			re = a * b * c * d * e * f * g + a * b * c * d * e * f + a * b * c * d * e * g + a * b * c * d * f * g + a * b * c * e * f * g + a * b * d * e * f * g + a * c * d * e * f * g + b * c * d * e * f * g;

		} else if (chuan.equals("7" + App.getInstance().getResources().getString(R.string.football_chuan) + "21")) {
			re = a * b * c * d * e + a * b * c * d * f + a * b * c * d * g + a * b * c * e * f + a * b * c * e * g + a * b * c * f * g + a * b * d * e * f + a * b * d * e * g + a * b * d * f * g + a * b * e * f * g + a * c * d * e * f + a * c * d * e * g + a * c * d * f * g + a * c * e * f * g + a * d * e * f * g + b * c * d * e * f + b * c * d * e * g + b * c * d * f * g + b * c * e * f * g + b * d * e * f * g + c * d * e * f * g;

		} else if (chuan.equals("7" + App.getInstance().getResources().getString(R.string.football_chuan) + "35")) {
			re = a * b * c * d + a * b * c * e + a * b * c * f + a * b * c * g + a * b * d * e + a * b * d * f + a * b * d * g + a * b * e * f + a * b * e * g + a * b * f * g + a * c * d * e + a * c * d * f + a * c * d * g + a * c * e * f + a * c * e * g + a * c * f * g + a * d * e * f + a * d * e * g + a * d * f * g + a * e * f * g + b * c * d * e + b * c * d * f + b * c * d * g + b * c * e * f + b * c * e * g + b * c * f * g + b * d * e * f + b * d * e * g + b * d * f * g + b * e * f * g + c * d * e * f + c * d * e * g + c * d * f * g + c * e * f * g + d * e * f * g;

		} else if (chuan.equals("7" + App.getInstance().getResources().getString(R.string.football_chuan) + "120")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) * (f + 1) * (g + 1) - (a + b + c + d + e + f + g + 1);

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h;

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "8")) {
			re = a * b * c * d * e * f * g + a * b * c * d * e * f * h + a * b * c * d * e * g * h + a * b * c * d * f * g * h + a * b * c * e * f * g * h + a * b * d * e * f * g * h + a * c * d * e * f * g * h + b * c * d * e * f * g * h;

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "9")) {
			re = a * b * c * d * e * f * g * h + a * b * c * d * e * f * g + a * b * c * d * e * f * h + a * b * c * d * e * g * h + a * b * c * d * f * g * h + a * b * c * e * f * g * h + a * b * d * e * f * g * h + a * c * d * e * f * g * h + b * c * d * e * f * g * h;

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "28")) {
			re = a * b * c * d * e * f + a * b * c * d * e * g + a * b * c * d * e * h + a * b * c * d * f * g + a * b * c * d * f * h + a * b * c * d * g * h + a * b * c * e * f * g + a * b * c * e * f * h + a * b * c * e * g * h + a * b * c * f * g * h + a * b * d * e * f * g + a * b * d * e * f * h + a * b * d * e * g * h + a * b * d * f * g * h + a * b * e * f * g * h + a * c * d * e * f * g + a * c * d * e * f * h + a * c * d * e * g * h + a * c * d * f * g * h + a * c * e * f * g * h + a * d * e * f * g * h + b * c * d * e * f * g + b * c * d * e * f * h + b * c * d * e * g * h + b * c * d * f * g * h + b * c * e * f * g * h + b * d * e * f * g * h + c * d * e * f * g * h;

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "56")) {
			re = a * b * c * d * e + a * b * c * d * f + a * b * c * d * g + a * b * c * d * h + a * b * c * e * f + a * b * c * e * g + a * b * c * e * h + a * b * c * f * g + a * b * c * f * h + a * b * c * g * h + a * b * d * e * f + a * b * d * e * g + a * b * d * e * h + a * b * d * f * g + a * b * d * f * h + a * b * d * g * h + a * b * e * f * g + a * b * e * f * h + a * b * e * g * h + a * b * f * g * h + a * c * d * e * f + a * c * d * e * g + a * c * d * e * h + a * c * d * f * g + a * c * d * f * h + a * c * d * g * h + a * c * e * f * g + a * c * e * f * h + a * c * e * g * h + a * c * f * g * h + a * d * e * f * g + a * d * e * f * h + a * d * e * g * h + a * d * f * g * h + a * e * f * g * h + b * c * d * e * f + b * c * d * e * g + b * c * d * e * h + b * c * d * f * g + b * c * d * f * h + b * c * d * g * h + b * c * e * f * g + b * c * e * f * h + b * c * e * g * h + b * c * f * g * h + b * d * e * f * g + b * d * e * f * h + b * d * e * g * h + b * d * f * g * h + b * e * f * g * h + c * d * e * f * g + c * d * e * f * h + c * d * e * g * h + c * d * f * g * h + c * e * f * g * h + d * e * f * g * h;

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "70")) {
			re = a * b * c * d + a * b * c * e + a * b * c * f + a * b * c * g + a * b * c * h + a * b * d * e + a * b * d * f + a * b * d * g + a * b * d * h + a * b * e * f + a * b * e * g + a * b * e * h + a * b * f * g + a * b * f * h + a * b * g * h + a * c * d * e + a * c * d * f + a * c * d * g + a * c * d * h + a * c * e * f + a * c * e * g + a * c * e * h + a * c * f * g + a * c * f * h + a * c * g * h + a * d * e * f + a * d * e * g + a * d * e * h + a * d * f * g + a * d * f * h + a * d * g * h + a * e * f * g + a * e * f * h + a * e * g * h + a * f * g * h + b * c * d * e + b * c * d * f + b * c * d * g + b * c * d * h + b * c * e * f + b * c * e * g + b * c * e * h + b * c * f * g + b * c * f * h + b * c * g * h + b * d * e * f + b * d * e * g + b * d * e * h + b * d * f * g + b * d * f * h + b * d * g * h + b * e * f * g + b * e * f * h + b * e * g * h + b * f * g * h + c * d * e * f + c * d * e * g + c * d * e * h + c * d * f * g + c * d * f * h + c * d * g * h + c * e * f * g + c * e * f * h + c * e * g * h + c * f * g * h + d * e * f * g + d * e * f * h + d * e * g * h + d * f * g * h + e * f * g * h;

		} else if (chuan.equals("8" + App.getInstance().getResources().getString(R.string.football_chuan) + "247")) {
			re = (a + 1) * (b + 1) * (c + 1) * (d + 1) * (e + 1) * (f + 1) * (g + 1) * (h + 1) - (a + b + c + d + e + f + g + h + 1);

		} else if (chuan.equals("9" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i;

		} else if (chuan.equals("10" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i * j;

		} else if (chuan.equals("11" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i * j * k;

		} else if (chuan.equals("12" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i * j * k * l;

		} else if (chuan.equals("13" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i * j * k * l * m;

		} else if (chuan.equals("14" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i * j * k * l * m * n;
		} else if (chuan.equals("15" + App.getInstance().getResources().getString(R.string.football_chuan) + "1")) {
			re = a * b * c * d * e * f * g * h * i * j * k * l * m * n * o;
		}
		return re;
	}

}
