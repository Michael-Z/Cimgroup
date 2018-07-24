package cn.com.cimgroup.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.BaseLotteryBidCartAdapter.AdapterCartChange;
import cn.com.cimgroup.adapter.BaseLotteryBidOnlyCartAdapter;
import cn.com.cimgroup.adapter.LotteryBasketBallDXCartAdapter;
import cn.com.cimgroup.adapter.LotteryBasketBallHHGGCartAdapter;
import cn.com.cimgroup.adapter.LotteryBasketBallSFCCartAdapter;
import cn.com.cimgroup.adapter.LotteryBasketBallSFCartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBall2X1CartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallBFCartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallBQCCartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallHHGGCartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallJQSCartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallNchuanMAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallOneWinCartAdapter;
import cn.com.cimgroup.adapter.LotteryFootBallSPFCartAdapter;
import cn.com.cimgroup.bean.Betting;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.TzObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.DLTConfiguration;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryHHGGUtil;
import cn.com.cimgroup.util.FootballLotteryTools;
import cn.com.cimgroup.util.FootballLotteryUtil;
import cn.com.cimgroup.util.PlayInfo;
import cn.com.cimgroup.util.betingCompute.MatchBetFactory;
import cn.com.cimgroup.util.betingCompute.MatchBetService;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 竞彩购物车 TODO 此购物车为足彩、篮彩所有投注方式的集中体现，其中胜平负、让球胜平负、进球数、比分、半全场。格式基本一致，通过lotoid区分。
 * 混合过关的数据格式与其他的差别较大，大部分需单独处理。
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月21日
 */
public class LotteryBidCartActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, AdapterCartChange {

	private static final String LOTTERYBIDMATCH_INFO = "lotterybidmatch_info";
	private static final String LOTTERYBIDMATCH_SPILTTIME = "lotterybidmatch_spilttimes";
	public static final String LOTTERYBIDMATCH_SELECT_INDEXS = "lotterybidmatch_select_indexs";
	private static final String FOOTBALLMATCH_HHGG_SELECT_INDEXS = "footballmatch_hhgg_select_indexs";
	
	public static final String LOTTERYONEWIN_SYS = "lotteryonewin_sys";
	private static final String LOTOID = "lotoid";

	/** 购物车适配器(足彩：胜平负、让球胜平负、进球数、比分、半全场 篮彩：让分胜负、胜负、大小分、胜分差) **/
	private BaseLotteryBidOnlyCartAdapter mOnlyLotteryBidAdapter;

	private LotteryFootBallSPFCartAdapter mSPFFootballAdapter;

	private LotteryBasketBallSFCartAdapter mSFBasketballAdapter;

	private LotteryBasketBallHHGGCartAdapter mHHGGBasketballAdapter;

	/** 购物车适配器(混合过关) **/
	private LotteryFootBallHHGGCartAdapter mHhggFootballAdapter;

	/** 筛选方式 **/
	public static MatchSearchType mSearchType;

	/** 选择的串 **/
	private List<String> mSelectChuan = new ArrayList<String>();
	/** mSelectChuanIndex串1 **/
	private int mSelectChuanIndex = 0;

	private int lotoId = 0;

	/** 购物车listview **/
	private ListView mMatchCartListView;

	/** 过关类型 **/
	private TextView mChuanTypeView;
	
	private ImageView mChuanTypeImageView;

	/** N串M layout **/
	private LinearLayout mNchuanMLayoutView;

	/** N串M gridview **/
	private GridView mNchuanMGridView;

	/** N串M Adapter **/
	private LotteryFootBallNchuanMAdapter mNchuanMAdapter;

	/** N串M选择取消 **/
	private Button mSelectNchuanMCanlView;

	/** N串M选择确认 **/
	private Button mSelectNchuanMOkView;

	/** 投注 、花费的钱数 **/
	public TextView txtView_total2payprice;

	/** 奖金 **/
	public TextView txtView_win_price;

	/** 返回 **/
	private TextView mBackView;

	/** 胜平负、让球胜平负、进球数、半全场、比分 **/
	private FootballLotteryUtil mFootballLotteryUtil;
	/** 混合过关 **/
	private FootballLotteryHHGGUtil mFootballLotteryHHGGUtil;

	/** 增加倍数 **/
	private LinearLayout mMultiplePlusView;

	/** 减少倍数 **/
	private LinearLayout mMultipleMinusView;

	/** 倍数选择 **/
	public EditText editView_lotterybidmultipley;

	/** 投注 **/
	private TextView mBettingView;

	private Controller mController;

//	private int defaultBei = 1;

	private Map<Integer, Map<Integer, List<Integer>>> mSelectHhggItemindexs;

	private UserInfo userInfo;

	private int mMultiple = 999;// 倍数

	private MatchBetService mMatchBetService;

	private long needpay = 0;

	private long itemTotal = 0;

	private boolean isCalculating = false;
	
	private Match oneWinMatch;
	
	private static int count = 0;

	/**
	 * 跳转到投注购物车 (足球：进球数、比分、胜平负、半全场 篮球：让分胜负、胜负、大小分、胜分差)
	 * 
	 * @Description:
	 * @param activity
	 * @param spiltTimes
	 * @param matchsInfos
	 * @param selectItemindex
	 * @param searchType
	 * @param lotoId
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	public static void forwardLotteryBidOnlyCartActivity(Activity activity,
			List<String> spiltTimes, List<List<Match>> matchsInfos,
			Map<Integer, List<Integer>> selectItemindex,
			MatchSearchType searchType, int lotoId) {
		Intent intent = new Intent(activity, LotteryBidCartActivity.class);
		intent.putExtra(LOTTERYBIDMATCH_INFO, (Serializable) matchsInfos);
		intent.putExtra(LOTTERYBIDMATCH_SPILTTIME, (Serializable) spiltTimes);
		intent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,
				(Serializable) selectItemindex);
		intent.putExtra(LOTOID, lotoId);
		mSearchType = searchType;
		activity.startActivityForResult(intent, lotoId);
	}
	
	public static void forwardLotteryOneWinCartActivity(Activity activity,
			List<String> spiltTimes, List<List<Match>> matchsInfos,
			Map<Integer, List<Integer>> selectItemindex,
			MatchSearchType searchType, Match match, int lotoId) {
		if (count == 0) {
			Intent intent = new Intent(activity, LotteryBidCartActivity.class);
			intent.putExtra(LOTTERYBIDMATCH_INFO, (Serializable) matchsInfos);
			intent.putExtra(LOTTERYBIDMATCH_SPILTTIME, (Serializable) spiltTimes);
			intent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,(Serializable) selectItemindex);
			intent.putExtra(LOTTERYONEWIN_SYS,(Serializable) match);
			intent.putExtra(LOTOID, lotoId);
			mSearchType = searchType;
			activity.startActivityForResult(intent, lotoId);
			count = 1;
		}
		
	}

	/**
	 * 跳转到混合过关投注购物车 (混合过关)
	 * 
	 * @Description:
	 * @param activity
	 * @param spiltTimes
	 * @param matchsInfos
	 * @param selectItemindex
	 * @param searchType
	 * @param lotoId
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	public static void forwardLotteryFootballHhggCartActivity(
			Activity activity, List<String> spiltTimes,
			List<List<Match>> matchsInfos,
			Map<Integer, Map<Integer, List<Integer>>> selectItemindex,
			MatchSearchType searchType, int lotoId) {
		Intent intent = new Intent(activity, LotteryBidCartActivity.class);
		intent.putExtra(LOTTERYBIDMATCH_INFO, (Serializable) matchsInfos);
		intent.putExtra(LOTTERYBIDMATCH_SPILTTIME, (Serializable) spiltTimes);
		intent.putExtra(FOOTBALLMATCH_HHGG_SELECT_INDEXS,
				(Serializable) selectItemindex);
		intent.putExtra(LOTOID, lotoId);
		mSearchType = searchType;
		activity.startActivityForResult(intent, lotoId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_lotterybid_cart);
		String temp = "1";
		if (savedInstanceState != null) {
			temp = savedInstanceState.getString("searchType");
			// System.out.println("onCreate: searchType = " + temp);
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
		}else {
//			mSearchType = MatchSearchType.TWIN;
		}
		initUI();
		initListener();
		initData();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		switch (mSearchType) {
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		count = 0;
	}

	/** 标题 */
	private TextView textView_title_word;

	void initUI() {
		lotoId = getIntent().getIntExtra(LOTOID, 0);
		mController = Controller.getInstance();
		mBackView = (TextView) findViewById(R.id.textView_title_back);
		textView_title_word = (TextView) findViewById(R.id.textView_title_word);
		textView_title_word.setText(getResources().getString(
				R.string.lottery_betting_makesure));
		mNchuanMLayoutView = (LinearLayout) findViewById(R.id.layoutView_lotterybid_nchuanm);
		mNchuanMLayoutView.setOnClickListener(this);
		mMatchCartListView = (ListView) findViewById(R.id.lvView_lotterybid_cart);
		mChuanTypeView = (TextView) findViewById(R.id.btnView_chuan_type);
		mChuanTypeImageView = (ImageView) findViewById(R.id.imageView_chuan_type);
		mNchuanMGridView = (GridView) findViewById(R.id.gridView_lotterybid_nchuanm);
		mSelectNchuanMCanlView = (Button) findViewById(R.id.btnView_lotterybid_nchuanm_canl);
		mSelectNchuanMOkView = (Button) findViewById(R.id.btnView_lotterybid_nchuanm_ok);
		txtView_total2payprice = (TextView) findViewById(R.id.txtView_total2payprice);
		txtView_win_price = (TextView) findViewById(R.id.txtView_win_price);
		mMultiplePlusView = (LinearLayout) findViewById(R.id.imgView_lotterybidmultiple_plus);
		mMultipleMinusView = (LinearLayout) findViewById(R.id.imgView_lotterybidmultiple_minus);
		editView_lotterybidmultipley = (EditText) findViewById(R.id.editView_lotterybidmultipley);
		mBettingView = (TextView) findViewById(R.id.btnView_lotterybid_betting);
		if (lotoId == FootballLotteryConstants.L502) {
			editView_lotterybidmultipley.setText(String.valueOf(5));
		} else {
			editView_lotterybidmultipley.setText(String.valueOf(1));
		}
		// 默认隐藏
		mNchuanMLayoutView.setVisibility(View.GONE);
		editView_lotterybidmultipley
				.addTextChangedListener(new MultTextWatcher());
	}

