package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.animation.StretchAnimation;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.bean.Betting;
import cn.com.cimgroup.bean.Product;
import cn.com.cimgroup.bean.TcjczqObj;
import cn.com.cimgroup.bean.TzObj;
import cn.com.cimgroup.bean.UserCount;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.dailog.DialogProgress;
import cn.com.cimgroup.dailog.LotteryProgressDialog;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.frament.GridFragment;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * Created by small on 16/1/14. 确认支付; PS:购买红包和乐米
 */
@SuppressLint({ "DefaultLocale", "InflateParams" })
public class CommitPayActivity extends BaseActivity implements
		OnClickListener, AdapterView.OnItemClickListener,
		CompoundButton.OnCheckedChangeListener {
	/** 支付类型：开启微信支付为“元”;未开启为米 */
	private static String UNIT;

	public static final String TYPE = "type";
	public static final String DATA = "data";
	
	private boolean isQuick = false;
	/** 返回 */
	private TextView id_back;
	/** 标题 */
	private TextView id_title;
	// private TextView freeCountView;// 暂时无用
	/** 提交支付 */
	private TextView id_commit;

	/** 红包抵扣 */
	private TextView id_cimmitpay_red_deduction;

	/** 账号余额 */
	private TextView id_crash_money;
	/** 订单需支付总金额 */
	private TextView id_order_pay_money;
	/** 实际需支付金额 */
	private TextView id_real_pay_money;
	/** 支付总额 */
	private double mSumMoney = 0;

	/** 账户总余额 */
	private BigDecimal mMoney;
	/** 可用余额数值 */
	private double userMoney = 0.0d;
	/** 购买红包跳转 */
	private Product product;
	private Serializable data;
	/** 投注并支付 */
	private TzObj tzObj;
	/** 投注成功--代付款 */
	private AllBetting allBetting;
	List<JSONObject> list = new ArrayList<JSONObject>();
	Map<String, String> bettingReq = new LinkedHashMap<String, String>();
	// /** 红包显示布局 */
	// private LinearLayout id_redbag_layout;
	/** 红包显示GridView */
	private GridView id_red_pkg_grid;
	private int choosePosition = -1;
	// private TzPkgAdapter adapter;
	private DialogProgress dialogProgress;
	/** 去充值 */
	private RelativeLayout id_to_recharge;
	/** 余额支付选择、取消layout */
	private LinearLayout id_paytype_layout_crash;
	/** 微信支付选择、取消layout */
	private LinearLayout id_paytype_layout_wechat;
	/** 红包支付选择、取消layout */
	private LinearLayout id_paytype_layout_redbag;

	/** 红包显示隐藏 动画 */
	private StretchAnimation mRedBagBeiAnimation;

	/** 支付方式 */

	/** 余额选项前的选框 */
	private ImageView id_crash_image;

	/** 微信选项前的选框 */
	private ImageView id_wechat_image;

	/** 红包支付选择框 选择、取消 */
	private ImageView id_redbag_image;

	private Intent intent;
	/** 红包显示适配器 */
	private MyRedPackageAdapter mAdapter;

	/** 支付方式 */
	private String payType = "1";

	/** 显示所选择的红包以及类型 */
	private TextView id_redpackage_choose_show;

	/** 支付结果回调 */
	private PayReceiver mPayReceiver;

	private boolean isCommit = true;

	private int mType = 0;
	
	private int mLotoId = 0;
	
	/** 网络加载loding框 **/
	protected LotteryProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UNIT = GlobalConstants.ISOPENWECHAT ? "元" : "米";
		intent = getIntent();
		mType = intent.getIntExtra("type", 0);
		if (intent != null) {
			data = intent.getSerializableExtra(DATA);
			if (data != null) {
				if (data instanceof Product) {
					product = (Product) data;// 购买红包
					UNIT = "元";
				} else if (data instanceof TzObj) {
					mLotoId = intent.getIntExtra("lotoId", 0);
					isQuick = intent.getBooleanExtra("isQuick", false);
					tzObj = (TzObj) data;// 投注
				} else if (data instanceof AllBetting) {
					// 待支付订单
					allBetting = (AllBetting) data;
				}
			}
		}
		setContentView(R.layout.activity_commit_pay);
		
		// 初始化dialog
		mProgressDialog = new LotteryProgressDialog(CommitPayActivity.this);
		initView();
		initEvent();
		if (UNIT.equals("元"))
//			initDataMoney();
			getUserCash();
		else

			initDatasLeMi();
		initAnimation();
		IntentFilter intentFilter = new IntentFilter("com.lebocp.wxorderpay");
		if (mPayReceiver == null) {
			mPayReceiver = new PayReceiver();
		}
		registerReceiver(mPayReceiver, intentFilter);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		mRedBagBeiAnimation = new StretchAnimation(id_red_pkg_grid,
				StretchAnimation.TYPE.vertical, 300);
		// 你可以换不能给的插值器
		mRedBagBeiAnimation.setInterpolator(new AccelerateInterpolator());
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		id_back.setOnClickListener(this);
		id_commit.setOnClickListener(this);
		id_title.setText("确认支付");
		id_to_recharge.setOnClickListener(this);

		id_paytype_layout_crash.setOnClickListener(this);
		id_paytype_layout_redbag.setOnClickListener(this);
		onClick(id_paytype_layout_crash);
		if (GlobalConstants.ISOPENWECHAT) {
			id_paytype_layout_wechat.setOnClickListener(this);
		}
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_cimmitpay_red_deduction = (TextView) findViewById(R.id.id_cimmitpay_red_deduction);

		id_redpackage_choose_show = (TextView) findViewById(R.id.id_redpackage_choose_show);
		id_redpackage_choose_show.setVisibility(View.GONE);
		id_paytype_layout_redbag = (LinearLayout) findViewById(R.id.id_paytype_layout_redbag);
		id_paytype_layout_crash = (LinearLayout) findViewById(R.id.id_paytype_layout_crash);
		id_paytype_layout_wechat = (LinearLayout) findViewById(R.id.id_paytype_layout_wechat);
		id_crash_image = (ImageView) findViewById(R.id.id_crash_image);
		id_wechat_image = (ImageView) findViewById(R.id.id_wechat_image);
		id_redbag_image = (ImageView) findViewById(R.id.id_redbag_image);
		// 去充值
		id_to_recharge = (RelativeLayout) findViewById(R.id.id_to_recharge);
		id_back = (TextView) findViewById(R.id.id_back);
		id_title = (TextView) findViewById(R.id.id_title);
		id_crash_money = (TextView) findViewById(R.id.id_crash_money);
		id_order_pay_money = (TextView) findViewById(R.id.id_order_pay_money);
		id_real_pay_money = (TextView) findViewById(R.id.id_real_pay_money);
		id_commit = (TextView) findViewById(R.id.id_commit);
		id_cimmitpay_red_deduction.setText("红包抵扣：-0" + UNIT);
		id_red_pkg_grid = (GridView) findViewById(R.id.id_red_pkg_grid);
		id_red_pkg_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		id_red_pkg_grid.setVisibility(View.GONE);
		mTcjczqObjList = new ArrayList<TcjczqObj>();
		if (product != null && tzObj == null && allBetting == null) {
			id_paytype_layout_redbag.setVisibility(View.GONE);
			id_paytype_layout_wechat.setVisibility(View.GONE);
		}
		if (GlobalConstants.ISOPENWECHAT) {
			id_paytype_layout_wechat.setVisibility(View.VISIBLE);
		} else {
			id_paytype_layout_wechat.setVisibility(View.GONE);
		}

	}

	/**
	 * 初始化数据(现金)
	 */
	private void initDataMoney() {
		if (App.userInfo != null) {
			double moneys = App.userInfo.getBetAcnt() != null
					&& !App.userInfo.getBetAcnt().equals("") ? Double
					.parseDouble(App.userInfo.getBetAcnt()) : 0;
			double prize = App.userInfo.getPrizeAcnt() != null
					&& !App.userInfo.getPrizeAcnt().equals("") ? Double
					.parseDouble(App.userInfo.getPrizeAcnt()) : 0;
			userMoney = moneys + prize;
			mMoney = BigDecimal.valueOf(userMoney);
			id_crash_money.setText(mMoney + UNIT);
			if (product != null) {
				id_order_pay_money.setText(Double
						.parseDouble(product.productSaleMoney) + UNIT);
				mSumMoney = Double.parseDouble(product.productSaleMoney);
			} else if (tzObj != null) {
				getSortDate(App.userInfo);
				for (int i = 0; i < tzObj.bettingReq.size(); i++) {
					list.add(new JSONObject(tzObj.bettingReq.get(i)));
				}
				id_order_pay_money.setText(Integer.parseInt(tzObj.totalSum)
						+ UNIT);
				mSumMoney = Integer.parseInt(tzObj.totalSum);
			} else if (allBetting != null) {
				getSortDate(App.userInfo);
				id_order_pay_money.setText(Double.parseDouble(TextUtils
						.isEmpty(allBetting.getMoney()) ? "0" : allBetting
						.getMoney())
						+ UNIT);
				mSumMoney = Double.parseDouble(allBetting.getMoney());
			}
			id_real_pay_money.setText(mSumMoney + UNIT);
			if (id_red_pkg_grid != null) {
				id_red_pkg_grid.setOnItemClickListener(this);
			}

		} else {
			startActivity(LoginActivity.class);
		}
	}
	/**
	 * 获取用户现金余额
	 *
	 * @return
	 */
	private void getUserCash() {
//		String money = "";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			BigDecimal bet = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getBetAcnt()) ? "0" : App.userInfo.getBetAcnt());
			BigDecimal price = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getPrizeAcnt()) ? "0" : App.userInfo.getPrizeAcnt());
			mMoney = bet.add(price);
			id_crash_money.setText(mMoney + UNIT);

