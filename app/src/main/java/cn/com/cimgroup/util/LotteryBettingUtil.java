package cn.com.cimgroup.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.text.TextUtils;
import cn.com.cimgroup.config.LotteryIssueConfig;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;

/**
 * 彩票各个彩种投注算法及显示提示及注码
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2014-12-30
 */
public class LotteryBettingUtil {

	private static void getBaseCodeString(ArrayList<Integer> list, DecimalFormat format, StringBuilder builder) {
		int index = 0;
		for (Integer integer : list) {
			if (index > 0) {
				builder.append(",");
			}
			builder.append(format.format(integer + 1));
			index++;
		}
	}

	private static void getBaseCodeString(ArrayList<Integer> list, StringBuilder builder, String format) {
		int index = 0;
		for (Integer integer : list) {
			if (index > 0) {
				builder.append(format);
			}
			builder.append(integer);
			index++;
		}
	}
	
	/**
	 * 大乐透投注提示
	 * 
	 * @Description:
	 * @param redList
	 * @param buleList
	 * @see:
	 * @since:
	 * @author:zhangjf
	 * @date:2014-12-30
	 */
	public static String setNumMoney(ArrayList<Integer> redList, ArrayList<Integer> buleList) {
		String resultStr = "";
		if (redList.size() >= 5 && buleList.size() >= 2) {
			int num = Combination(5, redList.size()) * Combination(2, buleList.size());
			int money = num * 2;
			resultStr = App.getInstance().getResources().getString(R.string.lottery_money, num, money);
		} else {
			resultStr = App.getInstance().getResources().getString(R.string.touzhu_hint, 5, 2);
		}
		return resultStr;
	}

	/**
	 * 大乐透所投钱数
	 * 
	 * @Description:
	 * @param redList
	 * @param buleList
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-23
	 */
	public static int getDltMoney(ArrayList<Integer> redList, ArrayList<Integer> buleList) {
		int num = LotteryBettingUtil.Combination(5, redList.size()) * LotteryBettingUtil.Combination(2, buleList.size());
		return num * 2;
	}

	/**
	 * 大乐透胆拖投注提示
	 * 
	 * @Description:
	 * @param redDanList
	 * @param redTuoList
	 * @param buleDanList
	 * @param buleTuoList
	 * @author:zhangjf
	 * @date:2015-1-8
	 */
	public static String setNumMoney(ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList, ArrayList<Integer> buleDanList, ArrayList<Integer> buleTuoList) {
		String resultStr = "";
		int redDanSize = redDanList.size();
		int redTuoSize = redTuoList.size();
		int buleDanSize = buleDanList.size();
		int buleTuoSize = buleTuoList.size();
		if ((redDanSize + redTuoSize) >= 6 && buleTuoSize >= 2) {
			int num = Combination(5 - redDanSize, redTuoSize) * Combination(2 - buleDanSize, buleTuoSize);
			int money = num * 2;
			resultStr = App.getInstance().getResources().getString(R.string.lottery_money, num, money);
		} else {
			resultStr = App.getInstance().getResources().getString(R.string.lottery_betting_tj1);
		}
		return resultStr;
	}

	/**
	 * 大乐透胆拖所投钱数
	 * 
	 * @Description:
	 * @param redDanList
	 * @param redTuoList
	 * @param buleDanList
	 * @param buleTuoList
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-23
	 */
	public static int getDltMoney(ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList, ArrayList<Integer> buleDanList, ArrayList<Integer> buleTuoList) {
		int redDanSize = redDanList.size();
		int redTuoSize = redTuoList.size();
		int buleDanSize = buleDanList.size();
		int buleTuoSize = buleTuoList.size();
		int num = Combination(5 - redDanSize, redTuoSize) * Combination(2 - buleDanSize, buleTuoSize);
		return num * 2;
	}

