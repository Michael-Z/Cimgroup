package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.frament.TzListFragment;
import cn.com.cimgroup.popwindow.PopupWndSwitch_TzList;
import cn.com.cimgroup.popwindow.PopupWndSwitch_TzList.OnTypeItemClickListener;
import cn.com.cimgroup.view.PagerSlidingTabStrip;
import cn.com.cimgroup.xutils.ActivityManager;

/**
 * 我的投注
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年10月31日
 */
@SuppressLint("HandlerLeak")
public class TzListActivity extends BaseActivity implements OnClickListener, OnTypeItemClickListener {
	/** 要展示的frament集合 MCenterRecordTz 为标识、主要在frament中区分投注记录类型，用于获取相应的数据 **/
	private Map<MCenterRecordTz, Fragment> mFraments = new HashMap<MCenterRecordTz, Fragment>();

	public PopupWndSwitch_TzList popup_tzlist;

	private View id_title_view;

	public LinearLayout pop_common_bg;

	private TextView mSwitchTextView;

	private TZPagerAdapter mPagerAdapter;

	public Handler mHandler1;

	private int currentTab = 0;

	private ViewPager mViewPager;

	private int tag = 0;
	/** 标题选择集合 */
	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();;

	// private boolean isFirstLoad = true;

	// 这个handler是fragment里边的Adapter构造方法的supper（）父类中的方法传递的
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case GlobalConstants.MSG_NOTIFY_SWITCH_WAY_TZLIST:
				showLayout((String) message.obj);
				popup_tzlist.showOrhideWindow(id_title_view);
				TzListFragment fragment = (TzListFragment) mPagerAdapter
						.getItem(currentTab);
				tag = message.arg1;
				fragment.loadData(message.arg1, 1);
				break;

