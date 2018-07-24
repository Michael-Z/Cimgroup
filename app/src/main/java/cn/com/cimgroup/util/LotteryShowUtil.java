package cn.com.cimgroup.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.view.FlowLayout;

/**
 * 彩票显示的工具类
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-9
 */
public class LotteryShowUtil {

	/**
	 * @Description:机选号码
	 * @param list
	 * @param num机选几个
	 * @param ballNum在几个球数里选
	 * @see:
	 * @since:
	 * @author:zhangjf
	 * @date:2015-1-9
	 */
	public static void randomBall(ArrayList<Integer> list, int num, int ballNum) {
		list.clear();
		Random random = new Random();
		boolean[] bool = new boolean[ballNum];
		int randInt = 0;
		for (int j = 0; j < num; j++) {
			do {
				randInt = random.nextInt(ballNum);
			} while (bool[randInt]);
			bool[randInt] = true;
			list.add(randInt);
		}
		Collections.sort(list);
	}
	
	public static void randomAddBall(ArrayList<Integer> list, int num, int ballNum) {
		Random random = new Random();
		boolean[] bool = new boolean[ballNum];
		int randInt = 0;
		for (int j = 0; j < num; j++) {
			do {
				randInt = random.nextInt(ballNum);
			} while (bool[randInt]);
			bool[randInt] = true;
			list.add(randInt);
		}
	}
	
	/**
	 * @Description: 机选号码 没有排序
	 * @param list
	 * @param num
	 * @param ballNum
	 * @author:zhangjf
	 * @date:2015-3-29
	 */
	public static void randomBallNoSort(ArrayList<Integer> list, int num, int ballNum) {
		list.clear();
		Random random = new Random();
		boolean[] bool = new boolean[ballNum];
		int randInt = 0;
		for (int j = 0; j < num; j++) {
			do {
				randInt = random.nextInt(ballNum);
			} while (bool[randInt]);
			bool[randInt] = true;
			list.add(randInt);
		}
	}
    
	/**
	 * 光标显示在最后
	 * 
	 * @param et
	 */
	public static void setSelection(EditText et) {
		String text = getText(et);
		if (!TextUtils.isEmpty(text)) {
			et.setSelection(text.length());
		}
	}

	/**
	 * 得到EditText的字符串
	 * 
	 * @param et
	 * @return
	 */
	public static String getText(EditText et) {
		String text = et.getText().toString().trim();
		if (!TextUtils.isEmpty(text)) {
			return text;
		} else {
			return "";
		}
	}

	/**
	 * 设置红色字体
	 * 
	 * @Description:
	 * @param ctx
	 * @param text
	 * @return
	 * @author:zhangjf
	 * @date:2015-1-16
	 */
	public static SpannableString setRedText(Context ctx, String text, String money) {
		String string = text + money;
		SpannableString spannable = new SpannableString(string);
		int hintColor = ctx.getResources().getColor(R.color.hall_grey_word);
		int redColor = ctx.getResources().getColor(R.color.color_red);
		spannable.setSpan(new ForegroundColorSpan(hintColor), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(redColor), text.length(), string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	public static SpannableString setCartNullBuleText(Context ctx, String text) {
		SpannableString spannable = new SpannableString(text);
		int hintColor = ctx.getResources().getColor(R.color.hall_grey_word);
		int buleColor = ctx.getResources().getColor(R.color.hall_blue);
		spannable.setSpan(new ForegroundColorSpan(hintColor), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(buleColor), text.length() - 2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	public static void setTextViewRightDrawable(TextView tv, Drawable drawable) {
		tv.setCompoundDrawablesWithIntrinsicBounds(tv.getCompoundDrawables()[0], tv.getCompoundDrawables()[1], drawable, tv.getCompoundDrawables()[3]);
	}

	/**
	 * 获取玩法对应的玩法名称
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-2
	 */
	public static String getLotteryName(int lottery) {
		String lotteryNameStr = "";
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lottery_dlt);
			break;
		case GlobalConstants.LOTTERY_PL3:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lottery_p3);
			break;
		case GlobalConstants.LOTTERY_PL5:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lottery_p5);
			break;
		case FootballLotteryConstants.L301:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lottery_football);
			break;
		case FootballLotteryConstants.L306:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lotteryhall_jclq);
			break;
		case GlobalConstants.LOTTERY_QXC:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lotteryhall_qxc);
			break;
		case GlobalConstants.LOTTERY_SF14:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.oldfootball_dialog_spf_desc);
			break;
		case GlobalConstants.LOTTERY_SF9:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.oldfootball_dialog_spf_desc);
			break;
		case GlobalConstants.LOTTERY_JQ4:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.football_dialog_jqs_desc);
			break;
		case GlobalConstants.LOTTERY_BQ6:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.football_dialog_bqc_desc);
			break;
		case FootballLotteryConstants.L501:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lotteryhall_2x1);
			break;
		case FootballLotteryConstants.L502:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.lotteryhall_1czs);
			break;
		default:
			lotteryNameStr = App.getInstance().getResources().getString(R.string.register_doc);
			break;
		}
		return lotteryNameStr;
	}

	/**
	 * 获取玩法对应的开奖所需的lotoID
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-2
	 */
