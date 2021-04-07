package com.pch.ete.ui.my_technician.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pch.ete.R;
import com.pch.ete.ui.my_technician.model.TechnicianInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TechnicianHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvUserName;
    @BindView(R.id.btn_view)
    TextView btnView;
    @BindView(R.id.btn_delete)
    TextView btnDelete;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_iron_ribbon)
    ImageView ivBlueRibbon;
    @BindView(R.id.iv_silver_ribbon)
    ImageView ivSilverRibbon;
    @BindView(R.id.iv_gold_ribbon)
    ImageView ivGoldRibbon;

    public TechnicianHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(TechnicianInfo userData) {
        Glide.with(itemView.getContext()).load(userData.getImage())
                .placeholder(R.drawable.icon_placeholder)
                .error(itemView.getContext().getDrawable(R.drawable.icon_placeholder))
                .into(ivProfile);

        tvUserName.setText(userData.getUserName());

        if(userData.getUserSkillLevel() != null) {
            if (userData.getUserSkillLevel().getIron() > 0) {
                ivBlueRibbon.setVisibility(View.VISIBLE);
            } else {
                ivBlueRibbon.setVisibility(View.INVISIBLE);
            }
            if (userData.getUserSkillLevel().getSilver() > 0) {
                ivSilverRibbon.setVisibility(View.VISIBLE);
            } else {
                ivSilverRibbon.setVisibility(View.INVISIBLE);
            }
            if (userData.getUserSkillLevel().getGold() >= 40) {
                ivGoldRibbon.setVisibility(View.VISIBLE);
            } else {
                ivGoldRibbon.setVisibility(View.INVISIBLE);
            }
        }
    }
}
