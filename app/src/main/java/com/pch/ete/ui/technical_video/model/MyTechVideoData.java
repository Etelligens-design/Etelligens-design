package com.pch.ete.ui.technical_video.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.helper.DateUtil;

public class MyTechVideoData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("created_at")
    @Expose
    private String date;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return DateUtil.formatDateTimeFromServer(date);
    }
}
