package com.core.commonlibrary.wechat.event;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-03-05 上午 11:14
 * 文件描述：
 */

public class WechatCodeEvent {

    private String code;
    private String transaction;

    public WechatCodeEvent(String code, String transaction) {
        this.code = code;
        this.transaction = transaction;
    }

    public WechatCodeEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getTransaction() {
        return transaction;
    }
}
