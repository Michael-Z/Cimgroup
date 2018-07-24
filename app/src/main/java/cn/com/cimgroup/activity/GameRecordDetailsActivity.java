package cn.com.cimgroup.activity;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GuessRecordBean;
import cn.com.cimgroup.bean.YlcRaceRecordBean;
import cn.com.cimgroup.util.DensityUtils;

/**
 * 游戏竞猜详情
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class GameRecordDetailsActivity extends BaseActivity implements
		OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_recored_details);
		// 初始化视图组件
		initView();
		// 绑定事件
		initEvent();
		// 初始化数据
		initDatas();
	}
	
	/** 是否显示布局 */
	private boolean isShow = false;
	/** 签到部分布局 */
	private LinearLayout id_remind_rule_layout;

	private LinearLayout id_remind_text_layout;
	/** 签到规则信息位移距离 */
	private float mMoveLength = 0;
	/** 签到说明状态 */
	private static final int SIGN_TEXT_STATE = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SIGN_TEXT_STATE:
				id_remind_text_layout.setVisibility(id_remind_text_layout
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


	/**
	 * 初始化数据
	 */
	private void initDatas() {
		
		TextView id_answer = (TextView) findViewById(R.id.id_answer);
		GuessRecordBean bean = (GuessRecordBean) getIntent()
				.getSerializableExtra("data");
//		bean.setStatus("3");
		//题目状态
		String status = bean.getStatus();
		//中奖金额
		Spanned winMoney =null;
		//竞猜题目
		String question = String.format("问：%s",bean.getQuestion());
		//问题答案
		String answer = "";
		int imageState =0;
		if (status.equals("0")) {
			((TextView) findViewById(R.id.id_answer)).setText("等待开奖");
			imageState = R.drawable.e_ui_detail_wei;
			winMoney = Html.fromHtml("耐心点， <big>还没开奖呢！</big>");
		} else if (status.equals("1")) {
			answer = bean.getLotteryResult().equals("1") ? "是" : "否";
			imageState = R.drawable.e_ui_detail_win;
			String str =TextUtils.isEmpty(bean.getReturnAmount())?"0 米":bean.getReturnAmount() + "米";
			winMoney = Html.fromHtml("恭喜您中了：<big>"+str+"</big>");
		}else if (status.equals("2")) {
			answer = bean.getLotteryResult().equals("1") ? "是" : "否";
			winMoney = Html.fromHtml("没中? <big>忍不了啊！</big>");
			imageState = R.drawable.e_ui_detail_fail;
		}else if (status.equals("3")) {
			answer ="因特殊原因导致题目作废并返还本金";
			question = "竞猜取消" ;
//			winMoney =Html.fromHtml("返款：<big>"+bean.getBetAmount()+"米</big>");
			imageState = R.drawable.e_ui_detail_error;
			id_answer.setTextColor(getResources().getColor(R.color.color_red));
			
			
		}
		YlcRaceRecordBean rBean = bean.getRecordBean();
		//是选项奖池
		String definitePool = rBean.getDefinitePool()+" 米";
		//否选项奖池
		String negativePool = rBean.getNegativePool()+" 米";
		//是选项参与人数
		String definiteUsers = String.format("已有%s人选择", rBean.getDefiniteUsers());
		//否选项参与人数
		String negativeUsers = String.format("已有%s人选择", rBean.getNegativeUsers());
		
		
		
		
		
		((TextView) findViewById(R.id.id_time)).setText(bean.getAddTime());
		((TextView) findViewById(R.id.id_team)).setText(bean.getHostName()
				+ "   vs   " + bean.getGuestName());
		((TextView) findViewById(R.id.id_option)).setText(bean.getContent().equals("1") ? "是" : "否");
		((TextView) findViewById(R.id.id_betting_money)).setText(bean.getBetAmount() + "米");
		id_answer.setText(answer);
		if (!status.equals("3")) {
			((TextView) findViewById(R.id.id_win_money)).setText(winMoney);
		}else {
			findViewById(R.id.id_win_money).setVisibility(View.GONE);
		}
		
		((ImageView)findViewById(R.id.id_imageview_state)).setImageResource(imageState);
		((TextView) findViewById(R.id.id_question)).setText(question);
		
		((TextView)findViewById(R.id.id_tzlemi_yes)).setText(definitePool);
		((TextView)findViewById(R.id.id_tzlemi_no)).setText(negativePool);
		((TextView)findViewById(R.id.id_tzuser_yes)).setText(definiteUsers);
		((TextView)findViewById(R.id.id_tzuser_no)).setText(negativeUsers);

	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_to_guess).setOnClickListener(this);
		findViewById(R.id.id_remind_rule).setOnClickListener(this);
		findViewById(R.id.id_get_remind).setOnClickListener(this);
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_remind_rule_layout = (LinearLayout) findViewById(R.id.id_remind_rule_layout);
		id_remind_text_layout = (LinearLayout) findViewById(R.id.id_remind_text_layout);
		
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		id_remind_text_layout.measure(w, h);
		mMoveLength = id_remind_text_layout.getMeasuredHeight()+DensityUtils.dip2px(5);
		id_remind_rule_layout.setPadding(0, 0, 0, -(int) mMoveLength);
		id_remind_rule_layout.requestLayout();
		id_remind_text_layout.setVisibility(View.INVISIBLE);
	}
	/**
	 * 显示或隐藏视图
	 */
	@SuppressLint("NewApi")
	public void showView() {
		if (isShow) {
			// 隐藏布局，逐渐减少父布局的padding值
			ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float alpha = animation.getAnimatedFraction()
							* (mMoveLength);
					id_remind_rule_layout.setPadding(0, 0, 0, -(int) alpha);
					id_remind_rule_layout.requestLayout();
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
					id_remind_rule_layout.setPadding(0, 0, 0,
							-(int) (mMoveLength - alpha));
					id_remind_rule_layout.requestLayout();
				}
			});
			animator.setDuration(800);
			animator.start();
			id_remind_rule_layout.setVisibility(View.VISIBLE);
		}
		isShow = !isShow;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_back:
			finish();
			break;
		case R.id.id_to_guess:
			startActivity(GameGuessMatchActivity.class);
			finish();
			break;
		case R.id.id_remind_rule:
			showView();
			break;
		case R.id.id_get_remind:
			jumpRemind();
			break;
		default:
			break;
		}
	}
	/** 跳转玩法说明 */
	public void jumpRemind(){
		Intent intent = new Intent();
		intent.putExtra(TextActiity.TYPE, TextActiity.GAMEHELP);
		intent.setClass(GameRecordDetailsActivity.this, TextActiity.class);
		startActivity(intent);
	}
}
