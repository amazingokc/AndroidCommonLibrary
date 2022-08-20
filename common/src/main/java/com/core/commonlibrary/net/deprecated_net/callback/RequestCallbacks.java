package com.core.commonlibrary.net.deprecated_net.callback;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.core.commonlibrary.event.LogoutEvent;
import com.core.commonlibrary.global.AppManager;
import com.core.commonlibrary.net.exception.ExceptionHandle;
import com.core.commonlibrary.net.exception.ResponseThrowable;
import com.core.commonlibrary.server.ServiceFactory;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class RequestCallbacks implements Callback<String> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    //    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;

    public RequestCallbacks(IRequest request, ISuccess success, IFailure failure, IError error, Context context) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.CONTEXT = context;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    JSONObject object = null;
                    try {
                        object = JSON.parseObject(response.body());
                    } catch (Exception e) {
                        ERROR.onError(response.code(), "网络不可用");
                    }
                    if (object != null) {
                        if (object.getInteger("code") != null) {//在其他设备登录
                            if (ServiceFactory.getInstance().getCommonService().verifyIsLogout(object.getInteger("code"))) {
                                if (AppManager.getAppManager().isForeground()) {
                                    EventBus.getDefault().post(new LogoutEvent(true,""));
                                }
                            } else {
                                SUCCESS.onSuccess(response.body());
                            }
                        } else {
                            //这部分逻辑是有些接口返回的码字段是respCode而不是code，后台返回码不统一造成逻辑不够清晰
                            SUCCESS.onSuccess(response.body());
                        }
                    }
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.toString());
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable e) {
        ResponseThrowable responseThrowable = ExceptionHandle.handleException(e);
        if (FAILURE != null) {
            FAILURE.onFailure(responseThrowable.getMessage());
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }

    }

}
