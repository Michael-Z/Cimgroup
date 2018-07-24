package cn.com.cimgroup.dailog;

import java.math.BigDecimal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 竞猜游戏投注Dialog
 * 
 * @author 秋风
 * 
 */
public class GuessBettingDialog extends BaseDialog implements
		View.OnClickListener {
	private View mView;
	private GuessBettingCallBack mCallBack;
	/** 投注乐米数输入框 */
	private EditText id_betting_num;

	/** 投注乐米数 */
	private TextView id_betting_50, id_betting_200, id_betting_500,
			id_betting_auto;
	/** 乐米数显示 */
	private TextView id_betting_num_show;
	/** 提交 */
	private TextView id_commit;
	/** 取消 */
	private TextView id_cancel;
	/** 选择类型 */
	private String mBettingType;
	/** 题目Id */
	private String mQuestionNo;

	private Context mContext;
	private InputMethodManager imm;
	/**黑色*/
	private int mColorBlack = 0;

	public GuessBettingDialog(Context context) {
		super(context);
		mContext = context;
		mColorBlack =mContext.getResources().getColor(R.color.color_gray_secondary);
		imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mView = View.inflate(context, R.layout.dialog_guess_betting, null);
		initView();
		initEvent();
		setContentView(mView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	/**
	 * 初始化Dialog
	 */
	private void initView() {
		id_betting_num = (EditText) mView.findViewById(R.id.id_betting_num);
		id_betting_num_show = (TextView) mView
				.findViewById(R.id.id_betting_num_show);
		id_betting_50 = (TextView) mView.findViewById(R.id.id_betting_50);
		id_betting_200 = (TextView) mView.findViewById(R.id.id_betting_200);
		id_betting_500 = (TextView) mView.findViewById(R.id.id_betting_500);
		id_betting_auto = (TextView) mView.findViewById(R.id.id_betting_auto);
		id_commit = (TextView) mView.findViewById(R.id.id_commit);
		id_cancel = (TextView) mView.findViewById(R.id.id_cancel);
		// float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
		// int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
		// float xdpi = dm.xdpi;
		// float ydpi = dm.ydpi;
		initStyle(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		id_betting_num.addTextChangedListener(watcher);
	}

	private void initEvent() {
		id_betting_50.setOnClickListener(this);
		id_betting_200.setOnClickListener(this);
		id_betting_500.setOnClickListener(this);
		id_betting_auto.setOnClickListener(this);
		id_commit.setOnClickListener(this);
		id_cancel.setOnClickListener(this);

		id_betting_num
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							v.setVisibility(View.GONE);
							setText(id_betting_num_show, ((EditText) v)
									.getText().toString().trim());
							id_betting_num_show.setVisibility(View.VISIBLE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						}
					}
				});
	}

	/**
	 * 显示Dialog
	 * 
	 * @param bettingType
	 * @param questionNo
	 */
	public void showDialog(String bettingType, String questionNo) {
		mQuestionNo = questionNo;
		mBettingType = bettingType;
		if (id_betting_num.isFocusable()) {
			id_betting_num.setFocusable(false);
		id_betting_num_show.setVisibility(View.VISIBLE);
		id_betting_num.setVisibility(View.GONE);
		imm.hideSoftInputFromWindow(
				id_betting_num.getWindowToken(), 0);
		}
		
		setText(id_betting_num, "");
		setText(id_betting_num_show, "");
		changeSelected(null);
		id_betting_num_show.setText("10");
		id_betting_num.setText("10");
		show();
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String num = s.toString();
			String lemiS = num;
			if (!TextUtils.isEmpty(s)) {
				double lemi = Double.valueOf(num);
				// BigDecimal money = new BigDecimal(
				// App.userInfo.getIntegralAcnt());
				if (lemi > 1998) {
					lemiS = "1998";
					setText(id_betting_num, lemiS);
				}
				setText(id_betting_num_show, lemiS);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_betting_50:
			//修改选中效果
			changeSelected(id_betting_50);
			if (id_betting_num_show.getVisibility() == View.GONE) {
				id_betting_num.setFocusable(false);
				id_betting_num_show.setVisibility(View.VISIBLE);
				id_betting_num.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(id_betting_num.getWindowToken(), 0);
			}
			setText(id_betting_num_show, "50");
			setText(id_betting_num, "50");
			break;
		case R.id.id_betting_200:
			changeSelected(id_betting_200);
			if (id_betting_num_show.getVisibility() == View.GONE) {
				id_betting_num.setFocusable(false);
				id_betting_num_show.setVisibility(View.VISIBLE);
				id_betting_num.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(id_betting_num.getWindowToken(), 0);
			}

			setText(id_betting_num_show, "200");
			setText(id_betting_num, "200");
			break;
		case R.id.id_betting_500:
			changeSelected(id_betting_500);
			if (id_betting_num_show.getVisibility() == View.GONE) {
				id_betting_num.setFocusable(false);
				id_betting_num_show.setVisibility(View.VISIBLE);
				id_betting_num.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(id_betting_num.getWindowToken(), 0);
			}

			setText(id_betting_num_show, "500");
			setText(id_betting_num, "500");
			break;
		case R.id.id_betting_auto:
			changeSelected(id_betting_auto);
			id_betting_num_show.setVisibility(View.GONE);
			id_betting_num.setVisibility(View.VISIBLE);
			id_betting_num.setFocusable(true);
			id_betting_num.setFocusableInTouchMode(true);
			id_betting_num.requestFocus();
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			String text = id_betting_num_show.getText().toString().trim();
			setText(id_betting_num, text);
			id_betting_num.setSelection(TextUtils.isEmpty(text) ? 0 : text
					.length());

			break;
		case R.id.id_commit:
			String lemi = id_betting_num.getText().toString().trim();
			if (TextUtils.isEmpty(lemi)) {
				ToastUtil.shortToast(mContext, "请输入投注金额");
			} else if (Integer.parseInt(lemi) < 2) {
				ToastUtil.shortToast(mContext, "最小投注金额为2乐米");
			} else if (App.userInfo == null
					|| TextUtils.isEmpty(App.userInfo.getUserId())) {
				mContext.startActivity(new Intent(mContext, LoginActivity.class));
			} else if (BigDecimal.valueOf(Double.parseDouble(lemi)).compareTo(
					BigDecimal.valueOf(Double.parseDouble(App.userInfo
							.getIntegralAcnt()))) == 1) {
				ToastUtil.shortToast(mContext, "乐米不足");
			} else {
				if (!TextUtils.isEmpty(lemi) && mCallBack != null) {
					int lemiNum = Integer.parseInt(lemi);
					if (lemiNum >= 2) {
						if (lemiNum % 2 == 1) {
							lemiNum--;
						}
						// 重置键盘
						dismiss();
						mCallBack.commit("" + lemiNum, mBettingType,
								mQuestionNo);
					} else
						ToastUtil.shortToast(mContext, "投注最少两米");

				}
			}
			break;
		case R.id.id_cancel:
			dismiss();
			imm.hideSoftInputFromWindow(id_betting_num.getWindowToken(), 0);
			setText(id_betting_num, "");
			setText(id_betting_num_show, "");
			changeSelected(null);
			break;
		default:
			break;
		}
	}
	/**
	 * 修改投注按钮的选中效果
	 * @param id_betting_502
	 */
	private void changeSelected(TextView id_betting_502) {
		id_betting_50.setSelected(false);
		id_betting_200.setSelected(false);
		id_betting_500.setSelected(false);
		id_betting_auto.setSelected(false);
		id_betting_50.setTextColor(mColorBlack);
		id_betting_200.setTextColor(mColorBlack);
		id_betting_500.setTextColor(mColorBlack);
		id_betting_auto.setTextColor(mColorBlack);
		if (id_betting_502!=null) {
			id_betting_502.setSelected(true);
			id_betting_502.setTextColor(Color.WHITE);
		}
		
		
	}

	public void setOnGuessBettingCallBack(GuessBettingCallBack callback) {
		mCallBack = callback;
	}

	/**
	 * 回调监听
	 * 
	 * @author 秋风
	 * 
	 */
	public interface GuessBettingCallBack {
		/** 确认 */
		void commit(String lemi, String bettingType, String questionNo);
	}

	private void setText(TextView view, String lemi) {
		view.setText(lemi);
	}

}
