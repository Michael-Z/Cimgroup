package cn.com.cimgroup.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseLoteryActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.frament.LoboHallFrament;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num;
import cn.com.cimgroup.popwindow.PopupWndSwitch_Jx_Num.PlayMenuItemClick;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.DragLayout;
import cn.com.cimgroup.view.LBGridView;
import cn.com.cimgroup.view.LBGridviewController;
import cn.com.cimgroup.xutils.ActivityManager;

/**
 * 七星彩购彩
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryQxcActivity extends BaseLoteryActivity implements PlayMenuItemClick {

	private TextView mTouzhuHint;
	private TextView mTouzhuStop;
	private TextView mTouzhuConfig;
	private LBGridviewController m1wController;
	private LBGridviewController m2wController;
	private LBGridviewController m3wController;
	private LBGridviewController m4wController;
	private LBGridviewController m5wController;
	private LBGridviewController m6wController;
	private LBGridviewController m7wController;
	private PopupWndSwitchPLayMenu mPopMenu;
	private TextView mIssueTextView;
	private TextView mIssueTimeTextView;
	private ListView mLotteryDrawListView;
	private TextView mHintTextView;
	
	private DragLayout mDragLayout;
	
	private RelativeLayout bottomTZ;
	private PopupWndSwitch_Jx_Num jxNum;

	@Override
	protected void onHandleMessage(Message message) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_qxc);
		initView();
		isCartForResult = intent.getBooleanExtra(GlobalConstants.CART_IS_FORRESULT, false);
		if (isCartForResult) {
			showIntentData(intent);
		}
		
		if (obj != null && source != 1) {
			promptDialog0.showDialog();
		}
		initTzHint();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isClear = intent.getBooleanExtra("isClear", true);
		if (isClear) {
			clearData();
			initTzHint();
		}
	}

	private void showIntentData(Intent intent) {
		ArrayList<Integer> list1 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST1);
		ArrayList<Integer> list2 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST2);
		ArrayList<Integer> list3 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST3);
		ArrayList<Integer> list4 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST4);
		ArrayList<Integer> list5 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST5);
		ArrayList<Integer> list6 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST6);
		ArrayList<Integer> list7 = intent.getIntegerArrayListExtra(GlobalConstants.BALL_LIST7);
		if (null != list1 && !list1.isEmpty()) {
			m1wController.insertSelect(list1);
			m2wController.insertSelect(list2);
			m3wController.insertSelect(list3);
			m4wController.insertSelect(list4);
			m5wController.insertSelect(list5);
			m6wController.insertSelect(list6);
			m7wController.insertSelect(list7);
		}
	}

	private void initView() {
		TextView mWordTextView = (TextView) findViewById(R.id.textView_title_word);
		mWordTextView.setText(getResources().getString(R.string.lottery_qxc));
		
		mHintTextView = (TextView) findViewById(R.id.sv_hint);
//		mHintTextView.setText(LotteryShowUtil.setRedText(mActivity, getResources().getString(R.string.lottery_qxc_sv_hint), 500 + getResources().getString(R.string.price_ten_thousand)));
		mHintTextView.setText(getResources().getString(R.string.lottery_qxc_sv_hint));
		((ImageView) findViewById(R.id.textView_title_menu)).setVisibility(View.VISIBLE);
		((ImageView) findViewById(R.id.imageView_lottery_shake)).setOnClickListener(this);
		bottomTZ = (RelativeLayout) findViewById(R.id.tz_layout);
		mTouzhuHint = (TextView) findViewById(R.id.textView_touzhu_hint);
//		mTouzhuStop = (TextView) findViewById(R.id.textView_touzhu_stop);
		mTouzhuConfig = (TextView) findViewById(R.id.rl_touzhu_config);
		LBGridView m1wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_1w);
		LBGridView m2wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_2w);
		LBGridView m3wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_3w);
		LBGridView m4wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_4w);
		LBGridView m5wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_5w);
		LBGridView m6wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_6w);
		LBGridView m7wGrigView = (LBGridView) findViewById(R.id.gridView_qxc_7w);
		mIssueTextView = (TextView) findViewById(R.id.issue_tv);
		mIssueTimeTextView = (TextView) findViewById(R.id.issue_time_tv);
		mLotteryDrawListView = (ListView) findViewById(R.id.listView_qxc_lv_multerm);
		mDragLayout = (DragLayout) findViewById(R.id.dragLayout_qxc);
		m1wController = m1wGrigView.getController();
		m2wController = m2wGrigView.getController();
		m3wController = m3wGrigView.getController();
		m4wController = m4wGrigView.getController();
		m5wController = m5wGrigView.getController();
		m6wController = m6wGrigView.getController();
		m7wController = m7wGrigView.getController();
		m1wGrigView.setCallBack(this);
		m2wGrigView.setCallBack(this);
		m3wGrigView.setCallBack(this);
		m4wGrigView.setCallBack(this);
		m5wGrigView.setCallBack(this);
		m6wGrigView.setCallBack(this);
		m7wGrigView.setCallBack(this);
	}

	private void initTzHint() {
		mTouzhuHint.setText(LotteryBettingUtil.setNumMoney(m1wController.getSelectData(), m2wController.getSelectData(), m3wController.getSelectData(), m4wController.getSelectData(), m5wController.getSelectData(), m6wController.getSelectData(), m7wController.getSelectData()));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			switch (source) {
			case 1:
				MainActivity mainActivity = (MainActivity) ActivityManager.isExistsActivity(MainActivity.class);
				if (mainActivity != null) {
					// 如果存在MainActivity实例，那么展示用户中心页面
					mainActivity.showFrament(0);
				}
				ActivityManager.retain(MainActivity.class);  
				break;

			default:
				mActivity.finish();
				break;
			}
			break;
		case R.id.textView_title_menu:
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchPLayMenu(mActivity, this,true);
			}
			mPopMenu.showPopWindow(v);
			break;
		case R.id.imageView_lottery_shake:
			onShakeEvent();
			break;
		case R.id.textView_lottery_jx:
			if (jxNum == null) {
				jxNum = new PopupWndSwitch_Jx_Num(mActivity, this);
			}
			jxNum.showListItemViewPopupWindow(mActivity, bottomTZ);
			break;
		case R.id.issue_time_tv:
			if (mDragLayout.isOpen()) {
				mDragLayout.close(DragLayout.PART_1);
				break;
			}
			mDragLayout.open(DragLayout.PART_1);
			break;
		case R.id.rl_touzhu_config:
			ArrayList<Integer> _1wList = m1wController.getSelectData();
			ArrayList<Integer> _2wList = m2wController.getSelectData();
			ArrayList<Integer> _3wList = m3wController.getSelectData();
			ArrayList<Integer> _4wList = m4wController.getSelectData();
			ArrayList<Integer> _5wList = m5wController.getSelectData();
			ArrayList<Integer> _6wList = m6wController.getSelectData();
			ArrayList<Integer> _7wList = m7wController.getSelectData();
			if (_1wList.size() < 1 || _2wList.size() < 1 || _3wList.size() < 1 || _4wList.size() < 1 || _5wList.size() < 1 || _6wList.size() < 1 || _7wList.size() < 1) {
				if (_1wList.size() > 0 || _2wList.size() > 0 || _3wList.size() > 0 || _4wList.size() > 0 || _5wList.size() > 0 || _6wList.size() > 0 || _7wList.size() > 0 || obj == null) {
					onShakeEvent();
					return;
				}
			}
			if (LotteryBettingUtil.getQxcMoney(_1wList, _2wList, _3wList, _4wList, _5wList, _6wList, _7wList) > GlobalConstants.LOTTERY_MAX_PRICE) {
				showToast(getResources().getString(R.string.lottery_money_than));
				return;
			}
			if (isCartForResult) {
				Intent data = new Intent();
				setIntent(_1wList, _2wList, _3wList, _4wList, _5wList, _6wList, _7wList, data);
				setResult(RESULT_OK, data);
				mActivity.finish();
			} else {
				Intent intent = new Intent(mActivity, LotteryQxcCartActivity.class);
				setIntent(_1wList, _2wList, _3wList, _4wList, _5wList, _6wList, _7wList, intent);
				startActivity(intent);
//				mActivity.finish();
			}
			break;

		default:
			break;
		}
	}

	private void setIntent(ArrayList<Integer> _1wList, ArrayList<Integer> _2wList, ArrayList<Integer> _3wList, ArrayList<Integer> _4wList, ArrayList<Integer> _5wList, ArrayList<Integer> _6wList, ArrayList<Integer> _7wList, Intent intent) {
		intent.putExtra(GlobalConstants.LOTTERY_TYPE, GlobalConstants.LOTTERY_QXC_CART);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST1, _1wList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST2, _2wList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST3, _3wList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST4, _4wList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST5, _5wList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST6, _6wList);
		intent.putIntegerArrayListExtra(GlobalConstants.BALL_LIST7, _7wList);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText().toString());
		intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_QXC));
	}

	private void clearData() {
		m1wController.clearSelectState();
		m2wController.clearSelectState();
		m3wController.clearSelectState();
		m4wController.clearSelectState();
		m5wController.clearSelectState();
		m6wController.clearSelectState();
		m7wController.clearSelectState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		if (parent == m1wController.getGridView()) {
			m1wController.updateSelect(position);
		} else if (parent == m2wController.getGridView()) {
			m2wController.updateSelect(position);
		} else if (parent == m3wController.getGridView()) {
			m3wController.updateSelect(position);
		} else if (parent == m4wController.getGridView()) {
			m4wController.updateSelect(position);
		} else if (parent == m5wController.getGridView()) {
			m5wController.updateSelect(position);
		} else if (parent == m6wController.getGridView()) {
			m6wController.updateSelect(position);
		} else if (parent == m7wController.getGridView()) {
			m7wController.updateSelect(position);
		}
		initTzHint();
	}

	@Override
	protected int getLotteryName() {
		return GlobalConstants.LOTTERY_QXC;
	}
	
	@Override
	protected void onShakeEvent() {
		super.onShakeEvent();
		m1wController.randomBall(m1wController.getSelectData(), 1, 10);
		m2wController.randomBall(m2wController.getSelectData(), 1, 10);
		m3wController.randomBall(m3wController.getSelectData(), 1, 10);
		m4wController.randomBall(m4wController.getSelectData(), 1, 10);
		m5wController.randomBall(m5wController.getSelectData(), 1, 10);
		m6wController.randomBall(m6wController.getSelectData(), 1, 10);
		m7wController.randomBall(m7wController.getSelectData(), 1, 10);
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
		if (isCartForResult) {
			ArrayList<Integer> list1 = new ArrayList<Integer>();
			ArrayList<Integer> list2 = new ArrayList<Integer>();
			ArrayList<Integer> list3 = new ArrayList<Integer>();
			ArrayList<Integer> list4 = new ArrayList<Integer>();
			ArrayList<Integer> list5 = new ArrayList<Integer>();
			ArrayList<Integer> list6 = new ArrayList<Integer>();
			ArrayList<Integer> list7 = new ArrayList<Integer>();
			for (int i = 0; i < num; i++) {
				LotteryShowUtil.randomAddBall(list1, 1, 10);
				LotteryShowUtil.randomAddBall(list2, 1, 10);
				LotteryShowUtil.randomAddBall(list3, 1, 10);
				LotteryShowUtil.randomAddBall(list4, 1, 10);
				LotteryShowUtil.randomAddBall(list5, 1, 10);
				LotteryShowUtil.randomAddBall(list6, 1, 10);
				LotteryShowUtil.randomAddBall(list7, 1, 10);
			}
			
			
			Intent data = new Intent();
			setIntent(list1, list2, list3, list4, list5, list6, list7, data);
			data.putExtra(GlobalConstants.LIST_JXNUM, num);
			data.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			data.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText().toString());
			data.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_QXC));
			setResult(RESULT_OK, data);
			mActivity.finish();
		} else {
			Intent intent = new Intent(mActivity, LotteryQxcCartActivity.class);
			intent.putExtra(GlobalConstants.LOTTERY_TYPE, GlobalConstants.LOTTERY_DLT_JX);
			intent.putExtra(GlobalConstants.LIST_JXNUM, num);
			intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE, mIssueTextView.getText().toString());
			intent.putExtra(GlobalConstants.LOTTERY_ISSUE_TIME, LotteryBettingUtil.getIssueEndTime(GlobalConstants.LOTTERY_QXC));
			startActivity(intent);
		}
	}

	@Override
	public void PopZST() {
		Intent intent=new Intent(this,ZSTWebViewActivity.class);
		intent.putExtra("url", GlobalConstants.getZSTUrl(3));
		intent.putExtra("title", "七星彩");
		intent.putExtra(GlobalConstants.LOTTERY_ISSUES, issues);
		startActivity(intent);
	}
}
