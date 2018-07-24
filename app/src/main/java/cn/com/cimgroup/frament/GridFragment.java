package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.BuyRedPacketActivity;
import cn.com.cimgroup.activity.CenterReChargeActivity;
import cn.com.cimgroup.activity.CommitPayActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.activity.UserManageActivity;
import cn.com.cimgroup.adapter.RedPacketAdapter;
import cn.com.cimgroup.bean.Product;
import cn.com.cimgroup.bean.RedPacketLeMiList;
import cn.com.cimgroup.bean.UserCount;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.dailog.ComfirmConvertDialog;
import cn.com.cimgroup.dailog.ComfirmConvertDialog.ConfirmListener;
import cn.com.cimgroup.dailog.ComfirmConvertDialogNew;
import cn.com.cimgroup.dailog.ComfirmConvertPhoneDialog;
import cn.com.cimgroup.dailog.ComfirmConvertPhoneDialog.ConfirmPhoneListener;
import cn.com.cimgroup.dailog.LotteryProgressDialog;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.view.NotScrollGridView;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * Created by small on 16/1/13.
 */
public class GridFragment extends BaseLoadFragment implements
		OnClickListener, ConfirmListener,
		ConfirmPhoneListener, ComfirmConvertDialogNew.ConfirmListener {

	public static final String TYPE = "type";
	/**购买红包*/
	public static final int BUYREDPACK = 0x1001; 
	/**乐米红包*/
	public static final int LEMIRED = 0x0000; 
	/**乐米充值卡*/
	public static final int LEMITELE = 0x0002; 
	/**乐米京东券*/
	public static final int LEMIJD = 0x0003; 
	/**乐米国美券*/
	public static final int LEMIGM = 0x0004; 
	/**乐米苏宁券*/
	public static final int LEMISN = 0x0005; 
	/**购买乐米*/
	public static final int BUYLEMI = 0x1007; 

	private View mView;
	private NotScrollGridView gridView;
	private TextView noticeText, titleView, rightTextView, userCountView;
	private TextView backButton;
	private RedPacketAdapter adapter;
	private RelativeLayout titleRel;
	/** 确认兑换除充值卡外的东西    的    dialog**/
	private ComfirmConvertDialog dialog;
	private ComfirmConvertDialogNew dialogNew;
	/** 确认兑换充值卡  的   dialog**/
	private ComfirmConvertPhoneDialog phoneDialog;
	/** 视图是否已经准备好**/
	private boolean isPrepared = false;
	/** 网络加载loding框 **/
	protected LotteryProgressDialog mProgressDialog;
	/**是否展示confirmdialognew的标志位**/
	private boolean confirm_flag = false;
	
	private double rechargeMoney = 100;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// 初始化dialog
		mProgressDialog = new LotteryProgressDialog(getActivity());
		
		if (mView == null) {
			mView = inflater.inflate(R.layout.activity_buy_redpacket, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeAllViews();
			}
		}
		isPrepared = true;
		gridView = (NotScrollGridView) mView.findViewById(R.id.gridView_buy);
		gridView.initView(inflater);
		
		gridView.setOnClickListener(new NotScrollGridView.OnClickListener() {
			
			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				
				case CException.NET_ERROR:
					if (!NetUtil.isConnected(mFragmentActivity)) {
						//无网络
					}else {
						initView(type);
					}
					break;
				case CException.IOERROR:
					initView(type);
					break;
				default:
					break;
				}
			}
		});
		noticeText = (TextView) mView.findViewById(R.id.red_packet_notice);
		titleView = (TextView) mView.findViewById(R.id.title_view);
		backButton = (TextView) mView.findViewById(R.id.left_button);
		rightTextView = (TextView) mView.findViewById(R.id.right_textview);
		titleRel = (RelativeLayout) mView.findViewById(R.id.title_view_real);
		userCountView = (TextView) mView.findViewById(R.id.tv_user_count);
		titleRel.setVisibility(View.GONE);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// isPrepared = true;
		Bundle bundle = getArguments();
		if (bundle != null) {
			type = bundle.getInt(TYPE);
			// if (type == BUYREDPACK || type == BUYLEMI||!isPrepared ||
			// isVisible) {
//			isVisible = true;
			if (type == BUYREDPACK || type == BUYLEMI) {
				//购买乐米，购买红包，正常加载，因为没有使用Viewpager
				initView(type);
			}else {
				//乐米兑换，走懒加载
				lazyLoad();
			}
		}
		// lazyLoad();
		// }
		return mView;
	}

	private int type;

	@Override
	protected void loadData(int page) {
	}

	@Override
	public void loadData(int title, int page) {

	}

	@Override
	protected void lazyLoad() {
		//这两个标志位缺一不可
		//isVisiable表示fragment是否已经可见，由父类维护，当true的时候可以加载数据
		//isPrapared表示fragment是否已经初始化完（inflate等），如果没有这个判断，则会导致空指针报错
		if (!isVisible || !isPrepared || mHasLoadeOnce) {
			return;
		}
		initView(type);
		isPrepared = false;
	}

	private void updateCount() {
		
		BigDecimal moneys = (BigDecimal) (!TextUtils.isEmpty(App.userInfo
				.getBetAcnt()) ? BigDecimal.valueOf(Double
				.parseDouble(App.userInfo.getBetAcnt())) : 0);
		BigDecimal prize = (BigDecimal) (!TextUtils.isEmpty(App.userInfo
				.getPrizeAcnt()) ? BigDecimal.valueOf(Double
				.parseDouble(App.userInfo.getPrizeAcnt())) : 0);
		// double prize = App.userInfo.getPrizeAcnt() != null
		// || !App.userInfo.getPrizeAcnt().equals("") ? Double
		// .parseDouble(App.userInfo.getPrizeAcnt()) : 0;
		String moneyStr = (moneys.add(prize)) + "";

		String lemi = "";
		String red = "";
		String bet = "";

		String betArr = moneyStr.split("\\.")[0];
		if (betArr.length() > 5) {
			bet = betArr.substring(0, betArr.length() - 4)
					+ getResources().getString(R.string.price_ten_thousand);
		} else {
			bet = moneyStr;
		}

		if (!StringUtil.isEmpty(App.userInfo.getIntegralAcnt())) {
			String leArr = App.userInfo.getIntegralAcnt().split("\\.")[0];
			if (leArr.length() > 5) {
				lemi = leArr.substring(0, leArr.length() - 4)
						+ getResources().getString(R.string.price_ten_thousand);
			} else {
				lemi = App.userInfo.getIntegralAcnt();
			}
		} else {
			lemi = "0";
		}

		if (!StringUtil.isEmpty(App.userInfo.redPkgAccount)) {
			String redArr = App.userInfo.redPkgAccount.split("\\.")[0];
			if (redArr.length() > 5) {
				red = redArr.substring(0, redArr.length() - 4)
						+ getResources().getString(R.string.price_ten_thousand);
			} else {
				red = App.userInfo.redPkgAccount;
			}
		}
		if (!TextUtils.isEmpty(App.userInfo.getBetAcnt())
				&& !TextUtils.isEmpty(App.userInfo.redPkgNum)
				&& !TextUtils.isEmpty(App.userInfo.redPkgAccount)){
			//将%d 替换成对应参数的整数  %s 替换成对应参数的字符串 
			//参数按顺序替换
			userCountView.setText(getString(R.string.user_count, bet,
					App.userInfo.redPkgNum, red));
			}else {
				userCountView.setText(getString(R.string.user_count,"0.00","0","0.00"));
			}
		
		if (!TextUtils.isEmpty(App.userInfo.getBetAcnt())
				&& !TextUtils.isEmpty(App.userInfo.getIntegralAcnt())) {
			userCountView.setText(getString(R.string.user_convert_count, bet,
					lemi));
		}else {
			userCountView.setText(getString(R.string.user_convert_count,"0.00","0.00"));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//TODO 人为制造没有实名认证的数据
//		if (App.userInfo != null) {
//			App.userInfo.setRealName("");
//		}
		//判断用户登录情况   未登录-乐米，红包，余额 显示为0   登录-正常显示
		if (App.userInfo != null) {
			updateCount();
		}else {
			userCountView.setText(getString(R.string.user_convert_count,"0.00","0.00"));
		}
		
	}

	private void initView(int type) {
		switch (type) {
		case BUYREDPACK:
			// 购买红包;
			buyRedPack();
			break;
		case BUYLEMI:
			// 购买乐米;
			buyLeMi();
			break;
		default:
			// 乐米兑换;
			lemiConvert(type);
			break;
		}

	}

	/**
	 * 购买红包
	 * 
	 * @param position
	 */
	private void goToBuy(final int position) {
		Product item = (Product) adapter.getItem(position);
		
		BigDecimal moneys = (BigDecimal) (!TextUtils.isEmpty(App.userInfo
				.getBetAcnt()) ? BigDecimal.valueOf(Double
				.parseDouble(App.userInfo.getBetAcnt())) : 0);
		BigDecimal prize = (BigDecimal) (!TextUtils.isEmpty(App.userInfo
				.getPrizeAcnt()) ? BigDecimal.valueOf(Double
				.parseDouble(App.userInfo.getPrizeAcnt())) : 0);
		// double prize = App.userInfo.getPrizeAcnt() != null
		// || !App.userInfo.getPrizeAcnt().equals("") ? Double
		// .parseDouble(App.userInfo.getPrizeAcnt()) : 0;
		String moneyStr = (moneys.add(prize)) + "";
		
		if (Double.parseDouble(item.productSaleMoney) > Double.parseDouble(moneyStr)) {
			rechargeMoney = Double.parseDouble(item.productSaleMoney);
			dialog.show();
			dialog.setData("余额不足，是否充值？");
			return;
		}
		
		Intent intent = new Intent(getActivity(),
				CommitPayActivity.class);
		intent.putExtra(CommitPayActivity.DATA, item);
		// Intent intent = new Intent(getActivity(),
		// ConfirmPayActivity.class);
		// intent.putExtra(ConfirmPayActivity.DATA, item);
		intent.putExtra("type", type);
		startActivity(intent);
//		final PromptDialog0 dialog = new PromptDialog0(getActivity());
//		dialog.setMessage("该红包无法用于虚拟购彩，红包将添加至您的账户，是否继续购买？");
//		dialog.setNegativeButton(getString(R.string.btn_canl),
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.hideDialog();
//
//					}
//				});
//		dialog.setPositiveButton(getString(R.string.btn_ok),
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.hideDialog();
//						Product item = (Product) adapter.getItem(position);
//						Intent intent = new Intent(getActivity(),
//								CommitPayActivity.class);
//						intent.putExtra(CommitPayActivity.DATA, item);
//						// Intent intent = new Intent(getActivity(),
//						// ConfirmPayActivity.class);
//						// intent.putExtra(ConfirmPayActivity.DATA, item);
//						intent.putExtra("type", type);
//						startActivity(intent);
//
//					}
//				});
//		dialog.showDialog();

	}

	// 购买红包列表;
	private void buyRedPack() {
		titleRel.setVisibility(View.VISIBLE);
		rightTextView.setVisibility(View.VISIBLE);
		rightTextView.setText("我的红包");
		titleView.setText("购买红包");
		noticeText.setText(Html.fromHtml(getResources().getString(
				R.string.red_packet_notice)));
		noticeText.setMovementMethod(LinkMovementMethod.getInstance());
		backButton.setOnClickListener(this);
		rightTextView.setOnClickListener(this);
		
		dialog = new ComfirmConvertDialog(getActivity(),
				R.style.MessageDelDialog);

		adapter = new RedPacketAdapter(getActivity(), null,
				new RedPacketAdapter.ClickListener() {
					@Override
					public void onClick(int position ,int type) {
						if (type == 0) {
							//可以购买
							if (App.userInfo!=null&&!TextUtils.isEmpty(App.userInfo.getUserId())) {
							goToBuy(position);
							}else {
							startActivity(LoginActivity.class);
							}
						}else if (type == 1) {
							dialog.show();
							Product item = (Product) adapter.getItem(position);
							rechargeMoney = Double.parseDouble(item.productSaleMoney);
							dialog.setData("余额不足，是否充值？");
						}
						
					}
//					@Override
//					public void goToRecharge(int position) {
//						//余额不足 前去充值？ 的弹框
//						Log.e("wushiqiu", "余额不足，购买红包，需要弹窗");
//						dialog.show();
//						dialog.setData("余额不足，去充值？");
//					}
				});
		
		//弹窗的点击事件 
		dialog.setListener(new ConfirmListener() {
			
			@Override
			public void onGo() {
				//按钮为    继续兑换   时的点击事件
			}
			
			@Override
			public void onClick(Product product) {
				
				dialog.dismiss();
				//跳转充值
				if (App.userInfo != null && TextUtils.isEmpty(App.userInfo.getRealName())) {
					//跳转
					isValidate();
				}else {
					Intent intent = new Intent(mFragmentActivity, CenterReChargeActivity.class);
					intent.putExtra("money", (int)rechargeMoney);
					startActivity(intent);
				}
			}
			
			@Override
			public void onCheck() {
				//按钮为     查看记录   时的点击事件
			}
		});

		adapter.setType(BUYREDPACK);
		gridView.setAdapter(adapter);

		//2.5期修改   判断用户的登陆状态   登陆/未登陆都能查看红包列表
		if (App.userInfo != null && !TextUtils.isEmpty(App.userInfo.getUserId())) {
			//用户已登陆
			Controller.getInstance().getRedPackAndLeMiList(
				GlobalConstants.NUM_PRODUCT,
				App.userInfo.getUserId(),
				Product.REDPKG, callBack);
		}else {
			//用户未登录
			Controller.getInstance().getRedPackAndLeMiList(
					GlobalConstants.NUM_PRODUCT,
					"", Product.REDPKG, callBack);
		}
		
	}	

	// 购买乐米列表;
	private void buyLeMi() {
		titleRel.setVisibility(View.VISIBLE);
		titleView.setText("购买乐米");
		String tips = "";
//		tips = "说明：<br/>" + "乐米是乐博彩票网站专有虚拟游戏货币，使用乐米才可以在本网站参与虚拟竞猜、娱乐场等多种游戏。"
//				+ "用户通过现金购买，或参与虚拟竞猜、娱乐场等活动还可赚取更多乐米，在乐米兑换专区可以兑换话费等奖励。<br/>";
		tips = "说明：<br/>"
				+ "1、乐米是乐博彩票网站专有虚拟游戏货币，使用乐米可以在本网站参与虚拟竞猜、娱乐场等多种游戏。<br/>"
				+ "2、用户可通过现金购买乐米，或参与虚拟竞猜、娱乐场等活动赚取乐米。<br/>"
				+ "3、在乐米兑换专区可以兑换话费等奖励。<br/>"
				+ "如有问题请联系本站客服："
				+ "<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		noticeText.setText(Html.fromHtml(tips));
		noticeText.setMovementMethod(LinkMovementMethod.getInstance());
		// noticeText.setText(Html.fromHtml(getResources().getString(
		// R.string.red_packet_notice)));
		backButton.setOnClickListener(this);
		dialog = new ComfirmConvertDialog(getActivity(),
				R.style.MessageDelDialog);
		adapter = new RedPacketAdapter(getActivity(), null,
				new RedPacketAdapter.ClickListener() {
					@Override
					public void onClick(int position ,int type) {
						if (type == 0) {
							// 购买乐米;
							if (App.userInfo!=null&&!TextUtils.isEmpty(App.userInfo.getUserId())) {
								goToBuy(position);
							}else {
								startActivity(LoginActivity.class);
							}
						}else if (type == 1) {
							dialog.show();
							Product item = (Product) adapter.getItem(position);
							rechargeMoney = Double.parseDouble(item.productSaleMoney);
							dialog.setData("余额不足，是否充值？");
						}
						
					}

//					@Override
//					public void goToRecharge(int position) {
//						//余额不足 前去充值？ 的弹框
////						Log.e("wushiqiu", "余额不足，购买乐米，需要弹窗");
//						dialog.show();
//						dialog.setData("余额不足，去充值？");
//					}
				});
		
		dialog.setListener(new ConfirmListener() {
			
			@Override
			public void onGo() {
				//按钮为     继续兑换  时的点击事件
			}
			
			@Override
			public void onClick(Product product) {
				
				dialog.dismiss();
				//跳转充值
				if (App.userInfo != null && TextUtils.isEmpty(App.userInfo.getRealName())) {
					//跳转
					isValidate();
				}else {
					Intent intent = new Intent(mFragmentActivity, CenterReChargeActivity.class);
					intent.putExtra("money", (int)rechargeMoney);
					startActivity(intent);
				}
			}
			
			@Override
			public void onCheck() {
				//按钮为     查看记录   时的点击事件
			}
		});

		adapter.setType(BUYLEMI);
		gridView.setAdapter(adapter);
		
		//2.5期修改   判断用户的登陆状态   登陆/未登陆都能查看乐米列表
		if (App.userInfo != null && !TextUtils.isEmpty(App.userInfo.getUserId())) {
			//用户已登录
			Controller.getInstance().getRedPackAndLeMiList(
				GlobalConstants.NUM_PRODUCT,
				App.userInfo.getUserId(),
				Product.LEMI, callBack);
		}else {
			//用户未登录
			Controller.getInstance().getRedPackAndLeMiList(
					GlobalConstants.NUM_PRODUCT,
					"",
					Product.LEMI, callBack);
		}
		
	}

	// 乐米兑换列表;
	private void lemiConvert(final int type) {

		String tips = "";
		if (type == LEMIRED) {
			// 提示语-->乐米红包
			tips = "说明：  <br/>"
					+ "1、乐米兑换的红包只能在乐博彩票客户端、网站购彩使用。<br/>"
					+ "2、兑换的红包为乐博通用红包。<br/>"
					+ "3、红包为一次性使用红包。<br/>"
					+ "4、兑换的红包请在30天内使用。<br/>"
					+ "<font color=#f66248>5、目前是彩票停售期，您兑换的红包无法用于虚拟购彩，暂停红包兑换。</font><br/>"
					+ "如有问题请联系本站客服："
					+ "<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		} else if (type == LEMITELE) {
			// 提示语-->乐米话费充值
			tips = "说明：<br/>"
					+ "1、网站目前支持移动、联通、电信话费充值，您可以给自己充值也可以给其他人充值。<br/>"
					+ "2、到账时间按运营商实际到账时间为准，如10分钟内未收到短信通知请登录运营商网站查询。<br/>"
					+ "如有问题请联系本站客服：<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		} else if (type == LEMIJD) {
			// 提示语-->乐米京东券
			tips = "说明：<br/>"
					+ "1、京东电子券兑换成功后，以短信形式下发密码到您的手机，请注意查收！<br/>"
					+ "2、京东电子券可以转让使用。<br/>"
					+ "3、京东电子券有效期为“自激活之日起36个月”。 <br/>"
					+ "4、京东电子券不记名、不挂失、不兑现、不计息、不可修改密码，请妥善保管卡号及密码。<br/>"
					+ "5、京东电子券不可与其他优惠券一起使用。<br/>"
					+ "6、京东电子券仅能购买京东自营商品。<br/>"
					+ "7、京东电子券购买的商品发生退货时，所支付资金会自动退回到卡内。 <br/>"
					+ "如有问题请联系本站客服："
					+ "<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		} else if (type == LEMIGM) {
			// 提示语-->乐米国美券
			tips = "说明：<br/>"
					+ "1、国美电子券兑换成功后，以短信形式下发密码到您的手机，请注意查收！<br/>"
					+ "2、国美电子券可以转让使用。<br/>"
					+ "3、国美电子券有效期为“自激活之日起36个月”。 <br/>"
					+ "4、国美电子券不记名、不挂失、不兑现、不计息、不可修改密码，请妥善保管卡号及密码。<br/>"
					+ "5、国美电子券不可与其他优惠券一起使用。<br/>"
					+ "6、国美电子券仅能购买国美自营商品。<br/>"
					+ "7、国美电子券购买的商品发生退货时，所支付资金会自动退回到卡内。 <br/>"
					+ "如有问题请联系本站客服："
					+ "<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		} else if (type == LEMISN) {
			// 提示语-->乐米苏宁券
			tips = "说明： <br/>"
					+ "1、苏宁电子券兑换成功后，以短信形式下发密码到您的手机，请注意查收！<br/>"
					+ "2、苏宁电子券可以转让使用。 <br/>"
					+ "3、苏宁电子券有效期为“自激活之日起36个月”。 <br/>"
					+ "4、苏宁电子券不记名、不挂失、不兑现、不计息、不可修改密码，请妥善保管卡号及密码。 <br/>"
					+ "5、苏宁电子券不可与其他优惠券一起使用。<br/>"
					+ "6、苏宁电子券仅能购买苏宁自营商品。<br/>"
					+ "7、苏宁电子券购买的商品发生退货时，所支付资金会自动退回到卡内。 <br/>"
					+ "如有问题请联系本站客服："
					+ "<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		} else if (type == BUYLEMI) {
//			tips = "说明：<br/>" + "乐米是乐博彩票网站专有虚拟游戏货币，使用乐米才可以在本网站参与虚拟竞猜、娱乐场等多种游戏。"
//					+ "用户通过现金购买，或参与虚拟竞猜、娱乐场等活动还可赚取更多乐米，在乐米兑换专区可以兑换话费等奖励。 <br/>";
			tips = "说明：<br/>"
					+ "1、乐米是乐博彩票网站专有虚拟游戏货币，使用乐米可以在本网站参与虚拟竞猜、娱乐场等多种游戏。<br/>"
					+ "2、用户可通过现金购买乐米，或参与虚拟竞猜、娱乐场等活动赚取乐米。<br/>"
					+ "3、在乐米兑换专区可以兑换话费等奖励。<br/>"
					+ "如有问题请联系本站客服："
					+ "<font color=#f66248><a href=tel:010-65617701>010-65617701</a></font> <br/>";
		}
		noticeText.setText(Html.fromHtml(tips));
		noticeText.setMovementMethod(LinkMovementMethod.getInstance());
		// noticeText.setText(Html.fromHtml(getResources().getString(
		// R.string.red_packet_notice)));
		adapter = new RedPacketAdapter(getActivity(), null,
				new RedPacketAdapter.ClickListener() {
					@Override
					public void onClick(int position , int cType) {
						if (cType == 0) {
							// 乐米兑换;
							//2.5期内容修改 点击按钮 先判断用户是否登陆
							// 未登陆  则  跳登陆
							if (App.userInfo != null && !TextUtils.isEmpty(App.userInfo.getUserId())) {
								if (type == 2) {
								//手机充值卡兑换  dialog和其他不同
									
									//进入此页面点击购买到登陆页面，返回后加的购买判断
									String lemi = App.userInfo.getIntegralAcnt();
									if (Double.parseDouble(((Product) adapter.getItem(position)).productSaleMoney) > Double.parseDouble(lemi)) {
										dialog.show();
										dialog.setData("乐米不足，是否购买？");
										dialog.flag = 1;
									} else {
									
										phoneDialog.show();
										phoneDialog.setData((Product) adapter
												.getItem(position));
									}
								} else {
								dialogNew.show();
//								dialog.setData((Product) adapter.getItem(position),
//										type);
								dialogNew.setData((Product)adapter.getItem(position), type);
								}
							}else {
								//未登录 跳转登陆
								startActivity(LoginActivity.class);
							}
						}else if (cType == 1) {
							dialog.show();
							dialog.setData("乐米不足，是否购买？");
							dialog.flag = 1;
						}
						
					}

//					@Override
//					public void goToRecharge(int position) {
////						Log.e("wushiqiu", "余额不足，乐米兑换，需要弹窗");
//						
//					}
				});

		dialog = new ComfirmConvertDialog(getActivity(),
				R.style.MessageDelDialog);
		dialogNew = new ComfirmConvertDialogNew(getActivity(),
				R.style.MessageDelDialog);
		phoneDialog = new ComfirmConvertPhoneDialog(getActivity(),
				R.style.MessageDelDialog);
		dialog.setListener(this);
		dialogNew.setListener(this);
		phoneDialog.setListener(this);

		adapter.setType(type);
		gridView.setAdapter(adapter);

		//2.5期修改   判断用户的登陆状态   登陆/未登陆都能查看乐米兑换列表
		if (App.userInfo != null && !TextUtils.isEmpty(App.userInfo.getUserId())) {
			//用户已登录
			Controller.getInstance().getLMConvertProdectList(
				GlobalConstants.NUM_CONVERT,
				App.userInfo.getUserId(),
				type + "", callBack);
		}else {
			//用户未登录
			Controller.getInstance().getLMConvertProdectList(
					GlobalConstants.NUM_CONVERT,
					"",
					type + "", callBack);
		}
		

	}

	private RedPacketLeMiList product;
	private UserCount userCount = new UserCount();

	private CallBack callBack = new CallBack() {
		@Override
		public void onSuccess(final Object object) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (object != null) {
						if (object instanceof RedPacketLeMiList) {
							getActivity().runOnUiThread(new Runnable() {
								@SuppressWarnings("unchecked")
								@Override
								public void run() {
									mProgressDialog.hideDialog();
									product = (RedPacketLeMiList) object;
									if (product.resCode.equals("0")) {
										// adapter.setDatas(product.productList);
										adapter.replaceAll(product.productList);
									} else {
										noticeText.setText("");
										gridView.resetHeight(getHeight());
										NoDataUtils.setNoDataView(mFragmentActivity, gridView.emptyImage, gridView.oneText, gridView.twoText, gridView.button, "0", 2);
									}

								}
							});
						} else if (object instanceof UserCount) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mProgressDialog.hideDialog();
									if(dialogNew != null){
										dialogNew.hide();
									}
//									dialogNew.showSucc(getActivity(), type);
									// 兑换回掉;兑换成功;
									userCount = (UserCount) object;
									if (userCount.resCode != null
											&& userCount.resCode.equals("0")) {
										if (confirm_flag) {
											UserInfo userInfo = App.userInfo;
											dialogNew.showSucc(getActivity(), type);
											confirm_flag = false;
										}else {
											dialog.showSucc(type);
										}
										
										CommitPayActivity
												.saveUserCount2UserInfo(
														userCount,
														UserLogic
																.getInstance()
																.getDefaultUserInfo());
									} else {
										ToastUtil.shortToast(mFragmentActivity,
												userCount.resMsg);
									}

								}
							});
						}

					}
				}
			});
		}

		@Override
		public void onFail(final String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(dialogNew != null){
						dialogNew.hide();
					}
//					ToastUtil.shortToast(getActivity(), error);
					noticeText.setText("");
					gridView.resetHeight(getHeight());
					NoDataUtils.setNoDataView(mFragmentActivity, gridView.emptyImage, gridView.oneText, gridView.twoText, gridView.button, error, 0);
				}
			});

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_button:
			getActivity().onBackPressed();
			break;
		case R.id.right_textview:
			if (App.userInfo == null) {
				startActivity(LoginActivity.class);
			}else {
				Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
				intent.putExtra(MessageCenterActivity.TYPE, MessageCenterActivity.REDPACKET);
				startActivity(intent);
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(Product product) {
		mProgressDialog.showDialog();
		// 确认兑换;
		dialog.dismiss();
		if (dialog.flag == 1) {
			//余额不足 进行充值逻辑  -- 跳转充值
//			startActivity(CenterReChargeActivity.class);
			Intent buyLeMi = new Intent(getActivity(),
					BuyRedPacketActivity.class);
			buyLeMi.putExtra(GridFragment.TYPE, GridFragment.BUYLEMI);
			getActivity().startActivity(buyLeMi);
			dialog.flag = 0;
			mProgressDialog.hideDialog();
		}else {
			//确认兑换逻辑
			int proType = 0;
			if (type == 0) {
				proType = 1;
			} else {
				proType = type;
			}
			Controller.getInstance().convertLiMeRequest(
					GlobalConstants.NUM_CONVERT_PRODUCT,
					App.userInfo.getUserId(),
					product.productMoney, product.productId, proType + "", "",
					callBack);
			}
	}

	/**
	 * 222222兑换完成后 弹出的dialog的点击事件（查看记录按钮）
	 */
	@Override
	public void onCheck() {
//		Intent it = new Intent(getActivity(), LeMiConvertNotesActivity.class);
//		getActivity().startActivity(it);
		// 跳转个人中心--红包明细
		GlobalConstants.isRefreshFragment = true;
		GlobalConstants.personTagIndex = 2;
		GlobalConstants.isShowLeMiFragment = true;
		MainActivity main = (MainActivity) ActivityManager
				.isExistsActivity(MainActivity.class);
		if (main != null) {
			// 如果存在MainActivity实例，那么展示LoboHallFrament页面
			ActivityManager.retain(MainActivity.class);
			main.showFrament(3);
		}
		
	}

	/**
	 * 111111乐米兑换充值卡的dialog的点击事件（确定按钮）
	 */
	@Override
	public void onClick(Product product, String phone) {
		mProgressDialog.showDialog();
		phoneDialog.dismiss();
		Controller.getInstance().convertLiMeRequest(
				GlobalConstants.NUM_CONVERT_PRODUCT,
				App.userInfo.getUserId(),
				product.productMoney, product.productId, type + "", phone,
				callBack);
	}

	/**
	 * 222222兑换完成后 弹出的dialog的点击事件（继续兑换按钮）
	 * 
	 */
	@Override
	public void onGo() {
		updateCount();
		initView(type);
	}
	
	//2016-9-19 修改记录 将removeCallBack放到onStop()中进行
	@Override
	public void onStop() {
		super.onStop();
		Controller.getInstance().removeCallback(callBack);
	}
	
	/**
	 * 账户未完善信息，是否去完善
	 */
	private void isValidate() {
//		final PromptDialog0 dialog = new PromptDialog0(getActivity());
//		dialog.setMessage("您未实名认证，请先完成认证？");
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(getActivity());
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
	 * 确认兑换处的确认按钮的点击事件
	 */
	@Override
	public void onClick(Product product, boolean flag) {
		//确认兑换逻辑
		int proType = 0;
		if (type == 0) {
			proType = 1;
		} else {
			proType = type;
		}
		confirm_flag = true;
		Controller.getInstance().convertLiMeRequest(
				GlobalConstants.NUM_CONVERT_PRODUCT,
				App.userInfo.getUserId(),
				product.productMoney, product.productId, proType + "", "",
				callBack);
	}
	
	
	/**
	 * 获取屏幕的高度 用于重置gridView的高度
	 * @return
	 */
	public int getHeight(){
		
		int screenHeight = DensityUtils.getScreenHeight(getActivity());
		Log.e("wushiqiu", "");
		
		ViewUtils.measureView(noticeText);
		int textHeight = noticeText.getMeasuredHeight();
		int result = 0;
		
		if (type == BUYREDPACK || type == BUYLEMI) {
			
			result = 50+30+textHeight+20;
		}else{
			result = 50+30+40+textHeight+20;
		}
		
		return screenHeight - DensityUtils.dip2px(result);
	}
}