//			money = mMoney.toPlainString() + GlobalConstants.PROJECT_UNIT;
//
//			if (mMoney.doubleValue() > 100000d) {
//				money = mMoney.divide(new BigDecimal("10000"), 2,
//						RoundingMode.DOWN).toPlainString()
//						+ "万元";
//			} else
//				money = mMoney.toPlainString() + GlobalConstants.PROJECT_UNIT;
			if (product != null) {
				id_order_pay_money.setText(Double
						.parseDouble(product.productSaleMoney) + UNIT);
				mSumMoney = Double.parseDouble(product.productSaleMoney);
			} else if (tzObj != null) {
				getSortDate(App.userInfo);
				for (int i = 0; i < tzObj.bettingReq.size(); i++) {
					list.add(new JSONObject(tzObj.bettingReq.get(i)));
				}
				id_order_pay_money.setText(Integer.parseInt(tzObj.totalSum)
						+ UNIT);
				mSumMoney = Integer.parseInt(tzObj.totalSum);
			} else if (allBetting != null) {
				getSortDate(App.userInfo);
				id_order_pay_money.setText(Double.parseDouble(TextUtils
						.isEmpty(allBetting.getMoney()) ? "0" : allBetting
						.getMoney())
						+ UNIT);
				mSumMoney = Double.parseDouble(allBetting.getMoney());
			}
			id_real_pay_money.setText(mSumMoney + UNIT);
			if (id_red_pkg_grid != null) {
				id_red_pkg_grid.setOnItemClickListener(this);
			}

		} else {

			startActivity(LoginActivity.class);
		}
	}

	/**
	 * 初始化数据(乐米)
	 */
	private void initDatasLeMi() {
		if (App.userInfo != null) {
			userMoney = Double.parseDouble(App.userInfo.getIntegralAcnt());
			mMoney = BigDecimal.valueOf(userMoney);
			id_crash_money.setText(App.userInfo.getIntegralAcnt() + UNIT);
			if (product != null) {
				// 如果是购买红包,或者购买乐米 需要特殊处理，显示为用money余额
				// double moneys = App.userInfo.getBetAcnt() != null
				// && !App.userInfo.getBetAcnt().equals("") ? Double
				// .parseDouble(App.userInfo.getBetAcnt()) : 0;
				// double prize = App.userInfo.getPrizeAcnt() != null
				// && !App.userInfo.getPrizeAcnt().equals("") ? Double
				// .parseDouble(App.userInfo.getPrizeAcnt()) : 0;
				// userMoney = moneys + prize;
				// mMoney = BigDecimal.valueOf(userMoney);
				// id_crash_money.setText(mMoney + UNIT);
				// 如果是购买红包或者购买乐米,需要特殊处理 ，显示为用money余额

				id_order_pay_money.setText(Double
						.parseDouble(product.productSaleMoney) + UNIT);
				mSumMoney = Double.parseDouble(product.productSaleMoney);
			} else if (tzObj != null) {
				// getSortDate(App.userInfo);
				for (int i = 0; i < tzObj.bettingReq.size(); i++) {
					list.add(new JSONObject(tzObj.bettingReq.get(i)));
				}
				id_order_pay_money.setText(Integer.parseInt(tzObj.totalSum)
						+ UNIT);
				mSumMoney = Integer.parseInt(tzObj.totalSum);
			}
			id_real_pay_money.setText(mSumMoney + UNIT);
			if (id_red_pkg_grid != null) {
				id_red_pkg_grid.setOnItemClickListener(this);
			}

		} else {
			startActivity(LoginActivity.class);
		}
		id_paytype_layout_redbag.setVisibility(View.GONE);
	}

	private CallBack callBack = new CallBack() {
		@Override
		public void onSuccess(final Object t) {
			CommitPayActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();					
					}
					if (t != null) {
						GlobalConstants.isRefreshFragment = true;
						GlobalConstants.personEndIndex = 0;
						GlobalConstants.personGameNo = "ALL";
						if (t instanceof UserCount) {
							// 购买成功;更新userInfor;
							UserCount userCount = (UserCount) t;
							if (userCount.resCode.equals("0")) {
								saveUserCount2UserInfo(userCount, App.userInfo);
								CommitPayActivity.this.finish();
								Intent intent = new Intent(
										CommitPayActivity.this,
										LotteryBettingSuccessActivity.class);
								String lototype = mType == 4103 ? "paySucess_lemi"
										: "paySucess";
								intent.putExtra("lototype", lototype);
								startActivity(intent);
								finish();
							} else {
								ToastUtil.shortToast(CommitPayActivity.this,
										userCount.resMsg);
							}

						}
					}
				}
			});
		}

		/**
		 * 投注成功
		 */
		public void syncLotteryBettingSuccess(final Betting obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					if (obj.getResCode().equals("0")) {
						GlobalConstants.isRefreshFragment = true;
						GlobalConstants.personGameNo = "ALL";
						GlobalConstants.personEndIndex = 0;
						ActivityManager.popClass(LotteryCartActivity.class);
						ToastUtil.shortToast(CommitPayActivity.this, "投注成功");
						App.userInfo.setIntegralAcnt(obj.getIntegralAcnt());
						App.userInfo.setPrizeAcnt(obj.getPrizeAcnt());
						App.userInfo.setBetAcnt(obj.getBetAcnt());
						App.userInfo.redPkgNum = obj.redPkgNum;
						App.userInfo.redPkgAccount = obj.redPkgAccount;
						App.userInfo.buyRedMoney = obj.buyRedMoney;
						App.userInfo.convertRedNum = obj.convertRedNum;
						App.userInfo.convertRedTotalMoney = obj.convertRedTotalMoney;
						App.userInfo.convertRedList = obj.convertRedList;
						App.userInfo.giveRedNum = obj.giveRedNum;
						App.userInfo.giveRedTotalMoney = obj.giveRedTotalMoney;
						App.userInfo.giveRedList = obj.giveRedList;
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						Intent tIntent = new Intent();
						setResult(GlobalConstants.CLEARLATTERYSELECT, tIntent);
						// 投注成功，关闭本页面
						Intent intent = new Intent(CommitPayActivity.this,
								LotteryBettingSuccessActivity.class);
						intent.putExtra("issue",
								LotteryBettingUtil.getIssue(tzObj.lotteryName));
						intent.putExtra("lototype", tzObj.gameType);
						intent.putExtra("isQuick", isQuick);
						intent.putExtra("lotoId", mLotoId);
						// startActivity(intent);
						startActivityForResult(intent,
								GlobalConstants.COMMIT_PAY);
						finish();
					} else {
						ToastUtil.shortToast(CommitPayActivity.this,
								obj.getResMsg());
					}
				}
			});
		}
		
		public void syncLotteryBettingFailure(final String error) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					ToastUtil.shortToast(CommitPayActivity.this, error);
				}

			});
		};

		@Override
		public void onFail(final String error) {
			super.onFail(error);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					ToastUtil.shortToast(CommitPayActivity.this, error);
				}

			});
		}

		/** 投注成功返回 */
		public void bettingPaySuccess(final Betting obj) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					doResult(obj);
					finish();
				}

			});
		};

		/** 投注失败返回 */
		public void bettingPayError(final String message) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					ToastUtil.shortToast(CommitPayActivity.this, message);
				}
			});

		};

		/** 再次支付成功 */
		public void orderAgainPaySuccess(final Betting obj) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					doResult(obj);
					finish();
				}
			});

		};

		public void orderAgainPayError(final String message) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialogProgress != null) {
						dialogProgress.dismiss();
					}
					if(mProgressDialog != null){
						mProgressDialog.hideDialog();
					}
					ToastUtil.shortToast(CommitPayActivity.this, message);
				}
			});

		};

	};

	/**
	 * 统一处理 支付、再次支付返回结果
	 * 
	 * @param obj
	 */
	private void doResult(Betting obj) {
		if (obj != null && obj.getResCode().equals("0")) {
			GlobalConstants.isRefreshFragment = true;
			GlobalConstants.personGameNo = "ALL";
			GlobalConstants.personEndIndex = 0;
			ActivityManager.popClass(LotteryCartActivity.class);
			App.userInfo.setIntegralAcnt(obj.getIntegralAcnt());
			App.userInfo.setPrizeAcnt(obj.getPrizeAcnt());
			App.userInfo.setBetAcnt(obj.getBetAcnt());
			App.userInfo.redPkgNum = obj.redPkgNum;
			App.userInfo.redPkgAccount = obj.redPkgAccount;
			App.userInfo.buyRedMoney = obj.buyRedMoney;
			App.userInfo.convertRedNum = obj.convertRedNum;
			App.userInfo.convertRedTotalMoney = obj.convertRedTotalMoney;
			App.userInfo.convertRedList = obj.convertRedList;
			App.userInfo.giveRedNum = obj.giveRedNum;
			App.userInfo.giveRedTotalMoney = obj.giveRedTotalMoney;
			App.userInfo.giveRedList = obj.giveRedList;
			UserLogic.getInstance().saveUserInfo(App.userInfo);

			if ((payType.equals("3") || payType.equals("4"))
					&& id_wechat_image.isSelected() && freeCount < mSumMoney) {
				// TODO 跳转微信支付
				prepayId = obj.prepayId;
				sign = obj.sign;
				appId = obj.appId;
				timestamp = obj.timestamp;
				nonceStr = obj.nonceStr;
				partnerId = obj.partnerId;
				// attach = obj.attach;
				initWXApi();// 初始化API
				if (!wxApi.isWXAppInstalled()) {
					ToastUtil.shortToast(CommitPayActivity.this,
							"未安装微信客户端,不能支付");
				} else {
					App.CALLBACK_ACTIVITY = "COMMITPAY";
					sendPayReq();
				}

			} else {
				ToastUtil.shortToast(CommitPayActivity.this, "投注成功");
				// 投注支付成功
				Intent tIntent = new Intent();
				setResult(GlobalConstants.CLEARLATTERYSELECT, tIntent);
				// 投注成功，关闭本页面
				ActivityManager.pop(CommitPayActivity.this);
				Intent intent = new Intent(CommitPayActivity.this,
						PaySuccessActivity.class);
				// Intent intent = new Intent(CommitPayActivity.this,
				// LotteryBettingSuccessActivity.class);

				// PaySuccessActivity
				if (tzObj != null) {
					intent.putExtra("issue",
							LotteryBettingUtil.getIssue(tzObj.lotteryName));
					intent.putExtra("lototype", tzObj.gameType);
				} else if (allBetting != null) {
					intent.putExtra("issue", allBetting.getIssue());
					intent.putExtra("lototype", allBetting.getGameNo());
				}

				startActivity(intent);
			}

		} else
			ToastUtil.shortToast(CommitPayActivity.this, obj.getResMsg());
	}

	/** 微信支付prepayId */
	private String prepayId;
	/** 微信支付 签名sign */
	private String sign;
	/** 微信支付--appId */
	public static String appId;
	/** 微信支付--时间戳 */
	private String timestamp;
	/** 微信支付--随机字符串 */
	private String nonceStr;
	/** 微信支付--商户号 */
	private String partnerId;
	// /** 微信支付--附加数据 */
	// private String attach;
	/** 支付请求 */
	private PayReq req;
	/** 微信Api */
	private IWXAPI wxApi;
	/** 红包Id */
	private String mRedBagId;
	/** 红包类型 */
	private String mRedBagType;

	private void initWXApi() {
		if (wxApi == null) {
			wxApi = WXAPIFactory.createWXAPI(this, null);
			// 支付参数
			req = new PayReq();
		}
	}

	/**
	 * 组装支付参数,调用支付接口
	 *
	 */
	private void sendPayReq() {
		req.appId = appId;
		req.partnerId = partnerId;
		req.prepayId = prepayId;
		req.packageValue = "Sign=WXPay";
		req.nonceStr = nonceStr;
		req.timeStamp = timestamp;
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = sign;
		wxApi.registerApp(appId);
		// 调用支付接口
		wxApi.sendReq(req);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_paytype_layout_redbag:
			id_redbag_image.setSelected(!id_redbag_image.isSelected());
			// if (id_redbag_image.isSelected())
			// // id_redbag_layout.setVisibility(View.VISIBLE);
			// mRedBagBeiAnimation.startAnimation(id_redbag_layout);
			// else
			if (!id_redbag_image.isSelected()) {
				int size = mTcjczqObjList.size();
				for (int i = 0; i < size; i++) {
					mTcjczqObjList.get(i).choose = false;
					choosePosition = -1;
				}
				changeFreeCount(null);
			}
			mRedBagBeiAnimation.startAnimation(id_red_pkg_grid);
			break;
		case R.id.id_paytype_layout_crash:
			// 选择、取消选择余额支付
			id_crash_image.setSelected(!id_crash_image.isSelected());
			if (id_wechat_image.isSelected()) {
				id_wechat_image.setSelected(false);
			}
			break;
		case R.id.id_paytype_layout_wechat:
			// 选择、取消选择微信支付
			id_wechat_image.setSelected(!id_wechat_image.isSelected());
			if (id_crash_image.isSelected()) {
				id_crash_image.setSelected(false);
			}
			break;
		case R.id.id_back:
			if (tzObj != null) {
				switch (tzObj.gameNo) {
				case GlobalConstants.TC_SF14:
				case GlobalConstants.TC_SF9:
				case GlobalConstants.TC_JQ4:
				case GlobalConstants.TC_BQ6:
					showExitDialog();
					break;
				default:
					finish();
					break;
				}
			} else {
				finish();
			}
			break;
		case R.id.id_to_recharge:
			// 充值;
			if (App.userInfo != null
					&& !TextUtils.isEmpty(App.userInfo.getRealName())) {

				if (UNIT.equals("元")) {
					startActivity(CenterReChargeActivity.class);
				} else {
					Intent buyLeMi = new Intent(CommitPayActivity.this,
							BuyRedPacketActivity.class);
					buyLeMi.putExtra("type", 0x1007);
					startActivity(buyLeMi);
				}
			} else {
				isValidate();
			}
			break;
		case R.id.id_commit:
			// 立即支付;
			// TODO 支付 购买红包、乐米
			if (product != null) {
				int tempType = intent.getIntExtra("type", 1);
				String type = "0";
				if (tempType == 0x1001) {
					type = "1";
				} else {
					type = "2";
				}
				mProgressDialog.showDialog();
				Controller.getInstance().confirmPayRequest(
						GlobalConstants.NUM_BUY, App.userInfo.getUserId(),
						type, product.productId, product.productSaleMoney,
						callBack);
			} else if (tzObj != null) {
				if (isCommit) {
					tz();// 投注生成订单并支付
				} else {
					ToastUtil.shortToast(CommitPayActivity.this, getResources()
							.getString(R.string.commit_pay_error));
				}
			} else if (allBetting != null) {
				// 订单的再次支付
				if (freeCount >= mSumMoney) {
					payType = "2";
				} else {
					if (id_crash_image.isSelected()) {
						if (id_redbag_image.isSelected()) {
							if (choosePosition != -1) {
								payType = "2";
							} else {
								ToastUtil.shortToast(mActivity, "请选择合适面值的红包");
								return;
							}
						} else {
							payType = "1";
						}
					} else if (id_wechat_image.isSelected()) {
						if (id_redbag_image.isSelected()) {
							if (choosePosition != -1) {
								payType = "4";
							} else {
								ToastUtil.shortToast(mActivity, "请选择合适面值的红包");
								return;
							}
						} else
							payType = "3";
					} else if (id_redbag_image.isSelected()) {
						if (choosePosition != -1) {
							payType = "2";
						} else {
							ToastUtil.shortToast(mActivity, "请选择合适面值的红包");
							return;
						}
					} else {
						ToastUtil.shortToast(mActivity, "请选择支付方式");
						return;
					}
				}
				if (choosePosition == -1) {
					if (GlobalConstants.isRed) {
						tzObj.isRedPackage = "0";
					} else {
						mRedBagId = "0";
						mRedBagType = "0";
					}
				} else {
					TcjczqObj item = (TcjczqObj) mAdapter
							.getItem(choosePosition);
					switch (item.type) {
					case TcjczqObj.BUY:
						mRedBagId = "0";
						mRedBagType = "0";
						break;
					case TcjczqObj.CONVERT:
						mRedBagId = item.converRedPkgID;
						mRedBagType = "1";
						break;
					case TcjczqObj.GIVE:
						mRedBagId = item.converRedPkgID;
						mRedBagType = "2";
						break;
					default:
						break;
					}
				}
				if (payType.equals("1")) {
					if (mSumMoney <= userMoney) {
						dialogProgress = DialogProgress.newIntance(
								getResources().getString(
										R.string.lottery_tz_loading), true);
						dialogProgress.show(getSupportFragmentManager(),
								"dialogProgress");
						Controller.getInstance().orderAgainPay(
								GlobalConstants.URL_ORDER_AGAIN_PAY,
								App.userInfo.getUserId(),
								allBetting.getOrderId(), "1", mSumMoney + "",
								payType, mRedBagId, mRedBagType, callBack);
					} else {
						isRecharge();
					}
				} else if (id_redbag_image.isSelected()
						&& !id_wechat_image.isSelected()) {
					if (mSumMoney - freeCount > 0) {
						ToastUtil.shortToast(mActivity, "红包金额不足，请选择其他的支付方式");
					}

				} else {
					dialogProgress = DialogProgress.newIntance(getResources()
							.getString(R.string.lottery_tz_loading), true);
					dialogProgress.show(getSupportFragmentManager(),
							"dialogProgress");
					Controller.getInstance().orderAgainPay(
							GlobalConstants.URL_ORDER_AGAIN_PAY,
							App.userInfo.getUserId(), allBetting.getOrderId(),
							"1", mSumMoney + "", payType, mRedBagId,
							mRedBagType, callBack);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 余额不足，提示是否充值
	 */
	private void isRecharge() {
		// final PromptDialog0 dialog = new
		// PromptDialog0(CommitPayActivity.this);
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(
				CommitPayActivity.this);
		// dialog.setMessage("余额不足，是否充值？");
		dialog.setMessage("乐米不足，是否购买？");
		dialog.setNegativeButton(getString(R.string.btn_canl),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.hideDialog();

					}
				});
		dialog.setPositiveButton(getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.hideDialog();
						// 2.5 期修改 乐米不足 跳转到购买乐米 因此不需要实名认证
						// if (App.userInfo != null
						// && !TextUtils.isEmpty(App.userInfo
						// .getRealName())) {
						// if (!GlobalConstants.ISOPENWECHAT) {
						// Intent buyLeMi = new
						// Intent(CommitPayActivity.this,BuyRedPacketActivity.class);
						// buyLeMi.putExtra("type",0x1007);
						// startActivity(buyLeMi);
						// }else
						// startActivity(CenterReChargeActivity.class);
						// } else {
						// isValidate();
						// }
						// 直接跳转购买乐米
						// 购买乐米;
						Intent buyLeMi = new Intent(CommitPayActivity.this,
								BuyRedPacketActivity.class);
						buyLeMi.putExtra(GridFragment.TYPE,
								GridFragment.BUYLEMI);
						startActivity(buyLeMi);
					}
				});
		dialog.showDialog();
	}

	/**
	 * 账户未完善信息，是否去完善
	 */
	private void isValidate() {
		// final PromptDialog0 dialog = new
		// PromptDialog0(CommitPayActivity.this);
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(
				CommitPayActivity.this);
		dialog.setMessage("您未实名认证，请先完成认证？");
		dialog.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hideDialog();

			}
		});
		dialog.setPositiveButton("去认证", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hideDialog();
				startActivity(UserManageActivity.class);
			}
		});
		dialog.showDialog();
	}

	/**
	 * 投注支付
	 */
	private void tz() {
		// TODO 投注支付
		if (choosePosition == -1) {
			if (GlobalConstants.isRed) {
				tzObj.isRedPackage = "0";
			} else {
				tzObj.isRedPackage = "2";
				tzObj.redPkgType = "0";
				tzObj.redPackageId = "0";
				tzObj.redPackageMoney = "0";
			}
		} else {
			TcjczqObj item = (TcjczqObj) mAdapter.getItem(choosePosition);
			switch (item.type) {
			case TcjczqObj.BUY:
				tzObj.redPkgType = "0";
				break;
			case TcjczqObj.CONVERT:
				tzObj.redPkgType = "1";
				tzObj.redPackageId = item.converRedPkgID;
				break;
			case TcjczqObj.GIVE:
				tzObj.redPkgType = "2";
				tzObj.redPackageId = item.giveRedPkgID;
				break;
			default:
				break;
			}
		}
		if (freeCount >= mSumMoney) {
			payType = "2";
		} else {
			if (id_crash_image.isSelected()) {
				if (id_redbag_image.isSelected()) {
					if (choosePosition != -1) {
						payType = "2";
					} else {
						ToastUtil.shortToast(mActivity, "请选择合适面值的红包");
						return;
					}
				} else {
					payType = "1";
				}
			} else if (id_wechat_image.isSelected()) {
				if (id_redbag_image.isSelected()) {
					if (choosePosition != -1) {
						payType = "4";
					} else {
						ToastUtil.shortToast(mActivity, "请选择合适面值的红包");
						return;
					}
				} else
					payType = "3";
			} else if (id_redbag_image.isSelected()) {
				if (choosePosition != -1) {
					payType = "2";
				} else {
					ToastUtil.shortToast(mActivity, "请选择合适面值的红包");
					return;
				}
			} else {
				ToastUtil.shortToast(mActivity, "请选择支付方式");
				return;
			}
		}
		if (payType.equals("1")) {
			if (mSumMoney <= userMoney) {
				payCommit();
			} else {
				isRecharge();
			}
		} else if (id_redbag_image.isSelected()
				&& !id_wechat_image.isSelected()) {
			if (mSumMoney - freeCount > 0) {
				ToastUtil.shortToast(mActivity, "红包金额不足，请选择其他的支付方式");
			} else
				payCommit();

		} else {
			payCommit();
		}

	}

	/**
	 * 下单并支付
	 */
	private void payCommit() {
		dialogProgress = DialogProgress.newIntance(
				getResources().getString(R.string.lottery_tz_loading), true);
		dialogProgress.show(getSupportFragmentManager(), "dialogProgress");
		if (!GlobalConstants.ISOPENWECHAT) {
			Controller.getInstance().syncLotteryBetting(
					GlobalConstants.NUM_BETTING, App.userInfo.getUserId(),
					tzObj.bettingType, tzObj.gameNo, tzObj.issue,
					tzObj.multiple, tzObj.totalSum, tzObj.isChase, tzObj.chase,
					tzObj.isRedPackage, tzObj.redPackageId,
					tzObj.redPackageMoney, tzObj.choiceType, tzObj.planEndTime,
					tzObj.stopCondition, list, tzObj.redPkgType, callBack);
		} else
			Controller.getInstance().bettingPay(
					GlobalConstants.URL_CREATE_ORDER,
					App.userInfo.getUserId(),
					tzObj.bettingType,
					tzObj.gameNo,
					tzObj.issue,
					tzObj.multiple,
					tzObj.totalSum,
					tzObj.isChase,
					tzObj.chase,
					payType,
					TextUtils.isEmpty(tzObj.redPackageId) ? ""
							: tzObj.redPackageId, tzObj.redPkgType,
					tzObj.redPackageMoney, tzObj.choiceType, tzObj.planEndTime,
					tzObj.stopCondition, list, callBack);
	}

	public static void saveUserCount2UserInfo(UserCount userCount,
			UserInfo userInfo) {
		if (userCount != null && userInfo != null) {
			userInfo.redPkgNum = userCount.redPkgNum;
			userInfo.redPkgAccount = userCount.redPkgAccount;
			userInfo.buyRedMoney = userCount.buyRedMoney;
			userInfo.convertRedNum = userCount.convertRedNum;
			userInfo.convertRedTotalMoney = userCount.convertRedTotalMoney;
			userInfo.convertRedList = userCount.convertRedList;
			userInfo.giveRedNum = userCount.giveRedNum;
			userInfo.giveRedTotalMoney = userCount.giveRedTotalMoney;
			userInfo.giveRedList = userCount.giveRedList;

			userInfo.setIntegralAcnt(userCount.lemiAccount);
			userInfo.setBetAcnt(userCount.betAccount);
			UserLogic.getInstance().saveUserInfo(userInfo);
		}
	}

	/** 红包集合 */
	private List<TcjczqObj> mTcjczqObjList;

	public void getSortDate(UserInfo userInfo) {
		// 红包使用顺序：赠送的红包（先快过期再专属后通用,）、购买的红包、兑换的红包。
		if (!userInfo.buyRedMoney.equals("0")) {
			TcjczqObj buyObj = new TcjczqObj();
			buyObj.buyRedMoney = userInfo.buyRedMoney;
			buyObj.type = TcjczqObj.BUY;
			mTcjczqObjList.add(buyObj);
		}// ///////////
		mTcjczqObjList = addToList(userInfo.convertRedList, mTcjczqObjList,
				TcjczqObj.CONVERT);
		mTcjczqObjList = addToList(userInfo.giveRedList, mTcjczqObjList,
				TcjczqObj.GIVE);
		int size = mTcjczqObjList.size();
		if (size == 0) {
			id_paytype_layout_redbag.setVisibility(View.GONE);
		} else {
			id_paytype_layout_redbag.setVisibility(View.VISIBLE);
			mAdapter = new MyRedPackageAdapter();
			id_red_pkg_grid.setAdapter(mAdapter);
			ViewUtils.setGridViewHeightBasedOnChildren(id_red_pkg_grid, 2);
		}
	}

	private List<TcjczqObj> addToList(HashMap<String, List<TcjczqObj>> map,
			List<TcjczqObj> list, int type) {
		if (map != null) {
			// TODO
			Iterator<Map.Entry<String, List<TcjczqObj>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<TcjczqObj>> entry = it.next();
				List<TcjczqObj> conList = entry.getValue();
				if (conList.size() > 0) {
					String name = entry.getKey();

					String tempName = "";
					if (tzObj != null) {
						tempName = tzObj.gameType.replace("_", "")
								.toLowerCase();
					} else if (allBetting != null) {
						tempName = allBetting.getGameNo().replace("_", "")
								.toLowerCase();
					}
					tempName += "List";
					if (tempName.equals(name) || name.equals("allList")) {
						list.addAll(conList);
						for (TcjczqObj item : conList) {
							item.type = type;
						}
					}
				}
			}
		}
		return list;
	}

	// TODO
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TcjczqObj item = (TcjczqObj) mAdapter.getItem(position);
		if (position == choosePosition) {
			item.choose = false;
			choosePosition = -1;
		} else {
			if (choosePosition != -1) {
				TcjczqObj oldItem = (TcjczqObj) mAdapter
						.getItem(choosePosition);
				oldItem.choose = false;
			}
			choosePosition = position;
			item.choose = true;
		}
		mAdapter.notifyDataSetChanged();
		changeFreeCount(item);
	}

	/** 抵扣金额 */
	private Double freeCount = 0d;
	/** 红包类型 */
	private String redPackageType = "";

	/**
	 * 修改红包抵扣金额;
	 * 
	 * @param item
	 */
	private void changeFreeCount(TcjczqObj item) {
		id_redpackage_choose_show.setText("");
		id_redpackage_choose_show.setVisibility(View.GONE);
		if (item != null) {
			// TODO 红包抵扣
			// sumMoney = Float.parseFloat(tzObj.totalSum);
			if (choosePosition != -1) {
				String money = "";
				if (item.type == TcjczqObj.BUY) {
					freeCount = mSumMoney > Double
							.parseDouble(item.buyRedMoney) ? Integer
							.parseInt(item.buyRedMoney) : mSumMoney;
					money = item.buyRedMoney;
					redPackageType = "普通红包";
				} else if (item.type == TcjczqObj.CONVERT) {
					freeCount = mSumMoney > Integer
							.parseInt(item.converRedMoney) ? Integer
							.parseInt(item.converRedMoney) : mSumMoney;
					money = item.converRedMoney;
					redPackageType = "兑换红包";
				} else if (item.type == TcjczqObj.GIVE) {
					freeCount = mSumMoney > Integer.parseInt(item.giveRedMoney) ? Integer
							.parseInt(item.giveRedMoney) : mSumMoney;
					money = item.giveRedMoney;
					redPackageType = "赠送红包";
				}
				id_redpackage_choose_show
						.setText(money + UNIT + redPackageType);
				id_redpackage_choose_show.setVisibility(View.VISIBLE);
			} else {
				freeCount = 0d;
			}
		} else {
			freeCount = 0d;
		}
		if (tzObj != null) {
			tzObj.redPackageMoney = freeCount + "";
		}

		id_cimmitpay_red_deduction.setText("红包抵扣：-" + freeCount + UNIT);
		id_real_pay_money.setText(Html.fromHtml("<font color='#f66248'>"
				+ (mSumMoney - freeCount) + "</font> " + UNIT));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked && choosePosition != -1) {
			id_red_pkg_grid.setVisibility(View.VISIBLE);
			TcjczqObj item = (TcjczqObj) mAdapter.getItem(choosePosition);
			item.choose = true;
			changeFreeCount(item);
		} else {
			id_red_pkg_grid.setVisibility(View.GONE);
			if (choosePosition != -1) {
				TcjczqObj item = (TcjczqObj) mAdapter.getItem(choosePosition);
				item.choose = false;

			}
			// freeCountView.setText(0 + getResources().getString(
			// R.string.lemi_unit));
			id_real_pay_money.setText(Float.parseFloat(tzObj.totalSum) + UNIT);
		}
		mAdapter.notifyDataSetChanged();
		ViewUtils.setGridViewHeightBasedOnChildren(id_red_pkg_grid, 2);
	}

	/**
	 * 红包ViewGroup适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class MyRedPackageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mTcjczqObjList.size();
		}

		@Override
		public Object getItem(int position) {
			return mTcjczqObjList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(CommitPayActivity.this).inflate(
						R.layout.item_tz_redpackage, parent, false);
				holder = new ViewHolder();
				holder.id_package_type = (TextView) view
						.findViewById(R.id.id_package_type);
				holder.id_package_facecount = (TextView) view
						.findViewById(R.id.id_package_facecount);
				holder.id_package_check = (ImageView) view
						.findViewById(R.id.id_package_check);
				holder.id_package_des = (TextView) view
						.findViewById(R.id.id_package_des);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			TcjczqObj item = mTcjczqObjList.get(position);
			if (item.choose) {
				holder.id_package_check.setVisibility(View.VISIBLE);
			} else {
				holder.id_package_check.setVisibility(View.GONE);
			}

			if (item != null) {
				if (item.type == TcjczqObj.GIVE) {
					if (item.giveRedMoney != null) {
						holder.id_package_type
								.setBackgroundResource(R.drawable.red_pkg_free);
						String time = GlobalTools.timeStamp2Date(
								item.giveFailTime, "");

						if (!StringUtil.isEmpty(time)) {
							time = time.substring(5, (time.length() - 3));
						}
						holder.id_package_des.setText(time + "失效" + "\n"
								+ item.giveDes);
						holder.id_package_facecount.setText(GlobalTools
								.string2Span(item.giveRedMoney));
					}
				} else if (item.type == TcjczqObj.CONVERT) {
					if (item.converRedMoney != null) {
						holder.id_package_type
								.setBackgroundResource(R.drawable.red_pkg_convert);
						String time = GlobalTools.timeStamp2Date(
								item.converFailTime, "");

						if (!StringUtil.isEmpty(time)) {
							time = time.substring(5, (time.length() - 3));
						}

						holder.id_package_des.setText(time + "失效" + "\n"
								+ item.converDes);
						holder.id_package_facecount.setText(GlobalTools
								.string2Span(item.converRedMoney));
					}
				} else if (item.type == TcjczqObj.BUY) {
					if (item.buyRedMoney != null) {
						holder.id_package_type
								.setBackgroundResource(R.drawable.red_pkg_buy);
						// holder.id_package_type.setBackgroundResource(R.drawable.red_pkg_buy);
						holder.id_package_des.setText("无失效期" + "\n" + "全场购彩通用");
						holder.id_package_facecount.setText(GlobalTools
								.string2Span(item.buyRedMoney));
					}
				}
			}

			return view;
		}

		class ViewHolder {
			/** 红包类型 */
			TextView id_package_type;
			/** 红包面值 */
			TextView id_package_facecount;
			/** 是否选择红包 */
			ImageView id_package_check;
			/** 红包描述 */
			TextView id_package_des;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPayReceiver != null) {
			unregisterReceiver(mPayReceiver);
			mPayReceiver = null;
		}

	}

	/**
	 * 支付成功的广播
	 *
	 */
	class PayReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String state = intent.getStringExtra("state");
			if (state.equals("1")) {
				ToastUtil.shortToast(context, "支付失败");
			} else {
				ToastUtil.shortToast(CommitPayActivity.this, "投注成功");
				// 投注支付成功
				Intent tIntent = new Intent();
				CommitPayActivity.this.setResult(
						GlobalConstants.CLEARLATTERYSELECT, tIntent);
				// 投注成功，关闭本页面
				ActivityManager.pop(CommitPayActivity.this);
				Intent intentJ = new Intent(CommitPayActivity.this,
						PaySuccessActivity.class);
				// Intent intent = new Intent(CommitPayActivity.this,
				// LotteryBettingSuccessActivity.class);

				// PaySuccessActivity
				if (tzObj != null) {
					intentJ.putExtra("issue",
							LotteryBettingUtil.getIssue(tzObj.lotteryName));
					intentJ.putExtra("lototype", tzObj.gameType);
				} else if (allBetting != null) {
					intentJ.putExtra("issue", allBetting.getIssue());
					intentJ.putExtra("lototype", allBetting.getGameNo());
				}
				startActivity(intentJ);
				finish();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			isCommit = false;
		}
	}
	

	private void showExitDialog() {
		final PromptDialog_Black_Fillet promptDialog0 = new PromptDialog_Black_Fillet(CommitPayActivity.this);
		promptDialog0.setMessage(getResources().getString(R.string.lottery_bid_content));
		promptDialog0.setPositiveButton(getResources().getString(R.string.btn_lottery_noback),
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					promptDialog0.hideDialog();
				}
			});
		promptDialog0.setNegativeButton(getResources().getString(R.string.btn_lottery_back),
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					promptDialog0.hideDialog();
					finish();
				}
			});
		promptDialog0.showDialog();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (tzObj != null) {
				switch (tzObj.gameNo) {
				case GlobalConstants.TC_SF14:
				case GlobalConstants.TC_SF9:
				case GlobalConstants.TC_JQ4:
				case GlobalConstants.TC_BQ6:
					showExitDialog();
					break;
				default:
					finish();
					break;
				}
			} else {
				finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
