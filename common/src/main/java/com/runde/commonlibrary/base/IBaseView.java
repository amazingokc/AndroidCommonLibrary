package com.runde.commonlibrary.base;

import androidx.annotation.Nullable;

import com.runde.commonlibrary.net.BaseResponse;

public interface IBaseView {

    /**
     * 初始化view
     */
    void initViews();

    /**
     * 初始化界面传递参数
     */
    void initParams();

    @Deprecated()
    void onEnterAnimationEnds();

    void initDatas();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();

    void registerViewModelObserver(BaseViewModel baseViewModel);

    void registerViewModelObserver(BaseViewModelV2 baseViewModel);

    /**
     * 数据请求的成功回调
     *
     * @param respondCode 响应码
     * @param respondType 接口名
     * @param data        View层需要的数据（需要强转数据类型）
     */
    void onApiSuccessCallBack(int respondCode, @Nullable String respondType, @Nullable Object data);

    void onApiSuccessCallBack(BaseResponse<Object> baseResponse);


    /**
     * 数据请求的错误回调
     *
     * @param respondCode 响应码
     * @param respondType 接口名
     * @param message     错误描述
     * @param data
     */
    void onApiErrorCallBack(int respondCode, @Nullable String respondType, @Nullable String message, @Nullable Object data);

    void onApiErrorCallBack(BaseResponse<Object> baseResponse);

}
