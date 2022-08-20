package com.core.commonlibrary.customview;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-02-20 下午 1:39
 * 文件描述：
 */

public class AlphaPageTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_ALPHA = 0.8f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            page.setAlpha(mMinAlpha);
            page.setScaleY(mMinAlpha);
        } else if (position <= 1) { // [-1,1]

            if (position < 0) //[0，-1]
            {
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
                page.setAlpha(factor);
                page.setScaleY(factor);
            } else//[1，0]
            {
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);
                page.setAlpha(factor);
                page.setScaleY(factor);
            }
        } else { // (1,+Infinity]
            page.setAlpha(mMinAlpha);
            page.setScaleY(mMinAlpha);
        }
    }
}
