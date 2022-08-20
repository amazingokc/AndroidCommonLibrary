package com.runde.commonlibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.webkit.WebView;

import androidx.multidex.MultiDexApplication;

import com.runde.commonlibrary.BuildConfig;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.global.CommonLibrarySDKManager;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.CrashUtil;
import com.runde.commonlibrary.utils.ProcessUtil;
import com.runde.commonlibrary.utils.ProcessUtil;
import com.runde.commonlibrary.utils.LLog;
import com.runde.commonlibrary.utils.SharePreUtil3;
import com.runde.commonlibrary.utils.ToastUtil;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Locale;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-09-03 上午 10:46
 * 文件描述：
 */
public abstract class BaseApplication extends MultiDexApplication {

    protected static boolean isInitApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashUtil.Companion.getInstance().init(this);
        ApplicationContext.initContext(this);
        fixWebViewDataDirectoryBug();
        //这部分初始化不能放在子线程
        CommonLibrarySDKManager.getInstance()
                .initHandler()
                //.installLeakCanary(this)
                .initRouter(this)
                .registerActivityLifecycle(this);

        //预初始化函数不会采集设备信息，也不会向友盟后台上报数据
        UMConfigure.preInit(this, ServiceFactory.getInstance().getCommonService().getUmengAppKey(),
                null);

    }


    public void fixWebViewDataDirectoryBug() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            String packageName = this.getPackageName();
            if (!packageName.equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    //不随系统的字体大小变化而变化（部分系统无效）
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    public static void setModuleApps(String[] moduleApps, Context context) {
        initComponentApplication(moduleApps, context);
    }

    private static void initComponentApplication(String[] moduleApps, Context context) {
        if (moduleApps == null) {
            return;
        }
        for (String moduleApp : moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                BaseApplication baseApp = (BaseApplication) clazz.newInstance();
                baseApp.init(context);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                ToastUtil.showDebug(e.getMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                ToastUtil.showDebug(e.getMessage());
            } catch (InstantiationException e) {
                e.printStackTrace();
                ToastUtil.showDebug(e.getMessage());
            }
        }
    }

    protected static void initTencentWebView(Context context) {
        if (!isInitApplication
                && SharePreUtil3.getBoolean(SharePreUtil3.PRIVACY_POLICY_PRE_KEY, false)) {
            //初始化X5内核
            QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
                @Override
                public void onCoreInitFinished() {
                    //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
                }

                @Override
                public void onViewInitFinished(boolean b) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    LLog.d("initTencentWebView", " onViewInitFinished is " + b);
                }
            });

            // 在调用TBS初始化、创建WebView之前进行如下配置
            HashMap map = new HashMap();
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
            QbSdk.initTbsSettings(map);
        } else {
            QbSdk.disableSensitiveApi();
        }
    }

    public abstract void init(Context context);
}
