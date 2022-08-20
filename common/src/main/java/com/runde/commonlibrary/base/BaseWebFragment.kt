package com.runde.commonlibrary.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.runde.commonlibrary.R
import com.runde.commonlibrary.customview.TencentWebView
import com.runde.commonlibrary.event.EventBusUtil
import com.runde.commonlibrary.server.ServiceFactory
import com.runde.commonlibrary.utils.*
import com.runde.commonlibrary.wechat.event.WXShareEvent
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


open class BaseWebFragment : BaseFragment() {

    private var view: ViewGroup? = null
    protected var mWebView: TencentWebView? = null
    private val isDestroy = false //页面是否销毁
    protected var isLoadFinish = false //页面是否加载完成(包括正常和异常)
    private var webUrl = ""
    var myWebChromeClient: MyWebChromeClient? = null

    override fun initViews() {
        super.initViews()
        view = setCenterView(R.layout.base_web_fragment_layout) as ViewGroup?
        mWebView = view?.findViewById(R.id.wv_content_resources)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        WebViewUtil.initWebSetting(mWebView?.settings)

        mWebView?.webViewClient = MyWebViewClient()
        mWebView?.webChromeClient = getWebViewChromeClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtil.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtil.unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WXShareEvent) {
        if (ShareUtil.SHARE_SUCCESS_CALL_TO_WEB == event.transaction) {
            callJsMethod("Mobile_Send_Share_Status")
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            if (url == null) {
                return false
            }
            if (url.startsWith("weixin://")) {
                if (IsClientAvailable.isWeixinAvilible()) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    return true
                } else {
                    ToastUtil.showDebug("请先安装微信应用")
                }
            }
            return super.shouldOverrideUrlLoading(webView, url)
        }

        //OPPO R15 偶尔出现不回调onProgressChanged 所以onPageFinished
        override fun onPageFinished(webView: WebView, s: String) {
            super.onPageFinished(webView, s)
            if (!isDestroy && !isLoadFinish) {
                isLoadFinish = true
                dismissDialog()
                val title = webView.title
                val toolbarTitle: String = getToolbarTopTitle().getText().toString()
                if (!TextUtils.isEmpty(title) &&
                    !webView.url.contains(title) &&
                    TextUtils.isEmpty(toolbarTitle)
                ) {
                    if (title != "undefined") setToolbarTopTitle(title)
                }
                setPageSuccess()
            }
        }
    }

    open inner class MyWebChromeClient : WebChromeClient() {

        //OPPO R15 偶尔出现不回调onProgressChanged 所以onPageFinished
        override fun onProgressChanged(webView: WebView, i: Int) {
            super.onProgressChanged(webView, i)
            if (i == 100) {
                if (!isDestroy && !isLoadFinish) {
                    isLoadFinish = true
                    dismissDialog()
                }
            }
        }
    }

    open fun getWebViewChromeClient(): WebChromeClient {
        myWebChromeClient = MyWebChromeClient()
        return myWebChromeClient as MyWebChromeClient
    }

    /**
     * 调用js无返回值的方法
     *
     * @param method
     */
    fun callJsMethod(method: String?) {
        if (isLoadFinish) {
            LLog.d("callJsMethod", method)
            if (method != null && method.contains("()")) {
                mWebView?.loadUrl("javascript:$method")
            } else {
                mWebView?.loadUrl("javascript:$method()")
            }
        }
    }

    /**
     * 调用js无返回值得方法 并带参数
     *
     * @param method 调用js字符串
     * @param parm   传给js方法的参数
     */
    fun callJsMethod(method: String, parm: String) {
        val newURl = "javascript:$method($parm)"
        if (isLoadFinish) {
            LLog.d("callJsMethod", newURl)
            mWebView?.loadUrl(newURl)
        }
    }

    fun loadWeb(url: String?) {
        webUrl = url!!
        loadUrl()
    }

    private fun loadUrl() {
        if (NetworkUtil.isNetworkAvailable()) {
            if (mWebView != null) {
                showDialog(false, "")
                isLoadFinish = false
                mWebView?.loadUrl(webUrl)
            }
        } else {
            setPageEmpty(ServiceFactory.getInstance().commonService.statepageConnectWrong,
                getString(R.string.statepage_no_network),
                getString(R.string.statepage_reload), object : LazyOnClickListener() {
                    override fun onLazyClick(v: View) {
                        loadUrl()
                    }
                })
        }
    }

}

