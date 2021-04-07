package com.pch.ete.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("skill_name")
    @Expose
    private String skillName;

    public String getId() {
        return id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
