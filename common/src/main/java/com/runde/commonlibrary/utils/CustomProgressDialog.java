package com.runde.commonlibrary.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.runde.commonlibrary.R;


public class CustomProgressDialog extends Dialog {

    private boolean isBackExit;
    private int frequency;

    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        setContentView(R.layout.view_progress_dialog);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
        }
        setCanceledOnTouchOutside(false);
        // 设置透明度  黑暗度
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.0f;
        lp.windowAnimations = R.style.CustomProgressDialog_Animation;
        window.setAttributes(lp);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = findViewById(R.id.iv_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }


    /**
     * [Summary] setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    private void setMessage(String strMessage) {
        TextView tvMsg = findViewById(R.id.tv_loading_msg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }

    // 显示滚动进度
    public void showProgressDialog(String strMessage) {
        isBackExit = false;
        try {
            setMessage(strMessage);
            show();
        } catch (Exception e) {

        }
    }

    // 隐藏滚动进度
    public void hideProgressDialog() {
        if (--frequency <= 0)
            try {
                if (isShowing()) {
                    dismiss();
                }
            } catch (Exception e) {
            }
    }

    @Override
    public void onBackPressed() {
        if (isShowing()) {
            if (!isBackExit) {
                dismiss();
            }
        }
    }

}
