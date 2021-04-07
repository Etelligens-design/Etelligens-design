package com.pch.ete.ui.accepted_request.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.incoming_request.model.IncomingRequestInfo;

public class AcceptedSRListAdapter extends BaseRecycler<IncomingRequestInfo> {

    IRecyclerClickListener listener;

    public AcceptedSRListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AcceptedRequestHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_accepted_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AcceptedRequestHolder) holder).setData(list.get(position));
        ((AcceptedRequestHolder) holder).tvSRId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_srid");
            }
        });
        ((AcceptedRequestHolder) holder).btnReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_reschedule");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
