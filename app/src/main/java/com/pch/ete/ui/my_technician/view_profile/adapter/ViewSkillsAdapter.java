package com.pch.ete.ui.my_technician.view_profile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.base.BaseRecycler;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.profile.model.UserSkillData;

public class ViewSkillsAdapter extends BaseRecycler<UserSkillData> {
    private IRecyclerClickListener clickListener;

    public ViewSkillsAdapter(IRecyclerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewSkillsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_skill, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewSkillsViewHolder) holder).setData(list.get(position));
        ((ViewSkillsViewHolder) holder).btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRecyclerClick(position, "", "click_view");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
