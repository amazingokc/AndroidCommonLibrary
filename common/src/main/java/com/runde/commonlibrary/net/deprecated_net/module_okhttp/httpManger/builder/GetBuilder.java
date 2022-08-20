package com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;

import android.net.Uri;

import com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.GetRequest;
import com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.RequestCall;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;


public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable {
    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }

        return new GetRequest(url, tag, params, headers, id).build();
    }

    protected String appendParams(String url, WeakHashMap<String, Object> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, (String) params.get(key));
        }
        return builder.build().toString();
    }


    @Override
    public GetBuilder params(WeakHashMap<String, Object> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new WeakHashMap<>();
        }
        params.put(key, val);
        return this;
    }


}
