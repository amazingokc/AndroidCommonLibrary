package com.core.commonlibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class HardwareUtil {
    private static final String TAG = "HardwareUtil";

    private HardwareUtil() {
        throw new Error("我是工具类,不要实例化我哦");
    }

    private static long[] romInfo = new long[2];
    private static double inch = 0;

    /**
     * 设备信息
     **/
    public static String getHardwareInfo(Activity activity) {
        DisplayMetrics displayMetrics = activity.getApplicationContext().getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int densityDpi = displayMetrics.densityDpi;
        int x_Pix = displayMetrics.widthPixels;
        int y_Pix = displayMetrics.heightPixels;
        float x_Dpi = displayMetrics.xdpi;
        float y_Dpi = displayMetrics.ydpi;
        int bar = getStatusHeight(activity);

        WifiManager wifiManager = (WifiManager) activity.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String mac = "";
        if (null != wifiInfo.getMacAddress()) {
            mac = wifiInfo.getMacAddress();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n+++++++++++++++++++++++++硬件信息+++++++++++++++++++++++++");
        sb.append("\n屏幕尺寸: " + getScreenInch(activity) + "\"");
        sb.append("\n逻辑密度: " + density);
        sb.append("\n物理密度: " + densityDpi);
        sb.append("\nX密度: " + x_Dpi);
        sb.append("\nY密度: " + y_Dpi);
        sb.append("\nX长度: " + x_Pix + "px; " + px2dip(density, x_Pix) + "dp");
        sb.append("\nY高度: " + y_Pix + "px; " + px2dip(density, y_Pix) + "dp");
        sb.append("\nBar高度: " + bar + "px; " + px2dip(density, bar) + "dp");
        sb.append("\nMAC地址: " + mac);
        sb.append("\nCPU_ABI: " + Build.CPU_ABI);
        sb.append("\nCPU_ABI2: " + Build.CPU_ABI2);
        sb.append("\nVERSION SDK: " + Build.VERSION.SDK);
        sb.append("\nVERSION CODENAME: " + Build.VERSION.CODENAME);
        sb.append("\nFIRMWARE VERSION RELEASE: " + Build.VERSION.RELEASE);//手机系统版本
        sb.append("\nSYSTEM VERSION: " + Build.DISPLAY);
        sb.append("\n厂商: " + Build.MANUFACTURER);// 手机厂商
        sb.append("\nEither a changelist number: " + Build.ID);
        sb.append("\nPRODUCT: " + Build.PRODUCT);
        sb.append("\nDEVICE: " + Build.DEVICE);
        sb.append("\nBOARD: " + Build.BOARD);
        sb.append("\nBRAND: " + Build.BRAND);
        sb.append("\nMODEL: " + Build.MODEL);
        sb.append("\nBOOTLOADER: " + Build.BOOTLOADER);
        sb.append("\nRADIO: " + Build.RADIO);
        sb.append("\nHARDWARE: " + Build.HARDWARE);
        sb.append("\nSERIAL: " + Build.SERIAL);
        sb.append("\nVERSION INCREMENTAL: " + Build.VERSION.INCREMENTAL);
//        sb.append("\nROM总共: " + StringUtil.formatSize2(getRomMemroy()[0]));
//        sb.append("\nROM可用: " + StringUtil.formatSize2(getRomMemroy()[1]));
        sb.append("\nCPU型号: " + getCpuName());
        sb.append("\nCPU序列号(16位): " + getCpuNO());
        sb.append("\nCPU当前频率（单位KHZ）: " + getCurCpuFreq());
        sb.append("\nCPU最小频率（单位KHZ）: " + getMinCpuFreq());
        sb.append("\nCPU最大频率（单位KHZ）: " + getMaxCpuFreq());
        sb.append("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        LLog.i(TAG, sb.toString());
        return sb.toString();
    }

    /**
     * 手机基本信息
     **/
    public static String getPhoneInfo(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n\n+++++++++++++++++++++++++手机信息+++++++++++++++++++++++++");
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            sb.append("\nDeviceId(国际移动设备身份码IMEI): " + tm.getDeviceId());
//            sb.append("\nDeviceSoftwareVersion: " + tm.getDeviceSoftwareVersion());
//            sb.append("\nLine1Number(电话号码): " + tm.getLine1Number());
            sb.append("\nNetworkCountryIso: " + tm.getNetworkCountryIso());
            sb.append("\nNetworkType: " + tm.getNetworkType());
            sb.append("\nPhoneType: " + tm.getPhoneType());
            sb.append("\nSimCountryIso: " + tm.getSimCountryIso());
            sb.append("\nSimOperator: " + tm.getSimOperator());
            sb.append("\nSimOperatorName: " + tm.getSimOperatorName());
            sb.append("\nSimSerialNumber: " + tm.getSimSerialNumber());
            sb.append("\nVoiceMailNumber: " + tm.getVoiceMailNumber());
            sb.append("\nSimState: " + tm.getSimState());
            String imsi = tm.getSubscriberId();
            if (null != imsi) {
                sb.append("\nSubscriberId(国际移动用户识别码IMSI): " + imsi);
                sb.append("\nNetworkOperator(移动运营商编号): " + tm.getNetworkOperator());
                sb.append("\nNetworkOperatorName(移动运营商名称): " + tm.getNetworkOperatorName());
                // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信
                if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                    sb.append("\n移动运营商名称:  中国移动");
                } else if (imsi.startsWith("46001")) {
                    sb.append("\n移动运营商名称:  中国联通");
                } else if (imsi.startsWith("46003")) {
                    sb.append("\n移动运营商名称:  中国电信");
                } else {
                    sb.append("\n移动运营商:  其他");
                }
            }
            sb.append("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            LLog.i(TAG, sb.toString());
        }

        return sb.toString();
    }


    /**
     * 内存RAM
     **/
    public static String getRAMTotalMemory() {
        String str1 = "/proc/meminfo";
        String ram = "";
        try {
            FileReader fileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fileReader, 8192);
            while (null != (ram = localBufferedReader.readLine())) {
                LLog.i(TAG, "RAM,第一行是总ram大小(用户可用的大小): " + ram);
            }
        } catch (IOException e) {
            LLog.e(TAG, e.getMessage());
        }
        return ram;
    }

    /**
     * Rom大小
     **/
    public static long[] getRomMemroy() {
        long[] romInfo = new long[2];
        romInfo[0] = getTotalInternalMemorySize();
        // Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
//        LLog.i(TAG, "+++++++++++++++++++++++ ROM +++++++++++++++++++++++++++++" + //
//                "\n 总共ROM: " + StringUtil.formatSize2(romInfo[0]) + //
//                "\n 可用ROM: " + StringUtil.formatSize2(romInfo[1]));
        return romInfo;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取CPU名字
     **/
    public static String getCpuName() {
        String cpuName = "";
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            cpuName = array[1];
        } catch (Exception e) {
            LLog.e(TAG, e.getMessage());
        }
        LLog.i(TAG, "CPU名字: " + cpuName);
        return cpuName;
    }

    /**
     * 获取CPU序列号(16位)
     **/
    public static String getCpuNO() {
        String str;
        String cpuNo = "0000000000000000";
        String cpuAddress = "0000000000000000";
        try {
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {  // 查找到序列号所在行
                    if (str.indexOf("Serial") > -1) {
                        cpuNo = str.substring(str.indexOf(":") + 1, str.length());  // 提取序列号
                        cpuAddress = cpuNo.trim(); // 去空格
                        break;
                    }
                } else { // 文件结尾
                    break;
                }
            }
        } catch (Exception e) {
            LLog.i(TAG, e.getMessage());
        }
        LLog.i(TAG, "cpuNo: " + cpuNo);
        LLog.i(TAG, "cpuAddress: " + cpuAddress);
        return cpuAddress;
    }

    /**
     * 获取CPU最小频率（单位KHZ）
     **/
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        LLog.i(TAG, "获取CPU最小频率: " + result.trim() + "KHZ");
        return result.trim();
    }

    /**
     * 实时获取CPU当前频率（单位KHZ）
     **/
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LLog.i(TAG, "实时获取CPU当前频率: " + result + "KHZ");
        return result;
    }

    /**
     * 获取CPU最大频率（单位KHZ）
     **/
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        LLog.i(TAG, "获取CPU最大频率: " + result.trim() + "KHZ");
        return result.trim();
    }

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                LLog.e(TAG, e.getMessage());
            }
        }
        LLog.i(TAG, "StatusHeight: " + statusHeight);
        return statusHeight;
    }


    public static int dip2px(float scale, float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float scale, float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕尺寸
     **/
    public static double getScreenInch(Activity activity) {
        if (inch != 0.0d) {
            return inch;
        }
        try {
            int realWidth = 0, realHeight = 0;
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (Build.VERSION.SDK_INT < 17 && Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }
            inch = formatDouble(Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi)), 1);
        } catch (Exception e) {
            LLog.e(TAG, e.getMessage());
        }
        return inch;
    }


    /**
     * Double类型保留指定位数的小数，返回double类型（四舍五入）
     * newScale 为指定的位数
     */
    private static double formatDouble(double d, int newScale) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
