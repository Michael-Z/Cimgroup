package cn.com.cimgroup.util;

import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 视图组件帮助类
 * 
 * @author 秋风
 * 
 */
public class ViewUtils {
	// 由于OnCreate里面拿不到header的高度所以需要手动计算
	/**
	 * 测量控件
	 * 
	 * @param childView
	 */
	public static void measureView(View childView) {
		ViewGroup.LayoutParams p = childView.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int height = p.height;
		int childHeightSpec;
		if (height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		childView.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 重新计算ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	/**
	 * 重新计算ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildrenSpecal(ListView listView,int addHeight) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))+addHeight;
		listView.setLayoutParams(params);
	}
	
	/**
	 * 重新计算ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView,BaseAdapter listAdapter) {
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 根据现实数目重新计算ListView的高度
	 * 
	 * @param listView
	 * @param showCount
	 */
	public static void setListViewHeightByShouCount(ListView listView,
			int showCount) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		showCount = showCount > listAdapter.getCount() ? listAdapter.getCount()
				: showCount;
		int totalHeight = 0;
		for (int i = 0; i < showCount; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (showCount - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 计算GridView 的高度
	 * 
	 * @param listView
	 * @param col
	 */
	public static void setGridViewHeightBasedOnChildren(GridView gridView,
			int col) {
		// 获取listview的adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		// listView.getNumColumns();
		int totalHeight = 0;
		int itemHeight = 0;
		if (listAdapter != null) {
			int size = listAdapter.getCount();
			for (int i = 0; i < size; i++) {
				// 获取gridView的每一个item
				View listItem = listAdapter.getView(i, null, gridView);
				listItem.measure(0, 0);
				// measureView(listItem);
				// 获取item的高度和
				itemHeight = Math.max(itemHeight, listItem.getMeasuredHeight());
				if (i % col == col - 1 || i == size - 1) {
					totalHeight += itemHeight;
					itemHeight = 0;
				}
			}
		}

		// 获取gridView的布局参数
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		// 设置高度
		params.height = totalHeight;
		// 设置margin
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		// 设置参数
		gridView.setLayoutParams(params);
	}

	/**
	 * 获取控件在屏幕中的位置
	 * 
	 * @param view
	 */
	public static Rect getViewLocation(View view) {
		Rect rect = new Rect();
		int location[] = new int[2];
		view.getLocationOnScreen(location); // 获取屏幕中位置
		rect.left = location[0];
		rect.top = location[1];
		rect.right = rect.left + view.getWidth();
		rect.bottom = rect.top + view.getHeight();
		// Log.e("qiufeng",
		// "left:"+rect.left+"__top:"+rect.top+"___right:"+rect.right+"___bottom:"+rect.bottom);
		return rect;
	}

}
