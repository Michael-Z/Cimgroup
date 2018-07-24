package cn.com.cimgroup.activity.ViewStub;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.view.LBGridView;
import cn.com.cimgroup.view.LBGridView.OnLBItemClickCallBack;
import cn.com.cimgroup.view.LBGridviewController;

/**
 * 排列3-组选和值
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class PL3_ZXHZ implements OnLBItemClickCallBack {

	public LBGridviewController mZxhzController;
	private TextView mTouzhuHint;

	public PL3_ZXHZ(View mViewZxhz, TextView tv) {
		LBGridView mZxhzGridView = (LBGridView) mViewZxhz.findViewById(R.id.gridView_pl3_zxhz);
		mZxhzController = mZxhzGridView.getController();
		mZxhzGridView.setCallBack(this);
		mTouzhuHint = tv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ArrayList<Integer> list = mZxhzController.getSelectData();
		mZxhzController.updateSelect(position);
//		if (list.contains(position)) {
//			list.remove((Integer)position);
//			((LBGridView) mZxhzController.getGridView()).notifyChanged();
//		}
//		clickOnlyNotify(position, list, mZxhzController.getAdapter());
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney_Pl3Zxhz(mZxhzController.getSelectData()));
	}
	
	private void clickOnlyNotify(int position, ArrayList<Integer> list, BaseAdapter adapter) {
		if (list.size() > 0) {
			if (list.contains(position)) {
				list.remove((Integer) position);
			} else {
				list.clear();
				list.add(position);
			}
		} else {
			list.add(position);
		}
		adapter.notifyDataSetChanged();
	}
}
