package cn.com.cimgroup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.com.cimgroup.bean.MatchFootballState;
import cn.com.cimgroup.App;
import cn.com.cimgroup.R;

/**
 * 竞彩足球
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-16
 */
public class FootballLotteryConstants {

	private static List<String[]> mLotteryDrawFootballSelect = new ArrayList<String[]>();

	/** 五大联赛 **/
	public final static String mFiveMatch[] = {
			App.getInstance().getResources()
					.getString(R.string.football_fivegame_yc),
			App.getInstance().getResources()
					.getString(R.string.football_fivegame_xj),
			App.getInstance().getResources()
					.getString(R.string.football_fivegame_yj),
			App.getInstance().getResources()
					.getString(R.string.football_fivegame_dj),
			App.getInstance().getResources()
					.getString(R.string.football_fivegame_fj) };
	/** 比赛结果ID **/
	private static Map<Integer, MatchFootballState> mMatchResult = new TreeMap<Integer, MatchFootballState>();
	/** 足球竞彩投注方式 **/
	private static List<String[]> mFootballMatchSelect = new ArrayList<String[]>();
	/** 胜负彩投注方式 **/
	private static List<String[]> mSfcMatchSelect = new ArrayList<String[]>();
	/** 单场彩投注方式 **/
	private static List<String[]> mDccMatchSelect = new ArrayList<String[]>();
	/** 篮球竞彩投注方式 **/
	private static List<String[]> mBasketballMatchSelect = new ArrayList<String[]>();
	/** 篮球单关竞彩投注方式 **/
	private static List<String[]> mBasketballDGMatchSelect = new ArrayList<String[]>();
	/** 足球单关竞彩投注方式 **/
	private static List<String[]> mfootballDGMatchSelect = new ArrayList<String[]>();

	// ///////////单场彩////////////////////
	/** 单场彩胜平负 **/
	public static final int L201 = 201;
	/** 单场彩上下单双 **/
	public static final int L202 = 202;
	/** 单场彩总进球 **/
	public static final int L203 = 203;
	/** 单场彩半全场 **/
	public static final int L204 = 204;
	/** 单场彩比分 **/
	public static final int L205 = 205;
	public static final ArrayList<Integer> LDCC = new ArrayList<Integer>();
	// ///////////竞彩足球////////////////////
	/** 竞彩足球让球胜平负 **/
	public static final int L301 = 301;
	/** 竞彩足球胜平负 **/
	public static final int L320 = 320;
	/** 竞彩足球比分 **/
	public static final int L302 = 302;
	/** 竞彩足球总进球数 **/
	public static final int L303 = 303;
	/** 竞彩足球 半全场 **/
	public static final int L304 = 304;
	/** 竞彩足球 混合过关 **/
	public static final int L305 = 305;

	// /////////////竞彩篮球//////////////////////
	/** 篮球让分胜负 1 **/
	public static final int L306 = 306;
	/** 篮球胜负1 **/
	public static final int L307 = 307;
	/** 篮球胜分差1 **/
	public static final int L308 = 308;
	/** 篮球大小分差1 **/
	public static final int L309 = 309;
	/** 篮球混合 **/
	public static final int L310 = 310;

	// /////////////竞彩足球单关//////////////////////
	/** 竞彩足球让球胜平负(单关) **/
	public static final int L311 = 311;
	/** 竞彩足球比分(单关) **/
	public static final int L312 = 312;
	/** 竞彩足球总进球数 (单关) **/
	public static final int L313 = 313;
	/** 竞彩足球 半全场(单关) **/
	public static final int L314 = 314;
	/** 竞彩足球胜平负(单关) **/
	public static final int L321 = 321;

	// /////////////竞彩篮球单关//////////////////////
	/** 竞彩篮球让分胜负(单关) **/
	public static final int L316 = 316;
	/** 竞彩篮球胜负(单关) **/
	public static final int L317 = 317;
	/** 竞彩篮球胜分差 (单关) **/
	public static final int L318 = 318;
	/** 竞彩篮球大小分差(单关) **/
	public static final int L319 = 319;
	
	/** 竞彩足球二选一 **/
	public static final int L501 = 501;
	/** 竞彩足球一场制胜 **/
	public static final int L502 = 502;

	// N串M 过关投注选项
	private static Map<String, String> chuan2num = new TreeMap<String, String>();

	private static Map<String, String> mChuanValue2key = new TreeMap<String, String>();
	
	private static Map<String, String> mChuanValue2chuan = new TreeMap<String, String>();

