package com.pch.ete.ui.incoming_request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;
import com.pch.ete.ui.service_request.model.ServiceTypeData;

public class IncomingRequestInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("service_type")
    @Expose
    private ServiceTypeData serviceTypeData;
    @SerializedName("equipment")
    @Expose
    private EquipmentInfo equipmentInfo;
    @SerializedName("preferred_time")
    @Expose
    private String preferredTime;
    @SerializedName("assign_at")
    @Expose
    private String assignDate;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("technician_name")
    @Expose
    private String technicianName;
    @SerializedName("state")
    @Expose
    private int state;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("user_data")
    @Expose
    private LoginData userData;
    @SerializedName("admin_accept")
    @Expose
    private String adminAcceptState;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("accepted_at")
    @Expose
    private String acceptedAt;
    @SerializedName("reassign_at")
    @Expose
    private String reassignAt;
    @SerializedName("rejected_at")
    @Expose
    private String rejectedAt;
    @SerializedName("appointment_date")
    @Expose
    private String appointmentDate;
    @SerializedName("completed_at")
    @Expose
    private String completedAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public ServiceTypeData getServiceTypeData() {
        return serviceTypeData;
    }

    public EquipmentInfo getEquipmentInfo() {
        return equipmentInfo;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public int getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public LoginData getUserData() {
        return userData;
    }

    public String getAdminAcceptState() {
        return adminAcceptState;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getAcceptedAt() {
        return acceptedAt;
    }

    public int getDuration() {
        return duration;
    }

    public String getReassignAt() {
        return reassignAt;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public String getRejectedAt() {
        return rejectedAt;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getCompletedAt() {
        return completedAt;
    }
}
