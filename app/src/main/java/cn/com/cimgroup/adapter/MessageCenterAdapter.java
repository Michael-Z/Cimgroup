package cn.com.cimgroup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.com.cimgroup.BaseFrament;

/**
 * Created by small on 15/12/28.
 */
public class MessageCenterAdapter extends FragmentPagerAdapter{

    private List<String> mTitles;
    private List<BaseFrament> mFragments;

    public MessageCenterAdapter(FragmentManager fm, List<String> mTitleLists,List<BaseFrament> mFragmentList) {
        super(fm);
        mTitles = mTitleLists;
        mFragments = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
