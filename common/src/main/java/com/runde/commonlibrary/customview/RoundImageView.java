package com.runde.commonlibrary.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.runde.commonlibrary.R;

/**
 * Created by zhengmeixiong on 2018-04-26.
 */

public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    float width, height;
    int mRadius;
    private Context mContext;
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mRadius = (int) typedArray.getDimension(R.styleable.RoundImageView_mRadius, 8);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > mRadius && height > mRadius) {
            Path path = new Path();
            path.moveTo(mRadius, 0);
            path.lineTo(width - mRadius, 0);
            path.quadTo(width, 0, width, mRadius);
            path.lineTo(width, height - mRadius);
            path.quadTo(width, height, width - mRadius, height);
            path.lineTo(mRadius, height);
            path.quadTo(0, height, 0, height - mRadius);
            path.lineTo(0, mRadius);
            path.quadTo(0, 0, mRadius, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
