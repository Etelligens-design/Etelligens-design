package com.pch.ete.ui.profile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pch.ete.R;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.profile.EditLocationActivity;
import com.pch.ete.ui.profile.ProfileUserActivity;
import com.pch.ete.ui.profile.ProfileVendorActivity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements IApiCallback {

    @BindView(R.id.et_full_name)
    EditText etUserName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_office_address)
    TextView etOfficeAddress;
    @BindView(R.id.et_phone)
    EditText etPhone;

    Handler mHandler;

//    private Runnable runnableFullName = new Runnable() {
//        @Override
//        public void run() {
//            ApiCall.getInstance().updateFullName(preference.getAccessToken(), etFullName.getText().toString(), SettingsFragment.this);
//        }
//    };

    private Runnable runnableName = new Runnable() {
        @Override
        public void run() {
            if (!etUserName.getText().toString().isEmpty())
                ApiCall.getInstance().updateUserName(preference.getAccessToken(), etUserName.getText().toString(), SettingsFragment.this);
        }
    };

    //    private Runnable runnableEmail = new Runnable() {
//        @Override
//        public void run() {
//            ApiCall.getInstance().updateEmail(preference.getAccessToken(), etEmail.getText().toString(), SettingsFragment.this);
//        }
//    };
    private Runnable runnablePhone = new Runnable() {
        @Override
        public void run() {
            ApiCall.getInstance().updatePhone(preference.getAccessToken(), etPhone.getText().toString(), SettingsFragment.this);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        bindView(view);
        mHandler = new Handler();
        setViewData();
        return view;
    }

    @OnClick({R.id.btn_edit_full_name, R.id.btn_edit_email, R.id.btn_edit_phone})
    void onClickItems(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_full_name:
                showFocus(etUserName);
                break;
            case R.id.btn_edit_email:
                showFocus(etEmail);
                break;
            case R.id.btn_edit_phone:
                showFocus(etPhone);
                break;
        }
        Helper.showKeyboard(getContext());
    }

    @OnClick({R.id.et_office_address, R.id.btn_edit_address})
    void onClickOfficeAddress() {
        Intent intent = new Intent(getContext(), EditLocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            etOfficeAddress.setText(data.getStringExtra("office_address"));
        }
    }

    @OnTextChanged(R.id.et_full_name)
    void updateFullName() {
        if (etUserName.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_user_name));
        } else {
            mHandler.removeCallbacks(runnableName);
            mHandler.postDelayed(runnableName, 1000);
        }
    }

//    @OnTextChanged(R.id.et_email)
//    void updateEmail() {
//        if (etEmail.getText().toString().isEmpty()) {
//            showToast(getString(R.string.p_enter_email));
//        } else {
//            mHandler.removeCallbacks(runnableEmail);
//            mHandler.postDelayed(runnableEmail, 1000);
//        }
//    }

    @OnTextChanged(R.id.et_phone)
    void updatePhone() {
        if (etPhone.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_phone));
        } else if (!isValidMobile(etPhone.getText().toString())) {
            showToast(getString(R.string.p_enter_valid_phone));
        } else {
            mHandler.removeCallbacks(runnablePhone);
            mHandler.postDelayed(runnablePhone, 1000);
        }
    }

    private void setData() {
        if (InternetCheck.isConnectedToInternet(getActivity())) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(getContext());
//            ApiCall.getInstance().getPendingJobs(preference.getLoginData().getId(), String.valueOf(page), search, this);
        }
    }

    void setViewData() {
        etUserName.setText(preference.getLoginData().getUserName());
        etEmail.setText(preference.getLoginData().getEmail());
        etOfficeAddress.setText(preference.getLoginData().getOfficeLocation());
        etPhone.setText(preference.getLoginData().getPhoneNumber());
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            BaseResponse res = (BaseResponse) response.body();
            if (type.equals("update_user_name")) {
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setUserName(etUserName.getText().toString());
                    preference.setLoginData(tmp);
                } else {
                    showToast(res.getErrorMsg());
                }
            } else if (type.equals("update_email")) {
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setEmail(etEmail.getText().toString());
                    preference.setLoginData(tmp);
                } else {
                    showToast(res.getErrorMsg());
                }
            } else if (type.equals("update_phone")) {
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setPhoneNumber(etPhone.getText().toString());
                    preference.setLoginData(tmp);
                } else {
                    showToast(res.getErrorMsg());
                }
            }
        } else {
            showToast("Something Wrong");
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast("Failed");
    }

    public static SettingsFragment instance;

    public static SettingsFragment getInstance() {
        if (instance == null) {
            instance = new SettingsFragment();
        }
        return instance;
    }

    public void updateUserNameFromFragment() {
        if (etUserName != null)
            etUserName.setText(preference.getLoginData().getUserName());
    }
}