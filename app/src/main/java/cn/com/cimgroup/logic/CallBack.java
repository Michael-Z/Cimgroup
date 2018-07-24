package cn.com.cimgroup.logic;

import java.util.List;

import cn.com.cimgroup.bean.AccountDetailList;
import cn.com.cimgroup.bean.AllBetting;
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
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryDrawBasketballList;
import cn.com.cimgroup.bean.LotteryDrawDetailInfo;
import cn.com.cimgroup.bean.LotteryDrawFootballList;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.LotteryList;
import cn.com.cimgroup.bean.LotteryNOs;
import cn.com.cimgroup.bean.NoticeOrMessageCount;
import cn.com.cimgroup.bean.ProCity;
import cn.com.cimgroup.bean.PushResult;
import cn.com.cimgroup.bean.ReCharge;
import cn.com.cimgroup.bean.ReChargeChannel;
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
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.bean.ZhuiDetailList;

/**
 * 页面的回调类
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2014-12-9
 */
public abstract class CallBack {

	public void onSuccess(List<AllBetting> mClass) {
	};

	public void onFail(String error) {
	};

	public void onFinish() {
	};

	/** 获取预售期成功 **/
	public void getLoBoPeriodSuccess(List<LoBoPeriodInfo> infos) {
	};

	/** 获取预售期失败 **/
	public void getLoBoPeriodFailure(String error) {
	};

	/** 获取追号预售期成功 **/
	public void getLoBoZhuiPeriodSuccess(IssuePreList list) {
	};

	/** 获取追号预售期失败 **/
	public void getLoBoZhuiPeriodFailure(String error) {
	};

	/** 获取账户明细和充值提现记录成功 */
	public void getLoBoAccountDetailSuccess(AccountDetailList object) {
	}

	/** 获取账户明细和充值提现记录失败 */
	public void getLoBoAccountDetailFailure(String error) {
	}

	/** 获取投注记录列表成功 */
	public void getLoBoTzListSuccess(AllBettingList list) {
	}

	/** 获取投注记录列表失败 */
	public void getLoBoTzListFailure(String error) {
	}

	/** 获取投注记录详情成功 */
	public void getLoBoTzDetailSuccess(TzDetail detail) {
	}

	/** 获取投注记录详情失败 */
	public void getLoBoTzDetailFailure(String error) {
	}

	/** 获取投注记录追号详情成功 */
	public void getLoBoTzZhuiDetailSuccess(ZhuiDetailList detail) {
	}

	/** 获取投注记录追号详情失败 */
	public void getLoBoTzZhuiDetailFailure(String error) {
	}

	/** 获取投注记录竞彩投注详情成功 */
	public void getFootballDetailSuccess(FootballDetailList detail) {
	}

	/** 获取投注记录竞彩投注详情失败 */
	public void getFootballDetailFailure(String error) {
	}

	/** 注册成功 */
	public void registerSuccess(UserInfo info) {
	}

	/** 注册失败 */
	public void registerFailure(String error) {
	}

	/** 登录成功 */
	public void loginSuccess(UserInfo info) {
	}

	/** 登录失败 */
	public void loginFailure(String error) {
	}

	/** 重设密码成功 */
	public void reSetPWDSuccess(UserInfo info) {
	}

	/** 重设密码失败 */
	public void reSetPWDFailure(String error) {
	}

	/** 找回密码成功 */
	public void getBackPWDSuccess(UserInfo info) {
	}

	/** 找回密码失败 */
	public void getBackPWDFailure(String error) {
	}

	/** 获取验证码成功 */
	public void getCodeSuccess(GetCode code) {
	}

	/** 获取验证码失败 */
	public void getCodeFailure(String error) {
	}

	/** 验证手机号成功 */
	public void veriPhoneSuccess(GetCode code) {
	}

	/** 验证手机号失败 */
	public void veriPhoneFailure(String error) {
	}

	/** 获取用户信息成功 */
	public void getUserInfoSuccess(UserInfo info) {
	}

	/** 获取用户信息失败 */
	public void getUserInfoFailure(String error) {
	}

	/** 提现成功 */
	public void outCashSuccess(GetCode code) {
	}

	/** 提现失败 */
	public void outCashFailure(String error) {
	}

