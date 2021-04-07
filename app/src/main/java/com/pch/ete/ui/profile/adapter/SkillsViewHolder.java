package com.pch.ete.ui.profile.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.ui.profile.model.SkillData;
import com.pch.ete.ui.profile.model.UserSkillData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkillsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.btn_view)
    TextView btnView;
    @BindView(R.id.tv_skill)
    TextView tvSkill;
    @BindView(R.id.tv_make_n_model)
    TextView tvMakeModel;
    @BindView(R.id.iv_iron_ribbon)
    ImageView ivBlueRibbon;
    @BindView(R.id.iv_silver_ribbon)
    ImageView ivSilverRibbon;
    @BindView(R.id.iv_gold_ribbon)
    ImageView ivGoldRibbon;

    SkillsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(UserSkillData data) {

        String modelName="", makeName = "", skillName = "";
        if(data.getMakeData() != null)
            makeName = data.getMakeData().getMakeName();
        if(data.getModelData() != null)
            modelName = data.getModelData().getModelName();
        if(data.getSkillData() != null)
            skillName = data.getSkillData().getSkillName();

        tvSkill.setText(skillName);
        tvMakeModel.setText(makeName + "/" + modelName);
        if (data.getCertificatedDoc() == null) {
            ivBlueRibbon.setVisibility(View.INVISIBLE);
        } else {
            ivBlueRibbon.setVisibility(View.VISIBLE);
        }

        if (data.getAuthorizedDoc() == null) {
            ivSilverRibbon.setVisibility(View.INVISIBLE);
        } else {
            ivSilverRibbon.setVisibility(View.VISIBLE);
        }

        if (data.getServiceHour() >= 40) {
            ivGoldRibbon.setVisibility(View.VISIBLE);
        } else {
            ivGoldRibbon.setVisibility(View.INVISIBLE);
        }
    }
}
