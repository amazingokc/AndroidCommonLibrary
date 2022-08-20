package com.core.commonlibrary.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;


import com.core.commonlibrary.R;
import com.core.commonlibrary.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的指示器
 * Created by Administrator on 2018/12/3 0003.
 */

public class PageIndicatorView extends LinearLayout {
    private Context mContext = null;
    private int dotSize = 4; // 指示器的大小（dp）
    private int margins = 2; // 指示器间距（dp）
    private List<View> indicatorViews = null; // 存放指示器
    private int mIndicatorSelectedResId = R.drawable.banner_white_radius;
    private int mIndicatorUnselectedResId = R.drawable.banner_gray_radius;

    public PageIndicatorView(@NonNull Context context) {
        this(context, null);
    }

    public PageIndicatorView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        dotSize = DensityUtil.dip2px(dotSize);
        margins = DensityUtil.dip2px(margins);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageIndicatorView);
        dotSize = (int) typedArray.getDimension(R.styleable.PageIndicatorView_size, dotSize);
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.PageIndicatorView_drawable_selected, mIndicatorSelectedResId);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.PageIndicatorView_drawable_unselected,mIndicatorUnselectedResId);
        typedArray.recycle();
    }

    /**
     * 初始化指示器，默认选中第一页
     *
     * @param count 指示器数量，即页数
     */
    public void initIndicator(int count) {

        if (indicatorViews == null) {
            indicatorViews = new ArrayList<>();
        } else {
            indicatorViews.clear();
            removeAllViews();
        }
        ImageView imageView;
        LayoutParams params = new LayoutParams(dotSize, dotSize);
        params.setMargins(margins, margins, margins, margins);
        for (int i = 0; i < count; i++) {
            imageView = new ImageView(mContext);
            imageView.setImageResource(mIndicatorUnselectedResId);
            addView(imageView, params);
            indicatorViews.add(imageView);
        }
        if (indicatorViews.size() > 0) {
            ImageView imageView1 = (ImageView) indicatorViews.get(0);
            imageView1.setImageResource(mIndicatorSelectedResId);
        }
    }

    /**
     * 设置选中页
     *
     * @param selected 页下标，从0开始
     */
    public void setSelectedPage(int selected) {
        if(indicatorViews==null)return;
        for (int i = 0; i < indicatorViews.size(); i++) {
            ImageView imageView = (ImageView) indicatorViews.get(i);
            if (i == selected) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
        }
    }

}