	/** 获取充值渠道列表成功 */
	public void getReChargeListSuccess(ReChargeChannel channel) {
	}

	/** 获取充值渠道列表失败 */
	public void getReChargeListFailure(String error) {
	}

	/** 完善用户资料成功 */
	public void bindUserInfoSuccess(UserInfo userInfo) {
	}

	/** 完善用户资料失败 */
	public void bindUserInfoFailure(String error) {
	}

	/** 获取银行列表成功 */
	public void getBankListSuccess(BankList infos) {
	}

	/** 获取银行列表失败 */
	public void getBankListFailure(String error) {
	}

	/** 获取省市列表成功 */
	public void getProCitySuccess(ProCity proCity) {
	}

	/** 获取省市列表失败 */
	public void getProCityFailure(String error) {
	}

	/** 绑定银行卡成功 */
	public void bindBankSuccess(UserInfo userInfo) {
	}

	/** 绑定银行卡失败 */
	public void bindBankFailure(String error) {
	}

	/** 获取银行卡类型成功 */
	public void bankTypeSuccess(BankType bankType) {
	}

	/** 获取银行卡类型失败 */
	public void bankTypeFailure(String error) {
	}

	/** 请求充值成功 */
	public void reChargeSuccess(ReCharge reCharge) {
	}

	/** 请求充值失败 */
	public void reChargeFailure(String error) {
	}

	/** 请求银联充值成功 */
	public void easyLinkReChargeSuccess(EasyLinkReCharge reCharge) {
	}

	/** 请求银联充值失败 */
	public void easyLinkReChargeFailure(String error) {
	}

	/** 极光push注册成功 */
	public void registerPushSuccess(GetCode code) {
	}

	/** 极光push注册失败 */
	public void registerPushFailure(String error) {
	}

	/** 比分-关注赛事成功 */
	public void watchMatchSuccess(GetCode code) {
	}

	/** 比分-关注赛事失败 */
	public void watchMatchFailure(String error) {
	}

	/** 比分-对阵列表成功 */
	public void getScoreListSuccess(ScoreList list) {
	}

	/** 比分-对阵列表失败 */
	public void getScoreListFailure(String error) {
	}
	
	/** 比分-对阵详情成功 */
	public void getScoreDetailSuccess(ScoreObj obj) {
	}

	/** 比分-对阵详情失败 */
	public void getScoreDetailFailure(String error) {
	}


	/** 比分-详情-赛事成功 */
	public void getScoreDetailGameSuccess(ScoreDetailGame game) {
	}

	/** 比分-详情-赛事失败 */
	public void getScoreDetailGameFailure(String error) {
	}

	/** 比分-详情-分析成功 */
	public void getScoreDetailAnalysisSuccess(ScoreDetailAnalysis analysis) {
	}

	/** 比分-详情-分析失败 */
	public void getScoreDetailAnalysisFailure(String error) {
	}

	/** 比分-详情-亚赔成功 */
	public void getScoreDetailAsiaSuccess(ScoreDetailOddsList list) {
	}

	/** 比分-详情-亚赔失败 */
	public void getScoreDetailAsiaFailure(String error) {
	}

	/** 比分-详情-欧赔成功 */
	public void getScoreDetailEuropeSuccess(ScoreDetailOddsList list) {
	}

	/** 比分-详情-欧赔失败 */
	public void getScoreDetailEuropeFailure(String error) {
	}

	/** 比分-赔率成功 */
	public void getScoreOddsSuccess(ScoreCompanyOdds obj) {
	}

	/** 比分-赔率失败 */
	public void getScoreOddsFailure(String error) {
	}

	/** 比分-公司成功 */
	public void getScoreCompanySuccess(ScoreCompanyOdds obj) {
	}

	/** 比分-公司失败 */
	public void getScoreCompanyFailure(String error) {
	}

	/** 获取竞彩足球对阵信息成功 */
	public void getFootBallMatchsInfoSuccess(FootBallMatch info) {
	}

	/** 获取竞彩足球对阵信息失败 */
	public void getFootBallMatchsInfoFailure(String error) {
	}

	/** 获取对阵时间期次成功 */
	public void getMatchTimeSuccess(IssueList list) {
	}

