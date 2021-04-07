package com.pch.ete.ui.my_technician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.ui.profile.model.SkillData;

import java.util.ArrayList;

public class TechnicianLevelData extends BaseResponse {
    @SerializedName("iron")
    @Expose
    private int iron;
    @SerializedName("silver")
    @Expose
    private int silver;
    @SerializedName("gold")
    @Expose
    private int gold;

    public int getIron() {
        return iron;
    }

    public int getSilver() {
        return silver;
    }

    public int getGold() {
        return gold;
    }
}
