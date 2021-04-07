package com.pch.ete.ui.my_equipment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class EquipmentInfoRes extends BaseResponse {

    @SerializedName("user_equipment")
    @Expose
    private ArrayList<EquipmentInfo> equipmentInfoList = null;

    public ArrayList<EquipmentInfo> getData() {
        return equipmentInfoList;
    }

}
