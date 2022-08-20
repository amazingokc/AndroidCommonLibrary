package com.core.commonlibrary.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-08-08 下午 12:02
 * 文件描述：图片数据绑定
 */
public class ImagBindingUtils {

    @BindingAdapter({"imgUrlCircleUrl", "error"})
    public static void loadCircleImage(ImageView imageView, String url, Drawable error) {
        ImageLoaderUtil.getInstance().loadCircleImg(imageView, url, null, error);
    }

    @BindingAdapter({"imgUrlNormalUrl", "placeHolder", "error"})
    public static void loadNormalImage(ImageView imageView, String url, Drawable placeHolder, Drawable error) {
        ImageLoaderUtil.getInstance().loadNormalImg(imageView, url, placeHolder, error);
    }
}
