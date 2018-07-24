package cn.com.cimgroup.util;

import java.util.HashMap;

public class PlayInfo {
	/**
	 * 玩法定义说明：共9位 <br>
	 * 中心类型 亿位：1、体彩 2、福彩<br>
	 * 彩种 万到千万位4位：例0101<br>
	 * 选号方式 千位：1、直选 2、组选 3、任选 4、包选<br>
	 * 其它 百位：0、不追加 1、追加 2、附加 <br>
	 * 竞技彩相关 十位：0、普通投注 1、冠亚军<br>
	 * 投注类型 个位：1、单式 2、复式 3、胆拖 4、和值
	 */
	// ************* 大乐透玩法 ***************************/
	public static final String DLT_DS = "101011001";// 单式
	public static final String DLT_FS = "101011002";// 复式
	public static final String DLT_DT = "101011003";// 胆拖
	public static final String DLT_FJ = "101011201";// 附加
	public static final String DLT_DSZJ = "101011101";// 单式追加
	public static final String DLT_FSZJ = "101011102";// 复式追加
	public static final String DLT_DTZJ = "101011103";// 胆拖追加

	// **************排列3玩法******************************/
	public static final String PL3_ZXDS = "102011001";// 直选单式
	public static final String PL3_ZXFS = "102011002";// 直选复式
	public static final String PL3_ZUX3DS = "102022001";// 组选3单式
	public static final String PL3_ZUX3FS = "102022002";// 组选3复式
	public static final String PL3_ZUX6DS = "102032001";// 组选6单式
	public static final String PL3_ZUX6FS = "102032002";// 组选6复式
	public static final String PL3_ZXHZ = "102041004";// 直选和值
	public static final String PL3_ZUXHZ = "102042004";// 组选和值
	// public static final int PL3_ZUX3HZ = 41080304;// 组选3和值
	// public static final int PL3_ZUX6HZ = 41080404;// 组选6和值
	// public static final int PL3_ZXBH = 202009;// 直选包号

	// **************排列5玩法******************************/
	public static final String PL5_DS = "103011001";// 单式
	public static final String PL5_FS = "103011002";// 复式

	// **************七星彩玩法******************************/
	public static final String QXC_DS = "104011001";// 单式
	public static final String QXC_FS = "104011002";// 复式

	// **************胜平负14玩法******************************/
	public static final String SPF_DS = "105011001";// 单式
	public static final String SPF_FS = "105011002";// 复式

	// **************胜平负任九玩法******************************/
	public static final String SPF9_DS = "105021001";// 单式
	public static final String SPF9_FS = "105021002";// 复式

	// **************4场进球玩法******************************/
	public static final String JQ4_DS = "105031001";// 单式
	public static final String JQ4_FS = "105031002";// 复式

	// **************6场半全场玩法******************************/
	public static final String BQC6_DS = "105041001";// 单式
	public static final String BQC6_FS = "105041002";// 复式

	// **************进球彩玩法******************************/
	public static final String JQC_DS = "105051001";// 单式
	public static final String JQC_FS = "105051002";// 复式

	// *************上海11选5玩法******************************************/
	/** 任选1 106013001 */
	public static final int SH11X5_RX1 = 106013001;

	/** 任选2 106023001 */
	public static final int SH11X5_RX2 = 106023001;

	/** 任选3 106033001 */
	public static final int SH11X5_RX3 = 106033001;

	/** 任选4 106043001 */
	public static final int SH11X5_RX4 = 106043001;

	/** 任选5 106053001 */
	public static final int SH11X5_RX5 = 106053001;

	/** 任选6 106063001 */
	public static final int SH11X5_RX6 = 106063001;

	/** 任选7 106073001 */
	public static final int SH11X5_RX7 = 106073001;

	/** 任选8 106083001 */
	public static final int SH11X5_RX8 = 106083001;

	/** 前2直选 106091001 */
	public static final int SH11X5_Q2ZX = 106091001;

	/** 前2直选复式 106091002 */
	public static final int SH11X5_Q2FS = 106091002;

	/** 前3直选 106101001 */
	public static final int SH11X5_Q3ZX = 106101001;