	/**
	 * 判断大乐透购物车每个item的投注类型
	 * @Description:
	 * @param redList
	 * @param buleList
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月20日
	 */
	public static String cartDltType(ArrayList<Integer> redList, ArrayList<Integer> buleList) {
		String result = "";
		if (redList.size() > 5 || buleList.size() > 2) {
			result = App.getInstance().getResources().getString(R.string.lottery_dlt_zx);
		} else if (redList.size() == 5 && buleList.size() == 2) {
			result = App.getInstance().getResources().getString(R.string.lottery_dlt_zxds);
		}
		return result;
	}

	/**
	 * 区分大乐透是单式还是复式
	 * 01单式，02复式
	 * @Description:
	 * @param redList
	 * @param buleList
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月20日
	 */
	public static String getSeleID_Dlt(ArrayList<Integer> redList, ArrayList<Integer> buleList) {
		String result = "";
		if (redList.size() > 5 || buleList.size() > 2) {
			result = "02";
		} else if (redList.size() == 5 && buleList.size() == 2) {
			result = "01";
		}
		return result;
	}

	/**
	 * 大乐透普通投注注码串
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-5
	 */
	public static String tzDlt(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		// 01,07,15,28,32|11,12 单式
		// 03,04,11,13,19,20,25,26,30,32|02,07,08,09,10 复式
		DecimalFormat format = new DecimalFormat("00");
		StringBuilder builder = new StringBuilder();
		getBaseCodeString(list1, format, builder);
		builder.append("|");
		getBaseCodeString(list2, format, builder);
		return builder.toString();
	}

	/**
	 * 大乐透胆拖投注注码串
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param list4
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-5
	 */
	public static String tzDlt(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4) {
		// 01,02,03,04@09,10#05@07,08
		DecimalFormat format = new DecimalFormat("00");
		StringBuilder builder = new StringBuilder();
		getBaseCodeString(list1, format, builder);
		builder.append("|");
		getBaseCodeString(list2, format, builder);
		builder.append("|");
		getBaseCodeString(list3, format, builder);
		builder.append("|");
		getBaseCodeString(list4, format, builder);
		return builder.toString();
	}

	/**
	 * 排列5投注提示
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param list4
	 * @param list5
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-16
	 */
	public static String setNumMoney(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5) {
		if (list1.size() > 0 && list2.size() > 0 && list3.size() > 0 && list4.size() > 0 && list5.size() > 0) {
			int num = list1.size() * list2.size() * list3.size() * list4.size() * list5.size();
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	public static String cartPl5Type(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5) {
		String result = "";
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1 && list4.size() == 1 && list5.size() == 1) {
			result = App.getInstance().getResources().getString(R.string.lottery_p3_zxds);
		} else {
			result = App.getInstance().getResources().getString(R.string.lottery_p3_zx);
		}
		return result;
	}

	public static String getSeleID_Pl5(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5) {
		String result = "";
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1 && list4.size() == 1 && list5.size() == 1) {
			result = "01";
		} else {
			result = "02";
		}
		return result;
	}

