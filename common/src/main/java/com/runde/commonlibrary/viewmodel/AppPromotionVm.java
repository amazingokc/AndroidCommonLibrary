package com.runde.commonlibrary.viewmodel;

import com.runde.commonlibrary.base.BaseViewModel;
import com.runde.commonlibrary.constants.BaseCommonApiContants;
import com.runde.commonlibrary.model.AppPromotionModel;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-02-15 下午 1:58
 * 文件描述：
 */

public class AppPromotionVm extends BaseViewModel {

    private AppPromotionModel appVersionModel;

    public AppPromotionVm() {
        appVersionModel = new AppPromotionModel();
        subscribe(appVersionModel);
    }

    /**
     * 上传活跃度
     *
     * @param isEnter 是不是进入app
     */
    public void postLiveness(boolean isEnter) {
        appVersionModel.postLiveness(isEnter);
    }

    @Override
    protected void onSuccess(int responCode, String responType, Object data) {
        if (BaseCommonApiContants.URL_POST_LIVENESS.equals(responType)) {

        } else {
            super.onSuccess(responCode, responType, data);
        }

    }
}
