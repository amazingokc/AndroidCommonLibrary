package com.core.commonlibrary.utils;

import com.core.commonlibrary.server.ServiceFactory;

import java.util.WeakHashMap;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/26 0026
 * description: 主要用户获取 http 请求需要的通用请求头
 */
public class RdRequestHeadsParamsUtil {


    public static WeakHashMap getFiledMapParams() {
        return ServiceFactory.getInstance().getCommonService().getFiledMapParams();
    }
}
