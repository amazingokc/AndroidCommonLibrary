package com.core.commonlibrary.bean;

import java.io.Serializable;

/**
 * Created by hua on 2019/8/7 0007.
 */
public class ShareInfoBean implements Serializable {

    public String url;
    public String title;
    public String desc;
    public String imgUrl;
    public String sharePlaformOption;
    public String appName = "kaoyaoshi";
    public String transactionId ;    //分享标志，用于区分分享事件

    public ShareInfoBean() {
    }

    public ShareInfoBean(String url, String title, String desc, String imgUrl, String transactionId) {
        this.url = url;
        this.title = title;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.transactionId = transactionId;
    }


    public String getSharePlaformOption() {
        return sharePlaformOption;
    }

    public void setSharePlaformOption(String sharePlaformOption) {
        this.sharePlaformOption = sharePlaformOption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


}
