package com.core.commonlibrary.bean;

import java.io.Serializable;

/**
 * +----------------------------------------------------------------------
 * | com.runde
 * +----------------------------------------------------------------------
 * | 功能描述:
 * +----------------------------------------------------------------------
 * | 时　　间: 2021/12/27.
 * +----------------------------------------------------------------------
 * | 代码创建: chenmingdu
 * +----------------------------------------------------------------------
 **/
public class SingleMsgParaBean implements Serializable {
    private Object Androird;
    private Object Android;

    public Object getAndroid() {
        if(Android!=null)return Android;
        return Androird;
    }

    public void setAndroid(Object android) {
        Android = android;
    }


    public void setAndroird(Object androird) {
        Androird = androird;
    }
}
