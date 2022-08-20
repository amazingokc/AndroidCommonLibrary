package com.runde.commonlibrary.utils;

import java.security.MessageDigest;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/27 0027
 * description:
 */
public class MD5Util {

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
}
