package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.payeco.android.plugin.PayecoPluginLoadingActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umpay.quickpay.UmpPayInfoBean;
import com.umpay.quickpay.UmpayQuickPay;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.BankInfo;
import cn.com.cimgroup.bean.ChannelContent;
import cn.com.cimgroup.bean.EasyLinkReCharge;
import cn.com.cimgroup.bean.PayResult;
import cn.com.cimgroup.bean.ReCharge;
import cn.com.cimgroup.bean.ReChargeChannel;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.SDConfig;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.util.thirdSDK.AlipayUtils;
import cn.com.cimgroup.util.thirdSDK.EasyLinkUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.DeEnCoder;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 充值
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月12日
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class CenterReChargeActivity extends BaseActivity implements
		OnClickListener {

	/** 是否测试模式，如果是测试莫是 金额修改为0.01元 */
//	private static final boolean isTest = false;

	/** 银联 */
	private final static String EASYLINK = "EASYLINK";
	/** 支付宝 */
	private final static String ALIPAY = "ALIPAY";
	/** 联动优势 */
	private final static String UMPAY = "UMPAY";
	/** 微信充值 */
	private final static String WECHAT = "WECHAT";
	// 支付请求码
	private static final int REQUESTCODE = 10000;
	/** 充值金额 */
	private EditText editText_recharge_money;

	// private LinearLayout items;
	/** 快捷充值添加layout */
	// private LinearLayout layoutView_recharge_fast_items;

	private String money;
	/** 本地存储快捷充值--充值渠道列表 */
	private ReChargeChannel mChannel = null;

	private String timeId;

	private String selected;

	private int fastSelected;
	/** 快捷支付布局 */
	private LinearLayout layoutView_recharge_fast_text;

	/** 用户网络状况判断 **/
	private ConnectivityManager manager;
	/** 上方快捷充值银行卡显示列表信息 */
	private List<BankInfo> mBankInfoList;
	/** 下方银行卡显示列表信息 */
	private List<ChannelContent> mChannelContentList;
	/** 下方支持的充值渠道列表适配器 */
	private ChannelContentAdapter mChannelContentAdapter;

	/** 下方充值渠道列表listview */
	private ListView id_recharte_channel_list;

	/** 上方便捷充值列表 */
	private ListView id_recharge_bank_list;
	/** 便捷充值适配器 */
	private BankInfoAdapter mBankInfoAdapter;

	/** 选择充值方式 */
	private int mChangeIndex = 0;

	// /**
	// * @Fields payecoPayBroadcastReceiver : 易联支付插件广播
	// */
	// private BroadcastReceiver payecoPayBroadcastReceiver;
	/** 支付广播 */
	private PayReceiver mPayReceiver;

	private SharedPreferences shared;

	private int loginPattern;
	private String openId;
	private boolean auto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("recharge");
			Object object = SDConfig.getConfiguration().readSDCard();
			if (object != null) {
				mChannel = (ReChargeChannel) object;
			}
		}
		if (mChannel == null || mChannel.getContents() == null
				|| mChannel.getContents().size() == 0
				|| mChannel.getContents().get(0) == null
				|| mChannel.getContents().get(0).getChannelId() == null) {
			timeId = "0";
		} else {
			timeId = mChannel.getTimeId();
		}
