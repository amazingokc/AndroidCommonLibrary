package com.runde.commonlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.base.StatuesBarBaseActivity;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.LazyOnClickListener;
import com.runde.commonlibrary.utils.NetworkUtil;
import com.runde.commonlibrary.utils.RundeKeyBoardUtils;

public class PrivacyWebViewActivity extends StatuesBarBaseActivity {

    public WebView wvContentResources;
    public RelativeLayout rlRootWebview;
    FrameLayout flFullVideo;
    private boolean isDestroy = false;          //页面是否销毁
    protected boolean isLoadFinish = false;       //页面是否加载完成(包括正常和异常)
    private String webUrl = "";
    public MyWebChromeClient myWebChromeClient;
    public static final String INTENT_PARAM_KEY_URL = "webUrl";

    public static void intentThere(Activity activity, String webUrl){
        Intent intent = new Intent(activity, PrivacyWebViewActivity.class);
        intent.putExtra(INTENT_PARAM_KEY_URL,webUrl);
        activity.startActivity(intent);
    }

    @Override
    public void initParams() {
        super.initParams();
        webUrl = getIntent().getStringExtra(INTENT_PARAM_KEY_URL);
    }

    @Override
    public void initDatas() {
        super.initDatas();

        if (TextUtils.isEmpty(webUrl)) {
            finish();
            return;
        }
        if(webUrl.startsWith("http")){
            loadUrl();
        }else{
            wvContentResources.loadData(webUrl,"text/html; charset=UTF-8","UTF-8");
        }

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

    @Override
    public void initViews() {
        super.initViews();
        setCenterView(R.layout.actvity_privacy_webview);
        wvContentResources = findViewById(R.id.wv_content_resources);
        rlRootWebview = findViewById(R.id.rl_root_webview);
        flFullVideo = findViewById(R.id.fl_full_video);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            wvContentResources.setWebContentsDebuggingEnabled(true);
        }
        //开启硬件加速
        //wvContentResources.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        WebSettings settings = wvContentResources.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //DOM存储API是否可用(默认是false，可能出现空白页，所以建议true)
        settings.setDomStorageEnabled(true);
        //解决图片不显示
        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);
        //设置渲染优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //设置 缓存模式
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        String userAgentString = settings.getUserAgentString();
        settings.setUserAgentString(userAgentString + ";android_APP");

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

    @Override
    public void onBackPressed() {
        if (wvContentResources.canGoBack()) {
            wvContentResources.goBack();//返回上个页面
            return;
        }
        super.onBackPressed();

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

        if (wvContentResources != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = wvContentResources.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(wvContentResources);
            }

            wvContentResources.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            wvContentResources.getSettings().setJavaScriptEnabled(false);
            wvContentResources.clearHistory();
            wvContentResources.clearView();
            wvContentResources.removeAllViews();
            wvContentResources.destroy();

        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        private View myView = null;


        /**
         * API >= 21(Android 5.0.1)回调此方法
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
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
        public void onHideCustomView() {
            super.onHideCustomView();
            if (myView != null) {
                flFullVideo.removeAllViews();
                rlRootWebview.addView(wvContentResources);
                flFullVideo.setVisibility(View.GONE);
                getToolbar().setVisibility(View.VISIBLE);
                myView = null;
            }
        }
    }

    public class MyWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url == null) {
                return false;
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

        }
    }

}