	void initListener() {
		mChuanTypeView.setOnClickListener(this);
		mNchuanMGridView.setOnItemClickListener(this);
		mSelectNchuanMCanlView.setOnClickListener(this);
		mSelectNchuanMOkView.setOnClickListener(this);
		mBackView.setOnClickListener(this);
		mBettingView.setOnClickListener(this);
		mMultipleMinusView.setOnClickListener(this);
		mMultiplePlusView.setOnClickListener(this);
	}

	@SuppressWarnings("unchecked")
	void initData() {
		
		mMatchBetService = MatchBetFactory.create(String.valueOf(lotoId));
		mFootballLotteryUtil = new FootballLotteryUtil();
		mFootballLotteryHHGGUtil = new FootballLotteryHHGGUtil();
		mFootballLotteryUtil.setLotId(lotoId);
		mFootballLotteryHHGGUtil.setLotId(lotoId);
		
		// 比分
		if (lotoId == FootballLotteryConstants.L302) {
			mOnlyLotteryBidAdapter = new LotteryFootBallBFCartAdapter(this,
					mSearchType, mFootballLotteryUtil);
			// 让球胜平负
		} else if (lotoId == FootballLotteryConstants.L301) {
			// mOnlyLotteryBidAdapter = new
			// LotteryFootBallRQSPFCartAdapter(this);
			// 胜平负
		} else if (lotoId == FootballLotteryConstants.L320) {
			mSPFFootballAdapter = new LotteryFootBallSPFCartAdapter(this,
					mSearchType, mFootballLotteryHHGGUtil);
			// 进球数
		} else if (lotoId == FootballLotteryConstants.L303) {
			mOnlyLotteryBidAdapter = new LotteryFootBallJQSCartAdapter(this,
					mSearchType, mFootballLotteryUtil);
			// 半全场
		} else if (lotoId == FootballLotteryConstants.L304) {
			mOnlyLotteryBidAdapter = new LotteryFootBallBQCCartAdapter(this,
					mSearchType, mFootballLotteryUtil);
			// 混合过关
		} else if (lotoId == FootballLotteryConstants.L305) {
			mHhggFootballAdapter = new LotteryFootBallHHGGCartAdapter(this,
					mSearchType, mFootballLotteryHHGGUtil);
		} else if (lotoId == FootballLotteryConstants.L309) {
			mOnlyLotteryBidAdapter = new LotteryBasketBallDXCartAdapter(this,
					mSearchType, mFootballLotteryUtil);
			textView_title_word.setText(getResources().getString(R.string.lottery_betting_baseket_makesure));
		} else if (lotoId == FootballLotteryConstants.L308) {
			mOnlyLotteryBidAdapter = new LotteryBasketBallSFCCartAdapter(this,
					mSearchType, mFootballLotteryUtil);
			textView_title_word.setText(getResources().getString(R.string.lottery_betting_baseket_makesure));
		} else if (lotoId == FootballLotteryConstants.L307) {
			mSFBasketballAdapter = new LotteryBasketBallSFCartAdapter(this,
					mSearchType, mFootballLotteryHHGGUtil);
			textView_title_word.setText(getResources().getString(R.string.lottery_betting_baseket_makesure));
		} else if (lotoId == FootballLotteryConstants.L310) {
			mHHGGBasketballAdapter = new LotteryBasketBallHHGGCartAdapter(this,
					mSearchType, mFootballLotteryHHGGUtil);
			textView_title_word.setText(getResources().getString(R.string.lottery_betting_baseket_makesure));
		} else if (lotoId == FootballLotteryConstants.L501) {
			mOnlyLotteryBidAdapter = new LotteryFootBall2X1CartAdapter(this,
					mSearchType, mFootballLotteryUtil);
			textView_title_word.setText(getResources().getString(R.string.lottery_betting_2x1_makesure));
		} else if (lotoId == FootballLotteryConstants.L502) {
			oneWinMatch = (Match) getIntent()
					.getSerializableExtra(LOTTERYONEWIN_SYS);
			mOnlyLotteryBidAdapter = new LotteryFootBallOneWinCartAdapter(this,
					mSearchType, mFootballLotteryUtil, oneWinMatch);
			textView_title_word.setText(getResources().getString(R.string.lottery_betting_1czs_makesure));
		} else {
			this.finish();
		}
		// 不等于混合过关
		if (lotoId != FootballLotteryConstants.L305
				&& lotoId != FootballLotteryConstants.L320
				&& lotoId != FootballLotteryConstants.L307
				&& lotoId != FootballLotteryConstants.L310) {
			// 获取本类型比赛的所有比赛信息TODO 因为计算时要用到这些数据，以后可能进行修改为选择的比赛信息
			List<List<Match>> matchs = (List<List<Match>>) getIntent()
					.getSerializableExtra(LOTTERYBIDMATCH_INFO);
			// 获取本类型比赛的索引比赛信息的sp值
			List<String> spiltTimes = (List<String>) getIntent()
					.getSerializableExtra(LOTTERYBIDMATCH_SPILTTIME);
			// 获取选择的索引信息
			Map<Integer, List<Integer>> mSelectItemindexs = (Map<Integer, List<Integer>>) getIntent()
					.getSerializableExtra(LOTTERYBIDMATCH_SELECT_INDEXS);
			mOnlyLotteryBidAdapter.setData(matchs, spiltTimes,
					mSelectItemindexs);
			mMatchCartListView.setAdapter(mOnlyLotteryBidAdapter);
			mSelectChuanIndex = mSelectItemindexs == null ? 0
					: mSelectItemindexs.size();
			// 添加数据发生改变时回调接口
			mOnlyLotteryBidAdapter.addDataChagneListener(this);
			mNchuanMAdapter = new LotteryFootBallNchuanMAdapter(this,
					mSelectChuanIndex, lotoId, mSelectItemindexs,
					mFootballLotteryUtil);
		} else {
			// 获取本类型比赛的所有比赛信息TODO 因为计算时要用到这些数据，以后可能进行修改为选择的比赛信息
			List<List<Match>> matchs = (List<List<Match>>) getIntent()
					.getSerializableExtra(LOTTERYBIDMATCH_INFO);
			// 获取本类型比赛的索引比赛信息的sp值
			List<String> spiltTimes = (List<String>) getIntent()
					.getSerializableExtra(LOTTERYBIDMATCH_SPILTTIME);
			// 获取选择的索引信息
			Map<Integer, Map<Integer, List<Integer>>> mSelectHhggItemindexs = (Map<Integer, Map<Integer, List<Integer>>>) getIntent()
					.getSerializableExtra(FOOTBALLMATCH_HHGG_SELECT_INDEXS);

			mNchuanMAdapter = new LotteryFootBallNchuanMAdapter(this,
					mSelectHhggItemindexs, mFootballLotteryHHGGUtil);
			mSelectChuanIndex = mSelectHhggItemindexs == null ? 0
					: mSelectHhggItemindexs.size();

			if (lotoId == FootballLotteryConstants.L320) {
				mSPFFootballAdapter.setData(matchs, mSelectHhggItemindexs,
						spiltTimes);
				mMatchCartListView.setAdapter(mSPFFootballAdapter);
				// 添加数据发生改变时回调接口
				mSPFFootballAdapter.addDataChagneListener(this);
			} else if (lotoId == FootballLotteryConstants.L307) {
				mSFBasketballAdapter.setData(matchs, mSelectHhggItemindexs,
						spiltTimes);
				mMatchCartListView.setAdapter(mSFBasketballAdapter);
				// 添加数据发生改变时回调接口
				mSFBasketballAdapter.addDataChagneListener(this);
			} else if (lotoId == FootballLotteryConstants.L310) {
				mHHGGBasketballAdapter.setData(matchs, mSelectHhggItemindexs,
						spiltTimes);
				mMatchCartListView.setAdapter(mHHGGBasketballAdapter);
				mHHGGBasketballAdapter.addDataChagneListener(this);
			} else {
				mHhggFootballAdapter.setData(matchs, mSelectHhggItemindexs,
						spiltTimes);
				mMatchCartListView.setAdapter(mHhggFootballAdapter);
				mHhggFootballAdapter.addDataChagneListener(this);
			}
		}

		mNchuanMGridView.setAdapter(mNchuanMAdapter);
		mSelectChuan = mNchuanMAdapter.getSelectData();

		mNchuanMAdapter.setSelectIndex(mNchuanMAdapter.getCount() == 0 ? 0
				: mNchuanMAdapter.getCount() - 1);
		// 计算注数、预计奖金
		startCalculate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnView_chuan_type:
			// 打开投注方式popWindow
			if (lotoId  == FootballLotteryConstants.L502) {
				Intent tIntent = new Intent();
				Map<Integer, List<Integer>> indexs = mOnlyLotteryBidAdapter.getSelectItemIndexs();
				List<Integer> keys = new ArrayList<Integer>(indexs.keySet());
				Collections.sort(keys);
				indexs.remove(keys.get(keys.size() - 1));
				tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS, (Serializable) indexs);
				setResult(lotoId, tIntent);
				finish();
			} else {
				if (mNchuanMLayoutView.getVisibility() == View.GONE) {
					mNchuanMLayoutView.setVisibility(View.VISIBLE);
					// 获取串 TODO如果选择的串，但是点击取消了，那么将串还原为此值
					mSelectChuan = mNchuanMAdapter.getSelectData();
				} else {
					mNchuanMLayoutView.setVisibility(View.GONE);
				}
			}
			break;
		case R.id.layoutView_lotterybid_nchuanm:
			mNchuanMLayoutView.setVisibility(View.GONE);
			mNchuanMAdapter.setSelectData(mSelectChuan);
			break;
		// N串M选择取消
		case R.id.btnView_lotterybid_nchuanm_canl:
			mNchuanMLayoutView.setVisibility(View.GONE);
			mNchuanMAdapter.setSelectData(mSelectChuan);
			break;
		// N串M选择确认
		case R.id.btnView_lotterybid_nchuanm_ok:
			if (mOnlyLotteryBidAdapter != null) {
				int maxChuan = 0;
				// 比分
				if (lotoId == FootballLotteryConstants.L302) {
					maxChuan = 4;
					// 让球胜平负
				} else if (lotoId == FootballLotteryConstants.L301) {
					maxChuan = 8;
					// 胜平负
				} else if (lotoId == FootballLotteryConstants.L320) {
					maxChuan = 8;
					// 进球数
				} else if (lotoId == FootballLotteryConstants.L303) {
					maxChuan = 6;
					// 半全场
				} else if (lotoId == FootballLotteryConstants.L304) {
					maxChuan = 4;
					// 混合过关
				} else if (lotoId == FootballLotteryConstants.L305) {
					// 篮球让球胜负
				} else if (lotoId == FootballLotteryConstants.L306) {
					maxChuan = 8;
					// 篮球胜负
				} else if (lotoId == FootballLotteryConstants.L307) {
					maxChuan = 8;
					// 胜分差
				} else if (lotoId == FootballLotteryConstants.L308) {
					maxChuan = 4;
					// 大小分
				} else if (lotoId == FootballLotteryConstants.L309) {
					maxChuan = 8;

				} else {
					maxChuan = 8;
				}
				mSelectChuan = mNchuanMAdapter.getSelectData();
				if (Integer.parseInt(mSelectChuan.get(mSelectChuan.size() - 1)
						.split(getResources()
								.getString(R.string.football_chuan))[0]) == maxChuan) {
					mOnlyLotteryBidAdapter.setDans(new ArrayList<Integer>());
					Map<String, String> mChuan1Num = new TreeMap<String, String>(
							FootballLotteryConstants.getmChuan1Num());
					mNchuanMAdapter.setmChuan1Num(mChuan1Num);
					mNchuanMAdapter.setnChuamn1Key(new ArrayList(mChuan1Num.keySet()));
					mNchuanMAdapter.setCount(maxChuan - 1);
					mNchuanMAdapter.notifyDataSetChanged();
				}
			}
			startCalculate();
			break;
		// 增加倍数
		case R.id.imgView_lotterybidmultiple_plus:
			if (isCalculating&& mSearchType.equals("TWIN")) {
				return;
			}
			String multiplePlusStr = editView_lotterybidmultipley.getText()
					.toString();
			int multiplePlus = 0;
			if (!TextUtils.isEmpty(multiplePlusStr)) {
				multiplePlus = Integer.parseInt(multiplePlusStr);
				if (multiplePlus < mMultiple) {
					multiplePlus++;
				}
			} else {
				if (lotoId  == FootballLotteryConstants.L502) {
					multiplePlus = 5;
				} else {
					multiplePlus = 1;
				}
			}
			editView_lotterybidmultipley.setText(String.valueOf(multiplePlus));
			startCalculate();
			break;
		// 减少倍数
		case R.id.imgView_lotterybidmultiple_minus:
			if (isCalculating&& mSearchType.equals("TWIN")) {
				return;
			}
			String multipleMinusStr = editView_lotterybidmultipley.getText()
					.toString();
			if (!TextUtils.isEmpty(multipleMinusStr)) {
				int multipleMinus = 0;
				if (lotoId  == FootballLotteryConstants.L502) {
					if (multipleMinusStr.length() == 0) {
						editView_lotterybidmultipley.setText("5");
					}
					multipleMinus = Integer.parseInt(multipleMinusStr);
					if (multipleMinus > 5) {
						multipleMinus--;
					} else {
						multipleMinus = 5;
					}
				} else {
					if (multipleMinusStr.length() == 0) {
						editView_lotterybidmultipley.setText("1");
					}
					multipleMinus = Integer.parseInt(multipleMinusStr);
					if (multipleMinus > 1) {
						multipleMinus--;
					} else {
						multipleMinus = 1;
					}
				}
				editView_lotterybidmultipley.setText(String
						.valueOf(multipleMinus));
				startCalculate();
			}

