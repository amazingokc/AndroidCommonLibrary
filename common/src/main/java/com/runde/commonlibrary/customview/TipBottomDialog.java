package com.runde.commonlibrary.customview;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runde.commonlibrary.R;


/** 底部提示框
 * Created by hua
 */

public class TipBottomDialog extends Dialog {

    public TipBottomDialog(@NonNull Context context) {
        super(context);
    }

    public TipBottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public interface OnTipBottomListener{
        void onRightClick(TipBottomDialog dialog);
        void onLeftClick(TipBottomDialog dialog);
    }

    public static class Builder {

        private final TipBottomDialog dialog;
        private final View layout;
        private OnTipBottomListener onTipBottomListener;
        private TextView tvMessageTipBottomDialogMine;
        private final TextView tvRightClick;
        private final TextView tvLeftClick;

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new TipBottomDialog(context, R.style.test_tip_dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.mine_dialog_layout_tip_bottom, null);
            tvRightClick = layout.findViewById(R.id.tv_no_tip_bottom_dialog_mine);
            tvLeftClick = layout.findViewById(R.id.tv_yes_tip_bottom_dialog_mine);
            tvMessageTipBottomDialogMine = layout.findViewById(R.id.tv_message_tip_bottom_dialog_mine);
            tvRightClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTipBottomListener!=null){
                        onTipBottomListener.onRightClick(dialog);
                    }
                    dialog.dismiss();
                }
            });
            tvLeftClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTipBottomListener!=null){
                        onTipBottomListener.onLeftClick(dialog);
                    }
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.view_region_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        public TipBottomDialog setOnTipBottomListener(OnTipBottomListener listener) {
            this.onTipBottomListener=listener;
            return dialog;
        }

        public void setMessage(String message){
            tvMessageTipBottomDialogMine.setText(message);
        }

        public void setRightText(String text){
            tvRightClick.setText(text);
        }

        public void setLeftText(String text){
            tvLeftClick.setText(text);
        }
    }
}
