package com.core.commonlibrary.global;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.commonlibrary.RDLibrary;
import com.core.commonlibrary.utils.LLog;
import com.core.commonlibrary.utils.SharePreUtil3;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.statistics.common.DeviceConfig;
import com.umeng.commonsdk.utils.UMUtils;


/**
 * Create by: xiaoguoqing
 * Date: 2018/12/28 0028
 * description:
 */
public class CommonLibrarySDKManager {

    private static RefWatcher refWatcher;
    private FrontMonitor frontMonitor;
    private static final String ACTIVITY_LIFE_CYCLE_LOG = "ACTIVITY_LIFE_CYCLE_LOG";

    private CommonLibrarySDKManager() {

    }

    static class CommonLibrarySDKManagerHolder {
        static CommonLibrarySDKManager commonLibrarySDKManager = new CommonLibrarySDKManager();
    }

    public static CommonLibrarySDKManager getInstance() {
        return CommonLibrarySDKManagerHolder.commonLibrarySDKManager;
    }

    public CommonLibrarySDKManager initHandler() {
        ApplicationContext.initUiHandler();
        return this;
    }

    /**
     * 友盟
     */
    public CommonLibrarySDKManager setUMConfig(Context context, String appKey, String channel,
                                               int deviceType, String pushSecret) {
        if (TextUtils.isEmpty(appKey)) {
            LLog.e("未提供UmengAppkey");
            return this;
        }
        //友盟日志开关
        UMConfigure.setLogEnabled(true);
        UMConfigure.preInit(context, appKey, channel);
        //是否同意隐私政策
        boolean agreed = SharePreUtil3.getBoolean(SharePreUtil3.PRIVACY_POLICY_PRE_KEY, false);
        if (agreed) {
            boolean isMainProcess = UMUtils.isMainProgress(context);
            if (isMainProcess) {
                //启动优化：建议在子线程中执行初始化
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UMConfigure.init(context, appKey, channel, deviceType, pushSecret);
                    }
                }).start();
            } else {
                //若不是主进程（":channel"结尾的进程），直接初始化sdk，不可在子线程中执行
                UMConfigure.init(context, appKey, channel, deviceType, pushSecret);
            }
        }
        return this;
    }


    public static String[] getTestDeviceInfo(Context context) {
        String[] deviceInfo = new String[2];
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e) {
        }
        return deviceInfo;
    }

    /**
     * 安装LeakCanary
     */
    public CommonLibrarySDKManager installLeakCanary(Application context) {
        //LeakCanary初始化
        if (RDLibrary.isDebug()) {
            //LeakCanary.install(context);
            refWatcher = setupLeakCanary(context);
        }
        return this;
    }

    private RefWatcher setupLeakCanary(Application context) {
        if (LeakCanary.isInAnalyzerProcess(context)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(context);
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    /**
     * 初始化路由框架
     */
    public CommonLibrarySDKManager initRouter(Application application) {
        if (RDLibrary.isDebug()) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
        return this;
    }

    public interface FrontMonitor {
        void toFront(Activity activity);
    }

    public void setFrontMonitor(FrontMonitor frontMonitor) {
        this.frontMonitor = frontMonitor;
    }

    public synchronized CommonLibrarySDKManager registerActivityLifecycle(@NonNull Application application) {

        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {


            /**
             * 是不是需要忽略
             * @param activity
             * @return
             */

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivityCreated()");
//                if (activity instanceof BaseActivity) {
                AppManager.getAppManager().addActivity(activity);
//                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivityStarted()");

                AppManager.getAppManager().setmActivityCount(
                        AppManager.getAppManager().getmActivityCount() + 1);

                if (AppManager.getAppManager().getmActivityCount() == 1) {
                    //                    ToastUtil.showDebug("应用进入前台");
                    if (frontMonitor != null) {
                        frontMonitor.toFront(activity);
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivityResumed()");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivityPaused()");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivityStopped()");

                AppManager.getAppManager().setmActivityCount(
                        AppManager.getAppManager().getmActivityCount() - 1);
                //应用从前台进入后台需要做延时才能判断
                AppManager.getAppManager().onActivityStopped();

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivitySaveInstanceState()");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LLog.d(ACTIVITY_LIFE_CYCLE_LOG, activity.getClass().getName()
                        + ":-->onActivityDestroyed()");

//                if (activity instanceof BaseActivity) {
                AppManager.getAppManager().removeActivity(activity);
//                }
            }
        });
        return this;
    }

}
