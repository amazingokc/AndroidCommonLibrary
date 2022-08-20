package com.core.commonlibrary.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.core.commonlibrary.interfaces.DataListener;
import com.core.commonlibrary.net.BaseResponse;
import com.core.commonlibrary.net.RdSubscriber;

import io.reactivex.Observer;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/24 0024
 * description: 各项目尽量写一个公共ViewModel来继承该类，不要直接用具体的ViewModel继承该类
 * 改为使用BaseViewModelV2
 */
@Deprecated()
public abstract class BaseViewModel extends ViewModel {

    private MutableLiveData<BaseResponse<Object>> successLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponse<Object>> errorLiveData = new MutableLiveData<>();

    @Deprecated
    @NonNull
    protected Observer createObserver(String requestType) {
        return new RdSubscriber(requestType) {
            @Override
            protected void onSuccess(BaseResponse<Object> data) {
                BaseViewModel.this.onSuccess(data.getCode(), data.getRepType(), data.getData());
            }

            @Override
            protected void onFail(BaseResponse<Object> data) {
                BaseViewModel.this.onFail(data.getCode(), data.getRepType(), data.getMsg(), data.getData());
            }
        };
    }

    protected void onSuccess(int responCode, String responType, Object data) {
        BaseResponse<Object> successData = new BaseResponse<>();
        successData.setCode(responCode);
        successData.setData(data);
        successData.setRepType(responType);
        successLiveData.setValue(successData);
    }

    protected void onPostSuccess(int responCode, String responType, Object data) {
        BaseResponse<Object> successData = new BaseResponse<>();
        successData.setCode(responCode);
        successData.setData(data);
        successData.setRepType(responType);
        successLiveData.postValue(successData);
    }


    protected void onFail(int responCode, String responType, String message, Object data) {
        BaseResponse<Object> errorData = new BaseResponse<>();
        errorData.setCode(responCode);
        errorData.setMsg(message);
        errorData.setRepType(responType);
        errorData.setData(data);
        errorLiveData.setValue(errorData);
    }

    public MutableLiveData<BaseResponse<Object>> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<BaseResponse<Object>> getSuccessLiveData() {
        return successLiveData;
    }

    protected void subscribe(BaseModel baseModel) {
        baseModel.setDataListener(new DataListener() {
            @Override
            public void getFailed(@NonNull BaseResponse<Object> baseResponse) {
                onFail(baseResponse.getCode(), baseResponse.getRepType(), baseResponse.getMsg(),
                        baseResponse.getData());
            }

            @Override
            public void getSuccess(@NonNull BaseResponse<Object> baseResponse) {
                onSuccess(baseResponse.getCode(), baseResponse.getRepType(), baseResponse.getData());
            }

        });
    }

}
