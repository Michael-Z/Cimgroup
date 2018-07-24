package cn.com.cimgroup.util;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.LotterySp;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 足彩工具类
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-2-2
 */
public class FootballLotteryTools {

	/**
	 * 获取此场比赛的sp值
	 * 
	 * @Description:
	 * @param footballMatchs
	 * @param footballSps
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-2-11
	 */
	public static String getMatchSp(List<MatchAgainstSpInfo> spList, String type, MatchSearchType mSearchType) {
		String sp = "";
		for (MatchAgainstSpInfo info : spList) {
//			MatchAgainstSpInfo info = spList.get(i);
			if(info.getPlayMethod().equals(type)){
				switch (mSearchType) {
				case TWIN:
					if (info.getPassType().equals("2")) {
						sp = info.getRealTimeSp();
					}
					break;
				case SINGLE:
					if (info.getPassType().equals("1")) {
						sp = info.getRealTimeSp();
					}
					break;
				default:
					break;
				}
				
			}
		}
		return sp;
	}
	
	/**
	 * 获取对应的一级，二级坐标
	 * @Description:
	 * @param position
	 * @param matchs
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public static int[] getMatchPositioin(int position, List<List<Match>> matchs) {
		int pos = position;
		int index = 0;
		while (index < matchs.size()) {
			if (pos > matchs.get(index).size()) {
				pos -= matchs.get(index).size();
				index++;
			} else if (pos == matchs.get(index).size()) {
				pos -= matchs.get(index).size();
			} else {
				break;
			}
		}
		int[] arr = new int[2];
		arr[0] = index;
		arr[1] = pos;
		return arr;
	}
	
	
	public static LotterySp getLotterySp(MatchAgainstSpInfo info, LotterySp sp, String passType) {
		String[] temp = null;
		String tempsp = "";
		if (info.getPassType().equals(passType)) {
			switch (info.getPlayMethod()) {
			case GlobalConstants.TC_JZSPF:
				tempsp = info.getRealTimeSp();
				if (!StringUtil.isEmpty(tempsp)) {
					String[] spArr = tempsp.split("\\@");
					temp = new String[spArr.length];
					for (int k = 0; k < spArr.length; k++) {
						switch (spArr[k].split("\\_")[0]) {
						case "3":
							temp[0] = spArr[k].split("\\_")[1];
							break;
						case "1":
							temp[1] = spArr[k].split("\\_")[1];
							break;
						case "0":
							temp[2] = spArr[k].split("\\_")[1];
							break;
						default:
							break;
						}
					}
				} else {
					temp = new String[0];
				}
				
				sp.setSpfSp(temp);
				break;
			case GlobalConstants.TC_JZXSPF:
				tempsp = info.getRealTimeSp();
				if (!StringUtil.isEmpty(tempsp)) {
					String[] spArr = tempsp.split("\\@");
					temp = new String[spArr.length];
					for (int k = 0; k < spArr.length; k++) {
						switch (spArr[k].split("\\_")[0]) {
						case "3":
							temp[0] = spArr[k].split("\\_")[1];
							break;
						case "1":
							temp[1] = spArr[k].split("\\_")[1];
							break;
						case "0":
							temp[2] = spArr[k].split("\\_")[1];
							break;
						default:
							break;
						}
					}
				} else {
					temp = new String[0];
				}
				sp.setRspfSp(temp);
				break;
			case GlobalConstants.TC_JZBF:
				tempsp = info.getRealTimeSp();
				if (!StringUtil.isEmpty(tempsp)) {
					String[] spArr = tempsp.split("\\@");
					temp = new String[spArr.length];
					for (int k = 0; k < spArr.length; k++) {
						temp[k] = spArr[k].split("\\_")[1];
					}
				} else {
					temp = new String[0];
				}
				sp.setBfSp(temp);
				break;
			case GlobalConstants.TC_JZBQC:
				tempsp = info.getRealTimeSp();
				if (!StringUtil.isEmpty(tempsp)) {
					String[] spArr = tempsp.split("\\@");
					temp = new String[spArr.length];
					for (int k = 0; k < spArr.length; k++) {
						temp[k] = spArr[k].split("\\_")[1];
					}
				} else {
					temp = new String[0];
				}
				sp.setBqcSp(temp);
				break;
			case GlobalConstants.TC_JZJQS:
				tempsp = info.getRealTimeSp();
				if (!StringUtil.isEmpty(tempsp)) {
					String[] spArr = tempsp.split("\\@");
					temp = new String[spArr.length];
					for (int k = 0; k < spArr.length; k++) {
						temp[k] = spArr[k].split("\\_")[1];
					}
				} else {
					temp = new String[0];
				}
				sp.setJqsSp(temp);
				break;
			default:
				break;
			}
		}
		return sp;
	}
}
