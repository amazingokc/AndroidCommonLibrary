package com.runde.commonlibrary.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-10-09 上午 11:58
 * 文件描述：
 */
public class ViewPagerScroll extends ViewPager {

    private boolean isScroll;//是否可以左右滑动

    public ViewPagerScroll(@NonNull Context context) {
        super(context);
    }

    public ViewPagerScroll(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean scroll) {
        isScroll = scroll;
    }

    /**
     * 是否拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onTouchEvent(ev);
        } else {
            return true;// 可行,消费,拦截事件
        }
    }

    //关闭滑动效果
    @Override
    public void setCurrentItem(int item) {
//        super.setCurrentItem(item);
        super.setCurrentItem(item, false);
    }
}
