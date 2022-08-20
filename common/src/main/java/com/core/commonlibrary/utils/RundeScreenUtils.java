package com.core.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * 与屏幕相关的工具类
 */
public class RundeScreenUtils {
    private static int height16_9;

    // 生成竖屏下w:h=16:9的高
    public static int generateHeight16_9(Activity activity) {
        return height16_9 != 0 ? height16_9 : (height16_9 = getNormalWH(activity)[isPortrait(activity) ? 0 : 1] * 9 / 16);
    }

    // 获取竖屏下w:h=16:9的高
    public static int getHeight16_9() {
        return height16_9;
    }

    // 是否竖屏
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    // 是否横屏
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    // 设置竖屏
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // 设置横屏
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 获取包含状态栏的屏幕宽度和高度
     *
     * @param activity
     * @return {宽,高}
     */
    public static int[] getNormalWH(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            return new int[]{dm.widthPixels, dm.heightPixels};
        } else {
            Point point = new Point();
            WindowManager wm = activity.getWindowManager();
            wm.getDefaultDisplay().getSize(point);
            return new int[]{point.x, point.y};
        }
    }

    // 重置状态栏
    public static void reSetStatusBar(Activity activity, boolean isTextDark, int statusBar) {
        if (activity == null) return;
        if (isLandscape(activity)) {
            hideStatusBar(activity);
        } else {
            setDecorVisible(activity, isTextDark, statusBar);
        }
    }

    // 隐藏状态栏
    public static void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = activity.getWindow().getDecorView();
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            int uiOptions;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                ;
            } else {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            decorView.setSystemUiVisibility(uiOptions);


//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(uiOptions);

        }


//        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // 恢复为不全屏状态
    public static void setDecorVisible(Activity activity, boolean isTextDark, int statusBar) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = activity.getWindow().getDecorView();
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
        if (statusBar > 0) {
            StatusBarUtil.setStatusBarMode(activity, isTextDark, statusBar);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue / scale + 0.5f);
    }

    public static ViewGroup.MarginLayoutParams getLayoutParamsLayout(View layout) {
        ViewGroup.MarginLayoutParams rlp = null;
        if (layout != null) {
            if (layout.getParent() instanceof RelativeLayout) {
                rlp = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            } else if (layout.getParent() instanceof LinearLayout) {
                rlp = (LinearLayout.LayoutParams) layout.getLayoutParams();
            } else if (layout.getParent() instanceof FrameLayout) {
                rlp = (FrameLayout.LayoutParams) layout.getLayoutParams();
            } else if (layout.getParent() instanceof ConstraintLayout) {
                rlp = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
            }
        }
        return rlp;
    }

}
