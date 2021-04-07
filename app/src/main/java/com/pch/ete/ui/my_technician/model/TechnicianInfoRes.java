package com.pch.ete.ui.my_technician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.List;

public class TechnicianInfoRes extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<TechnicianInfo> data = null;

    public List<TechnicianInfo> getData() {
        return data;
    }

}
