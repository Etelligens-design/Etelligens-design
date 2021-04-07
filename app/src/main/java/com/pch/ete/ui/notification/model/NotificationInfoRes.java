package com.pch.ete.ui.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.List;

public class NotificationInfoRes extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<NotificationInfo> data = null;

    public List<NotificationInfo> getData() {
        return data;
    }

}
