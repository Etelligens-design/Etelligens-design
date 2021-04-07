package com.pch.ete.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.helper.RealPathUtil;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.profile.model.MakeData;
import com.pch.ete.ui.profile.model.MakeModelSkillRes;
import com.pch.ete.ui.profile.model.ModelData;
import com.pch.ete.ui.profile.model.SkillData;
import com.pch.ete.ui.profile.model.UserSkillDataRes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class AddSkillActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_make_equipment)
    TextView tvMakeEquipment;
    @BindView(R.id.tv_model_number)
    TextView tvModel;
    @BindView(R.id.tv_skill)
    TextView tvSkill;
    @BindView(R.id.radios_certified)
    RadioGroup radioGroupCertified;
    @BindView(R.id.radios_authorized)
    RadioGroup radioGroupAuth;

    String[] mModelStr, mMakeStr, mSkillStr;

    ArrayList<ModelData> mModelList;
    ArrayList<MakeData> mMakeList;
    ArrayList<SkillData> mSkillList;

    String mCertificatedDocFile, mAuthorizedDocFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_skill);
        bindView(this);
        tvTitle.setText(getString(R.string.add_skill));

        setResult(Activity.RESULT_CANCELED);
        getMakeModelList();

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

    @OnClick(R.id.tv_model_number)
    void onClickModel() {
        openModelDialog();
    }

    @OnClick(R.id.tv_skill)
    void onClickSkill() {
        openSkillDialog();
    }

    public void openMakeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Make");
        builder.setSingleChoiceItems(mMakeStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvMakeEquipment.setText(mMakeStr[which]);
                tvMakeEquipment.setTag(String.valueOf(which));
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
                tvModel.setText(mModelStr[which]);
                tvModel.setTag(String.valueOf(which));
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


    public void openSkillDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Skill");
        builder.setSingleChoiceItems(mSkillStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvSkill.setText(mSkillStr[which]);
                tvSkill.setTag(String.valueOf(which));
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

    @OnClick(R.id.btn_upload_certified_doc)
    void onClickAddNewCertification() {
        if (radioGroupCertified.getCheckedRadioButtonId() == R.id.radio_certified_yes) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("application/pdf");
            chooseFile = Intent.createChooser(chooseFile, "Choose a pdf file");
            startActivityForResult(chooseFile, 3);
        } else {
            showToast(getString(R.string.please_check_yes));
        }
    }

    @OnClick(R.id.btn_upload_authorized_doc)
    void onClickAddNewAuthorized() {
        if (radioGroupAuth.getCheckedRadioButtonId() == R.id.radio_authorized_yes) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("application/pdf");
            chooseFile = Intent.createChooser(chooseFile, "Choose a pdf file");
            startActivityForResult(chooseFile, 4);
        } else {
            showToast(getString(R.string.please_check_yes));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) { // select certification pdf
            if (resultCode == BaseActivity.RESULT_OK) {
                Uri uri = data.getData();
                mCertificatedDocFile = RealPathUtil.getRealPath(this, uri);
                showToast(mCertificatedDocFile);
            }
        }

        if (requestCode == 4) { // select certification pdf
            if (resultCode == BaseActivity.RESULT_OK) {
                Uri uri = data.getData();
                mAuthorizedDocFile = RealPathUtil.getRealPath(this, uri);
                showToast(mAuthorizedDocFile);
            }
        }
    }

    @OnClick(R.id.btn_save_skill)
    void onClickAddSkill() {
        if (tvMakeEquipment.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_make));
            return;
        }
        if (tvModel.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_model));
            return;
        }
        if (tvSkill.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_select_skill));
            return;
        }

        if (radioGroupCertified.getCheckedRadioButtonId() == R.id.radio_certified_yes && mCertificatedDocFile == null) {
            showToast(getString(R.string.p_pick_certified_doc));
            return;
        }

        if (radioGroupAuth.getCheckedRadioButtonId() == R.id.radio_authorized_yes && mAuthorizedDocFile == null) {
            showToast(getString(R.string.p_pick_author_doc));
            return;
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().addSkill(
                    preference.getAccessToken(),
                    mMakeList.get(Integer.parseInt(tvMakeEquipment.getTag().toString())).getId(),
                    mModelList.get(Integer.parseInt(tvModel.getTag().toString())).getId(),
                    mSkillList.get(Integer.parseInt(tvSkill.getTag().toString())).getId(),
                    mCertificatedDocFile,
                    mAuthorizedDocFile,
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
                    mSkillList = res.getSkillList();
                    if (mModelList.size() == 0 || mMakeList.size() == 0 || mSkillList.size() == 0) {
                        showToast("Make or Model is empty now.");
                        finish();
                    }
                    mMakeStr = new String[mMakeList.size()];
                    mModelStr = new String[mModelList.size()];
                    mSkillStr = new String[mSkillList.size()];
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
                    i = 0;
                    for (SkillData tmp : mSkillList) {
                        mSkillStr[i] = tmp.getSkillName();
                        i++;
                    }
                } else
                    showToast(res.getErrorMsg());
            } else {
                showToast(getString(R.string.something_wrong));
            }
        }else if(type.equals("add_skill")){
            if(response.isSuccessful()){
                UserSkillDataRes res = (UserSkillDataRes) response.body();
                if(res.getErrorCode().equals("0")){
                    showToast(getString(R.string.p_wait_admin_approve));
//                    preference.addUserSkillData(res.getUserSkillDataList().get(0));
                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    showToast(res.getErrorMsg());
                }
            }else{
                showToast(getString(R.string.something_wrong));
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }
}
