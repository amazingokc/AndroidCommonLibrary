package com.runde.commonlibrary.wechat;

import android.content.Context;

import com.runde.commonlibrary.utils.IsClientAvailable;
import com.runde.commonlibrary.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WechatUtil {

    public static void bindWeixin(Context context) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = WechatConstants.getScope();
        req.state = WechatConstants.getState();
        req.transaction = WechatConstants.BIND_WX;
        IWXAPI api = WXAPIFactory.createWXAPI(context, WechatConstants.getWXAppId());
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("请安装微信应用");
            return;
        }
        api.sendReq(req);

    }


    public static void toLoginWithWX(Context context) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = WechatConstants.getScope();
        req.state = WechatConstants.getState();
        req.transaction = WechatConstants.LOGIN_WX;
        IWXAPI api = WXAPIFactory.createWXAPI(context, WechatConstants.getWXAppId());
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("请安装微信应用");
            return;
        }
        api.sendReq(req);
    }
}
