package com.pch.ete.ui.my_equipment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.ui.profile.model.MakeData;
import com.pch.ete.ui.profile.model.ModelData;

public class EquipmentInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("asset_name")
    @Expose
    private String assetName;
    @SerializedName("internal_id")
    @Expose
    private String internalId;
    @SerializedName("make")
    @Expose
    private MakeData make;
    @SerializedName("model")
    @Expose
    private ModelData model;
    @SerializedName("warranty_start_date")
    @Expose
    private String warrantyDate;
    @SerializedName("warranty_period")
    @Expose
    private String warrantyPeriod;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLocation() {
        return location;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getInternalId() {
        return internalId;
    }

    public MakeData getMake() {
        return make;
    }

    public ModelData getModel() {
        return model;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public void setMake(MakeData make) {
        this.make = make;
    }

    public void setModel(ModelData model) {
        this.model = model;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}