//	public static String getDrawLotteryID(int lottery) {
//		String lotteryNameStr = "";
//		switch (lottery) {
//		case GlobalConstants.LOTTERY_DLT:
//			lotteryNameStr = GlobalConstants.DRAW_LOTTERYID_DLT;
//			break;
//		case GlobalConstants.LOTTERY_PL3:
//			lotteryNameStr = GlobalConstants.DRAW_LOTTERYID_PL3;
//			break;
//		case GlobalConstants.LOTTERY_PL5:
//			lotteryNameStr = GlobalConstants.DRAW_LOTTERYID_PL5;
//			break;
//		case GlobalConstants.LOTTERY_QXC:
//			lotteryNameStr = GlobalConstants.DRAW_LOTTERYID_QXC;
//			break;
//		case GlobalConstants.LOTTERY_11X5:
//			lotteryNameStr = GlobalConstants.DRAW_LOTTERYID_11X5;
//			break;
//
//		}
//		return lotteryNameStr;
//	}
	
	/**
	 * 获取玩法对应的玩法说明
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-2
	 */
	public static String getPLayExplainHtml(int lottery) {
		String lotteryHtmlStr = "";
		switch (lottery) {
		case GlobalConstants.LOTTERY_DLT:
			lotteryHtmlStr = "wx-dlt.html";
			break;
		case GlobalConstants.LOTTERY_JCZQ:
			lotteryHtmlStr = "wx-jczq.html";
			break;
		case GlobalConstants.LOTTERY_JCLQ:
			lotteryHtmlStr = "wx-jclq.html";
			break;
		case GlobalConstants.LOTTERY_PL3:
			lotteryHtmlStr = "wx-pl3.html";
			break;
		case GlobalConstants.LOTTERY_PL5:
			lotteryHtmlStr = "wx-pl5.html";
			break;
		case GlobalConstants.LOTTERY_QXC:
			lotteryHtmlStr = "wx-qxc.html";
			break;
		case GlobalConstants.LOTTERY_SF14:
			lotteryHtmlStr = "wx-sf14.html";
			break;
		case GlobalConstants.LOTTERY_SF9:
			lotteryHtmlStr = "wx-sf14.html";
			break;
		case GlobalConstants.LOTTERY_JQ4:
			lotteryHtmlStr = "wx-jq4.html";
			break;
		case GlobalConstants.LOTTERY_BQ6:
			lotteryHtmlStr = "wx-bq6.html";
			break;
//		case GlobalConstants.LOTTERY_11X5:
//			lotteryHtmlStr = "playjx115.html";
//			break;
		case FootballLotteryConstants.L301:
			lotteryHtmlStr = "wx-jczq.html";
			break;
		case FootballLotteryConstants.L306:
			lotteryHtmlStr = "wx-jclq.html";
			break;
		case FootballLotteryConstants.L501:
			lotteryHtmlStr = "wx-2x1.html";
			break;
		case FootballLotteryConstants.L502:
			lotteryHtmlStr = "wx-1czs.html";
			break;
		default:
			lotteryHtmlStr = "protocol.html";
			break;
		}
		return lotteryHtmlStr;
	}

	/**
	 * 获取遗漏数据对应的请求ChartType
	 * 
	 * @Description:
	 * @param lottery
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-24
	 */
