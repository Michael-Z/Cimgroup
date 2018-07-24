package cn.com.cimgroup;

import java.io.File;

/**
 * 存放全局应用的常量类
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public class GlobalConstants {

	/**个人中心 登陆情况下显示的tag*/
	public static int personTagIndex = 0;
	/**个人中心 登陆情况是否显示的是 现金模块*/
	public static boolean isShowLeMiFragment = true;
	/**个人中心 我的投注类型*/
	public static String personGameNo ="ALL";
	/**是否刷新个人中心各碎片的数据*/
	public static boolean isRefreshFragment = false;
	/**个人中心--我的投注 选项 -1 未修改，0：全部，1：代购，2：追号，3中奖*/
	public static int personEndIndex = -1;

	/** 设置是否开启微信支付，2.3期之后开启 */
	public static final boolean ISOPENWECHAT = false;
	/** 存储是否提交过max信息的shared地址 */
	public static final String PATH_SHARED_MAC = "path_shared_mac";
	public final static String WEBVIEW_URL = "";
	
	public static final String PATH_VERSION = "path_version";

	public static final String IS_BANNER = "is_banner";
	public static final String IS_INFORMATION = "is_information";

	public static final String INFORMATION_ID = "information_id";

	// 9600正式商户号 联动优势
	public final static String UMPAYID = "9600";

	// 支付宝所需参数
	// 商户PID
	public static final String PARTNER = "2088711187837832";
	// 商户收款账号
	public static final String SELLER = "innokeji@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKwi2fRZyQPnXub+jpOjZ/jpBY/ZM658jad/53elSrfp81vszfA4uJm5C0mZHd1anZ5R56GFAHTmHDpejyUODfoHF2HhSRl5gqSHFne6TIwW7mR7PCjSV8V41FOuNgbu8uyVv9mhw4xLYqV9s2UoWZ05m1/Ed9pYwzmctjv29W2XAgMBAAECgYBp67wgDsuRmmKcU1FaabeFw6WHgYi6uqvTwPGmftB+YPg9vXZvdgepl+LpM83bsnHAxPd6PUQtqpb+F8ePbpmMGUmPWk9CH9NKFh6u5mBrSeNkkAS+V/J4xj8fF7sOtF/aGQr8a+xKfzf7IJb273gQoxIOg2yBuK6yFrt3DoC6kQJBANR3oKAEPw9nqKCRCxoyMOPKRZtIWjf4ean29TR19/qMfmWFAxmj9wU1LsNF/LLvAY07yVnBjEmxF53kQcQ9IJ8CQQDPZ8RUCVMCRe+R4iUEtkfPipa+UxO8X7XknS5jvyq7Mz4fCEQdAD1mgwV2NCayJhRjVYQJOWLQ43TmH2GQg7gJAkA/Qb6Y5arAs2QJKiXcDsVvjHFc4hW6REg0ykoG7HiAKAQZxZg+sgqBimv5x4SM4YvcuMgh6PYTmp4lbCgIfQqRAkAtpdD4DfVPEsVffj5Xgyba7s2upeo6E3adElsbaP+mo+iQO+mB2IOrBDs3gNLjcq4aDsA4p4CwKN+Zpl56yALBAkAvhQp2jMaWSwWOpEsxSPTIQL6mkHagg42b5cVBIVTu0HZU9JE9Qk6bZcRdRZUJKyOUii/HGyUoxv65dwZLb9vc";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsItn0WckD517m/o6To2f46QWP2TOufI2nf+d3pUq36fNb7M3wOLiZuQtJmR3dWp2eUeehhQB05hw6Xo8lDg36Bxdh4UkZeYKkhxZ3ukyMFu5kezwo0lfFeNRTrjYG7vLslb/ZocOMS2KlfbNlKFmdOZtfxHfaWMM5nLY79vVtlwIDAQAB";
	// 商品名称
	public static final String subject = "谊诺（北京）传媒科技有限公司";
	// 商品详情
	public static final String body = "网银充值";
	// 消息
	public static final int SDK_PAY_FLAG = 1;
	// end

	// 易联支付广播
	public final static String BROADCAST_PAY_END = "com.cimgroup.broadcast";

	public final static String PKGTYPE = "cimgroup";

	// 版本更新的新增条件字段
	public final static String PKGTYPE_ZY = "android_zy";

	public final static String PLATFORM = "android";
	/** 渠道编号 */
	public final static String CHANNEL = "00190000020151";

	// /** 自营渠道编号 */
	// public final static String CHANNEL = "00000000020121";

	public final static String VERSION = "0151";

	// 最大可选择对阵个数
	public final static int MUSTNUM = 8;

	public final static int LOTTERY_MAX_PRICE = 1000000;

	/** 是否使用红包支付 */
	public final static boolean isRed = false;

	public static final int LOTTERY_DLT = 100;// 大乐透
	public static final int LOTTERY_PL3 = 110;// 排列3
	public static final int LOTTERY_PL5 = 120;// 排列5
	public static final int LOTTERY_QXC = 130;// 七星彩
	public static final int LOTTERY_11X5 = 140;// 11选5
	public static final int LOTTERY_JCZQ = 150;// 竞彩足球
	public static final int LOTTERY_JCLQ = 160;// 竞彩篮球
	public static final int LOTTERY_SF14 = 170;
	public static final int LOTTERY_SF9 = 180;
	public static final int LOTTERY_JQ4 = 190;
	public static final int LOTTERY_BQ6 = 191;

	public static final int LOTTERY_DLT_PT = 200;// 大乐透普通
	public static final int LOTTERY_DLT_DT = 210;// 大乐透胆拖

	public static final int LOTTERY_DLT_JX = 220;// 大乐透机选
	public static final int LOTTERY_QXC_CART = 230;// 七星彩
	public static final int LOTTERY_P5_CART = 240;// p5

	public static final int LOTTERY_PL3_ZX = 250;// 排列3直选
	public static final int LOTTERY_PL3_ZXHZ = 251;// 排列3组选和值
	public static final int LOTTERY_PL3_Z3 = 252;// 排列3组三
	public static final int LOTTERY_PL3_Z6 = 255;// 排列3组六
	public static final int LOTTERY_PL3_ZHIXHZ = 256;// 排列3直选和值

	/** 投注记录全部彩种 */
	public static final int TAG_TZLIST_ALL = 400;
	/** 投注记录竞彩篮球 */
	public static final int TAG_TZLIST_BASKETBALL = 401;
	/** 投注记录竞彩足球 */
	public static final int TAG_TZLIST_FOOTBALL = 402;
	/** 投注记录胜负彩 */
	public static final int TAG_TZLIST_SFC = 403;
	/** 投注记录任九场 */
	public static final int TAG_TZLIST_R9 = 404;
	/** 投注记录进球数 */
	public static final int TAG_TZLIST_JQS = 405;
	/** 投注记录半全场 */
	public static final int TAG_TZLIST_BQC = 406;
	/** 投注记录大乐透 */
	public static final int TAG_TZLIST_DLT = 407;
	/** 投注记录排三 */
	public static final int TAG_TZLIST_P3 = 408;
	/** 投注记录七星彩 */
	public static final int TAG_TZLIST_QXC = 409;
	/** 投注记录排五 */
	public static final int TAG_TZLIST_P5 = 410;
	/** 投注记录11运 */
	public static final int TAG_TZLIST_11Y = 411;
	/** 投注记录11选5 */
	public static final int TAG_TZLIST_11X5 = 412;

	public static final int MSG_NOTIFY_SWITCH_WAY_DLT = 1000;
	public static final int MSG_NOTIFY_SWITCH_WAY_PL3 = 1010;
	public static final int MSG_NOTIFY_SWITCH_WAY_PL5 = 1020;
	public static final int MSG_NOTIFY_SWITCH_WAY_QXC = 1030;
	public static final int MSG_NOTIFY_SWITCH_WAY_TZLIST = 1040;
	public static final int MSG_NOTIFY_SWITCH_WAY_SCORELIST = 1050;

	public static final int MSG_NOTIFY_SWITCH_COMMON = 1000;

	public static final int MSG_NOTIFY_SWITCH_PLAY_MENU = 1100;

	public static final int MSG_NOTIFY_SWITCH_HALL_BEI = 1200;

	public static final int MSG_NOTIFY_SWITCH_JX_NUM = 1300;

	public static final int MSG_NOTIFY_DLT_JX = 1500;

	public static final int MSG_NOTIFY_SWITCH_SHARE = 1600;

	// 清空竞彩选中状态
	public final static int CLEARLATTERYSELECT = 1000;

	// 自选号码启动activity的消息id
	public static final int CART_ZX_RESULT = 5000;
	// 购物车点击item对应的消息id
	public static final int CART_ITEM_RESULT = 5050;
	// 支付页面跳转支付成功页面的消息id
	public static final int COMMIT_PAY = 6000;

	// 是否从购物车返回
	public static final String CART_IS_FORRESULT = "isCartForResult";
	public static final String CART_IS_ITEM_FORRESULT = "isCartItemForResult";

	// 大乐透
	public static final String LOTTERY_TYPE = "lottery_type";
	public static final String LIST_REDBALL = "list_redball";
	public static final String LIST_BULEBALL = "list_buleball";
	public static final String LIST_REDBALL_DT = "list_redball_dt";
	public static final String LIST_BULEBALL_DT = "list_buleball_dt";
	public static final String LIST_JXNUM = "list_jxnum";
	public static final String LOTTERY_P3_TYPE = "lottery_p3_type";
	public static final String LOTTERY_ISSUE = "lottery_issue";
	public static final String LOTTERY_ISSUES = "lottery_issues";
	public static final String LOTTERY_ISSUE_TIME = "lottery_issue_time";

	public static final String BALL_LIST1 = "ball_list1";
	public static final String BALL_LIST2 = "ball_list2";
	public static final String BALL_LIST3 = "ball_list3";
	public static final String BALL_LIST4 = "ball_list4";
	public static final String BALL_LIST5 = "ball_list5";
	public static final String BALL_LIST6 = "ball_list6";
	public static final String BALL_LIST7 = "ball_list7";

	public static final String AWARD_PUSH = "AWARD_PUSH";
	public static final String CHASE_STOP = "CHASE_STOP";
	public static final String WATCH_MATCH = "WATCH_MATCH";

	// 获得当前销售期次
	/** 竞彩足球 **/
	public static final String TC_JCZQ = "TC_JCZQ";
	/** 竞彩足球-胜平负 **/
	public static final String TC_JZSPF = "TC_JZSPF";
	/** 竞彩足球-让胜平负 **/
	public static final String TC_JZXSPF = "TC_JZXSPF";
	/** 竞彩足球-比分 **/
	public static final String TC_JZBF = "TC_JZBF";
	/** 竞彩足球-半全场 **/
	public static final String TC_JZBQC = "TC_JZBQC";
	/** 竞彩足球-进球数 **/
	public static final String TC_JZJQS = "TC_JZJQS";
	/** 竞彩足球-二选一 **/
	public static final String TC_2X1 = "TC_2X1";
	/** 竞彩足球-一场致胜 **/
	public static final String TC_1CZS = "TC_1CZS";

	/** 胜负彩 **/
	public static final String TC_SF14 = "TC_SF14";
	/** 任九场 **/
	public static final String TC_SF9 = "TC_SF9";
	/** 半全场 **/
	public static final String TC_BQ6 = "TC_BQ6";
	/** 进球数 **/
	public static final String TC_JQ4 = "TC_JQ4";

	/** 竞彩篮球 **/
	public static final String TC_JCLQ = "TC_JCLQ";
	/** 竞彩篮球-让分胜平负 **/
	public static final String TC_JLXSF = "TC_JLXSF";
	/** 竞彩篮球-胜平负 **/
	public static final String TC_JLSF = "TC_JLSF";
	/** 竞彩篮球-大小分 **/
	public static final String TC_JLDXF = "TC_JLDXF";
	/** 竞彩篮球-胜分差 **/
	public static final String TC_JLSFC = "TC_JLSFC";

	/** 北京单场 **/
	public static final String TC_BJDC = "TC_BJDC";
	/** 北京单场胜负过关 **/
	public static final String TC_BJSF = "TC_BJSF";
	/** 体彩大乐透 */
	public static final String TC_DLT = "TC_DLT";// 数字
	/** 排3 */
	public static final String TC_PL3 = "TC_PL3";// 数字
	/** 排5 */
	public static final String TC_PL5 = "TC_PL5";// 数字
	/** 体彩七星彩 */
	public static final String TC_QXC = "TC_QXC";// 数字
	/** 11运 */
	public static final String TC_11Y = "TC_SD11X5";
	/** 11选5 */
	public static final String TC_11X5 = "TC_SH11X5";

	/** 老足彩 */
	public static final String TC_ZQSPF = "TC_ZQSPF";
	
	public static String NUM_LOTTERY_FILE_NAME = "dlt_save";
	
	/** 本地保存JSESSIONID */
	public static String JSESSIONID = "";

	// transactionType值，用于区分请求了不同的服务
	public static final String NUM_COMMON = "1010";
	/** 账户明细 */
	public static final String NUM_ACCOUNTLIST = NUM_COMMON + "3009";
	/** 充值记录 */
	public static final String NUM_RECHARGELIST = NUM_COMMON + "3010";
	/** 提现记录 */
	public static final String NUM_CASH = NUM_COMMON + "3011";
	/** 投注记录-代购 */
	public static final String NUM_TZDG = NUM_COMMON + "3002";
	/** 投注记录-全部、中奖 */
	public static final String NUM_ALL = NUM_COMMON + "3015";
	/** 投注记录-追号 */
	public static final String NUM_ZHUI = NUM_COMMON + "3003";
	/** 投注记录-代购详情 */
	public static final String NUM_TZDGDETAIL = NUM_COMMON + "3005";
	/** 投注记录-追号详情 */
	public static final String NUM_ZHUIDETAIL = NUM_COMMON + "3007";
	/** 投注记录-竞彩投注详情 */
	public static final String NUM_FOOTBALLDETAIL = NUM_COMMON + "3028";
	/** 注册 */
	public static final String NUM_REGISTER = NUM_COMMON + "1001";
	/** 登录 */
	public static final String NUM_LOGIN = NUM_COMMON + "1002";
	/** 重设密码 */
	public static final String NUM_RESET = NUM_COMMON + "1010";
	/** 找回密码 */
	public static final String NUM_GETBACKPWD = NUM_COMMON + "1025";

	/** 验证用户登录名 */
	public static final String NUM_VERIUSERNAME = NUM_COMMON + "1006";
	/** 发送验证码 */
	public static final String NUM_SENDCODE = NUM_COMMON + "5017";
	/** 获取用户信息 */
	public static final String NUM_GETUSERINFO = NUM_COMMON + "1004";
	/** 提现 */
	public static final String NUM_OUTCASH = NUM_COMMON + "4002";
	/** 获取充值渠道列表 */
	public static final String NUM_RECHARGECHANNEL = NUM_COMMON + "5026";
	/** 用户信息更新 */
	// public static final String NUM_BIND = NUM_COMMON + "1003";
	/** 获取银行列表 */
	public static final String NUM_BANKLIST = NUM_COMMON + "5022";
	/** 获取省市列表 */
	public static final String NUM_PROCITY = NUM_COMMON + "5023";
	/** 获取竞彩足球对阵信息 */
	public static final String NUM_FOOTBALLMATCHSINFO = NUM_COMMON + "3017";
	/** 获取对阵时间期次 */
	public static final String NUM_MATCHTIME = NUM_COMMON + "3018";
	/** 充值 */
	public static final String NUM_RECHARGE = NUM_COMMON + "4001";
	/** 银联充值 */
	public static final String NUM_EASYLINK = NUM_COMMON + "7004";
	/** 完善用户资料 */
	public static final String NUM_BINDUSERINFO = NUM_COMMON + "1012";
	/** 绑定银行卡 */
	public static final String NUM_BINDBANK = NUM_COMMON + "1013";
	/** 银行卡类型 */
	public static final String NUM_BANKTYPE = NUM_COMMON + "7005";
	/** 获取焦点比赛 */
	public static final String NUM_FOCUSMATCH = NUM_COMMON + "3029";
	/** 极光push注册 */
	public static final String NUM_JPUSH = NUM_COMMON + "1011";
	/** 消息列表 */
	public static final String NUM_MESSAGE = NUM_COMMON + "1022";
	/** 查询乐米明细记录 */
	public static final String NUM_LEMI = NUM_COMMON + "3023";
	/** 购买红包,乐米列表 */
	public static final String NUM_PRODUCT = NUM_COMMON + "1015";
	/** 购买红包,乐米 */
	public static final String NUM_BUY = NUM_COMMON + "1016";
	/** 乐米兑换 */
	public static final String NUM_CONVERT = NUM_COMMON + "1017";
	/** 乐米兑换商品 */
	public static final String NUM_CONVERT_PRODUCT = NUM_COMMON + "1018";
	/** 成功兑换的商品列表 */
	public static final String NUM_CONVERT_LIST = NUM_COMMON + "1019";
	/** 可用红包列表 */
	public static final String NUM_REDPKG_USED = NUM_COMMON + "1020";
	/** 不可用红包列表 */
	public static final String NUM_REDPKG_NOUSED = NUM_COMMON + "1021";
	/** 站内信更新状态 */
	public static final String NUM_MESSAGE_STATE = NUM_COMMON + "1023";
	/** 获得数字彩当前销售期次请求 */
	public static final String NUM_LOTTERY_PRE = NUM_COMMON + "2001";
	/** 获取数字彩开奖列表 */
	public static final String NUM_LOTTERY_LIST = NUM_COMMON + "5027";

	public static final String NUM_LOTTERY_ZHUI_LIST = NUM_COMMON + "5025";

	public static final String NUM_RED_PKG = NUM_COMMON + "3032";

	/** 比分-对阵-关注比赛 */
	public static final String NUM_WATCHMATCH = NUM_COMMON + "1014";
	/** 比分-对阵列表 */
	public static final String NUM_SCORELIST = NUM_COMMON + "8001";
	/** 比分-详情-赛事 */
	public static final String NUM_SCOREDETAILGAME = NUM_COMMON + "8002";
	/** 比分-详情-分析 */
	public static final String NUM_SCOREDETAILANALYSIS = NUM_COMMON + "8006";
	/** 比分-详情-亚赔 */
	public static final String NUM_SCOREDETAIASIA = NUM_COMMON + "8003";
	/** 比分-详情-欧赔 */
	public static final String NUM_SCOREDETEUROPE = NUM_COMMON + "8004";
	/** 比分-详情 */
	public static final String NUM_SCOREDETAIL = NUM_COMMON + "8032";
	/** 比分-赔率 */
	public static final String NUM_SCOREODDS = NUM_COMMON + "8019";

	/** 比分-期号列表 */
	public static final String NUM_SCORELISTTIME = NUM_COMMON + "8018";

	/** 比分-公司 */
	public static final String NUM_SCORECOMPANY = NUM_COMMON + "8021";
	/** 投注 */
	public static final String NUM_BETTING = NUM_COMMON + "2002";
	/** 首页开奖列表 */
	public static final String NUM_HALLLOTTERYDRAW = NUM_COMMON + "5028";
	/** 开奖列表 */
	public static final String NUM_LOTTERYDRAW = NUM_COMMON + "5027";
	/** 开奖详情 */
	public static final String NUM_LOTTERYDRAWINFO = NUM_COMMON + "5002";
	/** 竞彩足球开奖列表 */
	public static final String NUM_LOTTERYDRAWFOOTBALLLIST = NUM_COMMON
			+ "5015";
	/** 头部广告 */
	public static final String NUM_HALLAD = NUM_COMMON + "8101";
	/** 弹出公告 */
	public static final String NUM_HALLNOTICE = NUM_COMMON + "8102";
	/** 软件版本升级 */
	public static final String NUM_UPGRADE = NUM_COMMON + "8103";
	/** 彩种列表控制 */
	public static final String NUM_CONTROLLLOTTERY = NUM_COMMON + "8104";
	/** 签到列表 */
	public static final String NUM_SIGNLIST = NUM_COMMON + "3025";
	/** 签到 */
	public static final String NUM_SIGN = NUM_COMMON + "3026";

	public static final String NUM_PUSHSET = NUM_COMMON + "1030";
	/** 用户未读站内信的数量 **/
	public static final String NUM_NOTICORMESSAGE = NUM_COMMON + "1032";

	/** 用户未读站内信的数量 **/
	public static final String NUM_ONEWINSYS = NUM_COMMON + "3034";
	
	/** 首页轮播文字 **/
	public static final String NUM_HALLTEXT = "10110014";

	/** 验证码是否正确（忘记密码处）**/
	public static final String VALIDATE_CODE = NUM_COMMON + "1024";
	
	//针对极光推送的数据保存
	public static String JPUSHEXTRA = "";
	public static String JPUSHALERT = "";

	//是否需要显示首次启动轮播引导页
	public final static Boolean isNeedOpenInfo = true;
	
	// true 访问测试服务器，false 访问正式服务器
	public final static Boolean isTest = false;
	// 192.168.2.181:18082测试服务器
	// 192.168.2.171:16669测试服务器
	// 测试用户名：testuser3
	// 测试密码 ：111111
	// userid:17
	// http://172.24.1.150:18080/
	// http://c.lebocp.com/正式服务器
	// 正式用户名：wodesky2003/123456
	// 正式用户名：testuser/111111

	/** 网络请求 **/
	private static String URL_PATH = null;//

	private static String URL_API_PATH = null;
	// 遗漏接口
	@SuppressWarnings("unused")
	private static String URL_ANALYSIS_PATH = null;

	public static String getUrlPath() {
		if (isTest) {
			URL_PATH = "http://192.168.2.181:18082/";
			// URL_PATH = "http://192.168.2.171:16669/";
		} else {
			URL_PATH = "http://c.lebocp.com/";
			// URL_PATH = "http://172.24.1.150:18080/";
		}
		return URL_PATH;
	}

	public static String getUrlApiPath() {
		if (isTest) {
			// URL_API_PATH = "http://192.168.2.181:18082/";
			URL_API_PATH = "http://192.168.2.171:16669/";
		} else {
			URL_API_PATH = "http://c.lebocp.com/";
			// URL_API_PATH = "http://172.24.1.150:18080/";
		}
		return URL_API_PATH;
	}


	/**
	 * 获取走势图网址:0.大乐透、1，排列三、2，排列五、3，七星彩
	 * 
	 * @param type
	 * @return
	 */
	public static String getZSTUrl(int type) {
		String basUrl = "";
		if (isTest)
			basUrl = "http://192.168.2.171:7777/";
		else
			basUrl = "http://m.lebocp.com/";
		switch (type) {
		case 0:
			basUrl+="trend/chart/H5_DLT_JBZS.html";
			break;
		case 1:
			basUrl+="trend/chart/H5_PL3_JBZS.html";
			break;
		case 2:
			basUrl+="trend/chart/H5_PL5_JBZS.html";
			break;
		case 3:
			basUrl+="trend/chart/H5_QXC_JBZS.html";
			break;

		default:
			basUrl+="other/info.html";
			break;
		}

		return basUrl;
	}

	/** h5缓存地址 **/
	public static final String H5_PATH = App.getInstance().getCacheDir()
			.getPath()
			+ File.separator;

	public static final String H5_FIRST_LEVE_NAME = "h5";
	/** h5首级目录 **/
	public static final String H5_FIRST_LEVE_NAME_PATH = H5_PATH
			+ H5_FIRST_LEVE_NAME + File.separator;
	/** assets 目录下 zip名称 **/
	public static final String H5_ASSETS_NAME = "h5.zip";

	/** 微信登陆-获取商户信息（appid） */
	public static final String URL_WECHAT_GET_APPID = NUM_COMMON + "1026";
	/** 微信登陆 -验证用户信息 */
	public static final String URL_WECHAT_LOAD = NUM_COMMON + "1027";
	/** 微信登陆-验证绑定用户 */
	public static final String URL_WECHAT_BIND_PHONE = NUM_COMMON + "1028";
	/** 微信登陆-注册新用户 */
	public static final String URL_REGIST_WECHAT_USER = NUM_COMMON + "1029";
	/** 微信充值Recharge */
	public static final String WECHAT_RECHARGE = NUM_COMMON + "7007";

	/** 用户投注下单并支付 */
	public static final String URL_CREATE_ORDER = NUM_COMMON + "2014";
	/** 订单二次支付 */
	public static final String URL_ORDER_AGAIN_PAY = NUM_COMMON + "2015";
	/** 发送渠道激活信息 */
	public static final String URL_SEND_MAC = NUM_COMMON + "1031";

	/** 获取游戏列表 */
	public static final String URL_GET_GAME_LIST = "10110001";

	/** 分享 **/
	public static final String NUM_SHARE = "10110013";
	/** 分享增加乐米 **/
	public static final String NUM_SHAREADDLEMI = "10110011";
	/** 娱乐场共用投注 10110002 */
	public static final String URL_GAME_BETTING = "10110002";
	/** 娱乐场足球竞猜用户周投注胜率接口*/
	public static final String URL_GET_GUESS_MATCH_WINNING = "10110003";
	/** 发布一条竞猜比赛的评论 */
	public static final String URL_SEND_GUESS_MATCH_MESSAGE = "10110004";
	/** 获取竞猜游戏比赛评论 */
	public static final String URL_GET_GUESS_MATCH_MESSAGE = "10110005";
	/** 获取竞猜游戏信息 */
	public static final String URL_GET_GUESS_MATCH_INFO = "10110006";
	/** 我的竞猜记录 **/
	public static final String URL_GET_GUESS_RECORD = "10110007";
	/** 我的当前排行 **/
	public static final String URL_GET_MY_RANKING_LIST_C = "10110008";
	/** 周排行期次 **/
	public static final String URL_GET_RANKING_LIST_DATE = "10110009";
	/** 周排行 **/
	public static final String URL_GET_MY_RANKING_LIST_W = "10110010";

	/** 获取七牛请求参数 */
	public static final String URL_GET_QINIU_PARAMS = "10106001";
	/** 图片更新后更新服务器数据 */
	public static final String URL_UPDATE_USER_HEADER_IMAGE = "10106002";
	/** 图片更新后更新服务器数据 */
	public static final String URL_GET_LEAGUE_DETAILS = "10108031";

}
