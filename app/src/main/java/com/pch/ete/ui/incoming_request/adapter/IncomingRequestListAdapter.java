package com.pch.ete.ui.incoming_request.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.incoming_request.model.IncomingRequestInfo;

public class IncomingRequestListAdapter extends BaseRecycler {

    IRecyclerClickListener listener;

    public IncomingRequestListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomingRequestHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_incoming_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((IncomingRequestHolder) holder).setData((IncomingRequestInfo) list.get(position));
        ((IncomingRequestHolder) holder).tvSRId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_srid");
            }
        });
        ((IncomingRequestHolder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_accept");
            }
        });
        ((IncomingRequestHolder) holder).btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_decline");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