	// 竞彩串与seleId对应关系集合
	private static Map<String, String> mChuanValue2SeleId = new TreeMap<String, String>();
	// 自由过关N串1
	private static Map<String, String> mChuan1Num = new TreeMap<String, String>();
	// 单场彩N串1
	private static Map<String, String> mChuan1Num_Dcc = new TreeMap<String, String>();
	// 单场彩串与seleId对应关系集合
	private static Map<String, String> mChuanValue2SeleId_Dcc = new TreeMap<String, String>();

	// 混合过关使用
	private static Map<String, List<Integer>> mPassTypeNum = new TreeMap<String, List<Integer>>();

	static {

		mLotteryDrawFootballSelect.add(new String[] {
				"0",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_spf1_desc) });
		mLotteryDrawFootballSelect.add(new String[] {
				"1",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_rspf_desc) });
		mLotteryDrawFootballSelect.add(new String[] {
				"2",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_bf_desc) });
		mLotteryDrawFootballSelect.add(new String[] {
				"3",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_zjqs_desc) });
		mLotteryDrawFootballSelect.add(new String[] {
				"4",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_bqc_desc) });

		// // 胜负彩
		// mSfcMatchSelect.add(new String[] {
		// "117",
		// App.getInstance().getResources()
		// .getString(R.string.lottery_sfc_14) });
		// mSfcMatchSelect.add(new String[] {
		// "118",
		// App.getInstance().getResources()
		// .getString(R.string.lottery_sfc_9) });
		// 单场彩
		mDccMatchSelect.add(new String[] {
				"201",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_spf_desc) });
		mDccMatchSelect.add(new String[] {
				"203",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_zjqs_desc) });
		mDccMatchSelect.add(new String[] {
				"205",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_bf_desc) });
		mDccMatchSelect.add(new String[] {
				"204",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_bqc_desc) });
		// mDccMatchSelect.add(new String[] {
		// "202",
		// App.getInstance().getResources()
		// .getString(R.string.football_dialog_sxds_desc) });
		LDCC.add(L201);
		LDCC.add(L202);
		LDCC.add(L203);
		LDCC.add(L204);
		LDCC.add(L205);
		// 足球
//		mFootballMatchSelect.add(new String[] {
//				"320",
//				App.getInstance().getResources()
//						.getString(R.string.football_dialog_spf_desc) });
//		mFootballMatchSelect.add(new String[] {
//				"301",
//				App.getInstance().getResources()
//						.getString(R.string.football_dialog_rspf_desc) });
//		mFootballMatchSelect.add(new String[] {
//				"302",
//				App.getInstance().getResources()
//						.getString(R.string.football_dialog_bf_desc) });
//		mFootballMatchSelect.add(new String[] {
//				"303",
//				App.getInstance().getResources()
//						.getString(R.string.football_dialog_zjqs_desc) });
//		mFootballMatchSelect.add(new String[] {
//				"304",
//				App.getInstance().getResources()
//						.getString(R.string.football_dialog_bqc_desc) });
//		mFootballMatchSelect.add(new String[] {
//				"305",
//				App.getInstance().getResources()
//						.getString(R.string.football_dialog_hhgg_desc) });
		mFootballMatchSelect.add(new String[] { "0", App.getInstance().getResources().getString(R.string.football_dialog_spf_desc) });
