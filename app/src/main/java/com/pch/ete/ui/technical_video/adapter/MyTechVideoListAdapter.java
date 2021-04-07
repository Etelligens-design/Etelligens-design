package com.pch.ete.ui.technical_video.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.technical_video.model.MyTechVideoData;

public class MyTechVideoListAdapter extends BaseRecycler<MyTechVideoData> {

    IRecyclerClickListener listener;

    public MyTechVideoListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyTechVideoHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_my_tech_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyTechVideoHolder) holder).setData(list.get(position));
        ((MyTechVideoHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_video");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
