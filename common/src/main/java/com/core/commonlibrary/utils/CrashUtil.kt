package com.core.commonlibrary.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import com.core.commonlibrary.activity.CustomCrashActivity
import com.core.commonlibrary.global.AppManager
import com.core.commonlibrary.global.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 崩溃日志收集器
 * 异常保存SD卡
 */
class CrashUtil : Thread.UncaughtExceptionHandler {
    //系统默认的UncaughtException处理类
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var context: Context

    /**
     * 设置自定异常处理类
     */
    fun init(context: Context) {
        this.context = context
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler() //获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this) //设置该CrashUtil为程序的默认处理器
    }

    /**
     * 重写捕捉异常
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        val crashLog = getExceptionInfo(ex)
        LLog.e("uncaughtException", crashLog)
        try {
            GlobalScope.launch {
                saveToSdcard(crashLog)
            }
        } catch (e: Exception) {
            LLog.i(TAG, e.message)
        } finally {

            AppManager.getAppManager().finishAllActivity() // 应用准备退出
            //跳转到自定义崩溃界面
            val stringWriter = StringWriter()
            ex.printStackTrace(PrintWriter(stringWriter))
            CustomCrashActivity.launch(context, stringWriter.toString(), crashLog)
        }
    }

    /**
     * 保存异常信息到sdcard中
     */
    private suspend fun saveToSdcard(crashLog: String) {

        withContext(Dispatchers.IO) {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                //创建一个error文件
                val file =
                    ApplicationContext.getContext().getExternalFilesDir("rundeCrashCollection")
                if (!file!!.exists()) {
                    file.mkdirs() //创建崩溃捕捉所在文件夹
                }
                val log = File(file, "error_" + TimeUtil.getCurrentTime() + ".txt")
                val outStream: FileOutputStream
                try {
                    outStream = FileOutputStream(log)
                    outStream.write(crashLog.toByteArray())
                    outStream.flush()
                } catch (e: Exception) {
                    LLog.e(TAG, e.message)
                }
            }
        }

    }


    /**
     * 设备、用户、运营商、异常信息
     */
    private fun getExceptionInfo(ex: Throwable): String {
        val stringWriter = StringWriter()
        ex.printStackTrace(PrintWriter(stringWriter))
        val sb = StringBuffer()
        sb.append(SoftwareUtil.getAPKInfo(context))
        sb.append("\n>>>>>>>CrashActivity-Log-Begin>>>>>>>>${TimeUtil.getCurrentTime()}>>>>>>>>>>>>>>>>>>>>>>>>")
        sb.append("\n$stringWriter")
        sb.append("\n>>>>>>>CrashActivity-Log-End>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        return sb.toString()
    }

    companion object {
        private const val TAG = "CrashUtil"

        @JvmStatic
        var instance: CrashUtil = CrashUtil()

        fun hasPermission(context: Context, permission: String): Boolean {
            val perm = context.checkCallingOrSelfPermission(permission)
            return perm == PackageManager.PERMISSION_GRANTED
        }
    }
}