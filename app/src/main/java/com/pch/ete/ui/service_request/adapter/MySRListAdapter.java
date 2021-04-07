package com.pch.ete.ui.service_request.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;
import com.pch.ete.ui.service_request.model.ServiceRequestData;

public class MySRListAdapter extends BaseRecycler {

    IRecyclerClickListener listener;

    public MySRListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MySRHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_my_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MySRHolder) holder).setData((ServiceRequestData) list.get(position));
        ((MySRHolder) holder).tvSRId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_srid");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
