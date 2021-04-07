package com.pch.ete.ui.service_request;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.SelectDate;
import com.pch.ete.interfaces.SelectTime;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;
import com.pch.ete.ui.my_equipment.model.EquipmentInfoRes;
import com.pch.ete.ui.service_request.adapter.SimpleDictionaryAdapter;
import com.pch.ete.ui.service_request.model.ServiceTypeData;
import com.pch.ete.ui.service_request.model.ServiceTypeDataRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import pl.sly.dynamicautocomplete.DynamicAutoCompleteTextView;
import pl.sly.dynamicautocomplete.OnDynamicAutocompleteListener;
import retrofit2.Response;

public class SendServiceRequestActivity extends BaseActivity implements IApiCallback, SelectDate, SelectTime, OnDynamicAutocompleteListener {

    @BindView(R.id.et_service_type)
    TextView etServiceType;
    @BindView(R.id.et_asset_name)
    TextView etAssetName;
    @BindView(R.id.et_model_type)
    TextView etModelType;
    @BindView(R.id.radio_emergency_yes)
    RadioButton radioEmergencyYes;
    @BindView(R.id.radio_emergency_no)
    RadioButton radioEmergencyNo;
    @BindView(R.id.tv_preferred_date)
    TextView tvPreferredDate;
    @BindView(R.id.tv_preferred_time)
    TextView tvPreferredTime;
    @BindView(R.id.radio_technician_yes)
    RadioButton radioTechnicianYes;
    @BindView(R.id.radio_technician_no)
    RadioButton radioTechnicianNo;
    @BindView(R.id.et_technician_name)
    EditText etTechnicianName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.auto_technician_name)
    DynamicAutoCompleteTextView autoCompleteTextView;

    private static final int THRESHOLD = 1;
    ArrayList<ServiceTypeData> mServiceTypeList;
    ArrayList<EquipmentInfo> mEquipmentList;
    String[] mAssets, mServiceTypes, vendorNames;
    private SimpleDictionaryAdapter mSimpleDictionaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        bindView(this);

        getServiceTypeNVendorNames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoCompleteTextView.dismissDropDown();
    }

    void getServiceTypeNVendorNames() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getServiceTypeNVendorNames(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    private void getMyEquipmentList() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getMyEquipmentAll(preference.getAccessToken(), "test", this);
        }
    }

    @OnClick(R.id.et_service_type)
    void onClickServiceType() {
        openServiceTypeDialog();
    }

    public void openServiceTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Service Type");
        builder.setSingleChoiceItems(mServiceTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etServiceType.setText(mServiceTypes[which]);
                etServiceType.setTag(mServiceTypeList.get(which).getId());
                dialog.dismiss();
            }
        });

        //Set Neutral Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Creating AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @OnClick(R.id.et_asset_name)
    void onClickAssetName() {
        openAssetNameDialog();
    }

    public void openAssetNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Asset");
        builder.setSingleChoiceItems(mAssets, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mEquipmentList.get(which).getMake() == null || mEquipmentList.get(which).getModel() == null){
                    showToast(getString(R.string.invalid_equipment));
                    return;
                }
                etAssetName.setText(mAssets[which]);
                etAssetName.setTag(mEquipmentList.get(which).getId());
                etModelType.setText(mEquipmentList.get(which).getMake().getMakeName() + " " + mEquipmentList.get(which).getModel().getModelName());
                dialog.dismiss();
            }
        });

        //Set Neutral Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Creating AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @OnClick({R.id.radio_emergency_no, R.id.radio_emergency_yes})
    void onClickRadioEmergency(RadioButton radio) {
        if (radio.getId() == R.id.radio_emergency_yes) {
            tvPreferredDate.setAlpha(1.0f);
            tvPreferredDate.setEnabled(true);
            tvPreferredTime.setAlpha(1.0f);
            tvPreferredTime.setEnabled(true);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tvPreferredDate.setText(currentDate);
            String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
            tvPreferredTime.setText(currentTime);
        } else {
            tvPreferredDate.setAlpha(0.5f);
            tvPreferredDate.setEnabled(false);
            tvPreferredTime.setAlpha(0.5f);
            tvPreferredTime.setEnabled(false);
            tvPreferredDate.setText("");
            tvPreferredTime.setText("");
        }
    }

    @OnClick({R.id.radio_technician_yes, R.id.radio_technician_no})
    void onClickRadioTechnician(RadioButton radio) {
        if (radio.getId() == R.id.radio_technician_yes) {
            etTechnicianName.setAlpha(1.0f);
            etTechnicianName.setEnabled(true);
            autoCompleteTextView.setAlpha(1.0f);
            autoCompleteTextView.setEnabled(true);
        } else {
            etTechnicianName.setAlpha(0.5f);
            etTechnicianName.setEnabled(false);
            autoCompleteTextView.setAlpha(0.5f);
            autoCompleteTextView.setEnabled(false);
        }
    }

    @OnClick(R.id.tv_preferred_date)
    void onClickPreferredDate() {
        DateUtil.selectDate(this, this, "", "");
    }

    @OnClick(R.id.tv_preferred_time)
    void onClickPreferredTime() {
        DateUtil.selectTime(this, this, "");
    }


    @OnClick(R.id.btn_request_service)
    void onClickRequestService() {
        if (etServiceType.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_service_type));
            return;
        }
        if (etAssetName.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_asset_type));
            return;
        }
        if (etModelType.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_make));
            return;
        }
        if (radioEmergencyYes.isChecked()) {
            if (tvPreferredDate.getText().toString().isEmpty()) {
                showToast(getString(R.string.p_select_preferred_date));
                return;
            }
            if (tvPreferredTime.getText().toString().isEmpty()) {
                showToast(getString(R.string.p_select_preferred_time));
                return;
            }
        }

        String preferredVendorName = "";
        if (radioTechnicianYes.isChecked()) {
            preferredVendorName = autoCompleteTextView.getText().toString();
            if (preferredVendorName.isEmpty()) {
                showToast(getString(R.string.p_enter_technician_name));
                return;
            }

            boolean isCorrectVendor = false;
            for (String vendorName : vendorNames) {
                if (vendorName.equals(preferredVendorName)) {
                    isCorrectVendor = true;
                    break;
                }
            }

            if (!isCorrectVendor) {
                showToast(getString(R.string.invalid_technician_name));
                return;
            }
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            String utcTime = tvPreferredDate.getText().toString() + " " + tvPreferredTime.getText().toString();
            if (utcTime.trim().length() > 10) {
                utcTime = DateUtil.formatDateHHMMForUtc(utcTime);
            }
            ApiCall.getInstance().sendServiceRequest(preference.getAccessToken(),
                    etServiceType.getTag().toString(), etAssetName.getTag().toString(), utcTime, preferredVendorName, etDescription.getText().toString(), this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        autoCompleteTextView.dismissDropDown();
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (type.equals("service_type_vendor_names")) {
                ServiceTypeDataRes res = (ServiceTypeDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    mServiceTypeList = res.getServiceTypeData();
                    mServiceTypes = new String[mServiceTypeList.size()];
                    int i = 0;
                    for (ServiceTypeData tmp : mServiceTypeList) {
                        mServiceTypes[i] = tmp.getServiceName();
                        i++;
                    }
                    vendorNames = res.getErrorMsg().split(",");
                    mSimpleDictionaryAdapter = new SimpleDictionaryAdapter(
                            this, android.R.layout.simple_list_item_1, vendorNames);
                    autoCompleteTextView.setOnDynamicAutocompleteListener(this);
                    autoCompleteTextView.setAdapter(mSimpleDictionaryAdapter);
                    autoCompleteTextView.setThreshold(THRESHOLD);
                    getMyEquipmentList();
                } else
                    showToast(res.getErrorMsg());
            } else if (type.equals("equipment_list")) {
                EquipmentInfoRes res = (EquipmentInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    mEquipmentList = res.getData();
                    mAssets = new String[mEquipmentList.size()];
                    int i = 0;
                    for (EquipmentInfo tmp : mEquipmentList) {
                        mAssets[i] = tmp.getAssetName();
                        i++;
                    }
                } else showToast(res.getErrorMsg());
            } else if (type.equals("send_request")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast("Your service requested successfully");
                    finish();
                } else showToast(res.getErrorMsg());
            }
        } else showToast(getString(R.string.something_wrong));
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

    @Override
    public void onDateSelected(String date, String sendDate) {
        tvPreferredDate.setText(date);
    }

    @Override
    public void onTimeSelected(String time) {
        tvPreferredTime.setText(time);
    }

    @Override
    public void onDynamicAutocompleteStart(AutoCompleteTextView view) {

    }

    @Override
    public void onDynamicAutocompleteStop(AutoCompleteTextView view) {

    }
}
