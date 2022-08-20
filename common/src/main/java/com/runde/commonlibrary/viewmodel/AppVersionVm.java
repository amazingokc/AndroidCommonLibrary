package com.runde.commonlibrary.viewmodel;

import android.text.TextUtils;

import com.runde.commonlibrary.base.BaseViewModel;
import com.runde.commonlibrary.bean.AppVersionBean;
import com.runde.commonlibrary.constants.BaseCommonApiContants;
import com.runde.commonlibrary.model.AppVersionModel;
import com.runde.commonlibrary.utils.AppVersionUtil;
import com.runde.commonlibrary.utils.CommonUtil;
import com.runde.commonlibrary.utils.LLog;
import com.runde.commonlibrary.utils.SharePreUtil3;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-02-15 下午 1:58
 * 文件描述：
 */

public class AppVersionVm extends BaseViewModel {

    private AppVersionModel appVersionModel;

    public AppVersionVm() {
        appVersionModel = new AppVersionModel();
    }

    @SuppressWarnings("unchecked")
    public void getAppVersion() {
        appVersionModel.checkServerStatus(BaseCommonApiContants.URL_HTTP_APPVERSIONINFO)
                .subscribe(createObserver(BaseCommonApiContants.URL_HTTP_APPVERSIONINFO));
    }

    public static final String SUGGEST_UPDATE = "SUGGEST_UPDATE";               //建议更新版本
    public static final String FORCE_UPDATE = "FORCE_UPDATE";                   //强制更新版本
    public static final String LOCAL_EQUAL_REMOTE = "LOCAL_EQUAL_REMOTE";       //当前版本等于服务器版本
    public static final String LOCAL_SUPER_REMOTE = "LOCAL_SUPER_REMOTE";       //当前版本大于于服务器版本

    @Override
    protected void onSuccess(int responCode, String responType, Object data) {
        if (data instanceof AppVersionBean) {
            super.onSuccess(responCode, responType, data);
            AppVersionBean appVersionBean = (AppVersionBean) data;
            SharePreUtil3.putString(
                    SharePreUtil3.APP_KEY,
                    appVersionBean.getCurrentVersionAppKey()
            );
            CommonUtil.mAppKey = appVersionBean.getCurrentVersionAppKey();
            if (!TextUtils.isEmpty(appVersionBean.getLastestVersion().getAppVersionCode())) {
                try {
                    //取前8位比较
                    String versionCode = appVersionBean.getLastestVersion().getAppVersionCode();
                    if (versionCode.length() >= 9) {
                        versionCode = versionCode.substring(0, 8);
                    }
                    String localVersionCodetmp ;
                    if (String.valueOf(AppVersionUtil.getVersionCode()).length() >= 8)
                        localVersionCodetmp = String.valueOf(AppVersionUtil.getVersionCode()).substring(0, 8);
                    else
                        localVersionCodetmp = String.valueOf(AppVersionUtil.getVersionCode());
                    long appVersionCode = Long.parseLong(versionCode);
                    long localVersionCode = Long.parseLong(localVersionCodetmp);
                    if (localVersionCode < appVersionCode) {
                        if (AppVersionUtil.getVersionCode() < Long.parseLong(appVersionBean.getLastestVersion().getMinimumVersion())) {//强制更新版本
                            super.onSuccess(responCode, FORCE_UPDATE, data);
                        } else {//建议更新版本
                            super.onSuccess(responCode, SUGGEST_UPDATE, data);
                        }
                    } else if (AppVersionUtil.getVersionCode() == appVersionCode) {
                        super.onSuccess(responCode, LOCAL_EQUAL_REMOTE, data);
                    } else {
                        super.onSuccess(responCode, LOCAL_SUPER_REMOTE, data);
                    }
                } catch (Exception e) {
                    LLog.d("版本信息数据异常");
                    super.onFail(responCode, responType, "版本信息数据异常", data);
                }
            } else {
                super.onFail(responCode, responType, "版本信息数据异常", data);
            }
        } else {
            super.onFail(responCode, responType, "版本信息数据异常", data);
        }
    }

    @Override
    protected void onFail(int responCode, String responType, String message, Object data) {
        super.onFail(responCode, responType, message, data);
    }
}
