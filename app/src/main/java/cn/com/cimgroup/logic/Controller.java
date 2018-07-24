package cn.com.cimgroup.logic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.protocol.Protocol;
import cn.com.cimgroup.xutils.ActivityManager;

/**
 * 程序控制器层 activity、frament的所有数据资源以及请求都须通过此类进行衔接与其他各层交互
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2014-12-5
 */
public class Controller implements Runnable {

	/**
	 * 计数器
	 **/
	private static AtomicInteger sequencing = new AtomicInteger(0);
	/**
	 * 线程池管理
	 **/
	private final ExecutorService mThreadPool = Executors.newCachedThreadPool();
	/**
	 * 阻塞队列用于线程按顺序执行
	 **/
	private BlockingQueue<Command> mCommands = new PriorityBlockingQueue<Command>();
	/**
	 * 用于回调
	 **/
	private Set<CallBack> mCallbacks = new CopyOnWriteArraySet<CallBack>();

	private final Thread mThread;

	private Object object = null;

	private static Controller instance = null;

	private SharedPreferences shared = App.getInstance().getSharedPreferences(
			GlobalConstants.PATH_SHARED_MAC,
			android.content.Context.MODE_PRIVATE);

	/** 用户的登陆方式 **/
	private int loginPattern;
	/** 用户的openid 用于微信自动登录 **/
	private String openId;
	private Boolean auto;

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	protected Controller() {
		mThread = new Thread(this);
		mThread.setName("Controller");
		mThread.start();
	}

	String xLogName = Controller.class.getSimpleName();

