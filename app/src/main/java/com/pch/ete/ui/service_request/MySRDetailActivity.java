package com.pch.ete.ui.service_request;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.login.model.LoginDataRes;
import com.pch.ete.ui.service_request.model.ServiceRequestData;

import butterknife.BindView;
import retrofit2.Response;

public class MySRDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_service_type)
    TextView tvServiceType;
    @BindView(R.id.et_asset_name)
    TextView tvAssetName;
    @BindView(R.id.et_model_type)
    TextView tvMakeModel;
    @BindView(R.id.radio_emergency_yes)
    RadioButton radioEGYes;
    @BindView(R.id.radio_emergency_no)
    RadioButton radioEGNo;
    @BindView(R.id.radio_technician_yes)
    RadioButton radioTechYes;
    @BindView(R.id.radio_technician_no)
    RadioButton radioTechNo;
    @BindView(R.id.tv_preferred_date)
    TextView tvPreferredDate;
    @BindView(R.id.tv_preferred_time)
    TextView tvPreferredTime;
    @BindView(R.id.et_user_name)
    TextView tvUserName;
    @BindView(R.id.et_description)
    TextView tvDescription;

    ServiceRequestData mServiceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requested_service);
        bindView(this);

        String jsonStr = getIntent().getStringExtra("sr_detail");
        if (jsonStr == null) {
            showToast("Invalid SR");
            return;
        }
        mServiceRequest = new Gson().fromJson(jsonStr, ServiceRequestData.class);

        setViewData();
    }

    void setViewData() {

        tvTitle.setText("SR#" + mServiceRequest.getId());
        if (mServiceRequest.getServiceTypeData() != null)
            tvServiceType.setText(mServiceRequest.getServiceTypeData().getServiceName());
        tvAssetName.setText(mServiceRequest.getEquipmentInfo().getAssetName());
        tvDescription.setText(mServiceRequest.getDescription());

        String modelName = "", makeName = "";
        if (mServiceRequest.getEquipmentInfo().getMake() != null)
            makeName = mServiceRequest.getEquipmentInfo().getMake().getMakeName();
        if (mServiceRequest.getEquipmentInfo().getModel() != null)
            modelName = mServiceRequest.getEquipmentInfo().getModel().getModelName();

        tvMakeModel.setText(makeName + " " + modelName);
        if (mServiceRequest.getPreferredTime() != null) {
            radioEGYes.setChecked(true);
            radioEGNo.setEnabled(false);
            String localPreferredTime = DateUtil.formatDateHHMMForLocal(mServiceRequest.getPreferredTime());
            tvPreferredDate.setText(localPreferredTime.split(" ")[0]);
            tvPreferredTime.setText(localPreferredTime.split(" ")[1]);
        } else {
            radioEGNo.setChecked(true);
            radioEGYes.setEnabled(false);
        }

        if (mServiceRequest.getTechnicianName() != null) {
            radioTechYes.setChecked(true);
            radioTechNo.setEnabled(false);
            tvUserName.setText(mServiceRequest.getTechnicianName());
        } else {
            radioTechNo.setChecked(true);
            radioTechYes.setEnabled(false);
        }
    }
}
