package com.runde.commonlibrary.base;

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
import com.runde.commonlibrary.R;
import com.runde.commonlibrary.bean.JsFuncBean;
import com.runde.commonlibrary.customview.TencentWebView;
import com.runde.commonlibrary.event.EventBusUtil;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.CustomExceptionUtil;
import com.runde.commonlibrary.utils.GsonUtil;
import com.runde.commonlibrary.utils.IsClientAvailable;
import com.runde.commonlibrary.utils.LLog;
import com.runde.commonlibrary.utils.LazyOnClickListener;
import com.runde.commonlibrary.utils.NetworkUtil;
import com.runde.commonlibrary.utils.PermissionXUtil;
import com.runde.commonlibrary.utils.RundeKeyBoardUtils;
import com.runde.commonlibrary.utils.RundeScreenUtils;
import com.runde.commonlibrary.utils.ShareUtil;
import com.runde.commonlibrary.utils.ToastUtil;
import com.runde.commonlibrary.utils.WebViewUtil;
import com.runde.commonlibrary.wechat.event.WXShareEvent;
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
    private boolean isDestroy = false;          //??????????????????
    protected boolean isLoadFinish = false;       //????????????????????????(?????????????????????)
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
                //???????????????
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
            wvContentResources.goBack();//??????????????????
            return;
        }
        super.onBackPressed();
    }

    /**
     * ?????????????????????????????????????????????
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
                    ToastUtil.showDebug("????????????????????????");
                }
            }
            return super.shouldOverrideUrlLoading(webView, url);
        }

        //OPPO R15 ?????????????????????onProgressChanged ??????onPageFinished
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
         * 11(Android 3.0) <= API <= 15(Android 4.0.3)???????????????
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            LLog.e("WangJ", "???????????? openFileChooser-2 (acceptType: " + acceptType + ")");
            mUploadCallbackBelow = uploadMsg;
            checkPermission(acceptType);
        }

        /**
         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)???????????????
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            LLog.e("WangJ", "???????????? openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
            openFileChooser(uploadMsg, acceptType);
        }

        /**
         * API >= 21(Android 5.0.1)???????????????
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            LLog.e("WangJ", "???????????? onShowFileChooser");
            // (1)??????????????????????????????API >= 21??????????????????????????? mUploadCallbackAboveL????????? != null
            mUploadCallbackAboveL = filePathCallback;
            checkPermission(fileChooserParams.getAcceptTypes()[0]);
            return true;
        }

        //OPPO R15 ?????????????????????onProgressChanged ??????onPageFinished
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
         * onPageFinished???onProgressChanged??????????????????title?????????????????????
         * ???????????????goback????????????
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
        PermissionXUtil.checkPermission(this, "??????????????????????????????????????????", new PermissionXUtil.PermissionListener() {

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
     * ????????????
     */
    private void takePhoto(String acceptType) {
        mUri = null;

        try {
            Intent intent = null;
            if (acceptType.contains("video")) {
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1); // ?????????????????????
                startActivityForResult(intent, 1000);
            } else {

                //???????????????
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // ??????????????????
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, 1001);
            }
        } catch (Exception e) {
            CustomExceptionUtil.reportError("WebView??????????????????:" + e.getMessage());
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

                //????????????
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
        // ????????????(1)???(2)??????????????????????????????????????????????????????????????????????????????????????????
        if (mUploadCallbackBelow != null) {
            chooseBelow(resultCode, intent);
        } else if (mUploadCallbackAboveL != null) {
            chooseAbove(resultCode, intent);
        } else {
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePhotos(Uri uri) {
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    /**
     * Android API < 21(Android 5.0)?????????????????????
     *
     * @param resultCode ?????????????????????????????????
     * @param data       ????????????????????????????????????
     */
    private void chooseBelow(int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (data != null) {
                // ?????????????????????????????????
                Uri uri = data.getData();
                updatePhotos(uri);
                if (uri != null) {
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // ??????????????????????????????????????????????????????????????????data??????
                mUploadCallbackBelow.onReceiveValue(null);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) ?????????????????????
     *
     * @param resultCode ?????????????????????????????????
     * @param data       ????????????????????????????????????
     */
    private void chooseAbove(int resultCode, Intent data) {

        if (RESULT_OK == resultCode) {


            if (data != null) {
                // ?????????????????????????????????????????????
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
     * ????????????
     */
    private void setFullScreen() {
        RundeScreenUtils.setLandscape(this);
        // ??????????????????????????????????????????????????????????????????????????????
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * ????????????
     */
    private void quitFullScreen() {
        RundeScreenUtils.setPortrait(this);
        // ??????????????????????????????????????????
        final WindowManager.LayoutParams attrs = this.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setAttributes(attrs);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public TencentWebView getBaseWebView() {
        return wvContentResources;
    }

    /**
     * ??????????????????
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
     * ??????js?????????????????????
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
     * ??????js????????????????????? ????????????
     *
     * @param method ??????js?????????
     * @param parm   ??????js???????????????
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

    //????????????????????????????????????
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
        public void share(String info) {  //????????????
            if (mActivity == null)
                return;
            ShareUtil.shareWechat(info, mActivity);
        }
    }

    //???????????????????????????
    protected void showRightIcon(@NonNull final JsFuncBean jsFuncBean) {
        runOnUiThread(() -> {
            if (jsFuncBean.getFuncShow() == 1) {//?????????????????????
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
            } else {//?????????????????????
                hideToolbarMainMenuItem();
            }
        });
    }

}
