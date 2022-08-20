package com.runde.commonlibrary.customview;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-07-23 下午 4:04
 * 文件描述：
 */
public class WrapContentLinearLayoutManager  extends LinearLayoutManager {
    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //防止出现java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder
    // adapter positionViewHolder{431a7450 position=1 id=-1, oldPos=-1, pLpos:-1 scrap [attachedScrap] tmpDetached no parent}
    //---------------------
    //作者：moble_xie
    //来源：CSDN
    //原文：https://blog.csdn.net/lovexieyuan520/article/details/50537846
    //版权声明：本文为博主原创文章，转载请附上博文链接！
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
