package com.pch.ete.ui.technical_note.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.ui.technical_note.model.MyTechNoteData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTechNoteHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_info)
    ImageView ivTechInfo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_download)
    ImageView ivDownload;

    public MyTechNoteHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(MyTechNoteData data) {
        tvTitle.setText(data.getTitle());
        tvDate.setText(data.getDate());
        if(data.getUrl().isEmpty()){ // not file
            ivDelete.setVisibility(View.GONE);
            ivDownload.setVisibility(View.GONE);
        }else{
            ivDelete.setVisibility(View.VISIBLE);
            ivDownload.setVisibility(View.VISIBLE);
        }
    }
}
