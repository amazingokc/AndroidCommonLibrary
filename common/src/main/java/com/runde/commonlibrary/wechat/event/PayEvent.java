package com.runde.commonlibrary.wechat.event;

;

/**
 * Created by zhengmeixiong on 2018-03-10.
 */
public class PayEvent {
    private boolean isSuccess;//是否支付成功
    private String payType;//支付的商品类型
    private String errStr;//失败原因

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrStr() {
        return errStr;
    }

    public PayEvent(boolean isSuccess, String payType, String errStr) {
        this.isSuccess = isSuccess;
        this.payType = payType;
        this.errStr = errStr;
    }

}
