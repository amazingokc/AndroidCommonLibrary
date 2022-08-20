package com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;

import java.util.Map;
import java.util.WeakHashMap;


public interface HasParamsable {
    OkHttpRequestBuilder params(WeakHashMap<String, Object> params);

    OkHttpRequestBuilder addParams(String key, String val);
}
