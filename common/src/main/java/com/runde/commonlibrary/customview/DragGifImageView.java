package com.runde.commonlibrary.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.runde.commonlibrary.utils.LLog;

import pl.droidsonroids.gif.GifImageView;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-05-09 上午 11:46
 * 文件描述：可拖动Gif控件
 */

public class DragGifImageView extends GifImageView {

    private int parentHeight;
    private int parentWidth;

    public DragGifImageView(Context context) {
        super(context);
    }

    public DragGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragGifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DragGifImageView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
    }

    private int lastX;
    private int lastY;

    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        LLog.d("MotionEventttt", event.getAction());
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                LLog.i("aa", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
                break;
            case MotionEvent.ACTION_UP:
                if (!isNotDrag()) {
                    //恢复按压效果
                    setPressed(false);
                    //Log.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
//                    if (rawX >= parentWidth / 2) {
//                        //靠右吸附
//                        animate().setInterpolator(new DecelerateInterpolator())
//                                .setDuration(500)
//                                .xBy(parentWidth - getWidth() - getX())
//                                .start();
//                    } else {
//                        //靠左吸附
//                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
//                        oa.setInterpolator(new DecelerateInterpolator());
//                        oa.setDuration(500);
//                        oa.start();
//                    }
                }
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    private boolean isNotDrag() {
//        LLog.d("dsadasww", "isDrag:" + isDrag
//                + "    getX():" + getX()
//                + "     parentWidth:" + parentWidth
//                + "     getWidth():" + getWidth());
//        return !isDrag && (getX() == 0
//                || (getX() == parentWidth - getWidth()));
        return !isDrag;
    }
}
