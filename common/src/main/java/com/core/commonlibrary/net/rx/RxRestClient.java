package com.core.commonlibrary.net.rx;

import com.core.commonlibrary.net.RestCreator;
import com.core.commonlibrary.net.RestService;
import com.core.commonlibrary.server.ServiceFactory;
import com.core.commonlibrary.utils.CommonUtil;
import com.core.commonlibrary.utils.RdRequestHeadsParamsUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


public final class RxRestClient {

    private final WeakHashMap<String, Object> FIELD_MAP_PARAMS;
    private final WeakHashMap<String, Object> QUERY_MAP_PARAMS;
    private final WeakHashMap<String, Object> HEADERS_PARAMS;
    private final String URL;
    private final RequestBody BODY;
    private final File FILE;
    private final List<File> FILES;


    RxRestClient(RxRestClientBuilder builder) {
        this.URL = builder.mUrl;
        this.QUERY_MAP_PARAMS = builder.QUERY_MAP_PARAMS;
        this.FIELD_MAP_PARAMS = builder.FIELD_MAP_PARAMS;
        this.HEADERS_PARAMS = builder.HEADERS_PARAMS;
        this.BODY = builder.mBody;
        this.FILE = builder.mFile;
        this.FILES = builder.mFiles;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    private Call<ResponseBody> requestSync(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<ResponseBody> call = null;
        if (!FIELD_MAP_PARAMS.isEmpty()) {
            CommonUtil.myEncrypt(FIELD_MAP_PARAMS);
        }

        switch (method) {
            case SYNC_POST:
                call = service.syncPost(URL, FIELD_MAP_PARAMS, HEADERS_PARAMS);
                break;
            case SYNC_POST_RAW:
                call = service.syncPostRaw(URL, BODY);
                break;
            case SYNC_PATCH:
                call = service.syncPatch(URL, FIELD_MAP_PARAMS, HEADERS_PARAMS);
                break;
            case SYNC_PATCH_RAW:
                call = service.syncPatchRaw(URL, BODY);
                break;
            case SYNC_UPLOAD:
                Map<String, RequestBody> map = new WeakHashMap<>();
                //遍历map中所有参数到builder
                if (FIELD_MAP_PARAMS != null) {
                    for (String key : FIELD_MAP_PARAMS.keySet()) {
                        map.put(key, RequestBody.create(null, (String) FIELD_MAP_PARAMS.get(key)));
                    }
                }
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);

                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);

                call = service.syncUpload(URL, map, body);
                break;
            default:
                break;
        }
        return call;
    }

    private Observable<ResponseBody> request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Observable<ResponseBody> observable = null;
        if (!FIELD_MAP_PARAMS.isEmpty()) {
            CommonUtil.myEncrypt(FIELD_MAP_PARAMS);
        }

        switch (method) {
            case GET:
                observable = service.get(URL, QUERY_MAP_PARAMS, HEADERS_PARAMS);
                break;
            case POST:
                observable = service.post(URL, FIELD_MAP_PARAMS, HEADERS_PARAMS);
                break;
            case POST_RAW:
                observable = service.postRaw(URL, BODY);
                break;
            case PUT:
                observable = service.put(URL, FIELD_MAP_PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = service.delete(URL, FIELD_MAP_PARAMS);
                break;
            case UPLOAD:
                Map<String, RequestBody> map = new WeakHashMap<>();
                //遍历map中所有参数到builder
                if (FIELD_MAP_PARAMS != null) {
                    for (String key : FIELD_MAP_PARAMS.keySet()) {
                        map.put(key, RequestBody.create(null, (String) FIELD_MAP_PARAMS.get(key)));
                    }
                }
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);

                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);

                observable = service.upload(URL, map, body);
                break;
            case UPLOAD_MORE_FILE:
                Map<String, RequestBody> map1 = new WeakHashMap<>();
                //遍历map中所有参数到builder
                if (FIELD_MAP_PARAMS != null) {
                    for (String key : FIELD_MAP_PARAMS.keySet()) {
                        map1.put(key, RequestBody.create(null, (String) FIELD_MAP_PARAMS.get(key)));
                    }
                }
                Map<String, RequestBody> photoMap = new HashMap<>();
                for (int i = 0; i < FILES.size(); i++) {
                    File file = FILES.get(i);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("image/jpg"), file);
                    photoMap.put("files\"; filename=\"" + file.getName(), requestFile);
                }
                observable = service.uploadMore(URL, map1, photoMap);

            default:
                break;
        }

        return observable;
    }

    public final Observable<ResponseBody> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<ResponseBody> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            return request(HttpMethod.POST_RAW);
        }
    }

    public final Call<ResponseBody> postSync() {
        if (BODY == null) {
            return requestSync(HttpMethod.SYNC_POST);
        } else {
            return requestSync(HttpMethod.SYNC_POST_RAW);
        }
    }

    public final Call<ResponseBody> patchSync() {
        if (BODY == null) {
            return requestSync(HttpMethod.SYNC_PATCH);
        } else {
            return requestSync(HttpMethod.SYNC_PATCH_RAW);
        }
    }

    public final Observable<ResponseBody> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            return request(HttpMethod.PUT_RAW);
        }
    }

    public final Observable<ResponseBody> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<ResponseBody> upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Call<ResponseBody> uploadSync() {
        return requestSync(HttpMethod.SYNC_UPLOAD);
    }

    public final Observable<ResponseBody> uploadMore() {
        return request(HttpMethod.UPLOAD_MORE_FILE);
    }

    public final Observable<ResponseBody> download() {
//        return RestCreator.getRestService().download(URL, FIELD_MAP_PARAMS);
        return RestCreator.getRestService().download(URL, QUERY_MAP_PARAMS);
    }

    public static class RxRestClientBuilder {

        private final WeakHashMap<String, Object> FIELD_MAP_PARAMS = new WeakHashMap<>();
        private final WeakHashMap<String, Object> QUERY_MAP_PARAMS = new WeakHashMap<>();
        private final WeakHashMap<String, Object> HEADERS_PARAMS = new WeakHashMap<>();
        private String mUrl = null;
        private RequestBody mBody = null;
        private File mFile = null;
        private List<File> mFiles = null;

        RxRestClientBuilder() {
        }

        public final RxRestClientBuilder url(String url) {
            this.mUrl = url;
            return this;
        }

        public final RxRestClientBuilder queryParams(WeakHashMap<String, Object> params) {
            QUERY_MAP_PARAMS.putAll(params);
            return this;
        }

        public final RxRestClientBuilder queryParam(String key, Object value) {
            if (value != null)
                QUERY_MAP_PARAMS.put(key, value);
            return this;
        }

        public final RxRestClientBuilder params(WeakHashMap<String, Object> params) {
            if (params != null && !params.isEmpty()) {
                FIELD_MAP_PARAMS.putAll(params);
            }
            return this;
        }

        public final RxRestClientBuilder param(String key, Object value) {
            if (value != null)
                FIELD_MAP_PARAMS.put(key, value);
            return this;
        }

        public final RxRestClientBuilder headersParam(String key, Object value) {
            if (value != null)
                HEADERS_PARAMS.put(key, value);
            return this;
        }

        public final RxRestClientBuilder headerParams(WeakHashMap<String, Object> params) {
            if (params != null && !params.isEmpty()) {
                HEADERS_PARAMS.putAll(params);
            }
            return this;
        }

        public final RxRestClientBuilder file(File file) {
            this.mFile = file;
            return this;
        }

        public final RxRestClientBuilder file(List<File> files) {
            this.mFiles = files;
            return this;
        }

        public final RxRestClientBuilder file(String file) {
            this.mFile = new File(file);
            return this;
        }

        public final RxRestClientBuilder raw(String raw) {
            this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
            return this;
        }


        @SuppressWarnings("unchecked")
        public final RxRestClient build() {
            params(RdRequestHeadsParamsUtil.getFiledMapParams());
            headerParams(ServiceFactory.getInstance().getCommonService().getHeaderMapParams());
            return new RxRestClient(this);
        }
    }
}
