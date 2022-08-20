package com.runde.commonlibrary.net.interceptors;


import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/19 0019
 * description:
 */
public class HeaderInterceptor implements Interceptor {

    private Builder builder;

    private HeaderInterceptor(Builder builder) {
        this.builder = builder;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (builder.getHeaders().size() > 0) {
            Headers headers = request.headers();
            Set<String> names = headers.names();
            Iterator<String> iterator = names.iterator();
            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder.headers(builder.getHeaders());
            while (iterator.hasNext()) {
                String name = iterator.next();
                requestBuilder.addHeader(name, headers.get(name));
            }
            request = requestBuilder.build();
        }
        return chain.proceed(request);
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private Headers.Builder builder;

        public Builder() {
            builder = new Headers.Builder();
        }

        Headers getHeaders() {
            return builder.build();
        }

        /**
         * @param name  Filed
         * @param value Value
         * @return Builder
         * Add a field with the specified value
         */
        public Builder addHeader(String name, String value) {
            builder.set(name, value);
            return this;
        }

        public HeaderInterceptor build() {
            return new HeaderInterceptor(this);
        }
    }

}
