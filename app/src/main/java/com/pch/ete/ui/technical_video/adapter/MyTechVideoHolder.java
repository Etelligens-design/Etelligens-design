package com.pch.ete.ui.technical_video.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pch.ete.R;
import com.pch.ete.ui.technical_video.model.MyTechVideoData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTechVideoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_video)
    ImageView ivTechVideo;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public MyTechVideoHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(MyTechVideoData data) {
        Glide.with(itemView.getContext())
                .asBitmap()
                .load(data.getUrl())
                .placeholder(R.drawable.icon_video_grey)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(ivTechVideo);
        tvTitle.setText(data.getTitle());
    }
}
