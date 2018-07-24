package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 通用的Adapter
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月2日
 */
public abstract class SimpleBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> mList;

	public SimpleBaseAdapter(Context context, List<T> data) {
		this.context = context;
		this.mList = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
	}

	public void setDatas(List<T> list) {
		mList = list;
		notifyDataSetChanged();
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

	/**
	 * 该方法需要子类实现，需要返回item布局的resource id
	 * 
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	public abstract int getItemResource();

	/**
	 * 使用该getItemView方法替换原来的getView方法，需要子类实现
	 * 
	 * @Description:
	 * @param position
	 * @param convertView
	 * @param holder
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	public abstract View getItemView(int position, View convertView,
			ViewHolder holder);

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = View.inflate(context, getItemResource(), null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return getItemView(position, convertView, holder);
	}

	public class ViewHolder {
		private SparseArray<View> views = new SparseArray<View>();
		private View convertView;

		public ViewHolder(View convertView) {
			this.convertView = convertView;
		}

		@SuppressWarnings({ "unchecked", "hiding" })
		public <T extends View> T getView(int resId) {
			View v = views.get(resId);
			if (null == v) {
				v = convertView.findViewById(resId);
				views.put(resId, v);
			}
			return (T) v;
		}
	}

	public void addAll(List<T> elem) {
		mList.addAll(elem);
		notifyDataSetChanged();
	}

	public void remove(T elem) {
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
