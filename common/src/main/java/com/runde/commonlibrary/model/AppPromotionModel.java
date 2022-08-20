package com.runde.commonlibrary.model;

import com.runde.commonlibrary.base.BaseModel;
import com.runde.commonlibrary.constants.BaseCommonApiContants;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.net.rx.RxRestClient;
import com.runde.commonlibrary.utils.SystemUtils;

public class AppPromotionModel extends BaseModel {

    public void postLiveness(boolean isEnter) {
        getRemoteDatas(RxRestClient.builder()
                        .url(BaseCommonApiContants.URL_POST_LIVENESS)
                        .param("type", isEnter ? 0 : 1)
                        .build()
                        .post()
                , Object.class, BaseCommonApiContants.URL_POST_LIVENESS);
    }

}
