package cn.com.cimgroup.protocol;

public enum Command {
	/** 投注操作请求 */
	LOBOBET,
	/** 用户信息查询请求 */
	LOBOUSERQUERY,
	/** 用户信息 */
	ACCOUNT,
	/** 信息查询请求 */
	INFOQUERY,
	/** 比分 */
	SCOREMATCH,
	/** 获得数字彩当前销售期次请求 */
	LOBOPERIOD,
	/** 获取数字彩开奖列表 */
	LOTTERYLIST,
	/** 账户明细 */
	LOBOACCOUNTDETAIL,
	/** 充值提现记录 */
	LOBOCHARGECASH,
	/** 投注记录 */
	LOBOTZLIST,
	/** 投注详情 */
	LOBOTZDETAIL,
	/** 投注追号详情 */
	LOBOTZZHUIDETAIL,
	/** 竞彩投注详情 */
	FOOTBALLDETAIL,
	/** 注册 */
	REGISTER,
	/** 登录 */
	LOGIN,
	/** 找回密码 */
	GETBACKPWD,
	/** 重设密码 */
	RESETPWD,
	/** 获取验证码 */
	GETCODE,
	/** 验证手机号 */
	VERIPHONE,
	/** 获取用户信息 */
	GETUSERINFO,
	/** 提现 */
	OUTCASH,
	/** 获取充值渠道列表 */
	GETRECHARGELIST,
	/** 完善用户资料 */
	BINDUSERINFO,
	/** 获取银行列表 */
	GETBANKLIST,
	/** 获取省市列表 */
	GETPROCITY,
	/** 绑定银行卡*/
	BINDBANK,
	/** 获取竞彩足球对阵信息 */
	GETFOOTBALLMATCHSINFO,
	/** 获取对阵时间期次 */
	GETMATCHTIME,
	/** 获取彩种期次列表 */
	GETPERIODLIST,
	/** 充值 */
	RECHARGE,
	/** 银联充值 */
	EASYLINKRECHARGE,
	/** 获取焦点比赛 */
	GETFOCUSMATCH,
	/** 极光push注册 */
	REGISTERPUSH,
	/** 比分-关注赛事 */
	WATCHMATCH,
	/** 比分-对阵列表 */
	GETSCORELIST,
	/** 比分-详情-赛事 */
	SCOREDETAILGAME,
	/** 比分-详情-分析 */
	SCOREDETAILANALYSIS,
	/** 比分-详情-亚赔 */
	SCOREDETAILASIA,
	/** 比分-详情-欧赔 */
	SCOREDETAILEUROPE,
	/** 比分-公司 */
	SCORECOMPANY,
	/** 比分-赔率 */
	SCOREODDS,
	/** 我的乐米 */
	MYLEMI,
	/** 投注 */
	BETTING,
	/** 现金购买红包乐米列表 */
	PRODUCT,
	/** 获取彩种期号 */
	GETLOTTERYNOS,
	/**获取商户信息AppId*/
	DOWEICHAT,
	/**签到列表获取*/
	NUM_SIGNLIST,
	/**消息推送获取／设置*/
	NUM_PUSHSET,
	/**未读站内信的数量**/
	NUM_NOTICORMESSAGE,
	/**分享**/
	NUM_SHARE,
	/**分享增加乐米**/
	NUM_SHAREADDLEMI,
	/**获取一场制胜系统匹配对阵**/
	NUM_ONEWINSYS
	
}
