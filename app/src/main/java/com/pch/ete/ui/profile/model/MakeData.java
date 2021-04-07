package com.pch.ete.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("make_name")
    @Expose
    private String makeName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public String getMakeName() {
        return makeName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
