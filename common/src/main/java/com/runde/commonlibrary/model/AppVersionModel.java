package com.runde.commonlibrary.model;

import com.runde.commonlibrary.base.BaseModel;
import com.runde.commonlibrary.bean.AppVersionBean;
import com.runde.commonlibrary.net.rx.RxRestClient;
import com.runde.commonlibrary.utils.AppVersionUtil;
import com.runde.commonlibrary.utils.SystemUtils;

import io.reactivex.Observable;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-02-15 下午 1:57
 * 文件描述：
 */

public class AppVersionModel extends BaseModel {

    public Observable checkServerStatus(String url) {

        return makeObservableCommon(RxRestClient.builder()
                .url(url)
                .param("appPlatform", "Android")
                .param("appPatflam", "Android")
                .param("downPlatform", SystemUtils.getEnglishNameChannel())
                .headersParam("kys-version", AppVersionUtil.getVersion())
                .build()
                .post(), AppVersionBean.class);
    }
}
