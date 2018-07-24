package cn.com.cimgroup.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.SignAdapter;
import cn.com.cimgroup.bean.Sign;
import cn.com.cimgroup.bean.SignList;
import cn.com.cimgroup.bean.SignStatus;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.view.NotScrollGridView;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class SignActivity extends BaseActivity implements OnClickListener {

	private String mTitleText = "";

	private NotScrollGridView gridView;

	private SignAdapter adapter;

	private int totalCount = DateUtil.getDaysOfMonth(DateUtil.getToYear(),
			DateUtil.getToMonth());

	private List<Sign> list = new ArrayList<Sign>();

	private int num = 0;

	private int week = DateUtil.getWeekDayNum(DateUtil.getToYear() + "-"
			+ DateUtil.getToMonth() + "-01");

	String month = "";

	private TextView signText;

	private int contiSignNum = 0;

	private TextView lemiText;

	private LinearLayout id_sign_text_layout;
	/** 签到规则按钮 */
	private TextView id_sign_rule;
	/** 签到部分布局 */
	private LinearLayout id_sign_rule_layout;

	/** 签到规则信息位移距离 */
	private float mMoveLength = 0;
	/** 签到说明状态 */
	private static final int SIGN_TEXT_STATE = 1;
	/** 缩放动画 */
	private ScaleAnimation scaleAnimation;
	/** 旋转动画 */
	private RotateAnimation mRotateAnimation;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SIGN_TEXT_STATE:
				id_sign_text_layout.setVisibility(id_sign_text_layout
						.getVisibility() == View.INVISIBLE ? View.VISIBLE
						: View.INVISIBLE);
				break;
			case 2:
				showView();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		initView();
		initEvent();
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		id_sign_text_layout.measure(w, h);
		mMoveLength = id_sign_text_layout.getMeasuredHeight() * 3 / 2 + 20;
		id_sign_rule_layout.setPadding(0, 0, 0, -(int) mMoveLength);
		id_sign_rule_layout.requestLayout();
		id_sign_text_layout.setVisibility(View.INVISIBLE);

		// scaleAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
		// Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// scaleAnimation.setRepeatCount(1);
		// scaleAnimation.setRepeatMode(Animation.REVERSE);
		// scaleAnimation.setDuration(500);
		// // animation = new
		// ScaleAnimation(this,getResources().getAnimation(R.anim.animation_scale_sign));
		// // animation.setDuration(1000);
		// /** 常用方法 */
		// scaleAnimation.setAnimationListener(scaleAnimationListener);
		//
		// mRotateAnimation=new
		// RotateAnimation(0.0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		// // mRotateAnimation.setRepeatCount(1);
		// // mRotateAnimation.setRepeatMode(Animation.REVERSE);
		// mRotateAnimation.setDuration(300);
		// mRotateAnimation.setStartTime(100);
		// mRotateAnimation.setAnimationListener(rotateAnimationListener);

	}

	private AnimationListener scaleAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mHandler.sendEmptyMessage(2);
		}
	};
	private AnimationListener rotateAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mHandler.sendEmptyMessage(2);
		}
	};

	private void initEvent() {
		id_sign_rule.setOnClickListener(this);
	}

	private void initView() {
		id_sign_rule_layout = (LinearLayout) findViewById(R.id.id_sign_rule_layout);

		mTitleText = getResources().getString(R.string.lotteryhall_sign);
		// title 左侧的文字
		TextView mTitleName = (TextView) findViewById(R.id.textView_title_word);
		mTitleName.setText(mTitleText);

		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);

		id_sign_rule = (TextView) findViewById(R.id.id_sign_rule);

		signText = (TextView) findViewById(R.id.textView_sign_sign);

		lemiText = (TextView) findViewById(R.id.textView_sign_lemi);
		if (!StringUtil.isEmpty(App.userInfo.getIntegralAcnt())) {
			String leArr = App.userInfo.getIntegralAcnt().split("\\.")[0];
			if (leArr.length() > 5) {
				lemiText.setText(leArr.substring(0, leArr.length() - 4)
						+ getResources().getString(R.string.price_ten_thousand)
						+ getResources().getString(R.string.lemi_unit));
			} else {
				lemiText.setText(App.userInfo.getIntegralAcnt()
						+ getResources().getString(R.string.lemi_unit));
			}
		}

		id_sign_text_layout = (LinearLayout) findViewById(R.id.id_sign_text_layout);

		gridView = (NotScrollGridView) findViewById(R.id.gridView_sign);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		switch (week) {
		case 0:
			num = 6;
			break;
		case 1:
			num = 0;
			break;
		case 2:
			num = 1;
			break;
		case 3:
			num = 2;
			break;
		case 4:
			num = 3;
			break;
		case 5:
			num = 4;
			break;
		case 6:
			num = 5;
			break;
		default:
			break;
		}

		for (int i = 0; i < num; i++) {
			Sign sign = new Sign();
			sign.setDay("");
			sign.setSelect(false);
			list.add(sign);
		}

		for (int i = 0; i < totalCount; i++) {
			Sign sign = new Sign();
			sign.setDay((i + 1) + "");
			sign.setSelect(false);
			list.add(sign);
		}
		adapter = new SignAdapter(SignActivity.this, list);
		gridView.setAdapter(adapter);
		signText.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (App.userInfo != null && App.userInfo.getUserId() != null) {
			if (App.userInfo.getResCode().equals("3002")) {
				App.userInfo.setResCode("0");
				UserLogic.getInstance().saveUserInfo(App.userInfo);
				finish();
			}

			if (DateUtil.getToMonth() <= 9) {
				month = "0" + DateUtil.getToMonth();
			} else {
				month = DateUtil.getToMonth() + "";
			}
			showLoadingDialog();
			Controller.getInstance().getSignList(GlobalConstants.NUM_SIGNLIST,
					App.userInfo.getUserId(),
					DateUtil.getToYear() + "-" + month + "-01",
					DateUtil.getToYear() + "-" + month + "-" + totalCount, "0",
					mBack);
		} else {
			App.userInfo = null;
			UserLogic.getInstance().saveUserInfo(App.userInfo);
			startActivity(LoginActivity.class);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	}

	private CallBack mBack = new CallBack() {

		public void getSignListSuccess(final SignList signList) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (signList.getList() != null) {
						adapter.setData(signList.getList());

						int all = 0;
						if (signList.getList().size() > 7) {
							all = 7;
						} else {
							all = signList.getList().size();
						}

						for (int i = 0; i < all; i++) {
							Sign sign1 = signList.getList().get(i);

							if (i == 0) {
								if (DateUtil.getToday()
										- Integer.parseInt(sign1.getDay()) == 1) {
									contiSignNum++;
								} else {
									if (DateUtil.getToday() == Integer
											.parseInt(sign1.getDay())) {
										signText.setEnabled(false);
										contiSignNum++;
									} else {
										break;
									}
								}
							} else {
								Sign sign2 = signList.getList().get(i - 1);
								if ((Integer.parseInt(sign2.getDay()) - Integer
										.parseInt(sign1.getDay())) == 1) {
									contiSignNum++;
								} else {
									break;
								}
							}
						}
						if (signText.isEnabled()) {
							if (contiSignNum < 7) {
								signText.setText("签到+" + (contiSignNum + 1)
										* 10);
							} else {
								signText.setText("签到+" + 70);
							}
						} else {
							if (contiSignNum < 7) {
								signText.setText("明日签到+" + (contiSignNum + 1)
										* 10);
							} else {
								signText.setText("明日签到+" + 70);
							}
						}
					}
					contiSignNum = 0;
				}
			});
		};

		public void getSignListError(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
				}
			});
		};

		public void signSuccess(final SignStatus status) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (status.getResCode().equals("0")) {
						List<Sign> signs = adapter.getData();
						Sign sign = new Sign();
						sign.setDay(DateUtil.getToday() + "");
						sign.setSelect(true);
						sign.setResCode("0");
						signs.add(0, sign);
						adapter.setData(signs);
						signText.setEnabled(false);
						BigDecimal b1 = new BigDecimal(
								App.userInfo.getIntegralAcnt());
						BigDecimal b2 = new BigDecimal(
								Integer.parseInt(signText.getText().toString()
										.split("\\+")[1]));
						String lemi = b1.add(b2).toString();
						App.userInfo.setIntegralAcnt(lemi);
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						if (!StringUtil.isEmpty(lemi)) {
							String leArr = lemi.split("\\.")[0];
							if (leArr.length() > 5) {
								// lemiText.setText(leArr.substring(0,
								// leArr.length() - 4) +
								// getResources().getString(R.string.price_ten_thousand));
								lemiText.setText(leArr.substring(0,
										leArr.length() - 4)
										+ getResources().getString(
												R.string.price_ten_thousand)
										+ getResources().getString(
												R.string.lemi_unit));

							} else {
								// lemiText.setText(lemi);
								lemiText.setText(App.userInfo.getIntegralAcnt()
										+ getResources().getString(
												R.string.lemi_unit));
							}
						}
						ToastUtil.shortToast(SignActivity.this, "签到成功");
						Controller.getInstance().getSignList(
								GlobalConstants.NUM_SIGNLIST,
								App.userInfo.getUserId(),
								DateUtil.getToYear() + "-" + month + "-01",
								DateUtil.getToYear() + "-" + month + "-"
										+ totalCount, "0", mBack);
					} else {
						ToastUtil.shortToast(SignActivity.this,
								status.getResMsg());
					}
				}
			});
		};

		public void signError(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					signText.setEnabled(true);
				}
			});
		};
	};
	/** 是否显示布局 */
	private boolean isShow;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			finish();
			break;
		case R.id.textView_sign_sign:
			signText.setEnabled(false);// DateUtil.currDay()
			Controller.getInstance().sign(GlobalConstants.NUM_SIGN,
					App.userInfo.getUserId(), "",
					DateUtil.getToYear() + "-" + month + "-01",
					DateUtil.getToYear() + "-" + month + "-" + totalCount, "0",
					mBack);
			break;
		case R.id.id_sign_rule:
			// if (id_sign_text_layout.getVisibility() == View.VISIBLE) {
			// id_sign_text_layout.setVisibility(View.GONE);
			// } else {
			// id_sign_text_layout.setVisibility(View.VISIBLE);
			// }
			showView();
			// 以下 为添加动画效果
			// if (!isShow) {
			// id_sign_rule.startAnimation(scaleAnimation);
			// } else//mRotateAnimation
			// id_sign_rule.startAnimation(mRotateAnimation);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		contiSignNum = 0;
		super.onStop();
	}

	/**
	 * 显示或隐藏视图
	 */
	public void showView() {
		if (isShow) {
			// 隐藏布局，逐渐减少父布局的padding值
			ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float alpha = animation.getAnimatedFraction()
							* (mMoveLength);
					id_sign_rule_layout.setPadding(0, 0, 0, -(int) alpha);
					id_sign_rule_layout.requestLayout();
				}
			});
			animator.setDuration(800);
			animator.start();
			mHandler.sendEmptyMessageDelayed(SIGN_TEXT_STATE, 800);

		} else {// 显示布局，逐渐增加父布局的padding值
			mHandler.sendEmptyMessage(SIGN_TEXT_STATE);
			ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float alpha = animation.getAnimatedFraction()
							* (mMoveLength);
					id_sign_rule_layout.setPadding(0, 0, 0,
							-(int) (mMoveLength - alpha));
					id_sign_rule_layout.requestLayout();
				}
			});
			animator.setDuration(800);
			animator.start();
			id_sign_rule_layout.setVisibility(View.VISIBLE);
		}
		isShow = !isShow;
	}

}