	/** 获取对阵时间期次失败 */
	public void getMatchTimeFailure(String error) {
	}

	/** 获取竞彩足球对阵信息胜平负成功 */
	public void getFootBallMatchsInfoSPFSuccess(FootBallMatch info) {
	}

	/** 获取竞彩足球对阵信息胜平负失败 */
	public void getFootBallMatchsInfoSPFFailure(String error) {
	}

	/** 获取竞彩足球对阵信息进球数成功 */
	public void getFootBallMatchsJQSSuccess(FootBallMatch info) {
	}

	/** 获取竞彩足球对阵信息进球数失败 */
	public void getFootBallMatchsJQSFailure(String error) {
	}

	/** 获取竞彩足球对阵信息半全场成功 */
	public void getFootBallMatchsBQCSuccess(FootBallMatch info) {
	}

	/** 获取竞彩足球对阵信息半全场失败 */
	public void getFootBallMatchsBQCFailure(String error) {
	}

	/** 获取竞彩足球对阵信息比分成功 */
	public void getFootBallMatchsBFSuccess(FootBallMatch info) {
	}

	/** 获取竞彩足球对阵信息比分失败 */
	public void getFootBallMatchsBFFailure(String error) {
	}

	/** 获取竞彩足球对阵信息混合成功 */
	public void getFootBallMatchsHHGGSuccess(FootBallMatch info) {
	}

	/** 获取竞彩足球对阵信息混合失败 */
	public void getFootBallMatchsHHGGError(String error) {
	}

	/** 获取老足彩对阵信息胜平负成功 */
	public void getOldFootBallMatchsInfoSPFSuccess(FootBallMatch info,
			List<LoBoPeriodInfo> infos) {
	}

	/** 获取老足彩对阵信息胜平负失败 */
	public void getOldFootBallMatchsInfoSPFFailure(String error) {
	}

	/** 获取老足彩对阵信息进球数成功 */
	public void getOldFootBallMatchsJQSSuccess(FootBallMatch info,
			List<LoBoPeriodInfo> infos) {
	}

	/** 获取老足彩对阵信息进球数失败 */
	public void getOldFootBallMatchsJQSFailure(String error) {
	}

	/** 获取老足彩对阵信息半全场成功 */
	public void getOldFootBallMatchsBQCSuccess(FootBallMatch info,
			List<LoBoPeriodInfo> infos) {
	}

	/** 获取老足彩对阵信息半全场失败 */
	public void getOldFootBallMatchsBQCFailure(String error) {
	}

	/** 获取焦点比赛成功 */
	public void getFocusMatchSuccess(FocusMatch fMatch) {
	}

	/** 获取焦点比赛失败 */
	public void getFocusMatchFailure(String error) {
	}

	/** 投注成功 */
	public void syncLotteryBettingSuccess(Betting betting) {
	}

	/** 投注失败 */
	public void syncLotteryBettingFailure(String error) {
	}

	/** 获取数字彩开奖列表成功 */
	public void getLotteryListSuccess(LotteryList list) {
	}

	/** 获取数字彩开奖列表失败 */
	public void getLotteryListFailure(String error) {
	}

	/** 获取开奖列表成功 */
	public void syncLotteryDrawSuccess(List<LotteryDrawInfo> lotteryDrawInfos) {
	}

	/** 获取开奖列表失败 */
	public void syncLotteryDrawError(String error) {
	};

	/** 获取开奖详情成功 */
	public void syncLotteryDrawInfoSuccess(LotteryDrawDetailInfo info) {
	}

	/** 获取开奖详情失败 */
	public void syncLotteryDrawInfoError(String error) {
	};

	/** 获取竞彩足球开奖列表成功 */
	public void syncLotteryDrawFootballListSuccess(LotteryDrawFootballList info) {
	}

	/** 获取竞彩足球开奖列表失败 */
	public void syncLotteryDrawFootballListError(String error) {
	};

	/** 获取头部广告成功 */
	public void getHallAdSuccess(HallAd ad) {
	}

	/** 获取头部广告失败 */
	public void getHallAdError(String error) {
	};

	/** 获取弹出公告成功 */
	public void getHallNoticeSuccess(HallNotice notice) {
	}