	/**
	 * 排列5注码
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param list4
	 * @param list5
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-10
	 */
	public static String tzPl5(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5) {
		StringBuilder builder = new StringBuilder();
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1 && list4.size() == 1 && list5.size() == 1) {
			builder.append(list1.get(0));
			builder.append("|");
			builder.append(list2.get(0));
			builder.append("|");
			builder.append(list3.get(0));
			builder.append("|");
			builder.append(list4.get(0));
			builder.append("|");
			builder.append(list5.get(0));
		} else {
			getBaseCodeString(list1, builder, " ");
			builder.append("|");
			getBaseCodeString(list2, builder, " ");
			builder.append("|");
			getBaseCodeString(list3, builder, " ");
			builder.append("|");
			getBaseCodeString(list4, builder, " ");
			builder.append("|");
			getBaseCodeString(list5, builder, " ");
		}
		return builder.toString();
	}

	/**
	 * 排列3-直选投注提示
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-19
	 */
	public static String setNumMoney(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		if (list1.size() > 0 && list2.size() > 0 && list3.size() > 0) {
			int num = list1.size() * list2.size() * list3.size();
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	/**
	 * 购物车排三每个item显示的选号类型：文字区分单式，复式
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月22日
	 */
	public static String cartPl3_ZxType(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		String result = "";
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1) {
			result = App.getInstance().getResources().getString(R.string.lottery_p3_zxds);
		} else {
			result = App.getInstance().getResources().getString(R.string.lottery_p3_zx);
		}
		return result;
	}

	/**
	 * 购物车排三每个item显示的选号类型：mSeleId区分01单式，02复式
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月22日
	 */
	public static String getSeleID_Pl3_Zx(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		String result = "";
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1) {
			result = "01";
		} else {
			result = "02";
		}
		return result;
	}

	/**
	 * 排列3-直选注码
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-10
	 */
	public static String tzPl3_Zx(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		StringBuilder builder = new StringBuilder();
		getBaseCodeString(list1, builder, ",");
		builder.append("|");
		getBaseCodeString(list2, builder, ",");
		builder.append("|");
		getBaseCodeString(list3, builder, ",");
		return builder.toString();
	}

	/**
	 * 排列3-直选和值投注提示
	 * 
	 * @Description:
	 * @param list
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-19
	 */
	public static String setNumMoney_Pl3Zhixhz(ArrayList<Integer> list) {
		if (list.size() > 0) {
			int num = getPl3_ZhixhzNum(list);
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}
	
	/**
	 * 排列3-组选和值投注提示
	 * @Description:
	 * @param list
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月22日
	 */
	public static String setNumMoney_Pl3Zxhz(ArrayList<Integer> list) {
		if (list.size() > 0) {
			int num = getPl3_ZxhzNum(list);
			getPl3_Zxhz(list);
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}
	
	

	public static int getPl3_ZhixhzNum(ArrayList<Integer> list) {
		int num = 0;
		for (Integer integer : list) {
			num += getHZ(integer);
		}
		return num;
	}
	
	public static int getPl3_ZxhzNum(ArrayList<Integer> list) {
		int num = 0;
		for (Integer integer : list) {
			num += getZX(integer + 1);
		}
		return num;
	}
	
	/**
	 * 排列3-组三投注提示
	 * 
	 * @Description:
	 * @param z3List
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-19
	 */
	public static String setNumMoney_Pl3Z3(ArrayList<Integer> z3List) {
		if (z3List.size() > 1) {
			int num = (int) Arrangement(2, z3List.size());
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	/**
	 * 排列3-组三组六注码
	 * 
	 * @Description:
	 * @param z3List
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-10
	 */
	public static String tzPl3_z3(ArrayList<Integer> z3List) {
		StringBuilder builder = new StringBuilder();
		getBaseCodeString(z3List, builder, ",");
		return builder.toString();
	}

	/**
	 * 排列3-组三单式投注提示
	 * 
	 * @Description:
	 * @param chList
	 * @param dhList
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-19
	 */
	public static String setNumMoney_Pl3Z3Ds(ArrayList<Integer> chList, ArrayList<Integer> dhList) {
		if (chList.size() > 0 && dhList.size() > 0) {
			return App.getInstance().getResources().getString(R.string.lottery_money, 1, 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	/**
	 * 排列3-组六投注提示
	 * 
	 * @Description:
	 * @param z6List
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-19
	 */
	public static String setNumMoney_Pl3Z6(ArrayList<Integer> z6List) {
		if (z6List.size() > 2) {
			int num = Combination(3, z6List.size());
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	/**
	 * 七星彩投注提示
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param list4
	 * @param list5
	 * @param list6
	 * @param list7
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-19
	 */
	public static String setNumMoney(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5, ArrayList<Integer> list6, ArrayList<Integer> list7) {
		if (list1.size() > 0 && list2.size() > 0 && list3.size() > 0 && list4.size() > 0 && list5.size() > 0 && list6.size() > 0 && list7.size() > 0) {
			int num = list1.size() * list2.size() * list3.size() * list4.size() * list5.size() * list6.size() * list7.size();
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	/**
	 * 七星彩投注金额
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param list4
	 * @param list5
	 * @param list6
	 * @param list7
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-23
	 */
	public static int getQxcMoney(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5, ArrayList<Integer> list6, ArrayList<Integer> list7) {
		int num = list1.size() * list2.size() * list3.size() * list4.size() * list5.size() * list6.size() * list7.size();
		return num * 2;
	}

	public static String cartQxcType(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5, ArrayList<Integer> list6, ArrayList<Integer> list7) {
		String result = "";
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1 && list4.size() == 1 && list5.size() == 1 && list6.size() == 1 && list7.size() == 1) {
			result = App.getInstance().getResources().getString(R.string.lottery_p3_zxds);
		} else {
			result = App.getInstance().getResources().getString(R.string.lottery_p3_zx);
		}
		return result;
	}

	public static String getSeleID_Qxc(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5, ArrayList<Integer> list6, ArrayList<Integer> list7) {
		String result = "";
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1 && list4.size() == 1 && list5.size() == 1 && list6.size() == 1 && list7.size() == 1) {
			result = "01";
		} else {
			result = "02";
		}
		return result;
	}

	/**
	 * 七星彩投注注码
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param list4
	 * @param list5
	 * @param list6
	 * @param list7
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-10
	 */
	public static String tzQxc(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Integer> list5, ArrayList<Integer> list6, ArrayList<Integer> list7) {
		StringBuilder builder = new StringBuilder();
		if (list1.size() == 1 && list2.size() == 1 && list3.size() == 1 && list4.size() == 1 && list5.size() == 1 && list6.size() == 1 && list7.size() == 1) {
			builder.append(list1.get(0));
			builder.append("|");
			builder.append(list2.get(0));
			builder.append("|");
			builder.append(list3.get(0));
			builder.append("|");
			builder.append(list4.get(0));
			builder.append("|");
			builder.append(list5.get(0));
			builder.append("|");
			builder.append(list6.get(0));
			builder.append("|");
			builder.append(list7.get(0));
		} else {
			getBaseCodeString(list1, builder, " ");
			builder.append("|");
			getBaseCodeString(list2, builder, " ");
			builder.append("|");
			getBaseCodeString(list3, builder, " ");
			builder.append("|");
			getBaseCodeString(list4, builder, " ");
			builder.append("|");
			getBaseCodeString(list5, builder, " ");
			builder.append("|");
			getBaseCodeString(list6, builder, " ");
			builder.append("|");
			getBaseCodeString(list7, builder, " ");
		}
		return builder.toString();
	}

	/**
	 * 11选5投注提示
	 * 
	 * @Description:
	 * @param list
	 * @param type
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-23
	 */
	public static String setNumMoney(ArrayList<Integer> list, int type) {
		if (list.size() >= type) {
			int num = Combination(type, list.size());
			return App.getInstance().getResources().getString(R.string.lottery_money, num, num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	public static String getSeleID_11x5(ArrayList<Integer> list, int type) {
		if (list.size() > type) {
			return "02";
		} else {
			return "01";
		}
	}

	/**
	 * 11选5注码
	 * 
	 * @Description:
	 * @param list
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-10
	 */
	public static String tz11x5(ArrayList<Integer> list) {
		StringBuilder builder = new StringBuilder();
		DecimalFormat format = new DecimalFormat("00");
		getBaseCodeString(list, format, builder);
		return builder.toString();
	}

	/**
	 * 得到11选5-直选2的注码
	 * 
	 * @Description:
	 * @param q1List
	 * @param q2List
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-10
	 */
	public static String get11x5_Q2Code(ArrayList<Integer> q1List, ArrayList<Integer> q2List) {
		int index = 0;
		StringBuilder builder = new StringBuilder();
		DecimalFormat format = new DecimalFormat("00");
		for (Integer integer1 : q1List) {
			for (Integer integer2 : q2List) {
				if (integer2 != integer1) {
					if (index > 0) {
						builder.append("-");
					}
					builder.append(format.format(integer1 + 1)).append(",").append(format.format(integer2 + 1));
					index++;
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 得到11选5-直选3的注码
	 * 
	 * @Description:
	 * @param q1List
	 * @param q2List
	 * @param q3List
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-10
	 */
	public static String get11x5_Q3Code(ArrayList<Integer> q1List, ArrayList<Integer> q2List, ArrayList<Integer> q3List) {
		int index = 0;
		StringBuilder builder = new StringBuilder();
		DecimalFormat format = new DecimalFormat("00");
		for (Integer integer1 : q1List) {
			for (Integer integer2 : q2List) {
				for (Integer integer3 : q3List) {
					if (integer3 != integer2 && integer3 != integer1 && integer2 != integer1) {
						if (index > 0) {
							builder.append("-");
						}
						builder.append(format.format(integer1 + 1)).append(",").append(format.format(integer2 + 1)).append(",").append(format.format(integer3 + 1));
						index++;
					}
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 11选5-前二直选投注提示
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-23
	 */
	public static String setQ2NumMoney(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		int q2Zx_Num = get11x5_Q2Zx_Num(list1, list2);
		if (q2Zx_Num > 0) {
			return App.getInstance().getResources().getString(R.string.lottery_money, q2Zx_Num, q2Zx_Num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	public static String getSeleID_Q2(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		if (list1.size() > 1 && list2.size() > 1) {
			return "02";
		} else {
			return "01";
		}
	}

	/**
	 * 11选5-前三直选投注提示
	 * 
	 * @Description:
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-23
	 */
	public static String setQ3NumMoney(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		int q2Zx_Num = get11x5_Q3Zx_Num(list1, list2, list3);
		if (q2Zx_Num > 0) {
			return App.getInstance().getResources().getString(R.string.lottery_money, q2Zx_Num, q2Zx_Num * 2);
		} else {
			return App.getInstance().getResources().getString(R.string.lottery_betting_tj2);
		}
	}

	public static String getSeleID_Q3(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
		if (list1.size() > 1 && list2.size() > 1 && list3.size() > 1) {
			return "02";
		} else {
			return "01";
		}
	}

	/**
	 * 得到11选5-前二直选的注数
	 * 
	 * @Description:
	 * @param q1List
	 * @param q2List
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-23
	 */
	public static int get11x5_Q2Zx_Num(ArrayList<Integer> q1List, ArrayList<Integer> q2List) {
		int num = 0;
		for (Integer integer1 : q1List) {
			for (Integer integer2 : q2List) {
				if (integer2 != integer1) {
					++num;
				}
			}
		}
		return num;
	}

	/**
	 * 得到11选5-前三直选的注数
	 * 
	 * @Description:
	 * @param q1List
	 * @param q2List
	 * @param q3List
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-23
	 */
	public static int get11x5_Q3Zx_Num(ArrayList<Integer> q1List, ArrayList<Integer> q2List, ArrayList<Integer> q3List) {
		int num = 0;
		for (Integer integer1 : q1List) {
			for (Integer integer2 : q2List) {
				for (Integer integer3 : q3List) {
					if (integer3 != integer2 && integer3 != integer1 && integer2 != integer1) {
						++num;
					}
				}
			}
		}
		return num;
	}

	/**
	 * 期号是否过期
	 * 
	 * @Description:
	 * @param lottery
	 * @return true为没过期
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
	public static boolean isExpire(int lottery) {
		boolean result = false;
		String endTime = getIssueEndTime(lottery);
		if (!TextUtils.isEmpty(endTime) && DateUtil.isTimeGreater(endTime)) {
			result = true;
		}
		return result;
	}

	/**
	 * 更新彩种期号
	 * 
	 * @Description:
	 * @param lottery
	 * @param issueStr
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
	public static void setIssue(int lottery, String issueStr) {
		LotteryIssueConfig issueConfig = LotteryIssueConfig.getInstance();
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			issueConfig.saveIssue(GlobalConstants.TC_DLT, issueStr);
			break;
		case GlobalConstants.LOTTERY_PL3:
			issueConfig.saveIssue(GlobalConstants.TC_PL3, issueStr);
			break;
		case GlobalConstants.LOTTERY_PL5:
			issueConfig.saveIssue(GlobalConstants.TC_PL5, issueStr);
			break;
		case GlobalConstants.LOTTERY_QXC:
			issueConfig.saveIssue(GlobalConstants.TC_QXC, issueStr);
			break;
		case GlobalConstants.LOTTERY_11X5:
			issueConfig.saveIssue(GlobalConstants.TC_11X5, issueStr);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取期号
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
	public static String getIssue(int lottery) {
		String issue = "";
		LotteryIssueConfig issueConfig = LotteryIssueConfig.getInstance();
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			issue = issueConfig.getIssue(GlobalConstants.TC_DLT);
			break;
		case GlobalConstants.LOTTERY_PL3:
			issue = issueConfig.getIssue(GlobalConstants.TC_PL3);
			break;
		case GlobalConstants.LOTTERY_PL5:
			issue = issueConfig.getIssue(GlobalConstants.TC_PL5);
			break;
		case GlobalConstants.LOTTERY_QXC:
			issue = issueConfig.getIssue(GlobalConstants.TC_QXC);
			break;

		default:
			break;
		}
		return issue;
	}

	/**
	 * 获取期号结束时间
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
	public static String getIssueEndTime(int lottery) {
		String issue = "";
		LotteryIssueConfig issueConfig = LotteryIssueConfig.getInstance();
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			issue = issueConfig.getEndTime(GlobalConstants.TC_DLT);
			break;
		case GlobalConstants.LOTTERY_PL3:
			issue = issueConfig.getEndTime(GlobalConstants.TC_PL3);
			break;
		case GlobalConstants.LOTTERY_PL5:
			issue = issueConfig.getEndTime(GlobalConstants.TC_PL5);
			break;
		case GlobalConstants.LOTTERY_QXC:
			issue = issueConfig.getEndTime(GlobalConstants.TC_QXC);
			break;

		default:
			break;
		}
		return issue;
	}

	/**
	 * 
	 * @Description:设置期号跟结束时间
	 * @param lottery
	 * @param endTime
	 * @author:zhangjf
	 * @date:2015-3-24
	 */
	public static void setIssueAndEndTime(int lottery, String issueStr, String endTime) {
		LotteryIssueConfig issueConfig = LotteryIssueConfig.getInstance();
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			issueConfig.saveIssue(GlobalConstants.TC_DLT, issueStr);
			issueConfig.saveEndTime(GlobalConstants.TC_DLT, endTime);
			break;
		case GlobalConstants.LOTTERY_PL3:
			issueConfig.saveIssue(GlobalConstants.TC_PL3, issueStr);
			issueConfig.saveEndTime(GlobalConstants.TC_PL3, endTime);
			break;
		case GlobalConstants.LOTTERY_PL5:
			issueConfig.saveIssue(GlobalConstants.TC_PL5, issueStr);
			issueConfig.saveEndTime(GlobalConstants.TC_PL5, endTime);
			break;
		case GlobalConstants.LOTTERY_QXC:
			issueConfig.saveIssue(GlobalConstants.TC_QXC, issueStr);
			issueConfig.saveEndTime(GlobalConstants.TC_QXC, endTime);
			break;
		}
	}

	/**
	 * 获取彩种gamename
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
	public static String getLottery(int lottery) {
		String lotteryStr = "";
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			lotteryStr = GlobalConstants.TC_DLT;
			break;
		case GlobalConstants.LOTTERY_PL3:
			lotteryStr = GlobalConstants.TC_PL3;
			break;
		case GlobalConstants.LOTTERY_PL5:
			lotteryStr = GlobalConstants.TC_PL5;
			break;
		case GlobalConstants.LOTTERY_QXC:
			lotteryStr = GlobalConstants.TC_QXC;
			break;

		default:
			break;
		}
		return lotteryStr;
	}

	/**
	 * 获取彩种id
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
//	public static String getLotteryId(int lottery) {
//		String lotteryId = "";
//		switch (lottery) {
//		case GlobalConstants.LOTTERY_DLT:
//			lotteryId = GlobalConstants.GAMETYPE_DLT;
//			break;
//		case GlobalConstants.LOTTERY_PL3:
//			lotteryId = GlobalConstants.GAMETYPE_PL3;
//			break;
//		case GlobalConstants.LOTTERY_PL5:
//			lotteryId = GlobalConstants.GAMETYPE_PL5;
//			break;
//		case GlobalConstants.LOTTERY_QXC:
//			lotteryId = GlobalConstants.GAMETYPE_QXC;
//			break;
//		case GlobalConstants.LOTTERY_11X5:
//			lotteryId = GlobalConstants.GAMETYPE_11X5;
//			break;
//
//		default:
//			break;
//		}
//		return lotteryId;
//	}

	// -------------------------------以下数字彩算法-----------------------------------------------------------

	/**
	 * 公式C是指组合，从N个元素取R个，不进行排列
	 * 
	 * @param r
	 *			上标
	 * @param n
	 *			下标
	 * @return
	 */
	public static int Combination(int r, int n) {
		if (r > n)
			return 0;
		if (r == n)
			return 1;
		int count = 1;
		for (int i = r; i > 0; i--) {
			count *= i;
		}
		return (int) (Arrangement(r, n) / count);
	}

	/**
	 * 公式P是指排列，从N个元素取R个进行排列
	 * 
	 * @param r
	 *			上标
	 * @param n
	 *			下标
	 * @return
	 */
	public static double Arrangement(int r, int n) {
		int i = 0;
		double count = 1;
		while (i < r) {
			count *= (n - i);
			i++;
		}
		return count;
	}
	
	/**
	 * 组合投注码
	 * 
	 * @param list
	 *			传入的号码
	 * @param tag
	 *			按几位组合
	 * @return 组合的投注码，每注的格式为“1，2，3，4，5”
	 */
	public static List<String> combination(List<Integer> list, int tag, List<Integer> dans) {
		List<String> result = new ArrayList<String>();
		DecimalFormat format = new DecimalFormat("00");
		StringBuffer buffer = new StringBuffer();
		int[] num = new int[list.size()];
		for (int i = 0; i < tag; i++) {
			num[i] = 1;
		}
		while (isEnd(num, tag)) {
			buffer = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				if (num[i] == 1) {
					if (!"".equals(buffer.toString())) {
						buffer.append(",");
					}
					buffer.append(format.format(list.get(i) + 1));
				}
			}
			result.add(buffer.toString());
			for (int i = 0; i < num.length; i++) {
				if (num[i] == 1 && i + 1 < num.length && num[i + 1] == 0) {
					num[i] = 0;
					num[i + 1] = 1;
					int count = 0;
					for (int j = 0; j < i; j++) {
						if (num[j] == 1) {
							count++;
							num[j] = 0;
						}
					}
					for (int j = 0; j < count; j++) {
						num[j] = 1;
					}
					break;
				}
			}
		}
		buffer = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (num[i] == 1) {
				if (!"".equals(buffer.toString())) {
					buffer.append(",");
				}
				buffer.append(format.format(list.get(i) + 1));
			}
		}
		result.add(buffer.toString());
		return result;
	}
	
	private static boolean isEnd(int[] num, int tag) {
		for (int i = num.length - tag; i < num.length; i++) {
			if (num[i] != 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 计算胆拖
	 * 
	 * @Description:
	 * @param num球基数
	 * @param dan
	 * @param tuo
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-13
	 */
	public static int getDanTuoNum(int num, int dan, int tuo) {
		return Combination(num - dan, tuo);
	}
	
	/**
	 * 双色球拖胆注数
	 * 
	 * @param m
	 *			拖码数
	 * @param n
	 *			胆码数
	 * @return
	 */
	public static int danTuoNum(int m, int n) {
		int count = 0;
		// 设红色球区胆码个数为n（1≤n≤5），红色球区拖码个数为m（6－n≤m≤20），蓝色球区所选个数为w，则此胆拖投注的注数个数为：
		// combin(m,6-n)×combin(w,1)
		// count = calculateNum(6 - n, m);
		count = Combination(6 - n, m);
		return count;
	}

	/**
	 * 和值注数
	 * 
	 * @param hz
	 * @return
	 */
	public static int getHZ(int hz) {
		int num = 0;
		for (int i = 0; i <= 1000; i++) {
			// 百位数
			int a = i / 100;
			// 十位数
			int b = i / 10 - a * 10;
			// 个位数
			int c = i - a * 100 - b * 10;
			int d = a + b + c;
			
			if (d == hz) {
				if (a < 10 && b < 10 && c < 10) {
					num++;
				}
			}
		}
		return num;
	}
	
	public static int getZX(int hz) {
		int num = 0;
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			// 百位数
			int a = i / 100;
			// 十位数
			int b = i / 10 - a * 10;
			// 个位数
			int c = i - a * 100 - b * 10;
			int d = a + b + c;
			if (d == hz) {
				int[] arr = {a,b,c};
				Arrays.sort(arr);
				
				if (!(a == b && b == c && a == c)) {
					if (list.size() == 0) {
						StringBuilder build = new StringBuilder();
						build.append(arr[0]);
						build.append(arr[1]);
						build.append(arr[2]);
						list.add(build.toString());
						num++;
					} else {
						for (int j = 0; j < list.size(); j++) {
							StringBuilder build = new StringBuilder();
							build.append(arr[0]);
							build.append(arr[1]);
							build.append(arr[2]);
							if (!list.contains(build.toString())) {
								list.add(build.toString());
								num++;
							}
						}
					}
				}
				
			}
		}
		return num;
	}
	
	/**
	 * 计算排3中的组3所有组合
	 * @Description:
	 * @param list
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public static List<List<Integer>> ArrangementPL3Z3(ArrayList<Integer> list) {
		List<List<Integer>> all = new ArrayList<List<Integer>>();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> tempList = new ArrayList<Integer>(list);
			tempList.remove(i);
			for (int j : tempList) {
				List<Integer> item = new ArrayList<Integer>();
				item.add(list.get(i));
				item.add(list.get(i));
				item.add(j);
				Collections.sort(item);
				all.add(item);
			}
		}
		return all;
	}
	
	/**
	 * 计算排3中的组6所有组合
	 * @Description:
	 * @param list
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public static List<List<Integer>> CombinationPL3Z6(ArrayList<Integer> list) {
		List<List<Integer>> all = new ArrayList<List<Integer>>();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> tempList = new ArrayList<Integer>(list);
			tempList.remove(i);
			for (int j = 0; j < tempList.size(); j++) {
				List<Integer> tempList1 = new ArrayList<Integer>(tempList);
				tempList1.remove(j);
				for (int k = 0; k < tempList1.size(); k++) {
					List<Integer> item = new ArrayList<Integer>();
					item.add(list.get(i));
					item.add(tempList.get(j));
					item.add(tempList1.get(k));
					Collections.sort(item);
					if (!all.contains(item)) {
						all.add(item);
					}
				}
			}
		}
		return all;
	}
	
	/**
	 * 计算排3中的直选和值所有组合
	 * @Description:
	 * @param list
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public static List<List<Integer>> getPl3_Zhixhz(ArrayList<Integer> list) {
		List<List<Integer>> all = new ArrayList<List<Integer>>();
		for (int i : list) {
			List<Integer> tempList = new ArrayList<Integer>();
			switch (i) {
			case 0:
				tempList.add(0);
				tempList.add(0);
				tempList.add(0);
				break;
			case 27:
				tempList.add(9);
				tempList.add(9);
				tempList.add(9);
				break;
			default:
				tempList.add(i);
				break;
			}
			
			all.add(tempList);
		}
		return all;
	}
	
	/**
	 * 计算排3中的组选和值所有组合
	 * @Description:
	 * @param list
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public static List<List<Integer>> getPl3_Zxhz(ArrayList<Integer> list) {
		List<List<Integer>> all = new ArrayList<List<Integer>>();
		for (int i : list) {
			List<Integer> tempList = new ArrayList<Integer>();
			switch (i) {
			case 0:
				tempList.add(0);
				tempList.add(0);
				tempList.add(1);
				break;
			case 25:
				tempList.add(8);
				tempList.add(9);
				tempList.add(9);
				break;
			default:
				tempList.add(i);
				break;
			}
			
			all.add(tempList);
		}
		return all;
	}
}
