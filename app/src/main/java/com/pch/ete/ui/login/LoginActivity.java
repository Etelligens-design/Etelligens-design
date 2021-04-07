package com.pch.ete.ui.login;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.Logger;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.login.model.LoginDataRes;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.ui.main.MainEndUserActivity;
import com.pch.ete.ui.main.MainVendorActivity;
import com.pch.ete.ui.signup.PasswordResetActivity;
import com.pch.ete.ui.signup.RegisterActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements IApiCallback<LoginDataRes> {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.check_keep_login)
    CheckBox checkBoxKeepLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView(this);

        if(preference.isKeepLogin()){
            etEmail.setText(preference.getEmail());
            etPassword.setText(preference.getPassword());
            checkBoxKeepLogin.setChecked(true);
        }

//        PackageManager pk = getPackageManager();
//        ComponentName componentName = new ComponentName(this, LoginActivity.class);
//        pk.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }


    @OnClick(R.id.btn_login)
    void loginClick() {
        Helper.hideSoftKeyboard(this, etEmail);
        if (!TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                showFocus(etEmail);
                showToast(getString(R.string.p_enter_email));
            } else if (Helper.checkValidEmail(etEmail.getText().toString())) {
                showFocus(etEmail);
                showToast(getString(R.string.p_enter_valid_email));
            }
        } else if (etPassword.getText().toString().isEmpty()) {
            showFocus(etPassword);
            showToast(getString(R.string.p_enter_password));
            return;
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().login(etEmail.getText().toString(), etPassword.getText().toString(), this);
        }
    }

    @OnClick(R.id.tv_create_account)
    void onClickSignUp() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.tv_reset_password)
    void onClickResetPassword() {
        Intent intent = new Intent(this, PasswordResetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onSuccess(String type, Response<LoginDataRes> response) {
        ProgressHelper.dismiss();
        if (type.equals("login")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {
                    if (checkBoxKeepLogin.isChecked()) {
                        preference.setKeepLogin(true);
                        preference.setEmail(etEmail.getText().toString());
                        preference.setPassword(etPassword.getText().toString());
                    } else {
                        preference.setKeepLogin(false);
                        preference.setEmail("");
                        preference.setPassword("");
                    }
                    preference.setAccessToken(response.body().getToken());
                    preference.setLoginData(response.body().getUserInfo());
                    if (preference.getLoginData().getUserType() == 1) {
                        Intent intent = new Intent(this, MainEndUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, MainVendorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    finish();
                } else
                    showToast(response.body().getErrorMsg());
            } else {
                showToast(getString(R.string.something_wrong));
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast(getString(R.string.something_wrong));
    }
}
