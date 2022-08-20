package com.runde.commonlibrary.base;

import androidx.fragment.app.Fragment;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-10-22 下午 4:17
 * 文件描述：
 */
public class BaseFragmentBackPress extends Fragment {

    /**
     * fragment中的返回键
     * <p>
     * 默认返回flase，交给Activity处理
     * 返回true：执行fragment中需要执行的逻辑
     * 返回false：执行activity中的 onBackPressed
     */
    public boolean onBackPressed() {
        return false;
    }
}
