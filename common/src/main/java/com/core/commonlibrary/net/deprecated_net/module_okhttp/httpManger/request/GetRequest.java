package com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request;

import java.util.WeakHashMap;

import okhttp3.Request;
import okhttp3.RequestBody;

public class GetRequest extends OkHttpRequest
{
    public GetRequest(String url, Object tag, WeakHashMap<String, Object> params, WeakHashMap<String, Object> headers, int id)
    {
        super(url, tag, params, headers,id);
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody)
    {
        return builder.get().build();
    }


}
