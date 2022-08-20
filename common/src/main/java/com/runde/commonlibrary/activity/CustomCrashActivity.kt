package com.runde.commonlibrary.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.runde.commonlibrary.R
import com.runde.commonlibrary.base.StatuesBarBaseActivity
import com.runde.commonlibrary.utils.LazyOnClickListener
import com.runde.commonlibrary.utils.SystemUtils
import com.runde.commonlibrary.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_custom_crash.*

class CustomCrashActivity : StatuesBarBaseActivity() {

    var errorInfo: String? = null
    var detailInfo: String? = null

    companion object {

        @JvmStatic
        fun launch(context: Context, errorInfo: String, detailInfo :String) {
            val intent = Intent(context, CustomCrashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("errorInfo", errorInfo)
            intent.putExtra("detailInfo", detailInfo)
            context.startActivity(intent)
        }
    }

    override fun initParams() {
        super.initParams()
        errorInfo = intent.getStringExtra("errorInfo")
        detailInfo = intent.getStringExtra("detailInfo")
    }

    override fun initViews() {
        super.initViews()
        setCenterView(R.layout.activity_custom_crash)
        setToolbarTopTitle("抱歉，应用崩溃了")
        tv_error_info.text = errorInfo

        tv_copy_detail_info.setOnClickListener(object :LazyOnClickListener() {
            override fun onLazyClick(v: View?) {
                SystemUtils.copyToClip(this@CustomCrashActivity, errorInfo)
                ToastUtil.show("复制成功")
            }
        })

        tv_restart.setOnClickListener(object : LazyOnClickListener() {
            override fun onLazyClick(v: View?) {
                val launchIntent = packageManager.getLaunchIntentForPackage(application.packageName)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(launchIntent)
                finish()
            }
        })

    }
}