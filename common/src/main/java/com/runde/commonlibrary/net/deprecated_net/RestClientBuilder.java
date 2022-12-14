package com.runde.commonlibrary.net.deprecated_net;

import android.content.Context;

import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.net.deprecated_net.callback.IError;
import com.runde.commonlibrary.net.deprecated_net.callback.IFailure;
import com.runde.commonlibrary.net.deprecated_net.callback.IRequest;
import com.runde.commonlibrary.net.deprecated_net.callback.ISuccess;
import com.runde.commonlibrary.utils.LLog;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public final class RestClientBuilder {

    private WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    private String mUrl = null;
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private RequestBody mBody = null;
    private Context mContext = null;
//    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;

    RestClientBuilder() {
    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String file) {
        this.mFile = new File(file);
        return this;
    }

    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final RestClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        return this;
    }


    public final HttpClient build() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        //???????????????
//        PARAMS.put("timestamp", timestamp);
//        PARAMS.put("appKey", CommonUtil.getAppKey(timestamp));
//        PARAMS.put("signature", CommonUtil.getSignature(timestamp));
//        PARAMS.put("appId", CommonUtil.getAppId());
//        PARAMS.put("userId", ServiceFactory.getInstance().getHostService().getHttpRequestParams());
        PARAMS.putAll(ServiceFactory.getInstance().getCommonService().getFiledMapParams());
        LLog.d("url--" + this.mUrl, PARAMS.toString());
        return new HttpClient(mUrl, PARAMS,
                mDownloadDir, mExtension, mName,
                mIRequest, mISuccess, mIFailure,
                mIError, mBody, mFile, mContext);
    }
}
