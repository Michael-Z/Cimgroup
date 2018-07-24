package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.MessageObj;
import cn.com.cimgroup.bean.RedPacketLeMiList;
import cn.com.cimgroup.bean.RedPkgProduct;

/**
 * @Description:
 * @author:www.wenchuang.com
 * @date:16/1/18
 */

@SuppressLint("InflateParams")
public class RedPacketNoticeAdapter<T> extends BaseAdapter {

	public static final String REDPKG = "1";
	public static final String CONVERT = "2";

	private String type = CONVERT;

	protected Context context;
	protected List<T> mList;

	public RedPacketNoticeAdapter(Context context, List data) {
		this.context = context.getApplicationContext();
		this.mList = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= mList.size())
			return null;
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		Object item = mList.get(position);
		if (item != null) {
			if (item instanceof RedPacketLeMiList) {
				RedPacketLeMiList list = (RedPacketLeMiList)item;
				if (list != null && list.betAccount.equals("void_message_wushiqiu")) {
					if (view == null) {
						view = View.inflate(context,R.layout.fragment_message_void_sign, null);
						holder = new ViewHolder();
						holder.tv_no_data_lemi = (TextView) view.findViewById(R.id.tv_no_data_lemi);
						holder.tv_no_data_message = (TextView) view.findViewById(R.id.tv_no_data_message);
						view.setTag(holder);
					} else {
						holder = (ViewHolder) view.getTag();
					}
					holder.tv_no_data_message.setVisibility(View.GONE);
					holder.tv_no_data_lemi.setText("尚无兑换记录");
				}
			} else {
				

//				if (convertView == null) {
//					view = View.inflate(context,R.layout.item_red_packet_notice, null);
//					holder = new ViewHolder();
//					holder.proPic = (ImageView) view.findViewById(R.id.red_packet_image);
//					holder.nameView = (TextView) view.findViewById(R.id.use_name);
//					holder.numView = (TextView) view.findViewById(R.id.textView_notice_num);
//					holder.timeView = (TextView) view.findViewById(R.id.red_packet_mark);
//					holder.effertTimeView = (TextView) view.findViewById(R.id.effective_period_time);
//					holder.tagView = (TextView) view.findViewById(R.id.red_packet_tag);
//					holder.markTextView = (TextView) view.findViewById(R.id.red_packet_mark_text);
//					holder.contentView = (TextView) view.findViewById(R.id.use_scope);
//					view.setTag(holder);
//				} else {
//					view = convertView;
//					holder = (ViewHolder) view.getTag();
//				}
				//兑换记录  已经移到 个人中心--兑换记录
//				if (type == CONVERT && item instanceof Product) {
//					Product product = (Product) item;
//
//					holder.timeView.setText(product.exchangeTime);
//					holder.nameView.setText(product.produceName);
//					holder.markTextView.setText("兑换时间：");
//
//					if (product.productType.equals(Product.HB)) {
//						// 红包;
//						holder.proPic
//								.setImageResource(R.drawable.red_pkg_convert_small);
//					} else if (product.productType.equals(Product.HF)) {
//						// 充值卡;
//						holder.proPic.setImageResource(R.drawable.topup_small);
//						holder.contentView.setText(product.produceDesc);
//						holder.numView.setText("兑换号码:");
//					} else {
//						// 卡券;
//						holder.proPic
//								.setImageResource(R.drawable.voucher_small);
//						holder.contentView.setText(product.produceDesc);
//						holder.numView.setText("卡密:");
//					}
//					if (TextUtils.isEmpty(product.cell)) {
//						holder.effertTimeView.setVisibility(View.GONE);
//					} else {
//						holder.effertTimeView.setText(product.cell);
//						holder.effertTimeView.setVisibility(View.VISIBLE);
//					}
//				} else if (type == REDPKG && item instanceof RedPkgProduct) {
//					RedPkgProduct redPkgProduct = (RedPkgProduct) item;
//
//					holder.nameView.setText(redPkgProduct.redPkgDesc);
//					holder.effertTimeView.setVisibility(View.GONE);
//
//					if (redPkgProduct.redPkgType.equals("1")) {
//						holder.proPic
//								.setImageResource(R.drawable.red_pkg_convert);
//					} else if (redPkgProduct.redPkgType.equals("2")) {
//						holder.proPic.setImageResource(R.drawable.red_pkg_free);
//					} else if (redPkgProduct.redPkgType.equals("3")) {
//						holder.proPic.setImageResource(R.drawable.red_pkg_buy);
//					}
//					if (TextUtils.isEmpty(redPkgProduct.redPkgInvalidTime)) {
//						holder.markTextView.setText("无失效期");
//					} else {
//						holder.markTextView.setText("失效期: "
//								+ redPkgProduct.redPkgInvalidTime);
//					}
//
//					if (!TextUtils.isEmpty(redPkgProduct.redPkgUseDesc)) {
//						holder.numView.setText(redPkgProduct.redPkgUseDesc);
//					}
//
//					if (!TextUtils.isEmpty(redPkgProduct.isInvalid)) {
//						if (redPkgProduct.isInvalid.equals("0")) {
//							holder.tagView.setVisibility(View.INVISIBLE);
//						} else {
//							holder.tagView.setText("即将过期");
//							holder.tagView.setVisibility(View.VISIBLE);
//						}
//					}
//
//					if (!TextUtils.isEmpty(redPkgProduct.isInvalidOrUsed)) {
//						if (redPkgProduct.isInvalidOrUsed.equals("1")) {
//							holder.tagView.setText("已使用");
//							holder.tagView.setVisibility(View.VISIBLE);
//						} else {
//							holder.tagView.setText("已失效");
//							holder.tagView.setVisibility(View.VISIBLE);
//						}
//					}
//				}
				//我的红包
				ViewHolderPackage viewHolderP = null;
				if (view == null) {
					viewHolderP = new ViewHolderPackage(); 
					view =LayoutInflater.from(context).inflate(R.layout.item_exchange_recored_package, null);
					viewHolderP.id_item_money = (TextView) view.findViewById(R.id.id_item_money);
					viewHolderP.id_item_invalid_time = (TextView) view.findViewById(R.id.id_item_invalid_time);
					viewHolderP.id_red_package_state = (ImageView) view.findViewById(R.id.id_red_package_state);
					viewHolderP.id_redpkg_type = (TextView) view.findViewById(R.id.id_redpkg_type);
					viewHolderP.id_pkg_image_type = (ImageView) view.findViewById(R.id.id_pkg_image_type);
					view.setTag(viewHolderP);
				}else {
					viewHolderP = (ViewHolderPackage) view.getTag();
				}
				if (type == REDPKG && item instanceof RedPkgProduct) {
					RedPkgProduct redPkgProduct = (RedPkgProduct) item;
					if (!TextUtils.isEmpty(redPkgProduct.isInvalidOrUsed)) {
						if (redPkgProduct.isInvalidOrUsed.equals("1")) {
							viewHolderP.id_red_package_state.setVisibility(View.VISIBLE);
							viewHolderP.id_red_package_state.setImageResource(R.drawable.icon_isused);
						} else {
							viewHolderP.id_red_package_state.setVisibility(View.VISIBLE);
							viewHolderP.id_red_package_state.setImageResource(R.drawable.icon_isinvalid);
						}
					}else {
						viewHolderP.id_red_package_state.setVisibility(View.GONE);
					}
					String pkgType = "";
					if (redPkgProduct.redPkgType.equals("0")) {
						pkgType = "购买红包";
					}else if (redPkgProduct.redPkgType.equals("1")){
						pkgType = "兑换红包";
					}else if (redPkgProduct.redPkgType.equals("2")){
						pkgType = "赠送红包";
					}
					
					String expTimeH = "";
					if (TextUtils.isEmpty(redPkgProduct.redPkgInvalidTime)||redPkgProduct.redPkgInvalidTime.equals("永久")) {
						expTimeH = "失效期：无失效期";
					}else {
						expTimeH = "失效期："+redPkgProduct.redPkgInvalidTime.split(" ")[0];
					}
					viewHolderP.id_item_invalid_time.setText(expTimeH);
					viewHolderP.id_item_money.setText(String.format("金额：%s元", redPkgProduct.redPkgMianE));
					viewHolderP.id_redpkg_type.setText(pkgType);
				}
				
			
				
				
			}
		}
		return view;
	}
	
	
	/**
	 * 红包
	 * @author 秋风
	 *
	 */
	public class ViewHolderPackage{
		/**兑换金额*/
		TextView id_item_money;
		/**失效时间*/
		TextView id_item_invalid_time;
		/**失效标识*/
		ImageView id_red_package_state;
		/**红包类型*/
		TextView id_redpkg_type;
		/**红包图片*/
		ImageView id_pkg_image_type;
	}

	public class ViewHolder {
		ImageView markView;
		TextView contentView;
		TextView tiemView;
		TextView titleView;
		TextView tv_no_data_lemi;
		TextView tv_no_data_message;
		ImageView proPic;
		TextView nameView;
		TextView numView;
		TextView timeView;
		TextView effertTimeView;
		TextView tagView;
		TextView markTextView;
		TextView tv_no_dat;

	}

	public void addAll(List<T> elem) {
		mList.addAll(elem);
		notifyDataSetChanged();
	}

	public void remove(MessageObj elem) {
		mList.remove(elem);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		mList.remove(index);
		notifyDataSetChanged();
	}

	public void replaceAll(List<T> elem) {
		mList.clear();
		mList.addAll(elem);
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return mList;
	}

}
