package cn.com.cimgroup.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.protocol.CException;

public class NoDataUtils {

	private static String mErrCode;
	private static int mType;

	public static String getmErrCode() {
		return mErrCode;
	}

	public static void setmErrCode(String mErrCode) {
		NoDataUtils.mErrCode = mErrCode;
	}

	public static int getmType() {
		return mType;
	}

	public static void setmType(int mType) {
		NoDataUtils.mType = mType;
	}

	public static boolean isNumeric00(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("异常：\"" + str + "\"不是数字/整数...");
			return false;
		}
	}

	/**
	 * 
	 * @Description:
	 * @param view
	 *            整个无数据view
	 * @param image
	 *            无数据图片
	 * @param oneText
	 *            首行textview
	 * @param twoText
	 *            第二行textview
	 * @param button
	 *            点击button
	 * @param errCode
	 *            错误号
	 * @param type
	 *            显示类型
	 * @author:www.wenchuang.com
	 * @date:2016-12-28
	 */
	@SuppressLint("NewApi")
	public static void setNoDataView(Context context, ImageView image,
			TextView oneText, TextView twoText, TextView button,
			String errCode, int type) {

		if (TextUtils.isEmpty(errCode) || errCode.equals("0")) {
			errCode = "0";
		}
		mType = type;
		if (isNumeric00(errCode)) {
			mErrCode = errCode;
		}else {
			mErrCode = "10000";
		}
		switch (Integer.parseInt(mErrCode)) {
		case CException.NET_ERROR:
		case CException.IOERROR:
			// 无网，超时
			image.setImageResource(R.drawable.icon_nodata_nowifi);
			oneText.setText(context.getResources().getString(
					R.string.nodata_no_net));
			twoText.setText(context.getResources().getString(
					R.string.nodata_no_net1));
			button.setText(context.getResources().getString(
					R.string.nodata_no_net_btn));
			button.setTextColor(context.getResources().getColor(
					R.color.color_gray_indicator));
			button.setBackground(context.getResources().getDrawable(
					R.drawable.selector_bg_center_textview));
			twoText.setVisibility(View.VISIBLE);
			button.setVisibility(View.VISIBLE);
			break;
		case 0:
			// 无数据
			// type 1,投注记录、资金明细、红包明细
			// 2,兑换记录、我的红包
			// 3,排行榜
			// 4,开奖详情
			// 5,赛事关注
			// 6,活动
			// 7,消息
			// 8,竞技彩投注页、比分直播、娱乐场
			// 9,商城
			// 10,开奖大厅、开奖列表
			// 11,实况（未开赛）
			// 12,实况（直播中，无事件）
			// 13,分析、赔率、亚盘
			// 14,实况 已结束
			switch (type) {
			case 1:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list));
				twoText.setText(context.getResources().getString(
						R.string.nodata_no_list1));
				button.setText(context.getResources().getString(
						R.string.nodata_no_list_btn1));
				button.setTextColor(context.getResources().getColor(
						R.color.color_white));
				button.setBackground(context.getResources().getDrawable(
						R.drawable.selector_bg_red_center_textview));
				twoText.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				break;
			case 2:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list));
				twoText.setText(context.getResources().getString(
						R.string.nodata_no_list2));
				button.setText(context.getResources().getString(
						R.string.nodata_no_list_btn2));
				button.setTextColor(context.getResources().getColor(
						R.color.color_white));
				button.setBackground(context.getResources().getDrawable(
						R.drawable.selector_bg_red_center_textview));
				twoText.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				break;
			case 3:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list));
				twoText.setText(context.getResources().getString(
						R.string.nodata_no_list1));
				button.setText(context.getResources().getString(
						R.string.nodata_no_list_btn3));
				button.setTextColor(context.getResources().getColor(
						R.color.color_white));
				button.setBackground(context.getResources().getDrawable(
						R.drawable.selector_bg_red_center_textview));
				twoText.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				break;
			case 4:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list_btn4));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 5:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list_btn5));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 6:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list_btn6));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 7:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list_btn7));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 8:
				image.setImageResource(R.drawable.icon_nodata_nogame);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list3));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 9:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list4));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 10:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_exception));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 11:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list5));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 12:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list6));
				twoText.setText(context.getResources().getString(
						R.string.nodata_no_list7));
				twoText.setVisibility(View.VISIBLE);
				button.setVisibility(View.GONE);
				break;
			case 13:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list7));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			case 14:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_list8));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			default:
				image.setImageResource(R.drawable.icon_nodata_noinfo);
				oneText.setText(context.getResources().getString(
						R.string.nodata_no_net));
				twoText.setVisibility(View.GONE);
				button.setVisibility(View.GONE);
				break;
			}
			break;
		default:
			// 系统异常
			image.setImageResource(R.drawable.icon_nodata_noinfo);
			oneText.setText(context.getResources().getString(
					R.string.nodata_exception));
			twoText.setVisibility(View.GONE);
			button.setVisibility(View.GONE);
			break;
		}

	}
}
