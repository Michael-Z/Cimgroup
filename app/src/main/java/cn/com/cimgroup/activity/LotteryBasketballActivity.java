package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.frament.BaseLotteryBidFrament;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.frament.LotteryBasketballDXrament;
import cn.com.cimgroup.frament.LotteryBasketballHHGGFrament;
import cn.com.cimgroup.frament.LotteryBasketballSFCrament;
import cn.com.cimgroup.frament.LotteryBasketballSFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopWindowFootballLotteryWay;
import cn.com.cimgroup.popwindow.PopWindowFootballLotteryWay.onItemClick;
import cn.com.cimgroup.popwindow.PopWindowFootballMatchChoose;
import cn.com.cimgroup.popwindow.PopWindowFootballMatchChoose.onMatchChooseItemClick;
import cn.com.cimgroup.popwindow.PopWindowLotteryBidMore;
import cn.com.cimgroup.popwindow.PopWindowLotteryBidMore.MoreSelectType;
import cn.com.cimgroup.popwindow.PopWindowLotteryBidMore.OnPopWindowItemListener;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.view.ViewPagerIndicator;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 竞彩篮球
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
@SuppressLint("UseSparseArrays")
public class LotteryBasketballActivity extends BaseFramentActivity implements
		OnClickListener, onItemClick, onMatchChooseItemClick,
		OnPopWindowItemListener {

	// 篮彩胜平负
	private static final int SPF = 0;
	// 篮彩大小分
	private static final int DX = 1;
	// 篮彩胜分差
	private static final int SFC = 2;
	// 篮彩混合过关
	private static final int HHGG = 3;

	// 默认投注方式
	private final int mDefaultSelectIndex = 0;// 混合过关

	private int mSelectIndex = mDefaultSelectIndex;

	public LinearLayout mBgPop;

	// 赛事信息
	private FootBallMatch mMatch = new FootBallMatch();

	List<String> spiltTimes = new ArrayList<String>();

	List<List<Match>> mMatchs = new ArrayList<List<Match>>();

	private List<String> mDatas;
	// 网络加载数据状态、LODING正在加载、SUCCESS加载成功、ERROR加载失败
	private LOAD_STATE loadState;

	public enum LOAD_STATE {
		LODING, SUCCESS, ERROR
	};

	private int mLotoId = 0;

	// 比赛方式popupwindow
	private PopWindowFootballLotteryWay mPopWindowJczq;
	// 赛事选择
	private PopWindowFootballMatchChoose mPopWindowFootballMatchChoose;
	FragmentManager mFragmentManager;

	// 业务逻辑类
	private Controller mController;
	// 返回按钮
	private TextView textWord;
	// 足彩赛事筛选
	private ImageView mLotteryBidSelectView;
	// 删除此次投注选项选择
	private TextView mlotteryBidSelectTrash;
	// title tab view
	private ViewPager mViewPagerView;
	// 选择的比赛场数
	private TextView mSelectMatchCountView;

	private TextView mSelectMatchFinishView;
	// 更多操作
	private RelativeLayout mMoreView;

	private LotteryFootBallPagerAdapter myPagerAdapter;

	private ViewPagerIndicator mIndicator;

	private RelativeLayout footbalTitle;

	private UserInfo userInfo;

	private String issue;

	/** 篮彩胜平负 */
	private LotteryBasketballSFrament[] mBasketballLotterySPF = {
			new LotteryBasketballSFrament(), new LotteryBasketballSFrament() };
	/** 篮彩大小分 */
	private LotteryBasketballDXrament[] mBasketballLotteryDX = {
			new LotteryBasketballDXrament(), new LotteryBasketballDXrament() };
	/** 篮彩胜分差 */
	private LotteryBasketballSFCrament[] mBasketballLotterySFC = {
			new LotteryBasketballSFCrament(), new LotteryBasketballSFCrament() };
	/** 篮彩混合过关 */
	private LotteryBasketballHHGGFrament[] mBasketballLotteryhhgg = {
			new LotteryBasketballHHGGFrament(),
			new LotteryBasketballHHGGFrament() };

	/**
	 * 跳转到本页面
	 * 
	 * @Description:
	 * @param context
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public static void forwardLotteryJQJCActivity(Context context) {
		Intent tIntent = new Intent(context, LotteryFootballActivity.class);
		context.startActivity(tIntent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_football);
		mFragmentManager = getSupportFragmentManager();
		mController = Controller.getInstance();
		mSelectIndex = getIntent().getIntExtra("selectIndex", mDefaultSelectIndex);
		initView();
		initListener();
		initData();

		// addData(HHGG);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userInfo = UserLogic.getInstance().getDefaultUserInfo();
		Intent intent = getIntent();
		boolean isClear = intent.getBooleanExtra("isClear", false);
		if (isClear) {
			clearFramentsSelectIndexs();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == GlobalConstants.CLEARLATTERYSELECT) {
			// 清空本次选择的场次信息
			clearFramentsSelectIndexs();
		} else {
			// frament通过onstartforresutactivity打开的activity，关闭后，将数据返回给响应的frament
			int index = mViewPagerView.getCurrentItem();
			BaseLotteryBidFrament frament = (BaseLotteryBidFrament) myPagerAdapter
					.getItem(index);
			frament.onActivityResult(requestCode, resultCode, data);

		}
	}

	public void initView() {
		mDatas = Arrays.asList(
				getResources().getString(R.string.football_item_twin),
				getResources().getString(R.string.football_item_single));
		mLotteryBidSelectView = (ImageView) findViewById(R.id.imgView_lotterybid_select);
		mLotteryBidSelectView.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.layoutView_title_left1))
				.setOnClickListener(this);
		;
		textWord = (TextView) findViewById(R.id.textView_lotterybid_text);
		footbalTitle = (RelativeLayout) findViewById(R.id.layoutView_footballlottery_title);

		mBgPop = (LinearLayout) findViewById(R.id.pop_common_bg);

		mIndicator = (ViewPagerIndicator) findViewById(R.id.cvView_lotterydraw_pagerSliding);
		mViewPagerView = (ViewPager) findViewById(R.id.v4View_lotterydraw_pager);
		mSelectMatchCountView = (TextView) findViewById(R.id.txtView_lotterybid_match_count);
		mSelectMatchFinishView = (TextView) findViewById(R.id.txtView_lotterybid_match_finish);
		mlotteryBidSelectTrash = (TextView) findViewById(R.id.imgView_lotterybid_trash);
		mMoreView = (RelativeLayout) findViewById(R.id.imgView_footballbid_more);
		mMoreView.setVisibility(View.VISIBLE);
		mPopWindowFootballMatchChoose = new PopWindowFootballMatchChoose(this);
		myPagerAdapter = new LotteryFootBallPagerAdapter(mFragmentManager);
		// myPagerAdapter.setFraments(mBasketballLotteryhhgg);
//		myPagerAdapter.setFraments(mBasketballLotterySPF);
		switch (mSelectIndex) {
		// 胜平负
		case SPF:
			myPagerAdapter.setFraments(mBasketballLotterySPF);
			break;
		// 大小分
		case DX:
			myPagerAdapter.setFraments(mBasketballLotteryDX);
			break;
		// 胜分差
		case SFC:
			myPagerAdapter.setFraments(mBasketballLotterySFC);
			break;
		// 混合
		case HHGG:
			myPagerAdapter.setFraments(mBasketballLotteryhhgg);
			break;
		default:
			myPagerAdapter.setFraments(mBasketballLotterySPF);
			break;
		}
	}

	public void initListener() {
		textWord.setOnClickListener(this);
		mPopWindowFootballMatchChoose.addCallBack(this);
		mSelectMatchFinishView.setOnClickListener(this);
		mLotteryBidSelectView.setOnClickListener(this);
		// mSelectMatchCountView.setOnClickListener(this);
		mlotteryBidSelectTrash.setOnClickListener(this);
		mMoreView.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	private void initData() {
		mViewPagerView.setAdapter(myPagerAdapter);
		mViewPagerView.setPageMargin(GlobalTools.px2dip(this, 2));
		// 设置选项卡字体选中、为选中颜色
		mIndicator.setTabItemTextColor(R.color.color_black, R.color.color_red);
		// 设置选项卡item 选中、未选中背景色
		mIndicator.setTabItemBgColor(R.color.color_white, R.color.color_white);
		mIndicator.setIndicatorHeight(4);
		mIndicator.setTextSize(16);
		// 设置选项卡title内容
		mIndicator.setTabItemTitles(mDatas);
		// 同步操作的viewpager
		mIndicator.setViewPager(mViewPagerView, 0);
		mPopWindowJczq = new PopWindowFootballLotteryWay(this, mBgPop);
		mPopWindowJczq.setData(
				FootballLotteryConstants.getBasketballLotteryWay(),
				mSelectIndex);
		mPopWindowJczq.addCallBack(this);
		textWord.setText(mPopWindowJczq.getData().get(mSelectIndex)[1]);

	}

	public void addData(int lotoId) {
		mLotoId = lotoId;
		// 加载足彩数据
		loadState = LOAD_STATE.LODING;
		mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
				GlobalConstants.TC_JLXSF, "2", mCallBack);
		showLoadingDialog();
	}

	/**
	 * 是否显示暂停销售
	 * 
	 * @Description:
	 * @param isStop
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	// private void showStopTzWord(Boolean isStop){
	// if (isStop) {
	// mSelectMatchCountView.setEnabled(false);
	// mSelectMatchCountView.setText(getResources().getString(R.string.stop_tz));
	// }
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layoutView_title_left1:
//			MainActivity mainActivity = (MainActivity) ActivityManager
//					.isExistsActivity(MainActivity.class);
//			if (mainActivity != null) {
//				// 如果存在MainActivity实例，那么展示LoboHallFrament页面
//				ActivityManager.retain(MainActivity.class);
//				mainActivity.showFrament(LoboHallFrament.class);
//			}
			this.finish();
			break;
		case R.id.textView_lotterybid_text:
			// 打开投注方式popWindow
			mPopWindowJczq.show2hideWindow(footbalTitle);
			mBgPop.setVisibility(View.VISIBLE);
			break;
		case R.id.imgView_lotterybid_select:
			// mPopWindowFootballMatchChoose.show2hideWindow(textWord);
			// mPopWindowFootballMatchChoose.showAtLocation(Gravity.CENTER);
			mPopWindowFootballMatchChoose.showFootballDialog();
			break;
		// 删除此次选择
		case R.id.imgView_lotterybid_trash:
			int indexTrash = mViewPagerView.getCurrentItem();
			BaseLotteryBidFrament frament0 = (BaseLotteryBidFrament) myPagerAdapter
					.getItem(indexTrash);
			frament0.onSuperActivityOnClick(v.getId());
			break;
		// 进入购物车页面
		case R.id.txtView_lotterybid_match_finish:
			int index = mViewPagerView.getCurrentItem();
			BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
					.getItem(index);
			frament1.onSuperActivityOnClick(v.getId());
			break;
		// 更多
		case R.id.imgView_footballbid_more:
			new PopWindowLotteryBidMore(LotteryBasketballActivity.this, this,2)
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

	/**
	 * 投注方式选择
	 */
	@Override
	public void onSelectItemClick(String[] selectData, int position) {
		textWord.setText(selectData[1]);
		if (mSelectIndex != position) {

			mSelectIndex = position;
			switch (mSelectIndex) {
			// 篮彩胜平负
			case SPF:
				myPagerAdapter.setFraments(mBasketballLotterySPF);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = SPF;
				notifyFramentDataChange(mError);
				break;
			case DX:
				// 篮彩大小分
				myPagerAdapter.setFraments(mBasketballLotteryDX);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = DX;
				notifyFramentDataChange(mError);
				break;
			case SFC:
				// 篮彩胜分差
				myPagerAdapter.setFraments(mBasketballLotterySFC);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = SFC;
				notifyFramentDataChange(mError);
				break;
			case HHGG:
				// 混合过关
				myPagerAdapter.setFraments(mBasketballLotteryhhgg);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = HHGG;
				notifyFramentDataChange(mError);
				break;
			default:
				break;
			}
		}
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mController.removeCallback(mCallBack);
		super.onDestroy();
	}

	private String mError;
	
	/**
	 * 更新Frament中数据内容， TODO 必须frament中将对应的mCallBack添加到Controller
	 * 
	 * @Description:
	 * @param error
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	private void notifyFramentDataChange(String error) {
		mError = error;
		for (CallBack back : mController.getCallbacks(null)) {

			if (mLotoId == SPF) {
				// 篮彩胜平负
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getBasketballMatchsInfoSPFSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getBasketballMatchsInfoSPFFailure(error);
				}
			} else if (mLotoId == DX) {
				// 篮彩大小分
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getBasketballMatchsDXSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getBasketballMatchsDXFailure(error);
				}
			} else if (mLotoId == SFC) {
				// 篮彩胜分差
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getBasketballMatchsSFCSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getBasketballMatchsSFCFailure(error);
				}
			} else if (mLotoId == HHGG) {
				// 混合过关
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getBasketballMatchsHHGGSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getBasketballMatchsHHGGError(error);
				}
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
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		if (frament1.mSearchType != null) {

			switch (frament1.mSearchType) {
			case TWIN:
				// 提示选择场次
				if (matchCount == 0) {
					mSelectMatchCountView
							.setText(getString(R.string.lottery_please_select));
				} else if (matchCount == 1) {
					// 提示还差一场
					mSelectMatchCountView
							.setText(getString(R.string.lottery_selectcount_one));
				} else {
					// 提示已经选择的场次，并且可用添加购物车
					mSelectMatchCountView.setText(getString(
							R.string.lottery_selectcount, matchCount));
				}
				break;
			case SINGLE:
				// 提示选择场次
				if (matchCount == 0) {
					mSelectMatchCountView
							.setText(getString(R.string.lottery_please_select));
				} else {
					// 提示已经选择的场次，并且可用添加购物车
					mSelectMatchCountView.setText(getString(
							R.string.lottery_selectcount, matchCount));
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 改变title中，比赛的数量
	 * 
	 * @Description:
	 * @param searchType
	 * @param count
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public void changeTitleMatchCount(MatchSearchType searchType, int count) {
		mIndicator.setTitle(searchType.ordinal(),
				mDatas.get(searchType.ordinal()) + "(" + count + ")");
	}

	private CallBack mCallBack = new CallBack() {

		/**
		 * 足彩数据加载成功（胜平负、进球数、半全场、比分）
		 * 
		 * @Description:
		 * @param lotoId
		 * @param footballMatchs
		 * @param footballSp
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
		public void getFootBallMatchsInfoSuccess(final FootBallMatch info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					loadState = LOAD_STATE.SUCCESS;
					List<Matchs> matchs = info.getList();
					if (matchs != null && matchs.size() != 0) {
						for (int i = 0; i < matchs.size(); i++) {
							Matchs match = matchs.get(i);
							spiltTimes.add(match.getSpiltTime());
							mMatchs.add(match.getMatchs());
						}
						mMatch = info;
						// 更新对应的frament数据结果
					}
					notifyFramentDataChange(null);
					mPopWindowFootballMatchChoose.setData(mMatchs);
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
					// mLotoId = lotoId;
					loadState = LOAD_STATE.ERROR;
					// TODO Auto-generated method stub
					// 更新对应的frament数据结果
					notifyFramentDataChange(error);
					hideLoadingDialog();
					ToastUtil.shortToast(LotteryBasketballActivity.this, error);
				}
			});
		};

		public void getMatchTimeSuccess(final IssueList list) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					issue = list.getIssues().get(1).getIssue();
					mController.getFootBallMatchsInfo(
							GlobalConstants.NUM_FOOTBALLMATCHSINFO, list
									.getIssues().get(0).getIssue(),
							GlobalConstants.TC_JCLQ, "0", mCallBack);
				}
			});
		};

		public void getMatchTimeFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					notifyFramentDataChange(error);
				}
			});
		};
	};

	/**
	 * 筛选赛事
	 */
	@Override
	public void onSelectItemClick(View view, List<String> selectData) {
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		frament.mSearchType = frament.mSearchType == null ? MatchSearchType.TWIN
				: frament.mSearchType;
		List<Matchs> matchs = mMatch.getList();
		List<Matchs> tempsmatchs = new ArrayList<Matchs>();
		FootBallMatch tempMatch = new FootBallMatch();
		for (int i = 0; i < matchs.size(); i++) {
			Matchs tempMatchs = new Matchs();
			Matchs match = matchs.get(i);
			List<Match> matchList = match.getMatchs();
			List<Match> tempMatchList = new ArrayList<Match>();
			for (String k : selectData) {
				for (Match j : matchList) {
					if (j.getLeagueShort().equals(k)) {
						tempMatchList.add(j);
					}
				}
			}
			tempMatchs.setSpiltTime(match.getSpiltTime());
			tempMatchs.setMatchs(tempMatchList);
			tempsmatchs.add(tempMatchs);
		}
		tempMatch.setList(tempsmatchs);
		frament.onSuperActivityOnClick(view.getId(), tempMatch);
	}

	@Override
	public void onPopWindowItemClick(MoreSelectType type) {
		switch (type) {
		// 比分直播
		case BFZB:
			if (userInfo != null) {
				startActivity(new Intent(this, ScoreListActivity.class));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}

			break;
		// 开奖信息
		case KJXX:
			// MainActivity mainActivity = (MainActivity)
			// ActivityManager.isExistsActivity(MainActivity.class);
			// if (mainActivity != null) {
			// ActivityManager.retain(MainActivity.class);
			// // 如果存在MainActivity实例，那么展示LoboHallFrament页面
			// mainActivity.showFrament(LoboDrawFrament.class);
			// }
			LotteryDrawInfo info = new LotteryDrawInfo();
			info.setGameNo(GlobalConstants.TC_JCLQ);
			info.setIssueNo(issue);
			Intent intent1 = new Intent(LotteryBasketballActivity.this,
					LotteryDrawBasketballListActivity.class);
			intent1.putExtra("LotteryDrawInfo", info);
			startActivity(intent1);
			break;
		// 我的投注
		case TZJL:
			if (userInfo != null) {
				GlobalConstants.personGameNo =GlobalConstants.TC_JCLQ;
				GlobalConstants.personTagIndex = 0;
				GlobalConstants.personEndIndex = 0;
				GlobalConstants.isShowLeMiFragment = true;
				GlobalConstants.isRefreshFragment = true;
				MainActivity main = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (main != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					ActivityManager.retain(MainActivity.class);
					main.showFrament(3);
				}
//				Intent intent = new Intent(this, TzListActivity.class);
//				intent.putExtra("type", GlobalConstants.TAG_TZLIST_BASKETBALL);
//				intent.putExtra("text",
//						getResources().getString(R.string.tz_select_basketball));
//				startActivity(intent);
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		// 玩法说明
		case WFSM:
			Intent intent = new Intent(this, HtmlCommonActivity.class);
			intent.putExtra("isWeb", false);
			intent.putExtra("lotteryName", FootballLotteryConstants.L306);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			// 获取MainActivity实例
//			MainActivity mainActivity = (MainActivity) ActivityManager
//					.isExistsActivity(MainActivity.class);
//			if (mainActivity != null) {
//				// 如果存在MainActivity实例，那么展示LoboHallFrament页面
//				ActivityManager.retain(MainActivity.class);
//				mainActivity.showFrament(LoboHallFrament.class);
//			}
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
