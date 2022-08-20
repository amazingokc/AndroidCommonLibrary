package com.runde.commonlibrary.base;

import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.RundeKeyBoardUtils;
import com.runde.commonlibrary.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity尽量继承该类
 */
public class StatuesBarBaseActivity extends BaseActivity {

    private Unbinder unbinder;
    protected boolean mIsNeedToCloseInput;//是不是需要隐藏键盘的

    //设置状态栏样式
    protected void setStatusBarStyle() {
        setStatusBarWhiteBackground();
    }

    public void setStatusBarThemeBackground(boolean isTextDark) {
        StatusBarUtil.setStatusBarMode(this, isTextDark,
                ServiceFactory.getInstance().getCommonService().getStatuesBarColor());
    }

    public void setStatusBarWhiteBackground() {
        //这里不能使用 ApplicationContext.getColor(R.color.white);获取
        //垃圾小米4.4系统更改状态栏颜色的API识别不了这个颜色，会导致崩溃
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);
    }

    protected void setStatusBarThemeCustomBackground(boolean isTextDark, int color) {
        StatusBarUtil.setStatusBarMode(this, isTextDark, color);
    }

    //设置toolbar颜色
    protected void setToolBarWhiteStyle() {
        /**toolbar的Menu字体颜色无法通过java代码更改，需要通过设置主题来修改(setTheme(R.style.ToolBarOrangeStyle))*/
        getToolbar().setNavigationIcon(ApplicationContext.getDrawable(R.drawable.app_toolbar_back));
        setToolbarTopTitleColor(ApplicationContext.getColor(R.color.text_color333));
        getToolbar().setBackgroundColor(ApplicationContext.getColor(R.color.white));
    }

    protected void setToolBarThemeBackground() {
        /**toolbar的Menu字体颜色无法通过java代码更改，需要通过设置主题来修改(setTheme(R.style.ToolBarOrangeStyle))*/
        getToolbar().setNavigationIcon(ApplicationContext.getDrawable(R.drawable.back));
        setToolbarTopTitleColor(ApplicationContext.getColor(R.color.white));
        getToolbar().setBackgroundColor(ServiceFactory.getInstance().getCommonService().getColorPrimary());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getToolbar().setElevation(0);
        }
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);
        setStatusBarStyle();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setCenterView(int layout) {
        super.setCenterView(layout);
        setStatusBarStyle();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setCenterView(View view) {
        super.setCenterView(view);
        setStatusBarStyle();
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 设置状态栏透明   沉浸式状态栏
     */

    public void setImmerseLayout() {// view为标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsNeedToCloseInput) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View view = getCurrentFocus();
                if (isHideInput(view, ev)) {
                    HideSoftInput(view.getWindowToken());
                    view.clearFocus();
                }
            }

        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {

        }
        return false;
    }


    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            RundeKeyBoardUtils.switchSoftInputMethod(this, token);
        }
    }

    /**
     * 判定是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    private static final int MIN_DELAY_TIME = 500;  // 两次点击间隔不能少于500ms
    private static long lastClickTime;

    /**
     * 此方法可以配合ButterKnife使用，在onViewClicked方法里调用
     *
     * @return
     */
    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
