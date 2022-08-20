package com.runde.commonlibrary.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.OkHttpUtils;
import com.runde.commonlibrary.net.deprecated_net.module_okhttp.httpManger.callback.FileCallBack;
import com.runde.commonlibrary.utils.LLog;
import com.runde.commonlibrary.utils.ToastUtil;

import java.io.File;

import okhttp3.Call;


/**
 * 自动下载更新apk服务
 * Create by: chenwei.li
 * Date: 2016-08-14
 * time: 09:50
 * Email: lichenwei.me@foxmail.com
 */
public class DownloadService extends Service {

    public static final String update_action = "com.runde.commonlibrary.server.DownloadService";
    private String mDownloadUrl;//APK的下载路径
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private int INSTALL_APK_REQUESTCODE = 33;
    private NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Log.i("onStartCommand", "onStartCommand: 1111111");
            notifyMsg("温馨提醒", "文件下载失败", 0);
            stopSelf();
        } else {
            Log.i("onStartCommand", "onStartCommand: 2222222");
            mDownloadUrl = intent.getStringExtra("apkUrl");//获取下载APK的链接
            downloadFile(mDownloadUrl);//下载APK
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void notifyMsg(String title, String content, int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ApplicationContext.getContext().getPackageName(),
                    "Channel1", NotificationManager.IMPORTANCE_LOW);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            channel.setSound(null, null);
            channel.enableVibration(false);
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, ApplicationContext.getContext().getPackageName());//为了向下兼容，这里采用了v7包下的NotificationCompat来构造
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(ServiceFactory.getInstance().getCommonService().getAppSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        ServiceFactory.getInstance().getCommonService().getAppLargeIcon()))
                .setContentTitle(title);
//        builder.setContentTitle(title);

        if (progress > 0 && progress < 100) {
            //下载进行中
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setContentIntent(getDefalutIntent(progress, Notification.FLAG_AUTO_CANCEL));
        builder.setWhen(System.currentTimeMillis());
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentText(content);
        builder.setSound(null);
        builder.setVibrate(new long[0]);
        builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        if (progress >= 100) {
            //下载完成
//            builder.setContentIntent(getInstallIntent());
            getInstallIntent();
        }
        mNotification = builder.build();
        mNotificationManager.notify(0, mNotification);
    }

    private PendingIntent getDefalutIntent(int progress, int flags) {
        String cachePath = (
                getExternalFilesDir("update_apk") +
                        File.separator +
                        getPackageName() +
                        ".apk");
        File file = new File(cachePath);
        Intent installApkIntent = new Intent();

        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installApkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),
                    getPackageName() + ".fileprovider", file), "application/vnd.android.package-archive");
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        if (progress == 100) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, installApkIntent, flags);
            return pendingIntent;
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
            return pendingIntent;
        }
    }


    /**
     * 安装apk文件
     *
     * @return
     */
    private void getInstallIntent() {
        String cachePath = (
                getExternalFilesDir("update_apk") +
                        File.separator +
                        getPackageName() +
                        ".apk");
        File file = new File(cachePath);
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),
                    getPackageName() + ".fileprovider", file), "application/vnd.android.package-archive");
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        if (getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            startActivity(installApkIntent);
        }
    }

    /**
     * 下载apk文件
     *
     * @param url
     */
    private void downloadFile(String url) {
        if (!TextUtils.isEmpty(url)) {

            File apkFile = new File(getExternalFilesDir("update_apk"),
                    File.separator + getPackageName() + ".apk");

            if (apkFile != null && apkFile.exists()) {
                apkFile.delete();
            }

            OkHttpUtils.get().url(url).build().execute(new FileCallBack(String.valueOf(getExternalFilesDir("update_apk")), File.separator +
                    getPackageName() +
                    ".apk") {
                @Override
                public void onError(Call call, Exception e, int id) {
                    notifyMsg("温馨提醒", "文件下载失败", 0);
                    Intent intent = new Intent();
                    intent.putExtra("strTip", "下载失败，请检测手机\n内存空间或网络环境，稍后再试。");
                    intent.putExtra("isFailed", true);
                    intent.setAction(update_action);
                    sendBroadcast(intent);
                    stopSelf();
                }

                @Override
                public void onResponse(File response, int id) {
                    //当文件下载完成后回调
                    downProgress = 100;
                    handler.sendEmptyMessage(100);
                    notifyMsg("温馨提醒", "文件下载已完成", 100);
                    stopSelf();
                }

                @Override
                public void inProgress(final float progress, long total, int id) {
                    LLog.d("inProgresss1", "" + progress);
                    //progress*100为当前文件下载进度，total为文件大小
                    if ((int) (progress * 100) % 10 == 0) {
                        downProgress = (int) (progress * 100);
                        if (isFirstNotify) {
                            isFirstNotify = false;
                            handler.sendEmptyMessageDelayed(100, 1);
                        }
                    }
                }
            });
        } else  {
            ToastUtil.show("未获取到下载链接");
            stopSelf();
        }
    }

    private boolean isFirstNotify = true;
    private int downProgress;

    @SuppressWarnings("all")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    notifyMsg("温馨提醒", "正在下载新版本..", downProgress);
                    Intent intent = new Intent();
                    intent.putExtra("count", downProgress);
                    intent.setAction(update_action);
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(100, 1000);
                    if (downProgress == 100) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;
            }
        }
    };


}