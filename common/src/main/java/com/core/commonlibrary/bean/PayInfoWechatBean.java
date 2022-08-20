package com.core.commonlibrary.bean;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-03-27 上午 11:25
 * 文件描述：
 */

public class PayInfoWechatBean {


    /**
     * timeStamp : 1553657051
     * serialVersionUID : 1
     * money : 0.88
     * partner : 1488230462
     * appId : wxb777cf502edc3d9a
     * WIDbody : 考药师相关教学资料
     * finalsign : B3C9B177BA857E6CA7D30F84DE5A7C93
     * packages : Sign=WXPay
     * wxpaytype : APP
     * nonceStr : 104715,190327112410464
     * prepay_id : wx27112411269314de43c8bac10544566900
     */

    private String timeStamp;
    private String serialVersionUID;
    private String money;
    private String partner;
    private String appId;
    private String WIDbody;
    private String finalsign;
    private String packages;
    private String wxpaytype;
    private String nonceStr;
    private String prepay_id;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(String serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getWIDbody() {
        return WIDbody;
    }

    public void setWIDbody(String WIDbody) {
        this.WIDbody = WIDbody;
    }

    public String getFinalsign() {
        return finalsign;
    }

    public void setFinalsign(String finalsign) {
        this.finalsign = finalsign;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getWxpaytype() {
        return wxpaytype;
    }

    public void setWxpaytype(String wxpaytype) {
        this.wxpaytype = wxpaytype;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    @Override
    public String toString() {
        return "PayInfoWechatBean{" +
                "timeStamp='" + timeStamp + '\'' +
                ", serialVersionUID='" + serialVersionUID + '\'' +
                ", money='" + money + '\'' +
                ", partner='" + partner + '\'' +
                ", appId='" + appId + '\'' +
                ", WIDbody='" + WIDbody + '\'' +
                ", finalsign='" + finalsign + '\'' +
                ", packages='" + packages + '\'' +
                ", wxpaytype='" + wxpaytype + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
