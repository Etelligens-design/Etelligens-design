package com.pch.ete.ui.technical_note.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.ArrayList;

public class MyTechNoteDataRes extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<MyTechNoteData> myTechNoteDataArrayList;

    public ArrayList<MyTechNoteData> getMyTechNoteDataArrayList() {
        return myTechNoteDataArrayList;
    }
}
