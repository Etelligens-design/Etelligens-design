package com.pch.ete.ui.service_request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;

public class ServiceRequestDataRes extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<ServiceRequestData> serviceRequestData;

    public ArrayList<ServiceRequestData> getServiceRequestData() {
        return serviceRequestData;
    }
}
