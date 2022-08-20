package com.runde.commonlibrary.model;

import com.runde.commonlibrary.base.BaseModel;
import com.runde.commonlibrary.bean.GetUserPhoneBean;
import com.runde.commonlibrary.net.rx.RxRestClient;

import io.reactivex.Observable;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-02-15 下午 1:57
 * 文件描述：
 */

public class GetUserPhoneModel extends BaseModel {

    public void getUserPhone(String url,String APPID,String APPKEY,String msgId,String type) {

        makeObservableCommon(RxRestClient.builder()
                .url(url)
                .param("account", APPID)
                .param("passWord", APPKEY)
                .param("msgId", msgId)
                .param("type", type)
                .build()
                .post(), GetUserPhoneBean.class);
    }
}
