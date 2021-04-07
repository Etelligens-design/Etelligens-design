package com.pch.ete.ui.profile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nex3z.flowlayout.FlowLayout;
import com.pch.ete.R;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.profile.AddSkillActivity;
import com.pch.ete.ui.profile.model.UserSkillData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment implements IApiCallback {

    @BindView(R.id.fl_skills)
    FlowLayout mSkillFlowLayout;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.btn_add_skill)
    Button btnAddSkill;

    Handler mHandler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ApiCall.getInstance().updateUserDescription(preference.getAccessToken(), etDescription.getText().toString(), AboutFragment.this);
        }
    };

    ArrayList<UserSkillData> mSkillArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_about, container, false);
        bindView(view);
        mHandler = new Handler();
        setViewData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preference.getUserSkillList().size() > mSkillFlowLayout.getChildCount()) {
            initSkillListView();
        }
    }

    @OnClick(R.id.btn_add_skill)
    void onClickAddSkill() {
        Intent intent = new Intent(getContext(), AddSkillActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);
    }

    @OnTextChanged(R.id.et_description)
    void updateDescription() {
        if (etDescription.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_description));
        } else {
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 1000);
        }
    }

    void setViewData() {
        etDescription.setText(preference.getLoginData().getAbout());
        initSkillListView();
    }

    void initSkillListView() {
        mSkillArrayList = preference.getUserSkillList();
        mSkillFlowLayout.removeAllViews();

        for (UserSkillData userSkill : mSkillArrayList) {

            if (userSkill.getSkillData() == null)
                continue;

            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10.0f,
                    getResources().getDisplayMetrics()
            );

            TextView tvSkillName = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) px / 2, (int) px / 2, (int) px / 2, (int) px / 2);
            tvSkillName.setLayoutParams(layoutParams);

            String modelName = "", makeName = "";
            if (userSkill.getMakeData() == null) {
                makeName = "";
            } else {
                makeName = userSkill.getMakeData().getMakeName();
            }
            if (userSkill.getModelData() == null) {
                modelName = "";
            } else {
                modelName = userSkill.getModelData().getModelName();
            }

            tvSkillName.setText(makeName + "/" + modelName);
            tvSkillName.setHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    36.0f,
                    getResources().getDisplayMetrics()
            ));
            tvSkillName.setTextSize(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8.0f,
                    getResources().getDisplayMetrics()
            ));
            tvSkillName.setTextColor(getContext().getResources().getColor(R.color.text_color));
            tvSkillName.setBackgroundResource(R.drawable.round_light_grey_rect);
            tvSkillName.setGravity(Gravity.CENTER);


            tvSkillName.setPadding((int) px * 2, 0, (int) px * 2, 0);
            mSkillFlowLayout.addView(tvSkillName);
        }
        mSkillFlowLayout.addView(btnAddSkill);
        mSkillFlowLayout.invalidate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            initSkillListView();
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (type.equals("update_description")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    LoginData tmp = preference.getLoginData();
                    tmp.setAbout(etDescription.getText().toString());
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

    public static AboutFragment instance;

    public static AboutFragment getInstance() {
        if (instance == null) {
            instance = new AboutFragment();
        }
        return instance;
    }
}