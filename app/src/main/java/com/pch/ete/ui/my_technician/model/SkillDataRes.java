package com.pch.ete.ui.my_technician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.ui.profile.model.MakeData;
import com.pch.ete.ui.profile.model.ModelData;
import com.pch.ete.ui.profile.model.SkillData;

import java.util.ArrayList;

public class SkillDataRes extends BaseResponse {
    @SerializedName("skill_list")
    @Expose
    private ArrayList<SkillData> skillList;

    public ArrayList<SkillData> getSkillList() {
        if (skillList == null) {
            skillList = new ArrayList<SkillData>();
        }
        return skillList;
    }
}
