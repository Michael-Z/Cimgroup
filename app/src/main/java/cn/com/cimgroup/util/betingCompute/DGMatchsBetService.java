package cn.com.cimgroup.util.betingCompute;

import java.util.List;
import java.util.Map;

import cn.com.cimgroup.xutils.Tick;
import cn.com.cimgroup.xutils.XLog;

public class DGMatchsBetService extends MatchBetService {

	@Override
	public Object[] startCalculate(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 单关玩法：足球；篮球
	 **/

//	@SuppressWarnings("unchecked")
//	@Override
//	public Object[] startCalculate(Object... params) {
//		// TODO Auto-generated method stub
//		// 所有赔率值
//		List<LotteryBidSp> mFootballSps = (List<LotteryBidSp>) params[0];
//		// 选中的赛事索引集合
//		Map<Integer, List<Integer>> mSelectItemindexs = (Map<Integer, List<Integer>>) params[1];
//		// 倍数
//		int multiple = (Integer) params[2];
//		// 拼装对阵信息 格式为:格式:[1,2|maxSp]/[1,2|maxSp]/[1,2|maxSp]/[1|maxSp]
//		String betMatch = getFormatMatchInfoByArray1(mFootballSps, mSelectItemindexs);
//		// 开始计算
//		return start(betMatch, multiple);
//	}
//
//	/**
//	 * 
//	 * @Description:
//	 * @param betMatch
//	 *            投注串 格式:[1,2|maxSp]/[1,2|maxSp]/[1,2|maxSp]/[1|maxSp]
//	 * @param multiple
//	 *            倍数
//	 * @return
//	 * @see:
//	 * @since:
//	 * @author:www.wozhongla.com
//	 * @date:2016-2-22
//	 */
//	Object[] start(String betMatch, int multiple) {
//
//		Tick tick = new Tick();
//		tick.begin();
//		if (!isDGLastComputeSame(betMatch)) {
//			tick.end("过程计算");
//			LAST_TOTAL_SUM = 0;
//			tempWinPrice = 0;
//			LAST_TOTAL_SUM += getDGTotalSum(betMatch).doubleValue();
//		}
//		// 注数
//		long itemTotal = (long) (LAST_TOTAL_SUM / 2);
//		// 投注金额
//		mSumPrice = (long) (LAST_TOTAL_SUM * multiple);
//		// 预计奖金
//		// String winPrice = new DecimalFormat("########0.00").format(tempWinPrice
//		// * multiple);
//		tick.end("计算完成");
//		XLog.d("betMatch=" + betMatch);
//		return new Object[] { itemTotal, mSumPrice };
//	}
//
//	/**
//	 * 
//	 * 
//	 * @Description:
//	 * @param mLotteryBidMatchs
//	 *            所有对阵信息
//	 * @param mFootballSps
//	 *            所有赔率信息
//	 * @param mSelectItemindexs
//	 *            选择的索引信息
//	 * @return 返回投注串格式为 格式:[1,2|maxSp]/[1,2|maxSp]/[1,2|maxSp]/[1|maxSp]
//	 * @see:
//	 * @since:
//	 * @author:www.wozhongla.com
//	 * @date:2016-2-22
//	 */
//	private String getFormatMatchInfoByArray1(List<LotteryBidSp> mFootballSps, Map<Integer, List<Integer>> mSelectItemindexs) {
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < mFootballSps.size(); i++) {
//			List<Integer> selectIndexItem = null;
//			if ((selectIndexItem = mSelectItemindexs.get(i)) != null) {
//				builder.append("[");
//				for (int j = 0; j < selectIndexItem.size(); j++) {
//					builder.append((j + 1) + ",");
//				}
//				builder.delete(builder.length() - 1, builder.length());
//				builder.append("|").append(getMaxSp(mFootballSps.get(i), selectIndexItem));
//				builder.append("]");
//				builder.append("/");
//			}
//		}
//		if (builder.length() != 0) {
//			builder.delete(builder.length() - 1, builder.length());
//		}
//		return builder.toString();
//	}
}
