package com.pch.ete.ui.service_request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;

public class ServiceTypeDataRes extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<ServiceTypeData> serviceTypeData;

    public ArrayList<ServiceTypeData> getServiceTypeData() {
        return serviceTypeData;
    }
}
