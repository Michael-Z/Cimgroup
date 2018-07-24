package cn.com.cimgroup.view;

import java.util.ArrayList;

import android.widget.GridView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.LBGridViewAdapter;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 控制器，用于数字彩 彩球业务处理
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月19日
 */
public class LBGridviewController {

	private LBGridView mLbGridView;
	private ArrayList<Integer> data = new ArrayList<Integer>();

	public LBGridviewController(LBGridView lbGridView) {
		this.mLbGridView = lbGridView;
	}

	public void updateSelect(int position) {
		if (data.size() > 0) {
			if (data.contains(position)) {
				data.remove((Integer) position);
			} else {
				data.add(position);
			}
		} else {
			data.add(position);
		}
		mLbGridView.notifyChanged();
	}
	
	public void updateSelectDlt_Red(int position) {
		if (data.size() > 0) {
			if (data.contains(position)) {
				data.remove((Integer) position);
			} else {
				if (data.size() == 18) {
					ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_red_most18));
					return;
				}
				data.add(position);
			}
		} else {
			data.add(position);
		}
		mLbGridView.notifyChanged();
	}

	public void updateSelectDlt_RedDan(int position, ArrayList<Integer> list) {
		if (list.contains(position)) {
			ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_diff_tm));
			return;
		}
		if (data.size() > 0) {
			if (data.contains(position)) {
				data.remove((Integer) position);
			} else {
				if (data.size() == 4) {
					ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_red_dm_most4));
					return;
				}
				data.add(position);
			}
		} else {
			data.add(position);
		}
		mLbGridView.notifyChanged();
	}

	public void updateSelectDlt_RedTuo(int position, ArrayList<Integer> list) {
		if (list.contains(position)) {
			ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_diff_dm));
			return;
		}
		if (data.size() > 0) {
			if (data.contains(position)) {
				data.remove((Integer) position);
			} else {
				if (data.size() == 18) {
					ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_red_dm_most18));
					return;
				}
				data.add(position);
			}
		} else {
			data.add(position);
		}
		mLbGridView.notifyChanged();
	}

	public void updateSelect_Dlt_BuleDan(int position, ArrayList<Integer> list) {
		if (list.contains(position)) {
			ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_diff_tm));
			return;
		}
		if (data.size() > 0) {
			if (data.contains(position)) {
				data.remove((Integer) position);
			} else {
				if (data.size() == 1) {
					ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_blue_only1));
					return;
				}
				data.add(position);
			}
		} else {
			data.add(position);
		}
		mLbGridView.notifyChanged();
	}

	public void updateSelect_Dlt_BuleTuo(int position, ArrayList<Integer> list) {
		if (list.contains(position)) {
			ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_diff_dm));
			return;
		}
		if (data.size() > 0) {
			if (data.contains(position)) {
				data.remove((Integer) position);
			} else {
//				if (data.size() == 11) {
//					ToastUtil.shortToast(App.getInstance(), App.getInstance().getResources().getString(R.string.lottery_blue_most11));
//					return;
//				}
				data.add(position);
			}
		} else {
			data.add(position);
		}
		mLbGridView.notifyChanged();
	}

	public void updateSelect_Pl3_Z3ds(int position, ArrayList<Integer> list, ArrayList<Integer> list2) {
		if (list.contains(position)) {
			list.remove((Integer) position);
			mLbGridView.notifyChanged();
		}

	}

	/**
	 * 清空状态
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年1月19日
	 */
	public void clearSelectState() {
		if (data != null) {
			data.clear();
		}
		mLbGridView.notifyChanged();
	}

	/**
	 * 插入新数据，更新界面
	 * @Description:
	 * @param list
	 * @author:www.wenchuang.com
	 * @date:2016年1月19日
	 */
	public void insertSelect(ArrayList<Integer> list) {
		if (list != null) {
			data = list;
		}
		mLbGridView.notifySetChanged(list);
	}
	
	/**
	 * 插入遗漏数据，更新界面
	 * @Description:
	 * @param missStr
	 * @author:www.wenchuang.com
	 * @date:2016年1月19日
	 */
	public void insertBallMiss(String missStr) {
//		if (!TextUtils.isEmpty(missStr)) {
			mLbGridView.notifyBallMissChanged(missStr);
//		}
	}
	
	/**
	 * 机选号码
	 * @Description:
	 * @param list
	 * @param num 机选几个
	 * @param ballNum 在几个球数里选
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void randomBall(ArrayList<Integer> list, int num, int ballNum) {
		LotteryShowUtil.randomBall(list, num, ballNum);
		mLbGridView.notifySetChanged(list);
		// mLbGridView.notifyChanged();
	}

	/**
	 * 返回选择的彩票状态结果集
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public ArrayList<Integer> getSelectData() {
		return data;
	}

	public GridView getGridView() {
		return mLbGridView;
	}

	public LBGridViewAdapter getAdapter() {
		return mLbGridView.getAdapter();
	}
}
