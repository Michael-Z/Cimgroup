package cn.com.cimgroup.activity.ViewStub;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.view.LBGridView;
import cn.com.cimgroup.view.LBGridView.OnLBItemClickCallBack;
import cn.com.cimgroup.view.LBGridviewController;

/**
 * 排列3-组三复式
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class PL3_Z3 implements OnLBItemClickCallBack {

	public LBGridviewController mZ3Controller;
	private TextView mTouzhuHint;

	public PL3_Z3(View mViewZ3, TextView tv) {
		LBGridView mZ3GridView = (LBGridView) mViewZ3.findViewById(R.id.gridView_pl3_z3);
		mZ3Controller = mZ3GridView.getController();
		mZ3GridView.setCallBack(this);
		mTouzhuHint = tv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mZ3Controller.updateSelect(position);
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney_Pl3Z3(mZ3Controller.getSelectData()));
	}
}
