package cn.com.cimgroup.util.betingCompute;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 竞彩计算基类
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-5-9
 */
public abstract class MatchBetService {

	// 花费总金额
	protected static long mSumPrice = 0;

	// 上一次计算的内容HASH，防止相同数据的二次计算带来的内存消耗以及时间上的消耗
	protected static int LASTCALCULATIONCONTENT = 0;
	// 上一次(单票&&单倍)计算结果
	protected static double LAST_TOTAL_SUM = 0;
	// 预计奖金
	protected static double tempWinPrice = 0d;
	
	protected MatchSearchType mSearchType;
	
	protected List<Integer> mDan = new ArrayList<Integer>();
	
	private int mType = 1;

	/**
	 * 进行计算
	 * @Description:
	 * @param params
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-5-9
	 */
	public abstract Object[] startCalculate(Object... params);

	/**
	 * 计算一张票的金额数
	 * 
	 * @param betMatch
	 *			投注串 201502124001:[胜,平]/201502124002:[胜,平]/201502124003:[胜,负]/
	 *			201502124004:[胜]
	 * @param commSelect
	 *			[1,2,3,4,5,6,7,8]
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getTotalSum(String betMatch, String[] commSelect, int type) {
		BigDecimal totalSum = new BigDecimal(0);
		mType = type;
		String[] matchs = betMatch.split("/");
		for (String sNum : commSelect) {
			String[] smatchs = getComArray(matchs, Integer.parseInt(sNum));
			if (smatchs == null || smatchs.length == 0) {
				return new BigDecimal(0);
			}
			for (String smatch : smatchs) {
				BigDecimal commMultiple = getCommNum(smatch);
				totalSum = totalSum.add(commMultiple
						.multiply(new BigDecimal(2)));
			}
		}
		return totalSum;
	}
	/**
	 * 单关计算一张票的金额数
	 * 
	 * @param betMatch
	 *			投注串 201502124001:[胜,平]/201502124002:[胜,平]/201502124003:[胜,负]/
	 *			201502124004:[胜]
	 * @param commSelect
	 *			[1,2,3,4,5,6,7,8]
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getDGTotalSum(String betMatch) {
		BigDecimal totalSum = new BigDecimal(0);
		String[] matchs = betMatch.split("/");
//			String[] smatchs = getComArray(matchs);
//			for (String smatch : smatchs) {
			for (String smatch : matchs) {
				BigDecimal commMultiple = getCommNum(smatch);
				totalSum = totalSum.add(commMultiple
						.multiply(new BigDecimal(2)));
			}
		return totalSum;
	}

	/**
	 * 从一个数组中挑选出num个数的元素用/连接，组成一个新的数组。
	 * 
	 * @param array
	 * @param num
	 * @return
	 */
	private String[] getComArray(String[] array, int num) {
		List<String> comArray = new ArrayList<String>();
		String[] temp = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			temp[i] = i + "";
		}
		List<String[]> cArray = ChoiceUtil.combine(temp, num);
		if (cArray == null || cArray.size() == 0) {
			return null;
		}
		
		List<String[]> list = new ArrayList<String[]>();
		
		if (mType == 1) {
			List<Integer> deList = new ArrayList<Integer>();
			if (mDan.size() > 0) {
				for (int i = 0; i < cArray.size(); i++) {
					for (int j : mDan) {
						if (!(Arrays.asList(cArray.get(i)).contains(j + ""))) {
//							cArray.remove(i);
							if (!deList.contains(i)) {
								deList.add(i);
							}
						}
					}
				}
			}
			
			
			if (deList.size() > 0) {
				for (int i = 0; i < cArray.size(); i++) {
					if (!deList.contains(i)) {
						list.add(cArray.get(i));
					}
				}
			} else {
				list = cArray;
			}
			
//			for (int i = 0; i < deList.size(); i++) {
//				cArray.remove(cArray.get(deList.get(i)));
//			}
		} else {
			list = cArray;
		}
		
