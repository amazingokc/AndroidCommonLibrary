package com.core.commonlibrary.event;

/**
 * 没有权限使用app的event
 */
public class NoUseAppPermissionsEvent {
    public String msg;//是不是被逼退的

    public NoUseAppPermissionsEvent() {
    }

    public NoUseAppPermissionsEvent(String msg) {
        this.msg = msg;
    }
}
