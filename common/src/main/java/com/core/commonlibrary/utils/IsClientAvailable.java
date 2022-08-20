package com.core.commonlibrary.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.core.commonlibrary.global.ApplicationContext;
import com.core.commonlibrary.wechat.WechatConstants;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;


public class IsClientAvailable {

    /**
     * 判断微信是否安装
     *
     * @return
     */
    public static boolean isWeixinAvilible() {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(ApplicationContext.getContext(), WechatConstants.getWXAppId());
        return iwxapi.isWXAppInstalled();
        //        try {
        //            final PackageManager packageManager = ApplicationContext.getContext().getPackageManager();// 获取packagemanager
        //            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        //            if (pinfo != null) {
        //                for (int i = 0; i < pinfo.size(); i++) {
        //                    String pn = pinfo.get(i).packageName;
        //                    if (pn.equals("com.tencent.mm")) {
        //                        return true;
        //                    }
        //                }
        //            }
        //            return false;
        //        } catch (Exception e) {
        //            return true;
        //        }
    }

    public static boolean isAlipayAvilible() {
        try {
            final PackageManager packageManager = ApplicationContext.getContext().getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.eg.android.AlipayGphone")) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