		for (String[] matchs : list) {
			String[] tempMatchs = new String[matchs.length];
			for (int i = 0; i < matchs.length; i++) {
				tempMatchs[i] = array[Integer.parseInt(matchs[i])];
			}
			comArray.add(StringUtil.join(tempMatchs, "/"));
		}
		return comArray.toArray(new String[] {});
	}

	/**
	 * 计算倍数（非购彩是选择的倍数）
	 * 
	 * @param betMatch
	 *			投注项 201502124001:[胜,平]/201502124002:[胜,平]
	 * @return
	 */
	private BigDecimal getCommNum(String betMatch) {
		double commSpNum = 1d;
		String[] matchs = betMatch.split("/");
		// 计算组合倍数 201502124001:[胜,平]/201502124002:[胜,平] 这个是 2*2 = 4
		BigDecimal commMultiple = new BigDecimal(1);
		for (String match : matchs) {
			String content = match.substring(match.indexOf("[") + 1,
					match.indexOf("]"));
			String[] bts = content.split(","); // 胜,平 投注内容
			commMultiple = commMultiple.multiply(new BigDecimal(bts.length));
			// 根据赔率计算预计奖金
			if (bts != null && bts.length > 0) {
				double maSp = Double.parseDouble(bts[bts.length - 1]
						.split("\\|")[1]);
				commSpNum = commSpNum * maSp;
			}
		}
		tempWinPrice += commSpNum * 2;
		return commMultiple;
	}

	/**
	 * 是否内容已经进行过计算
	 * @Description:
	 * @param betMatch
	 * @param commSelect
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-5-9
	 */
	protected Boolean isLastComputeSame(String betMatch, String[] commSelect) {
		StringBuffer sb = new StringBuffer();
		for (String s : commSelect) {
			sb.append(s);
		}
		if (mDan.size() > 0) {
            for (int dan : mDan) {
                sb.append(dan);
            }
        }
		int newContentHash = (betMatch + sb.toString()).hashCode();
		if (LASTCALCULATIONCONTENT == newContentHash && LAST_TOTAL_SUM > 0) {
			return true;
		} else {
			LASTCALCULATIONCONTENT = newContentHash;
			return false;
		}
	}
	
	
	/**
	 * 单关是否内容已经进行过计算
	 * @Description:
	 * @param betMatch
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-5-9
	 */
	protected Boolean isDGLastComputeSame(String betMatch) {
		StringBuffer sb = new StringBuffer();
		int newContentHash = (betMatch).hashCode();
		if (LASTCALCULATIONCONTENT == newContentHash && LAST_TOTAL_SUM > 0) {
			return true;
		} else {
			LASTCALCULATIONCONTENT = newContentHash;
			return false;
		}
	}
	

	/**
	 * 获取最大赔率值
	 * @Description:
	 * @param bidSp
	 * @param selectIndexItem
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-5-9
	 */
	protected double getMaxSp(String bidSp, List<Integer> selectIndexItem) {
		String[] sps = new String[selectIndexItem.size()];
		if (bidSp.indexOf("@") != -1) {
			String[] tempSp = bidSp.split("\\@");
			
			for (int i = 0; i < selectIndexItem.size(); i++) {
				if (tempSp.length >= selectIndexItem.size()) {
					sps[i] = tempSp[selectIndexItem.get(i)].split("\\_")[1];
				} else {
					sps[i] = "0";
				}
			}
		}
		double maxSp = 0d;
		//添加判空操作
		if (sps != null) {
			for (int i = 0; i < sps.length; i++) {
				if (sps[i] != null) {
					if (Double.parseDouble(sps[i]) > maxSp) {
						maxSp = Double.parseDouble(sps[i]);
					}
				}
			}
		}
		
		return maxSp;
	}

	/**
	 * 需要花费的金额
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-5-9
	 */
	public long getSumPrice() {
		return mSumPrice;
	}
	
	public double filterWinPrice(){
		//返回值
		double restr = 0d;
//		if(tempWinPrice){
			//截取数据
			String str2 = tempWinPrice + "";
		     //截取数据
			String[] fgn = str2.split("\\.");
			String l2 = "";
			String d2 = "";
			String d3 = "";
			String fgs = fgn[1] != null ? fgn[1] : "";
			int xL = fgn[1] != null ? fgn[1].length() :0;
	    	      if(xL < 3){
		    	     for(int i=xL; i<=3; i++ ){
		    	     	 fgs+="0";
		    	     }
		      }
		    l2 = fgs.substring(0,2);
			d2= fgs.substring(1,2);
			d3 = fgs.substring(2,3);
			if(Integer.parseInt(d3) == 5){
			    if(Integer.parseInt(d2)%2==0){
			           restr = Double.parseDouble(fgn[0]+"."+l2);
			    }else{
		                restr = Double.parseDouble(fgn[0]+"."+(Integer.parseInt(l2)+1));
			    }
			} else {
				restr = Double.parseDouble(new DecimalFormat("########0.00").format(tempWinPrice));
			}
			
//		}
		return restr;
	}

}
