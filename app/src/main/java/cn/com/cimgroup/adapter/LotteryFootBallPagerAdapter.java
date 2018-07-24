package cn.com.cimgroup.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import cn.com.cimgroup.frament.BaseLotteryBidFrament;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;

/**
 * 足彩 Pager选项卡
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
public class LotteryFootBallPagerAdapter extends FragmentStatePagerAdapter {

	private static final String SEARCH_MATCH = "SEARCH_MATCH";

	private BaseLotteryBidFrament[] mFraments;

	FragmentManager mFragmentManager;

	public LotteryFootBallPagerAdapter(FragmentManager fm) {
		super(fm);
		this.mFragmentManager = fm;
	}

	public void setFraments(BaseLotteryBidFrament[] fragments) {
		this.mFraments = fragments;
		notifyDataSetChanged();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public int getCount() {
		return mFraments != null ? mFraments.length : 0;
	}

	@Override
	public Fragment getItem(int position) {
		return newInstance(position);
	}

	private Fragment newInstance(int position) {
		BaseLotteryBidFrament frament = null;
		if (mFraments != null) {
			frament = this.mFraments[position];
			if (frament.getArguments() == null) {
				Bundle bundleParam = new Bundle();
				bundleParam.putInt(SEARCH_MATCH, MatchSearchType.values()[position].ordinal());
				frament.setArguments(bundleParam);
			}
		}
		return frament;
	}

}
