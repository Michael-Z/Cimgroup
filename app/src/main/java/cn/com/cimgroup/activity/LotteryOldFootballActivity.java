package cn.com.cimgroup.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFramentActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.BaseLotteryBidOnlyAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallPagerAdapter;
import cn.com.cimgroup.bean.FootBallMatch;
import cn.com.cimgroup.bean.IssueList;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.Matchs;
import cn.com.cimgroup.bean.TzObj;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.frament.BaseLotteryBidFrament;
import cn.com.cimgroup.frament.LotteryOldFootballBQCFrament;
import cn.com.cimgroup.frament.LotteryOldFootballJQSFrament;
import cn.com.cimgroup.frament.LotteryOldFootballSPFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.popwindow.PopWindowOldFootball;
import cn.com.cimgroup.popwindow.PopWindowOldFootball.onItemClick;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu;
import cn.com.cimgroup.popwindow.PopupWndSwitchPLayMenu.PlayMenuItemClick;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.FootballLotteryUtil;
import cn.com.cimgroup.util.LotteryClick;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.PlayInfo;
import cn.com.cimgroup.view.ViewPagerIndicator;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;
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
public class LotteryOldFootballActivity extends BaseFramentActivity implements
		OnClickListener, onItemClick, PlayMenuItemClick {

	// 胜平负
	private static final int SPF = 0;
	// 进球数
	private static final int JQS = 1;
	// 半全场
	private static final int BQC = 2;

	private String gameNo = GlobalConstants.TC_SF14;

	// 默认投注方式
	private final int mDefaultSelectIndex = 1;

	private int mSelectIndex = mDefaultSelectIndex;

	/** 当前所在页数 通过popupwindow来改变该值 **/
	private int currentPostion = 1;

	public LinearLayout mBgPop;

	private EditText mTbEditText;// 投多少倍

	private int mMultiple = 999;// 倍数

	private TextView mTVCartNumMult;// 投注注数跟倍数

	private TextView mTVCartTermMoney;// 钱数

	// 赛事信息
	private FootBallMatch mMatch = new FootBallMatch();

	private List<String> spiltTimes = new ArrayList<String>();

	private List<List<Match>> mMatchs = new ArrayList<List<Match>>();

	private FootballLotteryUtil mFootballLotteryUtil;

	private BaseLotteryBidOnlyAdapter mBaseLotteryBidOnlyAdapter;

	private List<String> mDatas;
	// 网络加载数据状态、LODING正在加载、SUCCESS加载成功、ERROR加载失败
	private LOAD_STATE loadState;

	public enum LOAD_STATE {
		LODING, SUCCESS, ERROR
	};

	private int mLotoId = 0;

	// 比赛方式popupwindow
	private PopWindowOldFootball mPopWindowJczq;
	private FragmentManager mFragmentManager;

	// 业务逻辑类
	private Controller mController;
	// 返回按钮
	public TextView textWord;
	// title tab view
	private ViewPager mViewPagerView;

	private TextView mSelectMatchFinishView;
	// 更多操作
	private RelativeLayout mMoreView;
	// 删除此次投注选项选择
	private TextView mlotteryBidSelectTrash;

	private LotteryFootBallPagerAdapter myPagerAdapter;

	private ViewPagerIndicator mIndicator;
	/** 头部标题栏布局 */
	private RelativeLayout footbalTitle;

	private List<LoBoPeriodInfo> infos;

	private int itemTotal = 0;
	private int numPrice = 0;

	private boolean isCalculating = false;

	// 足彩胜平负
	private LotteryOldFootballSPFrament[] mFootBallLotterySPF = {
			new LotteryOldFootballSPFrament(),
			new LotteryOldFootballSPFrament() };
	// // 半全场
	private LotteryOldFootballBQCFrament[] mFootBallLotterybqc = { new LotteryOldFootballBQCFrament() };
	// 进球数
	private LotteryOldFootballJQSFrament[] mFootBallLotteryjqs = { new LotteryOldFootballJQSFrament() };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_oldfootball);
		// setContentView(R.layout.activity_common_tab2);
		mFragmentManager = getSupportFragmentManager();
		mController = Controller.getInstance();
		mLotoId = getIntent().getIntExtra("lotoId", 0);
		initView();
		initListener();
		initData();
		// addData(mLotoId);
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

		((TextView) findViewById(R.id.layoutView_title_left1))
				.setOnClickListener(this);
		textWord = (TextView) findViewById(R.id.textView_lotterybid_text);
		footbalTitle = (RelativeLayout) findViewById(R.id.layout_oldfootball_title);
		textWord.setClickable(false);
		mBgPop = (LinearLayout) findViewById(R.id.pop_common_bg);

		mIndicator = (ViewPagerIndicator) findViewById(R.id.cvView_oldfootball_pagerSliding);
		myPagerAdapter = new LotteryFootBallPagerAdapter(mFragmentManager);

		switch (mLotoId) {
		case SPF:
			gameNo = GlobalConstants.TC_SF14;
			mDatas = Arrays.asList(
					getResources().getString(R.string.oldfootball_item_14),
					getResources().getString(R.string.oldfootball_item_9));
			mIndicator.setVisibility(View.VISIBLE);
			myPagerAdapter.setFraments(mFootBallLotterySPF);
			String index = getIntent().getStringExtra("pageIndex") == null ? "0"
					: getIntent().getStringExtra("pageIndex");
			DefaultIndex = Integer.parseInt(index);
			break;
		case JQS:
			gameNo = GlobalConstants.TC_JQ4;
			myPagerAdapter.setFraments(mFootBallLotteryjqs);
			mDatas = Arrays.asList(getResources().getString(
					R.string.oldfootball_item_14));
			mIndicator.setVisibility(View.GONE);
			textWord.setCompoundDrawables(null, null, null, null);
			textWord.setText(getResources().getString(R.string.tz_select_jqs));

			break;
		case BQC:
			gameNo = GlobalConstants.TC_BQ6;
			myPagerAdapter.setFraments(mFootBallLotterybqc);
			mDatas = Arrays.asList(getResources().getString(
					R.string.oldfootball_item_14));
			mIndicator.setVisibility(View.GONE);
			textWord.setCompoundDrawables(null, null, null, null);
			textWord.setText(getResources().getString(R.string.tz_select_bqc));
			break;
		default:

			break;
		}
		mViewPagerView = (ViewPager) findViewById(R.id.v4View_oldfootball_pager);
		mSelectMatchFinishView = (TextView) findViewById(R.id.layout_oldfootball_buy_tv);
		mMoreView = (RelativeLayout) findViewById(R.id.imgView_footballbid_more);
		mMoreView.setVisibility(View.VISIBLE);

		mTVCartNumMult = (TextView) findViewById(R.id.textView_oldfootball_num_mult);
		mTVCartNumMult.setVisibility(View.GONE);
		mTVCartTermMoney = (TextView) findViewById(R.id.textView_oldfootball_term_money);
		mTbEditText = (EditText) findViewById(R.id.editText_tb);

		mlotteryBidSelectTrash = (TextView) findViewById(R.id.imgView_lotterybid_trash);

		mTbEditText.addTextChangedListener(new TbTextWatcher());

		LotteryShowUtil.setSelection(mTbEditText);
	}

	public void initListener() {
//		textWord.setOnClickListener(this);
		mSelectMatchFinishView.setOnClickListener(this);
		mMoreView.setOnClickListener(this);
		mlotteryBidSelectTrash.setOnClickListener(this);
	}

	private int DefaultIndex = 0;

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
		mIndicator.setViewPager(mViewPagerView, DefaultIndex);
		mPopWindowJczq = new PopWindowOldFootball(this, mBgPop);
		mPopWindowJczq.addCallBack(this);

		mFootballLotteryUtil = new FootballLotteryUtil();
	}

	public void addData(int lotoId) {
		mLotoId = lotoId;
		// 加载足彩数据
		loadState = LOAD_STATE.LODING;
		// 切换玩法初始化投注方式
		currentPostion = 1;
		mController.getLoBoPeriod(GlobalConstants.NUM_LOTTERY_PRE, gameNo,
				mCallBack);
		showLoadingDialog();
	}

	@Override
	public void onClick(View v) {
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		mBaseLotteryBidOnlyAdapter = frament1.mBaseAdapter;

		switch (v.getId()) {
		case R.id.layoutView_title_left1:
			finish();
			break;
		case R.id.textView_lotterybid_text:
			// 打开投注方式popWindow
			if (mLotoId == SPF) {
				mPopWindowJczq.show2hideWindow(footbalTitle);
				mBgPop.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.textView_tb_sub:// 减倍数
			if (isCalculating) {
				return;
			}
			String str = mTbEditText.getText().toString();
			if (!TextUtils.isEmpty(str)) {
				int etTbSubInt = Integer.parseInt(str);
				if (etTbSubInt > 1) {
					etTbSubInt--;
					mTbEditText.setText(etTbSubInt + "");
					LotteryShowUtil.setSelection(mTbEditText);
					startCalculate();
				}
			}

			break;
		case R.id.textView_tb_add:// 加倍数
			if (isCalculating) {
				return;
			}
			String addStr = mTbEditText.getText().toString().trim();
			int etTbAddInt = 0;
			if (!TextUtils.isEmpty(addStr)) {
				etTbAddInt = Integer.parseInt(addStr);
				if (etTbAddInt < mMultiple) {
				}
			}
			mTbEditText.setText((++etTbAddInt) + "");
			LotteryShowUtil.setSelection(mTbEditText);
			startCalculate();
			break;
		case R.id.imgView_lotterybid_trash:
			mBaseLotteryBidOnlyAdapter.setSelectItemIndexs(null);
			// 用于更新底部UI
			changeSelectMatchCount2state(0);
			break;
		// 进入购物车页面
		case R.id.layout_oldfootball_buy_tv:
			if (isCalculating) {
				return;
			}
			/** 选择的索引 胜平负、让球胜平负、进球数、比分、半全场 **/
			if (App.userInfo == null) {
				startActivity(new Intent(LotteryOldFootballActivity.this,
						LoginActivity.class));
				return;
			}
			if (TextUtils.isEmpty(mTbEditText.getText().toString().trim())
					|| mTbEditText.getText().toString().trim().equals("0")) {
				mTbEditText.setText("1");
				LotteryShowUtil.setSelection(mTbEditText);
				startCalculate();
				changeSelectMatchCount2state(-1);
			}

			if (this.numPrice > GlobalConstants.LOTTERY_MAX_PRICE) {
				ToastUtil.shortToast(LotteryOldFootballActivity.this,
						getResources().getString(R.string.lottery_money_than));
				return;
			}

			Map<Integer, List<Integer>> mSelectItemIndexs = null;
			mSelectItemIndexs = mBaseLotteryBidOnlyAdapter
					.getSelectItemIndexs();
			if (mSelectItemIndexs.size() == 0) {
				ToastUtil.shortToast(LotteryOldFootballActivity.this,
						getResources().getString(R.string.lottery_betting_tj3));
				return;
			} else {
				int titleIndex = 0;
				if (mLotoId == SPF) {
					String titleWord = textWord.getText().toString();
					String titleIssue = titleWord.split("\\ ")[1].substring(0,
							(titleWord.split("\\ ")[1].length() - 1));

					for (int i = 0; i < infos.size(); i++) {
						if (titleIssue.equals(infos.get(i).getIssue())) {
							titleIndex = i;
						}
					}
				} else {
					titleIndex = 1;
				}

				switch (mLotoId) {
				case JQS:
				case BQC:
					if (mTVCartNumMult.getVisibility() != View.VISIBLE) {
						ToastUtil.shortToast(
								LotteryOldFootballActivity.this,
								getResources().getString(
										R.string.lottery_betting_tj3));
						return;
					}
					break;
				default:
					break;
				}

				String isChase = "0";
				String chase = "1";
				String bettingType = "1";
				String issue = "0";
				String stopCondition = "1";
				// 总金额
				String totalSum = "0";
				String choiceType = "1";
				Map<Integer, Map<String, String>> lists = new LinkedHashMap<Integer, Map<String, String>>();

				String codePlay = "";

				String planEndTime = "";
				// 注数
				String itemTotal = "";
				// 倍数
				String multiple = mTbEditText.getText().toString().trim();

				List<Integer> mSelectItemIndexsKey = null;
				/** 足球信息 **/
				List<List<Match>> mLotteryBidMatchs = null;

				// Map<Integer, Integer> dans = new HashMap<Integer, Integer>();
				List<Integer> dans = new ArrayList<Integer>();

				// 选择的赛事的数量 JSON拼装时使用
				int matchCount = 0;

				mSelectItemIndexsKey = new ArrayList<Integer>(
						mSelectItemIndexs.keySet());

				int id = 0;
				switch (mLotoId) {
				// 胜平负
				case SPF:
					switch (frament1.mSearchType) {
					case TWIN:
						id = 0;
						if (mSelectItemIndexsKey.size() < 14) {
							ToastUtil.shortToast(this, getResources()
									.getString(R.string.lottery_betting_tj3));
							return;
						}
						break;
					case SINGLE:
						id = 1;
						if (mSelectItemIndexsKey.size() < 9) {
							ToastUtil.shortToast(this, getResources()
									.getString(R.string.lottery_betting_tj3));
							return;
						}
						break;
					}

					break;
				// 总进球数
				case JQS:
					id = 2;
					if (mSelectItemIndexsKey.size() < 4) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.lottery_betting_tj3));
						return;
					}
					break;
				// 半全场
				case BQC:
					id = 3;
					if (mSelectItemIndexsKey.size() < 6) {
						ToastUtil.shortToast(
								this,
								getResources().getString(
										R.string.lottery_betting_tj3));
						return;
					}
					break;

				default:
					break;
				}

				if (titleIndex == 1) {
					// 不等于混合过关
//					multiple = String.valueOf(mFootballLotteryUtil
//							.getMultiple());
					mLotteryBidMatchs = mBaseLotteryBidOnlyAdapter
							.getlotterybidMatchsInfos();
					matchCount = mSelectItemIndexsKey.size();
					// totalSum =
					// String.valueOf(mFootballLotteryUtil.getSumPrice());
					// itemTotal = mFootballLotteryUtil.getLotteryItemTotal();
					itemTotal = this.itemTotal + "";
					totalSum = this.numPrice + "";
					dans = mFootballLotteryUtil.getDans();
					Collections.sort(mSelectItemIndexsKey);

					StringBuilder builder = new StringBuilder();
					for (int i = 0; i < matchCount; i++) {

						int[] arr = FootballLotteryTools.getMatchPositioin(
								mSelectItemIndexsKey.get(i), mLotteryBidMatchs);
						Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);

						issue = match.getIssueNo();

						String sp = "";

						List<Integer> tempIndex = mSelectItemIndexs
								.get(mSelectItemIndexsKey.get(i));

						if (i > 0) {
							int cha = mSelectItemIndexsKey.get(i)
									- mSelectItemIndexsKey.get(i - 1) - 1;
							if (cha > 0) {
								for (int j = 0; j < cha; j++) {
									builder.append("X,");
								}
							}
						} else {
							for (int j = 0; j < mSelectItemIndexsKey.get(i); j++) {
								builder.append("X,");
							}
						}

						switch (mLotoId) {
						// 胜平负
						case SPF:
							if (tempIndex.size() > 1) {
								switch (frament1.mSearchType) {
								case TWIN:
									codePlay = PlayInfo.SPF_FS;
									gameNo = GlobalConstants.TC_SF14;
									break;
								case SINGLE:
									codePlay = PlayInfo.SPF9_FS;
									gameNo = GlobalConstants.TC_SF9;
									break;
								}
							} else {
								switch (frament1.mSearchType) {
								case TWIN:
									if (!codePlay.equals(PlayInfo.SPF_FS)) {
										codePlay = PlayInfo.SPF_DS;
									}
									gameNo = GlobalConstants.TC_SF14;
									break;
								case SINGLE:
									if (!codePlay.equals(PlayInfo.SPF9_FS)) {
										codePlay = PlayInfo.SPF9_DS;
									}
									gameNo = GlobalConstants.TC_SF9;
									break;
								}
							}
							sp = "3,1,0";
							break;
						// 总进球数
						case JQS:
							if (tempIndex.size() > 0) {
								codePlay = PlayInfo.JQ4_FS;
							} else {
								if (!codePlay.equals(PlayInfo.JQ4_FS)) {
									codePlay = PlayInfo.JQ4_DS;
								}
							}
							sp = "0,1,2,3,0,1,2,3";
							break;
						// 半全场
						case BQC:
							if (tempIndex.size() > 0) {
								codePlay = PlayInfo.BQC6_FS;
							} else {
								if (!codePlay.equals(PlayInfo.BQC6_FS)) {
									codePlay = PlayInfo.BQC6_DS;
								}
							}
							sp = "3,1,0,3,1,0";
							break;

						default:
							break;
						}
						String[] bfArr = sp.split("\\,");
						// 胜平负、半全场、进球数

						Collections.sort(tempIndex, new Comparator<Integer>() {
							@Override
							public int compare(Integer arg0, Integer arg1) {
								return arg0.compareTo(arg1);
							}
						});

						if (mLotoId == BQC || mLotoId == JQS) {
							int subIndex = 0;
							if (mLotoId == BQC) {
								subIndex = 2;
							} else if (mLotoId == JQS) {
								subIndex = 3;
							}
							List<Integer> oneList = new ArrayList<Integer>();
							List<Integer> twoList = new ArrayList<Integer>();
							for (int j = 0; j < tempIndex.size(); j++) {
								if (tempIndex.get(j) > subIndex) {
									twoList.add(tempIndex.get(j));
								} else {
									oneList.add(tempIndex.get(j));
								}
							}
							
							for (int j = 0; j < oneList.size(); j++) {
								builder.append(bfArr[oneList.get(j)]);
							}
							builder.append(",");
							
							for (int j = 0; j < twoList.size(); j++) {
								builder.append(bfArr[twoList.get(j)]);
							}
							
							builder.append(",");
						} else {
							for (int j = 0; j < tempIndex.size(); j++) {
								builder.append(bfArr[tempIndex.get(j)]);
								if (j == (tempIndex.size() - 1)) {
									builder.append(",");
								}
							}
						}
						
						String date = DateUtil.getTimeInMillisToStr(match
								.getMatchTimeDate() + "");

						if (i == 0) {
							planEndTime = date;
						}
					}

					builder.deleteCharAt(builder.length() - 1);

					if (mLotoId == SPF) {
						String[] arr = builder.toString().split("\\,");
						int cha = 14 - arr.length;
						if (cha > 0) {
							builder.append(",");
							for (int j = 0; j < cha; j++) {
								builder.append("X,");
							}
							builder.deleteCharAt(builder.length() - 1);
						}
						
						if (frament1.mSearchType == frament1.mSearchType.SINGLE) {
							if (mSelectItemIndexsKey.size() > 9) {
								codePlay = PlayInfo.SPF9_FS;
							}
						}
						
					}

					if (dans.size() > 0) {
						builder.append("|");
						// List<Integer> danKeys = new
						// ArrayList<Integer>(dans.keySet());
						for (int j = 0; j < dans.size(); j++) {
							builder.append(dans.get(j) + 1);
							builder.append(",");
						}
						builder.deleteCharAt(builder.length() - 1);
					}

					Map<String, String> bettingReq = new LinkedHashMap<String, String>();

					bettingReq.put("codePlay", codePlay);
					bettingReq.put("codeContent", builder.toString());
					//TODO  确认修改位置
					bettingReq.put("codeMultiple", "1");
					bettingReq.put("codeMoney", (Integer.parseInt(totalSum)/Integer.parseInt(multiple))+"");
					bettingReq.put("codeNumbers", itemTotal);

					lists.put(0, bettingReq);

					TzObj obj = new TzObj();
					obj.bettingType = bettingType;
					obj.gameNo = gameNo;
					obj.issue = issue;
					obj.multiple = multiple;
					obj.totalSum = totalSum; // 订单金额
					obj.chase = chase;
					obj.isChase = isChase;
					obj.choiceType = choiceType;
					obj.planEndTime = planEndTime;
					obj.stopCondition = stopCondition;
					obj.gameType = gameNo;
					obj.bettingReq = lists;
					// CommitPayActivity
					// Intent intent = new
					// Intent(LotteryOldFootballActivity.this,
					// ConfirmPayActivity.class);
					// intent.putExtra(ConfirmPayActivity.DATA, obj);
					Intent intent = new Intent(LotteryOldFootballActivity.this,
							CommitPayActivity.class);
					intent.putExtra(CommitPayActivity.DATA, obj);
					intent.putExtra("lotoId", id);
					intent.putExtra(
							"selectChuan",
							mTVCartNumMult
									.getText()
									.toString()
									.substring(
											1,
											mTVCartNumMult.getText().toString()
													.length()));
					intent.putExtra("multiple", multiple);
					startActivity(intent);
				}
			}
			break;
		// 更多
		case R.id.imgView_footballbid_more:

			new PopupWndSwitchPLayMenu(LotteryOldFootballActivity.this, this).showPopWindow(v);

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
	public void onSelectItemClick(String selectData, int position) {
		showLoadingDialog();
		currentPostion = position;
		mSelectIndex = position;
		textWord.setText(getResources().getString(R.string.tz_select_sfc) + " "
				+ selectData + getResources().getString(R.string.cart_add_qi));
		if (position == 1) {
			List<Fragment> fragments = mFragmentManager.getFragments();

			if (fragments != null) {
				for (Fragment frament : fragments) {
					if (frament instanceof BaseLotteryBidFrament) {
						BaseLotteryBidFrament f = (BaseLotteryBidFrament) frament;
						f.onSuperActivityOnClick(1);
					}
				}
			}
		} else {
			changeSelectMatchCount2state(0);
		}
		mController.getFootBallMatchsInfo(
				GlobalConstants.NUM_FOOTBALLMATCHSINFO, selectData, gameNo,
				"1", mCallBack);
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
			// 胜平负
			if (mLotoId == SPF) {
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getOldFootBallMatchsInfoSPFSuccess(mMatch, infos);
				} else {
					back.getOldFootBallMatchsInfoSPFFailure(error);
				}
				// 总进球数
			} else if (mLotoId == JQS) {
				// 进球数
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getOldFootBallMatchsJQSSuccess(mMatch, infos);
				} else {
					back.getOldFootBallMatchsJQSFailure(error);
				}
				// 半全场
			} else if (mLotoId == BQC) {
				// 半全场
				if (getLoadState() == LOAD_STATE.SUCCESS) {
					back.getOldFootBallMatchsBQCSuccess(mMatch, infos);
				} else {
					back.getOldFootBallMatchsBQCFailure(error);
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
		String multiple = mTbEditText.getText().toString();
		if (StringUtil.isEmpty(multiple)) {
			multiple = "0";
		}
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		// 提示选择场次
		if (matchCount == 0) {
			if (currentPostion == 1) {

				mTVCartNumMult.setVisibility(View.GONE);
			}
			mTVCartTermMoney.setText(getString(R.string.lottery_please_select));
		} else {
			// mTVCartTermMoney.setText(getString(R.string.lottery_selectcount,
			// matchCount));
			// mTVCartTermMoney.setText(getString(R.string.lottery_select,
			// multiple));
			int mostCount = 14;
			switch (mLotoId) {
			case SPF:
				matchCount = frament1.mBaseAdapter.getSelectItemIndexs() == null ? 0
						: frament1.mBaseAdapter.getSelectItemIndexs().size();
				switch (frament1.mSearchType) {
				case TWIN:

					if (matchCount == 0) {
						mTVCartTermMoney
								.setText(getString(R.string.lottery_please_select));
					} else if (currentPostion != 1) {
						mTVCartTermMoney
								.setText(getString(R.string.lottery_please_select));
					} else {
						if (matchCount < 14) {
							mTVCartTermMoney.setText(getString(
									R.string.lottery_select, multiple));
						}
					}
					// mTVCartTermMoney.setText(getString(R.string.lottery_selectcount,
					// matchCount));

					mostCount = 14;
					break;
				case SINGLE:
					if (matchCount == 0) {
						mTVCartTermMoney
								.setText(getString(R.string.lottery_please_select));
					} else if (currentPostion != 1) {
						mTVCartTermMoney
								.setText(getString(R.string.lottery_please_select));
					} else {
						// mTVCartTermMoney.setText(getString(R.string.lottery_selectcount,
						// matchCount));
						if (matchCount < 9) {
							mTVCartTermMoney.setText(getString(
									R.string.lottery_select, multiple));
						}
					}
					mostCount = 9;
					break;
				default:
					break;
				}
				break;
			case JQS:
				mostCount = 4;
				if (matchCount < 8) {
					mTVCartTermMoney.setText(getString(R.string.lottery_select,
							multiple));
				}
				break;
			case BQC:
				mostCount = 6;
				if (matchCount < 12) {
					mTVCartTermMoney.setText(getString(R.string.lottery_select,
							multiple));
				}
				break;
			default:
				break;
			}
			if (matchCount >= mostCount && currentPostion == 1) {
				startCalculate();
			} else {
				mTVCartNumMult.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 计算金额
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年3月28日
	 */
	private void startCalculate() {
		if (isCalculating) {
			return;
		}
		final String multiple = TextUtils.isEmpty(mTbEditText.getText()
				.toString())
				|| Integer.parseInt(mTbEditText.getText().toString()) < 1 ? "1"
				: mTbEditText.getText().toString();
		// 计算注数、及奖金

		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		mBaseLotteryBidOnlyAdapter = frament1.mBaseAdapter;

		Map<Integer, List<Integer>> mSelectItemIndexs = mBaseLotteryBidOnlyAdapter
				.getSelectItemIndexs();
		List<Integer> keys = new ArrayList<Integer>(mSelectItemIndexs.keySet());

		switch (mLotoId) {
		// 胜平负
		case SPF:
			switch (frament1.mSearchType) {
			case TWIN:
				if (keys.size() < 14) {
					return;
				}
				break;
			case SINGLE:
				if (keys.size() < 9) {
					return;
				}
				break;
			}

			break;
		// 总进球数
		case JQS:
			if (keys.size() < 4) {
				return;
			}
			break;
		// 半全场
		case BQC:
			if (keys.size() < 6) {
				return;
			}
			break;

		default:
			break;
		}

		// 设置胆
		if (mFootballLotteryUtil.getDans() == null) {
			mFootballLotteryUtil.addDans(new ArrayList<Integer>());
		} else {
			if (mBaseLotteryBidOnlyAdapter.getDans() == null) {
				mFootballLotteryUtil.addDans(new ArrayList<Integer>());
			} else {
				mFootballLotteryUtil.addDans(mBaseLotteryBidOnlyAdapter
						.getDans());
			}
		}

		isCalculating = true;
		// Object[] result = mFootballLotteryUtil.startCalculate();
		//
		// int itemTotal = (Integer) result[0];
		// int numPrice = (Integer) result[1];

		final Map<Integer, List<Integer>> selectItemIndexs = mBaseLotteryBidOnlyAdapter
				.getSelectItemIndexs();
		if (mLotoId == SPF) {// 任选
			switch (frament1.mSearchType) {
			case TWIN:
				new Thread() {
					public void run() {
						itemTotal = LotteryClick.clickSfcHint(mLotoId,
								selectItemIndexs,
								mFootballLotteryUtil.getDans());
						final BigDecimal mul = new BigDecimal(multiple);
						numPrice = new BigDecimal(itemTotal).multiply(mul)
								.multiply(new BigDecimal(2)).intValue();
						runOnUiThread(new Runnable() {
							public void run() {

								isCalculating = false;
								mTVCartNumMult.setVisibility(View.VISIBLE);

								String moneyString = getResources().getString(
										R.string.all);
								String num = String.valueOf(itemTotal);
								String numString = getResources().getString(
										R.string.betting1);
								String mult = mTbEditText.getText().toString();
								String multString = getResources().getString(
										R.string.cart_add_bei);
								String totalNum = moneyString + num + numString
										+ mult + multString;
								mTVCartNumMult.setText(totalNum);
								mTVCartTermMoney.setText(numPrice
										+ getResources().getString(
												R.string.lemi_unit));
							}
						});
					};
				}.start();
				break;
			case SINGLE:
				new Thread() {
					public void run() {
						itemTotal = LotteryClick.clickSfcHint(3,
								selectItemIndexs,
								mFootballLotteryUtil.getDans());
						BigDecimal mul = new BigDecimal(multiple);
						numPrice = new BigDecimal(itemTotal).multiply(mul)
								.multiply(new BigDecimal(2)).intValue();
						runOnUiThread(new Runnable() {
							public void run() {
								isCalculating = false;
								mTVCartNumMult.setVisibility(View.VISIBLE);

								String moneyString = getResources().getString(
										R.string.all);
								String num = String.valueOf(itemTotal);
								String numString = getResources().getString(
										R.string.betting1);
								String mult = mTbEditText.getText().toString();
								String multString = getResources().getString(
										R.string.cart_add_bei);
								String totalNum = moneyString + num + numString
										+ mult + multString;
								mTVCartNumMult.setText(totalNum);
								mTVCartTermMoney.setText(numPrice
										+ getResources().getString(
												R.string.lemi_unit));
							}
						});
					};
				}.start();
				break;
			default:
				break;
			}

		} else {
			// 设置最小的胆
			mFootballLotteryUtil.setMinDan(0);
			// 设置最大的胆
			mFootballLotteryUtil.setMaxDan(0);

			mFootballLotteryUtil.setFootballMatchs(mBaseLotteryBidOnlyAdapter
					.getlotterybidMatchsInfos());
			mFootballLotteryUtil.setLotId(0);
			mFootballLotteryUtil.setSearchType(null);
			// 设置倍数
			mFootballLotteryUtil.setMultiple(Integer.parseInt(multiple));
			Map<Integer, List<Integer>> indexs = new HashMap<Integer, List<Integer>>();
			List<String> chuans = new ArrayList<String>();
			int mostCount = 14;
			int count = 0;
			switch (mLotoId) {
			case JQS:
				mostCount = 8;
				for (int i = 0; i < keys.size(); i++) {
					List<Integer> list = new ArrayList<Integer>(
							mSelectItemIndexs.get(i));
					count += list.size();
					List<Integer> tempList = new ArrayList<Integer>();
					List<Integer> tempList2 = new ArrayList<Integer>();
					for (int j = 0; j < list.size(); j++) {

						if (list.get(j) > 3) {
							tempList.add(list.get(j));
						} else {
							tempList2.add(list.get(j));
						}
					}
					indexs.put(indexs.size(), tempList2);
					indexs.put(indexs.size(), tempList);
				}
				// 选择的投注项
				mFootballLotteryUtil.setSelectNumber(indexs);

				break;
			case BQC:
				mostCount = 12;

				for (int i = 0; i < keys.size(); i++) {
					List<Integer> list = new ArrayList<Integer>(
							mSelectItemIndexs.get(i));
					count += list.size();
					List<Integer> tempList = new ArrayList<Integer>();
					List<Integer> tempList2 = new ArrayList<Integer>();
					for (int j = 0; j < list.size(); j++) {

						if (list.get(j) > 2) {
							tempList.add(list.get(j));
						} else {
							tempList2.add(list.get(j));
						}
					}
					indexs.put(indexs.size(), tempList2);
					indexs.put(indexs.size(), tempList);
				}
				// 选择的投注项
				mFootballLotteryUtil.setSelectNumber(indexs);
				break;
			default:
				break;
			}
			chuans.add(mostCount + "串1");
			mFootballLotteryUtil.addChuans(chuans);

			if (count >= mostCount) {

				Object[] result = mFootballLotteryUtil.startCalculate();
				itemTotal = (Integer) result[0];
				numPrice = (Integer) result[1];
				if (itemTotal > 0) {
					mTVCartNumMult.setVisibility(View.VISIBLE);

					String moneyString = getResources().getString(R.string.all);
					String num = String.valueOf(itemTotal);
					String numString = getResources().getString(
							R.string.betting1);
					// String mult = mTbEditText.getText().toString();
					String multString = getResources().getString(
							R.string.cart_add_bei);
					String totalNum = moneyString + num + numString + multiple
							+ multString;
					mTVCartNumMult.setText(totalNum);
					mTVCartTermMoney.setText(numPrice
							+ getResources().getString(R.string.lemi_unit));
				} else {
					mTVCartNumMult.setVisibility(View.GONE);
				}

			} else {
				mTVCartNumMult.setVisibility(View.GONE);
			}
			isCalculating = false;
		}

	}

	private CallBack mCallBack = new CallBack() {

		public void getLoBoPeriodSuccess(final List<LoBoPeriodInfo> infos) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (infos != null) {
						textWord.setClickable(true);
						mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
								gameNo, "2", mCallBack);
						List<LoBoPeriodInfo> tempInfos = new ArrayList<LoBoPeriodInfo>();
						tempInfos.add(infos.get(0));
						if (infos.size() > 1) {
							tempInfos.add(infos.get(1));
						}
						LotteryOldFootballActivity.this.infos = tempInfos;
					} else {
						textWord.setClickable(false);
					}
				}
			});
		};

		public void getLoBoPeriodFailure(final String error) {
			runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					notifyFramentDataChange(error);
				}
			});
		};

		/**
		 * 足彩数据加载成功（胜平负、进球数、半全场、比分）
		 * 
		 * @Description:
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
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
						// mPopWindowFootballMatchChoose.setData(mMatchs);
						hideLoadingDialog();
					}
				}
			});
		};

		/**
		 * 足彩数据获取失败（胜平负、进球数、半全场、比分）
		 * 
		 * @Description:
		 * @param error
		 * @author:www.wenchuang.com
		 * @date:2015年11月16日
		 */
		public void getFootBallMatchsInfoFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// mLotoId = lotoId;
					// loadState = LOAD_STATE.ERROR;
					// 更新对应的frament数据结果
					// notifyFramentDataChange(null);
					// hideLoadingDialog();
					// ToastUtil.shortToast(LotteryOldFootballActivity.this,
					// error);
					mController.getFootBallMatchsInfo(
							GlobalConstants.NUM_FOOTBALLMATCHSINFO,
							infos.get(mSelectIndex).getIssue(), gameNo, "1",
							mCallBack);
				}
			});
		};

		public void getMatchTimeSuccess(final IssueList list) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!list.getResCode().equals("0") || list == null) {
						mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
								gameNo, "2", mCallBack);
					} else {
						if (infos.size() < 3) {
							infos.add(0, list.getIssues().get(1));
						}
						mController.getFootBallMatchsInfo(
								GlobalConstants.NUM_FOOTBALLMATCHSINFO, infos
										.get(mSelectIndex).getIssue(), gameNo,
								"1", mCallBack);
						mPopWindowJczq.setData(infos, mSelectIndex);
						if (mLotoId == SPF) {
							textWord.setText(getResources().getString(
									R.string.tz_select_sfc)
									+ " "
									+ mPopWindowJczq.getData()
											.get(mSelectIndex).getIssue()
									+ getResources().getString(
											R.string.cart_add_qi));
						}
					}
				}
			});
		};

		public void getMatchTimeFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mController.getMatchTime(GlobalConstants.NUM_MATCHTIME,
							gameNo, "2", mCallBack);
				}
			});
		};
	};

	private class TbTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String string = s.toString();
			if (!TextUtils.isEmpty(string)) {
				int parseInt = Integer.parseInt(string);
				if (parseInt > mMultiple) {
					mTbEditText.setText(mMultiple + "");
					LotteryShowUtil.setSelection(mTbEditText);
					ToastUtil.shortToast(
							LotteryOldFootballActivity.this,
							getResources().getString(R.string.cart_max_mult,
									mMultiple+""));
				}
				if (string.length() > 1 && string.substring(0, 1).equals("0")) {
					mTbEditText.setText(string.substring(1));
				}
			}
			startCalculate();

			// changeSelectMatchCount2state(-1);
		}
	}

	@Override
	public void PopDraw() {
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		LotteryDrawInfo info = new LotteryDrawInfo();
		switch (mLotoId) {
		case SPF:
			switch (frament1.mSearchType) {
			case TWIN:
				info.setGameNo(GlobalConstants.TC_SF14);
				info.setGameName(getResources().getString(
						R.string.tz_select_sfc));
				break;
			case SINGLE:
				info.setGameNo(GlobalConstants.TC_SF9);
				info.setGameName(getResources()
						.getString(R.string.tz_select_r9));
				break;
			default:
				break;
			}
			break;
		case JQS:
			info.setGameNo(GlobalConstants.TC_JQ4);
			info.setGameName(getResources().getString(R.string.tz_select_jqs));
			break;
		case BQC:
			info.setGameNo(GlobalConstants.TC_BQ6);
			info.setGameName(getResources().getString(R.string.tz_select_bqc));
			break;
		default:
			break;
		}
		//修改跳转 跳转到比分直播
//		Intent intent1 = new Intent(LotteryOldFootballActivity.this,
//				LotteryDrawListActivity.class);
//		intent1.putExtra("LotteryDrawInfo", info);
//		startActivity(intent1);
		Intent intent1 = new Intent(LotteryOldFootballActivity.this, 
				ScoreListActivity.class);
		intent1.putExtra("special_flag", "lotteryoldfootball");
		startActivity(intent1);
	}

	@Override
	public void PopPLay() {
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		Intent intent = new Intent(this, HtmlCommonActivity.class);
		intent.putExtra("isWeb", false);
		switch (mLotoId) {
		case SPF:
			switch (frament1.mSearchType) {
			case TWIN:
				intent.putExtra("lotteryName", GlobalConstants.LOTTERY_SF14);
				break;
			case SINGLE:
				intent.putExtra("lotteryName", GlobalConstants.LOTTERY_SF9);
				break;
			default:
				break;
			}
			break;
		case JQS:
			intent.putExtra("lotteryName", GlobalConstants.LOTTERY_JQ4);
			break;
		case BQC:
			intent.putExtra("lotteryName", GlobalConstants.LOTTERY_BQ6);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

	@Override
	public void PopTzList() {
		int index = mViewPagerView.getCurrentItem();
		BaseLotteryBidFrament frament1 = (BaseLotteryBidFrament) myPagerAdapter
				.getItem(index);
		int type1 = GlobalConstants.TAG_TZLIST_SFC;
		String text = getResources().getString(R.string.tz_select_sfc);

	
		switch (mLotoId) {
		case SPF:
			switch (frament1.mSearchType) {
			case TWIN:
				type1 = GlobalConstants.TAG_TZLIST_SFC;
				GlobalConstants.personGameNo = GlobalConstants.TC_SF14;
				text = getResources().getString(R.string.tz_select_sfc);
				break;
			case SINGLE:
				type1 = GlobalConstants.TAG_TZLIST_R9;
				GlobalConstants.personGameNo = GlobalConstants.TC_SF9;
				text = getResources().getString(R.string.tz_select_r9);
				break;
			default:
				break;
			}
			break;
		case JQS:
			type1 = GlobalConstants.TAG_TZLIST_JQS;
			GlobalConstants.personGameNo = GlobalConstants.TC_JQ4;
			text = getResources().getString(R.string.tz_select_jqs);
			break;
		case BQC:
			GlobalConstants.personGameNo = GlobalConstants.TC_BQ6;
			type1 = GlobalConstants.TAG_TZLIST_BQC;
			text = getResources().getString(R.string.tz_select_bqc);
			break;
		default:
			break;
		}
		GlobalConstants.isRefreshFragment = true;
		GlobalConstants.personTagIndex = 0;
		GlobalConstants.personEndIndex = 0;
		GlobalConstants.isShowLeMiFragment = true;
		MainActivity main = (MainActivity) ActivityManager
				.isExistsActivity(MainActivity.class);
		if (main != null) {
			// 如果存在MainActivity实例，那么展示LoboHallFrament页面
			ActivityManager.retain(MainActivity.class);
			main.showFrament(3);
		}
//			Intent intent = new Intent(this, TzListActivity.class);
//			intent.putExtra("type", type1);
//			intent.putExtra("text", text);
//			startActivity(intent);
	
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
	}
}
