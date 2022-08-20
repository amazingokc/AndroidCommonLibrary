package com.core.commonlibrary.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.core.commonlibrary.R;
import com.core.commonlibrary.bean.JsFuncBean;
import com.core.commonlibrary.customview.TencentWebView;
import com.core.commonlibrary.event.EventBusUtil;
import com.core.commonlibrary.server.ServiceFactory;
import com.core.commonlibrary.utils.CustomExceptionUtil;
import com.core.commonlibrary.utils.GsonUtil;
import com.core.commonlibrary.utils.IsClientAvailable;
import com.core.commonlibrary.utils.LLog;
import com.core.commonlibrary.utils.LazyOnClickListener;
import com.core.commonlibrary.utils.NetworkUtil;
import com.core.commonlibrary.utils.PermissionXUtil;
import com.core.commonlibrary.utils.RundeKeyBoardUtils;
import com.core.commonlibrary.utils.RundeScreenUtils;
import com.core.commonlibrary.utils.ShareUtil;
import com.core.commonlibrary.utils.ToastUtil;
import com.core.commonlibrary.utils.WebViewUtil;
import com.core.commonlibrary.wechat.event.WXShareEvent;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.ButterKnife;

/**
 * Created by hua on 2019/8/5 0005.
 */
public class BaseWebActivity extends StatuesBarBaseActivity {

    public TencentWebView wvContentResources;
    public RelativeLayout rlRootWebview;
    FrameLayout flFullVideo;
    private boolean isDestroy = false;          //页面是否销毁
    protected boolean isLoadFinish = false;       //页面是否加载完成(包括正常和异常)
    private String webUrl = "";
    public MyWebChromeClient myWebChromeClient;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBusUtil.register(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setCenterView(R.layout.resources_fragment_base_web);
        wvContentResources = findViewById(R.id.wv_content_resources);
        rlRootWebview = findViewById(R.id.rl_root_webview);
        flFullVideo = findViewById(R.id.fl_full_video);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            wvContentResources.setWebContentsDebuggingEnabled(true);
        }
        WebViewUtil.initWebSetting(wvContentResources.getSettings());

        wvContentResources.setWebViewClient(new MyWebViewClient());
        wvContentResources.setWebChromeClient(getWebViewChromeClient());
        getToolbar().setNavigationOnClickListener(new LazyOnClickListener() {
            @Override
            public void onLazyClick(View v) {
                onBackPressed();
            }
        });

