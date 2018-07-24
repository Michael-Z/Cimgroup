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
 * 大乐透胆拖布局
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class DLT_DanTuo implements OnLBItemClickCallBack {

	public LBGridviewController mRedDanControllerGV;
	public LBGridviewController mRedTuoControllerGV;
	public LBGridviewController mBuleDanControllerGV;
	public LBGridviewController mBuleTuoControllerGV;
	private TextView mTouzhuHint;

	public DLT_DanTuo(View view, TextView tv) {
		LBGridView mGVRedDan = (LBGridView) view.findViewById(R.id.gridView_redDan);
		LBGridView mGVRedTuo = (LBGridView) view.findViewById(R.id.gridView_redTuo);
		LBGridView mGVBuleDan = (LBGridView) view.findViewById(R.id.gridView_buleDan);
		LBGridView mGVBuleTuo = (LBGridView) view.findViewById(R.id.gridView_buleTuo);
		mRedDanControllerGV = mGVRedDan.getController();
		mRedTuoControllerGV = mGVRedTuo.getController();
		mBuleDanControllerGV = mGVBuleDan.getController();
		mBuleTuoControllerGV = mGVBuleTuo.getController();
		mGVRedDan.setCallBack(this);
		mGVRedTuo.setCallBack(this);
		mGVBuleDan.setCallBack(this);
		mGVBuleTuo.setCallBack(this);
		mTouzhuHint = tv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == mRedDanControllerGV.getGridView()) {
			mRedDanControllerGV.updateSelectDlt_RedDan(position, mRedTuoControllerGV.getSelectData());
		} else if (parent == mRedTuoControllerGV.getGridView()) {
			mRedTuoControllerGV.updateSelectDlt_RedTuo(position, mRedDanControllerGV.getSelectData());
		} else if (parent == mBuleDanControllerGV.getGridView()) {
			mBuleDanControllerGV.updateSelect_Dlt_BuleDan(position, mBuleTuoControllerGV.getSelectData());
		} else if (parent == mBuleTuoControllerGV.getGridView()) {
			mBuleTuoControllerGV.updateSelect_Dlt_BuleTuo(position, mBuleDanControllerGV.getSelectData());
		}
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney(mRedDanControllerGV.getSelectData(), mRedTuoControllerGV.getSelectData(), mBuleDanControllerGV.getSelectData(), mBuleTuoControllerGV.getSelectData()));
	}
}