//	public static String getMissChartType(int lottery) {
//		String chartType = "";
//		switch (lottery) {
//		case GlobalConstants.LOTTERY_DLT:
//			chartType = GlobalConstants.CHARTTYPE_DLT;
//			break;
//		case GlobalConstants.LOTTERY_PL3:
//			chartType = GlobalConstants.CHARTTYPE_PL3;
//			break;
//		case GlobalConstants.LOTTERY_PL5:
//			chartType = GlobalConstants.CHARTTYPE_PL5;
//			break;
//		case GlobalConstants.LOTTERY_QXC:
//			chartType = GlobalConstants.CHARTTYPE_QXC;
//			break;
//		case GlobalConstants.LOTTERY_11X5:
//			chartType = GlobalConstants.CHARTTYPE_11X5;
//			break;
//		}
//		return chartType;
//	}

	/**
	 * 拼接每行的所需的遗漏数据
	 * 
	 * @Description:
	 * @param string
	 * @param start
	 * @param end
	 * @return
	 * @author:zhangjf
	 * @date:2015-3-4
	 */
	public static String getMissString(String string, int start, int end) {
		int index = 0;
		StringBuilder builder = new StringBuilder();
		if (string != "") {
			String[] split2 = string.split(",");
			for (int i = start; i < end; i++) {
				if (index > 0) {
					builder.append(",");
				}
				builder.append(split2[i]);
				index++;
			}
		}
		
		return builder.toString();
	}

	/**
	 * 数字彩展示
	 * 
	 * @Description:
	 * @param ctx
	 * @param ball
	 * @param mFLItem
	 * @param winbasecode
	 * @param showBg
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-2-4
	 */
	public static void showNumberLotteryView(Context ctx, int ball, FlowLayout mFLItem, String winbasecode, boolean showBg) {
		TextView redNumTv = null;
		// 是否存在空格
		Boolean isExistSpace = winbasecode.indexOf(",") > 0;
		// 存在空格的数据集
		String[] existSpaceData = winbasecode.split(",");
		// 不存在空格的数据集
		String noExistSqpceData = winbasecode;
		// 获取数据长度
		int dataLength = isExistSpace ? existSpaceData.length : noExistSqpceData.length();
		// 如果是蓝球，且没有空格则每次取俩位
		if (ball == 1 && !isExistSpace) {
			dataLength = dataLength / 2;
		}
		for (int i = 0; i < dataLength; i++) {
			String text = null;
			if (ball == 0) {
				text = isExistSpace ? existSpaceData[i] : noExistSqpceData.substring(i, i + 1);
			} else {
				text = isExistSpace ? existSpaceData[i] : noExistSqpceData.substring(i * 2, i * 2 + 2);
			}
			if (showBg) {
				redNumTv = showText(ctx, ball, text);
			} else {
				redNumTv = showTextBg(ctx, ball, text);
			}
			mFLItem.addView(redNumTv);
		}
	}

	public static void showNum(Context ctx, int ball, FlowLayout mFLItem, String winbasecode, boolean showBg) {
		TextView redNumTv = null;
		TextView buleNumTv = null;
		int baseCodeLength = winbasecode.length();
		if (baseCodeLength % 2 == 0) {
			for (int i = 0; i < baseCodeLength / 2; i++) {
				int start = i * 2;
				int end = start + 2;
				String substring = winbasecode.substring(start, end);
				if (showBg) {
					redNumTv = showText(ctx, ball, substring);
				} else {
					redNumTv = showTextBg(ctx, ball, substring);
				}
				mFLItem.addView(redNumTv);
			}
		} else {
			for (int i = 0; i < baseCodeLength; i++) {
				int start = i;
				int end = start + 1;
				String substring = winbasecode.substring(start, end);
				if (showBg) {
					buleNumTv = showText(ctx, ball, substring);
				} else {
					buleNumTv = showTextBg(ctx, ball, substring);
				}
				mFLItem.addView(buleNumTv);
			}
		}
	}

	public static TextView showTextBg(Context ctx, int ball, String substring) {
		TextView numTv = new TextView(ctx);
		numTv.setLayoutParams(new LayoutParams(GlobalTools.dip2px(ctx, 30), GlobalTools.dip2px(ctx, 30)));
		numTv.setTextSize(16);
		numTv.setGravity(Gravity.CENTER);
		numTv.setText(substring);
		numTv.setTextColor(ctx.getResources().getColor(R.color.color_white));
		if (ball == 0) {
			numTv.setBackgroundResource(R.drawable.lottery_redball_sel);
		} else {
			numTv.setBackgroundResource(R.drawable.lottery_blueball_sel);
		}
		return numTv;
	}

	public static TextView showText(Context ctx, int ball, String substring) {
		TextView numTv = new TextView(ctx);
		numTv.setTextSize(18);
		numTv.setPadding(6, 0, 6, 0);
		numTv.setGravity(Gravity.CENTER);
		numTv.setText(substring);
		if (ball == 0) {
			numTv.setBackgroundColor(ctx.getResources().getColor(R.color.hall_weak_green));
			numTv.setTextColor(ctx.getResources().getColor(R.color.color_white));
		} else {
			numTv.setBackgroundColor(ctx.getResources().getColor(R.color.hall_blue));
		}
		return numTv;
	}
	
	public static String htmlConvert(Context ctx) {
		StringBuilder sb = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(ctx.getAssets().open("information.html")));
			sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
