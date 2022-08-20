package com.core.commonlibrary.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.core.commonlibrary.R
import com.core.commonlibrary.base.StatuesBarBaseActivity
import com.core.commonlibrary.utils.LazyOnClickListener
import com.core.commonlibrary.utils.SystemUtils
import com.core.commonlibrary.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_custom_crash.*

private const val PARAM_ERROR_INFO = "errorInfo"
private const val PARAM_DETAIL_INFO = "detailInfo"

class CustomCrashActivity : StatuesBarBaseActivity() {

    private var errorInfo: String? = null
    private var detailInfo: String? = null

    companion object {
        @JvmStatic
        fun launch(context: Context, errorInfo: String, detailInfo: String) {
            val intent = Intent(context, CustomCrashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(PARAM_ERROR_INFO, errorInfo)
            intent.putExtra(PARAM_DETAIL_INFO, detailInfo)
            context.startActivity(intent)
        }
    }

    override fun initParams() {
        super.initParams()
        errorInfo = intent.getStringExtra(PARAM_ERROR_INFO)
        detailInfo = intent.getStringExtra(PARAM_DETAIL_INFO)
    }

    override fun initViews() {
        super.initViews()
        setCenterView(R.layout.activity_custom_crash)
        setToolbarTopTitle("抱歉，应用崩溃了")
        tvErrorInfo.text = errorInfo

        tvCopyDetailInfo.setOnClickListener(object : LazyOnClickListener() {
            override fun onLazyClick(v: View?) {
                SystemUtils.copyToClip(this@CustomCrashActivity, errorInfo)
                ToastUtil.show("复制成功")
            }
        })

        tvRestart.setOnClickListener(object : LazyOnClickListener() {
            override fun onLazyClick(v: View?) {
                val launchIntent = packageManager.getLaunchIntentForPackage(application.packageName)
                launchIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(launchIntent)
                finish()
            }
        })

    }
}