//		timeId = "0";
		initView();
		initEvent();
		initData();

		IntentFilter intentFilter = new IntentFilter("com.lebocp.wxrecharge");
		if (mPayReceiver == null) {
			mPayReceiver = new PayReceiver();
		}
		registerReceiver(mPayReceiver, intentFilter);
		
		if (App.userInfo != null) {
			Controller.getInstance().getReChargeList(
					GlobalConstants.NUM_RECHARGECHANNEL,
					App.userInfo.getUserId(), timeId, mBack);
		}
	}

	private void initData() {
		shared = getSharedPreferences(GlobalConstants.PATH_SHARED_MAC,
				Context.MODE_PRIVATE);
		loginPattern = shared.getInt("loginpattern", 0);
		openId = shared.getString("openid", "");
		auto = shared.getBoolean("auto", false);
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		editText_recharge_money.addTextChangedListener(moneyWatcher);
	}

	private TextWatcher moneyWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String money = s.toString();
			if (!TextUtils.isEmpty(money)) {
				int iMoney = Integer.parseInt(money);

				if (iMoney > 1000000) {
					editText_recharge_money.setText("1000000");
				} else if (iMoney < 1 && money.length() > 1) {
					editText_recharge_money.setText("0");
				}
			}

		}
	};

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		// 初始化上方充值渠道列表
		mBankInfoList = new ArrayList<BankInfo>();
		id_recharge_bank_list = (ListView) findViewById(R.id.id_recharge_bank_list);
		mBankInfoAdapter = new BankInfoAdapter();
		id_recharge_bank_list.setAdapter(mBankInfoAdapter);
		id_recharge_bank_list.setVisibility(View.GONE);
		// 初始化下方充值渠道列表
		mChannelContentList = new ArrayList<ChannelContent>();
		id_recharte_channel_list = (ListView) findViewById(R.id.id_recharge_channel_list);
		mChannelContentAdapter = new ChannelContentAdapter();
		id_recharte_channel_list.setAdapter(mChannelContentAdapter);

		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);

		((TextView) findViewById(R.id.textView_title_word))
				.setText(getResources().getString(R.string.tradetype4));
		((ImageView) findViewById(R.id.imageView_recharge_add))
				.setOnClickListener(this);

		((ImageView) findViewById(R.id.imageView_recharge_reduce))
				.setOnClickListener(this);

		editText_recharge_money = (EditText) findViewById(R.id.editText_recharge_money);
		Intent intent = getIntent();
		int rechargeMoney = intent.getIntExtra("money", 100);
		if (rechargeMoney < 10) {
			rechargeMoney = 10;
		}
		editText_recharge_money.setText(rechargeMoney + "");
		
		Editable text = editText_recharge_money.getText();
		Spannable spanText = text;
		Selection.setSelection(spanText, text.length());

		((TextView) findViewById(R.id.textView_recharge_submit))
				.setOnClickListener(this);

		layoutView_recharge_fast_text = (LinearLayout) findViewById(R.id.layoutView_recharge_fast_text);

		// items = (LinearLayout) findViewById(R.id.layoutView_recharge_items);
		//
		// layoutView_recharge_fast_items = (LinearLayout)
		// findViewById(R.id.layoutView_recharge_fast_items);

		TextView explain = (TextView) findViewById(R.id.textView_recharge_explain);
		explain.setText(Html.fromHtml(getResources().getString(
				R.string.recharge_reminde)));
		TextView explain1 = (TextView) findViewById(R.id.textView_recharge_explain1);
		explain1.setText(Html.fromHtml(getResources().getString(
				R.string.recharge_reminde1)));
		explain1.setMovementMethod(LinkMovementMethod.getInstance());
		// 初始化支付结果广播接收器
		EasyLinkUtils
				.initPayecoPayBroadcastReceiver(CenterReChargeActivity.this);

		// 注册支付结果广播接收器t
		EasyLinkUtils
				.registerPayecoPayBroadcastReceiver(CenterReChargeActivity.this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EasyLinkUtils
				.unRegisterPayecoPayBroadcastReceiver(CenterReChargeActivity.this);
		Controller.getInstance().removeCallback(mBack);
		if (mPayReceiver != null) {
			unregisterReceiver(mPayReceiver);
			mPayReceiver = null;
		}

	};

	/**
	 * 每条item
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月12日
	 */
	public class ItemDetail {

		public ImageView icon;
		public TextView bigText;
		public TextView smallText;
		public LinearLayout layout;

		public ItemDetail(View view) {
			bigText = (TextView) view
					.findViewById(R.id.textView_recharge_item_big);
			smallText = (TextView) view
					.findViewById(R.id.textView_recharge_item_small);
			icon = (ImageView) view
					.findViewById(R.id.imageView_recharge_item_icon);
		}
	}

	/**
	 * 快速充值列表适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class BankInfoAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mBankInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return mBankInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolderBank holder = null;
			if (view == null) {
				view = LayoutInflater.from(CenterReChargeActivity.this)
						.inflate(R.layout.item_fast_recharge, null);
				holder = new ViewHolderBank();
				holder.imageView_recharge_fast_item_icon = (ImageView) view
						.findViewById(R.id.imageView_recharge_fast_item_icon);
				holder.textView_recharge_fast_item_bank = (TextView) view
						.findViewById(R.id.textView_recharge_fast_item_bank);
				holder.textView_recharge_fast_item_banknum = (TextView) view
						.findViewById(R.id.textView_recharge_fast_item_banknum);
				holder.textView_recharge_fast_item_banktype = (TextView) view
						.findViewById(R.id.textView_recharge_fast_item_banktype);

				view.setTag(holder);
			} else {
				holder = (ViewHolderBank) view.getTag();
			}
			final BankInfo bank = mBankInfoList.get(position);
			holder.textView_recharge_fast_item_banktype.setText(bank
					.getCardType().equals("1") ? getResources().getString(
					R.string.recharge_jieji) : getResources().getString(
					R.string.recharge_xinyong));
			holder.textView_recharge_fast_item_bank.setText(bank.getBankname());
			holder.textView_recharge_fast_item_banknum.setText(getResources()
					.getString(
							R.string.recharge_num,
							bank.getCardNum().substring(
									bank.getCardNum().length() - 4,
									bank.getCardNum().length())));
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					money = editText_recharge_money.getText().toString();
					if (Double.parseDouble(money) < 10) {
						ToastUtil.shortToast(
								CenterReChargeActivity.this,
								getResources().getString(
										R.string.recharge_error));
						return;
					}
					showLoadingDialog();

					// 银联充值
					Controller.getInstance().easyLinkReCharge(
							GlobalConstants.NUM_EASYLINK,
							App.userInfo.getUserId(), bank.getCardNum(), money,
							bank.getCell(), bank.getProvince(), bank.getCity(),
							App.userInfo.getRealName(),
							App.userInfo.getIdCard(), bank.getCardType(),
							bank.getBankname(), bank.getCityCode(),
							bank.getProvinceCode(), mBack);
				}
			});

			return view;
		}
	}

	/** 快捷充值Bank ViewHolder */
	class ViewHolderBank {
		/** 银行卡图片 */
		ImageView imageView_recharge_fast_item_icon;
		/** 银行卡标题 */
		TextView textView_recharge_fast_item_bank;
		/** 银行卡描述--尾号 */
		TextView textView_recharge_fast_item_banknum;
		/** 银行卡描述--类别 */
		TextView textView_recharge_fast_item_banktype;
	}

	// /**
	// * 添加银行列表
	// *
	// * @param bankInfos
	// */
	// private void addFastItem(List<BankInfo> bankInfos) {
	// if (layoutView_recharge_fast_items != null) {
	// layoutView_recharge_fast_items.removeAllViews();
	// }
	// for (int i = 0; i < bankInfos.size(); i++) {
	// View view = View.inflate(mActivity, R.layout.item_fast_recharge,
	// null);
	// final BankInfo bankInfo = bankInfos.get(i);
	// ItemFastDetail item = new ItemFastDetail(view);
	//
	// if (Integer.parseInt(bankInfo.getCardType()) == 1) {
	// item.banktype.setText(getResources().getString(
	// R.string.recharge_jieji));
	// } else {
	// item.banktype.setText(getResources().getString(
	// R.string.recharge_xinyong));
	// }
	//
	// item.bank.setText(bankInfo.getBankname());
	// item.banknum.setText(getResources().getString(
	// R.string.recharge_num,
	// bankInfo.getCardNum().substring(
	// bankInfo.getCardNum().length() - 4,
	// bankInfo.getCardNum().length())));
	//
	// view.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// money = editText_recharge_money.getText().toString();
	// if (Double.parseDouble(money) < 10) {
	// ToastUtil.shortToast(
	// CenterReChargeActivity.this,
	// getResources().getString(
	// R.string.recharge_error));
	// return;
	// }
	// showLoadingDialog();
	//
	// // 银联充值
	// Controller.getInstance().easyLinkReCharge(
	// GlobalConstants.NUM_EASYLINK,
	// App.userInfo.getUserId(), bankInfo.getCardNum(),
	// money, bankInfo.getCell(), bankInfo.getProvince(),
	// bankInfo.getCity(), App.userInfo.getRealName(),
	// App.userInfo.getIdCard(), bankInfo.getCardType(),
	// bankInfo.getBankname(), bankInfo.getCityCode(),
	// bankInfo.getProvinceCode(), mBack);
	// }
	// });
	// layoutView_recharge_fast_items.addView(view);
	// }
	// }

	/**
	 * 下方银行卡适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class ChannelContentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mChannelContentList.size();
		}

		@Override
		public Object getItem(int position) {
			return mChannelContentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			ViewHolderChannel holder = null;
			if (view == null) {
				view = LayoutInflater.from(CenterReChargeActivity.this)
						.inflate(R.layout.item_recharge, null);
				holder = new ViewHolderChannel();
				holder.imageView_recharge_item_icon = (ImageView) view
						.findViewById(R.id.imageView_recharge_item_icon);
				holder.textView_recharge_item_big = (TextView) view
						.findViewById(R.id.textView_recharge_item_big);
				holder.textView_recharge_item_small = (TextView) view
						.findViewById(R.id.textView_recharge_item_small);
				view.setTag(holder);
			} else {
				holder = (ViewHolderChannel) view.getTag();
			}
			final ChannelContent channel = mChannelContentList.get(position);
			holder.textView_recharge_item_big.setText(channel.getChannelName());
			holder.textView_recharge_item_small.setText(channel.getSummary());
			String channelCode = channel.getChannelCode();
			if (EASYLINK.equals(channelCode)) {
				holder.imageView_recharge_item_icon
						.setImageDrawable(getResources().getDrawable(
								R.drawable.icon_recharge_upcash));
			} else if (ALIPAY.equals(channelCode)) {
				holder.imageView_recharge_item_icon
						.setImageDrawable(getResources().getDrawable(
								R.drawable.icon_recharge_alipay));
			} else if (UMPAY.equals(channelCode)) {
				holder.imageView_recharge_item_icon
						.setImageDrawable(getResources().getDrawable(
								R.drawable.icon_recharge_ump));
			} else if (WECHAT.equals(channelCode)) {
				holder.imageView_recharge_item_icon
						.setImageDrawable(getResources().getDrawable(
								R.drawable.wechat_pay_logo));
			}

			view.setBackgroundColor(mChangeIndex == position ? getResources()
					.getColor(R.color.color_bank_choose) : Color.WHITE);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mChangeIndex = position;
					selected = channel.getChannelCode();
					notifyDataSetChanged();
				}
			});
			return view;
		}
	}

	/** 渠道ViewHolder */
	class ViewHolderChannel {
		/** 银行卡图片 */
		ImageView imageView_recharge_item_icon;
		/** 银行卡标题 */
		TextView textView_recharge_item_big;
		/** 银行卡描述 */
		TextView textView_recharge_item_small;
	}

	CallBack mBack = new CallBack() {

		public void easyLinkReChargeSuccess(final EasyLinkReCharge reCharge) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if (Integer.parseInt(reCharge.getResCode()) == 0) {
						Intent intent = new Intent(CenterReChargeActivity.this,
								PayecoPluginLoadingActivity.class);
						JSONObject json = new JSONObject();
						try {
							json.put("Version", reCharge.getVersion());
							json.put("MerchOrderId", reCharge.getOrderId());
							json.put("MerchantId", reCharge.getMerchantId());
							json.put("Amount", reCharge.getAmount());
							json.put("TradeTime", reCharge.getOprTime());
							json.put("OrderId", reCharge.getTradeOrderId());
							json.put("Sign", reCharge.getSign());
							intent.putExtra("upPay.Req", json.toString());
							intent.putExtra("Broadcast",
									GlobalConstants.BROADCAST_PAY_END); // 广播接收地址
							intent.putExtra("Environment", "01"); // 00: 测试环境,
																	// 01: 生产环境
							startActivity(intent);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						ToastUtil.shortToast(CenterReChargeActivity.this,
								reCharge.getResMsg());
					}

				}
			});
		};

		public void easyLinkReChargeFailure(String error) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
				}
			});
		};

		public void getReChargeListSuccess(final ReChargeChannel info) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					// 通过channelCode来判断充值的方式：银联：EASYLINK 支付宝： ALIPAY 联动优势： UMPAY
					if (info != null && info.getResCode().equals("0")) {
						mChannelContentList = info.getContents();
						mBankInfoList = info.getBankInfos();

						// List<ChannelContent> contents =
						// channel.getContents();
						// List<BankInfo> bankInfos = channel.getBankInfos();
						if (mBankInfoList == null || mBankInfoList.size() == 0) {
							if (mChannel != null
									&& mChannel.getBankInfos() != null) {
								mBankInfoList = mChannel.getBankInfos();
							} else {
								mBankInfoList = new ArrayList<BankInfo>();
							}

						}
						if (mBankInfoList.size() == 0) {
							layoutView_recharge_fast_text
									.setVisibility(View.GONE);
							// 隐藏 头部快捷充值布局
						} else {
							// 显示快捷充值布局
							layoutView_recharge_fast_text
									.setVisibility(View.VISIBLE);
						}

						if (mChannelContentList == null
								|| mChannelContentList.size() == 0) {
							if (mChannel != null) {
								mChannelContentList = mChannel.getContents();

							} else {
								mChannelContentList = new ArrayList<ChannelContent>();
							}
						}
						if (info.getContents().size() > 0) {
							if (SDConfig.getConfiguration().isSdCardExist()) {
								SDConfig.getConfiguration().setFileName(
										"recharge");
								SDConfig.getConfiguration().saveToSDCard(info);
							}
						}
					} else if (CenterReChargeActivity.this.mChannel != null) {
						mChannelContentList = mChannel.getContents();
						mBankInfoList = mChannel.getBankInfos();

					}
					if (mChannelContentList != null) {
						selected = mChannelContentList.get(mChangeIndex)
								.getChannelCode();
					}
					mBankInfoAdapter.notifyDataSetChanged();
					mChannelContentAdapter.notifyDataSetChanged();
					ViewUtils
							.setListViewHeightBasedOnChildren(id_recharge_bank_list);
					ViewUtils
							.setListViewHeightBasedOnChildren(id_recharte_channel_list);
				}
			});
		};

		public void getReChargeListFailure(String error) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if (mChannel != null) {
						mChannelContentList = mChannel.getContents();
						if (mChannelContentList != null
								&& mChannelContentList.size() > 0) {
							mBankInfoList = mChannel.getBankInfos();
							if (null == mBankInfoList) {
								mBankInfoList = new ArrayList<>();
							}
							if (mBankInfoList.size() == 0) {
								layoutView_recharge_fast_text.setVisibility(View.GONE);
							}else{
								layoutView_recharge_fast_text.setVisibility(View.VISIBLE);
								mChannelContentAdapter.notifyDataSetChanged();
							}
								
						}

						if (mBankInfoList != null && mBankInfoList.size() > 0) {
							mBankInfoAdapter.notifyDataSetChanged();
							layoutView_recharge_fast_text
									.setVisibility(View.VISIBLE);
							id_recharge_bank_list.setVisibility(View.VISIBLE);
						}

					}
					if (mChannelContentList != null) {
						selected = ALIPAY;
					}
					ViewUtils
							.setListViewHeightBasedOnChildren(id_recharge_bank_list);
					ViewUtils
							.setListViewHeightBasedOnChildren(id_recharte_channel_list);

				}
			});
		};

		public void reChargeSuccess(final ReCharge reCharge) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if(reCharge != null && reCharge.getResCode() != null){
						if (Integer.parseInt(reCharge.getResCode()) == 0) {
							if (selected.equals(ALIPAY)) {
								AlipayUtils.pay(CenterReChargeActivity.this, mHandler,
										reCharge.getOrderNo(), reCharge.getReturnUrl(),
										money);
							} else if (selected.equals(UMPAY)) {
								UmpPayInfoBean infoBean = new UmpPayInfoBean();
								// String signInfo = SignEncodeUtil.Sign2("merCustId=" +
								// "13500000001" + "&merId=" + GlobalConstants.UMPAYID,
								// CenterReChargeActivity.this);
								// String signInfo = SignEncodeUtil.Sign2("merCustId=" +
								// DeEnCoder.encode(userInfo.getUserId()) + "&merId=" +
								// GlobalConstants.UMPAYID,
								// CenterReChargeActivity.this);
								// UmpayQuickPay.requestSign(CenterReChargeActivity.this,
								// GlobalConstants.UMPAYID, "13500000001", "", "",
								// signInfo, infoBean, REQUESTCODE);
								// UmpayQuickPay.requestSign(CenterReChargeActivity.this,
								// GlobalConstants.UMPAYID,
								// DeEnCoder.encode(userInfo.getUserId()), "", "",
								// signInfo, infoBean, REQUESTCODE);
								UmpayQuickPay.requestPayWithBind(
										CenterReChargeActivity.this,
										reCharge.getTradeNo(),
										DeEnCoder.encode(App.userInfo.getUserId()), "",
										"", infoBean, REQUESTCODE);
							}
						} else {
							ToastUtil.shortToast(CenterReChargeActivity.this, reCharge.getResMsg());
						}
					}
				}
			});
		};

		public void reChargeFailure(String error) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
				}
			});
		};

		/** 微信充值-生成订单成功 */
		public void weChatRechargeSuccess(final String json) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if (json != null) {
						getJson(json);
					}

				}

			});
		};

		/** 微信充值-生成订单失败 */
		public void weChatRechargeError(String message) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
				}
			});
		};
		
		/**获取用户信息
		 * 更新App.userInfo
		 */
		public void getUserInfoSuccess(final UserInfo info) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (info.getResCode() != null
							&& info.getResCode().equals("0")) {
						if (App.userInfo != null
								&& App.userInfo.getUserId() != null) {
							info.setUserId(App.userInfo.getUserId());
						}
						if (App.userInfo != null
								&& App.userInfo.getPassword() != null) {
							info.setPassword(App.userInfo.getPassword());
						}
						App.userInfo = info;
						// 更新本地文件
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						finish();
					}

				}
			});
		
		};
		
		/**
		 * 获取用户信息失败
		 */
		public void getUserInfoFailure(String error) {
			
		}
	};
	// TODT
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
	/** 微信支付--附加数据 */
	private String attach;
	/** 支付请求 */
	private PayReq req;
	/** 微信Api */
	private IWXAPI wxApi;

	private void initWXApi() {
		if (wxApi == null) {
			wxApi = WXAPIFactory.createWXAPI(this, null);
			// 支付参数
			req = new PayReq();
		}
	}

	/**
	 * 解析后台数据
	 * 
	 * @param json
	 */
	private void getJson(String json) {
		try {
			App.CALLBACK_ACTIVITY = "WECHAT_RECHARAGE";
			JSONObject object = new JSONObject(json);
			Integer resCode = object.getInt("resCode");
			if (resCode != null && resCode == 0) {
				prepayId = object.getString("prepayId");
				sign = object.getString("sign");
				appId = object.getString("appId");
				// appId = "wx651eab3b05ba82b0";
				timestamp = object.getString("timestamp");
				nonceStr = object.getString("nonceStr");
				partnerId = object.getString("partnerId");
				attach = object.getString("attach");
				initWXApi();// 初始化API
				if (!wxApi.isWXAppInstalled()) {
					ToastUtil.shortToast(this, "未安装微信客户端,不能支付");
				} else {
					sendPayReq();
				}
			} else if (resCode != null && resCode == 3002) {
				Log.e("wushiqiu", "您的登陆超时，正在为您重新登陆");
				if (loginPattern == 1 && auto) {
					// 之前的登陆状态为微信
					Controller.getInstance().loginWithOpenid(
							GlobalConstants.URL_WECHAT_LOAD, openId,
							new CallBack() {
								/** 使用openid登陆微信成功 **/
								public void loginWithOpenidSuccess(String json) {
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
											UserLogic.getInstance()
													.saveUserInfo(App.userInfo);
											// 登陆完成 重新发送该请求
											Controller
													.getInstance()
													.weChatRecharge(
															GlobalConstants.WECHAT_RECHARGE,
															App.userInfo
																	.getUserId(),
															money, "1", "0",
															"", mBack);
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
				} else if (loginPattern == 2) {
					// 之前的登陆状态为自营
					Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
							App.userInfo.getUserName(),
							App.userInfo.getPassword(), new CallBack() {
								@Override
								public void loginSuccess(UserInfo info) {
									if (info != null
											&& info.getResCode().equals("0")) {
										String password = App.userInfo
												.getPassword();
										App.userInfo = info;
										App.userInfo.setPassword(password);
										// 将用户信息保存到配置文件中
										UserLogic.getInstance().saveUserInfo(
												App.userInfo);
										Controller
												.getInstance()
												.weChatRecharge(
														GlobalConstants.WECHAT_RECHARGE,
														App.userInfo
																.getUserId(),
														money, "1", "0", "",
														mBack);
									}
								}

								@Override
								public void loginFailure(String error) {
								}
							});
				}else if(!auto){
					MainActivity mainActivity = (MainActivity) ActivityManager.isExistsActivity(MainActivity.class);
					if (mainActivity != null) {
						// 如果存在MainActivity实例，那么展示LoboHallFrament页面
						Intent intent = new Intent(mainActivity, LoginActivity.class);
						intent.putExtra("callthepage", CallThePage.LOBOHALL);
						mainActivity.startActivity(intent);
					} else {
						LoginActivity.forwartLoginActivity(App.getInstance(), CallThePage.LOBOHALL);
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 组装支付参数,调用支付接口
	 * 
	 * @param result
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
	public void onPause() {
		super.onPause();
		hideLoadingDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			GlobalTools.hideSoftInput(CenterReChargeActivity.this);
			finish();
			break;
		case R.id.imageView_recharge_add:
			money = editText_recharge_money.getText().toString();
			int moneyInt = 0;
			if (TextUtils.isEmpty(money) || Integer.parseInt(money) < 10) {
				moneyInt = 9;
			} else {
				moneyInt = Integer.parseInt(money);
			}
			editText_recharge_money.setText((++moneyInt) + "");
			break;
		case R.id.imageView_recharge_reduce:
			money = editText_recharge_money.getText().toString();
			if (!TextUtils.isEmpty(money) && Double.parseDouble(money) > 10) {
				editText_recharge_money.setText((Integer.parseInt(money) - 1)
						+ "");
			}
			break;
		case R.id.textView_recharge_submit:
			money = editText_recharge_money.getText().toString();
			if (checkNetworkState() && selected != null) {
				if (TextUtils.isEmpty(money)) {
					ToastUtil.shortToast(mActivity, "充值金额不能为空");
				} else if (Double.parseDouble(money) < 10) {
					ToastUtil.shortToast(CenterReChargeActivity.this,
							getResources().getString(R.string.recharge_error));
				} else if (selected.equals(ALIPAY)) {
//					if (isTest)
//						money = "0.01";
					// 支付宝
					Controller.getInstance().reCharge(
							GlobalConstants.NUM_RECHARGE,
							App.userInfo.getUserId(), money, ALIPAY, mBack);
				} else if (selected.equals(EASYLINK)) {
					hideLoadingDialog();
//					if (isTest)
//						money = "1";
					// 银联
					if (Integer.parseInt(App.userInfo.getIsCompUserName()) == 1) {
						Intent intent = new Intent(CenterReChargeActivity.this,
								EasyLinkBankActivity.class);
						intent.putExtra("money", money);
						startActivity(intent);
					} else {
						startActivity(UserManageActivity.class);
					}

				} else if (selected.equals(UMPAY)) {
//					if (isTest)
//						money = "0.01";
					// 联动优势
					Controller.getInstance().reCharge(
							GlobalConstants.NUM_RECHARGE,
							App.userInfo.getUserId(), money, UMPAY, mBack);
				} else if (selected.equals(WECHAT)) {
					// 测试打开 支付0.01元
//					if (isTest)
//						money = "0.01";
//					money="0.01";
					// 微信充值
					Controller.getInstance().weChatRecharge(
							GlobalConstants.WECHAT_RECHARGE,
							App.userInfo.getUserId(), money, "1", "0", "",
							mBack);
					showLoadingDialog();
					// Controller.getInstance().weChatRecharge(
					// GlobalConstants.WECHAT_RECHARGE,
					// App.userInfo.getUserId(), "0.01", "1", "0", "",
					// mBack);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 3.在这里接收并处理支付结果
	 * 
	 * @param requestCode
	 *            支付请求码
	 * @param resultCode
	 *            SDK固定返回88888
	 * @param data
	 *            支付结果和结果描述信息
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CenterReChargeActivity.REQUESTCODE) {
			ToastUtil.shortToast(CenterReChargeActivity.this,
					data.getStringExtra("umpResultMessage"));
			finish();
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GlobalConstants.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(CenterReChargeActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(CenterReChargeActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(CenterReChargeActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			// case SDK_CHECK_FLAG: {
			// Toast.makeText(CenterReChargeActivity.this, "检查结果为：" + msg.obj,
			// Toast.LENGTH_SHORT).show();
			// break;
			// }
			default:
				break;
			}
		};
	};

	/**
	 * 判断网络连接状况
	 * 
	 * @return
	 */
	private boolean checkNetworkState() {
		boolean flag = false;
		// 得到网络连接信息
		manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {
			// setNetwork();
			ToastUtil.shortToastCenter(CenterReChargeActivity.this, "您没有连接网络");
		} else {
			// isNetworkAvailable();
			// ToastUtil.shortToastCenter(CenterReChargeActivity.this,
			// "您已经连接到网络");
		}

		return flag;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 支付成功的广播
	 * 
	 * @param view
	 */
	class PayReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String state = intent.getStringExtra("state");
			if (state.equals("1")) {
				ToastUtil.shortToast(context, "充值失败");
			} else {
				//充值成功  获取用户信息，更新App.userinfo
				Controller.getInstance().getUserInfo(
						GlobalConstants.NUM_GETUSERINFO, App.userInfo.getUserId(),
						mBack);
//				startActivity(AccountDetailActivity.class);
//				finish();
			}
		}
	}
}
