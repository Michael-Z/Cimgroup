package cn.com.cimgroup.frament;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.GameGuessMatchActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.bean.RankingListBean;
import cn.com.cimgroup.frament.RankingListCFragment.loadMyListFinished;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.NoDataUtils;

/**
 * 没有数据的空碎片
 * 
 * @author 秋风
 * 
 */
public abstract class EmptyFragment extends BaseFrament {
	
	public View mEmptyView;
	
	public ImageView emptyImage;
	public TextView oneText;
	public TextView twoText;
	public TextView button;
	
	protected boolean mShouldInitialize = true;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		Bundle bundle = getArguments();
//		String colorType=bundle.getString("colorType");
//		String title=bundle.getString("content");
//		int color=getActivity().getResources().getColor(R.color.layout_bg_color);
//		if (!TextUtils.isEmpty(colorType)) {
//			color=colorType.equals("1")?Color.WHITE:getActivity().getResources().getColor(R.color.layout_bg_color);
//		}
//		LayoutParams lpLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//		RelativeLayout layout = new RelativeLayout(getActivity());
//		layout.setLayoutParams(lpLayout);
//		layout.setBackgroundColor(color);
//
//		RelativeLayout childLinearLayout = new RelativeLayout(getActivity());
//		LayoutParams lpChild = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		lpChild.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//		LayoutParams imParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		imParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		ImageView image = new ImageView(getActivity());
//		image.setImageResource(R.drawable.e_ui_game_guess_emty);
//		image.setId(1);
//
//		childLinearLayout.addView(image, imParams);
//
//		TextView tv = new TextView(getActivity());
//		LayoutParams tLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		tLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		tLp.addRule(RelativeLayout.BELOW, 1);
//		tLp.topMargin = DensityUtils.dip2px(10);
//		tv.setTextSize(getTargetRequestCode(), DensityUtils.dip2px(12));
//		tv.setText(title);
//		tv.setTextColor(getActivity().getResources().getColor(R.color.color_gray_indicator));
//		tv.setId(2);
//		childLinearLayout.addView(tv, tLp);
//		
//		
//		LayoutParams imParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		imParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		imParams1.addRule(RelativeLayout.BELOW, 2);
//		ImageView image1 = new ImageView(getActivity());
//		image1.setImageResource(R.drawable.e_ui_game_guess_emty);
//		image1.setId(3);
//		image1.setVisibility(View.INVISIBLE);
//
//		childLinearLayout.addView(image1, imParams1);
//
//		TextView tv1 = new TextView(getActivity());
//		LayoutParams tLp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		tLp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		tLp1.addRule(RelativeLayout.BELOW, 3);
//		tLp1.topMargin = DensityUtils.dip2px(10);
//		tv1.setTextSize(getTargetRequestCode(), DensityUtils.dip2px(12));
//		tv1.setText(title);
//		tv1.setTextColor(getActivity().getResources().getColor(R.color.color_gray_indicator));
//		tv1.setVisibility(View.INVISIBLE);
//		childLinearLayout.addView(tv1, tLp1);
//		
//		
//		
//		
//		
//		
//		
//		
//
//		layout.addView(childLinearLayout, lpChild);
		
		if (mEmptyView == null) {
			mEmptyView = inflater.inflate(R.layout.layout_loading_empty, null);
			
		} else {
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			emptyImage = (ImageView) mEmptyView.findViewById(R.id.imageView_load_empty);
			oneText = (TextView) mEmptyView.findViewById(R.id.textView_load_one);
			twoText = (TextView) mEmptyView.findViewById(R.id.textView_load_two);
			button = (TextView) mEmptyView.findViewById(R.id.button_load);
		}
		if (button != null) {
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					emptyBtnOnClick();
				}
			});
		}
		
		NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, mErrCode, mType);

		return mEmptyView;
	}

	
	/**
	 * 无数据时的统一跳转处理
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-12-29
	 */
	public void emptyBtnOnClick() {
		switch (Integer.parseInt(mErrCode)) {
		
		case CException.NET_ERROR:
		case CException.IOERROR:
			loadFirstData();
			break;
		case 0:
			//type 1,投注记录、资金明细、红包明细-立即购彩
			//2,兑换记录、我的红包-前往商城
			//3,排行榜-参与竞猜
			switch (mType) {
			case 1:
				startActivity(LotteryFootballActivity.class);
				break;
			case 2:
				Intent convertIntent = new Intent(getActivity(),MessageCenterActivity.class);
				convertIntent.putExtra(MessageCenterActivity.TYPE,MessageCenterActivity.LEMICONVERT);
				getActivity().startActivity(convertIntent);
				break;
			case 3:
				startActivity(GameGuessMatchActivity.class);
				break;

			default:
				break;
			}
			break;
		}
	}
	
	public abstract void loadFirstData();
	
	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}

	private static String mErrCode;
	private static int mType;

	
	public static String getmErrCode() {
		return mErrCode;
	}

	
	public static void setmErrCode(String mErrCode) {
		EmptyFragment.mErrCode = mErrCode;
	}

	
	public static int getmType() {
		return mType;
	}

	
	public static void setmType(int mType) {
		EmptyFragment.mType = mType;
	}
	
	


}
