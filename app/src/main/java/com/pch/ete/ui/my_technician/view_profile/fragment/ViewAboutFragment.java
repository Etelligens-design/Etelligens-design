package com.pch.ete.ui.my_technician.view_profile.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nex3z.flowlayout.FlowLayout;
import com.pch.ete.R;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.ui.my_technician.view_profile.ViewUserProfileActivity;
import com.pch.ete.ui.profile.model.UserSkillData;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAboutFragment extends BaseFragment {

    @BindView(R.id.fl_skills)
    FlowLayout mSkillFlowLayout;
    @BindView(R.id.tv_description)
    TextView tvDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile_about, container, false);
        bindView(view);
        setViewData();
        return view;
    }

    void setViewData() {
        tvDescription.setText(ViewUserProfileActivity.TechnicianInfoData.getAbout());
        initSkillListView();
    }

    void initSkillListView() {
        mSkillFlowLayout.removeAllViews();

        for (UserSkillData userSkill : ViewUserProfileActivity.USER_SKILL_LIST) {
            if (userSkill.getSkillData() == null) {
                continue;
            }

            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10.0f,
                    getResources().getDisplayMetrics()
            );

            TextView tvSkillName = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) px / 2, (int) px / 2, (int) px / 2, (int) px / 2);
            tvSkillName.setLayoutParams(layoutParams);
            tvSkillName.setText(userSkill.getSkillData().getSkillName());
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
            mSkillFlowLayout.invalidate();
        }
    }

    public static ViewAboutFragment instance;

    public static ViewAboutFragment getInstance() {
        if (instance == null) {
            instance = new ViewAboutFragment();
        }
        return instance;
    }
}