	/** 前3直选复式 106101002 */
	public static final int SH11X5_Q3FS = 106101002;

	/** 前2组选单式 106112001 */
	public static final int SH11X5_Q2ZXDS = 106112001;

	/** 前2组选复式 106112002 */
	public static final int SH11X5_Q2ZXFS = 106112002;

	/** 前3组选单式 106122001 */
	public static final int SH11X5_Q3ZXDS = 106122001;

	/** 前3组选复式 106122002 */
	public static final int SH11X5_Q3ZXFS = 106122002;

	/** 任选2复式 106133002 */
	public static final int SH11X5_RX2FS = 106133002;

	/** 任选3复式 106143002 */
	public static final int SH11X5_RX3FS = 106143002;

	/** 任选4复式 106153002 */
	public static final int SH11X5_RX4FS = 106153002;

	/** 任选5复式 106163002 */
	public static final int SH11X5_RX5FS = 106163002;

	/** 任选6复式 106173002 */
	public static final int SH11X5_RX6FS = 106173002;

	/** 任选7复式 106183002 */
	public static final int SH11X5_RX7FS = 106183002;

	/** 任选2胆拖 106193003 */
	public static final int SH11X5_RX2DT = 106193003;

	/** 任选3胆拖 106203003 */
	public static final int SH11X5_RX3DT = 106203003;

	/** 任选4胆拖 106213003 */
	public static final int SH11X5_RX4DT = 106213003;

	/** 任选5胆拖 106223003 */
	public static final int SH11X5_RX5DT = 106223003;

	/** 任选6胆拖 106233003 */
	public static final int SH11X5_RX6DT = 106233003;

	/** 任选7胆拖 106243003 */
	public static final int SH11X5_RX7DT = 106243003;

	/** 前2组选胆拖 106252003 */
	public static final int SH11X5_Q2ZXDT = 106252003;

	/** 前3组选胆拖 106203003 */
	public static final int SH11X5_Q3ZXDT = 106262003;

	// *****************************快乐扑克3玩法*****************************//
	public static final int KLPK3_THBX = 107014001; // 同花包选
	public static final int KLPK3_THSBX = 107024001; // 同花顺包选
	public static final int KLPK3_SZBX = 107034001; // 顺子包选
	public static final int KLPK3_BZBX = 107044001; // 豹子包选
	public static final int KLPK3_DZBX = 107054001; // 对子包选

	public static final int KLPK3_THDXDS = 107061001; // 同花单选-单式
	public static final int KLPK3_THSDXDS = 107071001; // 同花顺单选-单式
	public static final int KLPK3_SZDXDS = 107081001; // 顺子单选-单式
	public static final int KLPK3_BZDXDS = 107091001; // 豹子单选-单式
	public static final int KLPK3_DZDXDS = 107101001; // 对子单选-单式

	public static final int KLPK3_THDXFS = 107061002; // 同花单选-复式
	public static final int KLPK3_THSDXFS = 107071001; // 同花顺单选-复式
	public static final int KLPK3_SZDXFS = 107081002; // 顺子单选-复式
	public static final int KLPK3_BZDXFS = 107091002; // 豹子单选-复式
	public static final int KLPK3_DZDXFS = 107101002; // 对子单选-复式

	public static final int KLPK3_RX1DS = 107113001; // 任选一-单式
	public static final int KLPK3_RX2DS = 107123001; // 任选二-单式
	public static final int KLPK3_RX3DS = 107133001; // 任选三-单式
	public static final int KLPK3_RX4DS = 107143001; // 任选四-单式
	public static final int KLPK3_RX5DS = 107153001; // 任选五-单式
	public static final int KLPK3_RX6DS = 107163001; // 任选六-单式

	public static final int KLPK3_RX1FS = 107113002; // 任选一-复式
	public static final int KLPK3_RX2FS = 107123002; // 任选二-复式
	public static final int KLPK3_RX3FS = 107133002; // 任选三-复式
	public static final int KLPK3_RX4FS = 107143002; // 任选四-复式
	public static final int KLPK3_RX5FS = 107153002; // 任选五-复式
	public static final int KLPK3_RX6FS = 107163002; // 任选六-复式

