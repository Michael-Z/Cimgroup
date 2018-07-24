package cn.com.cimgroup.util.betingCompute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.Tick;
import cn.com.cimgroup.xutils.XLog;

/**
 * 普通玩法 li.(竞彩足球:胜平负、让球胜平负、比分、进球数) li.竞彩篮球
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-5-9
 */
public class OrdinaryMatchsBetService extends MatchBetService {
    private int lotoId = 0;
    
    private Match mOneWinMatch;
    
	/**
	 * 初始化数据方法一 TODO 目前支持竞彩足球(混合除外)、竞彩蓝球
	 * 
	 * @Description:
	 * @param1 mFootballSps 赔率集合
	 * @param2 mSelectItemindexs 选中的赛事详细索引
	 * @param3 commSelect 串
	 * @param4 multiple 倍数
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
	 * @Date:2016-5-9
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] startCalculate(Object... params) {
		// 所有赔率值
		List<List<Match>> mLotteryBidMatchs = (List<List<Match>>) params[0];
		// 选中的赛事索引集合
		Map<Integer, List<Integer>> mSelectItemindexs = (Map<Integer, List<Integer>>) params[1];
		// 选择的串
		List<String> selectChuan = (List<String>) params[2];
		// 倍数
		int multiple = (Integer) params[3];
		
		mSearchType = (MatchSearchType) params[4];
		
		lotoId = (int) params[5];
		
		mDan = (List<Integer>) params[6];
		
		if (lotoId == FootballLotteryConstants.L502) {
			mOneWinMatch = (Match) params[7];
		}
		
		// 拼装对阵信息 格式为:格式:[1,2|maxSp]/[1,2|maxSp]/[1,2|maxSp]/[1|maxSp]
		String betMatch = getFormatMatchInfoByArray1(mLotteryBidMatchs,
				mSelectItemindexs);
		String[] commSelect = new String[selectChuan.size()];
		for (int i = 0; i < selectChuan.size(); i++) {
			commSelect[i] = FootballLotteryConstants.getChuanValue2Key().get(
					selectChuan.get(i));
		}
		// 开始计算
		return start(betMatch, commSelect, multiple);
	}

	/**
	 * 
	 * @Description:
	 * @param betMatch
	 *            投注串 格式:[1,2|maxSp]/[1,2|maxSp]/[1,2|maxSp]/[1|maxSp]
	 * @param seleIds
	 *            串号 格式:[1,2,3,4,5,6,7,8]
	 * @param multiple
	 *            倍数
	 * @return
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
	 * @Date:2016-5-9
	 */
	Object[] start(String betMatch, String[] seleIds, int multiple) {

		Tick tick = new Tick();
		tick.begin();
		if (!isLastComputeSame(betMatch, seleIds)) {
			tick.end("过程计算");
			LAST_TOTAL_SUM = 0;
			tempWinPrice = 0;
			for (int i = 0; i < seleIds.length; i++) {
				String commSelect[] = MatchSelectType.getCommSelect(seleIds[i]);
				LAST_TOTAL_SUM += getTotalSum(betMatch, commSelect, 1)
						.doubleValue();
			}
		}
		// 注数
		long itemTotal = (long) (LAST_TOTAL_SUM / 2);
		// 投注金额
		mSumPrice = (long) (LAST_TOTAL_SUM * multiple);
		// 预计奖金
//		String winPrice = new DecimalFormat("########0.00").format(filterWinPrice()
//				* multiple);
		String winPrice = new DecimalFormat("########0.00").format(tempWinPrice
			                                           				* multiple);
		tick.end("计算完成");
		XLog.d("betMatch=" + betMatch);
		XLog.d("commSelect=" + seleIds.toString());
		return new Object[] { itemTotal, mSumPrice, winPrice };
	}

