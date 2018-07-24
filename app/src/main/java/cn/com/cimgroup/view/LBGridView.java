package cn.com.cimgroup.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.LBGridViewAdapter;

/**
 * 数字彩投注gridview
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LBGridView extends GridView implements OnItemClickListener {

	public LBGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public LBGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public LBGridView(Context context) {
		super(context);
	}

	// 适配器
	LBGridViewAdapter mAdapter;

	// 用于数字彩 彩球业务处理
	LBGridviewController mController;

	// 回调接口
	private OnLBItemClickCallBack mCallBack;

	// item回调接口，可进行扩展
	public interface OnLBItemClickCallBack {

		// 单选
		public void onItemClick(AdapterView<?> parent, View view, int position, long id);

		// 多选
		// public void onMultipleItemClick(AdapterView<?> parent,
		// List<Integer> data);
	}

	// 初始化
	private void init(Context context, AttributeSet attrs) {
		setHorizontalSpacing(5);
		setVerticalSpacing(10);
		setSelector(new ColorDrawable(Color.TRANSPARENT));
		setOnItemClickListener(this);

		mController = new LBGridviewController(this);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LBGridView);
		int itemBgResource = a.getResourceId(R.styleable.LBGridView_itemBackgroundResource, R.drawable.selector_bg_redball);
		int itemTvResource = a.getResourceId(R.styleable.LBGridView_itemTextResource, R.color.selector_tv_red_white);
		int itemCount = a.getInt(R.styleable.LBGridView_itemCount, 30);
		boolean itemPlus = a.getBoolean(R.styleable.LBGridView_itemIsPlus, false);
		mAdapter = new LBGridViewAdapter(getContext(), itemBgResource, itemTvResource, itemCount, mController.getSelectData(), itemPlus);
		setAdapter(mAdapter);
		a.recycle();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		// 如果页面添加了回调接口，那么有事件则进行回调
		if (mCallBack != null) {
			mCallBack.onItemClick(parent, view, position, id);
		}
	}

	/**
	 * 获取彩球控制器
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public LBGridviewController getController() {
		return mController;
	}

	/**
	 * 添加回调事件
	 * @Description:
	 * @param callBack
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void setCallBack(OnLBItemClickCallBack callBack) {
		if (callBack != null) {
			this.mCallBack = callBack;
		}
	}

	// 更新数据
	public void notifyChanged() {
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 更新新数据
	 * @Description:
	 * @param data
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void notifySetChanged(ArrayList<Integer> data) {
		mAdapter.setSelectData(data);
	}
	
	/**
	 * 更新遗漏数据
	 * @Description:
	 * @param str
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void notifyBallMissChanged(String str) {
		mAdapter.setMissData(str);
	}
	
	public LBGridViewAdapter getAdapter() {
		return mAdapter;
	}
}