	/** 获取弹出公告失败 */
	public void getHallNoticeError(String error) {
	};

	/** 获取软件版本升级成功 */
	public void getHallUpgradeSuccess(Upgrade upgrade) {
	}

	/** 获取软件版本升级失败 */
	public void getHallUpgradeError(String error) {
	};

	/** 获取彩种列表控制成功 */
	public void getHallControllLotterySuccess(ControllLottery cLottery) {
	}

	/** 获取彩种列表控制失败 */
	public void getHallControllLotteryError(String error) {
	};

	/** 获取签到列表成功 */
	public void getSignListSuccess(SignList signList) {
	}

	/** 获取签到列表失败 */
	public void getSignListError(String error) {
	};

	/** 签到成功 */
	public void signSuccess(SignStatus status) {
	}

	/** 签到失败 */
	public void signError(String error) {
	};

	/** 获取竞彩篮球对阵信息胜平负成功 */
	public void getBasketballMatchsInfoSPFSuccess(FootBallMatch info) {
	}

	/** 获取竞彩篮球对阵信息胜平负失败 */
	public void getBasketballMatchsInfoSPFFailure(String error) {
	}

	/** 获取竞彩篮球对阵信息大小分成功 */
	public void getBasketballMatchsDXSuccess(FootBallMatch info) {
	}

	/** 获取竞彩篮球对阵信息大小分失败 */
	public void getBasketballMatchsDXFailure(String error) {
	}

	/** 获取竞彩篮球对阵信息胜分差成功 */
	public void getBasketballMatchsSFCSuccess(FootBallMatch info) {
	}

	/** 获取竞彩篮球对阵信息胜分差失败 */
	public void getBasketballMatchsSFCFailure(String error) {
	}

	/** 获取竞彩篮球对阵信息混合成功 */
	public void getBasketballMatchsHHGGSuccess(FootBallMatch info) {
	}

	/** 获取竞彩篮球对阵信息混合失败 */
	public void getBasketballMatchsHHGGError(String error) {
	}

	public void onSuccess(Object t) {
	}

	public void onSuccess(Object t, String... params) {
	}

	public void onFail(String error, String... params) {
	}

	/** 获取期次信息成功 */
	public void getLotteryNoSuccess(LotteryNOs lotteryNOs) {
	}

	/** 获取期次信息失败 */
	public void getLotteryNoError(String message) {

	}

	/** 获取商户信息成功 */
	public void getAppIdSuccess(AppIdBean appIdBean) {
	}

	/** 获取商户信息失败 */
	public void getAppIdError(String message) {
	}

	/** 微信登陆 -验证用户信息 (登陆) 成功 */
	public void weChatLoadSuccess(String json) {
	}

	/** 微信登陆 -验证用户信息 (登陆) 失败 */
	public void weChatLoadError(String message) {
	}

	/** 微信登陆 -验证手机号 成功 */
	public void weChatBindPhoneSuccess(String json) {
	}

	/** 微信登陆 -验证手机号 失败 */
	public void weChatBindPhoneError(String message) {
	}

	/** 微信登陆 -注册新用户 成功 */
	public void weChatRegistUserSuccess(String json) {
	}

	/** 微信登陆 -注册新用户 失败 */
	public void weChatRegistUserError(String message) {
	}

	/** 微信充值-订单提交成功 */
	public void weChatRechargeSuccess(String json) {

	}

	/** 微信充值-订单提交失败 */
	public void weChatRechargeError(String message) {

	}

	/** 投注成功或支付成功 */
	public void bettingPaySuccess(Betting obj) {

	}

	/** 投注或支付失败 */
	public void bettingPayError(String message) {

	}

	/** 再次支付成功 */
	public void orderAgainPaySuccess(Betting obj) {

	}

	/** 再次支付失败 */
	public void orderAgainPayError(String message) {

	}

	/** 获取竞彩篮球开奖列表成功 */
	public void syncLotteryDrawBasketballListSuccess(
			LotteryDrawBasketballList info) {
	}

	/** 获取竞彩拉牛开奖列表失败 */
	public void syncLotteryDrawBasketballListError(String error) {
	};

