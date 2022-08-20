package com.core.commonlibrary.utils;

import com.core.commonlibrary.global.ApplicationContext;
import com.umeng.analytics.MobclickAgent;

/**
 * 自定义异常工具类
 * */
public class CustomExceptionUtil {

    public static void reportError(String errorMessage) {
        MobclickAgent.reportError(ApplicationContext.getContext(), errorMessage);
    }
}
