package com.core.commonlibrary.utils;

import android.text.style.ClickableSpan;
import android.view.View;


public abstract class LazyClickableSpan extends ClickableSpan {
    private long lastClickTime = -1;
    private long mInterval = 500;

    @Override
    public void onClick(View v) {
        if (checkPermission() && moreCheck()) {
            onLazyClick(v);
        } else {
            ToastUtil.showDebug("重复点击！");
        }
    }

    private boolean checkPermission() {
        if (-1 != lastClickTime) {
            long tmp = System.currentTimeMillis();
            if (mInterval > tmp - lastClickTime) {
                lastClickTime = tmp;
                return false;
            } else {
                lastClickTime = tmp;
                return true;
            }
        } else {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
    }

    public void setInterval(long interval) {
        mInterval = interval;
    }

    protected boolean moreCheck() {
        return true;
    }

    public abstract void onLazyClick(View v);

}
