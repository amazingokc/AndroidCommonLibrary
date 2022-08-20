package com.core.commonlibrary.net;

import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/24 0024
 * description:
 */
public interface RestService {

    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap WeakHashMap<String, Object> params,
                                 @HeaderMap WeakHashMap<String, Object> headersParams);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap WeakHashMap<String, Object> params,
                                  @HeaderMap WeakHashMap<String, Object> headersParams);

    @POST
    Observable<ResponseBody> postRaw(@Url String url, @Body RequestBody body);

    @PATCH
    Observable<ResponseBody> patchRaw(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Observable<ResponseBody> put(@Url String url, @FieldMap WeakHashMap<String, Object> params);

    @PUT
    Observable<ResponseBody> putRaw(@Url String url, @Body RequestBody body);

    @DELETE
    Observable<ResponseBody> delete(@Url String url, @QueryMap WeakHashMap<String, Object> params);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url, @QueryMap WeakHashMap<String, Object> params);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @PartMap Map<String, RequestBody> data, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Observable<ResponseBody> uploadMore(@Url String url, @PartMap Map<String, RequestBody> data, @PartMap Map<String, RequestBody> body);


    //同步
    @FormUrlEncoded
    @POST
    Call<ResponseBody> syncPost(@Url String url, @FieldMap WeakHashMap<String, Object> params,
                                @HeaderMap WeakHashMap<String, Object> headersParams);

    @POST
    Call<ResponseBody> syncPostRaw(@Url String url, @Body RequestBody body);

    @PATCH
    Call<ResponseBody> syncPatchRaw(@Url String url, @Body RequestBody body);

    @Multipart
    @POST
    Call<ResponseBody> syncUpload(@Url String url, @PartMap Map<String, RequestBody> data, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @PATCH
    Call<ResponseBody> syncPatch(@Url String url, @FieldMap WeakHashMap<String, Object> params,
                                 @HeaderMap WeakHashMap<String, Object> headersParams);
}
