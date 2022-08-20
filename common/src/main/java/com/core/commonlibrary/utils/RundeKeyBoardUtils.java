package com.core.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.core.commonlibrary.global.ApplicationContext;
import com.core.commonlibrary.interfaces.OnKeyBoardListener;


/**
 * 软键盘工具类
 */
public class RundeKeyBoardUtils {


    public static void openKeybord(EditText mEditText) {
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) ApplicationContext.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 切换软件盘 显示隐藏
     */
    public static void switchSoftInputMethod(Activity act) {
        // 方法一(如果输入法在窗口上已经显示，则隐藏，反之则显示)
        InputMethodManager iMM = (InputMethodManager) act
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 切换软件盘 显示隐藏
     */
    public static void switchSoftInputMethod(Context context, IBinder token) {
        // 方法一(如果输入法在窗口上已经显示，则隐藏，反之则显示)
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void closeKeybord(EditText mEditText, Context mContext) {

        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static void hideInput(Activity mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = mContext.getWindow().peekDecorView();
        if (null != v && imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 软键盘弹起隐藏监听
     */
    public static void setOnKeyBoardListener(Activity mContext, EditText mEditText, OnKeyBoardListener listener) {
        final boolean[] isFirst = {true};
        //当键盘弹出隐藏的时候会 调用此方法。
        mEditText.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            final Rect rect = new Rect();
            mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            final int screenHeight = mContext.getWindow().getDecorView().getRootView().getHeight();
            final int heightDifference = screenHeight - rect.bottom;
            boolean visible = heightDifference > screenHeight / 3;
            if (visible) { //弹起
                if (isFirst[0]) {
                    listener.showBoard();
                    isFirst[0] = false;
                }
            } else {      //隐藏
                listener.hideBoard();
                isFirst[0] = true;
            }
        });
    }
}
