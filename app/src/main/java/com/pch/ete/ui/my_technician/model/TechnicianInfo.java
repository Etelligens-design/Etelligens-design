package com.pch.ete.ui.my_technician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TechnicianInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("office_location")
    @Expose
    private String officeLocation;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("user_type")
    @Expose
    private int userType;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("skill_level")
    @Expose
    private TechnicianLevelData userSkillLevel;


    public String getId() {
        return id;
    }

    public String getUserName() {
        if (userName == null)
            userName = "";
        return userName;
    }

    public String getFullName() {
        if (fullName == null)
            fullName = "";
        return fullName;
    }

    public String getEmail() {
        if (email == null)
            email = "";
        return email;
    }

    public TechnicianLevelData getUserSkillLevel() {
        return userSkillLevel;
    }

    public String getImage() {
        if (image == null)
            image = "";
        return image;
    }

    public String getAbout() {
        if (about == null)
            about = "";
        return about;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null)
            phoneNumber = "";
        return phoneNumber;
    }

    public String getOfficeLocation() {
        if (officeLocation == null)
            officeLocation = "";
        return officeLocation;
    }

    public String getCompanyName() {
        if (companyName == null)
            companyName = "";
        return companyName;
    }

    public String getRole() {
        if (role == null)
            role = "";
        return role;
    }

    public String getCountry() {
        if (country == null)
            country = "";
        return country;
    }

    public int getUserType() {
        return userType;
    }

    public int getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
