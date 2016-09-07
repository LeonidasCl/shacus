package com.example.pc.shacus.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * 孙启凡
 * Date: 2016/09/06
 * Describe： 适配器
 */

public class CommonPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public CommonPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        int ret=0;
        if (mFragments != null) {
            ret=mFragments.size();
        }
        return ret;
    }
}