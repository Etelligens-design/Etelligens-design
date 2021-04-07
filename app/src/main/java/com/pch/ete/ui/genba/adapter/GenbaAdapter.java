package com.pch.ete.ui.genba.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.service_request.model.ServiceRequestData;

public class GenbaAdapter extends BaseRecycler {

    IRecyclerClickListener listener;

    public GenbaAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenbaHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_genba, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GenbaHolder) holder).setData((ServiceRequestData) list.get(position));
        ((GenbaHolder) holder).tvSRId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_srid");
            }
        });
        ((GenbaHolder) holder).btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_join");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
