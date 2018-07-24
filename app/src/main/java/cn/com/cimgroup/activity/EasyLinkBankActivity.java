package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.AbstractSpinerAdapter;
import cn.com.cimgroup.bean.BankInfo;
import cn.com.cimgroup.bean.BankList;
import cn.com.cimgroup.bean.City;
import cn.com.cimgroup.bean.EasyLinkReCharge;
import cn.com.cimgroup.bean.ProCity;
import cn.com.cimgroup.bean.Province;
import cn.com.cimgroup.config.SDConfig;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.popwindow.SpinerPopWindow;
import cn.com.cimgroup.util.thirdSDK.EasyLinkUtils;
import cn.com.cimgroup.xutils.ToastUtil;

import com.payeco.android.plugin.PayecoPluginLoadingActivity;

public class EasyLinkBankActivity extends BaseActivity implements
		OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener,
		OnFocusChangeListener {

	private String mDefaultPhone = "";

	// 选择的银行名字
	private TextView mBankName;

	// 选择银行的银行编号
	private String mBankCode = "";

	// 选择银行的适配器
	private BankAdapter mAdapterBank = null;

	// 选择的省名字
	private TextView mProvinceName;

	// 选择的市名字
	private TextView mCityName;

	// 选择省的适配器
	private ProvinceAdapter mAdapterProvince = null;
	/** 省名称 */
	private TextView textView_easylink_provinceName;

	// 现在市的适配器
	private CityAdapter mAdapterCity = null;

	private CardAdapter mAdapterCard = null;

	private BankList mBanks;

	private List<Province> mProvinces;

	private List<City> citys;

	private RelativeLayout provinceLayout;

	private RelativeLayout cityLayout;

	private RelativeLayout bankLayout;

	private String bankTimeId = "0";

	private String proCityTimeId = "0";

	private SpinerPopWindow mBankSpinerPopWindow;

	private SpinerPopWindow mProSpinerPopWindow;

	private SpinerPopWindow mCitySpinerPopWindow;

	private SpinerPopWindow mCardSpinerPopWindow;

	// 标识是不是点击的是银行卡
	private boolean isClickBank = false;
	/** 银行卡 卡号 */
	private EditText editView_easylink_cardnum;
	/** 银行卡卡号文本 */
	private String mBankNumTxt;

	private boolean mBankRes;

	private EditText mPhone;

	private String mPhoneText;

	// 选中的市code
	private String mCityCode;

	// 选中的省code
	private String mProvinceCode;

	// 验证手机号
	private boolean mPhoneRes;

	private EditText textView_easylink_money;

	private LinearLayout jieji;

	private LinearLayout xinyong;

	private String cardType = "";

	private List<String> cardList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_easylink_bank);
		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("procity");
			Object object = SDConfig.getConfiguration().readSDCard();
			if (object != null) {
				ProCity proCity = (ProCity) object;
				if (proCity != null) {
					mProvinces = proCity.getProvinces();
					proCityTimeId = proCity.getTimeId();
				}
			}
		}

		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("banks");
			Object object = SDConfig.getConfiguration().readSDCard();
			if (object != null) {
				BankList bankList = (BankList) object;
				if (bankList != null) {
					mBanks = bankList;
					bankTimeId = bankList.getTimeId();
				}
			}

		}

		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("cards");
			Object object = SDConfig.getConfiguration().readSDCard();
			if (object != null) {
				cardList = (List<String>) object;
			}
			mCardSpinerPopWindow = new SpinerPopWindow(this);
			mAdapterCard = new CardAdapter(this);
			mCardSpinerPopWindow.setAdatper(mAdapterCard);
			mCardSpinerPopWindow
					.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
						@Override
						public void onItemClick(View view, int pos) {
							String str = (String) mAdapterCard.getItem(pos);
							if (!str.equals(getString(R.string.register_write_card))) {
								editView_easylink_cardnum.setText(str);
							} else {
								mCardSpinerPopWindow.dismiss();
							}
						}
					});
			if (cardList != null && cardList.size() > 0) {
			} else {
				cardList = new ArrayList<String>();
				cardList.add(getString(R.string.register_write_card));
			}
			mAdapterCard.refreshData(cardList, 0);
		}

		Controller.getInstance().getProCity(GlobalConstants.NUM_PROCITY,
				proCityTimeId, mBack);
		Controller.getInstance().getBankList(GlobalConstants.NUM_BANKLIST,
				bankTimeId, mBack);

		// 初始化支付结果广播接收器
		EasyLinkUtils.initPayecoPayBroadcastReceiver(EasyLinkBankActivity.this);

		// 注册支付结果广播接收器
		EasyLinkUtils
				.registerPayecoPayBroadcastReceiver(EasyLinkBankActivity.this);
		initView();
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
					textView_easylink_money.setText("1000000");
				} else if (iMoney < 1 && money.length() > 1) {
					textView_easylink_money.setText("0");
				}
			}

		}
	};

	@Override
	protected void onDestroy() {
		EasyLinkUtils
				.unRegisterPayecoPayBroadcastReceiver(EasyLinkBankActivity.this);
		Controller.getInstance().removeCallback(mBack);
		super.onDestroy();
	}

	private void initView() {
		Intent intent = getIntent();
		String cell = intent.getStringExtra("money");

		mDefaultPhone = getResources().getString(R.string.register_write_phone);

		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.textView_title_word);
		title.setText(getResources().getString(R.string.easylink_recharge));

		TextView realName = (TextView) findViewById(R.id.textView_easylink_realname);
		realName.setText(App.userInfo == null ? "" : App.userInfo.getRealName());

		textView_easylink_money = (EditText) findViewById(R.id.textView_easylink_money);
		textView_easylink_money.addTextChangedListener(moneyWatcher);
		textView_easylink_money.setText(cell);

		((TextView) findViewById(R.id.textView_easylink_submit))
				.setOnClickListener(this);

		textView_easylink_provinceName = (TextView) findViewById(R.id.textView_easylink_provinceName);

		mBankName = (TextView) findViewById(R.id.textView_easylink_bankName);

		mProvinceName = (TextView) findViewById(R.id.textView_easylink_provinceName);

		mCityName = (TextView) findViewById(R.id.textView_easylink_cityName);

		((ImageView) findViewById(R.id.textView_easylink_bank))
				.setOnClickListener(this);

		((ImageView) findViewById(R.id.textView_easylink_province))
				.setOnClickListener(this);

		((ImageView) findViewById(R.id.textView_easylink_city))
				.setOnClickListener(this);
		((ImageView) findViewById(R.id.textView_easylink_card))
				.setOnClickListener(this);

		bankLayout = (RelativeLayout) findViewById(R.id.laytout_easylink_bank);

		provinceLayout = (RelativeLayout) findViewById(R.id.laytout_easylink_province);

		cityLayout = (RelativeLayout) findViewById(R.id.laytout_easylink_city);
		cityLayout.setEnabled(false);

		jieji = (LinearLayout) findViewById(R.id.layout_easylink_jieji);

		xinyong = (LinearLayout) findViewById(R.id.layout_easylink_xinyong);

		editView_easylink_cardnum = (EditText) findViewById(R.id.editView_easylink_cardnum);

		mPhone = (EditText) findViewById(R.id.editText_easylink_phone);

		TextView explain = (TextView) findViewById(R.id.textView_easylink_explain);
		explain.setText(Html.fromHtml(getResources().getString(
				R.string.recharge_reminde)));
		((TextView) findViewById(R.id.textView_recharge_explain1))
				.setText(Html.fromHtml(getResources().getString(
						R.string.recharge_reminde1)));

		jieji.setSelected(false);
		xinyong.setSelected(true);
		cardType = "2";

		textView_easylink_money.setOnFocusChangeListener(this);
		editView_easylink_cardnum.setOnFocusChangeListener(this);
		mPhone.setOnFocusChangeListener(this);

		jieji.setOnClickListener(this);
		xinyong.setOnClickListener(this);
		bankLayout.setOnClickListener(this);
		provinceLayout.setOnClickListener(this);
		cityLayout.setOnClickListener(this);

	}

	CallBack mBack = new CallBack() {
		public void easyLinkReChargeSuccess(final EasyLinkReCharge reCharge) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if (Integer.parseInt(reCharge.getResCode()) == 0) {
						Intent intent = new Intent(EasyLinkBankActivity.this,
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
							cardList.add(mBankNumTxt);
							if (SDConfig.getConfiguration().isSdCardExist()) {
								SDConfig.getConfiguration()
										.setFileName("cards");
								SDConfig.getConfiguration().saveToSDCard(
										cardList);
								mAdapterCard.refreshData(cardList, 0);
							}
							startActivity(intent);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						ToastUtil.shortToast(EasyLinkBankActivity.this,
								reCharge.getResMsg());
					}

				}
			});
		}

		;

		public void easyLinkReChargeFailure(String error) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
				}
			});
		};

		public void getProCitySuccess(final ProCity proCity) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					mAdapterProvince = new ProvinceAdapter(
							EasyLinkBankActivity.this);
					mProSpinerPopWindow = new SpinerPopWindow(
							EasyLinkBankActivity.this);
					mProSpinerPopWindow.setAdatper(mAdapterProvince);
					mProSpinerPopWindow
							.setItemListener(EasyLinkBankActivity.this);
					if (proCity != null && proCity.getResCode().equals("0")) {
						mProvinces = proCity.getProvinces();
						if (mProvinces.size() > 0) {
							mAdapterProvince.refreshData(mProvinces, 0);

							if (SDConfig.getConfiguration().isSdCardExist()) {
								SDConfig.getConfiguration().setFileName(
										"procity");
								SDConfig.getConfiguration().saveToSDCard(
										proCity);
							}
						} else {
							mAdapterProvince.refreshData(mProvinces, 0);
						}
					} else {
						if (mProvinces != null) {
							mAdapterProvince.refreshData(mProvinces, 0);
						}
					}
				}
			});
		};

		public void getProCityFailure(String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					mAdapterProvince = new ProvinceAdapter(
							EasyLinkBankActivity.this);
					mProSpinerPopWindow = new SpinerPopWindow(
							EasyLinkBankActivity.this);
					mProSpinerPopWindow.setAdatper(mAdapterProvince);
					mProSpinerPopWindow
							.setItemListener(EasyLinkBankActivity.this);
					if (mProvinces != null) {
						mAdapterProvince.refreshData(mProvinces, 0);
					}
				}
			});
		};

		public void getBankListSuccess(final BankList bankList) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					mAdapterBank = new BankAdapter(EasyLinkBankActivity.this);
					mBankSpinerPopWindow = new SpinerPopWindow(
							EasyLinkBankActivity.this);
					mBankSpinerPopWindow.setAdatper(mAdapterBank);
					mBankSpinerPopWindow
							.setItemListener(EasyLinkBankActivity.this);
					if (bankList.getResCode().equals("0") && bankList != null
							&& bankList.getList() != null) {
						mBanks = bankList;

						if (mBanks.getList().size() > 0) {
							mAdapterBank.refreshData(bankList.getList(), 0);

							if (SDConfig.getConfiguration().isSdCardExist()) {
								SDConfig.getConfiguration()
										.setFileName("banks");
								SDConfig.getConfiguration().saveToSDCard(
										bankList);
							}
						} else {
							mAdapterBank.refreshData(mBanks.getList(),0);
						}
					} else {
						if (mBanks.getList() != null) {
							mAdapterBank.refreshData(mBanks.getList(),0);
						}
					}
				}
			});
		}

		;

		public void getBankListFailure(String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					mAdapterBank = new BankAdapter(EasyLinkBankActivity.this);
					mBankSpinerPopWindow = new SpinerPopWindow(
							EasyLinkBankActivity.this);
					mBankSpinerPopWindow.setAdatper(mAdapterBank);
					mBankSpinerPopWindow
							.setItemListener(EasyLinkBankActivity.this);
					if (mBanks != null) {
						if (mBanks.getList() != null) {
							mAdapterBank.refreshData(mBanks.getList(), 0);
						}
					}
				}
			});
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			GlobalTools.hideSoftInput(EasyLinkBankActivity.this);
			finish();
			break;
		case R.id.textView_easylink_submit:
			if (App.userInfo != null) {
				mBankNumTxt = editView_easylink_cardnum.getText().toString();
				String numRegex = "(\\d{16})|(\\d{19})";
				mBankRes = mBankNumTxt.matches(numRegex);

				mPhoneText = mPhone.getText().toString();
				String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
				mPhoneRes = mPhoneText.matches(telRegex);

				String moneys = textView_easylink_money.getText().toString()
						.trim();
				if (TextUtils.isEmpty(moneys)) {
					ToastUtil.shortToast(mActivity, "充值金额不能为空");
				} else if (Integer.parseInt(moneys) < 10) {
					ToastUtil.shortToast(mActivity, "充值金额不能小于10元");
				} else if (TextUtils.isEmpty(mPhoneText)) {
					ToastUtil.shortToast(mActivity, "请输入手机号码");
				} else if (!mPhoneRes) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.register_error0));
				} else if (TextUtils.isEmpty(mBankName.getText().toString())) {
					ToastUtil.longToast(this,
							getResources().getString(R.string.card_errorinfo0));
				} else if (TextUtils
						.isEmpty(mProvinceName.getText().toString())) {
					ToastUtil.longToast(this,
							getResources().getString(R.string.card_errorinfo1));
				} else if (TextUtils.isEmpty(mCityName.getText().toString())) {
					ToastUtil.longToast(this,
							getResources().getString(R.string.card_errorinfo2));
				} else if (TextUtils.isEmpty(mBankNumTxt)) {
					ToastUtil.shortToast(this, "请输入银行卡号");
				} else if (!mBankRes) {
					ToastUtil
							.shortToast(
									EasyLinkBankActivity.this,
									getResources().getString(
											R.string.addcard_numerror));
				} else if (cardType.equals("")) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.easylink_error));

				} else if (TextUtils.isEmpty(moneys)
						|| Double.parseDouble(moneys) < 10) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.recharge_error));

				} else if (!TextUtils.isEmpty(cardType) && mPhoneRes
						&& mBankRes) {
					showLoadingDialog();
					Controller.getInstance().easyLinkReCharge(
							GlobalConstants.NUM_EASYLINK,
							App.userInfo.getUserId(), mBankNumTxt,
							textView_easylink_money.getText().toString(),
							mPhoneText, mProvinceName.getText().toString(),
							mCityName.getText().toString(),
							App.userInfo.getRealName(),
							App.userInfo.getIdCard(), cardType,
							mBankName.getText().toString(), mCityCode,
							mProvinceCode, mBack);
				}
			} else {
				startActivity(LoginActivity.class);
			}
			// String cardNum = "6217730704053824";
			// String amount = "0.6";
			// String cell = "15101502334";
			// String province = "北京市";
			// String city = "直辖区";
			// String realName = "张建峰";
			// String idCard = "130531198802203211";
			// cardType = "1";
			// String bankName = "中信银行";

			// 访问测试服务器的测试数据
			// Controller.getInstance().easyLinkReCharge(GlobalConstants.NUM_EASYLINK,
			// userInfo.getUserId(), cardNum, amount, cell, province, city,
			// realName, idCard, cardType, bankName, "110100", "110000", mBack);

			break;
		case R.id.textView_easylink_province:
		case R.id.laytout_easylink_province:
			showProSpinWindow();
			break;
		case R.id.textView_easylink_bank:
		case R.id.laytout_easylink_bank:
			showBankSpinWindow();
			break;
		case R.id.laytout_easylink_city:
			if (!textView_easylink_provinceName.getText().toString().trim()
					.equals("选择省")) {
				showCitySpinWindow();
			}

			break;
		case R.id.layout_easylink_jieji:
			jieji.setSelected(true);
			xinyong.setSelected(false);
			cardType = "1";
			break;
		case R.id.layout_easylink_xinyong:
			jieji.setSelected(false);
			xinyong.setSelected(true);
			cardType = "2";
			break;
		case R.id.textView_easylink_card:
			showBankCardWindow();
			break;

		default:
			break;
		}
	}

	/**
	 * 展开省列表
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月13日
	 */
	private void showProSpinWindow() {
		cityLayout.setEnabled(false);
		isClickBank = false;
		mProSpinerPopWindow.setWidth(provinceLayout.getWidth());
		mProSpinerPopWindow.showAsDropDown(provinceLayout);
	}

	/**
	 * 展开市列表
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月13日
	 */
	private void showCitySpinWindow() {
		isClickBank = false;
		mCitySpinerPopWindow.setWidth(cityLayout.getWidth());
		mCitySpinerPopWindow.showAsDropDown(cityLayout);
	}

	/**
	 * 展开银行列表
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月13日
	 */
	private void showBankSpinWindow() {
		isClickBank = true;
		mBankSpinerPopWindow.setWidth(bankLayout.getWidth());
		mBankSpinerPopWindow.showAsDropDown(bankLayout);
	}

	/**
	 * 展开银行卡列表;
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月13日
	 */
	private void showBankCardWindow() {
		isClickBank = false;
		mCardSpinerPopWindow.setWidth(bankLayout.getWidth());
		mCardSpinerPopWindow.showAsDropDown(editView_easylink_cardnum);
	}

	/**
	 * 银行adapter
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月13日
	 */
	private class BankAdapter extends AbstractSpinerAdapter<BankInfo> {

		private Context cxt;

		public BankAdapter(Context context) {
			super(context);
			cxt = context;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (convertView == null) {
				convertView = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.item_card_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			BankInfo item = (BankInfo) getItem(pos);
			viewHolder.mTextView.setText(item.getBankname());

			return convertView;
		}

	}

	/**
	 * 省adapter
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月13日
	 */
	private class ProvinceAdapter extends AbstractSpinerAdapter<Province> {

		private Context cxt;

		public ProvinceAdapter(Context context) {

			super(context);
			cxt = context;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (convertView == null) {
				convertView = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.item_card_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Province item = (Province) getItem(pos);
			viewHolder.mTextView.setText(item.getProvince());

			return convertView;
		}

	}

	/**
	 * 市adapter
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月13日
	 */
	private class CityAdapter extends AbstractSpinerAdapter<City> {

		private Context cxt;

		public CityAdapter(Context context) {

			super(context);
			cxt = context;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (convertView == null) {
				convertView = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.item_card_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			City item = (City) getItem(pos);
			viewHolder.mTextView.setText(item.getCity());

			return convertView;
		}

	}

	private class CardAdapter extends AbstractSpinerAdapter<String> {

		private Context cxt;

		public CardAdapter(Context context) {

			super(context);
			cxt = context;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (convertView == null) {
				convertView = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.item_card_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			String item = (String) getItem(pos);
			viewHolder.mTextView.setText(item);

			return convertView;
		}

	}

	@Override
	public void onItemClick(View view, int pos) {
		if (isClickBank) {
			if (pos >= 0 && pos <= mBanks.getList().size()) {
				mBankName.setText(mBanks.getList().get(pos).getBankname());
				mBankCode = mBanks.getList().get(pos).getBankcode();
			}
		} else {
			if (cityLayout.isEnabled()) {
				if (pos >= 0 && pos <= citys.size()) {
					mCityName.setText(citys.get(pos).getCity());
					mCityCode = citys.get(pos).getCityCode();
				}
			} else {
				if (pos >= 0 && pos <= mProvinces.size()) {
					mProvinceName.setText(mProvinces.get(pos).getProvince());
					mProvinceCode = mProvinces.get(pos).getProCode();
					mAdapterCity = new CityAdapter(EasyLinkBankActivity.this);
					citys = mProvinces.get(pos).getCitys();
					mAdapterCity.refreshData(citys, 0);
					cityLayout.setEnabled(true);
					mCitySpinerPopWindow = new SpinerPopWindow(
							EasyLinkBankActivity.this);
					mCitySpinerPopWindow.setAdatper(mAdapterCity);
					mCitySpinerPopWindow
							.setItemListener(EasyLinkBankActivity.this);
				}
			}
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		boolean ssss = hasFocus;
		switch (v.getId()) {
		// case R.id.textView_easylink_money:
		// if (hasFocus) {
		// money.setHint("");
		// GlobalTools.openSoftInput(money, EasyLinkBankActivity.this);
		// } else {
		// if (Double.parseDouble(money.getText().toString()) < 10) {
		// ToastUtil.shortToast(EasyLinkBankActivity.this,
		// getResources().getString(R.string.recharge_error));
		// }
		// }
		// break;
		case R.id.editView_easylink_cardnum:
			if (hasFocus) {
				editView_easylink_cardnum.setHint("");
				GlobalTools.openSoftInput(editView_easylink_cardnum,
						EasyLinkBankActivity.this);
			} else {
				mBankNumTxt = editView_easylink_cardnum.getText().toString();
				String numRegex = "(\\d{16})|(\\d{19})";
				mBankRes = mBankNumTxt.matches(numRegex);
				if (!mBankRes) {
					ToastUtil
							.shortToast(
									EasyLinkBankActivity.this,
									getResources().getString(
											R.string.addcard_numerror));
				}
			}
			break;
		case R.id.editText_easylink_phone:
			if (hasFocus) {
				mPhone.setHint("");
				GlobalTools.openSoftInput(mPhone, EasyLinkBankActivity.this);
			} else {
				mPhoneText = mPhone.getText().toString();
				if (mPhoneText.length() == 0) {
					mPhone.setHint(mDefaultPhone);
				} else {
					String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
					mPhoneRes = mPhoneText.matches(telRegex);
					if (!mPhoneRes) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.register_error0));
					}
				}
			}
			break;
		default:
			break;
		}
	}
}
