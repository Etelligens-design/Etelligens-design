package com.pch.ete.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class UserSkillDataRes extends BaseResponse {
    @SerializedName("user_skill")
    @Expose
    private ArrayList<UserSkillData> userSkillDataList = null;

    public ArrayList<UserSkillData> getUserSkillDataList() {
        return userSkillDataList;
    }
}
