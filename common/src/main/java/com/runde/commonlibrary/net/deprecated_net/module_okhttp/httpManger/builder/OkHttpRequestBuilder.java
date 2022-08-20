package com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;


import com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder>
{
    protected String url;
    protected Object tag;
    protected WeakHashMap<String, Object> headers;
    protected WeakHashMap<String, Object> params;
    protected int id;

    public T id(int id)
    {
        this.id = id;
        return (T) this;
    }

    public T url(String url)
    {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag)
    {
        this.tag = tag;
        return (T) this;
    }

    public T headers(WeakHashMap<String, Object> headers)
    {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String val)
    {
        if (this.headers == null)
        {
            headers = new WeakHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    public abstract RequestCall build();
}
