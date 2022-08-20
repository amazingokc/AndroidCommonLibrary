package com.core.commonlibrary.net.exception;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/27 0027
 * description:
 */
public class ResponseThrowable extends Exception {
    public int code;
    public String message;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ResponseThrowable(String message, int code) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseThrowable{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
