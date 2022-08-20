package com.runde.commonlibrary.server.service;

import android.graphics.drawable.Drawable;

import com.runde.commonlibrary.bean.BaseUrlBean;

import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Interceptor;

public interface ICommonService {

    Drawable getStatepageConnectWrong();

    Drawable getStatepageNodata();

    Drawable getStatePageBtnBackgroud();

    int getStatePageBtnTextColor();

    int getStatePageloading();

    int getColorPrimary();

    int getStatuesBarColor();

    String getUmengAppKey();

    String getOfficialBaseUrl();

    String getSandboxBaseUrl();

    String getOfficialBasePicUrl();

    String getSandboxBasePicUrl();

    String getWXAppId();

    WeakHashMap<String, Object> getFiledMapParams();

    //提供请求头
    WeakHashMap<String, Object> getHeaderMapParams();

    String getUserId();

    String getUserToken();

    String getRefreshToken();

    int getAppSmallIcon();

    int getAppLargeIcon();

    List<BaseUrlBean> getTestHost();

    //---------------------http请求相关-------------------------------------------------
    int getHttpSuccessCode();
    //验证是否需要token失效或未登录等，需要跳登录页面
    boolean verifyIsLogout(int code);

    //添加token
    Interceptor getTokenInterceptor();
    //token验证
    Interceptor getCommonVerifyInterceptor();
    //-------------------------http请求相关结束----------------------------------
}
