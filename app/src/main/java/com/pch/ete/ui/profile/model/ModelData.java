package com.pch.ete.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
