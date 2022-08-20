package com.runde.commonlibrary.wechat.event;

/**
 * Author:Zheng Meixiong
 * Time:2020/9/16   14:05
 * Des:
 */
public class WxToActivityEvent {
    private String extInfo;

    public WxToActivityEvent(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
}
