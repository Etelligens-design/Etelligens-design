package com.pch.ete.ui.my_technician.view_profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.ui.profile.model.UserSkillData;

import butterknife.BindView;

public class ViewSkillActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_make_equipment)
    TextView tvMakeEquipment;
    @BindView(R.id.tv_model_number)
    TextView tvModel;
    @BindView(R.id.tv_skill)
    TextView tvSkill;
    @BindView(R.id.tv_service_time)
    TextView tvServiceTime;
    @BindView(R.id.tv_last_txt)
    TextView tvLastTxt;
    @BindView(R.id.tv_you_are)
    TextView tvYouAre;
    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.radio_certified_yes)
    RadioButton radioCertiYes;
    @BindView(R.id.radio_certified_no)
    RadioButton radioCertNo;
    @BindView(R.id.radio_authorized_yes)
    RadioButton radioAuthYes;
    @BindView(R.id.radio_authorized_no)
    RadioButton radioAuthNo;
    @BindView(R.id.btn_save_skill)
    Button btnSaveSkill;
    @BindView(R.id.btn_upload_certified_doc)
    Button btnCertDoc;
    @BindView(R.id.btn_upload_authorized_doc)
    Button btnAuthDoc;

    UserSkillData mSkillData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_skill);
        bindView(this);

        tvTitle.setText(getString(R.string.view_skill));
        btnSaveSkill.setVisibility(View.GONE);
        btnCertDoc.setVisibility(View.INVISIBLE);
        btnAuthDoc.setVisibility(View.INVISIBLE);
        String jsonStr = getIntent().getStringExtra("skill_json");
        if (jsonStr != null) {
            mSkillData = new Gson().fromJson(jsonStr, UserSkillData.class);
        } else {
            showToast("Invalid Skill");
            finish();
        }
        setViewData();
    }

    void setViewData() {

        String makeName = "", modelName = "", skillName = "";
        if (mSkillData.getMakeData() != null) {
            makeName = mSkillData.getMakeData().getMakeName();
        }
        if (mSkillData.getModelData() != null) {
            modelName = mSkillData.getModelData().getModelName();
        }
        if (mSkillData.getSkillData() != null) {
            skillName = mSkillData.getSkillData().getSkillName();
        }

        tvMakeEquipment.setText(makeName);
        tvModel.setText(modelName);
        tvSkill.setText(skillName);

        if (mSkillData.getServiceHour() < 40) {
            tvServiceTime.setText(String.valueOf(40 - mSkillData.getServiceHour()));
        } else {
            tvYouAre.setText(getString(R.string.he_is_an_expert));
            tvLastTxt.setVisibility(View.GONE);
            tvServiceTime.setVisibility(View.GONE);
            tvHour.setVisibility(View.GONE);
        }
        if (mSkillData.getCertificatedDoc() != null) {
            radioCertiYes.setChecked(true);
            radioCertNo.setEnabled(false);
        } else {
            radioCertNo.setChecked(true);
            radioCertiYes.setEnabled(false);
        }

        if (mSkillData.getAuthorizedDoc() != null) {
            radioAuthYes.setChecked(true);
            radioAuthNo.setEnabled(false);
        } else {
            radioAuthNo.setChecked(true);
            radioAuthYes.setEnabled(false);
        }
    }

}
