package com.pch.ete.ui.notification.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.notification.model.NotificationInfo;

public class NotificationAdapter extends BaseRecycler<NotificationInfo> {

    IRecyclerClickListener listener;

    public NotificationAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NotificationHolder) holder).setData(list.get(position));
        ((NotificationHolder) holder).btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerClick(position, list.get(position), "click_clear");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
