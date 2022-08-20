package com.core.commonlibrary.net;

import com.core.commonlibrary.server.ServiceFactory;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/27 0027
 * description:
 */
public class HttpResponseCode {

    /**
     * 执行成功
     */
    public static int SUCCESS = ServiceFactory.getInstance().getCommonService().getHttpSuccessCode();


    public static final int NODATAS = 0;

    /**
     * //执行失败
     */
    //用户未登录
//    public static final int USER_UNLOGIN = -2;
//    //用户在在其他设备登录
//    public static final int LOGIN_AT_ANORTHER1 = -37;
//    //用户在其他设备登录
//    public static final int LOGIN_AT_ANORTHER2 = -64;
//    //token失效
//    public static final int TOKEN_DISABLED = -48;
//    //服务器不可用
//    public static final int SERVER_UNAVAILABLE = -6;
    //未绑定手机
//    public static final int NOT_BIND_TEL = 250;
}
