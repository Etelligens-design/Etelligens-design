package com.pch.ete.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.profile.model.UserSkillData;
import com.pch.ete.ui.profile.model.UserSkillDataRes;

import java.util.ArrayList;

public class AppSharedPreference {
    private static final String DB_NAME = "ete_pch";
    private static AppSharedPreference appSharedPreference;
    private static SharedPreferences preferences;

    private AppSharedPreference() {
    }

    public static AppSharedPreference getInstance(Context context) {
        if (appSharedPreference == null) {
            appSharedPreference = new AppSharedPreference();
            preferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        }
        return appSharedPreference;
    }

    public LoginData getLoginData() {
        String jsonStr = preferences.getString("loginData", "");
        if(!jsonStr.equals("")){
            return new Gson().fromJson(jsonStr, LoginData.class);
        }else{
            return null;
        }
    }

    public void setLoginData(LoginData loginData) {
        preferences.edit().putString("loginData", new Gson().toJson(loginData)).apply();
    }

    public String getAccessToken() {
        return preferences.getString("accessToken", "");
    }

    public void setFromNotification(boolean isFromNotification) {
        preferences.edit().putBoolean("is_from_notification", isFromNotification).apply();
    }

    public boolean isFromNotification() {
        return preferences.getBoolean("is_from_notification", false);
    }

    public void setKeepLogin(boolean isKeepLogin) {
        preferences.edit().putBoolean("is_keep_login", isKeepLogin).apply();
    }

    public boolean isKeepLogin() {
        return preferences.getBoolean("is_keep_login", false);
    }

    public void setEmail(String email) {
        preferences.edit().putString("email", email).apply();
    }

    public String getEmail() {
        return preferences.getString("email", "");
    }

    public void setPassword(String password) {
        preferences.edit().putString("password", password).apply();
    }

    public String getPassword() {
        return preferences.getString("password", "");
    }

    public void setAccessToken(String accessToken) {
        preferences.edit().putString("accessToken", accessToken).apply();
    }

    public String getDeviceToken() {
        return preferences.getString("deviceToken", "");
    }

    public void setDeviceToken(String deviceToken) {
        preferences.edit().putString("deviceToken", deviceToken).apply();
    }

    public ArrayList<UserSkillData> getUserSkillList() {
        String jsonStr = preferences.getString("user_skills", "");
        if(!jsonStr.equals("")){
            return new Gson().fromJson(jsonStr, new TypeToken<ArrayList<UserSkillData>>(){}.getType());
        }else{
            return null;
        }
    }

    public void setUserSkillList(ArrayList<UserSkillData> userSkillData) {
        preferences.edit().putString("user_skills", new Gson().toJson(userSkillData)).apply();
    }

    public void addUserSkillData(UserSkillData userSkillData){
        ArrayList<UserSkillData> tmpArray = getUserSkillList();
        tmpArray.add(userSkillData);
        setUserSkillList(tmpArray);
    }

}
