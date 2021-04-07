package com.pch.ete.ui.my_competence.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.ui.my_competence.model.CompetenceInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetenceHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_skill_name)
    TextView tvSkillName;
    @BindView(R.id.tv_make_model)
    TextView tvMakeModel;
    @BindView(R.id.btn_edit)
    TextView btnEdit;

    public CompetenceHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(CompetenceInfo data) {
        String makeName = "", modelName = "", skillName = "";
        if (data.getMakeData() != null) {
            makeName = data.getMakeData().getMakeName();
        }
        if (data.getModelData() != null) {
            modelName = data.getModelData().getModelName();
        }
        if (data.getSkillData() != null) {
            skillName = data.getSkillData().getSkillName();
        }
        tvSkillName.setText(skillName);
        tvMakeModel.setText(makeName + " " + modelName);
    }
}