	private void execute(final Runnable runnable) {

		mThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 向对首加入任务
	 * 
	 * @param description
	 * @param runnable
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-5-14
	 */
	protected void put(String description, Runnable runnable) {
		putCommand(mCommands, description, runnable, true);
	}

	/**
	 * 向对尾加入任务
	 * 
	 * @param description
	 * @param runnable
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-5-14
	 */
	protected void putBackground(String description, Runnable runnable) {
		putCommand(mCommands, description, runnable, false);
	}

	/**
	 * 向队列中添加任务，如果队列中有此条记录，那么不执行
	 * 
	 * @param queue
	 * @param description
	 * @param runnable
	 * @param isForeground
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-5-14
	 */
	private void putCommand(BlockingQueue<Command> queue, String description,
			Runnable runnable, boolean isForeground) {
		int retries = 3;
		Exception e = null;
		while (retries-- > 0) {
			try {
				for (Command command : queue) {
					if (command.description.equals(description)) {
						return;
					}
				}
				Command command = new Command();
				command.runnable = runnable;
				command.description = description;
				command.isForeground = isForeground;
				queue.put(command);
				return;
			} catch (InterruptedException ie) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ne) {
				}
				e = ie;
			}
		}
		throw new Error(e);
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			Command command = mCommands.peek();
			try {
				if (command == null) {
					command = mCommands.take();
				}
				if (command != null) {
					// WeMailDebug.d("will execute command --> " +
					// commandDescription);
					command.runnable.run();
				}
			} catch (Exception e) {
				// WeMailDebug.e("Error running command '" + commandDescription,
				// e);
			} finally {
				if (command != null) {
					mCommands.remove(command);
				}
			}
		}
	}

	/**
	 * 后台任务<br>
	 * 为了优化，如果队列中有同样的操作，则不再添加。<br>
	 * 通过description判断任务是否相同
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
	 * @Date:2014-12-11
	 */
	class Command implements Comparable<Command> {

		public Runnable runnable;
		public String description;
		boolean isForeground;
		int sequence = sequencing.getAndIncrement();

		@Override
		public int compareTo(Command other) {
			if (other.isForeground && !isForeground) {
				return 1;
			} else if (!other.isForeground && isForeground) {
				return -1;
			} else {
				return (sequence - other.sequence);
			}
		}
	}

	/**
	 * 获取全部回调
	 * 
	 * @param callback
	 * @return
	 * @Description:
	 * @see:
	 * @since:
	 * @author: xulei
	 * @date:Sep 17, 2013
	 */
	public Set<CallBack> getCallbacks(CallBack callback) {
		if (callback == null) {
			return mCallbacks;
		}
		Set<CallBack> callbacks = new HashSet<CallBack>(mCallbacks);
		callbacks.add(callback);
		return callbacks;
	}

	/**
	 * 增加回调
	 * 
	 * @param callback
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2015-1-7
	 */
	public void addCallback(CallBack callback) {
		mCallbacks.add(callback);
	}

	/**
	 * 移出回调
	 * 
	 * @param callback
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2015-1-7
	 */
	public void removeCallback(CallBack callback) {
		mCallbacks.remove(callback);
	}
	
	/**
	 * 移除所有的回调
	 * @param callback
	 */
	public void clearCallback(){
		mCallbacks.clear();
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

	/**
	 * 获得当前销售期次请求
	 * 
	 * @param gameNo
	 *            全部彩种传All，其余都传彩种编号
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月29日
	 */
	public void getLoBoPeriod(final String num, final String gameNo,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				List<LoBoPeriodInfo> infos = null;
				try {
					infos = Protocol.getInstance().getLoBoPeriod(num, gameNo);
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoPeriodSuccess(infos);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoPeriodFailure(e.getMessage());
					}
				}
			}
		});
	}

	public void getLoBoZhuiPeriod(final String num, final String gameNo,
			final String issue, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				IssuePreList list = null;
				try {
					list = Protocol.getInstance().getLoBoZhuiPeriod(num,
							gameNo, issue);
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoZhuiPeriodSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoZhuiPeriodFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取数字彩开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param gameNo
	 * @param pageNo
	 * @param pageAmount
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年1月18日
	 */
	public void getLotteryList(final String num, final String lotteryNo,
			final String pageNo, final String pageAmount, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				LotteryList list = null;
				try {
					list = Protocol.getInstance().getLotteryList(num,
							lotteryNo, pageNo, pageAmount);
					for (CallBack back : getCallbacks(mBack)) {
						back.getLotteryListSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLotteryListFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 账户明细
	 * 
	 * @param num
	 * @param oprType
	 * @param pageAmount
	 * @param pageNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月30日
	 */
	public void getLoBoAccountDetail(final String num, final String userid,
			final String oprType, final String pageAmount, final String pageNo,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				AccountDetailList list = null;
				try {
					list = Protocol.getInstance().getLoBoAccountDetail(num,
							userid, oprType, pageAmount, pageNo);
					object = list;
					if (list != null && list.getResCode() != null
							&& list.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getLoBoAccountDetail(
																	num,
																	userid,
																	oprType,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoAccountDetailSuccess((AccountDetailList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getLoBoAccountDetail(
																	num,
																	userid,
																	oprType,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoAccountDetailSuccess((AccountDetailList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}
					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getLoBoAccountDetailSuccess(list);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoAccountDetailFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取投注记录
	 * 
	 * @param gameNo
	 * @param num
	 * @param userid
	 * @param pageAmount
	 * @param pageNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月31日
	 */
	public void getLoBoTzList(final String gameNo, final String num,
			final String userid, final String onlyWin, final String pageAmount,
			final String pageNo, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				AllBettingList list = null;
				try {
					list = Protocol.getInstance().getLoBoTzList(gameNo, num,
							userid, onlyWin, pageAmount, pageNo);
					object = list;
					if (list != null && list.getResCode() != null
							&& list.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getLoBoTzList(
																	gameNo,
																	num,
																	userid,
																	onlyWin,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoTzListSuccess((AllBettingList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getLoBoTzList(
																	gameNo,
																	num,
																	userid,
																	onlyWin,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoTzListSuccess((AllBettingList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getLoBoTzListSuccess(list);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoTzListFailure(e.getMessage());
					}
				}
			}
		});
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
	public void getLoBoTzDetail(final String orderid, final String num,
			final String userid, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				TzDetail detail = null;
				try {
					detail = Protocol.getInstance().getLoBoTzDetail(orderid,
							num, userid);
					object = detail;
					if (detail != null && detail.getResCode() != null
							&& detail.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getLoBoTzDetail(
																	orderid,
																	num, userid);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoTzDetailSuccess((TzDetail) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getLoBoTzDetail(
																	orderid,
																	num, userid);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoTzDetailSuccess((TzDetail) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getLoBoTzDetailSuccess(detail);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoTzDetailFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 投注追号详情
	 * 
	 * @Description:
	 * @param chaseId
	 * @param num
	 * @param userid
	 * @param pageNo
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年1月27日
	 */
	public void getLoBoTzZhuiDetail(final String chaseId, final String num,
			final String userid, final String pageNo, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ZhuiDetailList detail = null;
				try {
					detail = Protocol.getInstance().getLoBoTzZhuiDetail(
							chaseId, num, userid, pageNo);
					object = detail;
					if (detail != null && detail.getResCode() != null
							&& detail.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getLoBoTzZhuiDetail(
																	chaseId,
																	num,
																	userid,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoTzZhuiDetailSuccess((ZhuiDetailList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getLoBoTzZhuiDetail(
																	chaseId,
																	num,
																	userid,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getLoBoTzZhuiDetailSuccess((ZhuiDetailList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getLoBoTzZhuiDetailSuccess(detail);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLoBoTzZhuiDetailFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 竞彩投注详情
	 * 
	 * @Description:
	 * @param orderid
	 * @param num
	 * @param userid
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年2月18日
	 */
	public void getFootballDetail(final String orderid, final String num,
			final String userid, final String gameNo, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				FootballDetailList detail = null;
				try {
					detail = Protocol.getInstance().getFootballDetail(orderid,
							num, userid, gameNo);
					object = detail;
					if (detail != null && detail.getResCode() != null
							&& detail.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getFootballDetail(
																	orderid,
																	num,
																	userid,
																	gameNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getFootballDetailSuccess((FootballDetailList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getFootballDetail(
																	orderid,
																	num,
																	userid,
																	gameNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getFootballDetailSuccess((FootballDetailList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getFootballDetailSuccess(detail);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getFootballDetailFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 注册
	 * 
	 * @param num
	 * @param userName
	 * @param mobile
	 * @param password
	 * @param code
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public void register(final String num, final String userName,
			final String mobile, final String password, final String code,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo info = null;
				try {
					info = Protocol.getInstance().register(num, userName,
							mobile, password, code);
					for (CallBack back : getCallbacks(mBack)) {
						back.registerSuccess(info);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.registerFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 登录
	 * 
	 * @param num
	 * @param userName
	 * @param password
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public void login(final String num, final String userName,
			final String password, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo info = null;
				try {
					info = Protocol.getInstance()
							.login(num, userName, password);
					for (CallBack back : getCallbacks(mBack)) {
						back.loginSuccess(info);
//						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.loginFailure(e.getMessage());
//						break;
					}
				}
			}
		});
	}

	/**
	 * 重置密码
	 * 
	 * @param num
	 * @param userName
	 * @param mobile
	 * @param password
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public void getReSetPwd(final String num, final String userId,
			final String password, final String oldPwd, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo info = null;
				try {
					info = Protocol.getInstance().getReSetPwd(num, userId,
							password, oldPwd);
					object = info;
					if (info != null && info.getResCode() != null
							&& info.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getReSetPwd(num,
																	userId,
																	password,
																	oldPwd);
													for (CallBack back : getCallbacks(mBack)) {
														back.reSetPWDSuccess((UserInfo) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getReSetPwd(num,
																	userId,
																	password,
																	oldPwd);
													for (CallBack back : getCallbacks(mBack)) {
														back.reSetPWDSuccess((UserInfo) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.reSetPWDSuccess(info);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.reSetPWDFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 找回密码
	 * 
	 * @param num
	 * @param userName
	 * @param mobile
	 * @param password
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public void getBackPWD(final String num, final String userId,
			final String cell, final String validCode, final String newPwd,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo info = null;
				try {
					info = Protocol.getInstance().getBackPWD(num, userId, cell,
							validCode, newPwd);
					object = info;
					if (info != null && info.getResCode() != null
							&& info.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getBackPWD(num,
																	userId,
																	cell,
																	validCode,
																	newPwd);
													for (CallBack back : getCallbacks(mBack)) {
														back.getBackPWDSuccess((UserInfo) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getBackPWD(num,
																	userId,
																	cell,
																	validCode,
																	newPwd);
													for (CallBack back : getCallbacks(mBack)) {
														back.getBackPWDSuccess((UserInfo) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getBackPWDSuccess(info);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getBackPWDFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取验证码
	 * 
	 * @param num
	 * @param mobile
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月3日
	 */
	public void getCode(final String num, final String mobile,
			final String codeType, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				GetCode code = null;
				try {
					code = Protocol.getInstance()
							.getCode(num, mobile, codeType);
					for (CallBack back : getCallbacks(mBack)) {
						back.getCodeSuccess(code);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getCodeFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 验证手机号
	 * 
	 * @param num
	 * @param mobile
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月4日
	 */
	public void veriPhone(final String num, final String mobile,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				GetCode code = null;
				try {
					code = Protocol.getInstance().veriPhone(num, mobile);
					for (CallBack back : getCallbacks(mBack)) {
						back.veriPhoneSuccess(code);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.veriPhoneFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取用户信息
	 * 
	 * @param num
	 * @param userId
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月5日
	 */
	public void getUserInfo(final String num, final String userId,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo info = null;
				try {
					info = Protocol.getInstance().getUserInfo(num, userId);
					object = info;
					if (info != null && info.getResCode() != null
							&& info.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getUserInfo(num,
																	userId);
													for (CallBack back : getCallbacks(mBack)) {
														back.getUserInfoSuccess((UserInfo) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getUserInfo(num,
																	userId);
													for (CallBack back : getCallbacks(mBack)) {
														back.getUserInfoSuccess((UserInfo) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getUserInfoSuccess(info);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getUserInfoFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 提现
	 * 
	 * @param num
	 * @param userId
	 * @param amount
	 * @param isQuick
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void outCash(final String num, final String userId,
			final String amount, final String isQuick, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				GetCode code = null;
				try {
					code = Protocol.getInstance().outCash(num, userId, amount,
							isQuick);
					object = code;
					if (code != null && code.getResCode() != null
							&& code.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.outCash(num,
																	userId,
																	amount,
																	isQuick);
													for (CallBack back : getCallbacks(mBack)) {
														back.outCashSuccess((GetCode) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.outCash(num,
																	userId,
																	amount,
																	isQuick);
													for (CallBack back : getCallbacks(mBack)) {
														back.outCashSuccess((GetCode) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.outCashSuccess(code);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.outCashFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取充值渠道列表
	 * 
	 * @param num
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月10日
	 */
	public void getReChargeList(final String num, final String userId,
			final String timeId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ReChargeChannel channel = null;
				try {
					channel = Protocol.getInstance().getReChargeList(num,
							userId, timeId);
					object = channel;
					if (channel != null && channel.getResCode() != null
							&& channel.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getReChargeList(
																	num,
																	userId,
																	timeId);
													for (CallBack back : getCallbacks(mBack)) {
														back.getReChargeListSuccess((ReChargeChannel) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getReChargeList(
																	num,
																	userId,
																	timeId);
													for (CallBack back : getCallbacks(mBack)) {
														back.getReChargeListSuccess((ReChargeChannel) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getReChargeListSuccess(channel);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getReChargeListFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 完善用户资料
	 * 
	 * @param num
	 * @param userId
	 * @param realName
	 * @param idCard
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月11日
	 */
	public void bindUserInfo(final String num, final String userId,
			final String realName, final String idCard, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo userInfo = null;
				try {
					userInfo = Protocol.getInstance().bindUserInfo(num, userId,
							realName, idCard);
					object = userInfo;
					if (userInfo != null && userInfo.getResCode() != null
							&& userInfo.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.bindUserInfo(num,
																	userId,
																	realName,
																	idCard);
													for (CallBack back : getCallbacks(mBack)) {
														back.bindUserInfoSuccess((UserInfo) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.bindUserInfo(num,
																	userId,
																	realName,
																	idCard);
													for (CallBack back : getCallbacks(mBack)) {
														back.bindUserInfoSuccess((UserInfo) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.bindUserInfoSuccess(userInfo);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.bindUserInfoFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取银行列表
	 * 
	 * @param num
	 * @param timeId
	 * @param areaCode
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月12日
	 */
	public void getBankList(final String num, final String timeId,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				BankList infos = null;
				try {
					infos = Protocol.getInstance().getBankList(num, timeId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getBankListSuccess(infos);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getBankListFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取省市列表
	 * 
	 * @param num
	 * @param timeId
	 * @param areaCode
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月12日
	 */
	public void getProCity(final String num, final String timeId,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ProCity proCity = null;
				try {
					proCity = Protocol.getInstance().getProCity(num, timeId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getProCitySuccess(proCity);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getProCityFailure(e.getMessage());
					}
				}
			}
		});
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
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public void bindBank(final String num, final String userId,
			final String province, final String city, final String bankName,
			final String bankCardId, final String bankCode, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				UserInfo userInfo = null;
				try {
					userInfo = Protocol.getInstance().bindBank(num, userId,
							province, city, bankName, bankCardId, bankCode);
					object = userInfo;
					if (userInfo != null && userInfo.getResCode() != null
							&& userInfo.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.bindBank(num,
																	userId,
																	province,
																	city,
																	bankName,
																	bankCardId,
																	bankCode);
													for (CallBack back : getCallbacks(mBack)) {
														back.bindBankSuccess((UserInfo) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.bindBank(num,
																	userId,
																	province,
																	city,
																	bankName,
																	bankCardId,
																	bankCode);
													for (CallBack back : getCallbacks(mBack)) {
														back.bindBankSuccess((UserInfo) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.bindBankSuccess(userInfo);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.bindBankFailure(e.getMessage());
					}
				}
			}
		});
	}

	public void bankType(final String num, final String bankCode,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				BankType bankType = null;
				try {
					bankType = Protocol.getInstance().bankType(num, bankCode);
					for (CallBack back : getCallbacks(mBack)) {
						back.bankTypeSuccess(bankType);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.bankTypeFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取竞彩足球对阵信息
	 * 
	 * @param num
	 * @param issueNo
	 * @param gameNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月18日
	 */
	public void getFootBallMatchsInfo(final String num, final String issueNo,
			final String gameNo, final String isCurrent, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				FootBallMatch info = null;
				try {
					info = Protocol.getInstance().getFootBallMatchsInfo(num,
							issueNo, gameNo, isCurrent);
					for (CallBack back : getCallbacks(mBack)) {
						back.getFootBallMatchsInfoSuccess(info);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getFootBallMatchsInfoFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取对阵时间期次
	 * 
	 * @param num
	 * @param gameNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月23日
	 */
	public void getMatchTime(final String num, final String gameNo,
			final String num1, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				IssueList list = null;
				try {
					list = Protocol.getInstance().getMatchTime(num, gameNo,
							num1);
					for (CallBack back : getCallbacks(mBack)) {
						back.getMatchTimeSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getMatchTimeFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 充值
	 * 
	 * @param num
	 * @param userId
	 * @param amount
	 * @param rechargeType
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月24日
	 */
	public void reCharge(final String num, final String userId,
			final String amount, final String rechargeType, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ReCharge reCharge = null;
				try {
					reCharge = Protocol.getInstance().reCharge(num, userId,
							amount, rechargeType);
					object = reCharge;
					if (reCharge != null && reCharge.getResCode() != null
							&& reCharge.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.reCharge(num,
																	userId,
																	amount,
																	rechargeType);
													for (CallBack back : getCallbacks(mBack)) {
														back.reChargeSuccess((ReCharge) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.reCharge(num,
																	userId,
																	amount,
																	rechargeType);
													for (CallBack back : getCallbacks(mBack)) {
														back.reChargeSuccess((ReCharge) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.reChargeSuccess(reCharge);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.reChargeFailure(e.getMessage());
					}
				}
			}
		});
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
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月25日
	 */
	public void easyLinkReCharge(final String num, final String userId,
			final String cardNum, final String amount, final String cell,
			final String province, final String city, final String realName,
			final String idCard, final String cardType, final String bankName,
			final String cityCode, final String provinceCode,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				EasyLinkReCharge reCharge = null;
				try {
					reCharge = Protocol.getInstance().easyLinkReCharge(num,
							userId, cardNum, amount, cell, province, city,
							realName, idCard, cardType, bankName, cityCode,
							provinceCode);
					object = reCharge;
					if (reCharge != null && reCharge.getResCode() != null
							&& reCharge.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.easyLinkReCharge(
																	num,
																	userId,
																	cardNum,
																	amount,
																	cell,
																	province,
																	city,
																	realName,
																	idCard,
																	cardType,
																	bankName,
																	cityCode,
																	provinceCode);
													for (CallBack back : getCallbacks(mBack)) {
														back.easyLinkReChargeSuccess((EasyLinkReCharge) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.easyLinkReCharge(
																	num,
																	userId,
																	cardNum,
																	amount,
																	cell,
																	province,
																	city,
																	realName,
																	idCard,
																	cardType,
																	bankName,
																	cityCode,
																	provinceCode);
													for (CallBack back : getCallbacks(mBack)) {
														back.easyLinkReChargeSuccess((EasyLinkReCharge) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.easyLinkReChargeSuccess(reCharge);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.easyLinkReChargeFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取焦点比赛
	 * 
	 * @param num
	 * @param gameNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月1日
	 */
	public void getFocusMatch(final String num, final String gameNo,
			final String isChange, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				FocusMatch fMatch = null;
				try {
					fMatch = Protocol.getInstance().getFocusMatch(num, gameNo,
							isChange);
					for (CallBack back : getCallbacks(mBack)) {
						back.getFocusMatchSuccess(fMatch);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getFocusMatchFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 极光push注册
	 * 
	 * @param num
	 * @param userId
	 * @param mobile
	 * @param jpushAppKey
	 * @param jpushRegistrationID
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月2日
	 */
	public void registerPush(final String num, final String userId,
			final String mobile, final String jpushAppKey,
			final String jpushRegistrationID, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				GetCode code = null;
				try {
					code = Protocol.getInstance().registerPush(num, userId,
							mobile, jpushAppKey, jpushRegistrationID);
					object = code;
					if (code != null && code.getResCode() != null
							&& code.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.registerPush(
																	num,
																	userId,
																	mobile,
																	jpushAppKey,
																	jpushRegistrationID);
													for (CallBack back : getCallbacks(mBack)) {
														back.registerPushSuccess((GetCode) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.registerPush(
																	num,
																	userId,
																	mobile,
																	jpushAppKey,
																	jpushRegistrationID);
													for (CallBack back : getCallbacks(mBack)) {
														back.registerPushSuccess((GetCode) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.registerPushSuccess(code);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.registerPushFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-关注赛事
	 * 
	 * @param num
	 * @param userId
	 * @param matchId
	 * @param status
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public void watchMatch(final String num, final String userId,
			final String matchId, final String status, final String lotteryNo,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				GetCode code = null;
				try {
					code = Protocol.getInstance().watchMatch(num, userId,
							matchId, status, lotteryNo);
					object = code;
					if (code != null && code.getResCode() != null
							&& code.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.watchMatch(num,
																	userId,
																	matchId,
																	status,
																	lotteryNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.watchMatchSuccess((GetCode) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.watchMatch(num,
																	userId,
																	matchId,
																	status,
																	lotteryNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.watchMatchSuccess((GetCode) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.watchMatchSuccess(code);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.watchMatchFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-对阵列表
	 * 
	 * @param num
	 * @param userId
	 * @param lotteryNo
	 * @param type
	 * @param filter
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public void getScoreList(final String num, final String userId,
			final String lotteryNo, final String type, final String filter,
			final String issueNo, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreList list = null;
				try {
					list = Protocol.getInstance().getScoreList(num, userId,
							lotteryNo, type, filter, issueNo);
					object = list;
					if (list != null && list.getResCode() != null
							&& list.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getScoreList(num,
																	userId,
																	lotteryNo,
																	type,
																	filter,
																	issueNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getScoreListSuccess((ScoreList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getScoreList(num,
																	userId,
																	lotteryNo,
																	type,
																	filter,
																	issueNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.getScoreListSuccess((ScoreList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getScoreListSuccess(list);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreListFailure(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 比分-对阵列表
	 * 
	 * @param num
	 * @param userId
	 * @param lotteryNo
	 * @param type
	 * @param filter
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public void getScoreList(final String num, final String userId,
			final String lotteryNo, final String type, final String filter,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreList list = null;
				try {
					list = Protocol.getInstance().getScoreList(num, userId,
							lotteryNo, type, filter);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreListSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreListFailure(e.getMessage());
						break;
					}
				}
			}
		});
	}
	
	public void getScoreDetail(final String num, final String userId, final String lotteryNo,
			final String matchId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreObj obj = null;
				try {
					obj = Protocol.getInstance().getScoreDetail(num, userId,
							lotteryNo, matchId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailSuccess(obj);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-详情-赛事
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public void getScoreDetailGame(final String num, final String lotteryNo,
			final String matchId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreDetailGame game = null;
				try {
					game = Protocol.getInstance().getScoreDetailGame(num,
							lotteryNo, matchId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailGameSuccess(game);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailGameFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-详情-分析
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @param companyName
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月10日
	 */
	public void getScoreDetailAnalysis(final String num,
			final String lotteryNo, final String matchId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreDetailAnalysis analysis = null;
				try {
					analysis = Protocol.getInstance().getScoreDetailAnalysis(
							num, lotteryNo, matchId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailAnalysisSuccess(analysis);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailAnalysisFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-详情-亚赔
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public void getScoreDetailAsia(final String num, final String lotteryNo,
			final String matchId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreDetailOddsList list = null;
				try {
					list = Protocol.getInstance().getScoreDetailAsia(num,
							lotteryNo, matchId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailAsiaSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailAsiaFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-详情-欧赔
	 * 
	 * @param num
	 * @param lotteryNo
	 * @param matchId
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public void getScoreDetailEurope(final String num, final String lotteryNo,
			final String matchId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreDetailOddsList list = null;
				try {
					list = Protocol.getInstance().getScoreDetailEurope(num,
							lotteryNo, matchId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailEuropeSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreDetailEuropeFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-公司赔率
	 * 
	 * @param num
	 * @param companyId
	 * @param matchId
	 * @param timeId
	 * @param pageNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public void getScoreOdds(final String num, final String companyId,
			final String lotteryNo, final String matchId,
			final String companyType, final String pageNo, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreCompanyOdds obj = null;
				try {
					obj = Protocol.getInstance().getScoreOdds(num, companyId,
							lotteryNo, matchId, companyType, pageNo);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreOddsSuccess(obj);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreOddsFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 比分-公司
	 * 
	 * @param num
	 * @param companyId
	 * @param matchId
	 * @param timeId
	 * @param pageNo
	 * @param mBack
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年12月11日
	 */
	public void getScoreCompany(final String num, final String timeId,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ScoreCompanyOdds obj = null;
				try {
					obj = Protocol.getInstance().getScoreCompany(num, timeId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreCompanySuccess(obj);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getScoreCompanyFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取站内信息;/活动;
	 * 
	 * @param userId
	 * @param type
	 * @param pageCount
	 * @param pageNum
	 */
	public void getInsideMessageList(final String num, final String userId,
			final String type, final String pageCount, final String pageNum,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				Message obj = null;
				try {
					obj = Protocol.getInstance().getInsideMessageList(num,
							userId, type, pageCount, pageNum);
					object = obj;
					if (obj != null && obj.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getInsideMessageList(
																	num,
																	userId,
																	type,
																	pageCount,
																	pageNum);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess(
																(Message) object,
																"");
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getInsideMessageList(
																	num,
																	userId,
																	type,
																	pageCount,
																	pageNum);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess(
																(Message) object,
																"");
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj, "");
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage(), "");
					}
				}
			}
		});
	}

	/**
	 * 查询乐米明细记录;
	 * 
	 * @param num
	 * @param userId
	 * @param dealType
	 * @param startTime
	 * @param endTime
	 * @param pageAmount
	 * @param pageNo
	 */
	public void getLeMiClearList(final String num, final String userId,
			final String dealType, final String startTime,
			final String endTime, final String pageAmount, final String pageNo,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				LeMi obj = null;
				try {
					obj = Protocol.getInstance().getLeMiClearList(num, userId,
							dealType, startTime, endTime, pageAmount, pageNo);
					object = obj;
					if (obj != null && obj.resCode == 3002) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getLeMiClearList(
																	num,
																	userId,
																	dealType,
																	startTime,
																	endTime,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((Message) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getLeMiClearList(
																	num,
																	userId,
																	dealType,
																	startTime,
																	endTime,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((Message) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});

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
	 * @param list1
	 * @param mBackf
	 * @author:www.wenchuang.com
	 * @date:2016年1月12日
	 */
	public void syncLotteryBetting(final String num, final String userId,
			final String bettingType, final String gameNo, final String issue,
			final String multiple, final String buyMoney, final String isChase,
			final String chase, final String isRedPackage,
			final String redPackageId, final String redPackageMoney,
			final String choiceType, final String planEndTime,
			final String stopCondition, final List<JSONObject> list,
			final String redPkgType, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				Betting obj = null;
				try {
					obj = Protocol.getInstance().syncLotteryBetting(num,
							userId, bettingType, gameNo, issue, multiple,
							buyMoney, isChase, chase, isRedPackage,
							redPackageId, redPackageMoney, choiceType,
							planEndTime, stopCondition, list, redPkgType);
					object = obj;
					if (obj != null && obj.getResCode() != null
							&& obj.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.syncLotteryBetting(
																	num,
																	userId,
																	bettingType,
																	gameNo,
																	issue,
																	multiple,
																	buyMoney,
																	isChase,
																	chase,
																	isRedPackage,
																	redPackageId,
																	redPackageMoney,
																	choiceType,
																	planEndTime,
																	stopCondition,
																	list,
																	redPkgType);
													for (CallBack back : getCallbacks(mBack)) {
														back.syncLotteryBettingSuccess((Betting) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.syncLotteryBetting(
																	num,
																	userId,
																	bettingType,
																	gameNo,
																	issue,
																	multiple,
																	buyMoney,
																	isChase,
																	chase,
																	isRedPackage,
																	redPackageId,
																	redPackageMoney,
																	choiceType,
																	planEndTime,
																	stopCondition,
																	list,
																	redPkgType);
													for (CallBack back : getCallbacks(mBack)) {
														back.syncLotteryBettingSuccess((Betting) object);
													}
												}
											} catch (CException e) {
												e.printStackTrace();
											} catch (JSONException e) {
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.syncLotteryBettingSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryBettingFailure(e.getMessage());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 现金购买红包和乐米列表;
	 * 
	 * @param num
	 * @param userId
	 * @param productType
	 * @param callBack
	 */
	public void getRedPackAndLeMiList(final String num, final String userId,
			final String productType, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				try {
					RedPacketLeMiList obj = null;
					obj = Protocol.getInstance().getProdectList(num, userId,
							productType);
					object = obj;
					if (obj != null && obj.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getProdectList(
																	num,
																	userId,
																	productType);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPacketLeMiList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getProdectList(
																	num,
																	userId,
																	productType);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPacketLeMiList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});

	}

	/**
	 * 现金购买红包乐米;
	 * 
	 * @param num
	 * @param userId
	 * @param productType
	 * @param productMoney
	 * @param productSaleMoney
	 * @param mBack
	 */
	public void confirmPayRequest(final String num, final String userId,
			final String productType, final String productId,
			final String productSaleMoney, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				try {
					UserCount obj = Protocol.getInstance().buyRedPkgLeMi(num,
							userId, productType, productId, productSaleMoney);
					object = obj;
					if (obj != null && obj.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.buyRedPkgLeMi(
																	num,
																	userId,
																	productType,
																	productId,
																	productSaleMoney);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((UserCount) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.buyRedPkgLeMi(
																	num,
																	userId,
																	productType,
																	productId,
																	productSaleMoney);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((UserCount) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 乐米兑换商品列表;
	 * 
	 * @param num
	 * @param userId
	 * @param productType
	 * @param callBack
	 */
	public void getLMConvertProdectList(final String num, final String userId,
			final String productType, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				try {
					RedPacketLeMiList obj = null;
					obj = Protocol.getInstance().getConvertProdectList(num,
							userId, productType);
					object = obj;
					if (obj != null && obj.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getConvertProdectList(
																	num,
																	userId,
																	productType);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPacketLeMiList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getConvertProdectList(
																	num,
																	userId,
																	productType);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPacketLeMiList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});

	}

	/**
	 * 乐米兑换记录;
	 * 
	 * @param num
	 * @param userId
	 * @param pageNo
	 * @param pageAmount
	 * @param mBack
	 */
	public void getConvertNotesList(final String num, final String userId,
			final String pageNo, final String pageAmount, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				try {
					RedPacketLeMiList obj = null;
					obj = Protocol.getInstance().getConvertNotesList(num,
							userId, pageNo, pageAmount);
					object = obj;
					if (obj != null && obj.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getConvertNotesList(
																	num,
																	userId,
																	pageNo,
																	pageAmount);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPacketLeMiList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getConvertNotesList(
																	num,
																	userId,
																	pageNo,
																	pageAmount);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPacketLeMiList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 已使用/未使用红包
	 * 
	 * @param num
	 * @param pageNo
	 * @param pageAmount
	 * @param mBack
	 */
	public void getNoUsedRedList(final String num, final String userId,
			final String pageNo, final String pageAmount, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				RedPkgUsed obj = null;
				try {
					obj = Protocol.getInstance().getNotUsedRedPkgList(num,
							userId, pageNo, pageAmount);
					object = obj;
					if (obj != null && obj.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getNotUsedRedPkgList(
																	num,
																	userId,
																	pageNo,
																	pageAmount);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPkgUsed) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getNotUsedRedPkgList(
																	num,
																	userId,
																	pageNo,
																	pageAmount);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPkgUsed) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 更新站内信已读和未读状态;
	 * 
	 * @param num
	 * @param userId
	 * @param messageId
	 * @param type
	 * @param mBack
	 */
	public void updateMessageState(final String num, final String userId,
			final String messageId, final String type, final CallBack mBack,
			final String postion) {
		execute(new Runnable() {
			@Override
			public void run() {
				try {
					Protocol.getInstance().updataMessageData(num, userId,
							messageId, type);
					for (CallBack back : getCallbacks(mBack)) {
						back.onSuccess(type, postion);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(type, postion);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 乐米兑换;
	 * 
	 * @param num
	 * @param String
	 * @param productPrice
	 * @param productId
	 * @param produtType
	 * @param cell
	 * @param mBack
	 */
	public void convertLiMeRequest(final String num, final String userId,
			final String productPrice, final String productId,
			final String produtType, final String cell, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				try {
					UserCount result = Protocol.getInstance().convertLeMi(num,
							userId, productPrice, productId, produtType, cell);
					object = result;
					if (result != null && result.resCode.equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.convertLeMi(
																	num,
																	userId,
																	productPrice,
																	productId,
																	produtType,
																	cell);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((UserCount) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.convertLeMi(
																	num,
																	userId,
																	productPrice,
																	productId,
																	produtType,
																	cell);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((UserCount) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(result);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {

					}
				}
			}
		});
	}

	/**
	 * 查询红包明细记录;
	 * 
	 * @param num
	 * @param userId
	 * @param dealType
	 * @param startTime
	 * @param endTime
	 * @param pageAmount
	 * @param pageNo
	 */
	public void getRedPkgClearList(final String num, final String userId,
			final String dealType, final String startTime,
			final String endTime, final String pageAmount, final String pageNo,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				RedPkgDetail obj = null;
				try {
					obj = Protocol.getInstance().getRedPkgClearList(num,
							userId, dealType, startTime, endTime, pageAmount,
							pageNo);
					object = obj;
					if (obj != null && obj.resCode == 3002) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getRedPkgClearList(
																	num,
																	userId,
																	dealType,
																	startTime,
																	endTime,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPkgDetail) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getRedPkgClearList(
																	num,
																	userId,
																	dealType,
																	startTime,
																	endTime,
																	pageAmount,
																	pageNo);
													for (CallBack back : getCallbacks(mBack)) {
														back.onSuccess((RedPkgDetail) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.onSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.onFail(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 开奖首页列表
	 * 
	 * @Description:
	 * @param num
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public void syncLotteryDraw(final String num, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				List<LotteryDrawInfo> list = null;
				try {
					list = Protocol.getInstance().getHallLotteryDrawInfoList(
							num);
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 单彩种开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param pageNo
	 * @param gameNo
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public void syncLotteryDrawInfoList(final String num, final String pageNo,
			final String gameNo, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				List<LotteryDrawInfo> list = null;
				try {
					list = Protocol.getInstance().getLotteryDrawInfoList(num,
							pageNo, gameNo);
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawSuccess(list);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawError(e.getMessage());
					}
				}
			}
		});
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
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public void syncLotteryDrawInfo(final String num, final String gameNo,
			final String startTime, final String endTime, final String issue,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				LotteryDrawDetailInfo info = null;
				try {
					info = Protocol.getInstance().getLotteryDrawInfo(num,
							gameNo, startTime, endTime, issue);
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawInfoSuccess(info);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawInfoError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取竞技彩（足球+篮球）开奖列表
	 * 
	 * @Description:
	 * @param num
	 * @param gameNo
	 * @param issue
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月15日
	 */
	public void syncLotteryDrawFootballList(final String num,
			final String gameNo, final String issue, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				LotteryDrawFootballList footballInfo = null;
				LotteryDrawBasketballList basketInfo = null;
				try {
					if (("TC_JCZQ").equals(gameNo)) {
						footballInfo = Protocol.getInstance()
								.getLotteryDrawFootballList(num, gameNo, issue);
						for (CallBack back : getCallbacks(mBack)) {
							back.syncLotteryDrawFootballListSuccess(footballInfo);
						}
					} else if (("TC_JCLQ").equals(gameNo)) {
						basketInfo = Protocol.getInstance()
								.getLotteryDrawBasketballList(num, gameNo,
										issue);
						for (CallBack back : getCallbacks(mBack)) {
							back.syncLotteryDrawBasketballListSuccess(basketInfo);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.syncLotteryDrawFootballListError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取头部广告
	 * 
	 * @Description:
	 * @param num
	 * @param timeId
	 * @param isNewUser
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public void getHallAd(final String num, final String timeId,
			final String isNewUser, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				HallAd info = null;
				try {
					info = Protocol.getInstance().getHallAd(num, timeId,
							isNewUser);
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallAdSuccess(info);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallAdError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 弹出公告
	 * 
	 * @Description:
	 * @param num
	 * @param timeId
	 * @param isNewUser
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public void getHallNotice(final String num, final String timeId,
			final String isNewUser, final String width, final String height,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				HallNotice notice = null;
				try {
					notice = Protocol.getInstance().getHallNotice(num, timeId,
							isNewUser, width, height);
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallNoticeSuccess(notice);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallNoticeError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 软件版本升级
	 * 
	 * @Description:
	 * @param num
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public void getHallUpgrade(final String num, final String width,
			final String height, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				Upgrade upgrade = null;
				try {
					upgrade = Protocol.getInstance().getHallUpgrade(num, width,
							height);
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallUpgradeSuccess(upgrade);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallUpgradeError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 彩种列表控制
	 * 
	 * @Description:
	 * @param num
	 * @param timeId
	 * @param ver
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年3月22日
	 */
	public void getHallControllLottery(final String num, final String timeId,
			final String ver, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				ControllLottery cLottery = null;
				try {
					cLottery = Protocol.getInstance().getHallControllLottery(
							num, timeId, ver);
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallControllLotterySuccess(cLottery);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallControllLotteryError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取签到列表
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public void getSignList(final String num, final String userId,
			final String startTime, final String endTime,
			final String signType, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				SignList signList = null;
				try {
					signList = Protocol.getInstance().getSignList(num, userId,
							startTime, endTime, signType);
					object = signList;
					if (signList != null && signList.getResCode() != null
							&& signList.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getSignList(num,
																	userId,
																	startTime,
																	endTime,
																	signType);
													for (CallBack back : getCallbacks(mBack)) {
														back.getSignListSuccess((SignList) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getSignList(num,
																	userId,
																	startTime,
																	endTime,
																	signType);
													for (CallBack back : getCallbacks(mBack)) {
														back.getSignListSuccess((SignList) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getSignListSuccess(signList);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getSignListError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 点击签到
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016年4月9日
	 */
	public void sign(final String num, final String userId,
			final String signDate, final String startTime,
			final String endTime, final String signType, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				SignStatus status = null;
				try {
					status = Protocol.getInstance().sign(num, userId, signDate,
							startTime, endTime, signType);
					object = status;
					if (status != null && status.getResCode() != null
							&& status.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.sign(num, userId,
																	signDate,
																	startTime,
																	endTime,
																	signType);
													for (CallBack back : getCallbacks(mBack)) {
														back.signSuccess((SignStatus) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.sign(num, userId,
																	signDate,
																	startTime,
																	endTime,
																	signType);
													for (CallBack back : getCallbacks(mBack)) {
														back.signSuccess((SignStatus) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.signSuccess(status);
							break;
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.signError(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 获取期次信息
	 * 
	 * @param lotteryNo
	 *            彩种
	 * @param queryNum
	 *            期号
	 * @param num
	 *            请求网址
	 * @param mBack
	 *            回调
	 */
	public void getLotteryNOs(final String lotteryNo, final String queryNum,
			final String num, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				LotteryNOs lotteryNOs = null;
				try {
					lotteryNOs = Protocol.getInstance().getLotteryNOs(
							lotteryNo, queryNum, num, mBack);
					for (CallBack back : getCallbacks(mBack)) {
						back.getLotteryNoSuccess(lotteryNOs);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getLotteryNoError(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 微信登陆-获取商户信息（appid）
	 * 
	 * @param num
	 *            接口号
	 * @param mBack
	 */
	public void getAppId(final String num, final String type,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				AppIdBean appIdBean = null;
				try {
					appIdBean = Protocol.getInstance().weiChatGetAppId(num,
							type, mBack);
					for (CallBack back : getCallbacks(mBack)) {
						back.getAppIdSuccess(appIdBean);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getAppIdError(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 微信登陆 -验证用户信息 (登陆)
	 * 
	 * @param num
	 * @param code
	 * @param mBack
	 */
	public void weiChatLoad(final String num, final String code,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = "";
				try {
					json = Protocol.getInstance().weiChatLoad(num, code, mBack);
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatLoadSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatLoadError(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 微信登陆 -验证手机号
	 * 
	 * @param num
	 * @param code
	 * @param mobile
	 * @param openId
	 * @param mBack
	 */
	public void weiChatBindPhone(final String num, final String code,
			final String mobile, final String openId, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = "";
				try {
					json = Protocol.getInstance().weiChatBindPhone(num, code,
							mobile, openId, mBack);
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatBindPhoneSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatBindPhoneError(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 微信登陆 -注册新用户
	 * 
	 * @param num
	 * @param mobile
	 * @param openId
	 * @param userName
	 * @param password
	 * @param mBack
	 */
	public void weiChatRegistUser(final String num, final String mobile,
			final String openId, final String userName, final String password,
			final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = "";
				try {
					json = Protocol.getInstance().weiChatRegistUser(num,
							mobile, openId, userName, password, mBack);
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatRegistUserSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatRegistUserError(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 微信充值
	 * 
	 * @param num接口号
	 * @param userId用户ID
	 * @param amount充值金额
	 * @param platform平台编号1
	 *            ：客户端，2：PHP，3：H5
	 * @param isOrder是否是订单充值
	 *            :1:是,2否
	 * @param orderId如果是订单充值
	 *            ，需传订单号
	 * @param mBack
	 * @return
	 * @throws CException
	 */
	public void weChatRecharge(final String num, final String userId,
			final String amount, final String platform, final String isOrder,
			final String orderId, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = "";
				try {
					json = Protocol.getInstance().weChatRecharge(num, userId,
							amount, platform, isOrder, orderId, mBack);
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatRechargeSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.weChatRechargeError(e.getMessage());
						break;
					}
				}
			}
		});
	}

	/**
	 * 投注支付
	 * 
	 * @param num
	 *            (接口号)
	 * @param userId
	 *            (userId)
	 * @param bettingType
	 *            (投注类型)
	 * @param gameNo
	 *            (彩种编号)
	 * @param issue
	 *            (期号列表)
	 * @param multiple
	 *            (倍数列表)
	 * @param buyMoney
	 *            (支付金额)
	 * @param isChase
	 *            (是否追号)
	 * @param chase
	 *            (总追期数，默认为1)
	 * @param payType
	 *            (支付类型：1、余额支付，2、余额+红包支付，3、微信支付，4、微信+红包支付，7、支付宝支付，8、支付宝+红包支付，)
	 * @param redPackageId
	 *            (用户红包Id)
	 * @param redPkgType
	 *            (红包类型)
	 * @param redPackageMoney
	 *            (红包支付金额)
	 * @param choiceType
	 *            (用户选好方式5，手选6，摇一摇7，幸运选好8，机选)
	 * @param planEndTime
	 *            (竞彩的第一场比赛截止时间)
	 * @param stopCondition
	 *            (追停：0：中奖不追停，1：中奖后追停)
	 * @param list
	 *            (投注列表)
	 * @param mBack
	 */
	public void bettingPay(final String num, final String userId,
			final String bettingType, final String gameNo, final String issue,
			final String multiple, final String buyMoney, final String isChase,
			final String chase, final String payType,
			final String redPackageId, final String redPkgType,
			final String redPackageMoney, final String choiceType,
			final String planEndTime, final String stopCondition,
			final List<JSONObject> list, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				Betting obj = null;
				try {
					obj = Protocol.getInstance().bettingPay(num, userId,
							bettingType, gameNo, issue, multiple, buyMoney,
							isChase, chase, payType, redPackageId,
							redPackageMoney, choiceType, planEndTime,
							stopCondition, list, redPkgType);
					object = obj;
					if (obj != null && obj.getResCode() != null
							&& obj.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.bettingPay(
																	num,
																	userId,
																	bettingType,
																	gameNo,
																	issue,
																	multiple,
																	buyMoney,
																	isChase,
																	chase,
																	payType,
																	redPackageId,
																	redPackageMoney,
																	choiceType,
																	planEndTime,
																	stopCondition,
																	list,
																	redPkgType);
													for (CallBack back : getCallbacks(mBack)) {
														back.bettingPaySuccess((Betting) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.bettingPay(
																	num,
																	userId,
																	bettingType,
																	gameNo,
																	issue,
																	multiple,
																	buyMoney,
																	isChase,
																	chase,
																	payType,
																	redPackageId,
																	redPackageMoney,
																	choiceType,
																	planEndTime,
																	stopCondition,
																	list,
																	redPkgType);
													for (CallBack back : getCallbacks(mBack)) {
														back.bettingPaySuccess((Betting) object);
													}
												}
											} catch (CException e) {
												e.printStackTrace();
											} catch (JSONException e) {
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.bettingPaySuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.bettingPayError(e.getMessage());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 订单的再次支付
	 * 
	 * @param num
	 *            -接口编号
	 * @param userId
	 *            -用户Id
	 * @param orderId
	 *            -订单编号
	 * @param betPlat
	 *            -投注平台1：客户端2：PHP3：H5
	 * @param payMoney
	 *            -支付金额
	 * @param payType
	 *            -支付类型-1、余额支付，2、余额+红包支付，3、微信支付，4、微信+红包支付，7、支付宝支付，8、支付宝+红包支付
	 * @param redBagId
	 *            -红包ID
	 * @param redBagType
	 *            -红包类型
	 * @param mBack
	 *            -回调
	 */
	public void orderAgainPay(final String num, final String userId,
			final String orderId, final String betPlat, final String payMoney,
			final String payType, final String redBagId,
			final String redBagType, final CallBack mBack) {

		execute(new Runnable() {
			@Override
			public void run() {
				Betting obj = null;
				try {
					obj = Protocol.getInstance().orderAgainPay(num, userId,
							orderId, betPlat, payMoney, payType, redBagId,
							redBagType);
					object = obj;
					if (obj != null && obj.getResCode() != null
							&& obj.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.orderAgainPay(num,
																	userId,
																	orderId,
																	betPlat,
																	payMoney,
																	payType,
																	redBagId,
																	redBagType);
													for (CallBack back : getCallbacks(mBack)) {
														back.orderAgainPaySuccess((Betting) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.orderAgainPay(num,
																	userId,
																	orderId,
																	betPlat,
																	payMoney,
																	payType,
																	redBagId,
																	redBagType);
													for (CallBack back : getCallbacks(mBack)) {
														back.orderAgainPaySuccess((Betting) object);
													}
												}
											} catch (CException e) {
												e.printStackTrace();
											} catch (JSONException e) {
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.orderAgainPaySuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.orderAgainPayError(e.getMessage());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 消息推送获取／设置
	 * 
	 * @Description:
	 * @param num
	 * @param userId
	 * @param isSetting
	 * @param list
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016-6-24
	 */
	public void pushSet(final String num, final String userId,
			final String isSetting, final List<JSONObject> list,
			final CallBack mBack) {

		execute(new Runnable() {
			@Override
			public void run() {
				PushResult obj = null;
				try {
					obj = Protocol.getInstance().pushSet(num, userId,
							isSetting, list);
					object = obj;
					if (obj != null && obj.getResCode() != null
							&& obj.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.pushSet(num,
																	userId,
																	isSetting,
																	list);
													for (CallBack back : getCallbacks(mBack)) {
														back.pushSetSuccess((PushResult) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.pushSet(num,
																	userId,
																	isSetting,
																	list);
													for (CallBack back : getCallbacks(mBack)) {
														back.pushSetSuccess((PushResult) object);
													}
												}
											} catch (CException e) {
												e.printStackTrace();
											} catch (JSONException e) {
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.pushSetSuccess(obj);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.pushSetError(e.getMessage());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 发送mac信息
	 * 
	 * @param num
	 *            ：协议号
	 * @param mac
	 *            ：mac地址
	 * @param sign
	 *            ：唯一标识 [mac+Ime md5加密]
	 * @param phone手机型号
	 * @param ime
	 * @param mBack
	 */
	public void sendMaxInfo(final String num, final String mac,
			final String sign, final String phone, final String ime,
			final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				String str = null;
				try {
					str = Protocol.getInstance().sendMaxInfo(num, mac, sign,
							phone, ime);
					for (CallBack back : getCallbacks(mBack)) {
						back.sendMaxInfoSuccess(str);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.sendMaxInfoFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 使用openid登陆微信
	 * 
	 * @param num
	 * @param openid
	 * @param mBack
	 */
	public void loginWithOpenid(final String num, final String openid,
			final CallBack mBack) {

		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().loginWithOpenid(num, openid);
					for (CallBack back : getCallbacks(mBack)) {
						back.loginWithOpenidSuccess(json);
					}
				} catch (Exception e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.loginWithOpenidFailure(json);
					}
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取用户未读站内信的数量
	 * 
	 * @param num
	 * @param userid
	 * @param mType
	 * @param sType
	 * @param mBack
	 */
	public void getNewNoticeOrMessage(final String num, final String userid,
			final String mType, final String sType, final CallBack mBack) {

		execute(new Runnable() {
			public void run() {
				NoticeOrMessageCount count = null;
				try {
					count = Protocol.getInstance().getNewNoticeOrMessage(num,
							userid, mType, sType);
					object = count;
					if (count != null && count.getResCode() != null
							&& count.getResCode().equals("3002")) {
						initDatas();
//						Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
						if (loginPattern == 1 && auto) {
							// 之前的登陆状态为微信
							Controller.getInstance().loginWithOpenid(
									GlobalConstants.URL_WECHAT_LOAD, openId,
									new CallBack() {
										/** 使用openid登陆微信成功 **/
										public void loginWithOpenidSuccess(
												String json) {
											try {
												JSONObject jsonObject = new JSONObject(
														json);
												String resCode = jsonObject
														.getString("resCode");
												if (resCode != null
														&& resCode.equals("0")) {
													App.userInfo = (UserInfo) Parser
															.parse(cn.com.cimgroup.protocol.Command.LOGIN,
																	jsonObject);
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													// 登陆完成 重新发送该请求
													object = Protocol
															.getInstance()
															.getNewNoticeOrMessage(
																	num,
																	userid,
																	mType,
																	sType);
													for (CallBack back : getCallbacks(mBack)) {
														back.getNewNoticeOrMessageSuccess((NoticeOrMessageCount) object);
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (CException e) {
												e.printStackTrace();
											}
										}

										/** 使用openid登陆微信失败 **/
										public void loginWithOpenidFailure(
												String json) {

										}
									});
						} else if (loginPattern == 2 && auto) {
							// 之前的登陆状态为自营
							Controller.getInstance().login(
									GlobalConstants.NUM_LOGIN,
									App.userInfo.getUserName(),
									App.userInfo.getPassword(), new CallBack() {
										@Override
										public void loginSuccess(UserInfo info) {
											try {
												if (info != null
														&& info.getResCode()
																.equals("0")) {
													String password = App.userInfo
															.getPassword();
													App.userInfo = info;
													App.userInfo
															.setPassword(password);
													// 将用户信息保存到配置文件中
													UserLogic
															.getInstance()
															.saveUserInfo(
																	App.userInfo);
													object = Protocol
															.getInstance()
															.getNewNoticeOrMessage(
																	num,
																	userid,
																	mType,
																	sType);
													for (CallBack back : getCallbacks(mBack)) {
														back.getNewNoticeOrMessageSuccess((NoticeOrMessageCount) object);
													}
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
								intent.putExtra("callthepage",
										CallThePage.LOBOHALL);
								mainActivity.startActivity(intent);
							} else {
								LoginActivity
										.forwartLoginActivity(
												App.getInstance(),
												CallThePage.LOBOHALL);
							}
						}

					} else {
						for (CallBack back : getCallbacks(mBack)) {
							back.getNewNoticeOrMessageSuccess(count);
						}
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getNewNoticeOrMessageFailure(e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 获取比赛竞猜游戏信息
	 * 
	 * @param num_请求号
	 * @param dayNum_请求数据天数
	 * @param userId_请求用户ID
	 * @param mBack_请求回调
	 */
	public void getMatchGameInfo(final String num, final String dayNum,
			final String userId, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getMatchGameInfo(num, dayNum,
							userId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getMatchGameInfoSuccess(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getMatchGameInfoFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取本场比赛的留言板信息
	 * 
	 * @param num_接口号
	 * @param leagueCode_比赛Id
	 * @param pageAmount_每页显示条数
	 *            （默认10条）
	 * @param pageNo_查询页码
	 *            ，默认1
	 * @return
	 * @throws Exception
	 */
	public void getMatchMessage(final String num, final String leagueCode,
			final String pageAmount, final String pageNo, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getMatchMessage(num,
							leagueCode, pageAmount, pageNo);
					for (CallBack back : getCallbacks(mBack)) {
						back.getMatchMessageSuccess(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getMatchMessageFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 游乐场游戏投注
	 * 
	 * @param num_接口码
	 * @param userId_用户Id
	 * @param gameId_
	 * @param leagueCode
	 * @param questionId
	 * @param content
	 * @param mBack
	 */
	public void gameBetting(final String num, final String userId,
			final String gameId, final String leagueCode,
			final String questionId, final String content,
			final String betAmount, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().gameBetting(num, userId,
							gameId, leagueCode, questionId, content, betAmount);
					for (CallBack back : getCallbacks(mBack)) {
						back.resultSuccessStr(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.resultFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 大转盘的投注接口
	 * @param num
	 * @param userId
	 * @param gameId
	 * @param content
	 * @param betAmount
	 * @param mBack
	 */
//	public void luckyPanBetting(final String num, final String userId, final String gameId, final String content, final String betAmount, final CallBack mBack){
//		execute(new Runnable() {
//			@Override
//			public void run() {
//				String json = null;
//				try {
//					json = Protocol.getInstance().luckyPanBetting(num, userId, gameId, content, betAmount);
//					for (CallBack back : getCallbacks(mBack)) {
//						back.resultSuccessStr(json);
//						break;
//					}
//				} catch (CException e) {
//					for (CallBack back : getCallbacks(mBack)) {
//						back.resultFailure(e.getMessage());
//						break;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	
//	}
	
	/**
	 * 娱乐场-我的竞猜记录
	 * @param num
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @param status
	 * @param pageAmount
	 * @param pageNo
	 * @param mBack
	 */
	public void myGuessRecord(final String num, final String userId, 
			final String beginTime, final String endTime, final String status,
			final String pageAmount, final String pageNo, final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().myGuessRecord(num, userId, beginTime, endTime, status, pageAmount, pageNo);
					for (CallBack back : getCallbacks(mBack)) {
						mBack.resultSuccessStr(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						mBack.resultFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}
	
	/**
	 * 获取当前排行（竞猜）
	 * @param num
	 * @param userId
	 * @param mBack
	 */
	public void myRankingListC(final String num, final String userId, final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().myRankingListC(num, userId);
					for (CallBack back : getCallbacks(mBack)) {
						back.resultSuccessStr(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.resultFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}
	
	/**
	 * 分享
	 * @Description:
	 * @param num
	 * @param gameId
	 * @param shareType
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016-8-5
	 */
	public void share(final String num, final String gameId, final String shareType, final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				ShareObj obj = null;
				try {
					obj = Protocol.getInstance().share(num, gameId, shareType);
					for (CallBack back : getCallbacks(mBack)) {
						back.shareSuccess(obj);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.shareFailure(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}
	
	/**
	 * 分享增加乐米
	 * @Description:
	 * @param num
	 * @param gameId
	 * @param leagueCode
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016-8-5
	 */
	public void shareAddLeMi(final String num, final String userId, final String gameId, final String leagueCode, final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				ShareAddLeMiObj obj = null;
				try {
					obj = Protocol.getInstance().shareAddLeMi(num, userId, gameId, leagueCode);
					for (CallBack back : getCallbacks(mBack)) {
						back.shareAddLeMiSuccess(obj);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.shareAddLeMiFailure(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}
	
	/**
	 * 获取用户周排行（竞猜）
	 * @param num
	 * @param userId
	 * @param mBack
	 */
	public void myRankingListW(final String num, final String userId,final String issue, final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().myRankingListW(num,userId, issue);
					for (CallBack back : getCallbacks(mBack)) {
						back.resultSuccessStr(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.resultFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 获取周排行期次信息
	 * @param num 接口名称
	 * @param weekNum 请求期数
	 * @param mBack 回调  resultSuccessStr & resultFailure
	 */
	public void getRankListDate(final String num, final String weekNum, final String gameId, final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getRankListDate(num,weekNum,gameId);
					for (CallBack back : getCallbacks(mBack)) {
						back.resultSuccessStr(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.resultFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 发布一条竞猜比赛留言
	 * 
	 * @param num
	 * @param userId
	 * @param leagueCode
	 * @param content
	 * @param mBack
	 */
	public void sendMatchMessage(final String num, final String userId,
			final String leagueCode, final String content, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().sendMatchMessage(num, userId,
							leagueCode, content);
					for (CallBack back : getCallbacks(mBack)) {
						back.resultSuccessStr(json);
						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.resultFailure(e.getMessage());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 获取游戏大厅游戏列表
	 * @param num
	 * @param mBack
	 */
	public void getGameList(final String num,final String timeId,final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getGameList(num,timeId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getGameListSuccess(json);
//						break;
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getGameListFailure(e.getMessage());
//						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 获取游戏大厅游戏列表
	 * @param num
	 * @param mBack
	 */
	public void getQiNiuParams(final String num,final String userId,final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getQiNiuParams(num,userId);
					for (CallBack back : getCallbacks(mBack)) {
						back.getQiNiuParamsSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getQiNiuParamsFailure(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 向服务器提交用户图片信息
	 * @param num
	 * @param userId_用户ID
	 * @param headImgUrl_七牛返回图片key
	 * @param mBack
	 */
	public void updateUserHeaderImage(final String num,final String userId,final String headImgUrl,final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().updateUserHeaderImage(num,userId,headImgUrl);
					for (CallBack back : getCallbacks(mBack)) {
						back.updateUserHeaderImageSuccessStr(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.updateUserHeaderImageFailure(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取一场制胜系统匹配对阵
	 * @Description:
	 * @param num
	 * @param matchMsg
	 * @param issueNo
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016-10-19
	 */
	public void getOneWinSys(final String num, final String matchMsg, final String issueNo, final CallBack mBack) {
		execute(new Runnable() {

			@Override
			public void run() {
				ResMatchAgainstInfo json = null;
				try {
					json = Protocol.getInstance().getOneWinSys(num, matchMsg, issueNo);
					for (CallBack back : getCallbacks(mBack)) {
						back.getOneWinSysSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getOneWinSysFailure(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 获取对阵信息详情
	 * @param num
	 * @param leagueCode
	 * @param mBack
	 */
	public void getLeagueDetail(final String num, final String leagueCode, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getLeagueDetail(num,
							leagueCode);
					mBack.getLeagueDetailSuccess(json);
				} catch (CException e) {
					mBack.getLeagueDetailFailure(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 首页轮播文字
	 * @Description:
	 * @param num
	 * @param mBack
	 * @author:www.wenchuang.com
	 * @date:2016-11-30
	 */
	public void getHallText(final String num, final CallBack mBack) {

		execute(new Runnable() {
			@Override
			public void run() {
				HallTextList list = null;
				try {
					list = Protocol.getInstance().getHallText(num);
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallTextSuccess(list);
					}
				} catch (Exception e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.getHallTextFailure(e.getMessage());
					}
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 验证验证码是否正确
	 * @param userId 用户id
	 * @param validCode 验证码
	 * @param cell 手机号码
	 * @param mBack
	 */
	public void validateCode(final String num, final String userId, final String validCode,final String cell, final CallBack mBack) {
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().validateCode(num, userId, validCode, cell);
					for (CallBack back : getCallbacks(mBack)) {
						back.validateCodeSuccess(json);
					}
				} catch (CException e) {
					for (CallBack back : getCallbacks(mBack)) {
						back.validateCodeFail(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 获取用户竞猜投注胜率
	 * @param num
	 * @param userId
	 * @param mBack
	 */
	public void getMatchGuessWinning(final String num,final String userId,final CallBack mBack){
		execute(new Runnable() {
			@Override
			public void run() {
				String json = null;
				try {
					json = Protocol.getInstance().getMatchGuessWinning(num, userId);
					mBack.getMatchGuessWinningSuccess(json);
				} catch (CException e) {
					mBack.getMatchGuessWinningFail(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
