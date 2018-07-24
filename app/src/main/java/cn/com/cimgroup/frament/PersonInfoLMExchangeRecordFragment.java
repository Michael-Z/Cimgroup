package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.bean.Product;
import cn.com.cimgroup.bean.RedPacketLeMiList;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.xutils.NetUtil;

/**
 * 个人中心碎片--兑换记录
 * 
 * @author 秋风
 * 
 */
@SuppressLint("InflateParams")
public class PersonInfoLMExchangeRecordFragment extends BaseFrament implements
		OnClickListener {

	private View mView;
	/** 数据显示集合 */
	private ListView id_lemi_convert_listview;

	/** 信息下方横线 */
	private View id_bottom_line;
	/** 当前信息页号 */
	private int page = 1;
//	/**旧版适配器*/
//	private ConvertAdapter mAdapter;
	/**新版适配器*/
	private ConvertNewAdapter mAdapter;
	
	private List<Product> mList = null;

	/** 空的时候显示ViewStub */
	private ViewStub id_empty_viewstub;
	
	private ImageView emptyImage;
	private TextView oneText;
	private TextView twoText;
	private TextView button;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(
					R.layout.fragment_person_info_lemi_convert, null);
			mList = new ArrayList<Product>();
			initView();
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		}

		return mView;
	}

	/** 显示娱乐场跳转 */
	private void initViewStub(boolean isShow, String errCode, int type) {
		if (null == id_empty_viewstub) {
			id_empty_viewstub = (ViewStub) mView
					.findViewById(R.id.id_empty_viewstub);
			id_empty_viewstub.inflate();
//			mView.findViewById(R.id.id_detail_jump_game).setOnClickListener(
//					this);
			
			emptyImage = (ImageView) mView.findViewById(R.id.imageView_load_empty);
			
			oneText = (TextView) mView.findViewById(R.id.textView_load_one);
			
			twoText = (TextView) mView.findViewById(R.id.textView_load_two);
			
			button = (TextView) mView.findViewById(R.id.button_load);
			
			button.setOnClickListener(this);
		}
		if (isShow) {
			id_empty_viewstub.setVisibility(View.VISIBLE);
			NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, errCode, type);
		} else {
			id_empty_viewstub.setVisibility(View.GONE);
		}
		
		
	}

	/**
	 * 绑定视图组件
	 */
	private void initView() {
		id_lemi_convert_listview = (ListView) mView
				.findViewById(R.id.id_lemi_convert_listview);
		id_bottom_line = mView.findViewById(R.id.id_bottom_line);
//		mAdapter = new ConvertAdapter();
		mAdapter = new ConvertNewAdapter();
		id_lemi_convert_listview.setAdapter(mAdapter);
	}

	/**
	 * 获取当前碎片实例
	 * 
	 * @param tag
	 * @return
	 */
	public static PersonInfoLMExchangeRecordFragment newInstance(String tag) {
		PersonInfoLMExchangeRecordFragment fragment = new PersonInfoLMExchangeRecordFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	protected void lazyLoad() {

	}

	private CallBack callBack = new CallBack() {
		public void onSuccess(final Object t) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (null != t) {
						RedPacketLeMiList o = (RedPacketLeMiList) t;
						List<Product> productList = null;
						String total = o.total;
						if (o.resCode.equals("0")) {
							productList = o.productList;
							if (isRefresh) {
								mList.clear();
							}
							mList.addAll(productList);
							if (productList != null && productList.size() == 10) {
								page++;
								// 通知父容器 可以加载更多数据
								isCanLoadDatas = true;
							} else {
								// 通知父容器 不能加载数据
								isCanLoadDatas = false;
							}
							// addDatas();
							// mAdapter.notifyDataSetChanged();
							// ViewUtils.setListViewHeightBasedOnChildren(
							// id_lemi_convert_listview, mAdapter);
						} else {
							isCanLoadDatas = false;
//							ToastUtil.shortToast(getActivity(), o.resMsg);
						}
						mAdapter.notifyDataSetChanged();
						if (mList == null || mList.size() == 0) {
							id_lemi_convert_listview.setVisibility(View.GONE);
							id_bottom_line.setVisibility(View.GONE);
							initViewStub(true, "0", 2);
						} else {
							ViewUtils.setListViewHeightBasedOnChildren(
									id_lemi_convert_listview, mAdapter);
							id_lemi_convert_listview
									.setVisibility(View.VISIBLE);
							id_bottom_line.setVisibility(View.VISIBLE);
							initViewStub(false, "0", 2);
						}
						notifyParentHideLoading();
					}
					GlobalConstants.isRefreshFragment = false;
				}
			});

		};

		public void onFail(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mList == null || mList.size() == 0) {
						id_lemi_convert_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, error, 0);
						isCanLoadDatas = false;
					} else {
						ViewUtils.setListViewHeightBasedOnChildren(
								id_lemi_convert_listview, mAdapter);
						id_lemi_convert_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, error, 0);
					}
					notifyParentHideLoading();
				}
			});
		};
	};
	
	/**
	 * 通知父布局收回刷新
	 */
	private void notifyParentHideLoading() {
		if (mFragmentListener != null) {
			mFragmentListener.hideLodingDialog();
			if (isRefresh) {
				mFragmentListener.onRefreshFinish(isCanLoadDatas);
			} else {
				mFragmentListener.onLoadFinish(isCanLoadDatas);
			}

		}
	}

	/** 添加测试数据 */
	@SuppressWarnings("unused")
	private void addDatas() {
		for (int i = 0; i < 10; i++) {
			Product product = new Product();
			product.productName = "测试数据:" + i;
			product.exchangeTime = "2016-11-" + (15 + i);
			if (i % 5 == 1) {
				product.productType = Product.HB;
			} else if (i % 5 == 2) {
				product.productType = Product.HF;
			} else if (i % 5 == 3) {
				product.productType = Product.REDPKG;
			} else {
				product.productType = Product.JDK;
			}
			product.produceDesc = product.productType + "兑换";
			product.cell = "sssss";
			mList.add(product);
		}
	}
	//2.7期使用adapter
	private class ConvertNewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public int getItemViewType(int position) {
			return mList.get(position).productType.equals(Product.HB)?0:1;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolderPackage viewHolderP = null;
			ViewHolderCard viewHolderC = null;
			Product product = mList.get(position);
			switch (getItemViewType(position)) {
			case 0:
				//红包
				if (view == null) {
					viewHolderP = new ViewHolderPackage(); 
					view =LayoutInflater.from(getActivity()).inflate(R.layout.item_exchange_recored_package, null);
					viewHolderP.id_item_exchange_time = (TextView) view.findViewById(R.id.id_item_exchange_time);
					viewHolderP.id_item_money = (TextView) view.findViewById(R.id.id_item_money);
					viewHolderP.id_item_invalid_time = (TextView) view.findViewById(R.id.id_item_invalid_time);
					view.setTag(viewHolderP);
				}else {
					viewHolderP = (ViewHolderPackage) view.getTag();
				}
				viewHolderP.id_item_exchange_time.setText(product.exchangeTime);
				viewHolderP.id_item_money.setText(String.format("金额：%s元", product.productMoney));
				
				String expTimeH = "";
				if (!TextUtils.isEmpty(product.expTime)) {
					expTimeH = "失效期："+product.expTime.split(" ")[0];
				}
				viewHolderP.id_item_invalid_time.setText(expTimeH);
				break;
			case 1:
				//兑换卡
				if (view ==null) {
					viewHolderC = new ViewHolderCard();
					view = LayoutInflater.from(getActivity()).inflate(R.layout.item_exchange_recored_card, null);
					viewHolderC.id_item_image = (ImageView) view.findViewById(R.id.id_item_image);
					viewHolderC.id_item_type = (TextView) view.findViewById(R.id.id_item_type);
					viewHolderC.id_item_state = (TextView) view.findViewById(R.id.id_item_state);
					viewHolderC.id_item_money_card = (TextView) view.findViewById(R.id.id_item_money_card);
					viewHolderC.id_item_invalid_time_card = (TextView) view.findViewById(R.id.id_item_invalid_time_card);
					viewHolderC.id_item_remark = (TextView) view.findViewById(R.id.id_item_remark);
					viewHolderC.id_item_image_name = (TextView) view.findViewById(R.id.id_item_image_name);
					viewHolderC.id_item_image_money = (TextView) view.findViewById(R.id.id_item_image_money);
					viewHolderC.id_item_image_huafei = (TextView) view.findViewById(R.id.id_item_image_huafei);
					viewHolderC.id_item_invalid_time_card = (TextView) view.findViewById(R.id.id_item_invalid_time_card);
					view.setTag(viewHolderC);
				}else {
					viewHolderC = (ViewHolderCard) view.getTag();
				}
				String desc = "";
				Spanned faceValue = Html.fromHtml("<big><big>"+product.productMoney+"</big></big> 元");
				String state ="";//充值卡表示状态；其他卡表示时间
				if (product.productType.equals(Product.HF)) {
					// 充值卡;
					viewHolderC.id_item_state.setText("");
					viewHolderC.id_item_image.setImageResource(R.drawable.lemi_car_hf);
					desc = String.format("充值号码：%s", product.produceDesc);
					viewHolderC.id_item_image_name.setVisibility(View.GONE);
					viewHolderC.id_item_image_money.setVisibility(View.GONE);
					viewHolderC.id_item_invalid_time_card.setVisibility(View.GONE);
					viewHolderC.id_item_image_huafei.setVisibility(View.VISIBLE);
					
					viewHolderC.id_item_image_huafei.setText(faceValue);
					state = TextUtils.isEmpty(product.exStatus) ? "0"
							: product.exStatus;
					if (state.equals("0")) {
						state = "充值成功";
					}else if (state.equals("1")) {
						state = "正在充值";
					}else if (state.equals("2")) {
						state = "充值失败";
					}
					
				} else {
					state = product.exchangeTime;
					Spanned faceName = null;
					viewHolderC.id_item_invalid_time_card.setVisibility(View.VISIBLE);
					viewHolderC.id_item_image_name.setVisibility(View.VISIBLE);
					viewHolderC.id_item_image_money.setVisibility(View.VISIBLE);
					viewHolderC.id_item_image_huafei.setVisibility(View.GONE);
					// 卡券;
					viewHolderC.id_item_image.setImageResource(R.drawable.lemi_car_convert);
					String descStr = product.produceDesc==null?"":product.produceDesc;
					desc = String.format("卡密：%s", descStr);
					if (product.productType.equals(Product.JDK)) {
						//京东卡
						faceName = Html.fromHtml("<big><big>京东</big></big> 购物券");
					} else if (product.productType.equals(Product.GMK)) {
						//国美券
						faceName = Html.fromHtml("<big><big>国美</big></big> 购物券");
					} else if (product.productType.equals(Product.SNK)) {
						//苏宁卡
						faceName = Html.fromHtml("<big><big>苏宁</big></big> 购物券");
					}
					viewHolderC.id_item_image_money.setText(faceValue);
					viewHolderC.id_item_image_name.setText(faceName);
					String expTimeC = "";
					if (!TextUtils.isEmpty(product.expTime)) {
						expTimeC = "失效期："+product.expTime.split(" ")[0];
					}
					Log.e("qiufeng", "失效时间："+expTimeC);
					viewHolderC.id_item_invalid_time_card.setText(expTimeC);
				}
				viewHolderC.id_item_money_card.setText(String.format("金额：%s元", product.productMoney));
				viewHolderC.id_item_state.setText(state);
				viewHolderC.id_item_type.setText(product.produceName);
				viewHolderC.id_item_remark.setText(desc);
				
				break;
			}
			
			return view;
		}
		
		/**
		 * 充值卡、兑换卡
		 * @author 秋风
		 *
		 */
		public class ViewHolderCard{
			/**卡类型图片*/
			ImageView id_item_image;
			/**卡类型名称*/
			TextView id_item_type;
			/**卡片兑换时间*/
			TextView id_item_state;
			/**兑换金额*/
			TextView id_item_money_card;
			/**失效时间*/
			TextView id_item_invalid_time_card;
			/**说明*/
			TextView id_item_remark;
			/**购卡图片类型*/
			TextView id_item_image_name;
			/**购卡图片面值*/
			TextView id_item_image_money;
			/**充值卡图片面值*/
			TextView id_item_image_huafei;
		}
		/**
		 * 红包
		 * @author 秋风
		 *
		 */
		public class ViewHolderPackage{
			/**兑换时间*/
			TextView id_item_exchange_time;
			/**兑换金额*/
			TextView id_item_money;
			/**失效时间*/
			TextView id_item_invalid_time;
			
		}
		
		
	}
