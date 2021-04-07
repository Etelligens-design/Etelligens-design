package com.pch.ete.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.main.MainEndUserActivity;
import com.pch.ete.ui.main.MainVendorActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class WelcomeActivity extends BaseActivity {

    String mRoleStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        bindView(this);

        mRoleStr = getIntent().getStringExtra("role");
        if (mRoleStr == null) {
            showToast(getString(R.string.invalid_user));
            finish();
        }
        preference.setFromNotification(false);
    }

    @OnClick(R.id.btn_get_started)
    void onClickGoToWelcome() {
        Intent intent;
        if (mRoleStr.equals("Equipment Vendor")) {
            intent = new Intent(this, MainVendorActivity.class);
        } else {
            intent = new Intent(this, MainEndUserActivity.class);
        }
        intent.putExtra("is_first", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
