package com.pch.ete.ui.service_request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceTypeData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
