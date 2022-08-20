package com.runde.commonlibrary.utils;

import android.os.Environment;
import android.os.UserManager;
import android.text.TextUtils;


import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通用工具类
 * Created by zmx
 */
public class CommonUtil {


    public static void myEncrypt(Map<String, Object> params) {

        //组成参数
        params.put("sign", getMd5Sign(params));
    }


    public static String getMd5Sign(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, Object> signParams = new HashMap<>();
            for (String key :
                    params.keySet()) {
                if (params.get(key) != null && !TextUtils.isEmpty(params.get(key).toString())) {
                    signParams.put(key, params.get(key));
                }
            }

            //按字典排序
            Set<String> allKeys = getAllKeys(signParams);
            List<String> list = new ArrayList<>(allKeys);
            Collections.sort(list);

            for (int i = 0; i < list.size(); i++) {

                builder.append(list.get(i));
                builder.append("=");
                builder.append(signParams.get(list.get(i)));
                if (i != list.size() - 1) {
                    builder.append("&");
                }

            }


            builder.append(getAppKey());
            //md5加密
            String md5Value = getMd5Value(builder.toString()).toUpperCase();
            return md5Value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String mAppKey = "";

    private static String getAppKey() {
        if (TextUtils.isEmpty(mAppKey)) {
            mAppKey = SharePreUtil3.getString(SharePreUtil3.APP_KEY, "");
        }
        return mAppKey;
    }

    /**
     * 得到JSONObject里的所有key
     *
     * @return Set
     */
    private static Set<String> getAllKeys(Map<String, Object> map) {
        return map.keySet();
    }

    /**
     * 从JSON字符串里得到所有key
     *
     * @param jsonString json字符串
     * @return Set
     */
    private static Set<String> getAllKeys(String jsonString) {
        HashMap map = JSON.parseObject(jsonString, HashMap.class);
        return map.keySet();
    }


    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();// 加密
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * MD5加密工具
     * Created by zmx
     *
     * @return
     */
    public static String string2MD5(String myinfo) {
        byte[] digesta = null;
        try {
            byte[] btInput = myinfo.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            digesta = mdInst.digest();
        } catch (Exception ex) {
            System.out.println("非法摘要算法");
        }
        return byte2hex(digesta);
    }

    private static String byte2hex(byte[] b) {
        StringBuilder md5Str = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                md5Str.append(0 + stmp);
            } else {
                md5Str.append(stmp);
            }
            if (n < b.length - 1) {
                md5Str.append("");
            }
        }
        return md5Str.toString();
    }

    /**
     * 获取sd卡路径
     * 双sd卡时，获得的是外置sd卡
     *
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedInputStream in = null;
        BufferedReader inBr = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            in = new BufferedInputStream(p.getInputStream());
            inBr = new BufferedReader(new InputStreamReader(in));


            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
        } catch (Exception e) {
            //return Environment.getExternalStorageDirectory().getPath();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inBr != null) {
                    inBr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    //手机号验证
    public static boolean checkCellphone(String cellphone) {
        //        boolean isPhoneNumber = false;
        //        if (TextUtils.isEmpty(cellphone)) {
        //            ToastUtil.show("请输入正确的手机号码");
        //            return isPhoneNumber;
        //        }
        //        if (cellphone.length() < 11) {
        //            ToastUtil.show("请输入正确的手机号码");
        //            return isPhoneNumber;
        //        }
        ////        String regex = "^1(3[0-9]|4[579]|5[0-35-9]|7[01356]|8[0-9])\\d{8}$";
        //        String regex = "^[1][0-9]{10}$";
        //
        //        try {
        //            Pattern pattern = Pattern.compile(regex);
        //            Matcher matcher = pattern.matcher(cellphone);
        //            isPhoneNumber = matcher.matches();
        //            if (!isPhoneNumber) {
        //                ToastUtil.show("请输入正确的手机号码");
        //            }
        //            return isPhoneNumber;
        //        } catch (Exception e) {
        //            ToastUtil.show("请输入正确的手机号码");
        //            return isPhoneNumber;
        //        }


        if (TextUtils.isEmpty(cellphone)) {
            ToastUtil.show("请输入正确的手机号码");
            return false;
        } else {
            return true;
        }

    }

    public static boolean isIdentityNumber(String identityNumber) {
        if (null == identityNumber || "".equals(identityNumber)) return false;
        Pattern p = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$");
        Matcher m = p.matcher(identityNumber);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 富文本适配
     */
    public static String getHtmlData(String bodyHTML) {
        String fontSize = DisplayUtil.sp2px(14) + "";
        String css = "<style> body{font-size: " + fontSize + ";} img{max-width:100%;width:100% !important;height:auto !important;min-height:10px;} p{margin-top:0 !important;margin-bottom:0 !important;}</style>";
        String html = "<html><header>" + css + "</header><body style='margin:0;padding:0'>" + bodyHTML + "</body></html>";
        return html;
    }

}