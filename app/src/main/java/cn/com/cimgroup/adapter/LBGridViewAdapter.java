package cn.com.cimgroup.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;

/**
 * 投注数字彩 彩球adapter
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class LBGridViewAdapter extends BaseAdapter {

	// 选中的彩球索引集合
	private ArrayList<Integer> mSelectData;
	private Context mContext;
	// 显示彩球数量
	private int mCount;
	// 默认红色
	private int mBackground;
	private int mTvBackground;
	private boolean isNumPlus = false;// item显示是否加1, true 为不加，false 为加 1
	private String mMissData = "";// 遗漏数据

	public LBGridViewAdapter(Context context, int itemBgResource, int itemTvResource, int itemCount, ArrayList<Integer> arrayList, boolean isPlus) {
		mContext = context;
		mBackground = itemBgResource;
		mTvBackground = itemTvResource;
		mCount = itemCount;
		mSelectData = arrayList;
		isNumPlus = isPlus;
	}

	@Override
	public int getCount() {
		return mCount;
	}

//	/**
//	 * 一共显示多少个item
//	 */
	// public void setCount(int count) {
	// mCount = count;
	// }

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
		HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LinearLayout.inflate(mContext, R.layout.item_lbgridview, null);
			holderView.ballView = (TextView) convertView.findViewById(R.id.txtView_item_ball);
			holderView.ballMiss = (TextView) convertView.findViewById(R.id.txtView_item_miss);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		holderView.ballView.setBackgroundResource(mBackground);
		holderView.ballView.setTextColor(mContext.getResources().getColorStateList(mTvBackground));
		if (isNumPlus) {
			holderView.ballView.setText(position + "");
		} else {
			holderView.ballView.setText(new DecimalFormat("00").format(position + 1));
		}
		holderView.ballMiss.setVisibility(getIsGone());
		holderView.ballMiss.setText(getSplitStr(mMissData, position));
		if (mSelectData != null) {
			if (mSelectData.contains(position)) {
				holderView.ballView.setSelected(true);
			} else {
				holderView.ballView.setSelected(false);
			}
		}
		return convertView;
	}

	private int getIsGone() {
		return TextUtils.isEmpty(mMissData) ? View.GONE : View.VISIBLE;
	}

	private String getSplitStr(String missData, int position) {
		if (!TextUtils.isEmpty(missData)) {
			String[] split = missData.split(",");
			return split[position];
		}
		return "";
	}

	/**
	 * 更新选择的彩球
	 * @Description:
	 * @param data
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void setSelectData(ArrayList<Integer> data) {
		mSelectData = data;
		notifyDataSetChanged();
	}

	/**
	 * 更新遗漏数据
	 * @Description:
	 * @param ballMissStr
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void setMissData(String ballMissStr) {
		mMissData = ballMissStr;
		notifyDataSetChanged();
	}

	/**
	 * 获取选择的彩球索引结果集
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public ArrayList<Integer> getData() {
		return mSelectData;
	}

	/**
	 * 单个彩球
	 * @Description:
	 * @author:zhangjf 
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月6日
	 */
	class HolderView {

		private TextView ballView;
		private TextView ballMiss;// 遗漏
	}

}
