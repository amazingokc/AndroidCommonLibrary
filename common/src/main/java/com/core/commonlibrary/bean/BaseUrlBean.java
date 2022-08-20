package com.core.commonlibrary.bean;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-07-08 下午 2:47
 * 文件描述：
 */
public class BaseUrlBean {

    public BaseUrlBean(String url, boolean isChecked, String picUrl) {
        this.url = url;
        this.isChecked = isChecked;
        this.picUrl = picUrl;
    }

    private String url;
    private String picUrl;

    private boolean isChecked;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