//	//2.7期之前使用adapter
//	private class ConvertAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			return mList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return mList.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View view, ViewGroup parent) {
//			ViewHolder holder;
//			if (view == null) {
//				view = View.inflate(mFragmentActivity,
//						R.layout.item_red_packet_notice, null);
//				holder = new ViewHolder();
//				holder.proPic = (ImageView) view
//						.findViewById(R.id.red_packet_image);
//				holder.nameView = (TextView) view.findViewById(R.id.use_name);
//				holder.numView = (TextView) view
//						.findViewById(R.id.textView_notice_num);
//				holder.timeView = (TextView) view
//						.findViewById(R.id.red_packet_mark);
//				holder.effertTimeView = (TextView) view
//						.findViewById(R.id.effective_period_time);
//				holder.tagView = (TextView) view
//						.findViewById(R.id.red_packet_tag);
//				holder.markTextView = (TextView) view
//						.findViewById(R.id.red_packet_mark_text);
//				holder.contentView = (TextView) view
//						.findViewById(R.id.use_scope);
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			Product product = mList.get(position);
//			holder.timeView.setText(product.exchangeTime);
//			holder.nameView.setText(product.produceName);
//			holder.markTextView.setText("兑换时间：");
//
//			if (product.productType.equals(Product.HB)) {
//				// 红包;
//				holder.proPic
//						.setImageResource(R.drawable.red_pkg_convert_small);
//			} else if (product.productType.equals(Product.HF)) {
//				// 充值卡;
//				holder.proPic.setImageResource(R.drawable.topup_small);
//				holder.contentView.setText(product.produceDesc);
//				holder.numView.setText("兑换号码:");
//			} else {
//				// 卡券;
//				holder.proPic.setImageResource(R.drawable.voucher_small);
//				holder.contentView.setText(product.produceDesc);
//				holder.numView.setText("卡密:");
//			}
//			if (TextUtils.isEmpty(product.cell)) {
//				holder.effertTimeView.setVisibility(View.GONE);
//			} else {
//				holder.effertTimeView.setText(product.cell);
//				holder.effertTimeView.setVisibility(View.VISIBLE);
//			}
//			return view;
//		}
//
//		public class ViewHolder {
//			TextView contentView;
//			ImageView proPic;
//			TextView nameView;
//			TextView numView;
//			TextView timeView;
//			TextView effertTimeView;
//			@SuppressWarnings("unused")
//			TextView tagView;
//			TextView markTextView;
//
//		}
//	}
	private boolean isFirst=true;
	@Override
	public void onResume() {
		super.onResume();
		if (isFirst) {
			isFirst=false;
			getDatas();
		}else {
			if (GlobalConstants.isRefreshFragment) {
				
//				if (page > 1) {
//					page--;
//				}
//				getDatas();
				refreshDatas();
			}else {
				if (mList ==null&&mList.size() == 0) {
					refreshDatas();
				}
			}
		}
		
	}

	/** 获取数据 */
	private void getDatas() {
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId()) && GlobalConstants.personTagIndex == 2) {
			if (!NetUtil.isConnected(getActivity())) {
				initViewStub(true,"10000", 0);
				isCanLoadDatas = false;
				notifyParentHideLoading();
				return;
			}
			if (mFragmentListener != null) {
				mFragmentListener.showLodingDialog();
			}
			Controller.getInstance().getConvertNotesList(
					GlobalConstants.NUM_CONVERT_LIST, App.userInfo.getUserId(),
					page + "", "10", callBack);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_load:
			
			switch (Integer.parseInt(NoDataUtils.getmErrCode())) {
			
			case CException.NET_ERROR:
			case CException.IOERROR:
				getDatas();
				break;
			case 0:
				//2,兑换记录、我的红包-前往商城
				switch (NoDataUtils.getmType()) {
				case 2:
					Intent convertIntent = new Intent(getActivity(), MessageCenterActivity.class);
					convertIntent.putExtra(MessageCenterActivity.TYPE,MessageCenterActivity.LEMICONVERT);
					getActivity().startActivity(convertIntent);
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

	/** 是否是刷新数据 */
	private boolean isRefresh = true;
	/** 是否可以加载更多 */
	private boolean isCanLoadDatas = true;

	/** 刷新数据 */
	public void refreshDatas() {
		isRefresh = true;
		page = 1;
		getDatas();
	}

	/** 加载数据 */
	public void loadDatas() {
		isRefresh = false;
		getDatas();
	}

	private OnPersonInfoLEMIExchangeListener mFragmentListener;

	public void setOnPersonInfoLEMIExchangeListener(
			OnPersonInfoLEMIExchangeListener listener) {
		mFragmentListener = listener;
	}

	public interface OnPersonInfoLEMIExchangeListener {
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

}
