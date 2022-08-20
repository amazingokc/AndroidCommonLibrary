package com.runde.commonlibrary.customview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.utils.DisplayUtil;

public class CustomDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private int orientation;

    //分割线的缩进值
    private final int leftInset;
    private final int rightInset;
    private final int topInset;
    private final int bottomInset;

    private final Paint linePaint;

    //分割线的高度（垂直模式时设置）
    private final int lineHeight;
    //分割线的宽度（水平模式时设置）
    private final int lineWidth;

    //首尾Item的偏移量
    private final int firstItemTopOffsets;
    private final int lastItemBottomOffsets;
    private final int firstItemLeftOffsets;
    private final int lastItemRightOffsets;

    //是否绘制最后一条分割线
    private boolean isDrawLastLine;

    public static class Builder {
        private int orientation = LinearLayoutManager.VERTICAL;
        private int lineColor = ApplicationContext.getColor(R.color.transparent);

        //分割线的缩进值
        private int leftInset;
        private int rightInset;
        private int topInset;
        private int bottomInset;

        //分割线的高度（垂直模式时设置）
        private int lineHeight = DisplayUtil.dip2px(1f);
        //分割线的宽度（水平模式时设置）
        private int lineWidth = DisplayUtil.dip2px(1f);

        //首尾Item的偏移量
        private int firstItemTopOffsets;
        private int lastItemBottomOffsets;
        private int firstItemLeftOffsets;
        private int lastItemRightOffsets;

        //是否绘制最后一条分割线
        private boolean isDrawLastLine;

        public Builder(@RecyclerView.Orientation int orientation) {
            this.orientation = orientation;
        }

        public CustomDecoration build() {
            return new CustomDecoration(this);
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setDrawLastLine(boolean drawLastLine) {
            isDrawLastLine = drawLastLine;
            return this;
        }

        //设置分割线的颜色（默认透明）
        public Builder setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        //设置第一条item的偏移量（垂直模式时可调用）
        public Builder setFirstItemTopOffsets(float firstItemTopOffsets) {
            this.firstItemTopOffsets = DisplayUtil.dip2px(firstItemTopOffsets);
            return this;
        }

        //设置第一条item的偏移量（水平模式时可调用）
        public Builder setFirstItemLeftOffsets(float firstItemLeftOffsets) {
            this.firstItemLeftOffsets = DisplayUtil.dip2px(firstItemLeftOffsets);
            return this;
        }

        //设置最后一条item的偏移量（垂直模式时可调用）
        public Builder setLastItemBottomOffsets(float lastItemBottomOffsets) {
            this.lastItemBottomOffsets = DisplayUtil.dip2px(lastItemBottomOffsets);
            return this;
        }

        //设置最后一条item的偏移量（水平模式时可调用）
        public Builder setLastItemRightOffsets(float lastItemRightOffsets) {
            this.lastItemRightOffsets = DisplayUtil.dip2px(lastItemRightOffsets);
            return this;
        }

        //设置分割线的高度（垂直模式时可调用）
        public Builder setLineHeight(float lineHeight) {
            this.lineHeight = DisplayUtil.dip2px(lineHeight);
            return this;
        }

        //设置分割线的宽度（水平模式时可调用）
        public Builder setLineWidth(float lineWidth) {
            this.lineWidth = DisplayUtil.dip2px(lineWidth);
            return this;
        }

        //设置分割线的头部缩进（水平模式时可调用）
        public Builder setTopInset(float topInset) {
            this.topInset = DisplayUtil.dip2px(topInset);
            return this;
        }

        //设置分割线底部的缩进（水平模式时可设置）
        public Builder setBottomInset(float bottomInset) {
            this.bottomInset = DisplayUtil.dip2px(bottomInset);
            return this;
        }

        //设置分割线左边的缩进（垂直模式时可设置）
        public Builder setLeftInset(float leftInset) {
            this.leftInset = DisplayUtil.dip2px(leftInset);
            return this;
        }

        //设置分割线右边的缩进（垂直模式时可设置）
        public Builder setRightInset(float rightInset) {
            this.rightInset = DisplayUtil.dip2px(rightInset);
            return this;
        }
    }

    private CustomDecoration(Builder builder) {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(true);

        orientation = builder.orientation;
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = builder.orientation;
        int lineColor = builder.lineColor;
        linePaint.setColor(lineColor);
        leftInset = builder.leftInset;
        rightInset = builder.rightInset;
        topInset = builder.topInset;
        bottomInset = builder.bottomInset;
        lineHeight = builder.lineHeight;
        lineWidth = builder.lineWidth;
        firstItemTopOffsets = builder.firstItemTopOffsets;
        lastItemBottomOffsets = builder.lastItemBottomOffsets;
        firstItemLeftOffsets = builder.firstItemLeftOffsets;
        lastItemRightOffsets = builder.lastItemRightOffsets;
        isDrawLastLine = builder.isDrawLastLine;
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        int drawCount;
        if (isDrawLastLine) {
            drawCount = childCount;
        } else  {
            //最后一个item不画分割线
            drawCount = childCount - 1;
        }
        for (int i = 0; i < drawCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + lineHeight;
            c.drawRect(left + leftInset, top, right - rightInset, bottom, linePaint);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        int drawCount;
        if (isDrawLastLine) {
            drawCount = childCount;
        } else  {
            //最后一个item不画分割线
            drawCount = childCount - 1;
        }
        for (int i = 0; i < drawCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + lineWidth;
            c.drawRect(left, top + topInset, right, bottom - bottomInset, linePaint);
        }
    }

    //设置Item偏移量
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int adapterPosition = parent.getChildAdapterPosition(view);
        if (orientation == VERTICAL_LIST) {//垂直模式
            if (adapterPosition == 0) {
                //首项
                outRect.set(0, firstItemTopOffsets, 0, lineHeight);
            } else if (adapterPosition == state.getItemCount() - 1) {
                //尾项
                outRect.set(0, 0, 0, lastItemBottomOffsets);
            } else {
                outRect.set(0, 0, 0, lineHeight);
            }
        } else {//水平模式
            if (adapterPosition == 0) {
                //首项
                outRect.set(firstItemLeftOffsets, 0, lineWidth, 0);
            } else if (adapterPosition == state.getItemCount() - 1) {
                //尾项
                outRect.set(0, 0, lastItemRightOffsets, 0);
            } else {
                outRect.set(0, 0, lineWidth, 0);
            }
        }
    }
}
