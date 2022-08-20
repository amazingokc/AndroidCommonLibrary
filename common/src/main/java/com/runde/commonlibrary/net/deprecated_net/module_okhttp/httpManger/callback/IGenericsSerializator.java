package com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.callback;


public interface IGenericsSerializator {
    <T> T transform(String response, Class<T> classOfT);
}
