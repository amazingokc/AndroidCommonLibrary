package com.core.commonlibrary.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * 调试工具类 (调试模式下：日志将被打印)
 */
public class LLog {
    static {
        //初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 打印verbose级别的日志
     *
     * @param msg 日志内容
     */
    public static void v(Object msg) {
        Logger.v(adjustMsg(msg));
    }

    /**
     * 打印verbose级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     */
    public static void v(String tag, Object msg) {
        Logger.t(tag);
        Logger.v(adjustMsg(msg));
    }

    /**
     * 打印verbose级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     * @param e   异常对象
     */
    public static void v(String tag, Object msg, Throwable e) {
        Logger.t(tag);
        Logger.v(adjustMsg(msg) + adjustException(e));
    }

    /**
     * 打印debug级别的日志
     *
     * @param msg 日志内容
     */
    public static void d(Object msg) {
        Logger.d(adjustMsg(msg));
    }

    /**
     * 打印debug级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     */
    public static void d(String tag, Object msg) {
        Logger.t(tag);
        Logger.d(adjustMsg(msg));
    }

    public static void d(String tag, Object msg, Throwable e) {
        Logger.t(tag);
        Logger.d(adjustMsg(msg) + adjustException(e));
    }


    /**
     * 打印info级别的日志
     *
     * @param msg 日志内容
     */
    public static void i(Object msg) {
        Logger.i(adjustMsg(msg));
    }

    /**
     * 打印info级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     */
    public static void i(String tag, Object msg) {
        Logger.t(tag);
        Logger.i(adjustMsg(msg));
    }

    /**
     * 打印warn级别的日志
     *
     * @param msg 日志内容
     */
    public static void w(Object msg) {
        Logger.w(adjustMsg(msg));
    }

    /**
     * 打印warn级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     */
    public static void w(String tag, Object msg) {
        Logger.t(tag);
        Logger.w(adjustMsg(msg));
    }

    /**
     * 打印warn级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     * @param e   异常对象
     */
    public static void w(String tag, Object msg, Throwable e) {
        Logger.t(tag);
        Logger.w(adjustMsg(msg) + adjustException(e));
    }

    /**
     * 打印error级别的日志
     *
     * @param msg 日志内容
     */
    public static void e(Object msg) {
        Logger.e(adjustMsg(msg));
    }

    /**
     * 打印error级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     */
    public static void e(String tag, Object msg) {
        Logger.t(tag);
        Logger.e(adjustMsg(msg));
    }

    /**
     * 打印error级别的日志
     *
     * @param tag 显示标签
     * @param msg 日志内容
     * @param e   异常对象
     */
    public static void e(String tag, Object msg, Throwable e) {
        Logger.t(tag);
        Logger.e(adjustMsg(msg) + adjustException(e));
    }

    private static String adjustMsg(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return msg.toString();
    }

    private static String adjustException(Throwable throwable) {
        if (throwable == null) {
           return "\nException:null";
        } else {
            return "\nException:" + throwable.getMessage();
        }
    }
}
