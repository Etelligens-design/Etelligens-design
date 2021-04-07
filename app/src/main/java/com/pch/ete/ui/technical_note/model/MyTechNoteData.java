package com.pch.ete.ui.technical_note.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;
import com.pch.ete.ui.service_request.model.ServiceTypeData;

public class MyTechNoteData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
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

    public String getText() {
        return text;
    }

    public String getUrl() {
        if(url == null)
            url = "";
        return url;
    }

    public String getSize() {
        if(size == null)
            size = "";
        return size;
    }

    public String getDate() {
        return DateUtil.formatDateTimeFromServer(date);
    }
}
