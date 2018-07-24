package cn.com.cimgroup.popwindow;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopupWndJx extends PopWnd {

	private int startPos = 0;
	private int endPos = 0;
	private JXNumAdapter adapter;

	public PopupWndJx(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg) {
		super(ctx, tag, mHandlerEx, popBg, null);
		initView(ctx, tag, view);
	}

	private void initView(Context ctx, final int tag, View view) {
		GridView mGVJx = (GridView) view.findViewById(R.id.gridView_jx);
		if (tag == 1) {
			startPos = 5;
			endPos = 16;
		} else {
			startPos = 2;
			endPos = 8;
		}
		adapter = new JXNumAdapter(ctx, startPos, endPos);
		mGVJx.setAdapter(adapter);
		mGVJx.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int item = (Integer) adapter.getItem(position);
				int tv = startPos + item;
				Message msg = new Message();
				if (tag == 1) {
					msg.arg1 = 1;
				} else if (tag == 2) {
					msg.arg1 = 2;
				}
				msg.obj = tv;
				msg.what = getMessageWhat();
				mHandler.sendMessage(msg);
			}
		});
	}

	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_jx;
	}

	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_DLT_JX;
	}

	public class JXNumAdapter extends BaseAdapter {

		private Context context;
		private int count;
		private int bgType;

		public JXNumAdapter(Context ctx, int min, int max) {
			this.context = ctx;
			this.count = max - min;
			this.bgType = min;
		}

		@Override
		public int getCount() {
			return count + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NumHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.item_pop_jx_red_num, null);
				holder = new NumHolder();
				holder.mLLJx = (LinearLayout) convertView.findViewById(R.id.item_jx_ll);
				holder.mTvNum = (TextView) convertView.findViewById(R.id.item_jx_num);
				convertView.setTag(holder);
			} else {
				holder = (NumHolder) convertView.getTag();
			}
			if (bgType == 2) {
				holder.mLLJx.setBackgroundResource(R.drawable.selector_bg_jx_bule_num);
			} else {
				holder.mLLJx.setBackgroundResource(R.drawable.selector_bg_jx_red_num);
			}
			holder.mTvNum.setText((startPos + position) + "");
			return convertView;
		}

		public class NumHolder {
			LinearLayout mLLJx;
			TextView mTvNum;
		}
	}
}
