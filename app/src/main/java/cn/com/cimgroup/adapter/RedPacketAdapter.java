package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Product;
import cn.com.cimgroup.frament.GridFragment;

/**
 * Created by small on 16/1/10.
 */
public class RedPacketAdapter extends SimpleBaseAdapter {
	public RedPacketAdapter(Context context, List data) {
		super(context, data);
	}

	public RedPacketAdapter(Context context, List data, ClickListener listener) {
		super(context, data);
		this.listener = listener;
	}

	public void setType(int type) {
		this.type = type;
	}

	private ClickListener listener;
	private int type;

	public interface ClickListener {
		/**
		 * 除余额不足外的点击事件回调
		 * @param position
		 */
		void onClick(int position, int type);
//		/**
//		 * 
//		 * 余额不足的点击事件回调
//		 * @param position
//		 */
//		void goToRecharge(int position);
	}

	@Override
	public int getItemResource() {
		return R.layout.item_redpacket;
	}

	@Override
	public View getItemView(final int position, View convertView,
			ViewHolder holder) {
		TextView gotoBuy = (TextView) holder.getView(R.id.gotu_buy);
		// TextView explainTextView = (TextView)
		// holder.getView(R.id.red_packet_explain);
		TextView countView = (TextView) holder.getView(R.id.lemi_count);
		ImageView pic = (ImageView) holder.getView(R.id.red_packet_img);
		TextView preferential = (TextView) holder
				.getView(R.id.product_preferential);
		TextView moneyView = (TextView) holder.getView(R.id.product_money);
		TextView lemiView = (TextView) holder.getView(R.id.lemi_money);
		TextView lemiTeleView = (TextView) holder
				.getView(R.id.lemi_convert_tele);
		TextView lemiTickTypeView = (TextView) holder
				.getView(R.id.lemi_convert_ticket_type);
		TextView lemiTickCountView = (TextView) holder
				.getView(R.id.lemi_convert_tick_count);
//		TextView productAmount = (TextView) holder.getView(R.id.product_amount);

		final Object item = mList.get(position);
		String unit = "乐米";
		//根据mList中的内容  确定按钮的显示内容和颜色
		if (item != null && item instanceof Product) {
			Product product = (Product) item;
			gotoBuy.setBackgroundColor(context.getResources().getColor(
					R.color.color_gray_red_pkt));
			String price = TextUtils.isEmpty(product.productAmount) ? "0"
					: product.productAmount;
			String gotuBuyText = "立即兑换";
			moneyView.setText("");
			// 开始判断 显示购买单位
			// 填充红包右下角的 钱数+购买单位 如5400乐米--5元红包
			switch (type) {
			case GridFragment.BUYREDPACK:
				preferential.setVisibility(View.GONE);
				// 现金购买红包;乐米兑换红包;
				pic.setImageResource(R.drawable.red_packet_defult);
				moneyView
						.setText(GlobalTools.string2Span(product.productMoney));
				// preferential.setText("优惠" + product.productPreferential +
				// "元");
				// preferential.setVisibility(View.GONE);
				gotuBuyText = "立即购买";
				unit = "元";
				break;
			case GridFragment.BUYLEMI:
				// 现金购买乐米;
				preferential.setVisibility(View.GONE);
				lemiView.setText(product.productMoney + product.unit);
				pic.setImageResource(R.drawable.item_lemi_grid);
				gotuBuyText = "立即购买";
				unit = "元";
				
				break;
			case GridFragment.LEMIRED:
				// 乐米兑换红包;
				preferential.setVisibility(View.VISIBLE);
				unit = "乐米";
				pic.setImageResource(R.drawable.red_packet_defult);
				moneyView
						.setText(GlobalTools.string2Span(product.productMoney));
				gotuBuyText = "立即兑换";
				break;
			case GridFragment.LEMITELE:
				// 充值卡;
				preferential.setVisibility(View.VISIBLE);
				pic.setImageResource(R.drawable.lemi_conver_tele);
				lemiTeleView.setText(GlobalTools
						.string2Span(product.productMoney));
				unit = "乐米";
				break;
			case GridFragment.LEMIJD:
			case GridFragment.LEMIGM:
			case GridFragment.LEMISN:
				// 购物券;
				preferential.setVisibility(View.VISIBLE);
				lemiTickCountView.setText(GlobalTools
						.string2Span(product.productMoney));
				pic.setImageResource(R.drawable.lemi_convert_ticket);
				unit = "乐米";
				lemiTickTypeView.setText(product.productName);
				gotuBuyText = "立即兑换";
				break;
			default:
				break;
			}
			countView.setText(product.productSaleMoney + unit);

			// 正式服务器上传之后 取消该if-- else 的判断，取消 else 方法体
			// if (!TextUtils.isEmpty(product.sale)) {
			final String sale = product.sale;
			//根据返回值判断购买按钮的状态和颜色
			if (sale.equals("0")) {
				if (type == GridFragment.BUYREDPACK) {
					gotuBuyText = "暂停购买";
					gotoBuy.setBackgroundColor(context.getResources().getColor(R.color.color_gray_red_pkt));
				}else {
					//按钮绿色 可点击
					gotoBuy.setBackgroundColor(context.getResources().getColor(
							R.color.color_green_red_pkt));
					gotoBuy.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (listener != null)
								listener.onClick(position,Integer.parseInt(sale));
						}
					});
				}
				
			} else if (sale.equals("1")) {
				//2.5期修改内容    取消余额不足的显示 改为弹出对话框 弹出充值提示
//				gotuBuyText = "余额不足";
				if (type == GridFragment.BUYREDPACK) {
					gotuBuyText = "暂停购买";
					gotoBuy.setBackgroundColor(context.getResources().getColor(R.color.color_gray_red_pkt));
				}else {
					gotoBuy.setBackgroundColor(context.getResources().getColor(
						R.color.color_green_red_pkt));
					gotoBuy.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (listener != null) {
//							listener.goToRecharge(position);
							listener.onClick(position, Integer.parseInt(sale));
						}
						}
					});
				}
				
			} else if (sale.equals("2")) {
				if (type == GridFragment.LEMIRED) {
					gotuBuyText = "暂停兑换";
					gotoBuy.setBackgroundColor(context.getResources().getColor(R.color.color_gray_red_pkt));
				}else {
					gotuBuyText = "已售完";
				}
			} else if (sale.equals("3")) {

				if (type == GridFragment.BUYREDPACK
						|| type == GridFragment.BUYLEMI)
					gotuBuyText = "暂停购买";
				else
					gotuBuyText = "暂停兑换";
			}

			// preferential.setText("剩余" + price+"个");
			preferential.setText(Html.fromHtml("剩<font color='"
					+ context.getResources().getColor(R.color.hall_yellow)
					+ "'>" + price + "</font>个"));
			gotoBuy.setText(gotuBuyText);

		}
		return convertView;
	}

}
