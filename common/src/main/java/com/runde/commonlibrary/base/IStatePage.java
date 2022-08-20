package com.runde.commonlibrary.base;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.runde.commonlibrary.interfaces.CommonClickListener;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-07-24 下午 2:10
 * 文件描述：
 */
public interface IStatePage {

    /**
     * @param isShowNormal true显示普通的菊花样式、false则在状态页显示设计师精心定制的loading
     * @param message      提示语
     */
    void showDialog(boolean isShowNormal, String message);

    void dismissDialog();

    void checkStatePage();

    /**
     * 更换状态页的ViewGroup
     */
    void resetStatePageParent(ViewGroup parent, int index);

    /**
     * 设置页面为需要展示的UI(数据加载成功)
     */
    void setPageSuccess();

    /**
     * 设置页面为加载数据出错的UI
     */
    void setPageError(int responCode, String errorMessage, CommonClickListener commonClickListener);

    /**
     * 设置页面为请求到的数据为空的UI
     */
    void setPageEmpty(@Nullable Drawable drawable, CharSequence tip);

    void setPageEmpty(@Nullable Drawable drawable, CharSequence tip, CharSequence option, View.OnClickListener
            onClickListener);

    void setPageEmpty(int drawableId, CharSequence tip, CharSequence option, View.OnClickListener
            onClickListener);

}
