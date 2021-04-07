package com.pch.ete.ui.my_technician.view_profile.fragment;

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
import com.pch.ete.ui.my_technician.view_profile.ViewUserProfileActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSettingsFragment extends BaseFragment {

    @BindView(R.id.et_full_name)
    TextView etFullName;
    @BindView(R.id.et_email)
    TextView etEmail;
    @BindView(R.id.et_office_address)
    TextView etOfficeAddress;
    @BindView(R.id.et_phone)
    TextView etPhone;

    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_settings, container, false);
        bindView(view);
        mHandler = new Handler();
        setViewData();
        return view;
    }

    void setViewData() {
        etFullName.setText(ViewUserProfileActivity.TechnicianInfoData.getFullName());
        etEmail.setText(ViewUserProfileActivity.TechnicianInfoData.getEmail());
        etOfficeAddress.setText(ViewUserProfileActivity.TechnicianInfoData.getOfficeLocation());
        etPhone.setText(ViewUserProfileActivity.TechnicianInfoData.getPhoneNumber());
    }

    public static ViewSettingsFragment instance;

    public static ViewSettingsFragment getInstance() {
        if (instance == null) {
            instance = new ViewSettingsFragment();
        }
        return instance;
    }
}