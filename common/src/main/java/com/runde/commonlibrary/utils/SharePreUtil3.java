package com.runde.commonlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.runde.commonlibrary.global.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Create by: xiaoguoqing
 * Date: 2020/04/01
 */
public class SharePreUtil3 {

    public static final String PRIVACY_POLICY_PRE_KEY = "PRIVACY_POLICY_PRE_KEY"; //隐私协议是否同意
    /**
     * 将项目的所有SharePreference都保存在PREFERENCE_NAME文件下
     */
    private static final String PREFERENCE_NAME = "pref-file3";

    /**
     * key
     */
    public static final String LOGIN_ACCOUNT_KEY = "LOGIN_ACCOUNT_KEY";
    public static final String GOODSTYPE_KEY = "GOODSTYPE_KEY";
    public static final String GOODSTYPES_KEY = "GOODSTYPES_KEY";
    public static final String GOODS_TK_TYPES_KEY = "GOODS_TK_TYPES_KEY";
    public static final String WATCHRECORD_KEY = "WATCHRECORD_KEY";  //看课记录
    public static final String LIVE_WATCHRECORD_KEY = "LIVE_WATCHRECORD_KEY";  //直播回放的看课记录
    public static final String WEB_TOKEN_KEY = "WEB_TOKEN_KEY";  //webToken
    public static final String USER_ID_KEY = "userId";  //
    public static final String USER_PHONE_KEY = "userPhone";  //用户手机

    public static final String WATCHRECORD_LIST_KEY = "WATCHRECORD_LIST_KEY";  //看课记录
    public static final String WATCHRECORD_LIVE_LIST_KEY = "WATCHRECORD_LIVE_LIST_KEY";  //直播看课记录
    public static final String WATCHRECORD_PLAYBACK_LIST_KEY = "WATCHRECORD_PLAYBACK_LIST_KEY";  //回放看课记录
    public static final String TLSELECTEDGOODTYPEID = "TLSELECTEDGOODTYPEID_KEY";//goodTypeId
    public static final String TLSELECTED_TKGOODTYPEID = "TLSELECTED_TKGOODTYPEID_KEY";//goodTypeId

    public static final String APP_KEY = "appKey";
    public static final String IS_REQUEST_CUR_NOT_PROTOCOL = "IS_REQUEST_CUR_NOT_PROTOCOL";//这次登录没有协议

    static final String UUID_KEY = "UUID_KEY";

    public static final String ISALLOWONELOGIN = "ISALLOWONELOGIN"; //是否允许一键登录

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static SharedPreferences getSecurePreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = ApplicationContext.getContext().getSharedPreferences(
                    PREFERENCE_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharedPreferences;
    }


    public static void putBoolean(String key, boolean value) {
        getSecurePreferences();
        editor.putBoolean(hashPrefKey(key), value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String s = hashPrefKey(key);
        boolean b;
        try {
            b = getSecurePreferences().getBoolean(s, defaultValue);
        } catch (Exception e) {
            b = defaultValue;
            LLog.e("getBoolean失败");
        }
        return b;
    }

    public static void putString(String key, String value) {
        getSecurePreferences();
        editor.putString(hashPrefKey(key), value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return getSecurePreferences().getString(hashPrefKey(key), defaultValue);
    }

    public static void putInt(String key, int value) {
        getSecurePreferences();
        editor.putInt(hashPrefKey(key), value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return getSecurePreferences().getInt(hashPrefKey(key), defaultValue);
    }

    public static void putLong(String key, long value) {
        getSecurePreferences();
        editor.putLong(hashPrefKey(key), value).apply();
    }

    public static long getLong(String key, long defaultValue) {
        return getSecurePreferences().getLong(hashPrefKey(key), defaultValue);
    }

    //清空该文件的所有数据(不要随意调用)
    public static void clearPreference() {
        SharedPreferences settings = ApplicationContext.getContext().getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    private static String hashPrefKey(String prefKey) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = prefKey.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);

            return Base64.encodeToString(digest.digest(), AesCbcWithIntegrity.BASE64_FLAGS);

        } catch (NoSuchAlgorithmException e) {
            LLog.w("hashPrefKey", "Problem generating hash", e);
        } catch (UnsupportedEncodingException e) {
            LLog.w("hashPrefKey", "Problem generating hash", e);
        }
        return null;
    }

}
