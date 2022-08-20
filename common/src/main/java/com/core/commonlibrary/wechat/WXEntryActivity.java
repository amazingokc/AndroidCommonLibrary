package com.core.commonlibrary.wechat;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.commonlibrary.R;
import com.core.commonlibrary.constants.BaseRouterPathConstants;
import com.core.commonlibrary.wechat.event.WechatCodeEvent;
import com.core.commonlibrary.wechat.event.WxToActivityEvent;
import com.core.commonlibrary.global.AppManager;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        IWXAPI api = WXAPIFactory.createWXAPI(this, WechatConstants.getWXAppId(), false);
        api.handleIntent(getIntent(), this);
    }

    //微信请求第三方应用回调
    @Override
    public void onReq(BaseReq req) {
        //获取开放标签传递的extinfo数据逻辑
        if (req.getType() == ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX && req instanceof ShowMessageFromWX.Req) {
            ShowMessageFromWX.Req showReq = (ShowMessageFromWX.Req) req;
            WXMediaMessage mediaMsg = showReq.message;
            String extInfo = mediaMsg.messageExt;
            toActivity(extInfo);
        }

    }

    private void toActivity(String extInfo) {
        //Handle...处理跳转
        //是否在后台
        if (AppManager.getAppManager().getActivityStack().size() < 2)
            ARouter.getInstance().build(BaseRouterPathConstants.SPLASH_ACTIVITY)
                    .withString("extInfo", extInfo).navigation();//启动app
        EventBus.getDefault().postSticky(new WxToActivityEvent(extInfo));
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
        finish();
    }

    //微信响应第三方应用回调
    @Override
    public void onResp(BaseResp resp) {
        String result = null;
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH &&
                WechatConstants.getState().equals(((SendAuth.Resp) resp).state)) {
            ////获取微信code回调（微信登录功能）
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "成功";
                    if (((SendAuth.Resp) resp).code != null) {
                        EventBus.getDefault().post(new WechatCodeEvent(((SendAuth.Resp) resp).code, resp.transaction));
                        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
                        finish();
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "登录已取消";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "登录被拒绝";
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    result = "登录失败";
                    break;
                default:
            }
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享回调
            //这部分逻辑迁移到ShareUtil工具类了
        } else if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            toActivity(extraData);
        }
        finish();
    }
}
