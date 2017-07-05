package com.dlg.personal.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：雇员卡片
 * 创建时间：2017/6/30 13:13
 */

public class EmployeeCardAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    public EmployeeCardAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.mFragments = fragments;
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
