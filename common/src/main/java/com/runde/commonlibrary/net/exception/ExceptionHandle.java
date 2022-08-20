package com.runde.commonlibrary.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.runde.commonlibrary.R;
import com.runde.commonlibrary.global.ApplicationContext;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.HttpException;


/**
 * Create by: xiaoguoqing
 * Date: 2018/12/27 0027
 * description: http异常处理
 */
public class ExceptionHandle {

    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case BAD_REQUEST:
                    ex.message = "错误请求" + httpException.code();
                    break;
                case UNAUTHORIZED:
                    ex.message = "操作未授权" + httpException.code();
                    break;
                case FORBIDDEN:
                    ex.message = "请求被拒绝" + httpException.code();
                    break;
                case NOT_FOUND:
                    ex.message = "资源不存在" + httpException.code();
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = "服务器执行超时" + httpException.code();
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = "服务器内部错误" + httpException.code();
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = "服务器不可用" + httpException.code();
                    break;
                default:
                    ex.message = "网络错误" + httpException.code();
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException
                || e instanceof NullPointerException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "数据解析错误" + "(" + e.getMessage() + ")";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = ApplicationContext.getString(R.string.statepage_no_network);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时，请检查网络情况";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时，请检查网络情况";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "主机地址未知，请检查网络情况";
            return ex;
        } else if (e instanceof ResponseThrowable) {
            return (ResponseThrowable) e;
        } else if (e instanceof  IOException ) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "当前网络不可用，请更换网络或检测网络状态";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "(" + e.getMessage() + ")";
            return ex;
        }
    }


    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        public static final int NETWORK_DISABLE = 1007;
    }

}