	public static final int KLPK3_RX2DT = 107123003; // 任选二-胆拖
	public static final int KLPK3_RX3DT = 107133003; // 任选三-胆拖
	public static final int KLPK3_RX4DT = 107143003; // 任选四-胆拖
	public static final int KLPK3_RX5DT = 107153003; // 任选五-胆拖
	public static final int KLPK3_RX6DT = 107163003; // 任选六-胆拖

	// *************竞彩玩法******************************************/
	/** 胜平负过关108011001 */
	public static final String JC_ZQ_SPF = "108011001";

	/** 比分过关108021001 */
	public static final String JC_ZQ_BF = "108021001";

	/** 总进球过关108031001 */
	public static final String JC_ZQ_ZJQ = "108031001";

	/** 胜负半全过关108041001 */
	public static final String JC_ZQ_SFBQ = "108041001";

	/** 混合玩法108051001 */
	public static final String JC_ZQ_HH = "108051001";

	/** 让球胜平负过关108061001 */
	public static final String JC_ZQ_RQSPF = "108061001";
	
	/** 胜平负二选一108015001 */
	public static final String JC_ZQ_2X1 = "108151001";
	
	/** 胜平负一场致胜108161001*/
	public static final String JC_ZQ_1CZS = "108161001";



	/** 胜平负二选一胆拖108015001 */
	public static final String JC_ZQ_2X1_DT = "108151003";
	
	/** 胜平负一场致胜胆拖108161001*/
	public static final String JC_ZQ_1CZS_DT = "108161003";
	/** 比分过关胆拖108021003 */
	public static final String JC_ZQ_BF_DT = "108021003";

	/** 总进球过关胆拖108031003 */
	public static final String JC_ZQ_ZJQ_DT = "108031003";
	
	/** 混合玩法胆拖108051001 */
	public static final String JC_ZQ_HH_DT = "108051003";

	/** 半全场过关胆拖108041003 */
	public static final String JC_ZQ_SFBQ_DT = "108041003";
	
	
	// *************竞篮玩法******************************************/
	/** 胜负投注11201xx */
	public static final String JC_LQ_SF = "1120101";  //普通投注
	public static final String JC_LQ_SF_DT = "1120102";//胆拖投注

	/** 让分胜负投注11202xx */
	public static final String JC_LQ_XSF = "1120201";//普通投注
	public static final String JC_LQ_XSF_DT = "1120202";//胆拖投注

	/** 胜分差投注11203xx */
	public static final String JC_LQ_SFC = "1120301";//普通投注
	public static final String JC_LQ_SFC_DT = "1120302";//胆拖投注

	/** 大小分投注11204xx */
	public static final String JC_LQ_DXF = "1120401";//普通投注
	public static final String JC_LQ_DXF_DT = "1120402";//胆拖投注

	/** 混合投注11205xx */
	public static final String JC_LQ_HH = "1120501";//普通投注
	public static final String JC_LQ_HH_DT = "1120502";//胆拖投注


	

//	/** 胜平负单关108071001 */
//	public static final int JC_ZQ_SPFDG = 108071001;

//	/** 比分单关108081001 */
//	public static final int JC_ZQ_BFDG = 108081001;

//	/** 总进球单关108091001 */
//	public static final int JC_ZQ_ZJQDG = 108091001;

//	/** 胜负半全单关108101001 */
//	public static final int JC_ZQ_SFBQDG = 108101001;

//	/** 胜负混合单关108121001 */
//	public static final int JC_ZQ_FHDG = 108111001;

//	/** 让球胜平负单关108111001 */
//	public static final int JC_ZQ_RQSPFDG = 108121001;

	/** 世界杯冠军 108121001 */
	public static final int JC_ZQ_GYJ_SJB = 108121011;

	/** 欧冠冠军杯,合买和代购同一用一个 108131001 */
	public static final int JC_ZQ_GYJ_OZGJB = 108131011;

	/** 世界杯冠亚军 108141001 */
	public static final int JC_ZQ_GYJ_SJBGYJ = 108141011;

