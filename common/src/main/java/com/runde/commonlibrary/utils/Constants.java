package com.runde.commonlibrary.utils;

import com.runde.commonlibrary.server.ServiceFactory;

/**
 * Created by zhengmeixiong on 2018-04-16.
 */

public class Constants {
    //答题页面选项条目状态
    public static final int NORMAL = 0; //无点击状态
    public static final int SELECTED = 1; //选中状态
    public static final int TRUE = 2; //选对状态
    public static final int FALSE = 3; //选错状态

    //页面列表数据接口回调的三种情况(可用于判断占位图显示情况)
    public static final String HAVE_DATA = "haveData";
    public static final String NO_DATA = "noData";
    public static final String CONNECT_ERROR = "connectError";

    public static final String APP_WEB_CALL_ANDROID_INTERFACE_NAME = "android"; //网页js调用安卓本地方法接口映射名字

}
