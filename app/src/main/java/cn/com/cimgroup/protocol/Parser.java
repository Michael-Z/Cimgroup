package cn.com.cimgroup.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import cn.com.cimgroup.bean.AccountDetail;
import cn.com.cimgroup.bean.AccountDetailList;
import cn.com.cimgroup.bean.AllBettingList;
import cn.com.cimgroup.bean.BankInfo;
import cn.com.cimgroup.bean.BankList;
import cn.com.cimgroup.bean.Betting;
import cn.com.cimgroup.bean.ChannelContent;
import cn.com.cimgroup.bean.City;
import cn.com.cimgroup.bean.EasyLinkReCharge;
import cn.com.cimgroup.bean.FocusMatch;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.FootballDetailList;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.GuestFutureObj;
import cn.com.cimgroup.bean.GuestNearObj;
import cn.com.cimgroup.bean.GuestRankObj;
import cn.com.cimgroup.bean.HostFutureObj;
import cn.com.cimgroup.bean.HostNearObj;
import cn.com.cimgroup.bean.HostRankObj;
import cn.com.cimgroup.bean.IssueList;
import cn.com.cimgroup.bean.LeMi;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryList;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.bean.NoticeOrMessageCount;
import cn.com.cimgroup.bean.ProCity;
import cn.com.cimgroup.bean.Province;
import cn.com.cimgroup.bean.PushResult;
import cn.com.cimgroup.bean.ReCharge;
import cn.com.cimgroup.bean.ReChargeChannel;
import cn.com.cimgroup.bean.RedPacketLeMiList;
import cn.com.cimgroup.bean.ResMatchAgainstInfo;
import cn.com.cimgroup.bean.ScoreCompanyOdds;
import cn.com.cimgroup.bean.ScoreDetailAnalysis;
import cn.com.cimgroup.bean.ScoreDetailGame;
import cn.com.cimgroup.bean.ScoreDetailGameEvent;
import cn.com.cimgroup.bean.ScoreDetailOdds;
import cn.com.cimgroup.bean.ScoreDetailOddsList;
import cn.com.cimgroup.bean.ScoreList;
import cn.com.cimgroup.bean.ScoreListObj;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.Sign;
import cn.com.cimgroup.bean.SignList;
import cn.com.cimgroup.bean.TzDetail;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.bean.VsObj;
import cn.com.cimgroup.bean.ZhuiDetailList;

import com.alibaba.fastjson.JSON;

public class Parser {

