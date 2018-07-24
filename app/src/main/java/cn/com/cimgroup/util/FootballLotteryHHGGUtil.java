package cn.com.cimgroup.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.xutils.Tick;

/**
 * 足彩混合投注util
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-2-9
 */
public class FootballLotteryHHGGUtil {

	/** 选择的串 **/
	private List<String> mSelectChuan2Num = new ArrayList<String>();
	/** 胆 内容为比赛列表的索引 **/
//	private Map<Integer, Integer> mDan = new HashMap<Integer, Integer>();
	private List<Integer> mDan = new ArrayList<Integer>();
	
	/** sp集合用于计算注数及奖金 **/
	private List<List<Tuo>> mTuos = new ArrayList<List<Tuo>>();

	/** 足彩索引-》比赛项目信息 **/
	private List<List<Match>> mMatchInfo = new ArrayList<List<Match>>();
	// 存储混合投注的选择投注选项(混合过关)
	Map<Integer, Map<Integer, List<Integer>>> mSelectHhggItemindexs = new LinkedHashMap<Integer, Map<Integer, List<Integer>>>();
	/** 倍数 **/
	private int mMultiple = 1;

	private int itemTotal = 0;// 注数
	private Double maxPrice = 0d;// 最大奖金
	/** 总金额 **/
	private int mSumPrice = 0;
	
	private int mLotoId;
	
	private MatchSearchType mSearchType;

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
	
	public void setLotId(int lotoId) {
		// TODO Auto-generated method stub
		mLotoId = lotoId;
	}
	
