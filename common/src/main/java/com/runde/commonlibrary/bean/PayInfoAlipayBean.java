package com.runde.commonlibrary.bean;

import java.io.Serializable;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-10-08 上午 11:36
 * 文件描述：
 */
public class PayInfoAlipayBean implements Serializable {

    private String orderString;

    public String getOrderString() {
        return orderString;
    }

    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }

    @Override
    public String toString() {
        return "PayInfoAlipayBean{" +
                "orderString='" + orderString + '\'' +
                '}';
    }
}
