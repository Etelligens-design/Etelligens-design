package com.pch.ete.ui.signup;

import android.content.Intent;
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
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.login.model.LoginDataRes;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class PasswordResetActivity extends BaseActivity implements IApiCallback<BaseResponse> {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.tv_otp_title)
    TextView tvOtpTitle;
    @BindView(R.id.et_otp)
    EditText etOtp;

    String mOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        bindView(this);
    }

    @OnClick(R.id.btn_reset)
    void loginClick() {
        if (etOtp.getVisibility() == View.VISIBLE) {
            if (etOtp.getText().toString().equals(mOTP)) {
                Intent intent = new Intent(this, SetNewPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("otp", mOTP);
                intent.putExtra("email", etEmail.getText().toString());
                startActivity(intent);
                finish();
            } else {
                showToast(getString(R.string.p_enter_correct_otp));
                return;
            }
        } else {
            Helper.hideSoftKeyboard(this, etEmail);
            if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {
                showFocus(etUserName);
                showToast(getString(R.string.p_enter_user_name));
                return;
            }

            if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                showToast(getString(R.string.p_enter_email));
                return;
            }

            if (Helper.checkValidEmail(etEmail.getText().toString())) {
                showFocus(etEmail);
                showToast(getString(R.string.p_enter_valid_email));
                return;
            }

            if (InternetCheck.isConnectedToInternet(this)) {
                ProgressHelper.dismiss();
                ProgressHelper.showDialog(this);
                ApiCall.getInstance().checkAccount(etUserName.getText().toString(), etEmail.getText().toString(), this);
            }
        }
    }


    @Override
    public void onSuccess(String type, Response<BaseResponse> response) {
        ProgressHelper.dismiss();
        if (type.equals("check_account")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {
                    mOTP = response.body().getErrorMsg();
                    tvOtpTitle.setVisibility(View.VISIBLE);
                    etOtp.setVisibility(View.VISIBLE);
                    etUserName.setEnabled(false);
                    etEmail.setEnabled(false);
                    etOtp.requestFocus();
                    showToast("OTP sent your email address");
                } else
                    showToast(response.body().getErrorMsg());
            }else{
                showToast(getString(R.string.something_wrong));
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
        showToast(getString(R.string.something_wrong));
    }
}
