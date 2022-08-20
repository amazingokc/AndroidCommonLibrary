package com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;

import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.PostStringRequest;
import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.RequestCall;

import okhttp3.MediaType;

public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder>
{
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }


}
