package com.runde.commonlibrary.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-08-12 上午 11:07
 * 文件描述：
 */
public class ConmmonFragmentAdapter extends FragmentPagerAdapter {

    private List fragmentList;
    private FragmentManager fragmentManager;

    public ConmmonFragmentAdapter(FragmentManager fm, List fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return (Fragment) fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        //需要更新的fragment必须返回POSITION_NONE，否则instantiateItem()不会被调用
        if (object instanceof Fragment) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //拿到缓存的fragment，如果没有缓存的，就新建一个，新建发生在fragment的第一次初始化时
        Fragment f = (Fragment) super.instantiateItem(container, position);
        String fragmentTag = f.getTag();
        if (f != getItem(position)) {
            //如果是新建的fragment，f 就和getItem(position)是同一个fragment，否则进入下面
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //移除旧的fragment
            ft.remove(f);
            //换成新的fragment
            f = getItem(position);
            //添加新fragment时必须用前面获得的tag
            ft.add(container.getId(), f, fragmentTag);
            ft.attach(f);
            ft.commitAllowingStateLoss();
        }
        return f;
    }

}
