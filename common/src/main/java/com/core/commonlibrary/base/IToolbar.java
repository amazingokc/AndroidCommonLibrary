package com.core.commonlibrary.base;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-01-28 下午 6:29
 * 文件描述：
 */

public interface IToolbar {

    void initToolbar(Toolbar toolbar);

    //设置标题内容
    void setToolbarTopTitle(String strTitle);

    TextView getToolbarTopTitle();

    //设置标题内容字体颜色
    void setToolbarTopTitleColor(int colorId);

    //设置标题栏背景颜色
    void setToolbarBackgroudColor(int color);

    //右边区域设置icon
    void setToolbarMainMenuItem(Drawable icon, MenuItem.OnMenuItemClickListener listener);

    //右边区域设置文字
    void setToolbarMainMenuItem(String title, MenuItem.OnMenuItemClickListener listener);

    //右边区域设置文字及文字颜色
    void setToolbarMainMenuItem(String title, int color, MenuItem.OnMenuItemClickListener listener);

    void hideToolbarMainMenuItem();

    void showToolbarMainMenuItem();

    //获取toolbar实例的引用
    Toolbar getToolbar();
    //设置阴影
    void setToolbarElevation(float value);

    /**
     * @param toolbar  要设置menuitem的toolbar
     * @param actionId menuitem的资源id
     * @param color    要设置的颜色
     * @Description hack方式设置menuItem的颜色
     */
    void hackSetToolbarMenuItemTextColor(Toolbar toolbar, int actionId, int color);

}

