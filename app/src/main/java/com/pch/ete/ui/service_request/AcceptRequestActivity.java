package com.pch.ete.ui.service_request;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.SelectDate;
import com.pch.ete.interfaces.SelectTime;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.service_request.model.ServiceRequestData;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class AcceptRequestActivity extends BaseActivity implements IApiCallback<BaseResponse>, SelectDate, SelectTime {

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
    @BindView(R.id.tv_appointment_date)
    TextView tvAppointmentDate;
    @BindView(R.id.tv_appointment_time)
    TextView tvAppointmentTime;
    @BindView(R.id.et_duration_time)
    EditText etDuration;


    ServiceRequestData mServiceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request_service);
        bindView(this);

        String jsonStr = getIntent().getStringExtra("sr_detail");
        if (jsonStr == null) {
            showToast("Invalid SR");
            finish();
            return;
        }
        mServiceRequest = new Gson().fromJson(jsonStr, ServiceRequestData.class);
        setViewData();
        setResult(Activity.RESULT_CANCELED);

    }

    void setViewData() {
        tvTitle.setText("SR#" + mServiceRequest.getId());
        if (mServiceRequest.getServiceTypeData() != null)
            tvServiceType.setText(mServiceRequest.getServiceTypeData().getServiceName());
        tvAssetName.setText(mServiceRequest.getEquipmentInfo().getAssetName());
        tvDescription.setText(mServiceRequest.getDescription());

        String makeName = "", modelName = "";
        if (mServiceRequest.getEquipmentInfo().getMake() != null)
            makeName = mServiceRequest.getEquipmentInfo().getMake().getMakeName();
        if (mServiceRequest.getEquipmentInfo().getModel() != null)
            modelName = mServiceRequest.getEquipmentInfo().getModel().getModelName();
        tvMakeModel.setText(makeName + " " + modelName);
        if (mServiceRequest.getPreferredTime() != null) {
            String localPreferredTime = DateUtil.formatDateHHMMForLocal(mServiceRequest.getPreferredTime());
            radioEGYes.setChecked(true);
            radioEGNo.setEnabled(false);
            String preferredDate = localPreferredTime.split(" ")[0];
            String preferredTime = localPreferredTime.split(" ")[1];
            tvPreferredDate.setText(preferredDate);
            tvPreferredTime.setText(preferredTime);
            tvAppointmentDate.setText(preferredDate);
            tvAppointmentTime.setText(preferredTime);
            tvAppointmentDate.setClickable(false);
            tvAppointmentTime.setClickable(false);
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

    @OnClick(R.id.tv_appointment_date)
    void onClickPreferredDate() {
        DateUtil.selectDate(this, this, "", "");
    }

    @OnClick(R.id.tv_appointment_time)
    void onClickPreferredTime() {
        DateUtil.selectTime(this, this, "");
    }

    @OnClick(R.id.btn_accept_sr)
    void onClickBtnAccept() {
        if (tvAppointmentDate.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_assign_date));
            return;
        }
        if (tvAppointmentTime.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_assign_time));
            return;
        }

        if (etDuration.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_duration));
            return;
        }

        float duration = Float.parseFloat(etDuration.getText().toString());
        if (duration <= 0.0f || duration > 24.0f) {
            showToast(getString(R.string.p_enter_valid_duration));
            return;
        }

        String utcTime = tvAppointmentDate.getText().toString() + " " + tvAppointmentTime.getText().toString();
        if (mServiceRequest.getPreferredTime() == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = (Date) formatter.parse(utcTime);
                if (date.getTime() <= System.currentTimeMillis()) {
                    showToast(getString(R.string.input_valid_date_time));
                    return;
                }
            } catch (Exception e) {
            }
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);

            if (utcTime.trim().length() > 10) {
                utcTime = DateUtil.formatDateHHMMForUtc(utcTime);
            }
            ApiCall.getInstance().acceptSr(preference.getAccessToken(), mServiceRequest.getId(), utcTime, etDuration.getText().toString(), this);
        }
    }

    @Override
    public void onSuccess(String type, Response<BaseResponse> response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (type.equals("accept_sr")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast("accepted successfully");
                    setResult(Activity.RESULT_OK);
                    finish();
                } else showToast(res.getErrorMsg());
            }
        } else {
            showToast(getString(R.string.something_wrong));
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast(getString(R.string.something_wrong));
    }

    @Override
    public void onDateSelected(String date, String sendDate) {
        tvAppointmentDate.setText(date);
    }

    @Override
    public void onTimeSelected(String time) {
        tvAppointmentTime.setText(time);
    }
}
