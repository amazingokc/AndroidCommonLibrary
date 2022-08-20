package com.runde.commonlibrary.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.utils.DisplayUtil;

/**
 * desc:
 * author: shcx
 * date: 2021/9/20 11:35
 */
public class CustomSettingLayout extends RelativeLayout {

    private LinearLayout llRoot;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private ImageView imgvRightIcon;
    private View line;
    private int leftTitleColor;
    private int rightTitleColor;

    public CustomSettingLayout(Context context) {
        super(context);
    }

    public CustomSettingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_setting_layout, this);
        llRoot = findViewById(R.id.ll_root);
        line = findViewById(R.id.line);
        tvLeftTitle = findViewById(R.id.tv_left_title);
        tvRightTitle = findViewById(R.id.tv_right_title);
        imgvRightIcon = findViewById(R.id.imgv_right_icon);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSettingLayout);
        if (ta != null) {
            //背景设置
            boolean isSetBg = ta.getBoolean(R.styleable.CustomSettingLayout_custom_is_set_bg, true);
            if (isSetBg) {
                llRoot.setBackgroundColor(ta.getColor(R.styleable.CustomSettingLayout_custom_bg_color,
                        ApplicationContext.getColor(R.color.white)));
            }

            String title = ta.getString(R.styleable.CustomSettingLayout_custom_left_title);
            tvLeftTitle.setText(title);

            boolean isShow = ta.getBoolean(R.styleable.CustomSettingLayout_custom_right_icon_isShow, true);
            if (!isShow) imgvRightIcon.setVisibility(GONE);

            String rightTitle = ta.getString(R.styleable.CustomSettingLayout_custom_right_title);
            tvRightTitle.setText(rightTitle);

            //分割线设置
            boolean isShowLine = ta.getBoolean(R.styleable.CustomSettingLayout_custom_is_show_line, true);
            line.setVisibility(isShowLine ? VISIBLE : GONE);
            float marginLeft = ta.getDimension(R.styleable.CustomSettingLayout_custom_line_margin_left, 0);
            float marginRight = ta.getDimension(R.styleable.CustomSettingLayout_custom_line_margin_right, 0);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) line.getLayoutParams();
            layoutParams.leftMargin = (int) marginLeft;
            layoutParams.rightMargin = (int) marginRight;
            line.setLayoutParams(layoutParams);

            //设置字体颜色
            leftTitleColor = ta.getColor(R.styleable.CustomSettingLayout_left_title_color,
                    ApplicationContext.getColor(R.color.color_1C1D1D));
            rightTitleColor = ta.getColor(R.styleable.CustomSettingLayout_right_title_color,
                    ApplicationContext.getColor(R.color.text_color999));
            tvLeftTitle.setTextColor(leftTitleColor);
            tvRightTitle.setTextColor(rightTitleColor);
            //设置字体大小
            float leftTextSize =  ta.getDimension(R.styleable.CustomSettingLayout_left_title_text_size, DisplayUtil.sp2px(16));
            float rightTextSize =  ta.getDimension(R.styleable.CustomSettingLayout_right_title_text_size, DisplayUtil.sp2px(16));
            tvLeftTitle.setTextSize(DisplayUtil.px2sp(leftTextSize));
            tvRightTitle.setTextSize(DisplayUtil.px2sp(rightTextSize));

            ta.recycle();
        }
    }

    public void setTvRightTitle(String rightTitle) {
        if (!TextUtils.isEmpty(rightTitle)) {
            tvRightTitle.setText(rightTitle);
        }
    }

    public TextView getTvRightTitle() {
        return tvRightTitle;
    }

}
