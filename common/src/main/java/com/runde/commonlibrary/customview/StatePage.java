package com.runde.commonlibrary.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.R;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.utils.DensityUtil;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-01-26 下午 6:04
 * 文件描述：
 */

public class StatePage extends FrameLayout implements IStatePageView {

    // 属性，为空则替换为默认
    public String errorTips = "网络不见了，请重试";
    public String emptyTips = "请稍候重新尝试吧～";
    public String errorOption = "重新加载";
    private Drawable emptyDrawable;
    private Drawable errorDrawable;

    private View rootView;
    private RelativeLayout rl_state_page_root;
    public ImageView iv_state_page_pic;
    public TextView tv_state_page_option;
    public TextView tv_state_page_tips;
    private LinearLayout ll_state_state_page;
    private LinearLayout ll_loading_state_page;
    private GifImageView gif_loading_state_page;
    private TextView tv_loading_msg_state_page;
    private GifDrawable gifDrawable;
    private boolean isDestroy;


    private Context context;

    public StatePage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
        initUI(context);
    }

    private void initData() {
    }

    private void initUI(Context context) {
        rootView = LayoutInflater
                .from(context)
                .inflate(R.layout.layout_state_page, this);
        rl_state_page_root = rootView.findViewById(R.id.ll_state_page_root);
        ll_state_state_page = rootView.findViewById(R.id.ll_state_state_page);
        iv_state_page_pic = rootView.findViewById(R.id.iv_state_page_pic);
        tv_state_page_option = rootView.findViewById(R.id.tv_state_page_option);
        tv_state_page_tips = rootView.findViewById(R.id.tv_state_page_tips);

        tv_state_page_option.setBackground(ServiceFactory.getInstance().getCommonService().getStatePageBtnBackgroud());
        tv_state_page_option.setTextColor(ServiceFactory.getInstance().getCommonService().getStatePageBtnTextColor());

        ll_loading_state_page = rootView.findViewById(R.id.ll_loading_state_page);
        tv_loading_msg_state_page = rootView.findViewById(R.id.tv_loading_msg_state_page);
        gif_loading_state_page = rootView.findViewById(R.id.gif_loading_state_page);
        gif_loading_state_page.setImageResource(ServiceFactory.getInstance().getCommonService().getStatePageloading());
        gifDrawable = (GifDrawable) gif_loading_state_page.getDrawable();
    }

    @Override
    public void onDestroy() {
        if (gifDrawable != null) {
            if (!gifDrawable.isRecycled()) {
                gifDrawable.recycle();
            }
            gifDrawable = null;
        }
        rootView = null;
        isDestroy = true;
    }

    private boolean checkIsDestroy() {
        if (isDestroy || rootView == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setPageSuccess() {
        if (checkIsDestroy()) return;
        tv_state_page_tips.setText(ApplicationContext.getString(R.string.loading_default_tip));
        if (gifDrawable != null && gifDrawable.canPause() && gifDrawable.isRunning()
                && !gifDrawable.isRecycled()) {
            gifDrawable.stop();
        }
        this.setVisibility(View.GONE);
    }

    @Override
    public void setPageError(CharSequence tip, OnClickListener onClickListener) {
        if (checkIsDestroy()) return;
        showState(true);
        showLoadingState(false);
        if (tip == null) {
            tv_state_page_tips.setText(errorTips);
        } else {
            tv_state_page_tips.setText(tip);
        }
        tv_state_page_option.setText(errorOption);
        tv_state_page_option.setOnClickListener(onClickListener);

        setImage(errorDrawable);

        this.setVisibility(View.VISIBLE);
        tv_state_page_tips.setVisibility(View.VISIBLE);
        tv_state_page_option.setVisibility(View.VISIBLE);
    }

    public void setPageError(CharSequence tip, CharSequence option, OnClickListener onClickListener) {
        if (checkIsDestroy()) return;
        showState(true);
        showLoadingState(false);
        if (tip == null) {
            tv_state_page_tips.setText(errorTips);
        } else {
            tv_state_page_tips.setText(tip);
        }
        tv_state_page_option.setText(option);
        tv_state_page_option.setOnClickListener(onClickListener);

        setImage(errorDrawable);

        this.setVisibility(View.VISIBLE);
        tv_state_page_tips.setVisibility(View.VISIBLE);
        tv_state_page_option.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPageEmpty(CharSequence tip) {
        if (checkIsDestroy()) return;
        showState(true);
        showLoadingState(false);
        if (tip == null) {
            tv_state_page_tips.setText(emptyTips);
        } else {
            tv_state_page_tips.setText(tip);
        }

        setImage(emptyDrawable);

        this.setVisibility(View.VISIBLE);
        tv_state_page_tips.setVisibility(View.VISIBLE);
        tv_state_page_option.setVisibility(View.GONE);
    }

    public void setPageEmpty(CharSequence tip, CharSequence option, OnClickListener onClickListener) {
        if (checkIsDestroy()) return;
        showState(true);
        showLoadingState(false);
        if (tip == null) {
            tv_state_page_tips.setText(emptyTips);
        } else {
            tv_state_page_tips.setText(tip);
        }

        setImage(emptyDrawable);
        if (!TextUtils.isEmpty(option)) {
            tv_state_page_option.setText(option);
        }
        tv_state_page_option.setOnClickListener(onClickListener);

        this.setVisibility(View.VISIBLE);
        tv_state_page_tips.setVisibility(View.VISIBLE);
        tv_state_page_option.setVisibility(View.VISIBLE);
    }

    @Override
    public void resetErrorBackgroud(Drawable drawable) {
        if (checkIsDestroy()) return;
        errorDrawable = drawable;
    }

    @Override
    public void resetEmptyBackgroud(Drawable drawable) {
        if (checkIsDestroy()) return;
        emptyDrawable = drawable;
    }

    @Override
    public void showLoading(String strLoadingTip) {
        if (checkIsDestroy()) return;
        showState(false);
        this.setVisibility(View.VISIBLE);
        if (gifDrawable != null && !gifDrawable.isRecycled()) {
            gifDrawable.start();
            showLoadingState(true);
        }
        if (!TextUtils.isEmpty(strLoadingTip)) {
            tv_loading_msg_state_page.setText(strLoadingTip);
        }

    }

    private void setImage(Drawable drawable) {
        if (drawable != null) {
            iv_state_page_pic.setImageDrawable(drawable);
        } else {
            iv_state_page_pic.setImageDrawable(
                    ServiceFactory.getInstance().getCommonService().getStatepageNodata());
        }
    }

    public void setStatePageBg(int color) {
        if (checkIsDestroy()) return;
        rl_state_page_root.setBackgroundColor(color);
    }

    public StatePage(Context context) {
        this(context, null);
        isDestroy = false;
    }

    //显示或隐藏加载过后的状态
    private void showState(boolean isShow) {
        if (isShow && ll_state_state_page.getVisibility() == GONE) {
            ll_state_state_page.setVisibility(VISIBLE);
        } else if (!isShow && ll_state_state_page.getVisibility() == VISIBLE) {
            ll_state_state_page.setVisibility(GONE);
        }
    }

    //显示会隐藏加载中的状态
    private void showLoadingState(boolean isShow) {
        if (isShow && ll_loading_state_page.getVisibility() == GONE) {
            if (gifDrawable != null && !gifDrawable.isRecycled()) {
                ll_loading_state_page.setVisibility(VISIBLE);
            }
        } else if (!isShow && ll_state_state_page.getVisibility() == VISIBLE) {
            ll_loading_state_page.setVisibility(GONE);
        }
    }

    public void setLayoutParams(int height) {
        ViewGroup.LayoutParams linearLayout = ll_state_state_page.getLayoutParams();
        linearLayout.height = DensityUtil.dip2px(height);
        ll_state_state_page.setLayoutParams(linearLayout);
    }

}
