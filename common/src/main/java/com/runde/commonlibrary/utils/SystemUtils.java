package com.runde.commonlibrary.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;

import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.server.ServiceFactory;

import java.util.Locale;
import java.util.UUID;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-01-19 下午 12:10
 * 文件描述：
 */

public class SystemUtils {

    public static void openMusic() {
        try {
            RingtoneManager rm = new RingtoneManager(ApplicationContext.getContext());//初始化 系统声音
            Uri uri = rm.getDefaultUri(rm.TYPE_NOTIFICATION);//获取系统声音路径
            Ringtone mRingtone = rm.getRingtone(ApplicationContext.getContext(), uri);//通过Uri 来获取提示音的实例对象
            mRingtone.play();//播放:
        } catch (Exception e) {

        }
    }

    public static void vibrate() {
        Vibrator vibrator = (Vibrator) ApplicationContext.getContext()
                .getSystemService(ApplicationContext.getContext().VIBRATOR_SERVICE);
        vibrator.vibrate(500);//震动时长 ms
    }

    private static boolean isPad;
    private static boolean isGet;

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad() {
        //        return (ApplicationContext.getContext().getResources().getConfiguration().screenLayout
        //                & Configuration.SCREENLAYOUT_SIZE_MASK)
        //                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        if (!isGet) {
            isGet = true;
            isPad = (ApplicationContext.getContext().getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        return isPad;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    private static String systemVersion = "";

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        if (TextUtils.isEmpty(systemVersion)) {
            systemVersion = android.os.Build.VERSION.RELEASE;
        }
        return systemVersion;

    }

    private static String systemModel = "";

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {

        if (TextUtils.isEmpty(systemModel)) {
            systemModel = android.os.Build.MODEL;
        }
        return systemModel;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        String brand = android.os.Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            return brand;
        } else {
            return "";
        }
    }

    public static String getUUID() {
        String uuid = SharePreUtil3.getString(SharePreUtil3.UUID_KEY, "");
        if (!TextUtils.isEmpty(uuid)) {
            return uuid;
        }
        uuid = getAndroidId();
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            if (TextUtils.isEmpty(uuid)) {
                CustomExceptionUtil.reportError("uuid为空");
            }
            SharePreUtil3.putString(SharePreUtil3.UUID_KEY, uuid);
        }
        return uuid;
    }

    private static String getAndroidId() {
        //如果androidid不为空,直接返回andoridid
        String androidId = "";
        try {
            androidId = Settings.System.getString(ApplicationContext.getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }

        if (!TextUtils.isEmpty(androidId)) {
            return androidId;
        }

        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.SERIAL;
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取渠道
     *
     * @return
     */
    public static String getChannel() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = ApplicationContext.getContext().getPackageManager()
                    .getApplicationInfo(ApplicationContext.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("UMENG_CHANNEL");

            if (TextUtils.isEmpty(msg)) {
                return "0";
            }
            switch (msg) {
                case "yingyongbao":
                    return "2";
                case "oppo":
                    return "3";
                case "vivo":
                    return "4";
                case "xiaomi":
                    return "5";
                case "huawei":
                    return "6";
                default:
                    return "0";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取渠道
     *
     * @return
     */
    public static String getChannelName() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = ApplicationContext.getContext().getPackageManager()
                    .getApplicationInfo(ApplicationContext.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("UMENG_CHANNEL");

            return msg;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "7";
    }


    /**
     * 获取渠道
     *
     * @return
     */
    public static String getEnglishNameChannel() {
        ApplicationInfo appInfo = null;
        String otherName = "";
        try {
            appInfo = ApplicationContext.getContext().getPackageManager()
                    .getApplicationInfo(ApplicationContext.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("UMENG_CHANNEL");

            if (TextUtils.isEmpty(msg)) {
                return "Official";
            }
            switch (msg) {
                case "yingyongbao":
                    return "ApplicationTreasure";
                case "oppo":
                    return "Oppo";
                case "vivo":
                    return "Vivo";
                case "xiaomi":
                    return "MIUI";
                case "huawei":
                    return "Huawei";
                default:
                    return otherName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return otherName;
    }

    /**
     * 复制文本到粘贴板
     */
    public static void copyToClip(Context context, String content) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    public static void jumpNotificationSettings(Activity activity) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", activity.getPackageName());
            intent.putExtra("app_uid", activity.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(intent);
    }
}
