package com.runde.commonlibrary.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.runde.commonlibrary.utils.ToastUtil;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-08-22 上午 11:04
 * 文件描述：防止出现刷新问题导致 java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 150(offset:150)
 */
public class RecyclerViewNoBugLinearLayoutManager extends LinearLayoutManager {
    public RecyclerViewNoBugLinearLayoutManager(Context context) {
        super( context );
    }

    public RecyclerViewNoBugLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super( context, orientation, reverseLayout );
    }

    public RecyclerViewNoBugLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren( recycler, state );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            ToastUtil.showDebug(e.getMessage());
        }

    }
}

