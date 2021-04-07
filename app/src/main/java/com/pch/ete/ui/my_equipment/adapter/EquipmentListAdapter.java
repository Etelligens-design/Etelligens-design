package com.pch.ete.ui.my_equipment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;

public class EquipmentListAdapter extends BaseRecycler {

    IRecyclerClickListener listener;

    public EquipmentListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EquipmentHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_my_equipment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((EquipmentHolder) holder).setData((EquipmentInfo) list.get(position));
        ((EquipmentHolder) holder).btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_edit");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
