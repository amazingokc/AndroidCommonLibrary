package com.core.commonlibrary.bean;

import java.io.Serializable;

/**
 */
public class GetUserPhoneBean implements Serializable {

    public String phone;

    public GetUserPhoneBean(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
