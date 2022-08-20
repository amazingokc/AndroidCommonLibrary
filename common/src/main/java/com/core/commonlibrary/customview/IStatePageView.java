package com.core.commonlibrary.customview;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-01-26 下午 6:04
 * 文件描述：
 */

public interface IStatePageView {

    void onDestroy();

    void setPageSuccess();

    void setPageError(CharSequence desc, View.OnClickListener onClickListener);

    void setPageEmpty(CharSequence desc);

    void resetErrorBackgroud(Drawable drawable);

    void resetEmptyBackgroud(Drawable drawable);

    /**
     * @param strLoadingTip 加载中的提示语
     */
    void showLoading(String strLoadingTip);
}
