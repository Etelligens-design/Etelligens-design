package com.pch.ete.ui.my_competence.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pch.ete.ui.profile.model.MakeData;
import com.pch.ete.ui.profile.model.ModelData;
import com.pch.ete.ui.profile.model.SkillData;

public class CompetenceInfo {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("certificated_doc")
    @Expose
    private String certificatedDoc;
    @SerializedName("authorized_doc")
    @Expose
    private String authorizedDoc;
    @SerializedName("service_hour")
    @Expose
    private int serviceHour;
    @SerializedName("make")
    @Expose
    private MakeData makeData;
    @SerializedName("model")
    @Expose
    private ModelData modelData;

    @SerializedName("skill")
    @Expose
    private SkillData skillData;

    public int getId() {
        return id;
    }

    public String getCertificatedDoc() {
        return certificatedDoc;
    }

    public String getAuthorizedDoc() {
        return authorizedDoc;
    }

    public int getServiceHour() {
        return serviceHour;
    }

    public MakeData getMakeData() {
        return makeData;
    }

    public ModelData getModelData() {
        return modelData;
    }

    public SkillData getSkillData() {
        return skillData;
    }
}
