package com.pch.ete.ui.my_equipment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.helper.RealPathUtil;
import com.pch.ete.interfaces.SelectDate;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;
import com.pch.ete.ui.my_equipment.model.EquipmentInfoRes;
import com.pch.ete.ui.profile.model.MakeData;
import com.pch.ete.ui.profile.model.MakeModelSkillRes;
import com.pch.ete.ui.profile.model.ModelData;
import com.pch.ete.ui.profile.model.SkillData;
import com.pch.ete.ui.profile.model.UserSkillDataRes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class EditEquipmentActivity extends BaseActivity implements IApiCallback, SelectDate {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_location_equipment)
    EditText etEquipmentLocation;
    @BindView(R.id.et_asset_name_equipment)
    EditText etEtEquipmentAssetName;
    @BindView(R.id.et_internal_id)
    EditText etInternalId;
    @BindView(R.id.tv_model_equipment)
    TextView tvModelEquipment;
    @BindView(R.id.tv_make_equipment)
    TextView tvMakeEquipment;
    @BindView(R.id.et_warranty_start_date)
    TextView etWarrantyStateDate;
    @BindView(R.id.et_warranty_period)
    EditText etWarrantyPeriod;
    @BindView(R.id.btn_save_equipment)
    Button btnSave;

    String[] mModelStr, mMakeStr;

    ArrayList<ModelData> mModelList;
    ArrayList<MakeData> mMakeList;

    EquipmentInfo mInfo;
    String mEqId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipment);
        bindView(this);
        setResult(Activity.RESULT_CANCELED);
        getMakeModelList();

        String dataJsonStr = getIntent().getStringExtra("eq_data");
        if (dataJsonStr != null) {
            mInfo = new Gson().fromJson(dataJsonStr, EquipmentInfo.class);
        }
        if (mInfo != null) {
            etEquipmentLocation.setText(mInfo.getLocation());
            etEtEquipmentAssetName.setText(mInfo.getAssetName());
            etInternalId.setText(mInfo.getInternalId());
            if (mInfo.getMake() != null) {
                tvMakeEquipment.setText(mInfo.getMake().getMakeName());
                tvMakeEquipment.setTag(mInfo.getMake().getId());
            }
            if (mInfo.getModel() != null) {
                tvModelEquipment.setText(mInfo.getModel().getModelName());
                tvModelEquipment.setTag(mInfo.getModel().getId());
            }
            etWarrantyStateDate.setText(mInfo.getWarrantyDate());
            etWarrantyPeriod.setText(mInfo.getWarrantyPeriod());
            btnSave.setText(getString(R.string.update));
            mEqId = mInfo.getId();
        } else {
            tvTitle.setText(getString(R.string.add_eq));
        }
    }

    void getMakeModelList() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getMakeModelSkillList(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    @OnClick(R.id.tv_make_equipment)
    void onClickMake() {
        openMakeDialog();
    }

    @OnClick(R.id.tv_model_equipment)
    void onClickModel() {
        openModelDialog();
    }

    @OnClick(R.id.et_warranty_start_date)
    void onClickWarrantyDate() {
        DateUtil.selectDate(this, this, "1950-01-01", "");
    }

    public void openMakeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Make");
        builder.setSingleChoiceItems(mMakeStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvMakeEquipment.setText(mMakeStr[which]);
                tvMakeEquipment.setTag(mMakeList.get(which).getId());
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

    public void openModelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Model");
        builder.setSingleChoiceItems(mModelStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvModelEquipment.setText(mModelStr[which]);
                tvModelEquipment.setTag(mModelList.get(which).getId());
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


    @OnClick(R.id.btn_save_equipment)
    void onClickAddSkill() {
        if (etEquipmentLocation.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_eq_location));
            return;
        }
        if (etEtEquipmentAssetName.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_eq_asset_name));
            return;
        }
        if (etInternalId.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_eq_internal_id));
            return;
        }
        if (tvMakeEquipment.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_make));
            return;
        }
        if (tvModelEquipment.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_model));
            return;
        }
        if (etWarrantyStateDate.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_warranty_start_date));
            return;
        }
        if (etWarrantyPeriod.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_warranty_period));
            return;
        }

        if (Float.parseFloat(etWarrantyPeriod.getText().toString()) <= 0.0f) {
            showToast(getString(R.string.p_select_warranty_period));
            return;
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().addEquipment(
                    preference.getAccessToken(),
                    etEquipmentLocation.getText().toString(),
                    etEtEquipmentAssetName.getText().toString(),
                    etInternalId.getText().toString(),
                    tvMakeEquipment.getTag().toString(),
                    tvModelEquipment.getTag().toString(),
                    etWarrantyStateDate.getText().toString(),
                    etWarrantyPeriod.getText().toString(),
                    mEqId,
                    this
            );
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (type.equals("make_model")) {
            if (response.isSuccessful()) {
                MakeModelSkillRes res = (MakeModelSkillRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    mMakeList = res.getMakeList();
                    mModelList = res.getModelList();
                    if (mModelList.size() == 0 || mMakeList.size() == 0) {
                        showToast("Make or Model is empty now.");
                        finish();
                    }
                    mMakeStr = new String[mMakeList.size()];
                    mModelStr = new String[mModelList.size()];
                    int i = 0;
                    for (MakeData tmp : mMakeList) {
                        mMakeStr[i] = tmp.getMakeName();
                        i++;
                    }
                    i = 0;
                    for (ModelData tmp : mModelList) {
                        mModelStr[i] = tmp.getModelName();
                        i++;
                    }
                } else
                    showToast(res.getErrorMsg());
            } else {
                showToast(getString(R.string.something_wrong));
            }
        } else if (type.equals("add_equipment")) {
            if (response.isSuccessful()) {
                EquipmentInfoRes res = (EquipmentInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(res.getErrorMsg());
//                    preference.addUserSkillData(res.getUserSkillDataList().get(0));
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    showToast(res.getErrorMsg());
                }
            } else {
                showToast(getString(R.string.something_wrong));
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

    @Override
    public void onDateSelected(String date, String sendDate) {
        etWarrantyStateDate.setText(date);
    }
}
