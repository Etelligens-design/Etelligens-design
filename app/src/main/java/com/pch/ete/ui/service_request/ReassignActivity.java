package com.pch.ete.ui.service_request;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.login.model.LoginDataRes;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class ReassignActivity extends BaseActivity implements IApiCallback<LoginDataRes> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reassign_service);
        bindView(this);
    }


    @Override
    public void onSuccess(String type, Response<LoginDataRes> response) {
        ProgressHelper.dismiss();
        if (type.equals("login")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {

                    LoginData loginData = AppSharedPreference.getInstance(this).getLoginData();
                    String value = "Client";
                    if (loginData != null) {
                        value = loginData.getUserName();
                    }
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
