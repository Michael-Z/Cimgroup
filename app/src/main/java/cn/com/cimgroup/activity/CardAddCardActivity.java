package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.AbstractSpinerAdapter;
import cn.com.cimgroup.bean.BankInfo;
import cn.com.cimgroup.bean.BankType;
import cn.com.cimgroup.bean.City;
import cn.com.cimgroup.bean.ProCity;
import cn.com.cimgroup.bean.Province;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.SDConfig;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.SpinerPopWindow;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 绑定银行卡
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
@SuppressLint("HandlerLeak")
public class CardAddCardActivity extends BaseActivity implements
		OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener,
		OnFocusChangeListener {

	/** 选择省市信息显示layout */
	private LinearLayout id_addbank_changecity_layout;
	/** 所绑定的银行卡号号 */
	private LinearLayout id_addcard_bank_layout;
	/** 银行卡卡行 */
	private TextView mBankName;

	// 选择的银行名字
	// private TextView mBankName;

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

	// 银行卡下啦适配器;
	private CardAdapter mAdapterCard = null;

	// 现在市的适配器
	private CityAdapter mAdapterCity = null;

	// private BankList banks;

	private List<Province> provinces;

	private List<City> citys;

	private RelativeLayout provinceLayout;

	private RelativeLayout cityLayout;

	private RelativeLayout bankLayout;

	// 银行号
	private EditText mBankNum;

	// 真实姓名
	private EditText mName;

	/** 提交、绑定银行卡 */
	private TextView textView_addcard_save;

	private String mBankNumTxt;

	private String mNameTxt;

	private boolean mBankRes;

	private UserInfo mUserInfo;

	private String bankTimeId = "0";

	private String proCityTimeId = "0";

	private int type;

	// private SpinerPopWindow mBankSpinerPopWindow;

	private SpinerPopWindow mProSpinerPopWindow;

	private SpinerPopWindow mCitySpinerPopWindow;

	private SpinerPopWindow mCardSpinerPopWindow;
	/**温馨提示*/
	private TextView textView_addcard_explain;

	// 标识是不是点击的是银行卡
	// private boolean isClickBank = false;

	private List<String> cardList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_addcard);
		Intent intent = getIntent();
		type = Integer.parseInt(intent.getStringExtra("type"));

		mUserInfo = UserLogic.getInstance().getDefaultUserInfo();
		initView();

		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("procity");
			ProCity proCity = (ProCity) SDConfig.getConfiguration()
					.readSDCard();
			if (proCity != null) {
				provinces = proCity.getProvinces();
				proCityTimeId = proCity.getTimeId();
			}
		}
		//
		// if (SDConfig.getConfiguration().isSdCardExist()) {
		// SDConfig.getConfiguration().setFileName("banks");
		// BankList bankList = (BankList) SDConfig.getConfiguration()
		// .readSDCard();
		// if (bankList != null) {
		// banks = bankList;
		// bankTimeId = bankList.getTimeId();
		// }
		// }

		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("cards");
			cardList = (List<String>) SDConfig.getConfiguration().readSDCard();
			mCardSpinerPopWindow = new SpinerPopWindow(this);
			mAdapterCard = new CardAdapter(this);
			mCardSpinerPopWindow.setAdatper(mAdapterCard);

			mCardSpinerPopWindow
					.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
						@Override
						public void onItemClick(View view, int pos) {
							String str = (String) mAdapterCard.getItem(pos);
							if (!str.equals(getString(R.string.register_write_card))) {
								mBankNum.setText(str);
							} else {
								mCardSpinerPopWindow.dismiss();
							}

						}
					});

			if (cardList != null && cardList.size() > 0) {
			} else {
				((TextView) findViewById(R.id.textview_addcard))
						.setVisibility(View.GONE);
				cardList = new ArrayList<String>();
				cardList.add("请输入卡号");
			}
			mAdapterCard.refreshData(cardList, 0);
		}

		mProSpinerPopWindow = new SpinerPopWindow(CardAddCardActivity.this);
		mCitySpinerPopWindow = new SpinerPopWindow(CardAddCardActivity.this);

		Controller.getInstance().getProCity(GlobalConstants.NUM_PROCITY,
				proCityTimeId, mBack);
		// Controller.getInstance().getBankList(GlobalConstants.NUM_BANKLIST,
		// bankTimeId, mBack);

	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_addbank_changecity_layout = (LinearLayout) findViewById(R.id.id_addbank_changecity_layout);
		id_addcard_bank_layout = (LinearLayout) findViewById(R.id.id_addcard_bank_layout);
		id_addbank_changecity_layout.setVisibility(View.GONE);
		id_addcard_bank_layout.setVisibility(View.GONE);
		
		
		textView_addcard_explain=(TextView) findViewById(R.id.textView_addcard_explain);
		textView_addcard_explain.setText(Html.fromHtml(getResources().getString(
				R.string.usermanage_explain)+"<br/>"+"3、客服电话：<font color=#f66248> <a href=tel:010-65617701>010-65617701</a></font>"));
		textView_addcard_explain.setMovementMethod(LinkMovementMethod.getInstance());
		// id_addcard_bankname=(TextView)
		// findViewById(R.id.id_addcard_bankname);
		// title 左侧的文字
		TextView mTitleName = (TextView) findViewById(R.id.textView_title_word);
		textView_addcard_save = (TextView) findViewById(R.id.textView_addcard_save);
		// type为页面显示类型 1-绑定银行卡 2-修改银行卡
		if (type == 1) {
			mTitleName.setText(getResources().getString(
					R.string.usermanage_bindbank));
			textView_addcard_save.setText(getResources().getString(
					R.string.usermanage_commitbank));
		} else {
			mTitleName.setText(getResources().getString(
					R.string.usermanage_resetbank));
			textView_addcard_save.setText(getResources().getString(
					R.string.usermanage_resetbank));
		}

		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);

		mBankName = (TextView) findViewById(R.id.id_addcard_bankname);

		mProvinceName = (TextView) findViewById(R.id.textView_addcard_provinceName);

		mCityName = (TextView) findViewById(R.id.textView_addcard_cityName);

		mBankNum = (EditText) findViewById(R.id.editView_addcard_card);

		mName = (EditText) findViewById(R.id.editView_addcard_name);
		mName.setText(mUserInfo.getRealName());
		mName.setEnabled(false);

		((TextView) findViewById(R.id.textView_addcard_bank))
				.setOnClickListener(this);

		((TextView) findViewById(R.id.textView_addcard_province))
				.setOnClickListener(this);

		((TextView) findViewById(R.id.textView_addcard_city))
				.setOnClickListener(this);
		((TextView) findViewById(R.id.textview_addcard))
				.setOnClickListener(this);

		bankLayout = (RelativeLayout) findViewById(R.id.laytout_addcard_bank);

		provinceLayout = (RelativeLayout) findViewById(R.id.laytout_addcard_province);

		cityLayout = (RelativeLayout) findViewById(R.id.laytout_addcard_city);
		cityLayout.setEnabled(false);

		textView_addcard_save.setOnClickListener(this);
		bankLayout.setOnClickListener(this);
		provinceLayout.setOnClickListener(this);
		cityLayout.setOnClickListener(this);
		mBankNum.setOnFocusChangeListener(this);
	}

	private CallBack mBack = new CallBack() {

		public void bindBankSuccess(final UserInfo userInfo) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(
							CardAddCardActivity.this,
							getResources().getString(
									R.string.usermanage_success));
					String bankName = mBankName.getText().toString();
					CardAddCardActivity.this.mUserInfo.setBankName(bankName);
					String provinceName = mProvinceName.getText().toString()
							.trim();

					CardAddCardActivity.this.mUserInfo
							.setProvince(provinceName);
					CardAddCardActivity.this.mUserInfo
							.setBankCardId(mBankNumTxt);
					CardAddCardActivity.this.mUserInfo.setCity(mCityName
							.getText().toString());
					CardAddCardActivity.this.mUserInfo.setIsBindedBank("1");
					UserLogic.getInstance().saveUserInfo(mUserInfo);

					cardList.add(mBankNumTxt);
					if (SDConfig.getConfiguration().isSdCardExist()) {
						SDConfig.getConfiguration().setFileName("cards");
						SDConfig.getConfiguration().saveToSDCard(cardList);
						((TextView) findViewById(R.id.textview_addcard))
								.setVisibility(View.VISIBLE);
						mAdapterCard.refreshData(cardList, 0);
					}
					textView_addcard_save.setEnabled(true);
					finish();
				}
			});
		};

		public void bindBankFailure(final String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(CardAddCardActivity.this, error);
				}
			});
		};

		public void getProCitySuccess(final ProCity proCity) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					mAdapterProvince = new ProvinceAdapter(
							CardAddCardActivity.this);

					mProSpinerPopWindow.setAdatper(mAdapterProvince);
					mProSpinerPopWindow
							.setItemListener(CardAddCardActivity.this);
					if (Integer.parseInt(proCity.getResCode()) == 0) {
						provinces = proCity.getProvinces();
						if (provinces.size() > 0) {
							mAdapterProvince.refreshData(provinces, 0);

							if (SDConfig.getConfiguration().isSdCardExist()) {
								SDConfig.getConfiguration().setFileName(
										"procity");
								SDConfig.getConfiguration().saveToSDCard(
										proCity);
							}
						} else {
							mAdapterProvince.refreshData(
									CardAddCardActivity.this.provinces, 0);
						}
					} else {
						mAdapterProvince.refreshData(
								CardAddCardActivity.this.provinces, 0);
					}
				}
			});
		};

		public void getProCityFailure(String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					mAdapterProvince = new ProvinceAdapter(
							CardAddCardActivity.this);
					mProSpinerPopWindow = new SpinerPopWindow(
							CardAddCardActivity.this);
					mProSpinerPopWindow.setAdatper(mAdapterProvince);
					mProSpinerPopWindow
							.setItemListener(CardAddCardActivity.this);
					mAdapterProvince.refreshData(
							CardAddCardActivity.this.provinces, 0);
				}
			});
		};

		// public void getBankListSuccess(final BankList bankList) {
		// runOnUiThread(new Runnable() {
		//
		// public void run() {
		// hideLoadingDialog();
		// mAdapterBank = new BankAdapter(CardAddCardActivity.this);
		// mBankSpinerPopWindow = new SpinerPopWindow(
		// CardAddCardActivity.this);
		// mBankSpinerPopWindow.setAdatper(mAdapterBank);
		// mBankSpinerPopWindow
		// .setItemListener(CardAddCardActivity.this);
		// if (bankList != null && Integer.parseInt(bankList.getResCode()) == 0)
		// {
		// banks = bankList;
		//
		// if (bankList.getInfos()!=null&&bankList.getInfos().size() > 0) {
		// mAdapterBank.refreshData(bankList.getInfos(), 0);
		//
		// if (SDConfig.getConfiguration().isSdCardExist()) {
		// SDConfig.getConfiguration()
		// .setFileName("banks");
		// SDConfig.getConfiguration().saveToSDCard(
		// bankList);
		// }
		// } else {
		// if (banks!=null&&banks.getInfos()!=null) {
		// mAdapterBank.refreshData(
		// CardAddCardActivity.this.banks.getInfos(),
		// 0);
		// }
		//
		// }
		// } else {
		// if (banks!=null&&banks.getInfos()!=null) {
		// mAdapterBank.refreshData(
		// CardAddCardActivity.this.banks.getInfos(),
		// 0);
		// }
		// // mAdapterBank.refreshData(
		// // CardAddCardActivity.this.banks.getInfos(), 0);
		// }
		// }
		// });
		// };
		//
		// public void getBankListFailure(String error) {
		// runOnUiThread(new Runnable() {
		//
		// public void run() {
		// hideLoadingDialog();
		// mAdapterBank = new BankAdapter(CardAddCardActivity.this);
		// mBankSpinerPopWindow = new SpinerPopWindow(
		// CardAddCardActivity.this);
		// mBankSpinerPopWindow.setAdatper(mAdapterBank);
		// mBankSpinerPopWindow
		// .setItemListener(CardAddCardActivity.this);
		// mAdapterBank.refreshData(
		// CardAddCardActivity.this.banks.getInfos(), 0);
		// }
		// });
		// };
		//
		public void bankTypeSuccess(final BankType bankType) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if(("1000000").equals(bankType.getResCode())){
						
						ToastUtil.shortToast(mActivity, "银行卡号错误！");
						textView_addcard_save.setEnabled(true);
					}else if(("0").equals(bankType.getResCode())){

						// card类型：1，借记卡；2，信用卡
						int cardType = Integer.parseInt(bankType.getCardType());
						String cardName = bankType.getBankName().toString();
						if (cardType == 2) {
							ToastUtil.shortToast(mActivity, "信用卡无法绑定");
							textView_addcard_save.setEnabled(true);
						} else {
							Message msg = mHandler.obtainMessage();
							HashMap<String, String> map = new HashMap<>();
							map.put("cardName", cardName);
							map.put("bankCode", bankType.getBankCode());
							msg.obj = map;
							mHandler.sendMessage(msg);
						}
					
					}

					// final PromptDialog0 dialog1 = new PromptDialog0(
					// CardAddCardActivity.this);
					// switch (cardType) {
					// case 1:
					// dialog1.setMessage("是否确定绑定此卡");
					// dialog1.setNegativeButton(getString(R.string.btn_canl),
					// new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// dialog1.hideDialog();
					// }
					// });
					// dialog1.setPositiveButton(getString(R.string.btn_ok),
					// new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// dialog1.hideDialog();
					// Controller.getInstance().bindBank(
					// GlobalConstants.NUM_BINDBANK,
					// userInfo.getUserId(),
					// mProvinceName.getText()
					// .toString(),
					// mCityName.getText().toString(),
					// mBankName.getText().toString(),
					// mBankNumTxt, mBankCode, mBack);
					// }
					// });
					// dialog1.showDialog();
					//
					// break;
					// case 2:
					// dialog1.setMessage("信用卡无法绑定");
					// //
					// dialog1.setNegativeButton(getString(R.string.btn_canl),
					// // new OnClickListener() {
					// //
					// // @Override
					// // public void onClick(View v) {
					// // dialog1.hideDialog();
					// // }
					// // });
					// dialog1.setPositiveButton(getString(R.string.btn_ok),
					// new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// dialog1.hideDialog();
					// }
					// });
					// dialog1.showDialog();
					//
					// break;
					// default:
					// break;
					// }
				}
			});
		};

		public void bankTypeFailure(String error) {
		};
	};

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			textView_addcard_save.setEnabled(true);
			textView_addcard_save.setText(getResources().getString(
					R.string.usermanage_bindbank));
			HashMap<String, String> map = (HashMap<String, String>) msg.obj;
			mBankName.setText(map.get("cardName"));
			mBankCode = map.get("bankCode");
			id_addbank_changecity_layout.setVisibility(View.VISIBLE);
			id_addcard_bank_layout.setVisibility(View.VISIBLE);
		};
	};

	@Override
	protected void onDestroy() {
		Controller.getInstance().removeCallback(mBack);
		super.onDestroy();
	}

	;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			GlobalTools.hideSoftInput(CardAddCardActivity.this);
			finish();
			break;
		case R.id.textView_addcard_province:
		case R.id.laytout_addcard_province:
			showProSpinWindow();
			break;
		case R.id.textView_addcard_bank:
			// case R.id.laytout_addcard_bank:
			// showBankSpinWindow();
			// break;
		case R.id.textView_addcard_city:
		case R.id.laytout_addcard_city:
			showCitySpinWindow();
			break;
		case R.id.textView_addcard_save:
			// 方法：1提交银行卡到后台 2 绑定银行卡
			GlobalTools.hideSoftInput(CardAddCardActivity.this);
			mBankNumTxt = mBankNum.getText().toString();
			mNameTxt = mName.getText().toString();
			String numRegex = "(\\d{16})|(\\d{19})";
			mBankRes = mBankNumTxt.matches(numRegex);
			String saveStr = textView_addcard_save.getText().toString().trim();
			if (saveStr.equals(getResources().getString(
					R.string.usermanage_commitbank))) {
				if (TextUtils.isEmpty(mBankNumTxt)) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.card_errorinfo3));
				} else if (!mBankRes) {
					ToastUtil.shortToast(
							CardAddCardActivity.this,
							getResources().getString(
									R.string.addcard_bankcarderror));
				} else {
					textView_addcard_save.setEnabled(false);
					// 提交银行卡信息
					showLoadingDialog();
					Controller.getInstance().bankType(
							GlobalConstants.NUM_BANKTYPE, mBankNumTxt, mBack);
				}
			} else if (saveStr.equals(getResources().getString(
					R.string.usermanage_bindbank))) {
				String bankName = mBankName.getText().toString().trim();
				String provinceName = mProvinceName.getText().toString();
				String cityName = mCityName.getText().toString();
				if (TextUtils.isEmpty(mBankNumTxt)) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.card_errorinfo3));
				} else if (!mBankRes) {
					ToastUtil.shortToast(
							CardAddCardActivity.this,
							getResources().getString(
									R.string.addcard_bankcarderror));
				} else if (TextUtils.isEmpty(bankName)) {
					ToastUtil.longToast(this,
							getResources().getString(R.string.card_errorinfo0));
				} else if (TextUtils.isEmpty(provinceName)) {
					ToastUtil.longToast(this,
							getResources().getString(R.string.card_errorinfo1));
				} else if (TextUtils.isEmpty(cityName)) {
					ToastUtil.longToast(this,
							getResources().getString(R.string.card_errorinfo2));
				} else {
					textView_addcard_save.setEnabled(false);
					// 绑定银行卡信息
					showLoadingDialog();
					Controller.getInstance().bindBank(
							GlobalConstants.NUM_BINDBANK,
							mUserInfo.getUserId(), provinceName, cityName,
							bankName, mBankNumTxt, mBankCode, mBack);
				}
			} else if (saveStr.equals(getResources().getString(
					R.string.usermanage_resetbank))) {
				if (TextUtils.isEmpty(mBankNumTxt)) {
					ToastUtil.shortToast(this,
							getResources().getString(R.string.card_errorinfo3));
				} else if (!mBankRes) {
					ToastUtil.shortToast(
							CardAddCardActivity.this,
							getResources().getString(
									R.string.addcard_bankcarderror));
				} else {
					textView_addcard_save.setEnabled(false);
					// 提交银行卡信息
					showLoadingDialog();
					Controller.getInstance().bankType(
							GlobalConstants.NUM_BANKTYPE, mBankNumTxt, mBack);
				}
			}
			// if (mBankRes && mNameTxt.length() > 0
			// && mBankName.getText().toString() != ""
			// && mProvinceName.getText().toString() != ""
			// && mCityName.getText().toString() != "") {
			// showLoadingDialog();
			// if
			// (((TextView)v).getText().toString().trim().equals(getResources().getString(R.string.usermanage_commitbank))&&type==1)
			// {
			// //提交绑定银行卡
			// Controller.getInstance().bankType(GlobalConstants.NUM_BANKTYPE,mBankNumTxt,
			// mBack);
			//
			// }else {
			// Controller.getInstance().bindBank(
			// GlobalConstants.NUM_BINDBANK,
			// userInfo.getUserId(),
			// mProvinceName.getText().toString(),
			// mCityName.getText().toString(),
			// mBankName.getText().toString(),
			// mBankNumTxt, mBankCode, mBack);
			// }
			//
			//
			// // //提交绑定银行卡
			// //
			// Controller.getInstance().bankType(GlobalConstants.NUM_BANKTYPE,
			// // mBankNumTxt, mBack);
			// //
			// Controller.getInstance().bindBank(GlobalConstants.NUM_BINDBANK,
			// // userInfo.getUserId(), mProvinceName.getText().toString(),
			// // mCityName.getText().toString(),
			// // mBankName.getText().toString(), mBankNumTxt, mBankCode,
			// // mBack);
			// } else if (mBankName.getText().toString().equals("")) {
			// ToastUtil.longToast(this,
			// getResources().getString(R.string.card_errorinfo0));
			// } else if (mProvinceName.getText().toString().equals("")) {
			// ToastUtil.longToast(this,
			// getResources().getString(R.string.card_errorinfo1));
			// } else if (mCityName.getText().toString().equals("")) {
			// ToastUtil.longToast(this,
			// getResources().getString(R.string.card_errorinfo2));
			// } else if (mBankNumTxt.length() == 0) {
			// ToastUtil.shortToast(this,
			// getResources().getString(R.string.card_errorinfo3));
			// } else if (mNameTxt.length() == 0) {
			// ToastUtil.shortToast(this,
			// getResources().getString(R.string.card_errorinfo4));
			// } else if (!mBankRes) {
			// ToastUtil.shortToast(CardAddCardActivity.this, getResources()
			// .getString(R.string.addcard_numerror));
			// }
			break;
		case R.id.textview_addcard:
			showBankCardWindow();
			break;

		default:
			break;
		}
	}

	/**
	 * 每一个列表item点击处理(省的列点击事件)
	 * 
	 * @param view
	 * @param pos
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月13日
	 */
	@Override
	public void onItemClick(View view, int pos) {
		if (cityLayout.isEnabled()) {
			if (pos >= 0 && pos <= citys.size()) {
				mCityName.setText(citys.get(pos).getCity());
			}
		} else {
			if (pos >= 0 && pos <= provinces.size()) {
				mProvinceName.setText(provinces.get(pos).getProvince());
				mAdapterCity = new CityAdapter(CardAddCardActivity.this);
				citys = provinces.get(pos).getCitys();
				mAdapterCity.refreshData(citys, 0);
				mCityName.setText(citys.get(0).getCity());
				cityLayout.setEnabled(true);

				mCitySpinerPopWindow.setAdatper(mAdapterCity);
				mCitySpinerPopWindow.setItemListener(CardAddCardActivity.this);
			}
		}
		// if (isClickBank) {
		// if (pos >= 0 && pos <= banks.getInfos().size()) {
		// mBankName.setText(banks.getInfos().get(pos).getBankname());
		// mBankCode = banks.getInfos().get(pos).getBankcode();
		// }
		// } else {
		//
		// }

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
		// isClickBank = false;
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
		// isClickBank = false;
		mCitySpinerPopWindow.setWidth(cityLayout.getWidth());
		mCitySpinerPopWindow.showAsDropDown(cityLayout);
	}

	//
	// /**
	// * 展开银行列表
	// *
	// * @Description:
	// * @author:www.wenchuang.com
	// * @date:2015年11月13日
	// */
	// private void showBankSpinWindow() {
	// isClickBank = true;
	// mBankSpinerPopWindow.setWidth(bankLayout.getWidth());
	// mBankSpinerPopWindow.showAsDropDown(bankLayout);
	// }

	/**
	 * 展开银行卡列表;
	 */
	private void showBankCardWindow() {
		// isClickBank = false;
		mCardSpinerPopWindow.setWidth(bankLayout.getWidth());
		mCardSpinerPopWindow.showAsDropDown(mBankNum);
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
		public View getView(int pos, View view, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (view == null) {
				view = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) view
						.findViewById(R.id.item_card_text);
				viewHolder.id_top_line=view.findViewById(R.id.id_top_line);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			BankInfo item = (BankInfo) getItem(pos);
			viewHolder.mTextView.setText(item.getBankname());
			viewHolder.id_top_line.setVisibility(pos==0?View.VISIBLE:View.GONE);

			return view;
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
		public View getView(int pos, View view, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (view == null) {
				view = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) view
						.findViewById(R.id.item_card_text);
				viewHolder.id_top_line=view.findViewById(R.id.id_top_line);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			Province item = (Province) getItem(pos);
			viewHolder.mTextView.setText(item.getProvince());
			viewHolder.id_top_line.setVisibility(pos==0?View.VISIBLE:View.GONE);
			return view;
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
		public View getView(int pos, View view, ViewGroup arg2) {
			ViewHolder viewHolder;

			if (view == null) {
				view = View.inflate(cxt, R.layout.item_card_spinner,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) view
						.findViewById(R.id.item_card_text);
				viewHolder.id_top_line=view.findViewById(R.id.id_top_line);
				view.setTag(viewHolder);
				
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			City item = (City) getItem(pos);
			viewHolder.mTextView.setText(item.getCity());
			viewHolder.id_top_line.setVisibility(pos==0?View.VISIBLE:View.GONE);
			return view;
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

	/**
	 * 输入银行卡失去焦点的点击事件
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (TextUtils.isEmpty(mBankNum.getText().toString().trim())) {
			ToastUtil.shortToast(CardAddCardActivity.this, "请输入银行卡号");
		}
	}
}