//		mFootballMatchSelect.add(new String[] { "1", App.getInstance().getResources().getString(R.string.football_dialog_rspf_desc) });
		mFootballMatchSelect.add(new String[] { "2", App.getInstance().getResources().getString(R.string.football_dialog_bf_desc) });
		mFootballMatchSelect.add(new String[] { "3", App.getInstance().getResources().getString(R.string.football_dialog_zjqs_desc) });
		mFootballMatchSelect.add(new String[] { "4", App.getInstance().getResources().getString(R.string.football_dialog_bqc_desc) });
		mFootballMatchSelect.add(new String[] { "5", App.getInstance().getResources().getString(R.string.football_dialog_hhgg_desc) });
		mFootballMatchSelect.add(new String[] { "6", "一场制胜"});
		mFootballMatchSelect.add(new String[] { "7", "二选一" });
		
 
		// 足球单关
		mfootballDGMatchSelect.add(new String[] {
				"311",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_rspf_desc) });
		mfootballDGMatchSelect.add(new String[] {
				"312",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_bf_desc) });
		mfootballDGMatchSelect.add(new String[] {
				"313",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_zjqs_desc) });
		mfootballDGMatchSelect.add(new String[] {
				"314",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_bqc_desc) });
		mfootballDGMatchSelect.add(new String[] {
				"321",
				App.getInstance().getResources()
						.getString(R.string.football_dialog_spf_desc) });

		mMatchResult.put(L311, new MatchFootballState(
				new Integer[] { 0, 1, 2 }, App.getInstance().getResources()
						.getStringArray(R.array.football_state_spf)));
		mMatchResult.put(
				L312,
				new MatchFootballState(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7,
						8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
						22, 23, 24, 25, 26, 27, 28, 29, 30 }, new String[] {
						App.getInstance().getResources()
								.getString(R.string.basketballlottery_sqt),
						"1:0",
						"2:0",
						"2:1",
						"3:0",
						"3:1",
						"3:2",
						"4:0",
						"4:1",
						"4:2",
						"5:0",
						"5:1",
						"5:2",
						App.getInstance().getResources()
								.getString(R.string.basketballlottery_pqt),
						"0:0",
						"1:1",
						"2:2",
						"3:3",
						App.getInstance().getResources()
								.getString(R.string.basketballlottery_fqt),
						"0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4",
						"2:4", "0:5", "1:5", "2:5" }));
		mMatchResult.put(L313, new MatchFootballState(new Integer[] { 0, 1, 2,
				3, 4, 5, 6, 7 }, new String[] { "0", "1", "2", "3", "4", "5",
				"6", "7" }));
		mMatchResult.put(L314, new MatchFootballState(new Integer[] { 0, 1, 2,
				3, 4, 5, 6, 7, 8 }, App.getInstance().getResources()
				.getStringArray(R.array.football_state_array)));
		mMatchResult.put(L321, new MatchFootballState(
				new Integer[] { 0, 1, 2 }, App.getInstance().getResources()
						.getStringArray(R.array.football_state_spf)));

		// 篮球单关
		// mBasketballDGMatchSelect.add(new String[] {
		// "316",
		// App.getInstance().getResources()
		// .getString(R.string.basketball_match_rfsf) });
		// mBasketballDGMatchSelect.add(new String[] {
		// "317",
		// App.getInstance().getResources()
		// .getString(R.string.basketball_match_sf) });
		// mBasketballDGMatchSelect.add(new String[] {
		// "318",
		// App.getInstance().getResources()
		// .getString(R.string.basketball_match_sfc) });
		// mBasketballDGMatchSelect.add(new String[] {
		// "319",
		// App.getInstance().getResources()
		// .getString(R.string.basketball_match_dxf) });

		mMatchResult
				.put(L316,
						new MatchFootballState(
								new Integer[] { 0, 1 },
								new String[] {
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primaryfail),
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins) }));
		mMatchResult
				.put(L317,
						new MatchFootballState(
								new Integer[] { 0, 1 },
								new String[] {
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primaryfail),
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins) }));
		mMatchResult
				.put(L318,
						new MatchFootballState(
								new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
										10, 11 },
								new String[] {
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "1-5",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "6-10",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "11-15",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "16-20",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "21-25",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "26+",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "1-5",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "6-10",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "11-15",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "16-20",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "21-25",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "26+" }));
		mMatchResult.put(
				L319,
				new MatchFootballState(new Integer[] { 0, 1 }, App
						.getInstance().getResources()
						.getStringArray(R.array.football_state_dx)));