			break;
		case R.id.textView_title_back:
			showExitDialog();
			break;
		// 进行投注
		case R.id.btnView_lotterybid_betting:
			GlobalTools.hideSoftInput(LotteryBidCartActivity.this);

			if (isCalculating && mSearchType.equals("TWIN")) {
				return;
			}

			String aa = txtView_total2payprice.getText().toString()
					.substring(0, 1);
			if (Integer.parseInt(aa) == 0) {
				return;
			}

			if (needpay > GlobalConstants.LOTTERY_MAX_PRICE) {
				showToast(getResources().getString(R.string.lottery_money_than));
				return;
			}

			String isChase = "0";
			String chase = "1";
			String bettingType = "1";
			String gameNo = GlobalConstants.TC_JCZQ;
			if (lotoId == FootballLotteryConstants.L306
					|| lotoId == FootballLotteryConstants.L307
					|| lotoId == FootballLotteryConstants.L308
					|| lotoId == FootballLotteryConstants.L309
					|| lotoId == FootballLotteryConstants.L310) {
				gameNo = GlobalConstants.TC_JCLQ;
			} else {
				gameNo = GlobalConstants.TC_JCZQ;
			}

			String issue = "0";
			String stopCondition = "1";
			String choiceType = "1";
			Map<Integer, Map<String, String>> lists = new LinkedHashMap<Integer, Map<String, String>>();

			String codePlay = "";

			String planEndTime = "";
			// 倍数
			if (TextUtils.isEmpty(editView_lotterybidmultipley.getText()
					.toString())) {
				editView_lotterybidmultipley.setText("1");
			}
			String multiple = "1";
			/** 选择的索引 胜平负、让球胜平负、进球数、比分、半全场 **/
			Map<Integer, List<Integer>> mSelectItemIndexs = null;//
			List<Integer> mSelectItemIndexsKey = null;
			/** 选择的所有 混合投注 **/
			Map<Integer, Map<Integer, List<Integer>>> mSelectItemHHGGIndexs = null;
			List<Integer> mSelectItemIndexsHHGGKey = null;
			/** 足球信息 **/
			List<List<Match>> mLotteryBidMatchs = null;

			// Map<Integer, Integer> dans = new HashMap<Integer, Integer>();
			List<Integer> dans = new ArrayList<Integer>();

			// 选择的赛事的数量 JSON拼装时使用
			int matchCount = 0;
			// 不等于混合过关
			if (lotoId != FootballLotteryConstants.L305
					&& lotoId != FootballLotteryConstants.L320
					&& lotoId != FootballLotteryConstants.L307
					&& lotoId != FootballLotteryConstants.L310) {
				multiple = String.valueOf(mFootballLotteryUtil.getMultiple());
				if (lotoId == FootballLotteryConstants.L502&&!TextUtils.isEmpty(multiple)&&Integer.parseInt(multiple)<5) {
					multiple="5";
					editView_lotterybidmultipley.setText(multiple);
					startCalculate();
					ToastUtil.shortToast(mActivity, "至少选择5倍");
				}
				mSelectItemIndexs = mOnlyLotteryBidAdapter
						.getSelectItemIndexs();
				mSelectItemIndexsKey = new ArrayList<Integer>(
						mSelectItemIndexs.keySet());
				Collections.sort(mSelectItemIndexsKey);
				mLotteryBidMatchs = mOnlyLotteryBidAdapter
						.getlotterybidMatchsInfos();
				matchCount = mSelectItemIndexsKey.size();
				// totalSum =
				// String.valueOf(mFootballLotteryUtil.getSumPrice());
				// itemTotal = mFootballLotteryUtil.getLotteryItemTotal();
				dans = mFootballLotteryUtil.getDans();
			} else {
				if (lotoId == FootballLotteryConstants.L305) {
					mLotteryBidMatchs = mHhggFootballAdapter
							.getlotterybidMatchsInfos();
					mSelectItemHHGGIndexs = mHhggFootballAdapter
							.getSelectItemIndexs();
				} else if (lotoId == FootballLotteryConstants.L307) {
					mLotteryBidMatchs = mSFBasketballAdapter
							.getlotterybidMatchsInfos();
					mSelectItemHHGGIndexs = mSFBasketballAdapter
							.getSelectItemIndexs();
				} else if (lotoId == FootballLotteryConstants.L310) {
					mLotteryBidMatchs = mHHGGBasketballAdapter
							.getlotterybidMatchsInfos();
					mSelectItemHHGGIndexs = mHHGGBasketballAdapter
							.getSelectItemIndexs();
				} else {
					mLotteryBidMatchs = mSPFFootballAdapter
							.getlotterybidMatchsInfos();
					mSelectItemHHGGIndexs = mSPFFootballAdapter
							.getSelectItemIndexs();
				}

				multiple = String.valueOf(mFootballLotteryHHGGUtil
						.getMultiple());
				mSelectItemIndexsHHGGKey = new ArrayList<Integer>(
						mSelectItemHHGGIndexs.keySet());
				Collections.sort(mSelectItemIndexsHHGGKey);
				matchCount = mSelectItemIndexsHHGGKey.size();
				// totalSum =
				// String.valueOf(mFootballLotteryHHGGUtil.getSumPrice());
				// itemTotal = mFootballLotteryHHGGUtil.getLotteryItemTotal();
				dans = mFootballLotteryHHGGUtil.getDans();
			}

