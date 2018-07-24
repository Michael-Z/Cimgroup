package cn.com.cimgroup.frament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ScoreListActivity;
import cn.com.cimgroup.activity.ScoreListActivity.MScoreList;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.frament.ScoreListFragment.onChangeMatchTimesListener;
import cn.com.cimgroup.popwindow.PopWindowScoreMatchChoose.onMatchChooseItemClick;
import cn.com.cimgroup.view.ScoreListIndicator;
import cn.com.cimgroup.view.ScoreListIndicator.PageChangeListener;

public class ScoreListFragment_Match extends BaseFrament implements onChangeMatchTimesListener,
		onMatchChooseItemClick, PageChangeListener{

	/** 布局填充器**/
	private LayoutInflater mInflater;
	/** 自定义控件-tabs**/
	private ScoreListIndicator mIndicator;
	private ViewPager mViewPager;

	/** tabs的内容，由Activity传入**/
	private static List<String> mTitles;
	/** 内容fragment  比赛列表/关注**/
	private List<ScoreListFragment> mFragments = new ArrayList<ScoreListFragment>();
	/** viewpager的adapter，使用默认的**/
	private FragmentPagerAdapter mAdapter;
	
	private View mView;
	/** 标志字段**/
	private static String TITLE = "title";
	private static String MSCORELIST = "mscorelist";
	
	private MScoreList type;
	
	private ScoreListFragment mShowFragment;
	
	private int currentPosition = 0;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mInflater = getLayoutInflater(savedInstanceState);
		
		mView = mInflater.inflate(R.layout.fragment_viewpager_match, null);
		mTitles = new ArrayList<String>();
		
		Bundle bundle = new Bundle();
		bundle = getArguments();
		mTitles = bundle.getStringArrayList(TITLE);
		Collections.reverse(mTitles);
		type = (MScoreList) bundle.getSerializable(MSCORELIST);
		
		initView();
		initData();
		//set current position
		
		mIndicator.setVisibleTabCount(1);
		mIndicator.setTabItemTitles(mTitles);
		
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
		initEvent();
		
		//用户第一次进入  默认停留在当前期
		setCurrentPage(getCurrentPage(ScoreListActivity.issueNo));
		
		return mView;
	
	}
	
	private void initEvent() {
		mIndicator.setOnPageChangeListener(this);
	}

	private void initData() {
		
		for (int i = 0; i < mTitles.size(); i++) {
			
			ScoreListFragment fragment = ScoreListFragment.newInstance(mTitles.get(i),type);
			fragment.setOnChangeMatchTimeListener(this);
			mFragments.add(fragment);
		}

		mAdapter = new ScorePagerAdapter(getChildFragmentManager());

	}

	private void initView() {
		mViewPager = (ViewPager) mView.findViewById(R.id.my_viewpager);
		mIndicator = (ScoreListIndicator) mView.findViewById(R.id.my_indicator);
	}
	
	/**
	 * 初始化Fragment
	 * @param titles
	 */
	public static ScoreListFragment_Match newInstance(ArrayList<String> titles, MScoreList type){
		
		ScoreListFragment_Match fragment = new ScoreListFragment_Match();
		 
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(TITLE, titles);
		bundle.putSerializable(MSCORELIST, type);
		
		fragment.setArguments(bundle);
		
		return fragment;
		
	}

	@Override
	public void onChangeMatchTimes(List<String> matchNameList) {
		mListener.onChange(matchNameList);
	}
	
	//为了调用接口回调而创建的接口回调
	//因为activity不持有有该接口的fragment，所以需要有这样一个接口回调
	//这个接口回调是用来填充   筛选的popwindow中内容的
	public interface MyListener{
		void onChange(List<String> matchNameList);
	}
	
	private MyListener mListener;
	
	public void setMyListener(MyListener listener){
		this.mListener = listener;
	}
	
	@Override
	public void onSelectItemClick(List<String> selectData,boolean flag) {
		mShowFragment.chooseScoreMatch(selectData);
	}
	
	public class ScorePagerAdapter extends FragmentPagerAdapter {
		private static final String MSCORELIST = "mscorelist";

//		private Map<MScoreList, Fragment> mFraments = new HashMap<MScoreList, Fragment>();

		FragmentManager mFragmentManager;

		public ScorePagerAdapter(FragmentManager fm) {
			super(fm);
			this.mFragmentManager = fm;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			mShowFragment = (ScoreListFragment) object;
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			MScoreList type = MScoreList.values()[position];
			String title = "";
			switch (type) {
			// 未结束
			case NOOVER:
				title = getResources().getString(R.string.scorelist_noover);
				break;
			// 已结束
			case OVER:
				title = getResources().getString(R.string.scorelist_over);
				break;
			// 关注
			case LIKE:
				title = getResources().getString(R.string.scorelist_like);
				break;
			case LIST:
				title = getResources().getString(R.string.scorelist_list);
				break;
			default:
				break;
			}
			return title;
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

	}
	
	/**
	 * Activity传入的筛选点击事件
	 * @param selectData
	 */
	public void setPopupWdListener(List<String> selectData){
		mShowFragment.chooseScoreMatch(selectData);
	}
	
	/**
	 * 指定Viewpager当前正在显示的页面
	 * @param position
	 */
	public void setCurrentPage(int position){
		if (mViewPager != null) {
			mViewPager.setCurrentItem(position);
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		//hidden 表示当前fragment是否被隐藏了   
		if (!hidden) {
			mAdapter.notifyDataSetChanged();
		}
//		
	}
	
	
	/** ViewPager中页面改变了的监听**/
	public interface onPageChangedListener{
		void onPageChanged(int position);
	}
	
	private onPageChangedListener myPageChangedListener;
	
	public void setOnPageChangedListener(onPageChangedListener listener){
		this.myPageChangedListener = listener;
	}

	/** ViewpagerIndicator 中的页面滑动监听 
	 *  注意 滑动监听如果重复设置，会导致其中一个无效 
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		if (myPageChangedListener != null) {
			myPageChangedListener.onPageChanged(position);
			this.currentPosition = position;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
	
	/**
	 * 获取当前的页面位置
	 * @return
	 */
	public int getCurrentPosition(){
		
		return currentPosition;
	}
	
	/**
	 * 获取指定期次在Viewpager中的位置
	 * @param num
	 * @return
	 */
	public int getCurrentPage(String num){
		int result = 0;
		for (int i = 0; i < mTitles.size(); i++) {
			if (mTitles.get(i).equals(num)) {
				result = i;
			}
		}
		return result;
	}
	
	
	/**
	 * 刷新Viewpager 
	 * 不加则导致切换老足彩/竞彩足球的时候
	 * 导致的tabs标题的日期顺序被打乱
	 */
	public void getDateFromScoreListFragment(List<String> mList){
		
		List<String> lists = new ArrayList<String>();
		lists.addAll(mList);
		Collections.reverse(lists);
		mTitles.clear();
		mFragments.clear();
		mTitles.addAll(lists);
		initData();
		mIndicator.setVisibleTabCount(1);
		mIndicator.setTabItemTitles(lists);
		
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
		//用户第一次进入  默认停留在当前期
		setCurrentPage(getCurrentPage("2016193"));
//		setCurrentPage(currentPosition);
		
	}
	
	/**
	 * 获取当前正在显示的fragment
	 * 用于刷新该fragment的期次内容
	 * @return
	 */
	public void updateCurrentFragment_Match(int position){
	
		mShowFragment.updateCurrentIssueNo(mTitles.get(position));
	}
	
	/**
	 * push推送 更新底层fragment的item中内容
	 * @param bean
	 */
	public void updateDatas(ScoreMatchBean bean){
		
		mShowFragment.updateDatas(bean);
		
	}
	
	/**
	 * push推送 更新底层fragment的item中内容
	 * @param list
	 */
	public void updateDatas(List<Map<String, String>> list){
		
		mShowFragment.updateDatas(list);
	}

	@Override
	protected void lazyLoad() {
		
	}
}