			default:
				break;
			}
		}
	};
	/**当前的活动Fragment*/
	public TzListFragment mShowFragment;

	/**
	 * 投注记录类型
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2015年10月31日
	 */
	public enum MCenterRecordTz {
		// 全部
		ALL,
		// 代购
		BUY,
		// 追号
		ZHUI,
		// 中奖
		WINNING
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_tab2);
		initViews();
	}

	private void initViews() {
		initTitleDatas();
		id_title_view = findViewById(R.id.id_title_view);

		// 这个控件是“全部彩种” 加下拉箭头
		mSwitchTextView = ((TextView) findViewById(R.id.textView_title_word2));
		((TextView) findViewById(R.id.textView_title_back2))
				.setOnClickListener(this);
		ImageView menu = (ImageView) findViewById(R.id.textView_title_menu2);
		// 图片 三个点 应该是设置然后弹出pupupwindow 暂时是隐藏的
		menu.setVisibility(View.GONE);
		// 最右侧的图标 暂时隐藏
		ImageView select = (ImageView) findViewById(R.id.imageView_title_select2);
		select.setVisibility(View.GONE);
		select.setOnClickListener(this);

		mSwitchTextView.setOnClickListener(this);
		mSwitchTextView.setText(getResources()
				.getString(R.string.tz_select_all));

		pop_common_bg = (LinearLayout) findViewById(R.id.pop_common_bg);

		// 初始化pager适配器
		mPagerAdapter = new TZPagerAdapter(getSupportFragmentManager());
		// android.support.v4.view.ViewPager这是个VewPager
		mViewPager = (ViewPager) findViewById(R.id.v4View_common_pager);
		// fragment->Map<MCenterRecordTz, Fragment>(集和)->PagerAdapter->ViewPager
		mFraments.put(MCenterRecordTz.ALL, new TzListFragment());
		mFraments.put(MCenterRecordTz.BUY, new TzListFragment());
		mFraments.put(MCenterRecordTz.ZHUI, new TzListFragment());
		mFraments.put(MCenterRecordTz.WINNING, new TzListFragment());
		mPagerAdapter.setFraments(mFraments);

		mViewPager.setAdapter(mPagerAdapter);
		// HorizontalScrollView的实现类，是用来实现画廊效果的
		PagerSlidingTabStrip mPagerTab = (PagerSlidingTabStrip) findViewById(R.id.cvView_common_pagertab);
		mPagerTab.setViewPager(mViewPager);
		mPagerTab.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentTab = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);
		String text = intent.getStringExtra("text");
		// 彩种选项的pupWindow初始化
		if (type != 0) {
			showLayout(text);
			tag = type;
			if (popup_tzlist == null) {
				popup_tzlist = new PopupWndSwitch_TzList(mActivity,
						pop_common_bg, tag,mList);
				popup_tzlist.setOnTypeItemClickListener(this);
			}
			TzListFragment fragment = (TzListFragment) mPagerAdapter
					.getItem(currentTab);
			// 发送请求-请求type对应的彩种的个人投注信息
			// fragment.loadData(type,1);
			fragment.updateType(type);
		} else {
			TzListFragment fragment = (TzListFragment) mPagerAdapter
					.getItem(currentTab);
			fragment.loadData(GlobalConstants.TAG_TZLIST_ALL, 1);
		}
	}
	/**
	 * 初始化标题数据源
	 */
	@SuppressWarnings("serial")
	private void initTitleDatas() {
		mList.add(new HashMap<String, String>(){{put("tag","400");put("name","全部");}});
		mList.add(new HashMap<String, String>(){{put("tag","401");put("name","竞彩篮球");}});
		mList.add(new HashMap<String, String>(){{put("tag","402");put("name","竞彩足球");}});
		mList.add(new HashMap<String, String>(){{put("tag","403");put("name","胜负彩");}});
		mList.add(new HashMap<String, String>(){{put("tag","404");put("name","任九场");}});
		mList.add(new HashMap<String, String>(){{put("tag","405");put("name","进球彩");}});
		mList.add(new HashMap<String, String>(){{put("tag","406");put("name","半全场");}});
		mList.add(new HashMap<String, String>(){{put("tag","407");put("name","大乐透");}});
		mList.add(new HashMap<String, String>(){{put("tag","408");put("name","排列三");}});
		mList.add(new HashMap<String, String>(){{put("tag","409");put("name","七星彩");}});
		mList.add(new HashMap<String, String>(){{put("tag","410");put("name","排列五");}});
//		mList.add(new HashMap<String, String>(){{put("tag","411");put("name","11运");}});
//		mList.add(new HashMap<String, String>(){{put("tag","411");put("name","11选五");}});
	}

	public class TZPagerAdapter extends FragmentPagerAdapter {
		private static final String MCENTERRECORDTZ = "mcenterrecordtz";

		private Map<MCenterRecordTz, Fragment> mFraments = new HashMap<MCenterRecordTz, Fragment>();

		FragmentManager mFragmentManager;

		public TZPagerAdapter(FragmentManager fm) {
			super(fm);
			this.mFragmentManager = fm;
		}
		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			mShowFragment = (TzListFragment) object;
			super.setPrimaryItem(container, position, object);
		}

		public void setFraments(Map<MCenterRecordTz, Fragment> fragments) {
			this.mFraments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			MCenterRecordTz type = MCenterRecordTz.values()[position];
			String title = "";
			switch (type) {
			// 全部
			case ALL:
				title = getResources().getString(R.string.tradetype1);
				break;
			// 代购
			case BUY:
				title = getResources().getString(R.string.tz_title_dg);
				break;
			// 追号
			case ZHUI:
				title = getResources().getString(R.string.tz_title_zh);
				break;
			// 中奖
			case WINNING:
				title = getResources().getString(R.string.tz_title_zj);
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
			return newInstance(position);
		}

		@Override
		public int getCount() {
			return mFraments != null ? mFraments.size() : 0;
		}

		private Fragment newInstance(int position) {
			Fragment frament = null;
			if (mFraments != null) {
				frament = this.mFraments
						.get(MCenterRecordTz.values()[position]);
				if (frament.getArguments() == null) {
					Bundle bundleParam = new Bundle();
					bundleParam.putSerializable(MCENTERRECORDTZ,
							MCenterRecordTz.values()[position]);
					frament.setArguments(bundleParam);
				}
			}
			return frament;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 关闭当前页面
		case R.id.textView_title_back2:
//			MainActivity mainActivity = (MainActivity) ActivityManager
//					.isExistsActivity(MainActivity.class);
//			if (mainActivity != null) {
//				// 如果存在MainActivity实例，那么展示LoboPerCenterFrament页面
//				ActivityManager.retain(MainActivity.class);
//				mainActivity.showFrament(LoboPerCenterFrament.class);
//			}
			finish();
			break;
		case R.id.textView_title_word2:
			if (tag == 0) {
				tag = GlobalConstants.TAG_TZLIST_ALL;
			}
			if (popup_tzlist == null) {
				popup_tzlist = new PopupWndSwitch_TzList(mActivity,
						pop_common_bg,tag,mList);
				popup_tzlist.setOnTypeItemClickListener(this);
			}
			popup_tzlist.showOrhideWindow(id_title_view);
			pop_common_bg.setVisibility(View.VISIBLE);
			break;
		case R.id.textView_title_menu2:

			break;
		default:
			break;
		}
	}

	private void showLayout(String obj) {
		mSwitchTextView.setText(obj);
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			MainActivity mainActivity = (MainActivity) ActivityManager
//					.isExistsActivity(MainActivity.class);
//			if (mainActivity != null) {
//				// 如果存在MainActivity实例，那么展示LoboPerCenterFrament页面
//				ActivityManager.retain(MainActivity.class);
//				mainActivity.showFrament(LoboPerCenterFrament.class);
//			}
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	@Override
	public void onSelectItemClick(int tag,int position) {
		mSwitchTextView.setText(mList.get(position).get("name"));
		mShowFragment.loadData(tag, 1);
	}
}