	/**
	 * 根据命令解析结果
	 * 
	 * @param command
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	@SuppressWarnings("incomplete-switch")
	public static Object parse(Command command, JSONObject json)
			throws CException {
		Object result = null;
		try {
			switch (command) {
			// 获得当前销售期次请求
			case LOBOPERIOD:
				result = parsegetLoBoPeriod(json);
				break;
			// 账户明细
			case LOBOACCOUNTDETAIL:
				result = parseGetLoBoAccountDetail(json);
				break;
			// 充值提现记录列表
			case LOBOCHARGECASH:
				result = parseGetLoBoCash(json);
				break;
			// 投注记录列表
			case LOBOTZLIST:
				result = parseGetLoBoTzList(json);
				break;
			// 投注详情
			case LOBOTZDETAIL:
				result = parseGetLoBoTzDetail(json);
				break;
			// 注册
			case REGISTER:
				result = parseRegister(json);
				break;
			// 登录
			case LOGIN:
				result = parseLogin(json);
				break;
			// 重设密码
			case RESETPWD:
				result = parseReSetPWD(json);
				break;
			case GETBACKPWD:
				result = parseGetBackPWD(json);
				break;
			// 获取验证码
			case GETCODE:
				result = parseGetCode(json);
				break;
			// 验证手机号
			case VERIPHONE:
				result = parseVeriPhone(json);
				break;
			// 获取个人信息
			case GETUSERINFO:
				result = parseGetUserInfo(json);
				break;
			// 提现
			case OUTCASH:
				result = parseOutCash(json);
				break;
			// 获取充值渠道列表
			case GETRECHARGELIST:
				result = parseGetReChargeList(json);
				break;
			// 完善用户资料
			case BINDUSERINFO:
				result = parseBindUserInfo(json);
				break;
			// 获取银行列表
			case GETBANKLIST:
				result = parseGetBankList(json);
				break;
			// 获取省市列表
			case GETPROCITY:
				result = parseGetProCity(json);
				break;
			// 绑定银行卡
			case BINDBANK:
				result = parseBindBank(json);
				break;
			// 获取竞彩足球对阵信息
			case GETFOOTBALLMATCHSINFO:
				result = parseGetFootBallMatchsInfo(json);
				break;
			// 获取对阵时间期次
			case GETMATCHTIME:
				result = parseGetMatchTime(json);
				break;
			// 充值
			case RECHARGE:
				result = parseReCharge(json);
				break;
			// 银联充值
			case EASYLINKRECHARGE:
				result = parseEasyLinkReCharge(json);
				break;
			// 获取焦点比赛
			case GETFOCUSMATCH:
				result = parseGetFocusMatch(json);
				break;
			// 极光push注册
			case REGISTERPUSH:
				result = parseRegisterPush(json);
				break;
			// 比分-关注赛事
			case WATCHMATCH:
				result = parseWatchMatch(json);
				break;
			// 比分-对阵列表
			case GETSCORELIST:
				result = parseGetScoreListMatch(json);
				break;
			// 比分-详情-赛事
			case SCOREDETAILGAME:
				result = parseGetScoreDetailGame(json);
				break;
			// 比分-详情-分析
			case SCOREDETAILANALYSIS:
				result = parseGetScoreDetailAnalysis(json);
				break;
			// 比分-详情-亚赔
			case SCOREDETAILASIA:
				result = parseGetScoreDetailAsia(json);
				break;
			// 比分-详情-欧赔
			case SCOREDETAILEUROPE:
				result = parseGetScoreDetailEurope(json);
				break;
			// 比分-公司
			case SCORECOMPANY:
				result = parseGetScoreCompany(json);
				break;
			// 比分-赔率
			case SCOREODDS:
				result = parseGetScoreOdds(json);
				break;
			case MYLEMI:
				result = parseGetLeMiList(json);
				break;
			// 现金购买红包,乐米列表;
			case PRODUCT:
				result = getProductList(json);
				break;
			// 投注
			case BETTING:
				result = parseBetting(json);
				break;
			// 获取数字彩开奖列表
			case LOTTERYLIST:
				result = parseLotteryList(json);
				break;
			// 投注追号详情
			case LOBOTZZHUIDETAIL:
				result = parseGetLoBoTzZhuiDetail(json);
				break;
			// 竞彩投注详情
			case FOOTBALLDETAIL:
				result = parseGetFootballDetail(json);
				break;
			// 签到列表信息
			case NUM_SIGNLIST:
				result = parseGetSignList(json);
				break;
			// 消息推送获取／设置
			case NUM_PUSHSET:
				result = parsePushSet(json);
				break;
			case NUM_NOTICORMESSAGE:
				result = parseNewNoticeCount(json);
				break;
			case NUM_SHARE:
				result = parseShare(json);
				break;
			case NUM_SHAREADDLEMI:
				result = parseShareAddLeMi(json);
				break;
			case NUM_ONEWINSYS:
				result = parseOneWinSys(json);
				break;
			}
		} catch (CException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 解析jsonarray
	 * 
	 * @param command
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	@SuppressWarnings("incomplete-switch")
	public static Object parse(Command command, JSONArray json)
			throws CException {
		Object result = null;
		// try {
		// switch (command) {
		// //账户明细
		// case LOBOACCOUNTDETAIL:
		// result = parseGetLoBoAccountDetail(json);
		// break;
		// /*
		// =============================new======================================*/
		//
		//
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// throw new CException(CException.JSONPARSEERROR);
		// }
		return result;
	}

	private static List<LoBoPeriodInfo> parsegetLoBoPeriod(JSONObject json)
			throws CException {
		Log.e("qiufeng", "期次时间获取：" + json);
		List<LoBoPeriodInfo> infos = new ArrayList<LoBoPeriodInfo>();
		try {
			if (json != null) {
				JSONArray array = json.getJSONArray("gameList");
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						LoBoPeriodInfo info = new LoBoPeriodInfo();
						info.setEndTime(object.optLong("endTime", 0));
						info.setStartTime(object.optLong("startTime", 0));
						info.setGameNo(object.optString("gameNo", ""));
						info.setIssue(object.optString("issue", ""));
						info.setIssueStatus(object.optString("issueStatus", ""));
						info.setTimeStep(object.optString("timeStep", ""));
						infos.add(info);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return infos;
	}

	/**
	 * 账户明细和充值
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月30日
	 */
	private static AccountDetailList parseGetLoBoAccountDetail(JSONObject json)
			throws CException {
		AccountDetailList detailList = new AccountDetailList();
		List<AccountDetail> list = new ArrayList<AccountDetail>();
		try {
			if (json != null) {
				// 账户明细数据解析
				String resCode = json.getString("resCode");
				if (resCode != null && resCode.equals("0")) {
					JSONArray accountArray = json.getJSONArray("list");
					if (accountArray != null) {
						for (int i = 0; i < accountArray.length(); i++) {
							JSONObject object = accountArray.getJSONObject(i);
							AccountDetail detail = new AccountDetail();
							detail.setCreateTime(object.optString("createTime", "0"));
							detail.setTypeDes(object.optString("typeDes", ""));
							detail.setInOut(object.optString("inOut", ""));
							detail.setMoney(object.optString("money", ""));
							detail.setBalance(object.optString("balance", ""));
							detail.setDetailMsg(object.optString("detailMsg",""));
							// 充值数据解析
							detail.setAddTime(object.optLong("addTime", 0));
							detail.setAmount(object.optString("amount", ""));
							detail.setDescription(object.optString("description", ""));
							detail.setStatus(object.optString("status", ""));
							detail.setResMsg(object.optString("resMsg", ""));
							detail.setCreateTimeSys(object.optString("createTimeSys",""));
							list.add(detail);
						}
						detailList.setList(list);
						detailList.setInMoney(json.getString("inMoney"));
						detailList.setOutMoney(json.getString("outMoney"));
						detailList.setPageNo(json.getString("pageNo"));
						detailList.setTotal(json.getString("total"));
					}
				}
				detailList.setResCode(resCode);
				detailList.setResMsg(json.getString("resMsg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return detailList;
	}

	/**
	 * 提现记录
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月30日
	 */
	private static AccountDetailList parseGetLoBoCash(JSONObject json) throws CException {
		AccountDetailList detailLists = new AccountDetailList();
		List<AccountDetail> list = new ArrayList<AccountDetail>();
		try {
			if (json != null) {
				// 提现数据解析
				String resCode = json.getString("resCode");
				if(resCode != null && resCode.equals(0)){
					JSONArray otherArray = json.getJSONArray("chargeList");
					if (otherArray != null) {
						for (int i = 0; i < otherArray.length(); i++) {
							JSONObject object = otherArray.getJSONObject(i);
							AccountDetail detail = new AccountDetail();
							detail.setAddTime(object.optLong("addTime", 0));
							detail.setAmount(object.optString("amount", ""));
							detail.setDescription(object.optString("description",
									""));
							detail.setStatus(object.optString("status", ""));
							detail.setResMsg(object.optString("resMsg", ""));
							list.add(detail);
							detailLists.setList(list);
						}
					}
				}
				detailLists.setResCode(resCode);
				detailLists.setResMsg(json.getString("resMsg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return detailLists;
	}

	/**
	 * 获取投注记录
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月31日
	 */
	private static AllBettingList parseGetLoBoTzList(JSONObject json)
			throws CException {
		AllBettingList obj = null;
		obj = JSON.parseObject(json.toString(), AllBettingList.class);
		return obj == null ? new AllBettingList() : obj;
	}

	/**
	 * 投注详情
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	private static TzDetail parseGetLoBoTzDetail(JSONObject json)
			throws CException {
		TzDetail obj = null;
		obj = JSON.parseObject(json.toString(), TzDetail.class);
		return obj == null ? new TzDetail() : obj;
	}

	/**
	 * 投注追号详情
	 * 
	 * @Description:
	 * @param json
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年1月27日
	 */
	private static ZhuiDetailList parseGetLoBoTzZhuiDetail(JSONObject json)
			throws CException {
		ZhuiDetailList obj = null;
		obj = JSON.parseObject(json.toString(), ZhuiDetailList.class);
		return obj == null ? new ZhuiDetailList() : obj;
	}

	/**
	 * 竞彩投注详情
	 * 
	 * @Description:
	 * @param json
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年2月18日
	 */
	private static FootballDetailList parseGetFootballDetail(JSONObject json) {
		FootballDetailList obj = null;
		obj = JSON.parseObject(json.toString(), FootballDetailList.class);
		return obj == null ? new FootballDetailList() : obj;
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
	private static Object parseRegister(JSONObject json) throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				info.setResCode(resCode);
				info.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					info.setUserName(json.optString("userName", ""));
					info.setUserId(json.optString("userId", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	/**
	 * 登录
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
	private static UserInfo parseLogin(JSONObject json) throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				info.setResCode(resCode);
				info.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					info = JSON.parseObject(json.toString(), UserInfo.class);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	/**
	 * 找回密码
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
	private static Object parseReSetPWD(JSONObject json) throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				info.setResCode(json.optString("resCode", ""));
				info.setResMsg(json.optString("resMsg", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	private static Object parseGetBackPWD(JSONObject json) throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				info.setResCode(json.optString("resCode", ""));
				info.setResMsg(json.optString("resMsg", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	/**
	 * 获取验证码
	 * 
	 * @param json
	 * @return
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	private static GetCode parseGetCode(JSONObject json) throws CException {
		GetCode obj = null;
		obj = JSON.parseObject(json.toString(), GetCode.class);
		return obj == null ? new GetCode() : obj;
	}

	/**
	 * 验证手机号
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月4日
	 */
	private static GetCode parseVeriPhone(JSONObject json) throws CException {
		GetCode obj = null;
		obj = JSON.parseObject(json.toString(), GetCode.class);
		return obj == null ? new GetCode() : obj;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月5日
	 */
	private static UserInfo parseGetUserInfo(JSONObject json) throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				info.setResCode(resCode);
				info.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					info = JSON.parseObject(json.toString(), UserInfo.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	/**
	 * 提现
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	private static GetCode parseOutCash(JSONObject json) throws CException {
		GetCode code = new GetCode();
		try {
			if (json != null) {
				code.setResMsg(json.optString("resMsg", ""));
				code.setResCode(json.optString("resCode", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return code;
	}

	/**
	 * 获取充值渠道列表
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月10日
	 */
	private static ReChargeChannel parseGetReChargeList(JSONObject json)
			throws CException {
		ReChargeChannel channel = new ReChargeChannel();
		List<ChannelContent> contents = new ArrayList<ChannelContent>();
		List<BankInfo> bankInfos = new ArrayList<BankInfo>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				channel.setResCode(resCode);
				channel.setResMsg(json.optString("resMsg", ""));
				channel.setTimeId(json.optString("timeId", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.optJSONArray("channelList");
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject item = array.getJSONObject(i);
							ChannelContent content = new ChannelContent();
							content.setChannelId(item
									.optString("channelId", ""));
							content.setChannelCode(item.optString(
									"channelCode", ""));
							content.setChannelName(item.optString(
									"channelName", ""));
							content.setContent(item.optString("content", ""));
							content.setChannelStatus(item.optString(
									"channelStatus", ""));
							content.setSummary(item.optString("summary", ""));
							contents.add(content);
						}
					}

					JSONArray array2 = json.optJSONArray("cardList");
					for (int j = 0; j < array2.length(); j++) {
						JSONObject item1 = array2.getJSONObject(j);
						BankInfo bankInfo = new BankInfo();
						bankInfo.setBankcode(item1.optString("bankCode", ""));
						bankInfo.setBankname(item1.optString("bankName", ""));
						bankInfo.setCardNum(item1.optString("cardNum", ""));
						bankInfo.setCardType(item1.optString("cardType", ""));
						bankInfo.setCell(item1.optString("cell", ""));
						bankInfo.setCity(item1.optString("city", ""));
						bankInfo.setCityCode(item1.optString("cityCode", ""));
						bankInfo.setProvince(item1.optString("province", ""));
						bankInfo.setProvinceCode(item1.optString(
								"provinceCode", ""));
						bankInfos.add(bankInfo);
					}
				}
				channel.setContents(contents);
				channel.setBankInfos(bankInfos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return channel;
	}

	/**
	 * 完善用户资料
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月11日
	 */
	private static UserInfo parseBindUserInfo(JSONObject json)
			throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				info.setResCode(json.optString("resCode", ""));
				info.setResMsg(json.optString("resMsg", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	/**
	 * 获取银行列表
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月12日
	 */
	private static BankList parseGetBankList(JSONObject json) throws CException {
		BankList list = null;
		list = JSON.parseObject(json.toString(), BankList.class);
		return list == null ? new BankList() : list;
	}

	/**
	 * 获取省市列表
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月12日
	 */
	@SuppressLint("NewApi")
	private static ProCity parseGetProCity(JSONObject json) throws CException {
		ProCity proCity = new ProCity();
		List<Province> provinces = new ArrayList<Province>();

		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				proCity.setResCode(resCode);
				proCity.setResMsg(json.optString("resMsg", ""));
				proCity.setTimeId(json.optString("timeId", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = new JSONArray(json.optString("list"));
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						Province province = new Province();
						province.setProvince(item.optString("province", ""));
						province.setProCode(item.optString("proCode", ""));

						JSONArray array2 = item.optJSONArray("array");
						List<City> citys = new ArrayList<City>();
						for (int j = 0; j < array2.length(); j++) {
							JSONObject item2 = array2.getJSONObject(j);
							City city = new City();
							city.setCity(item2.optString("city", ""));
							city.setCityCode(item2.optString("cityCode", ""));
							citys.add(city);
						}
						province.setCitys(citys);
						provinces.add(province);
					}
				}
				proCity.setProvinces(provinces);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return proCity;
	}

	/**
	 * 绑定银行卡
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	private static UserInfo parseBindBank(JSONObject json) throws CException {
		UserInfo info = new UserInfo();
		try {
			if (json != null) {
				info.setResCode(json.optString("resCode", ""));
				info.setResMsg(json.optString("resMsg", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;
	}

	/**
	 * 获取竞彩足球对阵信息
	 * 
	 * @param json
	 * @return
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月18日
	 */
	private static FootBallMatch parseGetFootBallMatchsInfo(JSONObject json)
			throws CException {
		FootBallMatch info = new FootBallMatch();

		List<Matchs> list = new ArrayList<Matchs>();
		
		try {
			if (json != null) {
//				Log.e("qiufeng", json.toString());
				String resCode = json.optString("resCode", "");
				info.setResCode(resCode);
				info.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = new JSONArray(json.optString("list"));
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						Matchs matchs = new Matchs();
						matchs.setSpiltTime(item.optString("spiltTime", ""));

						JSONArray array2 = item.optJSONArray("matchList");
						List<Match> matchlist = new ArrayList<Match>();
						for (int j = 0; j < array2.length(); j++) {
							JSONObject item2 = array2.getJSONObject(j);
							Match match = new Match();
							match.setDxResult(item2.optString("dxResult", ""));
							match.setFcResult(item2.optString("fcResult", ""));
							match.setFullScore(item2.optString("fullScore", ""));
							match.setGameId(item2.optString("gameId", ""));
							match.setGoal(item2.optString("goal", ""));
							match.setGuestTeamId(item2.optString("guestTeamId",
									""));
							match.setGuestTeamName(item2.optString(
									"guestTeamName", ""));
							match.setHalfScore(item2.optString("halfScore", ""));
							match.setHomeTeamId(item2.optString("homeTeamId",
									""));
							match.setHomeTeamName(item2.optString(
									"homeTeamName", ""));
							match.setIssueNo(item2.optString("issueNo", ""));
							match.setLeagueShort(item2.optString("leagueShort",
									""));
							match.setMatchName(item2.optString("matchName", ""));
							match.setMatchNo(item2.optString("matchNo", ""));
							match.setMatchResult(item2.optString("matchResult",
									""));
							match.setMatchSellOutTime(item2.optLong(
									"matchSellOutTime", 0));
							match.setMatchSort(item2.optString("matchSort", ""));
							match.setMatchStatus(item2.optString("matchStatus",
									""));
							match.setMatchTimeDate(item2.optLong(
									"matchTimeDate", 0));
							match.setRfsfResult(item2.optString("rfsfResult",
									""));
							match.setSfResult(item2.optString("sfResult", ""));
							match.setTotalGoal(item2.optString("totalGoal", ""));
							match.setWeek(item2.optString("week", ""));
							match.setLetScore(item2.optString("letScore", ""));
							match.setGameNo(item2.optString("gameNo", ""));
							match.setAvgOdds(item2.optString("avgOdds", ""));
							match.setLeagueCode(item2.optString("leagueCode","0"));

							JSONArray array3 = item2.optJSONArray("matchAgainstSpInfoList");
							List<MatchAgainstSpInfo> spList = new ArrayList<MatchAgainstSpInfo>();
							for (int k = 0; k < array3.length(); k++) {
								JSONObject item3 = array3.getJSONObject(k);
								MatchAgainstSpInfo spInfo = new MatchAgainstSpInfo();
								spInfo.setPassType(item3.optString("passType",
										""));
								spInfo.setPlayMethod(item3.optString(
										"playMethod", ""));
								spInfo.setRealTimeSp(item3.optString(
										"realTimeSp", ""));
								spInfo.setUpdateTime(item3.optString(
										"updateTime", ""));
								spList.add(spInfo);
							}
							
							match.setMatchAgainstSpInfoList(spList);
							matchlist.add(match);
						}
						matchs.setMatchs(matchlist);
						list.add(matchs);
					}
					info.setList(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return info;

	}

	/**
	 * 获取对阵时间期次
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月23日
	 */
	private static IssueList parseGetMatchTime(JSONObject json)
			throws CException {
		IssueList list = new IssueList();
		List<LoBoPeriodInfo> issues = new ArrayList<LoBoPeriodInfo>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				list.setResCode(resCode);
				list.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.getJSONArray("issueInfoList");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.optJSONObject(i);
						LoBoPeriodInfo info = new LoBoPeriodInfo();
						info.setEndTime(obj.optLong("endTime", 0));
						info.setStartTime(obj.optLong("startTime", 0));
						info.setGameNo(obj.optString("gameNo", ""));
						info.setIssue(obj.optString("issue", ""));
						info.setIssueStatus(obj.optString("issueStatus", ""));
						info.setTimeStep(obj.optString("timeStep", ""));
						issues.add(info);
					}
				}
				list.setIssues(issues);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return list;
	}

	/**
	 * 充值
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月24日
	 */
	private static ReCharge parseReCharge(JSONObject json) throws CException {
		ReCharge reCharge = new ReCharge();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				reCharge.setResCode(resCode);
				reCharge.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					reCharge.setOrderNo(json.optString("orderNo", ""));
					reCharge.setTradeNo(json.optString("tradeNo", ""));
					reCharge.setReturnUrl(json.optString("returnUrl", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return reCharge;
	}

	/**
	 * 银联充值
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月25日
	 */
	private static EasyLinkReCharge parseEasyLinkReCharge(JSONObject json)
			throws CException {
		EasyLinkReCharge reCharge = new EasyLinkReCharge();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				reCharge.setResCode(resCode);
				reCharge.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					reCharge.setAmount(json.optString("amount", ""));
					reCharge.setCardNum(json.optString("cardNum", ""));
					reCharge.setDescription(json.optString("description", ""));
					reCharge.setOprTime(json.optString("oprTime", ""));
					reCharge.setOrderId(json.optString("orderId", ""));
					reCharge.setMerchantId(json.optString("merchantId", ""));
					reCharge.setSign(json.optString("sign", ""));
					reCharge.setTradeOrderId(json.optString("tradeOrderId", ""));
					reCharge.setVersion(json.optString("version", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return reCharge;
	}

	/**
	 * 获取焦点比赛
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月1日
	 */
	private static FocusMatch parseGetFocusMatch(JSONObject json)
			throws CException {
		FocusMatch fMatch = new FocusMatch();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				fMatch.setResCode(resCode);
				fMatch.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONObject item = (JSONObject) json.optJSONArray(
							"matchList").get(0);
					Match match = new Match();
					match.setGameId(item.optString("gameId", ""));
					match.setLeagueShort(item.optString("leagueShort", ""));
					match.setGuestTeamId(item.optString("guestTeamId", ""));
					match.setGuestTeamName(item.optString("guestTeamName", ""));
					match.setHomeTeamId(item.optString("homeTeamId", ""));
					match.setHomeTeamName(item.optString("homeTeamName", ""));
					match.setIssueNo(item.optString("issueNo", ""));
					match.setMatchName(item.optString("matchName", ""));
					match.setMatchNo(item.optString("matchNo", ""));
					match.setMatchSort(item.optString("matchSort", ""));
					match.setMatchSellOutTime(item.optLong("matchSellOutTime", 0));
					match.setWeek(item.optString("week", ""));

					MatchAgainstSpInfo spInfo = new MatchAgainstSpInfo();
					spInfo.setPassType(item.optString("passType", ""));
					spInfo.setPlayMethod(item.optString("playMethod", ""));
					spInfo.setRealTimeSp(item.optString("realTimeSp", ""));
					spInfo.setUpdateTime(item.optString("updateTime", ""));
					spInfo.setLetScoreMinSp(item.optString("letScoreMinSp","0"));

					fMatch.setMatch(match);
					fMatch.setSpInfo(spInfo);
					
					fMatch.setGameIdMinSp(item.optString("gameIdMinSp", ""));
					fMatch.setLeagueShortMinSp(item.optString("leagueShortMinSp", ""));
					fMatch.setGuestTeamIdMinSp(item.optString("guestTeamIdMinSp", ""));
					fMatch.setGuestTeamNameMinSp(item.optString("guestTeamNameMinSp", ""));
					fMatch.setHomeTeamIdMinSp(item.optString("homeTeamIdMinSp", ""));
					fMatch.setHomeTeamNameMinSp(item.optString("homeTeamNameMinSp", ""));
					fMatch.setIssueNoMinSp(item.optString("issueNoMinSp", ""));
					fMatch.setMatchNameMinSp(item.optString("matchNameMinSp", ""));
					fMatch.setMatchNoMinSp(item.optString("matchNoMinSp", ""));
					fMatch.setMatchSortMinSp(item.optString("matchSortMinSp", ""));
					fMatch.setMatchSellOutTimeMinSp(item.optLong("matchSellOutTimeMinSp", 0));
					fMatch.setWeekMinSp(item.optString("weekMinSp", ""));
					
					fMatch.setPlayMethodMinSp(item.optString("playMethodMinSp", ""));
					fMatch.setRealTimeSpMinSp(item.optString("realTimeSpMinSp", ""));
					fMatch.setPassTypeMinSp(item.optString("passTypeMinSp", ""));
					fMatch.setUpdateTimeMinSp(item.optString("updateTimeMinSp", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return fMatch;
	}

	/**
	 * 极光push注册
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月2日
	 */
	private static GetCode parseRegisterPush(JSONObject json) throws CException {
		GetCode code = new GetCode();
		try {
			if (json != null) {
				code.setResMsg(json.optString("resMsg", ""));
				code.setResCode(json.optString("resCode", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return code;
	}

	/**
	 * 比分-关注赛事
	 * 
	 * @param json
	 * @return
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	private static GetCode parseWatchMatch(JSONObject json) throws CException {
		GetCode code = new GetCode();
		try {
			if (json != null) {
				code.setResMsg(json.optString("resMsg", ""));
				code.setResCode(json.optString("resCode", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return code;
	}

	/**
	 * 比分-对阵列表
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	private static ScoreList parseGetScoreListMatch(JSONObject json)
			throws CException {
		ScoreList score = new ScoreList();
		List<ScoreListObj> list = new ArrayList<ScoreListObj>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				score.setResCode(resCode);
				score.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.optJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						ScoreListObj obj = new ScoreListObj();
						obj.setRecord(item.optString("record", ""));
						obj.setStatus(item.optString("status", ""));
						obj.setTime(item.optString("time", ""));
						obj.setWatchStatus(item.optString("watchStatus", ""));
						list.add(obj);
					}
					score.setList(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return score;
	}

	private static ScoreDetailGame parseGetScoreDetailGame(JSONObject json)
			throws CException {
		ScoreDetailGame game = new ScoreDetailGame();
		List<ScoreDetailGameEvent> list = new ArrayList<ScoreDetailGameEvent>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				game.setResCode(resCode);
				game.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.optJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						ScoreDetailGameEvent event = new ScoreDetailGameEvent();
						JSONObject item = array.getJSONObject(i);
						event.setEvent(item.optString("event", ""));
						list.add(event);
					}
					game.setList(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return game;

	}

	/**
	 * 比分-详情-分析
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	private static ScoreDetailAnalysis parseGetScoreDetailAnalysis(
			JSONObject json) throws CException {
		ScoreDetailAnalysis analysis = new ScoreDetailAnalysis();
		// 主队积分排名
		List<HostRankObj> hostRankList = new ArrayList<HostRankObj>();
		// 客队积分排名
		List<GuestRankObj> guestRankList = new ArrayList<GuestRankObj>();
		// 主队近期战绩
		List<HostNearObj> hostNearList = new ArrayList<HostNearObj>();
		// 客队近期战绩
		List<GuestNearObj> guestNearList = new ArrayList<GuestNearObj>();
		// 双方交战
		List<VsObj> vsList = new ArrayList<VsObj>();
		// 主队未来赛事
		List<HostFutureObj> hostFutureVsList = new ArrayList<HostFutureObj>();
		// 客队未来赛事
		List<GuestFutureObj> guestFutureVsList = new ArrayList<GuestFutureObj>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				analysis.setResCode(resCode);
				analysis.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					analysis.setBothSideResult(json.optString("bothSideResult",
							""));
					analysis.setHostNearResult(json.optString("hostNearResult",
							""));
					analysis.setGuestNearResult(json.optString(
							"guestNearResult", ""));

					JSONArray array = json.optJSONArray("hostsRankList");
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject hostRankItem = array.getJSONObject(i);
							HostRankObj hostRankObj = new HostRankObj();
							hostRankObj.setHostAllResult(hostRankItem
									.optString("hostAllResult", ""));
							hostRankObj.setHostGResult(hostRankItem.optString(
									"hostGResult", ""));
							hostRankObj.setHostHResult(hostRankItem.optString(
									"hostHResult", ""));
							hostRankObj.setHostRankResult(hostRankItem
									.optString("hostRankResult", ""));
							hostRankList.add(hostRankObj);
						}
					}

					JSONArray array1 = json.optJSONArray("guestRankList");
					if (array1 != null) {
						for (int i = 0; i < array1.length(); i++) {
							JSONObject guestRankItem = array1.getJSONObject(i);
							GuestRankObj guestRankObj = new GuestRankObj();
							guestRankObj.setGuestAllResult(guestRankItem
									.optString("guestAllResult", ""));
							guestRankObj.setGuestGResult(guestRankItem
									.optString("guestGResult", ""));
							guestRankObj.setGuestHResult(guestRankItem
									.optString("guestHResult", ""));
							guestRankObj.setGuestRankResult(guestRankItem
									.optString("guestRankResult", ""));
							guestRankList.add(guestRankObj);
						}
					}

					JSONArray array2 = json.optJSONArray("hostNList");
					if (array2 != null) {
						for (int i = 0; i < array2.length(); i++) {
							JSONObject hostNearItem = array2.getJSONObject(i);
							HostNearObj hostNearObj = new HostNearObj();
							hostNearObj.setHostNearDuiZhen(hostNearItem
									.optString("hostNearDuiZhen", ""));
							hostNearObj.setHostNearMatchName(hostNearItem
									.optString("hostNearMatchName", ""));
							hostNearObj.setHostNearMatchResult(hostNearItem
									.optString("hostNearMatchResult", ""));
							hostNearObj.setHostNearMatchTime(hostNearItem
									.optString("hostNearMatchTime", ""));
							hostNearList.add(hostNearObj);
						}
					}

					JSONArray array3 = json.optJSONArray("guestNList");
					if (array3 != null) {
						for (int i = 0; i < array3.length(); i++) {
							JSONObject guestNearItem = array3.getJSONObject(i);
							GuestNearObj guestNearObj = new GuestNearObj();
							guestNearObj.setGuestNearDuiZhen(guestNearItem
									.optString("guestNearDuiZhen", ""));
							guestNearObj.setGuestNearMatchName(guestNearItem
									.optString("guestNearMatchName", ""));
							guestNearObj.setGuestNearMatchResult(guestNearItem
									.optString("guestNearMatchResult", ""));
							guestNearObj.setGuestNearMatchTime(guestNearItem
									.optString("guestNearMatchTime", ""));
							guestNearList.add(guestNearObj);
						}
					}

					JSONArray array4 = json.optJSONArray("bothSideList");
					if (array4 != null) {
						for (int i = 0; i < array4.length(); i++) {
							JSONObject vsItem = array4.getJSONObject(i);
							VsObj vsObj = new VsObj();
							vsObj.setDuizhen(vsItem.optString("duizhen", ""));
							vsObj.setMatchName(vsItem
									.optString("matchName", ""));
							vsObj.setMatchResult(vsItem.optString(
									"matchResult", ""));
							vsObj.setMatchTime(vsItem
									.optString("matchTime", ""));
							vsList.add(vsObj);
						}
					}

					JSONArray array5 = json.optJSONArray("hostsFutureList");
					if (array5 != null) {
						for (int i = 0; i < array5.length(); i++) {
							JSONObject hostFutureItem = array5.getJSONObject(i);
							HostFutureObj hostFutureObj = new HostFutureObj();
							hostFutureObj.setHostFutureDZ(hostFutureItem
									.optString("hostFutureDZ", ""));
							hostFutureObj.setHostFutureJG(hostFutureItem
									.optString("hostFutureJG", ""));
							hostFutureObj.setHostFutureName(hostFutureItem
									.optString("hostFutureName", ""));
							hostFutureObj.setHostFutureTime(hostFutureItem
									.optString("hostFutureTime", ""));
							hostFutureVsList.add(hostFutureObj);
						}
					}

					JSONArray array6 = json.optJSONArray("guestFutureList");
					if (array6 != null) {
						for (int i = 0; i < array6.length(); i++) {
							JSONObject guestFutureItem = array6
									.getJSONObject(i);
							GuestFutureObj guestFutureObj = new GuestFutureObj();
							guestFutureObj.setGuestFutureDZ(guestFutureItem
									.optString("guestFutureDZ", ""));
							guestFutureObj.setGuestFutureJG(guestFutureItem
									.optString("guestFutureJG", ""));
							guestFutureObj.setGuestFutureName(guestFutureItem
									.optString("guestFutureName", ""));
							guestFutureObj.setGuestFutureTime(guestFutureItem
									.optString("guestFutureTime", ""));
							guestFutureVsList.add(guestFutureObj);
						}
					}

					analysis.setHostRankList(hostRankList);
					analysis.setGuestRankList(guestRankList);
					analysis.setHostNearList(hostNearList);
					analysis.setGuestNearList(guestNearList);
					analysis.setVsList(vsList);
					analysis.setHostFutureVsList(hostFutureVsList);
					analysis.setGuestFutureVsList(guestFutureVsList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return analysis;
	}

	/**
	 * 比分-详情-亚赔
	 * 
	 * @param json
	 * @return
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	private static ScoreDetailOddsList parseGetScoreDetailAsia(JSONObject json)
			throws CException {
		ScoreDetailOddsList odd = new ScoreDetailOddsList();
		List<ScoreDetailOdds> list = new ArrayList<ScoreDetailOdds>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				odd.setResCode(resCode);
				odd.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.optJSONArray("asiaList");
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						ScoreDetailOdds obj = new ScoreDetailOdds();
						obj.setChangeOdds(item.optString("changeOdds", ""));
						obj.setCompany(item.optString("company", ""));
						obj.setCompanyId(item.optString("companyId", ""));
						obj.setInitOdds(item.optString("initOdds", ""));
						list.add(obj);
					}
					odd.setList(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return odd;
	}

	/**
	 * 比分-详情-欧赔
	 * 
	 * @param json
	 * @return
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	private static ScoreDetailOddsList parseGetScoreDetailEurope(JSONObject json)
			throws CException {
		ScoreDetailOddsList odd = new ScoreDetailOddsList();
		List<ScoreDetailOdds> list = new ArrayList<ScoreDetailOdds>();
		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				odd.setResCode(resCode);
				odd.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.optJSONArray("europeList");
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						ScoreDetailOdds obj = new ScoreDetailOdds();
						obj.setChangeOdds(item.optString("changeOdds", ""));
						obj.setCompany(item.optString("company", ""));
						obj.setCompanyId(item.optString("companyId", ""));
						obj.setInitOdds(item.optString("initOdds", ""));
						list.add(obj);
					}
					odd.setList(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}
		return odd;
	}

	/**
	 * 比分-公司赔率
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	private static ScoreCompanyOdds parseGetScoreCompany(JSONObject json)
			throws CException {
		ScoreCompanyOdds obj = null;
		obj = JSON.parseObject(json.toString(), ScoreCompanyOdds.class);
		return obj == null ? new ScoreCompanyOdds() : obj;
	}

	private static ScoreCompanyOdds parseGetScoreOdds(JSONObject json)
			throws CException {
		ScoreCompanyOdds obj = null;
		obj = JSON.parseObject(json.toString(), ScoreCompanyOdds.class);
		return obj == null ? new ScoreCompanyOdds() : obj;
	}

	/**
	 * 投注
	 * 
	 * @Description:
	 * @param json
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2016年1月12日
	 */
	private static Betting parseBetting(JSONObject json) throws CException {
		Betting betting = null;
		betting = JSON.parseObject(json.toString(), Betting.class);
		return betting == null ? new Betting() : betting;
	}

	private static LeMi parseGetLeMiList(JSONObject json) throws CException {
		LeMi obj = null;
		obj = JSON.parseObject(json.toString(), LeMi.class);
		return obj == null ? new LeMi() : obj;

	}

	/**
	 * 现金购买红包,乐米列表;
	 */
	private static RedPacketLeMiList getProductList(JSONObject json) {
		RedPacketLeMiList obj = null;
		obj = JSON.parseObject(json.toString(), RedPacketLeMiList.class);
		return obj == null ? new RedPacketLeMiList() : obj;
	}

	/**
	 * 获取数字彩开奖列表
	 * 
	 * @Description:
	 * @param json
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月18日
	 */
	private static LotteryList parseLotteryList(JSONObject json) {
		LotteryList obj = null;
		obj = JSON.parseObject(json.toString(), LotteryList.class);
		return obj == null ? new LotteryList() : obj;
	}

	/**
	 * 获取签到列表信息
	 * 
	 * @param json
	 * @return
	 * @throws CException
	 * 
	 * 
	 */
	private static SignList parseGetSignList(JSONObject json) throws CException {
		SignList signList = new SignList();
		List<Sign> signlists = new ArrayList<Sign>();

		try {
			if (json != null) {
				String resCode = json.optString("resCode", "");
				signList.setResCode(resCode);
				signList.setResMsg(json.optString("resMsg", ""));
				if (Integer.parseInt(resCode) == 0) {
					JSONArray array = json.optJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.optJSONObject(i);
						Sign item = new Sign();
						item.setResCode(object.optString("resCode", ""));
						item.setDay(object.optString("day", ""));
						String isSelect = object.optString("isSelect", "true");
						if (isSelect != null && isSelect.equals("true")) {
							item.setSelect(true);
						}
						signlists.add(item);
					}
					signList.setList(signlists);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CException(CException.JSONPARSEERROR);
		}

		return signList;
	}

	/**
	 * 消息推送获取／设置
	 * 
	 * @Description:
	 * @param json
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-6-24
	 */
	private static PushResult parsePushSet(JSONObject json) {
		PushResult obj = null;
		obj = JSON.parseObject(json.toString(), PushResult.class);
		return obj == null ? new PushResult() : obj;
	}
	
	/**
	 * 获取未读站内信的数量
	 * @param json
	 * @return
	 */
	private static NoticeOrMessageCount parseNewNoticeCount(JSONObject json) { 
		NoticeOrMessageCount obj = null;
		obj = JSON.parseObject(json.toString(), NoticeOrMessageCount.class);
		return obj == null ? new NoticeOrMessageCount() : obj;
	}
	
	/**
	 * 分享
	 * @Description:
	 * @param json
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-8-5
	 */
	private static ShareObj parseShare(JSONObject json) {
		ShareObj obj = null;
		obj = JSON.parseObject(json.toString(), ShareObj.class);
		return obj == null ? new ShareObj() : obj;
	}
	
	/**
	 * 
	 * @Description:
	 * @param json
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-8-5
	 */
	private static ShareAddLeMiObj parseShareAddLeMi(JSONObject json) {
		ShareAddLeMiObj obj = null;
		obj = JSON.parseObject(json.toString(), ShareAddLeMiObj.class);
		return obj == null ? new ShareAddLeMiObj() : obj;
	}
	
	/**
	 * 获取一场制胜系统匹配对阵
	 * @Description:
	 * @param json
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016-10-19
	 */
	private static ResMatchAgainstInfo parseOneWinSys(JSONObject json) {
		ResMatchAgainstInfo obj = null;
		obj = JSON.parseObject(json.toString(), ResMatchAgainstInfo.class);
		return obj == null ? new ResMatchAgainstInfo() : obj;
	}
}
