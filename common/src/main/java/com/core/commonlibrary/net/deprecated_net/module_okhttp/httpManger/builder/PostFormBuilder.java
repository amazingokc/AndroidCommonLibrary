package com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.builder;

import android.util.Log;

import com.core.commonlibrary.server.ServiceFactory;
import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.PostFormRequest;
import com.core.commonlibrary.net.deprecated_net.module_okhttp.httpManger.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, files, id).build();
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PostFormBuilder addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }


    @Override
    public PostFormBuilder params(WeakHashMap<String, Object> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val) {
        if (this.params == null) {
            //加入时间戳
            params = new WeakHashMap<>();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        //封装校验码
//        params.put("timestamp", timestamp);
//        params.put("userId", UserManager.getUserInfoBean().getUserId());
//        params.put("appKey", CommonUtil.getAppKey(timestamp));
//        params.put("appId", CommonUtil.getAppId());
//        params.put("signature", CommonUtil.getSignature(timestamp));

        params.putAll(ServiceFactory.getInstance().getCommonService().getFiledMapParams());
        params.put(key, val);
        Log.d("params参数---", params.toString());
        return this;
    }


}
