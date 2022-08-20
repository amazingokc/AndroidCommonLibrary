package com.runde.commonlibrary.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.runde.commonlibrary.global.ApplicationContext;


public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = ApplicationContext.getContext().getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = ApplicationContext.getContext().getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    public static int dp2px(float dipValue) {
        final float scale = ApplicationContext.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
