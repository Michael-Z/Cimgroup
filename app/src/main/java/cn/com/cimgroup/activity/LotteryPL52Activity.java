package cn.com.cimgroup.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseLoteryActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num.PlayMenuItemClick;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.DragLayout;
import cn.com.cimgroup.view.LBGridView;
import cn.com.cimgroup.view.LBGridviewController;

/**
 * 排列5购彩
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryPL52Activity extends BaseLoteryActivity implements
		PlayMenuItemClick {

	private TextView mTouzhuHint;
	private TextView mTouzhuConfig;
	private LBGridviewController mWwController;
	private LBGridviewController mQwController;
	private LBGridviewController mBwController;
	private LBGridviewController mSwController;
	private LBGridviewController mGwController;
	private PopupWndSwitchPLayMenu mPopMenu;
	private TextView mIssueTextView;
	private TextView mIssueTimeTextView;
	private ListView mLotteryDrawListView;

	private DragLayout mDragLayout;

	private RelativeLayout bottomTZ;

	private TextView mJxtView;

	private ArrayList<Integer> list1;
	private ArrayList<Integer> list2;
	private ArrayList<Integer> list3;
	private ArrayList<Integer> list4;
	private ArrayList<Integer> list5;

	@Override
	protected void onHandleMessage(Message message) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_pl5);
		initView();
		Intent intent = getIntent();
		isCartForResult = intent.getBooleanExtra(
				GlobalConstants.CART_IS_FORRESULT, false);
		if (isCartForResult) {
			showIntentData(intent);
		}
		initTzHint();
	}

	private void showIntentData(Intent intent) {
		ArrayList<Integer> list1 = intent
				.getIntegerArrayListExtra(GlobalConstants.BALL_LIST1);
		ArrayList<Integer> list2 = intent
				.getIntegerArrayListExtra(GlobalConstants.BALL_LIST2);
		ArrayList<Integer> list3 = intent
				.getIntegerArrayListExtra(GlobalConstants.BALL_LIST3);
		ArrayList<Integer> list4 = intent
				.getIntegerArrayListExtra(GlobalConstants.BALL_LIST4);
		ArrayList<Integer> list5 = intent
				.getIntegerArrayListExtra(GlobalConstants.BALL_LIST5);
		if (list1 == null || list2 == null || list3 == null || list4 == null
				|| list5 == null) {
			this.list1 = new ArrayList<Integer>();
			this.list2 = new ArrayList<Integer>();
			this.list3 = new ArrayList<Integer>();
			this.list4 = new ArrayList<Integer>();
			this.list5 = new ArrayList<Integer>();
		} else {
			this.list1 = new ArrayList<Integer>(list1);
			this.list2 = new ArrayList<Integer>(list2);
			this.list3 = new ArrayList<Integer>(list3);
			this.list4 = new ArrayList<Integer>(list4);
			this.list5 = new ArrayList<Integer>(list5);
		}
		if (null != list1 && !list1.isEmpty()) {
			mWwController.insertSelect(list1);
			mQwController.insertSelect(list2);
			mBwController.insertSelect(list3);
			mSwController.insertSelect(list4);
			mGwController.insertSelect(list5);
		}
	}

	private void initView() {

		TextView mWordTextView = (TextView) findViewById(R.id.textView_title_word);
		mWordTextView.setText(getResources().getString(R.string.lottery_p5));

		mJxtView = (TextView) findViewById(R.id.textView_lottery_jx);
		mJxtView.setText(getResources().getString(R.string.btn_clear));
		mJxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
				.getDrawable(R.drawable.icon_trash_all), null, null);

		mTouzhuHint = (TextView) findViewById(R.id.textView_touzhu_hint);
		// mTouzhuStop = (TextView) findViewById(R.id.textView_touzhu_stop);
		mTouzhuConfig = (TextView) findViewById(R.id.rl_touzhu_config);
		bottomTZ = (RelativeLayout) findViewById(R.id.tz_layout);
		TextView mHintTextView = (TextView) findViewById(R.id.sv_hint);
		mHintTextView.setText(LotteryShowUtil.setRedText(mActivity,
				getResources().getString(R.string.lottery_p5_init),
				100000 + getResources().getString(R.string.lemi_unit)));
		((ImageView) findViewById(R.id.textView_title_menu))
				.setVisibility(View.VISIBLE);
		LBGridView mWwGridView = (LBGridView) findViewById(R.id.gridView_pl5_ww);
		LBGridView mQwGridView = (LBGridView) findViewById(R.id.gridView_pl5_qw);
		LBGridView mBwGridView = (LBGridView) findViewById(R.id.gridView_pl5_bw);
		LBGridView mSwGridView = (LBGridView) findViewById(R.id.gridView_pl5_sw);
		LBGridView mGwGridView = (LBGridView) findViewById(R.id.gridView_pl5_gw);
		mWwController = mWwGridView.getController();
		mQwController = mQwGridView.getController();
		mBwController = mBwGridView.getController();
		mSwController = mSwGridView.getController();
		mGwController = mGwGridView.getController();
		mWwGridView.setCallBack(this);
		mQwGridView.setCallBack(this);
		mBwGridView.setCallBack(this);
		mSwGridView.setCallBack(this);
		mGwGridView.setCallBack(this);

		mIssueTextView = (TextView) findViewById(R.id.issue_tv);
		mIssueTimeTextView = (TextView) findViewById(R.id.issue_time_tv);
		mLotteryDrawListView = (ListView) findViewById(R.id.listView_pl5_lv_multerm);
		mDragLayout = (DragLayout) findViewById(R.id.dragLayout_pl5);
		((ImageView) findViewById(R.id.imageView_lottery_shake))
				.setOnClickListener(this);
	}

	private void initTzHint() {
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney(
				mWwController.getSelectData(), mQwController.getSelectData(),
				mBwController.getSelectData(), mSwController.getSelectData(),
				mGwController.getSelectData()));
	}

	private void clearData() {
		mWwController.clearSelectState();
		mQwController.clearSelectState();
		mBwController.clearSelectState();
		mSwController.clearSelectState();
		mGwController.clearSelectState();
		initTzHint();
	}

	@Override
	public void onClick(View v) {
		ArrayList<Integer> wwList = mWwController.getSelectData();
		ArrayList<Integer> qwList = mQwController.getSelectData();
		ArrayList<Integer> bwList = mBwController.getSelectData();
		ArrayList<Integer> swList = mSwController.getSelectData();
		ArrayList<Integer> gwList = mGwController.getSelectData();
		switch (v.getId()) {
		case R.id.textView_title_back:
			Intent data1 = new Intent();
			setIntent(list1, list2, list3, list4, list5, data1);
			setResult(RESULT_OK, data1);
			mActivity.finish();
			break;
		case R.id.textView_title_menu:
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchPLayMenu(mActivity, this, true);
			}
			mPopMenu.showPopWindow(v);
			break;
		case R.id.textView_lottery_jx:
			clearData();
			break;
		case R.id.issue_time_tv:
			if (mDragLayout.isOpen()) {
				mDragLayout.close(DragLayout.PART_1);
				break;
			}
			mDragLayout.open(DragLayout.PART_1);
			break;
		case R.id.imageView_lottery_shake:
			onShakeEvent();
			break;
		case R.id.rl_touzhu_config:
			if (wwList.size() < 1 || qwList.size() < 1 || bwList.size() < 1
					|| swList.size() < 1 || gwList.size() < 1) {
				onShakeEvent();
				return;
			}

			if (wwList.size() * qwList.size() * bwList.size() * swList.size()
					* gwList.size() * 2 > GlobalConstants.LOTTERY_MAX_PRICE) {
				showToast(getResources().getString(R.string.lottery_money_than));
				return;
			}
			// if (isCartForResult) {
			Intent data = new Intent();
			setIntent(wwList, qwList, bwList, swList, gwList, data);
			setResult(RESULT_OK, data);
			mActivity.finish();
			// } else {
			// Intent intent = new Intent(mActivity,
			// LotteryPL5CartActivity.class);
			// setIntent(wwList, qwList, bwList, swList, gwList, intent);
			// startActivity(intent);
			// mActivity.finish();
			// // }
			break;

		default:
			break;
		}
	}

	private void setIntent(ArrayList<Integer> wwList,
			ArrayList<Integer> qwList, ArrayList<Integer> bwList,
			ArrayList<Integer> swList, ArrayList<Integer> gwList, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE,
				GlobalConstants.LOTTERY_P5_CART);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, wwList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, qwList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, bwList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST4, swList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST5, gwList);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL5));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		if (parent == mWwController.getGridView()) {
			mWwController.updateSelect(position);
		} else if (parent == mQwController.getGridView()) {
			mQwController.updateSelect(position);
		} else if (parent == mBwController.getGridView()) {
			mBwController.updateSelect(position);
		} else if (parent == mSwController.getGridView()) {
			mSwController.updateSelect(position);
		} else if (parent == mGwController.getGridView()) {
			mGwController.updateSelect(position);
		}
		initTzHint();
	}

	@Override
	protected int getLotteryName() {
		return GlobalConstants.LOTTERY_PL5;
	}

	@Override
	public void onShake() {
		super.onShake();
		mWwController.randomBall(mWwController.getSelectData(), 1, 10);
		mQwController.randomBall(mQwController.getSelectData(), 1, 10);
		mBwController.randomBall(mBwController.getSelectData(), 1, 10);
		mSwController.randomBall(mSwController.getSelectData(), 1, 10);
		mGwController.randomBall(mGwController.getSelectData(), 1, 10);
		initTzHint();
	}

	@Override
	protected void onShakeEvent() {
		super.onShakeEvent();
		mWwController.randomBall(mWwController.getSelectData(), 1, 10);
		mQwController.randomBall(mQwController.getSelectData(), 1, 10);
		mBwController.randomBall(mBwController.getSelectData(), 1, 10);
		mSwController.randomBall(mSwController.getSelectData(), 1, 10);
		mGwController.randomBall(mGwController.getSelectData(), 1, 10);
		initTzHint();
	}

	@Override
	protected TextView getIssueTextView() {
		return mIssueTextView;
	}

	@Override
	protected TextView getIssueTimeTextView() {
		return mIssueTimeTextView;
	}

	@Override
	protected ListView getMulTermListView() {
		return mLotteryDrawListView;
	}

	@Override
	protected TextView getTZTextView() {
		return mTouzhuHint;
	}

	@Override
	protected TextView getConfigRelativeLayout() {
		return mTouzhuConfig;
	}

	@Override
	public void PopJx(int num) {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		ArrayList<Integer> list3 = new ArrayList<Integer>();
		ArrayList<Integer> list4 = new ArrayList<Integer>();
		ArrayList<Integer> list5 = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			LotteryShowUtil.randomAddBall(list1, 1, 10);
			LotteryShowUtil.randomAddBall(list2, 1, 10);
			LotteryShowUtil.randomAddBall(list3, 1, 10);
			LotteryShowUtil.randomAddBall(list4, 1, 10);
			LotteryShowUtil.randomAddBall(list5, 1, 10);
		}

		Intent data = new Intent();
		setIntent(list1, list2, list3, list4, list5, data);
		data.putExtra(GlobalConstants.LIST_JXNUM, num);
		data.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		data.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText()
				.toString());
		data.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME,
				LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_PL5));
		setResult(RESULT_OK, data);
		mActivity.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent data1 = new Intent();
			setIntent(list1, list2, list3, list4, list5, data1);
			setResult(RESULT_OK, data1);
			mActivity.finish();
		}
		return false;
	}

	@Override
	public void PopZST() {
		Intent intent=new Intent(this,ZSTWebViewActivity.class);
		intent.putExtra("url", GlobalConstants.getZSTUrl(2));
		intent.putExtra("title", "排列五");
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		startActivity(intent);
	}
}
