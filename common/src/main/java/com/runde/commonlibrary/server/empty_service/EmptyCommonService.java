package com.runde.commonlibrary.server.empty_service;

import android.graphics.drawable.Drawable;

import com.runde.commonlibrary.bean.BaseUrlBean;
import com.runde.commonlibrary.server.service.ICommonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Interceptor;
import okhttp3.Response;

public class EmptyCommonService implements ICommonService {
    @Override
    public Drawable getStatepageConnectWrong() {
        return null;
    }

    @Override
    public Drawable getStatepageNodata() {
        return null;
    }

    @Override
    public int getColorPrimary() {
        return 0;
    }

    @Override
    public int getStatuesBarColor() {
        return 0;
    }

    @Override
    public String getUmengAppKey() {
        return "";
    }

    @Override
    public String getOfficialBaseUrl() {
        return "";
    }

    @Override
    public String getSandboxBaseUrl() {
        return "";
    }

    @Override
    public String getOfficialBasePicUrl() {
        return "";
    }

    @Override
    public String getSandboxBasePicUrl() {
        return "";
    }

    @Override
    public String getWXAppId() {
        return "";
    }

    @Override
    public WeakHashMap<String, Object> getFiledMapParams() {
        return new WeakHashMap<>();
    }

    @Override
    public WeakHashMap<String, Object> getHeaderMapParams() {
        return new WeakHashMap<>();
    }

    @Override
    public String getUserId() {
        return "";
    }

    @Override
    public String getUserToken() {
        return "";
    }

    @Override
    public String getRefreshToken() {
        return "";
    }

    @Override
    public int getAppSmallIcon() {
        return 0;
    }

    @Override
    public int getAppLargeIcon() {
        return 0;
    }

    @Override
    public List<BaseUrlBean> getTestHost() {
        return new ArrayList<>();
    }

    @Override
    public int getHttpSuccessCode() {
        return 1;
    }

    @Override
    public boolean verifyIsLogout(int code) {
        return false;
    }

    @Override
    public Interceptor getTokenInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        };
    }

    @Override
    public Interceptor getCommonVerifyInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        };
    }

    @Override
    public Drawable getStatePageBtnBackgroud() {
        return null;
    }

    @Override
    public int getStatePageBtnTextColor() {
        return 0;
    }

    @Override
    public int getStatePageloading() {
        return 0;
    }

}
