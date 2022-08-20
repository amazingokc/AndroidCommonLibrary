package com.core.commonlibrary.net;

import com.core.commonlibrary.R;
import com.core.commonlibrary.event.LogoutEvent;
import com.core.commonlibrary.global.ApplicationContext;
import com.core.commonlibrary.net.exception.ExceptionHandle;
import com.core.commonlibrary.net.exception.ResponseThrowable;
import com.core.commonlibrary.server.ServiceFactory;
import com.core.commonlibrary.utils.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/24 0024
 * description:
 */
public abstract class RdSubscriber implements Observer {

    private String requestType;

    public RdSubscriber(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        if (!NetworkUtil.isNetworkAvailable()) {
            onError(new ResponseThrowable(ApplicationContext.getString(R.string.statepage_no_network),
                    ExceptionHandle.ERROR.NETWORK_DISABLE));
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * @param o
     */
    @Override
    public void onNext(Object o) {
        if (o instanceof BaseResponse) {
            BaseResponse<Object> baseResponse = (BaseResponse) o;

            if (baseResponse.getCode() == HttpResponseCode.SUCCESS || baseResponse.getCode() == HttpResponseCode.NODATAS) {
                baseResponse.setRepType(requestType);
                onSuccess(baseResponse);
            } else if (ServiceFactory.getInstance().getCommonService().verifyIsLogout(baseResponse.getCode())) {
                EventBus.getDefault().post(new LogoutEvent(true, baseResponse.getMsg()));
            } else {
                baseResponse.setRepType(requestType);
                onFail(baseResponse);
            }
        } else {
            onFail(new BaseResponse<>(0, "数据异常", o, requestType));
        }
    }

    @Override
    public void onError(Throwable e) {
        ResponseThrowable responseThrowable = ExceptionHandle.handleException(e);
        onFail(new BaseResponse<>(responseThrowable.code,"数据异常", null, requestType));
    }

    protected abstract void onSuccess(BaseResponse<Object> data);

    protected abstract void onFail(BaseResponse<Object> data);

}
