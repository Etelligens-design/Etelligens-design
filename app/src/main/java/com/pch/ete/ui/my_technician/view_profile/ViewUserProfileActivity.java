package com.pch.ete.ui.my_technician.view_profile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.my_technician.model.TechnicianInfo;
import com.pch.ete.ui.my_technician.view_profile.fragment.ViewAboutFragment;
import com.pch.ete.ui.my_technician.view_profile.fragment.ViewSettingsFragment;
import com.pch.ete.ui.my_technician.view_profile.fragment.ViewSkillsFragment;
import com.pch.ete.ui.profile.adapter.ProfileFragmentPagerAdapter;
import com.pch.ete.ui.profile.model.UserSkillData;
import com.pch.ete.ui.profile.model.UserSkillDataRes;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Response;

public class ViewUserProfileActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.profile_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.et_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_main_skill)
    TextView tvRole;

    public static TechnicianInfo TechnicianInfoData;
    public static ArrayList<UserSkillData> USER_SKILL_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);
        bindView(this);

        String jsonStr = getIntent().getStringExtra("user_data");
        if (jsonStr == null) {
            showToast(getString(R.string.invalid_user));
            finish();
            return;
        }

        TechnicianInfoData = new Gson().fromJson(jsonStr, TechnicianInfo.class);
        tvUserName.setText(TechnicianInfoData.getUserName());
        tvRole.setText(TechnicianInfoData.getRole());

        Glide.with(getApplicationContext()).load(TechnicianInfoData.getImage())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(ivProfile);

        getUserSkills();
    }

    void getUserSkills() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getOtherUserSkills(preference.getAccessToken(), TechnicianInfoData.getId(), this);
        }
    }

    private void setPager() {
        ProfileFragmentPagerAdapter pagerAdapter = new ProfileFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setTitle(getString(R.string.about));
        pagerAdapter.setTitle(getString(R.string.settings));
        pagerAdapter.setTitle(getString(R.string.skills));
        pagerAdapter.addFragment(ViewAboutFragment.getInstance());
        pagerAdapter.addFragment(ViewSettingsFragment.getInstance());
        pagerAdapter.addFragment(ViewSkillsFragment.getInstance());
        viewPager.setAdapter(pagerAdapter);
        ((TabLayout) findViewById(R.id.tab_layout)).setupWithViewPager(viewPager);
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (type.equals("user_skills")) {
            if (response.isSuccessful()) {
                UserSkillDataRes res = (UserSkillDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    USER_SKILL_LIST = res.getUserSkillDataList();
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
}
