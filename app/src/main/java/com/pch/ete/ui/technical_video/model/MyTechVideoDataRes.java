package com.pch.ete.ui.technical_video.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;

public class MyTechVideoDataRes extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<MyTechVideoData> myTechVideoDataArrayList;

    public ArrayList<MyTechVideoData> getMyTechVideoDataArrayList() {
        return myTechVideoDataArrayList;
    }
}