			// 组合
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < matchCount; i++) {

				// 不等于混合过关 拼装
				if (lotoId != FootballLotteryConstants.L305
						&& lotoId != FootballLotteryConstants.L320
						&& lotoId != FootballLotteryConstants.L307
						&& lotoId != FootballLotteryConstants.L310
						&& lotoId != FootballLotteryConstants.L501
						&& lotoId != FootballLotteryConstants.L502) {
					int[] arr = FootballLotteryTools.getMatchPositioin(
							mSelectItemIndexsKey.get(i), mLotteryBidMatchs);
					Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);
					String bfSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZBF, mSearchType);
					String jqssp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZJQS, mSearchType);
					String bqcSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZBQC, mSearchType);

					String dxsp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JLDXF, mSearchType);
					String sfcSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JLSFC, mSearchType);
					
					
					String dx = "";

					builder.append(match.getIssueNo() + match.getWeek()
							+ match.getMatchSort() + "@");

					String sp = "";
					switch (lotoId) {
					// 比分
					case FootballLotteryConstants.L302:
						codePlay = PlayInfo.JC_ZQ_BF;
						builder.append(codePlay + "@");
						sp = bfSp;
						break;
					// 总进球数
					case FootballLotteryConstants.L303:
						codePlay = PlayInfo.JC_ZQ_ZJQ;
						builder.append(codePlay + "@");
						sp = jqssp;
						break;
					// 半全场
					case FootballLotteryConstants.L304:
						codePlay = PlayInfo.JC_ZQ_SFBQ;
						builder.append(codePlay + "@");
						sp = bqcSp;
						break;
					case FootballLotteryConstants.L308:
						codePlay = PlayInfo.JC_LQ_SFC;
						// builder.append(codePlay + "@");
						// sp = sfcSp;
						break;
					case FootballLotteryConstants.L309:
						codePlay = PlayInfo.JC_LQ_DXF;
						// builder.append(codePlay + "@");
						// sp = dxsp;
						break;
					default:
						break;
					}
					String[] bfArr = sp.split("\\@");
					// 让球胜平负、胜平负、比分、半全场、进球数
					List<Integer> tempIndex = mSelectItemIndexs
							.get(mSelectItemIndexsKey.get(i));
					if (lotoId == FootballLotteryConstants.L308
							|| lotoId == FootballLotteryConstants.L309) {
						if (lotoId == FootballLotteryConstants.L308) {
							builder.append(PlayInfo.JC_LQ_SFC + "@");
							String[] spfArr = sfcSp.split("\\@");
							for (int j = 0; j < tempIndex.size(); j++) {
								if (tempIndex.get(j) + 1 > 6) {
									builder.append("3" + (tempIndex.get(j) - 5));
								} else {
									builder.append("0" + (tempIndex.get(j) + 1));
								}
								builder.append("_");
								builder.append(spfArr[tempIndex.get(j)]
										.split("\\_")[1]);
								if (j != (tempIndex.size() - 1)) {
									builder.append(",");
								}
							}
						}

						if (lotoId == FootballLotteryConstants.L309) {
							builder.append(PlayInfo.JC_LQ_DXF + "@");
							String[] spfArr = dxsp.split("\\@");
							dx = spfArr[0].split("\\|")[0];
							for (int j = 0; j < tempIndex.size(); j++) {
								if (tempIndex.get(j) == 0) {
									builder.append("D");
								} else {
									builder.append("X");
								}
								builder.append("_");
								builder.append(spfArr[tempIndex.get(j)]
										.split("\\_")[1]);
								if (j != (tempIndex.size() - 1)) {
									builder.append(",");
								}
							}
						}
					} else {
						for (int j = 0; j < tempIndex.size(); j++) {

							switch (lotoId) {
							case FootballLotteryConstants.L302:
								switch (bfArr[tempIndex.get(j)].split("\\_")[0]) {
								case "胜其它":
									builder.append("90_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "平其它":
									builder.append("99_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "负其它":
									builder.append("09_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								default:
									builder.append(bfArr[tempIndex.get(j)].replace(":", ""));
									break;
								}
								break;
							case FootballLotteryConstants.L303:
								if (bfArr[tempIndex.get(j)].split("\\_")[0].equals("7+")) {
									builder.append("7_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
								} else {
									builder.append(bfArr[tempIndex.get(j)].replace(":", ""));
								}
								break;
							case FootballLotteryConstants.L304:
								switch (bfArr[tempIndex.get(j)].split("\\_")[0]) {
								case "胜胜":
									builder.append("33_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "胜平":
									builder.append("31_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "胜负":
									builder.append("30_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "平胜":
									builder.append("13_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "平平":
									builder.append("11_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "平负":
									builder.append("10_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "负胜":
									builder.append("03_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "负平":
									builder.append("01_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								case "负负":
									builder.append("00_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
									break;
								default:
									builder.append(bfArr[tempIndex.get(j)].replace(":", ""));
									break;
								}
								break;
	
							default:
								builder.append(bfArr[tempIndex.get(j)].replace(":", ""));
								
								break;
							}
							
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
					}

					String date = DateUtil.getTimeInMillisToStr(match
							.getMatchSellOutTime() + "");
					builder.append("@" + date);

					if (lotoId == FootballLotteryConstants.L308
							|| lotoId == FootballLotteryConstants.L309) {
						String rsfSp = FootballLotteryTools.getMatchSp(
								match.getMatchAgainstSpInfoList(),
								GlobalConstants.TC_JLXSF, mSearchType);
						String[] spfArr = rsfSp.split("\\@");
						String rsf = spfArr[0].split("\\|")[0];
						if (!StringUtil.isEmpty(rsf)) {
							builder.append("$").append(rsf);
						} else {
							builder.append("$").append("0");
						}
						builder.append("_");
						if (StringUtil.isEmpty(dx)) {
							builder.append("0");
						} else {
							builder.append(dx);
						}
					}

					builder.append(";");

					if (i == 0) {
						planEndTime = date;
					}
				} else if (lotoId == FootballLotteryConstants.L501) {
					codePlay = PlayInfo.JC_ZQ_2X1;
					int[] arr = FootballLotteryTools.getMatchPositioin(mSelectItemIndexsKey.get(i), mLotteryBidMatchs);
					Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);
					List<Integer> tempIndex = mSelectItemIndexs.get(mSelectItemIndexsKey.get(i));
					String jz2x1Sp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_2X1, mSearchType);
					
					builder.append(match.getIssueNo() + match.getWeek()
									+ match.getMatchSort() + "@");
					
					String[] jz2x1Arr = jz2x1Sp.split("\\@");
					for (int j = 0; j < tempIndex.size(); j++) {
						if (Integer.parseInt(match.getLetScore()) < 0) {
							if (Integer.parseInt(jz2x1Arr[tempIndex.get(j)].split("\\_")[0]) == 3) {
								builder.append(PlayInfo.JC_ZQ_SPF + "@");
							} else {
								builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
							}
							builder.append(jz2x1Arr[tempIndex.get(j)]);
						} else {
							if (Integer.parseInt(jz2x1Arr[tempIndex.get(j)].split("\\_")[0]) == 0) {
								builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
								builder.append("3_" + jz2x1Arr[1].split("\\_")[1]);
							} else {
								builder.append(PlayInfo.JC_ZQ_SPF + "@");
								builder.append("0_" + jz2x1Arr[0].split("\\_")[1]);
							}
							
						}
						
						if (j != (tempIndex.size() - 1)) {
							builder.append("|");
						} else {
							
						}
					}
//					builder.append("|");
					
					String date = DateUtil.getTimeInMillisToStr(match.getMatchSellOutTime() + "");
					builder.append("@" + date);

					String rsfSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(),GlobalConstants.TC_JLXSF, mSearchType);
					String[] spfArr = rsfSp.split("\\@");
					String rsf = spfArr[0].split("\\|")[0];
					
					
					// 让球
					if (!StringUtil.isEmpty(match.getLetScore())) {
						builder.append("$").append(match.getLetScore());
					} else {
						if (!StringUtil.isEmpty(rsf)) {
							builder.append("$").append(rsf);
						} else {
							builder.append("$").append("0");
						}
					}

					if (i != (matchCount - 1)) {
						builder.append(";");
					}
				} else if (lotoId == FootballLotteryConstants.L502) {
					
					codePlay = PlayInfo.JC_ZQ_1CZS;
					int[] arr = FootballLotteryTools.getMatchPositioin(mSelectItemIndexsKey.get(i), mLotteryBidMatchs);
					Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);
					List<Integer> tempIndex = mSelectItemIndexs.get(mSelectItemIndexsKey.get(i));
					String oneWinSp = FootballLotteryTools.getMatchSp(match.getMatchAgainstSpInfoList(), GlobalConstants.TC_1CZS, mSearchType);
					
					
					
					String[] jzoneWinArr = oneWinSp.split("\\@");
					if (tempIndex.size() > 0) {
						builder.append(match.getIssueNo() + match.getWeek() + match.getMatchSort() + "@");
						for (int j = 0; j < tempIndex.size(); j++) {
							
							builder.append(PlayInfo.JC_ZQ_SPF + "@");
							builder.append(jzoneWinArr[tempIndex.get(j)]);
							
							if (j != (tempIndex.size() - 1)) {
								builder.append("|");
							} else {
								
							}
						}
					} else {
						builder.append(oneWinMatch.getIssueNo() + oneWinMatch.getWeek() + oneWinMatch.getMatchSort() + "@");
						String sp = FootballLotteryTools.getMatchSp(oneWinMatch.getMatchAgainstSpInfoList(), GlobalConstants.TC_2X1, mSearchType);
//						String[] sysArr = sp.split("\\@");
						String[] jz2x1Arr = sp.split("\\@");
						for (int j = 0; j < jz2x1Arr.length; j++) {
							if (Integer.parseInt(oneWinMatch.getLetScore()) < 0) {
								if (Integer.parseInt(jz2x1Arr[j].split("\\_")[0]) == 3) {
									builder.append(PlayInfo.JC_ZQ_SPF + "@");
								} else {
									builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
								}
								builder.append(jz2x1Arr[j]);
							} else {
								if (Integer.parseInt(jz2x1Arr[j].split("\\_")[0]) == 0) {
									builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
									builder.append("3_" + jz2x1Arr[1].split("\\_")[1]);
								} else {
									builder.append(PlayInfo.JC_ZQ_SPF + "@");
									builder.append("0_" + jz2x1Arr[0].split("\\_")[1]);
								}
								
							}
							
							if (j != (jz2x1Arr.length - 1)) {
								builder.append("|");
							} else {
								
							}
						}
					}
					
//					builder.append("|");
					
					String date = DateUtil.getTimeInMillisToStr(match.getMatchSellOutTime() + "");
					builder.append("@" + date);
					String letScore="0";
					if (!TextUtils.isEmpty(oneWinMatch.getLetScore())) {
						letScore=oneWinMatch.getLetScore();
					}
					builder.append("$"+letScore);
					if (i != (matchCount - 1)) {
						builder.append(";");
					}
				} else {
					// 混合过关拼装
					int[] arr = FootballLotteryTools.getMatchPositioin(
							mSelectItemIndexsHHGGKey.get(i), mLotteryBidMatchs);
					Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);
					String spfSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZSPF, mSearchType);
					String rspfSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZXSPF, mSearchType);
					String bfSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZBF, mSearchType);
					String jqsSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZJQS, mSearchType);
					String bqcSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JZBQC, mSearchType);

					String sfSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JLSF, mSearchType);
					String rsfSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JLXSF, mSearchType);
					String dxsp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JLDXF, mSearchType);
					String sfcSp = FootballLotteryTools.getMatchSp(
							match.getMatchAgainstSpInfoList(),
							GlobalConstants.TC_JLSFC, mSearchType);
					

					String rsf = "";
					String dx = "";

					builder.append(match.getIssueNo() + match.getWeek()
							+ match.getMatchSort() + "@");

					if (lotoId == FootballLotteryConstants.L305
							|| lotoId == FootballLotteryConstants.L320) {
						codePlay = PlayInfo.JC_ZQ_HH;
					} else {
						codePlay = PlayInfo.JC_LQ_HH;
					}

					Map<Integer, List<Integer>> tempIndexs = mSelectItemHHGGIndexs
							.get(mSelectItemIndexsHHGGKey.get(i));
					List<Integer> tempIndex = null;
					
					/** 获取所有比分 **/
					// 让球胜平负
					if (tempIndexs.containsKey(FootballLotteryConstants.L301)) {
						builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
						String[] rspfArr = rspfSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L301);
						for (int j = 0; j < tempIndex.size(); j++) {
							builder.append(rspfArr[tempIndex.get(j)]);
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					// 比分
					if (tempIndexs.containsKey(FootballLotteryConstants.L302)) {
						builder.append(PlayInfo.JC_ZQ_BF + "@");
						String[] bfArr = bfSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L302);
						for (int j = 0; j < tempIndex.size(); j++) {
							switch (bfArr[tempIndex.get(j)].split("\\_")[0]) {
							case "胜其它":
								builder.append("90_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "平其它":
								builder.append("99_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "负其它":
								builder.append("09_" + bfArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							default:
								builder.append(bfArr[tempIndex.get(j)].replace(":", ""));
								break;
							}
//							builder.append(bfArr[tempIndex.get(j)].replace(":",
//									""));
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					// 总进球数
					if (tempIndexs.containsKey(FootballLotteryConstants.L303)) {
						builder.append(PlayInfo.JC_ZQ_ZJQ + "@");
						String[] jqsArr = jqsSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L303);
						for (int j = 0; j < tempIndex.size(); j++) {
//							builder.append(jqsArr[tempIndex.get(j)]);
							
							if (jqsArr[tempIndex.get(j)].split("\\_")[0].equals("7+")) {
								builder.append("7_" + jqsArr[tempIndex.get(j)].split("\\_")[1]);
							} else {
								builder.append(jqsArr[tempIndex.get(j)].replace(":", ""));
							}
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					// 半全场
					if (tempIndexs.containsKey(FootballLotteryConstants.L304)) {
						builder.append(PlayInfo.JC_ZQ_SFBQ + "@");
						String[] bqcArr = bqcSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L304);
						for (int j = 0; j < tempIndex.size(); j++) {
							switch (bqcArr[tempIndex.get(j)].split("\\_")[0]) {
							case "胜胜":
								builder.append("33_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "胜平":
								builder.append("31_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "胜负":
								builder.append("30_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "平胜":
								builder.append("13_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "平平":
								builder.append("11_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "平负":
								builder.append("10_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "负胜":
								builder.append("03_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "负平":
								builder.append("01_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							case "负负":
								builder.append("00_" + bqcArr[tempIndex.get(j)].split("\\_")[1]);
								break;
							default:
								builder.append(bqcArr[tempIndex.get(j)].replace(":", ""));
								break;
							}

							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					// 胜平负
					if (tempIndexs.containsKey(FootballLotteryConstants.L320)) {
						builder.append(PlayInfo.JC_ZQ_SPF + "@");
						String[] spfArr = spfSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L320);
						for (int j = 0; j < tempIndex.size(); j++) {
							builder.append(spfArr[tempIndex.get(j)]);
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					if (tempIndexs.containsKey(FootballLotteryConstants.L306)) {
						builder.append(PlayInfo.JC_LQ_XSF + "@");
						String[] spfArr = rsfSp.split("\\@");
						rsf = spfArr[0].split("\\|")[0];
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L306);
						for (int j = 0; j < tempIndex.size(); j++) {
							if (tempIndex.get(j) == 0) {
								builder.append(0);
							} else {
								builder.append(3);
							}
							builder.append("_");
							builder.append(spfArr[tempIndex.get(j)]
									.split("\\_")[1]);
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					if (tempIndexs.containsKey(FootballLotteryConstants.L307)) {
						builder.append(PlayInfo.JC_LQ_SF + "@");
						String[] spfArr = sfSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L307);
						for (int j = 0; j < tempIndex.size(); j++) {
							if (tempIndex.get(j) == 0) {
								builder.append(0);
							} else {
								builder.append(3);
							}
							builder.append("_");
							builder.append(spfArr[tempIndex.get(j)]
									.split("\\_")[1]);
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					if (tempIndexs.containsKey(FootballLotteryConstants.L308)) {
						builder.append(PlayInfo.JC_LQ_SFC + "@");
						String[] spfArr = sfcSp.split("\\@");
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L308);
						for (int j = 0; j < tempIndex.size(); j++) {
							if (tempIndex.get(j) + 1 > 6) {
								builder.append("3" + (tempIndex.get(j) - 5));
							} else {
								builder.append("0" + (tempIndex.get(j) + 1));
							}
							builder.append("_");
							builder.append(spfArr[tempIndex.get(j)]
									.split("\\_")[1]);
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}

					if (tempIndexs.containsKey(FootballLotteryConstants.L309)) {
						builder.append(PlayInfo.JC_LQ_DXF + "@");
						String[] spfArr = dxsp.split("\\@");
						dx = spfArr[0].split("\\|")[0];
						tempIndex = tempIndexs
								.get(FootballLotteryConstants.L309);
						for (int j = 0; j < tempIndex.size(); j++) {
							if (tempIndex.get(j) == 0) {
								builder.append("D");
							} else {
								builder.append("X");
							}
							builder.append("_");
							builder.append(spfArr[tempIndex.get(j)]
									.split("\\_")[1]);
							if (j != (tempIndex.size() - 1)) {
								builder.append(",");
							}
						}
						builder.append("|");
					}
					
					builder.deleteCharAt(builder.length() - 1);

					String date = DateUtil.getTimeInMillisToStr(match
							.getMatchSellOutTime() + "");
					builder.append("@" + date);

					// 让球
					if (!StringUtil.isEmpty(match.getLetScore())) {
						builder.append("$").append(match.getLetScore());
					} else {
						if (!StringUtil.isEmpty(rsf)) {
							builder.append("$").append(rsf);
						} else {
							builder.append("$").append("0");
						}
						builder.append("_");
						if (StringUtil.isEmpty(dx)) {
							builder.append("0");
						} else {
							builder.append(dx);
						}
					}

					if (i != (matchCount - 1)) {
						builder.append(";");
					}

				}

			}

			StringBuilder chuanBuilder = new StringBuilder();
			if (lotoId == FootballLotteryConstants.L502) {
				chuanBuilder.append("^");
				chuanBuilder.append("2*1");
			} else {
				// 获取串id（选号方式）
				mSelectChuan = mNchuanMAdapter.getSelectData();
				chuanBuilder.append("^");
				if (mSelectChuan != null) {
					for (int i = 0; i < mSelectChuan.size(); i++) {
						
						switch (mSearchType) {
						case SINGLE:
							mSelectChuan.remove(0);
							mSelectChuan.add(
									0,
									getResources().getString(
											R.string.football_item_single));
							chuanBuilder.append("1*1");
							break;
						case TWIN:
							chuanBuilder
									.append(FootballLotteryConstants
											.getChuanValue2Chuan().get(
													mSelectChuan.get(i)));
							break;
						default:
							break;
						}
						
						if (i != (mSelectChuan.size() - 1)) {
							chuanBuilder.append(",");
						}
	
					}
				}
			}
			
			builder.append(chuanBuilder.toString());

			if (dans.size() > 0) {
				builder.append("^");
				// List<Integer> danKeys = new
				// ArrayList<Integer>(dans.keySet());
				for (int i = 0; i < dans.size(); i++) {

					// 不等于混合过关 拼装
					if (lotoId != FootballLotteryConstants.L305
							&& lotoId != FootballLotteryConstants.L320
							&& lotoId != FootballLotteryConstants.L307
							&& lotoId != FootballLotteryConstants.L310
							&& lotoId != FootballLotteryConstants.L502) {
						int[] arr = FootballLotteryTools.getMatchPositioin(
								mSelectItemIndexsKey.get(dans.get(i)),
								mLotteryBidMatchs);
						Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);
						builder.append(match.getIssueNo() + match.getWeek()
								+ match.getMatchSort());
					} else if (lotoId == FootballLotteryConstants.L502) {
						builder.append(oneWinMatch.getIssueNo() + oneWinMatch.getWeek()
										+ oneWinMatch.getMatchSort());
					} else {
						int[] arr = FootballLotteryTools.getMatchPositioin(
								mSelectItemIndexsHHGGKey.get(dans.get(i)),
								mLotteryBidMatchs);
						Match match = mLotteryBidMatchs.get(arr[0]).get(arr[1]);
						builder.append(match.getIssueNo() + match.getWeek()
								+ match.getMatchSort());
					}

					if (i != (dans.size() - 1)) {
						builder.append(";");
					}
				}
			}

			if (dans.size() > 0) {
				switch (lotoId) {
				// 比分
				case FootballLotteryConstants.L302:
					codePlay = PlayInfo.JC_ZQ_BF_DT;
					break;
				// 总进球数
				case FootballLotteryConstants.L303:
					codePlay = PlayInfo.JC_ZQ_ZJQ_DT;
					break;
				// 半全场
				case FootballLotteryConstants.L304:
					codePlay = PlayInfo.JC_ZQ_SFBQ_DT;
					break;
				case FootballLotteryConstants.L306:
					codePlay = PlayInfo.JC_LQ_HH_DT;
					break;
				case FootballLotteryConstants.L307:
					codePlay = PlayInfo.JC_LQ_HH_DT;
					break;
				case FootballLotteryConstants.L308:
					codePlay = PlayInfo.JC_LQ_SFC_DT;
					break;
				case FootballLotteryConstants.L309:
					codePlay = PlayInfo.JC_LQ_DXF_DT;
					break;
				case FootballLotteryConstants.L310:
					codePlay = PlayInfo.JC_LQ_HH_DT;
					break;
				case FootballLotteryConstants.L502:
					codePlay = PlayInfo.JC_ZQ_1CZS_DT;
					break;
				case FootballLotteryConstants.L501:
					codePlay = PlayInfo.JC_ZQ_2X1_DT;
					break;
				default:
					codePlay = PlayInfo.JC_ZQ_HH_DT;
					break;
				}
			}
			Map<String, String> bettingReq = new LinkedHashMap<String, String>();

			bettingReq.put("codePlay", codePlay);
			String codeContent = builder.toString();
			bettingReq.put("codeContent", codeContent);
			Log.e("qiiufeng", codeContent);
			bettingReq.put("codeMultiple", "1");
			bettingReq.put("codeMoney", needpay/Long.parseLong(multiple) + "");
			bettingReq.put("codeNumbers", itemTotal + "");

			lists.put(0, bettingReq);

			userInfo = UserLogic.getInstance().getDefaultUserInfo();
			if (userInfo == null) {
				startActivity(LoginActivity.class);
			} else {
				TzObj obj = new TzObj();
				obj.bettingType = bettingType;
				obj.gameNo = gameNo;
				obj.issue = issue;
				obj.multiple = multiple;
				obj.totalSum = needpay + ""; // 订单金额
				obj.chase = chase;
				obj.isChase = isChase;
				obj.choiceType = choiceType;
				obj.planEndTime = planEndTime;
				obj.stopCondition = stopCondition;
				if (lotoId == FootballLotteryConstants.L306
						|| lotoId == FootballLotteryConstants.L307
						|| lotoId == FootballLotteryConstants.L308
						|| lotoId == FootballLotteryConstants.L309
						|| lotoId == FootballLotteryConstants.L310) {
					obj.gameType = GlobalConstants.TC_JCLQ;
				} else {
					obj.gameType = GlobalConstants.TC_JCZQ;
				}

				obj.bettingReq = lists;
				// Intent intent = new Intent(LotteryBidCartActivity.this,
				// ConfirmPayActivity.class);
				// intent.putExtra(ConfirmPayActivity.DATA, obj);

				Intent intent = new Intent(LotteryBidCartActivity.this,
						CommitPayActivity.class);
				intent.putExtra(CommitPayActivity.DATA, obj);
				intent.putExtra("lotoId", lotoId);
				intent.putExtra("selectChuan", mSelectChuan.toString());
				intent.putExtra("multiple", multiple);
				startActivity(intent);
				// mController.syncLotteryBetting(GlobalConstants.NUM_BETTING,
				// userInfo.getUserId(), bettingType, gameNo, issue, multiple,
				// totalSum, isChase, chase, isRedPackage, redPackageId,
				// redPackageMoney, choiceType, planEndTime, stopCondition,
				// list, mCallBack);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 开始计算
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年1月21日
	 */
	private void startCalculate() {
		mNchuanMLayoutView.setVisibility(View.GONE);
		if (isCalculating && mSearchType.equals("TWIN")) {
			return;
		}
		String multiple = editView_lotterybidmultipley.getText().toString()
				.trim();
		int mul = 1;
		if (StringUtil.isEmpty(multiple) || multiple.equals("0")) {
			// mMultipleEditView.setText("1");
			mul = 1;
		} else {
			mul = Integer.parseInt(multiple);
		}

		isCalculating = true;

		// 不等于混合过关
		if (lotoId != FootballLotteryConstants.L305
				&& lotoId != FootballLotteryConstants.L320
				&& lotoId != FootballLotteryConstants.L307
				&& lotoId != FootballLotteryConstants.L310) {
			// 计算注数、及奖金

			// 设置胆
			if (mFootballLotteryUtil.getDans() == null) {
				mFootballLotteryUtil.addDans(new ArrayList<Integer>());
			} else {
				if (mOnlyLotteryBidAdapter.getDans() != null) {
					mFootballLotteryUtil.addDans(mOnlyLotteryBidAdapter.getDans());
				}
			}

			// 选择的投注项
			mFootballLotteryUtil.setSelectNumber(mOnlyLotteryBidAdapter
					.getSelectItemIndexs());
			// 设置最小的胆
			mFootballLotteryUtil.setMinDan(0);
			// 设置最大的胆
			mFootballLotteryUtil.setMaxDan(0);

			mFootballLotteryUtil.setFootballMatchs(mOnlyLotteryBidAdapter
					.getlotterybidMatchsInfos());
			mFootballLotteryUtil.setLotId(lotoId);
			mFootballLotteryUtil.setSearchType(mSearchType);
			// 设置倍数
			mFootballLotteryUtil.setMultiple(mul);
			// 计算
			// 获取串
			mSelectChuan = mNchuanMAdapter.getSelectData();
			mFootballLotteryUtil.addChuans(mSelectChuan);

			Object[] result = null;
			switch (mSearchType) {
			case SINGLE:
				mChuanTypeView.setText(getResources().getString(
						R.string.football_item_single));
				mChuanTypeView.setEnabled(false);
				result = mFootballLotteryUtil.startSingleCalculate();
				itemTotal = (Integer) result[0];
				needpay = (Integer) result[1];
				String winPrice = (String) result[2];
				String beishu = getResources().getString(R.string.num_money,
						""+itemTotal, mul + "", ""+needpay);
				txtView_total2payprice.setText(beishu);
				txtView_win_price.setText(winPrice
						+ getResources().getString(R.string.lemi_unit));
				break;
			case TWIN:
				if (lotoId == FootballLotteryConstants.L502) {
					List<Integer> list = new ArrayList<Integer>();
					Map<Integer, List<Integer>> indexs = mOnlyLotteryBidAdapter.getSelectItemIndexs();
					List<Integer> temp = new ArrayList<Integer>(indexs.keySet());
					Collections.sort(temp);
					list.add(temp.size() - 1);
					mFootballLotteryUtil.addDans(list);
					mChuanTypeView.setText("修改投注方案");
					mChuanTypeView.setEnabled(true);
					mChuanTypeImageView.setVisibility(View.GONE);
					List<String> resultChuan=new ArrayList<String>();
					resultChuan.add("2串1");
					calculate502ToSubThread(
					  						mOnlyLotteryBidAdapter.getlotterybidMatchsInfos(),
					  						mOnlyLotteryBidAdapter.getSelectItemIndexs(), oneWinMatch,
					  						resultChuan, mul);
				} else {
					mChuanTypeView.setText(mSelectChuan.get(0));
					mChuanTypeView.setEnabled(true);
					calculateOtherToSubThread(
					  						mOnlyLotteryBidAdapter.getlotterybidMatchsInfos(),
					  						mOnlyLotteryBidAdapter.getSelectItemIndexs(),
					  						mNchuanMAdapter.getSelectData(), mul);
				}
				
				// result = mFootballLotteryUtil.startCalculate();
				
				break;
			default:
				break;
			}

			mOnlyLotteryBidAdapter.notifyDataSetChanged();
			// Object[] result = mFootballLotteryUtil.startCalculate();
			// int itemTotal = (Integer) result[0];
			// int numPrice = (Integer) result[1];
			// String winPrice = (String) result[2];
			// mTotal2PriceView.setText(getResources().getString(R.string.num_money,
			// itemTotal,
			// Integer.parseInt(mMultipleEditView.getText().toString()),
			// numPrice));
			// mWinPriceView.setText(winPrice +
			// getResources().getString(R.string.lemi_unit));
//		} else if (lotoId == FootballLotteryConstants.L502) {
//			
		} else {

			// 计算注数、及奖金
			// mFootballLotteryHHGGUtil = new FootballLotteryHHGGUtil();

			// 设置胆
			if (mFootballLotteryHHGGUtil.getDans() == null) {
				mFootballLotteryHHGGUtil.addDans(new ArrayList<Integer>());
			} else {
				if (lotoId == FootballLotteryConstants.L305) {
					mFootballLotteryHHGGUtil.addDans(mHhggFootballAdapter
							.getDans());
				} else if (lotoId == FootballLotteryConstants.L307) {
					mFootballLotteryHHGGUtil.addDans(mSFBasketballAdapter
							.getDans());
				} else if (lotoId == FootballLotteryConstants.L310) {
					mFootballLotteryHHGGUtil.addDans(mHHGGBasketballAdapter
							.getDans());
				} else {
					mFootballLotteryHHGGUtil.addDans(mSPFFootballAdapter
							.getDans());
				}
			}

			if (lotoId == FootballLotteryConstants.L305) {
				// 选择的投注项
				mFootballLotteryHHGGUtil.setSelectNumber(mHhggFootballAdapter
						.getSelectItemIndexs());
				mFootballLotteryHHGGUtil.setFootballMatchs(mHhggFootballAdapter
						.getlotterybidMatchsInfos());
				mHhggFootballAdapter.notifyDataSetChanged();
			} else if (lotoId == FootballLotteryConstants.L307) {
				// 选择的投注项
				mFootballLotteryHHGGUtil.setSelectNumber(mSFBasketballAdapter
						.getSelectItemIndexs());
				mFootballLotteryHHGGUtil.setFootballMatchs(mSFBasketballAdapter
						.getlotterybidMatchsInfos());
				mSFBasketballAdapter.notifyDataSetChanged();
			} else if (lotoId == FootballLotteryConstants.L310) {
				// 选择的投注项
				mFootballLotteryHHGGUtil.setSelectNumber(mHHGGBasketballAdapter
						.getSelectItemIndexs());
				mFootballLotteryHHGGUtil
						.setFootballMatchs(mHHGGBasketballAdapter
								.getlotterybidMatchsInfos());
				mHHGGBasketballAdapter.notifyDataSetChanged();
			} else {
				// 选择的投注项
				mFootballLotteryHHGGUtil.setSelectNumber(mSPFFootballAdapter
						.getSelectItemIndexs());
				mFootballLotteryHHGGUtil.setFootballMatchs(mSPFFootballAdapter
						.getlotterybidMatchsInfos());
				mSPFFootballAdapter.notifyDataSetChanged();
			}

			mFootballLotteryHHGGUtil.setSearchType(mSearchType);
			// 设置倍数
			mFootballLotteryHHGGUtil.setMultiple(mul);
			mSelectChuan = mNchuanMAdapter.getSelectData();
			mFootballLotteryHHGGUtil.addChuans(mSelectChuan);

			Object[] result = null;
			// 获取串
			switch (mSearchType) {
			case SINGLE:
				mChuanTypeView.setText(getResources().getString(
						R.string.football_item_single));
				mChuanTypeView.setEnabled(false);
				result = mFootballLotteryHHGGUtil.startSingleCalculate();
				itemTotal = (Integer) result[0];
				needpay = (Integer) result[1];
				double winPrice = (Double) result[2];
				txtView_total2payprice.setText(getResources().getString(
						R.string.num_money, itemTotal+"", mul + "", needpay+""));
				txtView_win_price.setText(winPrice+ getResources().getString(R.string.lemi_unit));
				break;
			case TWIN:
				mChuanTypeView.setText(mSelectChuan.get(0));
				mChuanTypeView.setEnabled(true);
				// result = mFootballLotteryHHGGUtil.startCalculate();
				if (lotoId == FootballLotteryConstants.L305) {
					calculateHhggToSubThread(
							mHhggFootballAdapter.getlotterybidMatchsInfos(),
							mHhggFootballAdapter.getSelectItemIndexs(),
							mNchuanMAdapter.getSelectData(), mul);
				} else if (lotoId == FootballLotteryConstants.L307) {
					String num = editView_lotterybidmultipley.getText()
							.toString();
					calculateHhggToSubThread(
							mSFBasketballAdapter.getlotterybidMatchsInfos(),
							mSFBasketballAdapter.getSelectItemIndexs(),
							mNchuanMAdapter.getSelectData(),
							Integer.parseInt(TextUtils.isEmpty(num) ? "0" : num));
				} else if (lotoId == FootballLotteryConstants.L310) {
					String num = editView_lotterybidmultipley.getText()
							.toString();
					calculateHhggToSubThread(
							mHHGGBasketballAdapter.getlotterybidMatchsInfos(),
							mHHGGBasketballAdapter.getSelectItemIndexs(),
							mNchuanMAdapter.getSelectData(),
							Integer.parseInt(TextUtils.isEmpty(num) ? "0" : num));
				} else {
					calculateHhggToSubThread(
							mSPFFootballAdapter.getlotterybidMatchsInfos(),
							mSPFFootballAdapter.getSelectItemIndexs(),
							mNchuanMAdapter.getSelectData(), mul);
				}

			default:

				break;
			}

			// int itemTotal = (Integer) result[0];
			// int numPrice = (Integer) result[1];
			// double winPrice = (Double) result[2];
			// mTotal2PriceView.setText(getResources().getString(R.string.num_money,
			// itemTotal,
			// Integer.parseInt(mMultipleEditView.getText().toString()),
			// numPrice));
			// mWinPriceView.setText(winPrice +
			// getResources().getString(R.string.lemi_unit));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	private void calculateHhggToSubThread(
			final List<List<Match>> mLotteryBidMatchs,
			final Map<Integer, Map<Integer, List<Integer>>> mSelectItemindexs,
			final List<String> selectChuan, final int multiple) {
		// showLoadingDialog();
		new Thread() {
			public void run() {
				// 计算
				Object[] result = mMatchBetService.startCalculate(
						mLotteryBidMatchs, mSelectItemindexs, selectChuan,
						multiple, mSearchType,
						mFootballLotteryHHGGUtil.getDans());
				itemTotal = (Long) result[0];
				needpay = (Long) result[1];
				final String winPrice = result[2].toString();
				runOnUiThread(new Runnable() {
					public void run() {
						// hideLoadingDialog();
						isCalculating = false;
						txtView_total2payprice
								.setText(getResources()
										.getString(
												R.string.num_money,
												itemTotal+"",
												TextUtils
														.isEmpty(editView_lotterybidmultipley
																.getText())
														|| editView_lotterybidmultipley
																.getText()
																.toString()
																.trim()
																.equals("0") ? "1"
														: editView_lotterybidmultipley
																.getText(),
												needpay+""));
						txtView_win_price.setText(winPrice
								+ getResources().getString(R.string.lemi_unit));
					}
				});
			};
		}.start();
	}
	
	private void calculate502ToSubThread(
			final List<List<Match>> mLotteryBidMatchs,
			final Map<Integer, List<Integer>> mSelectItemindexs, final Match oneWinMatch,
			final List<String> selectChuan, final int multiple) {
		// showLoadingDialog();
		new Thread() {
			public void run() {
				// 计算
				Object[] result = mMatchBetService.startCalculate(
						mLotteryBidMatchs, mSelectItemindexs, selectChuan,
						multiple, mSearchType, lotoId,
						mFootballLotteryUtil.getDans(), oneWinMatch);
				itemTotal = (Long) result[0];
				needpay = (Long) result[1];
				final String winPrice = (String) result[2];
				runOnUiThread(new Runnable() {
					public void run() {
						// hideLoadingDialog();
						isCalculating = false;
						txtView_total2payprice
								.setText(getResources()
										.getString(
												R.string.num_money,
												""+itemTotal,
												TextUtils
														.isEmpty(editView_lotterybidmultipley
																.getText())
														|| editView_lotterybidmultipley
																.getText()
																.toString()
																.trim()
																.equals("0") ? "1"
														: editView_lotterybidmultipley
																.getText(),
												needpay+""));
						txtView_win_price.setText(winPrice
								+ getResources().getString(R.string.lemi_unit));
					}
				});
			};
		}.start();
	}

	private void calculateOtherToSubThread(
			final List<List<Match>> mLotteryBidMatchs,
			final Map<Integer, List<Integer>> mSelectItemindexs,
			final List<String> selectChuan, final int multiple) {
		// showLoadingDialog();
		new Thread() {
			public void run() {
				// 计算
				Object[] result = mMatchBetService.startCalculate(
						mLotteryBidMatchs, mSelectItemindexs, selectChuan,
						multiple, mSearchType, lotoId,
						mFootballLotteryUtil.getDans());
				itemTotal = (Long) result[0];
				needpay = (Long) result[1];
				final String winPrice = (String) result[2];
				runOnUiThread(new Runnable() {
					public void run() {
						// hideLoadingDialog();
						isCalculating = false;
						txtView_total2payprice
								.setText(getResources()
										.getString(
												R.string.num_money,
												itemTotal+"",
												TextUtils
														.isEmpty(editView_lotterybidmultipley
																.getText())
														|| editView_lotterybidmultipley
																.getText()
																.toString()
																.trim()
																.equals("0") ? "1"
														: editView_lotterybidmultipley
																.getText(),
												needpay+""));
						txtView_win_price.setText(winPrice
								+ getResources().getString(R.string.lemi_unit));
					}
				});
			};
		}.start();
	}

	/**
	 * 购物车数据发生变化回调
	 */
	@Override
	public void onIndexClickCartChagne() {
		// 重置串
		List<Integer> dans = new ArrayList<Integer>();
		String chuan = mNchuanMAdapter.getSelectData()
				.get(mNchuanMAdapter.getSelectData().size() - 1)
				.split(getResources().getString(R.string.football_chuan))[0];
		boolean isChange = true;
		if (chuan.equals(getResources()
				.getString(R.string.football_item_single))) {
			chuan = "1";
		}
		// 不等于混合过关
		if (lotoId != FootballLotteryConstants.L305
				&& lotoId != FootballLotteryConstants.L320
				&& lotoId != FootballLotteryConstants.L307
				&& lotoId != FootballLotteryConstants.L310) {
			mSelectChuanIndex = mOnlyLotteryBidAdapter.getCount();
			dans = mOnlyLotteryBidAdapter.getDans();
			if (Integer.parseInt(chuan) <= mOnlyLotteryBidAdapter
					.getSelectItemIndexs().keySet().size()) {
				isChange = false;
				if (dans.size() == 0) {
					int maxChuan = 0;
					// 比分
					if (lotoId == FootballLotteryConstants.L302) {
						maxChuan = 4;
						// 让球胜平负
					} else if (lotoId == FootballLotteryConstants.L301) {
						maxChuan = 8;
						// 胜平负
					} else if (lotoId == FootballLotteryConstants.L320) {
						maxChuan = 8;
						// 进球数
					} else if (lotoId == FootballLotteryConstants.L303) {
						maxChuan = 6;
						// 半全场
					} else if (lotoId == FootballLotteryConstants.L304) {
						maxChuan = 4;
						// 混合过关
					} else if (lotoId == FootballLotteryConstants.L305) {
						// 篮球让球胜负
					} else if (lotoId == FootballLotteryConstants.L306) {
						maxChuan = 8;
						// 篮球胜负
					} else if (lotoId == FootballLotteryConstants.L307) {
						maxChuan = 8;
						// 胜分差
					} else if (lotoId == FootballLotteryConstants.L308) {
						maxChuan = 4;
						// 大小分
					} else if (lotoId == FootballLotteryConstants.L309) {
						maxChuan = 8;

					} else {
						maxChuan = 8;
					}

					mNchuanMAdapter.setBidN(maxChuan,
							mOnlyLotteryBidAdapter.getSelectItemIndexs());
				} else {
					mNchuanMAdapter.setBidN(Integer.parseInt(chuan),
							mOnlyLotteryBidAdapter.getSelectItemIndexs());
				}
			} else {
				isChange = true;
				mNchuanMAdapter.setBidN(mSelectChuanIndex,
						mOnlyLotteryBidAdapter.getSelectItemIndexs());
			}
			mNchuanMAdapter.setSelectData(mFootballLotteryUtil
					.getSelectChuanState());
		} else {
			if (lotoId == FootballLotteryConstants.L320) {
				mSelectChuanIndex = mSPFFootballAdapter.getCount();
				dans = mSPFFootballAdapter.getDans();
				if (Integer.parseInt(chuan) <= mSPFFootballAdapter
						.getSelectItemIndexs().keySet().size()) {
					isChange = false;
					mNchuanMAdapter.setN(Integer.parseInt(chuan),
							mSPFFootballAdapter.getSelectItemIndexs());
				} else {
					isChange = true;
					mNchuanMAdapter.setN(mSelectChuanIndex,
							mSPFFootballAdapter.getSelectItemIndexs());
				}
			} else if (lotoId == FootballLotteryConstants.L305) {
				mSelectChuanIndex = mHhggFootballAdapter.getCount();
				dans = mHhggFootballAdapter.getDans();
				if (Integer.parseInt(chuan) <= mHhggFootballAdapter
						.getSelectItemIndexs().keySet().size()) {
					isChange = false;
					mNchuanMAdapter.setN(Integer.parseInt(chuan),
							mHhggFootballAdapter.getSelectItemIndexs());
					
					//进入后，选择其他串法，设多个胆时，会造成直接恢复成初始进入页面状态
//					if (mNchuanMAdapter.getCount() < Integer.parseInt(chuan)) {
//						isChange = true;
//					}
				} else {
					isChange = true;
					mNchuanMAdapter.setN(mSelectChuanIndex,
							mHhggFootballAdapter.getSelectItemIndexs());
				}
			} else if (lotoId == FootballLotteryConstants.L307) {
				mSelectChuanIndex = mSFBasketballAdapter.getCount();
				dans = mSFBasketballAdapter.getDans();
				if (Integer.parseInt(chuan) <= mSFBasketballAdapter
						.getSelectItemIndexs().keySet().size()) {
					isChange = false;
					mNchuanMAdapter.setN(Integer.parseInt(chuan),
							mSFBasketballAdapter.getSelectItemIndexs());
				} else {
					isChange = true;
					mNchuanMAdapter.setN(mSelectChuanIndex,
							mSFBasketballAdapter.getSelectItemIndexs());
				}
			} else if (lotoId == FootballLotteryConstants.L310) {
				mSelectChuanIndex = mHHGGBasketballAdapter.getCount();
				dans = mHHGGBasketballAdapter.getDans();
				if (Integer.parseInt(chuan) <= mHHGGBasketballAdapter
						.getSelectItemIndexs().keySet().size()) {
					isChange = false;
					mNchuanMAdapter.setN(Integer.parseInt(chuan),
							mHHGGBasketballAdapter.getSelectItemIndexs());
				} else {
					isChange = true;
					mNchuanMAdapter.setN(mSelectChuanIndex,
							mHHGGBasketballAdapter.getSelectItemIndexs());
				}
			}

		}
		if (chuan.equals("1")) {
			mChuanTypeView.setEnabled(false);
		} else {
			if (mSelectChuanIndex < 2) {
				mChuanTypeView.setEnabled(false);
			} else {
				mChuanTypeView.setEnabled(true);
			}
		}
		if (isChange) {
			mNchuanMAdapter.setSelectData(null);

			mNchuanMAdapter.setSelectIndex(mNchuanMAdapter.getCount() == 0 ? 0
					: mNchuanMAdapter.getCount() - 1);
		}

		startCalculate();
	}

	private CallBack mCallBack = new CallBack() {

		/**
		 * 投注成功
		 */
		public void syncLotteryBettingSuccess(final Betting obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(LotteryBidCartActivity.this, "投注成功");

					userInfo.setIntegralAcnt(obj.getIntegralAcnt());
					userInfo.setPrizeAcnt(obj.getPrizeAcnt());
					userInfo.setBetAcnt(obj.getBetAcnt());
					UserLogic.getInstance().saveUserInfo(userInfo);

					Intent tIntent = new Intent();
					setResult(GlobalConstants.CLEARLATTERYSELECT, tIntent);
					// 投注成功，关闭本页面
					ActivityManager.pop(LotteryBidCartActivity.this);
					Intent intent = new Intent(LotteryBidCartActivity.this,
							LotteryBettingSuccessActivity.class);
					intent.putExtra("issue", "");
					intent.putExtra("lototype", GlobalConstants.TC_JCZQ);
					startActivity(intent);
				}
			});
		}

		/**
		 * 投注失败
		 */
		public void syncLotteryBettingFailure(final int code, final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					// 是否弹出提示alert
					Boolean isShowAlert = false;
					String message = "";
					// 用户未登录
					// if (code == GlobalConstants.LOTTERY_BETTING_ERROR13) {
					// message = getString(R.string.no_login_prompt);
					// isShowAlert = true;
					// // 余额不足投注失败，请充值
					// } else if (code ==
					// GlobalConstants.LOTTERY_BETTING_ERROR14) {
					// message = getString(R.string.no_balance_prompt);
					// isShowAlert = true;
					// }
					if (isShowAlert) {
//						final PromptDialog0 dialog = new PromptDialog0(
//								LotteryBidCartActivity.this);
						final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(LotteryBidCartActivity.this);
						dialog.setMessage(message);
						dialog.setNegativeButton(getString(R.string.btn_canl),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.hideDialog();
										LotteryBidCartActivity.this.finish();

									}
								});
						dialog.setPositiveButton(getString(R.string.btn_ok),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.hideDialog();
										// 未登录、进入登陆页面
										// if (code ==
										// GlobalConstants.LOTTERY_BETTING_ERROR13)
										// {
										// startActivity(new
										// Intent(LotteryBidCartActivity.this,
										// LoginActivity.class));
										// }
										// 未充值进入充值页面

									}
								});
						dialog.showDialog();
					} else {
						ToastUtil
								.shortToast(LotteryBidCartActivity.this, error);
					}
				}
			});
		}

	};

	private class MultTextWatcher implements TextWatcher {

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
				if (parseInt < 1) {
					// showToast(getResources().getString(R.string.cart_least_mult,
					// 1));
					// mMultipleEditView.setText("1");
					// LotteryShowUtil.setSelection(mMultipleEditView);
				}
				if (parseInt > mMultiple) {
					editView_lotterybidmultipley.setText(mMultiple + "");
					// LotteryShowUtil.setSelection(mMultipleEditView);
					// showToast(getResources().getString(R.string.cart_max_mult,
					// mMultiple));
				}
				//TODO
//				if (lotoId  == FootballLotteryConstants.L502) {
//					if (parseInt < 5) {
//						editView_lotterybidmultipley.setText("5");
//					}
//					if (string.length() > 1 && string.substring(0, 1).equals("0")) {
//						if (Integer.parseInt(string.substring(1)) < 5) {
//							editView_lotterybidmultipley.setText("5");
//						} else {
//							editView_lotterybidmultipley.setText(string.substring(1));
//						}
//					}
//				} else {
//					if (string.length() > 1 && string.substring(0, 1).equals("0")) {
//						editView_lotterybidmultipley.setText(string.substring(1));
//					}
//				}
			}
			startCalculate();
			// else {
			// showToast(getResources().getString(R.string.cart_least_mult, 1));
			// mMultipleEditView.setText("1");
			// LotteryShowUtil.setSelection(mMultipleEditView);
			// }

		}
	}
	
	
	private void showExitDialog() {
		final PromptDialog_Black_Fillet promptDialog0 = new PromptDialog_Black_Fillet(mActivity);
		promptDialog0.setMessage(getResources().getString(R.string.lottery_bid_content));
		promptDialog0.setPositiveButton(getResources().getString(R.string.btn_lottery_noback),
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					promptDialog0.hideDialog();
				}
			});
		promptDialog0.setNegativeButton(getResources().getString(R.string.btn_lottery_back),
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					promptDialog0.hideDialog();
					GlobalTools.hideSoftInput(LotteryBidCartActivity.this);
					Intent tIntent = new Intent();
					// 不等于混合过关
					if (lotoId != FootballLotteryConstants.L305
							&& lotoId != FootballLotteryConstants.L320
							&& lotoId != FootballLotteryConstants.L307
							&& lotoId != FootballLotteryConstants.L310
							&& lotoId != FootballLotteryConstants.L502) {
						tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,
								(Serializable) mOnlyLotteryBidAdapter
										.getSelectItemIndexs());
					} else if (lotoId == FootballLotteryConstants.L502) {
						Map<Integer, List<Integer>> indexs = mOnlyLotteryBidAdapter.getSelectItemIndexs();
						List<Integer> keys = new ArrayList<Integer>(indexs.keySet());
						Collections.sort(keys);
						indexs.remove(keys.get(keys.size() - 1));
						tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS, (Serializable) indexs);
					} else {
						// 混合过关
						if (lotoId == FootballLotteryConstants.L305) {
							tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,
									(Serializable) mHhggFootballAdapter
											.getSelectItemIndexs());
						} else if (lotoId == FootballLotteryConstants.L307) {
							tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,
									(Serializable) mSFBasketballAdapter
											.getSelectItemIndexs());
						} else if (lotoId == FootballLotteryConstants.L310) {
							tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,
									(Serializable) mHHGGBasketballAdapter
											.getSelectItemIndexs());
						} else {
							tIntent.putExtra(LOTTERYBIDMATCH_SELECT_INDEXS,
									(Serializable) mSPFFootballAdapter
											.getSelectItemIndexs());
						}

					}
					setResult(lotoId, tIntent);
					finish();
				}
			});
		promptDialog0.showDialog();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			showExitDialog();
		return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
