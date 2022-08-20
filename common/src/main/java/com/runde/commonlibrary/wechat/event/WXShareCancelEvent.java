package com.runde.commonlibrary.wechat.event;

import com.runde.commonlibrary.event.EventBusUtil;

/**
 * desc: 取消微信分享弹窗
 */
public class WXShareCancelEvent {

    private String mTransaction;

    public WXShareCancelEvent(String transaction) {
        mTransaction = transaction;
    }

    public static void post(String transaction) {
        EventBusUtil.postEvent(new WXShareCancelEvent(transaction));
    }

    public String getTransaction() {
        return mTransaction;
    }

}
