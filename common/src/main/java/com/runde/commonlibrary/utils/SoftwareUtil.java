package com.runde.commonlibrary.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.global.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class SoftwareUtil {
    private static final String TAG = "SoftwareUtil";

    private SoftwareUtil() {
        throw new Error("我是工具类,不要实例化我哦");
    }


    /**
     * APK基本信息
     **/
    public static String getAPKInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n\n+++++++++++++++++++++++++APK信息+++++++++++++++++++++++++");
        try {
            String pkName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkName, PackageManager.GET_PERMISSIONS | PackageManager.GET_SIGNATURES | PackageManager.GET_SERVICES | PackageManager.GET_RECEIVERS | PackageManager.GET_ACTIVITIES);
            int labelRes = packageInfo.applicationInfo.labelRes;
            String appName = context.getResources().getString(labelRes);
            sb.append("\nappName(应用名):" + appName);
            sb.append("\npackage(应用包):" + pkName);
            sb.append("\nversionCode(版本号):" + packageInfo.versionCode);
            sb.append("\nversionName(版本名):" + packageInfo.versionName);

            Signature[] signs = packageInfo.signatures;
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signs[0].toByteArray()));
            sb.append("\n签名信息: ");
            sb.append("\nsignName: " + cert.getSigAlgName());
            sb.append("\nsignNumber: " + cert.getSerialNumber().toString());
            sb.append("\npubKey: " + cert.getPublicKey().toString());
            sb.append("\nsubjectDN: " + cert.getSubjectDN().toString());

            ActivityInfo[] activities = packageInfo.activities;
            if (null != activities && activities.length > 0) {
                sb.append("\nactivity信息: ");
                for (ActivityInfo activityInfo : activities) {
                    sb.append(activityInfo.toString());
                }
            }
            activities = packageInfo.receivers;
            if (null != activities && activities.length > 0) {
                sb.append("\nreceiver信息: ");
                for (ActivityInfo receiver : activities) {
                    sb.append("\nreceiver: " + receiver.name);
                }
            }
            ServiceInfo[] serviceInfos = packageInfo.services;
            if (null != serviceInfos && serviceInfos.length > 0) {
                sb.append("\nservice信息: ");
                for (ServiceInfo serviceInfo : serviceInfos) {
                    sb.append("\nservice: " + serviceInfo.name);
                }
            }
        } catch (Exception e) {
            LLog.e(TAG, e.getMessage());
        }
        sb.append("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        LLog.i(TAG, sb.toString());
        return sb.toString();
    }


}