	/** 消息推送获取／设置成功 */
	public void pushSetSuccess(PushResult obj) {

	}

	/** 消息推送获取／设置失败 */
	public void pushSetError(String message) {

	}

	/** mac信息发送成功 */
	public void sendMaxInfoFailure(String message) {

	}

	/** mac信息发送失败 */
	public void sendMaxInfoSuccess(String str) {

	}

	/** 使用openid进行微信登陆成功 **/
	public void loginWithOpenidSuccess(String json) {
	}

	/** 使用openid进行微信登陆失败 **/
	public void loginWithOpenidFailure(String json) {
	}

	/** 获取未读站内信数量成功 **/
	public void getNewNoticeOrMessageSuccess(NoticeOrMessageCount count) {
	}

	/** 获取未读站内信数量失败 **/
	public void getNewNoticeOrMessageFailure(String error) {

	}

	/** 获取竞猜游戏比赛成功 */
	public void getMatchGameInfoSuccess(String json) {
	}

	/** 获取竞猜游戏比赛失败 */
	public void getMatchGameInfoFailure(String message) {
	}

	/** 获取本场比赛评论成功 */
	public void getMatchMessageSuccess(String json) {
	}

	/** 获取本场比赛评论失败 */
	public void getMatchMessageFailure(String error) {
	}

	/** 成功返回结果String类型回调 */
	public void resultSuccessStr(String json) {
	}

	/** 错误返回结果回调 */
	public void resultFailure(String error) {
	}

	/** 分享成功回调 */
	public void shareSuccess(ShareObj obj) {
	}

	/** 分享失败回调 */
	public void shareFailure(String error) {
	}

	/** 分享增加乐米成功回调 */
	public void shareAddLeMiSuccess(ShareAddLeMiObj obj) {
	}

	/** 分享增加乐米失败回调 */
	public void shareAddLeMiFailure(String error) {
	}
	/**获取游戏大厅游戏列表成功*/
	public void getGameListSuccess(String json) {

	}
	/**获取游戏大厅游戏列表失败*/
	public void getGameListFailure(String message) {

	}
	
	/**获取七牛需要参数成功*/
	public void getQiNiuParamsSuccess(String json) {
		
	}
	/**获取七牛需要参数失败*/
	public void getQiNiuParamsFailure(String message) {
		
	}
	
	/**修改用户头像成功*/
	public void updateUserHeaderImageSuccessStr(String json) {
	}
	/**修改用户头像失败*/
	public void updateUserHeaderImageFailure(String message) {
	}
	
	/** 获取老足彩对阵信息进球数成功 */
	public void getFootBallMatchsOneWinSuccess(FootBallMatch info,
			List<LoBoPeriodInfo> infos) {
	}

	/** 获取老足彩对阵信息进球数失败 */
	public void getFootBallMatchsOneWinFailure(String error) {
	}

	/** 获取老足彩对阵信息进球数成功 */
	public void getFootBallMatchs2x1Success(FootBallMatch info) {
	}
	
	/** 获取老足彩对阵信息进球数失败 */
	public void getFootBallMatchs2x1Failure(String error) {
	}

	/**获取一场制胜系统匹配对阵成功*/
	public void getOneWinSysSuccess(ResMatchAgainstInfo json) {

	}
	/**获取一场制胜系统匹配对阵失败*/
	public void getOneWinSysFailure(String message) {

	}
	/**获取竞猜信息对阵详情成功*/
	public void getLeagueDetailSuccess(String json) {
		
	}
	/**获取竞猜信息对阵详情失败*/
	public void getLeagueDetailFailure(String message) {
		
	}
	
	/**首页轮播文字成功*/
	public void getHallTextSuccess(HallTextList list) {
		
	}
	/**首页轮播文字失败*/
	public void getHallTextFailure(String message) {
		
	}
	
	/** 验证验证码成功**/
	public void validateCodeSuccess(String json){
		
	}
	/** 验证码获取失败**/
	public void validateCodeFail(String msg){
		
	}
	/** 获取用户竞猜胜率成功 */
	public void getMatchGuessWinningSuccess(String json) {
		
	}
	/** 获取用户竞猜胜率成失败*/
	public void getMatchGuessWinningFail(String message) {
		
	}
}
