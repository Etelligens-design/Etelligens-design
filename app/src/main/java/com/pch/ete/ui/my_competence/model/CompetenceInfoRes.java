package com.pch.ete.ui.my_competence.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;

public class CompetenceInfoRes extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<CompetenceInfo> equipmentInfoList = null;

    public ArrayList<CompetenceInfo> getData() {
        return equipmentInfoList;
    }

}
