package cn.com.cimgroup.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.bean.GetCode;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.dailog.LoadingDialogProgress;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 比分列表Adapter
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年12月8日
 */
public class ScoreListAdapter extends SimpleBaseAdapter<ScoreMatchBean> {
	private LoadingDialogProgress mDialog = null;
	/** 玩法类别 */
	private String mLotteryNo = "";
	/** 标签类别 */
	private String mType = "0";

	public ScoreListAdapter(Context context, MCenterRecordTz centerRecordTz,
			String gameNo, String type) {
		super(context, null);
		mDialog = LoadingDialogProgress.newIntance(true);
		mLotteryNo = gameNo;
		mType = type;
	}

	/**
	 * 更新彩种的状态
	 * 
	 * @param gameNo
	 */
	public void setLotteryNo(String gameNo) {
		mLotteryNo = gameNo;
	}

	@Override
	public int getItemResource() {
		return R.layout.item_fragment_score;
	}

	@Override
	public View getItemView(final int position, View convertView,
			ViewHolder holder) {
		final ImageView like = holder.getView(R.id.textView_score_item_like);
		TextView matchTime = holder
				.getView(R.id.textView_score_item_match_time);
		TextView matchName = holder.getView(R.id.textView_score_item_name);
		TextView textView_score_item_begin = holder
				.getView(R.id.textView_score_item_begin);
		TextView half = holder.getView(R.id.textView_score_item_half);
		TextView zhu = holder.getView(R.id.textView_score_item_zhu);
		TextView ke = holder.getView(R.id.textView_score_item_ke);
		TextView vs = holder.getView(R.id.textView_score_item_vs);
		TextView circleView_score_item_circle = holder
				.getView(R.id.circleView_score_item_circle);
		// if (mList.size() >= 5) {
		// if (position == 1) {
		// mList.get(1).setStatus("1");
		// mList.get(1).setHostFullGoals("3");
		// mList.get(1).setGuestFullGoals("1");
		// mList.get(1).setTime("35");
		// } else if (position == 2) {
		// // mList.get(2).setStatus("2");
		// } else if (position == 3) {
		// // mList.get(3).setStatus("3");
		// } else if (position == 5) {
		// // mList.get(5).setStatus("5");
		// }
		// }

		final ScoreMatchBean obj = mList.get(position);
		final String time = obj.getTime();
		final String status = obj.getStatus();
		String watchStatus = obj.getWatchStatus();
		matchTime.setText(obj.getMatchTime());
		matchName.setText(obj.getMatchName());
		textView_score_item_begin.setVisibility(View.VISIBLE);
		zhu.setText(obj.getHostName().length() >= 5 ? obj.getHostName()
				.substring(0, 5) : obj.getHostName());
		ke.setText(obj.getGuestName().length() >= 5 ? obj.getGuestName()
				.substring(0, 5) : obj.getGuestName());
		circleView_score_item_circle.setVisibility(View.GONE);
		stopFlick(circleView_score_item_circle);
		String vsStr = "";
		switch (status) {
		case "0":
			textView_score_item_begin.setText("未开赛");
			vsStr = "VS";
			half.setText("");
			break;
		case "1":
		case "11":
		case "12":
			textView_score_item_begin.setText(time);
			vsStr = obj.getHostFullGoals() + " : " + obj.getGuestFullGoals();
			circleView_score_item_circle.setVisibility(View.VISIBLE);
			circleView_score_item_circle.setBackgroundColor(context
					.getResources().getColor(R.color.color_red));
			startFlick(circleView_score_item_circle);
			half.setText("");
			break;
		case "2":
			circleView_score_item_circle.setVisibility(View.GONE);
			textView_score_item_begin.setText("完场");
			vsStr = obj.getHostFullGoals() + " : " + obj.getGuestFullGoals();
			String result = "";
			int hostGoals = Integer.parseInt(obj.getHostFullGoals());
			int guestGoals = Integer.parseInt(obj.getGuestFullGoals());
			if (hostGoals > guestGoals) {
				result = "3";
			} else if (hostGoals == guestGoals) {
				result = "1";
			} else {
				result = "0";
			}
			half.setText("彩果：" + result);
			break;
		case "3":
			vsStr = "VS";
			circleView_score_item_circle.setVisibility(View.GONE);
			textView_score_item_begin.setText("改期");
			half.setText("");
			break;
		case "4":
			vsStr = "VS";
			circleView_score_item_circle.setVisibility(View.GONE);
			textView_score_item_begin.setText("腰斩");
			half.setText("");
			break;
		case "5":
			vsStr = obj.getHostFullGoals() + " : " + obj.getGuestFullGoals();
			textView_score_item_begin.setText("中场");
			half.setText("");
			break;
		case "13":
			circleView_score_item_circle.setVisibility(View.GONE);
			textView_score_item_begin.setText("延期");
			vsStr = "VS";
			half.setText("");
			break;
		case "14":
			circleView_score_item_circle.setVisibility(View.GONE);
			textView_score_item_begin.setText("待定");
			vsStr = "VS";
			half.setText("");
			break;
		case "15":
			half.setText("");
			break;
		case "16":
			half.setText("");
			break;
		default:
			half.setText("");
			break;
		}
		vs.setText(vsStr);

		switch (watchStatus) {

		case "0":
			// 未关注
			like.setSelected(false);
			break;
		case "1":
			// 已关注
			like.setSelected(true);
			break;
		default:
			break;
		}
		like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (App.userInfo != null) {
					if (status.equals("2") && !like.isSelected()) {
						//比赛已结束  无法关注
						ToastUtil.shortToast(context, "已结束比赛，无法关注");
						return;
					}
					if (mDialog != null) {
						mDialog.show(((FragmentActivity) context)
								.getSupportFragmentManager(), "dialogProgress");
					}
					String watchStatus = "0";
					if (like.isSelected()) {
						watchStatus = "0";
					} else {
						watchStatus = "1";
					}
					v.setEnabled(false);
					Controller.getInstance().watchMatch(
							GlobalConstants.NUM_WATCHMATCH,
							App.userInfo.getUserId(), obj.getMatchId(),
							watchStatus, mLotteryNo, new CallBack() {
								public void watchMatchSuccess(final GetCode code) {
									((Activity) context)
											.runOnUiThread(new Runnable() {
												public void run() {
													v.setEnabled(true);
													mDialog.dismiss();
													if (code != null
															&& code.getResCode()
																	.equals("0")) {
														if (like.isSelected()) {
															mList.get(position)
																	.setWatchStatus(
																			"0");
															like.setSelected(false);
															if (mType
																	.equals("3")// 如果是未关注fragment调用监听，刷新数据
																	&& mRefreshDataListener != null) {
																// 刷新
																mRefreshDataListener
																		.refresh();
															}
															ToastUtil
																	.shortToast(
																			context,
																			"取消关注");
														} else {
															mList.get(position)
																	.setWatchStatus(
																			"1");
															like.setSelected(true);
															ToastUtil
																	.shortToast(
																			context,
																			"关注成功");
														}
													}
												}
											});
								};

								public void watchMatchFailure(String error) {
									((Activity) context)
											.runOnUiThread(new Runnable() {
												public void run() {
													v.setEnabled(true);
													mDialog.dismiss();
												}
											});
								};
							});
				} else {
					context.startActivity(new Intent(context,
							LoginActivity.class));
				}
			}
		});

		return convertView;
	}

	private void startFlick(View view) {
		if (null == view) {
			return;
		}
		Animation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setInterpolator(new LinearInterpolator());
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		view.startAnimation(alphaAnimation);
	}

	/**
	 * 取消View闪烁效果
	 * 
	 * */
	private void stopFlick(View view) {
		if (null == view) {
			return;
		}
		view.clearAnimation();
	}

	/** 刷新数据 */
	private RefreshDataListener mRefreshDataListener;

	public void setRefreshDataListener(RefreshDataListener refreshDataListener) {
		mRefreshDataListener = refreshDataListener;
	}

	/**
	 * 自定义刷新数据接口
	 * 
	 * @author 秋风
	 * 
	 */
	public interface RefreshDataListener {
		void refresh();
	}
}
