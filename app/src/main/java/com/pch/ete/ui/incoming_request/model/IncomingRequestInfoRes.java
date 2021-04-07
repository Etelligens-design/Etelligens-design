package com.pch.ete.ui.incoming_request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class IncomingRequestInfoRes extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<IncomingRequestInfo> data = null;

    public ArrayList<IncomingRequestInfo> getData() {
        return data;
    }

}
