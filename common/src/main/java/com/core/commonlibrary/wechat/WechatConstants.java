package com.core.commonlibrary.wechat;


import android.text.TextUtils;

import com.core.commonlibrary.server.ServiceFactory;
import com.core.commonlibrary.utils.ToastUtil;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-03-05 上午 11:34
 * 文件描述：微信相关的常量
 */

public class WechatConstants {

    private static final String scope = "snsapi_userinfo";
    private static final String state = "WechatConstants";
    public static final String BIND_WX = "bindWx";
    public static final String LOGIN_WX = "loginWx";

    private static final String WXAppId = ServiceFactory.getInstance().getCommonService().getWXAppId();
    //    private static final String WXAppSecret = ServiceFactory.getInstance().getCommonService().getWXAppSecret();

    public static final String EXTDATA_PAYORDER = "EXTDATA_PAYORDER";   //订单支付
    public static final String EXTDATA_PAYGIVEAREWARD = "EXTDATA_PAYGIVEAREWARD";//打赏支付
    public static final String EXTDATA_PAYLIVEGIFT = "LiveGift";//礼物支付
    public static final String EXTDATA_PAYMOCK = "EXTDATA_PAYMOCK"; //模考大赛支付

    public static String getWXAppId() {
        if (TextUtils.isEmpty(WXAppId)) {
            ToastUtil.showDebug("请实现ICommonService类，并通过实现类提供WXAppId");
        }
        return WXAppId;
    }

    public static String getScope() {
        return scope;
    }


    public static String getState() {
        return state;
    }

}