	// *************北单玩法******************************************/
	/** 北单胜平负109011001 */
	public static final int BJDC_ZQ_SPF = 109011001;

	/** 北单总进球109021001 */
	public static final int BJDC_ZQ_ZJQ = 109021001;

	/** 北单上下单双109031001 */
	public static final int BJDC_ZQ_SXDS = 109031001;

	/** 北单比分109041001 */
	public static final int BJDC_ZQ_BF = 109041001;

	/** 北单半全场109051001 */
	public static final int BJDC_ZQ_BQC = 109051001;

	/** 北单胜平负胆拖109011003 */
	public static final int BJDC_ZQ_SPF_DT = 109011003;

	/** 北单总进球胆拖109021003 */
	public static final int BJDC_ZQ_ZJQ_DT = 109021003;

	/** 北单上下单双胆拖109031003 */
	public static final int BJDC_ZQ_SXDS_DT = 109031003;

	/** 北单比分胆拖109041003 */
	public static final int BJDC_ZQ_BF_DT = 109041003;

	/** 北单半全场胆拖109051003 */
	public static final int BJDC_ZQ_BQC_DT = 109051003;
	
	
	// *************山东11运玩法******************************************/
	/** 任选1 106013001 */
	public static final int SD11X5_RX1 = 110013001;
	/** 任选2 110023001 */
	public static final int SD11X5_RX2 = 110023001;
	/** 任选3 110033001 */
	public static final int SD11X5_RX3 = 110033001;
	/** 任选4 110043001 */
	public static final int SD11X5_RX4 = 110043001;
	/** 任选5 110053001 */
	public static final int SD11X5_RX5 = 110053001;
	/** 任选6 110063001 */
	public static final int SD11X5_RX6 = 110063001;
	/** 任选7 110073001 */
	public static final int SD11X5_RX7 = 110073001;
	/** 任选8 110083001 */
	public static final int SD11X5_RX8 = 110083001;
	/** 前2直选 110091001 */
	public static final int SD11X5_Q2ZX = 110091001;
	/** 前2直选复式 110091002 */
	public static final int SD11X5_Q2FS = 110091002;
	/** 前3直选 106101001 */
	public static final int SD11X5_Q3ZX = 110101001;
	/** 前3直选复式 110101002 */
	public static final int SD11X5_Q3FS = 110101002;
	/** 前2组选单式 110112001 */
	public static final int SD11X5_Q2ZXDS = 110112001;
	/** 前2组选复式 110112002 */
	public static final int SD11X5_Q2ZXFS = 110112002;
	/** 前3组选单式 110122001 */
	public static final int SD11X5_Q3ZXDS = 110122001;
	/** 前3组选复式 110122002 */
	public static final int SD11X5_Q3ZXFS = 110122002;
	/** 任选2复式 110133002 */
	public static final int SD11X5_RX2FS = 110133002;
	/** 任选3复式 110143002 */
	public static final int SD11X5_RX3FS = 110143002;
	/** 任选4复式 110153002 */
	public static final int SD11X5_RX4FS = 110153002;
	/** 任选5复式 110163002 */
	public static final int SD11X5_RX5FS = 110163002;
	/** 任选6复式 110173002 */
	public static final int SD11X5_RX6FS = 110173002;
	/** 任选7复式 110183002 */
	public static final int SD11X5_RX7FS = 110183002;
	/** 任选2胆拖 110193003 */
	public static final int SD11X5_RX2DT = 110193003;
	/** 任选3胆拖 106203003 */
	public static final int SD11X5_RX3DT = 110203003;
	/** 任选4胆拖 110213003 */
	public static final int SD11X5_RX4DT = 110213003;
	/** 任选5胆拖 110223003 */
	public static final int SD11X5_RX5DT = 110223003;
	/** 任选6胆拖 110233003 */
	public static final int SD11X5_RX6DT = 110233003;
	/** 任选7胆拖 110243003 */
	public static final int SD11X5_RX7DT = 110243003;
	/** 前2组选胆拖 110252003 */
	public static final int SD11X5_Q2ZXDT = 110252003;
	/** 前3组选胆拖 110203003 */
	public static final int SD11X5_Q3ZXDT = 110262003;

}
