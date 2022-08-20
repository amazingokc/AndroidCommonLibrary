package com.runde.commonlibrary.net.deprecated_net;


import com.runde.commonlibrary.RDLibrary;
import com.runde.commonlibrary.net.deprecated_net.interceptors.interceptors.logging.Level;
import com.runde.commonlibrary.net.deprecated_net.interceptors.interceptors.logging.LoggingInterceptor;
import com.runde.commonlibrary.global.BaseUrl;
import com.runde.commonlibrary.utils.AppVersionUtil;
import com.runde.commonlibrary.utils.SystemUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public final class RestCreator {

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 10;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();

        private static OkHttpClient.Builder addInterceptor() {
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                //                .addInterceptor(new CacheInterceptor(App.getInstance()))
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(RDLibrary.isDebug()) //是否开启日志打印
                        //                        .addHeader("Cache-Control", "max-age=640000")
                        //                        .addHeader("Accept", "application/json")
                        //                        .addHeader("Content-Type", "application/json")
                        //                        .addHeader("Connection", "close")
                        .addHeader("equipment-type", "Android")
                        .addHeader("system-version", SystemUtils.getSystemVersion())
                        .addHeader("phone-model", SystemUtils.getSystemModel())
                        .addHeader("meid", SystemUtils.getUUID())
                        .addHeader("sn", SystemUtils.getUUID())
                        .addHeader("kys-version", AppVersionUtil.getVersion())
                        .addHeader("pad", SystemUtils.isPad() + "")
                        .addHeader("down-platform", SystemUtils.getChannel())
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        //                        .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                        .build())
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
}
