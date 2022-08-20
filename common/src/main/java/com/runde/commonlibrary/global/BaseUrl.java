package com.runde.commonlibrary.global;

import android.text.TextUtils;

import com.runde.commonlibrary.RDLibrary;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.SharePreferenceDebugUtil;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/29 0029
 * description:
 */
public class BaseUrl {

    private final static String OFFICIAL_BASE_URL = ServiceFactory.getInstance()
            .getCommonService().getOfficialBaseUrl();//正式
    private final static String SANDBOX_BASE_URL = ServiceFactory.getInstance()
            .getCommonService().getSandboxBaseUrl();//沙盒测试

    //图片
    private final static String OFFICIAL_BASE_PIC_URL = ServiceFactory.getInstance()
            .getCommonService().getOfficialBasePicUrl();//正式
    private final static String SANDBOX_BASE_PIC_URL = ServiceFactory.getInstance()
            .getCommonService().getSandboxBasePicUrl();//沙盒测试

    private static String BASE_URL;
    private static String BASE_PIC_URL;

    public static String getOfficialBaseUrl() {
        return OFFICIAL_BASE_URL;
    }

    public static String getSandboxBaseUrl() {
        return SANDBOX_BASE_URL;
    }

    public static String getOfficialBasePicUrl() {
        return OFFICIAL_BASE_PIC_URL;
    }

    public static String getSandboxBasePicUrl() {
        return SANDBOX_BASE_PIC_URL;
    }

    public static String getPicUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                return url;
            } else {
                return getBasePicUrl() + url;
            }
        }
        return null;
    }

    public static String getUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                return url;
            } else {
                return getBaseUrl() + url;
            }
        }
        return null;
    }

    public static String getBaseUrl() {
        if (TextUtils.isEmpty(BASE_URL)) {
            if (RDLibrary.isDebug()) {
                BASE_URL = SharePreferenceDebugUtil.getString(SharePreferenceDebugUtil.BASE_URL_KEY,
                        SANDBOX_BASE_URL);
            } else {
                BASE_URL = OFFICIAL_BASE_URL;
            }
        }
        return BASE_URL;
    }

    private static String getBasePicUrl() {
        if (TextUtils.isEmpty(BASE_PIC_URL)) {
            if (RDLibrary.isDebug()) {
                BASE_PIC_URL = SharePreferenceDebugUtil.getString(SharePreferenceDebugUtil.BASE_PIC_URL_KEY,
                        SANDBOX_BASE_PIC_URL);
            } else {
                BASE_PIC_URL = OFFICIAL_BASE_PIC_URL;
            }
        }
        return BASE_PIC_URL;
    }


}
