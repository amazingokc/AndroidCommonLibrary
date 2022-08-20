package com.core.commonlibrary.event;

/**
 * 退出登录的event
 */
public class LogoutEvent {
    private boolean isPushBack;//是不是被逼退的
    private String msg;//

    public LogoutEvent() {
    }

    public String getMsg() {
        return msg;
    }

    public boolean isPushBack() {
        return isPushBack;
    }

    public LogoutEvent(boolean isPushBack, String msg) {
        this.isPushBack = isPushBack;
        this.msg = msg;
    }
}
