package com.pch.ete.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;

public class MakeModelSkillRes extends BaseResponse {
    @SerializedName("make_list")
    @Expose
    private ArrayList<MakeData> makeList;

    @SerializedName("model_list")
    @Expose
    private ArrayList<ModelData> modelList;

    @SerializedName("skill_list")
    @Expose
    private ArrayList<SkillData> skillList;

    public ArrayList<ModelData> getModelList() {
        if (modelList == null) {
            modelList = new ArrayList<ModelData>();
        }
        return modelList;
    }

    public ArrayList<MakeData> getMakeList() {
        if (makeList == null) {
            makeList = new ArrayList<MakeData>();
        }
        return makeList;
    }

    public ArrayList<SkillData> getSkillList() {
        if (skillList == null) {
            skillList = new ArrayList<SkillData>();
        }
        return skillList;
    }
}
