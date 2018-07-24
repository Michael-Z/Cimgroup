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
import cn.com.cimgroup.App;
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
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.frament.Lottery2X1Frament;
import cn.com.cimgroup.frament.LotteryFootballBFFrament;
import cn.com.cimgroup.frament.LotteryFootballBQCFrament;
import cn.com.cimgroup.frament.LotteryFootballHHGGFrament;
import cn.com.cimgroup.frament.LotteryFootballJQSFrament;
import cn.com.cimgroup.frament.LotteryFootballSPFrament;
import cn.com.cimgroup.frament.LotteryOneWinFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
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
import cn.com.cimgroup.xutils.XLog;

/**
 * 足球竞彩FramentActivity
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
@SuppressLint("UseSparseArrays")
public class LotteryFootballActivity extends BaseFramentActivity implements
		OnClickListener, onItemClick, onMatchChooseItemClick,
		OnPopWindowItemListener {
	/**彩票类型*/
	private String mGameNo="TC_JCZQ";

	/** 胜平负 */
	private static final int SPF = 0;
	// 让球胜平负
	// private static final int RQSPF = 1;
	/** 比分 */
	private static final int BF = 1;
	/** 进球数 */
	private static final int JQS = 2;
	/** 半全场 */
	private static final int BQC = 3;
	/** 混合过关 */
	private static final int HHGG = 4;
	/** 一场制胜 */
	private static final int YCZS = 5;
	/** 二选一 */
	private static final int EXY = 6;

	// 默认投注方式
	private final int mDefaultSelectIndex = 4;// 混合过关

	private int mSelectIndex = mDefaultSelectIndex;

	public LinearLayout mBgPop;

	// 赛事信息
	private FootBallMatch mMatch = new FootBallMatch();
	
	private FootBallMatch mYCZSMatch = new FootBallMatch();
	
	private FootBallMatch mEXYMatch = new FootBallMatch();

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

	private String issue;

	/** 足彩胜平负 */
	private LotteryFootballSPFrament[] mFootBallLotterySPF = {
			new LotteryFootballSPFrament(), new LotteryFootballSPFrament() };
	// 足彩让球胜平负
	// private LotteryFootballRQSPFrament[] mFootBallLotteryRQSPF = {
	// new LotteryFootballRQSPFrament(), new LotteryFootballRQSPFrament() };
	/** 半全场 */
	private LotteryFootballBQCFrament[] mFootBallLotterybqc = {
			new LotteryFootballBQCFrament(), new LotteryFootballBQCFrament() };
	/** 进球数 */
	private LotteryFootballJQSFrament[] mFootBallLotteryjqs = {
			new LotteryFootballJQSFrament(), new LotteryFootballJQSFrament() };
	/** 比分 */
	private LotteryFootballBFFrament[] mFootBallLotterybf = {
			new LotteryFootballBFFrament(), new LotteryFootballBFFrament() };
	/** 混合过关 */
	private LotteryFootballHHGGFrament[] mFootBallLotteryhhgg = {
			new LotteryFootballHHGGFrament(), new LotteryFootballHHGGFrament() };

	/** 一场制胜 */
	private LotteryOneWinFrament[] mFootBallYCZS = { new LotteryOneWinFrament() };
	/** 二选一 */
	private Lottery2X1Frament[] mFootBallLotteryEXY = { new Lottery2X1Frament() };

	private MatchSearchType mSearchType = MatchSearchType.TWIN;

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
		String temp = "1";
		if (savedInstanceState != null) {
			temp = savedInstanceState.getString("searchType");
			System.out.println("onCreate: searchType = " + temp);
			switch (temp) {
			case "1":
				mSearchType = MatchSearchType.TWIN;
				break;
			case "2":
				mSearchType = MatchSearchType.SINGLE;
				break;
			default:
				break;
			}
		}
		mFragmentManager = getSupportFragmentManager();
		mController = Controller.getInstance();
		
		Intent intent = getIntent();
		mSelectIndex = intent.getIntExtra("selectIndex", mDefaultSelectIndex);
		if (mSelectIndex == YCZS) {
			mGameNo="TC_1CZS";
		}
		initView();
		initListener();
		initData();

		// addData(HHGG);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		switch (frament.mSearchType) {
		case TWIN:
			outState.putString("searchType", "1");
			break;
		case SINGLE:
			outState.putString("searchType", "2");
			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		boolean isClear = intent.getBooleanExtra("isClear", false);
		if (isClear) {
			clearFramentsSelectIndexs();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		if (mSelectIndex == YCZS) {
			myPagerAdapter.setFraments(mFootBallYCZS);
			mIndicator.setVisibility(View.GONE);
		} else {
			switch (mSelectIndex) {
			// 比分
			case BF:
				myPagerAdapter.setFraments(mFootBallLotterybf);
				break;
			// 总进球数
			case JQS:
				myPagerAdapter.setFraments(mFootBallLotteryjqs);
				break;
			// 半全场
			case BQC:
				myPagerAdapter.setFraments(mFootBallLotterybqc);
				break;
			case HHGG:
				myPagerAdapter.setFraments(mFootBallLotteryhhgg);
				break;
			case SPF:
				myPagerAdapter.setFraments(mFootBallLotterySPF);
				break;
			case EXY:
				myPagerAdapter.setFraments(mFootBallLotteryEXY);
				mIndicator.setVisibility(View.GONE);
				mGameNo="TC_2X1";
				break;
			default:
				myPagerAdapter.setFraments(mFootBallLotteryhhgg);
				break;
			}
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
				FootballLotteryConstants.getFootballLotteryWay(), mSelectIndex);
		mPopWindowJczq.addCallBack(this);
		textWord.setText(mPopWindowJczq.getData().get(mSelectIndex)[1]);

	}

	public void addData(int lotoId) {
		mLotoId = lotoId;
		// 加载足彩数据
		loadState = LOAD_STATE.LODING;
		mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
				GlobalConstants.TC_JZSPF, "2", mCallBack);
		showLoadingDialog();
	}

	/**
	 * 是否显示暂停销售
	 * 
	 * @Description:
	 * @param
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layoutView_title_left1:
			this.finish();
			break;
		case R.id.textView_lotterybid_text:
			// 打开投注方式popWindow
			mPopWindowJczq.show2hideWindow(footbalTitle);
			mBgPop.setVisibility(View.VISIBLE);
			break;
		case R.id.imgView_lotterybid_select:
			mPopWindowFootballMatchChoose.showFootballDialog();
			break;
		// 删除此次选择
		case R.id.imgView_lotterybid_trash:
			//TODO
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
			if (mGameNo.equals("TC_1CZS")) {
				frament1.onSuperActivityOnClick(v.getId(),mGameNo);
			}else
				frament1.onSuperActivityOnClick(v.getId());
			
			break;
		// 更多
		case R.id.imgView_footballbid_more:
			new PopWindowLotteryBidMore(LotteryFootballActivity.this, this, 1)
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
			// 胜平负
			case SPF:
				myPagerAdapter.setFraments(mFootBallLotterySPF);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = SPF;
				mGameNo="TC_JCZQ";
				mIndicator.setVisibility(View.VISIBLE);
				break;
			// 比分
			case BF:
				mIndicator.setViewPager(mViewPagerView, 0);
				myPagerAdapter.setFraments(mFootBallLotterybf);
				mLotoId = BF;
				mGameNo="TC_JCZQ";
				mIndicator.setVisibility(View.VISIBLE);
				break;
			case BQC:
				// 半全场
				myPagerAdapter.setFraments(mFootBallLotterybqc);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = BQC;
				mGameNo="TC_JCZQ";
				mIndicator.setVisibility(View.VISIBLE);
				break;
			case JQS:
				// 进球数
				myPagerAdapter.setFraments(mFootBallLotteryjqs);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = JQS;
				mGameNo="TC_JCZQ";
				mIndicator.setVisibility(View.VISIBLE);
				break;
			case HHGG:
				// 混合过关
				myPagerAdapter.setFraments(mFootBallLotteryhhgg);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = HHGG;
				mGameNo="TC_JCZQ";
				mIndicator.setVisibility(View.VISIBLE);
				break;
			case YCZS:
				// 一场制胜
				myPagerAdapter.setFraments(mFootBallYCZS);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = YCZS;
				mGameNo="TC_1CZS";
				mIndicator.setVisibility(View.GONE);
				break;
			case EXY:
				// 二选一
				myPagerAdapter.setFraments(mFootBallLotteryEXY);
				mIndicator.setViewPager(mViewPagerView, 0);
				mLotoId = EXY;
				mGameNo="TC_2X1";
				mIndicator.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			notifyFramentDataChange(mError);
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
		mController.removeCallback(mCallBack);
//		myPagerAdapter = null;
//		mIndicator = null;
//		mViewPagerView = null;
		super.onDestroy();
	}
	
	private String mError;

	/**
	 * 更新Frament中数据内容， 必须frament中将对应的mCallBack添加到Controller
	 * 
	 * @Description:
	 * @param error
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	private void notifyFramentDataChange(String error) {
		mError = error;
		for (CallBack back : mController.getCallbacks(null)) {
			// 比分数据获取完成
			switch (mLotoId) {
			case BF:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchsBFSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchsBFFailure(error);
				}
				break;
			case SPF:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchsInfoSPFSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchsInfoSPFFailure(error);
				}
				break;
			case JQS:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchsJQSSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchsJQSFailure(error);
				}
				break;

			case BQC:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchsBQCSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchsBQCFailure(error);
				}
				break;

			case HHGG:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchsHHGGSuccess(mMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchsHHGGError(error);
				}
				break;

			case YCZS:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchsOneWinSuccess(mYCZSMatch, null);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchsOneWinFailure(error);
				}
				break;
			case EXY:
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getFootBallMatchs2x1Success(mEXYMatch);
				} else if (!StringUtil.isEmpty(error)) {
					back.getFootBallMatchs2x1Failure(error);
				}
				break;

			default:
				break;
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
		if (mSelectIndex == YCZS) {
			if (matchCount == 0) {
				mSelectMatchCountView
						.setText(getString(R.string.lottery_please_select));
			} else {
				// 提示已经选择的场次，并且可用添加购物车
				mSelectMatchCountView.setText(getString(
						R.string.lottery_selectcount, matchCount+""));
			}
			return;
		}
		
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		if (frament1.mSearchType == null) {
			frament1.mSearchType = mSearchType;
		}
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
						R.string.lottery_selectcount, matchCount+""));
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
						R.string.lottery_selectcount, matchCount+""));
			}
			break;
		default:
			break;
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
		 * @param
		 * @param
		 * @param
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
		public void getFootBallMatchsInfoSuccess(final FootBallMatch info) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadState = LOAD_STATE.SUCCESS;
					List<Matchs> matchs = info.getList();
					if (matchs != null && matchs.size() != 0) {
						for (int i = 0; i < matchs.size(); i++) {
							Matchs match = matchs.get(i);
							spiltTimes.add(match.getSpiltTime());
							mMatchs.add(match.getMatchs());
						}
						if (mLotoId == YCZS) {
							mYCZSMatch = info;
						} else if (mLotoId == EXY) {
							mEXYMatch = info;
						} else {
							mMatch = info;
						}
					}
					// 更新对应的frament数据结果
					notifyFramentDataChange(null);
					mPopWindowFootballMatchChoose.setData(mMatchs);
					hideLoadingDialog();
				}
			});
		};

		/**
		 * 足彩数据获取失败（胜平负、进球数、半全场、比分）
		 * 
		 * @Description:
		 * @param
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
					// 更新对应的frament数据结果
					notifyFramentDataChange(error);
					hideLoadingDialog();
//					ToastUtil.shortToast(LotteryFootballActivity.this, error);
				}
			});
		};

		public void getMatchTimeSuccess(final IssueList list) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					//TODO
					issue = list.getIssues().get(1).getIssue();
					mController.getFootBallMatchsInfo(
							GlobalConstants.NUM_FOOTBALLMATCHSINFO, issue,
							mGameNo, "0", mCallBack);
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
		if (mLotoId == YCZS) {
			matchs = mYCZSMatch.getList();
		} else if (mLotoId == EXY) {
			matchs = mEXYMatch.getList();
		} else {
			matchs = mMatch.getList();
		}
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
//			if (App.userInfo != null) {
				startActivity(new Intent(this, ScoreListActivity.class));
//			} else {
//				startActivity(new Intent(this, LoginActivity.class));
//			}

			break;
		// 开奖信息
		case KJXX:
			LotteryDrawInfo info = new LotteryDrawInfo();
			info.setGameNo(GlobalConstants.TC_JCZQ);
			info.setIssueNo(issue);
			Intent intent1 = new Intent(LotteryFootballActivity.this,
					LotteryDrawFootballListActivity.class);
			intent1.putExtra("LotteryDrawInfo", info);
			startActivity(intent1);
			break;
		// 我的投注
		case TZJL:
			GlobalConstants.personGameNo =GlobalConstants.TC_JCZQ;
			GlobalConstants.personTagIndex = 0;
			GlobalConstants.personEndIndex = 0;
			GlobalConstants.isRefreshFragment = true;
			GlobalConstants.isShowLeMiFragment = true;
			MainActivity main = (MainActivity) ActivityManager
					.isExistsActivity(MainActivity.class);
			if (main != null) {
				// 如果存在MainActivity实例，那么展示LoboHallFrament页面
				ActivityManager.retain(MainActivity.class);
				main.showFrament(3);
			}
			break;
		// 玩法说明
		case WFSM:
			Intent intent = new Intent(this, HtmlCommonActivity.class);
			intent.putExtra("isWeb", false);
			if (mSelectIndex == YCZS) {
				intent.putExtra("lotteryName", FootballLotteryConstants.L502);
			} else if (mSelectIndex == EXY) {
				intent.putExtra("lotteryName", FootballLotteryConstants.L501);
			} else {
				intent.putExtra("lotteryName", FootballLotteryConstants.L301);
			}
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
