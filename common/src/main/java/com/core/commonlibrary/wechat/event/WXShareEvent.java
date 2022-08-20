package com.core.commonlibrary.wechat.event;

import com.core.commonlibrary.event.EventBusUtil;

/**
 * desc: 微信分享
 * date:2021/8/2
 * by:xiaoguoqing
 */
public class WXShareEvent {

    private String mTransaction;

    public WXShareEvent(String transaction) {
        mTransaction = transaction;
    }

    public static void post(String transaction) {
        EventBusUtil.postEvent(new WXShareEvent(transaction));
    }

    public String getTransaction() {
        return mTransaction;
    }

}