	// ////////将原数据拼装成可以进行计算的数据格式////////////////////////////////////
	/**
	 * 
	 * 
	 * @Description:
	 * @param mLotteryBidMatchs
	 *            所有对阵信息
	 * @param mFootballSps
	 *            所有赔率信息
	 * @param mSelectItemindexs
	 *            选择的索引信息
	 * @return 返回投注串格式为 格式:[1,2|maxSp]/[1,2|maxSp]/[1,2|maxSp]/[1|maxSp]
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
	 * @Date:2016-5-9
	 */
	private String getFormatMatchInfoByArray1(List<List<Match>> mLotteryBidMatchs,
			Map<Integer, List<Integer>> mSelectItemindexs) {
		
		ArrayList<Integer> mSelectItemIndexsKey = new ArrayList<Integer>(
				mSelectItemindexs.keySet());
		Collections.sort(mSelectItemIndexsKey);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < mSelectItemIndexsKey.size(); i++) {
			int[] positionArr = FootballLotteryTools.getMatchPositioin(mSelectItemIndexsKey.get(i), mLotteryBidMatchs);
			
			int count = 0;
			for (int j = 0; j < mLotteryBidMatchs.size(); j++) {
				count += mLotteryBidMatchs.get(j).size();
			}
			
			Match tempMatch = null;
			if (positionArr[0] <= count && mLotteryBidMatchs.size() > positionArr[0]) {
				if (mLotteryBidMatchs.get(positionArr[0]).size() > 0) {
					tempMatch = mLotteryBidMatchs.get(positionArr[0]).get(positionArr[1]);
				}
			} else {
				tempMatch = null;
			}
			
			List<Integer> selectIndexItem = mSelectItemindexs.get(mSelectItemIndexsKey.get(i));
			if (selectIndexItem != null && selectIndexItem.size() > 0 && tempMatch != null) {
				String sp = "";
				switch (lotoId) {
				case FootballLotteryConstants.L302:
					// 比分
					sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZBF, mSearchType);
					break;
				case FootballLotteryConstants.L303:
					// 总进球数
					sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
					break;
				case FootballLotteryConstants.L304:
					// 半全场
					sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
					break;
				case FootballLotteryConstants.L308:
					// 胜分差
					sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLSFC, mSearchType);
					break;
				case FootballLotteryConstants.L309:
					// 大小分
					sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_JLDXF, mSearchType);
					break;
				case FootballLotteryConstants.L501:
					// 大小分
					sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_2X1, mSearchType);
					break;
				case FootballLotteryConstants.L502:
					// 大小分
					if (tempMatch != null) {
						sp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_1CZS, mSearchType);
					}
					break;
				default:
					break;
				}
				builder.append("[");
				for (int j = 0; j < selectIndexItem.size(); j++) {
					builder.append((j + 1) + ",");
				}
				builder.delete(builder.length() - 1, builder.length());
				builder.append("|").append(
						getMaxSp(sp, selectIndexItem));
				builder.append("]");
				builder.append("/");
			} else {
//				mOneWinMatch
				String sp = FootballLotteryTools.getMatchSp(mOneWinMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_2X1, mSearchType);
				builder.append("[");
				selectIndexItem = new ArrayList<Integer>();
				selectIndexItem.add(0);
				selectIndexItem.add(1);
				for (int j = 0; j < selectIndexItem.size(); j++) {
					builder.append((j + 1) + ",");
				}
				builder.delete(builder.length() - 1, builder.length());
				builder.append("|").append(
						getMaxSp(sp, selectIndexItem));
				builder.append("]");
				builder.append("/");
			}
		}
		if (builder.length() != 0) {
			builder.delete(builder.length() - 1, builder.length());
		}
		return builder.toString();
	}

	// public static void main(String[] args) {
	// LotteryBidCompute tionCalculation = new LotteryBidCompute();
	// // ...,lastNum(为最大赔率)
	// String betMatch = "[1,2|3.3]/[1,2|3.3]/[1,2|3.3]/[1|3.3]";
	// String[] commSelect = { "2", "3", "4" };
	// System.out.println(tionCalculation.getTotalSum(betMatch, commSelect));
	// }

}
