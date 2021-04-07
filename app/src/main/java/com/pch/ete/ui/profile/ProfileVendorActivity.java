package com.pch.ete.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.PickImageDialog;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.PickImage;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.main.MainVendorActivity;
import com.pch.ete.ui.profile.adapter.ProfileFragmentPagerAdapter;
import com.pch.ete.ui.profile.fragment.AboutFragment;
import com.pch.ete.ui.profile.fragment.SettingsFragment;
import com.pch.ete.ui.profile.fragment.SkillsFragment;
import com.pch.ete.ui.profile.model.UserSkillDataRes;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class ProfileVendorActivity extends BaseActivity implements IApiCallback, PickImage {

    @BindView(R.id.profile_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.et_user_name)
    EditText etFullName;
    @BindView(R.id.et_role)
    EditText etRole;

    Handler mHandler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ApiCall.getInstance().updateFullName(preference.getAccessToken(), etFullName.getText().toString(), ProfileVendorActivity.this);
        }
    };

    private Runnable runnableRole = new Runnable() {
        @Override
        public void run() {
            if (!etRole.getText().toString().isEmpty())
                ApiCall.getInstance().updateUserRole(preference.getAccessToken(), etRole.getText().toString(), ProfileVendorActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vendor);
        bindView(this);

        mHandler = new Handler();
        etFullName.setText(preference.getLoginData().getFullName());
        etFullName.setEnabled(false);
        etRole.setText(preference.getLoginData().getRole());

        Glide.with(getApplicationContext()).load(preference.getLoginData().getImage())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(ivProfile);

        getUserSkills();
    }

    void getUserSkills() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getUserSkills(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    @OnClick({R.id.iv_profile, R.id.iv_edit_profile_image})
    void onClickSelectImage() {
        PickImageDialog.newInstance(getSupportFragmentManager(), 1);
    }

    @OnTextChanged(R.id.et_user_name)
    void updateUserName() {
        if (etFullName.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_user_name));
        } else {
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 700);
        }
    }

    @OnClick(R.id.iv_edit_user_name)
    void onClickEditName() {
        etFullName.setEnabled(true);
        etFullName.requestFocus();
        Helper.showKeyboard(this);
    }

    @OnFocusChange(R.id.et_user_name)
    void changeFocus(boolean isFocus) {
        if (!isFocus)
            etFullName.setEnabled(false);
    }

    private void setPager() {
        ProfileFragmentPagerAdapter pagerAdapter = new ProfileFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setTitle(getString(R.string.about));
        pagerAdapter.setTitle(getString(R.string.settings));
        pagerAdapter.setTitle(getString(R.string.skills));
        pagerAdapter.addFragment(AboutFragment.getInstance());
        pagerAdapter.addFragment(SettingsFragment.getInstance());
        pagerAdapter.addFragment(SkillsFragment.getInstance());
        viewPager.setAdapter(pagerAdapter);
        ((TabLayout) findViewById(R.id.tab_layout)).setupWithViewPager(viewPager);
    }

    @OnTextChanged(R.id.et_role)
    void updateUserRole() {
        if (etFullName.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_user_role));
        } else {
            mHandler.removeCallbacks(runnableRole);
            mHandler.postDelayed(runnableRole, 700);
        }
    }

    void loginClick() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
//            SkillData data = new SkillData();
//            data.setDeviceToken(preference.getDeviceToken());
//            data.setCountryCode(countryCodePicker.getSelectedCountryCodeWithPlus());
//            data.setPhone(etPhone.getText().toString());
//            data.setPassword(etPassword.getText().toString());
//            ApiCall.getInstance().login(data, this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (type.equals("update_full_name")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setFullName(etFullName.getText().toString());
                    preference.setLoginData(tmp);
                } else
                    showToast(res.getErrorMsg());
            }
        } else if (type.equals("update_profile_image")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                Log.d("sdfskdf: ", "" + response);
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setImage(res.getErrorMsg());
                    preference.setLoginData(tmp);
                } else
                    showToast(res.getErrorMsg());
            }
        } else if (type.equals("update_role")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setRole(etRole.getText().toString());
                    preference.setLoginData(tmp);
                } else
                    showToast(res.getErrorMsg());
            }
        } else if (type.equals("user_skills")) {
            if (response.isSuccessful()) {
                UserSkillDataRes res = (UserSkillDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    preference.setUserSkillList(res.getUserSkillDataList());
                    setPager();
                } else
                    showToast(res.getErrorMsg());
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PickImageDialog.TAG);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImagePicked(String path) {
        Glide.with(this).load(path).into(ivProfile);
        ivProfile.setTag(path);

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().updateProfileImage(preference.getAccessToken(), path, this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
