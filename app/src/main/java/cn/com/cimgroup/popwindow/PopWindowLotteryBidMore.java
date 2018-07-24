package cn.com.cimgroup.popwindow;
import java.util.Arrays;
import java.util.List;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.SimpleBaseAdapter;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 足彩更多选择 popwindow
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-3-6
 */
public class PopWindowLotteryBidMore extends PopWnd implements OnItemClickListener{

	/**足球/篮球的标志位    1-足球  2-篮球**/
	private int flag = 0;
	
	/**
	 * 足球/篮球投注右上角更多选项的按钮
	 * @param ctx  上下文
	 * @param onPopWindowItemListener  itemClick的点击事件监听（回调）
	 * @param flag 足球/篮球的标志位    1-足球  2-篮球
	 */
	public PopWindowLotteryBidMore(Context ctx,OnPopWindowItemListener onPopWindowItemListener,int flag) {
		super(ctx, 0, null, null, null);
		this.flag = flag;
		initView(view);
		this.mItemListener=onPopWindowItemListener;
	}

	public interface OnPopWindowItemListener {

		public void onPopWindowItemClick(MoreSelectType type);
	}

	@Override
	protected boolean isWrapContent() {
		return true;
	}
	
	// 回调监听
	private OnPopWindowItemListener mItemListener;
	private ListView mPopWindowListView;
	private PopWindowLotteryBidMoreAdapter mMoreAdapter;

	// 数据结果集
	private List<Integer> mMoreTextId_basktball =Arrays.asList(new Integer[]{ R.string.popwindow_lotterybid_more0, R.string.popwindow_lotterybid_more2, R.string.popwindow_lotterybid_more3 });
	private List<Integer> mMoreTextId_football =Arrays.asList(new Integer[]{ R.string.popwindow_lotterybid_more0, R.string.popwindow_lotterybid_more1, R.string.popwindow_lotterybid_more3 });

	// 结果标识
	public enum MoreSelectType {
		// 我的投注
		TZJL,
		// 比分直播
		BFZB,
		// 开奖信息
		KJXX,
		// 玩法说明
		WFSM
	}

	public PopWindowLotteryBidMore(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
		initView(view);
	}

	private void initView(View view) {
		mPopWindowListView = (ListView) view.findViewById(R.id.lvView_popwindow_lotterybidmore);
		// 根据传入的参数 确定右上角的popwindow下拉内容
		if (flag == 1) {
			mMoreAdapter = new PopWindowLotteryBidMoreAdapter(context,
					mMoreTextId_football);
		} else if (flag == 2) {
			mMoreAdapter = new PopWindowLotteryBidMoreAdapter(context,
					mMoreTextId_basktball);
		}
		mPopWindowListView.setAdapter(mMoreAdapter);
		mPopWindowListView.setOnItemClickListener(this);
	}

	@Override
	public int getLayoutResource() {
		return R.layout.layout_popwindow_lotterybidmore;
	}

	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_PLAY_MENU;
	}

	class PopWindowLotteryBidMoreAdapter extends SimpleBaseAdapter<Integer> {

		
		public PopWindowLotteryBidMoreAdapter(Context context, List<Integer> mMoreTextId) {
			super(context, mMoreTextId);
		}

		@Override
		public int getItemResource() {
			return R.layout.item_lotterybid_more_popwindow;
		}

		@Override
		public View getItemView(int position, View convertView, ViewHolder holder) {
			TextView itemView=(TextView) convertView.findViewById(R.id.txtView_lotterybid_more_popwindow);
			itemView.setText(context.getString(mList.get(position)));
			return convertView;
		}

		

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		dissmiss();
		if(mItemListener != null){
			switch (flag) {
			case 1:
				if (position > 1) {
					//足球的点击事件控制 修改emun涉及较多，所以这里简单加了控制
					mItemListener.onPopWindowItemClick(MoreSelectType.values()[position+1]);
				} else {
					mItemListener.onPopWindowItemClick(MoreSelectType.values()[position]);
				}
				break;
			case 2:
				if (position == 1 || position == 2) {
					mItemListener.onPopWindowItemClick(MoreSelectType.values()[position+1]);
				}else {
					mItemListener.onPopWindowItemClick(MoreSelectType.values()[position]);
				}
				break;
			default:
				break;
			}
			
		}
	}

}
