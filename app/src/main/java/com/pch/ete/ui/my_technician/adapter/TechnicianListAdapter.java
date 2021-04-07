package com.pch.ete.ui.my_technician.adapter;

import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.my_technician.model.TechnicianInfo;

public class TechnicianListAdapter extends BaseRecycler<TechnicianInfo> {

    IRecyclerClickListener listener;

    public TechnicianListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TechnicianHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_technician, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TechnicianHolder) holder).setData((TechnicianInfo) list.get(position));

        ((TechnicianHolder) holder).btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecyclerClick(position, null, "click_view");
            }
        });

        ((TechnicianHolder) holder).btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecyclerClick(position, null, "click_delete");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
