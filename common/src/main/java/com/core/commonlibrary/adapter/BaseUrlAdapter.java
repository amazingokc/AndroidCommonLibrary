package com.core.commonlibrary.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.core.commonlibrary.R;
import com.core.commonlibrary.bean.BaseUrlBean;
import com.core.commonlibrary.utils.LazyOnClickListener;

import java.util.List;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-07-08 下午 2:28
 * 文件描述：
 */
public class BaseUrlAdapter extends RecyclerView.Adapter<BaseUrlAdapter.MyViewHolder> {

    private List<BaseUrlBean> urlList;

    public BaseUrlAdapter(List<BaseUrlBean> urlList) {
        this.urlList = urlList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.baseurladapter_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.cb_base_url.setText(urlList.get(position).getUrl());
        myViewHolder.cb_base_url.setChecked(urlList.get(position).isChecked());

        myViewHolder.cb_base_url.setOnClickListener(new LazyOnClickListener() {
            @Override
            public void onLazyClick(View v) {
                if (clickListener != null && myViewHolder.cb_base_url.isChecked()) {
                    clickListener.checkedItem(urlList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return urlList == null ? 0 : urlList.size();
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void checkedItem(BaseUrlBean baseUrlBean);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox cb_base_url;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_base_url = itemView.findViewById(R.id.cb_base_url);
        }
    }

}