	public int getLotId() {
		// TODO Auto-generated method stub
		return mLotoId;
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
	 * 得到选中的比赛列表结果集
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-14
	 */
	public Map<Integer, Map<Integer, List<Integer>>> getSelectNumber() {
		return mSelectHhggItemindexs;
	}

	public void setSelectNumber(Map<Integer, Map<Integer, List<Integer>>> selectNumber) {
		this.mSelectHhggItemindexs = selectNumber;
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
	
	public void setSearchType(MatchSearchType searchType) {
		// TODO Auto-generated method stub
		mSearchType = searchType;
	};
	
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
	
	public Object[] startSingleCalculate() {
		
		Double winPrice = 0d;
		itemTotal = 0;// 注数
		try {
			// 计算注数
			List<Integer> key0 = new ArrayList<Integer>(mSelectHhggItemindexs.keySet());
			for (int i = 0; i < key0.size(); i++) {
				if (mSelectHhggItemindexs.get(key0.get(i)) != null) {
					Set<Integer> set = mSelectHhggItemindexs.get(key0.get(i)).keySet();
					Iterator<Integer> it = set.iterator();  
					while (it.hasNext()) {  
						int str = it.next();
						itemTotal += mSelectHhggItemindexs.get(key0.get(i)).get(str).size();
					}
				}
			}
			// 钱数
			mSumPrice = itemTotal * mMultiple * 2;
			// 预计奖金
			winPrice = new BigDecimal((getSingleWinTotal().doubleValue() *getMultiple())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Object[]{itemTotal,mSumPrice,winPrice};
	}
	
	@SuppressWarnings("null")
	private BigDecimal getSingleWinTotal() {
		Double total = 0d;
		List<Integer> key0 = new ArrayList<Integer>(mSelectHhggItemindexs.keySet());

		for (int i = 0; i < key0.size(); i++) {
			if (mSelectHhggItemindexs.get(key0.get(i)) != null) {
				Set<Integer> key = mSelectHhggItemindexs.get(key0.get(i)).keySet();
				for (Iterator it = key.iterator(); it.hasNext();) {
					Integer s = (Integer) it.next();
					int[] positionArr = FootballLotteryTools.getMatchPositioin(key0.get(i), mMatchInfo);
					Match match = mMatchInfo.get(positionArr[0]).get(positionArr[1]);
					String spfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZSPF, mSearchType);
					String rspfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZXSPF, mSearchType);
					String bfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBF, mSearchType);
					String jqsSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
					String bqcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
					
					String sfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLSF, mSearchType);
					String rsfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLXSF, mSearchType);
					String sfcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLSFC, mSearchType);
					String dxSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLDXF, mSearchType);
					
					
					String[] spfArr = spfSp.split("\\@");
					String[] rspfArr = rspfSp.split("\\@");
					String[] bfArr = bfSp.split("\\@");
					String[] jqsArr = jqsSp.split("\\@");
					String[] bqcArr = bqcSp.split("\\@");
					
					String[] sfArr = sfSp.split("\\@");
					String[] rsffArr = rsfSp.split("\\@");
					String[] sfcArr = sfcSp.split("\\@");
					String[] dxArr = dxSp.split("\\@");
					
					String[] arrs = new String[100];
					List<Integer> list = new ArrayList<Integer>();
					switch (s) {
					case 301:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(301);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(rspfArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(rspfArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(rspfArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					case 302:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(302);
						for (int j = 0; j < list.size(); j++) {
//							total += Double.parseDouble(bfArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(bfArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(bfArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					case 303:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(303);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = jqsArr[list.get(j)].split("\\_")[1] + "";
//							total += Double.parseDouble(jqsArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(jqsArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(jqsArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					case 304:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(304);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(bqcArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(bqcArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(bqcArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
//					case 320:
//						list = mSelectHhggItemindexs.get(key0.get(i)).get(320);
//						for (int j = 0; j < list.size(); j++) {
////							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(spfArr[list.get(j)].split("\\_")[1]) * 2;
//						}
//						break;
					case 306:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(306);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(rsffArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(rsffArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(rsffArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					case 307:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(307);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(sfArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(sfArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(sfArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					case 308:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(308);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(sfArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(sfcArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(sfcArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					case 309:
						list = mSelectHhggItemindexs.get(key0.get(i)).get(309);
						for (int j = 0; j < list.size(); j++) {
//							arrs[i] = bqcArr[list.get(i)].split("\\_")[1] + "";
//							total += Double.parseDouble(dxArr[list.get(j)].split("\\_")[1]) * 2;
							if (Double.parseDouble(dxArr[list.get(j)].split("\\_")[1]) > total) {
								total = Double.parseDouble(dxArr[list.get(j)].split("\\_")[1]);
							}
						}
						break;
					default:
						break;
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
		itemTotal = 0;
		maxPrice = 0d;
		try {
			// 计算注数/奖金
			getItemTotal();
			mSumPrice = Integer.parseInt(new java.text.DecimalFormat("0").format(itemTotal)) * mMultiple * 2;
			maxPrice = new BigDecimal((maxPrice * getMultiple() * 2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}catch(Error error){
			error.printStackTrace();
		}
		return new Object[] { Integer.parseInt(new java.text.DecimalFormat("0").format(itemTotal)), mSumPrice, maxPrice };
	}

	private int getItemTotal() {

		int danLength = mDan.size();
		// 获取position索引
		List<Integer> key0 = new ArrayList<Integer>(mSelectHhggItemindexs.keySet());
		mTuos = new ArrayList<List<Tuo>>();
		mTuos.clear();
		for (int i : key0) {
			if (mSelectHhggItemindexs.get(i) != null) {
				int[] positionArr = FootballLotteryTools.getMatchPositioin(i, mMatchInfo);
				Match match = mMatchInfo.get(positionArr[0]).get(positionArr[1]);
				String spfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZSPF, mSearchType);
				String rspfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZXSPF, mSearchType);
				String bfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBF, mSearchType);
				String jqsSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
				String bqcSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
				List<Tuo> tuos = new ArrayList<Tuo>();
				
				// 获取投注方式sp的索引
				Map<Integer, List<Integer>> indexs1 = mSelectHhggItemindexs.get(i);
				//302/301==
				List<Integer> key1 = new ArrayList<Integer>(indexs1.keySet());
				
				if (indexs1 != null) {
					for (int j : key1) {
						// 获取每种投注方式的索引结果集
						List<Integer> indexs2 = indexs1.get(j);

						if (indexs2 != null) {
							Tuo tuo = new Tuo();
							tuo.box = j;
							tuo.num = indexs2.size();
							Double max = 0.0d;
							String sps = "";
							switch (j) {
							case 301:
								sps = rspfSp;
								break;
							case 302:
								sps = bfSp;
								break;
							case 303:
								sps = jqsSp;
								break;
							case 304:
								sps = bqcSp;
								break;
							case 320:
								sps = spfSp;
								break;
							default:
								break;
							}
							
							for (int n : indexs2) {
								String[] spsArr = sps.split("\\@");
								Double sp = Double.parseDouble(spsArr[n].split("\\_")[1] + "");
								if (max < sp) {
									max = sp;
								}
							}
							tuo.maxSp = max;
							tuos.add(tuo);
						}
					}
				}
				mTuos.add(tuos);
			}
		}

		for (String t : mSelectChuan2Num) {
			// 获取串
			String[] t_list = t.split(App.getInstance().getResources().getString(R.string.football_chuan));
			List<Integer> fs = new ArrayList<Integer>();
			if (Integer.parseInt(t_list[1]) == 1) {
				fs.add(Integer.parseInt(t_list[0]));
			} else {
				fs = FootballLotteryConstants.getPassTypeNum().get(t);// 3*4-->[2,3] //过关方式
			}

			// 过关方式
			int guoguan_num = Integer.parseInt(t_list[0]);
			List<List<List<Tuo>>> array = null;
			// 胆的长度
//			if (danLength > 0) {// 有胆
//				
//			} else {
				array = eachArr(mTuos, guoguan_num);
//			}

			List<List<Tuo>> arr = new ArrayList<List<Tuo>>();
			Boolean isShowhh = false;// 不显示单一玩法
			// 过滤重复
			for (List<List<Tuo>> i : array) {
				
				List<List<Tuo>> v = cl(i);
				if (isShowhh) {
					for (List<Tuo> a : v) {
						int spf_num = 0, rqspf_num = 0, cbf_num = 0, jqs_num = 0, bqc_num = 0;
						for (Tuo rq : a) {
							if (rq.box == FootballLotteryConstants.L301) {
								spf_num++;
							} else if (rq.box == FootballLotteryConstants.L320) {
								rqspf_num++;
							} else if (rq.box == FootballLotteryConstants.L302) {
								jqs_num++;
							} else if (rq.box == FootballLotteryConstants.L303) {
								cbf_num++;
							} else if (rq.box == FootballLotteryConstants.L304) {
								bqc_num++;
							}
							if (spf_num == guoguan_num || rqspf_num == guoguan_num || jqs_num == guoguan_num || cbf_num == guoguan_num || bqc_num == guoguan_num) {
								v.set(0, new ArrayList<Tuo>());
								break;
							}
						}
					}
				}
				if (v.size() != 0) {
					arr.addAll(v);
				}
				
			}
			
			if (fs != null) {
				for (Integer j : fs) {
					for (List<Tuo> k : arr) {
						
						List<List<Tuo>> len = eachArr2(k, j);// [["2=1:4.3",
																				// "3=1:3.29|3=0:2.7"]]
						for (List<Tuo> n : len) {
							itemTotal += multiplyCount(n, 0);// 注数
							maxPrice += multiplyCount(n, 1);// max
							// minPrice += multiplyCount(len[n],"1");//max
							// touzhu.push(len[n]);
						}
					}
				}
			}
			
		}
		return 0;
	}

	List<Tuo> code = new ArrayList<Tuo>();

	int index=0;
	private List<List<Tuo>> cl(List<List<Tuo>> t) {
		List<List<Tuo>> tuos = new ArrayList<List<Tuo>>();
		code.clear();
		int n = 0;
		if (t.size() != 0) {
			tick.begin();
			index=1;
			allArr(tuos, t, n);
		}
		return tuos;
	}
	Tick tick=new Tick();
	private void allArr(List<List<Tuo>> tuos, List<List<Tuo>> t, int n) {
		index++;
		tick.end("allArr======"+index);
		if (n >= t.size()) {
			if (mDan.size() > 0) {
//				List<Integer> keys = new ArrayList<Integer>(mDan.keySet());
				boolean contain = false;
				boolean isBreak = false;
				for (int i : mDan) {
					if (mTuos.size() > i) {
						List<Tuo> itemTuos = mTuos.get(i);
						if (isBreak) {
							break;
						}
						for (int j = 0; j < itemTuos.size(); j++) {
							if (code.contains(itemTuos.get(j))) {
								contain = true;
								break;
							} else {
								contain = false;
								if (j == itemTuos.size() - 1) {
									isBreak = true;
								}
							}
						}
					}
				}
				if (contain) {
					tuos.add(new ArrayList<Tuo>(code));
					contain = false;
				}
			} else {
				tuos.add(new ArrayList<Tuo>(code));
			}
			code = code.subList(0, n - 1);
		} else {
			for (Tuo i : t.get(n)) {
				code.add(i);
				allArr(tuos, t, n + 1);
			}
			if (n > 0) {
				code = code.subList(0, n - 1);
			}
		}
	}

	private Double multiplyCount(List<Tuo> arr, int flag) {
		Double a = 1d;
		if (arr.size() == 0)
			return 0d;
		for (Tuo i : arr) {
			// var v = arr[i].split("_")
			if (flag == 1) {
				a *= i.maxSp;// 价格
			} else {
				a *= i.num;// 注数
			}
		}
		return a;
	}

	private List<List<List<Tuo>>> eachArr(List<List<Tuo>> arr, int num) {
		List<List<List<Tuo>>> r = new ArrayList<List<List<Tuo>>>();
		List<List<List<Tuo>>> t = new ArrayList<List<List<Tuo>>>();
		t.add(new ArrayList<List<Tuo>>());
		int n = mTuos.size();

		for (int i = 0; i < n; i++) {
			int k = t.size();
			for (int j = 0; j < k; j++) {
				List<List<Tuo>> temptuo = null;
				// if (t.size() == 0) {
				// temptuo = new ArrayList<List<Tuo>>();
				// } else {
				temptuo = new ArrayList<List<Tuo>>(t.get(j));
				// }
				temptuo.add(arr.get(i));
				if (temptuo.size() < num) {
					t.add(temptuo);
				} else {
					r.add(temptuo);
				}
			}
		}
		if (n < num) {
			return t;
		} else {
			return r;
		}
	}

	private List<List<Tuo>> eachArr2(List<Tuo> arr, int num) {
		List<List<Tuo>> r = new ArrayList<List<Tuo>>();
		List<List<Tuo>> t = new ArrayList<List<Tuo>>();
		t.add(new ArrayList<Tuo>());
		int n = arr.size();

		for (int i = 0; i < n; i++) {
			int k = t.size();
			for (int j = 0; j < k; j++) {
				List<Tuo> temptuo = null;
				// if (t.size() == 0) {
				// temptuo = new ArrayList<List<Tuo>>();
				// } else {
				temptuo = new ArrayList<Tuo>(t.get(j));
				// }
				temptuo.add(arr.get(i));
				if (temptuo.size() < num) {
					t.add(temptuo);
				} else {
					r.add(temptuo);
				}
			}
		}
		if (n < num) {
			return t;
		} else {
			return r;
		}
		
	}

	private class Tuo {

		public int box;
		public int num;
		public Double maxSp;
	}

}
