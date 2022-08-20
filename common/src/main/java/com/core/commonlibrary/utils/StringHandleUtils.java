package com.core.commonlibrary.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 *  用于字符串的处理
 * Created by hua on 2019/8/15 0015.
 */
public class StringHandleUtils {

    /**  将一个以一个标志隔开由数字组成的字符串转成int集合
     * @param handledStr
     * @param flag  标志
     * @return
     */
    public static ArrayList getIntList(String handledStr, String flag){
        if (!TextUtils.isEmpty(handledStr)){
            try {
                String[] strArray = handledStr.split(flag);
                ArrayList<Integer> intList = new ArrayList<>();
                for (String str : strArray){
                    int handleInt = Integer.parseInt(str);
                    intList.add(handleInt);
                }
                return intList;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }
}
