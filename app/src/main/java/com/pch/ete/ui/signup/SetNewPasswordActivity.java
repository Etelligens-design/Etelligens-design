package com.pch.ete.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class SetNewPasswordActivity extends BaseActivity implements IApiCallback<BaseResponse> {

    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;

    String mOtp, mEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        bindView(this);

        mOtp = getIntent().getStringExtra("otp");
        mEmail = getIntent().getStringExtra("email");

        if(mOtp == null || mEmail == null){
            showToast("Invalid email or OTP. Try again.");
            finish();
            return;
        }
    }

    @OnClick(R.id.btn_reset)
    void onClickResetPassword() {
        if(etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())){
            if (InternetCheck.isConnectedToInternet(this)) {
                ProgressHelper.dismiss();
                ProgressHelper.showDialog(this);
                ApiCall.getInstance().resetPassword(mOtp, mEmail, etNewPassword.getText().toString(), this);
            }
        }
    }


    @Override
    public void onSuccess(String type, Response<BaseResponse> response) {
        ProgressHelper.dismiss();
        if (type.equals("new_password")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {
                    showToast(response.body().getErrorMsg());
                    finish();
                } else
                    showToast(response.body().getErrorMsg());
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }
}