        showCloseTv();
    }

    public WebChromeClient getWebViewChromeClient() {
        myWebChromeClient = new MyWebChromeClient();
        return myWebChromeClient;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        RundeKeyBoardUtils.hideInput(this);
        EventBusUtil.unregister(this);
        WebViewUtil.destroyWebView(wvContentResources);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WXShareEvent event) {
        if (ShareUtil.SHARE_SUCCESS_CALL_TO_WEB.equals(event.getTransaction())) {
            callJsMethod("Mobile_Send_Share_Status");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getToolbar().setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //切换到全屏
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getToolbar().setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (wvContentResources.canGoBack()) {
            wvContentResources.goBack();//返回上个页面
            return;
        }
        super.onBackPressed();
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        myWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url == null) {
                return false;
            }
            if (url.startsWith("weixin://")) {
                if (IsClientAvailable.isWeixinAvilible()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    ToastUtil.showDebug("请先安装微信应用");
                }
            }
            return super.shouldOverrideUrlLoading(webView, url);
        }

        //OPPO R15 偶尔出现不回调onProgressChanged 所以onPageFinished
        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (!isDestroy && !isLoadFinish) {
                isLoadFinish = true;
                dismissDialog();
                String title = webView.getTitle();
                String toolbarTitle = getToolbarTopTitle().getText().toString();
                if (!TextUtils.isEmpty(title) &&
                        !webView.getUrl().contains(title) &&
                        TextUtils.isEmpty(toolbarTitle)) {
                    if (!title.equals("undefined"))
                        setToolbarTopTitle(title);
                }
                setPageSuccess();
            }
        }

    }

    private ValueCallback<Uri> mUploadCallbackBelow;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    public class MyWebChromeClient extends WebChromeClient {
        private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;
        private View myView = null;

        /**
         * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            LLog.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
            mUploadCallbackBelow = uploadMsg;
            checkPermission(acceptType);
        }

        /**
         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            LLog.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
            openFileChooser(uploadMsg, acceptType);
        }

        /**
         * API >= 21(Android 5.0.1)回调此方法
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            LLog.e("WangJ", "运行方法 onShowFileChooser");
            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
            mUploadCallbackAboveL = filePathCallback;
            checkPermission(fileChooserParams.getAcceptTypes()[0]);
            return true;
        }

        //OPPO R15 偶尔出现不回调onProgressChanged 所以onPageFinished
        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i == 100) {
                if (!isDestroy && !isLoadFinish) {
                    isLoadFinish = true;
                    dismissDialog();
                    String title = webView.getTitle();
                    String toolbarTitle = getToolbarTopTitle().getText().toString();
                    if (!TextUtils.isEmpty(title) &&
                            !webView.getUrl().contains(title) &&
                            TextUtils.isEmpty(toolbarTitle)) {
                        setToolbarTopTitle(title);
                    }
                    setPageSuccess();
                }
            }
        }


        /**
         * onPageFinished、onProgressChanged会出现拿不到title但在这个回调有
         * 这个回调在goback时不调用
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title) && !view.getUrl().contains(title)) {
                if (!title.equals("undefined"))
                    setToolbarTopTitle(title);
            }
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (view == null)
                return;
            ViewGroup parent = (ViewGroup) wvContentResources.getParent();
            parent.removeView(wvContentResources);
            flFullVideo.addView(view);
            flFullVideo.setVisibility(View.VISIBLE);
            getToolbar().setVisibility(View.GONE);
            myView = view;
            setFullScreen();
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (myView != null) {
                flFullVideo.removeAllViews();
                rlRootWebview.addView(wvContentResources);
                flFullVideo.setVisibility(View.GONE);
                getToolbar().setVisibility(View.VISIBLE);
                myView = null;
                quitFullScreen();
            }
        }
    }


    private void checkPermission(String acceptType) {
        PermissionXUtil.checkPermission(this, "需要读写内存卡权限、拍照权限", new PermissionXUtil.PermissionListener() {

            @Override
            public void onDenied() {

            }

            @Override
            public void onGranted() {
                takePhoto(acceptType);
            }
        }, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});


    }

    /**
     * 调用相机
     */
    private void takePhoto(String acceptType) {
        mUri = null;

        try {
            Intent intent = null;
            if (acceptType.contains("video")) {
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头
                startActivityForResult(intent, 1000);
            } else {

                //选择图片的
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, 1001);
            }
        } catch (Exception e) {
            CustomExceptionUtil.reportError("WebView调用相机失败:" + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {

            uploadDataToWeb(resultCode, data);
        } else if (requestCode == 1001) {
            if (RESULT_OK == resultCode) {
                Uri uriData = data.getData();

                //拍照用的
                if (uriData == null)
                    uriData = mUri;

                if (uriData == null) {
                    uploadDataToWeb(resultCode, null);
                    return;
                }

                Tiny.getInstance().source(uriData).asFile().compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        if (isSuccess) {
                            Uri uri = Uri.fromFile(new File(outfile));
                            //Uri uri = getMediaUriFromPath(outfile);
                            data.setData(uri);
                            uploadDataToWeb(resultCode, data);
                        } else {
                            uploadDataToWeb(resultCode, null);
                        }
                    }
                });
            } else {
                uploadDataToWeb(resultCode, null);
            }
        }
    }

    private void uploadDataToWeb(int resultCode, Intent intent) {
        // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
        if (mUploadCallbackBelow != null) {
            chooseBelow(resultCode, intent);
        } else if (mUploadCallbackAboveL != null) {
            chooseAbove(resultCode, intent);
        } else {
            Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePhotos(Uri uri) {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                updatePhotos(uri);
                if (uri != null) {
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                mUploadCallbackBelow.onReceiveValue(null);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {

        if (RESULT_OK == resultCode) {


            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    updatePhotos(uriData);
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                if (mUri != null)
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{mUri});
                else
                    mUploadCallbackAboveL.onReceiveValue(null);
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    /**
     * 设置全屏
     */
    private void setFullScreen() {
        RundeScreenUtils.setLandscape(this);
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        RundeScreenUtils.setPortrait(this);
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs = this.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setAttributes(attrs);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public TencentWebView getBaseWebView() {
        return wvContentResources;
    }

    /**
     * 加载页面链接
     *
     * @param url
     */
    public void loadWeb(String url) {
        this.webUrl = url;
        loadUrl();
    }

    private void loadUrl() {
        if (NetworkUtil.isNetworkAvailable()) {
            if (wvContentResources != null) {
                showDialog(false, "");
                isLoadFinish = false;
                wvContentResources.loadUrl(webUrl);
            }
        } else {
            setPageEmpty(ServiceFactory.getInstance().getCommonService().getStatepageConnectWrong(),
                    getString(R.string.statepage_no_network),
                    getString(R.string.statepage_reload), new LazyOnClickListener() {
                        @Override
                        public void onLazyClick(View v) {
                            loadUrl();
                        }
                    });
        }
    }

    /**
     * 调用js无返回值的方法
     *
     * @param method
     */
    public void callJsMethod(String method) {
        if (wvContentResources != null && isLoadFinish) {
            LLog.d("callJsMethod", method);
            if (method != null && method.contains("()")) {
                wvContentResources.loadUrl("javascript:" + method);
            } else {
                wvContentResources.loadUrl("javascript:" + method + "()");
            }
        }
    }

    /**
     * 调用js无返回值得方法 并带参数
     *
     * @param method 调用js字符串
     * @param parm   传给js方法的参数
     */
    public void callJsMethod(String method, String parm) {
        String newURl = "javascript:" + method + "(" + parm + ")";
        if (wvContentResources != null && isLoadFinish) {
            LLog.d("callJsMethod", newURl);
            wvContentResources.loadUrl(newURl);
        }
    }

    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    //这两个方法移到具体项目中
    @Deprecated()
    public static class JsInterFace {

        protected BaseWebActivity mActivity;

        @JavascriptInterface
        public void showFuncAction(String infos) {
            if (infos != null && mActivity != null) {
                JsFuncBean jsFuncBean = GsonUtil.fromJson(infos, JsFuncBean.class);
                if (jsFuncBean != null) {
                    mActivity.showRightIcon(jsFuncBean);
                }
            }
        }

        @JavascriptInterface
        public void share(String info) {  //微信分享
            if (mActivity == null)
                return;
            ShareUtil.shareWechat(info, mActivity);
        }
    }

    //处理右上角图标逻辑
    protected void showRightIcon(@NonNull final JsFuncBean jsFuncBean) {
        runOnUiThread(() -> {
            if (jsFuncBean.getFuncShow() == 1) {//显示右上角图标
                Glide.with(BaseWebActivity.this)
                        .asDrawable()
                        .load(jsFuncBean.getFuncIcon())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable Transition<? super Drawable> transition) {
                                setToolbarMainMenuItem(resource, item -> {
                                    callJsMethod(jsFuncBean.getFuncFeedback());
                                    return true;
                                });
                            }
                        });
            } else {//隐藏右上角图标
                hideToolbarMainMenuItem();
            }
        });
    }

}
