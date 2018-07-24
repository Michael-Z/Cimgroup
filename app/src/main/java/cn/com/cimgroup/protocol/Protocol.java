package cn.com.cimgroup.protocol;

import android.content.Intent;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.bean.AccountDetailList;
import cn.com.cimgroup.bean.AllBettingList;
import cn.com.cimgroup.bean.AppIdBean;
import cn.com.cimgroup.bean.BankList;
import cn.com.cimgroup.bean.BankType;
import cn.com.cimgroup.bean.Betting;
import cn.com.cimgroup.bean.ControllLottery;
import cn.com.cimgroup.bean.EasyLinkReCharge;
import cn.com.cimgroup.bean.FocusMatch;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.FootballDetailList;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.HallAd;
import cn.com.cimgroup.bean.HallNotice;
import cn.com.cimgroup.bean.HallTextList;
import cn.com.cimgroup.bean.IssueList;
import cn.com.cimgroup.bean.IssuePreList;
import cn.com.cimgroup.bean.LeMi;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryDrawBasketballList;
import cn.com.cimgroup.bean.LotteryDrawDetailInfo;
import cn.com.cimgroup.bean.LotteryDrawFootballList;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.LotteryList;
import cn.com.cimgroup.bean.LotteryNOs;
import cn.com.cimgroup.bean.Message;
import cn.com.cimgroup.bean.NoticeOrMessageCount;
import cn.com.cimgroup.bean.ProCity;
import cn.com.cimgroup.bean.PushResult;
import cn.com.cimgroup.bean.ReCharge;
import cn.com.cimgroup.bean.ReChargeChannel;
import cn.com.cimgroup.bean.RedPacketLeMiList;
import cn.com.cimgroup.bean.RedPkgDetail;
import cn.com.cimgroup.bean.RedPkgUsed;
import cn.com.cimgroup.bean.ResMatchAgainstInfo;
import cn.com.cimgroup.bean.ScoreCompanyOdds;
import cn.com.cimgroup.bean.ScoreDetailAnalysis;
import cn.com.cimgroup.bean.ScoreDetailGame;
import cn.com.cimgroup.bean.ScoreDetailOddsList;
import cn.com.cimgroup.bean.ScoreList;
import cn.com.cimgroup.bean.ScoreObj;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.SignList;
import cn.com.cimgroup.bean.SignStatus;
import cn.com.cimgroup.bean.TzDetail;
import cn.com.cimgroup.bean.Upgrade;
import cn.com.cimgroup.bean.UserCount;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.bean.ZhuiDetailList;
import cn.com.cimgroup.bean.lotteryDrawInfoList;
import cn.com.cimgroup.json.JSONObjectSort;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.JsonHelper;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 网络请求协议封装
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
@SuppressWarnings("unchecked")
public class Protocol {

	private static Protocol mProtocol;
	private Map<String, Object> params = new HashMap<String, Object>();

	private SharedPreferences shared = App.getInstance().getSharedPreferences(
			GlobalConstants.PATH_SHARED_MAC,
			android.content.Context.MODE_PRIVATE);

	/** 用户的登陆方式 **/
	private int loginPattern;
	/** 用户的openid 用于微信自动登录 **/
	private String openId;
	private Boolean auto;

	/**
	 * 单例模式会使params参数存在异步错误。现在这采用每次创建该对象，以后进行改进
	 * 
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public static Protocol getInstance() {
		// if (mProtocol == null) {
		mProtocol = new Protocol();
		// }
		return mProtocol;
	}

	/**
	 * 防止二次赋值
	 * 
	 * @Description:
	 * @param requestCommand
	 * @param command
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	private String getRequestCommand(String requestCommand, String command) {
		if (!StringUtil.isEmpty(requestCommand)) {
			return requestCommand;
		}
		return command;
	}

	/**
	 * 判断用户之前的登陆状态 1-微信登陆 2-自营登陆
	 * 
	 * @return
	 */
	public void initDatas() {

		loginPattern = shared.getInt("loginpattern", 0);
		openId = shared.getString("openid", "");
		auto = shared.getBoolean("auto", false);
	}

	@SuppressWarnings("incomplete-switch")
	private String getUrl(Command command) {
		// URL_PATH = "http://c.lebocp.com/";
		StringBuilder builder = new StringBuilder(GlobalConstants.getUrlPath());
		String publicUrlPashFrament = null;
		String requestCommand = null;
		switch (command) {
		case LOBOBET:
			// requestCommand = "bet";
			// break;
		case LOBOUSERQUERY:
			// requestCommand = "userQuery";
			// break;
		case ACCOUNT:
			// requestCommand = "account";
			// break;
		case INFOQUERY:
			// requestCommand = "infoQuery";
			// break;
		case SCOREMATCH:
		case GETLOTTERYNOS:
		case DOWEICHAT:
			// requestCommand = "scoreMatch";
			requestCommand = "api";
			break;

		}
		if (!StringUtil.isEmpty(publicUrlPashFrament)) {
			builder.append(publicUrlPashFrament);
		}
		if (!StringUtil.isEmpty(requestCommand)) {
			builder.append(requestCommand);
		}
		return builder.toString();
	}

