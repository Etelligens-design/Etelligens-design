package com.pch.ete.ui.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.base.BaseResponse;

import java.util.List;

public class LoginDataRes extends BaseResponse {
    @SerializedName("user_info")
    @Expose
    private LoginData userInfo;

    public LoginData getUserInfo() {
        return userInfo;
    }
}
