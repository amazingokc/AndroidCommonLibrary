package com.runde.commonlibrary.global;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.runde.commonlibrary.base.BaseViewModel;
import com.runde.commonlibrary.bean.BaseUrlBean;
import com.runde.commonlibrary.event.EventBusUtil;
import com.runde.commonlibrary.event.LogoutEvent;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.SharePreUtil3;
import com.runde.commonlibrary.utils.SharePreferenceDebugUtil;
import com.runde.commonlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-07-08 下午 2:56
 * 文件描述：
 */
public class BaseUrlVm extends BaseViewModel {

    private List<BaseUrlBean> urlList;
    private String strCheckedBaseUrl;
    private String strCheckedBasePicUrl;

    public boolean isMatchLocalHost;//是否匹配到了本地写死的host

    public List<BaseUrlBean> getUrlList() {
        initDatas();
        return urlList;
    }

    private void initDatas() {
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        if (urlList.size() > 0) {
            urlList.clear();
        }

        urlList.add(new BaseUrlBean(BaseUrl.getOfficialBaseUrl(), false, BaseUrl.getOfficialBasePicUrl()));
        urlList.add(new BaseUrlBean(BaseUrl.getSandboxBaseUrl(), false, BaseUrl.getSandboxBasePicUrl()));
        urlList.addAll(ServiceFactory.getInstance().getCommonService().getTestHost());

        String strBaseUrl = BaseUrl.getBaseUrl();
        for (int i = 0; i < urlList.size(); i++) {
            if (urlList.get(i).getUrl().equals(strBaseUrl)) {
                urlList.get(i).setChecked(true);
                isMatchLocalHost = true;
                break;
            }
        }
    }

    public void recheckItem(BaseUrlBean baseUrlBean) {
        strCheckedBaseUrl = baseUrlBean.getUrl();
        strCheckedBasePicUrl = baseUrlBean.getPicUrl();
        for (int i = 0; i < urlList.size(); i++) {
            urlList.get(i).setChecked(false);
        }
        baseUrlBean.setChecked(true);
    }

    public void toChange(String customHost) {
        if (!TextUtils.isEmpty(customHost) && !"http://".equals(customHost)) {
            if (!customHost.startsWith("http")) {
                ToastUtil.showDebug("请检测你输入的地址格式");
                return;
            }
            strCheckedBaseUrl = customHost;
        }
        if (TextUtils.isEmpty(strCheckedBaseUrl)) {
            ToastUtil.showDebug("请选择！！！");
            return;
        }
        SharePreferenceDebugUtil.putString(SharePreferenceDebugUtil.BASE_URL_KEY, strCheckedBaseUrl);
        SharePreferenceDebugUtil.putString(SharePreferenceDebugUtil.BASE_PIC_URL_KEY, strCheckedBasePicUrl);
        //清空文件数据
        SharePreUtil3.clearPreference();
        EventBusUtil.postEvent(new LogoutEvent());
    }
}
