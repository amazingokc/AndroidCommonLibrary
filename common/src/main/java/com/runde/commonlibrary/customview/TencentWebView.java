package com.runde.commonlibrary.customview;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;


/**
 * Created by hua on 2019/8/14 0014.
 */
public class TencentWebView extends com.tencent.smtt.sdk.WebView {


    public TencentWebView(Context context) {
        super(getFixedContext(context));
//        this.context = getFixedContext(context);
//        setDataDirectorySuffix();
    }

    public TencentWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
//        this.context = getFixedContext(context);
//        setDataDirectorySuffix();
    }

    public TencentWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
//        this.context = getFixedContext(context);
//        setDataDirectorySuffix();
    }


    @Override
    public void loadUrl(String s) {

        WebSettings settings = getSettings();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //https可以加载http图片
            settings.setMixedContentMode(0);
        }
        super.loadUrl(s);
    }

    // To fix Android Lollipop WebView problem create a new configuration on that Android version only
    //修复了vivo5.1机型无法加载webview而闪退问题
    private static Context getFixedContext(Context context) {
        if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) // Android Lollipop 5.0 & 5.1
            return context.createConfigurationContext(new Configuration());
        return context;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    //    private void setDataDirectorySuffix() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            View view = getView();
//            if (view instanceof WebView) {
//                ((WebView) view).setDataDirectorySuffix(getProcessName(context));
//            }
//        }
//    }
//
//    public String getProcessName(Context context) {
//        if (context == null) return null;
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
//            if (processInfo.pid == android.os.Process.myPid()) {
//                return processInfo.processName;
//            }
//        }
//        return null;
//    }

}
