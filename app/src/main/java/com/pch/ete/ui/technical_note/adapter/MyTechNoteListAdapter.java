package com.pch.ete.ui.technical_note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.technical_note.model.MyTechNoteData;

public class MyTechNoteListAdapter extends BaseRecycler<MyTechNoteData> {

    IRecyclerClickListener listener;

    public MyTechNoteListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyTechNoteHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_my_tech_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyTechNoteHolder) holder).setData(list.get(position));
        ((MyTechNoteHolder) holder).ivTechInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_info");
            }
        });
        ((MyTechNoteHolder) holder).ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_delete");
            }
        });
        ((MyTechNoteHolder) holder).ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_download");
            }
        });
        ((MyTechNoteHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_note");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
