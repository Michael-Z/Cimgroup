package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.BaseFramentActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.LotteryFootBallPagerAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.IssueList;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.frament.BaseLotteryBidFrament;
import cn.com.cimgroup.frament.Lottery2X1Frament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu.PlayMenuItemClick;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 足球竞彩FramentActivity
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
@SuppressLint("UseSparseArrays")
public class Lottery2X1Activity extends BaseFramentActivity implements
		OnClickListener, PlayMenuItemClick {


	// 赛事信息
	private FootBallMatch mMatch = new FootBallMatch();

	private List<String> spiltTimes = new ArrayList<String>();

	private List<List<Match>> mMatchs = new ArrayList<List<Match>>();


	// 网络加载数据状态、LODING正在加载、SUCCESS加载成功、ERROR加载失败
	private LOAD_STATE loadState;

	public enum LOAD_STATE {
		LODING, SUCCESS, ERROR
	};

	private int mLotoId = 0;

	private FragmentManager mFragmentManager;

	// 业务逻辑类
	private Controller mController;
	// 返回按钮
	public TextView textWord;
	// title tab view
	private ViewPager mViewPagerView;

	private TextView mSelectMatchFinishView;
	// 更多操作
	private ImageView mMoreView;
	// 删除此次投注选项选择
	private TextView mlotteryBidSelectTrash;
	
	private TextView mSelectMatchCountView;

	private LotteryFootBallPagerAdapter myPagerAdapter;


	private Lottery2X1Frament[] mFootBallLotteryjqs = { new Lottery2X1Frament() };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_onewin);
		mFragmentManager = getSupportFragmentManager();
		mController = Controller.getInstance();
		mLotoId = getIntent().getIntExtra("lotoId", 0);
		initView();
		initListener();
		initData();
	}

	@Override
	public void onResume() {
		super.onResume();
		boolean isClear = getIntent().getBooleanExtra("isClear", false);
		if (isClear) {
			clearFramentsSelectIndexs();
		}
	}

	public void initView() {

		((TextView) findViewById(R.id.textView_title_back))
				.setOnClickListener(this);
		textWord = (TextView) findViewById(R.id.textView_title_word);
		textWord.setText("二选一");

		myPagerAdapter = new LotteryFootBallPagerAdapter(mFragmentManager);
		myPagerAdapter.setFraments(mFootBallLotteryjqs);
		
		mViewPagerView = (ViewPager) findViewById(R.id.v4View_onewin_football_pager);
		mSelectMatchFinishView = (TextView) findViewById(R.id.txtView_lotterybid_match_finish);
		mMoreView = (ImageView) findViewById(R.id.textView_title_menu);
		mMoreView.setVisibility(View.VISIBLE);

		mlotteryBidSelectTrash = (TextView) findViewById(R.id.imgView_lotterybid_trash);
		mSelectMatchCountView = (TextView) findViewById(R.id.txtView_lotterybid_match_count);
	}

	public void initListener() {
		textWord.setOnClickListener(this);
		mSelectMatchFinishView.setOnClickListener(this);
		mMoreView.setOnClickListener(this);
		mlotteryBidSelectTrash.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	private void initData() {
		mViewPagerView.setAdapter(myPagerAdapter);
		mViewPagerView.setPageMargin(GlobalTools.px2dip(this, 2));
		// 设置选项卡字体选中、为选中颜色

	}

	public void addData(int lotoId) {
		mLotoId = lotoId;
		// 加载足彩数据
		loadState = LOAD_STATE.LODING;
		mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
		         				GlobalConstants.TC_JZSPF, "2", mCallBack);
		showLoadingDialog();
	}

	@Override
	public void onClick(View v) {
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);

		switch (v.getId()) {
		case R.id.textView_title_back:
			finish();
			break;
		case R.id.imgView_lotterybid_trash:
			// 用于更新底部UI
			frament1.onSuperActivityOnClick(v.getId());
			break;
		// 进入购物车页面
		case R.id.txtView_lotterybid_match_finish:
			frament1.onSuperActivityOnClick(v.getId());
			break;
		// 更多
		case R.id.textView_title_menu:
			new PopupWndSwitchPLayMenu(Lottery2X1Activity.this, this)
					.showPopWindow(v);
			break;
		default:
			break;
		}
	}

	/**
	 * 数据获取状态
	 * 
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public LOAD_STATE getLoadState() {
		return loadState;
	}

	@Override
	protected void onDestroy() {
		mController.removeCallback(mCallBack);
		super.onDestroy();
	}

	/**
	 * 更新Frament中数据内容，必须frament中将对应的mCallBack添加到Controller
	 * 
	 * @Description:
	 * @param error
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	private void notifyFramentDataChange(String error) {
		for (CallBack back : mController.getCallbacks(null)) {
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getBasketballMatchsDXSuccess(mMatch);
				} else {
					back.getBasketballMatchsDXFailure(error);
				}
			}

	}

	/**
	 * 通过选择的场次，来更新UI
	 * 
	 * @Description:
	 * @param matchCount
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public void changeSelectMatchCount2state(int matchCount) {
		if (matchCount == 0) {
			mSelectMatchCountView
					.setText(getString(R.string.lottery_please_select));
		} else {
			// 提示已经选择的场次，并且可用添加购物车
			mSelectMatchCountView.setText(getString(
					R.string.lottery_selectcount, matchCount));
		}
	}

	private CallBack mCallBack = new CallBack() {
		public void getFootBallMatchsInfoSuccess(final FootBallMatch info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadState = LOAD_STATE.SUCCESS;
					List<Matchs> matchs = info.getList();
					if (matchs != null) {
						for (int i = 0; i < matchs.size(); i++) {
							Matchs match = matchs.get(i);
							spiltTimes.add(match.getSpiltTime());
							mMatchs.add(match.getMatchs());
						}
						mMatch = info;
						// 更新对应的frament数据结果
						notifyFramentDataChange(null);
						hideLoadingDialog();
					}
				}
			});
		};

		/**
		 * 足彩数据获取失败（胜平负、进球数、半全场、比分）
		 * 
		 * @Description:
		 * @param lotoId
		 * @param error
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
		public void getFootBallMatchsInfoFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					mController.getFootBallMatchsInfo(
//							GlobalConstants.NUM_FOOTBALLMATCHSINFO,
//							infos.get(mSelectIndex).getIssue(), gameNo, "1",
//							mCallBack);
					loadState = LOAD_STATE.ERROR;
					// 更新对应的frament数据结果
					notifyFramentDataChange(null);
					hideLoadingDialog();
					ToastUtil.shortToast(Lottery2X1Activity.this, error);
				}
			});
		};

		public void getMatchTimeSuccess(final IssueList list) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					issue = list.getIssues().get(1).getIssue();
					mController.getFootBallMatchsInfo(
							GlobalConstants.NUM_FOOTBALLMATCHSINFO, list
									.getIssues().get(0).getIssue(),
							GlobalConstants.TC_2X1, "0", mCallBack);
				}
			});
		};

		public void getMatchTimeFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
//							gameNo, "2", mCallBack);
				}
			});
		};
	};

	@Override
	public void PopDraw() {
		LotteryDrawInfo info = new LotteryDrawInfo();
		Intent intent1 = new Intent(Lottery2X1Activity.this,
				LotteryDrawListActivity.class);
		intent1.putExtra("LotteryDrawInfo", info);
		startActivity(intent1);
	}

	@Override
	public void PopPLay() {
		Intent intent = new Intent(this, HtmlCommonActivity.class);
		intent.putExtra("isWeb", false);
		startActivity(intent);
	}

	@Override
	public void PopTzList() {
		int type1 = GlobalConstants.TAG_TZLIST_SFC;
		String text = getResources().getString(R.string.tz_select_sfc);
	}

	/**
	 * 清空所有frament中场次的选中状态
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	private void clearFramentsSelectIndexs() {
		List<Fragment> fragments = mFragmentManager.getFragments();

		if (fragments != null) {
			for (Fragment frament : fragments) {
				if (frament instanceof BaseLotteryBidFrament) {
					BaseLotteryBidFrament f = (BaseLotteryBidFrament) frament;
					f.onSuperActivityOnClick(R.id.btnView_football_match_clear_selectindex);
					changeSelectMatchCount2state(0);
				}
			}
		}
	}

	@Override
	public void PopZST() {
		// TODO Auto-generated method stub
		
	}

}