	/**
	 * 获得当前销售期次请求
	 * 
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月29日
	 */
	public List<LoBoPeriodInfo> getLoBoPeriod(String num, String gameNo)
			throws CException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("gameNo", gameNo);
		params.put("transactionType", num);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.LOBOPERIOD, response.toJson());
		}

		if (data == null) {
			data = new ArrayList<LoBoPeriodInfo>();
		}
		return (List<LoBoPeriodInfo>) data;
	}

	public IssuePreList getLoBoZhuiPeriod(String num, String lottery,
			String issue) throws CException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("transactionType", num);
		params.put("lottery", lottery);
		params.put("issue", issue);
		params.put("num", "50");
		Response response = Request.post(url, params);
		IssuePreList list = new IssuePreList();
		if (response != null) {
			System.out.println("--------------------"+response.toJson());
			list = JSON.parseObject(response.toJson().toString(),
					IssuePreList.class);
		}

		if (list == null) {
			list = new IssuePreList();
		}
		return list;
	}

	/**
	 * 获取数字彩开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param lotteryNo
	 * @param pageNo
	 * @param pageAmount
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年1月18日
	 */
	public LotteryList getLotteryList(String num, String lotteryNo,
			String pageNo, String pageAmount) throws CException {
		String url = getUrl(Command.INFOQUERY);
		params.clear();
		params.put("lotteryNo", lotteryNo);
		params.put("transactionType", num);
		params.put("pageNo", pageNo);
		params.put("pageAmount", pageAmount);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.LOTTERYLIST, response.toJson());
		}

		if (data == null) {
			data = new LotteryList();
		}
		// Object data = Parser.parse(Command.LOTTERYLIST, response.toJson());
		return (LotteryList) data;
	}

	/**
	 * 账户明细
	 * 
	 * @param num
	 * @param oprType
	 * @param pageAmount
	 * @param pageNo
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月30日
	 */
	public AccountDetailList getLoBoAccountDetail(String num, String userid,
			String oprType, String pageAmount, String pageNo) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("userId", userid);
		if (!oprType.equals("-1") && !oprType.equals("-2")) {
			params.put("oprType", oprType);
		}
		params.put("transactionType", num);
		params.put("pageAmount", pageAmount);
		params.put("pageNo", pageNo);
		params.put("startTime", "");
		params.put("endTime", "");
		Response response = Request.post(url, params);
		AccountDetailList data = null;
		if (response != null) {
			if (!oprType.equals("-2")) {
				data = (AccountDetailList) Parser.parse(
						Command.LOBOACCOUNTDETAIL, response.toJson());
			} else {
				data = (AccountDetailList) Parser.parse(Command.LOBOCHARGECASH,
						response.toJson());
			}
		}
		if (data == null) {
			data = new AccountDetailList();
		}
		return data;
	}

	/**
	 * 获取投注记录
	 * 
	 * @param gameNo
	 * @param num
	 * @param userid
	 * @param pageAmount
	 * @param pageNo
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月31日
	 */
	public AllBettingList getLoBoTzList(String gameNo, String num,
			String userid, String onlyWin, String pageAmount, String pageNo)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("userId", userid);
		params.put("gameNo", gameNo);
		params.put("transactionType", num);
		params.put("pageAmount", pageAmount);
		params.put("pageNo", pageNo);
		params.put("beginTime", "");
		params.put("endTime", "");
		params.put("status", "");
		params.put("chaseStatus", "");
		params.put("onlyWin", onlyWin);
		params.put("accoutType", GlobalConstants.ISOPENWECHAT ? "1" : "0");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.LOBOTZLIST, response.toJson());
		}

		if (data == null) {
			data = new AllBettingList();
		}
		return ((AllBettingList) data);
	}

	/**
	 * 投注详情
	 * 
	 * @param orderId
	 * @param num
	 * @param userid
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	public TzDetail getLoBoTzDetail(String orderId, String num, String userid)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("userId", userid);
		params.put("orderId", orderId);
		params.put("transactionType", num);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.LOBOTZDETAIL, response.toJson());
		}

		if (data == null) {
			data = new TzDetail();
		}
		return (TzDetail) data;
	}

	/**
	 * 投注追号详情
	 * 
	 * @Description:
	 * @param chaseId
	 * @param num
	 * @param userid
	 * @param pageNo
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年1月27日
	 */
	public ZhuiDetailList getLoBoTzZhuiDetail(String chaseId, String num,
			String userid, String pageNo) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("userId", userid);
		params.put("chaseId", chaseId);
		params.put("transactionType", num);
		params.put("pageNo", pageNo);
		params.put("pageAmount", "100");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.LOBOTZZHUIDETAIL, response.toJson());
		}

		if (data == null) {
			data = new ZhuiDetailList();
		}

		return (ZhuiDetailList) data;
	}

	/**
	 * 竞彩投注详情
	 * 
	 * @Description:
	 * @param orderId
	 * @param num
	 * @param userid
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年2月18日
	 */
	public FootballDetailList getFootballDetail(String orderId, String num,
			String userid, String gameNo) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("userId", userid);
		params.put("orderId", orderId);
		params.put("transactionType", num);
		params.put("gameNo", gameNo);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.FOOTBALLDETAIL, response.toJson());
		}

		if (data == null) {
			data = new FootballDetailList();
		}
		// Object data = Parser.parse(Command.FOOTBALLDETAIL,
		// response.toJson());
		return (FootballDetailList) data;
	}

	/**
	 * 注册
	 * 
	 * @param num
	 * @param userName
	 * @param mobile
	 * @param password
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public UserInfo register(String num, String userName, String mobile,
			String password, String code) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("userName", userName);
		params.put("mobile", mobile);
		params.put("transactionType", num);
		params.put("password", password);
		params.put("code", code);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.REGISTER, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.REGISTER, response.toJson());
		return (UserInfo) data;
	}

	/**
	 * 投注
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param bettingType
	 * @param gameNo
	 * @param issue
	 * @param multiple
	 * @param buyMoney
	 * @param isChase
	 * @param chase
	 * @param isRedPackage
	 * @param redPackageId
	 * @param redPackageMoney
	 * @param choiceType
	 * @param list
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @throws JSONException
	 * @date:2016年1月12日
	 */
	public Betting syncLotteryBetting(String num, String userId,
			String bettingType, String gameNo, String issue, String multiple,
			String buyMoney, String isChase, String chase, String isRedPackage,
			String redPackageId, String redPackageMoney, String choiceType,
			String planEndTime, String stopCondition, List<JSONObject> list,
			String redPkgType) throws CException, JSONException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("bettingType", bettingType);
		params.put("gameNo", gameNo);
		params.put("issue", issue);
		params.put("buyMoney", buyMoney);
		params.put("isChase", isChase);
		params.put("chase", chase);

		if (Integer.parseInt(chase) > 1) {
			StringBuilder sbMoney = new StringBuilder();
			for (int i = 0; i < Integer.parseInt(chase); i++) {
				sbMoney.append(
						Integer.parseInt(buyMoney) / Integer.parseInt(chase))
						.append("^");
			}
			sbMoney.deleteCharAt(sbMoney.length() - 1);
			params.put("planMoney", sbMoney.toString());

			StringBuilder sbMul = new StringBuilder();
			for (int i = 0; i < Integer.parseInt(chase); i++) {
				sbMul.append(multiple).append("^");
			}
			sbMul.deleteCharAt(sbMul.length() - 1);
			params.put("multiple", sbMul.toString());
		} else {
			params.put("multiple", multiple);
			params.put("planMoney", buyMoney);
		}

		params.put("isRedPackage", isRedPackage);
		params.put("redPackageId", redPackageId);
		params.put("redPackageMoney", redPackageMoney);
		params.put("choiceType", choiceType);
		params.put("planEndTime", planEndTime);
		params.put("list", new JSONArray(list));
		params.put("planTitle", "0");
		params.put("planDes", "");
		params.put("planMinLimit", "0");
		params.put("guaranteeMoney", "0");
		params.put("stopCondition", stopCondition);
		params.put("isCommision", "0");
		params.put("commisionRatio", "0");
		params.put("viewType", "0");
		params.put("isTop", "0");
		// params.put("lotteryType", "0");
		params.put("betPlat", "1");
		params.put("isOptimize", "0");
		params.put("redPkgType", redPkgType);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.BETTING, response.toJson());
		return (Betting) data;
	}

	@SuppressWarnings({ "rawtypes" })
	public JSONObjectSort parseMapConvertJson(Map map) throws Exception {
		JSONObjectSort json = new JSONObjectSort();
		if (!map.isEmpty()) {
			// 遍历map
			Set set = map.entrySet();
			Iterator i = set.iterator();
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				json.put(key, value);
			}

		}
		return json;
	}

	/**
	 * 登录
	 * 
	 * @param num
	 * @param userName
	 * @param password
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public UserInfo login(String num, String userName, String password)
			throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("userName", userName);
		params.put("transactionType", num);
		params.put("password", password);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.LOGIN, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.LOGIN, response.toJson());
		return (UserInfo) data;
	}

	/**
	 * 找回密码
	 * 
	 * @param num
	 * @param userId
	 * @param oldPwd
	 * @param password
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public UserInfo getReSetPwd(String num, String userId, String password,
			String oldPwd) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("userId", userId);
		params.put("transactionType", num);
		params.put("newPwd", password);
		params.put("oldPwd", oldPwd);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETBACKPWD, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.GETBACKPWD, response.toJson());
		return (UserInfo) data;
	}

	public UserInfo getBackPWD(String num, String userId, String cell,
			String validCode, String newPwd) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("userId", userId);
		params.put("transactionType", num);
		params.put("cell", cell);
		params.put("validCode", validCode);
		params.put("newPwd", newPwd);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETBACKPWD, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.GETBACKPWD, response.toJson());
		return (UserInfo) data;
	}

	/**
	 * 获取验证码
	 * 
	 * @param num
	 * @param mobile
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public GetCode getCode(String num, String mobile, String codeType)
			throws CException {
		String url = getUrl(Command.INFOQUERY);
		params.clear();
		params.put("mobile", mobile);
		params.put("codeType", codeType);
		params.put("transactionType", num);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETCODE, response.toJson());
		}

		if (data == null) {
			data = new GetCode();
		}
		// Object data = Parser.parse(Command.GETCODE, response.toJson());
		return (GetCode) data;
	}

	/**
	 * 验证手机号
	 * 
	 * @param num
	 * @param mobile
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月4日
	 */
	public GetCode veriPhone(String num, String mobile) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("mobile", mobile);
		params.put("transactionType", num);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.VERIPHONE, response.toJson());
		}

		if (data == null) {
			data = new GetCode();
		}
		// Object data = Parser.parse(Command.VERIPHONE, response.toJson());
		return (GetCode) data;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param num
	 * @param userId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月5日
	 */
	public UserInfo getUserInfo(String num, String userId) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("userId", userId);
		params.put("transactionType", num);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETUSERINFO, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.GETUSERINFO, response.toJson());
		return (UserInfo) data;
	}

	/**
	 * 提现
	 * 
	 * @param num
	 * @param userId
	 * @param amount
	 * @param isQuick
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public GetCode outCash(String num, String userId, String amount,
			String isQuick) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("userId", userId);
		params.put("transactionType", num);
		params.put("amount", amount);
		params.put("isQuick", isQuick);
		params.put("platform", "2");
		params.put("isMc", "1");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.OUTCASH, response.toJson());
		}

		if (data == null) {
			data = new GetCode();
		}
		// Object data = Parser.parse(Command.OUTCASH, response.toJson());
		return (GetCode) data;
	}

	/**
	 * 获取充值渠道列表
	 * 
	 * @param num
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月10日
	 */
	public ReChargeChannel getReChargeList(String num, String userId,
			String timeId) throws CException {
		String url = getUrl(Command.INFOQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("timeId", timeId);
		params.put("userId", userId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETRECHARGELIST, response.toJson());
		}

		if (data == null) {
			data = new ReChargeChannel();
		}
		// Object data = Parser.parse(Command.GETRECHARGELIST,
		// response.toJson());
		return (ReChargeChannel) data;
	}

	/**
	 * 完善用户资料
	 * 
	 * @param num
	 * @param userId
	 * @param realName
	 * @param idCard
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月11日
	 */
	public UserInfo bindUserInfo(String num, String userId, String realName,
			String idCard) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("realName", realName);
		params.put("idCard", idCard);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.BINDUSERINFO, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.BINDUSERINFO, response.toJson());
		return (UserInfo) data;
	}

	/**
	 * 获取银行列表
	 * 
	 * @param num
	 * @param timeId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月12日
	 */
	public BankList getBankList(String num, String timeId) throws CException {
		String url = getUrl(Command.INFOQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("timeId", timeId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETBANKLIST, response.toJson());
		}

		if (data == null) {
			data = new BankList();
		}
		// Object data = Parser.parse(Command.GETBANKLIST, response.toJson());
		return (BankList) data;
	}

	/**
	 * 获取省市列表
	 * 
	 * @param num
	 * @param timeId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月12日
	 */
	public ProCity getProCity(String num, String timeId) throws CException {
		String url = getUrl(Command.INFOQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("timeId", timeId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETPROCITY, response.toJson());
		}

		if (data == null) {
			data = new ProCity();
		}
		// Object data = Parser.parse(Command.GETPROCITY, response.toJson());
		return (ProCity) data;
	}

	/**
	 * 绑定银行卡
	 * 
	 * @param num
	 * @param userId
	 * @param province
	 * @param city
	 * @param bankName
	 * @param bankCardId
	 * @param bankCode
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public UserInfo bindBank(String num, String userId, String province,
			String city, String bankName, String bankCardId, String bankCode)
			throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("province", province);
		params.put("city", city);
		params.put("bankName", bankName);
		params.put("bankCardId", bankCardId);
		params.put("bankCode", bankCode);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.BINDBANK, response.toJson());
		}

		if (data == null) {
			data = new UserInfo();
		}
		// Object data = Parser.parse(Command.BINDBANK, response.toJson());
		return (UserInfo) data;
	}

	public BankType bankType(String num, String bankCode) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("cardNum", bankCode);

		Response response = Request.post(url, params);
		BankType data = null;
		if (response != null) {
			data = JSON.parseObject(response.toJson().toString(),
					BankType.class);
		}

		// BankType data = JSON.parseObject(response.toJson().toString(),
		// BankType.class);
		if (data == null) {
			data = new BankType();
		}
		return data;
	}

	/**
	 * 获取竞彩足球对阵信息
	 * 
	 * @param num
	 * @param issueNo
	 * @param gameNo
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月18日
	 */
	public FootBallMatch getFootBallMatchsInfo(String num, String issueNo,
			String gameNo, String isCurrent) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("issueNo", issueNo);
		params.put("gameNo", gameNo);
		params.put("isCurrent", isCurrent);
		// 投注平台
		params.put("betPlatform", "2");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETFOOTBALLMATCHSINFO,
					response.toJson());
		}

		if (data == null) {
			data = new FootBallMatch();
		}
		// Object data = Parser.parse(Command.GETFOOTBALLMATCHSINFO,
		// response.toJson());
		return (FootBallMatch) data;
	}

	/**
	 * 获取对阵时间期次
	 * 
	 * @param num
	 * @param gameNo
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月23日
	 */
	public IssueList getMatchTime(String num, String gameNo, String num1)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("gameNo", gameNo);
		// 查询数量
		params.put("num", num1);
		// 投注平台
		params.put("betPlatform", "2");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETMATCHTIME, response.toJson());
		}

		if (data == null) {
			data = new IssueList();
		}
		// Object data = Parser.parse(Command.GETMATCHTIME, response.toJson());
		return (IssueList) data;
	}

	/**
	 * 充值
	 * 
	 * @param num
	 * @param userId
	 * @param amount
	 * @param rechargeType
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月24日
	 */
	public ReCharge reCharge(String num, String userId, String amount,
			String rechargeType) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("amount", amount);
		params.put("rechargeType", rechargeType);
		params.put("platform", "2");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.RECHARGE, response.toJson());
		}

		if (data == null) {
			data = new ReCharge();
		}
		// Object data = Parser.parse(Command.RECHARGE, response.toJson());
		return (ReCharge) data;
	}

	/**
	 * 银联充值
	 * 
	 * @param num
	 * @param userId
	 * @param cardNum
	 * @param amount
	 * @param cell
	 * @param province
	 * @param city
	 * @param realName
	 * @param idCard
	 * @param cardType
	 * @param bankName
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月25日
	 */
	public EasyLinkReCharge easyLinkReCharge(String num, String userId,
			String cardNum, String amount, String cell, String province,
			String city, String realName, String idCard, String cardType,
			String bankName, String cityCode, String provinceCode)
			throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("amount", amount);
		params.put("cardNum", cardNum);
		params.put("cell", cell);
		params.put("province", province);
		params.put("city", city);
		params.put("realName", realName);
		params.put("idCard", idCard);
		params.put("cardType", cardType);
		params.put("bankName", bankName);
		params.put("provinceCode", provinceCode);
		params.put("cityCode", cityCode);
		params.put("platform", "2");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.EASYLINKRECHARGE, response.toJson());
		}

		if (data == null) {
			data = new EasyLinkReCharge();
		}
		// Object data = Parser.parse(Command.EASYLINKRECHARGE,
		// response.toJson());
		return (EasyLinkReCharge) data;
	}

	/**
	 * 获取焦点比赛
	 * 
	 * @param num
	 * @param gameNo
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月1日
	 */
	public FocusMatch getFocusMatch(String num, String gameNo, String isChange)
			throws CException {
		// Command.INFOQUERY为信息查询请求 为enum类型的类，这个类和接口抽象类类似
		// 只是无需声明参数类型，默认为private static
		String url = getUrl(Command.INFOQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("gameNo", gameNo);
		params.put("isChange", isChange);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETFOCUSMATCH, response.toJson());
		}

		if (data == null) {
			data = new FocusMatch();
		}
		// Object data = Parser.parse(Command.GETFOCUSMATCH, response.toJson());
		return (FocusMatch) data;
	}

	/**
	 * 极光push注册
	 * 
	 * @param num
	 * @param userId
	 * @param mobile
	 * @param jpushAppKey
	 * @param jpushRegistrationID
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月2日
	 */
	public GetCode registerPush(String num, String userId, String mobile,
			String jpushAppKey, String jpushRegistrationID) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("mobile", mobile);
		params.put("jpushAppKey", jpushAppKey);
		params.put("jpushRegistrationID", jpushRegistrationID);
		params.put("platform", "android");
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.REGISTERPUSH, response.toJson());
		}

		if (data == null) {
			data = new GetCode();
		}
		// Object data = Parser.parse(Command.REGISTERPUSH, response.toJson());
		return (GetCode) data;
	}

	/**
	 * 比分-关注赛事
	 * 
	 * @param num
	 * @param userId
	 * @param matchId
	 * @param status
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public GetCode watchMatch(String num, String userId, String matchId,
			String status, String lotteryNo) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("matchId", matchId);
		params.put("status", status);
		params.put("lotteryNo", lotteryNo);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.WATCHMATCH, response.toJson());
		}

		if (data == null) {
			data = new GetCode();
		}
		// Object data = Parser.parse(Command.WATCHMATCH, response.toJson());
		return (GetCode) data;
	}

	/**
	 * 比分-对阵列表
	 * 
	 * @param num
	 * @param userId
	 * @param lotteryNo
	 * @param type
	 * @param filter
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public ScoreList getScoreList(String num, String userId, String lotteryNo,
			String type, String filter, String issueNo) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("lotteryNo", lotteryNo);
		params.put("type", type);
		params.put("filter", filter);
		params.put("issueNo", issueNo);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETSCORELIST, response.toJson());
		}

		if (data == null) {
			data = new ScoreList();
		}
		// Object data = Parser.parse(Command.GETSCORELIST, response.toJson());
		return (ScoreList) data;
	}

	/**
	 * 比分-对阵列表
	 * 
	 * @param num
	 * @param userId
	 * @param lotteryNo
	 * @param type
	 * @param filter
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public ScoreList getScoreList(String num, String userId, String lotteryNo,
			String type, String filter) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("lotteryNo", lotteryNo);
		params.put("type", type);
		params.put("filter", filter);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.GETSCORELIST, response.toJson());
		}

		if (data == null) {
			data = new ScoreList();
		}
		// Object data = Parser.parse(Command.GETSCORELIST, response.toJson());
		return (ScoreList) data;
	}
	
	public ScoreObj getScoreDetail(String num, String userId, String lotteryNo, String matchId) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("matchId", matchId);
		params.put("userId", userId);
		Response response = Request.post(url, params);
		ScoreObj obj = null;
		if (response != null) {
			String json = response.toJson().toString();
			obj = JSON.parseObject(json, ScoreObj.class);
		}
		
		return obj == null ? new ScoreObj() : obj;
	}

	/**
	 * 比分-详情-赛事
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public ScoreDetailGame getScoreDetailGame(String num, String lotteryNo,
			String matchId) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("matchId", matchId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.SCOREDETAILGAME, response.toJson());
		}

		if (data == null) {
			data = new ScoreDetailGame();
		}
		// Object data = Parser.parse(Command.SCOREDETAILGAME,
		// response.toJson());
		return (ScoreDetailGame) data;
	}

	/**
	 * 比分-详情-分析
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public ScoreDetailAnalysis getScoreDetailAnalysis(String num,
			String lotteryNo, String matchId) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("matchId", matchId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.SCOREDETAILANALYSIS, response.toJson());
		}

		if (data == null) {
			data = new ScoreDetailAnalysis();
		}
		// Object data = Parser.parse(Command.SCOREDETAILANALYSIS,
		// response.toJson());
		return (ScoreDetailAnalysis) data;
	}

	/**
	 * 比分-详情-亚赔
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public ScoreDetailOddsList getScoreDetailAsia(String num, String lotteryNo,
			String matchId) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("matchId", matchId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.SCOREDETAILASIA, response.toJson());
		}

		if (data == null) {
			data = new ScoreDetailOddsList();
		}
		// Object data = Parser.parse(Command.SCOREDETAILASIA,
		// response.toJson());
		return (ScoreDetailOddsList) data;
	}

	/**
	 * 比分-详情-欧赔
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public ScoreDetailOddsList getScoreDetailEurope(String num,
			String lotteryNo, String matchId) throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("matchId", matchId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.SCOREDETAILEUROPE, response.toJson());
		}

		if (data == null) {
			data = new ScoreDetailOddsList();
		}
		// Object data = Parser
		// .parse(Command.SCOREDETAILEUROPE, response.toJson());
		return (ScoreDetailOddsList) data;
	}

	/**
	 * 比分-赔率
	 * 
	 * @param num
	 * @param companyId
	 * @param matchId
	 * @param pageNo
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public ScoreCompanyOdds getScoreOdds(String num, String companyId,
			String lotteryNo, String matchId, String companyType, String pageNo)
			throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("companyId", companyId);
		params.put("lotteryNo", lotteryNo);
		params.put("matchId", matchId);
		params.put("companyType", companyType);
		params.put("pageAmount", "20");
		params.put("pageNo", pageNo);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.SCOREODDS, response.toJson());
		}

		if (data == null) {
			data = new ScoreCompanyOdds();
		}
		// Object data = Parser.parse(Command.SCOREODDS, response.toJson());
		return (ScoreCompanyOdds) data;
	}

	/**
	 * 比分-公司
	 * 
	 * @param num
	 * @param timeId
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月21日
	 */
	public ScoreCompanyOdds getScoreCompany(String num, String timeId)
			throws CException {
		String url = getUrl(Command.SCOREMATCH);
		params.clear();
		params.put("transactionType", num);
		params.put("timeId", timeId);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.SCORECOMPANY, response.toJson());
		}

		if (data == null) {
			data = new ScoreCompanyOdds();
		}
		// Object data = Parser.parse(Command.SCORECOMPANY, response.toJson());
		return (ScoreCompanyOdds) data;
	}

	/**
	 * 获取站内消息;
	 * 
	 * @param userId
	 * @param type
	 * @param pageAmount
	 * @param pageNo
	 * @throws CException
	 */
	public Message getInsideMessageList(String num, String userId, String type,
			String pageAmount, String pageNo) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("messageType", type);
		params.put("pageAmount", pageAmount);
		params.put("sourceType", "1");
		params.put("pageNo", pageNo);
		Response response = Request.post(url, params);
		Message result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					Message.class);
		}

		if (result == null) {
			result = new Message();
		}
		// Message result = JSON.parseObject(response.toJson().toString(),
		// Message.class);
		return result;

	}

	/**
	 * 获取乐米详情列表;
	 * 
	 * @param num
	 * @param userId
	 * @param dealType
	 * @param startTime
	 * @param endTime
	 * @param pageAmount
	 * @param pageNo
	 * @throws CException
	 */
	public LeMi getLeMiClearList(String num, String userId, String dealType,
			String startTime, String endTime, String pageAmount, String pageNo)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("dealType", dealType);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageAmount", pageAmount);
		params.put("pageNo", pageNo);

		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.MYLEMI, response.toJson());
		}

		if (data == null) {
			data = new LeMi();
		}
		// Object data = Parser.parse(Command.MYLEMI, response.toJson());
		return (LeMi) data;
	}

	/**
	 * 现金购买红包列表;
	 * 
	 * @param num
	 * @param userId
	 * @param productType
	 * @throws CException
	 */
	public RedPacketLeMiList getProdectList(String num, String userId,
			String productType) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("productType", productType);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.PRODUCT, response.toJson());
		}

		if (data == null) {
			data = new RedPacketLeMiList();
		}
		// Object obj = Parser.parse(Command.PRODUCT, response.toJson());
		return (RedPacketLeMiList) data;
	}

	/**
	 * 后买红包/乐米
	 * 
	 * @param num
	 * @param userId
	 * @param productType
	 * @param productId
	 * @param productSaleMoney
	 * @return
	 * @throws CException
	 */
	public UserCount buyRedPkgLeMi(String num, String userId,
			String productType, String productId, String productSaleMoney)
			throws CException {
		String url = getUrl(Command.ACCOUNT);

		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("productType", productType);
		params.put("productId", productId);
		params.put("productPrice", productSaleMoney);
		Response response = Request.post(url, params);
		UserCount result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					UserCount.class);
		}

		if (result == null) {
			result = new UserCount();
		}
		// UserCount result = JSON.parseObject(response.toJson().toString(),
		// UserCount.class);
		return result;

	}

	/**
	 * 乐米兑换商品列表;
	 * 
	 * @param num
	 * @param userId
	 * @param productType
	 * @return
	 * @throws CException
	 */
	public RedPacketLeMiList getConvertProdectList(String num, String userId,
			String productType) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("productType", productType);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.PRODUCT, response.toJson());
		}

		if (data == null) {
			data = new RedPacketLeMiList();
		}
		// Object obj = Parser.parse(Command.PRODUCT, response.toJson());
		return (RedPacketLeMiList) data;
	}

	/**
	 * 乐米兑换记录列表;
	 * 
	 * @param num
	 * @param userId
	 * @param pageNo
	 * @param pageAmount
	 * @return
	 * @throws CException
	 */
	public RedPacketLeMiList getConvertNotesList(String num, String userId,
			String pageNo, String pageAmount) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("pageNo", pageNo);
		params.put("pageAmount", pageAmount);
		Response response = Request.post(url, params);
		RedPacketLeMiList result = null;
		String json=response.toJson().toString();
		if (response != null) {
			result = JSON.parseObject(json,
					RedPacketLeMiList.class);
		}

		if (result == null) {
			result = new RedPacketLeMiList();
		}
		// RedPacketLeMiList result = JSON.parseObject(response.toJson()
		// .toString(), RedPacketLeMiList.class);
		return result == null ? new RedPacketLeMiList() : result;
	}

	/**
	 * 未使用红包列表;
	 * 
	 * @param num
	 * @param userId
	 * @param pageNo
	 * @param pageAmount
	 * @return
	 * @throws CException
	 */
	public RedPkgUsed getNotUsedRedPkgList(String num, String userId,
			String pageNo, String pageAmount) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("pageNo", pageNo);
		params.put("pageAmount", pageAmount);
		Response response = Request.post(url, params);

		RedPkgUsed result = null;
		String json = response.toJson().toString();
		if (response != null) {
			result = JSON.parseObject(json,
					RedPkgUsed.class);
		}

		if (result == null) {
			result = new RedPkgUsed();
		}
		// RedPkgUsed result = JSON.parseObject(response.toJson().toString(),
		// RedPkgUsed.class);
		return result == null ? new RedPkgUsed() : result;
	}

	/**
	 * 更新站内信未读状态;
	 * 
	 * @param num
	 * @param userId
	 * @param messageId
	 * @param type
	 * @throws CException
	 * @throws JSONException
	 */
	public void updataMessageData(String num, String userId, String messageId,
			String type) throws CException, JSONException {
		final String url = getUrl(Command.ACCOUNT);
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("messageId", messageId);
		params.put("delOrEdit", type);

		Response response = Request.post(url, params);
		String result = response.toJson().toString();
		JSONObject object = null;
		String resCode = null;
		if (response != null) {
			object = new JSONObject(result);
			resCode = object.getString("resCode");
		}
		if (object != null && resCode != null && resCode.equals("3002")) {
			initDatas();
			if (loginPattern == 1 && auto) {
				// 之前的登陆状态为微信
				Controller.getInstance().loginWithOpenid(
						GlobalConstants.URL_WECHAT_LOAD, openId,
						new CallBack() {
							/** 使用openid登陆微信成功 **/
							public void loginWithOpenidSuccess(String json) {
								try {
									JSONObject jsonObject = new JSONObject(json);
									String resCode = jsonObject
											.getString("resCode");
									if (resCode != null && resCode.equals("0")) {
										App.userInfo = (UserInfo) Parser
												.parse(Command.LOGIN,
														jsonObject);
										UserLogic.getInstance().saveUserInfo(
												App.userInfo);
										// 登陆完成 重新发送该请求
										Response response = Request.post(url,
												params);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								} catch (CException e) {
									e.printStackTrace();
								}
							}

							/** 使用openid登陆微信失败 **/
							public void loginWithOpenidFailure(String json) {

							}
						});
			} else if (loginPattern == 2 && auto) {
				// 之前的登陆状态为自营
				Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
						App.userInfo.getUserName(), App.userInfo.getPassword(),
						new CallBack() {
							@Override
							public void loginSuccess(UserInfo info) {
								try {
									if (info != null
											&& info.getResCode().equals("0")) {
										String password = App.userInfo
												.getPassword();
										App.userInfo = info;
										App.userInfo.setPassword(password);
										// 将用户信息保存到配置文件中
										Response response = Request.post(url,
												params);
									}
								} catch (CException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void loginFailure(String error) {
							}
						});
			} else if (!auto) {
				MainActivity mainActivity = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (mainActivity != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					Intent intent = new Intent(mainActivity,
							LoginActivity.class);
					intent.putExtra("callthepage", CallThePage.LOBOHALL);
					mainActivity.startActivity(intent);
				} else {
					LoginActivity.forwartLoginActivity(App.getInstance(),
							CallThePage.LOBOHALL);
				}
			}

		}
	}

	/**
	 * 乐米兑换
	 * 
	 * @param num
	 * @param userId
	 * @param productPrice
	 * @param productId
	 * @param productType
	 * @param cell
	 * @throws CException
	 */
	public UserCount convertLeMi(String num, String userId,
			String productPrice, String productId, String productType,
			String cell) throws CException {
		String url = getUrl(Command.ACCOUNT);

		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("productPrice", productPrice);
		params.put("productId", productId);
		params.put("productType", productType);
		params.put("cell", cell);

		Response response = Request.post(url, params);

		UserCount result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					UserCount.class);
		}

		if (result == null) {
			result = new UserCount();
		}
		// UserCount result = JSON.parseObject(response.toJson().toString(),
		// UserCount.class);
		return result == null ? new UserCount() : result;
	}

	/**
	 * 获取红包详情列表;
	 * 
	 * @param num
	 * @param userId
	 * @param dealType
	 * @param startTime
	 * @param endTime
	 * @param pageAmount
	 * @param pageNo
	 * @throws CException
	 * @return
	 */
	public RedPkgDetail getRedPkgClearList(String num, String userId,
			String dealType, String startTime, String endTime,
			String pageAmount, String pageNo) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("dealType", dealType);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageAmount", pageAmount);
		params.put("pageNo", pageNo);

		Response response = Request.post(url, params);

		RedPkgDetail result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					RedPkgDetail.class);
		}

		if (result == null) {
			result = new RedPkgDetail();
		}
		// RedPkgDetail data = JSON.parseObject(response.toJson().toString(),
		// RedPkgDetail.class);
		return result == null ? new RedPkgDetail() : result;
	}

	/**
	 * 开奖首页列表
	 * 
	 * @Description:
	 * @param num
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public List<LotteryDrawInfo> getHallLotteryDrawInfoList(String num)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);

		Response response = Request.post(url, params);

		lotteryDrawInfoList result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					lotteryDrawInfoList.class);
		}

		if (result == null) {
			result = new lotteryDrawInfoList();
		}

		// lotteryDrawInfoList data = JSON.parseObject(response.toJson()
		// .toString(), lotteryDrawInfoList.class);
		List<LotteryDrawInfo> list = new ArrayList<LotteryDrawInfo>();
		if (result != null) {
			list = result.getList();
		}
		return list;
	}

	/**
	 * 单彩种开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param pageNo
	 * @param lotteryNo
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public List<LotteryDrawInfo> getLotteryDrawInfoList(String num,
			String pageNo, String lotteryNo) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("pageNo", pageNo);
		params.put("pageAmount", "10");

		Response response = Request.post(url, params);

		lotteryDrawInfoList result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					lotteryDrawInfoList.class);
		}

		if (result == null) {
			result = new lotteryDrawInfoList();
		}
		// lotteryDrawInfoList data = JSON.parseObject(response.toJson()
		// .toString(), lotteryDrawInfoList.class);
		List<LotteryDrawInfo> list = new ArrayList<LotteryDrawInfo>();
		if (result != null) {
			list = result.getList();
		}
		return list;
	}

	/**
	 * 开奖详情
	 * 
	 * @Description:
	 * @param num
	 * @param gameNo
	 * @param startTime
	 * @param endTime
	 * @param issue
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public LotteryDrawDetailInfo getLotteryDrawInfo(String num, String gameNo,
			String startTime, String endTime, String issue) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("gameNo", gameNo);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("issue", issue);
		params.put("betPlatform", "2");

		Response response = Request.post(url, params);

		LotteryDrawDetailInfo result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					LotteryDrawDetailInfo.class);
		}

		if (result == null) {
			result = new LotteryDrawDetailInfo();
		}

		// LotteryDrawDetailInfo data = JSON.parseObject(response.toJson()
		// .toString(), LotteryDrawDetailInfo.class);
		// if (data == null) {
		// data = new LotteryDrawDetailInfo();
		// }
		return result;
	}

	/**
	 * 获取竞彩足球开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param gameNo
	 * @param issue
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public LotteryDrawFootballList getLotteryDrawFootballList(String num,
			String gameNo, String issue) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("gameNo", gameNo);
		params.put("issue", issue);
		params.put("betPlatform", "2");

		Response response = Request.post(url, params);

		LotteryDrawFootballList result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					LotteryDrawFootballList.class);
		}

		if (result == null) {
			result = new LotteryDrawFootballList();
		}
		// LotteryDrawFootballList data = JSON.parseObject(response.toJson()
		// .toString(), LotteryDrawFootballList.class);
		// if (data == null) {
		// data = new LotteryDrawFootballList();
		// }
		return result;
	}

	/**
	 * 获取头部广告
	 * 
	 * @Description:
	 * @param num
	 * @param timeId
	 * @param isNewUser
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public HallAd getHallAd(String num, String timeId, String isNewUser)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("isNewUser", isNewUser);
		params.put("timeId", timeId);
		params.put("fromType", "1");

		Response response = Request.post(url, params);

		HallAd result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					HallAd.class);
		}

		if (result == null) {
			result = new HallAd();
		}

		// HallAd data = JSON.parseObject(response.toJson().toString(),
		// HallAd.class);
		// if (data == null) {
		// data = new HallAd();
		// }
		return result;
	}

	/**
	 * 弹出公告
	 * 
	 * @Description:
	 * @param num
	 * @param timeId
	 * @param isNewUser
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public HallNotice getHallNotice(String num, String timeId,
			String isNewUser, String width, String height) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("isNewUser", isNewUser);
		params.put("timeId", timeId);

		params.put("width", width);
		params.put("height", height);
		params.put("platform", GlobalConstants.PLATFORM);
		params.put("pkgType", GlobalConstants.PKGTYPE);

		Response response = Request.post(url, params);

		HallNotice result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					HallNotice.class);
		}

		if (result == null) {
			result = new HallNotice();
		}
		// HallNotice data = JSON.parseObject(response.toJson().toString(),
		// HallNotice.class);
		// if (data == null) {
		// data = new HallNotice();
		// }
		return result;
	}

	/**
	 * 软件版本升级
	 * 
	 * @Description:
	 * @param num
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public Upgrade getHallUpgrade(String num, String width, String height)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);

		params.put("width", width);
		params.put("height", height);
		params.put("platform", GlobalConstants.PLATFORM);
		params.put("pkgType", GlobalConstants.PKGTYPE_ZY);
		params.put("src", GlobalConstants.CHANNEL);
		params.put("ver", GlobalConstants.VERSION);

		Response response = Request.post(url, params);

		Upgrade result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					Upgrade.class);
		}

		if (result == null) {
			result = new Upgrade();
		}

		// Upgrade data = JSON.parseObject(response.toJson().toString(),
		// Upgrade.class);
		// if (data == null) {
		// data = new Upgrade();
		// }
		return result;
	}

	/**
	 * 彩种列表控制
	 * 
	 * @Description:
	 * @param num
	 * @param timeId
	 * @param ver
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public ControllLottery getHallControllLottery(String num, String timeId,
			String ver) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("timeId", timeId);
		params.put("ver", ver);

		Response response = Request.post(url, params);

		ControllLottery result = null;
		if (response != null) {
			result = JSON.parseObject(response.toJson().toString(),
					ControllLottery.class);
		}

		if (result == null) {
			result = new ControllLottery();
		}
		// String json = response.toJson().toString();
		// Log.e("qiufeng", json);
		// ControllLottery data = JSON.parseObject(json, ControllLottery.class);
		// if (data == null) {
		// data = new ControllLottery();
		// }
		return result;
	}

	/**
	 * 获取签到列表
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public SignList getSignList(String num, String userId, String startTime,
			String endTime, String signType) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("signType", signType);
		Response response = Request.post(url, params);
		Object data = null;
		if (response != null) {
			data = Parser.parse(Command.NUM_SIGNLIST, response.toJson());
		}

		if (data == null) {
			data = new SignList();
		}
		// Object signList = Parser.parse(Command.NUM_SIGNLIST,
		// response.toJson());
		// if (signList == null) {
		// signList = new SignList();
		// }
		return (SignList) data;
	}

	/**
	 * 点击签到
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public SignStatus sign(String num, String userId, String signDate,
			String startTime, String endTime, String signType)
			throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("signDate", signDate);
		params.put("signType", signType);

		Response response = Request.post(url, params);

		SignStatus data = null;
		if (response != null) {
			data = JsonHelper.jsonToObject(response.toJson().toString(),
					SignStatus.class);
			// data = JSON.parseObject(response.toJson().toString(),
			// SignStatus.class);
		}

		if (data == null) {
			data = new SignStatus();
		}
		// SignStatus status = JSON.parseObject(response.toJson().toString(),
		// SignStatus.class);
		// if (status == null) {
		// status = new SignStatus();
		// }
		return data;
	}

	/**
	 * 获取彩种期次
	 * 
	 * @param lotteryNo
	 * @param queryNum
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public LotteryNOs getLotteryNOs(String lotteryNo, String queryNum,
			String num, CallBack mBack) throws CException {
		String url = getUrl(Command.GETLOTTERYNOS);
		params.clear();
		params.put("transactionType", num);
		params.put("lotteryNo", lotteryNo);
		params.put("queryNum", queryNum);
		Response response = Request.post(url, params);
		LotteryNOs lotteryNOs = JSON.parseObject(response.toJson().toString(),
				LotteryNOs.class);
		return lotteryNOs;
	}

	/**
	 * 微信登陆-获取商户信息（appid）
	 * 
	 * @param num
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public AppIdBean weiChatGetAppId(String num, String type, CallBack mBack)
			throws CException {
		String url = getUrl(Command.DOWEICHAT);
		params.clear();
		params.put("transactionType", num);
		params.put("type", type);
		Response response = Request.post(url, params);
		AppIdBean appIdBean = JSON.parseObject(response.toJson().toString(),
				AppIdBean.class);
		return appIdBean;
	}

	/**
	 * 微信登陆 -验证用户信息
	 * 
	 * @param num
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public String weiChatLoad(String num, String code, CallBack mBack)
			throws CException {
		String url = getUrl(Command.DOWEICHAT);
		params.clear();
		params.put("transactionType", num);
		params.put("code", code);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 微信登陆 -验证手机号
	 * 
	 * @param num
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public String weiChatBindPhone(String num, String code, String mobile,
			String openId, CallBack mBack) throws CException {
		String url = getUrl(Command.DOWEICHAT);
		params.clear();
		params.put("transactionType", num);
		params.put("code", code);
		params.put("mobile", mobile);
		params.put("openId", openId);
		Response response = Request.post(url, params);
		return response.toJson().toString();
	}

	/**
	 * 微信登陆 -注册新用户
	 * 
	 * @param num
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public String weiChatRegistUser(String num, String mobile, String openId,
			String userName, String password, CallBack mBack) throws CException {
		String url = getUrl(Command.DOWEICHAT);
		params.clear();
		params.put("transactionType", num);
		params.put("mobile", mobile);
		params.put("openId", openId);
		params.put("userName", userName);
		params.put("password", password);
		Response response = Request.post(url, params);
		return response.toJson().toString();
	}

	/**
	 * 微信充值
	 * 
	 * @param num 接口号
	 * @param userId 用户ID
	 * @param amount 充值金额
	 * @param platform 平台编号1
	 *            ：客户端，2：PHP，3：H5
	 * @param isOrder 是否是订单充值
	 *            :1:是,2否
	 * @param orderId 如果是订单充值
	 *            ，需传订单号
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public String weChatRecharge(String num, String userId, String amount,
			String platform, String isOrder, String orderId, CallBack mBack)
			throws CException {
		String url = getUrl(Command.DOWEICHAT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("amount", amount);
		params.put("platform", "1");
		params.put("isOrder", isOrder);
		params.put("orderId", orderId);
		Response response = Request.post(url, params);
		return response.toJson().toString();
	}

	/**
	 * 获取竞彩篮球开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param gameNo
	 * @param issue
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年6月7日
	 */
	public LotteryDrawBasketballList getLotteryDrawBasketballList(String num,
			String gameNo, String issue) throws CException {
		String url = getUrl(Command.LOBOUSERQUERY);
		params.clear();
		params.put("transactionType", num);
		params.put("gameNo", gameNo);
		params.put("issue", issue);
		params.put("betPlatform", "2");

		Response response = Request.post(url, params);
		String jsonStr = response.toJson().toString();
		LotteryDrawBasketballList data = JSON.parseObject(jsonStr,
				LotteryDrawBasketballList.class);
		if (data == null) {
			data = new LotteryDrawBasketballList();
		}
		return data;
	}

	/**
	 * 投注接口
	 * 
	 * @param num
	 * @param userId
	 * @param bettingType
	 * @param gameNo
	 * @param issue
	 * @param multiple
	 * @param buyMoney
	 * @param isChase
	 * @param chase
	 * @param payType
	 * @param redPackageId
	 * @param redPackageMoney
	 * @param choiceType
	 * @param planEndTime
	 * @param stopCondition
	 * @param list
	 * @param redPkgType
	 * @return
	 * @throws CException
	 * @throws JSONException
	 */
	public Betting bettingPay(String num, String userId, String bettingType,
			String gameNo, String issue, String multiple, String buyMoney,
			String isChase, String chase, String payType, String redPackageId,
			String redPackageMoney, String choiceType, String planEndTime,
			String stopCondition, List<JSONObject> list, String redPkgType)
			throws CException, JSONException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("bettingType", bettingType);
		params.put("gameNo", gameNo);
		params.put("issue", issue);
		// buyMoney = "0.01";
		params.put("buyMoney", buyMoney);
		params.put("isChase", isChase);
		params.put("chase", chase);

		if (Integer.parseInt(chase) > 1) {
			StringBuilder sbMoney = new StringBuilder();
			for (int i = 0; i < Integer.parseInt(chase); i++) {
				sbMoney.append(buyMoney).append("^");
			}
			sbMoney.deleteCharAt(sbMoney.length() - 1);
			// params.put("planMoney", "0.01");
			params.put("planMoney", sbMoney.toString());

			StringBuilder sbMul = new StringBuilder();
			for (int i = 0; i < Integer.parseInt(chase); i++) {
				sbMul.append(multiple).append("^");
			}
			sbMul.deleteCharAt(sbMul.length() - 1);
			params.put("multiple", sbMul.toString());
		} else {
			params.put("multiple", multiple);
			params.put("planMoney", buyMoney);
		}
		params.put("payType", payType);
		params.put("redPackageId", redPackageId);
		params.put("redPackageMoney", redPackageMoney);
		params.put("choiceType", choiceType);
		params.put("planEndTime", planEndTime);
		params.put("list", new JSONArray(list));
		params.put("planTitle", "0");
		params.put("planDes", "");
		params.put("planMinLimit", "0");
		params.put("guaranteeMoney", "0");
		params.put("stopCondition", stopCondition);
		params.put("isCommision", "0");
		params.put("commisionRatio", "0");
		params.put("viewType", "0");
		params.put("isTop", "0");
		// params.put("lotteryType", "0");
		params.put("betPlat", "1");
		params.put("isOptimize", "0");
		params.put("redPkgType", redPkgType);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.BETTING, response.toJson());
		return (Betting) data;
	}

	/**
	 * 订单再次支付
	 * 
	 * @param num
	 * @param userId
	 * @param betPlat
	 * @param payMoney
	 * @param payType
	 * @param redBagId
	 * @param redBagType
	 * @return
	 * @throws CException
	 * @throws JSONException
	 */
	public Betting orderAgainPay(String num, String userId, String orderId,
			String betPlat, String payMoney, String payType, String redBagId,
			String redBagType) throws CException, JSONException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("orderId", orderId);
		params.put("betPlat", betPlat);
		params.put("payMoney", payMoney);
		params.put("payType", payType);
		params.put("redBagId", redBagId);
		params.put("redBagType", redBagType);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.BETTING, response.toJson());
		return (Betting) data;
	}

	/**
	 * 消息推送获取／设置
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param isSetting
	 * @param list
	 * @return
	 * @throws CException
	 * @throws JSONException
	 * @author:www.wenchuang.com
	 * @date:2016-6-24
	 */
	public PushResult pushSet(String num, String userId, String isSetting,
			List<JSONObject> list) throws CException, JSONException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("isSetting", isSetting);
		params.put("list", new JSONArray(list));
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.NUM_PUSHSET, response.toJson());
		return (PushResult) data;
	}

	/**
	 * 发送mac信息
	 * 
	 * @param num
	 * @param mac
	 * @param sign
	 * @param phone
	 * @param ime
	 * @return
	 */
	public String sendMaxInfo(String num, String mac, String sign,
			String phone, String ime) throws CException {
		String url = getUrl(Command.LOBOBET);
		params.clear();
		params.put("transactionType", num);
		params.put("mac", mac);
		params.put("sign", sign);
		params.put("phone", phone);
		params.put("ime", ime);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 通过openid进行用户登陆 为了避免微信的授权页的产生
	 * 
	 * @param num
	 * @param openid
	 * @return
	 */
	public String loginWithOpenid(String num, String openid) throws Exception {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("openId", openid);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 获取未读状态的消息/活动的总数量
	 * 
	 * @param num
	 * @param userid
	 * @param mType
	 * @param sType
	 * @return
	 * @throws Exception
	 */
	public NoticeOrMessageCount getNewNoticeOrMessage(String num,
			String userid, String mType, String sType) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userid);
		params.put("messageType", mType);
		params.put("sourceType", sType);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.NUM_NOTICORMESSAGE,
				response.toJson());
		return (NoticeOrMessageCount) data;
	}

	/**
	 * 获取游戏竞猜数据
	 * 
	 * @param num
	 * @param dayNum
	 * @param userId
	 * @return
	 */
	public String getMatchGameInfo(String num, String dayNum, String userId)
			throws Exception {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("dayNum", dayNum);
		params.put("userId", userId);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 获取本场比赛的留言板信息
	 * 
	 * @param num 接口号
	 * @param leagueCode 比赛Id
	 * @param pageAmount 煤业显示条数
	 *            （默认10条）
	 * @param pageNo 查询页码
	 *            ，默认1
	 * @return
	 * @throws Exception
	 */
	public String getMatchMessage(String num, String leagueCode,
			String pageAmount, String pageNo) throws Exception {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("leagueCode", leagueCode);
		params.put("pageAmount", pageAmount);
		params.put("pageNo", pageNo);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 发布一条比赛竞猜留言
	 * 
	 * @param num
	 * @param userId
	 * @param leagueCode 题目Id
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String sendMatchMessage(String num, String userId,
			String leagueCode, String content) throws Exception {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("leagueCode", leagueCode);
		params.put("content", content);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 娱乐场游戏投注
	 * 
	 * @param num
	 * @param userId
	 * @param gameId
	 * @param leagueCode
	 * @param questionId
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public String gameBetting(String num, String userId, String gameId,
			String leagueCode, String questionId, String content,
			String betAmount) throws Exception {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("gameId", gameId);
		params.put("leagueCode", leagueCode);
		params.put("questionId", questionId);
		params.put("content", content);
		params.put("betAmount", betAmount);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 大转盘的投注接口
	 * 
	 * @param num
	 * @param userId
	 * @param gameId
	 * @param content
	 * @param betAmount
	 * @return
	 * @throws CException
	 */
	// public String luckyPanBetting(String num, String userId, String gameId,
	// String content, String betAmount) throws CException {
	//
	// String url = getUrl(Command.ACCOUNT);
	// params.clear();
	// params.put("transactionType", num);
	// params.put("userId", userId);
	// params.put("gameId", gameId);
	// params.put("content", content);
	// params.put("betAmount", betAmount);
	// Response response = Request.post(url, params);
	// String json = response.toJson().toString();
	// return json;
	// }

	/**
	 * 我的竞猜记录接口
	 * 
	 * @param num
	 * @param userId
	 * @param pageAmount
	 * @param pageNo
	 * @return
	 * @throws CException
	 */
	public String myGuessRecord(String num, String userId, String beginTime,
			String endTime, String status, String pageAmount, String pageNo)
			throws CException {

		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("status", status);
		params.put("pageAmount", pageAmount);
		params.put("pageNo", pageNo);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 我的排行-当前排行
	 * 
	 * @param num
	 * @param userId
	 * @return
	 * @throws CException
	 */
	public String myRankingListC(String num, String userId) throws CException {

		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 分享
	 * 
	 * @Description:
	 * @param num
	 * @param gameId
	 * @param shareType
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016-8-5
	 */
	public ShareObj share(String num, String gameId, String shareType)
			throws CException {

		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("gameId", gameId);
		params.put("shareType", shareType);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.NUM_SHARE, response.toJson());
		return (ShareObj) data;
	}

	/**
	 * 分享增加乐米
	 * 
	 * @Description:
	 * @param num
	 * @param gameId
	 * @param leagueCode
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016-8-5
	 */
	public ShareAddLeMiObj shareAddLeMi(String num, String userId,
			String gameId, String leagueCode) throws CException {

		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("gameId", gameId);
		params.put("leagueCode", leagueCode);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.NUM_SHAREADDLEMI, response.toJson());
		return (ShareAddLeMiObj) data;
	}

	/**
	 * 我的排行-周排行
	 * 
	 * @param num
	 * @param userId
	 * @return
	 * @throws CException
	 */
	public String myRankingListW(String num, String userId, String issue)
			throws CException {

		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("issue", issue);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 获取周排行的期次信息
	 * 
	 * @param num
	 * @param weekNum
	 * @return
	 * @throws CException
	 */
	public String getRankListDate(String num, String weekNum, String gameId)
			throws CException {

		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("weekNum", weekNum);
		params.put("gameId", gameId);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 获取游戏列表
	 * 
	 * @param num
	 * @return
	 * @throws CException
	 */
	public String getGameList(String num,String timeId) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("timeId", timeId);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 获取一场制胜系统匹配对阵
	 * 
	 * @Description:
	 * @param num
	 * @param matchMsg
	 * @param issueNo
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016-10-19
	 */
	public ResMatchAgainstInfo getOneWinSys(String num, String matchMsg,
			String issueNo) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("matchMsg", matchMsg);
		params.put("issueNo", issueNo);
		Response response = Request.post(url, params);
		Object data = Parser.parse(Command.NUM_ONEWINSYS, response.toJson());
		return (ResMatchAgainstInfo) data;
	}

	/**
	 * 获取七牛上传图片需要参数
	 * 
	 * @param num
	 * @param userId
	 * @return
	 * @throws CException
	 */
	public String getQiNiuParams(String num, String userId) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}

	/**
	 * 获取七牛上传图片需要参数
	 * 
	 * @param num
	 * @param userId
	 * @return
	 * @throws CException
	 */
	public String updateUserHeaderImage(String num, String userId,
			String headImgUrl) throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("headImgUrl", headImgUrl);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}
	
	/**
	 * 获取七牛上传图片需要参数
	 * 
	 * @param num
	 * @param leagueCode
	 * @return
	 * @throws CException
	 */
	public String getLeagueDetail(String num, String leagueCode)
			throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("leagueCode", leagueCode);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}
	
	/**
	 * 首页轮播文字
	 * @Description:
	 * @param num
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016-11-30
	 */
	public HallTextList getHallText(String num)
			throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		HallTextList obj = null;
		obj = JSON.parseObject(json, HallTextList.class);
		return obj == null ? new HallTextList() : obj;
	}
	
	/**
	 * 验证验证码是否正确
	 * @param num
	 * @param userId
	 * @param validateCode
	 * @param cell
	 * @return
	 * @throws CException
	 */
	public String validateCode(String num, String userId, String validateCode, String cell)
			throws CException {
		String url = getUrl(Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		params.put("validCode", validateCode);
		params.put("cell", cell);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}
	/**
	 * 获取用户投注胜率
	 * @param num
	 * @param userId
	 * @return
	 * @throws CException
	 */
	public String getMatchGuessWinning(String num , String userId) throws CException{
		String url = getUrl (Command.ACCOUNT);
		params.clear();
		params.put("transactionType", num);
		params.put("userId", userId);
		Response response = Request.post(url, params);
		String json = response.toJson().toString();
		return json;
	}
	
}