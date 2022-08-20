package com.core.commonlibrary.net.deprecated_net.interceptors.interceptors.logging;

import okhttp3.internal.platform.Platform;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/19 0019
 * description:
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface Logger {
    void log(int level, String tag, String msg);

    Logger DEFAULT = new Logger() {
        @Override
        public void log(int level, String tag, String message) {
            Platform.get().log(message, level, null);
        }
    };
}
