package com.runde.commonlibrary.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.runde.commonlibrary.RDLibrary;
import com.runde.commonlibrary.global.ApplicationContext;

import java.lang.reflect.Field;

import es.dmoral.toasty.Toasty;

/**
 * toast工具类、
 * Created by Zmx
 */
public class ToastUtil {

    private static Toast toast;

    public static void show(String content) {
        if (TextUtils.isEmpty(content)) return;
        if (toast == null) {
            toast = Toasty.normal(ApplicationContext.getContext(), content, Toast.LENGTH_LONG);
        }
        toast.cancel();
        toast = Toasty.normal(ApplicationContext.getContext(), content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        //7.1.1系统需要hook Toast的内部实现，防止主线程耗时导致出现异常
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            hookToast(toast);
        }
        toast.show();
//        if (!TextUtils.isEmpty(content)) {
//            if (toast == null && ApplicationContext.getContext() != null) {
//                toast = Toast.makeText(ApplicationContext.getContext(), content, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.getView().setBackground(ApplicationContext.getDrawable(R.drawable.shape_black_bg));
//                TextView v = toast.getView().findViewById(android.R.id.message);
//                v.setTextColor(ApplicationContext.getColor(R.color.white));
//                toast.show();
//            } else if (toast != null) {
//                toast.setText(content);//如果不为空，则直接改变当前toast的文本
//                toast.show();
//            }
//        }
    }

    //Debug模式生效
    public static void showDebug(String content) {
        if (RDLibrary.isDebug()) {
            show("DEBUG:" + content);
        }
    }

    private static class HandlerProxy extends Handler {

        private Handler mHandler;

        public HandlerProxy(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                mHandler.handleMessage(msg);
            } catch (WindowManager.BadTokenException e) {
                //ignore
            }
        }
    }

    private static void hookToast(Toast toast) {
        Class<Toast> cToast = Toast.class;
        try {
            //TN是private的
            Field fTn = cToast.getDeclaredField("mTN");
            fTn.setAccessible(true);

            //获取tn对象
            Object oTn = fTn.get(toast);
            //获取TN的class，也可以直接通过Field.getType()获取。
            Class<?> cTn = oTn.getClass();
            Field fHandle = cTn.getDeclaredField("mHandler");

            //重新set->mHandler
            fHandle.setAccessible(true);
            fHandle.set(oTn, new HandlerProxy((Handler) fHandle.get(oTn)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}