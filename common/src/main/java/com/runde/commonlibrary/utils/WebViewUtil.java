package com.runde.commonlibrary.utils;

import android.view.ViewGroup;
import android.view.ViewParent;

import com.runde.commonlibrary.customview.TencentWebView;
import com.runde.commonlibrary.global.ApplicationContext;
import com.tencent.smtt.sdk.WebSettings;

public class WebViewUtil {

    public static void initWebSetting(WebSettings settings) {
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //DOM存储API是否可用(默认是false，可能出现空白页，所以建议true)
        //------实现localStorage需要的存储条件  start----
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        String appCachePath = ApplicationContext.getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setDatabaseEnabled(true);
        //------实现localStorage需要的存储条件  end-----
        //解决图片不显示
        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);
        //设置渲染优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //设置 缓存模式
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        String userAgentString = settings.getUserAgentString();
        settings.setUserAgent(userAgentString + ";android_APP");
    }

    public static void destroyWebView(TencentWebView webView) {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
    }

}