//		 篮球
		 mBasketballMatchSelect.add(new String[] { "0", App.getInstance().getResources().getString(R.string.basketball_match_sf) });
		 mBasketballMatchSelect.add(new String[] { "1", App.getInstance().getResources().getString(R.string.basketball_match_dxf) });
		 mBasketballMatchSelect.add(new String[] { "2", App.getInstance().getResources().getString(R.string.basketball_match_sfc) });
		 mBasketballMatchSelect.add(new String[] { "3", App.getInstance().getResources().getString(R.string.football_dialog_hhgg_desc) });

		mMatchResult
				.put(L306,
						new MatchFootballState(
								new Integer[] { 0, 1 },
								new String[] {
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primaryfail),
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins) }));
		mMatchResult
				.put(L307,
						new MatchFootballState(
								new Integer[] { 0, 1 },
								new String[] {
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primaryfail),
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins) }));
		mMatchResult
				.put(L308,
						new MatchFootballState(
								new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
										10, 11 },
								new String[] {
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "1-5",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "6-10",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "11-15",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "16-20",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "21-25",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_customerwins)
												+ "26+",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "1-5",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "6-10",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "11-15",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "16-20",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "21-25",
										App.getInstance()
												.getResources()
												.getString(
														R.string.basketballlottery_primarywins)
												+ "26+" }));
		mMatchResult.put(
				L309,
				new MatchFootballState(new Integer[] { 0, 1 }, App
						.getInstance().getResources()
						.getStringArray(R.array.football_state_dx)));

		mMatchResult.put(L301, new MatchFootballState(
				new Integer[] { 0, 1, 2 }, App.getInstance().getResources()
						.getStringArray(R.array.football_state_spf)));
		mMatchResult.put(L320, new MatchFootballState(
				new Integer[] { 0, 1, 2 }, App.getInstance().getResources()
						.getStringArray(R.array.football_state_spf)));
		mMatchResult.put(L201, new MatchFootballState(
				new Integer[] { 0, 1, 2 }, App.getInstance().getResources()
						.getStringArray(R.array.football_state_spf)));
		// mMatchResult.put(
		// L202,
		// new MatchFootballState(new Integer[] { 0, 1, 2, 3 }, App
		// .getInstance().getResources()
		// .getStringArray(R.array.football_state_sxds)));
		mMatchResult.put(L203, new MatchFootballState(new Integer[] { 0, 1, 2,
				3, 4, 5, 6, 7 }, new String[] { "0", "1", "2", "3", "4", "5",
				"6", "7" }));
		mMatchResult.put(
				L302,
				new MatchFootballState(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7,
						8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
						22, 23, 24, 25, 26, 27, 28, 29, 30 }, new String[] {
						"1:0",
						"2:0",
						"2:1",
						"3:0",
						"3:1",
						"3:2",
						"4:0",
						"4:1",
						"4:2",
						"5:0",
						"5:1",
						"5:2",
						App.getInstance().getResources()
						.getString(R.string.basketballlottery_sqt),
						"0:0",
						"1:1",
						"2:2",
						"3:3",
						App.getInstance().getResources()
						.getString(R.string.basketballlottery_pqt),
						"0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4",
						"2:4", "0:5", "1:5", "2:5",App.getInstance().getResources()
						.getString(R.string.basketballlottery_fqt) }));
		mMatchResult.put(
				L205,
				new MatchFootballState(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7,
						8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
						22, 23, 24 }, new String[] {
						App.getInstance().getResources()
								.getString(R.string.basketballlottery_sqt),
						"1:0",
						"2:0",
						"2:1",
						"3:0",
						"3:1",
						"3:2",
						"4:0",
						"4:1",
						"4:2",
						App.getInstance().getResources()
								.getString(R.string.basketballlottery_pqt),
						"0:0",
						"1:1",
						"2:2",
						"3:3",
						App.getInstance().getResources()
								.getString(R.string.basketballlottery_fqt),
						"0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4",
						"2:4" }));

		mMatchResult.put(L303, new MatchFootballState(new Integer[] { 0, 1, 2,
				3, 4, 5, 6, 7 }, new String[] { "0", "1", "2", "3", "4", "5",
				"6", "7" }));
		mMatchResult.put(L304, new MatchFootballState(new Integer[] { 0, 1, 2,
				3, 4, 5, 6, 7, 8 }, App.getInstance().getResources()
				.getStringArray(R.array.football_state_array)));
		addMapData(chuan2num, new String[] { "01", "02", "03", "04", "05",
				"06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
				"26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
				"36", "37", "38", "39", "40" }, App.getInstance()
				.getResources()
				.getStringArray(R.array.football_state_chuan2num));
		addMapData(
				mChuan1Num,
				new String[] { "02", "03", "06", "11", "18", "28", "34" },
				App.getInstance().getResources()
						.getStringArray(R.array.football_state_chuan1num));
		// addMapData(mChuan1Num_Dcc, new String[] { "21", "22", "24", "27",
		// "31",
		// "36", "42", "43" }, App.getInstance().getResources()
		// .getStringArray(R.array.football_state_chuan1num_dcc));
		addMapData(mChuanValue2chuan, App.getInstance().getResources()
				.getStringArray(R.array.football_state_chuan1num),
				new String[] { "2*1", "3*1", "4*1", "5*1", "6*1", "7*1", "8*1" });
		
		addMapData(mChuanValue2key, App.getInstance().getResources()
				.getStringArray(R.array.football_state_chuan1num),
				new String[] { "02", "03", "06", "11", "18", "28", "34" });
		
		// 串与seleId 对应关系
		addMapData(mChuanValue2SeleId, App.getInstance().getResources()
				.getStringArray(R.array.football_state_chuan1num),
				new String[] { "02", "03", "06", "11", "18", "28", "34" });
		// addMapData(mChuanValue2SeleId_Dcc, App.getInstance().getResources()
		// .getStringArray(R.array.football_state_chuan1num_dcc),
		// new String[] { "21", "22", "24", "27", "31", "36", "42", "43" });
		addMapData(mPassTypeNum, App.getInstance().getResources()
				.getStringArray(R.array.football_state_chuan2num),
				new Integer[][] { { 2 }, { 3 }, { 4 }, { 5 }, { 6 }, { 7 },
						{ 8 }, { 2 }, { 2, 3 }, { 3 }, { 3, 4 }, { 2 },
						{ 2, 3, 4 }, { 4 }, { 4, 5 }, { 2 }, { 3, 4, 5 },
						{ 2, 3 }, { 2, 3, 4, 5 }, { 5 }, { 5, 6 }, { 2 },
						{ 3 }, { 4, 5, 6 }, { 2, 3 }, { 3, 4, 5, 6 },
						{ 2, 3, 4 }, { 2, 3, 4, 5, 6 }, { 6 }, { 6, 7 }, { 5 },
						{ 4 }, { 2, 3, 4, 5, 6, 7 }, { 7 }, { 7, 8 }, { 6 },
						{ 5 }, { 4 }, { 2, 3, 4, 5, 6, 7, 8 } });

	}

	/**
	 * 混合过关使用
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-2-9
	 */
	public static Map<String, List<Integer>> getPassTypeNum() {
		return mPassTypeNum;
	}

	/**
	 * 获取当前比赛的所有结果
	 * 
	 * @Description:
	 * @param matchType
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-1-13
	 */
	public static MatchFootballState getMatchResult(Integer matchType) {
		return mMatchResult.get(matchType);
	}

	private static void addMapData(Map<String, String> map, Object[] keys,
			Object[] values) {
		if (map != null && keys != null && values != null
				&& keys.length == values.length) {
			for (int i = 0; i < keys.length; i++) {
				map.put(keys[i] + "", values[i] + "");
			}
		}
	}

	private static void addMapData(Map<String, List<Integer>> map,
			Object[] keys, Integer[][] values) {
		if (map != null && keys != null && values != null
				&& keys.length == values.length) {
			for (int i = 0; i < keys.length; i++) {
				List<Integer> value = new ArrayList<Integer>();
				for (int j = 0; j < values[i].length; j++) {
					value.add(values[i][j]);
				}
				map.put(keys[i] + "", value);
			}
		}
	}

	/**
	 * 返回N串1结果集
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-1-27
	 */
	public static Map<String, String> getmChuan1Num() {
		return mChuan1Num;
	}

	/**
	 * 返回单场彩N串1结果集
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-1-27
	 */
	public static Map<String, String> getmChuan1Num_Dcc() {
		return mChuan1Num_Dcc;
	}

	public static Map<String, String> getChuanValue2Key() {
		return mChuanValue2key;
	}
	
	public static Map<String, String> getChuanValue2Chuan() {
		return mChuanValue2chuan;
	}


	/**
	 * 串与seleid对应关系，用于投注时使用
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2016-1-28
	 */

	public static Map<String, String> getmChuanValue2SeleId() {
		return mChuanValue2SeleId;
	}

	/**
	 * 单场彩串与seleid对应关系，用于投注时使用
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2016-1-28
	 */

	public static Map<String, String> getmChuanValue2SeleId_Dcc() {
		return mChuanValue2SeleId_Dcc;
	}

	public static void setmChuanValue2SeleId(
			Map<String, String> mChuanValue2SeleId) {
		FootballLotteryConstants.mChuanValue2SeleId = mChuanValue2SeleId;
	}

	/**
	 * 获取篮球投注方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-2-12
	 */
	public static List<String[]> getBasketballLotteryWay() {
		return mBasketballMatchSelect;
	}

	/**
	 * 获取足球单关投注方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-2-12
	 */
	public static List<String[]> getFootballDGLotteryWay() {
		return mfootballDGMatchSelect;
	}

	/**
	 * 获取篮球单关投注方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-2-12
	 */
	public static List<String[]> getBsketballDGLotteryWay() {
		return mBasketballDGMatchSelect;
	}

	/**
	 * 获取足球投注方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-1-16
	 */
	public static List<String[]> getFootballLotteryWay() {
		return mFootballMatchSelect;
	}

	/**
	 * 获取单场彩投注方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-1-16
	 */
	public static List<String[]> getDccLotteryWay() {
		return mDccMatchSelect;
	}

	/**
	 * 获取胜负彩投注方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wozhongla.com
	 * @date:2015-1-16
	 */
	public static List<String[]> getSfcLotteryWay() {
		return mSfcMatchSelect;
	}

	public static List<String[]> getFootballLotteryDrawWay() {
		return mLotteryDrawFootballSelect;
	}

}
