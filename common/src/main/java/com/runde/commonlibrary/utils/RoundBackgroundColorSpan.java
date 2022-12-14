package com.runde.commonlibrary.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import com.runde.commonlibrary.global.ApplicationContext;

/**
 * SpannableString 设置文字圆角背景
 * Created by hua on 2019/2/16 0016.
 */

public class RoundBackgroundColorSpan extends ReplacementSpan {


    private int mBgColorResId; //Icon背景颜色
    private String mText;  //Icon内文字
    private float mBgHeight;  //Icon背景高度
    private float mBgWidth;  //Icon背景宽度
    private float mRadius;  //Icon圆角半径
    private float mRightMargin; //右边距
    private float mTextSize; //文字大小
    private int mTextColorResId; //文字颜色

    private Paint mBgPaint; //icon背景画笔
    private Paint mTextPaint; //icon文字画笔
    private Context mContext;

    public RoundBackgroundColorSpan(int bgColorResId, int mTextColorResId, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //初始化默认数值
        initDefaultValue(bgColorResId, mTextColorResId, text);
        //计算背景的宽度
        this.mBgWidth = caculateBgWidth(text);
        //初始化画笔
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //初始化背景画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColorResId);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);

        //初始化文字画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColorResId);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 初始化默认数值
     */
    private void initDefaultValue(int bgColorResId, int mTextColorResId, String text) {
        mContext = ApplicationContext.getContext();
        this.mBgColorResId = bgColorResId;
        this.mText = text;
        this.mBgHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17f,
                mContext.getResources().getDisplayMetrics());
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                mContext.getResources().getDisplayMetrics());
        this.mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                mContext.getResources().getDisplayMetrics());
        this.mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                mContext.getResources().getDisplayMetrics());
        this.mTextColorResId = mTextColorResId;
    }

    /**
     * 计算icon背景宽度
     *
     * @param text icon内文字
     */
    private float caculateBgWidth(String text) {
        if (text.length() > 1) {
            //多字，宽度=文字宽度+padding
            Rect textRect = new Rect();
            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.getTextBounds(text, 0, text.length(), textRect);
            float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                    mContext.getResources().getDisplayMetrics());
            return textRect.width() + padding * 2;
        } else {
            //单字，宽高一致为正方形
            return mBgHeight;
        }
    }

    /**
     * 设置右边距
     *
     * @param rightMarginDpValue dp
     */
    public void setRightMarginDpValue(int rightMarginDpValue) {
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightMarginDpValue,
                ApplicationContext.getContext().getResources().getDisplayMetrics());
    }

    /**
     * 设置字体大小
     *
     * @param textSize sp
     *
     * */
    public void setTextSize(float textSize) {
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize,
                ApplicationContext.getContext().getResources().getDisplayMetrics());
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (mBgWidth + mRightMargin);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                     int bottom, Paint paint) {
        //画背景
        Paint bgPaint = new Paint();
        bgPaint.setColor(mBgColorResId);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        Paint.FontMetrics metrics = paint.getFontMetrics();

        float textHeight = metrics.descent - metrics.ascent;

        //算出背景开始画的y坐标
        float bgStartY = y + (textHeight - mBgHeight) / 2 + metrics.ascent;

        //画背景
        RectF bgRect = new RectF(x, bgStartY, x + mBgWidth, bgStartY + mBgHeight);
        canvas.drawRoundRect(bgRect, mRadius, mRadius, bgPaint);

        //把字画在背景中间
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(mTextColorResId);
        textPaint.setTextSize(mTextSize);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);  //这个只针对x有效
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textRectHeight = fontMetrics.bottom - fontMetrics.top;
        canvas.drawText(mText, x + mBgWidth / 2, bgStartY + (mBgHeight - textRectHeight) / 2
                - fontMetrics.top, textPaint);
    }
}
