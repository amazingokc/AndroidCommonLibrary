package com.core.commonlibrary.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerConmmonFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] strTitles;

    private ViewPagerConmmonFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerConmmonFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] strTitles) {
        this(fm);
        this.fragments = fragments;
        this.strTitles = strTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return strTitles[position];
    }
}
