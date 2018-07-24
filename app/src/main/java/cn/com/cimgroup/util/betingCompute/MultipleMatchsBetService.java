package cn.com.cimgroup.util.betingCompute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchFootballState;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.Tick;

/**
 * 竞彩混合过关
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-5-9
 */
public class MultipleMatchsBetService extends MatchBetService {

	/**
	 * 初始化数据方法一 TODO 竞彩混合彩
	 * 
	 * @Description:
	 * @param mLotteryBidMatchs
	 *            对阵信息
	 * @param1 mFootballSps 赔率集合
	 * @param2 mSelectItemindexs 选中的赛事详细索引
	 * @param3 selectChuan 串
	 * @param4 multiple 倍数
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
     * @Date:2016-5-9
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] startCalculate(Object... params) {
		// 所有赛事信息
		List<List<Match>> mLotteryBidMatchs = (List<List<Match>>) params[0];
		// 选中的赛事索引集合
		HashMap<Integer, Map<Integer, List<Integer>>> mSelectItemindexs = (HashMap<Integer, Map<Integer, List<Integer>>>) params[1];
		// 选择的串
		List<String> selectChuan = (List<String>) params[2];
		// 倍数
		int multiple = (Integer) params[3];
		
		mSearchType = (MatchSearchType) params[4];
		
		mDan = (List<Integer>) params[5];
		// 获取拼装好的注码串，方便解析，格式为:betMatchs "201602232001 :[负##1,2,3##负]",
		// "201602232002 :[负##1,2,3##负]",
		// "201602232003 :[负##1,2,3##负]"
		String[] betMatch = getFormatMatchInfoByArray(mLotteryBidMatchs, mSelectItemindexs);
		String[] commSelect = new String[selectChuan.size()];
		for (int i = 0; i < selectChuan.size(); i++) {
			commSelect[i] = FootballLotteryConstants.getChuanValue2Key().get(
					selectChuan.get(i));
		}
		if (commSelect == null || commSelect.length == 0) {
			return new Object[] { 0, 0, 0 };
		}
		return start(betMatch, commSelect, multiple);
	}

	/**
	 * 进行计算
	 * 
	 * @Description:
	 * @param betMatchs
	 *            "201602232001 :[负##1,2,3##负]", "201602232002 :[负##1,2,3##负]",
	 *            "201602232003 :[负##1,2,3##负]"
	 * @param seleIds
	 *            "1", "2", "4"
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
     * @Date:2016-5-9
	 */
	Object[] start(String[] betMatchs, String[] seleIds, int multiple) {
		Tick tick = new Tick();
		tick.begin();
		if (!isLastComputeSame(Arrays.toString(betMatchs), seleIds)) {
			tick.end("过程计算");
			LAST_TOTAL_SUM = 0;
			tempWinPrice = 0;
			// 循环选号方式，进行拆单
			for (String seleId : seleIds) {
				// 注意组合，计算倍数
				int num = MatchSelectType.getChoiceNum(seleId);
				// 根据选号方式进行组合
				String[] comMatchs = null;

				comMatchs = getComMatchs(betMatchs, num);
				if (comMatchs == null) {
					LAST_TOTAL_SUM = 0;
					tempWinPrice = 0;
					break;
				}
				int i = 0;
				for (String betMatch : comMatchs) {
					i++;
					tick.end("遍历次数=" + i);
					// 看选号方式中包含了哪些选号方式 例如7串21 包含的是5串1 7串120 包含的是 2串1 3串1 4串1 5串1
					// 6串1 7串1
					// 混合过关，对比赛串关进行拆分。
					String[] commSelect = MatchSelectType.getCommSelect(seleId);
					// 计算这张票次选号方式下的总金额数
					LAST_TOTAL_SUM += getTotalSum(betMatch, commSelect, 2)
							.doubleValue();
				}
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
		return new Object[] { itemTotal, mSumPrice, winPrice };
	}

	/**
	 * 获取比赛组合
	 * 
	 * @param betMatchs
	 *            所有比赛
	 * @param num
	 *            个数
	 * @return
	 */
	public String[] getComMatchs(String[] betMatchs, int num) {
		String[] realMatchs = getRealMatchs(betMatchs);
		List<String[]> comMatchList = ChoiceUtil.combine(realMatchs, num);
		if (comMatchList == null) {
			return null;
		}
		
		List<Integer> deList = new ArrayList<Integer>();
		List<String[]> list = new ArrayList<String[]>();
		if (mDan.size() > 0) {
			for (int i = 0; i < mDan.size(); i++) {
				List<String> bb = Arrays.asList(getRealMatchs(new String[]{Arrays.asList(betMatchs).get(mDan.get(i))}));
				
				if (i == 0) {
					for (int j = 0; j < comMatchList.size(); j++) {
						
						for (String k : bb) {
							if (Arrays.asList(comMatchList.get(j)).contains(k)) {
//								deList.add(comMatchList.get(j));
								if (!list.contains(j)) {
									list.add(comMatchList.get(j));
								} else {
									list.remove(comMatchList.get(j));
								}
							}
						}
					}
				} else {
					List<String[]> temp1 = new ArrayList<String[]>();
					for (String[] j : list) {
						
						for (String k : bb) {
							if (Arrays.asList(j).contains(k)) {
								temp1.add(j);
							}
						}
					}
					list = temp1;
				}
			}
		} else {
			list = comMatchList;
		}
		
//		if (deList.size() > 0) {
//			for (int i = 0; i < comMatchList.size(); i++) {
//				if (!deList.contains(i)) {
//					list.add(comMatchList.get(i));
//				}
//			}
//		} else {
//			list = comMatchList;
//		}
		
		
		String[] matchCom = getMatchComByMatchs(list);
		return matchCom;
	}

	/**
	 * 组合中有自己跟自己的串关，要进行剔除
	 * 
	 * @param comMatchs
	 * @return
	 */
	private String[] getMatchComByMatchs(List<String[]> comMatchList) {

		List<String> matchList = new ArrayList<String>();
		// 组合中有比赛自身的组合，要剔除出去
		for (String[] betMatch : comMatchList) {
			boolean isNotExist = true;

			String bet = StringUtil.join(betMatch, "/");

			// 如果一个串关中存在两个相同比赛场次，则不能添加到最终组合中
			flag: for (String match1 : betMatch) {
				String dataIssue1 = match1.substring(match1.indexOf("=") + 1,
						match1.indexOf(":"));

				// 记录这个比赛在比赛组合中出现的次数，一旦出现两次，则为无效的串关，就要剔除掉。
				int i = 0;
				for (String match2 : betMatch) {
					String dataIssue2 = match2.substring(
							match2.indexOf("=") + 1, match2.indexOf(":"));
					if (dataIssue1.equals(dataIssue2)) {
						i++;
						if (i == 2) {
							isNotExist = false;
							break flag;
						}
					}
				}
			}
			if (isNotExist) {
				matchList.add(bet);
			}
		}
		return matchList.toArray(new String[matchList.size()]);
	}

	/**
	 * 将比赛场次拆分，有效的进行组合
	 * 
	 * @param comMatchs
	 * @return
	 */
	@SuppressLint("NewApi")
	private String[] getRealMatchs(String[] comMatchs) {
		String[] realMatchs = new String[0]; // 201502124001:[胜,平,负###胜胜,平负#胜,平,负]
												// 这种#号分割时，应该有效为三个。
		for (String betMatch : comMatchs) {
			String betContent = betMatch.substring(betMatch.indexOf("[") + 1,
					betMatch.indexOf("]")); // 胜,平,负#1:0,0:2#0,3,4#胜胜,平负#胜,平,负
			String dataIssue = betMatch.substring(0, betMatch.indexOf(":"));// 201502124001
			String[] betContents = betContent.split("#");// 让球胜平负#比分#总进球数#半全场#胜平负
			for (int i = 0; i < betContents.length; i++) {//
				if (betContents[i].trim().equals("")) {
					continue;
				}
				realMatchs = Arrays.copyOf(realMatchs, realMatchs.length + 1);// dataIssue+":"+"["+betContents[i]+"]"
				realMatchs[realMatchs.length - 1] = getPostMatch(dataIssue,
						betContents[i], i);
			}
		}
		return realMatchs;
	}

	/**
	 * 获得投注到B2B的比赛格式 混合投注专用
	 * 
	 * @param dataIssue
	 * @param string
	 * @param i
	 * @return
	 */
	private String getPostMatch(String dataIssue, String betContent, int i) {
		String lotoId = getLotoByOrder(i);
		StringBuffer sb = new StringBuffer(lotoId);
		sb.append("=").append(dataIssue).append(":[").append(betContent)
				.append("]");
		return sb.toString();
	}

	/**
	 * 混合投注投注串的彩种顺序，#分割 1,3,0##0,1,2#00,33,01#1,3,0
	 * 
	 * @param i
	 *            #拆分后数组的下标
	 * @return
	 */
	private String getLotoByOrder(int i) {
		String lotoId = "";
		switch (i) {
		case 0:
			lotoId = "301";
			break;
		case 1:
			lotoId = "302";
			break;
		case 2:
			lotoId = "303";
			break;
		case 3:
			lotoId = "304";
			break;
		case 4:
			lotoId = "320";
		case 5:
			lotoId = "306";
		case 6:
			lotoId = "307";
		case 7:
			lotoId = "308";
		case 8:
			lotoId = "309";
			break;
		default:
			break;
		}
		return lotoId;
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
	 * @return 返回投注串格式为 格式:betMatchs "201602232001 :[负##1,2,3##负]",
	 *         "201602232002 :[负##1,2,3##负]", "201602232003 :[负##1,2,3##负]"
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
     * @Date:2016-5-9
	 */
	private String[] getFormatMatchInfoByArray(
			List<List<Match>> mLotteryBidMatchs,
			HashMap<Integer, Map<Integer, List<Integer>>> mSelectItemindexs) {
		String[] matchInfo = new String[mSelectItemindexs.size()];
		ArrayList<Integer> mSelectItemIndexsHHGGKey = new ArrayList<Integer>(
				mSelectItemindexs.keySet());
		StringBuilder builder = new StringBuilder();
		// 组合
		for (int i = 0; i < mSelectItemindexs.size(); i++) {

			int[] positionArr = FootballLotteryTools.getMatchPositioin(mSelectItemIndexsHHGGKey.get(i), mLotteryBidMatchs);
			Match tempMatch = mLotteryBidMatchs.get(positionArr[0]).get(positionArr[1]);
			// 混合过关拼装
			Map<Integer, List<Integer>> tempIndexs = mSelectItemindexs
					.get(mSelectItemIndexsHHGGKey.get(i));
			List<Integer> tempIndex = null;
			/** 获取所有比分 **/
			MatchFootballState stateIndexs = null;
			// 组装第一步:MatchNo
			builder.append(tempMatch.getMatchNo()).append(":");
			builder.append("[");
			// 注：因为拼装的json串后台要进行验证，顺序不可颠倒
			// 让球胜平负
			if (tempIndexs.containsKey(FootballLotteryConstants.L301)) {
				String rspfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZXSPF, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L301);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L301);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(rspfSp, tempIndex));
			}
			builder.append("#");
			// 比分
			if (tempIndexs.containsKey(FootballLotteryConstants.L302)) {
				String bfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZBF, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L302);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L302);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(bfSp, tempIndex));
			}
			builder.append("#");
			// 总进球数
			if (tempIndexs.containsKey(FootballLotteryConstants.L303)) {
				String jqsSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZJQS, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L303);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L303);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(jqsSp, tempIndex));
			}
			builder.append("#");
			// 半全场
			if (tempIndexs.containsKey(FootballLotteryConstants.L304)) {
				String bqcSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_JZBQC, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L304);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L304);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}

				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(bqcSp, tempIndex));
			}
			builder.append("#");
			// 胜平负
			if (tempIndexs.containsKey(FootballLotteryConstants.L320)) {
				String spfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JZSPF, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L320);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L320);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(spfSp, tempIndex));
			}
			builder.append("#");
			if (tempIndexs.containsKey(FootballLotteryConstants.L306)) {
				String spfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JLXSF, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L306);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L306);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(spfSp, tempIndex));
			}
			builder.append("#");
			if (tempIndexs.containsKey(FootballLotteryConstants.L307)) {
				String spfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JLSF, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L307);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L307);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(spfSp, tempIndex));
			}
			builder.append("#");
			if (tempIndexs.containsKey(FootballLotteryConstants.L308)) {
				String spfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JLSFC, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L308);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L308);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(spfSp, tempIndex));
			}
			builder.append("#");
			if (tempIndexs.containsKey(FootballLotteryConstants.L309)) {
				String spfSp = FootballLotteryTools.getMatchSp(tempMatch.getMatchAgainstSpInfoList(),GlobalConstants.TC_JLDXF, mSearchType);
				stateIndexs = FootballLotteryConstants
						.getMatchResult(FootballLotteryConstants.L309);
				tempIndex = tempIndexs.get(FootballLotteryConstants.L309);
				for (int j = 0; j < tempIndex.size(); j++) {
					builder.append(stateIndexs.getNumName(tempIndex.get(j)))
							.append(",");
				}
				if (builder.lastIndexOf(",") + 1 == builder.length()) {
					builder.delete(builder.length() - 1, builder.length());
				}
				builder.append("|").append(getMaxSp(spfSp, tempIndex));
			}
			builder.append("]");
			matchInfo[i] = builder.toString();
			builder.setLength(0);
		}
		return matchInfo;
	}
}
