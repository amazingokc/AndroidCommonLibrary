package com.runde.commonlibrary.utils;

import com.runde.commonlibrary.global.ApplicationContext;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-03-14 上午 11:20
 * 文件描述：
 */

public class DisplayUtil {

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * <p>
     * （DisplayMetrics类中属性density）
     */
    public static int px2dip(float pxValue) {
        final float scale = ApplicationContext.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * <p>
     * （DisplayMetrics类中属性density）
     */
    public static int dip2px(float dipValue) {
        final float scale = ApplicationContext.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     */
    public static int px2sp(float pxValue) {
        final float fontScale = ApplicationContext.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     */
    public static int sp2px(float spValue) {
        final float fontScale = ApplicationContext.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
