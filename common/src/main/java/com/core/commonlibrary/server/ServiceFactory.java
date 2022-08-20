package com.core.commonlibrary.server;

import com.core.commonlibrary.server.empty_service.EmptyCommonService;
import com.core.commonlibrary.server.service.ICommonService;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-09-25 下午 6:49

 */
public class ServiceFactory {
    /**
     * 禁止外部创建 ServiceFactory 对象
     */
    private ServiceFactory() {
    }

    /**
     * 通过静态内部类方式实现 ServiceFactory 的单例
     */
    public static ServiceFactory getInstance() {
        return Inner.serviceFactory;
    }

    private static class Inner {
        private static ServiceFactory serviceFactory = new ServiceFactory();
    }

    private ICommonService commonService;

    public ICommonService getCommonService() {
        if (commonService == null) {
            commonService = new EmptyCommonService();
        }
        return commonService;
    }

    public void setCommonService(ICommonService iCommonService) {
        this.commonService = iCommonService;
    }

}
