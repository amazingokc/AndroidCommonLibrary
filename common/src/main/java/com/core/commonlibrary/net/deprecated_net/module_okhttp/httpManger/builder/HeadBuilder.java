package com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;

import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.OkHttpUtils;
import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.OtherRequest;
import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.RequestCall;

public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
