package cn.com.cimgroup.frament;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.BuyRedPacketActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.UserLogic;

public class LoboTogeBuyFrament extends BaseFrament implements OnClickListener {

	// 整个页面view
	private View mView;
	
	private UserInfo userInfo;
	
	private RelativeLayout buyHead;
	
	private boolean mShouldInitialize = true;
	
	private boolean isPrepared;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if (mView == null) {
			mView = inflater.inflate(R.layout.frament_togebuy, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		
		if (mShouldInitialize) {
			initView();
			isPrepared = true;
			lazyLoad();
		}
		return mView;
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		buyHead = (RelativeLayout)mView.findViewById(R.id.layout_buy_head);
		
		((TextView) mView.findViewById(R.id.textView_title_back)).setVisibility(View.GONE);
		TextView mTitleNameView = (TextView) mView.findViewById(R.id.textView_title_word);
		mTitleNameView.setText(getString(R.string.frament_tab_index2));
		((RelativeLayout) mView.findViewById(R.id.layout_shop_lemi)).setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.layout_shop_red_packet)).setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.layout_shop_lemi_dui)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		userInfo = UserLogic.getInstance().getDefaultUserInfo();
		//2.5期新增内容 购买红包/乐米   乐米兑换  都可以在未登录的状态下进行查看
		switch (v.getId()) {
		//case 乐米兑换
		case R.id.layout_shop_lemi:
//			if (userInfo != null) {
				Intent convertIntent = new Intent(getActivity(),MessageCenterActivity.class);
				convertIntent.putExtra(MessageCenterActivity.TYPE,MessageCenterActivity.LEMICONVERT);
				getActivity().startActivity(convertIntent);
//			} else {
//				startActivity(LoginActivity.class);
//			}
			break;
		//case 购买红包
		case R.id.layout_shop_red_packet:
//			if (userInfo != null) {
				Intent buyRed = new Intent(getActivity(),BuyRedPacketActivity.class);
				buyRed.putExtra(GridFragment.TYPE, GridFragment.BUYREDPACK);
				getActivity().startActivity(buyRed);
//			} else {
//				startActivity(LoginActivity.class);
//			}
			break;
		//case 购买乐米
		case R.id.layout_shop_lemi_dui:
//			if (userInfo != null) {
				Intent buyLeMi = new Intent(getActivity(),BuyRedPacketActivity.class);
				buyLeMi.putExtra(GridFragment.TYPE,GridFragment.BUYLEMI);
				getActivity().startActivity(buyLeMi);
//			} else {
//				startActivity(LoginActivity.class);
//			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
	}

}
