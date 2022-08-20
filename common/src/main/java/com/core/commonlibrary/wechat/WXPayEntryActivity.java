package com.core.commonlibrary.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.core.commonlibrary.wechat.event.PayEvent;
import com.core.commonlibrary.utils.LLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, com.core.commonlibrary.wechat.WechatConstants.getWXAppId());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);


    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        LLog.d("微信支付回调", "" + resp.toString() + "\n" + resp.getType()
                + "\n" + ((PayResp) resp).extData + "\n" + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                EventBus.getDefault().post(new PayEvent(true, ((PayResp) resp).extData, ""));
            } else {
                EventBus.getDefault().post(new PayEvent(false, ((PayResp) resp).extData, resp.errStr));
            }
            finish();
        }
    }

}
