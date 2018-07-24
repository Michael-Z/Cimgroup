package cn.com.cimgroup.util.betingCompute;

/**
 * 竞彩计算工厂
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-5-9
 */
public class MatchBetFactory {

	public static MatchBetService create(String lotoId) {
		switch (lotoId) {
		case "305":
		case "320":
		case "307":
		case "310":
			return new MultipleMatchsBetService();
		case "301":
		case "302":
		case "303":
		case "304":
		case "306":
		case "308":
		case "309":
		case "501":
		case "502":
			return new OrdinaryMatchsBetService();
		case "311":
		case "312":
		case "313":
		case "314":
		case "321":
		case "316":
		case "317":
		case "318":
		case "319":
			return new DGMatchsBetService();
		}
		return null;
	}
}
