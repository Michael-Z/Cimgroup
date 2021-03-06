package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AbstractSpinerAdapter<T> extends BaseAdapter {

	public static interface IOnItemSelectListener {
		public void onItemClick(View view, int pos);
	};

	private Context mContext;
	private List<T> mObjects = new ArrayList<T>();
	private int mSelectItem = 0;

	public AbstractSpinerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<T> objects, int selIndex) {
		mObjects = objects;
		if (selIndex < 0) {
			selIndex = 0;
		}
		if (selIndex >= mObjects.size()) {
			selIndex = mObjects.size() - 1;
		}

		mSelectItem = selIndex;
	}

	private void init(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {

		return mObjects.size();
	}

	@Override
	public Object getItem(int pos) {
		return mObjects.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	// @Override
	// public View getView(int pos, View convertView, ViewGroup arg2) {
	// ViewHolder viewHolder;
	//
	// if (convertView == null) {
	// convertView = mInflater.inflate(R.layout.item_card_spinner, null);
	// viewHolder = new ViewHolder();
	// viewHolder.mTextView = (TextView)
	// convertView.findViewById(R.id.item_card_text);
	// convertView.setTag(viewHolder);
	// } else {
	// viewHolder = (ViewHolder) convertView.getTag();
	// }
	//
	//
	// Object item = getItem(pos);
	// viewHolder.mTextView.setText(item.toString());
	//
	// return convertView;
	// }

	public static class ViewHolder {
		public TextView mTextView;
		public View id_top_line;
	}

}
