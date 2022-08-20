package com.core.commonlibrary.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.core.commonlibrary.R;
import com.core.commonlibrary.utils.DisplayUtil;

/**
 * desc:
 * date:2021/10/19
 * by:xiaoguoqing
 */
public class ShadowView extends FrameLayout {
    public ShadowView(Context context) {
        super(context);
        initView(context);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.shadow_bottom, null);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(0, DisplayUtil.dip2px(5));
    }
}
