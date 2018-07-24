package cn.com.cimgroup.frament;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.bean.AccountDetail;
import cn.com.cimgroup.bean.AccountDetailList;
import cn.com.cimgroup.bean.RedPkgDetail;
import cn.com.cimgroup.bean.RedPkgItemDetail;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.view.HorizontalScrollTabStrip;
import cn.com.cimgroup.view.HorizontalScrollTabStrip.TagChangeListener;
import cn.com.cimgroup.xutils.NetUtil;

/**
 * 个人信息现金碎片
 * 
 * @author 秋风
 * 
 */
@SuppressLint("InflateParams")
public class PersonInfoCashFragment extends BaseFrament implements OnClickListener,
		TagChangeListener {
	private View mView;

	/** 横向滚动导航 */
	private HorizontalScrollTabStrip id_horizontal_view;

	/** 红线 */
	private View id_line;
	/** 指示器下的红线的参数 */
	private LinearLayout.LayoutParams lineLp;
	/** 默认显示tag数量 */
	private static final int DEFAULT_SHOW_TAG_NUM = 4;

	/** 数据显示ListView */
	private ListView id_cash_listview;
	/**数据列表下方横线*/
	private View id_bottom_line;
	
	/** 适配器 */
	private CashDetailAdapter mAdapter;
	/** tag数据源 */
	private List<String> mTags = null;

	/** 资金明细信息 */
	private List<AccountDetail> mCashDetails = null;
	/** 红包明细 */
	private List<RedPkgItemDetail> mRedPackages = null;

	/** 数据为空的时候显示 */
	private ViewStub id_viewstub_cash_empty;
	
	private ImageView emptyImage;
	private TextView oneText;
	private TextView twoText;
	private TextView button;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (null == mView) {
			mView = inflater.inflate(R.layout.fragment_person_info_cash, null);
			mCashDetails = new ArrayList<AccountDetail>();
			mRedPackages = new ArrayList<RedPkgItemDetail>();
			mAdapter = new CashDetailAdapter();
			initView();
			initLineParams();
			id_line.setLayoutParams(lineLp);
			if (mTags == null || mTags.size() == 0) {
				mTags = new ArrayList<String>();
				mTags.add("全部订单");
				mTags.add("购彩明细");
				mTags.add("派奖明细");
				mTags.add("充值明细");
				mTags.add("提现");
				mTags.add("红包明细");
			}
			id_horizontal_view.setTags(mTags, DEFAULT_SHOW_TAG_NUM, 0);
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		
		return mView;
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_horizontal_view = (HorizontalScrollTabStrip) mView
				.findViewById(R.id.id_horizontal_view);
		id_horizontal_view.setOnTagChangeListener(this);
		id_line = mView.findViewById(R.id.id_line);
		id_cash_listview = (ListView) mView.findViewById(R.id.id_cash_listview);
		id_bottom_line=mView.findViewById(R.id.id_bottom_line);
		id_cash_listview.setAdapter(mAdapter);
	}

	/**
	 * 获取现金模块碎片实例
	 * 
	 * @param tag
	 * @return
	 */
	public static PersonInfoCashFragment newInstance(String tag) {
		PersonInfoCashFragment fragment = new PersonInfoCashFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onPause() {
		super.onPause();
		Controller.getInstance().removeCallback(callBack);
	}

	@Override
	protected void lazyLoad() {

	}

	/**
	 * 红线当前的位置
	 */
	private int mLineLocation_X = 0;

	/**
	 * 初始化指示器“红线”参数
	 */
	private void initLineParams() {
		lineLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lineLp.weight = 0f;
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Service.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		lineLp.width = (int) (width / (DEFAULT_SHOW_TAG_NUM + 0.6));
		lineLp.height = (int) (getResources().getDisplayMetrics().density * 2 + 0.5f);
	}

	/** 修改被激活的tag */
	public void changeStripTag(int position) {
		id_horizontal_view.changeActiveTag(position);
	}

	@Override
	public void changeLine(int location_x, int position, boolean isClick) {
		lineMove(mLineLocation_X,location_x,200);
		mLineLocation_X = location_x;
		mFragmentListener.changeCashTagIndex(position);
	}

	@Override
	public void updateDatas(int position) {
		requestDatas(position);
	}
	
	public void updateDatas(int position,boolean isRefresh) {
		mIsRefresh = isRefresh;
		requestDatas(position);
	}

	/** 获取当前指示器位置 */
	public Map<String, int[]> getLocation() {
		Map<String, int[]> map = new HashMap<String, int[]>();
		int[] wLocationLine = new int[2];
		// 横线x坐标位置
		 id_line.getLocationInWindow(wLocationLine);
		wLocationLine[0] = mLineLocation_X;
		// 当前选项的index
		wLocationLine[1] = mPosition;
		// id_line.getLocationInWindow(wLocationLine);
		View view = ((LinearLayout) id_horizontal_view.getChildAt(0))
				.getChildAt(0);
		int[] wLocationView = new int[2];
		view.getLocationInWindow(wLocationView);
		map.put("line", wLocationLine);
		map.put("view", wLocationView);
		return map;
	}

	/** 记录上一次的请求tag索引 */
	private int mPosition = -1;

	private String orderType = "";

	/**
	 * 获取资金明细信息
	 * 
	 * @param position
	 */
	public void requestDatas(int position) {
		
		
		if (position == mPosition || App.userInfo == null
				|| TextUtils.isEmpty(App.userInfo.getUserId())) {
			return;
		}
		// position："0全部订单", "1购彩明细", "2派奖明细", "3充值明细","4提现","5红包明细";;-1为首次进入
		// if (mPosition != position || position == -1) {
		mPosition = position;
		mIsRefresh = true;
		// 1请求新数据
		// 2
		page = 1;
		if (mCashDetails!=null) 
			mCashDetails.clear();
		else
			mRedPackages = new ArrayList<RedPkgItemDetail>();
		if (mRedPackages!=null) 
			mRedPackages.clear();
		else 
			mRedPackages = new ArrayList<RedPkgItemDetail>();
		
		
		getDatas();
		// }
	}
	private long startTime = 0;
	private long endTime=0;
	private void getDatas(){
		
		//如果显示的不是当前页，不执行请求
		if (GlobalConstants.isShowLeMiFragment) {
			return ;
		}
		if (!NetUtil.isConnected(getActivity())) {
			id_cash_listview.setVisibility(View.GONE);
			initViewStub(true,"10000", 0);
			isCanLoadDatas = false;
			notifyParentHideLoading();
			return;
		}
		endTime = System.currentTimeMillis();
		if (endTime - startTime < 500 || App.userInfo == null
				|| TextUtils.isEmpty(App.userInfo.getUserId())) {
			return;
		}
		startTime = endTime;
		switch (mPosition) {
		case 0:
			// 全部订单
			orderType = "";
			break;
		case 1:
			// 购彩
			orderType = "4";
			break;
		case 2:
			// 派奖
			orderType = "8";
			break;
		case 3:
			// 充值
			orderType = "1";
			break;
		case 4:
			// 提现
			orderType = "2";
			break;
		case 5:
			// 红包
			orderType = "-1";
			break;

		default:
			break;
		}
		if (mFragmentListener!= null) {
			mFragmentListener.showLodingDialog();
		}
		
		if (orderType.equals("-1")) {
			// 请求红包明细
			Controller.getInstance().getRedPkgClearList(
					GlobalConstants.NUM_RED_PKG, App.userInfo.getUserId(), "",
					"", "", "10", page + "", callBack);
		} else {
			// 请求资金明细
			Controller.getInstance().getLoBoAccountDetail(
					GlobalConstants.NUM_ACCOUNTLIST, App.userInfo.getUserId(),
					orderType, 10 + "", page + "", callBack);
		}
	}

	/**
	 * 通知父布局收回刷新
	 */
	private void notifyParentHideLoading() {
		if (mFragmentListener != null) {
			mFragmentListener.hideLodingDialog();
			if (mIsRefresh) {
				mFragmentListener.onRefreshFinish(isCanLoadDatas);
			} else {
				mFragmentListener.onLoadFinish(isCanLoadDatas);
			}

		}
	}

	/** 记录当前页码 */
	private int page = 1;
	private CallBack callBack = new CallBack() {
		public void onSuccess(final Object t) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					RedPkgDetail leMi = (RedPkgDetail) t;
					List<RedPkgItemDetail> list = null;
					list=leMi.tradeList;
					if (leMi.resCode == 0) {
						if (mIsRefresh) {
							mRedPackages.clear();
						}
						mRedPackages.addAll(list);
						if (leMi.tradeList != null
								&& leMi.tradeList.size() == 10) {
							page++;
							isCanLoadDatas = true;
							// 告诉父容器 可以加载数据
						} else {
							isCanLoadDatas = false;
							// 告诉父容器不能加载数据
						}
						// addDatas();
						mAdapter.notifyDataSetChanged();
						ViewUtils.setListViewHeightBasedOnChildren(
								id_cash_listview);
						if (mFragmentListener != null) {
							mFragmentListener.scrollToLocation(0);
						}
					}
					if (null == mRedPackages || mRedPackages.size() == 0) {
						id_cash_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, "0", 1);
					} else {
						id_cash_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, "0", 1);
					}
					if (mFragmentListener != null) {
						if (mIsRefresh) {
							mFragmentListener.onRefreshFinish(isCanLoadDatas);
						} else
							mFragmentListener.onLoadFinish(isCanLoadDatas);
						mFragmentListener.hideLodingDialog();
					}
					GlobalConstants.isRefreshFragment = false;
				}
			});
		};

		public void onFail(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (null == mRedPackages || mRedPackages.size() == 0) {
						id_cash_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, error, 0);
						isCanLoadDatas = false;
					} else {
						id_cash_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, error, 0);
					}
					if (mFragmentListener != null) {
						mFragmentListener.hideLodingDialog();
						if (mIsRefresh) {
							mFragmentListener.onRefreshFinish(isCanLoadDatas);
						} else
							mFragmentListener.onLoadFinish(isCanLoadDatas);
					}
				}
			});
		};

		public void getLoBoAccountDetailSuccess(final AccountDetailList object) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String resCode = object.getResCode();
					List<AccountDetail> list = null;
					if (!TextUtils.isEmpty(resCode) && resCode.equals("0")) {
						list = object.getList();
						if (mIsRefresh) {
							mCashDetails.clear();
						}
						mCashDetails.addAll(list);
						if (object.getList() != null
								&& object.getList().size() == 10) {
							page++;
							isCanLoadDatas = true;
							// 通知父容器可以加载更多数据
						} else {
							// 通知父容器不能加载更多数据
							isCanLoadDatas = false;
						}
//						 addTestDatas();
						mAdapter.notifyDataSetChanged();
						ViewUtils.setListViewHeightBasedOnChildren(
								id_cash_listview, mAdapter);
						getLocation();
						if (mFragmentListener != null) {
							mFragmentListener.scrollToLocation(0);
						}
					}
					if (null == mCashDetails || mCashDetails.size() == 0) {
						id_cash_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, "0", 1);
					} else {
						id_cash_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, "0", 1);
					}
					if (mFragmentListener != null) {
						mFragmentListener.hideLodingDialog();
						if (mIsRefresh) {
							mFragmentListener.onRefreshFinish(isCanLoadDatas);
						} else
							mFragmentListener.onLoadFinish(isCanLoadDatas);
					}
					GlobalConstants.isRefreshFragment = false;
				}
			});
		};

		public void getLoBoAccountDetailFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					if (null == mCashDetails || mCashDetails.size() == 0) {
						id_cash_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, error, 0);
						isCanLoadDatas = false;
					} else {
						id_cash_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, error, 0);
					}
					if (mFragmentListener != null) {
						if (mIsRefresh) {
							mFragmentListener.onRefreshFinish(isCanLoadDatas);
						} else
							mFragmentListener.onLoadFinish(isCanLoadDatas);
						mFragmentListener.hideLodingDialog();
					}
				}
			});
		};
	};

	/** 添加资金明细测试数据 */
	@SuppressWarnings("unused")
	private void addTestDatas() {
		for (int i = 0; i < 15; i++) {
			AccountDetail details = new AccountDetail();
			details.setBalance("99998454.77");
			details.setCreateTime("1476944910635");
			details.setInOut("+");
			details.setOprType("按时打发打发");
			details.setMoney("100");
			details.setStatus("0000");
			details.setCreateTimeSys("2016-11-24 00:12:20");
			details.setDetailMsg("收钱，充值");
			mCashDetails.add(details);
		}

	}

	/** 添加模拟数据 */
	@SuppressWarnings("unused")
	private void addDatas() {
		for (int i = 0; i < 10; i++) {
			RedPkgItemDetail details = new RedPkgItemDetail();
			details.balance = "99998454.77";
			details.tradeTypeMsg = "进账";
			details.inOut = "-";
			details.detailMsg = "快递";
			details.tradeTime = "1476944910635";
			mRedPackages.add(details);
		}
	}

	/**
	 * 通用适配器
	 * 
	 * @author 秋风
	 * 
	 */
	class CashDetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return orderType.equals("-1") ? mRedPackages.size() : mCashDetails
					.size();
		}

		@Override
		public Object getItem(int position) {
			return orderType.equals("-1") ? mCashDetails.get(position)
					: mCashDetails.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder = null;
			if (null == view) {
				view = LayoutInflater.from(mFragmentActivity).inflate(
						R.layout.item_person_cash_details, null);
				holder = new ViewHolder();
				holder.id_time = (TextView) view.findViewById(R.id.id_time);
				holder.id_type = (TextView) view.findViewById(R.id.id_type);
				holder.id_price = (TextView) view.findViewById(R.id.id_price);
				holder.id_balance = (TextView) view
						.findViewById(R.id.id_balance);
				holder.id_remarks = (TextView) view
						.findViewById(R.id.id_remarks);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (orderType.equals("-1")) {
				// 红包明细
				RedPkgItemDetail detail = mRedPackages.get(position);
				holder.id_time.setText("时间:  " + detail.tradeTime);
				holder.id_type.setText("类型:  " + detail.tradeTypeMsg);
				holder.id_price.setText(detail.inOut
							+ detail.tradeMoney + "元");
				if (detail.inOut.equals("-")) {
					holder.id_price.setTextColor(getActivity().getResources()
							.getColor(R.color.color_green_warm));
				} else {
					holder.id_price.setTextColor(getActivity().getResources()
							.getColor(R.color.color_red));
				}
				String remark=detail.detailMsg;
				if (!TextUtils.isEmpty(remark)&&remark.length()>20) {
					int startIndex = remark.indexOf("(");
					int endIndex = remark.lastIndexOf("，");
					if (startIndex != -1 && endIndex != -1) {
						String header = remark.substring(0, startIndex);
						String footer = remark.substring(endIndex,
								remark.length() - 1);
						remark = header + footer;
					}
				}
				
				holder.id_remarks.setText("备注:  " + remark );
				BigDecimal money = new BigDecimal(detail.balance);
				String moneyStr = "";
				if (money.doubleValue() > 100000d) {
					moneyStr = money.divide(new BigDecimal("10000"), 2,
							RoundingMode.DOWN).toPlainString()
							+ "万";
				} else
					moneyStr = money.toPlainString();
				holder.id_balance.setText("余额:  " + moneyStr + "元");
			} else {
				// 资金明细
				AccountDetail detail = mCashDetails.get(position);
				if (detail.getInOut().equals("-")) {
					holder.id_price.setTextColor(getActivity().getResources()
							.getColor(R.color.color_green_warm));
				} else {
					holder.id_price.setTextColor(getActivity().getResources()
							.getColor(R.color.color_red));
				}
				String unit = "元";
				BigDecimal money = new BigDecimal(detail.getBalance());
				String moneyStr = "";
				if (money.doubleValue() > 100000d) {
					moneyStr = money.divide(new BigDecimal("10000"), 2,
							RoundingMode.DOWN).toPlainString()
							+ "万";
				} else
					moneyStr = money.toPlainString();
				holder.id_price.setText((detail.getInOut() + "￥"+detail.getMoney())
						+ "元");
				holder.id_time.setText(detail.getCreateTimeSys());
				holder.id_balance.setText("余额:  " +moneyStr + unit);
				holder.id_remarks.setText("备注:  " +detail.getDetailMsg());
				switch (mPosition) {
				case 0:
				case 1:
				case 2:
					// 全部，购彩，派奖
					holder.id_type.setText("类型:  " +detail.getTypeDes());
					break;
				case 3:
					// 充值
					holder.id_type.setText("类型:  " +getActivity().getResources()
							.getString(R.string.tradetype4));
					break;
				case 4:
					// 提现
					holder.id_type.setText("类型:  " +getActivity().getResources()
							.getString(R.string.tradetype5));

					String status = detail.getStatus();
					// 成功
					if (status.equals("0000")) {
						holder.id_price
								.setText("+" + detail.getAmount() + unit);
					} else if (status.equals("9999")) {
						// 失败
						holder.id_price
								.setText("-" + detail.getAmount() + unit);
						holder.id_price.setTextColor(getActivity()
								.getResources().getColor(R.color.hall_blue));

					} else if (status.equals("2222")) {
						// 等待受理
						holder.id_price
								.setText("+" + detail.getAmount() + unit);
					}
					break;

				default:
					break;
				}

			}
			return view;
		}

		class ViewHolder {
			/** 订单时间 */
			TextView id_time;
			/** 订单类型 */
			TextView id_type;
			/** 操作金额 */
			TextView id_price;
			/** 账户余额 */
			TextView id_balance;
			/** 备注 */
			TextView id_remarks;
		}
	}

	private boolean isFirst = true;

	@Override
	public void onResume() {
		super.onResume();
		
		if (isFirst) {
			isFirst=false;
//			getDatas();
		}else {
			if (GlobalConstants.isRefreshFragment) {
//				if (page > 1) {
//					page--;
//				}
//				getDatas();
				refreshDatas();
//				lineMove(0,mLineLocation_X,0);
			}else {
				lineMove(0,mLineLocation_X,0);
				if ((orderType.equals("-1") && mRedPackages.size()==0)||(!orderType.equals("-1") && mCashDetails.size()==0)) {
					getDatas();
				}
			}
		}
	}
	/**红线移动*/
	private void lineMove(int start,int end,int duration){
		TranslateAnimation animation = new TranslateAnimation(start,
				end, 0f, 0f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(duration);
		animation.setFillAfter(true);
		id_line.startAnimation(animation);
	}
	/**
	 * 滚动视图
	 * 
	 * @param offsetX
	 */
	public void tagScrollBy(int offsetX) {
		id_horizontal_view.tagScrollBy(offsetX);

	}

	/** 初始化ViewStub */
	private void initViewStub(boolean isShow, String errCode, int type) {
		if (null == id_viewstub_cash_empty) {
			id_viewstub_cash_empty = (ViewStub) mView
					.findViewById(R.id.id_viewstub_cash_empty);
			id_viewstub_cash_empty.inflate();
			
			emptyImage = (ImageView) mView.findViewById(R.id.imageView_load_empty);
			
			oneText = (TextView) mView.findViewById(R.id.textView_load_one);
			
			twoText = (TextView) mView.findViewById(R.id.textView_load_two);
			
			button = (TextView) mView.findViewById(R.id.button_load);
			
			button.setOnClickListener(this);
		}
		if (isShow) {
			id_viewstub_cash_empty.setVisibility(View.VISIBLE);
			NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, errCode, type);
		} else {
			id_viewstub_cash_empty.setVisibility(View.GONE);
		}
	}

	/** 是否是刷新数据 */
	private boolean mIsRefresh = true;
	/** 是否可以加载更多 */
	private boolean isCanLoadDatas = true;

	/** 刷新数据 */
	public void refreshDatas() {
		mIsRefresh = true;
		page = 1;
		getDatas();
	}

	/** 加载数据 */
	public void loadDatas() {
		mIsRefresh = false;
		getDatas();
	}

	/** 监听 */
	private OnCashFragmentListener mFragmentListener;

	public void setOnCashFragmentListener(OnCashFragmentListener listener) {
		mFragmentListener = listener;
	}

	/**
	 * 
	 * @author 秋风
	 * 
	 */
	public interface OnCashFragmentListener {
		/***
		 * 通知父类滚动
		 * 
		 * @param y距离顶端的距离
		 */
		void scrollToLocation(int y);

		/**
		 * 切换选择的tag
		 * 
		 * @param position
		 */
		void changeCashTagIndex(int position);

		/***
		 * 信息加载完成
		 * 
		 * @param isCanLoadDatas
		 */
		void onLoadFinish(boolean isCanLoadDatas);

		void onRefreshFinish(boolean isCanLoadDatas);
		void showLodingDialog();
		void hideLodingDialog();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_load:
			switch (Integer.parseInt(NoDataUtils.getmErrCode())) {
			
			case CException.NET_ERROR:
			case CException.IOERROR:
				getDatas();
				break;
			case 0:
				//type 1,投注记录、资金明细、红包明细-立即购彩
				switch (NoDataUtils.getmType()) {
				case 1:
					startActivity(LotteryFootballActivity.class);
					break;
				default:
					break;
				}
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

}
