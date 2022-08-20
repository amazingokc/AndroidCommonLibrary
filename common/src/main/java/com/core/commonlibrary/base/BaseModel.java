package com.core.commonlibrary.base;


import androidx.annotation.NonNull;

import com.core.commonlibrary.R;
import com.core.commonlibrary.global.ApplicationContext;
import com.core.commonlibrary.interfaces.DataListener;
import com.core.commonlibrary.net.BaseResponse;
import com.core.commonlibrary.net.RdSubscriber;
import com.core.commonlibrary.net.exception.ExceptionHandle;
import com.core.commonlibrary.net.exception.ResponseThrowable;
import com.core.commonlibrary.net.reflect.TypeBuilder;
import com.core.commonlibrary.utils.GsonUtil;
import com.core.commonlibrary.utils.NetworkUtil;

import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/24 0024
 * description:
 */
public class BaseModel {

    @Deprecated
    private Observable makeObservable(Observable observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Deprecated
    protected <T> Observable makeObservableCommon(Observable observable, final Class<T> clazz) {
        return makeObservable(observable.map(new Function<ResponseBody, BaseResponse<T>>() {
            @Override
            public BaseResponse<T> apply(ResponseBody o) throws Exception {
                //                return GsonUtil.fromJson(o.string(), TypeBuilder
                //                        .newInstance(BaseResponse.class)
                //                        .addTypeParam(clazz)
                //                        .build());

                return handleBaseResponse(o, clazz);
            }
        }));
    }

    protected  Observable makeObservableCommon(Observable observable) {
        return makeObservable(observable.map(new Function<ResponseBody, InputStream>() {
            @Override
            public InputStream apply(ResponseBody o) throws Exception {
                return o.byteStream();
            }
        }));
    }


    /**
     * 待完善
     ****************************************************************************************************************/

    //设置缓存策略
    protected BaseModel setCacheType() {
        return this;
    }

    //这个方法只能解析data字段为JSONObject，如果data为JSONArray请使用getRemoteDatasV2方法
    @NonNull
    @SuppressWarnings("unchecked")
    protected <T> void getRemoteDatas(Observable observable, final Class<T> clazz, String requestType) {
        Observable observable1 = observable
                .map(new Function<ResponseBody, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(ResponseBody responseBody) throws Exception {
                        return handleBaseResponse(responseBody, clazz);
                    }
                }).doOnNext(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> baseResponse) {
                        // TODO: 2019/7/31 0031 可以在这里将数据保存到本地
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        observable1.subscribe(new RdSubscriber(requestType) {
            @Override
            protected void onSuccess(BaseResponse<Object> baseResponse) {
                if (dataListener != null) {
                    dataListener.getSuccess(baseResponse);
                }
            }

            @Override
            protected void onFail(BaseResponse<Object> baseResponse) {
                if (dataListener != null) {
                    dataListener.getFailed(baseResponse);
                }
            }
        });

    }

    public interface OnRequestDoOnNextListener<T> {
        void doOnNext(BaseResponse<T> baseResponse);
    }


    @NonNull
    @SuppressWarnings("unchecked")
    protected <T> void getRemoteDatasNeedDoNext(Observable observable, final Class<T> clazz, String requestType, OnRequestDoOnNextListener onRequestDoOnNextListener) {
        Observable observable1 = observable
                .map(new Function<ResponseBody, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(ResponseBody responseBody) throws Exception {
                        return handleBaseResponse(responseBody, clazz);
                    }
                }).doOnNext(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> baseResponse) {
                        if (onRequestDoOnNextListener != null)
                            onRequestDoOnNextListener.doOnNext(baseResponse);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        observable1.subscribe(new RdSubscriber(requestType) {
            @Override
            protected void onSuccess(BaseResponse<Object> baseResponse) {
                if (dataListener != null) {
                    dataListener.getSuccess(baseResponse);
                }
            }

            @Override
            protected void onFail(BaseResponse<Object> baseResponse) {
                if (dataListener != null) {
                    dataListener.getFailed(baseResponse);
                }
            }
        });
    }

    @SuppressWarnings("all")
    protected <T> BaseResponse<T> handleBaseResponse(ResponseBody responseBody, Class<T> clazz) throws Exception {
        BaseResponse baseResponse = null;
        String responseBodyString = responseBody.string();
        baseResponse = GsonUtil.fromJson(responseBodyString, TypeBuilder
                .newInstance(BaseResponse.class)
                .addTypeParam(clazz)
                .build());

        return baseResponse;
    }

    //该方法可以处理data字段的JSONObject和JSONArray类型
    @SuppressWarnings("all")
    protected <T extends BaseResponse> void getRemoteDatasV2(
            Observable observable, final Class<T> clazz, String requestType) {
        Observable observable1 = observable
                .map(new Function<ResponseBody, BaseResponse>() {
                    @Override
                    public BaseResponse apply(ResponseBody responseBody) throws Exception {
                        return handleBaseResponseV2(responseBody.string(), clazz);
                    }
                }).doOnNext(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> baseResponse) {
                        // TODO: 2019/7/31 0031 可以在这里将数据保存到本地
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        observable1.subscribe(new RdSubscriber(requestType) {
            @Override
            protected void onSuccess(BaseResponse baseResponse) {
                if (dataListener != null) {
                    dataListener.getSuccess(baseResponse);
                }
            }

            @Override
            protected void onFail(BaseResponse baseResponse) {
                if (dataListener != null) {
                    dataListener.getFailed(baseResponse);
                }
            }
        });
    }

    @SuppressWarnings("all")
    private <T extends BaseResponse> BaseResponse handleBaseResponseV2(
            String responseBodyString, Class<T> clazz) throws Exception {
        BaseResponse baseResponse = null;
        //        String responseBodyString = responseBody.string();
        baseResponse = GsonUtil.fromJson(responseBodyString, clazz);

        return baseResponse;
    }

    protected <T extends BaseResponse> BaseResponse getRemoteDataSync(Call<ResponseBody> call,
                                                                      final Class<T> clazz) {
        BaseResponse baseResponse = new BaseResponse();
        if (NetworkUtil.isNetworkAvailable()) {
            try {
                Response<ResponseBody> response = call.execute();
                if (response.body() != null) {
                    baseResponse = handleBaseResponseV2(response.body().string(), clazz);
                } else {
                    ResponseThrowable responseThrowable = ExceptionHandle
                            .handleException(new HttpException(response));
                    baseResponse.setCode(responseThrowable.code);
                    baseResponse.setMsg(responseThrowable.message);
                }
            } catch (Exception e) {
                ResponseThrowable responseThrowable = ExceptionHandle.handleException(e);
                baseResponse.setCode(responseThrowable.code);
                baseResponse.setMsg(responseThrowable.message);
            }
        } else {
            baseResponse.setCode(ExceptionHandle.ERROR.NETWORK_DISABLE);
            baseResponse.setMsg(ApplicationContext.getString(R.string.statepage_no_network));
        }
        return baseResponse;
    }



    protected <T> BaseResponse<T> getRemoteDataSyncNotV2(Call<ResponseBody> call,
                                                                      final Class<T> clazz) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.body() != null) {
                baseResponse = handleBaseResponse(response.body(), clazz);
            } else {
                ResponseThrowable responseThrowable = ExceptionHandle
                        .handleException(new HttpException(response));
                baseResponse.setCode(responseThrowable.code);
                baseResponse.setMsg(responseThrowable.message);
            }
        } catch (Exception e) {
            ResponseThrowable responseThrowable = ExceptionHandle.handleException(e);
            baseResponse.setCode(responseThrowable.code);
            baseResponse.setMsg(responseThrowable.message);
        }
        return baseResponse;
    }


    protected DataListener dataListener;

    public BaseModel setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
        return this;
    }
}
