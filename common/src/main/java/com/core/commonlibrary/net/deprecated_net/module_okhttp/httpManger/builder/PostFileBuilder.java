package com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;

import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.PostFileRequest;
import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.RequestCall;

import java.io.File;

import okhttp3.MediaType;

public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder>
{
    private File file;
    private MediaType mediaType;


    public OkHttpRequestBuilder file(File file)
    {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostFileRequest(url, tag, params, headers, file, mediaType,id).build();
    }


}
