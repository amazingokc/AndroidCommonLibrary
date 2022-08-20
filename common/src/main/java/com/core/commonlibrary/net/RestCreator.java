package com.core.commonlibrary.net;


import com.core.commonlibrary.global.BaseUrl;
import com.core.commonlibrary.net.interceptors.HeaderInterceptor;
import com.core.commonlibrary.server.ServiceFactory;
import com.core.commonlibrary.utils.AppVersionUtil;
import com.core.commonlibrary.utils.SystemUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public final class RestCreator {

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 15;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();

        private static OkHttpClient.Builder addInterceptor() {
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor
                        .Builder()
                        .addHeader("equipment-type", "Android")
                        .addHeader("system-version", SystemUtils.getSystemVersion())
                        .addHeader("phone-model", SystemUtils.getSystemModel())
                        .addHeader("sn", SystemUtils.getUUID())
                        .addHeader("kys-version", AppVersionUtil.getVersion())
                        .addHeader("rdjy-version", AppVersionUtil.getVersion())
                        .addHeader("version-code", AppVersionUtil.getVersionCode() + "")
                        .addHeader("pad", SystemUtils.isPad() + "")
                        .addHeader("down-platform", SystemUtils.getChannel())
                        .build())
                .addInterceptor(ServiceFactory.getInstance().getCommonService().getTokenInterceptor())
                .addInterceptor(ServiceFactory.getInstance().getCommonService().getCommonVerifyInterceptor())
                .build();
    }


    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BaseUrl.getBaseUrl() + "/")
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //使用RxJava作为回调适配器
                .build();
    }

    /**
     * Service接口
     */
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return OKHttpHolder.addInterceptor();
    }

}
