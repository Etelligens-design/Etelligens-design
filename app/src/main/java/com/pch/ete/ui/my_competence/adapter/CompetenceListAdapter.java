package com.pch.ete.ui.my_competence.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.my_competence.model.CompetenceInfo;

public class CompetenceListAdapter extends BaseRecycler {

    IRecyclerClickListener listener;

    public CompetenceListAdapter(IRecyclerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompetenceHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_my_competence, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CompetenceHolder) holder).setData((CompetenceInfo) list.get(position));
        ((CompetenceHolder) holder).btnEdit.setOnClickListener(new View.OnClickListener() {
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
