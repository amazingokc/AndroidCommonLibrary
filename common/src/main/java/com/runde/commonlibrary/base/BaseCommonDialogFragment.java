package com.runde.commonlibrary.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.bean.SingleMsgBean;

import java.lang.reflect.Field;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-09-25 上午 11:38
 * 文件描述：
 */
public class BaseCommonDialogFragment extends DialogFragment {

    private int style = R.style.HomeDialog;

    public void setStyle(int style) {
        this.style = style;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, style);
    }

    //防止（Can not perform this action after onSaveInstanceState）
    @Override
    public void show(FragmentManager manager, String tag) {
        if (isAdded()) return;
        try {
            Field dismissed = BaseCommonDialogFragment.class.getSuperclass().getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
            Field shownByMe = BaseCommonDialogFragment.class.getSuperclass().getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    //防止（Can not perform this action after onSaveInstanceState）
    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }

    //防止（Can not perform this action after onSaveInstanceState）
    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }
}
