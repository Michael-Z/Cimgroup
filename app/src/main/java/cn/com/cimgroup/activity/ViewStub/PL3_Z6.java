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
 * 排列3-组六复式
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class PL3_Z6 implements OnLBItemClickCallBack {

	public LBGridviewController mZ6Controller;
	private TextView mTouzhuHint;

	public PL3_Z6(View mViewZ6, TextView tv) {
		LBGridView mZ6GridView = (LBGridView) mViewZ6.findViewById(R.id.gridView_pl3_z6);
		mZ6Controller = mZ6GridView.getController();
		mZ6GridView.setCallBack(this);
		mTouzhuHint = tv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mZ6Controller.updateSelect(position);
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney_Pl3Z6(mZ6Controller.getSelectData()));
	}
}
