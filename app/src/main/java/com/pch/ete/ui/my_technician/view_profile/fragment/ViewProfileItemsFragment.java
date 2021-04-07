package com.pch.ete.ui.my_technician.view_profile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pch.ete.R;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.ui.profile.adapter.ProfileFragmentPagerAdapter;

import butterknife.BindView;

public class ViewProfileItemsFragment extends BaseFragment {

    @BindView(R.id.job_status_view_pager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_items_pager, container, false);
        bindView(view);
        setPager(view);
        return view;
    }

    private void setPager(View view) {
        ProfileFragmentPagerAdapter pagerAdapter = new ProfileFragmentPagerAdapter(getFragmentManager());
        pagerAdapter.setTitle(getString(R.string.about));
        pagerAdapter.setTitle(getString(R.string.settings));
        pagerAdapter.setTitle(getString(R.string.skills));
        pagerAdapter.addFragment(ViewAboutFragment.getInstance());
        pagerAdapter.addFragment(ViewSettingsFragment.getInstance());
        pagerAdapter.addFragment(ViewSkillsFragment.getInstance());
        viewPager.setAdapter(pagerAdapter);
        ((TabLayout) view.findViewById(R.id.tab_layout)).setupWithViewPager(viewPager);
    }

    private static ViewProfileItemsFragment instance;

    public static ViewProfileItemsFragment getInstance() {
        if (instance == null)
            instance = new ViewProfileItemsFragment();

        return instance;
    }

}
