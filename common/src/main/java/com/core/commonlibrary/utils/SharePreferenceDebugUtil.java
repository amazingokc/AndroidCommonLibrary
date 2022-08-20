package com.core.commonlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.core.commonlibrary.global.ApplicationContext;

/**
 * Debug版用的
 */
public class SharePreferenceDebugUtil {

    public static final String PREFERENCE_NAME = "runde_debug_preferences";

    public static final String OPENLEAKCANARYSWITCH_KEY = "OPENLEAKCANARYSWITCH_KEY";
    public static final String BASE_URL_KEY = "BASE_URL_KEY";
    public static final String BASE_PIC_URL_KEY = "BASE_PIC_URL_KEY";


    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = ApplicationContext.getContext().getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean putString(String key, String value) {
        SharedPreferences settings = ApplicationContext.getContext().getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences settings = ApplicationContext.getContext().getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }
}
