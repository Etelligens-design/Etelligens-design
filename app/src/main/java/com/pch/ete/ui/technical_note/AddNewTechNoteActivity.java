package com.pch.ete.ui.technical_note;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.helper.RealPathUtil;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class AddNewTechNoteActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.et_note_title)
    EditText etNoteTitle;
    @BindView(R.id.et_note_description)
    EditText etNoteText;
    @BindView(R.id.tv_file_name)
    TextView tvFileName;

    private static final int PERMISSION_REQ_ID = 22;
    private final int TEXT_REQUEST_CODE = 232;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    File mUploadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        bindView(this);
        setResult(Activity.RESULT_CANCELED);
    }

    @OnClick(R.id.btn_add_note)
    void onClickAddNote() {
        if (etNoteTitle.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.p_enter_title));
            return;
        }

        if (mUploadFile == null && etNoteText.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.p_enter_note_text));
            return;
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            if (mUploadFile == null)
                ApiCall.getInstance().addNewTechNote(preference.getAccessToken(), etNoteTitle.getText().toString(), etNoteText.getText().toString(), this);
            else
                ApiCall.getInstance().uploadMyTechText(preference.getAccessToken(), mUploadFile, etNoteTitle.getText().toString(), this);
        }
    }

    @OnClick(R.id.iv_add_file)
    void onClick() {
        if (etNoteTitle.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.p_enter_title));
            return;
        }

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            Intent intent = new Intent();
            intent.setType("text/plain");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Text File"), TEXT_REQUEST_CODE);
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                showToast("Need permissions");
                return;
            }
            Intent intent = new Intent();
            intent.setType("text/plain");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Text File"), TEXT_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                final String file = RealPathUtil.getRealPath(this, resultUri);
                mUploadFile = new File(file);
                if (mUploadFile.length() > 1024 * 1024 * 5) { // 5 MB
                    showToast(getString(R.string.big_video_file));
                    mUploadFile = null;
                    return;
                }

                if (!mUploadFile.isFile() || !mUploadFile.exists()) {
                    showToast(getString(R.string.invalid_file));
                    return;
                }
                tvFileName.setText(mUploadFile.getName());
                etNoteText.setEnabled(false);
            }
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (type.equals("added_new_note")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(res.getErrorMsg());
                    setResult(Activity.RESULT_OK);
                    finish();
                } else
                    showToast(res.getErrorMsg());
            }

            if (type.equals("upload_text")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(res.getErrorMsg());
                    setResult(Activity.RESULT_OK);
                    finish();
                } else
                    showToast(res.getErrorMsg());
            }
        } else showToast(getString(R.string.something_wrong));
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

}
